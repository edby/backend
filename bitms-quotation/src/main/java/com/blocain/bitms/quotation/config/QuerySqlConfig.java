package com.blocain.bitms.quotation.config;

public class QuerySqlConfig
{
    /**
     * 宕机或时间差超过2分钟时成交流水取数逻辑说明（K线补全）
     */
    public static final String SQL_GET_INITKLINEDATAINFO = "select * from ( select max(id) over (partition by groupid) closeid, groupid displayTime,ROW_NUMBER() OVER(PARTITION BY groupid ORDER BY dealtime DESC) rn,last_value(dealTime) over(partition by groupid order by dealTime) quotationTime,first_value(dealPrice) over(partition by groupid order by dealTime) openPrice,last_value(dealPrice) over(partition by groupid order by dealTime) closePrice,max(dealPrice) over (partition by groupid) highestPrice,min(dealPrice) over (partition by groupid) lowestPrice,sum(dealAmt) over (partition by groupid) dealamtsum,sum(dealbalance) over (partition by groupid) dealbal "
            + "from (select id, dealPrice, dealamt, dealbalance, dealtime, trunc(dealtime, 'hh') + floor(to_char(dealtime, 'mi') / 1) * 1 / 60 / 24 groupid "
            + "from " + InQuotationConfig.TBL_REALDEAL
            + " where tradeType='matchTrade' and dealTime between ? and ? "
            + "union all "
            + "select id, dealPrice, dealamt, dealbalance, dealtime,trunc(dealtime, 'hh') + floor(to_char(dealtime, 'mi') / 1) * 1 / 60 / 24 groupid "
            + "from " + InQuotationConfig.TBL_REALDEALHIS
            + " where tradeType='matchTrade' and dealTime between ? and ? "
            + "order by groupid)) where rn=1";

    /**
     * 获取成交表与历史成交中成交时间最早的一条数据
     */
    public static final String SQL_GET_EARLY ="select distinct * from " +
            "(select * from " + InQuotationConfig.TBL_REALDEAL + " union all select * from " + InQuotationConfig.TBL_REALDEALHIS + ") " +
            "where id = (select min(id) from (select id from " + InQuotationConfig.TBL_REALDEAL + " union all select id from " + InQuotationConfig.TBL_REALDEALHIS + "))";

    /**
     * 成交流水取数逻辑说明
     * 1. 取出最近N条流水记录
     * 2. 保证前台显示成交金额，成交数量格式一致，
     *    价格保留小数点两位(25.00)，成交金额保留小数点4位(125.0000)。
     */
    public static String SQL_GET_REALDEAL    = new StringBuilder("select dealTime,to_char(dealPrice) dealPrice, ")
            .append("to_char(dealAmt) dealAmt, dealDirect activeDealDirect from ")
            .append("(select deal.dealTime,deal.dealPrice,deal.dealAmt ,deal.dealDirect from ").append(InQuotationConfig.TBL_REALDEAL).append(" deal where ")
            .append("tradeType = 'matchTrade' order by id desc) where rownum <= ?").toString();
    //    public static String SQL_GET_KLINE = new StringBuilder("select to_char(c.dealtime,?)  quotationTime, to_char(a.highestPrice,'FM9999999999999990.00999999') highestPrice, ")
    //                                        .append("to_char(a.lowestPrice,'FM9999999999999990.00999999') lowestPrice,to_char(a.dealbal,'FM9999999999999990.00999999') dealbal,")
    //                                        .append("to_char(a.dealamtsum ,'FM9999999999999990.00999999') dealamtsum,to_char(b.dealPrice ,'FM9999999999999990.00999999') openPrice,")
    //                                        .append(" to_char(c.dealPrice ,'FM9999999999999990.00999999') closePrice from (select max(dealPrice) highestPrice,min(dealPrice) lowestPrice, ")
    //                                        .append("min(id) openid,max(id) closeid, sum(dealbalance) dealbal,sum(dealamt) dealamtsum from ")
    //                                        .append("(select id,dealPrice,dealamt,dealbalance,dealtime,floor(to_char(dealtime, ?) / ?) groupid from ").append(InQuotationConfig.TBL_REALDEAL).append(" deal ")
    //                                        .append("where exists (select id from ").append(InQuotationConfig.TBL_REALDEAL).append(" where dealTime between sf_addTime(?, -1, ?) and sysdate and deal.id = id)) ")
    //                                        .append("group by groupid)a, ").append(InQuotationConfig.TBL_REALDEAL).append(" b, ")
    //                                        .append(InQuotationConfig.TBL_REALDEAL).append(" c where a.openid = b.id and a.closeid = c.id order by  c.dealtime desc")
    //                                        .toString();


