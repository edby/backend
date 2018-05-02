package com.blocain.bitms.apps.basic.controller;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.beans.RequestModel;
import com.blocain.bitms.apps.basic.consts.AppsContsts;
import com.blocain.bitms.apps.sdk.ApiException;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Signature;
import com.blocain.bitms.tools.bean.BeanValidators;
import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.editor.*;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.google.common.collect.Maps;

/**
 * AppsController Introduce
 * <p>Title: AppsController</p>
 * <p>File：AppsController.java</p>
 * <p>Description: AppsController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AppsController {
    protected static final Logger logger = LoggerFactory.getLogger(AppsController.class);

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    @Autowired(required = false)
    private AccountService accountService;

    /**
     * 初始化数据绑定
     * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 2. 将字段中Date类型转换为String类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
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

    public void checkSesion(SessionInfo sessionInfo, String authToken) {
        if (null == sessionInfo) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append("mobile_authtoken").append(sessionInfo.getAccountName());
        try {
            String tokenInRedis = RedisUtils.get(key.toString());
            if (null == tokenInRedis) throw new BusinessException(CommonEnums.ERROR_LOGIN_TIMEOUT);
            if (!StringUtils.equalsIgnoreCase(tokenInRedis, authToken)) {
                throw new BusinessException(CommonEnums.ERROR_LOGIN_TIMEOUT);
            }
        } catch (Exception e) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_TIMEOUT);
        }
    }

    public Account checkAccount(Long id) {
        Account account = accountService.selectByPrimaryKey(id);
        if (null == account) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        if (null != account && !account.verifySignature()) {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK);
        }
        return account;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView handleException(Exception ex) {
        ModelAndView mav = new ModelAndView();
        if (ex instanceof BusinessException) { // 自定义业务异常
            BusinessException bex = (BusinessException) ex;
            AppsMessage json = getJsonMessage(bex.getErrorCode());
            mav.addObject("code", json.getCode());
            mav.addObject("message", json.getMessage());
            mav.addObject("object", bex.getObject());
        } else {
            mav.addObject("code", "1000");
            mav.addObject("message", ex.getLocalizedMessage());
            // 将运行异常记录到日志文件
            LoggerUtils.logError(logger, "RuntimeException:{}", ex.getLocalizedMessage());
        }
        return mav;
    }

    /**
     * 服务端参数有效性验证
     *
     * @param jsonMessage 错误信息（不能为null）
     * @param object      验证的实体对象
     * @param groups      验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 jsonMessage rows 中
     */
    protected boolean beanValidator(AppsMessage jsonMessage, Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            if (list.contains("authToken: 不能为null")) {
                jsonMessage.setCode(CommonEnums.USER_NOT_LOGIN.getCode());
                jsonMessage.setMessage(CommonEnums.USER_NOT_LOGIN.getMessage());
            } else {
                jsonMessage.setCode(CommonEnums.PARAMS_VALID_ERR.getCode());
                jsonMessage.setMessage(CommonEnums.PARAMS_VALID_ERR.getMessage());
            }
            jsonMessage.setData(list);
            return false;
        }
        return true;
    }


    /**
     * 取所有请求参数
     *
     * @param request
     * @return
     */
    protected Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        String parameterName;
        while (parameterNames.hasMoreElements()) {
            parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        return params;
    }

    /**
     * 验证公共参数
     *
     * @param params
     * @param jsonMessage
     * @return {@link Map}
     * @throws BusinessException
     */
    protected boolean checkParams(Map<String, String> params, AppsMessage jsonMessage) throws BusinessException {
        try {
            RequestModel model = new RequestModel();
            BeanUtils.populate(model, params);
            Long requestTime = Long.valueOf(model.getTimestamp());
            Long currTime = System.currentTimeMillis();
            if (currTime.longValue() - requestTime >= 5000 || currTime.longValue() - requestTime < -5000) {
                // request 请求已过期
                throw new BusinessException(CommonEnums.ERROR_REQUEST_EXPIRED);
            }
            return beanValidator(jsonMessage, model);
        } catch (Exception e) {
            throw new BusinessException(e.getLocalizedMessage());
        }
    }

    /**
     * 验证签名并返回请求参数对象
     *
     * @param params 验证参数
     * @param clazz  返回类型
     * @return {@link T}
     * @throws ApiException
     * @version1.0
     */
    protected <T> T checkSign(Map<String, String> params, Class<T> clazz) throws ApiException {
        if (null == params) return null;
        boolean isDecrypt = null != params.get(BitmsConstants.ENCRYPT_KEY);
        String content = Signature.checkSignAndDecrypt(params, AppsContsts.PUBLIC_KEY, isDecrypt);
        return JSON.parseObject(content, clazz);
    }

    /**
     * 验证签名并返回请求参数JSON对象
     *
     * @param params 验证参数
     * @throws ApiException
     * @version2.0
     * @author:yukai
     * @since :04.24.2018
     */
    protected String checkSignJson(Map<String, String> params) throws ApiException {
        if (null == params) return null;
        boolean isDecrypt = null != params.get(BitmsConstants.ENCRYPT_KEY);
        String content = Signature.checkSignAndDecrypt(params, AppsContsts.PUBLIC_KEY, isDecrypt);
        return content;
    }

    /**
     * 接口处理结果反馈 : ：API接口返回数据或交易处理结果的JSON处理
     *
     * @param describable 异常代码描述
     * @param object      单结果返回对象
     * @return {@link AppsMessage}
     * @author Playguy
     */
    protected AppsMessage getJsonMessage(EnumDescribable describable, Object object) {
        AppsMessage jsonMessage = new AppsMessage();
        jsonMessage.setCode(describable.getCode());
        jsonMessage.setMessage(describable.getMessage());
        jsonMessage.setData(object);
        return jsonMessage;
    }

    /**
     * 接口处理结果反馈 : ：API接口返回数据或交易处理结果的JSON处理
     *
     * @param describable 异常代码描述
     * @return JsonMessage JsonMessage
     * @author Playguy
     */
    public AppsMessage getJsonMessage(EnumDescribable describable) {
        AppsMessage jsonMessage = new AppsMessage();
        jsonMessage.setCode(describable.getCode());
        jsonMessage.setMessage(describable.getMessage());
        return jsonMessage;
    }
}
