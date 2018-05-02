package com.blocain.bitms.security.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountLogConsts;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;

/**
 * <p>File：CSRFHandlerInterceptor</p>
 * <p>Title: </p>
 * <p>Description: 用于配合 spring <form:form> 标签防止 CSRF 攻击 </form:form></p>
 * <p>Copyright: Copyright (c) 2014 2014/3/22 10:52</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CSRFHandlerInterceptor extends HandlerInterceptorAdapter
{
    private CSRFTokenManager        csrfTokenManager;
    
    private RedisTemplate           redisTemplate;
    
    private AccountLogNoSql         accountLogNoSql;
    
    public static final String      BOSS_PROJECT_NAME   = "BOSS";
    
    public static final String      BOSS_RUNNER_STATUS  = "production";
    
    protected static final String[] blackUrlPathPattern = new String[]{"*220.189.223.218*", "*221.12.40.19*"};
    
    public static final Boolean     ACCOUNT_LOGGER      = BitmsConst.ACCOUNT_LOGGER_STATUS.equals("enable");
    
    public static final Logger      logger              = LoggerFactory.getLogger(CSRFHandlerInterceptor.class);
    
    public void setCsrfTokenManager(CSRFTokenManager csrfTokenManager)
    {
        this.csrfTokenManager = csrfTokenManager;
    }
    
    public void setAccountLogNoSql(AccountLogNoSql accountLogNoSql)
    {
        this.accountLogNoSql = accountLogNoSql;
    }
    
    public void setRedisTemplate(RedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        // if (BOSS_PROJECT_NAME.equals(BitmsConst.BITMS_PROJECT_NAME) && BOSS_RUNNER_STATUS.equals(BitmsConst.RUNNING_ENVIRONMONT) && !isAccessAllowed(request))
        if (BitmsConst.BITMS_PROJECT_NAME.contains(BOSS_PROJECT_NAME) && BOSS_RUNNER_STATUS.equals(BitmsConst.RUNNING_ENVIRONMONT) && !isAccessAllowed(request))
        {// 审核支撑系统访问权限
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "the requested resource is not available.");
            return false;
        }
        if (!request.getMethod().equalsIgnoreCase(BitmsConst.POST))
        {// 非POST请求直接放行
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        CSRFToken csrfToken = method.getAnnotation(CSRFToken.class);
        // 如果配置了校验csrf token校验，则校验
        if (null != csrfToken && csrfToken.check())
        {
            String requestToken = CSRFTokenManager.getTokenFromRequest(request);
            if (StringUtils.isBlank(requestToken) || requestToken.contains("csrf"))
            {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF value");
                return false;
            }
            RedisLock lock = new RedisLock(redisTemplate, requestToken);
            if (lock.lock())
            {
                boolean flag = true;
                try
                {
                    String decrypt = EncryptUtils.desDecrypt(requestToken);
                    String formId = decrypt.substring(0, decrypt.indexOf(BitmsConst.SEPARATOR));
                    String sessionToken = csrfTokenManager.getTokenForRequest(request, formId);
                    if (StringUtils.equals(sessionToken, requestToken))
                    {// 验证成功之后清除TOKEN
                        csrfTokenManager.removeTokenForRequest(request, formId);
                        response.addHeader(CSRFTokenManager.CSRF_TOKEN_NAME, csrfTokenManager.getTokenForRequest(request, formId));
                    }
                    else
                    {
                        flag = false;
                        response.addHeader(CSRFTokenManager.CSRF_TOKEN_NAME, sessionToken);
                    }
                }
                catch (RuntimeException e)
                {
                    flag = false;
                    LoggerUtils.logError(logger, "锁获取失败：{} requestToken:{}", e.getMessage(), requestToken);
                }
                finally
                {
                    lock.unlock();
                }
                if (!flag)
                {// 加入非法请求限制
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF value");
                }
                return flag;
            }
            else
            {// 未取到锁时直接拒绝请求
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF value");
                return false;
            }
        }
        else
        {// 不需要验证TOKEN的请求直接放行
            return true;
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        super.afterCompletion(request, response, handler, ex);
        if (request.getMethod().equalsIgnoreCase(BitmsConst.POST))
        {// 只记录POST请求
            if (ACCOUNT_LOGGER && null != accountLogNoSql)
            {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                CSRFToken csrfToken = method.getAnnotation(CSRFToken.class);
                // 如果配置了校验csrf token校验，则校验
                if (null != csrfToken && csrfToken.check())
                {
                    saveOperationLogs(request, getRequestParms(request));
                }
            }
        }
    }
    
    /**
     * 保存操作日志
     * @param request
     * @param content
     */
    void saveOperationLogs(HttpServletRequest request, String content)
    {
        try
        {
            AccountLog accountLog;
            UserPrincipal principal = OnLineUserUtils.getPrincipal();
            if (null == principal)
            {
                accountLog = new AccountLog(100000000000L);
                accountLog.setAccountName("匿名用户");
            }
            else
            {
                accountLog = new AccountLog(principal.getId());
                accountLog.setAccountName(principal.getUserName());
            }
            accountLog.setContent(content);
            accountLog.setUrl(request.getRequestURI());
            accountLog.setSystemName(BitmsConst.BITMS_PROJECT_NAME);
            accountLog.setOpType(AccountLogConsts.LOG_TYPE_DEFAULT);
            accountLog.setIpAddr(IPUtil.getOriginalIpAddr(request));
            accountLog.setCreateDate(CalendarUtils.getCurrentLong());
            accountLogNoSql.insert(accountLog);
        }
        catch (RuntimeException e)
        {
            logger.error("操作日志记录失败：{}", e.getCause());
        }
    }
    
    /**
     * 获取请求报文
     * @param request
     * @return
     */
    String getRequestParms(HttpServletRequest request)
    {
        Map<String, String[]> params = request.getParameterMap();
        StringBuffer buffer = new StringBuffer("");
        for (String key : params.keySet())
        {
            if (isAccessDenied(key)) continue;
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++)
            {
                String value = values[i];
                buffer.append(key).append("=").append(value).append("&");
            }
        }
        return buffer.toString();
    }
    
    /**
     * 判断是否有权限访问,有返回true,反之false
     * @param request
     * @return
     * @throws Exception
     */
    boolean isAccessAllowed(HttpServletRequest request)
    {
        String requestIp = IPUtil.getOriginalIpAddr(request);
        for (String pattern : blackUrlPathPattern)
        {
            if (PatternMatchUtils.simpleMatch(pattern, requestIp)) { return true; }
        }
        return false;
    }
    
    /**
     * 排除敏感参数
     * @param key
     * @return
     */
    protected boolean isAccessDenied(String key)
    {
        for (String pattern : BitmsConst.blackStrPathPattern)
        {
            if (PatternMatchUtils.simpleMatch(pattern, key)) { return true; }
        }
        return false;
    }
}
