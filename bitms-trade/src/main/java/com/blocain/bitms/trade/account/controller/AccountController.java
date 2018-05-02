/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.maxmind.geoip.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.ServletsUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.IPUtil;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.consts.AccountLogConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 账户表 控制器
 * <p>File：AccountController.java </p>
 * <p>Title: AccountController </p>
 * <p>Description:AccountController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
@Api(description = "帐户信息")
public class AccountController extends GenericController
{
    @Autowired(required = false)
    private AccountService              accountService;
    
    @Autowired(required = false)
    private AccountLogNoSql             accountLogNoSql;
    
    @Autowired(required = false)
    private MsgRecordNoSql              msgRecordService;
    
    @Autowired(required = false)
    private AccountPolicyService        accountPolicyService;
    
    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;
    
    @Autowired(required = false)
    private StockInfoService            stockInfoService;
    
    @Autowired(required = false)
    private AccountDebitAssetService    accountDebitAssetService;
    
    /**
     * 账户基本信息页面导航
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/baseInfo")
    @ApiOperation(value = "账户基本信息页面导航", httpMethod = "GET")
    public ModelAndView baseInfo() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/baseInfo");
        mav.addObject("account", accountService.selectByPrimaryKey(OnLineUserUtils.getId()));
        return mav;
    }
    
    /**
     * 帐户设置页面导航
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/setting")
    @ApiOperation(value = "帐户设置页面导航", httpMethod = "GET")
    public ModelAndView setting() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/setting");
        AccountCertification accountCertification = accountCertificationService.findByAccountId(OnLineUserUtils.getId());
        mav.addObject("accountCertification", accountCertification);
        Account account = accountService.selectByPrimaryKey(OnLineUserUtils.getId());
        String mobNo = "";
        if(account!=null && StringUtils.isNotBlank(account.getMobNo())){
            // mobNo =account.getMobNo().substring(0,account.getMobNo().length()-(account.getMobNo().substring(3)).length()) + "****" + account.getMobNo().substring(7);
            mobNo = "****" + account.getMobNo().substring(account.getMobNo().length()-3);
            account.setMobNo(mobNo);
        }
        mav.addObject("account",account);
        return mav;
    }
    
    /**
     * 修改登陆密码
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/changeLoginPwd", method = RequestMethod.POST)
    @ApiOperation(value = "修改登录密码", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "origPass", value = "原始密码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "newPass", value = "新的登陆密码", required = true, paramType = "form")})
    public JsonMessage changeLoginPwd(String origPass, String newPass) throws BusinessException
    {
        if (StringUtils.isBlank(origPass) || StringUtils.isBlank(newPass))
        {// 判断参数
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        accountService.changeLoginPwd(principal.getId(), origPass, newPass);
        /*if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            if (StringUtils.isNotBlank(principal.getUserMobile()))
            {// 确保手机已绑定过
                String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
                String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CHANGE_LOGINPASS_PHONE, principal.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }*/
        if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_CHANGE_LOGINPASS_EMAIL, "en_US", BitmsConst.HOST_EMAIL_LOGO_URL,principal.getUserMail(),
                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        }
        saveOperationLogs(principal, "modify login password");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 修改资金密码
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/changeFundPwd", method = RequestMethod.POST)
    @ApiOperation(value = "修改资金密码", httpMethod = "POST")
    public JsonMessage changeFundPwd(String fundPwd, @ModelAttribute PolicyModel policy) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        accountPolicyService.validSecurityPolicy(account, policy);
        accountService.changeFundPwd(principal.getId(), fundPwd);
       /* if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            if (StringUtils.isNotBlank(principal.getUserMobile()))
            {// 确保手机已绑定过
                String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
                String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CHANGE_FUNDPASS_PHONE, principal.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }*/
        if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_CHANGE_FUNDPASS_EMAIL, "en_US", BitmsConst.HOST_EMAIL_LOGO_URL,principal.getUserMail(),
                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        }
        saveOperationLogs(principal, "modify payment password");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 绑定手机号码
     * @param phone
     * @param location
     * @param vlidCode
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/bindPhone", method = RequestMethod.POST)
    @ApiOperation(value = "绑定手机号码", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "location", value = "所在地", required = true, paramType = "form"),
            @ApiImplicitParam(name = "vlidCode", value = "手机验证码", required = true, paramType = "form")})
    public JsonMessage bindPhone(String phone, String location, String vlidCode) throws BusinessException
    {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(location) || StringUtils.isBlank(vlidCode))
        {// 参数需要验证
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        String mobile = new StringBuffer(location).append(phone).toString();
        if (!msgRecordService.validSMSCode(mobile, vlidCode))
        {// 判断验证码
         // 开始记录操作次数
            String opCountKey = new StringBuffer(CacheConst.OPERATION_COUNT_PREFIX)// 加入缓存前缀
                    .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_ACCOUNT_BIND_PHONE)// 加入模块标识
                    .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
            int count = accountPolicyService.errorOperatorCounter(opCountKey);
            if (count >= BitmsConst.LOCK_INTERVAL_COUNT)
            {// 操作频率达到30次时,锁定用户
                accountService.modifyAccountStatusToFrozen(OnLineUserUtils.getId(), AccountConsts.FROZEN_REASON_BIND_PHONE);
            }
            return getJsonMessage(AccountEnums.ACCOUNT_SMSCODE_ERROR);
        }
        if (accountService.checkBindPhone(phone))
        {// 一个手机号只能绑定一个帐号
            throw new BusinessException(AccountEnums.ACCOUNT_PHONE_HAS_BIND);
        }
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if (null == account || !account.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if (StringUtils.isNotBlank(account.getMobNo()))
        {// 表示当前帐户已绑定过手机号,防止用户串改会话ID来修改绑定的手机
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        account.setMobNo(phone);
        account.setCountry(location);
        account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGAORSMS);
        accountService.updateByPrimaryKey(account);
        saveOperationLogs(principal, "bind phone");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 绑定谷歌认证
     * @param secretKey
     * @param gaCode
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/bindGoogle", method = RequestMethod.POST)
    @ApiOperation(value = "绑定谷歌认证", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "secretKey", value = "Google私钥", required = true, paramType = "form"),
            @ApiImplicitParam(name = "gaCode", value = "GoogleE验证码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "validCode", value = "短信验证码", required = true, paramType = "form")})
    public JsonMessage bindGoogleAuth(String secretKey, String gaCode, String validCode) throws BusinessException
    {
        Authenticator authenticator = new Authenticator();
        if (StringUtils.isBlank(validCode) || StringUtils.isBlank(gaCode) || StringUtils.isBlank(secretKey))
        {// 参数需要验证
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!authenticator.checkCode(secretKey, Long.valueOf(gaCode)))
        {// 判断验证码
            return getJsonMessage(AccountEnums.ACCOUNT_GACODE_ERROR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        StringBuffer buffer = new StringBuffer(account.getCountry()).append(account.getMobNo());
        if (!msgRecordService.validSMSCode(buffer.toString(), validCode))
        {// 手机验证码判断
            return getJsonMessage(AccountEnums.ACCOUNT_SMSCODE_ERROR);
        }
        String cacheKey = new StringBuffer(CacheConst.GOOGLE_CODE_PERFIX)// 加入缓存前缀
                .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_ACCOUNT_BIND_PHONE)// 加入模块标识
                .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
        RedisUtils.putObject(cacheKey, String.valueOf(secretKey), CacheConst.ONE_HOUR_CACHE_TIME);
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 绑定谷歌认证
     * @param secretKey
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/bindGoogle/confirm", method = RequestMethod.POST)
    @ApiOperation(value = "校验谷歌认证", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParam(name = "secretKey", value = "Google私钥", required = true, paramType = "form")
    public JsonMessage bindGoogleConfirm(String secretKey) throws BusinessException
    {
        if (StringUtils.isBlank(secretKey))
        {// 参数需要验证
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        String cacheKey = new StringBuffer(CacheConst.GOOGLE_CODE_PERFIX)// 加入缓存前缀
                .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_ACCOUNT_BIND_PHONE)// 加入模块标识
                .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
        String cacheSecretKey = RedisUtils.get(cacheKey);// 缓存中的GA私钥
        if (!cacheSecretKey.equals(cacheSecretKey))
        {// 判断缓存中的私钥和页面传如的私钥是否匹配，不匹配时抛出异常
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if(StringUtils.isNotBlank(account.getAuthKey()))
        {//判断GA是否已绑定过
            return getJsonMessage(CommonEnums.ERROR_ILLEGAL_REQUEST);
        }
        account.setAuthKey(EncryptUtils.desEncrypt(secretKey));
        account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGA);
        accountService.updateByPrimaryKey(account);
        saveOperationLogs(principal, "bind Google Auth");
        RedisUtils.del(cacheKey);// 清除缓存
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 绑定谷歌认证(一步到位控制器入口)
     * @param secretKey
     * @param gaCode
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/bindGoogleGA", method = RequestMethod.POST)
    @ApiOperation(value = "绑定谷歌认证", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "secretKey", value = "Google私钥", required = true, paramType = "form"),
            @ApiImplicitParam(name = "gaCode", value = "GoogleE验证码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "validCode", value = "短信验证码", required = true, paramType = "form")})
    public JsonMessage bindGoogleGA(String secretKey, String gaCode, String validCode) throws BusinessException
    {
        Authenticator authenticator = new Authenticator();
        if (StringUtils.isBlank(validCode) || StringUtils.isBlank(gaCode) || StringUtils.isBlank(secretKey))
        {// 参数需要验证
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!authenticator.checkCode(secretKey, Long.valueOf(gaCode)))
        {// 判断验证码
            return getJsonMessage(AccountEnums.ACCOUNT_GACODE_ERROR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if(StringUtils.isNotBlank(account.getAuthKey()))
        {//判断GA是否已绑定过
            return getJsonMessage(CommonEnums.ERROR_ILLEGAL_REQUEST);
        }
        StringBuffer buffer = new StringBuffer(account.getCountry()).append(account.getMobNo());
        if (!msgRecordService.validSMSCode(buffer.toString(), validCode))
        {// 手机验证码判断
            return getJsonMessage(AccountEnums.ACCOUNT_SMSCODE_ERROR);
        }
        // 账户实体类更新
        account.setAuthKey(EncryptUtils.desEncrypt(secretKey));
        account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGA); // 安全策略
        accountService.updateByPrimaryKey(account);
        saveOperationLogs(principal, "bind Google Auth");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 换绑手机号码
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/changeBindPhone", method = RequestMethod.POST)
    @ApiOperation(value = "换绑手机号码", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "新手机号码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "vlidCode", value = "新手机验证码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "location", value = "区域代码", required = true, paramType = "form")})
    public JsonMessage changeBindPhone(String phone, String location, String vlidCode, @ModelAttribute PolicyModel policy) throws BusinessException
    {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(vlidCode) || StringUtils.isBlank(location))
        {// 必传参数校验
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        accountPolicyService.validSecurityPolicy(account, policy);
        if (!accountPolicyService.validSMSCode(new StringBuffer(location).append(phone).toString(), vlidCode))
        {// 新手机校验失败
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        account.setMobNo(phone);
        account.setCountry(location);
        accountService.updateByPrimaryKey(account);
        /*if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            if (StringUtils.isNotBlank(principal.getUserMobile()))
            {// 确保手机已绑定过
                String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
                String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CHANGE_PHONE_PHONE, principal.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }*/
       /* if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            if (StringUtils.isNotBlank(principal.getUserMobile())) msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_CHANGE_PHONE_EMAIL,
                    principal.getLang(), principal.getUserMail(), CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        }*/
        saveOperationLogs(principal, "change phone");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 解绑谷歌认证
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/unBindGoogle", method = RequestMethod.POST)
    @ApiOperation(value = "解绑谷歌认证", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage unBindGoogle(@ModelAttribute PolicyModel policy) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        accountPolicyService.validSecurityPolicy(account, policy);
        if (AccountConsts.SECURITY_POLICY_NEEDGAANDSMS.equals(account.getSecurityPolicy()))
        {// 判断用户安全等级，如是同时启用了手GA和短信，此时由系统自动将安全等级降低成或
            account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDGAORSMS);
        }
        else if (AccountConsts.SECURITY_POLICY_NEEDGA.equals(account.getSecurityPolicy()))
        {// 判断用户安全等级，如是启用了GA，此时由系统自动将安全等级降低成短信
            account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDSMS);
        }
        account.setAuthKey(null);// 请空GOOGLE密匙
        accountService.updateByPrimaryKey(account);
        /*if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            if (StringUtils.isNotBlank(principal.getUserMobile()))
            {// 确保手机已绑定过
                String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
                String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CHANGE_GOOGLE_PHONE, principal.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }
        if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_CHANGE_GOOGLE_EMAIL, principal.getLang(), principal.getUserMail(),
                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        }*/
        saveOperationLogs(principal, "unbind Google Auth");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 保存操作日志
     * @param principal
     * @param content
     */
    void saveOperationLogs(UserPrincipal principal, String content)
    {
        try
        {
            if (null == principal) principal = OnLineUserUtils.getPrincipal();
            HttpServletRequest request = ServletsUtils.getRequest();
            AccountLog accountLog = new AccountLog();
            accountLog.setContent(content);
            accountLog.setSystemName(BitmsConst.BITMS_PROJECT_NAME);
            accountLog.setAccountId(principal.getId());
            accountLog.setUrl(request.getRequestURI());
            accountLog.setOpType(AccountLogConsts.LOG_TYPE_SETTING);
            accountLog.setAccountName(principal.getTrueName());
            accountLog.setIpAddr(IPUtil.getOriginalIpAddr(request));
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
        catch (RuntimeException e)
        {
            logger.error("操作日志记录失败：{}", e.getCause());
        }
    }
    
    /**
     * 修改自动借贷默认策略
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/changeBorrowSwitch", method = RequestMethod.POST)
    @ApiOperation(value = "修改自动借贷默认策略", httpMethod = "POST")
    public JsonMessage changeBorrowSwitch(Integer autoDebit) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_USD_TYPE);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        account.setAutoDebit(autoDebit);
        AccountDebitAsset entity = new AccountDebitAsset();
        entity.setTableName(stockInfo.getTableDebitAsset());
        entity.setBorrowerAccountId(principal.getId());
        entity.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
        List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
        if (list.size() > 0)
        {
            entity = list.get(0);
            logger.debug("存在借款 不能关闭");
            if (autoDebit.intValue() == 0) { throw new BusinessException(CommonEnums.FAIL, entity.getStockCode()); }
        }
        accountService.updateByPrimaryKeySelective(account);
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
}
