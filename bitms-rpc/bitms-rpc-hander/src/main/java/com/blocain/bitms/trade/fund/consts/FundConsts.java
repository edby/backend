/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.consts;

/**
 * <p>File：FundConsts.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月10日 上午10:44:38</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class FundConsts
{
    // 证券信息 现金货币
    public static final Long    WALLET_BMS_TYPE                                                              = 100000000000L;                                       // BMS
    
    public static final Long    WALLET_USD_TYPE                                                              = 100000000001L;                                       // USD
    
    public static final Long    WALLET_EUR_TYPE                                                              = 100000000002L;                                       // EUR
    
    // 证券信息 数字货币
    public static final Long    WALLET_BTC_TYPE                                                              = 111111111101L;                                       // BTC
    
    public static final Long    WALLET_ETH_TYPE                                                              = 111111111102L;                                       // ETH
    
    public static final Long    WALLET_BITMS_TYPE                                                            = 122222222201L;                                       // BITMS
    
    public static final Long    WALLET_BIEX_TYPE                                                             = 122222222202L;                                       // BIEX
    
    // ERC20 VIN2ETH 的默认合约地址
    public static final String  WALLET_VINPAIR_CONTRACTADDR                                                  = "0x806336c912762274bfc4d0f78b1be2c0119e86f0";
    
    // 证券信息 现货币对
    public static final Long    WALLET_BTC2EUR_TYPE                                                          = 133333333302L;                                       // BTC2EUR
    
    public static final Long    WALLET_BIEX2BTC_TYPE                                                         = 144444444402L;                                       // BIEX2BTC
    
    public static final Long    WALLET_ETH2BTC_TYPE                                                          = 144444444403L;                                       // 2BTC
    
    // 证券信息 杠杆币对
    public static final Long    WALLET_BTC2USD_TYPE                                                          = 155555555502L;                                       // BTC2USD
    
    public static final Long    WALLET_USD2BTC_TYPE                                                          = 166666666602L;                                       // USD2BTC
    
    // 证券信息 合约币对
    public static final Long    WALLET_BTC2USDX_TYPE                                                         = 177777777702L;                                       // BTC2USDX
    
    public static final Long    WALLET_USDZ2BTC_TYPE                                                         = 188888888802L;                                       // USDZ2BTC
    
    // 证券信息 待整理
    public static final Long    WALLET_USDX2ETH_TYPE                                                         = 177777777902L;                                       // ETH/USDX
    
    public static final Long    WALLET_BTC2LTCX_TYPE                                                         = 177777777703L;                                       // BTC/LTCX
    
    public static final Long    WALLET_BTC2ETHX_TYPE                                                         = 177777777704L;                                       // BTC/ETHX
    
    public static final Long    WALLET_LTCZ2BTC_TYPE                                                         = 188888888803L;                                       // LTCZ/BTC
    
    public static final Long    WALLET_ETHZ2BTC_TYPE                                                         = 188888888804L;                                       // ETHZ/BTC
    
    public static final Long    WALLET_LTC_TYPE                                                              = 177777777801L;                                       // LTC
    
    public static final Long    WALLET_USDX2LTC_TYPE                                                         = 177777777802L;                                       // LTC/USDX
    
    public static final Long    WALLET_BTCIndex_TYPE                                                         = 177777777801L;                                       // BTCIndex
    
    public static final Long    WALLET_VIN2ETH_TYPE                                                          = 154444444402L;                                       // VIN2ETH
    
    // 费率类型-提现手续费
    public static final String  RATE_TYPE_WITHDRAW_FEE                                                       = "WithDrawFeeRate";                                   // 提现手续费
    
    // 持仓方向
    public static final String  ASSET_DIRECTION_LONG                                                         = "Long";                                              // 多头
    
    public static final String  ASSET_DIRECTION_SHORT                                                        = "Short";                                             // 空头
    
    // 钱包交易确认方
    public static final String  WALLET_TRANS_CONFIRMSIDE_BTC                                                 = "btc";
    
    public static final String  WALLET_TRANS_CONFIRMSIDE_BITGO                                               = "bitgo";
    
    public static final String  WALLET_TRANS_CONFIRMSIDE_BLOCKMETA                                           = "blockmeta";
    
    // 钱包区块确认状态
    public static final String  WALLET_TRANS_STATUS_UNCONFIRM                                                = "unconfirm";                                         // 未确认
    
    public static final String  WALLET_TRANS_STATUS_CONFIRM                                                  = "confirm";                                           // 已确认
    
    // 钱包用途类别
    public static final String  WALLET_USAGE_TYPE_CHARGE_ACCOUNT                                             = "chargeAccount";                                     // 充币转账
    
    public static final String  WALLET_USAGE_TYPE_COLLECT_ACCOUNT                                            = "collectAccount";                                    // 充币归集
    
    public static final String  WALLET_USAGE_TYPE_TRANSFER_ACCOUNT                                           = "transferAccount";                                   // 提币划拨
    
    // 钱包归集状态
    public static final String  WALLET_COLLECT_STATUS_NO                                                     = "noCollect";                                         // 无需归集
    
    public static final String  WALLET_COLLECT_STATUS_UN                                                     = "unCollect";                                         // 未归集
    
    public static final String  WALLET_COLLECT_STATUS_ED                                                     = "collected";                                         // 已归集
    
    // 认证状态
    public static final String  WALLET_AUTH_STATUS_AUTH                                                      = "auth";                                              // 已认证
    
    public static final String  WALLET_AUTH_STATUS_UNAUTH                                                    = "unauth";                                            // 未认证
    
    // 钱包区块资金方向
    public static final String  WALLET_TRANS_FUND_DIRECT_COLLECT                                             = "collect";                                           // 收款
    
    public static final String  WALLET_TRANS_FUND_DIRECT_PAYMENT                                             = "payment";                                           // 付款
    
    // 业务类别businessFlag
    public static final String  SYSTEM_BUSSINESS_FLAG                                                        = "businessFlag";                                      // 顶级
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE                                         = "walletRecharge";                                    // 钱包账户充值
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE_SDF                                     = "walletRechargeSDF";                                 // 钱包账户小额充值手续费
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW                                         = "walletWithdraw";                                    // 钱包账户提现
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL                                  = "walletWithdrawCancel";                              // 钱包账户提现取消
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT                                  = "walletWithdrawReject";                              // 钱包账户提现拒绝（包括审核拒绝和复核拒绝）
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PLAT_WALLET_WITHDRAW_NETFEE_CHANGE                     = "walletWithdrawNetFeeChange";                        // 钱包提现网络手续费调整
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT                                      = "wallet2Contract";                                   // 钱包账户转合约账户
    
    public static final String  SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET                                      = "contract2Wallet";                                   // 合约账户转钱包账户
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT                                          = "wallet2Spot";                                       // 钱包账户转杠杆现货账户
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET                                          = "spot2Wallet";                                       // 杠杆现货账户转钱包账户
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH                                          = "spot2Wealth";                                       // 钱包账户转理财账户
    
    public static final String  SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT                                          = "wealth2Spot";                                       // 理财账户转钱包账户
    
    public static final String  SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_PRE_REQ                                   = "icoSubscribePreReq";                                // ICO预购申请
    
    public static final String  SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_REQ                                       = "icoSubscribeReq";                                   // ICO正式认购申请
    
    public static final String  SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_SUCCESS                                   = "icoSubscribeSuccess";                               // ICO认购确认成功
    
    public static final String  SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_FAIL                                      = "icoSubscribeFail";                                  // ICO认购确认失败
    
    public static final String  SYSTEM_BUSSINESS_FLAG_ICOMINT                                                = "icoMint";                                           // ICO锻造铸币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_REGIST_AWARD                                           = "registAward";                                       // 注册邀请奖励
    
    public static final String  SYSTEM_BUSSINESS_FLAG_TRADE_AWARD                                            = "tradeAward";                                        // BIEX交易奖励
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD                              = "platformAdjustAssetAdd";                            // 余额数量平台调增
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB                              = "platformAdjustAssetSub";                            // 余额数量平台调减
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD                        = "platformAdjustForzenAssetAdd";                      // 冻结数量平台调增
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB                        = "platformAdjustForzenAssetSub";                      // 冻结数量平台调减
    
    public static final String  SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN                                         = "assetUnFrozen";                                     // 账户资产解冻
    
    // Push
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST                              = "pushTradeSpotBuyEntrust";                           // Push交易现货买入委托
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST                             = "pushTradeSpotSellEntrust";                          // Push交易现货卖出委托
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_WITHDRAW                     = "pushTradeSpotBuyEntrustWithdraw";                   // Push交易现货买入委托撤单
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_WITHDRAW                    = "pushTradeSpotSellEntrustWithdraw";                  // Push交易现货卖出委托撤单
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_REJECT                       = "pushTradeSpotBuyEntrustReject";                     // Push交易现货买入委托拒绝
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_REJECT                      = "pushTradeSpotSellEntrustReject";                    // Push交易现货卖出委托拒绝
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_DEAL                         = "pushTradeSpotBuyEntrustDeal";                       // Push交易现货买入委托成交
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_DEAL                        = "pushTradeSpotSellEntrustDeal";                      // Push交易现货卖出委托成交
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_NOENTRUST_DEAL                       = "pushTradeSpotBuyNoEntrustDeal";                     // Push交易现货买入无委托成交
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_NOENTRUST_DEAL                      = "pushTradeSpotSellNoEntrustDeal";                    // Push交易现货卖出无委托成交
    
    // 集市
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST                              = "fairTradeSpotBuyEntrust";                           // 集市交易现货买入委托
    
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST                             = "fairTradeSpotSellEntrust";                          // 集市交易现货卖出委托
    
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_WITHDRAW                     = "fairTradeSpotBuyEntrustWithdraw";                   // 集市交易现货买入委托撤单
    
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_WITHDRAW                    = "fairTradeSpotSellEntrustWithdraw";                  // 集市交易现货卖出委托撤单
    
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_DEAL                         = "fairTradeSpotBuyEntrustDeal";                       // 集市交易现货买入委托成交
    
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_DEAL                        = "fairTradeSpotSellEntrustDeal";                      // 集市交易现货卖出委托成交
    
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_NOENTRUST_DEAL                       = "fairTradeSpotBuyNoEntrustDeal";                     // 集市交易现货买入无委托成交
    
    public static final String  SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_NOENTRUST_DEAL                      = "fairTradeSpotSellNoEntrustDeal";                    // 集市交易现货卖出无委托成交
    
    // 撮合交易
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST                             = "matchTradeSpotBuyEntrust";                          // 撮合交易现货买入委托
    
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST                            = "matchTradeSpotSellEntrust";                         // 撮合交易现货卖出委托
    
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST_WITHDRAW                    = "matchTradeSpotBuyEntrustWithdraw";                  // 撮合交易现货买入委托撤单
    
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST_WITHDRAW                   = "matchTradeSpotSellEntrustWithdraw";                 // 撮合交易现货卖出委托撤单
    
    // 撮合交易借款还款
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT                             = "matchTradeSpotAutoDebit";                           // 撮合交易现货自动借贷
    
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT                   = "matchTradeSpotAutoDebitRepayment";                  // 撮合交易现货借贷自动还款
    
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_INTEREST                    = "matchTradeSpotAutoDebitInterest";                   // 撮合交易现货借贷自动计息
    
    public static final String  SYSTEM_BUSSINESS_FLAG_AUTO_WEALTH_INTEREST                                   = "autoWealthInterest";                                // 理财自动计息
    
    // 强平
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER          = "matchTradeSpotClosePositionAssetTransfer";          // 强制平仓账户资产转移
    
    public static final String  SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER          = "matchTradeSpotClosePositionDebitTransfer";          // 强制平仓借贷资产转移
    
    public static final String  SYSTEM_BUSSINESS_FLAG_POSITION_PREMINM_FEE                                   = "positionPremiumFee";                                // 持仓调节费
    
    // 交割
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_TRANSFER                              = "settlementMoneyTransfer";                           // 交割结算转移Money债务内部转移
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_TRANSFER                              = "settlementVCoinTransfer";                           // 交割结算转移VCoin债务内部转移
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_MONEY                           = "settlementAutoBorrowMoney";                         // 交割结算法定货币自动借款
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_VCOIN                           = "settlementAutoBorrowVCoin";                         // 交割结算数字货币自动借款
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MOVE_VCOIN                                  = "settlementMoveVCoinTransfer";                       // 交割结算超级用户数字货币内部转移
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MOVE_MONEY                                  = "settlementMoveMoneyTransfer";                       // 交割结算超级用户法定货币内部转移
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_VCOIN_ASSET_TO_ZREO            = "settlementSuperAccountVCOINAssetToZrea";            // 交割结算超级用户合约资产清零
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_VCOIN_DEBIT_TO_ZREO            = "settlementSuperAccountVCOINDebitToZrea";            // 交割结算超级用户借款清零
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_MONEY_ASSET_TO_ZREO            = "settlementSuperAccountMONEYAssetToZrea";            // 交割结算超级用户合约资产清零
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_MONEY_DEBIT_TO_ZREO            = "settlementSuperAccountMONEYDebitToZrea";            // 交割结算超级用户借款清零
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_MOVE_INCREASE      = "settlementContributionQuotaVcoinMoveIncrease";      // 交割结算准备金增加数字货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_MOVE_DECREASE      = "settlementContributionQuotaVcoinMoveDecrease";      // 交割结算准备金转移减少数字货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_INCREASE      = "settlementContributionQuotaMoneyMoveIncreaset";     // 交割结算准备金增加法定货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE      = "settlementContributionQuotaMoneyMoveDecrease";      // 交割结算准备金转移减少法定货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_INCREASE = "settlementContributionQuotaVcoinDeductionIncrease"; // 交割结算准备金抵扣分摊增加数字货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE = "settlementContributionQuotaVcoinDeductionDecrease"; // 交割结算准备金抵扣分摊减少数字货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_EXCHANGE_DECREASE  = "settlementContributionQuotaMoneyExchangeDecrease";  // 交割结算准备金兑换减少法定货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_EXCHANGE_INCREASE  = "settlementContributionQuotaVcoinExchangeIncrease";  // 交割结算准备金兑换增加数字货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_EXCHANGE_INCREASE  = "settlementContributionQuotaMoneyExchangeIncrease";  // 交割结算准备金兑换增加法定货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_EXCHANGE_DECREASE  = "settlementContributionQuotaVcoinExchangeDecrease";  // 交割结算准备金兑换减少数字货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PLAT_SHARE_OF_LOSSES_VCOIN_MONEY                       = "platSharingOfLossesVCoinMoney";                     // 平台交割结算亏损分摊
    
    public static final String  SYSTEM_BUSSINESS_FLAG_PLAT_SETTLEMENT_VCOIN_MONEY_EXCHANGE_MONEY_TO_VCOIN    = "platSettlementVCoinMoneyExchangMoneyToVCoin";       // 平台交割结算法定货币兑换数字货币
    
    public static final String  SYSTEM_BUSSINESS_FLAG_TRADE_ACTIVE_TOKEN_TRADE_FEE                           = "activeTokenTradeFee";                               // 激活TOKEN交易手续费
    
    public static final String  SYSTEM_BUSSINESS_FLAG_TRADE_ACTIVE_TOKEN_TRADE_AWARD                         = "activeTokenTradeAward";                             // 激活TOKEN交易奖励

    public static final String  SYSTEM_BUSSINESS_FLAG_TRADE_RECHARGE_AWARD                                   = "rechargeAward";                             // 充值奖励

    // 账户资产类型
    public static final String  ACCOUNT_ASSET_TYPE                                                           = "accountAssetType";                                  // 顶级
    
    public static final String  ACCOUNT_ASSET_TYPE_WALLET_ASSET                                              = "walletAccountAsset";                                // 钱包账户资产
    
    public static final String  ACCOUNT_ASSET_TYPE_SPOT_ASSET                                                = "spotAccountAsset";                                  // 现货账户资产
    
    public static final String  ACCOUNT_ASSET_TYPE_SPOT_DEBIT                                                = "spotAccountDebit";                                  // 现货账户负债
    
    public static final String  ACCOUNT_ASSET_TYPE_CONTRACT_ASSET                                            = "contractAccountAsset";                              // 合约账户资产
    
    public static final String  ACCOUNT_ASSET_TYPE_CONTRACT_DEBIT                                            = "contractAccountDebit";                              // 合约账户负债
    
    public static final String  ACCOUNT_ASSET_TYPE_WEALTH_ASSET                                              = "wealthAccountAsset";                                // 理财账户资产
    
    public static final String  ACCOUNT_ASSET_TYPE_WEALTH_DEBIT                                              = "wealthAccountDebit";                                // 理财账户负债
    
    // 资产发生方向(增加、减少、冻结、解冻)
    public static final String  ACCOUNT_ASSET_DIRECT                                                         = "assetDirect";                                       // 顶级
    
    public static final String  ACCOUNT_ASSET_DIRECT_INCREASE                                                = "increase";                                          // 增加
    
    public static final String  ACCOUNT_ASSET_DIRECT_DECREASE                                                = "decrease";                                          // 减少
    
    public static final String  ACCOUNT_ASSET_DIRECT_FROZEN                                                  = "frozen";                                            // 冻结
    
    public static final String  ACCOUNT_ASSET_DIRECT_UNFROZEN                                                = "unfrozen";                                          // 解冻
    
    // 账户资金流水状态
    public static final String  ACCOUNT_FUND_CURRENT_EFFECTIVE                                               = "effective";                                         // 有效
    
    public static final String  ACCOUNT_FUND_CURRENT_INVALID                                                 = "invalid";                                           // 无效
    
    // 账户资金流水审核状态
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_CANCEL                                           = "cancel";                                            // 申请取消
    
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE                                        = "noApprove";                                         // 无需审批
    
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_WAITING_EMAIL_CONFIRM                            = "waitingEmailConfirm";                               // 待Email确认
    
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING                                     = "auditPending";                                      // 待审核
    
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING                                     = "checkPending";                                      // 待复核
                                                                                                                                                                    // 也就是
                                                                                                                                                                    // 审核通过
    
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT                                     = "auditReject";                                       // 审核拒绝
    
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH                                     = "checkThrough";                                      // 复核通过
    
    public static final String  ACCOUNT_FUND_APPROVE_STATUS_CHECKREJECT                                      = "checkReject";                                       // 复核拒绝
    
    // 账户资金流水划拨状态
    public static final String  ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER                                      = "noTransfer";                                        // 无需划拨
    
    public static final String  ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER                                      = "unTransfer";                                        // 未划拨
    
    public static final String  ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING                                 = "transferPending";                                   // 待划拨
    
    public static final String  ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER                                        = "transfer";                                          // 已划拨
    
    public static final String  ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER_REJECTED                               = "transferRejected";                                  // 划拨审批拒绝
    
    // 锁定状态
    public static final String  ASSET_LOCK_STATUS_YES                                                        = "yes";                                               // 已锁定
    
    public static final String  ASSET_LOCK_STATUS_NO                                                         = "no";                                                // 已解锁
    
    // 共用状态 yes or no
    public static final String  PUBLIC_STATUS_YES                                                            = "yes";
    
    public static final String  PUBLIC_STATUS_NO                                                             = "no";
    
    public static final Long    SYSTEM_ACCOUNT_ID                                                            = 200000000000L;                                       // 平台超级用户
    
    public static final Long    SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID                                 = 199999999999L;                                       // 平台撮合交易费用超级账户
    
    public static final Long    SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID                                = 199999999998L;                                       // 平台撮合交易多头超级账户
    
    public static final Long    SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID                               = 199999999997L;                                       // 平台撮合交易空头超级账户
    
    public static final Long    SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID                  = 199999999996L;                                       // 平台撮合交易准备金分摊超级账户
    
    public static final Long    SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID                              = 199999999995L;
    
    public static final Long    SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_DEBIT_WEALTH_ID                        = 199999999994L;                                       // 累计理财利息超级用户
    
    public static final Long    SYSTEM_ACCOUNT_MATCHTRADE_POSITION_PREMINM_FEE_ID                            = 199999999993L;                                       // 持仓调节费超级用户
    
    // 提币申请临时表 状态 待确认confirming、待邮件激活activating、已处理done
    public static final String  FUND_WITHDRAW_APPLY_STATUS_CONFIRMING                                        = "confirming";
    
    public static final String  FUND_WITHDRAW_APPLY_STATUS_ACTIVATING                                        = "activating";
    
    public static final String  FUND_WITHDRAW_APPLY_STATUS_DONE                                              = "done";
    
    // 借款货币类型
    public static final String  BORROW_STOCKINFO_TYPE_DIGITAL_CASH                                           = "digitalCash";                                       // 数字货币
    
    public static final String  BORROW_STOCKINFO_TYPE_LEGAL_MONEY                                            = "legalMoney";                                        // 法定货币
    
    // 委托交易费率
    public static final String  MATCHTRADE_BUY_FEE_RATE                                                      = "tradeBuyFee";
    
    public static final String  MATCHTRADE_SELL_FEE_RATE                                                     = "tradeSellFee";
    
    // 证券类型
    public static final String  STOCKTYPE_DIGITALCOIN                                                        = "digitalCoin";                                       // 数字货币
    
    public static final String  STOCKTYPE_CASHCOIN                                                           = "cashCoin";                                          // 现金货币
    
    public static final String  STOCKTYPE_ERC20_TOKEN                                                        = "erc20Token";                                        // ETH
                                                                                                                                                                    // ERC20TOKE货币
    
    public static final String  STOCKTYPE_PURESPOT                                                           = "pureSpot";                                          // 纯正现货
    
    public static final String  STOCKTYPE_LEVERAGEDSPOT                                                      = "leveragedSpot";                                     // 杠杆现货
    
    public static final String  STOCKTYPE_CONTRACTSPOT                                                       = "contractSpot";                                      // 合约现货
    
    // ERC20钱包类型
    public static final Integer WALLETTYPE_HWALLET                                                           = 1;
    
    public static final Integer WALLETTYPE_CWALLET                                                           = 2;
    
    public static final Integer WALLETTYPE_COLLECTFEE                                                        = 3;
}
