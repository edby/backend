package com.blocain.bitms.security;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.DelegatingSession;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 自定义WebSessionManager，用于替代DefaultWebSessionManager；
 * 解决：
 *      在shiro的一次认证过程中会调用10次左右的 doReadSession，如果使用内存缓存这个问题不大。
 *      但是如果使用redis，而且子网络情况不是特别好的情况下这就成为问题了。我简单在我的环境下测试了一下。
 *      一次redis请求需要80 ~ 100 ms， 一下来10次，我们一次认证就需要10 * 100 = 1000 ms, 这个就是我们无法接受的了。
 *
 * 大部分代码都是从DefaultWebSessionManager中复制过来的，扩展点在 line：200~212、225~229
 *
 * @author Goma oma1989@yeah.net  2016.03.31
 *
 */
public class CustomSessionManager extends DefaultSessionManager implements WebSessionManager
{
    private static final Logger log = LoggerFactory.getLogger(DefaultWebSessionManager.class);
    
    private Cookie              sessionIdCookie;
    
    private boolean             sessionIdCookieEnabled;
    
    public Cookie getSessionIdCookie()
    {
        return sessionIdCookie;
    }
    
    public void setSessionIdCookie(Cookie sessionIdCookie)
    {
        this.sessionIdCookie = sessionIdCookie;
    }
    
