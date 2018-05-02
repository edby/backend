/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetPremium;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetPremiumService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 持仓调节费 控制器
 * <p>File：AccountDebitAssetPremiumController.java</p>
 * <p>Title: AccountDebitAssetPremiumController</p>
 * <p>Description:AccountDebitAssetPremiumController</p>
 * <p>Copyright: Copyright (c) 2018年4月3日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountDebitAssetPremiumController extends GenericController
{
    @Autowired(required = false)
    AccountDebitAssetPremiumService accountDebitAssetPremiumService;
    
    @ResponseBody
    @RequestMapping(value = "/premiumData")
    @ApiOperation(value = "持仓调节费")
    public JsonMessage getData(String timeStart, String timeEnd, @ModelAttribute Pagination pagin) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountDebitAssetPremium accountCandyRecord = new AccountDebitAssetPremium();
        accountCandyRecord.setAccountId(principal.getId());
        if (!StringUtils.isBlank(timeStart))
        {
            accountCandyRecord.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            accountCandyRecord.setTimeEnd(timeEnd + " 23:59:59");
        }
        PaginateResult<AccountDebitAssetPremium> data = accountDebitAssetPremiumService.search(pagin, accountCandyRecord);
        return getJsonMessage(CommonEnums.SUCCESS, data);
    }
}
