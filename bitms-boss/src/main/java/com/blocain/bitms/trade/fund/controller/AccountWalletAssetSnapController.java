package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAssetSnap;
import com.blocain.bitms.trade.fund.entity.MarketSnap;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetSnapService;
import com.blocain.bitms.trade.fund.service.MarketSnapService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 账户资产快照
 * <p>File：AccountWalletAssetSnapController.java</p>
 * <p>Title: AccountWalletAssetSnapController</p>
 * <p>Description:AccountWalletAssetSnapController</p>
 * <p>Copyright: Copyright (c) 2018年4月4日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountWalletAssetSnapController extends GenericController
{
    @Autowired(required = false)
    private AccountWalletAssetSnapService accountWalletAssetSnapService;
    
    @Autowired(required = false)
    private MarketSnapService             marketSnapService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/walletAssetSnap")
    @RequiresPermissions("trade:setting:walletAssetSnap:index")
    public String list() throws BusinessException
    {
        return "trade/fund/walletAssetSnap/list";
    }
    
    /**
     * 账户钱包资产快照-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/walletAssetSnap/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletAssetSnap:data")
    public JsonMessage data(AccountWalletAssetSnap entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountWalletAssetSnap> result = accountWalletAssetSnapService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
