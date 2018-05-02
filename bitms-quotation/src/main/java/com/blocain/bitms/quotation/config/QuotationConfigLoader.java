package com.blocain.bitms.quotation.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.PropertiesUtils;

/**
 * 行情服务配置文件装载工具
 *
 */
public class QuotationConfigLoader
{
    public static final String QUOTATIONSERVERS = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_QUOTATION_SERVERS)
            .append(BitmsConst.SEPARATOR).toString();
    
    public static final Logger logger           = LoggerFactory.getLogger(QuotationConfigLoader.class);
    
    /**
     * 装载指定目录的配置文件，支持两种形式
     * "/dir/config.properties"  ,"config.properties "
     * 当使用绝对路径时，会自动读取绝读路径的地址
     *
     * @param fileName
     * @return
     */
    public static void loadProp(String fileName) throws IOException
    {
        List<Long> virtualCurs = null;
        PropertiesUtils properties = new PropertiesUtils(fileName);
        // 服务名称
        InQuotationConfig.SERVER_NAME = properties.getProperty("server.name");
        // 业务表 委托表
        InQuotationConfig.TBL_ENTRUST = properties.getProperty("tbl.entrust");
        // 业务表 成交表
        InQuotationConfig.TBL_REALDEAL = properties.getProperty("tbl.realdeal");
        // 业务表 成交历史表
        InQuotationConfig.TBL_REALDEALHIS = properties.getProperty("tbl.realdealhis");
        InQuotationConfig.TBL_KLINE = properties.getProperty("tbl.kline");
        // 实时外部指数来源
        InQuotationConfig.QUOTATION_CHANNEL = properties.getProperty("quotation.channel");
        // 转换标志
        InQuotationConfig.BIZ_CONVERT = properties.getProperty("biz.convert");
        // 业务标的
        InQuotationConfig.BIZ_TARGET = properties.getProperty("biz.target").trim();
        // WS通知主题
        InQuotationConfig.PUSH_TOPIC = properties.getProperty("topic.exchangePair").trim();
        // 业务品种
        InQuotationConfig.BIZ_CATEGORY = properties.getProperty("biz.category").trim();
        // 价格上限默认值
        InQuotationConfig.UPRATEDEFAULT = new BigDecimal(properties.getProperty("biz.up.default"));
        // 价格下限默认值
        InQuotationConfig.DOWNRATEDEFAULT = new BigDecimal(properties.getProperty("biz.down.default"));
        // 交易流水每次推送数量
        InQuotationConfig.REALDEAL_NUM = Integer.valueOf(properties.getProperty("realdeal.num"));
        // 买卖盘口委托数量
        InQuotationConfig.ENTRUST_NUM = properties.getProperty("entrust.num");
        // 买卖盘口深度
        InQuotationConfig.ENTRUST_DEEPLEVEL = Integer.valueOf(properties.getProperty("entrust.deeplevel"));
        // 盘口价格保留小数位数
        InQuotationConfig.QUOTATION_PRICE_DIGIT = Integer.valueOf(properties.getProperty("quotation.price.digit"));
        // 盘口挂单数量保留小数位数
        InQuotationConfig.QUOTATION_AMT_DIGIT = Integer.valueOf(properties.getProperty("quotation.amt.digit"));
        // 盘口累计保留小数位数
        InQuotationConfig.QUOTATION_ACCUMULATEBAL_DIGIT = Integer.valueOf(properties.getProperty("quotation.balance.digit"));
        //盘口买盘显示顺序
        InQuotationConfig.ENTRUST_BUY_SORT = properties.getProperty("entrust.buy.sort");
        //盘口卖盘显示顺序
        InQuotationConfig.ENTRUST_SELL_SORT = properties.getProperty("entrust.sell.sort");

        // 消息主题
        // 1分钟 K线
        InQuotationConfig.TOPIC_KLINE_1M = properties.getProperty("topic.kline.1m");
        // 5分钟 K线
        InQuotationConfig.TOPIC_KLINE_5M = properties.getProperty("topic.kline.5m");
        // 15分钟 K线
        InQuotationConfig.TOPIC_KLINE_15M = properties.getProperty("topic.kline.15m");
        // 30分钟 K线
        InQuotationConfig.TOPIC_KLINE_30M = properties.getProperty("topic.kline.30m");
        // 时钟 K线
        InQuotationConfig.TOPIC_KLINE_HOUR = properties.getProperty("topic.kline.hour");
        // 日线 K线
        InQuotationConfig.TOPIC_KLINE_DAY = properties.getProperty("topic.kline.day");
        // 周线 K线
        InQuotationConfig.TOPIC_KLINE_WEEK = properties.getProperty("topic.kline.week");
        // 月线 K线
        InQuotationConfig.TOPIC_KLINE_MONTH = properties.getProperty("topic.kline.month");
        // 委托盘口深度行情
        InQuotationConfig.TOPIC_ENTRUST_DEEPPRICE = properties.getProperty("topic.entrust.deepprice");
        // 撮合成交流水
        InQuotationConfig.TOPIC_REALDEAL_TRANSACTION = properties.getProperty("topic.realdeal.transaction");
        // 最新撮合成交行情
        InQuotationConfig.TOPIC_RTQUOTATION_PRICE = properties.getProperty("topic.rtquotation.price");
        // 全行情推送开关
        InQuotationConfig.QUOTATION_SWITCH = properties.getProperty("allQuotation.switch");
        PropertiesUtils quotationProp = new PropertiesUtils("quotation.properties");
        // K线 1分钟线 取6小时的交易数据
        InQuotationConfig.KLINE1M_RANGE = Integer.valueOf(quotationProp.getProperty("kline.1m.range")) - 1;
        // K线 5分钟线 取6小时的交易数据
        InQuotationConfig.KLINE5M_RANGE = (Integer.valueOf(quotationProp.getProperty("kline.5m.range")) - 1) * 5;
        // K线 15分钟线 取20小时的交易数据
        InQuotationConfig.KLINE15M_RANGE = (Integer.valueOf(quotationProp.getProperty("kline.15m.range")) - 1) * 15;
        // K线 30分钟线 取42小时的交易数据
        InQuotationConfig.KLINE30M_RANGE = (Integer.valueOf(quotationProp.getProperty("kline.30m.range")) - 1) * 30;
        // K线 小时线 取60小时的交易数据
        InQuotationConfig.KLINE1H_RANGE = (Integer.valueOf(quotationProp.getProperty("kline.1h.range")) - 1) * 60;
        // K线 日线 取60小时的交易数据
        InQuotationConfig.KLINE1D_RANGE = (Integer.valueOf(quotationProp.getProperty("kline.1d.range")) - 1) * 60 * 24;
        // K线休眠时间 1分钟
        InQuotationConfig.THREAD_SLEEP_KLINE_1M = Long.valueOf(quotationProp.getProperty("sleep.kline.1m"));
        InQuotationConfig.THREAD_SLEEP_KLINE_5M = Long.valueOf(quotationProp.getProperty("sleep.kline.5m"));
        InQuotationConfig.THREAD_SLEEP_KLINE_15M = Long.valueOf(quotationProp.getProperty("sleep.kline.15m"));
        InQuotationConfig.THREAD_SLEEP_KLINE_30M = Long.valueOf(quotationProp.getProperty("sleep.kline.30m"));
        InQuotationConfig.THREAD_SLEEP_KLINE_1H = Long.valueOf(quotationProp.getProperty("sleep.kline.1h"));
        InQuotationConfig.THREAD_SLEEP_KLINE_1D = Long.valueOf(quotationProp.getProperty("sleep.kline.1d"));
        InQuotationConfig.THREAD_SLEEP_KLINE_CREATE = Long.valueOf(quotationProp.getProperty("sleep.kline.create"));
        // 深度行情休眠时间 1秒
        InQuotationConfig.THREAD_SLEEP_DEEPPRICE = Long.valueOf(quotationProp.getProperty("sleep.deepprice"));
        // 交易流水休眠时间 1秒
        InQuotationConfig.THREAD_SLEEP_REALDEAL = Long.valueOf(quotationProp.getProperty("sleep.realdeal"));
        // 最新撮合行情休眠时间 1秒
        InQuotationConfig.THREAD_SLEEP_RTQUOTATION = Long.valueOf(quotationProp.getProperty("sleep.quotation"));
        // 全行情推送属性文件
        PropertiesUtils allQuotationProp = new PropertiesUtils("allQuotation.properties");
        // 最新全行情推送休眠时间
        InQuotationConfig.THREAD_SLEEP_ALLRTQUOTATION = Integer.valueOf(allQuotationProp.getProperty("sleep.allQuotation"));
        // 交易对id列表
        InQuotationConfig.QUOTATION_STOCKS = allQuotationProp.getProperty("stocks");
        // 全行情主题
        InQuotationConfig.TOPIC_ALLQUOTATION = allQuotationProp.getProperty("topic.allRtQuotation");
    }
    
    public static void preProcessSQLByConfig()
    {
        // String channel = new StringBuffer("\'").append(InQuotationConfig.QUOTATION_CHANNEL).append("\'").toString();
        StringBuffer sqlBuffer = new StringBuffer();
        for (int i = 0; i <= InQuotationConfig.ENTRUST_DEEPLEVEL; i++)
        {
            if (i == 0)
            {
                sqlBuffer.append(StringUtils.replaceEach(QuerySqlConfig.SQL_DEEPPRICE_UNIT, new String[]{"#level#", "#rownum#","#buySort#","#sellSort#"},
                        new String[]{String.valueOf(i), InQuotationConfig.ENTRUST_NUM,InQuotationConfig.ENTRUST_BUY_SORT,InQuotationConfig.ENTRUST_SELL_SORT}));
            }
            else
            {
                sqlBuffer.append(" union all ");
                sqlBuffer.append(StringUtils.replaceEach(QuerySqlConfig.SQL_DEEPPRICE_UNIT, new String[]{"#level#", "#rownum#","#buySort#","#sellSort#"},
                        new String[]{String.valueOf(i), InQuotationConfig.ENTRUST_NUM,InQuotationConfig.ENTRUST_BUY_SORT,InQuotationConfig.ENTRUST_SELL_SORT}));
            }
        }
        String priceDigit = getDigitFormat(InQuotationConfig.QUOTATION_PRICE_DIGIT);
        String amtDigit = getDigitFormat(InQuotationConfig.QUOTATION_AMT_DIGIT);
        String accumulatebalDigit = getDigitFormat(InQuotationConfig.QUOTATION_ACCUMULATEBAL_DIGIT);
        QuerySqlConfig.SQL_GET_DEEPPRICE = StringUtils.replaceEach(QuerySqlConfig.SQL_GET_DEEPPRICE, new String[]{"#priceDigit#", "#amtDigit#", "#accumulatebalDigit#"},
                new String[]{priceDigit, amtDigit, accumulatebalDigit});
        QuerySqlConfig.SQL_GET_DEEPPRICE = sqlBuffer.insert(0, QuerySqlConfig.SQL_GET_DEEPPRICE).append(")").toString();
    }
    
    private static String getDigitFormat(int digit)
    {
        StringBuffer strDigit = new StringBuffer("\'FM9999999999999990.");
        for (int i = 0; i < digit; i++)
        {
            strDigit.append("0");
        }
        return strDigit.append("\'").toString();
    }
    
    public static void main(String[] args)
    {
        // InQuotationConfig.TBL_ENTRUST = "EntrustVCoinMoney02";
        // InQuotationConfig.ENTRUST_DEEPLEVEL = 0;
        // preProcessSQLByConfig();
        // QuerySqlConfig.SQL_GET_QUOTATION = StringUtils.replaceEach(QuerySqlConfig.SQL_GET_QUOTATION, new String[]{"#stockid#", "#channel#"}, new String[]{"177777777703",
        // "\'kraken\'"});
        // System.out.println(QuerySqlConfig.SQL_GET_DEEPPRICE);
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(StringUtils.replaceEach(QuerySqlConfig.SQL_DEEPPRICE_UNIT, new String[]{"#level#", "#rownum#"},
                new String[]{String.valueOf(1), InQuotationConfig.ENTRUST_NUM}));
        String priceDigit = getDigitFormat(2);
        String amtDigit = getDigitFormat(4);
        String accumulatebalDigit = getDigitFormat(8);
        System.out.println("改动前：" + QuerySqlConfig.SQL_GET_DEEPPRICE);
        QuerySqlConfig.SQL_GET_DEEPPRICE = StringUtils.replaceEach(QuerySqlConfig.SQL_GET_DEEPPRICE, new String[]{"#priceDigit#", "#amtDigit#", "#accumulatebalDigit#"},
                new String[]{priceDigit, amtDigit, accumulatebalDigit});

        QuerySqlConfig.SQL_GET_DEEPPRICE = sqlBuffer.insert(0, QuerySqlConfig.SQL_GET_DEEPPRICE).append(")").toString();
        System.out.println("改动后：" + QuerySqlConfig.SQL_GET_DEEPPRICE);
    }
}
