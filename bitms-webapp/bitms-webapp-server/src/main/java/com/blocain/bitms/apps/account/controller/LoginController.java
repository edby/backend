package com.blocain.bitms.apps.account.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.*;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.apps.spot.model.EntrustModel;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.RedisSessionManager;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.AliyunUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Map;

/**
 * 登陆控制器 Introduce
 * <p>Title: CommonController</p>
 * <p>File：CommonController.java</p>
 * <p>Description: CommonController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
public class LoginController extends AppsController {
    @Autowired(required = false)
    private AccountService accountService;

    @Autowired(required = false)
    private MsgRecordNoSql msgRecordService;

    @Autowired(required = false)
    private AccountPolicyService accountPolicyService;

    @Autowired(required = false)
    private RedisSessionManager redisSessionManager;

    String errCntKey = "trade_login_check_error_cnt_"; // 统计二次一分钟内登录错误次数

    String errFrozenKey = "trade_login_check_frozen_";    // 标识账户冻结半小时

    /**
     * 帐户登陆
     *
     * @param request
     * @return {@link AppsMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/login")
    public AppsMessage login(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        String content = checkSignJson(params); // 校验签名并返回请求参数
        LoginRequest loginRequest = JSON.parseObject(content, LoginRequest.class);
        if (!beanValidator(message, loginRequest)) return message;// 验证业务参数
        AliyunModel model = JSON.parseObject(content, AliyunModel.class);
        if (StringUtils.isNotBlank(model.getCsessionid()) || StringUtils.isNotBlank(model.getScene())) { // 判断验证码
            if (!AliyunUtils.validParams(model)) {// 验证不通过时
                return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA, Boolean.TRUE);
            }
        }
        Account account = accountService.findByName(loginRequest.getUsername().toLowerCase());
        //先验证登陆次数
        Integer loginTimes = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
        if (null != loginTimes && loginTimes > 2)
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNTPASSWORD, true);
        if (null == account) {
            logLoginTimes(request);
            Integer showCaptcha = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_ACCOUNTPASSWORD, null != showCaptcha && showCaptcha > 2 ? true : false);
        }
        if (!EncryptUtils.validatePassword(loginRequest.getPassword(), account.getLoginPwd())) {// 验证密码
            logLoginTimes(request);
            Integer showCaptcha = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_ACCOUNTPASSWORD, null != showCaptcha && showCaptcha > 2 ? true : false);
        }
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        Long currentTime = System.currentTimeMillis();
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setId(account.getId());
        sessionInfo.setCountry(account.getCountry());
        sessionInfo.setAccountName(account.getAccountName());
        sessionInfo.setLoginDate(currentTime);
        sessionInfo.setMobile(account.getMobNo());
        sessionInfo.setLang(account.getLang());
        if (account.getSecurityPolicy() > AccountConsts.SECURITY_POLICY_DEFAULT) {// 二次验证
            Map<String, String> result = Maps.newHashMap();
            StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append("mobile_").append(account.getUnid());
            RedisUtils.putObject(key.toString(), sessionInfo, CacheConst.DEFAULT_CACHE_TIME);
            result.put("access_token", EncryptUtils.desEncrypt(key.toString()));
            if (AccountConsts.SECURITY_POLICY_NEEDGA == account.getSecurityPolicy()) {// GA验证
                result.put("check_type", "GA");
            }
            if (AccountConsts.SECURITY_POLICY_NEEDSMS == account.getSecurityPolicy()) {// 短信验证
                result.put("check_type", "SMS");
            }
            if (AccountConsts.SECURITY_POLICY_NEEDGAORSMS == account.getSecurityPolicy()) {// GA或短信验证
                result.put("check_type", account.getAuthKey() == null ? "SMS" : "GA");
            }
            String resultJson = JSON.toJSONString(result);
            message.setCode(2001);
            message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        } else {// 普通认证
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTime);
            calendar.add(Calendar.DATE, 7);
            sessionInfo.setExpireDate(calendar.getTimeInMillis());
            Map<String, String> result = Maps.newHashMap();
            String authJson = JSON.toJSONString(sessionInfo);
            String authToken = EncryptUtils.desEncrypt(authJson);
            result.put("auth_token", authToken);
            String resultJson = JSON.toJSONString(result);
            message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
            StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append("mobile_authtoken").append(sessionInfo.getAccountName());
            RedisUtils.putObject(key.toString(), authToken, CacheConst.TWENTYFOUR_HOUR_CACHE_TIME * 7);
        }
        return message;
    }

    /**
     * 帐户登陆二次校验
     *
     * @param request
     * @return {@link AppsMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/login/check")
    public AppsMessage check(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        CheckRequest checkRequest = checkSign(params, CheckRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, checkRequest)) return message;// 验证业务参数
        String accessToken = EncryptUtils.desDecrypt(checkRequest.getAccessToken());
        SessionInfo sessionInfo = (SessionInfo) RedisUtils.getObject(accessToken);
        if (null == sessionInfo) throw new BusinessException(CommonEnums.ERROR_ILLEGAL_REQUEST);
        Account account = accountService.selectByPrimaryKey(sessionInfo.getId());
        // 构建验证策略对象
        PolicyModel policy = new PolicyModel();
        policy.setGa(checkRequest.getCode());
        policy.setSms(checkRequest.getCode());
        Long cnt = (Long) RedisUtils.getObject(errCntKey + account.getUnid());
        Long frozen = (Long) RedisUtils.getObject(errFrozenKey + account.getUnid());
        if (null == cnt) cnt = 0L;
        if (null == frozen) frozen = 0L;
        if (frozen.longValue() > 0L) {
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        if (cnt >= 10) {
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        // 二次验证
        try {
            accountPolicyService.validSecurityPolicy(account, policy);
        } catch (BusinessException e) {
            loginCheckCnt(account.getUnid());
            throw e;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sessionInfo.getLoginDate());
        calendar.add(Calendar.DATE, 7);
        sessionInfo.setExpireDate(calendar.getTimeInMillis());
        Map<String, String> result = Maps.newHashMap();
        String authJson = JSON.toJSONString(sessionInfo);
        String authToken = EncryptUtils.desEncrypt(authJson);
        result.put("auth_token", authToken);
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        RedisUtils.del(accessToken);
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append("mobile_authtoken").append(sessionInfo.getAccountName());
        RedisUtils.putObject(key.toString(), authToken, CacheConst.TWENTYFOUR_HOUR_CACHE_TIME * 7);
        return message;
    }

    /**
     * 发送手机验证码，登陆时用
     *
     * @param request
     * @return {@link AppsMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/login/sendSms")
    public AppsMessage sendSMS(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        AccessRequest accessRequest = checkSign(params, AccessRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, accessRequest)) return message;// 验证业务参数
        String accessToken = EncryptUtils.desDecrypt(accessRequest.getAccessToken());
        SessionInfo sessionInfo = (SessionInfo) RedisUtils.getObject(accessToken);
        if (null == sessionInfo) throw new BusinessException(CommonEnums.ERROR_ILLEGAL_REQUEST);
        msgRecordService.sendSms(new StringBuffer(sessionInfo.getCountry()).append(sessionInfo.getMobile()).toString(), sessionInfo.getLang());
        return message;
    }

    /**
     * 退出接口
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/logout")
    public AppsMessage signOut(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        AuthTokenRequest tokenRequest = checkSign(params, AuthTokenRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append("mobile_authtoken").append(session.getAccountName());
        RedisUtils.del(key.toString());
        return message;
    }

    /**
     * 记录登陆出错的次数
     *
     * @param request
     */
    void logLoginTimes(HttpServletRequest request) {
        Integer count = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
        if (null == count) count = 0; // 默认为0
        redisSessionManager.put(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA, count + 1, CacheConst.DEFAULT_CACHE_TIME);
    }

    public void loginCheckCnt(Long uid) throws BusinessException {
        Long cnt = (Long) RedisUtils.getObject(errCntKey + uid);
        Long frozen = (Long) RedisUtils.getObject(errFrozenKey + uid);
        if (null == cnt) cnt = 0L;
        if (null == frozen) frozen = 0L;
        if (frozen.longValue() > 0L) {
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        if (cnt >= 10) {
            RedisUtils.putObject(errFrozenKey + uid, 1L, CacheConst.THIRTY_MINUTE_CACHE_TIME);
            throw new BusinessException("Your validate code has been wrong over 10 times. The system will unlock your account for 30 minutes.");
        }
        cnt += 1;
        RedisUtils.putObject(errCntKey + uid, Long.valueOf(cnt), CacheConst.ONE_MINUTE_CACHE_TIME);
    }
}
