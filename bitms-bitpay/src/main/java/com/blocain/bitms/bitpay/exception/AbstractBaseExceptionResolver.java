package com.blocain.bitms.bitpay.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

 /**
  * <p>File：AbstractBaseExceptionResolver.java</p>
  * <p>Title: </p>
  * <p>Description:</p>
  * <p>Copyright: Copyright (c) 2017年7月18日 下午1:36:50</p>
  * <p>Company: BloCain</p>
  * @author 施建波
  * @version 1.0
  */
public abstract class AbstractBaseExceptionResolver extends AbstractHandlerMethodExceptionResolver
{
    private HttpMessageConverter<Object> jsonMessageConverter;

    public HttpMessageConverter<Object> getJsonMessageConverter()
    {
        return jsonMessageConverter;
    }

    public void setJsonMessageConverter(
            HttpMessageConverter<Object> jsonMessageConverter)
    {
        this.jsonMessageConverter = jsonMessageConverter;
    }

    /**
     * 支持的异常类型
     * @param ex
     * @return
     */
    protected abstract boolean isSupportedException(Exception ex);

    @Override
    protected ModelAndView doResolveHandlerMethodException(
            HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex){
    	if(!isSupportedException(ex)) {
    	    return null;
        }
        if(isAjax(request, response, handlerMethod, ex)) {
            return doAjaxResolveHandlerMethodException(request, response, handlerMethod, ex);
        }
        return doNormalResolveHandlerMethodException(request, response, handlerMethod, ex);
    }

    /**
     * 判断是否为 ajax 请求
     * @param request
     * @param response
     * @param handlerMethod
     * @param ex
     * @return
     */
    private boolean isAjax(HttpServletRequest request,
            HttpServletResponse response, HandlerMethod handlerMethod,
            Exception ex)
    {
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }
        if(null == handlerMethod){
            return false;
        }
        ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class);
        return responseBody != null;
    }

    /**
     * 普通请求异常处理
     * @param request
     * @param response
     * @param handlerMethod
     * @param ex
     * @return
     */
    protected abstract ModelAndView doNormalResolveHandlerMethodException(
            HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex);

    /**
     * ajax请求异常处理
     * @param request
     * @param response
     * @param handlerMethod
     * @param ex
     * @return
     */
    protected abstract ModelAndView doAjaxResolveHandlerMethodException(
            HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex);
}
