package com.blocain.bitms.security.shiro.session;

import java.util.Collection;

import org.apache.shiro.session.Session;

/**
 * <p>File：SessionDAO.java </p>
 * <p>Title:  自定义授权会话 </p>
 * <p>Description: SessionDAO </p>
 * <p>Copyright: Copyright (c) 2014 08/08/2015 09:52</p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public interface CustomSessionDAO extends org.apache.shiro.session.mgt.eis.SessionDAO
{
    /**
     * 获取活动会话
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @return
     */
    Collection<Session> getActiveSessions(boolean includeLeave);
    
    /**
     * 获取活动会话
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @param principal 根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话。
     * @return
     */
    Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession);
}
