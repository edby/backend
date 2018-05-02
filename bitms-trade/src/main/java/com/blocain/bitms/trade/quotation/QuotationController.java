/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.quotation;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.fund.service.FundScanService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 *  行情共用  控制器
 * <p>File：QuotationController.java</p>
 * <p>Title: QuotationController</p>
 * <p>Description:QuotationController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class QuotationController extends GenericController
{
    private static final Logger      logger           = LoggerFactory.getLogger(QuotationController.class);
    
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String      keyPrefix        = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();
    
    private static final String      opQuotationKey   = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR)
            .append(BitmsConst.OP_RTQUOTATIONINFO).append(BitmsConst.SEPARATOR).toString();
    
    // 账户信息KEY: platscan_entrustvcoinmoney_[acctid]_[exchangeMoney]
    private static final String      keyEntrustPrefix = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR)
            .append(BitmsConst.OP_ENTRUSTVCOOINMONEY).append(BitmsConst.SEPARATOR).toString();
    
    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    
    @Autowired(required = false)
    private RtQuotationInfoService   rtQuotationInfoService;
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;
    
    @Autowired(required = false)
    private FundScanService          fundScanService;
    
    @Autowired(required = false)
    private AccountService           accountService;
    
    public RtQuotationInfo getRtQuotationInfoCache(Long exchangePairVCoin, Long exchangePairMoney)
    {
        String quotationKey = new StringBuffer(opQuotationKey).append(exchangePairMoney).toString();
        RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
        if (null == rtQuotationInfo || rtQuotationInfo.getPlatPrice().compareTo(BigDecimal.ZERO) == 0)
        {
            logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: 缓存中不存在行情，尝试从数据库获取行情");
            rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(exchangePairVCoin, exchangePairMoney);
        }
        return rtQuotationInfo;
    }
    
    public FundChangeModel getAccountAsset(Long accountId, Long exchangePairVCoin, Long exchangePairMoney) throws BusinessException
    {
        // 1.获取缓存基本数据
        FundChangeModel fundChangeModel = getAccountAssetCache(accountId, exchangePairVCoin, exchangePairMoney);
        // 2.参与账户资产指标计算
        fundChangeModel = calculateFundChangeModel(fundChangeModel, exchangePairVCoin, exchangePairMoney);
        return fundChangeModel;
    }

    public FundChangeModel getAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney) throws BusinessException
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        // 缓存读取
        logger.debug("开始缓存中获取");
        Object accountFundAsset = null;
        try
        {
            accountFundAsset = RedisUtils.getObject(key);
        }
        catch (Exception e)
        {
            logger.debug("获取缓存异常！" + e.getStackTrace());
        }
        logger.debug("结束缓存中获取");
        if (null != accountFundAsset)
        {
            logger.debug("缓存中读取accountFundAsset");
            logger.debug("accountFundAsset Cache:" + accountFundAsset);
            return (FundChangeModel) accountFundAsset;
        }
        else
        {
            logger.debug("数据库中读取accountFundAsset");
            logger.debug("★★fundChangeScan key=" + key);
            FundChangeModel fundChangeModel = fundScanService.setAccountAssetAttr(accountId, exchangePairVCoin, exchangePairMoney);
            logger.debug("★★fundChangeScan fundChangeModel=" + fundChangeModel.toString());
            RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
            accountFundAsset = RedisUtils.getObject(key);
            return (FundChangeModel) accountFundAsset; // 设置完缓存 返回
        }
    }

    public void setAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        logger.debug("数据库中读取accountFundAsset");
        logger.debug("★★fundChangeScan key=" + key);
        FundChangeModel fundChangeModel = fundScanService.setAccountAssetAttr(accountId, exchangePairVCoin, exchangePairMoney);
        logger.debug("★★fundChangeScan fundChangeModel=" + fundChangeModel.toString());
        RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
    }

    public void clearAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        RedisUtils.del(key);
    }

    private FundChangeModel calculateFundChangeModel(FundChangeModel fundChangeModel, Long exchangePairVCoin, Long exchangePairMoney)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = getRtQuotationInfoCache(exchangePairVCoin, exchangePairMoney);
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("当前内部行情：" + platPrice);
            if (platPrice == null)
            {
                platPrice = BigDecimal.ONE;
            }
            if (platPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                platPrice = BigDecimal.ONE;
            }
            if (isVCoin)
            {
                BigDecimal vcoinNet = fundChangeModel.getBtcAmount();
                BigDecimal moneyNet = fundChangeModel.getUsdxAmount();
                BigDecimal vcoinDebit = fundChangeModel.getBtcBorrow();
                BigDecimal moneyDebit = fundChangeModel.getUsdxBorrow();
                // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
                BigDecimal accountVcoinNetAmt = BigDecimal.ZERO;
                accountVcoinNetAmt = vcoinNet.subtract(vcoinDebit).add((moneyNet.subtract(moneyDebit)).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP));
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountVcoinNetAmt);
                // 最终实时可借等值的VCOIN数量
                BigDecimal btcCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getBtcLever()).subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP))
                        .subtract(vcoinDebit);
                BigDecimal usdxCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getUsdxLever())
                        .subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)).subtract(vcoinDebit);
                // 账户USDX余额 其实就是账户USDX可用
                BigDecimal usdxAmtNet = fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxBorrow()); // usdxEnable.add(usdxFrozen).subtract(usdxBorrow);
                logger.debug("usdxAmtNet=" + usdxAmtNet);
                fundChangeModel.setUsdxAmtBalance((usdxAmtNet.subtract(fundChangeModel.getUsdxFrozen())));
                // 账户BTC余额 其实就是账户BTC可用
                BigDecimal btcAmtNet = fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcBorrow()); // btcEnable.add(btcFrozen).subtract(btcBorrow);
                logger.debug("btcAmtNet=" + btcAmtNet);
                fundChangeModel.setBtcAmtBalance(btcAmtNet.subtract(fundChangeModel.getBtcFrozen()));
                // 账户BTC净值
                BigDecimal btcNetValue = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcNetValue = (usdxAmtNet).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).add(btcAmtNet);
                }
                logger.debug("btcNetValue=" + btcNetValue);
                fundChangeModel.setBtcNetValue(btcNetValue);
                // 强平价格 = 账户USDX余额 ÷ 账户BTC余额
                BigDecimal explosionPrice = BigDecimal.ZERO;
                if (btcAmtNet.compareTo(BigDecimal.ZERO) != 0)
                {
                    explosionPrice = usdxAmtNet.divide(btcAmtNet, 8, BigDecimal.ROUND_HALF_UP);
                    explosionPrice = explosionPrice.multiply(new BigDecimal(-1));
                    if (explosionPrice.compareTo(BigDecimal.ZERO) < 0)
                    {
                        explosionPrice = BigDecimal.ZERO;
                    }
                }
                logger.debug("explosionPrice=" + explosionPrice);
                fundChangeModel.setExplosionPrice(explosionPrice);
                // 方向 多 空 无
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    riskRate = calculateRiskRate("Long", btcAmtNet, btcNetValue, stockInfo.getClosePositionLongPrePercent());
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    if (explosionPrice.compareTo(BigDecimal.ZERO) != 0)
                    {
                        riskRate = calculateRiskRate("Short", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                    }
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                // 界面显示多空方向
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    fundChangeModel.setDirection("Long");
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    fundChangeModel.setDirection("Short");
                }
                else
                {
                    fundChangeModel.setDirection("None");
                }
                // usdx仓位数量
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    fundChangeModel.setUsdxPosition(usdxAmtNet.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).abs());
                }
                else
                {
                    fundChangeModel.setUsdxPosition(BigDecimal.ZERO);
                }
                // usdx仓位价值 其实就是 usdx净值的绝对值
                fundChangeModel.setUsdxPositionValue(usdxAmtNet.abs());
                // 最大可买 = USDX可用/平台行情 + 可借款额度
                if (usdxCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    usdxCanBorrowAmt = BigDecimal.ZERO;
                }
                BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)
                            .add(usdxCanBorrowAmt);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
                // 最大可卖 = BTC可用+BTC可借额度
                BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
                if (btcCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    btcCanBorrowAmt = BigDecimal.ZERO;
                }
                btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen())).add(btcCanBorrowAmt);
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
                fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
                // 账户盈亏=当前账户净值BTC数量-期初总流入BTC数量+期间总流出-期初初始数量
                BigDecimal profitAndLoss = fundChangeModel.getBtcNetValue().subtract(fundChangeModel.getBtcBeginning()).subtract(fundChangeModel.getBtcSumIn())
                        .add(fundChangeModel.getBtcSumOut());
                fundChangeModel.setProfitAndLoss(profitAndLoss);
                logger.debug("cal fundChangeModel=" + fundChangeModel.toString());
            }
            else
            { // 法定货币标的
                BigDecimal vcoinNet = fundChangeModel.getBtcAmount();
                BigDecimal moneyNet = fundChangeModel.getUsdxAmount();
                BigDecimal vcoinDebit = fundChangeModel.getBtcBorrow();
                BigDecimal moneyDebit = fundChangeModel.getUsdxBorrow();
                // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
                BigDecimal accountMoneyNetAmt = BigDecimal.ZERO;
                accountMoneyNetAmt = moneyNet.subtract(moneyDebit).add((vcoinNet.subtract(vcoinDebit)).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP));
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountMoneyNetAmt);
                // 最终实时可借等值的VCOIN数量
                BigDecimal btcCanBorrowAmt = accountMoneyNetAmt.multiply(fundChangeModel.getBtcLever()).subtract(vcoinDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP))
                        .subtract(moneyDebit);
                BigDecimal usdxCanBorrowAmt = accountMoneyNetAmt.multiply(fundChangeModel.getUsdxLever())
                        .subtract(vcoinDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)).subtract(moneyDebit);
                // 账户USDX余额 其实就是账户USDX可用
                BigDecimal usdxAmtNet = fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxBorrow()); // usdxEnable.add(usdxFrozen).subtract(usdxBorrow);
                logger.debug("usdxAmtNet=" + usdxAmtNet);
                fundChangeModel.setUsdxAmtBalance((usdxAmtNet.subtract(fundChangeModel.getUsdxFrozen())));
                // 账户BTC余额 其实就是账户BTC可用
                BigDecimal btcAmtNet = fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcBorrow()); // btcEnable.add(btcFrozen).subtract(btcBorrow);
                logger.debug("btcAmtNet=" + btcAmtNet);
                fundChangeModel.setBtcAmtBalance(btcAmtNet.subtract(fundChangeModel.getBtcFrozen()));
                // 账户BTC净值
                BigDecimal btcNetValue = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcNetValue = (usdxAmtNet).multiply(platPrice).add(btcAmtNet);
                }
                logger.debug("btcNetValue=" + btcNetValue);
                fundChangeModel.setBtcNetValue(btcNetValue);
                // 强平价格 = 账户USDX余额 ÷ 账户BTC余额
                BigDecimal explosionPrice = BigDecimal.ZERO;
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) != 0)
                {
                    explosionPrice = btcAmtNet.divide(usdxAmtNet, 8, BigDecimal.ROUND_HALF_UP);
                    explosionPrice = explosionPrice.multiply(new BigDecimal(-1));
                    if (explosionPrice.compareTo(BigDecimal.ZERO) < 0)
                    {
                        explosionPrice = BigDecimal.ZERO;
                    }
                }
                logger.debug("explosionPrice=" + explosionPrice);
                fundChangeModel.setExplosionPrice(explosionPrice);
                // 方向 多 空 无
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    riskRate = calculateRiskRate("Long", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    if (explosionPrice.compareTo(BigDecimal.ZERO) != 0)
                    {
                        riskRate = calculateRiskRate("Short", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                    }
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                // 界面显示多空方向
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    fundChangeModel.setDirection("Long");
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    fundChangeModel.setDirection("Short");
                }
                else
                {
                    fundChangeModel.setDirection("None");
                }
                // usdx仓位数量
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    fundChangeModel.setUsdxPosition(usdxAmtNet.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).abs());
                }
                else
                {
                    fundChangeModel.setUsdxPosition(BigDecimal.ZERO);
                }
                // usdx仓位价值 其实就是 usdx净值的绝对值
                fundChangeModel.setUsdxPositionValue(usdxAmtNet.abs());
                // 最大可买 = USDX可用/平台行情 + 可借款额度
                if (usdxCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    usdxCanBorrowAmt = BigDecimal.ZERO;
                }
                BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)
                            .add(usdxCanBorrowAmt);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
                // 最大可卖 = BTC可用+BTC可借额度
                BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
                if (btcCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    btcCanBorrowAmt = BigDecimal.ZERO;
                }
                btcSellMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).add(btcCanBorrowAmt);
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
                fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
                // 账户盈亏=当前账户净值BTC数量-期初总流入BTC数量+期间总流出-期初初始数量
                BigDecimal profitAndLoss = fundChangeModel.getBtcNetValue().subtract(fundChangeModel.getBtcBeginning()).subtract(fundChangeModel.getBtcSumIn())
                        .add(fundChangeModel.getBtcSumOut());
                fundChangeModel.setProfitAndLoss(profitAndLoss);
                logger.debug("cal fundChangeModel=" + fundChangeModel.toString());
            }
        }
        else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = getRtQuotationInfoCache(stockInfo.getTradeStockinfoId(), exchangePairMoney);
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("当前内部行情：" + platPrice);
            if (platPrice == null)
            {
                platPrice = BigDecimal.ONE;
            }
            if (platPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                platPrice = BigDecimal.ONE;
            }
            BigDecimal vcoinNet = fundChangeModel.getBtcAmount();
            BigDecimal moneyNet = fundChangeModel.getUsdxAmount();
            BigDecimal vcoinDebit = fundChangeModel.getBtcBorrow();
            BigDecimal moneyDebit = fundChangeModel.getUsdxBorrow();
            // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
            BigDecimal accountVcoinNetAmt = BigDecimal.ZERO;
            accountVcoinNetAmt = vcoinNet.subtract(vcoinDebit).add((moneyNet.subtract(moneyDebit)).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP));
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountVcoinNetAmt);
            // 最终实时可借等值的VCOIN数量
            BigDecimal btcCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getBtcLever()).subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP))
                    .subtract(vcoinDebit);
            BigDecimal usdxCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getUsdxLever())
                    .subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)).subtract(vcoinDebit);
            // 账户USDX余额 其实就是账户USDX可用
            BigDecimal usdxAmtNet = fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxBorrow()); // usdxEnable.add(usdxFrozen).subtract(usdxBorrow);
            logger.debug("usdxAmtNet=" + usdxAmtNet);
            fundChangeModel.setUsdxAmtBalance((usdxAmtNet.subtract(fundChangeModel.getUsdxFrozen())));
            // 账户BTC余额 其实就是账户BTC可用
            BigDecimal btcAmtNet = fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcBorrow()); // btcEnable.add(btcFrozen).subtract(btcBorrow);
            logger.debug("btcAmtNet=" + btcAmtNet);
            fundChangeModel.setBtcAmtBalance(btcAmtNet.subtract(fundChangeModel.getBtcFrozen()));
            // 账户BTC净值
            BigDecimal btcNetValue = BigDecimal.ZERO;
            if (platPrice.compareTo(BigDecimal.ZERO) != 0)
            {
                btcNetValue = (usdxAmtNet).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).add(btcAmtNet);
            }
            logger.debug("btcNetValue=" + btcNetValue);
            fundChangeModel.setBtcNetValue(btcNetValue);
            // 强平价格 = 账户USDX余额 ÷ 账户BTC余额
            BigDecimal explosionPrice = BigDecimal.ZERO;
            if (btcAmtNet.compareTo(BigDecimal.ZERO) != 0)
            {
                explosionPrice = usdxAmtNet.divide(btcAmtNet, 8, BigDecimal.ROUND_HALF_UP);
                explosionPrice = explosionPrice.multiply(new BigDecimal(-1));
                if (explosionPrice.compareTo(BigDecimal.ZERO) < 0)
                {
                    explosionPrice = BigDecimal.ZERO;
                }
            }
            logger.debug("explosionPrice=" + explosionPrice);
            // 方向 多 空
            if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
            {
                fundChangeModel.setExplosionPrice(explosionPrice.multiply(BigDecimal.ONE.add(stockInfo.getClosePositionLongPrePercent())));
                logger.debug("explosionPrice多头=" + explosionPrice);
            }
            else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                fundChangeModel.setExplosionPrice(explosionPrice.multiply(BigDecimal.ONE.subtract(stockInfo.getClosePositionShortPrePercent())));
                logger.debug("explosionPrice空头=" + explosionPrice);
            }else
            {
                fundChangeModel.setExplosionPrice(explosionPrice);
                logger.debug("explosionPrice3=" + explosionPrice);
            }
            logger.debug("explosionPrice=" + explosionPrice);
            // 方向 多 空 无
            if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
            {
                BigDecimal riskRate = BigDecimal.ZERO;
                riskRate = calculateRiskRate("Long", btcAmtNet, btcNetValue, stockInfo.getClosePositionLongPrePercent());
                logger.debug("riskRate=" + riskRate);
                fundChangeModel.setRiskRate(riskRate);
            }
            else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                BigDecimal riskRate = BigDecimal.ZERO;
                if (explosionPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    riskRate = calculateRiskRate("Short", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                }
                logger.debug("riskRate=" + riskRate);
                fundChangeModel.setRiskRate(riskRate);
            }
            else
            {
                BigDecimal riskRate = BigDecimal.ZERO;
                logger.debug("riskRate=" + riskRate);
                fundChangeModel.setRiskRate(riskRate);
            }
            // 界面显示多空方向
            if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
            {
                fundChangeModel.setDirection("Long");
            }
            else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                fundChangeModel.setDirection("Short");
            }
            else
            {
                fundChangeModel.setDirection("None");
            }
            // usdx仓位数量
            if (platPrice.compareTo(BigDecimal.ZERO) != 0)
            {
                fundChangeModel.setUsdxPosition(usdxAmtNet.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).abs());
            }
            else
            {
                fundChangeModel.setUsdxPosition(BigDecimal.ZERO);
            }
            // usdx仓位价值 其实就是 usdx净值的绝对值
            fundChangeModel.setUsdxPositionValue(usdxAmtNet.abs());
            // 最大可买 = USDX可用/平台行情 + 可借款额度
            if (usdxCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
            {
                usdxCanBorrowAmt = BigDecimal.ZERO;
            }
            BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
            if (platPrice.compareTo(BigDecimal.ZERO) != 0)
            {
                btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)
                        .add(usdxCanBorrowAmt);
            }
            logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
            // 最大可卖 = BTC可用+BTC可借额度
            BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
            if (btcCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
            {
                btcCanBorrowAmt = BigDecimal.ZERO;
            }
            btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen())).add(btcCanBorrowAmt);
            logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
            fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
            fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
            // 账户盈亏=当前账户净值BTC数量-期初总流入BTC数量+期间总流出-期初初始数量
            BigDecimal profitAndLoss = fundChangeModel.getBtcNetValue().subtract(fundChangeModel.getBtcBeginning()).subtract(fundChangeModel.getBtcSumIn())
                    .add(fundChangeModel.getBtcSumOut());
            fundChangeModel.setProfitAndLoss(profitAndLoss);

            // 账户不存在 或 设置不借时候的基本参数
            Account account = accountService.selectByPrimaryKey(fundChangeModel.getAccountId());
            if(account == null || account.getAutoDebit() == null || account.getAutoDebit().intValue() == 0)
            {
                btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen()));
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);

                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
