package com.blocain.bitms.boss.common.consts;

/**
 * 系统参数常量
 * <p>File：ParamConsts.java</p>
 * <p>Title: ParamConsts</p>
 * <p>Description:ParamConsts</p>
 * <p>Copyright: Copyright (c) 2017年7月28日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class ParamConsts
{
    private ParamConsts()
    {// 防止实例化
    }
    
    // 系统参数 系统名称
    public static final String SYS_PARAMETER_SYSTEM_NAME_TRADE              = "TRADE";
    
    // 单用户使用BTC认购/预购限额
    public static final String DO_SUBSCRIBE_PERIOD_UP_LIMIT                 = "DoSubscribePeriodUpLimt";
    
    // 未认证用户 提币转账BTC当天额度上限
    public static final String WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER          = "WithdrawTansferBTCDayQuotaUpper";
    
    // 已认证用户 提币转账BTC当天额度上限
    public static final String WITHDRAW_TANSFER_BTC_DAY_QUOTAUPPER_FOR_AUTH = "AuthUserWithdrawBTCDayQuotaUpper";
    
    // 提币转账LTC费用推荐值
    public static final String WITHDRAW_TANSFER_LTC_COSTAVER                = "WithdrawTansferLTCCostAver";
    
    // 提币转账LTC费用下限
    public static final String WITHDRAW_TANSFER_LTC_COSTLOW                 = "WithdrawTansferLTCCostLow";
    
    // 提币转账LTC费用上限
    public static final String WITHDRAW_TANSFER_LTC_COSTUPPER               = "WithdrawTansferLTCCostUpper";
    
    // 提币转账LTC当天额度上限
    public static final String WITHDRAW_TANSFER_LTC_DAY_QUOTAUPPER          = "WithdrawTansferLTCDayQuotaUpper";
    
    // 提币转账ETH费用推荐值
    public static final String WITHDRAW_TANSFER_ETH_COSTAVER                = "WithdrawTansferETHCostAver";
    
    // 提币转账ETH费用下限
    public static final String WITHDRAW_TANSFER_ETH_COSTLOW                 = "WithdrawTansferETHCostLow";
    
    // 提币转账ETH费用上限
    public static final String WITHDRAW_TANSFER_ETH_COSTUPPER               = "WithdrawTansferETHCostUpper";
    
    // 提币转账ETH当天额度上限
    public static final String WITHDRAW_TANSFER_ETH_DAY_QUOTAUPPER          = "WithdrawTansferETHDayQuotaUpper";
    
    // 提币转账BTC费用推荐值
    public static final String WITHDRAW_TANSFER_BTC_COSTAVER                = "WithdrawTansferBTCCostAver";
    
    // 提币转账BTC费用下限
    public static final String WITHDRAW_TANSFER_BTC_COSTLOW                 = "WithdrawTansferBTCCostLow";
    
    // 提币转账BTC费用上限
    public static final String WITHDRAW_TANSFER_BTC_COSTUPPER               = "WithdrawTansferBTCCostUpper";
    
    // ICO模块开关
    public static final String ICO_SWITCH                                   = "IcoSwitch";
    
    // PUSH模块开关
    public static final String PUSH_SWITCH                                  = "PushSwitch";
    
    // 集市模块开关
    public static final String FAIR_SWITCH                                  = "FairSwitch";
    
    // 提币模块开关
    public static final String WITHDRAW_SWITCH                              = "WithdrawSwitch";
    
    // 撮合交易模块开关
    public static final String MATCHTRADE_SWITCH                            = "MatchTradeSwitch";
    
    // 自动爆仓模块开关
    public static final String CLOSEPOSITION_SWITCH                         = "ClosePositionSwitch";
    
    // OSS文件服务器路径
    public static final String OSS_FILE_BASE_URL                            = "OssFileBaseUrl";
    
    // 撮合交易单用户单币对最大委托数量
    public static final String MATCHTRADE_TRADE_ACCOUNT_MAX_ENTRUST_CNT     = "MatchTradeAccMaxEntrustCnt";
    
    // 撮合交易单币对最大委托数量
    public static final String MATCHTRADE_TRADE_MONEY_MAX_ENTRUST_CNT       = "MatchTradeMoneyMaxEntrustCnt";
    
    // 提币地址几天后有效
    public static final String WITHDRAW_ADDR_CAN_USE_AFTER_DAYS             = "WithdrawAddrCanUseAfterDays";
    
    // ERC20 TOKEN 交易对开通手续费
    public static final String ERC20TOKEN_PAIR_ACTIVE_FEE                   = "Erc20TokenPairActiveFee";
    
    // ERC20 TOKEN 交易对开通奖励
    public static final String ERC20TOKEN_PAIR_ACTIVE_AWARD                 = "Erc20TokenPairActiveAward";
    
    // ERC20 TOKEN 提币手续费折合ETH
    public static final String ERC20TOKEN_WITHDRAW_ETH_FEE                  = "Erc20TokenWithdrawEthFee";
    
    // ERC20 TOKEN TOKEN单笔提现额度折合ETH
    public static final String ERC20TOKEN_WITHDRAW_UP_ETH_FOR_ONE           = "Erc20TokenWithdrawETHforOne";
    
    // ERC20 TOKEN TOKEN单日提现次数
    public static final String ERC20TOKEN_WITHDRAW_CNT_ETH_FOR_DAY          = "Erc20TokenWithdrawCntForDay";
}
