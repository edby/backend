package com.blocain.bitms.trade.account.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.AliyunModel;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 忘记密码控制器 Introduce
 * <p>File：ForgotPassController.java</p>
 * <p>Title: ForgotPassController</p>
 * <p>Description: ForgotPassController</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@Api(description = "找回密码")
public class ForgetPassController extends GenericController
{
    @Autowired(required = false)
    private AccountService   accountService;
    
    @Autowired(required = false)
    private MsgRecordNoSql msgRecordService;
    
    /**
     * 忘记密码导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/forgetPass")
    @ApiOperation(value = "忘记密码导航", httpMethod = "GET")
    public ModelAndView forgotPass(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/forget");
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
     * 找回密码版块发送手机验证码
     * @param request
     * @param phone
     * @param email
     * @param model
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping("/forgetPass/sendSMS")
    @ApiOperation(value = "发送短信", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "邮箱地址", required = true, paramType = "form")})
    public JsonMessage sendSMS(HttpServletRequest request, String phone, String email, @ModelAttribute AliyunModel model) throws BusinessException
    {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(phone))
        {// 校验参数
            return this.getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!AliyunUtils.validParams(model))
        {// 验证不通过时
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA);
        }
        Account account = accountService.findByEmailAndMob(email.toLowerCase(), phone);
        if (null == account)
        {//
            errorCounter(request);
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
        lang = "en_US";
        msgRecordService.sendSms(new StringBuffer(account.getCountry()).append(account.getMobNo()).toString(), lang);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 错误统计
     * @param request
     * @throws BusinessException
     */
    void errorCounter(HttpServletRequest request) throws BusinessException
    {
        String accountLockKey = new StringBuffer(CacheConst.ACCOUNT_LOCK_PREFIX)// 加入缓存前缀
                .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FINDPWD)// 加入模块标识
                .append(BitmsConst.SEPARATOR).append(IPUtil.getOriginalIpAddr(request)).toString();
        String cacheHost = RedisUtils.get(accountLockKey);
        if (StringUtils.isNotBlank(cacheHost) && StringUtils.equalsIgnoreCase(cacheHost, IPUtil.getOriginalIpAddr(request)))
        {// 锁定24小时的IP不允许找回密码
            throw new BusinessException("ip locked");
        }
        // 开始记录操作次数
        int count = 1;
        String opCountKey = new StringBuffer(CacheConst.OPERATION_COUNT_PREFIX)// 加入缓存前缀
                .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FINDPWD)// 加入模块标识
                .append(BitmsConst.SEPARATOR).append(IPUtil.getOriginalIpAddr(request)).toString();
        String opTimes = RedisUtils.get(opCountKey);
        if (StringUtils.isNotBlank(opTimes))
        {// 表示操作记数缓存中已经存在
            count = Integer.valueOf(opTimes) + 1;
            if (count >= BitmsConst.LOCK_INTERVAL_COUNT)
            {// 操作频率达到30次时,锁定用户
                RedisUtils.putObject(accountLockKey, IPUtil.getOriginalIpAddr(request), CacheConst.TWENTYFOUR_HOUR_CACHE_TIME);
            }
        }
        RedisUtils.putObject(opCountKey, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
    }
    
    /**
     * 发送邮件验证
     * @param request
     * @param email
     * @param model
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping("/forgetPass/submit")
    @ApiOperation(value = "发送邮件验证", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "email", value = "邮箱地址", required = true, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "vlidCode", value = "验证码", required = true, paramType = "form")})
    public JsonMessage submit(HttpServletRequest request, String email, String phone, String vlidCode, @ModelAttribute AliyunModel model) throws BusinessException
    {
        if (StringUtils.isBlank(email))
        {// 校验参数,邮件参数必须校验
            return this.getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        Account account = accountService.findByNameAndNormal(email.toLowerCase());
        if (null == account)
        {// 判断帐户是否存在
            errorCounter(request);
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        if (!AliyunUtils.validParams(model))
        {// 验证不通过时
            return this.getJsonMessage(CommonEnums.ERROR_LOGIN_CAPTCHA);
        }
        if (StringUtils.isBlank(phone) && StringUtils.isNotBlank(account.getMobNo()))
        {// 判断用户是否已绑定手机
            return this.getJsonMessage(AccountEnums.ACCOUNT_PHONE_TIPS);
        }
        if (StringUtils.isNotBlank(account.getMobNo()))
        {// 已绑定手机号的帐号处理
            StringBuffer buffer = new StringBuffer(account.getCountry()).append(account.getMobNo());
            if (!msgRecordService.validSMSCode(buffer.toString(), vlidCode))
            {// 判断用户输入的验证码与缓存中的验证码
                errorCounter(request);
                return this.getJsonMessage(CommonEnums.ERROR_AUTHER_FAILED);
            }
            request.getSession().setAttribute("email", email);
            String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
            if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
            lang = "en_US";
            msgRecordService.sendEmailForgetPasswordCode(email, lang);
        }
        else
        {
            request.getSession().setAttribute("email", email);
            String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
            if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
            lang = "en_US";
            msgRecordService.sendEmailForgetPasswordCode(email, lang);
        }
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 忘记密码确认页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/forgetPass/confirm")
    @ApiOperation(value = "忘记密码确认页面导航", httpMethod = "GET")
    public ModelAndView confirm(HttpServletRequest request) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/forget_confirm");
        String email = (String) request.getSession().getAttribute("email");
        String hostName = "http://mail." + email.substring(email.indexOf("@") + 1, email.length());
        mav.addObject("email", email);
        mav.addObject("email_host", hostName);
        return mav;
    }
    
    /**
     * 重置密码导航
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/resetPass")
    @ApiOperation(value = "重置密码导航", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "unid", value = "帐户编号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "op_id", value = "操作ID", required = true, paramType = "form")})
    public ModelAndView resetPass(HttpServletRequest request) throws BusinessException
    {
        String unid = request.getParameter("unid");
        String oid = request.getParameter("op_id");
        if (StringUtils.isBlank(unid) || StringUtils.isBlank(oid))
        {// 判断会话和unid
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(unid);
        String sessionId = RedisUtils.get(cacheKey.toString());
        if (StringUtils.isBlank(sessionId) || !StringUtils.equalsIgnoreCase(sessionId, oid))
        {// 判断会话是否超时或匹配
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        ModelAndView mav = new ModelAndView("account/reset");
        mav.addObject("unid", unid);
        mav.addObject("oid", oid);
        return mav;
    }
    
    /**
     * 保存密码
     * @param account
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping("/resetPass/savePass")
    @ApiOperation(value = "保存密码", httpMethod = "POST")
    public JsonMessage savePass(@ModelAttribute Account account) throws BusinessException
    {
        if (null == account.getUnid() || null == account.getOid() || StringUtils.isBlank(account.getLoginPwd()))
        {//
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!ValidateUtils.isRegex(account.getLoginPwd(), 8, 32, true))
        {// 限制密规则
            throw new BusinessException(CommonEnums.ERROR_LOGIN_PASSWORD);
        }
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(account.getUnid());
        String sessionId = RedisUtils.get(cacheKey.toString());
        if (!StringUtils.equalsIgnoreCase(sessionId, String.valueOf(account.getOid())))
        {// 判断验证码
            throw new BusinessException(CommonEnums.ERROR_SESSION_TIME_OUT);
        }
        accountService.resetPass(account);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
