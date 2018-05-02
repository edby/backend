package com.blocain.bitms.security.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.blocain.bitms.tools.bean.ClientParameter;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ParameterUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 *  RPC安全验证
 * <p>File：ClientApiInterceptor.java</p>
 * <p>Title: ClientApiInterceptor</p>
 * <p>Description:RPC安全验证</p>
 * <p>Copyright: Copyright (c) 2017 </p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ClientApiInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger LOGGER = Logger.getLogger(ClientApiInterceptor.class);
    
    private String              clientKey;
    
    /**
     * 数据验证
     * @param request
     * @param response
     * @param handler
     * @return {@link Boolean}
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (!request.getMethod().equalsIgnoreCase("POST")) throw new BusinessException(CommonEnums.ERROR_ILLEGAL_REQUEST);
        // 如果启用INFO日志, 记录接口调用信息
        if (LOGGER.isInfoEnabled())
        {
            StringBuilder sb = new StringBuilder(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort())
                    .append(request.getContextPath()).append(request.getRequestURI());
            String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) sb.append("?").append(queryString);
            LOGGER.info("Request url:" + sb.toString());
            Enumeration<?> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements())
            {
                String paramName = (String) parameterNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                StringBuilder valuesString = new StringBuilder(paramName).append(": ");
                for (String paramValue : paramValues)
                    valuesString.append(paramValue);
                LOGGER.info(valuesString.toString());
            }
        }
        // 用户KEY验证
        String userKey = StringUtils.trimToEmpty(request.getParameter(ClientParameter.USER_KEY));
        if (StringUtils.isBlank(userKey)) throw new BusinessException(CommonEnums.ERROR_AUTHER_FAILED);
        // 判断Key是否正确
        if (!StringUtils.equals(clientKey, userKey)) throw new BusinessException(CommonEnums.ERROR_AUTHER_FAILED);
        // 异或校验验证
        String userDes = StringUtils.trimToEmpty(request.getParameter(ClientParameter.USER_DES));
        if (StringUtils.isBlank(userDes)) throw new BusinessException(CommonEnums.ERROR_DES_CHECK_FAILED);
        // 数据长度验证
        String lenStr = StringUtils.trimToEmpty(request.getParameter(ClientParameter.DATA_LEN));
        if (!StringUtils.isNumeric(lenStr)) throw new BusinessException(CommonEnums.ERROR_DATA_LENGTH_FAILED);
        int dataLen = Integer.parseInt(lenStr);
        String data = request.getParameter(ClientParameter.DATA);
        if (!ParameterUtils.validateClientDataLength(dataLen, data)) throw new BusinessException(CommonEnums.ERROR_DATA_LENGTH_FAILED);
        if (!ParameterUtils.validateClientData(userDes, userKey, dataLen)) throw new BusinessException(CommonEnums.ERROR_DES_CHECK_FAILED);
        return true;
    }
    
    public void setClientKey(String clientKey)
    {
        this.clientKey = clientKey;
    }
}
