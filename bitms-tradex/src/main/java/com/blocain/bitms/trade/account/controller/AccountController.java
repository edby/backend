/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.controller;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
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
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.IPUtil;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountLogConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.blocain.bitms.trade.account.service.AccountService;
import com.google.common.collect.Maps;
import com.maxmind.geoip.Location;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import reactor.bus.Bus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
@Api(description = "帐户信息")
public class AccountController extends GenericController
{
    @Autowired(required = false)
    private AccountService  accountService;
    
    @Autowired(required = false)
    private AccountLogNoSql accountLogNoSql;
    
    @Autowired(required = false)
    private MsgRecordNoSql  msgRecordService;
    
    /**
     * 账户基本信息页面导航
     *
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/changeLoginPwd")
    @ApiOperation(value = "修改登录密码页面导航", httpMethod = "GET")
    public ModelAndView changeLoginPwd() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/changePwd");
        mav.addObject("account", accountService.selectByPrimaryKey(OnLineUserUtils.getId()));
        return mav;
    }
    
    /**
     * 修改登陆密码
     *
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
        /*
         * if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
         * {// 短信提醒
         * if (StringUtils.isNotBlank(principal.getUserMobile()))
         * {// 确保手机已绑定过
         * String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
         * String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
         * msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CHANGE_LOGINPASS_PHONE, principal.getLang(), vagueMobile,
         * CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
         * }
         * }
         */
      /*  if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_CHANGE_LOGINPASS_EMAIL, "en_US", BitmsConst.HOST_EMAIL_LOGO_URL,
                    principal.getUserMail(), CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        }*/
        // 退出
        Subject subject = SecurityUtils.getSubject();
        if (null != subject)
        {
            subject.logout();
        }
        saveOperationLogs(principal, "modify login password");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 绑定ga页面导航
     *
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/bindGA")
    @ApiOperation(value = "绑定GA页面导航", httpMethod = "GET")
    public ModelAndView bindGA() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/bindGA");
        mav.addObject("account", accountService.selectByPrimaryKey(OnLineUserUtils.getId()));
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal || StringUtils.isBlank(principal.getUserMail()))
        {// 用户必须登陆
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        Map<String, String> result = Maps.newHashMap();
        String issuer = BitmsConst.PROJECT_BIEX_DEV_NAME;
        if (BitmsConst.RUNNING_ENVIRONMONT.equalsIgnoreCase("production"))
        {
            issuer = BitmsConst.PROJECT_BIEX_NAME;
        }
        if (BitmsConst.RUNNING_ENVIRONMONT.equalsIgnoreCase("testing"))
        {
            issuer = BitmsConst.PROJECT_BIEX_TEST_NAME;
        }
        String secretKey = Authenticator.generateSecretKey();
        result.put("secretKey", secretKey);
        result.put("email", principal.getUserMail());
        result.put("gaInfo", "otpauth://totp/" + principal.getUserMail() + "?secret=" + secretKey + "&issuer=" + issuer);
        mav.addObject("gaMap", result);
        mav.addObject("bindStatus", false);
        return mav;
    }
    
    /**
     * 绑定谷歌认证
     *
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
            @ApiImplicitParam(name = "gaCode", value = "GoogleE验证码", required = true, paramType = "form")})
    public JsonMessage bindGoogleGA(String secretKey, String gaCode) throws BusinessException
    {
        Authenticator authenticator = new Authenticator();
        if (StringUtils.isBlank(gaCode) || StringUtils.isBlank(secretKey))
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
        if (StringUtils.isNotBlank(account.getAuthKey()))
        {// 判断GA是否已绑定过
            return getJsonMessage(CommonEnums.ERROR_ILLEGAL_REQUEST);
        }
        // 账户实体类更新
        account.setAuthKey(EncryptUtils.desEncrypt(secretKey));
        accountService.updateByPrimaryKey(account);
        OnLineUserUtils.getPrincipal().setAuthKey(EncryptUtils.desEncrypt(secretKey));
        saveOperationLogs(principal, "bind Google Auth");
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    @RequestMapping("/unbindGA")
    @ApiOperation(value = "解绑GA页面导航", httpMethod = "GET")
    public ModelAndView unbindGA() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/unbindGA");
        Account account = accountService.selectByPrimaryKey(OnLineUserUtils.getId());
        mav.addObject("account", account);
        return mav;
    }
    
    /**
     * 解绑谷歌认证
     *
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/setting/unBindGoogle", method = RequestMethod.POST)
    @ApiOperation(value = "解绑谷歌认证", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage unBindGoogle(String gaCode) throws BusinessException
    {
        // 用户没登录
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if (null == account) throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        // ga没绑定
        if (StringUtils.isBlank(account.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
        String secretKey = EncryptUtils.desDecrypt(account.getAuthKey());
        if (StringUtils.isBlank(gaCode)) { return getJsonMessage(CommonEnums.PARAMS_VALID_ERR); }
        checkGaErrorCnt(account.getUnid(),secretKey,gaCode);
        account.setAuthKey(null);// 请空GOOGLE密匙
        accountService.updateByPrimaryKey(account);
        /*
         * if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
         * {// 短信提醒
         * if (StringUtils.isNotBlank(principal.getUserMobile()))
         * {// 确保手机已绑定过
         * String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
         * String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
         * msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CHANGE_GOOGLE_PHONE, principal.getLang(), vagueMobile,
         * CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
         * }
         * }
         * if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
         * {// 邮件提醒
         * msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_CHANGE_GOOGLE_EMAIL, principal.getLang(), principal.getUserMail(),
         * CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
         * }
         */
        saveOperationLogs(principal, "unbind Google Auth");
        OnLineUserUtils.getPrincipal().setAuthKey(null);
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 保存操作日志
     *
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
                for (String ip : ipArray)
                {
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
}