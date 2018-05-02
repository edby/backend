/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;


/**
 * 账户钱包资产
 * <p>File：AccountContractAssetController.java</p>
 * <p>Title: AccountContractAssetController</p>
 * <p>Description:AccountContractAssetController</p>
 * <p>Copyright: Copyright (c) 2017年10月24日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountWalletAssetController extends GenericController
{
    @Autowired(required = false)
    private AccountWalletAssetService accountWalletAssetService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/walletAsset")
    @RequiresPermissions("trade:setting:walletAsset:index")
    public String list() throws BusinessException
    {
        return "trade/fund/walletAsset/list";
    }
    
    /**
     * 账户钱包资产-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/walletAsset/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletAsset:data")
    public JsonMessage data(AccountWalletAsset entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountWalletAsset> result = accountWalletAssetService.selectAll(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
