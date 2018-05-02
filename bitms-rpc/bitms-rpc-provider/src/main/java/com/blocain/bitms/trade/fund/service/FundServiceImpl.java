/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.entity.AccountInvitation;
import com.blocain.bitms.trade.account.service.AccountInvitationService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.model.DebitAssetModel;
import com.blocain.bitms.trade.fund.model.FundCurrentModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 资金类统一服务实现类
 * <p>File：FundServiceImpl.java </p>
 * <p>Title: FundServiceImpl </p>
 * <p>Description:FundServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
@Service
public class FundServiceImpl implements FundService
{
    public static final Logger             logger    = LoggerFactory.getLogger(FundServiceImpl.class);
    
    @Autowired
    private AccountWalletAssetService      accountWalletAssetService;
    
    @Autowired
    private AccountSpotAssetService        accountSpotAssetService;
    
    @Autowired
    private AccountContractAssetService    accountContractAssetService;
    
    @Autowired
    private AccountWealthAssetService      accountWealthAssetService;
    
    @Autowired
    private AccountFundCurrentService      accountFundCurrentService;
    
    @Autowired
    private AccountWithdrawRecordService   accountWithdrawRecordService;
    
    @Autowired
    private AccountInvitationService       accountInvitationService;
    
    @Autowired
    private EnableService                  enableService;
    
    @Autowired
    private AccountFundAdjustService       accountFundAdjustService;
    
    @Autowired
    private AccountDebitAssetService       accountDebitAssetService;
    
    @Autowired
    private StockInfoService               stockInfoService;
    
    @Autowired
    private AccountAssetService            accountAssetService;
    
    @Autowired
    private AccountWealthDebitAssetService accountWealthDebitAssetService;
    
    @Autowired
    private AccountCandyRecordService      accountCandyRecordService;
    
    @Autowired
    private WalletTransferCurrentService   walletTransferCurrentService;
    
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String            keyPrefix = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();
    
    private void setAccountAssetCache(Long accountId, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        RedisUtils.del(key);
    }
    
