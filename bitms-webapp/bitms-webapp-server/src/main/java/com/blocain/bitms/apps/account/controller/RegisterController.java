package com.blocain.bitms.apps.account.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.apps.account.beans.LoginRequest;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.CheckRequest;
import com.blocain.bitms.apps.account.beans.RegisterRequest;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.boss.common.model.EmailModel;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.google.common.collect.Maps;

/**
 * 注册控制器 Introduce
 * <p>Title: RegisterController</p>
 * <p>File：RegisterController.java</p>
 * <p>Description: RegisterController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
public class RegisterController extends AppsController
{
    @Autowired(required = false)
    private AccountService accountService;
    
    String                 errCntKey    = "trade_register_check_error_cnt_"; // 统计注册二次确认一分钟内错误次数
    
    String                 errFrozenKey = "trade_register_check_frozen_";    // 标识账户冻结半小时
    
    /**
     * 帐户注册
     * @param request
     * @return {@link AppsMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/register")
    public AppsMessage register(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        String content = checkSignJson(params); // 校验签名并返回请求参数
        RegisterRequest registerRequest = JSON.parseObject(content, RegisterRequest.class);
        if (!beanValidator(message, registerRequest)) return message;// 验证业务参数
        AliyunModel model = JSON.parseObject(content, AliyunModel.class);
        if (StringUtils.isNotBlank(model.getCsessionid()) || StringUtils.isNotBlank(model.getScene()))
        { // 判断验证码
            if (!AliyunUtils.validParams(model))
            {// 验证不通过时
                return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA, Boolean.TRUE);
            }
        }
        else
        {
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA, Boolean.TRUE);
        }
        Account account = new Account(registerRequest.getEmail());
        String cacheAddress = accountService.mobileRegister(account, BitmsConst.DEFAULT_LANGUAGE, IPUtil.getOriginalIpAddr(request));
        HashMap<String, String> result = Maps.newHashMap();
        result.put("email", registerRequest.getEmail());
        result.put("access_token", EncryptUtils.desEncrypt(cacheAddress));
        String json = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(json, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
    
    /**
     * 帐户注册复查
     * @param request
     * @return {@link AppsMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/register/check")
    public AppsMessage check(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        CheckRequest checkRequest = checkSign(params, CheckRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, checkRequest)) return message;
        if (StringUtils.isBlank(checkRequest.getCode()))
        {// 验证业务参数
            message = this.getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            return message;
        }
        String accessToken = EncryptUtils.desDecrypt(checkRequest.getAccessToken());
        EmailModel model = (EmailModel) RedisUtils.getObject(accessToken);
        if (null == model) throw new BusinessException(CommonEnums.ERROR_ILLEGAL_REQUEST);
        Long cnt = (Long) RedisUtils.getObject(errCntKey + model.getEmail());
        Long frozen = (Long) RedisUtils.getObject(errFrozenKey + model.getEmail());
        if (null == cnt) cnt = 0L;
        if (null == frozen) frozen = 0L;
        if (frozen.longValue() > 0L)
        {
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        if (cnt >= 10)
        {
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        if (!model.getRandomKey().equals(checkRequest.getCode()))
        {// 会话已过期
            registerCheckCnt(model.getEmail());
            throw new BusinessException(CommonEnums.ERROR_EMAIL_VALID_FAILED);
        }
        HashMap<String, String> result = Maps.newHashMap();
        result.put("check_state", "true");
        result.put("access_token", checkRequest.getAccessToken());
        String json = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(json, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
    
    /**
     * 帐户注册设置密码
     * @param request
     * @return {@link AppsMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/register/setPass")
    public AppsMessage setPass(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        CheckRequest checkRequest = checkSign(params, CheckRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, checkRequest)) return message;
        if (null == checkRequest || StringUtils.isBlank(checkRequest.getPassword()))
        {// 验证业务参数
            message = this.getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            return message;
        }
        String accessToken = EncryptUtils.desDecrypt(checkRequest.getAccessToken());
        EmailModel model = (EmailModel) RedisUtils.getObject(accessToken);
        if (null == model) throw new BusinessException(CommonEnums.ERROR_ILLEGAL_REQUEST);
        Account account = new Account();
        // 设置account的id
        Long id = SerialnoUtils.buildPrimaryKey();
        account.setId(id);
        account.setEmail(model.getEmail());
        account.setLoginPwd(checkRequest.getPassword());
        String invitCode = model.getInvitCode();
        account.setInvitCode(null != invitCode && !"null".equals(invitCode) ? Long.valueOf(invitCode) : null);
        accountService.registerConfirm(account);
        RedisUtils.del(accessToken);// 删除临时缓存
        return message;
    }

    public void registerCheckCnt(String email) throws BusinessException
    {
        Long cnt = (Long) RedisUtils.getObject(errCntKey + email);
        Long frozen = (Long) RedisUtils.getObject(errFrozenKey + email);
        if (null == cnt) cnt = 0L;
        if (null == frozen) frozen = 0L;
        if (frozen.longValue() > 0L)
        {
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        if (cnt >= 10)
        {
            RedisUtils.putObject(errFrozenKey + email, 1L, CacheConst.THIRTY_MINUTE_CACHE_TIME);
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        cnt += 1;
        RedisUtils.putObject(errCntKey + email, Long.valueOf(cnt), CacheConst.ONE_MINUTE_CACHE_TIME);
    }
}