    public static String SQL_GET_KLINEDATAINFO = new StringBuilder("select 0 closeid,a.* from ").append(InQuotationConfig.TBL_KLINE).append(" a ")
            .append("where displayTime = (select max(displayTime) from ").append(InQuotationConfig.TBL_KLINE)
            .append(" )").toString();

    /**
     * 生成K线取数逻辑
     * 统计交易流水区间段内的最高价，最低价，开盘价，收盘价，成交数量，成交金额。
     * 开盘价，收盘价统计逻辑：获取区间段内最早的ID，和最新的ID，然后去匹配出开盘价和收盘价
     *
     * 性能优化考虑：
     *  1. 走索引，历史表走时间索引(历史表dealTime是索引，而当日成交表没有建dealTime索引避免影响数据插入性能)
     *  2. 增量查询，记住查询的最后一笔数据的ID，做为下次查询的起始位置。
     *
     * 注意：没有缓存ID的时候，不能用该SQL会造成违法主键约束。
     *       因为没有缓存ID的时候，当日成交表是全量查询
     */
    public static String SQL_GET_KLINE       = new StringBuilder(
            "select  b.dealtime displayTime, a.closeid,a.maxPrice highestPrice,a.minPrice lowestPrice,a.dealamtsum,a.dealbal,b.dealtime quotationTime,b.openprice,b.closeprice from ")
            .append("(select max(id) closeid,max(dealPrice) maxPrice,min(dealPrice) minPrice,sum(dealamt) dealamtsum,sum(dealbalance) dealbal from ")
            .append("(select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEAL).append(" where id > ? and tradeType = 'matchTrade' and dealTime between ? and ? ").append(" union all ")
            .append("select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEALHIS)
            .append(" where dealTime between ? and ? and tradeType = 'matchTrade'))a ,")
            .append("(select  id,dealtime,first_value(dealPrice) OVER (order by id ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) openprice,")
            .append("last_value(dealPrice) OVER (order by id ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) closeprice from")
            .append("(select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEAL).append(" where id > ? and tradeType = 'matchTrade' and dealTime between ? and ? ").append(" union all ")
            .append("select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEALHIS)
            .append(" where dealTime between ? and ? and tradeType = 'matchTrade'))b ").append("where a. closeid = b.id ").toString();

    /**
     * 系统启动后首次取数
     * 生成K线取数逻辑
     * 统计交易流水区间段内的最高价，最低价，开盘价，收盘价，成交数量，成交金额。
     * 开盘价，收盘价统计逻辑：获取区间段内最早的ID，和最新的ID，然后去匹配出开盘价和收盘价
     */
    public static String SQL_GET_KLINE_START = new StringBuilder(
            "select  b.dealtime displayTime, a.closeid,a.maxPrice highestPrice,a.minPrice lowestPrice,a.dealamtsum,a.dealbal,b.dealtime quotationTime,b.openprice,b.closeprice from ")
            .append("(select max(id) closeid,max(dealPrice) maxPrice,min(dealPrice) minPrice,sum(dealamt) dealamtsum,sum(dealbalance) dealbal from ")
            .append("(select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEAL)
            .append(" where dealTime between ? and ? and tradeType = 'matchTrade'").append(" union all ")
            .append("select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEALHIS)
            .append(" where dealTime between ? and ? and tradeType = 'matchTrade'))a ,")
            .append("(select  id,dealtime,first_value(dealPrice) OVER (order by id ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) openprice,")
            .append("last_value(dealPrice) OVER (order by id ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) closeprice from")
            .append("(select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEAL)
            .append(" where dealTime between ? and ? and tradeType = 'matchTrade'").append(" union all ")
            .append("select id,dealPrice, dealamt,  dealbalance, dealtime from ").append(InQuotationConfig.TBL_REALDEALHIS)
            .append(" where dealTime between ? and ? and tradeType = 'matchTrade'))b ").append("where a. closeid = b.id ").toString();

    /**
     * 取当天0点时刻的K线数据,使用其收盘价作为当天计算涨跌幅的基价
     */
    public static String SQL_GET_FIRST_KLINE = new StringBuilder("select 0 closeid , a.* from ").append(InQuotationConfig.TBL_KLINE).append(" a where a.displayTime=?").toString();

    /**
     * 生成K线数据
     * 目前是每1分钟生成一条K线数据
     */
    public static String SQL_INSERT_KLINE    = new StringBuilder(" insert into ").append(InQuotationConfig.TBL_KLINE)
            .append(" (displayTime,quotationTime,highestPrice,lowestPrice,openPrice,closePrice,dealbal,dealamtsum) ").append("values(?,?,?,?,?,?,?,?)").toString();

    /**
     * 从K线表取数
     * 没有交易数据时，则从K线表中取最近一条，作为本时间段的K线数据。
     * K线展示时间为当前时间，行情日期、收盘价仍然是原数据，最高价、最低价、开盘价沿用原数据的收盘价作为当期的最高，最低，开盘，成交量和成交金额为0，
     */
    public static String SQL_QUERY_KLINE     = new StringBuilder(
            "select 0 closeid,current_timestamp displaytime,quotationTime,closePrice highestPrice,closePrice lowestPrice,closePrice openPrice,closePrice,0 dealbal,0 dealamtsum from ")
            .append(InQuotationConfig.TBL_KLINE).append(" where  displaytime = (select max(displaytime) from ").append(InQuotationConfig.TBL_KLINE).append(" ) ")
            .toString();

    /**
     * 1分钟线推送
     * 直接从K线表取数返回。
     */
    public static String SQL_PUSH_KLINE_1M   = new StringBuilder(
            "select to_char(displayTime,'mm-dd hh24:mi') displayTime, to_char(highestPrice,'FM9999999999999990.00999999') highestPrice,")
            .append("to_char(lowestPrice,'FM9999999999999990.00999999') lowestPrice,to_char(dealbal,'FM9999999999999990.00999999') dealbal,")
            .append("to_char(dealamtsum ,'FM9999999999999990.00999999') dealamtsum,to_char(openPrice ,'FM9999999999999990.00999999') openPrice,")
            .append("to_char(closePrice ,'FM9999999999999990.00999999') closePrice from ").append(InQuotationConfig.TBL_KLINE)
            .append(" where displayTime between ? and ? order by displayTime desc").toString();

    /**
     * 其他K线推送：推送一组数据给前台
     * 1. 根据K线频率分组  floor(to_char(displayTime, 日期格式化) / K线频率) groupid
     * 2. 根据分组后的数据进行最高价，最低价，开盘价、收盘价、成交数量、成交金额统计
     * 3. K线显示时间根据频率不同会做相应格式化
     * 4. 最后对数据根据收盘价进行倒序排序
     */
    public static String SQL_PUSH_KLINE_MIN      = new StringBuilder(
            "select to_char(trunc(displayTime, 'hh') + floor(to_char(displayTime, 'mi') / ? ) * ? / 60 / 24,'mm-dd hh24:mi') displayTime, to_char(highestPrice,'FM9999999999999990.00999999') highestPrice,")
            .append("to_char(lowestPrice,'FM9999999999999990.00999999') lowestPrice,to_char(dealbal,'FM9999999999999990.00999999') dealbal,")
            .append("to_char(dealamtsum ,'FM9999999999999990.00999999') dealamtsum,to_char(openPrice ,'FM9999999999999990.00999999') openPrice,")
            .append("to_char(closePrice ,'FM9999999999999990.00999999') closePrice from ")
            .append("(select first_value(openPrice) over(partition by groupid order by displayTime) openPrice, ")
            .append("last_value(closePrice) over(partition by groupid order by displayTime) closePrice,")
            .append("last_value(displayTime) over(partition by groupid order by displayTime) displayTime,")
            .append("max(highestPrice) over(partition by groupid) highestPrice,").append("min(lowestPrice) over(partition by groupid) lowestPrice,")
            .append("sum(dealamtsum)over(partition by groupid)dealamtsum,").append("sum(dealbal)over(partition by groupid)dealbal ,")
            .append("ROW_NUMBER() OVER(PARTITION BY groupid ORDER BY displayTime DESC) rn from ")
            .append("(select displayTime, highestPrice,lowestPrice,closePrice,openPrice,dealamtsum, dealbal,trunc(displayTime,'hh')+floor(to_char(displayTime,'mi')/?)*?/60/24 groupid from ")
            .append(InQuotationConfig.TBL_KLINE).append(" where displayTime between ? and ? )) where rn = 1 order by displayTime desc ").toString();


    public static String SQL_PUSH_KLINE_HOUR      = new StringBuilder(
            "select to_char(trunc(displayTime,'hh'),'mm-dd hh24:mi') displayTime, to_char(highestPrice,'FM9999999999999990.00999999') highestPrice,")
            .append("to_char(lowestPrice,'FM9999999999999990.00999999') lowestPrice,to_char(dealbal,'FM9999999999999990.00999999') dealbal,")
            .append("to_char(dealamtsum ,'FM9999999999999990.00999999') dealamtsum,to_char(openPrice ,'FM9999999999999990.00999999') openPrice,")
            .append("to_char(closePrice ,'FM9999999999999990.00999999') closePrice from ")
            .append("(select first_value(openPrice) over(partition by groupid order by displayTime) openPrice, ")
            .append("last_value(closePrice) over(partition by groupid order by displayTime) closePrice,")
            .append("last_value(displayTime) over(partition by groupid order by displayTime) displayTime,")
            .append("max(highestPrice) over(partition by groupid) highestPrice,").append("min(lowestPrice) over(partition by groupid) lowestPrice,")
            .append("sum(dealamtsum)over(partition by groupid)dealamtsum,").append("sum(dealbal)over(partition by groupid)dealbal ,")
            .append("ROW_NUMBER() OVER(PARTITION BY groupid ORDER BY displayTime DESC) rn from ")
            .append("(select displayTime, highestPrice,lowestPrice,closePrice,openPrice,dealamtsum, dealbal,trunc(displayTime,'hh') groupid from ")
            .append(InQuotationConfig.TBL_KLINE).append(" where displayTime between ? and ? )) where rn = 1 order by displayTime desc ").toString();

    public static String SQL_PUSH_KLINE_DAY      = new StringBuilder(
            "select to_char(trunc(displayTime,'dd'),'mm-dd') displayTime, to_char(highestPrice,'FM9999999999999990.00999999') highestPrice,")
            .append("to_char(lowestPrice,'FM9999999999999990.00999999') lowestPrice,to_char(dealbal,'FM9999999999999990.00999999') dealbal,")
            .append("to_char(dealamtsum ,'FM9999999999999990.00999999') dealamtsum,to_char(openPrice ,'FM9999999999999990.00999999') openPrice,")
            .append("to_char(closePrice ,'FM9999999999999990.00999999') closePrice from ")
            .append("(select first_value(openPrice) over(partition by groupid order by displayTime) openPrice, ")
            .append("last_value(closePrice) over(partition by groupid order by displayTime) closePrice,")
            .append("last_value(displayTime) over(partition by groupid order by displayTime) displayTime,")
            .append("max(highestPrice) over(partition by groupid) highestPrice,").append("min(lowestPrice) over(partition by groupid) lowestPrice,")
            .append("sum(dealamtsum)over(partition by groupid)dealamtsum,").append("sum(dealbal)over(partition by groupid)dealbal ,")
            .append("ROW_NUMBER() OVER(PARTITION BY groupid ORDER BY displayTime DESC) rn from ")
            .append("(select displayTime, highestPrice,lowestPrice,closePrice,openPrice,dealamtsum, dealbal,trunc(displayTime,'dd') groupid from ")
            .append(InQuotationConfig.TBL_KLINE).append(" where displayTime between ? and ? )) where rn = 1 order by displayTime desc ").toString();

    /***************************************************************
     *  深度委托盘口行情
     *  1.  买入行情取从高到低排序后，取前6档；卖出行情从低到高排序后，取前6档。
     *  2.  累计委托数据，买入由高到低累加，卖出委托，由低到高累加
     *  3.  委托流水只取【未成交，部分成交】
     *  4.  sf_deeppricecalc 深度行情处理函数，第一个绑定参数为因子，第二个绑定参数为深度
     */
    public static String SQL_GET_DEEPPRICE   = new StringBuilder(
            "select rowno,deepLevel,entrustdirect,entrustaccounttype,to_char(entrustprice,#priceDigit#) entrustprice, ")
            .append("to_char(entrustAmt,#amtDigit#) entrustAmt,to_char(entrustBal,#accumulatebalDigit#) entrustBal,enttotalbal from  (").toString();
    //                                            .append("select rownum rowno,0 deepLevel,'spotBuy' entrustdirect,decode(entrustaccounttype,0,0,1) entrustaccounttype,entrustprice,entrustAmt,sum(entrustAmt) over (order by entrustprice desc ) entrustBal from ")
    //                                            .append("(select entrustaccounttype,entrustprice,entrustAmt from (select entrustprice,sum(entrustAmt) entrustAmt,sum(entrustaccounttype) entrustaccounttype from ")
    //                                            .append("(select sf_deeppricecalc(entrustprice,1,'B',0) entrustprice,(entrustAmt - dealAmt) entrustAmt,entrustaccounttype from ").append(InQuotationConfig.TBL_ENTRUST)
    //                                            .append(" where status in ('noDeal', 'partialDeal') and  entrustdirect = 'spotBuy' and  tradetype = 'matchTrade' and entrusttype='limitPrice' ) ")
    //                                            .append("group by entrustprice order by entrustprice desc) where rownum <=? order by entrustprice desc)")
    //                                            .append(" union all ")
    //                                            .append("select rowno,deepLevel, entrustdirect,entrustaccounttype,entrustprice,entrustAmt, entrustBal from ")
    //                                            .append("(select rownum rowno,0 deepLevel,'spotSell' entrustdirect,decode(entrustaccounttype,0,0,1) entrustaccounttype,entrustprice,entrustAmt,sum(entrustAmt) over (order by entrustprice) entrustBal from ")
    //                                            .append("(select entrustaccounttype,entrustprice,entrustAmt from (select entrustprice,sum(entrustAmt) entrustAmt,sum(entrustaccounttype) entrustaccounttype from ")
    //                                            .append("(select sf_deeppricecalc(entrustprice,1,'S',0) entrustprice,(entrustAmt - dealAmt) entrustAmt,entrustaccounttype from ").append(InQuotationConfig.TBL_ENTRUST)
    //                                            .append(" where status in ('noDeal', 'partialDeal') and  entrustdirect = 'spotSell' and  tradetype = 'matchTrade' and entrusttype='limitPrice' ) ")
    //                                            .append("group by entrustprice order by entrustprice) where rownum <=? order by entrustprice) order by rowno desc)")
    //                                            .append(" union all ")
    //                                            .append("select rownum rowno,1 deepLevel,'spotBuy' entrustdirect,decode(entrustaccounttype,0,0,1) entrustaccounttype,entrustprice,entrustAmt,sum(entrustAmt) over (order by entrustprice desc ) entrustBal from ")
    //                                            .append("(select entrustaccounttype,entrustprice,entrustAmt from (select entrustprice,sum(entrustAmt) entrustAmt,sum(entrustaccounttype) entrustaccounttype from ")
    //                                            .append("(select sf_deeppricecalc(entrustprice,1,'B',1) entrustprice,(entrustAmt - dealAmt) entrustAmt,entrustaccounttype from ").append(InQuotationConfig.TBL_ENTRUST)
    //                                            .append(" where status in ('noDeal', 'partialDeal') and  entrustdirect = 'spotBuy' and  tradetype = 'matchTrade' and entrusttype='limitPrice') ")
    //                                            .append("group by entrustprice order by entrustprice desc) where rownum <=? order by entrustprice desc)")
    //                                            .append(" union all ")
    //                                            .append("select rowno,deepLevel, entrustdirect,entrustaccounttype,entrustprice,entrustAmt, entrustBal from ")
    //                                            .append("(select rownum rowno,1 deepLevel,'spotSell' entrustdirect,decode(entrustaccounttype,0,0,1) entrustaccounttype,entrustprice,entrustAmt,sum(entrustAmt) over (order by entrustprice) entrustBal from ")
    //                                            .append("(select entrustaccounttype,entrustprice,entrustAmt from (select entrustprice,sum(entrustAmt) entrustAmt,sum(entrustaccounttype) entrustaccounttype from ")
    //                                            .append("(select sf_deeppricecalc(entrustprice,1,'S',1) entrustprice,(entrustAmt - dealAmt) entrustAmt,entrustaccounttype from ").append(InQuotationConfig.TBL_ENTRUST)
    //                                            .append(" where status in ('noDeal', 'partialDeal') and  entrustdirect = 'spotSell' and  tradetype = 'matchTrade' and entrusttype='limitPrice' ) ")
    //                                            .append("group by entrustprice order by entrustprice) where rownum <=? order by entrustprice)order by rowno desc))").toString();

    public static String SQL_DEEPPRICE_UNIT  = new StringBuilder("")
            .append("select rowno,deepLevel, entrustdirect,entrustaccounttype,entrustprice,entrustAmt, entrustBal,MAX(Entrustbal) OVER(PARTITION BY Entrustdirect ) EntTotalbal from ")
            .append("(select rownum rowno,#level# deepLevel,'spotBuy' entrustdirect,decode(entrustaccounttype,0,0,1) entrustaccounttype,entrustprice,entrustAmt,sum(entrustAmt) over (order by entrustprice desc ) entrustBal from ")
            .append("(select entrustaccounttype,entrustprice,entrustAmt from (select entrustprice,sum(entrustAmt) entrustAmt,sum(entrustaccounttype) entrustaccounttype from ")
            .append("(select sf_deeppricecalc(entrustprice,1,'B',#level#) entrustprice,(entrustAmt - dealAmt) entrustAmt,entrustaccounttype from ")
            .append(InQuotationConfig.TBL_ENTRUST)
            .append(" where status in ('noDeal', 'partialDeal') and  entrustdirect = 'spotBuy' and  tradetype = 'matchTrade' and entrusttype='limitPrice') ")
            .append("group by entrustprice order by entrustprice desc) where rownum <=#rownum# order by entrustprice desc)order by rowno #buySort#) ")
            .append(" union all ")
            .append("select rowno,deepLevel, entrustdirect,entrustaccounttype,entrustprice,entrustAmt, entrustBal,MAX(Entrustbal) OVER(PARTITION BY Entrustdirect ) Enttotalbal from ")
            .append("(select rownum rowno,#level# deepLevel,'spotSell' entrustdirect,decode(entrustaccounttype,0,0,1) entrustaccounttype,entrustprice,entrustAmt,sum(entrustAmt) over (order by entrustprice) entrustBal from ")
            .append("(select entrustaccounttype,entrustprice,entrustAmt from (select entrustprice,sum(entrustAmt) entrustAmt,sum(entrustaccounttype) entrustaccounttype from ")
            .append("(select sf_deeppricecalc(entrustprice,1,'S',#level#) entrustprice,(entrustAmt - dealAmt) entrustAmt,entrustaccounttype from ")
            .append(InQuotationConfig.TBL_ENTRUST)
            .append(" where status in ('noDeal', 'partialDeal') and  entrustdirect = 'spotSell' and  tradetype = 'matchTrade' and entrusttype='limitPrice' ) ")
            .append("group by entrustprice order by entrustprice) where rownum <=#rownum# order by entrustprice)order by rowno #sellSort#)").toString();

    // 获取外部指数行情
    public static String SQL_GET_QUOTATION   = new StringBuffer("select a.id,a.channel,a.stockid,a.idxPrice,b.idxPriceAvg from ")
            //实时外部指数价
            .append("(select id,channel,stockid,idxPrice from ")
            .append("(select id,channel,stockid,idxPrice from Quotation WHERE id=(select max(id) from Quotation where id >= ? and stockId = #stockid# and channel =#channel#) ")
            .append(" union all ")
            .append("select id,channel,stockid,idxPrice from Quotation WHERE  id=(select max(id) from Quotation where id >= ? and stockId = #stockid# and channel ='internalConversion')))a, ")
            // 指数加权价取数逻辑
            .append("(select stockid,avg(idxPrice) idxPriceAvg from ").append("(select  stockid,idxPrice from ")
            .append("(select stockid,idxPrice from Quotation  WHERE stockId = #stockid# and channel='bitfienex'  order by id desc) WHERE  rownum <= 100 ")
            .append(" union all ")
            .append("select stockid,idxPrice from (select stockid,idxPrice from Quotation  WHERE stockId = #stockid# and channel='bitstamp'  order by id desc)  WHERE  rownum <= 100 ")
            .append(" union all ")
            .append("select stockid,idxPrice  FROM  (select stockid,idxPrice from Quotation  WHERE stockId = #stockid# and channel='coinbase'  order by id desc) WHERE  rownum <= 100 ")
            .append(" union all ")
            .append(" select stockid,idxPrice  FROM  (select stockid,idxPrice from Quotation  WHERE stockId = #stockid# and channel='kraken'  order by id desc) WHERE  rownum <= 100 ")
            .append(" union all ")
            .append("select stockid,idxPrice FROM  (select stockid,idxPrice from Quotation  WHERE stockId = #stockid# and channel='internalConversion'  order by id desc) WHERE  rownum <= 100) group by stockid)b ")
            .append("where a.stockid = b.stockid").toString();
}
