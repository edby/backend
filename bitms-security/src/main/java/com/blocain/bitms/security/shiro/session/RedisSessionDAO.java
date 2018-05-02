package com.blocain.bitms.security.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * <p>File：RedisSessionDAO.java </p>
 * <p>Title:  自定义授权会话实现类 </p>
 * <p>Description: RedisSessionDAO </p>
 * <p>Copyright: Copyright (c) 2014 08/08/2015 09:52</p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public class RedisSessionDAO extends AbstractSessionDAO implements CustomSessionDAO
{
    private static final Logger logger         = LoggerFactory.getLogger(RedisSessionDAO.class);
    
    public static final String  SESSION_GROUPS = CacheConst.REDIS_SHIRO_SESSION_PREFIX;
    
    public static final String  SESSION_PREFIX = "session";
    
    @Override
    public void update(Session session) throws UnknownSessionException
    {
        if (session == null || session.getId() == null) { return; }
        try
        {
            String key = new StringBuffer(SESSION_PREFIX).append(BitmsConst.SEPARATOR).append(session.getId()).toString();
            // 设置超期时间
            int timeoutSeconds = (int) (session.getTimeout() / 1000);
            RedisUtils.putObject(key, session, timeoutSeconds);
            // 获取登录者编号
            UserPrincipal principal = null;
            SimplePrincipalCollection collection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (null != collection)
            {
                principal = (UserPrincipal) collection.getPrimaryPrincipal();
            }
            if (null != principal)
            {
                Long principalId = principal != null ? principal.getId() : null;
                Map<Object, Object> map = Maps.newHashMap();
                StringBuffer buffer = new StringBuffer(30).append(principalId);
                buffer.append(BitmsConst.SEPARATOR).append(session.getTimeout());
                buffer.append(BitmsConst.SEPARATOR).append(session.getLastAccessTime().getTime());
                map.put(key, buffer.toString());
                RedisUtils.putMap(SESSION_GROUPS, map);
            }
        }
        catch (Exception e)
        {
            logger.error("update {} {}", session.getId(), e);
        }
    }
    
    /**
     * 清空会话及缓存
     */
    public static void clean()
    {
        try
        {
            Map<String, Object> map = RedisUtils.getMap(SESSION_GROUPS);
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                RedisUtils.delMap(SESSION_GROUPS, entry.getKey());
                RedisUtils.del(entry.getKey());
            }
        }
        catch (Exception e)
        {
            logger.error("getActiveSessions", e);
        }
    }
    
    @Override
    public void delete(Session session)
    {
        if (session == null || session.getId() == null) { return; }
        try
        {
            String key = new StringBuffer(SESSION_PREFIX).append(BitmsConst.SEPARATOR).append(session.getId()).toString();
            RedisUtils.delMap(SESSION_GROUPS, key);
            RedisUtils.del(key);
            logger.debug("delete {} ", session.getId());
        }
        catch (Exception e)
        {
            logger.error("delete {} ", session.getId(), e);
        }
    }
    
    @Override
    public Collection<Session> getActiveSessions()
    {
        return getActiveSessions(true);
    }
    
    /**
     * 获取活动会话
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave)
    {
        return getActiveSessions(includeLeave, null, null);
    }
    
    /**
     * 获取活动会话
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @param principal 根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话。
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession)
    {
        Set<Session> sessions = Sets.newHashSet();
        try
        {
            Map<String, Object> map = RedisUtils.getMap(SESSION_GROUPS);
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                if (StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(String.valueOf(entry.getValue())))
                {
                    String[] ss = StringUtils.split(String.valueOf(entry.getValue()), BitmsConst.SEPARATOR);
                    if (ss != null && ss.length == 3)
                    {
                        SimpleSession session = new SimpleSession();
                        String sessionId = entry.getKey();
                        session.setId(sessionId.substring(sessionId.indexOf(BitmsConst.SEPARATOR) + 1, sessionId.length()));
                        session.setAttribute("principalId", ss[0]);
                        session.setTimeout(Long.valueOf(ss[1]));
                        session.setLastAccessTime(new Date(Long.valueOf(ss[2])));
                        try
                        {
                            // 验证SESSION
                            session.validate();
                            boolean isActiveSession = false;
                            // 不包括离线并符合最后访问时间小于等于3分钟条件。
                            if (includeLeave || DateUtils.pastMinutes(session.getLastAccessTime()) <= 3)
                            {
                                isActiveSession = true;
                            }
                            // 过滤掉的SESSION
                            if (filterSession != null && filterSession.getId().equals(session.getId()))
                            {
                                isActiveSession = false;
                            }
                            if (isActiveSession)
                            {
                                sessions.add(session);
                            }
                        }
                        // SESSION验证失败
                        catch (Exception e2)
                        {
                            RedisUtils.delMap(SESSION_GROUPS, entry.getKey());
                            RedisUtils.del(entry.getKey());
                        }
                    }
                    // 存储的SESSION不符合规则
                    else
                    {
                        RedisUtils.delMap(SESSION_GROUPS, entry.getKey());
                        RedisUtils.del(entry.getKey());
                    }
                }
                // 存储的SESSION无Value
                else if (StringUtils.isNotBlank(entry.getKey()))
                {
                    RedisUtils.delMap(SESSION_GROUPS, entry.getKey());
                    RedisUtils.del(entry.getKey());
                }
            }
            logger.info("getActiveSessions size: {} ", sessions.size());
        }
        catch (Exception e)
        {
            logger.error("getActiveSessions", e);
        }
        return sessions;
    }
    
    @Override
    protected Serializable doCreate(Session session)
    {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        return sessionId;
    }
    
    @Override
    protected Session doReadSession(Serializable sessionId)
    {
        Session session = null;
        try
        {
            String key = new StringBuffer(SESSION_PREFIX).append(BitmsConst.SEPARATOR).append(sessionId).toString();
            session = (Session) RedisUtils.getObject(key);
        }
        catch (Exception e)
        {
            logger.error("doReadSession {} {}", sessionId, e);
        }
        return session;
    }
}
