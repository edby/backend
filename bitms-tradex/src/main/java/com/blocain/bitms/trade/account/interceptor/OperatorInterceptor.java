package com.blocain.bitms.trade.account.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.IPUtil;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.maxmind.geoip.Location;

/**
 * 操作记录拦截器 Introduce
 * <p>File：OperatorInterceptor.java</p>
 * <p>Title: OperatorInterceptor</p>
 * <p>Description: OperatorInterceptor</p>
 * <p>Copyright: Copyright (c) 2017/7/4</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class OperatorInterceptor extends HandlerInterceptorAdapter
{
    public static final Logger logger = LoggerFactory.getLogger(OperatorInterceptor.class);
    
    // 日志服务
    private AccountLogNoSql    accountLogNoSql;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException
    {
        if (!request.getMethod().equalsIgnoreCase("POST"))
        { // 只拦截POST请求
            return true;
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null != principal)
        {
            try
            {
                AccountLog accountLog = new AccountLog(principal.getId());
                accountLog.setIpAddr(IPUtil.getOriginalIpAddr(request));
                accountLog.setUrl(request.getRequestURI());
                accountLog.setContent(request.getRequestURI());
                accountLog.setCreateDate(CalendarUtils.getCurrentLong());
                if (null != accountLog.getIpAddr())
                {
                    String rigonName = "Unknown address";
                    String[] ipArray = accountLog.getIpAddr().split(",");
                    for (String ip : ipArray) {
                        Location location = GeoIPUtils.getInstance().getLocation(ip);
                        if (null != location)
                        {
                            rigonName = new StringBuilder(location.countryName).append("|").append(location.city).toString();
                        }
                        break;
                    }
                    accountLog.setRigonName(rigonName);
                }
                accountLogNoSql.insert(accountLog);
            }
            catch (BusinessException e)
            {
                logger.error(e.getMessage());
            }
        }
        return true;
    }
    
    public void setAccountLogService(AccountLogNoSql accountLogService)
    {
        this.accountLogNoSql = accountLogService;
    }
}