//                fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
            }
            logger.debug("stcokinfo === "+stockInfo.toString());
            // 多头杠杆关闭
            if(!StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
            {
                logger.debug("多头杠杆关闭");
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCntBalance(fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen()));
            }

            // 空头杠杆关闭
            if(!StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
            {
                logger.debug("空头杠杆关闭");
                btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen()));
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
                fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
            }

            logger.debug("cal fundChangeModel=" + fundChangeModel.toString());
        }
        else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
        {
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = getRtQuotationInfoCache(stockInfo.getTradeStockinfoId(), exchangePairMoney);
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("当前内部行情：" + platPrice);
            if (platPrice == null)
            {
                platPrice = BigDecimal.ONE;
            }
            if (platPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                platPrice = BigDecimal.ONE;
            }
            BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
            btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_DOWN);
            logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
            fundChangeModel.setBtcMaxBorrow(btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
            // 最大可卖 = BTC可用
            BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
            btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen()));
            logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
            fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
        }
        else
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        fundChangeModel.setAccountId(null);
        return fundChangeModel;
    }

    public List<EntrustVCoinMoney> setEntrustOnDoingCache(EntrustVCoinMoney entity, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyEntrustPrefix).append(entity.getAccountId()).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_entrust_[acctid]
        Object entrustList = null;
        logger.debug("操作完毕 刷新未成交缓存 数据库中读取entrustxOnDoingCache");
        logger.debug("操作完毕 刷新未成交缓存 entrustxOnDoingCache key=" + key);
        entrustList = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyList(entity);
        RedisUtils.putObject(key, entrustList, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
        return (List<EntrustVCoinMoney>) entrustList; // 设置完缓存 返回
    }

    public void clearEntrustOnDoingCache(EntrustVCoinMoney entity, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyEntrustPrefix).append(entity.getAccountId()).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_entrust_[acctid]
        RedisUtils.del(key);
    }
    
    public List<EntrustVCoinMoney> entrustxOnDoingCache(EntrustVCoinMoney entity, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyEntrustPrefix).append(entity.getAccountId()).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_entrust_[acctid]
        logger.debug("entrustxOnDoingCache key=" + key);
        // 缓存读取
        logger.debug("开始缓存中获取");
        Object entrustList = null;
        try
        {
            entrustList = RedisUtils.getObject(key);
        }
        catch (Exception e)
        {
            logger.debug("获取缓存异常！" + e.getStackTrace());
        }
        logger.debug("结束缓存中获取");
        if (null != entrustList)
        {
            logger.debug("缓存中读取entrustxOnDoingCache");
            return (List<EntrustVCoinMoney>) entrustList;
        }
        else
        {
            return setEntrustOnDoingCache(entity, exchangePairMoney);
        }
    }
    
    private BigDecimal calculateRiskRate(String direct, BigDecimal btcNetValue, BigDecimal acountBtcNetValue, BigDecimal proPercent) throws BusinessException
    {
        if (StringUtils.equalsIgnoreCase(direct, "Long"))
        {
            if (acountBtcNetValue.compareTo(BigDecimal.ZERO) <= 0)
            {
                return BigDecimal.ONE;
            }
            else
            {
                BigDecimal ret = proPercent.multiply(BigDecimal.valueOf(100)).multiply(btcNetValue)
                        .divide((proPercent.multiply(BigDecimal.valueOf(100)).add(BigDecimal.valueOf(100))), 8, BigDecimal.ROUND_HALF_UP)
                        .divide(acountBtcNetValue, 8, BigDecimal.ROUND_HALF_UP).abs();
                return ret;
            }
        }
        else
        {
            if (acountBtcNetValue.compareTo(BigDecimal.ZERO) == 0)
            {
                return BigDecimal.ZERO;
            }
            else
            {
                BigDecimal ret = proPercent.multiply(BigDecimal.valueOf(100)).multiply(btcNetValue)
                        .divide((BigDecimal.valueOf(100).subtract(proPercent.multiply(BigDecimal.valueOf(100)))), 8, BigDecimal.ROUND_HALF_UP)
                        .divide(acountBtcNetValue, 8, BigDecimal.ROUND_HALF_UP).abs();
                return ret;
            }
        }
    }
}
