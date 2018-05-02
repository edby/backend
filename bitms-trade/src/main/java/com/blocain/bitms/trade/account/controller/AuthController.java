package com.blocain.bitms.trade.account.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.RedisSessionManager;
import com.blocain.bitms.security.exception.AccountPolicyException;
import com.blocain.bitms.security.shiro.model.AccountToken;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountLogConsts;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.maxmind.geoip.Location;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 基础权限认证 Introduce
 * <p>File：LoginController.java </p>
 * <p>Title: LoginController </p>
 * <p>Description:LoginController </p>
 * <p>Copyright: Copyright (c) 17/6/21</p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@Api(description = "权限认证")
public class AuthController extends GenericController
{
    @Autowired(required = false)
    private RedisSessionManager redisSessionManager;
    
    @Autowired(required = false)
    private AccountLogNoSql     accountLogNoSql;
    
    @Autowired(required = false)
    private MsgRecordNoSql      msgRecordService;
    
    /**
     *后台首页
     * @return {@link ModelAndView}
     * @throws BusinessException
     */
    @RequestMapping("/dispatch")
    @ApiOperation(value = "后台首页导航", httpMethod = "GET")
    public ModelAndView dispatch(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("index");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        String locale = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if(null != principal.getLang() && !principal.getLang().equals(locale)){
            localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString(principal.getLang()));
        } else if(null == principal.getLang()){
            localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString("en_US"));
        }
        return mav;
    }
    
    /**
     * 登陆页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/login")
    @ApiOperation(value = "登陆导航", httpMethod = "GET")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        if (SecurityUtils.getSubject().isAuthenticated())
        {// 如果登陆过就直接进入后台
            return new ModelAndView("redirect:/dispatch");
        }
        String locale = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(locale) && null != localeResolver)
        {
            locale = LanguageUtils.getLang(request);
            if (locale.length() <= 2)
            {
                if (locale.equalsIgnoreCase("zh")) locale = "zh_CN";
                else locale = "en_US";
            }
            localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString(locale));
        }
        ModelAndView mav = new ModelAndView("account/login");
        Integer showCaptcha = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
        mav.addObject(RedisSessionManager.SessionKey.SHOW_CAPTCHA.getValue(), null != showCaptcha && showCaptcha > 2 ? true : false);
        return mav;
    }
    
    /**
     * 用户登陆认证
     * @param request
     * @param token
     * @param model
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping("/login/submit")
    @ApiOperation(value = "登陆", httpMethod = "POST")
    public JsonMessage loginSubmit(HttpServletRequest request, @ModelAttribute AccountToken token, @ModelAttribute AliyunModel model) throws BusinessException
    {
        if (SecurityUtils.getSubject().isAuthenticated())
        {// 如果登陆过就直接进入后台
            return this.getJsonMessage(CommonEnums.SUCCESS);
        }
        if (StringUtils.isNotBlank(model.getCsessionid()) || StringUtils.isNotBlank(model.getScene()))
        { // 判断验证码
            if (!AliyunUtils.validParams(model))
            {// 验证不通过时
                return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA, Boolean.TRUE);
            }
        }
        token.setHost(IPUtil.getOriginalIpAddr(request));
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            saveOperationLogs(request, OnLineUserUtils.getPrincipal(), "登入");
        }
        catch (IncorrectCredentialsException ice)
        {
            logLoginTimes(request);
            Integer showCaptcha = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_PASSWORD, null != showCaptcha && showCaptcha > 2 ? true : false);
        }
        catch (UnknownAccountException uae)
        {
            logLoginTimes(request);
            Integer showCaptcha = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_ACCOUNT, null != showCaptcha && showCaptcha > 2 ? true : false);
        }
        catch (LockedAccountException uae)
        {
            logLoginTimes(request);
            Integer showCaptcha = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_LOCK, null != showCaptcha && showCaptcha > 2 ? true : false);
        }
        catch (ExcessiveAttemptsException eae)
        {
            logLoginTimes(request);
            Integer showCaptcha = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_TIMEOUT, null != showCaptcha && showCaptcha > 2 ? true : false);
        }
        catch (AccountPolicyException gae)
        {
            redisSessionManager.remove(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
            StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append(subject.getSession().getId());
            RedisUtils.putObject(key.toString(), token, CacheConst.DEFAULT_CACHE_TIME);
            return this.getJsonMessage(CommonEnums.NEED_POLICY_CHECK);
        }
        redisSessionManager.remove(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
        // if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        // {// 短信提醒
        // UserPrincipal principal = OnLineUserUtils.getPrincipal();
        // if (StringUtils.isNotBlank(principal.getUserMobile()))
        // {// 确保手机已绑定过
        // String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
        // String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
        // msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_LOGIN_PHONE, principal.getLang(), vagueMobile,
        // CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        // }
        // }
        String ip = NetworkUtils.getRemortIp(request);
        if(StringUtils.isNotBlank(ip))
        {
            ip = ip.split(",")[0];
        }
        String userAgent = request.getHeader("User-Agent");
        String userAgentStrng = UserAgentUtils.getUserAgentInfo(userAgent);
        if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            UserPrincipal principal = OnLineUserUtils.getPrincipal();
            msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_LOGIN_EMAIL, "en_US", BitmsConst.HOST_EMAIL_LOGO_URL, principal.getUserMail(),
                    userAgentStrng, CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS), ip);
        }
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 登陆页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/login/check")
    @ApiOperation(value = "登陆导航", httpMethod = "GET")
    public ModelAndView check() throws BusinessException
    {
        Subject subject = SecurityUtils.getSubject();
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append(subject.getSession().getId());
        AccountToken token = (AccountToken) RedisUtils.getObject(key.toString());
        if (null == token) return new ModelAndView("redirect:/login");
        ModelAndView mav = new ModelAndView("account/loginGA");
        String mobNo = "";
        if(token!=null && StringUtils.isNotBlank(token.getMobNo())){
            // mobNo = token.getMobNo().substring(0,token.getMobNo().length()-(token.getMobNo().substring(3)).length()) + "****" + token.getMobNo().substring(7);
            mobNo = "****" + token.getMobNo().substring(token.getMobNo().length()-3);
            token.setMobNo(mobNo);
        }
        mav.addObject("token", token);
        return mav;
    }
    
    /**
     * 用户登陆认证
     * @param request
     * @param policy
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping("/login/check/submit")
    @ApiOperation(value = "登陆", httpMethod = "POST")
    public JsonMessage checkSubmit(HttpServletRequest request, @ModelAttribute PolicyModel policy) throws BusinessException
    {
        Subject subject = SecurityUtils.getSubject();
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append(subject.getSession().getId());
        AccountToken token = (AccountToken) RedisUtils.getObject(key.toString());
        if (null == token) return this.getJsonMessage(CommonEnums.ERROR_SESSION_TIME_OUT);
        if (null == policy) return this.getJsonMessage(CommonEnums.NEED_POLICY_CHECK);
        token.setPolicy(policy);
        token.setHost(IPUtil.getOriginalIpAddr(request));
        try
        {
            subject.login(token);
            saveOperationLogs(request, OnLineUserUtils.getPrincipal(), "登入");
        }
        catch (AccountPolicyException gae)
        {
            return this.getJsonMessage(CommonEnums.ERROR_AUTHER_FAILED);
        }
        RedisUtils.del(key.toString());
        // if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        // {// 短信提醒
        // UserPrincipal principal = OnLineUserUtils.getPrincipal();
        // if (StringUtils.isNotBlank(principal.getUserMobile()))
        // {// 确保手机已绑定过
        // String vagueMobile = StringUtils.vagueMobile(principal.getUserMobile());
        // String mobile = new StringBuffer(principal.getCountry()).append(principal.getUserMobile()).toString();
        // msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_LOGIN_PHONE, principal.getLang(), vagueMobile,
        // CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        // }
        // }
        String ip = NetworkUtils.getRemortIp(request);
        if(StringUtils.isNotBlank(ip))
        {
            ip = ip.split(",")[0];
        }
        String userAgent = request.getHeader("User-Agent");
        String userAgentStrng = UserAgentUtils.getUserAgentInfo(userAgent);
        if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            UserPrincipal principal = OnLineUserUtils.getPrincipal();
            msgRecordService.sendRemindEmail(principal.getUserMail(), MessageConst.REMIND_LOGIN_EMAIL, "en_US", BitmsConst.HOST_EMAIL_LOGO_URL, principal.getUserMail(),
                    userAgentStrng, CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS), ip);
        }
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 退出认证
     * @return {@link String}
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(BitmsConst.COMMON + "/logout")
    @ApiOperation(value = "退出", httpMethod = "POST")
    public JsonMessage logout(HttpServletRequest request) throws Exception
    {
        Subject subject = SecurityUtils.getSubject();
        if (null != subject)
        {
            saveOperationLogs(request, OnLineUserUtils.getPrincipal(), "登出");
            subject.logout();
        }
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(CommonEnums.SUCCESS.getCode());
        jsonMessage.setMessage(CommonEnums.SUCCESS.getMessage());
        return jsonMessage;
    }
    
    /**
     * 记录登陆出错的次数
     * @param request
     */
    void logLoginTimes(HttpServletRequest request)
    {
        Integer count = redisSessionManager.getInteger(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);
        if (null == count) count = 0; // 默认为0
        redisSessionManager.put(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA, count + 1, CacheConst.DEFAULT_CACHE_TIME);
    }
    
    /**
     * 保存操作日志
     * @param request
     * @param principal
     * @param content
     */
    void saveOperationLogs(HttpServletRequest request, UserPrincipal principal, String content)
    {
        try
        {
            if (null == principal) return;
            AccountLog accountLog = new AccountLog();
            accountLog.setContent(content);
            accountLog.setAccountId(principal.getId());
            accountLog.setUrl(request.getRequestURI());
            accountLog.setSystemName(BitmsConst.BITMS_PROJECT_NAME);
            accountLog.setOpType(AccountLogConsts.LOG_TYPE_LOGIN);
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
}
