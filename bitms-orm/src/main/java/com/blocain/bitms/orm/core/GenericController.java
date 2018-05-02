package com.blocain.bitms.orm.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.orm.utils.ServletsUtils;
import com.blocain.bitms.tools.bean.*;
import com.blocain.bitms.tools.editor.*;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * <p>File：GenericController.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-21 下午1:46:45</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public abstract class GenericController
{
    protected static final Logger         logger = LoggerFactory.getLogger(GenericController.class);
    
    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator                   validator;
    
    @Autowired
    protected LocaleResolver              localeResolver;
    
    @Autowired
    protected ResourceBundleMessageSource rms;
    
//    /**
//     * 取Spring国际化资源
//     * @param key
//     * @return
//     */
//    public String getMessage(String key)
//    {
//        HttpServletRequest request = ServletsUtils.getRequest();
//        String message;
//        try
//        {
//            message = rms.getMessage(key, null, localeResolver.resolveLocale(request));
//        }
//        catch (NoSuchMessageException e)
//        {
//            LoggerUtils.logError(logger, e.getLocalizedMessage());
//            message = key;
//        }
//        return message;
//    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView handleException(Exception ex)
    {
        ModelAndView mav = new ModelAndView();
        HttpServletResponse response = ServletsUtils.getResponse();
        if (StringUtils.isNotBlank(response.getHeader("csrf")))
        {// 将CSRF放到返回对象中
            mav.addObject("csrf", response.getHeader("csrf"));
        }
        if (ex instanceof BusinessException)
        { // 自定义业务异常
            BusinessException bex = (BusinessException) ex;
            JsonMessage json = getJsonMessage(bex.getErrorCode());
            mav.addObject("code", json.getCode());
            mav.addObject("message", json.getMessage());
            mav.addObject("object", bex.getObject());
        }
        else
        {
            JsonMessage json = getJsonMessage(CommonEnums.FAIL);
            mav.addObject("code", json.getCode());
            mav.addObject("message", json.getMessage());
            mav.addObject("object", "Business Exception");
            //将运行异常记录到日志文件
            LoggerUtils.logError(logger, "RuntimeException:{}",ex.getLocalizedMessage());
        }
        return mav;
    }
    
    /**
     * 初始化数据绑定
     * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 2. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder)
    {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
        // Byte
        binder.registerCustomEditor(Byte.class, new ByteEditorSupport());
        // Float
        binder.registerCustomEditor(Float.class, new FloatEditorSupport());
        // Double
        binder.registerCustomEditor(Double.class, new DoubleEditorSupport());
        // Long
        binder.registerCustomEditor(Long.class, new LongEditorSupport());
        // Integer
        binder.registerCustomEditor(Integer.class, new IntegerEditorSupport());
        // Boolean
        binder.registerCustomEditor(Boolean.class, new BooleanEditorSupport());
        // Short
        binder.registerCustomEditor(Short.class, new ShortEditorSupport());
        // String
        binder.registerCustomEditor(String.class, new StringEditorSupport());
    }
    
    /**
     * 服务端参数有效性验证
     *
     * @param jsonMessage 错误信息（不能为null）
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 jsonMessage rows 中
     */
    protected boolean beanValidator(JsonMessage jsonMessage, Object object, Class<?> ... groups)
    {
        try
        {
            BeanValidators.validateWithException(validator, object, groups);
        }
        catch (ConstraintViolationException ex)
        {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            jsonMessage.setCode(CommonEnums.FAIL.getCode());
            jsonMessage.setMessage(CommonEnums.FAIL.getMessage());
//            jsonMessage.setMessage(getMessage(CommonEnums.FAIL.getMessage()));
            jsonMessage.setObject(list);
            return false;
        }
        return true;
    }
    
    /**
     * 分页查询记录集：API接口返回分页及记录集的JSON处理，不返回状态码描述
     * @param describable 异常码
     * @param result 结果集及Pagination对象
     * @return JsonMessage JsonMessage
     */
    protected <T extends Object> JsonMessage getJsonMessage(EnumDescribable describable, PaginateResult<T> result)
    {
        JsonMessage json = getJsonMessage(describable);
        if (null != result)
        {
            Pagination page = result.getPage();
            json.setRows(result.getList());
            json.setTotal(result.getPage().getTotalRows());
            json.setHasNext(page.getHasNextPage());
            json.setHasPrevious(page.getHasPreviousPage());
            json.setPages(page.getPage());
            json.setTotalPage(page.getTotalPage());
            json.setMessage(json.getMessage());
        }
        return json;
    }
    
    /**
     * 接口处理结果反馈 : ：API接口返回数据或交易处理结果的JSON处理
     * @param describable 异常代码描述
     * @param object 单结果返回对象
     * @return
     * @author Playguy
     */
    protected JsonMessage getJsonMessage(EnumDescribable describable, Object object)
    {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(describable.getCode());
        jsonMessage.setMessage(describable.getMessage());
//        jsonMessage.setMessage(getMessage(describable.getMessage()));
        jsonMessage.setObject(object);
        return setCsrf(jsonMessage);
    }
    
    /**
     * 接口处理结果反馈 : ：API接口返回数据或交易处理结果的JSON处理
     * @param describable 异常代码描述
     * @return JsonMessage JsonMessage
     * @author Playguy
     */
    public JsonMessage getJsonMessage(EnumDescribable describable)
    {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(describable.getCode());
        jsonMessage.setMessage(describable.getMessage());
//        jsonMessage.setMessage(getMessage(describable.getMessage()));
        return setCsrf(jsonMessage);
    }
    
    /**
     * 放置CSRF到返回对象中
     * @param json
     * @return
     */
    public JsonMessage setCsrf(JsonMessage json)
    {
        HttpServletResponse response = ServletsUtils.getResponse();
        String csrf = response.getHeader("csrf");
        if (StringUtils.isNotBlank(csrf))
        {// 将CSRF放到返回对象中
            json.setCsrf(csrf);
        }
        return json;
    }

    String errCntKey = "tradex_ga_error_cnt_";
    String errFrozenKey = "tradex_ga_error_frozen_";
    public void checkGaErrorCnt(Long uid,String secretKey,String gaCode) throws BusinessException
    {
        Authenticator authenticator = new Authenticator();
        Long cnt = (Long) RedisUtils.getObject(errCntKey+uid);
        Long frozen = (Long) RedisUtils.getObject(errFrozenKey+uid);
        if(null == cnt)cnt = 0L;
        if(null == frozen)frozen = 0L;
        if(frozen.longValue() > 0L)
        {
            throw new BusinessException("Your GA has been wrong over 10 times. The system will unlock your GA for 30 minutes.");
        }
        if(cnt >= 10)
        {
            RedisUtils.putObject(errFrozenKey+uid, 1L, CacheConst.THIRTY_MINUTE_CACHE_TIME);
            throw new BusinessException("Your GA has been wrong over 10 times. The system will unlock your GA for 30 minutes.");
        }
        if (!authenticator.checkCode(secretKey, Long.valueOf(gaCode)))
        {// 判断验证码
            cnt +=1;
            RedisUtils.putObject(errCntKey+uid, Long.valueOf(cnt), CacheConst.ONE_MINUTE_CACHE_TIME);
            throw new BusinessException("Google auth code error");
        }
    }
}
