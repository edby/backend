package com.blocain.bitms.trade.account.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.model.EmailModel;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.maxmind.geoip.Location;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户注册 Introduce
 * <p>File：RegisterController.java</p>
 * <p>Title: RegisterController</p>
 * <p>Description: RegisterController</p>
 * <p>Copyright: Copyright (c) 2017/7/4</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@Api(description = "注册中心")
public class RegisterController extends GenericController
{
    @Autowired(required = false)
    private AccountService accountService;
    
    /**
     * 注册页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ApiOperation(value = "注册页面导航", httpMethod = "GET")
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/register");
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
        return mav;
    }
    
    /**
     * 注册协议页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/register/userAgreement", method = RequestMethod.GET)
    @ApiOperation(value = "注册页面导航", httpMethod = "GET")
    public ModelAndView userAgreement(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/userAgreement");
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
        return mav;
    }
    
    /**
     * 注册提交
     * @param request
     * @param account 账户信息
     * @param model   验证码信息
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/register/submit", method = RequestMethod.POST)
    @ApiOperation(value = "注册提交", httpMethod = "POST")
    public JsonMessage registerSubmit(HttpServletRequest request, @ModelAttribute Account account, @ModelAttribute AliyunModel model) throws BusinessException
    {
        if (!AliyunUtils.validParams(model))
        {// 验证不通过时
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA, Boolean.TRUE);
        }
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        accountService.register(account, lang, IPUtil.getOriginalIpAddr(request));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 注册确认
     * @param uid
     * @param oid
     * @return {@link ModelAndView}
     * @throws BusinessException
     */
    @RequestMapping(value = "/register/confirm", method = RequestMethod.GET)
    @ApiOperation(value = "注册确认", httpMethod = "GET")
    public ModelAndView registerConfirm(HttpServletRequest request, String uid, String oid) throws BusinessException
    {
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(oid)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(uid);
        EmailModel model = (EmailModel) RedisUtils.getObject(cacheKey.toString());
        if (null == model) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        if (!StringUtils.equals(oid, model.getRandomKey())) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        // 避免加速乐CDN随机变换IP
        // if (!StringUtils.equals(IPUtil.getOriginalIpAddr(request), model.getRequestIp())) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        ModelAndView mav = new ModelAndView("account/register_confirm");
        mav.addObject("id", uid);
        mav.addObject("oid", oid);
        return mav;
    }
    
    /**
     * 确认注册提交
     * @param account 账户信息
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/register/confirm/submit", method = RequestMethod.POST)
    @ApiOperation(value = "注册提交", httpMethod = "POST")
    public JsonMessage registerConfirmSubmit(HttpServletRequest request, @ModelAttribute Account account) throws BusinessException
    {
        if (null == account.getId() || null == account.getOid()) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(account.getId());
        EmailModel model = (EmailModel) RedisUtils.getObject(cacheKey.toString());
        // 避免加速乐CDN随机变换IP
        // if (!StringUtils.equals(IPUtil.getOriginalIpAddr(request), model.getRequestIp())) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        if (!StringUtils.equals(String.valueOf(account.getOid()), model.getRandomKey())) throw new BusinessException(CommonEnums.ERROR_SESSION_TIME_OUT);
        account.setEmail(model.getEmail());
        String ipAddr = IPUtil.getOriginalIpAddr(request);
        if (StringUtils.isNotBlank(ipAddr))
        {
            String rigonName = "Unknown address";
            String[] ipArray = ipAddr.split(",");
            for (String ip : ipArray)
            {
                Location location = GeoIPUtils.getInstance().getLocation(ip);
                if (null != location)
                {
                    rigonName = new StringBuilder(location.countryName).append("|").append(location.city).toString();
                }
                break;
            }
            account.setLocation(rigonName);
        }
        String invitCode = model.getInvitCode();
        account.setInvitCode(null != invitCode && !"null".equals(invitCode) ? Long.valueOf(invitCode) : null);
        accountService.registerConfirm(account);
        RedisUtils.del(cacheKey.toString());// 删除临时缓存
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
