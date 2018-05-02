/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.account.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.AccountInvitation;
import com.blocain.bitms.trade.account.service.AccountInvitationService;
import com.blocain.bitms.trade.fund.service.FundCurrentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 账户邀请记录
 * <p>File：AccountInvitationController.java</p>
 * <p>Title: AccountInvitationController</p>
 * <p>Description:AccountInvitationController</p>
 * <p>Copyright: Copyright (c) 2017年7月20日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
public class AccountInvitationController extends GenericController
{
    public static final Logger        logger = LoggerFactory.getLogger(AccountInvitationController.class);

    @Autowired(required = false)
    private AccountInvitationService  accountInvitationService;

    @Autowired(required = false)
    private FundCurrentService        fundCurrentService;

    /**
     * 账户邀请记录查询-页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/invitation")
    @RequiresPermissions("trade:setting:accountinvitation:index")
    public String list() throws BusinessException
    {
        return "trade/account/invitation/list";
    }

    /**
     * 账户邀请记录查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/invitation/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountinvitation:data")
    public JsonMessage data(AccountInvitation entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountInvitation> result = accountInvitationService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 奖励发放
     * @param id
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/invitation/doGrant", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountinvitation:operator")
    public JsonMessage doGrant(Long id, BigDecimal bmsNum) throws BusinessException
    {
        logger.info("id=" + id);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        // fundCurrentService.doReward(id, bmsNum,principal.getId());
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
