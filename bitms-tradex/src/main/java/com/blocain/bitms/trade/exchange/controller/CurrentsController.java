/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.exchange.controller;

import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.common.service.DictionaryService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.CookieUtils;
import com.blocain.bitms.tools.utils.ExportExcel;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Fund资金流水  控制器
 * <p>File：CurrentsController.java</p>
 * <p>Title: CurrentsController</p>
 * <p>Description:CurrentsController</p>
 * <p>Copyright: Copyright (c) 2018-03-28</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.EXCHANGE)
@Api(description = "Fund资金流水")
public class CurrentsController extends GenericController
{
    public static final Logger        logger = LoggerFactory.getLogger(CurrentsController.class);
    
    @Autowired(required = false)
    private AccountFundCurrentService accountFundCurrentService;
    
    @Autowired(required = false)
    private Erc20TokenService         erc20TokenService;
    
    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    /**
     * Fund财务流水列表
     * @param accountFundCurrent
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/currents/financialCurrentsList", method = RequestMethod.GET)
    @ApiOperation(value = "Fund资金流水列表", httpMethod = "GET")
    public JsonMessage financialCurrentsList(String isHis, String timeStart,String isCapital,  String timeEnd, @ModelAttribute AccountFundCurrent accountFundCurrent,
            @ModelAttribute Pagination pagin, String contractAddr) throws BusinessException
    {
        if(isCapital == null)isCapital = "no";
        // 证券信息ID判断s
        if(StringUtils.isNotBlank(contractAddr))
        {
            StockInfo stockInfo = getStockInfo(contractAddr);
            accountFundCurrent.setStockinfoId(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(isCapital,"yes")?stockInfo.getCapitalStockinfoId():stockInfo.getTradeStockinfoId());
        }
        boolean isHisValue = StringUtils.equalsIgnoreCase(isHis, "yes");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountFundCurrent.setAccountId(principal.getId());
        if (!StringUtils.isBlank(timeStart))
        {
            accountFundCurrent.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            accountFundCurrent.setTimeEnd(timeEnd + " 23:59:59");
        }
        accountFundCurrent.setTableName(isHisValue ? getStockInfo(contractAddr).getTableFundCurrentHis() : getStockInfo(contractAddr).getTableFundCurrent());
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.findListByAccount(pagin, accountFundCurrent, FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE,
                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE_SDF, FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW,
                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL, FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT,
                FundConsts.SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN, FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET, FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT,
                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT, FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET, FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH,
                FundConsts.SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT, FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD,
                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB, FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD,
                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB, FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }


    /**
     * Fund 邀请奖励流水
     * @param
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2018-04-16 19:51:05
     */
    @ResponseBody
    @RequestMapping(value = "/currents/awardCurrentsList", method = RequestMethod.GET)
    @ApiOperation(value = "邀请奖励流水", httpMethod = "GET")
    public JsonMessage awardCurrentsList( @ModelAttribute Pagination pagin) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        Erc20Token erc20Token = new Erc20Token();
        erc20Token.setInviteAccountId(principal.getId());
        PaginateResult<Erc20Token> result = erc20TokenService.findListForAward(erc20Token);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    /**
     * Fund资金流水列表
     * @param accountFundCurrent
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/currents/currentsList", method = RequestMethod.GET)
    @ApiOperation(value = "Fund资金流水列表", httpMethod = "GET")
    public JsonMessage currentsList(String isHis, String timeStart, String timeEnd, @ModelAttribute AccountFundCurrent accountFundCurrent, @ModelAttribute Pagination pagin,
            String contractAddr,String isCapital) throws BusinessException
    {
        if(isCapital == null)isCapital = "no";
        // 证券信息ID判断s
        if(StringUtils.isNotBlank(contractAddr))
        {
            if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase("eth",contractAddr))
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
                isCapital = "yes";
            }
            StockInfo stockInfo = getStockInfo(contractAddr);
            accountFundCurrent.setStockinfoId(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(isCapital,"yes")?stockInfo.getCapitalStockinfoId():stockInfo.getTradeStockinfoId());
        }
        boolean isHisValue = StringUtils.equalsIgnoreCase(isHis, "yes");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountFundCurrent.setAccountId(principal.getId());
        if (!StringUtils.isBlank(timeStart))
        {
            accountFundCurrent.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            accountFundCurrent.setTimeEnd(timeEnd + " 23:59:59");
        }
        // 正常前端要传入参数为： 账户类型；证券信息ID；业务类别；流水时间戳
        logger.debug("currentsList accountFundCurrent:" + accountFundCurrent.toString());
        accountFundCurrent.setTableName(isHisValue ? getStockInfo(contractAddr).getTableFundCurrentHis() : getStockInfo(contractAddr).getTableFundCurrent());
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.findListByAccount(pagin, accountFundCurrent
        // ,FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_PRE_REQ,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_REQ,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_REGIST_AWARD,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_NOENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_NOENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_NOENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_NOENTRUST_DEAL,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_WITHDRAW,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_WITHDRAW,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_REJECT,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_REJECT,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_WITHDRAW,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_WITHDRAW,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET,
        // FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT
        );
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    public StockInfo getStockInfo(String addr)
    {
        StockInfo stockInfo = stockInfoService.findByContractAddr(addr);
        if(!org.apache.commons.lang3.StringUtils.equalsIgnoreCase(stockInfo.getIsActive(),FundConsts.PUBLIC_STATUS_YES))
        {
            throw new BusinessException("pair not open");
        }
        return stockInfo;
    }
}
