/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.account.controller;

import java.util.Collection;
import java.util.List;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.boss.system.entity.RoleInfo;
import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.CustomSessionManager;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.TradeAuthorizingRealm;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import oracle.sql.NUMBER;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import org.springframework.web.servlet.ModelAndView;

/**
 * 账户查询和审核
 * <p>File：AccountController.java</p>
 * <p>Title: AccountController</p>
 * <p>Description:AccountController</p>
 * <p>Copyright: Copyright (c) 2017年7月5日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
public class AccountController extends GenericController
{
    @Autowired(required = false)
    private AccountService  accountService;

    @Autowired(required = false)
    private UserInfoService userInfoService;
    // 凭证ID
    private static final String  PRINCIPALS_ID = "principalId";
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(BitmsConst.ACCOUNT)
    @RequiresPermissions("trade:setting:account:index")
    public String list() throws BusinessException
    {
        return "trade/account/account/list";
    }
    
    /**
     * 账户-查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/account/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:account:data")
    public JsonMessage data(Account entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<Account> result = accountService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 账户-审核通过(解冻)
     * @param id
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/account/pass", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:account:operator")
    public JsonMessage pass(Long id) throws BusinessException
    {
        Account account = accountService.selectByPrimaryKey(id);
        if (null == account)
        {
            logger.info("账户信息 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        account.setStatus(AccountConsts.ACCOUNT_STATUS_NORMAL);
        accountService.updateByPrimaryKey(account);
       /* if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            if (StringUtils.isNotBlank(account.getMobNo()))
            {// 确保手机已绑定过
                String vagueMobile = StringUtils.vagueMobile(account.getMobNo());
                String mobile = new StringBuffer(account.getCountry()).append(account.getMobNo()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_THAW_PHONE, account.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }
        if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            msgRecordService.sendRemindEmail(account.getEmail(), MessageConst.REMIND_THAW_EMAIL, "en_US", account.getEmail(),
                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        }*/
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    /**
     * 清理其它用户，保障一个帐户同时只能登陆一个
     * @param accountId
     */
    void cleanOtherUsers(Long accountId)
    {
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        CustomSessionManager sessionManager = (CustomSessionManager) securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        for (Session session : sessions)
        {
            if (accountId.equals(Long.valueOf(String.valueOf(session.getAttribute(PRINCIPALS_ID)))))
            { // 清除该用户以前登录时保存的session
                sessionManager.getSessionDAO().delete(session);
            }
        }
    }

    /**
     * 账户-审核不通过（冻结）
     * @param id
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/account/nopass", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:account:operator")
    public JsonMessage nopass(Long id) throws BusinessException
    {
        Account account = accountService.selectByPrimaryKey(id);
        if (null == account)
        {
            logger.info("账户信息 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        account.setStatus(AccountConsts.ACCOUNT_STATUS_FROZEN);
        accountService.updateByPrimaryKey(account);
        cleanOtherUsers(id);
        /*if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 短信提醒
            if (StringUtils.isNotBlank(account.getMobNo()))
            {// 确保手机已绑定过
                String vagueMobile = StringUtils.vagueMobile(account.getMobNo());
                String mobile = new StringBuffer(account.getCountry()).append(account.getMobNo()).toString();
                msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_FROZEN_PHONE, account.getLang(), vagueMobile,
                        CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
            }
        }*/
        /*if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
        {// 邮件提醒
            msgRecordService.sendRemindEmail(account.getEmail(), MessageConst.REMIND_FROZEN_EMAIL, "en_US",account.getEmail(),
                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
        }*/
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 关闭账户（关闭）
     * @param id
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/account/close", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:account:operator")
    public JsonMessage closeAccount(Long id) throws BusinessException
    {
        Account account = accountService.selectByPrimaryKey(id);
        if (null == account)
        {
            logger.info("账户信息 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        account.setStatus(AccountConsts.ACCOUNT_STATUS_CLOSE);
        accountService.updateByPrimaryKey(account);
        cleanOtherUsers(id);
        return getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
     * 解绑GA界面
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/account/unbindga")
    @RequiresPermissions("trade:setting:account:operator")
    public ModelAndView unbindga(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/account/account/unbindga");
        Account user = null;
        if (null != id)
        {
            user = accountService.selectByPrimaryKey(id);
        }
        mav.addObject("user", user);
        return mav;
    }

    /**
     *  解绑GA操作
     * @param info
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequiresPermissions("trade:setting:account:operator")
    @RequestMapping(value = "/account/clearga", method = RequestMethod.POST)
    public JsonMessage saveunGa(Account info, String gaCode) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        UserInfo operateUser = userInfoService.selectByPrimaryKey(principal.getId());
        if(operateUser==null || StringUtils.isBlank(operateUser.getAuthKey()))
        {
            throw new BusinessException("你未绑定GA");
        }
        String secretKey = EncryptUtils.desDecrypt(operateUser.getAuthKey());
        Authenticator authenticator = new Authenticator();
        if ( StringUtils.isBlank(gaCode) || StringUtils.isBlank(secretKey))
        {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!authenticator.checkCode(secretKey, Long.valueOf(gaCode)))
        {// 判断验证码
            return getJsonMessage(AccountEnums.ACCOUNT_GACODE_ERROR);
        }
        Account userinfo = accountService.selectByPrimaryKey(info.getId());
        userinfo.setAuthKey(null);
        userinfo.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDSMS);
        accountService.save(userinfo);
        return getJsonMessage(CommonEnums.SUCCESS);
    }


    /**
     * 查询
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/account/all", method = RequestMethod.GET)
    @RequiresPermissions("trade:setting:account:operator")
    public List<Account> all() throws BusinessException
    {
        Account account = new Account();
        account.setStatus(AccountConsts.ACCOUNT_STATUS_NORMAL);
        List<Account> list = accountService.findList(account);
        return list;
    }
}
