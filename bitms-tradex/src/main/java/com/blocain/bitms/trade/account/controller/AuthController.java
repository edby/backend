package com.blocain.bitms.trade.account.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.RedisSessionManager;
import com.blocain.bitms.security.shiro.model.AccountToken;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.consts.AccountLogConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.blocain.bitms.trade.account.service.AccountService;
import com.maxmind.geoip.Location;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private AccountService      accountService;
    
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
        if (null != principal.getLang() && !principal.getLang().equals(locale))
        {
            localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString(principal.getLang()));
        }
        else if (null == principal.getLang())
        {
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
        Account account = accountService.findByName(token.getUsername());
        if(account == null){return this.getJsonMessage(CommonEnums.ERROR_LOGIN_ACCOUNT);}
        if (StringUtils.isNotBlank(account.getAuthKey()))
        {// 如果已绑定GA
            if (StringUtils.isBlank(token.getGaCode()))
            {// 判断用户是否已绑定ga
                return this.getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            else
            {
                String secretKey = EncryptUtils.desDecrypt(account.getAuthKey());
                checkGaErrorCnt(account.getUnid(),secretKey,token.getGaCode());
            }
        }
        if(account.getSecurityPolicy().intValue() != AccountConsts.SECURITY_POLICY_DEFAULT.intValue())
        {
            // 自动修复默认安全策略
            account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_DEFAULT);
            accountService.updateByPrimaryKey(account);
        }
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
        redisSessionManager.remove(request, RedisSessionManager.SessionKey.SHOW_CAPTCHA);

        new Thread(() -> {
            try
            {
                accountService.checkWalletassetTradex(account);
            }
            catch (Exception e)
            {
                logger.error(e.getLocalizedMessage());
            }
            finally
            {
            }
        }).start();
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
