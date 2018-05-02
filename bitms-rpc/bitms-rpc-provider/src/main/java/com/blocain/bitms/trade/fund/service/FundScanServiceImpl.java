package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.RealDealVCoinMoneyService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 资产变动扫描服务接口
 * <p>File：FundScanService.java </p>
 * <p>Title: FundScanService </p>
 * <p>Description:FundScanService </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author jiangsc
 * @version 1.0
 */
@Service
public class FundScanServiceImpl implements FundScanService
{
    private static final Logger logger           = LoggerFactory.getLogger(FundScanServiceImpl.class);
    
    // 账户资产信息KEY: platscan_fundasset_[acctid]
    private static final String keyPrefix        = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();
    
    // 成交记录统计起始位置: platscan_start_location
    private static final String startKey         = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.STARTLOCATION)
            .toString();
    
    // 委托信息KEY: platscan_entrustvcoinmoney_[acctid]_[exchangeMoney]
    private static final String entrustKeyPrefix = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_ENTRUSTVCOOINMONEY)
            .append(BitmsConst.SEPARATOR).toString();
    
    private static Timestamp    scanTimestamp    = null;
    
    @Autowired(required = false)
    AccountDebitAssetService    accountDebitAssetService;
    
    @Autowired(required = false)
    private EnableService       enableService;
    
    @Autowired(required = false)
    private StockRateService    stockRateService;
    
    @Autowired(required = false)
    private StockInfoService    stockInfoService;
    
    @Autowired(required = false)
    AccountContractAssetService accountContractAssetService;
    
    @Autowired(required = false)
    AccountSpotAssetService     accountSpotAssetService;
    
    @Autowired(required = false)
    AccountWalletAssetService   accountWalletAssetService;
    
    @Autowired(required = false)
    EntrustVCoinMoneyService    entrustVCoinMoneyService;
    
    @Autowired(required = false)
    RealDealVCoinMoneyService   realDealVCoinMoneyService;
    
    @Autowired(required = false)
    RtQuotationInfoService      rtQuotationInfoService;
    
    /**
     * 资产变动执行扫描
     * @return
     * @throws BusinessException
     */
    @Override
    public void fundChangeScan() throws BusinessException
    {
        Timestamp cacheDate = getQueryParam(); // 获取上次记录的时间点，后台数据库扫描从上次记录时间扫描到当前时间。
        logger.debug("★☆★☆★☆★☆★☆ 查询时间起始点: " + cacheDate);
        Timestamp currentDate = new Timestamp(System.currentTimeMillis() - 2 * 1000);// 获取当前时间点，作为下次查询的起始点
        logger.debug("★☆★☆★☆★☆★☆ 当前时间点: " + currentDate);
        // 现货合约品种
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
        if (stockInfoList.size() > 0)
        {
            for (int i = 0; i < stockInfoList.size(); i++)
            {
                // stockInfo
                StockInfo stockInfo = stockInfoList.get(i);

                // 最新成交价与指数均价监控
                checkPlatPrice(stockInfo);

                // 成交表开始扫描
                logger.debug("成交表" + stockInfo.getTableRealDeal() + "扫描开始!");
                List<Long> list = this.queryAccountRealDealChangeLists(cacheDate, stockInfo.getTableRealDeal());
                int size = list == null ? 0 : list.size();
                logger.debug("★☆★☆★☆★☆★☆ 变动账户数: " + size);
                if (CollectionUtils.isNotEmpty(list))
                {
                    for (Long accountId : list)
                    {
                        logger.debug("★☆★☆★☆★☆★☆★☆ 变动账户id: " + accountId);
                        // 交易资产表
                        List<AccountContractAsset> assetList = this.queryAccountContractAssetLists(accountId, stockInfo.getTableAsset());
                        int assetSize = assetList == null ? 0 : assetList.size();
                        logger.debug("★☆★☆★☆★☆★☆★☆★☆ 合约资产账户数: " + assetSize);
                        for (AccountContractAsset accountContractAsset : assetList)
                        {
                            // 一个交易对只计算一次 传入数字货币代码与法定货币代码
                            if (accountContractAsset.getStockinfoId().longValue() != accountContractAsset.getRelatedStockinfoId().longValue())
                            {
                                logger.debug("★☆★☆★☆★☆★☆★☆★☆ 合约资产账户id: " + accountContractAsset.getRelatedStockinfoId());
                                String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR)
                                        .append(accountContractAsset.getRelatedStockinfoId()).toString(); // platscan_fundCurrent_[acctid]
                                logger.debug("fundChangeScan key=" + key);
                                // 合约资产表
                                FundChangeModel fundChangeModel = setAccountAssetAttr(accountId, accountContractAsset.getStockinfoId(),
                                        accountContractAsset.getRelatedStockinfoId());
                                logger.debug("★☆★☆★☆★☆★☆★☆★☆fundChangeScan fundChangeModel=" + fundChangeModel.toString());
                                RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
                                // 刷新用户未成交委托数据
                                // 刷新用户未成交委托数据
                                String entrustKey = new StringBuilder(entrustKeyPrefix).append(accountId).append(BitmsConst.SEPARATOR)
                                        .append(accountContractAsset.getRelatedStockinfoId()).toString(); // platscan_entrust_[acctid]
                                logger.debug("set entrustxOnDoingCache key=" + entrustKey);
                                logger.debug("set entrustxOnDoingCache 变动账户id: " + accountId);
                                List<EntrustVCoinMoney> entrustList = this.getAccountDoingEntrustVCoinMoneyList(accountId, accountContractAsset.getStockinfoId(),
                                        accountContractAsset.getRelatedStockinfoId());
                                logger.debug("set entrustxOnDoingCache =" + entrustList.toString());
                                RedisUtils.putObject(entrustKey, entrustList, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
                            }
                        }
                    }
                }
                logger.debug("成交表" + stockInfo.getTableRealDeal() + "扫描结束!");
            }
        }
        // 杠杆现货品种
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        stockInfoList = stockInfoService.findList(stockInfoSelect);
        if (stockInfoList.size() > 0)
        {
            for (int i = 0; i < stockInfoList.size(); i++)
            {
                // stockInfo
                StockInfo stockInfo = stockInfoList.get(i);

                // 最新成交价与指数均价监控
                checkPlatPrice(stockInfo);

                // 成交表开始扫描
                logger.debug("成交表" + stockInfo.getTableRealDeal() + "扫描开始!");
                List<Long> list = this.queryAccountRealDealChangeLists(cacheDate, stockInfo.getTableRealDeal());
                int size = list == null ? 0 : list.size();
                logger.debug("★☆★☆★☆★☆★☆ 变动账户数: " + size);
                if (CollectionUtils.isNotEmpty(list))
                {
                    for (Long accountId : list)
                    {
                        logger.debug("★☆★☆★☆★☆★☆★☆ 变动账户id: " + accountId);
                        // 根据stockInfo的品种信息来进行更新
                        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(stockInfo.getId()).toString(); // platscan_fundCurrent_[acctid]
                        logger.debug("fundChangeScan key=" + key);
                        FundChangeModel fundChangeModel = setAccountAssetAttr(accountId, stockInfo.getId(), stockInfo.getId());
                        logger.debug("★☆★☆★☆★☆★☆★☆★☆fundChangeScan fundChangeModel=" + fundChangeModel.toString());
                        RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
                        // 刷新用户未成交委托数据
                        // 刷新用户未成交委托数据
                        String entrustKey = new StringBuilder(entrustKeyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(stockInfo.getId()).toString(); // platscan_entrust_[acctid]
                        logger.debug("set entrustxOnDoingCache key=" + entrustKey);
                        logger.debug("set entrustxOnDoingCache 变动账户id: " + accountId);
                        List<EntrustVCoinMoney> entrustList = this.getAccountDoingEntrustVCoinMoneyList(accountId, stockInfo.getTradeStockinfoId(), stockInfo.getId());
                        logger.debug("set entrustxOnDoingCache =" + entrustList.toString());
                        RedisUtils.putObject(entrustKey, entrustList, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
                    }
                }
                logger.debug("成交表" + stockInfo.getTableRealDeal() + "扫描结束!");
            }
        }
        // 纯正现货品种
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_PURESPOT);
        stockInfoList = stockInfoService.findList(stockInfoSelect);
        if (stockInfoList.size() > 0)
        {
            for (int i = 0; i < stockInfoList.size(); i++)
            {
                // stockInfo
                StockInfo stockInfo = stockInfoList.get(i);
                // 成交表开始扫描
                logger.debug("成交表" + stockInfo.getTableRealDeal() + "扫描开始!");
                List<Long> list = this.queryAccountRealDealChangeLists(cacheDate, stockInfo.getTableRealDeal());
                int size = list == null ? 0 : list.size();
                logger.debug("★☆★☆★☆★☆★☆ 变动账户数: " + size);
                if (CollectionUtils.isNotEmpty(list))
                {
                    for (Long accountId : list)
                    {
                        logger.debug("★☆★☆★☆★☆★☆★☆ 变动账户id: " + accountId);
                        // 根据stockInfo的品种信息来进行更新
                        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(stockInfo.getId()).toString(); // platscan_fundCurrent_[acctid]
                        logger.debug("fundChangeScan key=" + key);
                        FundChangeModel fundChangeModel = setAccountAssetAttr(accountId, stockInfo.getId(), stockInfo.getId());
                        logger.debug("★☆★☆★☆★☆★☆★☆★☆fundChangeScan fundChangeModel=" + fundChangeModel.toString());
                        RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
                        // 刷新用户未成交委托数据
                        // 刷新用户未成交委托数据
                        String entrustKey = new StringBuilder(entrustKeyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(stockInfo.getId()).toString(); // platscan_entrust_[acctid]
                        logger.debug("set entrustxOnDoingCache key=" + entrustKey);
                        logger.debug("set entrustxOnDoingCache 变动账户id: " + accountId);
                        List<EntrustVCoinMoney> entrustList = this.getAccountDoingEntrustVCoinMoneyList(accountId, stockInfo.getTradeStockinfoId(), stockInfo.getId());
                        logger.debug("set entrustxOnDoingCache =" + entrustList.toString());
                        RedisUtils.putObject(entrustKey, entrustList, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
                    }
                }
                logger.debug("成交表" + stockInfo.getTableRealDeal() + "扫描结束!");
            }
        }
        // RedisUtils.putObject(startKey, currentDate, CacheConst.DEFAULT_CACHE_TIME);// 起始位置缓存
        scanTimestamp = currentDate;
    }
    
    /**
     * 获取变动账户查询的起始ID
     * @return
     */
    private Timestamp getQueryParam()
    {
        // Timestamp currentDate = (Timestamp) RedisUtils.getObject(startKey);
        if (null == scanTimestamp)
        {
            scanTimestamp = new Timestamp(System.currentTimeMillis() - 6 * 1000);
            return scanTimestamp;
        }
        return scanTimestamp;
    }

    /**
     * 多空杠杆自动阈值开关，相对风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆，负溢价超出-5%关闭空头杠杆。
     * @param stockInfo
     * @throws BusinessException
     */
    public void checkPlatPrice(StockInfo stockInfo) throws BusinessException
    {
        StockInfo stockInfo1 = new StockInfo();
        stockInfo1.setId(stockInfo.getId());
        logger.debug("进入：多空杠杆自动阈值开关，相对风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆，负溢价超出-5%关闭空头杠杆。");
        RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(stockInfo.getTradeStockinfoId(), stockInfo.getId());
        logger.debug(rtQuotationInfo.toString());
        BigDecimal idxAvgPrice = BigDecimal.ZERO;
        BigDecimal platPrice = BigDecimal.ZERO;
        if (rtQuotationInfo != null)
        {
            idxAvgPrice = rtQuotationInfo.getIdxAvgPrice();
            platPrice = rtQuotationInfo.getPlatPrice();
            if (idxAvgPrice == null || idxAvgPrice.compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(10000001, "行情异常！"); }
            if (platPrice == null || platPrice.compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(10000001, "行情异常！"); }
            logger.debug("多空杠杆自动阈值开关，相对风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆，负溢价超出-5%关闭空头杠杆。");
            logger.debug("指数均价："+idxAvgPrice);
            logger.debug("平台价格："+platPrice);
            logger.debug("高于 关闭多头杠杆："+stockInfo.getMaxLongFuse());
            logger.debug("负溢价超出 关闭空头杠杆："+stockInfo.getMaxShortFuse());

            boolean key = false;

            if(platPrice.compareTo(idxAvgPrice)>0
                    && (platPrice.divide(idxAvgPrice,8,BigDecimal.ROUND_HALF_UP)).compareTo((BigDecimal.ONE.add(stockInfo.getMaxLongFuse())))>0)
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                {
                    stockInfo1.setMaxLongLeverSwitch(FundConsts.PUBLIC_STATUS_NO);
                    logger.debug("风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆");
                    key = true;
                }
            }
            else
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(),FundConsts.PUBLIC_STATUS_NO))
                {
                    stockInfo1.setMaxLongLeverSwitch(FundConsts.PUBLIC_STATUS_YES);
                    logger.debug("多头杠杆解除关闭");
                    key = true;
                }
            }

            if(platPrice.compareTo(idxAvgPrice)<0
                    && (platPrice.divide(idxAvgPrice,8,BigDecimal.ROUND_HALF_UP)).compareTo((BigDecimal.ONE.subtract(stockInfo.getMaxShortFuse())))<0)
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                {
                    stockInfo1.setMaxShortLeverSwitch(FundConsts.PUBLIC_STATUS_NO);
                    logger.debug("风控基价的±溢价百分比关闭，负溢价超出-5%关闭空头杠杆");
                    key = true;
                }
            }
            else
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(),FundConsts.PUBLIC_STATUS_NO))
                {
                    stockInfo1.setMaxShortLeverSwitch(FundConsts.PUBLIC_STATUS_YES);
                    logger.debug("空头杠杆解除关闭");
                    key = true;
                }
            }
            if(key)
            {
                stockInfoService.updateByPrimaryKeySelective(stockInfo1);
            }
            logger.debug("完成：多空杠杆自动阈值开关，相对风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆，负溢价超出-5%关闭空头杠杆。");
        }
        else
        {
            throw new BusinessException("行情异常！");
        }
    }
    
    /**
     * 给资产流水变动赋值
     * @param accountId
     * @param exchangePairVCoin
     * @param exchangePairMoney
     * @return
     */
    @Override
    public FundChangeModel setAccountAssetAttr(Long accountId, Long exchangePairVCoin, Long exchangePairMoney) throws BusinessException
    {
        FundChangeModel accountFundAssetDB = new FundChangeModel();
        accountFundAssetDB.setAccountId(accountId);
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        if (stockInfo == null)
        {
            logger.debug("证券ID不存在，非法下单");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            BigDecimal usdxLever = stockInfo.getMaxLongLever();
            logger.debug("USDX最大可贷款杠杆倍数:" + usdxLever);
            accountFundAssetDB.setUsdxLever(usdxLever);
            BigDecimal btcLever = stockInfo.getMaxShortLever();
            logger.debug("BTC最大可贷款杠杆倍数:" + btcLever);
            accountFundAssetDB.setBtcLever(btcLever);
            // 查询USDX借款情况
            AccountDebitAsset record = new AccountDebitAsset();
            record.setStockinfoId(exchangePairMoney);
            record.setRelatedStockinfoId(exchangePairMoney);
            record.setBorrowerAccountId(accountId);
            record.setTableName(stockInfo.getTableDebitAsset());
            List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
            BigDecimal usdxBorrow = BigDecimal.ZERO;
            if (list.size() > 0)
            {
                record = list.get(0);
                usdxBorrow = record.getDebitAmt(); // 已借款金额
            }
            logger.debug("usdxBorrow=" + usdxBorrow);
            accountFundAssetDB.setUsdxBorrow(usdxBorrow);
            // 查询BTC借款情况
            record = new AccountDebitAsset();
            record.setStockinfoId(exchangePairVCoin);
            record.setRelatedStockinfoId(exchangePairMoney);
            record.setBorrowerAccountId(accountId);
            record.setTableName(stockInfo.getTableDebitAsset());
            list = accountDebitAssetService.findList(record);
            BigDecimal btcBorrow = BigDecimal.ZERO;
            if (list.size() > 0)
            {
                record = list.get(0);
                btcBorrow = record.getDebitAmt(); // 已借款BTC
            }
            logger.debug("btcBorrow=" + btcBorrow);
            accountFundAssetDB.setBtcBorrow(btcBorrow);
            // USDX可用查询
            EnableModel enableModel = new EnableModel();
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);// 买数字货币算法定货币可用
            enableModel.setStockinfoId(exchangePairMoney);
            enableModel.setRelatedStockinfoId(exchangePairMoney);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            BigDecimal usdxEnable = enableModel.getEnableAmount();
            BigDecimal usdxFrozen = enableModel.getFrozenAmt();
            logger.debug("usdxEnable=" + usdxEnable);
            logger.debug("usdxFrozen=" + usdxFrozen);
            accountFundAssetDB.setUsdxFrozen(usdxFrozen);
            accountFundAssetDB.setUsdxAmount(usdxEnable.add(usdxFrozen));
            // BTC可用查询
            enableModel = new EnableModel();
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);// 卖数字货币算数字货币可用
            enableModel.setStockinfoId(exchangePairVCoin);
            enableModel.setRelatedStockinfoId(exchangePairMoney);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            BigDecimal btcEnable = enableModel.getEnableAmount();
            BigDecimal btcFrozen = enableModel.getFrozenAmt();
            logger.debug("btcEnable=" + btcEnable);
            logger.debug("btcFrozen=" + btcFrozen);
            accountFundAssetDB.setBtcFrozen(btcFrozen);
            accountFundAssetDB.setBtcAmount(btcEnable.add(btcFrozen));
            // 当前账户-当前BTC持仓数量 tableName已在服务层处理
            ContractAssetModel modelPerson = accountContractAssetService.findAccountSumContractAsset(exchangePairVCoin, exchangePairMoney, accountId);
            BigDecimal allBtcOfPersion = modelPerson.getSumAmount();
            logger.debug("当前账户" + accountId + ":" + allBtcOfPersion);
            BigDecimal allBtcSumInitialOfPersion = modelPerson.getSumInitialAmt();
            accountFundAssetDB.setBtcBeginning(allBtcSumInitialOfPersion);
            // 当前账户-总流入数字货币数量
            BigDecimal allInOfPersion = modelPerson.getSumFlowInAmt();
            accountFundAssetDB.setBtcSumIn(allInOfPersion);
            // 当前账户--总流出数字货币数量
            logger.debug("当前账户" + accountId + "--总流入数字货币数量:" + allInOfPersion);
            // 当前账户-总流出数字货币数量
            BigDecimal allOutOfPersion = modelPerson.getSumFlowOutAmt();
            logger.debug("当前用户" + accountId + "-总流出数字货币数量:" + allOutOfPersion);
            accountFundAssetDB.setBtcSumOut(allOutOfPersion);
            // 买入成交费用比例
            BigDecimal buyBtcFeeRate = BigDecimal.ZERO;
            StockRate stockRate = new StockRate();
            stockRate.setStockinfoId(exchangePairMoney);
            stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);// 费率
            List<StockRate> feeList = stockRateService.findList(stockRate);
            if (feeList.size() > 0)
            {
                stockRate = feeList.get(0);
                buyBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
            }
            else
            {
                logger.debug("买入数字货币买入成交费用比例有问题");
            }
            logger.debug("buyBtcFeeRate=" + buyBtcFeeRate);
            accountFundAssetDB.setBuyBtcFeeRate(buyBtcFeeRate);
            // 卖出成交费用比例etDB;
            BigDecimal sellBtcFeeRate = BigDecimal.ZERO;
            stockRate = new StockRate();
            stockRate.setStockinfoId(exchangePairMoney);
            stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);// 费率
            feeList = stockRateService.findList(stockRate);
            if (feeList.size() > 0)
            {
                stockRate = feeList.get(0);
                sellBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
            }
            else
            {
                logger.debug("卖出数字货币扣除法定货币的费率有问题");
            }
            logger.debug("sellBtcFeeRate=" + sellBtcFeeRate);
            accountFundAssetDB.setSellBtcFeeRate(sellBtcFeeRate);
            // 交割结算时间 其实就是合约编号
            accountFundAssetDB.setSettlementTimeNo(stockInfo.getStockName() + "" + stockInfo.getSettlementCycle());
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            BigDecimal usdxLever = stockInfo.getMaxLongLever();
            logger.debug("USDX最大可贷款杠杆倍数:" + usdxLever);
            accountFundAssetDB.setUsdxLever(usdxLever);
            BigDecimal btcLever = stockInfo.getMaxShortLever();
            logger.debug("BTC最大可贷款杠杆倍数:" + btcLever);
            accountFundAssetDB.setBtcLever(btcLever);
            // 查询USDX借款情况
            AccountDebitAsset record = new AccountDebitAsset();
            record.setStockinfoId(stockInfo.getCapitalStockinfoId());
            record.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            record.setTableName(stockInfo.getTableDebitAsset());
            record.setBorrowerAccountId(accountId);
            List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
            BigDecimal usdxBorrow = BigDecimal.ZERO;
            if (list.size() > 0)
            {
                record = list.get(0);
                usdxBorrow = record.getDebitAmt(); // 已借款金额
            }
            logger.debug("usdxBorrow=" + usdxBorrow);
            accountFundAssetDB.setUsdxBorrow(usdxBorrow);
            // 查询BTC借款情况
            record = new AccountDebitAsset();
            record.setStockinfoId(stockInfo.getTradeStockinfoId());
            record.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            record.setTableName(stockInfo.getTableDebitAsset());
            record.setBorrowerAccountId(accountId);
            list = accountDebitAssetService.findList(record);
            BigDecimal btcBorrow = BigDecimal.ZERO;
            if (list.size() > 0)
            {
                record = list.get(0);
                btcBorrow = record.getDebitAmt(); // 已借款BTC
            }
            logger.debug("btcBorrow=" + btcBorrow);
            accountFundAssetDB.setBtcBorrow(btcBorrow);
            // USDX可用查询
            EnableModel enableModel = new EnableModel();
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);// 买数字货币算法定货币可用
            enableModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
            enableModel.setRelatedStockinfoId(exchangePairMoney);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            BigDecimal usdxEnable = enableModel.getEnableAmount();
            BigDecimal usdxFrozen = enableModel.getFrozenAmt();
            logger.debug("usdxEnable=" + usdxEnable);
            logger.debug("usdxFrozen=" + usdxFrozen);
            accountFundAssetDB.setUsdxFrozen(usdxFrozen);
            accountFundAssetDB.setUsdxAmount(usdxEnable.add(usdxFrozen));
            // BTC可用查询
            enableModel = new EnableModel();
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);// 卖数字货币算数字货币可用
            enableModel.setStockinfoId(stockInfo.getTradeStockinfoId());
            enableModel.setRelatedStockinfoId(exchangePairMoney);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            BigDecimal btcEnable = enableModel.getEnableAmount();
            BigDecimal btcFrozen = enableModel.getFrozenAmt();
            logger.debug("btcEnable=" + btcEnable);
            logger.debug("btcFrozen=" + btcFrozen);
            accountFundAssetDB.setBtcFrozen(btcFrozen);
            accountFundAssetDB.setBtcAmount(btcEnable.add(btcFrozen));
            BigDecimal allBtcSumInitialOfPersion = BigDecimal.ZERO;
            accountFundAssetDB.setBtcBeginning(allBtcSumInitialOfPersion);
            BigDecimal allInOfPersion = BigDecimal.ZERO;
            accountFundAssetDB.setBtcSumIn(allInOfPersion);
            BigDecimal allOutOfPersion = BigDecimal.ZERO;
            accountFundAssetDB.setBtcSumOut(allOutOfPersion);
            // 买入成交费用比例
            BigDecimal buyBtcFeeRate = BigDecimal.ZERO;
            StockRate stockRate = new StockRate();
            stockRate.setStockinfoId(exchangePairMoney);
            stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);// 费率
            List<StockRate> feeList = stockRateService.findList(stockRate);
            if (feeList.size() > 0)
            {
                stockRate = feeList.get(0);
                buyBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
            }
            else
            {
                logger.debug("买入数字货币买入成交费用比例有问题");
            }
            logger.debug("buyBtcFeeRate=" + buyBtcFeeRate);
            accountFundAssetDB.setBuyBtcFeeRate(buyBtcFeeRate);
            // 卖出成交费用比例etDB;
            BigDecimal sellBtcFeeRate = BigDecimal.ZERO;
            stockRate = new StockRate();
            stockRate.setStockinfoId(exchangePairMoney);
            stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);// 费率
            feeList = stockRateService.findList(stockRate);
            if (feeList.size() > 0)
            {
                stockRate = feeList.get(0);
                sellBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
            }
            else
            {
                logger.debug("卖出数字货币扣除法定货币的费率有问题");
            }
            logger.debug("sellBtcFeeRate=" + sellBtcFeeRate);
            accountFundAssetDB.setSellBtcFeeRate(sellBtcFeeRate);
            // 交割结算时间 其实就是合约编号
            accountFundAssetDB.setSettlementTimeNo(stockInfo.getStockName() + "" + stockInfo.getSettlementCycle());
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
        {
            // USDX可用查询
            EnableModel enableModel = new EnableModel();
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);// 买数字货币算法定货币可用
            enableModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
            enableModel.setRelatedStockinfoId(exchangePairMoney);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            BigDecimal usdxEnable = enableModel.getEnableAmount();
            BigDecimal usdxFrozen = enableModel.getFrozenAmt();
            logger.debug("usdxEnable=" + usdxEnable);
            logger.debug("usdxFrozen=" + usdxFrozen);
            accountFundAssetDB.setUsdxFrozen(usdxFrozen);
            accountFundAssetDB.setUsdxAmount(usdxEnable.add(usdxFrozen));
            // BTC可用查询
            enableModel = new EnableModel();
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);// 卖数字货币算数字货币可用
            enableModel.setStockinfoId(stockInfo.getTradeStockinfoId());
            enableModel.setRelatedStockinfoId(exchangePairMoney);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            BigDecimal btcEnable = enableModel.getEnableAmount();
            BigDecimal btcFrozen = enableModel.getFrozenAmt();
            logger.debug("btcEnable=" + btcEnable);
            logger.debug("btcFrozen=" + btcFrozen);
            accountFundAssetDB.setBtcFrozen(btcFrozen);
            accountFundAssetDB.setBtcAmount(btcEnable.add(btcFrozen));
            // 买入成交费用比例
            BigDecimal buyBtcFeeRate = BigDecimal.ZERO;
            StockRate stockRate = new StockRate();
            stockRate.setStockinfoId(exchangePairMoney);
            stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);// 费率
            List<StockRate> feeList = stockRateService.findList(stockRate);
            if (feeList.size() > 0)
            {
                stockRate = feeList.get(0);
                buyBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
            }
            else
            {
                logger.debug("买入数字货币买入成交费用比例有问题");
            }
            logger.debug("buyBtcFeeRate=" + buyBtcFeeRate);
            accountFundAssetDB.setBuyBtcFeeRate(buyBtcFeeRate);
            // 卖出成交费用比例
            BigDecimal sellBtcFeeRate = BigDecimal.ZERO;
            stockRate = new StockRate();
            stockRate.setStockinfoId(exchangePairMoney);
            stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);// 费率
            feeList = stockRateService.findList(stockRate);
            if (feeList.size() > 0)
            {
                stockRate = feeList.get(0);
                sellBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
            }
            else
            {
                logger.debug("卖出数字货币扣除法定货币的费率有问题");
            }
            logger.debug("sellBtcFeeRate=" + sellBtcFeeRate);
            accountFundAssetDB.setSellBtcFeeRate(sellBtcFeeRate);
        }
        else
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        logger.debug("accountFundAssetDB=" + accountFundAssetDB.toString());
        return accountFundAssetDB;
    }
    
    /**
     * 给资产流水变动赋值
     * @param accountId
     * @param exchangePairVCoin
     * @param exchangePairMoney
     * @return
     */
    private FundChangeModel setAccountWalletFundAssetAttr2(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        FundChangeModel accountFundAssetDB = new FundChangeModel();
        accountFundAssetDB.setAccountId(accountId);
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        if (stockInfo == null)
        {
            logger.debug("证券ID不存在，非法下单");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        // USDX可用查询
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountId);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);// 买数字货币算法定货币可用
        enableModel.setStockinfoId(exchangePairMoney);
        enableModel.setRelatedStockinfoId(exchangePairMoney);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        BigDecimal usdxEnable = enableModel.getEnableAmount();
        BigDecimal usdxFrozen = enableModel.getFrozenAmt();
        logger.debug("usdxEnable=" + usdxEnable);
        logger.debug("usdxFrozen=" + usdxFrozen);
        accountFundAssetDB.setUsdxFrozen(usdxFrozen);
        accountFundAssetDB.setUsdxAmount(usdxEnable.add(usdxFrozen));
        // BTC可用查询
        enableModel = new EnableModel();
        enableModel.setAccountId(accountId);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);// 卖数字货币算数字货币可用
        enableModel.setStockinfoId(exchangePairVCoin);
        enableModel.setRelatedStockinfoId(exchangePairMoney);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        BigDecimal btcEnable = enableModel.getEnableAmount();
        BigDecimal btcFrozen = enableModel.getFrozenAmt();
        logger.debug("btcEnable=" + btcEnable);
        logger.debug("btcFrozen=" + btcFrozen);
        accountFundAssetDB.setBtcFrozen(btcFrozen);
        accountFundAssetDB.setBtcAmount(btcEnable.add(btcFrozen));
        // 买入成交费用比例
        BigDecimal buyBtcFeeRate = BigDecimal.ZERO;
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(exchangePairMoney);
        stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);// 费率
        List<StockRate> feeList = stockRateService.findList(stockRate);
        if (feeList.size() > 0)
        {
            stockRate = feeList.get(0);
            buyBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
        }
        else
        {
            logger.debug("买入数字货币买入成交费用比例有问题");
        }
        logger.debug("buyBtcFeeRate=" + buyBtcFeeRate);
        accountFundAssetDB.setBuyBtcFeeRate(buyBtcFeeRate);
        // 卖出成交费用比例
        BigDecimal sellBtcFeeRate = BigDecimal.ZERO;
        stockRate = new StockRate();
        stockRate.setStockinfoId(exchangePairMoney);
        stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);// 费率
        feeList = stockRateService.findList(stockRate);
        if (feeList.size() > 0)
        {
            stockRate = feeList.get(0);
            sellBtcFeeRate = stockRate.getRate().multiply(BigDecimal.valueOf(100));
        }
        else
        {
            logger.debug("卖出数字货币扣除法定货币的费率有问题");
        }
        logger.debug("sellBtcFeeRate=" + sellBtcFeeRate);
        accountFundAssetDB.setSellBtcFeeRate(sellBtcFeeRate);
        logger.debug("accountWalletFundAssetDB=" + accountFundAssetDB.toString());
        return accountFundAssetDB;
    }
    
    private List<AccountContractAsset> queryAccountContractAssetLists(Long accountId, String accountContractAsset_tab)
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(accountId);
        accountContractAsset.setTableName(accountContractAsset_tab);
        List<AccountContractAsset> accountContractAssetLists = accountContractAssetService.findList(accountContractAsset);
        return accountContractAssetLists;
    }
    
    private List<AccountWalletAsset> queryAccountWalletAssetLists(Long accountId)
    {
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(accountId);
        List<AccountWalletAsset> accountWalletAssetLists = accountWalletAssetService.findList(accountWalletAsset);
        return accountWalletAssetLists;
    }
    
    private List<AccountSpotAsset> queryAccountSpotAssetLists(Long accountId)
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(accountId);
        List<AccountSpotAsset> accountSpotAssetLists = accountSpotAssetService.findList(accountSpotAsset);
        return accountSpotAssetLists;
    }
    
    private List<Long> queryAccountRealDealChangeLists(Timestamp currentDate, String realDealVCoinMoney_tab)
    {
        List<Long> realDealLists = realDealVCoinMoneyService.getChangeAcctListByTimestamp(currentDate, realDealVCoinMoney_tab);
        return realDealLists;
    }
    
    private List<EntrustVCoinMoney> getAccountDoingEntrustVCoinMoneyList(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        EntrustVCoinMoney entity = new EntrustVCoinMoney();
        entity.setAccountId(accountId);// 个人数据
        entity.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entity.setEntrustStockinfoId(exchangePairVCoin);
        entity.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entity.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        return entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyList(entity);
    }
    
    private StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
