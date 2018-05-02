package com.blocain.bitms.quotation.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.config.QuerySqlConfig;
import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.quotation.entity.KLineEntity;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.mapper.KLineEntityRowMapper;
import com.blocain.bitms.quotation.model.RtQuotationInfoMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * 最新撮合行情接口服务
 * <p>File：RtQuotationEngineServiceImpl.java</p>
 * <p>Title: RtQuotationEngineServiceImpl</p>
 * <p>Description:RtQuotationEngineServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 *
 * @version 1.0
 */
@Service
public class RtQuotationEngineServiceImpl implements RtQuotationEngineService
{
    public static final Logger     logger                         = LoggerFactory.getLogger(RtQuotationEngineServiceImpl.class);
    
    @Autowired
    private JdbcTemplate           jdbcTemplate;
    
    @Autowired
    private RtQuotationInfoService rtQuotationInfoService;
    
    public static final String     opQuotationKey                 = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR)
            .append(BitmsConst.OP_RTQUOTATIONINFO).append(BitmsConst.SEPARATOR).toString();
    
    public static final String     QUOTATION_CURRENTDAY_FIRST_KEY = BitmsConst.OP_QUOTATION_CURRENTDAY_FIRST;
    
    public static final String     OP_QUOTATION_AMTSUM24H_KEY     = BitmsConst.OP_QUOTATION_AMTSUM24H;
    
    @Override
    public void pushRtQuotationInfoData(String topic)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        LoggerUtils.logDebug(logger, "开始推送最新撮合行情数据");
        LoggerUtils.logDebug(logger, "最新实时行情开始时间：【" + formatter.format(new Date()) + "】");
        if (StringUtils.isNotBlank(InQuotationConfig.BIZ_CATEGORY))
        {
            Long targetCur = Long.valueOf(InQuotationConfig.BIZ_TARGET);
            Long category = Long.valueOf(InQuotationConfig.BIZ_CATEGORY);
            LoggerUtils.logDebug(logger, "================= BIZ_VIRTUALCUR" + InQuotationConfig.BIZ_CATEGORY);
            LoggerUtils.logDebug(logger, "最新实时行情计算处理开始时间：【" + formatter.format(new Date()) + "】");
            RtQuotationInfo price = rtQuotationInfoService.queryRtQuotationInfo(targetCur, category);
            // 首次启动时，加载行情开盘价，最高价，最低价
            if (!InQuotationConsts.CACHE_MAP.containsKey(QUOTATION_CURRENTDAY_FIRST_KEY)) init();
            dealRtQuotationInfoDigit(price);
            price = dealQuotationExtraInfo(price);
            if (null != price && price.getPlatPrice().compareTo(BigDecimal.ZERO) == 1)
            {
                String quotationKey = new StringBuffer(opQuotationKey).append(category).toString();
                RedisUtils.putObject(quotationKey, price, CacheConst.DEFAULT_CACHE_TIME);
            }
            LoggerUtils.logDebug(logger, "最新实时行情计算处理完成时间：【" + formatter.format(new Date()) + "】");
            RtQuotationInfoMessage priceMessage = new RtQuotationInfoMessage();
            LoggerUtils.logDebug(logger, "最新实时行情转json开始时间：【" + formatter.format(new Date()) + "】");
            String content = priceMessage.getMsgInfo(price);
            LoggerUtils.logDebug(logger, "最新实时行情转json完成时间：【" + formatter.format(new Date()) + "】");
            LoggerUtils.logDebug(logger, "================= 推送最新撮合行情数据 ===========================");
            LoggerUtils.logDebug(logger, "topic:" + topic);
            LoggerUtils.logDebug(logger, "content:" + content.toString());
            LoggerUtils.logDebug(logger, "================= 推送最新撮合行情数据 ===========================");
            LoggerUtils.logDebug(logger, "最新实时行情推送开始时间：【" + formatter.format(new Date()) + "】");
            DataPushUtil.doDataPush(content, topic);
            LoggerUtils.logDebug(logger, "最新实时行情推送完成时间：【" + formatter.format(new Date()) + "】");
            LoggerUtils.logDebug(logger, "推送最新撮合行情数据结束");
        }
    }
    
    /**
     * 小数点位数处理
     * @param price
     */
    private void dealRtQuotationInfoDigit(RtQuotationInfo price)
    {
        BigDecimal dealBalance = price.getDealBalance().setScale(InQuotationConfig.QUOTATION_ACCUMULATEBAL_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal dealAmt = price.getDealAmt().setScale(InQuotationConfig.QUOTATION_AMT_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal vcoinAmtSum24h = price.getVcoinAmtSum24h().setScale(InQuotationConfig.QUOTATION_AMT_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal entrustSellOne = price.getEntrustSellOne().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal entrustBuyOne = price.getEntrustBuyOne().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal idxAvgPrice = price.getIdxAvgPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal idxPrice = price.getIdxPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal platPrice = price.getPlatPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal highestPrice = price.getHighestPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_DOWN); // 最高价，做截位处理
        BigDecimal lowestPrice = price.getLowestPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal buyHighestLimitPrice = price.getBuyHighestLimitPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_DOWN);// 最高价，做截位处理
        BigDecimal buyLowestLimitPrice = price.getBuyLowestLimitPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal sellHighestLimitPrice = price.getSellHighestLimitPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_DOWN);// 最高价，做截位处理
        BigDecimal sellLowestLimitPrice = price.getSellLowestLimitPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        BigDecimal premium = price.getPremium().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        price.setDealBalance(dealBalance);
        price.setDealAmt(dealAmt);
        price.setVcoinAmtSum24h(vcoinAmtSum24h);
        price.setEntrustSellOne(entrustSellOne);
        price.setEntrustBuyOne(entrustBuyOne);
        price.setIdxAvgPrice(idxAvgPrice);
        price.setIdxPrice(idxPrice);
        price.setPlatPrice(platPrice);
        price.setHighestPrice(highestPrice);
        price.setLowestPrice(lowestPrice);
        price.setBuyHighestLimitPrice(buyHighestLimitPrice);
        price.setBuyLowestLimitPrice(buyLowestLimitPrice);
        price.setSellHighestLimitPrice(sellHighestLimitPrice);
        price.setSellLowestLimitPrice(sellLowestLimitPrice);
        price.setPremium(premium);
    }
    
    /**
     * 处理行情额外信息
     * 缓存在静态变量的行情
     * KEY: QUOTATION_CURRENTDAY_FIRST_KEY
     * RtQuotationInfo
     * PlatPrice     存的是当日开盘价，一天赋值一次。
     * highestPrice  最高价，实时变化，通过与成交价格比较
     * lowestPrice   最低价，实时变化，通过与成交价比较
     * QuotationTime 行情价格，用于统计24小时成交量，判断是否为最新成交量
     * <p>
     * 涨跌幅，最低价，最高价
     * 1. 开盘价，初次启动的时加载的是当天第一笔成交价格。
     * 2. 24小时后，开盘价，最低价，最高价一开始是同一价格。
     * 开盘价，如果0点有交易则以第一笔交易价格作为开盘价，否则以昨天最后一笔交易作为开盘价。
     * <p>
     * 24小时成交量：
     * 依赖K线统计的24小时成交量，实时部分通过判断行情的时间来加入成交量
     * <p>
     * 只用于前台显示
     *
     * @param price
     * @return
     */
    private RtQuotationInfo dealQuotationExtraInfo(RtQuotationInfo price)
    {
        RtQuotationInfo rtQuotationInfo = null;
        BigDecimal platPrice = price.getPlatPrice();
        // 行情为空，直接返回
        if (platPrice.compareTo(BigDecimal.ZERO) == 0) return price;
        BigDecimal closePrice = BigDecimal.ZERO;
        BigDecimal highestPrice = platPrice;
        BigDecimal lowestPrice = platPrice;
        BigDecimal openPrice = platPrice;
        BigDecimal vcoinAmtSum24h = price.getVcoinAmtSum24h();
        Long curDay = DateUtils.getCurrentDateFirstSec(); // 当天凌晨12点
        double rate = 0;
        // 判断是否已存在缓存行情
        if (InQuotationConsts.CACHE_MAP.containsKey(QUOTATION_CURRENTDAY_FIRST_KEY))
        {
            rtQuotationInfo = (RtQuotationInfo) InQuotationConsts.CACHE_MAP.get(QUOTATION_CURRENTDAY_FIRST_KEY);
            if (curDay.longValue() == DateUtils.getPreMinFirstSec(System.currentTimeMillis(), 0).longValue()
                    && curDay.longValue() > rtQuotationInfo.getQuotationTime().getTime())
            {
                rtQuotationInfo = null;
            }
        }
        // 0点清空map缓存或map缓存本身不存在时会走该方法
        if (null == rtQuotationInfo)
        {
            Long curDayFirstSec = DateUtils.getCurrentDateFirstSec();
            Timestamp startTime = DateUtils.getPreDate(new Timestamp(curDayFirstSec), 1);
            Timestamp endTime = new Timestamp(DateUtils.getCurrentDateFirstSec() - 1);
            // 当日开盘优先顺序，昨日收盘价 > 平滑过来的
            BigDecimal yesterdayClosePrice = getCurDayOpenPrice(startTime, endTime);
            closePrice = yesterdayClosePrice.compareTo(BigDecimal.ZERO) == 0 ? openPrice : yesterdayClosePrice;
            rtQuotationInfo = new RtQuotationInfo();
            rtQuotationInfo.setQuotationTime(new Timestamp(curDay));
            rtQuotationInfo.setPlatPrice(closePrice);
            rtQuotationInfo.setHighestPrice(highestPrice);
            rtQuotationInfo.setLowestPrice(lowestPrice);
            rtQuotationInfo.setVcoinAmtSum24h(vcoinAmtSum24h);
            InQuotationConsts.CACHE_MAP.put(QUOTATION_CURRENTDAY_FIRST_KEY, rtQuotationInfo);
            price.setLowestPrice(lowestPrice);
            price.setHighestPrice(highestPrice);
        }
        else
        {
            // 当前成交价如果比历史最高价高，则作为最新最高价，否则不变
            highestPrice = highestPrice.compareTo(rtQuotationInfo.getHighestPrice()) == 1 ? highestPrice : rtQuotationInfo.getHighestPrice();
            // 当前成交价如果比历史最低价低，则作为最新最低价，否则不变
            lowestPrice = lowestPrice.compareTo(rtQuotationInfo.getLowestPrice()) == -1 ? lowestPrice : rtQuotationInfo.getLowestPrice();
            openPrice = rtQuotationInfo.getPlatPrice();
            // 涨跌幅 = (最新成交价 - 基价)/基价 * 100
            if (openPrice.compareTo(BigDecimal.ZERO) > 0)
            {
                double dPlatPrice = platPrice.doubleValue();
                double dOpenPrice = openPrice.doubleValue();
                rate = (dPlatPrice - dOpenPrice) / dOpenPrice * 100;
            }
            rtQuotationInfo.setHighestPrice(highestPrice);
            rtQuotationInfo.setLowestPrice(lowestPrice);
            // 如果行情的24小时K线缓存最新的24小时成交量
            if (vcoinAmtSum24h.compareTo(rtQuotationInfo.getVcoinAmtSum24h()) != 0)
            {
                rtQuotationInfo.setVcoinAmtSum24h(vcoinAmtSum24h);
                InQuotationConsts.CACHE_MAP.put(OP_QUOTATION_AMTSUM24H_KEY, vcoinAmtSum24h);
            }
            // 根据行情时间判断,当前交易是历史成交还是最新成交。如果是最新成交，统计到24小时成交量
            if (price.getQuotationTime().getTime() > rtQuotationInfo.getQuotationTime().getTime())
            {
                BigDecimal amtSum24h = (BigDecimal) InQuotationConsts.CACHE_MAP.get(OP_QUOTATION_AMTSUM24H_KEY);
                amtSum24h = amtSum24h == null ? price.getVcoinAmtSum24h() : amtSum24h;
                price.setVcoinAmtSum24h(amtSum24h.add(price.getDealAmt()));
                InQuotationConsts.CACHE_MAP.put(OP_QUOTATION_AMTSUM24H_KEY, price.getVcoinAmtSum24h());
                // 更新行情时间
                rtQuotationInfo.setQuotationTime(price.getQuotationTime());
                InQuotationConsts.CACHE_MAP.put(QUOTATION_CURRENTDAY_FIRST_KEY, rtQuotationInfo);
            }
            else
            {
                BigDecimal amtSum24h = (BigDecimal) InQuotationConsts.CACHE_MAP.get(OP_QUOTATION_AMTSUM24H_KEY);
                amtSum24h = amtSum24h == null ? price.getVcoinAmtSum24h() : amtSum24h;
                price.setVcoinAmtSum24h(amtSum24h);
            }
            price.setLowestPrice(lowestPrice);
            price.setHighestPrice(highestPrice);
            price.setRange(rate);
        }
        return price;
    }
    
    /**
     * 如果map缓存中不存在行情,表示是首次启动或启动后相关查询一直是空表,将会执行该方法
     */
    private void init()
    {
        // 从成交流水表中取出当天0点至今时间内的当天最低，最高价，行情时间等，
        Timestamp kLineTimeStart = new Timestamp(DateUtils.getCurrentDateFirstSec());
        // 1. 从K线数据表去当天0点数据
        Timestamp kLineTimeEnd = new Timestamp(System.currentTimeMillis());
        List<KLineEntity> kLineList = (List<KLineEntity>) jdbcTemplate.query(QuerySqlConfig.SQL_GET_KLINE_START, new KLineEntityRowMapper(), kLineTimeStart, kLineTimeEnd,
                kLineTimeStart, kLineTimeEnd, kLineTimeStart, kLineTimeEnd, kLineTimeStart, kLineTimeEnd);
        if (CollectionUtils.isNotEmpty(kLineList))
        {
            KLineEntity kLineEntity = kLineList.get(0);
            // k线收盘价不为0，则取K线收盘价为基价，否则取当天开盘价为基价
            Timestamp kLineStart = new Timestamp(DateUtils.getCurrentDateFirstSec());
            BigDecimal closePrice = getBasePrice(kLineStart);
            closePrice = closePrice.compareTo(BigDecimal.ZERO) == 0 ? kLineEntity.getOpenPrice() : closePrice;
            RtQuotationInfo price = new RtQuotationInfo();
            price.setQuotationTime(kLineEntity.getQuotationTime());
            price.setPlatPrice(closePrice.setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP));
            price.setHighestPrice(kLineEntity.getHighestPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP));
            price.setLowestPrice(kLineEntity.getLowestPrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP));
            price.setVcoinAmtSum24h(BigDecimal.ZERO);
            InQuotationConsts.CACHE_MAP.put(QUOTATION_CURRENTDAY_FIRST_KEY, price);
            InQuotationConsts.CACHE_MAP.put(OP_QUOTATION_AMTSUM24H_KEY, BigDecimal.ZERO);
        }
    }
    
    /**
     * 获取涨跌幅基价
     * 从K线表读取当天0点数据，以其收盘价作基价，如果K线数据不存在，返回默认基价0
     */
    private BigDecimal getBasePrice(Timestamp kLineTimeStart)
    {
        BigDecimal closePrice = new BigDecimal("0");
        List<KLineEntity> kLine = (List<KLineEntity>) jdbcTemplate.query(QuerySqlConfig.SQL_GET_FIRST_KLINE, new KLineEntityRowMapper(), kLineTimeStart);
        if (CollectionUtils.isNotEmpty(kLine))
        {
            closePrice = kLine.get(0).getClosePrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        }
        return closePrice;
    }
    
    /**
     * 获取当天开盘价
     *  取自昨日最后一笔交易价格作为当天的开盘价
     * @return
     */
    private BigDecimal getCurDayOpenPrice(Timestamp startTime, Timestamp endTime)
    {
        // 从成交流水表中取出当天0点至今时间内的当天最低，最高价，行情时间等，
        BigDecimal openPrice = BigDecimal.ZERO;
        List<KLineEntity> kLineList = (List<KLineEntity>) jdbcTemplate.query(QuerySqlConfig.SQL_GET_KLINE_START, new KLineEntityRowMapper(), startTime, endTime, startTime,
                endTime, startTime, endTime, startTime, endTime);
        if (CollectionUtils.isNotEmpty(kLineList)) openPrice = kLineList.get(0).getClosePrice().setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP);
        else openPrice = getBasePrice(startTime);
        return openPrice;
    }
}
