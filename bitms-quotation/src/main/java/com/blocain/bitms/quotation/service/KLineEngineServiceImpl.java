package com.blocain.bitms.quotation.service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.config.QuerySqlConfig;
import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.quotation.entity.KLine;
import com.blocain.bitms.quotation.entity.KLineEntity;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.mapper.KLineEntityRowMapper;
import com.blocain.bitms.quotation.mapper.KLineRowMapper;
import com.blocain.bitms.quotation.model.KlineMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.tools.utils.RedisUtils;

/**
 * K 线图服务
 * <p>File：KLineServiceImpl.java</p>
 * <p>Title: KLineServiceImpl</p>
 * <p>Description:KLineServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 *
 * @version 1.0
 */
@Service
public class KLineEngineServiceImpl implements KLineEngineService
{
    public static final Logger logger            = LoggerFactory.getLogger(KLineEngineServiceImpl.class);
    
    public static final String KLINE_DATA_KEY    = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_KLINE)
            .append(BitmsConst.SEPARATOR).append(InQuotationConfig.BIZ_CATEGORY).append(BitmsConst.SEPARATOR).toString();
    
    public static final String opQuotationKey    = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RTQUOTATIONINFO)
            .append(BitmsConst.SEPARATOR).toString();
    
    public static final String STARTLOCATION_KEY = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(InQuotationConfig.BIZ_CATEGORY)
            .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_KLINE).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_KLINE_STARTLOCATION).toString();
    
    @Autowired
    private JdbcTemplate       jdbcTemplate;
    
    @Override
    public void buildKLineDate()
    {
        // 0.判断redis中k线缓存标志是否为空，为空则清空所有相关K线redis缓存和map缓存，并设置K线缓存标志不为空(false)。
        String reloadKey = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_KLINE).append(BitmsConst.SEPARATOR)
                .append(InQuotationConfig.BIZ_CATEGORY).append(BitmsConst.SEPARATOR).append(InQuotationConsts.RELOAD_KLINEDATA).toString();
        if (RedisUtils.getObject(reloadKey) == null)
        {
            clearKLineCache();
            RedisUtils.putObject(reloadKey, false, CacheConst.TWENTYFOUR_HOUR_CACHE_TIME);
        }
        // 1. 服务初始化:补全K线数据，缓存K线数据
        if (!InQuotationConsts.CAMHE_RTKLINE_MAP.containsKey(InQuotationConsts.KLINE_RTKLINEENTITY)) initialize();
        // 2. 处理实时K线数据
        dealRTKLineData();
        // 3. 拼接处理K线
        doKLineDataListBuilde();
    }
    
    private void clearKLineCache()
    {
        InQuotationConsts.CAMHE_RTKLINE_MAP.clear();
        InQuotationConsts.CAMHE_KLINELIST_MAP.clear();
        RedisUtils.del(InQuotationConfig.TOPIC_KLINE_1M);
        RedisUtils.del(InQuotationConfig.TOPIC_KLINE_5M);
        RedisUtils.del(InQuotationConfig.TOPIC_KLINE_15M);
        RedisUtils.del(InQuotationConfig.TOPIC_KLINE_30M);
        RedisUtils.del(InQuotationConfig.TOPIC_KLINE_HOUR);
        RedisUtils.del(InQuotationConfig.TOPIC_KLINE_DAY);
    }
    
    /**
     * @param topic  主题
     */
    @Override
    public void pushKLineData(String topic)
    {
        String content = getKLineDataFromRedis(topic);
        DataPushUtil.doDataPush(content, topic);
    }
    
    /**
     * 从Redis中获取对应主题K线数据
     * Redis键值区分业务类别
     *
     * @param topic
     * @return
     */
    private String getKLineDataFromRedis(String topic)
    {
        String kLineKey = new StringBuffer(KLINE_DATA_KEY).append(topic).toString();
        List<KLine> list = (List<KLine>) RedisUtils.getObject(kLineKey);
        // 获取不到缓存数据，情况缓存
        if (CollectionUtils.isEmpty(list)) { return null; }
        KlineMessage klineMessage = new KlineMessage();
        String content = klineMessage.getMsgInfo(list);
        return content;
    }
    
    /**
     * 服务初始化
     * 1. 补全K线数据
     * 2. 初始化K线服务数据
     */
    private void initialize()
    {
        long curtime = System.currentTimeMillis();
        // 1. 补全K线数据
        completeKlineTbl(curtime);
        // 2. 初始化K线服务数据
        initializeServiceData(curtime);
    }
    
    /**
     * 首次启动时补全K线表，逻辑如下：
     * 首先查询K线表，取最新一条记录，并构造时间区间：最新记录的displaytime(A)到当前分钟起始时刻(B)
     * 如果K线表为空，则构造时间区间：0 （A）到当前分钟起始时刻(B)
     * 取时间区间AB,从成交表查询构造出K线数据，插入到K线表中。
     * 如果成交表查询结果为空，表示无历史成交记录，则不需补全，只需等待最新交易产生后入库即可。
     */
    private void completeKlineTbl(Long curtime)
    {
        // 查询成交表的结束时间
        Long lastMin = curtime - curtime % 60000;
        // 查询成交表的起始时间
        Long firstMin = 0l;
        List<KLineEntity> list = (List<KLineEntity>) jdbcTemplate.query(QuerySqlConfig.SQL_GET_KLINEDATAINFO, new KLineEntityRowMapper());
        if (!CollectionUtils.isEmpty(list))
        {
            KLineEntity lastKLine = list.get(0);
            // 缓存最新的K线表记录
            InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M, lastKLine);
            Long kLastMin = lastKLine.getDisplayTime().getTime();
            if (lastMin > kLastMin + 60000)
            {
                firstMin = kLastMin + 60000;
            }
            else
            {
                // K线表已有当前时间，跳过补全
                return;
            }
        }
        else
        {
            firstMin = 0l;
        }
        // 查询成交表，得到K线数据
        List<KLineEntity> kLineList = jdbcTemplate.query(QuerySqlConfig.SQL_GET_INITKLINEDATAINFO, new KLineEntityRowMapper(), new Timestamp(firstMin),
                new Timestamp(lastMin - 1), new Timestamp(firstMin), new Timestamp(lastMin - 1));
        // 如果是首次启动且K线表为空，作如下处理
        if (firstMin == 0)
        {
            if (CollectionUtils.isEmpty(kLineList))
            {
                // 成交表也为空，直接终止补全,缓存0化的初始K线数据
                InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M, getInitKLine(curtime));
                return;
            }
            else
            {
                // 如果成交表有数据，将成交表最早的一条数据缓存起来。
                InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M, kLineList.get(0));
                // K线表为空时，以成交表最早的一条记录的displaytime作为补全K线的起始时间
                firstMin = kLineList.get(0).getDisplayTime().getTime();
            }
        }
        // K线数据转化并入库，完成补全。（此时klinelist可能为空，在convert方法中会有判断）
        convertKData(kLineList, firstMin, lastMin);
    }
    
    // 首次启动空库时初始化lastkline1m
    private KLineEntity getInitKLine(Long curtime)
    {
        Long timeStart = curtime - curtime % 60000;
        KLineEntity k1 = new KLineEntity();
        k1.setId(0L);
        k1.setQuotationTime(new Timestamp(0L));
        k1.setDisplayTime(new Timestamp(timeStart - 60000));
        k1.setClosePrice(new BigDecimal(0L));
        k1.setOpenPrice(new BigDecimal(0L));
        k1.setHighestPrice(new BigDecimal(0L));
        k1.setLowestPrice(new BigDecimal(0L));
        k1.setDealBal(new BigDecimal(0L));
        k1.setDealAmtSum(new BigDecimal(0L));
        return k1;
    }
    
    /**
     * 将成交表查询到的不连续的K线数据转化为连续的K线数据
     */
    private void convertKData(List<KLineEntity> kLineList, Long timeStart, Long timeEnd)
    {
        KLineEntity tmp = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M);
        int count = (int) ((timeEnd - timeStart) / (1000 * 60));
        // list：即将插入k线表的数据集合
        List<KLineEntity> list = new ArrayList<KLineEntity>();
        /**
         * 循环次数为count,每次循环插入一组数据，插入逻辑如下：
         * 判断成交表klinelist集合的第一条数据是否在本次循环分钟内，
         * 存在,则在list添加该条数据，并在klinelist中移除该数据，
         * 不存在,则插入上一分钟缓存数据（插入前对），
         * 继续下一次循环
         */
        //
        for (int m = 0; m < count; m++)
        {
            KLineEntity kLine = new KLineEntity();
            // 当klinelist元素都被添加到list,则表示之后时间内无成交记录数据，
            // 则不需要再进入下面判断
            if (kLineList != null && (kLineList.size() > 0))
            {
                long time = kLineList.get(0).getDisplayTime().getTime();
                if (time < timeStart + (m + 1) * 60 * 1000 && time >= timeStart + m * 60 * 1000)
                {
                    list.add(kLineList.get(0));
                    tmp = kLineList.get(0);
                    kLineList.remove(0);
                    // 成交表有符合条件的数据，添加成功后直接进入下次循环，，
                    continue;
                }
            }
            // klinelist中无符合条件的数据或者klinelist元素被移除完时都会执行下列方法：向list添加缓存数据
            kLine.setId(0l);
            kLine.setQuotationTime(tmp.getQuotationTime());
            kLine.setDisplayTime(new Timestamp(timeStart + 1000 * 60 * m));
            kLine.setClosePrice(tmp.getClosePrice());
            kLine.setOpenPrice(tmp.getClosePrice());
            kLine.setHighestPrice(tmp.getClosePrice());
            kLine.setLowestPrice(tmp.getClosePrice());
            kLine.setDealBal(new BigDecimal(0L));
            kLine.setDealAmtSum(new BigDecimal(0L));
            list.add(kLine);
            tmp = kLine;
        }
        // 将得到的新的list插入数据库
        jdbcTemplate.batchUpdate(QuerySqlConfig.SQL_INSERT_KLINE, new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                ps.setTimestamp(1, list.get(i).getDisplayTime());
                ps.setTimestamp(2, list.get(i).getQuotationTime());
                ps.setBigDecimal(3, list.get(i).getHighestPrice());
                ps.setBigDecimal(4, list.get(i).getLowestPrice());
                ps.setBigDecimal(5, list.get(i).getOpenPrice());
                ps.setBigDecimal(6, list.get(i).getClosePrice());
                ps.setBigDecimal(7, list.get(i).getDealBal());
                ps.setBigDecimal(8, list.get(i).getDealAmtSum());
            }
            
            @Override
            public int getBatchSize()
            {
                return list.size();
            }
        });
        // 入库成功后将最后插入的一条数据放入缓存
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M, tmp);
    }
    
    /**
     * 首次启动时初始化缓存数据
     */
    public void initializeServiceData(Long curtime)
    {
        // 从K线数据表中加载K线数据list
        List<KLine> kLines1m = getKLineListFromDB(InQuotationConfig.TOPIC_KLINE_1M, InQuotationConsts.KLINE_TIMETYPE_LIST, curtime);
        if (!CollectionUtils.isEmpty(kLines1m)) InQuotationConsts.CAMHE_KLINELIST_MAP.put(InQuotationConfig.TOPIC_KLINE_1M, kLines1m);
        List<KLine> kLines5m = getKLineListFromDB(InQuotationConfig.TOPIC_KLINE_5M, InQuotationConsts.KLINE_TIMETYPE_LIST, curtime);
        if (!CollectionUtils.isEmpty(kLines5m)) InQuotationConsts.CAMHE_KLINELIST_MAP.put(InQuotationConfig.TOPIC_KLINE_5M, kLines5m);
        List<KLine> kLines15m = getKLineListFromDB(InQuotationConfig.TOPIC_KLINE_15M, InQuotationConsts.KLINE_TIMETYPE_LIST, curtime);
        if (!CollectionUtils.isEmpty(kLines15m)) InQuotationConsts.CAMHE_KLINELIST_MAP.put(InQuotationConfig.TOPIC_KLINE_15M, kLines15m);
        List<KLine> kLines30m = getKLineListFromDB(InQuotationConfig.TOPIC_KLINE_30M, InQuotationConsts.KLINE_TIMETYPE_LIST, curtime);
        if (!CollectionUtils.isEmpty(kLines30m)) InQuotationConsts.CAMHE_KLINELIST_MAP.put(InQuotationConfig.TOPIC_KLINE_30M, kLines30m);
        List<KLine> kLines1h = getKLineListFromDB(InQuotationConfig.TOPIC_KLINE_HOUR, InQuotationConsts.KLINE_TIMETYPE_LIST, curtime);
        if (!CollectionUtils.isEmpty(kLines1h)) InQuotationConsts.CAMHE_KLINELIST_MAP.put(InQuotationConfig.TOPIC_KLINE_HOUR, kLines1h);
        List<KLine> kLines1d = getKLineListFromDB(InQuotationConfig.TOPIC_KLINE_DAY, InQuotationConsts.KLINE_TIMETYPE_LIST, curtime);
        if (!CollectionUtils.isEmpty(kLines1d)) InQuotationConsts.CAMHE_KLINELIST_MAP.put(InQuotationConfig.TOPIC_KLINE_DAY, kLines1d);
        // 获取实时K线数据
        KLineEntity kLine1m = getKLineFromDB(InQuotationConfig.TOPIC_KLINE_1M, InQuotationConsts.KLINE_TIMETYPE_LAST, curtime);
        if (kLine1m == null)
        {
            kLine1m = useLastKData(curtime, 1);
        }
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY, kLine1m);
        KLineEntity kLine5m = getKLineFromDB(InQuotationConfig.TOPIC_KLINE_5M, InQuotationConsts.KLINE_TIMETYPE_LAST, curtime);
        if (kLine5m == null)
        {
            kLine5m = useLastKData(curtime, 5);
        }
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_5M, kLine5m);
        KLineEntity kLine15m = getKLineFromDB(InQuotationConfig.TOPIC_KLINE_15M, InQuotationConsts.KLINE_TIMETYPE_LAST, curtime);
        if (kLine15m == null)
        {
            kLine15m = useLastKData(curtime, 15);
        }
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_15M, kLine15m);
        KLineEntity kLine30m = getKLineFromDB(InQuotationConfig.TOPIC_KLINE_30M, InQuotationConsts.KLINE_TIMETYPE_LAST, curtime);
        if (kLine30m == null)
        {
            kLine30m = useLastKData(curtime, 30);
        }
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_30M, kLine30m);
        KLineEntity kLine1h = getKLineFromDB(InQuotationConfig.TOPIC_KLINE_HOUR, InQuotationConsts.KLINE_TIMETYPE_LAST, curtime);
        if (kLine1h == null)
        {
            kLine1h = useLastKData(curtime, 60);
        }
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_1H, kLine1h);
        KLineEntity kLine1d = getKLineFromDB(InQuotationConfig.TOPIC_KLINE_DAY, InQuotationConsts.KLINE_TIMETYPE_LAST, curtime);
        if (kLine1d == null)
        {
            kLine1d = useLastKData(curtime, 60 * 24);
        }
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_1D, kLine1d);
    }
    
    // 取上次缓存的K线数据作为实时缓存数据
    private KLineEntity useLastKData(Long curtime, int n)
    {
        KLineEntity kn = new KLineEntity();
        if (InQuotationConsts.CAMHE_RTKLINE_MAP.containsKey(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M))
        {
            KLineEntity tmp = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M);
            BigDecimal price = tmp.getClosePrice();
            kn.setId(0l);
            kn.setQuotationTime(tmp.getQuotationTime());
            kn.setDisplayTime(new Timestamp(curtime - curtime % (60000 * n)));
            // LoggerUtils.logInfo(logger, "清空缓存或首次启动时的第一个实时K线缓存：" + n + " :" + kn.getDisplayTime());
            kn.setClosePrice(price);
            kn.setOpenPrice(price);
            kn.setHighestPrice(price);
            kn.setLowestPrice(price);
            kn.setDealBal(BigDecimal.ZERO);
            kn.setDealAmtSum(BigDecimal.ZERO);
            kn.setAccumulatedBal(BigDecimal.ZERO);
            kn.setAccumulatedAmt(BigDecimal.ZERO);
        }
        return kn;
    }
    
    /**
     * 从成交表查询某时间段真实K线数据
     */
    private KLineEntity getKLineFromDB(String topic, String calType, Long curtime)
    {
        HashMap<String, Timestamp> kLineTimeMap = null;
        // 获取K线表查询起始时间
        kLineTimeMap = calKLineTime(topic, curtime, calType);
        Timestamp start = kLineTimeMap.get(InQuotationConsts.KLINE_TIMETYPE_PUSH_START);
        Timestamp end = kLineTimeMap.get(InQuotationConsts.KLINE_TIMETYPE_PUSH_END);
        KLineEntity kLineEntity = null;
        List<KLineEntity> kList = jdbcTemplate.query(QuerySqlConfig.SQL_GET_KLINE_START, new KLineEntityRowMapper(), start, end, start, end, start, end, start, end);
        if (!CollectionUtils.isEmpty(kList))
        {
            kLineEntity = kList.get(0);
            kLineEntity.setDisplayTime(start);
        }
        return kLineEntity;
    }
    
    /**
     * 处理实时K线数据
     * 1. 满足K线时间刻度，处理到K线列表数据
     * 2. 不满足K线时间刻度，追加到实时K线数据
     */
    private void dealRTKLineData()
    {
        // 取缓存实时1k线，将当前时间，K线时间做比较。
        KLineEntity rtKLineEntity = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_RTKLINEENTITY);
        Long curDisplay = DateUtils.getPreMinFirstSec(System.currentTimeMillis(), 0);
        Timestamp a = new Timestamp(curDisplay);
        Timestamp b = rtKLineEntity.getDisplayTime();
        if (curDisplay > rtKLineEntity.getDisplayTime().getTime())
        {
            if (rtKLineEntity.getClosePrice().compareTo(BigDecimal.ZERO) == 1)
            {
                /**
                 * 如果缓存实时1K线收盘价为0，证明空库，这时候区间刷新时缓实时kn数据将会在 dealRtKlineCacheData()内初始化，
                 * 然后当有新外部行情时，也会将新行情添加到实时1k线，此时缓存的实时1k线收盘价不为0，
                 * 此时再进入新的一分钟时，就会执行下列代码，
                 */
                // 基于交易流水表统计上一分钟完整的K线数据
                KLineEntity kLineEntity = getKLineFromDB(InQuotationConfig.TOPIC_KLINE_1M, null, curDisplay);
                dealKlineListCacheData(kLineEntity);
            }
            else
            {
                // 空库但是满时间刻度时，要更新上分钟K线缓存。以及初始化实时1K线缓存，
                // 这里只更新上1分缓存，实时kn缓存的初始化放在后面的方法执行
                KLineEntity kLineEntityLast = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M);
                Timestamp displayTime = DateUtils.getNextMin(kLineEntityLast.getDisplayTime(), 1);
                kLineEntityLast.setDisplayTime(displayTime);
            }
        }
        // 处理实时K线数据
        dealRtKlineCacheData();
    }
    
    /**
     * 处理各主题K线实时数据
     * 1. 将还不满足刻度的实时K线数据处理到各主题的实时缓存数据中
     * 2. 将满足刻度实时K线数据，进行初始化
     *
     * @return
     */
    private void dealRtKlineCacheData()
    {
        Long curtime = System.currentTimeMillis();
        dealRTKLineEntity(InQuotationConfig.TOPIC_KLINE_1M, curtime, InQuotationConsts.KLINE_RTKLINEENTITY);
        dealRTKLineEntity(InQuotationConfig.TOPIC_KLINE_5M, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_5M);
        dealRTKLineEntity(InQuotationConfig.TOPIC_KLINE_15M, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_15M);
        dealRTKLineEntity(InQuotationConfig.TOPIC_KLINE_30M, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_30M);
        dealRTKLineEntity(InQuotationConfig.TOPIC_KLINE_HOUR, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_1H);
        dealRTKLineEntity(InQuotationConfig.TOPIC_KLINE_DAY, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_1D);
    }
    
    /**
     * 处理实时K线数据
     * 1. 满足时间刻度，初始化K线数据
     * 2. 不满足时间刻度，将实时数据处理到各主题缓存
     *
     * @param topic
     * @param curtime
     */
    private void dealRTKLineEntity(String topic, Long curtime, String rtEntityKey)
    {
        // 满足时间刻度，初始化实时K线数据
        if (checkKLineTime(topic, curtime, rtEntityKey))
        {
            initRTKLineEntity(topic);
        }
        // 如果有新行情，则开始进行行情拼接
        calRTKLineData(topic);
    }
    
    /**
     * 初始化K线实时数据,并缓存
     * 基于上个K线时间刻度的基础初始化实时K线数据
     * 开盘，收盘，最低，最高初始以上一个刻度的收盘价
     * 成交量，成交金额为0
     *
     * @return
     */
    private void initRTKLineEntity(String topic)
    {
        KLineEntity kLineEntityLast = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M);
        KLineEntity kLineEntity = new KLineEntity();
        BigDecimal price = kLineEntityLast.getClosePrice();
        Timestamp displayTime = DateUtils.getNextMin(kLineEntityLast.getDisplayTime(), 1);
        // LoggerUtils.logInfo(logger, "初始化前上分钟K线：" + kLineEntityLast.toString());
        kLineEntity.setDisplayTime(displayTime);
        kLineEntity.setQuotationTime(kLineEntityLast.getQuotationTime());
        kLineEntity.setOpenPrice(price);
        kLineEntity.setClosePrice(price);
        kLineEntity.setLowestPrice(price);
        kLineEntity.setHighestPrice(price);
        kLineEntity.setDealBal(BigDecimal.ZERO);
        kLineEntity.setDealAmtSum(BigDecimal.ZERO);
        kLineEntity.setAccumulatedAmt(BigDecimal.ZERO);
        kLineEntity.setAccumulatedBal(BigDecimal.ZERO);
        String cacheKey = getRTKLineDataCacheKey(topic);
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(cacheKey, kLineEntity);
    }
    
    /**
     * 行情与K线的拼接：
     *
     * @param topic
     */
    private void calRTKLineData(String topic)
    {
        // 最新行情获取来源1：行情推送线程中的缓存最新行情
        String quotationKey = new StringBuffer(opQuotationKey).append(InQuotationConfig.BIZ_CATEGORY).toString();
        RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
        /**
         * 最新行情获取来源2：成交流水表
         * 取数逻辑：初始化K线缓存时，查询成交表时将查询到的最后（最新）一条记录的id缓存起来，
         * 程序运行到这里时，查询大于缓存id且小于等于当前时间的所有记录作为该时间段内最新行情数据
         * 然后与缓存的实时数据拼接即可
         */
        if (null == rtQuotationInfo) { return; }
        KLineEntity kLineEntity = null;
        String cacheKey = getRTKLineDataCacheKey(topic);
        kLineEntity = (KLineEntity) InQuotationConsts.CAMHE_RTKLINE_MAP.get(cacheKey);
        Long curtime = System.currentTimeMillis();
        // 开始拼接外部行情与缓存实时数据的处理
        if (rtQuotationInfo.getQuotationTime().getTime() > kLineEntity.getQuotationTime().getTime())
        {
            BigDecimal price = rtQuotationInfo.getPlatPrice();
            BigDecimal openPrice = kLineEntity.getOpenPrice();
            BigDecimal lowestPrice = kLineEntity.getLowestPrice();
            BigDecimal highestPrice = kLineEntity.getHighestPrice();
            BigDecimal dealBal = kLineEntity.getDealBal();
            BigDecimal dealAmtSum = kLineEntity.getDealAmtSum();
            openPrice = kLineEntity.getDealBal().compareTo(BigDecimal.ZERO) == 0 ? price : openPrice;
            if (lowestPrice.longValue() == 0 || lowestPrice.compareTo(price) == 1)
            {
                lowestPrice = price;
            }
            highestPrice = highestPrice.compareTo(price) == 1 ? highestPrice : price;
            dealAmtSum = dealAmtSum.add(rtQuotationInfo.getDealAmt());
            dealBal = dealBal.add(rtQuotationInfo.getDealBalance());
            kLineEntity.setQuotationTime(rtQuotationInfo.getQuotationTime());
            kLineEntity.setOpenPrice(openPrice);
            kLineEntity.setClosePrice(price);
            kLineEntity.setLowestPrice(lowestPrice);
            kLineEntity.setHighestPrice(highestPrice);
            kLineEntity.setDealAmtSum(dealAmtSum);
            kLineEntity.setDealBal(dealBal);
            InQuotationConsts.CAMHE_RTKLINE_MAP.put(cacheKey, kLineEntity);
        }
    }
    
    /**
     * 累计处理
     * 有两种场景会执行该方法：
     *
     * @param topic
     * @return
     */
    private KLineEntity doAccumulative(String topic, KLineEntity rtKLineEntity)
    {
        KLineEntity kLineEntity = null;
        // 1分钟K线返回null..
        if (InQuotationConfig.TOPIC_KLINE_1M.equalsIgnoreCase(topic)) return kLineEntity;
        // 获取非一分钟区间内最后一分钟真实数据
        KLineEntity kLineEntityLast = rtKLineEntity;
        if (null == rtKLineEntity) kLineEntityLast = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M);
        // 获取缓存的实时k线数据
        String rtKlineKey = getRTKLineDataCacheKey(topic);
        kLineEntity = (KLineEntity) InQuotationConsts.CAMHE_RTKLINE_MAP.get(rtKlineKey);
        // 拼接实时K线与上一分钟K线
        BigDecimal accumulatedAmt = kLineEntity.getAccumulatedAmt() == null ? BigDecimal.ZERO : kLineEntity.getAccumulatedAmt();
        BigDecimal accumulatedBal = kLineEntity.getAccumulatedBal() == null ? BigDecimal.ZERO : kLineEntity.getAccumulatedBal();
        BigDecimal lowestPrice = kLineEntity.getLowestPrice();
        BigDecimal highestPrice = kLineEntity.getHighestPrice();
        // 最低价要注意首次启动空库的场景，首次启动空库时最低价默认为0，非实际数据，故应加以判断
        if (kLineEntityLast.getLowestPrice().longValue() != 0)
        {
            lowestPrice = lowestPrice.compareTo(kLineEntityLast.getLowestPrice()) == -1 ? lowestPrice : kLineEntityLast.getLowestPrice();
        }
        highestPrice = highestPrice.compareTo(kLineEntityLast.getHighestPrice()) == 1 ? highestPrice : kLineEntityLast.getHighestPrice();
        accumulatedAmt = accumulatedAmt.add(kLineEntityLast.getDealAmtSum());
        accumulatedBal = accumulatedBal.add(kLineEntityLast.getDealBal());
        kLineEntity.setQuotationTime(kLineEntityLast.getQuotationTime());
        kLineEntity.setHighestPrice(highestPrice);
        kLineEntity.setLowestPrice(lowestPrice);
        // 开盘价仍沿用原来的，不需设置
        kLineEntity.setClosePrice(kLineEntityLast.getClosePrice());
        kLineEntity.setDealAmtSum(accumulatedAmt);
        kLineEntity.setDealBal(accumulatedBal);
        kLineEntity.setAccumulatedAmt(accumulatedAmt);
        kLineEntity.setAccumulatedBal(accumulatedBal);
        return kLineEntity;
    }
    
    private String getRTKLineDataCacheKey(String topic)
    {
        String cacheKey = null;
        if (InQuotationConfig.TOPIC_KLINE_1M.equalsIgnoreCase(topic)) cacheKey = InQuotationConsts.KLINE_RTKLINEENTITY;
        if (InQuotationConfig.TOPIC_KLINE_5M.equalsIgnoreCase(topic)) cacheKey = InQuotationConsts.KLINE_RTKLINEENTITY_5M;
        if (InQuotationConfig.TOPIC_KLINE_15M.equalsIgnoreCase(topic)) cacheKey = InQuotationConsts.KLINE_RTKLINEENTITY_15M;
        if (InQuotationConfig.TOPIC_KLINE_30M.equalsIgnoreCase(topic)) cacheKey = InQuotationConsts.KLINE_RTKLINEENTITY_30M;
        if (InQuotationConfig.TOPIC_KLINE_HOUR.equalsIgnoreCase(topic)) cacheKey = InQuotationConsts.KLINE_RTKLINEENTITY_1H;
        if (InQuotationConfig.TOPIC_KLINE_DAY.equalsIgnoreCase(topic)) cacheKey = InQuotationConsts.KLINE_RTKLINEENTITY_1D;
        return cacheKey;
    }
    
    /**
     * 时间满一分钟时K线缓存处理
     * 1. 将满足K线刻度(5分钟，15分钟等)数据，加入列表，并保证数据长度，不满足刻度则追加该分钟实际k线
     * 2. 将一分钟K线数据入库，同时更新上一分钟K线缓存
     *
     * @param rtKLineEntity
     */
    private void dealKlineListCacheData(KLineEntity rtKLineEntity)
    {
        KLineEntity kLineEntity = rtKLineEntity;
        // 刻度内未发生任何交易，则取上分钟缓存K线数据
        if (null == kLineEntity)
        {
            kLineEntity = new KLineEntity();
            KLineEntity kLineEntityLast = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M);
            BigDecimal price = kLineEntityLast.getClosePrice();
            Timestamp disPlayTime = DateUtils.getNextMin(kLineEntityLast.getDisplayTime(), 1);
            kLineEntity.setDisplayTime(disPlayTime);
            kLineEntity.setQuotationTime(kLineEntityLast.getQuotationTime());
            kLineEntity.setOpenPrice(price);
            kLineEntity.setClosePrice(price);
            kLineEntity.setLowestPrice(price);
            kLineEntity.setHighestPrice(price);
            kLineEntity.setDealBal(BigDecimal.ZERO);
            kLineEntity.setDealAmtSum(BigDecimal.ZERO);
        }
        // 追加到1k线list,此时尚未初始化实时1k缓存
        appendKlineListCacheData(InQuotationConfig.TOPIC_KLINE_1M, kLineEntity);
        Long curtime = System.currentTimeMillis();
        // 则首先将实时kn缓存补充完整，然后如果满以下刻度，就追加到knlist
        KLineEntity k5Entity = doAccumulative(InQuotationConfig.TOPIC_KLINE_5M, rtKLineEntity);
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_5M, k5Entity);
        if (checkKLineTime(InQuotationConfig.TOPIC_KLINE_5M, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_5M))
        {
            appendKlineListCacheData(InQuotationConfig.TOPIC_KLINE_5M, k5Entity);
        }
        KLineEntity k15Entity = doAccumulative(InQuotationConfig.TOPIC_KLINE_15M, rtKLineEntity);
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_15M, k15Entity);
        if (checkKLineTime(InQuotationConfig.TOPIC_KLINE_15M, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_15M))
        {
            appendKlineListCacheData(InQuotationConfig.TOPIC_KLINE_15M, k15Entity);
        }
        KLineEntity k30Entity = doAccumulative(InQuotationConfig.TOPIC_KLINE_30M, rtKLineEntity);
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_30M, k30Entity);
        if (checkKLineTime(InQuotationConfig.TOPIC_KLINE_30M, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_30M))
        {
            appendKlineListCacheData(InQuotationConfig.TOPIC_KLINE_30M, k30Entity);
        }
        KLineEntity k60Entity = doAccumulative(InQuotationConfig.TOPIC_KLINE_HOUR, rtKLineEntity);
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_1H, k60Entity);
        if (checkKLineTime(InQuotationConfig.TOPIC_KLINE_HOUR, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_1H))
        {
            appendKlineListCacheData(InQuotationConfig.TOPIC_KLINE_HOUR, k60Entity);
        }
        KLineEntity k1dEntity = doAccumulative(InQuotationConfig.TOPIC_KLINE_DAY, rtKLineEntity);
        InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_RTKLINEENTITY_1D, k1dEntity);
        if (checkKLineTime(InQuotationConfig.TOPIC_KLINE_DAY, curtime, InQuotationConsts.KLINE_RTKLINEENTITY_1D))
        {
            appendKlineListCacheData(InQuotationConfig.TOPIC_KLINE_DAY, k1dEntity);
        }
        // 追加完毕，将最新真实1k数据入库，并更新缓存的上分钟k线
        saveKlineData(kLineEntity);
    }
    
    private void saveKlineData(KLineEntity klineData)
    {
        if (null != klineData)
        {
            logger.info("=== 生成K线数据:" + klineData.toString());
            String sql = getSql("insert");
            int iResult = jdbcTemplate.update(sql, new Object[]{klineData.getDisplayTime(), klineData.getQuotationTime(), klineData.getHighestPrice(),
                    klineData.getLowestPrice(), klineData.getOpenPrice(), klineData.getClosePrice(), klineData.getDealBal(), klineData.getDealAmtSum()});
            if (iResult > 0)
            {
                cacheStartLocation(klineData.getId());
                InQuotationConsts.CAMHE_RTKLINE_MAP.put(InQuotationConsts.KLINE_KLINEENTITY_LAST_1M, klineData);
            }
        }
    }
    
    /**
     * 根据主题获取SQL语句
     *
     * @param sqlType
     * @return
     */
    private String getSql(String sqlType)
    {
        StringBuilder sql = new StringBuilder();
        if (sqlType.equalsIgnoreCase("insert")) sql.append(QuerySqlConfig.SQL_INSERT_KLINE);
        else sql.append(QuerySqlConfig.SQL_QUERY_KLINE);
        return sql.toString();
    }
    
    /**
     * 缓存起始位置
     *
     * @param startLocation
     */
    private void cacheStartLocation(Long startLocation)
    {
        if (startLocation != null && startLocation.longValue() > 0) InQuotationConsts.CACHE_MAP.put(STARTLOCATION_KEY, startLocation);
    }
    
    /**
     * 满足时间刻度的K线数据追加到K线列表，并保证列表长度
     *
     * @param topic
     * @param rtKLineEntity
     */
    private void appendKlineListCacheData(String topic, KLineEntity rtKLineEntity)
    {
        List<KLine> kLines = new ArrayList<KLine>();
        // 获取KLine列表
        if (InQuotationConsts.CAMHE_KLINELIST_MAP.containsKey(topic)) kLines = (List<KLine>) InQuotationConsts.CAMHE_KLINELIST_MAP.get(topic);
        // 实时K线数据转换成前台展示K线数据
        KLine kLine = dataConvert(rtKLineEntity, topic);
        // 最新数据追加在列表最前面
        kLines.add(0, kLine);
        // 如果超出范围，则将最早时间刻度数据剔除
        int cacheDataLastIdx = kLines.size() - 1;
        if (cacheDataLastIdx == 1000) kLines.remove(cacheDataLastIdx);
        // 将K线列表缓存在静态变量
        InQuotationConsts.CAMHE_KLINELIST_MAP.put(topic, kLines);
    }
    
    /**
     * 判断是否满足K线主题时间刻度
     * 满足K线时间刻度返回true，否则返回false
     *
     * @param topic
     * @param curTime
     * @return
     */
    private boolean checkKLineTime(String topic, Long curTime, String rtEntityKey)
    {
        Long chkTime = 0L;
        Long currTime = DateUtils.getPreMinFirstSec(curTime, 0);
        KLineEntity kLineEntity = (KLineEntity) InQuotationConsts.CAMHE_RTKLINE_MAP.get(rtEntityKey);
        long displayTime = kLineEntity.getDisplayTime().getTime();
        if (InQuotationConfig.TOPIC_KLINE_1M.equalsIgnoreCase(topic))
        {
            chkTime = displayTime + 60000;
        }
        if (InQuotationConfig.TOPIC_KLINE_5M.equalsIgnoreCase(topic)) chkTime = displayTime + 5 * 60000;
        if (InQuotationConfig.TOPIC_KLINE_15M.equalsIgnoreCase(topic)) chkTime = displayTime + 15 * 60000;
        if (InQuotationConfig.TOPIC_KLINE_30M.equalsIgnoreCase(topic)) chkTime = displayTime + 30 * 60000;
        if (InQuotationConfig.TOPIC_KLINE_HOUR.equalsIgnoreCase(topic)) chkTime = displayTime + 60 * 60000;
        if (InQuotationConfig.TOPIC_KLINE_DAY.equalsIgnoreCase(topic)) chkTime = displayTime + 24 * 60 * 60000;
        LoggerUtils.logDebug(logger, "curTime:" + currTime);
        LoggerUtils.logDebug(logger, "DateUtils.getPreMinFirstSec(curTime,0):" + currTime);
        LoggerUtils.logDebug(logger, "chkTime:" + chkTime);
        // LoggerUtils.logInfo(logger, "初始化时间检查：" + new Timestamp(chkTime) + "----" + new Timestamp(currTime));
        return currTime.longValue() >= chkTime.longValue();
    }
    
    /**
     * 处理前端展示的K线数据
     * 历史 + 动态
     */
    private void doKLineDataListBuilde()
    {
        KLineEntity rtKLineEntity = InQuotationConsts.CAMHE_RTKLINE_MAP.get(InQuotationConsts.KLINE_RTKLINEENTITY);
        // 首次启动空库，不需要发送list
        if (rtKLineEntity.getClosePrice().compareTo(BigDecimal.ZERO) == 0) { return; }
        doKLineDataListBuilde(InQuotationConfig.TOPIC_KLINE_1M);
        doKLineDataListBuilde(InQuotationConfig.TOPIC_KLINE_5M);
        doKLineDataListBuilde(InQuotationConfig.TOPIC_KLINE_15M);
        doKLineDataListBuilde(InQuotationConfig.TOPIC_KLINE_30M);
        doKLineDataListBuilde(InQuotationConfig.TOPIC_KLINE_HOUR);
        doKLineDataListBuilde(InQuotationConfig.TOPIC_KLINE_DAY);
    }
    
    /**
     * 拼接K线数据
     * K线列表 + 实时数据
     *
     * @param topic
     */
    private void doKLineDataListBuilde(String topic)
    {
        String kLine1mKey = new StringBuffer(KLINE_DATA_KEY).append(topic).toString();
        List<KLine> kLines = new ArrayList<KLine>();
        if (InQuotationConsts.CAMHE_KLINELIST_MAP.containsKey(topic)) kLines = (List<KLine>) InQuotationConsts.CAMHE_KLINELIST_MAP.get(topic);
        String rtKlineKey = getRTKLineDataCacheKey(topic);
        List<KLine> kLineList = new ArrayList<KLine>(kLines);
        java.util.Collections.copy(kLineList, kLines);
        if (InQuotationConsts.CAMHE_RTKLINE_MAP.containsKey(rtKlineKey))
        {
            KLineEntity kLineEntity = InQuotationConsts.CAMHE_RTKLINE_MAP.get(rtKlineKey);
            KLine kLine = dataConvert(kLineEntity, topic);
            kLineList.add(0, kLine);
        }
        if (!CollectionUtils.isEmpty(kLineList)) RedisUtils.putObject(kLine1mKey, kLineList, CacheConst.TWENTYFOUR_HOUR_CACHE_TIME);
    }
    
    /**
     * 从成交流水表加载实时K线数据
     *
     * @return
     */
    private KLineEntity getRTKlineEntityFromDB(Long curMin)
    {
        KLineEntity kLineEntity = null;
        List<KLineEntity> kLineList = null;
        Timestamp kLineTimeStart = new Timestamp(DateUtils.getPreMinFirstSec(curMin, 1));
        Timestamp kLineTimeEnd = new Timestamp(curMin - 1);
        kLineList = (List<KLineEntity>) jdbcTemplate.query(QuerySqlConfig.SQL_GET_KLINE_START, new KLineEntityRowMapper(), kLineTimeStart, kLineTimeEnd, kLineTimeStart,
                kLineTimeEnd, kLineTimeStart, kLineTimeEnd, kLineTimeStart, kLineTimeEnd);
        if (!CollectionUtils.isEmpty(kLineList)) kLineEntity = kLineList.get(0);
        return kLineEntity;
    }
    
    /**
     * 从K线数据表中获取K线列表
     *
     * @param topic
     * @return
     */
    private List<KLine> getKLineListFromDB(String topic, String calType, Long curtime)
    {
        HashMap<String, Timestamp> kLineTimeMap = null;
        List<KLine> kLineList = null;
        String sql = null;
        Timestamp pushTimeStart = null;
        Timestamp pushTimeEnd = null;
        // 获取K线表查询起始时间
        kLineTimeMap = calKLineTime(topic, curtime, calType);
        pushTimeStart = kLineTimeMap.get(InQuotationConsts.KLINE_TIMETYPE_PUSH_START);
        pushTimeEnd = kLineTimeMap.get(InQuotationConsts.KLINE_TIMETYPE_PUSH_END);
        // 动态获取查询sql语句
        if (InQuotationConfig.TOPIC_KLINE_1M.equalsIgnoreCase(topic)) sql = QuerySqlConfig.SQL_PUSH_KLINE_1M;
        else if (InQuotationConfig.TOPIC_KLINE_5M.equalsIgnoreCase(topic) || InQuotationConfig.TOPIC_KLINE_15M.equalsIgnoreCase(topic)
                || InQuotationConfig.TOPIC_KLINE_30M.equalsIgnoreCase(topic))
            sql = QuerySqlConfig.SQL_PUSH_KLINE_MIN;
        else if (InQuotationConfig.TOPIC_KLINE_HOUR.equalsIgnoreCase(topic)) sql = QuerySqlConfig.SQL_PUSH_KLINE_HOUR;
        else if (InQuotationConfig.TOPIC_KLINE_DAY.equalsIgnoreCase(topic)) sql = QuerySqlConfig.SQL_PUSH_KLINE_DAY;
        // 开始查询指定klinelsit
        int frequency = 0;
        if (InQuotationConfig.TOPIC_KLINE_5M.equalsIgnoreCase(topic)) frequency = 5;
        else if (InQuotationConfig.TOPIC_KLINE_15M.equalsIgnoreCase(topic)) frequency = 15;
        else if (InQuotationConfig.TOPIC_KLINE_30M.equalsIgnoreCase(topic)) frequency = 30;
        if (InQuotationConfig.TOPIC_KLINE_1M.equalsIgnoreCase(topic)) kLineList = (List<KLine>) jdbcTemplate.query(sql, new KLineRowMapper(), pushTimeStart, pushTimeEnd);
        else if (InQuotationConfig.TOPIC_KLINE_5M.equalsIgnoreCase(topic) || InQuotationConfig.TOPIC_KLINE_15M.equalsIgnoreCase(topic)
                || InQuotationConfig.TOPIC_KLINE_30M.equalsIgnoreCase(topic))
            kLineList = (List<KLine>) jdbcTemplate.query(sql, new KLineRowMapper(), frequency, frequency, frequency, frequency, pushTimeStart, pushTimeEnd);
        else if (InQuotationConfig.TOPIC_KLINE_HOUR.equalsIgnoreCase(topic))
            kLineList = (List<KLine>) jdbcTemplate.query(sql, new KLineRowMapper(), pushTimeStart, pushTimeEnd);
        else if (InQuotationConfig.TOPIC_KLINE_DAY.equalsIgnoreCase(topic))
            kLineList = (List<KLine>) jdbcTemplate.query(sql, new KLineRowMapper(), pushTimeStart, pushTimeEnd);
        return kLineList;
    }
    
    /**
     * @param topic   K线主题
     * @param curtime 当前时间
     * @param calType 计算方式  (增量，append；全量，add)
     * @return
     */
    private HashMap<String, Timestamp> calKLineTime(String topic, Long curtime, String calType)
    {
        HashMap<String, Timestamp> klineTimeMap = new HashMap<String, Timestamp>();
        Timestamp startKlineTime = null;
        Timestamp endKlineTime = null;
        long times = 0L;
        if (InQuotationConfig.TOPIC_KLINE_1M.equalsIgnoreCase(topic))
        {
            /**
             * 1分钟线起始时间推算
             * 1. 处理成当前分钟的00.000
             * 2. 再根据1分钟线取数区间往前推算
             */
            times = curtime - curtime % 60000;
            if (InQuotationConsts.KLINE_TIMETYPE_LIST.equalsIgnoreCase(calType))
            {
                startKlineTime = DateUtils.getPreMin(new Timestamp(times), InQuotationConfig.KLINE1M_RANGE);
                endKlineTime = new Timestamp(times - 1);
            }
            else if (InQuotationConsts.KLINE_TIMETYPE_LAST.equalsIgnoreCase(calType))
            {
                startKlineTime = new Timestamp(times);
                endKlineTime = new Timestamp(curtime);
            }
            else
            {
                startKlineTime = DateUtils.getPreMin(new Timestamp(times), 1);
                endKlineTime = new Timestamp(times - 1);
            }
        }
        else if (InQuotationConfig.TOPIC_KLINE_5M.equalsIgnoreCase(topic))
        {
            /**
             * 5分钟线开始时间推算
             * 1. 处理成当前分钟的00.000
             * 2. 再根据1分钟线取数区间往前推算
             */
            times = curtime - curtime % 300000;
            if (InQuotationConsts.KLINE_TIMETYPE_LIST.equalsIgnoreCase(calType))
            {
                startKlineTime = DateUtils.getPreMin(new Timestamp(times), InQuotationConfig.KLINE5M_RANGE);
                endKlineTime = new Timestamp(times - 1);
            }
            else if (InQuotationConsts.KLINE_TIMETYPE_LAST.equalsIgnoreCase(calType))
            {
                startKlineTime = new Timestamp(times);
                endKlineTime = new Timestamp(curtime);
            }
        }
        else if (InQuotationConfig.TOPIC_KLINE_15M.equalsIgnoreCase(topic))
        {
            times = curtime - curtime % 900000;
            if (InQuotationConsts.KLINE_TIMETYPE_LIST.equalsIgnoreCase(calType))
            {
                startKlineTime = DateUtils.getPreMin(new Timestamp(times), InQuotationConfig.KLINE15M_RANGE);
                endKlineTime = new Timestamp(times - 1);
            }
            else if (InQuotationConsts.KLINE_TIMETYPE_LAST.equalsIgnoreCase(calType))
            {
                startKlineTime = new Timestamp(times);
                endKlineTime = new Timestamp(curtime);
            }
        }
        else if (InQuotationConfig.TOPIC_KLINE_30M.equalsIgnoreCase(topic))
        {
            times = curtime - curtime % 1800000;
            if (InQuotationConsts.KLINE_TIMETYPE_LIST.equalsIgnoreCase(calType))
            {
                startKlineTime = DateUtils.getPreMin(new Timestamp(times), InQuotationConfig.KLINE30M_RANGE);
                endKlineTime = new Timestamp(times - 1);
            }
            else if (InQuotationConsts.KLINE_TIMETYPE_LAST.equalsIgnoreCase(calType))
            {
                startKlineTime = new Timestamp(times);
                endKlineTime = new Timestamp(curtime);
            }
        }
        else if (InQuotationConfig.TOPIC_KLINE_HOUR.equalsIgnoreCase(topic))
        {
            times = curtime - curtime % 3600000;
            if (InQuotationConsts.KLINE_TIMETYPE_LIST.equalsIgnoreCase(calType))
            {
                startKlineTime = DateUtils.getPreHour(new Timestamp(times), InQuotationConfig.KLINE1H_RANGE);
                endKlineTime = new Timestamp(times - 1);
            }
            else if (InQuotationConsts.KLINE_TIMETYPE_LAST.equalsIgnoreCase(calType))
            {
                startKlineTime = new Timestamp(times);
                endKlineTime = new Timestamp(curtime);
            }
        }
        else if (InQuotationConfig.TOPIC_KLINE_DAY.equalsIgnoreCase(topic))
        {
            times = DateUtils.getCurrentDateFirstSec();
            if (InQuotationConsts.KLINE_TIMETYPE_LIST.equalsIgnoreCase(calType))
            {
                startKlineTime = DateUtils.getPreDate(new Timestamp(times), InQuotationConfig.KLINE1D_RANGE);
                endKlineTime = new Timestamp(times - 1);
            }
            else if (InQuotationConsts.KLINE_TIMETYPE_LAST.equalsIgnoreCase(calType))
            {
                startKlineTime = new Timestamp(times);
                endKlineTime = new Timestamp(curtime);
            }
        }
        if (null != startKlineTime && null != endKlineTime)
        {
            klineTimeMap.put(InQuotationConsts.KLINE_TIMETYPE_PUSH_START, startKlineTime);
            klineTimeMap.put(InQuotationConsts.KLINE_TIMETYPE_PUSH_END, endKlineTime);
        }
        return klineTimeMap;
    }
    
    private KLine dataConvert(KLineEntity kLineEntity, String topic)
    {
        KLine kLine = new KLine();
        String priceTime = "";
        if (InQuotationConfig.TOPIC_KLINE_DAY.equalsIgnoreCase(topic) || DateUtils.getCurrentDateFirstSec().longValue() == kLineEntity.getDisplayTime().getTime())
        {
            SimpleDateFormat df = new SimpleDateFormat("MM-dd");
            priceTime = df.format(kLineEntity.getDisplayTime());
        }
        else
        {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            priceTime = df.format(kLineEntity.getDisplayTime());
        }
        String openPrice = kLineEntity.getOpenPrice().toPlainString();
        String closePrice = kLineEntity.getClosePrice().toPlainString();
        String lowestPrice = kLineEntity.getLowestPrice().toPlainString();
        String hightestPrice = kLineEntity.getHighestPrice().toPlainString();
        String dealbal = kLineEntity.getDealBal().toPlainString();
        String dealAmtSum = kLineEntity.getDealAmtSum().toPlainString();
        return kLine.getObject(priceTime, openPrice, closePrice, lowestPrice, hightestPrice, dealbal, dealAmtSum);
    }
}
