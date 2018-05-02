package com.blocain.bitms.quotation.service;

import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.quotation.entity.Quotation;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.mapper.RtQuotationInfoMapper;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.mapper.RealDealVCoinMoneyMapper;
import com.google.zxing.client.result.BizcardResultParser;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 实时行情接口服务
 * <p>File：RtQuotationInfoServiceImpl.java</p>
 * <p>Title: RtQuotationInfoServiceImpl</p>
 * <p>Description:RtQuotationInfoServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class RtQuotationInfoServiceImpl implements RtQuotationInfoService
{
    public static final Logger         logger         = LoggerFactory.getLogger(RtQuotationInfoServiceImpl.class);
    
    public static final String         opQuotationKey = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR)
            .append(BitmsConst.OP_RTQUOTATIONINFO).append(BitmsConst.SEPARATOR).toString();
    
    protected RealDealVCoinMoneyMapper realDealVCoinMoneyMapper;
    
    @Autowired
    private RtQuotationInfoMapper      rtQuotationInfoMapper;
    
    @Autowired
    private StockInfoService           stockInfoService;
    
    @Autowired
    private QuotationService           quotationService;
    
    @Autowired
    public RtQuotationInfoServiceImpl(RealDealVCoinMoneyMapper realDealVCoinMoneyMapper)
    {
        this.realDealVCoinMoneyMapper = realDealVCoinMoneyMapper;
    }
    
    /**
     * 最新实时行情取数逻辑
     * 1. 交易流水有数据，则取最新一笔交易流水成交价
     * 2. 没有交易流水数据，则取指数行情作为指导价。
     * 3. 指数行情价取不到，则为默认值0
     * @return
     */
    @Override
    public RtQuotationInfo queryRtQuotationInfo(Long baseCur, Long bizCategoryId)
    {
        return getRtQuotationInfoFromDB(bizCategoryId);
    }
    
    @Override
    public RtQuotationInfo queryRtQuotationInfoFromCache(Long baseCur, Long bizCategoryId)
    {
        String quotationKey = new StringBuffer(opQuotationKey).append(bizCategoryId).toString();
        RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
        logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: quotationKey-" + quotationKey);
        if (null == rtQuotationInfo)
        {
            logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: 缓存中不存在" + bizCategoryId + "行情，尝试从数据库获取行情");
            rtQuotationInfo = getRtQuotationInfoFromDB(bizCategoryId);
        }
        return rtQuotationInfo;
    }
    
    /**
     * 从数据中拉取实时行情
     * @param   bizCategoryId  数字货币
     * @return
     */
    private RtQuotationInfo getRtQuotationInfoFromDB(Long bizCategoryId)
    {
        // 获取外部指数查询参数，
        Quotation quotationParam = getQueryQuotationParam(bizCategoryId);
        Quotation quotation = quotationService.findQuotationByLastTime(quotationParam);
        if (null != quotation && quotation.getIdxPriceAvg().compareTo(BigDecimal.ZERO) == 1)
        {
            // 缓存外部指数查询的起始位置，避免全表扫描
            cacheStartLocation(quotation, bizCategoryId);
        }
        String tblName = getStockInfo(bizCategoryId).getTableRealDeal();
        RealDealVCoinMoney realDealVCoinMoney = realDealVCoinMoneyMapper.queryRealDealVCoinMoney(tblName);
        logger.debug("★☆★☆★☆★☆★☆ [getRtQuotationInfoFromDB]: 平台成交行情查询表:" + tblName);
        RtQuotationInfo rtQuotationInfo = dealResult(quotation, realDealVCoinMoney, bizCategoryId);
        return rtQuotationInfo;
    }
    
    /**
     * 根据外部指数行情和实时成交行情生成对应的实时行情数据
     * 1. 实时成交行情为空，则以外部指数行情作为指导价
     * 2. 上下限价格处理，基于加权指数行情
     * 3.  行情涨跌幅处理
     * @param  quotation             最新外部指数行情
     * @param  realDealVCoinMoney    最新实时成交行情
     * @param  exchangePairMoneyCode 虚拟货币
     * @return rtQuotationInfo       最新实时行情
     */
    public RtQuotationInfo dealResult(Quotation quotation, RealDealVCoinMoney realDealVCoinMoney, Long exchangePairMoneyCode)
    {
        RtQuotationInfo rtQuotationInfo = null;
        // 涨跌幅默认为涨
        String upDown = InQuotationConsts.RTQUOTATION_RANGE_ZERO;
        String upDownIdx = InQuotationConsts.RTQUOTATION_RANGE_ZERO;
        // 获取指数均价,指数价,平台成交价,计算价格上下限及价格精度处理
        rtQuotationInfo = calLimitPrice(quotation, realDealVCoinMoney, exchangePairMoneyCode);
        // 从缓存中获取上次内部行情价。
        String quotationKey = new StringBuffer(opQuotationKey).append(exchangePairMoneyCode).toString();
        RtQuotationInfo lastPrice = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
        if (null != lastPrice)
        {
            double tail = rtQuotationInfo.getPlatPrice().doubleValue() - lastPrice.getPlatPrice().doubleValue();
            double tailIdx = rtQuotationInfo.getIdxAvgPrice().doubleValue() - lastPrice.getIdxAvgPrice().doubleValue();
            if (tail == 0) upDown = lastPrice.getUpDown();
            else upDown = tail > 0 ? InQuotationConsts.RTQUOTATION_RANGE_UP : InQuotationConsts.RTQUOTATION_RANGE_DOWN;
            if (tailIdx == 0) upDownIdx = lastPrice.getUpDownIdx();
            else upDownIdx = tailIdx > 0 ? InQuotationConsts.RTQUOTATION_RANGE_UP : InQuotationConsts.RTQUOTATION_RANGE_DOWN;
        }
        rtQuotationInfo.setUpDown(upDown);
        rtQuotationInfo.setUpDownIdx(upDownIdx);
        if (null != realDealVCoinMoney) rtQuotationInfo.setDirect(realDealVCoinMoney.getDealDirect());
        return rtQuotationInfo;
    }
    
    /**
     * 上下限价格计算方法
     *  基于外部指数加权价计算。
     * 精度处理说明：
     *    最低价，四舍五入
     *    最高价，截位处理
     * @param  quotation
     * @param  realDealVCoinMoney
     * @param  exchangePairMoneyCode
     * @return
     */
    private RtQuotationInfo calLimitPrice(Quotation quotation, RealDealVCoinMoney realDealVCoinMoney, Long exchangePairMoneyCode)
    {
        // 初始化行情信息
        RtQuotationInfo rtQuotationInfo = initRtQuotationInfo();
        StockInfo stockInfo = getStockInfo(exchangePairMoneyCode);
        if (stockInfo != null && stockInfo.getStockType().equalsIgnoreCase(FundConsts.STOCKTYPE_PURESPOT))
            rtQuotationInfo = calPureSpotLimitPrice(stockInfo, rtQuotationInfo, realDealVCoinMoney);
        else if (stockInfo != null && stockInfo.getStockType().equalsIgnoreCase(FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            rtQuotationInfo = calLeveragedSpotLimitPrice(quotation, stockInfo, rtQuotationInfo, realDealVCoinMoney);
        else if (stockInfo != null && stockInfo.getStockType().equalsIgnoreCase(FundConsts.STOCKTYPE_CONTRACTSPOT))
            rtQuotationInfo = calLeveragedSpotLimitPrice(quotation, stockInfo, rtQuotationInfo, realDealVCoinMoney);
        return rtQuotationInfo;
    }
    
    /**
     * 纯正现货价格计算
     *  成交价 取自成交流水，没有成交流水就直接为0
     *  限价 基于盘口的买1，卖1计算限价
     *   买价最低: 根据单笔精度处理
     *   买价最高：根据盘口买1 * 上浮比例，没有盘口设置为默认最大99999999
     *
     *   卖价最低：根据盘口卖1 * 下调比例，没有盘口设置为默认最小，根据精度处理
     *   卖价最高：设置为默认最大99999999
     *
     * @param  realDealVCoinMoney     成交流水
     * @param  rtQuotationInfo        初始化行情
     * @param  stockInfo              币对信息
     * @return
     */
    private RtQuotationInfo calPureSpotLimitPrice(StockInfo stockInfo, RtQuotationInfo rtQuotationInfo, RealDealVCoinMoney realDealVCoinMoney)
    {
        RtQuotationInfo extraInfo = getQuotationExtraInfo(stockInfo.getId());
        // 单笔最高买入价
        BigDecimal buyMaxPrice = new BigDecimal(99999999);
        // 单笔最低买入价
        BigDecimal buyMinPrice = BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyPricePrecision())));
        // 单笔最低卖出价
        BigDecimal sellMinPrice = BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellPricePrecision())));
        // 单笔最高卖出价
        BigDecimal sellMaxPrice = new BigDecimal(99999999);
        BigDecimal entrustBuy = extraInfo.getEntrustBuyOne();
        BigDecimal entrustSell = extraInfo.getEntrustSellOne();
        // 盘口买入价存在，则买入最高价 = 买1 * 小限价上浮比例
        if (entrustBuy.compareTo(BigDecimal.ZERO) == 1) buyMaxPrice = entrustBuy.multiply(BigDecimal.ONE.add(stockInfo.getBuyMinLimitPriceUpPercent()));
        // 盘口卖出价存在且下调比例大于0，则卖出最低价 = 卖1 * 小限价下调比例
        if (entrustSell.compareTo(BigDecimal.ZERO) == 1 && BigDecimal.ONE.subtract(stockInfo.getSellMinLimitPriceDownPercent()).compareTo(BigDecimal.ZERO) == 1)
            sellMinPrice = entrustSell.multiply(BigDecimal.ONE.subtract(stockInfo.getSellMinLimitPriceDownPercent()));
        Timestamp quotationTime = realDealVCoinMoney == null ? new Timestamp(0L) : new Timestamp(realDealVCoinMoney.getDealTime().getTime());
        BigDecimal dealAmt = realDealVCoinMoney == null ? BigDecimal.ZERO : realDealVCoinMoney.getDealAmt();
        BigDecimal dealBalance = realDealVCoinMoney == null ? BigDecimal.ZERO : realDealVCoinMoney.getDealBalance();
        BigDecimal platPrice = realDealVCoinMoney == null ? BigDecimal.ZERO : realDealVCoinMoney.getDealPrice();
        rtQuotationInfo.setBuyLowestLimitPrice(buyMinPrice);
        rtQuotationInfo.setBuyHighestLimitPrice(buyMaxPrice);
        rtQuotationInfo.setSellLowestLimitPrice(sellMinPrice);
        rtQuotationInfo.setSellHighestLimitPrice(sellMaxPrice);
        rtQuotationInfo.setEntrustBuyOne(entrustBuy);
        rtQuotationInfo.setEntrustSellOne(entrustSell);
        rtQuotationInfo.setVcoinAmtSum24h(extraInfo.getVcoinAmtSum24h());
        rtQuotationInfo.setHighestPrice(extraInfo.getHighestPrice());
        rtQuotationInfo.setLowestPrice(extraInfo.getLowestPrice());
        rtQuotationInfo.setQuotationTime(quotationTime);
        rtQuotationInfo.setDealAmt(dealAmt);
        rtQuotationInfo.setDealBalance(dealBalance);
        rtQuotationInfo.setPlatPrice(platPrice);
        return rtQuotationInfo;
    }
    
    /**
     * 杠杆现货价格计算
     *  成交价
     *    取自成交流水，没有成交流水，以外部指数均价为指导价
     *
     *  限价 基于盘口的买1，卖1计算限价
     *   买价最低: 根据单笔精度处理
     *   买价最高：根据盘口买1 * 上浮比例，没有盘口设置为默认最大99999999
     *
     *   卖价最低：根据盘口卖1 * 下调比例，没有盘口设置为默认最小，根据精度处理
     *   卖价最高：设置为默认最大99999999
     *
     * @param  realDealVCoinMoney     成交流水
     * @param  rtQuotationInfo        初始化行情
     * @param  stockInfo              币对信息
     * @return
     */
    private RtQuotationInfo calLeveragedSpotLimitPrice(Quotation quotation, StockInfo stockInfo, RtQuotationInfo rtQuotationInfo, RealDealVCoinMoney realDealVCoinMoney)
    {
        BigDecimal indexAvg = quotation == null ? BigDecimal.ZERO : quotation.getIdxPriceAvg();
        BigDecimal idxPrice = quotation == null ? BigDecimal.ZERO : quotation.getIdxPrice();
        BigDecimal buyMinPrice = BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyPricePrecision())));
        BigDecimal buyMaxPrice = BigDecimal.ZERO;
        BigDecimal sellMinPrice = BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellPricePrecision())));
        BigDecimal sellMaxPrice = BigDecimal.ZERO;
        Timestamp quotationTime = realDealVCoinMoney == null ? new Timestamp(0L) : new Timestamp(realDealVCoinMoney.getDealTime().getTime());
        BigDecimal dealAmt = realDealVCoinMoney == null ? BigDecimal.ZERO : realDealVCoinMoney.getDealAmt();
        BigDecimal dealBalance = realDealVCoinMoney == null ? BigDecimal.ZERO : realDealVCoinMoney.getDealBalance();
        BigDecimal platPrice = realDealVCoinMoney == null ? BigDecimal.ZERO : realDealVCoinMoney.getDealPrice();
        // 成交价格为0，以外部均价作为指导价
        platPrice = platPrice.compareTo(BigDecimal.ZERO) == 0 ? indexAvg : platPrice;
        RtQuotationInfo extraInfo = getQuotationExtraInfo(stockInfo.getId());
        BigDecimal entrustSell = extraInfo.getEntrustSellOne();
        BigDecimal entrustBuy = extraInfo.getEntrustBuyOne();
        // 盘口价格优先顺序：盘口 -- 成交价 -- 外部指数均价
        entrustSell = entrustSell.compareTo(BigDecimal.ZERO) == 0 ? platPrice : entrustSell;
        entrustBuy = entrustBuy.compareTo(BigDecimal.ZERO) == 0 ? platPrice : entrustBuy;
        BigDecimal premium = BigDecimal.ZERO;
        BigDecimal premiumRate = BigDecimal.ZERO;
        // 外部指数均价存在
        if (indexAvg.compareTo(BigDecimal.ZERO) == 1)
        {
            // 买入最低价
            if (BigDecimal.ONE.subtract(stockInfo.getBuyMaxLimitPriceDownPercent()).compareTo(BigDecimal.ZERO) == 1)
                buyMinPrice = indexAvg.multiply(BigDecimal.ONE.subtract(stockInfo.getBuyMaxLimitPriceDownPercent()));
            // 买入最高价
            buyMaxPrice = indexAvg.multiply(BigDecimal.ONE.add(stockInfo.getBuyMaxLimitPriceUpPercent()));
            // 卖出最低价
            if (BigDecimal.ONE.subtract(stockInfo.getSellMaxLimitPriceDownPercent()).compareTo(BigDecimal.ZERO) == 1)
                sellMinPrice = indexAvg.multiply(BigDecimal.ONE.subtract(stockInfo.getSellMaxLimitPriceDownPercent()));
            // 卖出最高价
            sellMaxPrice = indexAvg.multiply(BigDecimal.ONE.add(stockInfo.getSellMaxLimitPriceUpPercent()));
            // 溢价=最新成交价-风控基价
            premium = platPrice.subtract(indexAvg);
            //溢价率=溢价/风控基价
            premiumRate = premium.divide(indexAvg,4,BigDecimal.ROUND_HALF_UP);
        }
        rtQuotationInfo.setBuyLowestLimitPrice(buyMinPrice);
        rtQuotationInfo.setBuyHighestLimitPrice(buyMaxPrice);
        rtQuotationInfo.setSellLowestLimitPrice(sellMinPrice);
        rtQuotationInfo.setSellHighestLimitPrice(sellMaxPrice);
        rtQuotationInfo.setIdxAvgPrice(indexAvg);
        rtQuotationInfo.setIdxPrice(idxPrice);
        rtQuotationInfo.setPlatPrice(platPrice);
        rtQuotationInfo.setQuotationTime(quotationTime);
        rtQuotationInfo.setDealAmt(dealAmt);
        rtQuotationInfo.setDealBalance(dealBalance);
        rtQuotationInfo.setEntrustBuyOne(entrustBuy);
        rtQuotationInfo.setEntrustSellOne(entrustSell);
        rtQuotationInfo.setVcoinAmtSum24h(extraInfo.getVcoinAmtSum24h());
        rtQuotationInfo.setHighestPrice(extraInfo.getHighestPrice());
        rtQuotationInfo.setLowestPrice(extraInfo.getLowestPrice());
        rtQuotationInfo.setPremium(premium);
        rtQuotationInfo.setPremiumRate(premiumRate);
        return rtQuotationInfo;
    }

    /**
     * 获取最新行情价参数
     * 1. 从缓存获取上次行情信息
     * 2. 不存在上次行情信息，则初始化行情参数
     * @return
     */
    private Quotation getQueryQuotationParam(Long bizCategoryId)
    {
        Quotation quotation = new Quotation();
        String quotationKey = new StringBuffer(opQuotationKey).append(bizCategoryId).toString();
        Quotation param = (Quotation) InQuotationConsts.CACHE_MAP.get(quotationKey);
        if (null == param)
        {
            StockInfo stockInfo = getStockInfo(bizCategoryId);
            if (null == stockInfo) throw new BusinessException(1000, "获取不到业务品种：【" + bizCategoryId + "】信息");
            quotation.setStockId(stockInfo.getQuotationStockinfoId());
            quotation.setId(0L);
        }
        else
        {
            quotation.setStockId(param.getStockId());
            quotation.setId(param.getId());
        }
        return quotation;
    }
    
    /**
     * 缓存外部行情起始位置，避免全表扫描
     * @param quotation
     */
    private void cacheStartLocation(Quotation quotation, Long exchangePairMoneyCode)
    {
        if (quotation != null && quotation.getIdxPrice().compareTo(BigDecimal.ZERO) == 1)
        {
            String quotationKey = new StringBuffer(opQuotationKey).append(exchangePairMoneyCode).toString();
            InQuotationConsts.CACHE_MAP.put(quotationKey, quotation);
        }
    }
    
    /**
     * 初始化 实时行情信息
     * @return
     */
    private RtQuotationInfo initRtQuotationInfo()
    {
        RtQuotationInfo rtQuotationInfo = new RtQuotationInfo();
        rtQuotationInfo.setQuotationTime(new Timestamp(0L));
        rtQuotationInfo.setIdxPrice(BigDecimal.ZERO);
        rtQuotationInfo.setPlatPrice(BigDecimal.ZERO);
        rtQuotationInfo.setIdxAvgPrice(BigDecimal.ZERO);
        rtQuotationInfo.setEntrustSellOne(BigDecimal.ZERO);
        rtQuotationInfo.setEntrustBuyOne(BigDecimal.ZERO);
        rtQuotationInfo.setDealAmt(BigDecimal.ZERO);
        rtQuotationInfo.setDealBalance(BigDecimal.ZERO);
        rtQuotationInfo.setVcoinAmtSum24h(BigDecimal.ZERO);
        rtQuotationInfo.setBuyHighestLimitPrice(new BigDecimal(99999999));
        rtQuotationInfo.setBuyLowestLimitPrice(BigDecimal.ZERO);
        rtQuotationInfo.setSellHighestLimitPrice(new BigDecimal(99999999));
        rtQuotationInfo.setSellLowestLimitPrice(BigDecimal.ZERO);
        rtQuotationInfo.setDirect("spotBuy");
        rtQuotationInfo.setUpDown(InQuotationConsts.RTQUOTATION_RANGE_UP);
        rtQuotationInfo.setUpDownIdx(InQuotationConsts.RTQUOTATION_RANGE_UP);
        rtQuotationInfo.setRange(0);
        rtQuotationInfo.setPremium(BigDecimal.ZERO);
        rtQuotationInfo.setPremiumRate(BigDecimal.ZERO);
        return rtQuotationInfo;
    }
    
    private StockInfo getStockInfo(Long bizCategoryId)
    {
        StockInfo stockInfo = null;
        StockInfo queryParam = new StockInfo();
        queryParam.setId(bizCategoryId);
        List<StockInfo> list = stockInfoService.findList(queryParam);
        if (CollectionUtils.isNotEmpty(list)) stockInfo = list.get(0);
        return stockInfo;
    }
    
    /**
     * 获取行情额外信息
     * 买1，卖1,24小时成交量，最高价，最低价
     * @param exchangePairMoneyCode
     * @return
     */
    private RtQuotationInfo getQuotationExtraInfo(Long exchangePairMoneyCode)
    {
        String tblEntrustName = getStockInfo(exchangePairMoneyCode).getTableEntrust();
        String tblKlineName = getStockInfo(exchangePairMoneyCode).getTableQuotationKline();
        RtQuotationInfo rtQuotationInfo = rtQuotationInfoMapper.getQuotationExtraInfo(tblEntrustName, tblKlineName);
        return rtQuotationInfo;
    }
}
