package com.blocain.bitms.trade.account.consts;

/**
 * 帐户模块常量 介绍
 * <p>File：AccountConsts.java </p>
 * <p>Title: AccountConsts </p>
 * <p>Description:AccountConsts </p>
 * <p>Copyright: Copyright (c) 2017/7/10 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountConsts
{
    private AccountConsts()
    {
    }
    
    // 正常
    public static final Integer ACCOUNT_STATUS_NORMAL           = 0;
    
    // 冻结
    public static final Integer ACCOUNT_STATUS_FROZEN           = 1;
    
    // 注销
    public static final Integer ACCOUNT_STATUS_CLOSE            = 2;
    
    // 默认安全验证策略
    public static final Integer SECURITY_POLICY_DEFAULT         = 0;
    
    // 安全验证策略启用SMS
    public static final Integer SECURITY_POLICY_NEEDSMS         = 1;
    
    // 安全验证策略启用GA
    public static final Integer SECURITY_POLICY_NEEDGA          = 2;
    
    // 安全验证策略启用SMS或GA
    public static final Integer SECURITY_POLICY_NEEDGAORSMS     = 3;
    
    // 安全验证策略启用SMS和GA
    public static final Integer SECURITY_POLICY_NEEDGAANDSMS    = 4;
    
    // 默认交易验证策略
    public static final Integer TRADE_POLICY_DEFAULT            = 0;
    
    // 交易验证策略两小时验证一次
    public static final Integer TRADE_POLICY_TWOHOUR            = 1;
    
    // 交易验证策略每次都验证
    public static final Integer TRADE_POLICY_EVERYTIME          = 2;
    
    // 验证方式：短信
    public static final String  ACCOUNT_VALID_SMS               = "sms";
    
    // 验证方式：GA
    public static final String  ACCOUNT_VALID_GA                = "ga";
    
    public static final String  ACCOUNT_GRANT_YES               = "yes";
    
    public static final String  ACCOUNT_GRANT_NO                = "no";
    
    public static final String  FROZEN_REASON_BIND_PHONE        = "冻结原因:绑定手机";
    
    public static final String  FROZEN_REASON_CHANGE_WALLET     = "冻结原因:修改钱包账户提现";
    
    public static final String  FROZEN_REASON_DO_BUY_FAIR       = "冻结原因:无委托买入";
    
    public static final String  FROZEN_REASON_DO_SELL_FAIR      = "冻结原因:无委托卖出";
    
    public static final String  FROZEN_REASON_ACCEPT_PUSH       = "冻结原因:PUSH成交";
    
    public static final String  FROZEN_REASON_DO_PUSH_SELL_FAIR = "冻结原因:集市交易发起委托卖出";
    
    public static final String  FROZEN_REASON_DO_PUSH_BUY_FAIR  = "冻结原因:集市交易发起委托买入";
    
    public static final String  FROZEN_REASON_DO_WITHDRAW       = "冻结原因:提币申请";
    
    public static final String  FROZEN_REASON_DO_PUSH           = "冻结原因:发送PUSH请求";
    
    public static final String  FROZEN_REASON_DO_SELL           = "冻结原因:提币申请";
}
