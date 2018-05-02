/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.spot.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.NumericUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.quotation.QuotationController;
import com.blocain.bitms.trade.risk.service.RiskService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.EntrustModel;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.RealDealVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.TradeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 *  撮合交易中心  控制器
 * <p>File：MatchTradeController.java</p>
 * <p>Title: MatchTradeController</p>
 * <p>Description:MatchTradeController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SOPT)
@Api(description = "撮合交易交易中心")
public class MatchTradeController extends QuotationController
{
    public static final Logger        logger = LoggerFactory.getLogger(MatchTradeController.class);
    
    @Autowired(required = false)
    private TradeService              tradeService;
    
    @Autowired(required = false)
    private AccountService            accountService;
    
    @Autowired(required = false)
    private SysParameterService       sysParameterService;
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService  entrustVCoinMoneyService;
    
    @Autowired(required = false)
    private RealDealVCoinMoneyService realDealVCoinMoneyService;
    
    @Autowired(required = false)
    private RiskService               riskService;
    
    @Autowired(required = false)
    private StockRateService          stockRateService;
    
    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    /**
     * 撮合交易C2CTrade-进行中的委托列表
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/matchTrade/entrustxOnDoing", method = RequestMethod.GET)
    @ApiOperation(value = "撮合交易C2CTrade-进行中的委托", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage entrustxOnDoing(@ModelAttribute Pagination pagin, Long exchangePairVCoin, Long exchangePairMoney) throws BusinessException
    {
        EntrustVCoinMoney entity = new EntrustVCoinMoney();
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        entity.setAccountId(principal.getId());// 个人数据
        // entity.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        // entity.setEntrustStockinfoId(exchangePairVCoin);
        entity.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entity.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        List<EntrustVCoinMoney> list = entrustxOnDoingCache(entity, exchangePairMoney);
        PaginateResult<EntrustVCoinMoney> result = new PaginateResult<>();
        pagin.setRows(list.size());
        pagin.setPage(1);
        pagin.setTotalRows(Long.parseLong(list.size() + ""));
        pagin.setTotalPage(1);
        result.setPage(pagin);
        result.setList(list);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 撮合交易C2CTrade-历史委托列表
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/matchTrade/entrustxOnHistory", method = RequestMethod.GET)
    @ApiOperation(value = "撮合交易C2CTrade-历史委托", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage entrustxOnHistory(@ModelAttribute EntrustVCoinMoney entity, @ModelAttribute Pagination pagin, Long exchangePairVCoin, Long exchangePairMoney,
            String noPage) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        entity.setAccountId(principal.getId());// 个人数据
        // entity.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        // entity.setEntrustStockinfoId(exchangePairVCoin);
        entity.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.getAccountHistoryEntrustVCoinMoneyList(pagin, entity);
        if (StringUtils.equalsIgnoreCase(noPage, "yes"))
        {
            List<EntrustVCoinMoney> list = result.getList();
            pagin.setRows(list.size());
            pagin.setPage(1);
            pagin.setTotalRows(Long.parseLong(list.size() + ""));
            pagin.setTotalPage(1);
            result.setPage(pagin);
            result.setList(list);
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 撮合交易C2CTrade-成交情况
     * @param entity
     * @param id
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/matchTrade/realDealByEntrustVCoinMoneyId", method = RequestMethod.GET)
    @ApiOperation(value = "撮合交易C2CTrade-成交情况", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage realDealByEntrustVCoinMoneyId(@ModelAttribute RealDealVCoinMoney entity, long id, @ModelAttribute Pagination pagin, Long exchangePairVCoin,
            Long exchangePairMoney) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        EntrustVCoinMoney entrustVCoinMoney = entrustVCoinMoneyService.selectByPrimaryKey(getStockInfo(exchangePairMoney).getTableEntrust(), id);
        if (entrustVCoinMoney.getAccountId().longValue() != principal.getId().longValue())
        {
            throw new BusinessException("Illegal operation");
        }
        else
        {
            entity.setEntrustId(id);
            entity.setTableName(getStockInfo(exchangePairMoney).getTableRealDeal());
            entity.setEnturstTableName(getStockInfo(exchangePairMoney).getTableEntrust());
            PaginateResult<RealDealVCoinMoney> result = realDealVCoinMoneyService.findRealDealListByEntrustId(pagin, entity);
            for (RealDealVCoinMoney realDealVCoinMoney : result.getList())
            {
                realDealVCoinMoney.setEntrustDirect(entrustVCoinMoney.getEntrustDirect());
            }
            return getJsonMessage(CommonEnums.SUCCESS, result);
        }
    }
    
    /**
     * 撮合交易发起委托卖出操作
     * @param entrustAmt
     * @param entrustPrice
     * @param entrustType
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/matchTrade/doMatchSell", method = RequestMethod.POST)
    @ApiOperation(value = "撮合交易发起委托卖出操作", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "entrustAmt", value = "委托数量", required = true, paramType = "form"),
            @ApiImplicitParam(name = "entrustPrice", value = "委托价格", required = true, paramType = "form"),
            @ApiImplicitParam(name = "entrustType", value = "委托类型(限价limitPrice、市价marketPrice)", required = true, paramType = "form")})
    public JsonMessage doMatchSellBtc(BigDecimal entrustAmt, BigDecimal entrustPrice, String entrustType, Long exchangePairMoney) throws BusinessException
    {
        EntrustModel entrustModel = new EntrustModel();
        String entrustDirect = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode();// 现货卖出
        logger.debug("入参：entrustAmt=" + entrustAmt + " entrustPrice=" + entrustPrice + " entrustType=" + entrustType);
        if (exchangePairMoney == null)
        {
            logger.debug("交易对错误 入参为空");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.debug("交易对错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) { return getJsonMessage(CommonEnums.USER_NOT_LOGIN); }
        checkSwitch();// 检查开关
        checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        checkEntrustMaxCnt(principal.getId(), exchangePairMoney);
        // 委托类型判断
        if (StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())
                || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            if (StringUtils.equalsIgnoreCase(entrustType, stockInfo.getOpenEntrustType())
                    || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITANDMARKETPRICE.getCode()))
            {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围正确");
                entrustModel.setEntrustType(entrustType);
            }
            else
            {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        else
        {
            logger.debug("委托类型(限价limitPrice、市价marketPrice)范围错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(BigDecimal.ZERO);
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            if (entrustModel.getEntrustAmt().compareTo(stockInfo.getSellMinAmount()) < 0 || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.debug("委托范围错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getSellAmountPrecision());
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(entrustPrice);
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            BigDecimal sellMaxCnt = stockInfo.getMaxSingleSellEntrustAmt();
            logger.debug("单笔卖出上限：" + sellMaxCnt);
            if (entrustModel.getEntrustAmt().compareTo(sellMaxCnt) > 0)
            {
                logger.debug("委托范围错误 已经超过系统单笔卖出上限");
                return getJsonMessage(CommonEnums.ERROR_GT_MAX_AMT);
            }
            BigDecimal sellMinAmt = stockInfo.getSellMinAmount();
            logger.debug("单笔卖出下限：" + sellMinAmt);
            if (entrustModel.getEntrustAmt().compareTo(sellMinAmt) < 0)
            {
                logger.debug("委托范围错误 已经超过系统单笔卖出下限");
                return getJsonMessage(CommonEnums.ERROR_GT_MIN_AMT);
            }
            if (entrustModel.getEntrustAmt().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellAmountPrecision())))) < 0
                    || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.debug("委托数量范围不对");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            // 委托价格0~999999个
            if (entrustModel.getEntrustPrice().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellPricePrecision())))) < 0
                    || entrustModel.getEntrustPrice().compareTo(BigDecimal.valueOf(999999)) >= 0)
            {
                logger.debug("委托价格不在范围内");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getSellAmountPrecision());
            NumericUtils.checkDecimalDigits("entrustPrice", entrustModel.getEntrustPrice(), stockInfo.getSellPricePrecision());
            Account account = accountService.selectByPrimaryKey(principal.getId());
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue())
            {
                logger.debug("用户异常");
                return getJsonMessage(CommonEnums.ERROR_LOGIN_LOCK);
            }
            // ---------------------------入参判断 end ------------------------------------------
            // 风控
            riskService.entrustRisk(stockInfo, principal.getId(), entrustDirect, entrustPrice, entrustType, exchangePairVCoin, exchangePairMoney);
        }
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(exchangePairMoney);
        stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);// 卖出费率
        List<StockRate> list = stockRateService.findList(stockRate);
        if (list.size() > 0)
        {
            stockRate = list.get(0);
            entrustModel.setFeeRate(stockRate.getRate());
        }
        else
        {
            logger.debug("费率有问题");
            return getJsonMessage(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        logger.debug("/matchTrade/doMatchSell page form = " + entrustModel.toString());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(principal.getId());
        entrustModel.setEntrustDirect(entrustDirect);// 委托卖出BTC
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setStockinfoIdEx(exchangePairMoney);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(principal.getId(), exchangePairVCoin, exchangePairMoney);
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(principal.getId());// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        clearEntrustOnDoingCache(entrustVCoinMoney, exchangePairMoney);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 撮合交易发起委托买入操作
     * @param entrustAmt
     * @param entrustPrice
     * @param entrustType
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/matchTrade/doMatchBuy", method = RequestMethod.POST)
    @ApiOperation(value = "撮合交易发起委托买入操作", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "entrustAmt", value = "委托数量", required = true, paramType = "form"),
            @ApiImplicitParam(name = "entrustPrice", value = "委托价格", required = true, paramType = "form"),
            @ApiImplicitParam(name = "entrustType", value = "委托类型(限价limitPrice、市价marketPrice)", required = true, paramType = "form")})
    public JsonMessage doMatchBuyBtc(BigDecimal entrustAmt, BigDecimal entrustPrice, String entrustType, Long exchangePairMoney) throws BusinessException
    {
        EntrustModel entrustModel = new EntrustModel();
        String entrustDirect = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode();// 现货买入
        logger.debug("入参：entrustAmt=" + entrustAmt + " entrustPrice=" + entrustPrice + " entrustType=" + entrustType);
        if (exchangePairMoney == null)
        {
            logger.debug("交易对错误 入参为空");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.debug("交易对错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) { return getJsonMessage(CommonEnums.USER_NOT_LOGIN); }
        checkSwitch();// 检查开关
        checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        checkEntrustMaxCnt(principal.getId(), exchangePairMoney);
        // 委托类型判断
        if (StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())
                || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            if (StringUtils.equalsIgnoreCase(entrustType, stockInfo.getOpenEntrustType())
                    || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITANDMARKETPRICE.getCode()))
            {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围正确");
                entrustModel.setEntrustType(entrustType);
            }
            else
            {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        else
        {
            logger.debug("委托类型(限价limitPrice、市价marketPrice)范围错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.ZERO);
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(entrustAmt);
            // ---------------------------入参判断 start ------------------------------------------
            // 委托价格0~999999个
            if (entrustModel.getEntrustAmtEx().compareTo(stockInfo.getBuyMinAmount()) < 0 || entrustModel.getEntrustAmtEx().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.debug("USDX数量范围不对");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getBuyAmountPrecision());
            Account account = accountService.selectByPrimaryKey(principal.getId());
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue())
            {
                logger.debug("账户状态异常");
                return getJsonMessage(CommonEnums.ERROR_LOGIN_LOCK);
            }
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(entrustPrice);
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            // ---------------------------入参判断 start ------------------------------------------
            BigDecimal buyMaxCnt = stockInfo.getMaxSingleBuyEntrustAmt();
            logger.debug("单笔买入上限：" + buyMaxCnt);
            if (entrustModel.getEntrustAmt().compareTo(buyMaxCnt) > 0)
            {
                logger.debug("委托范围错误 已经超过系统参数单笔买入上限");
                return getJsonMessage(CommonEnums.ERROR_GT_MAX_AMT);
            }
            BigDecimal buyMinAmt = stockInfo.getBuyMinAmount();
            logger.debug("单笔买入下限：" + buyMinAmt);
            if (entrustModel.getEntrustAmt().compareTo(buyMinAmt) < 0)
            {
                logger.debug("委托范围错误 已经超过系统单笔买入下限");
                return getJsonMessage(CommonEnums.ERROR_GT_MIN_AMT);
            }
            // BMS委托数量999~999999个
            if (entrustModel.getEntrustAmt().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyAmountPrecision())))) < 0
                    || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.debug("委托数量范围不对");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            // 委托价格0~999999个
            if (entrustModel.getEntrustPrice().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyPricePrecision())))) < 0
                    || entrustModel.getEntrustPrice().compareTo(BigDecimal.valueOf(999999)) >= 0)
            {
                logger.debug("委托价格不在范围内");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getBuyAmountPrecision());
            NumericUtils.checkDecimalDigits("entrustPrice", entrustModel.getEntrustPrice(), stockInfo.getBuyPricePrecision());
            Account account = accountService.selectByPrimaryKey(principal.getId());
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue())
            {
                logger.debug("账户状态异常");
                return getJsonMessage(CommonEnums.ERROR_LOGIN_LOCK);
            }
            // ---------------------------入参判断 end ------------------------------------------
            // 风控
            riskService.entrustRisk(stockInfo, principal.getId(), entrustDirect, entrustPrice, entrustType, exchangePairVCoin, exchangePairMoney);
        }
        entrustModel.setFee(BigDecimal.ZERO);
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(exchangePairMoney);
        stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);// 买入费率
        List<StockRate> list = stockRateService.findList(stockRate);
        if (list.size() > 0)
        {
            stockRate = list.get(0);
            entrustModel.setFeeRate(stockRate.getRate());
            logger.debug("费率" + stockRate.getRate());
        }
        else
        {
            logger.debug("费率有问题");
            return getJsonMessage(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        logger.debug("检查 完毕");
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(principal.getId());
        entrustModel.setEntrustDirect(entrustDirect);// 现货买入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setStockinfoIdEx(exchangePairMoney);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(principal.getId(), exchangePairVCoin, exchangePairMoney);
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(principal.getId());// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        clearEntrustOnDoingCache(entrustVCoinMoney, exchangePairMoney);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 撮合交易取消发起操作
     * @param entrustId
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/matchTrade/doMatchCancel", method = RequestMethod.POST)
    @ApiOperation(value = "撮合交易取消发起操作", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage doMatchCancel(Long entrustId, Long exchangePairMoney) throws BusinessException
    {
        EntrustModel entrustModel = new EntrustModel();
        entrustModel.setEntrustId(entrustId);
        logger.debug("exchangePairMoney=" + exchangePairMoney);
        if (exchangePairMoney == null)
        {
            logger.debug("交易对错误 入参为空");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.debug("交易对错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        checkSwitch();// 检查开关
        checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
        logger.debug("检查开关完毕");
        EntrustVCoinMoney entrustDB = new EntrustVCoinMoney();
        entrustDB = entrustVCoinMoneyService.selectByPrimaryKey(getStockInfo(exchangePairMoney).getTableEntrust(), entrustModel.getEntrustId());
        logger.debug("操作者=" + principal.getId() + " 拥有者=" + entrustDB.getAccountId());
        if (principal.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID.longValue()) // 超级用户可以为用户撤单
        {
            if (principal.getId().longValue() != entrustDB.getAccountId().longValue())
            {
                logger.debug("委托交易 越权");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        logger.debug("校验数据完毕");
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        entrustModel.setStockinfoIdEx(exchangePairMoney);
        entrustModel.setAccountId(account.getId());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        tradeService.entrustWithdrawX(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(principal.getId(), exchangePairVCoin, exchangePairMoney);
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(principal.getId());// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        clearEntrustOnDoingCache(entrustVCoinMoney, exchangePairMoney);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    private void checkAccountDataValidate(Account account)
    {
        if (null == account) { throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT); }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) { throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT); }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK);
        }
    }
    
    private void checkSwitch()
    {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null)
        {
            logger.debug("===========开关值空==========");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!StringUtils.isBlank(params.getValue()))
        {
            if (!params.getValue().equals("yes"))
            {
                logger.debug("===========开关已关闭==========");
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            else
            {
                logger.debug("===========开关已打开==========");
            }
        }
        else
        {
            logger.debug("===========开关值不存在==========");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
    }
    
    private void checkEntrustMaxCnt(Long accountId, Long exchangePairMoney)
    {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_TRADE_ACCOUNT_MAX_ENTRUST_CNT);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params != null)
        {
            Long cnt = Long.parseLong(params.getValue().toString());
            Long done = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyCnt(accountId, exchangePairMoney);
            if (done.compareTo(cnt) >= 0) { throw new BusinessException(CommonEnums.ERROR_GT_MAX_ORDER_CNT); }
        }
        params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_TRADE_MONEY_MAX_ENTRUST_CNT);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params != null)
        {
            Long cnt = Long.parseLong(params.getValue().toString());
            Long doing = entrustVCoinMoneyService.getMoneyDoingEntrustVCoinMoneyCnt(exchangePairMoney);
            if (doing.compareTo(cnt) >= 0) { throw new BusinessException(CommonEnums.ERROR_GT_MAX_ORDER_CNT); }
        }
    }
    
    private void checkTradeSwitch(Long stockinfoId)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockinfoId);
        if (null == stockInfo) { throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN); }
        if (!StringUtils.isBlank(stockInfo.getIsExchange()))
        {
            if (!stockInfo.getCanTrade().equals("yes"))
            {
                logger.debug("===========币种交易开关已关闭==========");
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            else
            {
                logger.debug("===========币种交易开关已打开==========");
            }
        }
        else
        {
            logger.debug("===========币种交易开关值不存在==========");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
