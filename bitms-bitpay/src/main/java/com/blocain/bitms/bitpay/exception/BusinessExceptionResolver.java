package com.blocain.bitms.bitpay.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.bitpay.common.ApplicationConst;
import com.blocain.bitms.bitpay.common.JsonMessage;
import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * <p>File：BusinessExceptionResolver.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 下午1:37:48</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class BusinessExceptionResolver extends AbstractBaseExceptionResolver
{
    @Override
    protected boolean isSupportedException(Exception ex)
    {
        return ex instanceof BusinessException;
    }

    @Override
    protected ModelAndView doNormalResolveHandlerMethodException(
            HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex)
    {
        logger.error("系统异常:" + ex.getMessage(), ex);
        BusinessException businessException = (BusinessException) ex;
        EnumDescribable errorCodeDescribable = businessException.getErrorCode();
        ModelAndView mav = this.getModelAndView(request, null);
        mav.addObject("code", errorCodeDescribable.getCode());
        mav.addObject("message", errorCodeDescribable.getMessage());
        return mav;
    }

    @Override
    protected ModelAndView doAjaxResolveHandlerMethodException(
            HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex)
    {
        logger.error("系统异常:" + ex.getMessage(), ex);
    	BusinessException businessException = (BusinessException) ex;
        HttpMessageConverter<Object> messageConverter = getJsonMessageConverter();
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        outputMessage.getHeaders().add("Content-Type", "text/plain;charset=UTF-8"); 
        try
        {
            JsonMessage jsonMessage = new JsonMessage();
            EnumDescribable errorCode = businessException.getErrorCode();
            jsonMessage.setCode(errorCode.getCode());
            jsonMessage.setMessage(errorCode.getMessage());
            messageConverter.write(jsonMessage, MediaType.APPLICATION_JSON, outputMessage);
            return new ModelAndView();
        }
        catch (Exception e)
        {
            ModelAndView mav = this.getModelAndView(request, null);
            mav.addObject("code", ApplicationConst.ERROR_CODE_FAILURE);
            mav.addObject("message", "网络异常");
            return mav;
        }
    }
    
    private ModelAndView getModelAndView(HttpServletRequest request, String errUrl)
    {
        if(StringUtils.isBlank(errUrl)){
            errUrl = "error/error_500";
        }
        return new ModelAndView(errUrl);
    }
}
