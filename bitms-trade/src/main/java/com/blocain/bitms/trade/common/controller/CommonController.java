package com.blocain.bitms.trade.common.controller;

import java.awt.image.BufferedImage;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.AccountToken;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountService;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.RedisSessionManager;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.google.code.kaptcha.Producer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 常规接口控制器
 * <p>File：CommonController.java </p>
 * <p>Title: CommonController </p>
 * <p>Description:CommonController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@Api(description = "常规接口")
@RequestMapping(BitmsConst.COMMON)
public class CommonController extends GenericController
{
    @Autowired
    private Producer            producer;
    
    @Autowired(required = false)
    private AccountService      accountService;
    
    @Autowired(required = false)
    private MsgRecordNoSql msgRecordService;
    
    @Autowired
    private RedisSessionManager redisSessionManager;
    
    /**
     * 生成常规验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/simple/captcha", method = RequestMethod.GET)
    @ApiOperation(value = "生成验证码", httpMethod = "GET", produces = MediaType.IMAGE_JPEG_VALUE)
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Prama", "no-cache");
        response.setContentType("image/jpeg");
        String captext = producer.createText();
        redisSessionManager.remove(request, RedisSessionManager.SessionKey.CAPTCHA);
        redisSessionManager.put(request, RedisSessionManager.SessionKey.CAPTCHA, captext, CacheConst.DEFAULT_CACHE_TIME);
        BufferedImage bi = producer.createImage(captext);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try
        {
            out.flush();
        }
        finally
        {
            out.close();
        }
    }
    
    /**
     * 生成GOOGLE校验码
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @ApiOperation(value = "生成GOOGLE校验码", httpMethod = "POST")
    @RequestMapping(value = "/buildGAKey", method = RequestMethod.POST)
    public JsonMessage buildGASecretKey() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal || StringUtils.isBlank(principal.getUserMail()))
        {// 用户必须登陆
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        Map<String, String> result = Maps.newHashMap();
        String issuer = BitmsConst.PROJECT_DEV_NAME;
        if (BitmsConst.RUNNING_ENVIRONMONT.equalsIgnoreCase("production"))
        {
            issuer = BitmsConst.PROJECT_NAME;
        }
        if (BitmsConst.RUNNING_ENVIRONMONT.equalsIgnoreCase("testing"))
        {
            issuer = BitmsConst.PROJECT_TEST_NAME;
        }
        String secretKey = Authenticator.generateSecretKey();
        result.put("secretKey", secretKey);
        result.put("email", principal.getUserMail());
        result.put("gaInfo", "otpauth://totp/" + principal.getUserMail() + "?secret=" + secretKey + "&issuer=" + issuer);
        return this.getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 发送手机验证码
     * @param phone
     * @param location
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    @ApiOperation(value = "发送手机验证码", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "location", value = "区域代码", required = true, paramType = "form")})
    public JsonMessage sendSms(HttpServletRequest request, String phone, String location, @ModelAttribute AliyunModel model) throws BusinessException
    {
        if (!AliyunUtils.validParams(model))
        {// 验证不通过时
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA);
        }
        if (accountService.checkBindPhone(phone))
        {// 一个手机号只能绑定一个帐号
            throw new BusinessException(AccountEnums.ACCOUNT_PHONE_HAS_BIND);
        }
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        msgRecordService.sendSms(new StringBuffer(location).append(phone).toString(), lang);
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 给已绑定的手机发送验证码
     * <p>
     *     手机号需要帐户表中已绑定的前提下进行
     * </p>
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/bind/sendSms", method = RequestMethod.POST)
    public JsonMessage sendBindSms(HttpServletRequest request) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal)
        {// 判断用户是否已登陆
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if (StringUtils.isBlank(account.getCountry()) || StringUtils.isBlank(account.getMobNo()))
        {// 判断手机号有没有绑定
            throw new BusinessException(AccountEnums.ACCOUNT_PHONE_NOTBIND);
        }
        String lang = account.getLang();
        if (StringUtils.isBlank(lang)) lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        msgRecordService.sendSms(new StringBuffer(account.getCountry()).append(account.getMobNo()).toString(), lang);
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 半登陆状态下发送验证码
     * <p>
     *     手机号需要帐户表中已绑定的前提下进行
     * </p>
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/login/sendSms", method = RequestMethod.POST)
    public JsonMessage sendLoginSms(HttpServletRequest request) throws BusinessException
    {
        Subject subject = SecurityUtils.getSubject();
        StringBuffer key = new StringBuffer(CacheConst.LOGIN_PERFIX).append(subject.getSession().getId());
        AccountToken token = (AccountToken) RedisUtils.getObject(key.toString());
        if (null == token) return this.getJsonMessage(CommonEnums.ERROR_SESSION_TIME_OUT);
        Account account = accountService.selectByPrimaryKey(token.getId());
        if (StringUtils.isBlank(account.getCountry()) || StringUtils.isBlank(account.getMobNo()))
        {// 判断手机号有没有绑定
            throw new BusinessException(AccountEnums.ACCOUNT_PHONE_NOTBIND);
        }
        String lang = account.getLang();
        if (StringUtils.isBlank(lang)) lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        msgRecordService.sendSms(new StringBuffer(account.getCountry()).append(account.getMobNo()).toString(), lang);
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
}