    public boolean isSessionIdCookieEnabled()
    {
        return sessionIdCookieEnabled;
    }
    
    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled)
    {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }
    
    private void storeSessionId(Serializable currentId, HttpServletRequest request, HttpServletResponse response)
    {
        if (currentId == null)
        {
            String msg = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(msg);
        }
        Cookie template = getSessionIdCookie();
        Cookie cookie = new SimpleCookie(template);
        String idString = currentId.toString();
        cookie.setValue(idString);
        cookie.saveTo(request, response);
        log.trace("Set session ID cookie for session with id {}", idString);
    }
    
    private void removeSessionIdCookie(HttpServletRequest request, HttpServletResponse response)
    {
        getSessionIdCookie().removeFrom(request, response);
    }
    
    private String getSessionIdCookieValue(ServletRequest request, ServletResponse response)
    {
        if (!isSessionIdCookieEnabled())
        {
            log.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        }
        if (!(request instanceof HttpServletRequest))
        {
            log.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
    }
    
    private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response)
    {
        String id = getSessionIdCookieValue(request, response);
        if (id != null)
        {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
        }
        else
        {
            // not in a cookie, or cookie is disabled - try the request URI as a
            // fallback (i.e. due to URL rewriting):
            // try the URI path segment parameters first:
            id = getUriPathSegmentParamValue(request, ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
            if (id == null)
            {
                // not a URI path segment parameter, try the query parameters:
                String name = getSessionIdName();
                id = request.getParameter(name);
                if (id == null)
                {
                    // try lowercase:
                    id = request.getParameter(name.toLowerCase());
                }
            }
            if (id != null)
            {
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
            }
        }
        if (id != null)
        {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            // automatically mark it valid here. If it is invalid, the
            // onUnknownSession method below will be invoked and we'll remove
            // the attribute at that time.
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        return id;
    }
    
    // SHIRO-351
    // also see
    // http://cdivilly.wordpress.com/2011/04/22/java-servlets-uri-parameters/
    // since 1.2.2
    private String getUriPathSegmentParamValue(ServletRequest servletRequest, String paramName)
    {
        if (!(servletRequest instanceof HttpServletRequest)) { return null; }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        if (uri == null) { return null; }
        int queryStartIndex = uri.indexOf('?');
        if (queryStartIndex >= 0)
        { // get rid of the query string
            uri = uri.substring(0, queryStartIndex);
        }
        int index = uri.indexOf(';'); // now check for path segment parameters:
        if (index < 0)
        {
            // no path segment params - return:
            return null;
        }
        // there are path segment params, let's get the last one that may exist:
        final String TOKEN = paramName + "=";
        uri = uri.substring(index + 1); // uri now contains only the path
        // segment params
        // we only care about the last JSESSIONID param:
        index = uri.lastIndexOf(TOKEN);
        if (index < 0)
        {
            // no segment param:
            return null;
        }
        uri = uri.substring(index + TOKEN.length());
        index = uri.indexOf(';'); // strip off any remaining segment params:
        if (index >= 0)
        {
            uri = uri.substring(0, index);
        }
        return uri; // what remains is the value
    }
    
    // ------------------------------------------------------------------------------------------------------------------
    /**
     * Modify By Goma
     */
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException
    {
        Serializable sessionId = getSessionId(sessionKey);
        if (sessionId == null)
        {
            log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a " + "session could not be found.", sessionKey);
            return null;
        }
        // ***************Add By Goma****************
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey)
        {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null)
        {
            Object s = request.getAttribute(sessionId.toString());
            if (s != null) { return (Session) s; }
        }
        // ***************Add By Goma****************
        Session s = retrieveSessionFromDataSource(sessionId);
        if (s == null)
        {
            // session ID was provided, meaning one is expected to be found, but
            // we couldn't find one:
            String msg = "Could not find session with ID [" + sessionId + "]";
            throw new UnknownSessionException(msg);
        }
        // ***************Add By Goma****************
        if (request != null)
        {
            request.setAttribute(sessionId.toString(), s);
        }
        // ***************Add By Goma****************
        return s;
    }
    
    // ------------------------------------------------------------------------------------------------------------------
    // since 1.2.1
    private String getSessionIdName()
    {
        String name = this.sessionIdCookie != null ? this.sessionIdCookie.getName() : null;
        if (name == null)
        {
            name = ShiroHttpSession.DEFAULT_SESSION_ID_NAME;
        }
        return name;
    }
    
    protected Session createExposedSession(Session session, SessionContext context)
    {
        if (!WebUtils.isWeb(context)) { return super.createExposedSession(session, context); }
        ServletRequest request = WebUtils.getRequest(context);
        ServletResponse response = WebUtils.getResponse(context);
        SessionKey key = new WebSessionKey(session.getId(), request, response);
        return new DelegatingSession(this, key);
    }
    
    protected Session createExposedSession(Session session, SessionKey key)
    {
        if (!WebUtils.isWeb(key)) { return super.createExposedSession(session, key); }
        ServletRequest request = WebUtils.getRequest(key);
        ServletResponse response = WebUtils.getResponse(key);
        SessionKey sessionKey = new WebSessionKey(session.getId(), request, response);
        return new DelegatingSession(this, sessionKey);
    }
    
    /**
     * Stores the Session's ID, usually as a Cookie, to associate with future
     * requests.
     *
     * @param session
     *            the session that was just {@link #createSession created}.
     */
    @Override
    protected void onStart(Session session, SessionContext context)
    {
        super.onStart(session, context);
        if (!WebUtils.isHttp(context))
        {
            log.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response " + "pair. No session ID cookie will be set.");
            return;
        }
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);
        if (isSessionIdCookieEnabled())
        {
            Serializable sessionId = session.getId();
            storeSessionId(sessionId, request, response);
        }
        else
        {
            log.debug("Session ID cookie is disabled.  No cookie has been set for new session with id {}", session.getId());
        }
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }
    
    @Override
    public Serializable getSessionId(SessionKey key)
    {
        Serializable id = super.getSessionId(key);
        if (id == null && WebUtils.isWeb(key))
        {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            id = getSessionId(request, response);
        }
        return id;
    }
    
    protected Serializable getSessionId(ServletRequest request, ServletResponse response)
    {
        return getReferencedSessionId(request, response);
    }
    
    @Override
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key)
    {
        super.onExpiration(s, ese, key);
        onInvalidation(key);
    }
    
    @Override
    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key)
    {
        super.onInvalidation(session, ise, key);
        onInvalidation(key);
    }
    
    private void onInvalidation(SessionKey key)
    {
        ServletRequest request = WebUtils.getRequest(key);
        if (request != null)
        {
            request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
        }
        if (WebUtils.isHttp(key))
        {
            log.debug("Referenced session was invalid.  Removing session ID cookie.");
            removeSessionIdCookie(WebUtils.getHttpRequest(key), WebUtils.getHttpResponse(key));
        }
        else
        {
            log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response "
                    + "pair. Session ID cookie will not be removed due to invalidated session.");
        }
    }
    
    @Override
    protected void onStop(Session session, SessionKey key)
    {
        super.onStop(session, key);
        if (WebUtils.isHttp(key))
        {
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            log.debug("Session has been stopped (subject logout or explicit stop).  Removing session ID cookie.");
            removeSessionIdCookie(request, response);
        }
        else
        {
            log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response "
                    + "pair. Session ID cookie will not be removed due to stopped session.");
        }
    }
    
    /**
     * This is a native session manager implementation, so this method returns
     * {@code false} always.
     *
     * @return {@code false} always
     * @since 1.2
     */
    public boolean isServletContainerSessions()
    {
        return false;
    }
}