    /**
     * 资金类统一服务接口
     */
    public FundModel fundTransaction(FundModel fundModel) throws BusinessException
    {
        logger.debug("fundTransaction 资金类统一服务接口start--------------------------------------------------");
        logger.debug("fundTransaction fundModel:" + fundModel.toString());
        // 入参统一校验判断
        if (null == fundModel.getAccountId()) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        if (null == fundModel.getStockinfoId()) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        if (null == fundModel.getAmount() || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        AccountWealthAsset accountWealthAsset = new AccountWealthAsset();
        FundModel fundModelReturn = null;
        // 根据业务类别进行不同的资金交易处理
        // 余额数量平台调增
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.assetAdjustAdd(fundModel, accountWalletAsset);
        } // 余额数量平台调减
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.assetAdjustSub(fundModel, accountWalletAsset);
        } // 冻结数量平台调增
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.forzenAssetAdjustAdd(fundModel, accountWalletAsset);
        } // 冻结数量平台调减
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.forzenAssetAdjustSub(fundModel, accountWalletAsset);
        } // 解冻
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.assetUnFrozen(fundModel, accountWalletAsset);
        } // 钱包账户充值
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.walletAssetRecharge(fundModel, accountWalletAsset);
        } // 钱包账户提现
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW.equals(fundModel.getBusinessFlag()))
        {
            checkWalletAsset(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID, fundModel.getStockinfoId());
            if (null == fundModel.getFee()) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID, fundModel.getStockinfoId());
            if (null == accountWalletAsset) { throw new BusinessException("super admin asset doesn't exist"); }
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            fundModelReturn = this.walletAssetWithdraw(fundModel, accountWalletAsset);
        } // 钱包账户提现取消
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID, fundModel.getStockinfoId());
            if (null == accountWalletAsset) { throw new BusinessException("super admin asset doesn't exist"); }
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.walletAssetWithdrawCancel(fundModel, accountWalletAsset);
        } // 钱包账户提现拒绝（包括审核拒绝和复核拒绝）
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID, fundModel.getStockinfoId());
            if (null == accountWalletAsset) { throw new BusinessException("super admin asset doesn't exist"); }
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.walletAssetWithdrawReject(fundModel, accountWalletAsset);
        } // 钱包账户转合约账户
        else if (fundModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT))
        {
            // 判断合约账户 不存在 则自动开一个账户
            checkContractAsset(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            checkContractAsset(fundModel.getAccountId(), fundModel.getStockinfoIdEx(), fundModel.getStockinfoIdEx());
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.wallet2Contract(fundModel, accountWalletAsset);
        } // 合约账户转钱包账户
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.contract2Wallet(fundModel, accountWalletAsset);
        } // 撮合交易自动借款 || 交割平仓法定货币自动借款 || 交割平仓数字货币自动借款
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_MONEY.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_AUTO_BORROW_VCOIN.equals(fundModel.getBusinessFlag()))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
                this.assetDebitBorrow(fundModel, accountContractAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
                this.assetDebitBorrow(fundModel, accountSpotAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 撮合交易自动还款
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT.equals(fundModel.getBusinessFlag()))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
                this.assetDebitRepayment(fundModel, accountContractAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(),
                        getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
                this.assetDebitRepayment(fundModel, accountSpotAsset);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 撮合交易现货卖出委托
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("已经进入撮合交易委托卖出 stockinfoId = " + fundModel.getStockinfoId() + " , stockinfoidEx =" + fundModel.getStockinfoIdEx());
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                boolean isVCoin = stockInfo.getTradeStockinfoId().longValue() == fundModel.getStockinfoId().longValue();
                logger.debug("资金处理是否数字货币标的：" + isVCoin);
                logger.debug("进入撮合交易委托卖出合约资产处理");
                accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), (isVCoin ? fundModel.getStockinfoId() : fundModel.getStockinfoIdEx()),
                        fundModel.getStockinfoIdEx());
                logger.debug("USDX合约资产查询" + accountContractAsset.toString());
                // 现货卖出委托资产处理
                this.spotSellBtcEntrustVCoinMoney(fundModel, accountContractAsset, isVCoin);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                logger.debug("进入撮合交易委托卖出钱包资产处理");
                accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), stockInfo.getTradeStockinfoId(), fundModel.getStockinfoIdEx());
                fundModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                // 现货卖出委托资产处理
                this.spotSellBtcEntrustVCoinMoney(fundModel, accountSpotAsset, true);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("进入撮合交易委托卖出钱包资产处理");
                accountWalletAsset = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), stockInfo.getTradeStockinfoId());
                fundModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                // 现货卖出委托资产处理
                this.spotSellBtcEntrustVCoinMoney(fundModel, accountWalletAsset, true);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 撮合交易现货买入委托
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("已经进入撮合交易委托买入 stockinfoId = " + fundModel.getStockinfoId() + " , stockinfoidEx =" + fundModel.getStockinfoIdEx());
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                boolean isVCoin = stockInfo.getTradeStockinfoId().longValue() == fundModel.getStockinfoId().longValue();
                logger.debug("资金处理是否数字货币标的：" + isVCoin);
                logger.debug("进入撮合交易委托买入合约资产处理");
                accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), (isVCoin ? fundModel.getStockinfoIdEx() : fundModel.getStockinfoId()),
                        fundModel.getStockinfoIdEx());
                // 现货买入委托资产处理
                this.spotBuyBtcEntrustVCoinMoney(fundModel, accountContractAsset, isVCoin);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), stockInfo.getCapitalStockinfoId(), fundModel.getStockinfoIdEx());
                fundModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
                // 现货买入委托资产处理
                this.spotBuyBtcEntrustVCoinMoney(fundModel, accountSpotAsset, false);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("进入撮合交易委托买入钱包资产处理");
                accountWalletAsset = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), stockInfo.getCapitalStockinfoId());
                fundModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
                // 现货买入委托资产处理
                this.spotBuyBtcEntrustVCoinMoney(fundModel, accountWalletAsset, false);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 撮合交易现货卖出委托撤单
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST_WITHDRAW.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("已经进入撮合交易现货卖出委托撤单 stockinfoId = " + fundModel.getStockinfoId() + " , stockinfoidEx =" + fundModel.getStockinfoIdEx());
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                boolean isVCoin = stockInfo.getTradeStockinfoId().longValue() == fundModel.getStockinfoId().longValue();
                logger.debug("结果：" + isVCoin);
                logger.debug("已经进入撮合交易现货卖出委托撤单合约资产处理");
                accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), (isVCoin ? fundModel.getStockinfoId() : fundModel.getStockinfoIdEx()),
                        fundModel.getStockinfoIdEx());
                // 现货卖出委托撤单或拒绝资产处理
                this.spotSellBTCEntrustVCoinMoneyWithdraw(fundModel, accountContractAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), stockInfo.getTradeStockinfoId(), fundModel.getStockinfoIdEx());
                fundModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                // 现货卖出委托撤单或拒绝资产处理
                this.spotSellBTCEntrustVCoinMoneyWithdraw(fundModel, accountSpotAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("已经进入撮合交易现货卖出委托撤单钱包资产处理");
                accountWalletAsset = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), stockInfo.getTradeStockinfoId());
                fundModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                // 现货卖出委托撤单或拒绝资产处理
                this.spotSellBTCEntrustVCoinMoneyWithdraw(fundModel, accountWalletAsset);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 撮合交易现货买入委托撤单
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST_WITHDRAW.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("已经进入撮合交易现货买入委托撤单 stockinfoId = " + fundModel.getStockinfoId() + " , stockinfoidEx =" + fundModel.getStockinfoIdEx());
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                boolean isVCoin = stockInfo.getTradeStockinfoId().longValue() == fundModel.getStockinfoIdEx().longValue();
                logger.debug("已经进入撮合交易现货买入委托撤单合约资产处理");
                // 这里的stockinfoId 为法定货币
                accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), (isVCoin ? fundModel.getStockinfoId() : fundModel.getStockinfoIdEx()),
                        fundModel.getStockinfoIdEx());
                // 现货买入 委托撤单或拒绝资产处理
                this.spotBuyBTCEntrustVCoinMoneyWithdraw(fundModel, accountContractAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), stockInfo.getCapitalStockinfoId(), fundModel.getStockinfoIdEx());
                fundModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
                // 现货买入 委托撤单或拒绝资产处理
                this.spotBuyBTCEntrustVCoinMoneyWithdraw(fundModel, accountSpotAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("已经进入撮合交易现货买入委托撤单钱包资产处理");
                accountWalletAsset = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), stockInfo.getCapitalStockinfoId());
                fundModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
                // 现货买入 委托撤单或拒绝资产处理
                this.spotBuyBTCEntrustVCoinMoneyWithdraw(fundModel, accountWalletAsset);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 强制平仓内部转移 || 强制平仓数字货币内部转移
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER.equals(fundModel.getBusinessFlag()))
        {
            // 现货卖出委托成交资产处理
            this.spotInnerSellNoEntrustDeal(fundModel);
        } // 超级账户数字合约资产清零 || 超级账户法定合约资产清零
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_VCOIN_ASSET_TO_ZREO.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_MONEY_ASSET_TO_ZREO.equals(fundModel.getBusinessFlag()))
        {
            accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            this.superAccountContractAssetToZero(fundModel, accountContractAsset);
        }// 超级账户数字合约借款清零 || 超级账户法定合约借款清零
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_VCOIN_DEBIT_TO_ZREO.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_SUPERACCOUNT_MONEY_DEBIT_TO_ZREO.equals(fundModel.getBusinessFlag()))
        {
            this.superAccountContractDebitToZero(fundModel);
        } // 交割平仓准备金转移减少法定货币(转移给准备金账户) || 交割平仓准备金转移减少数字货币(转移给准备金账户) || 交割平仓准备金抵扣分摊减少数字货币(转移给超级账户)
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_MOVE_DECREASE.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            this.reserveFundDecrease(fundModel);
        } // 借贷自动计息
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_INTEREST.equals(fundModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_AUTO_WEALTH_INTEREST.equals(fundModel.getBusinessFlag()))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
                doInterestRate(fundModel, accountContractAsset);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
                doInterestRate(fundModel, accountSpotAsset);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }// 现货账户转理财账户
        else if (fundModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH))
        {
            // 判断合约账户 不存在 则自动开一个账户
            logger.debug("现货账户转理财账户fundModel:" + fundModel.toString());
            checkWealthAsset(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            checkWealthAsset(fundModel.getAccountId(), fundModel.getStockinfoIdEx(), fundModel.getStockinfoIdEx());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH);
            accountSpotAsset = this.findAccountSpotAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(),
                    getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getCapitalStockinfoId());
            accountWealthAsset = this.findAccountWealthAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoId());
            this.spot2Wealth(fundModel, accountSpotAsset, accountWealthAsset);
        } // 理财账户转现货账户
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT.equals(fundModel.getBusinessFlag()))
        {
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT);
            accountWealthAsset = this.findAccountWealthAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            accountSpotAsset = this.findAccountSpotAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(),
                    getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getCapitalStockinfoId());
            this.wealth2Spot(fundModel, accountSpotAsset, accountWealthAsset);
        } // 钱包账户转杠杆现货账户
        else if (fundModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT))
        {
            // 判断合约账户 不存在 则自动开一个账户
            logger.debug("钱包账户转杠杆现货账户fundModel:" + fundModel.toString());
            checkSpotAsset(fundModel.getAccountId(), fundModel.getStockinfoId(), getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
            checkSpotAsset(fundModel.getAccountId(), getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId(),
                    getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            accountSpotAsset = this.findAccountSpotAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(),
                    getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
            this.wallet2Spot(fundModel, accountWalletAsset, accountSpotAsset);
        } // 杠杆现货账户转钱包账户
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            accountSpotAsset = this.findAccountSpotAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(),
                    getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
            this.spot2Wallet(fundModel, accountWalletAsset, accountSpotAsset);
        }
        // ERC20token以及ETH等小额手续费
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE_SDF.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            this.assetERC20Recharge(fundModel, accountWalletAsset);
        }// 交易奖励
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD.equals(fundModel.getBusinessFlag()))
        {
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            AccountWalletAsset adminWalletAsset = this.findAccountWalletAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId());
            this.tradeAward(fundModel, accountWalletAsset, adminWalletAsset);
        }
        // 持仓调节费
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_POSITION_PREMINM_FEE.equals(fundModel.getBusinessFlag()))
        {
            accountSpotAsset = this.findAccountSpotAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            AccountSpotAsset superSpotAsset = this.findAccountSpotAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_POSITION_PREMINM_FEE_ID,
                    fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            AccountDebitAsset accountDebitAsset = checkAddGetAccountDebitAsset(fundModel);
            accountDebitAssetService.selectByPrimaryKeyForUpdate(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableDebitAsset(),accountDebitAsset.getId());
            this.positionPreminmFee(fundModel, accountSpotAsset, superSpotAsset);
        }

        // 激活TOKEN交易手续费
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_ACTIVE_TOKEN_TRADE_FEE.equals(fundModel.getBusinessFlag()))
        {
            checkWalletAsset(fundModel.getAccountId(), fundModel.getStockinfoId());
            checkWalletAsset(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId());
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            AccountWalletAsset adminWalletAsset = this.findAccountWalletAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId());
            this.activeTokenTradeFee(fundModel, accountWalletAsset, adminWalletAsset);
        }

        // 激活TOKEN交易奖励
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_ACTIVE_TOKEN_TRADE_AWARD.equals(fundModel.getBusinessFlag()))
        {
            checkWalletAsset(fundModel.getAccountId(), fundModel.getStockinfoId());
            checkWalletAsset(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId());
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            AccountWalletAsset adminWalletAsset = this.findAccountWalletAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId());
            this.activeTokenTradeAward(fundModel, adminWalletAsset, accountWalletAsset);
        }
        //  TRADEX 充值奖励
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_RECHARGE_AWARD.equals(fundModel.getBusinessFlag()))
        {
            checkWalletAsset(fundModel.getAccountId(), fundModel.getStockinfoId());
            checkWalletAsset(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId());
            accountWalletAsset = this.findAccountWalletAssetFormDBForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId());
            AccountWalletAsset adminWalletAsset = this.findAccountWalletAssetFormDBForUpdate(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId());
            this.rechargeAward(fundModel, adminWalletAsset, accountWalletAsset);
        }
        logger.debug("fundTransaction 资金类统一服务接口end--------------------------------------------------");
        return fundModelReturn;
    }

    /**
     * 激活TOKEN交易奖励
     * @param fundModel
     * @param adminWalletAsset
     * @throws BusinessException
     * @author zhangchunxi  2018-04-08 15:34:25
     */
    private void activeTokenTradeAward(FundModel fundModel, AccountWalletAsset adminWalletAsset, AccountWalletAsset userWalletAsset) throws BusinessException
    {
        if (null == userWalletAsset) { throw new BusinessException("user's wallet asse not exist!"); }
        if (null == adminWalletAsset) { throw new BusinessException("admin's wallet asse not exist!"); }
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        orgAmt = adminWalletAsset.getAmount();
        forzenOrgAmt = adminWalletAsset.getFrozenAmt();
        // 校验金额
        this.validateAmountIsSmallZero(adminWalletAsset.getAmount().subtract(fundModel.getAmount()));
        adminWalletAsset.setAmount(adminWalletAsset.getAmount().subtract(fundModel.getAmount()));
        logger.debug("交易奖励 超级账户 钱包账户资产 准备修改数据为:" + adminWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(adminWalletAsset);

        // step6 增加账户资金流水 增加资产流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(adminWalletAsset.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(adminWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setOrgAmt(orgAmt);
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setLastAmt(orgAmt.subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountFundCurrent.setRemark("激活交易奖励:" + fundModel.getAmount()+"给用户："+fundModel.getAccountId());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt);
        accountFundCurrent.setOriginalBusinessId(adminWalletAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("激活交易 超级准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 普通用户资产处理
        BigDecimal adminOrgAmt = userWalletAsset.getAmount();
        userWalletAsset.setAmount(userWalletAsset.getAmount().add(fundModel.getAmount()));
        accountWalletAssetService.updateByPrimaryKey(userWalletAsset);
        // 普通账户资产处理
        AccountFundCurrent adminFundCurrent = new AccountFundCurrent();
        adminFundCurrent.setAccountId(userWalletAsset.getAccountId());
        adminFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        adminFundCurrent.setAccountAssetId(userWalletAsset.getId());// 资产对应的id
        adminFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        adminFundCurrent.setBusinessFlag(fundModel.getBusinessFlag());
        adminFundCurrent.setStockinfoId(userWalletAsset.getStockinfoId());
        adminFundCurrent.setContractAmt(BigDecimal.ZERO);
        adminFundCurrent.setOrgAmt(adminOrgAmt);
        adminFundCurrent.setOccurAmt(fundModel.getAmount());
        adminFundCurrent.setLastAmt(adminOrgAmt.add(fundModel.getAmount()));
        adminFundCurrent.setForzenOrgAmt(userWalletAsset.getFrozenAmt());
        adminFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        adminFundCurrent.setForzenLastAmt(userWalletAsset.getFrozenAmt());
        adminFundCurrent.setFee(BigDecimal.ZERO);
        adminFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        adminFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        adminFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        adminFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        adminFundCurrent.setRemark("激活交易新增奖励:" + fundModel.getAmount());
        adminFundCurrent.setOriginalBusinessId(userWalletAsset.getId());
        adminFundCurrent.setRelatedStockinfoId(userWalletAsset.getStockinfoId());
        adminFundCurrent.setTableName(getStockInfo(adminFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("激活交易 用户准备插入数据为:" + adminFundCurrent.toString());
        accountFundCurrentService.insert(adminFundCurrent);
    }

    /**
     * 充值奖励
     * @param fundModel
     * @param adminWalletAsset
     * @throws BusinessException
     * @author zhangchunxi  2018-04-25 11:17:14
     */
    private void rechargeAward(FundModel fundModel, AccountWalletAsset adminWalletAsset, AccountWalletAsset userWalletAsset) throws BusinessException
    {
        if (null == userWalletAsset) { throw new BusinessException("user's wallet asse not exist!"); }
        if (null == adminWalletAsset) { throw new BusinessException("admin's wallet asse not exist!"); }
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        orgAmt = adminWalletAsset.getAmount();
        forzenOrgAmt = adminWalletAsset.getFrozenAmt();
        // 校验金额
        this.validateAmountIsSmallZero(adminWalletAsset.getAmount().subtract(fundModel.getAmount()));
        adminWalletAsset.setAmount(adminWalletAsset.getAmount().subtract(fundModel.getAmount()));
        logger.debug("充值奖励 超级账户 钱包账户资产 准备修改数据为:" + adminWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(adminWalletAsset);

        // step6 增加账户资金流水 增加资产流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(adminWalletAsset.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(adminWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setOrgAmt(orgAmt);
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setLastAmt(orgAmt.subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountFundCurrent.setRemark("充值奖励:" + fundModel.getAmount()+"给用户："+fundModel.getAccountId());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt);
        accountFundCurrent.setOriginalBusinessId(adminWalletAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("充值奖励 超级准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 普通用户资产处理
        BigDecimal adminOrgAmt = userWalletAsset.getAmount();
        userWalletAsset.setAmount(userWalletAsset.getAmount().add(fundModel.getAmount()));
        accountWalletAssetService.updateByPrimaryKey(userWalletAsset);
        // 普通账户资产处理
        AccountFundCurrent adminFundCurrent = new AccountFundCurrent();
        adminFundCurrent.setAccountId(userWalletAsset.getAccountId());
        adminFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        adminFundCurrent.setAccountAssetId(userWalletAsset.getId());// 资产对应的id
        adminFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        adminFundCurrent.setBusinessFlag(fundModel.getBusinessFlag());
        adminFundCurrent.setStockinfoId(userWalletAsset.getStockinfoId());
        adminFundCurrent.setContractAmt(BigDecimal.ZERO);
        adminFundCurrent.setOrgAmt(adminOrgAmt);
        adminFundCurrent.setOccurAmt(fundModel.getAmount());
        adminFundCurrent.setLastAmt(adminOrgAmt.add(fundModel.getAmount()));
        adminFundCurrent.setForzenOrgAmt(userWalletAsset.getFrozenAmt());
        adminFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        adminFundCurrent.setForzenLastAmt(userWalletAsset.getFrozenAmt());
        adminFundCurrent.setFee(BigDecimal.ZERO);
        adminFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        adminFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        adminFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        adminFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        adminFundCurrent.setRemark("充值新增奖励:" + fundModel.getAmount());
        adminFundCurrent.setOriginalBusinessId(userWalletAsset.getId());
        adminFundCurrent.setRelatedStockinfoId(userWalletAsset.getStockinfoId());
        adminFundCurrent.setTableName(getStockInfo(adminFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("充值奖励 用户准备插入数据为:" + adminFundCurrent.toString());
        accountFundCurrentService.insert(adminFundCurrent);
    }

    /**
     * 激活TOKEN交易手续费
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author zhangchunxi  2018-04-08 15:34:25
     */
    private void activeTokenTradeFee(FundModel fundModel, AccountWalletAsset accountWalletAsset, AccountWalletAsset adminWalletAsset) throws BusinessException
    {
        if (null == adminWalletAsset) { throw new BusinessException("admin's wallet asse not exist!"); }
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        orgAmt = accountWalletAsset.getAmount();
        forzenOrgAmt = accountWalletAsset.getFrozenAmt();
        // 校验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        logger.debug("交易奖励 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);

        // step6 增加账户资金流水 增加资产流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setOrgAmt(orgAmt);
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setLastAmt(orgAmt.subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountFundCurrent.setRemark("激活交易扣除手续费:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt);
        accountFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("激活交易 普通用户准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 超级用户资产处理
        BigDecimal adminOrgAmt = adminWalletAsset.getAmount();
        adminWalletAsset.setAmount(adminWalletAsset.getAmount().add(fundModel.getAmount()));
        accountWalletAssetService.updateByPrimaryKey(adminWalletAsset);
        // 超级账户资产处理
        AccountFundCurrent adminFundCurrent = new AccountFundCurrent();
        adminFundCurrent.setAccountId(adminWalletAsset.getAccountId());
        adminFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        adminFundCurrent.setAccountAssetId(adminWalletAsset.getId());// 资产对应的id
        adminFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        adminFundCurrent.setBusinessFlag(fundModel.getBusinessFlag());
        adminFundCurrent.setStockinfoId(adminWalletAsset.getStockinfoId());
        adminFundCurrent.setContractAmt(BigDecimal.ZERO);
        adminFundCurrent.setOrgAmt(adminOrgAmt);
        adminFundCurrent.setOccurAmt(fundModel.getAmount());
        adminFundCurrent.setLastAmt(adminOrgAmt.add(fundModel.getAmount()));
        adminFundCurrent.setForzenOrgAmt(adminWalletAsset.getFrozenAmt());
        adminFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        adminFundCurrent.setForzenLastAmt(adminWalletAsset.getFrozenAmt());
        adminFundCurrent.setFee(BigDecimal.ZERO);
        adminFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        adminFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        adminFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        adminFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        adminFundCurrent.setRemark("激活交易新增手续费:" + fundModel.getAmount()+"账户："+accountWalletAsset.getAccountId());
        adminFundCurrent.setOriginalBusinessId(adminWalletAsset.getId());
        adminFundCurrent.setRelatedStockinfoId(adminWalletAsset.getStockinfoId());
        adminFundCurrent.setTableName(getStockInfo(adminFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("激活交易 超级用户准备插入数据为:" + adminFundCurrent.toString());
        accountFundCurrentService.insert(adminFundCurrent);
    }
    
    public void positionPreminmFee(FundModel fundModel, AccountSpotAsset userSpotAsset, AccountSpotAsset adminSpotAsset)
    {
        // 普通用户资产增加 超级用户资产减少
        if (fundModel.getDirection() == 1)
        {
            // 普通用户 增加
            BigDecimal orgAmt = BigDecimal.ZERO;
            BigDecimal lastAmt = BigDecimal.ZERO;
            // step1 修改钱包账户资产当前金额
            orgAmt = userSpotAsset.getAmount();
            lastAmt = orgAmt.add(fundModel.getAmount());
            userSpotAsset.setAmount(lastAmt);
            logger.debug("准备修改数据为:" + userSpotAsset.toString());
            accountSpotAssetService.updateByPrimaryKey(userSpotAsset);
            // 普通用户负债流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(userSpotAsset.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET); //
            fundCurrentModel.setAccountAssetId(userSpotAsset.getId());
            fundCurrentModel.setStockinfoId(userSpotAsset.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(lastAmt);
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
            accountFundCurrent.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
            accountFundCurrent.setRemark("持仓调节费:" + fundModel.getAmount());
            accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
            accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableFundCurrent());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(userSpotAsset.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(userSpotAsset.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(userSpotAsset.getId());
            logger.debug("持仓调节费 负债流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);

            // 超级用户新增一笔流水
            // step1 修改钱包账户资产当前金额
            orgAmt = adminSpotAsset.getAmount();
            lastAmt = orgAmt.subtract(fundModel.getAmount());
            adminSpotAsset.setAmount(lastAmt);
            logger.debug("准备修改数据为:" + adminSpotAsset.toString());
            accountSpotAssetService.updateByPrimaryKey(adminSpotAsset);
            // 校验金额
            this.validateAmountIsSmallZero(lastAmt);
            // step2 增加账户资金流水
            fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(adminSpotAsset.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
            fundCurrentModel.setAccountAssetId(adminSpotAsset.getId());
            fundCurrentModel.setStockinfoId(adminSpotAsset.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(lastAmt);
            accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
            accountFundCurrent.setRemark("持仓调节费调减金额:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(adminSpotAsset.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(adminSpotAsset.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(adminSpotAsset.getId());
            accountFundCurrent.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
            accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableFundCurrent());
            logger.debug("持仓调节费调减  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
        }
        // 普通用户负债增加资产不处理 超级用户资产增加
        else if (fundModel.getDirection() == -1)
        {
            // 普通用户负债增加
            BigDecimal orgDebit = BigDecimal.ZERO;
            AccountDebitAsset accountDebitAsset = checkAddGetAccountDebitAsset(fundModel);
            orgDebit = accountDebitAsset.getDebitAmt();
            accountDebitAsset.setDebitAmt(accountDebitAsset.getDebitAmt().add(fundModel.getAmount()));
            accountDebitAsset.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableDebitAsset());
            accountDebitAssetService.updateByPrimaryKey(accountDebitAsset);
            // 普通用户负债流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(userSpotAsset.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT);
            fundCurrentModel.setAccountAssetId(accountDebitAsset.getId());
            fundCurrentModel.setStockinfoId(userSpotAsset.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgDebit);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(orgDebit.add(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
            accountFundCurrent.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
            accountFundCurrent.setRemark("持仓调节费:" + fundModel.getAmount());
            accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT);
            accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableFundCurrent());
            accountFundCurrent.setOriginalBusinessId(accountDebitAsset.getId());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
            logger.debug("持仓调节费 负债流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);

            // 超级用户新增一笔流水
            BigDecimal orgAmt = BigDecimal.ZERO;
            BigDecimal lastAmt = BigDecimal.ZERO;
            // step1 修改钱包账户资产当前金额
            orgAmt = adminSpotAsset.getAmount();
            lastAmt = orgAmt.add(fundModel.getAmount());
            adminSpotAsset.setAmount(lastAmt);
            logger.debug("准备修改数据为:" + adminSpotAsset.toString());
            accountSpotAssetService.updateByPrimaryKey(adminSpotAsset);
            // 校验金额
            this.validateAmountIsSmallZero(lastAmt);
            // step2 增加账户资金流水
            fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(adminSpotAsset.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
            fundCurrentModel.setAccountAssetId(adminSpotAsset.getId());
            fundCurrentModel.setStockinfoId(adminSpotAsset.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(lastAmt);
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
            accountFundCurrent.setRemark("持仓调节费调增金额:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(adminSpotAsset.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(adminSpotAsset.getFrozenAmt());
            accountFundCurrent.setRelatedStockinfoId(FundConsts.WALLET_BTC2USD_TYPE);
            accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableFundCurrent());
            accountFundCurrent.setOriginalBusinessId(adminSpotAsset.getId());
            logger.debug("持仓调节费调增  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
        }
        else
        {
            throw new BusinessException("错误的方向(-1,1):" + fundModel.getDirection());
        }
    }
    
    /**
     * 检查并查询负债
     * @param fundModel
     * @return
     */
    public AccountDebitAsset checkAddGetAccountDebitAsset(FundModel fundModel)
    {
        AccountDebitAsset record = new AccountDebitAsset();
        record.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableDebitAsset());
        record.setStockinfoId(fundModel.getStockinfoId());
        record.setRelatedStockinfoId(getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
        record.setBorrowerAccountId(fundModel.getAccountId());
        List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
        if (list.size() > 0)
        {
            record = list.get(0);
        }
        else
        {
            Long id = SerialnoUtils.buildPrimaryKey();
            AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
            accountDebitAsset.setId(id);
            accountDebitAsset.setBorrowerAccountId(fundModel.getAccountId());
            accountDebitAsset.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            accountDebitAsset.setStockinfoId(fundModel.getStockinfoId());
            accountDebitAsset.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
            accountDebitAsset.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableDebitAsset());
            accountDebitAsset.setDebitAmt(BigDecimal.ZERO);
            accountDebitAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountDebitAsset.setAccumulateInterest(BigDecimal.ZERO);
            accountDebitAsset.setLastInterestDay(0L);
            accountDebitAssetService.insert(accountDebitAsset);
            accountDebitAsset.setId(id);
            record = accountDebitAsset;
        }
        return record;
    }
    
    /**
     * 杠杆现货帐户 转 钱包帐户
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zhangchunxi 2018-01-03
     */
    private void spot2Wallet(FundModel fundModel, AccountWalletAsset accountWalletAsset, AccountSpotAsset accountSpotAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel.setRelatedStockinfoId(getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 杠杆现货账户资产处理
        Long transId = SerialnoUtils.buildPrimaryKey();
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        orgAmt = accountSpotAsset.getAmount();
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        if (fundModel.getAmount().compareTo(
                accountSpotAsset.getAmount().subtract(accountSpotAsset.getFrozenAmt())) > 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        // 修改杠杆现货帐户资产当前金额
        accountSpotAsset.setAmount(orgAmt.subtract(fundModel.getAmount()));
        logger.debug("杠杆现货帐户转钱包帐户 杠杆现货账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
        // 插入账户资金流水
        accountFundCurrent.setRelatedStockinfoId(accountSpotAsset.getRelatedStockinfoId());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount());
        accountFundCurrent.setRemark("杠杆现货帐户转钱包帐户转出：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setTransId(transId.toString());
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountSpotAsset.getId());
        accountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableFundCurrent());
        logger.debug("杠杆现货帐户转钱包帐户 杠杆现货账户资产 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 钱包账户资产处理
        // 修改钱包账户资产当前金额
        BigDecimal orgAmtEx = accountWalletAsset.getAmount();
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWalletAsset.setAmount(orgAmtEx.add(fundModel.getAmount()));
        logger.debug("杠杆现货帐户转钱包帐户 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmtEx);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountContractFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountContractFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountContractFundCurrent.setLastAmt(orgAmtEx.add(fundModel.getAmount()));
        accountContractFundCurrent.setRemark("杠杆现货帐户转钱包帐户转入：" + fundModel.getAmount());
        accountContractFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setFee(BigDecimal.ZERO);
        accountContractFundCurrent.setTransId(transId.toString());
        accountContractFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountContractFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountContractFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
        accountContractFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountContractFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
        logger.debug("杠杆现货帐户转钱包帐户 钱包账户资产 账户资金交易流水 准备插入数据为:" + accountContractFundCurrent.toString());
        accountFundCurrentService.insert(accountContractFundCurrent);
    }
    
    /**
     * 钱包转户转杠杆现货账户
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zhangchunxi  2018-01-03
     */
    private void wallet2Spot(FundModel fundModel, AccountWalletAsset accountWalletAsset, AccountSpotAsset accountSpotAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            logger.debug("正常可用不足");
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 钱包资产处理
        orgAmt = accountWalletAsset.getAmount();
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWalletAsset.setAmount(orgAmt.subtract(fundModel.getAmount()));
        logger.debug("钱包账户转杠杆现货  钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        Long transId = SerialnoUtils.buildPrimaryKey();
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountContractFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountContractFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountContractFundCurrent.setLastAmt(orgAmt.subtract(fundModel.getAmount()));
        accountContractFundCurrent.setRemark("钱包帐户转杠杆现货转户转出：" + fundModel.getAmount());
        accountContractFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setFee(BigDecimal.ZERO);
        accountContractFundCurrent.setTransId(transId.toString());
        accountContractFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountContractFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountContractFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
        accountContractFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountContractFundCurrent.setTableName(getStockInfo(accountContractFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("钱包帐户转杠杆现货 钱包账户账户资产 账户资金交易流水 准备插入数据为:" + accountContractFundCurrent.toString());
        accountFundCurrentService.insert(accountContractFundCurrent);
        // step2 杠杆现货资产处理
        BigDecimal orgAmtEx = BigDecimal.ZERO;
        orgAmtEx = accountSpotAsset.getAmount();
        // 修改杠杆现货账户资产当前金额
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountSpotAsset.setAmount(orgAmtEx.add(fundModel.getAmount()));
        logger.debug("钱包帐户转杠杆现货 杠杆现货账户资产 准备修改数据为:" + accountSpotAsset.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmtEx);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setLastAmt(orgAmtEx.add(fundModel.getAmount()));
        accountFundCurrent.setRemark("钱包帐户转杠杆现货转入：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setTransId(transId.toString());
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountSpotAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(accountSpotAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableFundCurrent());
        logger.debug("钱包帐户转杠杆现货 杠杆现货账户资产 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 理财帐户转钱包转户
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zhangchunxi  2018-01-03
     */
    private void wealth2Spot(FundModel fundModel, AccountSpotAsset accountSpotAsset, AccountWealthAsset accountWealthAsset) throws BusinessException
    {
        String tableName = "";
        StockInfo info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        info.setCapitalStockinfoId(fundModel.getStockinfoIdEx());
        List<StockInfo> list = stockInfoService.findList(info);
        if (list.size() > 0)
        {
            tableName = list.get(0).getTableFundCurrent();
        }
        else
        {
            throw new BusinessException("tableFundCurrent not exist!");
        }
        BigDecimal orgAmt = BigDecimal.ZERO;
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            logger.debug("正常可用不足");
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 理财账户资产处理
        // 修改理财账户资产当前金额
        orgAmt = accountWealthAsset.getWealthAmt();
        accountWealthAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWealthAsset.setWealthAmt(orgAmt.subtract(fundModel.getAmount()));
        logger.debug("理财帐户转钱包帐户 理财账户资产 准备修改数据为:" + accountWealthAsset.toString());
        accountWealthAssetService.updateByPrimaryKey(accountWealthAsset);
        Long transId = SerialnoUtils.buildPrimaryKey();
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WEALTH_ASSET);
        fundCurrentModel.setAccountAssetId(accountWealthAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountContractFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountContractFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountContractFundCurrent.setLastAmt(orgAmt.subtract(fundModel.getAmount()));
        accountContractFundCurrent.setRemark("理财帐户转钱包转户转出：" + fundModel.getAmount());
        accountContractFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setFee(BigDecimal.ZERO);
        accountContractFundCurrent.setTransId(transId.toString());
        accountContractFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setOriginalBusinessId(accountWealthAsset.getId());
        accountContractFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractFundCurrent.setTableName(getStockInfo(accountContractFundCurrent.getRelatedStockinfoId()).getTableWealthCurrent());
        logger.debug("理财帐户转钱包帐户 理财账户资产 账户资金交易流水 准备插入数据为:" + accountContractFundCurrent.toString());
        accountFundCurrentService.insert(accountContractFundCurrent);
        // step2 钱包账户资产处理
        BigDecimal orgAmtEx = BigDecimal.ZERO;
        orgAmtEx = accountSpotAsset.getAmount();
        // 修改钱包账户资产当前金额
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountSpotAsset.setAmount(orgAmtEx.add(fundModel.getAmount()));
        logger.debug("理财帐户转钱包帐户 钱包账户资产 准备修改数据为:" + accountSpotAsset.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmtEx);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setLastAmt(orgAmtEx.add(fundModel.getAmount()));
        accountFundCurrent.setRemark("理财帐户转钱包转户转入：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setTransId(transId.toString());
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountSpotAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(tableName);
        logger.debug("理财帐户转钱包帐户 钱包账户资产 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 钱包帐户 转 理财帐户
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zhangchunxi 2018-01-03
     */
    private void spot2Wealth(FundModel fundModel, AccountSpotAsset accountSpotAsset, AccountWealthAsset accountWealthAsset) throws BusinessException
    {
        String tableName = "";
        StockInfo info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        info.setCapitalStockinfoId(fundModel.getStockinfoIdEx());
        List<StockInfo> list = stockInfoService.findList(info);
        if (list.size() > 0)
        {
            tableName = list.get(0).getTableFundCurrent();
        }
        else
        {
            throw new BusinessException("tableFundCurrent not exist!");
        }
        BigDecimal orgAmt = BigDecimal.ZERO;
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 钱包账户资产处理
        Long transId = SerialnoUtils.buildPrimaryKey();
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        orgAmt = accountSpotAsset.getAmount();
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        if (fundModel.getAmount().compareTo(
                accountSpotAsset.getAmount().subtract(accountSpotAsset.getFrozenAmt())) > 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        // 修改钱包账户资产当前金额
        accountSpotAsset.setAmount(orgAmt.subtract(fundModel.getAmount()));
        logger.debug("钱包帐户转理财帐户 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
        // 插入账户资金流水
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount());
        accountFundCurrent.setRemark("钱包帐户转理财帐户转出：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setTransId(transId.toString());
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountSpotAsset.getId());
        accountFundCurrent.setTableName(tableName);
        logger.debug("钱包帐户转理财帐户 钱包账户资产 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 理财账户资产处理
        // 修改理财账户资产当前金额
        BigDecimal orgAmtEx = accountWealthAsset.getWealthAmt();
        accountWealthAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWealthAsset.setWealthAmt(orgAmtEx.add(fundModel.getAmount()));
        logger.debug("钱包帐户转合约帐户 合约账户资产 准备修改数据为:" + accountWealthAsset.toString());
        accountWealthAssetService.updateByPrimaryKey(accountWealthAsset);
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WEALTH_ASSET);
        fundCurrentModel.setAccountAssetId(accountWealthAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmtEx);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountContractFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountContractFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountContractFundCurrent.setLastAmt(orgAmtEx.add(fundModel.getAmount()));
        accountContractFundCurrent.setRemark("钱包帐户转理财帐户转入：" + fundModel.getAmount());
        accountContractFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setFee(BigDecimal.ZERO);
        accountContractFundCurrent.setTransId(transId.toString());
        accountContractFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setOriginalBusinessId(accountWealthAsset.getId());
        accountContractFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountContractFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableWealthCurrent());
        logger.debug("钱包帐户转理财帐户 理财账户资产 账户资金交易流水 准备插入数据为:" + accountContractFundCurrent.toString());
        accountFundCurrentService.insert(accountContractFundCurrent);
    }
    
    /**
     * 计息流水处理
     * @param fundModel
     */
    private void doInterestRate(FundModel fundModel, AccountContractAsset accountContractAsset)
    {
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(fundModel.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getFee());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(fundModel.getAmount().add(fundModel.getFee()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("自动计息 合约账户借款增加:" + fundModel.getFee());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("自动计息合约账户借款增加:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 计息流水处理
     * @param fundModel
     */
    private void doInterestRate(FundModel fundModel, AccountSpotAsset accountSpotAsset)
    {
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT); // 现货账户负债
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(fundModel.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getFee());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(fundModel.getAmount().add(fundModel.getFee()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_INTEREST.equals(fundModel.getBusinessFlag()))
        {
            accountFundCurrent.setRemark("自动计息 合约账户借款增加:" + fundModel.getFee());
            accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT);
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        }
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_AUTO_WEALTH_INTEREST.equals(fundModel.getBusinessFlag()))
        {
            accountFundCurrent.setRemark("自动计息 合约账户理财增加:" + fundModel.getFee());
            accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WEALTH_ASSET);
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableWealthCurrent());
        }
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        logger.debug("自动计息:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 检查理财账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     */
    public void checkWealthAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountWealthAsset accountWealthAsset = new AccountWealthAsset();
        accountWealthAsset.setWealthAccountId(accountId);
        accountWealthAsset.setStockinfoId(stockinfoId);
        accountWealthAsset.setRelatedStockinfoId(relatedStockinfoId);
        List<AccountWealthAsset> list = accountWealthAssetService.findList(accountWealthAsset);
        if (list.size() > 0)
        {
            accountWealthAsset = accountWealthAssetService.selectByPrimaryKey(list.get(0).getId());
        }
        else
        {
            accountWealthAsset = new AccountWealthAsset();
            accountWealthAsset.setWealthAmt(BigDecimal.ZERO);
            accountWealthAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountWealthAsset.setStockinfoId(stockinfoId);
            accountWealthAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountWealthAsset.setRemark("");
            accountWealthAsset.setIssuerAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_DEBIT_WEALTH_ID);
            accountWealthAsset.setAccumulateInterest(BigDecimal.ZERO);
            accountWealthAsset.setWealthAccountId(accountId);
            accountWealthAsset.setLastInterestDay(0L);
            accountWealthAssetService.insert(accountWealthAsset);
        }
    }
    
    /**
     * 检查杠杆现货账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     */
    public void checkSpotAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(accountId);
        accountSpotAsset.setStockinfoId(stockinfoId);
        accountSpotAsset.setRelatedStockinfoId(relatedStockinfoId);
        List<AccountSpotAsset> list = accountSpotAssetService.findList(accountSpotAsset);
        if (list.size() > 0)
        {
            accountSpotAsset = accountSpotAssetService.selectByPrimaryKey(list.get(0).getId());
        }
        else
        {
            accountSpotAsset = new AccountSpotAsset();
            accountSpotAsset.setDirection("Long");
            accountSpotAsset.setAmount(BigDecimal.ZERO);
            accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
            accountSpotAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountSpotAsset.setStockinfoId(stockinfoId);
            accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountSpotAsset.setRemark("");
            accountSpotAsset.setAccountId(accountId);
            accountSpotAssetService.insert(accountSpotAsset);
        }
    }
    
    /**
     * 检查合约账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     */
    public void checkContractAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountContractAsset accountContractAsset = this.findAccountContractAssetFormDB(accountId, stockinfoId, relatedStockinfoId);
        if (accountContractAsset == null)
        {
            accountContractAsset = new AccountContractAsset();
            accountContractAsset.setAmount(BigDecimal.ZERO);
            accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountContractAsset.setStockinfoId(stockinfoId);
            accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
            accountContractAsset.setPrice(BigDecimal.ONE);
            accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
            accountContractAsset.setRemark("");
            accountContractAsset.setAccountId(accountId);
            accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
            accountContractAssetService.insert(accountContractAsset);
        }
    }
    
    /**
     * 检查钱包账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     */
    public void checkWalletAsset(Long accountId, Long stockinfoId)
    {
        AccountWalletAsset accountWalletAsset = this.findAccountWalletAssetFormDB(accountId, stockinfoId);
        if (accountWalletAsset == null)
        {
            accountWalletAsset = new AccountWalletAsset();
            accountWalletAsset.setAmount(BigDecimal.ZERO);
            accountWalletAsset.setRelatedStockinfoId(stockinfoId);
            accountWalletAsset.setStockinfoId(stockinfoId);
            accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
            accountWalletAsset.setPrice(BigDecimal.ONE);
            accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
            accountWalletAsset.setRemark("");
            accountWalletAsset.setAccountId(accountId);
            accountWalletAsset.setRelatedStockinfoId(stockinfoId);
            accountWalletAssetService.insert(accountWalletAsset);
        }
    }
    
    @Override
    public int fundUnfrozenBmsAsset() throws BusinessException
    {
        logger.debug("fundUnfrozenBmsAsset 统一资产解冻服务接口start--------------------------------------------------");
        // 统一当前时间
        Date date = new Date();
        Long time = CalendarUtils.getLongFromTime(date);
        logger.debug("current time=" + time);
        logger.debug("FundAdjust start ");
        AccountFundAdjust adjustSearch = new AccountFundAdjust();
        adjustSearch.setLockEndDay(new Timestamp(System.currentTimeMillis())); // 这里传入的是当前日期，查询小于当前时间的记录
        adjustSearch.setNeedLock(FundConsts.ASSET_LOCK_STATUS_YES);// 查找已锁定的
        List<AccountFundAdjust> adjustList = accountFundAdjustService.findLockedList(adjustSearch);
        logger.debug("FundAdjust length " + adjustList.size());
        for (AccountFundAdjust adjust : adjustList)
        {
            logger.debug("FundAdjust fundAdjust " + adjust.toString());
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(adjust.getAccountId());
            fundModel.setStockinfoId(adjust.getStockinfoId());
            fundModel.setAmount(adjust.getAdjustAmt());
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setCreateBy(adjust.getAccountId());
            fundModel.setBusinessFlag(adjust.getBusinessFlag());
            fundModel.setOriginalBusinessId(adjust.getId());
            logger.debug("FundAdjust fundModel = " + fundModel.toString());
            this.fundTransaction(fundModel);
            // 设置标记
            adjust.setLockStatus(FundConsts.ASSET_LOCK_STATUS_NO);
            accountFundAdjustService.updateByPrimaryKey(adjust);
        }
        logger.debug("FundAdjust end ");
        logger.debug("AccountInvitation start ");
        AccountInvitation accountInvitationSearch = new AccountInvitation();
        accountInvitationSearch.setLockEndDay(time); // 这里传入的是当前日期，查询小于当前时间的记录
        List<AccountInvitation> invitationList = accountInvitationService.findLockedList(accountInvitationSearch);
        logger.debug("AccountInvitation length= " + invitationList.size());
        for (AccountInvitation invitation : invitationList)
        {
            logger.debug("AccountInvitation invitation = " + invitation.toString());
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(invitation.getAccountId());
            fundModel.setStockinfoId(FundConsts.WALLET_BMS_TYPE);
            fundModel.setAmount(invitation.getBmsNum());
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setCreateBy(invitation.getAccountId());
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN);// 账户资产解冻
            fundModel.setOriginalBusinessId(invitation.getId());
            logger.debug("AccountInvitation fundModel = " + fundModel.toString());
            this.fundTransaction(fundModel);
            // 设置标记
            invitation.setLockStatus(FundConsts.ASSET_LOCK_STATUS_NO);
            accountInvitationService.updateByPrimaryKey(invitation);
        }
        logger.debug("AccountInvitation end ");
        logger.debug("fundUnfrozenBmsAsset 统一资产解冻服务接口end--------------------------------------------------");
        return 0;
    }
    
    /**
     * 撮合交易现货买入委托--合约资产
     * @param fundModel
     * @param accountContractAsset
     * @throws BusinessException
     * @author zcx  2017-09-20 10:54:16
     */
    private void spotBuyBtcEntrustVCoinMoney(FundModel fundModel, AccountContractAsset accountContractAsset, boolean isVCoin) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(isVCoin ? fundModel.getStockinfoIdEx() : fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountContractAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());// 交易对
        accountFundCurrent.setRemark("撮合交易现货买入委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmountEx() + ";当前合约账户资产冻结:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmountEx());
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货买入委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountContractAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountContractAsset.setFrozenAmt(accountContractAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        logger.debug("撮合交易现货买入委托 合约账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
    }
    
    /**
     * 撮合交易现货买入委托--钱包资产
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author zcx  2017-11-29 09:50:19
     */
    private void spotBuyBtcEntrustVCoinMoney(FundModel fundModel, AccountWalletAsset accountWalletAsset, boolean isVCoin) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(isVCoin ? fundModel.getStockinfoIdEx() : fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());// 交易对
        accountFundCurrent.setRemark("撮合交易现货买入委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmountEx() + ";当前钱包账户资产冻结:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmountEx());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货买入委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("撮合交易现货买入委托 合约账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 撮合交易现货买入委托--杠杆钱包资产
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zcx  2017-11-29 09:50:19
     */
    private void spotBuyBtcEntrustVCoinMoney(FundModel fundModel, AccountSpotAsset accountSpotAsset, boolean isVCoin) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(isVCoin ? fundModel.getStockinfoIdEx() : fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountSpotAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());// 交易对
        accountFundCurrent.setRemark("撮合交易现货买入委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmountEx() + ";当前钱包账户资产冻结:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmountEx());
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货买入委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountSpotAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountSpotAsset.setFrozenAmt(accountSpotAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("撮合交易现货买入委托 合约账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
    }
    
    /**
     * 撮合交易现货卖出委托-合约资产处理
     * @param fundModel
     * @param accountContractAsset
     * @throws BusinessException
     * @author zcx  2017-09-20 12:31:28
     */
    private void spotSellBtcEntrustVCoinMoney(FundModel fundModel, AccountContractAsset accountContractAsset, boolean isVCoin) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId((isVCoin ? fundModel.getStockinfoId() : fundModel.getStockinfoIdEx()));
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountContractAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());// 交易对
        accountFundCurrent.setRemark("撮合交易现货卖出委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产冻结:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货卖出委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountContractAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountContractAsset.setFrozenAmt(accountContractAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        logger.debug("撮合交易现货卖出委托 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
    }
    
    /**
     * 撮合交易现货卖出委托-钱包资产处理
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author zcx  2017-11-29 09:49:06
     */
    private void spotSellBtcEntrustVCoinMoney(FundModel fundModel, AccountWalletAsset accountWalletAsset, boolean isVCoin) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(isVCoin ? fundModel.getStockinfoId() : fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());// 交易对
        accountFundCurrent.setRemark("撮合交易现货卖出委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产冻结:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货卖出委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("撮合交易现货卖出委托 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 撮合交易现货卖出委托-杠杆钱包资产处理
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zcx  2017-11-29 09:49:06
     */
    private void spotSellBtcEntrustVCoinMoney(FundModel fundModel, AccountSpotAsset accountSpotAsset, boolean isVCoin) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(isVCoin ? fundModel.getStockinfoId() : fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountSpotAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());// 交易对
        accountFundCurrent.setRemark("撮合交易现货卖出委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产冻结:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货卖出委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountSpotAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountSpotAsset.setFrozenAmt(accountSpotAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("撮合交易现货卖出委托 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
    }
    
    /**
     * 现货卖出委托
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotSellEntrust(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRemark("现货卖出委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产冻结:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货卖出委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
        logger.debug("现货卖出委托 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 现货买入委托
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotBuyEntrust(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRemark("现货买入委托:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmountEx() + ";当前钱包账户资产冻结:" + fundModel.getAmountEx());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmountEx());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入委托 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmountEx()));
        logger.debug("现货买入委托 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 现货卖出委托撤单或拒绝
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotSellEntrustWithdrawOrReject(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRemark("现货买入委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入委托撤单或拒绝 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        logger.debug("现货买入委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 现货买入委托撤单或拒绝
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotBuyEntrustWithdrawOrReject(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRemark("现货买入委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入委托撤单或拒绝 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        logger.debug("现货买入委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 撮合交易现货卖出委托撤单或拒绝--合约资产
     * @param fundModel
     * @param accountContractAsset
     * @throws BusinessException
     * @author zhangcx  2017年7月21日 上午10:30:55
     */
    private void spotSellBTCEntrustVCoinMoneyWithdraw(FundModel fundModel, AccountContractAsset accountContractAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(accountContractAsset.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountContractAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());// 交易对
        accountFundCurrent.setRemark("撮合交易现货卖出委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货卖出委托撤单或拒绝 合约资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountContractAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountContractAsset.setFrozenAmt(accountContractAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        logger.debug("现货卖出委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
    }
    
    /**
     * 撮合交易现货卖出委托撤单或拒绝--钱包资产
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author zhangcx  2017年7月21日 上午10:30:55
     */
    private void spotSellBTCEntrustVCoinMoneyWithdraw(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(accountWalletAsset.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());// 交易对
        accountFundCurrent.setRemark("撮合交易现货卖出委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货卖出委托撤单或拒绝 合约资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("现货卖出委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 撮合交易现货卖出委托撤单或拒绝--杠杆钱包资产
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zhangcx  2017年7月21日 上午10:30:55
     */
    private void spotSellBTCEntrustVCoinMoneyWithdraw(FundModel fundModel, AccountSpotAsset accountSpotAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(accountSpotAsset.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountSpotAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());// 交易对
        accountFundCurrent.setRemark("撮合交易现货卖出委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货卖出委托撤单或拒绝 合约资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountSpotAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountSpotAsset.setFrozenAmt(accountSpotAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("现货卖出委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountSpotAsset.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
    }
    
    /**
    * 撮合交易现货买入委托撤单或拒绝-合约资产
    * @param fundModel
    * @param accountContractAsset
    * @throws BusinessException
    * @author zhangcx  2017年7月21日 上午10:30:55
    */
    private void spotBuyBTCEntrustVCoinMoneyWithdraw(FundModel fundModel, AccountContractAsset accountContractAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(accountContractAsset.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountContractAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());// 交易对
        accountFundCurrent.setRemark("撮合交易现货买入委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货买入委托撤单或拒绝 合约资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountContractAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountContractAsset.setFrozenAmt(accountContractAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        logger.debug("现货买入委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
    }
    
    /**
     * 撮合交易现货买入委托撤单或拒绝-钱包资产
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author zhangcx  2017年7月21日 上午10:30:55
     */
    private void spotBuyBTCEntrustVCoinMoneyWithdraw(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(accountWalletAsset.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRemark("撮合交易现货买入委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货买入委托撤单或拒绝 合约资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("现货买入委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 撮合交易现货买入委托撤单或拒绝-杠杆钱包资产
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zhangcx  2017年7月21日 上午10:30:55
     */
    private void spotBuyBTCEntrustVCoinMoneyWithdraw(FundModel fundModel, AccountSpotAsset accountSpotAsset) throws BusinessException
    {
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(accountSpotAsset.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountSpotAsset.getAmount());
        fundCurrentModel.setOccurAmt(BigDecimal.ZERO);
        fundCurrentModel.setFee(BigDecimal.ZERO); // 委托费用为0
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRemark("撮合交易现货买入委托撤单或拒绝:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("撮合交易现货买入委托撤单或拒绝 合约资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量
        // 检验金额
        this.validateAmountIsSmallZero(accountSpotAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountSpotAsset.setFrozenAmt(accountSpotAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("现货买入委托撤单或拒绝 钱包账户资产 准备修改数据为:" + accountSpotAsset.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
    }
    
    /**
     * 现货卖出委托成交
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotSellEntrustDeal(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // 本身证券BMS
        // step1 增加账户资金流水 资产解冻流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount()); // 解冻金额本金
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRemark("现货卖出委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";钱包账户资产解冻:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货卖出委托成交  账户资金交易流水 资产解冻流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step1 增加账户资金流水 资产减少流水
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("现货卖出委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";钱包账户资产减少:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货卖出委托成交  账户资金交易流水 资产减少流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量 减少钱包账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmount()));
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        logger.debug("现货卖出委托成交 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // 扩展证券BTC
        BigDecimal orgAmt = BigDecimal.ZERO;
        AccountWalletAsset accountWalletAssetNew = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoIdEx());
        if (null == accountWalletAssetNew)
        {
            orgAmt = BigDecimal.ZERO;
            accountWalletAssetNew = new AccountWalletAsset();
            accountWalletAssetNew.setAccountId(fundModel.getAccountId());
            accountWalletAssetNew.setStockinfoId(fundModel.getStockinfoIdEx());
            accountWalletAssetNew.setAmount(fundModel.getAmountEx().subtract(fundModel.getFee()));
            accountWalletAssetNew.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            logger.debug("现货卖出委托成交 钱包账户资产 扩展证券 准备插入数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.insert(accountWalletAssetNew);
        }
        else
        {
            orgAmt = accountWalletAssetNew.getAmount();
            accountWalletAssetNew.setAmount(accountWalletAssetNew.getAmount().add(fundModel.getAmountEx().subtract(fundModel.getFee())));
            logger.debug("现货卖出委托成交 钱包账户资产 扩展证券 准备修改数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAssetNew);
        }
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAssetNew.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(fundModel.getFee());
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmountEx().subtract(fundModel.getFee())));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent
                .setRemark("现货卖出委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmountEx() + ";钱包账户资产增加:" + fundModel.getAmountEx().subtract(fundModel.getFee()));
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货卖出委托成交  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 现货买入委托成交
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotBuyEntrustDeal(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // 本身证券BTC
        // step1 增加账户资金流水 资产解冻流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx()); // 解冻金额本金
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRemark("现货买入委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";钱包账户资产解冻:" + fundModel.getAmountEx());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmountEx());
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入委托成交  账户资金交易流水 资产解冻流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step1 增加账户资金流水 资产减少流水
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
        accountFundCurrent.setRemark("现货买入委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";钱包账户资产减少:" + fundModel.getAmountEx());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入委托成交 账户资金交易流水 资产减少流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产冻结数量 减少钱包账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
        accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        logger.debug("现货买入委托成交 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // 扩展证券BMS
        BigDecimal orgAmt = BigDecimal.ZERO;
        AccountWalletAsset accountWalletAssetNew = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId());
        if (null == accountWalletAssetNew)
        {
            orgAmt = BigDecimal.ZERO;
            accountWalletAssetNew = new AccountWalletAsset();
            accountWalletAssetNew.setAccountId(fundModel.getAccountId());
            accountWalletAssetNew.setStockinfoId(fundModel.getStockinfoId());
            accountWalletAssetNew.setAmount(fundModel.getAmount().subtract(fundModel.getFee()));
            accountWalletAssetNew.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            logger.debug("现货买入委托成交 钱包账户资产 扩展证券 准备插入数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.insert(accountWalletAssetNew);
        }
        else
        {
            orgAmt = accountWalletAssetNew.getAmount();
            accountWalletAssetNew.setAmount(accountWalletAssetNew.getAmount().add(fundModel.getAmount().subtract(fundModel.getFee())));
            logger.debug("现货买入委托成交 钱包账户资产 扩展证券 准备修改数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAssetNew);
        }
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAssetNew.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(fundModel.getFee());
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmount().subtract(fundModel.getFee())));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent
                .setRemark("现货买入委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";钱包账户资产增加:" + fundModel.getAmount().subtract(fundModel.getFee()));
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入委托成交  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 现货卖出无委托成交
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotSellNoEntrustDeal(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // 本身证券BMS
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("现货卖出无委托成交," + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前钱包账户资产当前数量减少:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货卖出无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmount()));
        logger.debug("现货卖出无委托成交 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // 扩展证券BTC
        BigDecimal orgAmt = BigDecimal.ZERO;
        AccountWalletAsset accountWalletAssetNew = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoIdEx());
        if (null == accountWalletAssetNew)
        {
            orgAmt = BigDecimal.ZERO;
            accountWalletAssetNew = new AccountWalletAsset();
            accountWalletAssetNew.setAccountId(fundModel.getAccountId());
            accountWalletAssetNew.setStockinfoId(fundModel.getStockinfoIdEx());
            accountWalletAssetNew.setAmount(fundModel.getAmountEx().subtract(fundModel.getFee()));
            accountWalletAssetNew.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            logger.debug("现货卖出无委托成交 钱包账户资产 扩展证券 准备插入数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.insert(accountWalletAssetNew);
        }
        else
        {
            orgAmt = accountWalletAssetNew.getAmount();
            accountWalletAssetNew.setAmount(accountWalletAssetNew.getAmount().add(fundModel.getAmountEx().subtract(fundModel.getFee())));
            logger.debug("现货卖出无委托成交 钱包账户资产 扩展证券 准备修改数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAssetNew);
        }
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAssetNew.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(fundModel.getFee());
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmountEx().subtract(fundModel.getFee())));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark(
                "现货卖出无委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmountEx() + ";钱包账户资产增加:" + fundModel.getAmountEx().subtract(fundModel.getFee()));
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货卖出无委托成交  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 内部成交 现货卖出无委托成交
     * @param fundModel
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotInnerSellNoEntrustDeal(FundModel fundModel) throws BusinessException
    {
        logger.debug("spotInnerSellNoEntrustDeal 传入参数：" + fundModel.toString());
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            spotContractInnerSellNoEntrustDeal(fundModel, stockInfo);
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            spotWalletInnerSellNoEntrustDeal(fundModel);
        }
        else
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }
    
    private void spotWalletInnerSellNoEntrustDeal(FundModel fundModel) throws BusinessException
    {
        // 当前用户数字货币账户
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        logger.debug("杠杆现货 当前用户的数字货币资产余额：" + accountSpotAsset.getAmount());
        // 当前账户卖出数字货币 扩展证券代码
        // step1 增加当前账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountSpotAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setAccountId(fundModel.getAccountId());
        // 检验金额
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("内部交易现货卖出无委托成交," + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产当前数量减少:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("杠杆现货 内部交易现货卖出无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少当前账户合约账户资产当前数量
        // 检验金额
        logger.debug("杠杆现货 当前用户数字货币转出前" + accountSpotAsset.getAmount());
        logger.debug("杠杆现货 当前用户数字货币转出" + fundModel.getAmount());
        logger.debug("杠杆现货 当前用户数字货币转出后" + accountSpotAsset.getAmount().subtract(fundModel.getAmount()));
        this.validateAmountIsSmallZero(accountSpotAsset.getAmount().subtract(fundModel.getAmount()));
        accountSpotAsset.setAmount(accountSpotAsset.getAmount().subtract(fundModel.getAmount()));
        accountSpotAsset.setFrozenAmt(accountSpotAsset.getFrozenAmt());
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("杠杆现货 内部交易现货卖出无委托成交 合约账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
        // 超级用户数字货币账户
        accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset = this.findAccountSpotAssetFormDB(fundModel.getCreateBy(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        // 扩展证券 BTC
        // step1 增加当前账户资金流水
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getCreateBy());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountSpotAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setAccountId(fundModel.getCreateBy());
        // 检验金额
        this.validateAmountIsSmallZero(accountSpotAsset.getAmount().add(fundModel.getAmount()));
        accountFundCurrent.setLastAmt(accountSpotAsset.getAmount().add(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark("内部交易现货卖出无委托成交," + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产当前数量增加:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("杠杆现货 内部交易现货卖出无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少超级账户合约账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountSpotAsset.getAmount().add(fundModel.getAmount()));
        accountSpotAsset.setAmount(accountSpotAsset.getAmount().add(fundModel.getAmount()));
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("杠杆现货 内部交易现货卖出无委托成交 合约账户资产 准备修改数据为:" + accountSpotAsset.toString());
        accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
    }
    
    private void spotContractInnerSellNoEntrustDeal(FundModel fundModel, StockInfo stockInfo) throws BusinessException
    {
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        logger.debug("是否为数字货币标的：" + isVCoin);
        // 数字货币 内部成交转移
        if (StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(), FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER))
        { // 用户委托卖出 超级用户委托买入
          // 当前用户数字货币账户
            AccountContractAsset accountContractAsset2 = new AccountContractAsset();
            accountContractAsset2 = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            logger.debug("当前用户的数字货币合约资产余额：" + accountContractAsset2.getAmount());
            // 当前账户卖出数字货币 扩展证券代码
            // step1 增加当前账户资金流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
            fundCurrentModel.setAccountAssetId(accountContractAsset2.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(accountContractAsset2.getAmount());
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setAccountId(fundModel.getAccountId());
            // 检验金额
            accountFundCurrent.setLastAmt(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
            accountFundCurrent.setRemark("内部交易现货卖出无委托成交," + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产当前数量减少:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(accountContractAsset2.getRelatedStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("内部交易现货卖出无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            // step2 减少当前账户合约账户资产当前数量
            // 检验金额
            logger.debug("当前用户数字货币转出前" + accountContractAsset2.getAmount());
            logger.debug("当前用户数字货币转出" + fundModel.getAmount());
            logger.debug("当前用户数字货币转出后" + accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            this.validateAmountIsSmallZero(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            accountContractAsset2.setAmount(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            accountContractAsset2.setFrozenAmt(accountContractAsset2.getFrozenAmt());
            accountContractAsset2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset2.setTableName(getStockInfo(accountContractAsset2.getRelatedStockinfoId()).getTableAsset());
            logger.debug("内部交易现货卖出无委托成交 合约账户资产 准备修改数据为:" + accountFundCurrent.toString());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset2);
            // 超级用户数字货币账户
            accountContractAsset2 = new AccountContractAsset();
            accountContractAsset2 = this.findAccountContractAssetFormDB(fundModel.getCreateBy(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            // 扩展证券 BTC
            // step1 增加当前账户资金流水
            fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getCreateBy());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
            fundCurrentModel.setAccountAssetId(accountContractAsset2.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(accountContractAsset2.getAmount());
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setAccountId(fundModel.getCreateBy());
            // 检验金额
            this.validateAmountIsSmallZero(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountFundCurrent.setLastAmt(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
            accountFundCurrent.setRemark("内部交易现货卖出无委托成交," + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产当前数量增加:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(accountContractAsset2.getRelatedStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("内部交易现货卖出无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            // step2 减少超级账户合约账户资产当前数量
            // 检验金额
            this.validateAmountIsSmallZero(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountContractAsset2.setAmount(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountContractAsset2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset2.setTableName(getStockInfo(accountContractAsset2.getRelatedStockinfoId()).getTableAsset());
            logger.debug("内部交易现货卖出无委托成交 合约账户资产 准备修改数据为:" + accountContractAsset2.toString());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset2);
        }
        // 法定货币 内部成交转移
        else if (StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(), FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER))
        { // 用户委托买入 超级用户委托卖出
          // 当前用户法定货币账户
            AccountContractAsset accountContractAsset2 = new AccountContractAsset();
            accountContractAsset2 = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            logger.debug("当前用户的法定货币合约资产余额：" + accountContractAsset2.getAmount());
            // 当前账户买入数字货币 扩展证券代码
            // step1 增加当前账户资金流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
            fundCurrentModel.setAccountAssetId(accountContractAsset2.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(accountContractAsset2.getAmount());
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setAccountId(fundModel.getAccountId());
            // 检验金额
            accountFundCurrent.setLastAmt(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
            accountFundCurrent.setRemark("内部交易现货卖出无委托成交," + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产当前数量减少:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(accountContractAsset2.getRelatedStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("内部交易现货卖出无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            // step2 减少当前账户合约账户资产当前数量
            // 检验金额
            logger.debug("当前用户法定货币转出前" + accountContractAsset2.getAmount());
            logger.debug("当前用户法定货币转出" + fundModel.getAmount());
            logger.debug("当前用户法定货币转出后" + accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            this.validateAmountIsSmallZero(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            accountContractAsset2.setAmount(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
            accountContractAsset2.setFrozenAmt(accountContractAsset2.getFrozenAmt());
            accountContractAsset2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset2.setTableName(getStockInfo(accountContractAsset2.getRelatedStockinfoId()).getTableAsset());
            logger.debug("内部交易现货卖出无委托成交 合约账户资产 准备修改数据为:" + accountFundCurrent.toString());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset2);
            // 超级用户法定货币账户
            accountContractAsset2 = new AccountContractAsset();
            accountContractAsset2 = this.findAccountContractAssetFormDB(fundModel.getCreateBy(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
            // 扩展证券 法定货币
            // step1 增加当前账户资金流水
            fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getCreateBy());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
            fundCurrentModel.setAccountAssetId(accountContractAsset2.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(accountContractAsset2.getAmount());
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setAccountId(fundModel.getCreateBy());
            // 检验金额
            this.validateAmountIsSmallZero(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountFundCurrent.setLastAmt(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
            accountFundCurrent.setRemark("内部交易现货卖出无委托成交," + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";当前合约账户资产当前数量增加:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(accountContractAsset2.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(accountContractAsset2.getRelatedStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("内部交易现货卖出无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            // step2 减少超级账户合约账户资产当前数量
            // 检验金额
            this.validateAmountIsSmallZero(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountContractAsset2.setAmount(accountContractAsset2.getAmount().add(fundModel.getAmount()));
            accountContractAsset2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset2.setTableName(getStockInfo(accountContractAsset2.getRelatedStockinfoId()).getTableAsset());
            logger.debug("内部交易现货卖出无委托成交 合约账户资产 准备修改数据为:" + accountContractAsset2.toString());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset2);
        }
        else
        {
            logger.debug("内部成交资金处理异常，不支持改业务类别，ID:" + fundModel.getBusinessFlag());
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }
    
    /**
     * 现货买入无委托成交
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void spotBuyNoEntrustDeal(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // 本身证券BTC
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("现货买入无委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";钱包账户资产当前数量减少:" + fundModel.getAmountEx());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入无委托成交 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        logger.debug("现货买入无委托成交 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // 扩展证券BMS
        BigDecimal orgAmt = BigDecimal.ZERO;
        AccountWalletAsset accountWalletAssetNew = this.findAccountWalletAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId());
        if (null == accountWalletAssetNew)
        {
            orgAmt = BigDecimal.ZERO;
            accountWalletAssetNew = new AccountWalletAsset();
            accountWalletAssetNew.setAccountId(fundModel.getAccountId());
            accountWalletAssetNew.setStockinfoId(fundModel.getStockinfoId());
            accountWalletAssetNew.setAmount(fundModel.getAmount().subtract(fundModel.getFee()));
            accountWalletAssetNew.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            logger.debug("现货买入无委托成交 钱包账户资产 扩展证券 准备插入数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.insert(accountWalletAssetNew);
        }
        else
        {
            orgAmt = accountWalletAssetNew.getAmount();
            accountWalletAssetNew.setAmount(accountWalletAssetNew.getAmount().add(fundModel.getAmount().subtract(fundModel.getFee())));
            logger.debug("现货买入无委托成交 钱包账户资产 扩展证券 准备修改数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAssetNew);
        }
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAssetNew.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(fundModel.getFee());
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmount().subtract(fundModel.getFee())));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent
                .setRemark("现货买入无委托成交:" + fundModel.getBusinessFlag() + ":" + fundModel.getAmount() + ";钱包账户资产增加:" + fundModel.getAmount().subtract(fundModel.getFee()));
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("现货买入无委托成交  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * ICO预购申请
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void icoSubscribePreReq(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoIdEx());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmountEx().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("ICO预购申请:" + fundModel.getAmount() + ";当前钱包账户资产减少:" + fundModel.getAmountEx());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("ICO预购申请 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        logger.debug("ICO预购申请 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // BMS-----------------------------
        // ICO认购确认成功要处理BMS对应的资产
        AccountWalletAsset accountWalletAssetNew = new AccountWalletAsset();
        accountWalletAssetNew.setAccountId(fundModel.getAccountId());
        accountWalletAssetNew.setStockinfoId(fundModel.getStockinfoId());
        List<AccountWalletAsset> accountWalletAssetList = accountWalletAssetService.findList(accountWalletAssetNew);
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // 第一次认购确认成功
        if (accountWalletAssetList.size() <= 0)
        {
            orgAmt = BigDecimal.ZERO;
            forzenOrgAmt = BigDecimal.ZERO;
            accountWalletAssetNew = this.constructAccountWalletAsset(fundModel);
            accountWalletAssetNew.setAmount(fundModel.getAmount());
            accountWalletAssetNew.setFrozenAmt(accountWalletAssetNew.getFrozenAmt().add(fundModel.getAmount()));
            logger.debug("ICO预购申请  处理BMS对应的资产 钱包账户资产 准备插入数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.insert(accountWalletAssetNew);
        } // 非第一次认购确认成功
        else
        {
            accountWalletAssetNew = accountWalletAssetList.get(0);
            orgAmt = accountWalletAssetNew.getAmount();
            forzenOrgAmt = accountWalletAssetNew.getFrozenAmt();
            accountWalletAssetNew.setAmount(accountWalletAssetNew.getAmount().add(fundModel.getAmount()));
            accountWalletAssetNew.setFrozenAmt(accountWalletAssetNew.getFrozenAmt().add(fundModel.getAmount()));
            logger.debug("ICO预购申请  处理BMS对应的资产 钱包账户资产 准备修改数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAssetNew);
        }
        // BMS对应的资产FundCurrent 资产增加
        FundCurrentModel fundCurrentModelNew = new FundCurrentModel();
        fundCurrentModelNew.setAccountId(fundModel.getAccountId());
        fundCurrentModelNew.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModelNew.setAccountAssetId(accountWalletAssetNew.getId());
        fundCurrentModelNew.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModelNew.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModelNew.setOrgAmt(orgAmt);
        fundCurrentModelNew.setOccurAmt(fundModel.getAmount());
        fundCurrentModelNew.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrentNew = this.constructAccountFundCurrent(fundModel, fundCurrentModelNew);
        accountFundCurrentNew.setLastAmt(orgAmt.add(fundModel.getAmount()));
        accountFundCurrentNew.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrentNew.setRemark("ICO预购申请  处理BMS对应的资产:" + fundModel.getAmount() + ";当前钱包账户资产增加:" + fundModel.getAmount());
        accountFundCurrentNew.setContractAmt(BigDecimal.ZERO);
        accountFundCurrentNew.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrentNew.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrentNew.setForzenLastAmt(forzenOrgAmt);
        accountFundCurrentNew.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrentNew.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("ICO预购申请 处理BMS对应的资产 账户资金交易流水 准备插入数据为:" + accountFundCurrentNew.toString());
        accountFundCurrentService.insert(accountFundCurrentNew);
        // BMS对应的资产FundCurrent 资产冻结
        fundCurrentModelNew = new FundCurrentModel();
        fundCurrentModelNew.setAccountId(fundModel.getAccountId());
        fundCurrentModelNew.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModelNew.setAccountAssetId(accountWalletAssetNew.getId());
        fundCurrentModelNew.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModelNew.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModelNew.setOrgAmt(orgAmt.add(fundModel.getAmount()));
        fundCurrentModelNew.setOccurAmt(fundModel.getAmount());
        fundCurrentModelNew.setFee(BigDecimal.ZERO);
        accountFundCurrentNew = this.constructAccountFundCurrent(fundModel, fundCurrentModelNew);
        accountFundCurrentNew.setLastAmt(orgAmt.add(fundModel.getAmount()));
        accountFundCurrentNew.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrentNew.setRemark("ICO预购申请  处理BMS对应的资产:" + fundModel.getAmount() + ";当前钱包账户资产冻结:" + fundModel.getAmount());
        accountFundCurrentNew.setContractAmt(BigDecimal.ZERO);
        accountFundCurrentNew.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrentNew.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrentNew.setForzenLastAmt(forzenOrgAmt.add(fundModel.getAmount()));
        accountFundCurrentNew.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrentNew.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("ICO预购申请 处理BMS对应的资产 账户资金交易流水 准备插入数据为:" + accountFundCurrentNew.toString());
        accountFundCurrentService.insert(accountFundCurrentNew);
        // BMS-----------------------------
    }
    
    /**
     * ICO正式认购申请
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void icoSubscribeReq(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoIdEx());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmountEx().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("ICO正式认购申请:" + fundModel.getAmount() + ";当前钱包账户资产减少:" + fundModel.getAmountEx());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("ICO正式认购申请 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少钱包账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        logger.debug("ICO正式认购申请 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // BMS-----------------------------
        // ICO认购确认成功要处理BMS对应的资产
        AccountWalletAsset accountWalletAssetNew = new AccountWalletAsset();
        accountWalletAssetNew.setAccountId(fundModel.getAccountId());
        accountWalletAssetNew.setStockinfoId(fundModel.getStockinfoId());
        List<AccountWalletAsset> accountWalletAssetList = accountWalletAssetService.findList(accountWalletAssetNew);
        BigDecimal orgAmt = BigDecimal.ZERO;
        // 第一次认购确认成功
        if (accountWalletAssetList.size() <= 0)
        {
            orgAmt = BigDecimal.ZERO;
            accountWalletAssetNew = this.constructAccountWalletAsset(fundModel);
            accountWalletAssetNew.setAmount(fundModel.getAmount());
            logger.debug("ICO正式认购申请  处理BMS对应的资产 钱包账户资产 准备插入数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.insert(accountWalletAssetNew);
        } // 非第一次认购确认成功
        else
        {
            accountWalletAssetNew = accountWalletAssetList.get(0);
            orgAmt = accountWalletAssetNew.getAmount();
            accountWalletAssetNew.setAmount(accountWalletAssetNew.getAmount().add(fundModel.getAmount()));
            logger.debug("ICO正式认购申请  处理BMS对应的资产 钱包账户资产 准备修改数据为:" + accountWalletAssetNew.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAssetNew);
        }
        // BMS对应的资产FundCurrent
        FundCurrentModel fundCurrentModelNew = new FundCurrentModel();
        fundCurrentModelNew.setAccountId(fundModel.getAccountId());
        fundCurrentModelNew.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModelNew.setAccountAssetId(accountWalletAssetNew.getId());
        fundCurrentModelNew.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModelNew.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModelNew.setOrgAmt(orgAmt);
        fundCurrentModelNew.setOccurAmt(fundModel.getAmount());
        fundCurrentModelNew.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrentNew = this.constructAccountFundCurrent(fundModel, fundCurrentModelNew);
        accountFundCurrentNew.setLastAmt(fundCurrentModelNew.getOrgAmt().add(fundModel.getAmount()));
        accountFundCurrentNew.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrentNew.setRemark("ICO正式认购申请  处理BMS对应的资产:" + fundModel.getAmount() + ";当前钱包账户资产增加:" + fundModel.getAmount());
        accountFundCurrentNew.setContractAmt(BigDecimal.ZERO);
        accountFundCurrentNew.setForzenOrgAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrentNew.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrentNew.setForzenLastAmt(accountWalletAssetNew.getFrozenAmt());
        accountFundCurrentNew.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrentNew.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("ICO正式认购申请 处理BMS对应的资产 账户资金交易流水 准备插入数据为:" + accountFundCurrentNew.toString());
        accountFundCurrentService.insert(accountFundCurrentNew);
        // BMS-----------------------------
    }
    
    /**
     * ICO认购确认
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    @SuppressWarnings("unused")
    private void icoSubscribeConfirm(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoIdEx());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmountEx().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoIdEx());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmountEx());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // ICO认购确认成功
        if (fundModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_SUCCESS))
        {
            accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
            accountFundCurrent.setRemark("ICO认购确认:" + fundModel.getAmountEx() + ";当前钱包账户资产解冻:" + fundModel.getAmountEx() + ";当前钱包账户资产减少:" + fundModel.getAmountEx());
            // BMS-----------------------------
            // ICO认购确认成功要处理BMS对应的资产
            if (null == fundModel.getStockinfoId()) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
            if (null == fundModel.getAmount()) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
            AccountWalletAsset accountWalletAssetNew = new AccountWalletAsset();
            accountWalletAssetNew.setAccountId(fundModel.getAccountId());
            accountWalletAssetNew.setStockinfoId(fundModel.getStockinfoId());
            List<AccountWalletAsset> accountSpotAssetList = accountWalletAssetService.findList(accountWalletAssetNew);
            BigDecimal orgAmt = BigDecimal.ZERO;
            // 第一次认购确认成功
            if (accountSpotAssetList.size() <= 0)
            {
                orgAmt = BigDecimal.ZERO;
                accountWalletAssetNew = this.constructAccountWalletAsset(fundModel);
                accountWalletAssetNew.setAmount(fundModel.getAmount());
                logger.debug("ICO认购确认 处理BMS对应的资产 钱包账户资产 准备插入数据为:" + accountWalletAssetNew.toString());
                accountWalletAssetService.insert(accountWalletAssetNew);
            } // 非第一次认购确认成功
            else
            {
                accountWalletAssetNew = accountSpotAssetList.get(0);
                orgAmt = accountWalletAssetNew.getAmount();
                accountWalletAssetNew.setAmount(accountWalletAssetNew.getAmount().add(fundModel.getAmount()));
                accountWalletAssetNew.setFrozenAmt(accountWalletAssetNew.getFrozenAmt().add(fundModel.getAmount()));
                logger.debug("ICO认购确认 处理BMS对应的资产 钱包账户资产 准备修改数据为:" + accountWalletAssetNew.toString());
                accountWalletAssetService.updateByPrimaryKey(accountWalletAssetNew);
            }
            // BMS对应的资产FundCurrent
            FundCurrentModel fundCurrentModelNew = fundCurrentModel;
            fundCurrentModelNew.setAccountId(fundModel.getAccountId());
            fundCurrentModelNew.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
            fundCurrentModelNew.setAccountAssetId(accountWalletAssetNew.getId());
            fundCurrentModelNew.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModelNew.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModelNew.setOrgAmt(orgAmt);
            fundCurrentModelNew.setOccurAmt(fundModel.getAmount());
            fundCurrentModelNew.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrentNew = this.constructAccountFundCurrent(fundModel, fundCurrentModelNew);
            accountFundCurrentNew.setLastAmt(fundCurrentModelNew.getOrgAmt().add(fundModel.getAmount()));
            accountFundCurrentNew.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
            accountFundCurrentNew
                    .setRemark("ICO认购确认 处理BMS对应的资产:" + fundModel.getAmount() + ";当前钱包账户资产增加:" + fundModel.getAmount() + ";当前钱包账户资产冻结:" + fundModel.getAmount());
            accountFundCurrentNew.setContractAmt(BigDecimal.ZERO);
            accountFundCurrentNew.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("ICO认购确认 处理BMS对应的资产 账户资金交易流水 准备插入数据为:" + accountFundCurrentNew.toString());
            accountFundCurrentService.insert(accountFundCurrentNew);
            // BMS-----------------------------
        } // ICO认购确认失败
        else if (fundModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_FAIL))
        {
            accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
            accountFundCurrent.setRemark("ICO认购确认:0;当前钱包账户资产解冻:" + fundModel.getAmountEx() + ";当前钱包账户资产减少:0");
        }
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("ICO认购确认 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 钱包账户资产处理
        // ICO认购确认成功
        if (fundModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_SUCCESS))
        {
            // step2 减少钱包账户资产冻结金额 减少钱包账户资产当前金额
            // 检验金额
            this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
            accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
            // 检验金额
            this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
            accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmountEx()));
        } // ICO认购确认失败
        else if (fundModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_FAIL))
        {
            // step2 减少钱包账户资产冻结金额
            // 检验金额
            this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
            accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().subtract(fundModel.getAmountEx()));
        }
        logger.debug("ICO认购确认 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * ICO锻造铸币
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void icoMint(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        if (null == fundModel.getAmountEx() || fundModel.getAmountEx().compareTo(BigDecimal.ZERO) < 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountWalletAsset.getAmount());
        if ((fundModel.getAmountEx().subtract(fundModel.getAmount())).compareTo(BigDecimal.ZERO) >= 0)
        {
            fundCurrentModel.setOccurAmt(fundModel.getAmountEx().subtract(fundModel.getAmount()));
        }
        else
        {
            fundCurrentModel.setOccurAmt(fundModel.getAmount().subtract(fundModel.getAmountEx()));
        }
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        if ((fundModel.getAmountEx().subtract(fundModel.getAmount())).compareTo(BigDecimal.ZERO) >= 0)
        {
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        }
        else
        {
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        }
        // 校验金额
        this.validateAmountIsSmallZero(accountWalletAsset.getAmount().subtract(fundModel.getAmount()).add(fundModel.getAmountEx()));
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount().subtract(fundModel.getAmount()).add(fundModel.getAmountEx()));
        accountFundCurrent.setRemark("ICO锻造铸币 原币金额:" + fundModel.getAmount() + ";奖励金额:" + fundModel.getAmountEx());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("ICO锻造铸币 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 修改钱包账户资产当前金额
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().subtract(fundModel.getAmount()).add(fundModel.getAmountEx()));
        logger.debug("ICO锻造铸币 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
    }
    
    /**
     * 注册邀请奖励
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void registAward(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            orgAmt = BigDecimal.ZERO;
            forzenOrgAmt = BigDecimal.ZERO;
            accountWalletAsset = this.constructAccountWalletAsset(fundModel);
            accountWalletAsset.setAmount(fundModel.getAmount());
            accountWalletAsset.setFrozenAmt(fundModel.getAmount());
            logger.debug("注册邀请奖励  钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            forzenOrgAmt = accountWalletAsset.getFrozenAmt();
            // 校验金额
            this.validateAmountIsSmallZero(accountWalletAsset.getAmount().add(fundModel.getAmount()));
            accountWalletAsset.setAmount(accountWalletAsset.getAmount().add(fundModel.getAmount()));
            // 校验金额
            this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
            accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
            logger.debug("注册邀请奖励  钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // step2 增加账户资金流水 增加资产流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增加
        accountFundCurrent.setRemark("注册邀请奖励BMS,奖励金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt);
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("注册邀请奖励  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加账户资金流水 增加冻结流水
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt.add(fundModel.getAmount()));
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN);// 冻结
        accountFundCurrent.setRemark("注册邀请奖励BMS,冻结金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt.add(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("注册邀请奖励  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 交易奖励
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author zhangchunxi  2018-03-13 15:34:25
     */
    private void tradeAward(FundModel fundModel, AccountWalletAsset accountWalletAsset, AccountWalletAsset adminWalletAsset) throws BusinessException
    {
        if (null == adminWalletAsset) { throw new BusinessException("admin's wallet asse not exist!"); }
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            orgAmt = BigDecimal.ZERO;
            forzenOrgAmt = BigDecimal.ZERO;
            accountWalletAsset = this.constructAccountWalletAsset(fundModel);
            accountWalletAsset.setRelatedStockinfoId(fundModel.getStockinfoId());// 避免用混
            accountWalletAsset.setAmount(fundModel.getAmount());
            accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
            logger.debug("交易奖励 钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            forzenOrgAmt = accountWalletAsset.getFrozenAmt();
            // 校验金额
            this.validateAmountIsSmallZero(accountWalletAsset.getAmount().add(fundModel.getAmount()));
            accountWalletAsset.setAmount(accountWalletAsset.getAmount().add(fundModel.getAmount()));
            logger.debug("交易奖励 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // step2 取剩余奖励(总)
        BigDecimal hasCandyAmt = adminWalletAsset.getAmount();
        // step 4 判断昨天的奖励是否已经奖励
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sp.format(d);
        String today = sp.format(new Date());
        AccountCandyRecord accountCandyRecord = accountCandyRecordService.findRecordByDateStrng(fundModel.getAccountId(), fundModel.getStockinfoId(), today);
        if (null != accountCandyRecord) { throw new BusinessException(yestoday + "交易奖励给账户：" + fundModel.getAccountId() + " 不可以重复奖励"); }
        // step5 插入奖励记录
        Long id = SerialnoUtils.buildPrimaryKey();
        AccountCandyRecord entity = new AccountCandyRecord();
        entity.setId(id);
        entity.setAccountId(fundModel.getAccountId());
        entity.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        entity.setAccountAssetId(accountWalletAsset.getId());
        entity.setCurrentDate(new Date());
        entity.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD);
        entity.setStockinfoId(accountWalletAsset.getStockinfoId());
        entity.setRelatedStockinfoId(accountWalletAsset.getRelatedStockinfoId());
        entity.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        entity.setOrgAmt(hasCandyAmt);
        entity.setOccurAmt(fundModel.getAmount());
        entity.setLastAmt(hasCandyAmt.subtract(entity.getOccurAmt()));
        if (entity.getLastAmt().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException("糖果数量不足，不能发放！" + fundModel.getStockinfoId()); }
        entity.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        entity.setRemark(yestoday + "交易奖励");
        accountCandyRecordService.insert(entity);
        // step6 增加账户资金流水 增加资产流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark(yestoday + "交易奖励增加金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt);
        accountFundCurrent.setOriginalBusinessId(id);
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("交易奖励 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 超级用户资产处理
        BigDecimal adminOrgAmt = adminWalletAsset.getAmount();
        adminWalletAsset.setAmount(adminWalletAsset.getAmount().subtract(fundModel.getAmount()));
        accountWalletAssetService.updateByPrimaryKey(adminWalletAsset);
        // 超级账户资产处理
        AccountFundCurrent adminFundCurrent = new AccountFundCurrent();
        adminFundCurrent.setAccountId(adminWalletAsset.getAccountId());
        adminFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        adminFundCurrent.setAccountAssetId(adminWalletAsset.getId());// 资产对应的id
        adminFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        adminFundCurrent.setBusinessFlag(fundModel.getBusinessFlag());
        adminFundCurrent.setStockinfoId(adminWalletAsset.getStockinfoId());
        adminFundCurrent.setContractAmt(BigDecimal.ZERO);
        adminFundCurrent.setOrgAmt(adminOrgAmt);
        adminFundCurrent.setOccurAmt(fundModel.getAmount());
        adminFundCurrent.setLastAmt(adminOrgAmt.subtract(fundModel.getAmount()));
        adminFundCurrent.setForzenOrgAmt(adminWalletAsset.getFrozenAmt());
        adminFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        adminFundCurrent.setForzenLastAmt(adminWalletAsset.getFrozenAmt());
        adminFundCurrent.setFee(BigDecimal.ZERO);
        adminFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        adminFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        adminFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        adminFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 资金减少
        adminFundCurrent.setRemark(yestoday + "糖果总额减少：" + fundModel.getAmount() + "账户：" + fundModel.getAccountId());
        adminFundCurrent.setOriginalBusinessId(adminWalletAsset.getId());
        adminFundCurrent.setRelatedStockinfoId(adminWalletAsset.getStockinfoId());
        adminFundCurrent.setTableName(getStockInfo(adminFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("当前账户糖果减少 准备插入数据为:" + adminFundCurrent.toString());
        accountFundCurrentService.insert(adminFundCurrent);
    }
    
    /**
     * bms交易奖励
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void bmsTradeAward(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        if (FundConsts.WALLET_BMS_TYPE.equals(fundModel.getStockinfoId()))
        {
            BigDecimal orgAmt = BigDecimal.ZERO;
            BigDecimal forzenOrgAmt = BigDecimal.ZERO;
            // step1 修改钱包账户资产当前金额
            if (null == accountWalletAsset)
            {
                orgAmt = BigDecimal.ZERO;
                forzenOrgAmt = BigDecimal.ZERO;
                accountWalletAsset = this.constructAccountWalletAsset(fundModel);
                accountWalletAsset.setAmount(fundModel.getAmount());
                accountWalletAsset.setFrozenAmt(fundModel.getAmount());
                logger.debug("交易奖励 钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
                accountWalletAssetService.insert(accountWalletAsset);
            }
            else
            {
                orgAmt = accountWalletAsset.getAmount();
                forzenOrgAmt = accountWalletAsset.getFrozenAmt();
                // 校验金额
                this.validateAmountIsSmallZero(accountWalletAsset.getAmount().add(fundModel.getAmount()));
                accountWalletAsset.setAmount(accountWalletAsset.getAmount().add(fundModel.getAmount()));
                // 校验金额
                this.validateAmountIsSmallZero(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
                accountWalletAsset.setFrozenAmt(accountWalletAsset.getFrozenAmt().add(fundModel.getAmount()));
                logger.debug("交易奖励 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
                accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
            }
            // step2 增加账户资金流水 增加资产流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
            fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
            accountFundCurrent.setRemark("交易奖励BMS,增加金额:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(forzenOrgAmt);
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("交易奖励 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            // step2 增加账户资金流水 增加冻结流水
            fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
            fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt.add(fundModel.getAmount()));
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(orgAmt.add(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN);// 冻结
            accountFundCurrent.setRemark("交易奖励BMS,冻结金额:" + fundModel.getAmount());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
            accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
            accountFundCurrent.setForzenLastAmt(forzenOrgAmt.add(fundModel.getAmount()));
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("交易奖励  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            if (!FundConsts.SYSTEM_ACCOUNT_ID.equals(accountWalletAsset.getAccountId()))
            {
                // 系统账户反方向变化 BMS减少
                AccountWalletAsset accountWalletAssetSystem = this.findAccountWalletAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, FundConsts.WALLET_BMS_TYPE);
                if (null == accountWalletAssetSystem)
                {
                    logger.debug("交易奖励  钱包账户资产 系统账户反方向变化 系统账户为空");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }
                else
                {
                    // step2 增加账户资金流水 增加资产流水
                    FundCurrentModel fundCurrentModelSystem = new FundCurrentModel();
                    fundCurrentModelSystem.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                    fundCurrentModelSystem.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
                    fundCurrentModelSystem.setAccountAssetId(accountWalletAssetSystem.getId());
                    fundCurrentModelSystem.setStockinfoId(fundModel.getStockinfoId());
                    fundCurrentModelSystem.setBusinessFlag(fundModel.getBusinessFlag());
                    fundCurrentModelSystem.setOrgAmt(accountWalletAssetSystem.getAmount());
                    fundCurrentModelSystem.setOccurAmt(fundModel.getAmount());
                    fundCurrentModelSystem.setFee(BigDecimal.ZERO);
                    AccountFundCurrent accountFundCurrentSystem = this.constructAccountFundCurrent(fundModel, fundCurrentModelSystem);
                    accountFundCurrentSystem.setLastAmt(accountWalletAssetSystem.getAmount().subtract(fundModel.getAmount()));
                    accountFundCurrentSystem.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
                    accountFundCurrentSystem.setRemark("交易奖励BMS, 系统账户反方向变化减少金额:" + fundModel.getAmount());
                    accountFundCurrentSystem.setContractAmt(BigDecimal.ZERO);
                    accountFundCurrentSystem.setForzenOrgAmt(accountWalletAssetSystem.getFrozenAmt());
                    accountFundCurrentSystem.setOccurForzenAmt(BigDecimal.ZERO);
                    accountFundCurrentSystem.setForzenLastAmt(accountWalletAssetSystem.getFrozenAmt());
                    accountFundCurrentSystem.setOriginalBusinessId(fundModel.getOriginalBusinessId());
                    accountFundCurrentSystem.setRelatedStockinfoId(fundModel.getStockinfoId());
                    accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                    logger.debug("交易奖励  账户资金交易流水 系统账户反方向变  准备插入数据为:" + accountFundCurrentSystem.toString());
                    accountFundCurrentService.insert(accountFundCurrentSystem);
                    // 校验金额
                    this.validateAmountIsSmallZero(accountWalletAssetSystem.getAmount().subtract(fundModel.getAmount()));
                    // step1 修改钱包账户资产当前金额
                    accountWalletAssetSystem.setAmount(accountWalletAssetSystem.getAmount().subtract(fundModel.getAmount()));
                    logger.debug("交易奖励  钱包账户资产 系统账户反方向变化 BMS减少 :" + accountWalletAsset.toString());
                    accountWalletAssetService.updateByPrimaryKey(accountWalletAssetSystem);
                }
            }
        }
    }
    
    /**
     * 撮合交易自动借款
     * @param fundModel
     * @param accountContractAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void assetDebitBorrow(FundModel fundModel, AccountContractAsset accountContractAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountContractAsset)
        {
            orgAmt = BigDecimal.ZERO;
            lastAmt = fundModel.getAmount();
            accountContractAsset = this.constructAccountContractAsset(fundModel);
            accountContractAsset.setAmount(lastAmt);
            accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
            logger.debug("自动借款合约账户资产增加 合约账户资产 准备插入数据为:" + accountContractAsset.toString());
            accountContractAssetService.insert(accountContractAsset);
        }
        else
        {
            orgAmt = accountContractAsset.getAmount();
            lastAmt = orgAmt.add(fundModel.getAmount());
            accountContractAsset.setAmount(lastAmt);
            accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
            logger.debug("自动借款合约账户资产增加 合约账户资产 准备修改数据为:" + accountContractAsset.toString());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("自动借款 合约账户资产增加:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("自动借款合约账户资产增加:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        AccountDebitAsset record = new AccountDebitAsset();
        record.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableDebitAsset());
        record.setStockinfoId(fundModel.getStockinfoId());
        record.setRelatedStockinfoId(getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
        record.setBorrowerAccountId(fundModel.getAccountId());
        List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
        record = list.get(0);
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_DEBIT);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setOrgAmt(record.getDebitAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setLastAmt(record.getDebitAmt());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("自动借款 合约账户借贷资产增加:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
        accountFundCurrent.setOriginalBusinessId(record.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("自动借款合约账户资产增加:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 撮合交易自动借款（钱包资产）
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zcx
     */
    private void assetDebitBorrow(FundModel fundModel, AccountSpotAsset accountSpotAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountSpotAsset)
        {
            orgAmt = BigDecimal.ZERO;
            lastAmt = fundModel.getAmount();
            accountSpotAsset = this.walletAccountWalletAsset(fundModel);
            accountSpotAsset.setAmount(lastAmt);
            accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountSpotAsset.setRelatedStockinfoId(getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
            logger.debug("自动借款钱包账户资产增加 合约账户资产 准备插入数据为:" + accountSpotAsset.toString());
            accountSpotAssetService.insert(accountSpotAsset);
        }
        else
        {
            orgAmt = accountSpotAsset.getAmount();
            lastAmt = orgAmt.add(fundModel.getAmount());
            accountSpotAsset.setAmount(lastAmt);
            accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            logger.debug("自动借款合约账户资产增加 钱包账户资产 准备修改数据为:" + accountSpotAsset.toString());
            accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("自动借款 钱包账户资产增加:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountSpotAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("自动借款钱包账户资产增加:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        AccountDebitAsset record = new AccountDebitAsset();
        record.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableDebitAsset());
        record.setStockinfoId(fundModel.getStockinfoId());
        record.setRelatedStockinfoId(getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
        record.setBorrowerAccountId(fundModel.getAccountId());
        List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
        record = list.get(0);
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT);
        fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setOrgAmt(record.getDebitAmt().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setLastAmt(record.getDebitAmt());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("自动借款 钱包账户借贷资产增加:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
        accountFundCurrent.setOriginalBusinessId(record.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("自动借款合约账户资产增加:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 撮合交易自动还款
     * @param fundModel
     * @param accountContractAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void assetDebitRepayment(FundModel fundModel, AccountContractAsset accountContractAsset) throws BusinessException
    {
        logger.debug(accountContractAsset.getAmount() + "=" + accountContractAsset.getFrozenAmt() + "=" + fundModel.getAmount());
        if (accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()).compareTo(fundModel.getAmount()) < 0)
        {
            logger.debug("账户资产不足，不能还款 ");
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        else
        {
            BigDecimal orgAmt = BigDecimal.ZERO;
            BigDecimal lastAmt = BigDecimal.ZERO;
            // step1 修改钱包账户资产当前金额
            orgAmt = accountContractAsset.getAmount();
            lastAmt = orgAmt.subtract(fundModel.getAmount());
            accountContractAsset.setAmount(lastAmt);
            accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
            logger.debug("自动还款 合约账户资产 准备修改数据为:" + accountContractAsset.toString());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset);
            // 校验金额
            this.validateAmountIsSmallZero(lastAmt);
            // step2 增加账户资金流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
            fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(lastAmt);
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
            accountFundCurrent.setRemark("自动还款金额:" + fundModel.getAmount());
            if (fundCurrentModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC_TYPE)
            {
                accountFundCurrent.setRemark("自动还款金额:" + fundModel.getAmount());
            }
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("自动还款合约账户资产减少:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_DEBIT);
            fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setOrgAmt(fundModel.getAmountEx());
            accountFundCurrent.setOccurAmt(fundModel.getAmount());
            accountFundCurrent.setLastAmt(fundModel.getAmountEx().subtract(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
            accountFundCurrent.setRemark("自动还款 借贷资产减少:" + fundModel.getAmount());
            if (fundCurrentModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC_TYPE)
            {
                accountFundCurrent.setRemark("自动还款 借贷资产减少:" + fundModel.getAmount());
            }
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("自动还款合约借贷账户资产减少:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
        }
    }
    
    /**
     * 撮合交易自动还款
     * @param fundModel
     * @param accountSpotAsset
     * @throws BusinessException
     * @author zcx  2017-12-19
     */
    private void assetDebitRepayment(FundModel fundModel, AccountSpotAsset accountSpotAsset) throws BusinessException
    {
        logger.debug(accountSpotAsset.getAmount() + "=" + accountSpotAsset.getFrozenAmt() + "=" + fundModel.getAmount());
        if (accountSpotAsset.getAmount().subtract(accountSpotAsset.getFrozenAmt()).compareTo(fundModel.getAmount()) < 0)
        {
            logger.debug("账户资产不足，不能还款 ");
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        else
        {
            BigDecimal orgAmt = BigDecimal.ZERO;
            BigDecimal lastAmt = BigDecimal.ZERO;
            // step1 修改钱包账户资产当前金额
            orgAmt = accountSpotAsset.getAmount();
            lastAmt = orgAmt.subtract(fundModel.getAmount());
            accountSpotAsset.setAmount(lastAmt);
            accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            logger.debug("自动还款 合约账户资产 准备修改数据为:" + accountSpotAsset.toString());
            accountSpotAssetService.updateByPrimaryKey(accountSpotAsset);
            // 校验金额
            this.validateAmountIsSmallZero(lastAmt);
            // step2 增加账户资金流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
            fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setLastAmt(lastAmt);
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
            accountFundCurrent.setRemark("自动还款金额:" + fundModel.getAmount());
            if (fundCurrentModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC_TYPE)
            {
                accountFundCurrent.setRemark("自动还款金额:" + fundModel.getAmount());
            }
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(accountSpotAsset.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(accountSpotAsset.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(accountSpotAsset.getId());
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("自动还款合约账户资产减少:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT);
            fundCurrentModel.setAccountAssetId(accountSpotAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setOrgAmt(fundModel.getAmountEx());
            accountFundCurrent.setOccurAmt(fundModel.getAmount());
            accountFundCurrent.setLastAmt(fundModel.getAmountEx().subtract(fundModel.getAmount()));
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
            accountFundCurrent.setRemark("自动还款 借贷资产减少:" + fundModel.getAmount());
            if (fundCurrentModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC_TYPE)
            {
                accountFundCurrent.setRemark("自动还款 借贷资产减少:" + fundModel.getAmount());
            }
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
            accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("自动还款钱包账户借贷资产减少:  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
        }
    }
    
    /**
     * 余额数量平台调增
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void assetAdjustAdd(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            orgAmt = BigDecimal.ZERO;
            lastAmt = fundModel.getAmount();
            accountWalletAsset = this.constructAccountWalletAsset(fundModel);
            accountWalletAsset.setAmount(lastAmt);
            accountWalletAsset.setRelatedStockinfoId(fundModel.getStockinfoId());
            logger.debug("余额数量平台调增 钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            lastAmt = orgAmt.add(fundModel.getAmount());
            accountWalletAsset.setAmount(lastAmt);
            logger.debug("余额数量平台调增  钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("余额数量平台调增金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("余额数量平台调增  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 系统账户反方向变化 BMS减少
        if (FundConsts.WALLET_BMS_TYPE.equals(fundModel.getStockinfoId()))
        {
            if (!FundConsts.SYSTEM_ACCOUNT_ID.equals(accountWalletAsset.getAccountId()))
            {
                AccountWalletAsset accountWalletAssetSystem = this.findAccountWalletAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, FundConsts.WALLET_BMS_TYPE);
                if (null == accountWalletAssetSystem)
                {
                    logger.debug("余额数量平台调增  钱包账户资产 系统账户反方向变化 系统账户为空");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }
                else
                {
                    // step2 增加账户资金流水 增加资产流水
                    FundCurrentModel fundCurrentModelSystem = new FundCurrentModel();
                    fundCurrentModelSystem.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                    fundCurrentModelSystem.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
                    fundCurrentModelSystem.setAccountAssetId(accountWalletAssetSystem.getId());
                    fundCurrentModelSystem.setStockinfoId(fundModel.getStockinfoId());
                    fundCurrentModelSystem.setBusinessFlag(fundModel.getBusinessFlag());
                    fundCurrentModelSystem.setOrgAmt(accountWalletAssetSystem.getAmount());
                    fundCurrentModelSystem.setOccurAmt(fundModel.getAmount());
                    fundCurrentModelSystem.setFee(BigDecimal.ZERO);
                    AccountFundCurrent accountFundCurrentSystem = this.constructAccountFundCurrent(fundModel, fundCurrentModelSystem);
                    accountFundCurrentSystem.setLastAmt(accountWalletAssetSystem.getAmount().subtract(fundModel.getAmount()));
                    accountFundCurrentSystem.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
                    accountFundCurrentSystem.setRemark("余额数量平台调增BMS,系统账户反方向变化减少金额:" + fundModel.getAmount());
                    accountFundCurrentSystem.setContractAmt(BigDecimal.ZERO);
                    accountFundCurrentSystem.setForzenOrgAmt(accountWalletAssetSystem.getFrozenAmt());
                    accountFundCurrentSystem.setOccurForzenAmt(BigDecimal.ZERO);
                    accountFundCurrentSystem.setForzenLastAmt(accountWalletAssetSystem.getFrozenAmt());
                    accountFundCurrentSystem.setOriginalBusinessId(fundModel.getOriginalBusinessId());
                    accountFundCurrentSystem.setRelatedStockinfoId(fundModel.getStockinfoId());
                    accountFundCurrentSystem.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                    logger.debug("余额数量平台调增  账户资金交易流水 系统账户反方向变  准备插入数据为:" + accountFundCurrentSystem.toString());
                    accountFundCurrentService.insert(accountFundCurrentSystem);
                    // 校验金额
                    this.validateAmountIsSmallZero(accountWalletAssetSystem.getAmount().subtract(fundModel.getAmount()));
                    // step1 修改钱包账户资产当前金额
                    accountWalletAssetSystem.setAmount(accountWalletAssetSystem.getAmount().subtract(fundModel.getAmount()));
                    logger.debug("余额数量平台调增  钱包账户资产 系统账户反方向变化 BMS减少 :" + accountWalletAssetSystem.toString());
                    accountWalletAssetService.updateByPrimaryKey(accountWalletAssetSystem);
                }
            }
        }
    }
    
    /**
     * 余额数量平台调减
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void assetAdjustSub(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            orgAmt = BigDecimal.ZERO;
            lastAmt = fundModel.getAmount();
            accountWalletAsset = this.constructAccountWalletAsset(fundModel);
            accountWalletAsset.setAmount(lastAmt);
            accountWalletAsset.setRelatedStockinfoId(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getCapitalStockinfoId());
            logger.debug("余额数量平台调减 钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            lastAmt = orgAmt.subtract(fundModel.getAmount());
            accountWalletAsset.setAmount(lastAmt);
            logger.debug("余额数量平台调减 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
        accountFundCurrent.setRemark("余额数量平台调减金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("余额数量平台调减  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 系统账户反方向变化 BMS增加
        if (FundConsts.WALLET_BMS_TYPE.equals(fundModel.getStockinfoId()))
        {
            if (!FundConsts.SYSTEM_ACCOUNT_ID.equals(accountWalletAsset.getAccountId()))
            {
                AccountWalletAsset accountWalletAssetSystem = this.findAccountWalletAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, FundConsts.WALLET_BMS_TYPE);
                if (null == accountWalletAssetSystem)
                {
                    logger.debug("余额数量平台调减  钱包账户资产 系统账户反方向变化 系统账户为空");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }
                else
                {
                    // step2 增加账户资金流水 增加资产流水
                    FundCurrentModel fundCurrentModelSystem = new FundCurrentModel();
                    fundCurrentModelSystem.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                    fundCurrentModelSystem.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
                    fundCurrentModelSystem.setAccountAssetId(accountWalletAssetSystem.getId());
                    fundCurrentModelSystem.setStockinfoId(fundModel.getStockinfoId());
                    fundCurrentModelSystem.setBusinessFlag(fundModel.getBusinessFlag());
                    fundCurrentModelSystem.setOrgAmt(accountWalletAssetSystem.getAmount());
                    fundCurrentModelSystem.setOccurAmt(fundModel.getAmount());
                    fundCurrentModelSystem.setFee(BigDecimal.ZERO);
                    AccountFundCurrent accountFundCurrentSystem = this.constructAccountFundCurrent(fundModel, fundCurrentModelSystem);
                    accountFundCurrentSystem.setLastAmt(accountWalletAssetSystem.getAmount().add(fundModel.getAmount()));
                    accountFundCurrentSystem.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
                    accountFundCurrentSystem.setRemark("余额数量平台调减BMS,系统账户反方向变化增加金额:" + fundModel.getAmount());
                    accountFundCurrentSystem.setContractAmt(BigDecimal.ZERO);
                    accountFundCurrentSystem.setForzenOrgAmt(accountWalletAssetSystem.getFrozenAmt());
                    accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
                    accountFundCurrentSystem.setForzenLastAmt(accountWalletAssetSystem.getFrozenAmt());
                    accountFundCurrentSystem.setOriginalBusinessId(fundModel.getOriginalBusinessId());
                    accountFundCurrentSystem.setRelatedStockinfoId(fundModel.getStockinfoId());
                    accountFundCurrentSystem.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                    logger.debug("余额数量平台调减  账户资金交易流水 系统账户反方向变  准备插入数据为:" + accountFundCurrentSystem.toString());
                    accountFundCurrentService.insert(accountFundCurrentSystem);
                    // 校验金额
                    this.validateAmountIsSmallZero(accountWalletAssetSystem.getAmount().add(fundModel.getAmount()));
                    // step1 修改钱包账户资产当前金额
                    accountWalletAssetSystem.setAmount(accountWalletAssetSystem.getAmount().add(fundModel.getAmount()));
                    logger.debug("余额数量平台调减  钱包账户资产 系统账户反方向变化 BMS增加 :" + accountWalletAsset.toString());
                    accountWalletAssetService.updateByPrimaryKey(accountWalletAssetSystem);
                }
            }
        }
    }
    
    /**
     * 冻结数量平台调增
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void forzenAssetAdjustAdd(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            orgAmt = BigDecimal.ZERO;
            forzenOrgAmt = BigDecimal.ZERO;
            accountWalletAsset = this.constructAccountWalletAsset(fundModel);
            accountWalletAsset.setAmount(BigDecimal.ZERO);
            accountWalletAsset.setFrozenAmt(fundModel.getAmount());
            accountWalletAsset.setRelatedStockinfoId(fundModel.getStockinfoId());
            logger.debug("冻结数量平台调增 钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            forzenOrgAmt = accountWalletAsset.getFrozenAmt();
            accountWalletAsset.setFrozenAmt(forzenOrgAmt.add(fundModel.getAmount()));
            logger.debug("冻结数量平台调增  钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(forzenOrgAmt.add(fundModel.getAmount()));
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_FROZEN); // 冻结
        accountFundCurrent.setRemark("冻结数量平台调增金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt.add(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("冻结数量平台调增  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * ERC20、ETH 等 小额充值手续费
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author ZHANGCHUNXI  2018-03-06
     */
    private void assetERC20Recharge(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            orgAmt = BigDecimal.ZERO;
            lastAmt = fundModel.getAmount();
            accountWalletAsset = this.constructAccountWalletAsset(fundModel);
            accountWalletAsset.setAmount(lastAmt);
            accountWalletAsset.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
            logger.debug("小额充值手续费 钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            lastAmt = orgAmt.subtract(fundModel.getAmount());
            accountWalletAsset.setAmount(lastAmt);
            logger.debug("小额充值手续费 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setTransId(fundModel.getTransId());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
        accountFundCurrent.setRemark("小额充值手续费:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("小额充值手续费  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 把扣掉的手续费给超级用户
        AccountWalletAsset accountWalletAsset95 = null;
        try
        {
            accountWalletAsset95 = accountWalletAssetService.selectForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID, fundModel.getStockinfoId());
        }
        catch (Exception e)
        {
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        if (null == accountWalletAsset95) { throw new BusinessException("super admin asset doesn't exist"); }
        FundModel feeFundModel = new FundModel();
        feeFundModel.setAmount(fundModel.getAmount());
        feeFundModel.setStockinfoId(fundModel.getStockinfoId());
        feeFundModel.setFee(fundModel.getAmount());
        feeFundModel.setBusinessFlag("doERC20Recharge");
        feeFundModel.setAddress(fundModel.getOriginalBusinessId().toString());
        feeFundModel.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        AccountWalletAsset adminWalletAsset = new AccountWalletAsset();
        adminWalletAsset.setStockinfoId(fundModel.getStockinfoId());
        adminWalletAsset.setRelatedStockinfoId(fundModel.getStockinfoId());
        this.superAdminNetFee(feeFundModel, adminWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
    }
    
    /**
     * 冻结数量平台调减
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void forzenAssetAdjustSub(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            logger.debug("冻结数量平台调减 钱包账户资产 为空，请检查！");
            throw new BusinessException(CommonEnums.FAIL);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            forzenOrgAmt = accountWalletAsset.getFrozenAmt();
            accountWalletAsset.setFrozenAmt(forzenOrgAmt.subtract(fundModel.getAmount()));
            logger.debug("冻结数量平台调减 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(forzenOrgAmt.subtract(fundModel.getAmount()));
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN); // 解冻
        accountFundCurrent.setRemark("冻结数量平台调减金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt.subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("冻结数量平台调减  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 钱包资产解冻
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void assetUnFrozen(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal forzenOrgAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        orgAmt = accountWalletAsset.getAmount();
        forzenOrgAmt = accountWalletAsset.getFrozenAmt();
        // 校验金额
        this.validateAmountIsSmallZero(forzenOrgAmt.subtract(fundModel.getAmount()));
        // 校验金额
        accountWalletAsset.setFrozenAmt(forzenOrgAmt.subtract(fundModel.getAmount()));
        logger.debug("钱包资产解冻 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(orgAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_UNFROZEN);// 解冻
        accountFundCurrent.setRemark("钱包资产解冻金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(forzenOrgAmt);
        accountFundCurrent.setOccurForzenAmt(fundModel.getAmount());
        accountFundCurrent.setForzenLastAmt(forzenOrgAmt.subtract(fundModel.getAmount()));
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("钱包资产解冻 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 钱包账户充值
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:30:55
     */
    private void walletAssetRecharge(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        // step1 修改钱包账户资产当前金额
        if (null == accountWalletAsset)
        {
            orgAmt = BigDecimal.ZERO;
            lastAmt = fundModel.getAmount();
            accountWalletAsset = this.constructAccountWalletAsset(fundModel);
            accountWalletAsset.setAmount(lastAmt);
            accountWalletAsset.setRelatedStockinfoId(getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getCapitalStockinfoId());
            logger.debug("钱包账户充值 钱包账户资产 准备插入数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            orgAmt = accountWalletAsset.getAmount();
            lastAmt = orgAmt.add(fundModel.getAmount());
            accountWalletAsset.setAmount(lastAmt);
            accountWalletAsset.setChargedTotal(accountWalletAsset.getChargedTotal().add(fundModel.getAmount()));
            logger.debug("钱包账户充值 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        }
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setTransId(fundModel.getTransId());
        accountFundCurrent.setChargeAddr(fundModel.getAddress());
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark("钱包账户充值：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        // accountFundCurrent.setNetFee(fundModel.getFee());
        accountFundCurrent.setNetFee(BigDecimal.ZERO);
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("钱包账户充值 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 清除账户资产缓存
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> stockInfolist = stockInfoService.findList(stockInfoSelect);
        for (StockInfo stockInfo : stockInfolist)
        {
            this.setAccountAssetCache(fundModel.getAccountId(), stockInfo.getId());
        }
    }
    
    /**
     *  钱包账户提现
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:33:09
     */
    private FundModel walletAssetWithdraw(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        orgAmt = accountWalletAsset.getAmount();
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel.setRelatedStockinfoId(accountWalletAsset.getRelatedStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        String transId = fundModel.getTransId();
        // case1 钱包账户提现 界面端发起提现申请
        if (null == transId)
        {
            if (StringUtils.isBlank(fundModel.getAddress())) { throw new BusinessException("withdraw addr is blank"); }
            // 减少钱包账户当前数量
            lastAmt = accountWalletAsset.getAmount().subtract(fundModel.getAmount().add(fundModel.getFee()));
            // 校验金额
            this.validateAmountIsSmallZero(lastAmt);
            // 校验费用
            this.validateFee(fundModel.getFee());
            accountWalletAsset.setAmount(lastAmt);
            accountWalletAsset.setWithdrawingTotal(accountWalletAsset.getWithdrawingTotal().add(fundModel.getAmount()));
            logger.debug("钱包账户提现 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
            // 增加账户资金流水
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(fundModel.getAccountId());
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
            fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
            fundCurrentModel.setOrgAmt(orgAmt);
            fundCurrentModel.setOccurAmt(fundModel.getAmount().add(fundModel.getFee()));
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
            accountFundCurrent.setLastAmt(lastAmt);
            accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
            accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);// 待审核
            accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_UNTRANSFER);// 未划拨
            accountFundCurrent.setRemark("钱包账户提现申请:" + fundModel.getAmount() + ";手续费金额:" + fundModel.getFee() + ";减少金额:" + fundModel.getAmount().add(fundModel.getFee()));
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
            accountFundCurrent.setNetFee(fundModel.getFee());
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            logger.debug("钱包账户提现申请  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
            Long id = SerialnoUtils.buildPrimaryKey();
            accountFundCurrent.setId(id);
            accountFundCurrentService.insert(accountFundCurrent);
            logger.debug("钱包账户提现申请记录处理开始");
            // 其它币种(现金，ERC20TOKEN已在方法外处理)
            if (fundModel.getStockinfoId().longValue() == FundConsts.WALLET_BTC_TYPE)
            {
                AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
                BeanUtils.copyProperties(accountFundCurrent, accountWithdrawRecord);
                accountWithdrawRecord.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_NO);
                accountWithdrawRecord.setCreateBy(fundModel.getAccountId());
                accountWithdrawRecord.setCreateDate(new Timestamp(System.currentTimeMillis()));
                accountWithdrawRecord.setId(id);
                accountWithdrawRecordService.insert(accountWithdrawRecord);
            }
            logger.debug("钱包账户提现申请记录处理结束");
            // 钱包账户提现 返回账户资金流水唯一id给前端
            fundModel.setAccountFundCurrentId(accountFundCurrent.getId());
            // 超级用户网络手续费处理
            logger.debug("钱包账户提现超级用户网络手续费处理开始");
            superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
            logger.debug("钱包账户提现超级用户网络手续费处理结束");
        } // case2 钱包账户提现 三方接口区块交易确认接口异步回调 对账户资金流水进行修改
        else
        {
            AccountFundCurrent whereAccountFundCurrent = new AccountFundCurrent();
            whereAccountFundCurrent.setStockinfoId(fundModel.getStockinfoId());
            whereAccountFundCurrent.setTransId(fundModel.getTransId());
            whereAccountFundCurrent.setWithdrawAddr(fundModel.getAddress());
            whereAccountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
            whereAccountFundCurrent = accountFundCurrentService.findAccountFundCurrent(whereAccountFundCurrent);
            if (null != whereAccountFundCurrent)
            {
                // 修改原提现账户资金流水
                whereAccountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                whereAccountFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoId()).getTableFundCurrent());
                accountFundCurrentService.updateByPrimaryKey(whereAccountFundCurrent);
            }
        }
        fundModel.setOriginalBusinessId(accountWalletAsset.getId());
        return fundModel;
    }
    
    /**
     * 网络手续费处理
     * @param fundModel
     * @param accountWalletAsset
     * @param direct
     */
    @Override
    public void superAdminNetFee(FundModel fundModel, AccountWalletAsset accountWalletAsset, String direct) throws BusinessException
    {
        AccountWalletAsset entity = new AccountWalletAsset();
        entity.setStockinfoId(accountWalletAsset.getStockinfoId());
        entity.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID);
        List<AccountWalletAsset> list = accountWalletAssetService.findList(entity);
        if (list.size() > 0)
        {
            AccountWalletAsset superAccountWalletAsset = accountWalletAssetService.selectForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID,
                    accountWalletAsset.getStockinfoId());
            FundCurrentModel fundCurrentModel = new FundCurrentModel();
            fundCurrentModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID);
            fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
            fundCurrentModel.setAccountAssetId(superAccountWalletAsset.getId());
            fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
            fundCurrentModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_WALLET_WITHDRAW_NETFEE_CHANGE);
            fundCurrentModel.setOrgAmt(superAccountWalletAsset.getAmount());
            fundCurrentModel.setOccurAmt(fundModel.getFee());
            fundCurrentModel.setFee(BigDecimal.ZERO);
            AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
            accountFundCurrent.setAccountId(superAccountWalletAsset.getAccountId());
            if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
            {
                BigDecimal lastAmt = superAccountWalletAsset.getAmount().add(fundModel.getFee());
                // 校验金额
                this.validateAmountIsSmallZero(lastAmt);
                accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
                accountFundCurrent.setOccurAmt(fundModel.getFee());
                accountFundCurrent.setLastAmt(lastAmt);
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增加
            }
            else if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
            {
                BigDecimal lastAmt = superAccountWalletAsset.getAmount().subtract(fundModel.getFee());
                // 校验金额
                this.validateAmountIsSmallZero(lastAmt);
                accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
                accountFundCurrent.setOccurAmt(fundModel.getFee());
                accountFundCurrent.setLastAmt(lastAmt);
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 增加
            }
            else
            {
                throw new BusinessException("assetDirect error");
            }
            accountFundCurrent.setRemark(fundModel.getBusinessFlag());
            accountFundCurrent.setContractAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenOrgAmt(superAccountWalletAsset.getFrozenAmt());
            accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
            accountFundCurrent.setForzenLastAmt(superAccountWalletAsset.getFrozenAmt());
            accountFundCurrent.setOriginalBusinessId(superAccountWalletAsset.getId());
            accountFundCurrent.setNetFee(BigDecimal.ZERO);
            accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
            accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
            if (fundModel.getAddress() != null)
            {
                accountFundCurrent.setRemark("充值区块ID：" + fundModel.getAddress());
            }
            logger.debug("钱包账户提现 网络手续费调整 准备插入数据为:" + accountFundCurrent.toString());
            accountFundCurrentService.insert(accountFundCurrent);
            // 增加钱包账户资产数量
            if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
            {
                superAccountWalletAsset.setAmount(superAccountWalletAsset.getAmount().add(fundModel.getFee()));
            }
            else if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
            {
                superAccountWalletAsset.setAmount(superAccountWalletAsset.getAmount().subtract(fundModel.getFee()));
            }
            else
            {
                throw new BusinessException("assetDirect error");
            }
            logger.debug("钱包账户提现 网络手续费调整 准备修改数据为:" + superAccountWalletAsset.toString());
            accountWalletAssetService.updateByPrimaryKey(superAccountWalletAsset);
        }
        else
        {
            throw new BusinessException("super admin asset doesn't exist");
        }
    }
    
    /**
     * 理财利息和借款利息处理
     * @param fundModel
     * @param direct
     */
    @Override
    public void doSuperAdminDebitInterest(FundModel fundModel, String direct) throws BusinessException
    {
        if (StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(), FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_INTEREST))
        {
            AccountSpotAsset entity = new AccountSpotAsset();
            entity.setRelatedStockinfoId(getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
            entity.setStockinfoId(fundModel.getStockinfoId());
            entity.setAccountId(fundModel.getAccountId());
            List<AccountSpotAsset> list = accountSpotAssetService.findList(entity);
            if (list.size() > 0)
            {
                AccountSpotAsset superAccountSpotAsset = accountSpotAssetService.selectForUpdate(fundModel.getAccountId(), fundModel.getStockinfoId(),
                        getStockInfo(fundModel.getStockinfoIdEx()).getCapitalStockinfoId());
                FundCurrentModel fundCurrentModel = new FundCurrentModel();
                fundCurrentModel.setAccountId(fundModel.getAccountId());
                fundCurrentModel.setAccountAssetId(superAccountSpotAsset.getId());
                fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
                fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
                fundCurrentModel.setOrgAmt(superAccountSpotAsset.getAmount());
                fundCurrentModel.setOccurAmt(fundModel.getFee());
                fundCurrentModel.setFee(BigDecimal.ZERO);
                AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
                accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
                accountFundCurrent.setAccountId(superAccountSpotAsset.getAccountId());
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
                {
                    BigDecimal lastAmt = superAccountSpotAsset.getAmount().add(fundModel.getAmount());
                    // 校验金额
                    this.validateAmountIsSmallZero(lastAmt);
                    accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
                    accountFundCurrent.setOccurAmt(fundModel.getAmount());
                    accountFundCurrent.setLastAmt(lastAmt);
                    accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增加
                }
                else if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
                {
                    // BigDecimal lastAmt = superAccountSpotAsset.getAmount().subtract(fundModel.getAmount());
                    // // 校验金额
                    // this.validateAmountIsSmallZero(lastAmt);
                    // accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
                    // accountFundCurrent.setOccurAmt(fundModel.getAmount());
                    // accountFundCurrent.setLastAmt(lastAmt);
                    // accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 增加
                    throw new BusinessException("assetDirect error");
                }
                else
                {
                    throw new BusinessException("assetDirect error");
                }
                accountFundCurrent.setRemark(fundModel.getBusinessFlag());
                accountFundCurrent.setContractAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
                accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
                accountFundCurrent.setFee(BigDecimal.ZERO);
                accountFundCurrent.setOriginalBusinessId(superAccountSpotAsset.getId());
                accountFundCurrent.setNetFee(BigDecimal.ZERO);
                accountFundCurrent.setRemark("普通账户：" + fundModel.getCreateBy() + " 业务ID：" + fundModel.getOriginalBusinessId());
                logger.debug("钱包账户提现 理财或借款利息 准备插入数据为:" + accountFundCurrent.toString());
                accountFundCurrentService.insert(accountFundCurrent);
                // 增加钱包账户资产数量
                if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
                {
                    superAccountSpotAsset.setAmount(superAccountSpotAsset.getAmount().add(fundModel.getAmount()));
                }
                else if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
                {
                    superAccountSpotAsset.setAmount(superAccountSpotAsset.getAmount().subtract(fundModel.getAmount()));
                }
                else
                {
                    throw new BusinessException("assetDirect error");
                }
                logger.debug("钱包账户 理财或借款利息 准备修改数据为:" + superAccountSpotAsset.toString());
                accountSpotAssetService.updateByPrimaryKey(superAccountSpotAsset);
            }
            else
            {
                throw new BusinessException("super admin asset doesn't exist");
            }
        }
        else if (StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(), FundConsts.SYSTEM_BUSSINESS_FLAG_AUTO_WEALTH_INTEREST))
        {
            AccountWealthDebitAsset entity = new AccountWealthDebitAsset();
            entity.setRelatedStockinfoId(fundModel.getStockinfoId());
            entity.setStockinfoId(fundModel.getStockinfoId());
            entity.setWealthAccountId(fundModel.getAccountId());
            List<AccountWealthDebitAsset> list = accountWealthDebitAssetService.findList(entity);
            if (list.size() > 0)
            {
                AccountWealthDebitAsset accountWealthDebitAsset = accountWealthDebitAssetService.selectByPrimaryKeyForUpdate(list.get(0).getId());
                FundCurrentModel fundCurrentModel = new FundCurrentModel();
                fundCurrentModel.setAccountId(fundModel.getAccountId());
                fundCurrentModel.setAccountAssetId(accountWealthDebitAsset.getId());
                fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
                fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
                fundCurrentModel.setOrgAmt(accountWealthDebitAsset.getDebitAmt());
                fundCurrentModel.setOccurAmt(fundModel.getFee());
                fundCurrentModel.setFee(BigDecimal.ZERO);
                AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
                accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
                accountFundCurrent.setAccountId(accountWealthDebitAsset.getWealthAccountId());
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WEALTH_DEBIT);
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableWealthCurrent());
                if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
                {
                    BigDecimal lastAmt = accountWealthDebitAsset.getDebitAmt().add(fundModel.getAmount());
                    // 校验金额
                    this.validateAmountIsSmallZero(lastAmt);
                    accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
                    accountFundCurrent.setOccurAmt(fundModel.getAmount());
                    accountFundCurrent.setLastAmt(lastAmt);
                    accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增加
                }
                else if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
                {
                    // BigDecimal lastAmt = accountWealthDebitAsset.getDebitAmt().subtract(fundModel.getAmount());
                    // // 校验金额
                    // this.validateAmountIsSmallZero(lastAmt);
                    // accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
                    // accountFundCurrent.setOccurAmt(fundModel.getAmount());
                    // accountFundCurrent.setLastAmt(lastAmt);
                    // accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 增加
                    throw new BusinessException("assetDirect error");
                }
                else
                {
                    throw new BusinessException("assetDirect error");
                }
                accountFundCurrent.setRemark(fundModel.getBusinessFlag());
                accountFundCurrent.setContractAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
                accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
                accountFundCurrent.setFee(BigDecimal.ZERO);
                accountFundCurrent.setOriginalBusinessId(accountWealthDebitAsset.getId());
                accountFundCurrent.setNetFee(BigDecimal.ZERO);
                accountFundCurrent.setRemark("普通账户：" + fundModel.getCreateBy() + " 业务ID：" + fundModel.getOriginalBusinessId());
                logger.debug("钱包账户提现 理财或借款利息 准备插入数据为:" + accountFundCurrent.toString());
                accountFundCurrentService.insert(accountFundCurrent);
                // 增加钱包账户资产数量
                if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
                {
                    accountWealthDebitAsset.setDebitAmt(accountWealthDebitAsset.getDebitAmt().add(fundModel.getAmount()));
                }
                else if (StringUtils.equalsIgnoreCase(direct, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
                {
                    accountWealthDebitAsset.setDebitAmt(accountWealthDebitAsset.getDebitAmt().subtract(fundModel.getAmount()));
                }
                else
                {
                    throw new BusinessException("assetDirect error");
                }
                logger.debug("钱包账户 理财或借款利息 准备修改数据为:" + accountWealthDebitAsset.toString());
                accountWealthDebitAssetService.updateByPrimaryKey(accountWealthDebitAsset);
            }
            else
            {
                throw new BusinessException("super admin asset doesn't exist");
            }
        }
        else
        {
            throw new BusinessException("BusinessFlag Error");
        }
    }
    
    /**
     *  钱包账户提现取消
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:33:09
     */
    private void walletAssetWithdrawCancel(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        orgAmt = accountWalletAsset.getAmount();
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount().setScale(12, BigDecimal.ROUND_HALF_UP).add(fundModel.getFee()));
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        lastAmt = orgAmt.add(fundModel.getAmount()).add(fundModel.getFee());
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增加
        accountFundCurrent.setRemark("钱包账户提现取消:" + fundModel.getAmount() + ";手续费金额:" + fundModel.getFee() + ";增加金额:"
                + fundModel.getAmount().setScale(12, BigDecimal.ROUND_HALF_UP).add(fundModel.getFee()));
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
        accountFundCurrent.setNetFee(fundModel.getFee());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("钱包账户提现取消  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 增加钱包账户资产数量
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().add(fundModel.getAmount().add(fundModel.getFee())));
        accountWalletAsset.setWithdrawingTotal(accountWalletAsset.getWithdrawingTotal().subtract(fundModel.getAmount()));
        logger.debug("钱包账户提现取消  钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        logger.debug("钱包账户提现取消  superAdminNetFee处理");
        superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
    }
    
    /**
     *  钱包账户提现拒绝（包括审核拒绝和复核拒绝）
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:33:09
     */
    private void walletAssetWithdrawReject(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        BigDecimal lastAmt = BigDecimal.ZERO;
        orgAmt = accountWalletAsset.getAmount();
        // step1 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount().setScale(12, BigDecimal.ROUND_HALF_UP).add(fundModel.getFee()));
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        lastAmt = orgAmt.add(fundModel.getAmount()).add(fundModel.getFee());
        // 校验金额
        this.validateAmountIsSmallZero(lastAmt);
        accountFundCurrent.setWithdrawAddr(fundModel.getAddress());
        accountFundCurrent.setLastAmt(lastAmt);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);// 增加
        accountFundCurrent.setRemark("钱包账户提现拒绝:" + fundModel.getAmount() + ";手续费金额:" + fundModel.getFee() + ";增加金额:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
        accountFundCurrent.setNetFee(fundModel.getFee());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("钱包账户提现拒绝  账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // 增加钱包账户资产数量
        accountWalletAsset.setAmount(accountWalletAsset.getAmount().add(fundModel.getAmount().add(fundModel.getFee())));
        accountWalletAsset.setWithdrawingTotal(accountWalletAsset.getWithdrawingTotal().subtract(fundModel.getAmount()));
        logger.debug("钱包账户提现拒绝  钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        logger.debug("钱包账户提现拒绝  superAdminNetFee处理");
        superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
    }
    
    /**
     * 钱包帐户转合约帐户
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:37:16
     */
    private void wallet2Contract(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // 行所
        try
        {
            accountContractAssetService.selectByPrimaryKeyOnRowLock(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx(),
                    getStockInfo(fundModel.getStockinfoIdEx()).getTableAsset());
        }
        catch (Exception e)
        {
            logger.debug(e.getStackTrace().toString());
            throw new BusinessException(CommonEnums.ERROR_SYS_BUSY);
        }
        BigDecimal orgAmt = BigDecimal.ZERO;
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // step1 钱包账户资产处理
        Long transId = SerialnoUtils.buildPrimaryKey();
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        orgAmt = accountWalletAsset.getAmount();
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        if (fundModel.getAmount().compareTo(accountWalletAsset.getAmount()
                .subtract(accountWalletAsset.getFrozenAmt())) > 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        // 修改钱包账户资产当前金额
        accountWalletAsset.setAmount(orgAmt.subtract(fundModel.getAmount()));
        logger.debug("钱包帐户转合约帐户 钱包账户资产 准备修改数据为:" + accountFundCurrent.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        // 插入账户资金流水
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountFundCurrent.setLastAmt(accountWalletAsset.getAmount());
        accountFundCurrent.setRemark("钱包帐户转合约帐户转出：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setTransId(transId.toString());
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("钱包帐户转合约帐户 钱包账户资产 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 合约账户资产处理
        BigDecimal orgAmtEx = BigDecimal.ZERO;
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(fundModel.getAccountId());
        accountContractAsset.setStockinfoId(fundModel.getStockinfoId());
        accountContractAsset.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractAsset.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableAsset());
        accountContractAsset = accountContractAssetService.findAccountContractAsset(accountContractAsset);
        if (null == accountContractAsset)
        {
            orgAmtEx = BigDecimal.ZERO;
            accountContractAsset = this.constructAccountContractAsset(fundModel);
            accountContractAsset.setAmount(fundModel.getAmount());
            accountContractAsset.setFlowInAmt(fundModel.getAmount());
            accountContractAsset.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableAsset());
            logger.debug("钱包帐户转合约帐户 合约账户资产 准备插入数据为:" + accountFundCurrent.toString());
            accountContractAssetService.insert(accountContractAsset);
        }
        else
        {
            // 修改合约账户资产当前金额
            orgAmtEx = accountContractAsset.getAmount();
            accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset.setAmount(orgAmtEx.add(fundModel.getAmount()));
            accountContractAsset.setFlowInAmt(accountContractAsset.getFlowInAmt().add(fundModel.getAmount()));
            accountContractAsset.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableAsset());
            logger.debug("钱包帐户转合约帐户 合约账户资产 准备修改数据为:" + accountFundCurrent.toString());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        }
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmtEx);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountContractFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountContractFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountContractFundCurrent.setLastAmt(orgAmtEx.add(fundModel.getAmount()));
        accountContractFundCurrent.setRemark("钱包帐户转合约帐户转入：" + fundModel.getAmount());
        accountContractFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setFee(BigDecimal.ZERO);
        accountContractFundCurrent.setTransId(transId.toString());
        accountContractFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountContractFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountContractFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountContractFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountContractFundCurrent.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableFundCurrent());
        logger.debug("钱包帐户转合约帐户 合约账户资产 账户资金交易流水 准备插入数据为:" + accountContractFundCurrent.toString());
        accountFundCurrentService.insert(accountContractFundCurrent);
    }
    
    /**
     * 合约帐户转钱包转户
     * @param fundModel
     * @param accountWalletAsset
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 上午10:39:01
     */
    private void contract2Wallet(FundModel fundModel, AccountWalletAsset accountWalletAsset) throws BusinessException
    {
        // 行所
        try
        {
            accountContractAssetService.selectByPrimaryKeyOnRowLock(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx(),
                    getStockInfo(fundModel.getStockinfoIdEx()).getTableAsset());
        }
        catch (Exception e)
        {
            logger.debug(e.getStackTrace().toString());
            throw new BusinessException(CommonEnums.ERROR_SYS_BUSY);
        }
        BigDecimal orgAmt = BigDecimal.ZERO;
        // step0 判断可用足否
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setBusinessFlag(fundModel.getBusinessFlag());
        enableModel.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount().compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            // 委托可用不足异常
            logger.debug("正常可用不足");
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // 可用判断
        accountAssetService.contract2WalletEnable(fundModel);
        // step1 合约账户资产处理
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(fundModel.getAccountId());
        accountContractAsset.setStockinfoId(fundModel.getStockinfoId());
        accountContractAsset.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractAsset.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableAsset());
        accountContractAsset = accountContractAssetService.findAccountContractAsset(accountContractAsset);
        if (fundModel.getAmount().compareTo(accountContractAsset.getAmount()) > 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        // 修改合约账户资产当前金额
        orgAmt = accountContractAsset.getAmount();
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setAmount(orgAmt.subtract(fundModel.getAmount()));
        accountContractAsset.setFlowOutAmt(accountContractAsset.getFlowOutAmt().add(fundModel.getAmount()));
        accountContractAsset.setTableName(getStockInfo(fundModel.getStockinfoIdEx()).getTableAsset());
        logger.debug("合约帐户转钱包帐户 合约账户资产 准备修改数据为:" + accountContractAsset.toString());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        Long transId = SerialnoUtils.buildPrimaryKey();
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmt);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountContractFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountContractFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
        accountContractFundCurrent.setLastAmt(orgAmt.subtract(fundModel.getAmount()));
        accountContractFundCurrent.setRemark("合约帐户转钱包转户转出：" + fundModel.getAmount());
        accountContractFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setFee(BigDecimal.ZERO);
        accountContractFundCurrent.setTransId(transId.toString());
        accountContractFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountContractFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountContractFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountContractFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountContractFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractFundCurrent.setTableName(getStockInfo(accountContractFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("合约帐户转钱包帐户 合约账户资产 账户资金交易流水 准备插入数据为:" + accountContractFundCurrent.toString());
        accountFundCurrentService.insert(accountContractFundCurrent);
        // step2 钱包账户资产处理
        BigDecimal orgAmtEx = BigDecimal.ZERO;
        orgAmtEx = accountWalletAsset.getAmount();
        // 修改钱包账户资产当前金额
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWalletAsset.setAmount(orgAmtEx.add(fundModel.getAmount()));
        logger.debug("合约帐户转钱包帐户 钱包账户资产 准备修改数据为:" + accountWalletAsset.toString());
        accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_WALLET_ASSET);
        fundCurrentModel.setAccountAssetId(accountWalletAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(orgAmtEx);
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 插入账户资金流水
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setLastAmt(orgAmtEx.add(fundModel.getAmount()));
        accountFundCurrent.setRemark("合约帐户转钱包转户转入：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setTransId(transId.toString());
        accountFundCurrent.setForzenOrgAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountWalletAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountWalletAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("合约帐户转钱包帐户 钱包账户资产 账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
    }
    
    /**
     * 从db中查找理财账户资产记录并加行锁
     * @param accountId
     * @param stockinfoId
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountWealthAsset findAccountWealthAssetFormDBForUpdate(Long accountId, Long stockinfoId, Long relatedStockinfoId) throws BusinessException
    {
        AccountWealthAsset accountWealthAsset = new AccountWealthAsset();
        try
        {
            accountWealthAsset.setWealthAccountId(accountId);
            accountWealthAsset.setStockinfoId(stockinfoId);
            accountWealthAsset.setRelatedStockinfoId(stockinfoId);
            List<AccountWealthAsset> list = accountWealthAssetService.findList(accountWealthAsset);
            if (list.size() > 0)
            {
                accountWealthAsset = accountWealthAssetService.selectByPrimaryKeyForUpdate(list.get(0).getId());
            }
        }
        catch (Exception e)
        {
            logger.debug("行锁资源占用");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        return accountWealthAsset;
    }
    
    /**
     * 从db中查找钱包账户资产记录并加行锁
     * @param accountId
     * @param stockinfoId
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountWalletAsset findAccountWalletAssetFormDBForUpdate(Long accountId, Long stockinfoId) throws BusinessException
    {
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        try
        {
            accountWalletAsset = accountWalletAssetService.selectForUpdate(accountId, stockinfoId);
        }
        catch (Exception e)
        {
            logger.debug("行锁资源占用");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        return accountWalletAsset;
    }
    
    /**
     * 从db中查找现货账户资产记录并加行锁
     * @param accountId
     * @param stockinfoId
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountSpotAsset findAccountSpotAssetFormDBForUpdate(Long accountId, Long stockinfoId, Long relatedStockinfoId) throws BusinessException
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        try
        {
            accountSpotAsset = accountSpotAssetService.selectForUpdate(accountId, stockinfoId, getStockInfo(relatedStockinfoId).getCapitalStockinfoId());
        }
        catch (Exception e)
        {
            logger.debug("行锁资源占用");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        return accountSpotAsset;
    }
    
    /**
     * 从db中查找钱包账户资产记录
     * @param accountId
     * @param stockinfoId
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountSpotAsset findAccountSpotAssetFormDB(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(accountId);
        accountSpotAsset.setStockinfoId(stockinfoId);
        accountSpotAsset.setRelatedStockinfoId(getStockInfo(relatedStockinfoId).getCapitalStockinfoId());
        List<AccountSpotAsset> accountSpotAssetList;
        try
        {
            accountSpotAssetList = accountSpotAssetService.findList(accountSpotAsset);
            if (accountSpotAssetList.size() > 0)
            {
                accountSpotAsset = accountSpotAssetList.get(0);
            }
            else
            {
                accountSpotAsset = null;
            }
        }
        catch (Exception e)
        {
            accountSpotAsset = null;
            logger.debug("从db中查找钱包账户资产记录 error:" + e.getMessage());
        }
        if (null == accountSpotAsset)
        {
            logger.debug("从db中查找钱包账户资产记录 accountSpotAsset is null");
        }
        else
        {
            logger.debug("从db中查找钱包账户资产记录 accountSpotAsset:" + accountSpotAsset.toString());
        }
        return accountSpotAsset;
    }
    
    /**
     * 从db中查找钱包账户资产记录
     * @param accountId
     * @param stockinfoId
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountWalletAsset findAccountWalletAssetFormDB(Long accountId, Long stockinfoId)
    {
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(accountId);
        accountWalletAsset.setStockinfoId(stockinfoId);
        List<AccountWalletAsset> list;
        try
        {
            list = accountWalletAssetService.findList(accountWalletAsset);
            if (list.size() > 0)
            {
                accountWalletAsset = list.get(0);
            }
            else
            {
                accountWalletAsset = null;
            }
        }
        catch (Exception e)
        {
            accountWalletAsset = null;
            logger.debug("从db中查找钱包账户资产记录 error:" + e.getMessage());
        }
        if (null == accountWalletAsset)
        {
            logger.debug("从db中查找钱包账户资产记录 accountWalletAsset is null");
        }
        else
        {
            logger.debug("从db中查找钱包账户资产记录 accountWalletAsset:" + accountWalletAsset.toString());
        }
        return accountWalletAsset;
    }
    
    /**
     * 从db中查找合约账户资产记录
     * @param accountId
     * @param stockinfoId
     * @return
     * @author zcx 2017-09-19 15:52:34
     */
    private AccountContractAsset findAccountContractAssetFormDB(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(accountId);
        accountContractAsset.setStockinfoId(stockinfoId);
        accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
        accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
        List<AccountContractAsset> accountWalletAssetList;
        try
        {
            accountWalletAssetList = accountContractAssetService.findList(accountContractAsset);
            if (accountWalletAssetList.size() > 0)
            {
                accountContractAsset = accountWalletAssetList.get(0);
                logger.debug("从db中查找合约账户资产记录 accountContractAsset:" + accountContractAsset.toString());
            }
            else
            {
                accountContractAsset = new AccountContractAsset();
                accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
                accountContractAsset.setStockinfoId(stockinfoId);
                accountContractAsset.setAccountId(accountId);
                accountContractAsset.setAmount(BigDecimal.ZERO);
                accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
                accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                accountContractAsset.setId(SerialnoUtils.buildPrimaryKey());
                accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
                accountContractAssetService.insert(accountContractAsset);
                logger.debug("从db中查找合约账户资产记录 accountContractAsset:" + accountContractAsset.toString());
            }
        }
        catch (Exception e)
        {
            accountContractAsset = null;
            logger.debug("从db中查找合约账户资产记录 error:" + e.getMessage());
        }
        return accountContractAsset;
    }
    
    /**
     * 从合约账户资产db中查找可用数量
     * @param enableModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private EnableModel findEnableAmtFormAccountContractAssetDB(EnableModel enableModel)
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(enableModel.getAccountId());
        accountContractAsset.setStockinfoId(enableModel.getStockinfoId());
        accountContractAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
        accountContractAsset.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableAsset());
        logger.debug("从合约账户资产db中查找可用数量 accountContractAsset:" + accountContractAsset.toString());
        List<AccountContractAsset> accountContractAssetList;
        try
        {
            accountContractAssetList = accountContractAssetService.findList(accountContractAsset);
            if (accountContractAssetList.size() > 0)
            {
                accountContractAsset = accountContractAssetList.get(0);
                // 如果冻结数量小于等于 直接取0 防止风险
                if (accountContractAsset.getFrozenAmt().compareTo(BigDecimal.ZERO) <= 0)
                {
                    accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
                }
                enableModel.setEnableAmount(accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()));
                enableModel.setFrozenAmt(accountContractAsset.getFrozenAmt());
            }
            else
            {
                enableModel.setEnableAmount(BigDecimal.ZERO);
                enableModel.setFrozenAmt(BigDecimal.ZERO);
            }
        }
        catch (Exception e)
        {
            enableModel.setEnableAmount(BigDecimal.ZERO);
            enableModel.setFrozenAmt(BigDecimal.ZERO);
            logger.error("从合约账户资产db中查找可用数量 error:" + e.getMessage());
        }
        logger.debug("从合约账户资产db中查找可用数量 enableModel:" + enableModel.toString());
        return enableModel;
    }
    
    /**
     * 构造组装账户资金交易流水
     * @param fundModel
     * @param fundCurrentModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountFundCurrent constructAccountFundCurrent(FundModel fundModel, FundCurrentModel fundCurrentModel)
    {
        long currentTime = CalendarUtils.getCurrentTime();
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(fundCurrentModel.getAccountId());
        accountFundCurrent.setAccountAssetType(fundCurrentModel.getAccountAssetType());
        accountFundCurrent.setAccountAssetId(fundCurrentModel.getAccountAssetId());
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(fundCurrentModel.getBusinessFlag());
        accountFundCurrent.setStockinfoId(fundCurrentModel.getStockinfoId());
        accountFundCurrent.setOrgAmt(fundCurrentModel.getOrgAmt());
        accountFundCurrent.setOccurAmt(fundCurrentModel.getOccurAmt());
        accountFundCurrent.setFee(fundCurrentModel.getFee());
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        logger.debug("组装账户资金交易流水 constructAccountFundCurrent:" + accountFundCurrent.toString());
        return accountFundCurrent;
    }
    
    /**
     * 构造组装钱包账户资产
     * @param fundModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountWalletAsset constructAccountWalletAsset(FundModel fundModel)
    {
        long currentTime = CalendarUtils.getCurrentTime();
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(fundModel.getAccountId());
        accountWalletAsset.setStockinfoId(fundModel.getStockinfoId());
        accountWalletAsset.setAmount(BigDecimal.ZERO);
        accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("组装账户钱包资产 constructAccountWalletAsset:" + accountWalletAsset.toString());
        return accountWalletAsset;
    }
    
    /**
     * 构造组装合约账户资产
     * @param fundModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountContractAsset constructAccountContractAsset(FundModel fundModel)
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(fundModel.getAccountId());
        accountContractAsset.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountContractAsset.setStockinfoId(fundModel.getStockinfoId());
        accountContractAsset.setAmount(BigDecimal.ZERO);
        accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("组装合约账户资产 constructAccountContractAsset:" + accountContractAsset.toString());
        return accountContractAsset;
    }
    
    /**
     * 构造组装钱包账户资产
     * @param fundModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountSpotAsset walletAccountWalletAsset(FundModel fundModel)
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(fundModel.getAccountId());
        accountSpotAsset.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountSpotAsset.setStockinfoId(fundModel.getStockinfoId());
        accountSpotAsset.setAmount(BigDecimal.ZERO);
        accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("组装合约账户资产 accountSpotAsset:" + accountSpotAsset.toString());
        return accountSpotAsset;
    }
    
    @Override
    public int fundDebitMoveToPlatAsset(AccountDebitAsset record, Long superAccountId, String businessFlag) throws BusinessException
    {
        // 强制平仓转移VCOIN债务内部转移 || 强制平仓转移Money债务内部转移 || 交割平仓转移VCOIN债务内部转移 || 交割平仓转移Money债务内部转移
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER.equals(businessFlag)
                || FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER.equals(businessFlag)
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_TRANSFER.equals(businessFlag)
                || FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_TRANSFER.equals(businessFlag))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(record.getRelatedStockinfoId());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                // 写借款减少资金流水(当前用户)
                AccountContractAsset accountContractAsset = new AccountContractAsset();
                accountContractAsset = this.findAccountContractAssetFormDB(record.getBorrowerAccountId(), record.getStockinfoId(), record.getRelatedStockinfoId());
                AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
                accountFundCurrent.setAccountId(record.getBorrowerAccountId());
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_DEBIT);
                accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
                accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
                accountFundCurrent.setBusinessFlag(businessFlag);
                accountFundCurrent.setStockinfoId(record.getStockinfoId());
                accountFundCurrent.setContractAmt(BigDecimal.ZERO);
                accountFundCurrent.setOrgAmt(record.getDebitAmt());
                accountFundCurrent.setOccurAmt(record.getDebitAmt());
                accountFundCurrent.setLastAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
                accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
                accountFundCurrent.setFee(BigDecimal.ZERO);
                accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
                accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
                accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 债务减少
                accountFundCurrent.setRemark("借款总额减少：" + record.getDebitAmt());
                accountFundCurrent.setOriginalBusinessId(record.getId());
                accountFundCurrent.setRelatedStockinfoId(record.getRelatedStockinfoId());
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                logger.debug("当前账户借款减少 强制平仓转移 债务内部转移 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
                accountFundCurrentService.insert(accountFundCurrent);
                // 写借款增加资金流水(超级用户)
                accountContractAsset = new AccountContractAsset();
                accountContractAsset = this.findAccountContractAssetFormDB(superAccountId, record.getStockinfoId(), record.getRelatedStockinfoId());
                AccountDebitAsset recordSuper = new AccountDebitAsset();
                recordSuper.setStockinfoId(record.getStockinfoId());
                recordSuper.setRelatedStockinfoId(record.getRelatedStockinfoId());
                recordSuper.setBorrowerAccountId(superAccountId);
                recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
                recordSuper = accountDebitAssetService.findList(recordSuper).get(0);
                accountFundCurrent = new AccountFundCurrent();
                accountFundCurrent.setAccountId(superAccountId);
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_DEBIT);
                accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
                accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
                accountFundCurrent.setBusinessFlag(businessFlag);
                accountFundCurrent.setStockinfoId(record.getStockinfoId());
                accountFundCurrent.setContractAmt(BigDecimal.ZERO);
                accountFundCurrent.setOrgAmt(recordSuper.getDebitAmt().subtract(record.getDebitAmt()));
                accountFundCurrent.setOccurAmt(record.getDebitAmt());
                accountFundCurrent.setLastAmt(recordSuper.getDebitAmt());
                accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
                accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
                accountFundCurrent.setFee(BigDecimal.ZERO);
                accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
                accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
                accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 债务增加
                accountFundCurrent.setRemark("借款总额增加：" + record.getDebitAmt());
                accountFundCurrent.setOriginalBusinessId(recordSuper.getId());
                accountFundCurrent.setRelatedStockinfoId(record.getRelatedStockinfoId());
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                logger.debug("超级用户借款增加 强制平仓转移 债务内部转移 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
                accountFundCurrentService.insert(accountFundCurrent);
            }
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                // 写借款减少资金流水(当前用户)
                AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
                accountSpotAsset = this.findAccountSpotAssetFormDB(record.getBorrowerAccountId(), record.getStockinfoId(),
                        getStockInfo(record.getRelatedStockinfoId()).getCapitalStockinfoId());
                AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
                accountFundCurrent.setAccountId(record.getBorrowerAccountId());
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT);
                accountFundCurrent.setAccountAssetId(accountSpotAsset.getId());// 资产对应的id
                accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
                accountFundCurrent.setBusinessFlag(businessFlag);
                accountFundCurrent.setStockinfoId(record.getStockinfoId());
                accountFundCurrent.setContractAmt(BigDecimal.ZERO);
                accountFundCurrent.setOrgAmt(record.getDebitAmt());
                accountFundCurrent.setOccurAmt(record.getDebitAmt());
                accountFundCurrent.setLastAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
                accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
                accountFundCurrent.setFee(BigDecimal.ZERO);
                accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
                accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
                accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 债务减少
                accountFundCurrent.setRemark("借款总额减少：" + record.getDebitAmt());
                accountFundCurrent.setOriginalBusinessId(record.getId());
                accountFundCurrent.setRelatedStockinfoId(record.getRelatedStockinfoId());
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                logger.debug("当前账户借款减少 强制平仓转移 债务内部转移 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
                accountFundCurrentService.insert(accountFundCurrent);
                // 写借款增加资金流水(超级用户)
                accountSpotAsset = new AccountSpotAsset();
                accountSpotAsset = this.findAccountSpotAssetFormDB(superAccountId, record.getStockinfoId(),
                        getStockInfo(record.getRelatedStockinfoId()).getCapitalStockinfoId());
                AccountDebitAsset recordSuper = new AccountDebitAsset();
                recordSuper.setStockinfoId(record.getStockinfoId());
                recordSuper.setRelatedStockinfoId(getStockInfo(record.getRelatedStockinfoId()).getCapitalStockinfoId());
                recordSuper.setTableName(getStockInfo(record.getRelatedStockinfoId()).getTableDebitAsset());
                recordSuper.setBorrowerAccountId(superAccountId);
                recordSuper = accountDebitAssetService.findList(recordSuper).get(0);
                accountFundCurrent = new AccountFundCurrent();
                accountFundCurrent.setAccountId(superAccountId);
                accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_DEBIT);
                accountFundCurrent.setAccountAssetId(accountSpotAsset.getId());// 资产对应的id
                accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
                accountFundCurrent.setBusinessFlag(businessFlag);
                accountFundCurrent.setStockinfoId(record.getStockinfoId());
                accountFundCurrent.setContractAmt(BigDecimal.ZERO);
                accountFundCurrent.setOrgAmt(recordSuper.getDebitAmt().subtract(record.getDebitAmt()));
                accountFundCurrent.setOccurAmt(record.getDebitAmt());
                accountFundCurrent.setLastAmt(recordSuper.getDebitAmt());
                accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
                accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
                accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
                accountFundCurrent.setFee(BigDecimal.ZERO);
                accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
                accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
                accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
                accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 债务增加
                accountFundCurrent.setRemark("借款总额增加：" + record.getDebitAmt());
                accountFundCurrent.setOriginalBusinessId(recordSuper.getId());
                accountFundCurrent.setRelatedStockinfoId(record.getRelatedStockinfoId());
                accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
                logger.debug("超级用户借款增加 强制平仓转移 债务内部转移 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
                accountFundCurrentService.insert(accountFundCurrent);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        else
        {
            logger.debug("错误的业务类型：" + businessFlag);
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return 0;
    }
    
    /**
     * 金额校验是否小于0
     * @param amount
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private void validateAmountIsSmallZero(BigDecimal amount) throws BusinessException
    {
        logger.debug("validateAmountIsSmallZero amount:" + amount.doubleValue());
        if (amount.compareTo(BigDecimal.ZERO) < 0)
        {
            logger.debug("金额校验小于0");
            throw new BusinessException(CommonEnums.RISK_FUND_AMOUNT_SMALL_ZERO);
        }
    }
    
    /**
     * 费用校验
     * @param fee
     * @throws BusinessException
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private void validateFee(BigDecimal fee) throws BusinessException
    {
        logger.debug("validateFee fee:" + fee.doubleValue());
        if (null == fee)
        {
            logger.debug("费用校验为空");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (fee.compareTo(BigDecimal.ZERO) < 0)
        {
            logger.debug("费用校验小于0");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }
    
    @Override
    public void doSettlementVCoinMove(FundModel fundModel) throws BusinessException
    {
        logger.debug("交割资产处理：" + fundModel.toString());
        // 数字货币减少(当前用户)
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        logger.debug("当前用户发生变化前：" + accountContractAsset.toString());
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(fundModel.getAccountId());
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SHARE_OF_LOSSES_VCOIN_MONEY);
        accountFundCurrent.setStockinfoId(fundModel.getStockinfoId());// 数字货币
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("平台亏损分摊减少：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台亏损分摊较少 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().subtract(fundModel.getAmount()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        logger.debug("当前用户发生变化后：" + accountContractAsset.toString());
        // 数字货币增加 (超级用户)
        accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        logger.debug("超级用户发生变化前：" + accountContractAsset.toString());
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SHARE_OF_LOSSES_VCOIN_MONEY);
        accountFundCurrent.setStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().add(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark("平台亏损分摊增加：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("超级用户 平台亏损分摊 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().add(fundModel.getAmount()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        logger.debug("超级用户发生变化后：" + accountContractAsset.toString());
    }
    
    /**
     * 交割时超级用户用数字货币兑换法定货币
     * @return
     * @throws BusinessException
     * @author zcx
     */
    @Override
    public void doSettlementMoneyExchangeVCoin(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        logger.debug("进入兑换");
        logger.debug("强制还款处理start");
        // 调用强制还款
        accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY, exchangePairMoney, exchangePairMoney, exchangePairVCoin);
        accountDebitAssetService.doDebitRepaymentPowerByPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, exchangePairVCoin, exchangePairMoney, exchangePairMoney);
        logger.debug("强制还款处理end");
        logger.debug("兑换开始。。。。。。。。。。。。。。。。。。。。。。。。");
        BigDecimal settlementPrice = BigDecimal.ZERO;
        settlementPrice = getSettlementPrice(exchangePairMoney);
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setStockinfoId(exchangePairMoney);// 法定货币
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);// 法定货币
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> list = accountContractAssetService.findList(accountContractAsset);
        for (AccountContractAsset asset : list)
        {
            if (asset.getAmount().subtract(asset.getFrozenAmt()).compareTo(BigDecimal.ZERO) > 0)
            {
                logger.debug("法定货币可用余额大于0 用户：" + asset.getAccountId() + asset.getAccountName() + " 有法定数字可用余额！");
                doSettlementMoneyExchangeVCoin(asset, settlementPrice, exchangePairVCoin, exchangePairMoney);
            }
            else
            {
                logger.debug("法定货币可用余额小于0 用户：" + asset.getAccountId() + asset.getAccountName() + " 没有法定货币可用余额！");
            }
        }
        logger.debug("兑换结束。。。。。。。。。。。。。。。。。。。。。。。。");
    }
    
    public void doSettlementMoneyExchangeVCoin(AccountContractAsset accountContractAsset, BigDecimal settlementPrice, Long exchangePairVCoin, Long exchangePairMoney)
            throws BusinessException
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        Long clearStockinfoId = stockInfo.getClearStockinfoId();
        Long tradeStockinfoId = stockInfo.getTradeStockinfoId();
        logger.debug("清算标的证券ID:" + clearStockinfoId);
        logger.debug("交易标的证券ID:" + tradeStockinfoId);
        // 清算标的证券ID不等于交易标的证券ID 需要将法币兑换成数字货币
        boolean isVCoin = clearStockinfoId.longValue() != tradeStockinfoId.longValue();
        logger.debug("是否交易数字货币：" + isVCoin);
        BigDecimal usdx = accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt());
        BigDecimal btc = isVCoin ? usdx.divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP) : usdx.multiply(settlementPrice);
        Long accountId = accountContractAsset.getAccountId();
        Long businessId = accountContractAsset.getId();
        // (当前用户)法定货币资产减少
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(accountId);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SETTLEMENT_VCOIN_MONEY_EXCHANGE_MONEY_TO_VCOIN);
        accountFundCurrent.setStockinfoId(exchangePairMoney);// 法定货币
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()));
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("平台交割，兑换数字货币：" + btc + "，法定货币减少：" + accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()));
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(businessId);
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换数字货币：" + btc + "，法定货币减少： 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getFrozenAmt());
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        // (当前用户)数字货币增加
        accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(accountId, exchangePairVCoin, exchangePairMoney);
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(accountId);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SETTLEMENT_VCOIN_MONEY_EXCHANGE_MONEY_TO_VCOIN);
        accountFundCurrent.setStockinfoId(exchangePairVCoin);// 数字货币
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(btc);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().add(btc));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark("平台交割，兑换法定货币:" + usdx + "，数字货币增加：" + btc);
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(businessId);
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换法定货币" + usdx + "，数字货币增加 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().add(btc));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        // (超级用户) 数字资产减少
        accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, exchangePairVCoin, exchangePairMoney);
        // 如果数字货币不够 调用借款
        if (btc.compareTo(accountContractAsset.getAmount()) > 0)
        {
            BigDecimal borrowBtc = btc.subtract(accountContractAsset.getAmount());
            DebitAssetModel debitAssetModel = new DebitAssetModel();
            debitAssetModel.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            debitAssetModel.setDebitAmt(borrowBtc);
            debitAssetModel.setStockinfoId(exchangePairVCoin);
            debitAssetModel.setRelatedStockinfoId(exchangePairMoney);
            logger.debug("贷款准备=" + debitAssetModel.toString());
            debitAssetModel = accountDebitAssetService.doDebitSuperAdminBorrowFromPlat(debitAssetModel);
            logger.debug("贷款完毕=" + debitAssetModel.toString());
        }
        // 借款后重新获取资产
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, exchangePairVCoin, exchangePairMoney);
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SETTLEMENT_VCOIN_MONEY_EXCHANGE_MONEY_TO_VCOIN);
        accountFundCurrent.setStockinfoId(exchangePairVCoin);
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(btc);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().subtract(btc));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("平台交割，兑换法定货币:" + usdx + "，数字货币减少：" + btc);
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换法定货币:" + usdx + "，数字货币减少：" + btc + " 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().subtract(btc));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, exchangePairMoney, exchangePairMoney);
        // (超级账户)法定货币增加
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SETTLEMENT_VCOIN_MONEY_EXCHANGE_MONEY_TO_VCOIN);
        accountFundCurrent.setStockinfoId(exchangePairMoney);
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(usdx);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().add(usdx));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("平台交割，兑换数字货币：" + btc + "，法定货币增加：" + usdx);
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(businessId);
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换数字货币：" + btc + "，法定货币增加： 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().add(usdx));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
    }
    
    @Override
    public void doSettlementAssetMove(FundModel fundModel) throws BusinessException
    {
        // 当前用户
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(fundModel.getAccountId());
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(fundModel.getBusinessFlag());
        accountFundCurrent.setStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("交割超级用户内部资产转移：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("交割超级用户内部资产转移 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().subtract(fundModel.getAmount()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        // 超级用户
        accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(fundModel.getBusinessFlag());
        accountFundCurrent.setStockinfoId(fundModel.getStockinfoId());
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(fundModel.getAmount());
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().add(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark("交割超级用户内部资产转移：" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("交割超级用户内部资产转移： 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().add(fundModel.getAmount()));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
    }
    
    /**
     * 超级账户合约资产清零
     * @param fundModel
     * @param accountContractAsset
     * @throws BusinessException
     * @author zcx  2017-10-26 15:56:20
     */
    private void superAccountContractAssetToZero(FundModel fundModel, AccountContractAsset accountContractAsset) throws BusinessException
    {
        // step1 修改钱包账户资产当前金额
        if (null == accountContractAsset)
        {
            accountContractAsset = this.constructAccountContractAsset(fundModel);
            accountContractAsset.setAmount(BigDecimal.ZERO);
            logger.debug("超级用户账户资产清零 准备插入数据为:" + accountContractAsset.toString());
            accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
            accountContractAssetService.insert(accountContractAsset);
        }
        else
        {
            accountContractAsset.setAmount(BigDecimal.ZERO);
            logger.debug("超级用户账户资产清零 准备修改数据为:" + accountContractAsset.toString());
            accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
            accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        }
        // step2 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountContractAsset.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(BigDecimal.ZERO);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
        accountFundCurrent.setRemark("超级用户账户资产清零:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("超级用户账户资产清零 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        logger.debug("超级用户账户借款清零 资金清零资金流水处理完毕:" + accountFundCurrent.toString());
    }
    
    /**
     * 超级账户合约借款清零
     * @param fundModel
     * @throws BusinessException
     * @author zcx  2017-10-26 15:56:20
     */
    private void superAccountContractDebitToZero(FundModel fundModel) throws BusinessException
    {
        // 增加账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_SPOT_ASSET);
        fundCurrentModel.setAccountAssetId(fundModel.getOriginalBusinessId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(fundModel.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        accountFundCurrent.setLastAmt(BigDecimal.ZERO);
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);// 减少
        accountFundCurrent.setRemark("超级用户账户借款清零:" + fundModel.getAmount());
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(BigDecimal.ZERO);
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(BigDecimal.ZERO);
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("超级用户账户借款清零 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        logger.debug("超级用户账户借款清零 债务清零资金流水处理完毕:" + accountFundCurrent.toString());
    }
    
    /**
     * 多空头超级用户准备金转移给准备金用户 准备金账户准备金抵扣给超级用户
     * @param fundModel
     * @throws BusinessException
     * @author zcx  2017-10-27 09:15:16
     */
    private void reserveFundDecrease(FundModel fundModel) throws BusinessException
    {
        logger.debug("reserveFundDecrease 传入参数：" + fundModel.toString());
        // 当前用户
        AccountContractAsset accountContractAsset2 = new AccountContractAsset();
        accountContractAsset2 = this.findAccountContractAssetFormDB(fundModel.getAccountId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        logger.debug("账户：" + accountContractAsset2.getAccountId() + "转移前资产情况：" + accountContractAsset2.toString());
        // 当前账户买入数字货币 扩展证券代码
        // step1 增加当前账户资金流水
        FundCurrentModel fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getAccountId());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset2.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        fundCurrentModel.setBusinessFlag(fundModel.getBusinessFlag());
        fundCurrentModel.setOrgAmt(accountContractAsset2.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        AccountFundCurrent accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        accountFundCurrent.setLastAmt(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            accountFundCurrent.setRemark(accountContractAsset2.getAccountId() + "准备金用户为分摊抵扣：" + fundModel.getAmount());
        }
        else
        {
            accountFundCurrent.setRemark(accountContractAsset2.getAccountId() + "超级用户挂单的盈利转出：" + fundModel.getAmount());
        }
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset2.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset2.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("准备金用户为分摊抵扣 准备插入数据为:" + accountFundCurrent.toString());
        }
        else
        {
            logger.debug("超级用户挂单的盈利转出 准备插入数据为:" + accountFundCurrent.toString());
        }
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 减少当前账户合约账户资产当前数量
        // 检验金额
        logger.debug("当前用户货币转出前" + accountContractAsset2.getAmount());
        logger.debug("当前用户货币转出" + fundModel.getAmount());
        logger.debug("当前用户货币转出后" + accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
        this.validateAmountIsSmallZero(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
        accountContractAsset2.setAmount(accountContractAsset2.getAmount().subtract(fundModel.getAmount()));
        accountContractAsset2.setFrozenAmt(accountContractAsset2.getFrozenAmt());
        accountContractAsset2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("准备金用户为分摊抵扣 准备修改资金数据为:" + accountContractAsset2.toString());
        }
        else
        {
            logger.debug("超级用户挂单的盈利转出 准备修改资金数据为:" + accountContractAsset2.toString());
        }
        accountContractAsset2.setTableName(getStockInfo(accountContractAsset2.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset2);
        logger.debug("账户：" + accountContractAsset2.getAccountId() + "转移后资产情况：" + accountContractAsset2.toString());
        // 超级用户法定货币账户
        accountContractAsset2 = new AccountContractAsset();
        accountContractAsset2 = this.findAccountContractAssetFormDB(fundModel.getCreateBy(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());
        logger.debug("账户：" + fundModel.getCreateBy() + "转移前资产情况：" + accountContractAsset2.toString());
        // step1 增加当前账户资金流水
        fundCurrentModel = new FundCurrentModel();
        fundCurrentModel.setAccountId(fundModel.getCreateBy());
        fundCurrentModel.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        fundCurrentModel.setAccountAssetId(accountContractAsset2.getId());
        fundCurrentModel.setStockinfoId(fundModel.getStockinfoId());
        if (StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(), FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE))
        {
            fundCurrentModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_INCREASE);
        }
        else if (StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(), FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_MOVE_DECREASE))
        {
            fundCurrentModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_MOVE_INCREASE);
        }
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            fundCurrentModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_INCREASE);
        }
        else
        {
            logger.debug("错误的业务类别");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        fundCurrentModel.setOrgAmt(accountContractAsset2.getAmount());
        fundCurrentModel.setOccurAmt(fundModel.getAmount());
        fundCurrentModel.setFee(BigDecimal.ZERO);
        accountFundCurrent = this.constructAccountFundCurrent(fundModel, fundCurrentModel);
        // 检验金额
        this.validateAmountIsSmallZero(accountContractAsset2.getAmount().add(fundModel.getAmount()));
        accountFundCurrent.setLastAmt(accountContractAsset2.getAmount().add(fundModel.getAmount()));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            accountFundCurrent.setRemark(accountContractAsset2.getAccountId() + "准备金用户为分摊抵扣：" + fundModel.getAmount());
        }
        else
        {
            accountFundCurrent.setRemark(accountContractAsset2.getAccountId() + "超级用户挂单的盈利转入：" + fundModel.getAmount());
        }
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset2.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset2.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(fundModel.getOriginalBusinessId());
        accountFundCurrent.setRelatedStockinfoId(fundModel.getStockinfoIdEx());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("准备金用户为分摊抵扣 准备插入数据为:" + accountFundCurrent.toString());
        }
        else
        {
            logger.debug("超级用户挂单的盈利转入 准备插入数据为:" + accountFundCurrent.toString());
        }
        accountFundCurrentService.insert(accountFundCurrent);
        // step2 增加超级账户合约账户资产当前数量
        // 检验金额
        this.validateAmountIsSmallZero(accountContractAsset2.getAmount().add(fundModel.getAmount()));
        accountContractAsset2.setAmount(accountContractAsset2.getAmount().add(fundModel.getAmount()));
        accountContractAsset2.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_DEDUCTION_DECREASE.equals(fundModel.getBusinessFlag()))
        {
            logger.debug("准备金用户为分摊抵扣 准备修改资金数据为:" + accountContractAsset2.toString());
        }
        else
        {
            logger.debug("超级用户挂单的盈利转入 准备修改资金数据为:" + accountContractAsset2.toString());
        }
        accountContractAsset2.setTableName(getStockInfo(accountContractAsset2.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset2);
        logger.debug("账户：" + fundModel.getCreateBy() + "转移后资产情况：" + accountContractAsset2.toString());
    }
    
    @Override
    public void superSettlementMoneyExchangeVCoin(AccountContractAsset accountContractAsset, BigDecimal settlementPrice) throws BusinessException
    {
        BigDecimal usdx = accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt());
        BigDecimal btc = usdx.divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP);
        Long accountId = accountContractAsset.getAccountId();
        Long businessId = accountContractAsset.getId();
        // (当前用户)USDX减少
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(accountId);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_EXCHANGE_DECREASE);
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()));
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("平台交割，兑换BTC：" + btc + "，USDX减少：" + accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()));
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(businessId);
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换BTC：" + btc + "，USDX减少： 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getFrozenAmt());
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        // (当前用户)BTC增加
        accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(accountId, FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE);
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(accountId);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_EXCHANGE_INCREASE);
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(btc);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().add(btc));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
        accountFundCurrent.setRemark("平台交割，兑换USDX:" + usdx + "，BTC增加：" + btc);
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(businessId);
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换USDX" + usdx + "，BTC增加 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().add(btc));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        // (超级用户) BTC减少
        accountContractAsset = new AccountContractAsset();
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE);
        // 如果BTC不够 调用借款
        if (btc.compareTo(accountContractAsset.getAmount()) > 0)
        {
            BigDecimal borrowBtc = btc.subtract(accountContractAsset.getAmount());
            DebitAssetModel debitAssetModel = new DebitAssetModel();
            debitAssetModel.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            debitAssetModel.setDebitAmt(borrowBtc);
            debitAssetModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            debitAssetModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            logger.debug("贷款准备=" + debitAssetModel.toString());
            debitAssetModel = accountDebitAssetService.doDebitSuperAdminBorrowFromPlat(debitAssetModel);
            logger.debug("贷款完毕=" + debitAssetModel.toString());
        }
        // 借款后重新获取资产
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE);
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_EXCHANGE_DECREASE);
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(btc);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().subtract(btc));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE); // 减少
        accountFundCurrent.setRemark("平台交割，兑换USDX:" + usdx + "，BTC减少：" + btc);
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(accountContractAsset.getId());
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换USDX:" + usdx + "，BTC减少：" + btc + " 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().subtract(btc));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
        accountContractAsset = this.findAccountContractAssetFormDB(FundConsts.SYSTEM_ACCOUNT_ID, FundConsts.WALLET_BTC2USDX_TYPE, FundConsts.WALLET_BTC2USDX_TYPE);
        // (超级账户)USDX增加
        accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountFundCurrent.setAccountAssetType(FundConsts.ACCOUNT_ASSET_TYPE_CONTRACT_ASSET);
        accountFundCurrent.setAccountAssetId(accountContractAsset.getId());// 资产对应的id
        accountFundCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_MONEY_CONTRIBUTION_QUOTA_EXCHANGE_INCREASE);
        accountFundCurrent.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        accountFundCurrent.setOrgAmt(accountContractAsset.getAmount());
        accountFundCurrent.setOccurAmt(usdx);
        accountFundCurrent.setFee(BigDecimal.ZERO);
        accountFundCurrent.setStatus(FundConsts.ACCOUNT_FUND_CURRENT_EFFECTIVE);
        accountFundCurrent.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_NOAPPROVE);
        accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_NOTRANSFER);
        accountFundCurrent.setLastAmt(accountContractAsset.getAmount().add(usdx));
        accountFundCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
        accountFundCurrent.setRemark("平台交割，兑换BTC：" + btc + "，USDX增加：" + usdx);
        accountFundCurrent.setContractAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenOrgAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOccurForzenAmt(BigDecimal.ZERO);
        accountFundCurrent.setForzenLastAmt(accountContractAsset.getFrozenAmt());
        accountFundCurrent.setOriginalBusinessId(businessId);
        accountFundCurrent.setRelatedStockinfoId(accountContractAsset.getRelatedStockinfoId());
        accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        logger.debug("平台交割，兑换BTC：" + btc + "，USDX增加： 合约账户资金交易流水 准备插入数据为:" + accountFundCurrent.toString());
        accountFundCurrentService.insert(accountFundCurrent);
        accountContractAsset.setAmount(accountContractAsset.getAmount().add(usdx));
        accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        accountContractAssetService.updateByPrimaryKey(accountContractAsset);
    }
    
    /**
     * 公共方法 获取结算价
     * @param stockInfoId
     * @return
     * @throws BusinessException
     */
    public BigDecimal getSettlementPrice(Long stockInfoId) throws BusinessException
    {
        BigDecimal ret = BigDecimal.ZERO;
        List<StockInfo> list = stockInfoService.findListByIds(stockInfoId.toString());
        if (list.size() > 0)
        {
            StockInfo info = list.get(0);
            if (info.getSettlementPrice() == null)
            {
                logger.debug("结算价不存在");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            ret = info.getSettlementPrice();
        }
        else
        {
            ret = BigDecimal.ZERO;
            logger.debug("结算价不存在");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return ret;
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
