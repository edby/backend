package com.blocain.bitms.monitor.consts;

import java.util.concurrent.ConcurrentHashMap;

public class MonitorConst
{
    private MonitorConst()
    {// 防止实例化
    }

    //============ 存储过程出入参，返回值常亮定义
    // 监控指标
    public static final String            CHECK_MONITOR_IDX          = "monitor_idx";
    // 存储过程出参，执行结果
    public static final String            CHECK_RETURN_CODE          = "return_code";

    // 存储过程出参，对账结果
    public static final String            CHECK_RETURN_CHEKRESULT    = "monitor_result";

    // 存储过程出参，提示信息
    public static final String            CHECK_RETURN_CHEKMSG       = "return_str";

    // 存储过程出参，预警内容
    public static final String            CHECK_RETURN_DATA          = "monitor_data";

    // 存储过程出参，监控结果，正常
    public static final Integer CHECK_CHEKRESULT_NORMAL              = 1;

    public static final Integer CHECK_RETURNCODE_NORMAL              = 1;

    // 存储过程出参，监控结果，触发预警
    public static final Integer CHECK_RETDATA_WARN                       = -1;

    // 存储过程出参，监控结果，触发禁止
    public static final Integer CHECK_RETDATA_FORBID                     = -2;

    //======= 监控服务配置常亮定义

    /**
     * 执行方式：冻结账户
     */
    public static final String MONITOR_ACTIONTYPE_FROZENACCT            = "FrozenAcct";

    /**
     * 执行方式：短信和邮件通知
     */
    public static final String MONITOR_ACTIONTYPE_MESSAGE               = "Message";

    /**
     * 执行方式：关闭交易和提现
     */
    public static final String MONITOR_ACTIONTYPE_CLOSETRADEANDWITHDRAW = "CloseTradeAndWithdraw";


    public static final String MONITOR_MSGTYPE_SMS = "SMS";

    public static final String MONITOR_MSGTYPE_EMAIL = "EMAIL";

    /**
     * 监控参数类型：通用
     */
    public static final String  MONITOR_PARAMTYPE_COMMON                 = "COMMON";

    /**
     * 监控参数类型：保证金监控专用
     */
    public static final String  MONITOR_PARAMTYPE_MARGIN                 = "MARGIN";

    /**
     * 监控参数类型：杠杆现货资金总账实时监控
     */
    public static final String  MONITOR_PARAMTYPE_INTERNALPLATFUNDCUR   = "INTERNALPLATFUNDCUR";

    /**
     * 账户级别监控
     */
    public static final String  MONITOR_PARAMTYPE_ACCTFUNDCUR            = "ACCTFUNDCUR";
    /**
     * 数字资产总额监控
     */
    public static final String  MONITOR_PARAMTYPE_DIGITALCOIN             = "DIGITALCOIN";

    public static final String  MONITOR_PARAMTYPE_CASHCOIN                = "CASHCOIN";

    public static final String  MONITOR_PARAMTYPE_BLOCKNUM                = "BLOCKNUM";

    public static final String  MONITOR_PARAMTYPE_ERC20BAL                = "ERC20BAL";

    public static final String  MONITOR_PARAMTYPE_ERC20COLDWALLET         = "ERC20COLDWALLET";

    public static final String  MONITOR_PARAMTYPE_ERC20HOTWALLET          = "ERC20HOTWALLET";

    public static final String  MONITOR_PARAMTYPE_ERC20COLLECTFEE         = "ERC20COLLECTFEE";
    /**
     * 账户资产类别-钱包账户资产
     */
    public static final Integer           MONITOR_ASSETTYPE_WALLET   = 1;

    /**
     * 账户资产类别-合约账户资产
     */
    public static final Integer           MONITOR_ASSETTYPE_CONTRACT = 2;


    /**
     * 监控配置
     */
    public static final String            MONITOR_CONFIG             = "CONFIG";

    public static final Integer           MONITOR_LEVEL_WARN         = 1;

    public static final Integer           MONITOR_LEVEL_FORBID       = 2;

    public static   ConcurrentHashMap CACHE_MAP                  = new ConcurrentHashMap();
}
