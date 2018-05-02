package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountSpotAssetSnap;
import com.blocain.bitms.trade.fund.entity.MarketSnap;
import com.blocain.bitms.trade.fund.service.AccountSpotAssetSnapService;
import com.blocain.bitms.trade.fund.service.MarketSnapService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 账户现货资产快照
 * <p>File：AccountSpotAssetSnapController.java</p>
 * <p>Title: AccountSpotAssetSnapController</p>
 * <p>Description:AccountSpotAssetSnapController</p>
 * <p>Copyright: Copyright (c) 2018年4月4日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountSpotAssetSnapController extends GenericController
{
    @Autowired(required = false)
    private AccountSpotAssetSnapService accountSpotAssetSnapService;
    
    @Autowired(required = false)
    private MarketSnapService           marketSnapService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/spotAssetSnap")
    @RequiresPermissions("trade:setting:spotAssetSnap:index")
    public String list() throws BusinessException
    {
        return "trade/fund/spotAssetSnap/list";
    }
    
    /**
     * 账户钱包资产快照-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/spotAssetSnap/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:spotAssetSnap:data")
    public JsonMessage data(AccountSpotAssetSnap entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountSpotAssetSnap> result = accountSpotAssetSnapService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 行情快照-查询
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/marketSnap/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:spotAssetSnap:data")
    public JsonMessage data() throws BusinessException
    {
        MarketSnap entity = new MarketSnap();
        entity.setPairStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
        List<MarketSnap> list = marketSnapService.findList(entity);
        if(list.size()>0)
        {
            entity = list.get(0);
        }
        return getJsonMessage(CommonEnums.SUCCESS, entity);
    }
}
