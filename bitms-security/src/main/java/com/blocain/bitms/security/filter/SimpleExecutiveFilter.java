package com.blocain.bitms.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.util.PatternMatchUtils;

/**
 * <p>File：SimpleExecutiveFilter.java </p>
 * <p>Title: 黑名单可执行程序请求过滤器   </p>
 * <p>Description: SimpleExecutiveFilter </p>
 * <p>Copyright: Copyright (c) 15/9/12</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SimpleExecutiveFilter extends AuthorizationFilter
{
    protected static final String[] blackUrlPathPattern = new String[]{"*.aspx*", "*.asp*", "*.php*", "*.exe*", "*.pl*", "*.py*", "*.groovy*", "*.sh*", "*.rb*", "*.dll*",
            "*.bat*", "*.bin*", "*.dat*", "*.bas*", "*.so*", "*.cmd*", "*.com*", "*.cpp*", "*.jar*", "*.class*", "*.lnk*"};
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object obj) throws Exception
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String reqUrl = httpRequest.getRequestURI().toLowerCase().trim();
        for (String pattern : blackUrlPathPattern)
        {
            if (PatternMatchUtils.simpleMatch(pattern, reqUrl)) { return false; }
        }
        return true;
    }
}
