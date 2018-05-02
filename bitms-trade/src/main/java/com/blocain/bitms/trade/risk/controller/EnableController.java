/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.risk.controller;

import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户可用余额查询
 * <p>File：EnableController.java</p>
 * <p>Title: EnableController</p>
 * <p>Description:EnableController</p>
 * <p>Copyright: Copyright (c) 2017年7月28日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping("/risk")
@Api(description = "账户可用余额查询")
public class EnableController extends GenericController
{
    
    @Autowired(required = false)
    private EnableService       enableService;

    @Autowired(required = false)
    private StockInfoService    stockInfoService;

    @Autowired(required = false)
    private AccountAssetService accountAssetService;
    
    /**
     * 查询账户可用余额
     * @param fundModel
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/enableAmount", method = RequestMethod.POST)
    @ApiOperation(value = "查询账户可用余额", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage data(@ModelAttribute FundModel fundModel) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(fundModel.getAmount());
        logger.debug("/conversion/conversion page form = " + fundModel.toString());

        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
       // 业务类别判断
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_NOT_EXIST); }
        // 如果是合约转钱包 互调stockinfoId
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET.equals(fundModel.getBusinessFlag()))
        {
            Long temp = fundModel.getStockinfoId();
            fundModel.setStockinfoId(fundModel.getStockinfoIdEx());
            fundModel.setStockinfoIdEx(temp);
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(fundModel.getStockinfoIdEx());
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.debug("交易对错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        stockInfo = stockInfoList.get(0);
        // 合约资产
        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
            Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
            if (!(fundModel.getStockinfoId().equals(exchangePairVCoin)))
            {
                // 如果不是BTC和USDX合约互转 则参数错误
                logger.debug("参数验证错误：换转类型不匹配");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            if (!FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT.equals(fundModel.getBusinessFlag()) && !FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET
                    .equals(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_ERROR); }
            fundModel.setAccountId(principal.getId());
            fundModel.setCreateBy(principal.getId());
            logger.debug("conversion fundModel:" + fundModel.toString());

            if(StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(),FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT))
            {
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setBusinessFlag(fundModel.getBusinessFlag());
                enableModel.setStockinfoId(fundModel.getStockinfoId());
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
                return getJsonMessage(CommonEnums.SUCCESS, enableModel);
            }
            else
            {
                BigDecimal enableAmt = accountAssetService.contract2WalletEnableAmount(fundModel);
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setStockinfoId(fundModel.getStockinfoId());
                enableModel.setBusinessFlag(fundModel.getBusinessFlag());
                enableModel.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
                enableModel.setEnableAmount(enableAmt);
                return getJsonMessage(CommonEnums.SUCCESS, enableModel);
            }
        }else  if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            if(StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(),FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT))
            {
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT);
                enableModel.setStockinfoId(fundModel.getStockinfoId());
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
                return getJsonMessage(CommonEnums.SUCCESS, enableModel);
            }
            else
            {
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setStockinfoId(fundModel.getStockinfoId());
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET);
                enableModel = enableService.entrustTerminalEnable(enableModel);
                return getJsonMessage(CommonEnums.SUCCESS, enableModel);
            }
        }else
        {
            EnableModel enableModel = new EnableModel();
            enableModel.setEnableAmount(BigDecimal.ZERO);
            return getJsonMessage(CommonEnums.SUCCESS, enableModel);
        }
    }

    /**
     * 查询账户可用余额Spot（杠杆现货账户 与 理财账户 互转）
     * @param fundModel
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/enableAmountSpot", method = RequestMethod.POST)
    @ApiOperation(value = "查询账户可用余额（杠杆现货账户 与 理财账户 互转）", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage dataSpot(@ModelAttribute FundModel fundModel) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(fundModel.getAmount());
        logger.debug("/conversion/conversion page form = " + fundModel.toString());

        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        // 业务类别判断
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_NOT_EXIST); }
        // 如果是合约转钱包 互调stockinfoId
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH.equals(fundModel.getBusinessFlag()))
        {
            Long temp = fundModel.getStockinfoId();
            fundModel.setStockinfoId(fundModel.getStockinfoIdEx());
            fundModel.setStockinfoIdEx(temp);
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(fundModel.getStockinfoIdEx());
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.debug("交易对错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        stockInfo = stockInfoList.get(0);
        if (!FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH.equals(fundModel.getBusinessFlag()) && !FundConsts.SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT
                .equals(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_ERROR); }
        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setBusinessFlag(fundModel.getBusinessFlag());
                enableModel.setStockinfoId(fundModel.getStockinfoId());
                enableModel.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                return getJsonMessage(CommonEnums.SUCCESS, enableModel);

        }else
        {
            EnableModel enableModel = new EnableModel();
            enableModel.setEnableAmount(BigDecimal.ZERO);
            return getJsonMessage(CommonEnums.SUCCESS, enableModel);
        }
    }
}
