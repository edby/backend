/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.model.DebitAssetModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.AccountSpotAssetService;
import com.blocain.bitms.trade.fund.service.FundService;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.DealModel;
import com.blocain.bitms.trade.trade.model.EntrustModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 交易类统一服务接口实现类
 * <p>File：TradeServiceImpl.java </p>
 * <p>Title: TradeServiceImpl </p>
 * <p>Description:TradeServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
@Service
public class TradeServiceImpl implements TradeService
{
    private static final Logger       logger = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Autowired
    private AccountService            accountService;

    @Autowired
    private EntrustVCoinMoneyService  entrustVCoinMoneyService;

    @Autowired
    private RealDealVCoinMoneyService realDealVCoinMoneyService;

    @Autowired
    private EnableService             enableService;

    @Autowired
    private FundService               fundService;

    @Autowired
    AccountDebitAssetService 		  accountDebitAssetService;

    @Autowired
    StockInfoService                  stockInfoService;

    @Autowired
    AccountContractAssetService       accountContractAssetService;

    @Autowired
    AccountWalletAssetService 		  accountWalletAssetService;

    @Autowired
    AccountSpotAssetService 		  accountSpotAssetService;

    @Override
    public Long entrust(EntrustModel entrustModel) throws BusinessException
    {
        logger.debug("委托服务 start--------------------------------------------------");
        logger.debug("委托服务 enableModel:" + entrustModel.toString());
        if (null == entrustModel.getAccountId() || null == entrustModel.getStockinfoId() || null == entrustModel.getTradeType() || null == entrustModel.getEntrustDirect())
        {// 入参统一校验判断
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 根据交易类型不同分别处理
        EnableModel enableModel = new EnableModel();
        FundModel fundModel = new FundModel();
        // 撮合交易
        if (TradeEnums.TRADE_TYPE_MATCHTRADE.getCode().equals(entrustModel.getTradeType()))
        {
            // 组装
            this.constructEnableModelAndFundModelByEntrustModel(entrustModel, enableModel, fundModel);
            // call
            Long id = this.matchTradeEntrust(entrustModel, enableModel, fundModel);
            logger.debug("委托服务 end--------------------------------------------------");
            return id;
        }// 交割撮合交易
        else if (TradeEnums.TRADE_TYPE_SETTLEMENTTRADE.getCode().equals(entrustModel.getTradeType()))
        {
            // 组装
            this.constructEnableModelAndFundModelByEntrustModel(entrustModel, enableModel, fundModel);
            // call
            Long id = this.settlementTradeEntrust(entrustModel, enableModel, fundModel);
            logger.debug("委托服务 end--------------------------------------------------");
            return id;
        }
        // 其他直接抛出
        else
        {
            logger.error("委托服务 tradeType " + entrustModel.getTradeType() + " is error Please check it!");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }

    @Override
    public int entrustWithdrawX(EntrustModel entrustModel) throws BusinessException
    {
        logger.debug("撮合交易委托撤单 start--------------------------------------------------");
        logger.debug("撮合交易委托撤单 entrustModel:" + entrustModel.toString());
        // 入参统一校验判断
        if (null == entrustModel.getEntrustId()) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        try
        {
            // step1 委托状态修改
            EntrustVCoinMoney entrustDB = new EntrustVCoinMoney();
            entrustDB = entrustVCoinMoneyService.selectByPrimaryKeyOnRowLock(getStockInfo(entrustModel.getStockinfoIdEx()).getTableEntrust(),
                    entrustModel.getEntrustId());
            entrustModel.setAccountId(entrustDB.getAccountId());
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(entrustModel.getStockinfoIdEx());
            if(stockInfo==null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }

            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                accountContractAssetService.selectByPrimaryKeyOnRowLock(entrustModel.getAccountId(), entrustModel.getStockinfoId(), entrustModel.getStockinfoIdEx(),
                        getStockInfo(entrustModel.getStockinfoIdEx()).getTableAsset());
                accountContractAssetService.selectByPrimaryKeyOnRowLock(entrustModel.getAccountId(), entrustModel.getStockinfoIdEx(), entrustModel.getStockinfoIdEx(),
                        getStockInfo(entrustModel.getStockinfoIdEx()).getTableAsset());
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustDB.getEntrustDirect()))
                {
                    accountSpotAssetService.selectForUpdate(entrustModel.getAccountId(), stockInfo.getTradeStockinfoId(), stockInfo.getCapitalStockinfoId());
                }
                else
                {
                    accountSpotAssetService.selectForUpdate(entrustModel.getAccountId(), stockInfo.getCapitalStockinfoId(), stockInfo.getCapitalStockinfoId());
                }
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustDB.getEntrustDirect()))
                {
                    accountWalletAssetService.selectForUpdate(entrustModel.getAccountId(), stockInfo.getTradeStockinfoId());
                }
                else
                {
                    accountWalletAssetService.selectForUpdate(entrustModel.getAccountId(), stockInfo.getCapitalStockinfoId());
                }
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }

            logger.debug("撮合交易委托撤单 entrustVCoinMoneyDB:" + entrustDB.toString());
            // 判断委托成交状态为全部成交
            if (TradeEnums.DEAL_STATUS_ALLDEAL.getCode().equals(entrustDB.getStatus()))
            {
                // 全部成交不能委托撤单
                throw new BusinessException(CommonEnums.RISK_TRADE_ENTRUST_ALLDEAL_NOTWITHDRAW);
            }
            if (TradeEnums.DEAL_STATUS_WITHDRAW.getCode().equals(entrustDB.getStatus()) || TradeEnums.DEAL_STATUS_ABNORMAL.getCode().equals(entrustDB.getStatus()))
            {
                // 已撤单、异常委托的委托 不能委托撤单
                throw new BusinessException(CommonEnums.RISK_TRADE_ENTRUST_ENTRUSTSTATUS_NOTWITHDRAW);
            }
            // 委托状态修改
            entrustDB.setStatus(TradeEnums.DEAL_STATUS_WITHDRAW.getCode());
            entrustDB.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            entrustDB.setTableName(getStockInfo(entrustModel.getStockinfoIdEx()).getTableEntrust());
            logger.debug("撮合交易委托撤单 entrustVCoinMoneyDB 修改记录:" + entrustDB.toString());
            entrustVCoinMoneyService.updateByPrimaryKey(entrustDB);
            // setp2 资产处理 减少证券冻结数量增加可用数量，并记录账户资金流水
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(entrustDB.getAccountId());
            fundModel.setOriginalBusinessId(entrustDB.getId());
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustDB.getEntrustDirect()))
            {
                fundModel.setStockinfoId(entrustDB.getEntrustStockinfoId());
                fundModel.setStockinfoIdEx(entrustDB.getEntrustRelatedStockinfoId());
                fundModel.setAmount(entrustDB.getEntrustAmt().subtract(entrustDB.getDealAmt())); // 未成交数量
                fundModel.setFee(BigDecimal.ZERO); // 未成交费用
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST_WITHDRAW);
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustDB.getEntrustDirect()))
            {
                if (entrustDB.getEntrustType().equals(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
                { // 限价
                    fundModel.setStockinfoId(entrustDB.getEntrustStockinfoId());
                    fundModel.setStockinfoIdEx(entrustDB.getEntrustRelatedStockinfoId());
                    fundModel.setAmount((entrustDB.getEntrustAmt().subtract(entrustDB.getDealAmt())).multiply(entrustDB.getEntrustPrice())); // 未成交数量
                    fundModel.setFee(BigDecimal.ZERO); // 未成交费用
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST_WITHDRAW);
                }
                else
                { // 市价
                    fundModel.setStockinfoId(entrustDB.getEntrustStockinfoId());
                    fundModel.setStockinfoIdEx(entrustDB.getEntrustRelatedStockinfoId());
                    fundModel.setAmount((entrustDB.getEntrustAmt().subtract(entrustDB.getDealAmt()))); // 未成交数量(USDX金额)
                    fundModel.setFee(BigDecimal.ZERO); // 未成交费用
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST_WITHDRAW);
                }
            }
            logger.debug("撮合交易委托撤单 fundModel:" + fundModel.toString());
            fundService.fundTransaction(fundModel);
        }
        catch (BusinessException e)
        {
            logger.error("撮合交易委托撤单错误:" + e.getLocalizedMessage());
            throw new BusinessException(e.getErrorCode());
        }
        logger.debug("撮合交易委托撤单 end--------------------------------------------------");
        return 0;
    }

    /**
     * 撮合交易
     * @param entrustModel
     * @param enableModel
     * @param fundModel
     * @throws BusinessException
     * @author zhangcx  2017年7月21日 上午10:30:55
     */
    private Long matchTradeEntrust(EntrustModel entrustModel, EnableModel enableModel, FundModel fundModel) throws BusinessException
    {
        logger.debug("撮合交易委托 start--------------------------------------------------");
        logger.debug("撮合交易委托 entrustModel:" + entrustModel.toString());
        logger.debug("撮合交易委托 enableModel:" + enableModel.toString());
        logger.debug("撮合交易委托 fundModel:" + fundModel.toString());
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(entrustModel.getStockinfoIdEx());
        if(stockInfo==null)
        {
            logger.debug("证券ID不存在，非法下单");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        // step1 判断可用足否
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect())) // 撮合现货买入
        {
            //  ---撮合现货买入---现货合约交易---开始----
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                logger.debug("交易标的证券ID："+stockInfo.getTradeStockinfoId());
                boolean isVCoin = stockInfo.getTradeStockinfoId().longValue()==entrustModel.getStockinfoId().longValue();
                logger.debug("是否数字货币标的："+isVCoin);
                // 可借 锁合约资产两笔:因为要借款
                accountContractAssetService.selectByPrimaryKeyOnRowLock(entrustModel.getAccountId(),entrustModel.getStockinfoId(),entrustModel.getStockinfoIdEx(),getStockInfo(entrustModel.getStockinfoIdEx()).getTableAsset());
                accountContractAssetService.selectByPrimaryKeyOnRowLock(entrustModel.getAccountId(),entrustModel.getStockinfoIdEx(),entrustModel.getStockinfoIdEx(),getStockInfo(entrustModel.getStockinfoIdEx()).getTableAsset());
                // 用户自动还款 法定货币
                accountDebitAssetService.doAccountDebitRepaymentToPlat((isVCoin?FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY:FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH), (isVCoin?entrustModel.getStockinfoIdEx():entrustModel.getStockinfoId()), entrustModel.getStockinfoIdEx(), entrustModel.getAccountId());
                // 判断法定货币可用
                enableModel.setStockinfoId((isVCoin?entrustModel.getStockinfoIdEx():entrustModel.getStockinfoId()));
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());// 法定货币
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) < 0)
                {
                    logger.debug("法定货币可用余额小于0");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmtEx()) < 0)
                {
                    // 现货合约交易 可借的时候去借款
                    if(StringUtils.equalsIgnoreCase(stockInfo.getCanBorrow(),FundConsts.PUBLIC_STATUS_YES)
                        && StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                    {
                        // 法定货币可用余额不足 向平台借法定货币
                        logger.debug("撮合交易委托 借款法定货币金额=" + (entrustModel.getEntrustAmtEx().subtract(enableModel.getEnableAmount())));
                        DebitAssetModel debitModel = new DebitAssetModel();
                        debitModel.setBorrowerAccountId(enableModel.getAccountId());
                        debitModel.setDebitAmt(entrustModel.getEntrustAmtEx().subtract(enableModel.getEnableAmount())); // 法定货币借款金额
                        debitModel.setStockinfoId(entrustModel.getStockinfoId());// 数字货币：与法定货币对应的数字货币
                        debitModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());// 法定货币
                        debitModel.setStockinfoType((isVCoin?FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY:FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH));//借款类型：法定货币借款
                        debitModel.setMaxShortLever(stockInfo.getMaxShortLever());
                        debitModel.setMaxLongLever(stockInfo.getMaxLongLever());
                        logger.debug("撮合交易委托 借款法定货币准备=" + debitModel.toString());
                        try
                        {
                            // 累计借款和本次借款是否已超过 数字货币折合法定货币 的数量 已在贷款业务中处理
                            debitModel = accountDebitAssetService.doDebitBorrowFromPlat(debitModel);
                            logger.debug("撮合交易委托 借款法定货币成功=" + debitModel.toString());
                        }
                        catch (BusinessException e)
                        {
                            logger.debug("撮合交易委托 借款法定货币失败=" + debitModel.toString());
                            throw new BusinessException(e.getErrorCode());
                        }
                    }
                    // 现货合约交易 不可借的时候 余额不足
                    else
                    {
                        logger.debug("法定货币可用余额不足");
                        throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                    }
                }
            }
            //  ---撮合现货买入---现货合约交易---结束----

            //  ---撮合现货买入---杠杆现货交易---开始----
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                // 可借 锁杠杆现货资产两笔:因为要借款
                accountSpotAssetService.selectForUpdate(entrustModel.getAccountId(),stockInfo.getCapitalStockinfoId(), stockInfo.getCapitalStockinfoId());
                accountSpotAssetService.selectForUpdate(entrustModel.getAccountId(),stockInfo.getTradeStockinfoId(), stockInfo.getCapitalStockinfoId());
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getCapitalStockinfoId(), entrustModel.getStockinfoIdEx(), entrustModel.getAccountId());
                enableModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) < 0)
                {
                    logger.debug("法定货币可用余额小于0");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmtEx()) < 0)
                {
                    Account account = accountService.selectByPrimaryKey(entrustModel.getAccountId());
                    if(StringUtils.equalsIgnoreCase(stockInfo.getCanBorrow(),FundConsts.PUBLIC_STATUS_YES) && (account.getAutoDebit().intValue() == 1)
                            && StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                    {
                        // 杠杆现货交易 可借的时候
                        logger.debug("撮合交易委托 钱包资产借款金额=" + (entrustModel.getEntrustAmtEx().subtract(enableModel.getEnableAmount())));
                        DebitAssetModel debitModel = new DebitAssetModel();
                        debitModel.setBorrowerAccountId(enableModel.getAccountId());
                        debitModel.setDebitAmt(entrustModel.getEntrustAmtEx().subtract(enableModel.getEnableAmount())); // 借款金额
                        debitModel.setStockinfoId(stockInfo.getCapitalStockinfoId());// 数字货币
                        debitModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());// 法定货币
                        debitModel.setStockinfoType(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH);
                        debitModel.setMaxShortLever(stockInfo.getMaxShortLever());
                        debitModel.setMaxLongLever(stockInfo.getMaxLongLever());
                        logger.debug("撮合交易委托 借款钱包资产准备=" + debitModel.toString());
                        try
                        {
                            // 累计借款和本次借款是否已超过 数字货币折合法定货币 的数量 已在贷款业务中处理
                            debitModel = accountDebitAssetService.doDebitBorrowFromPlat(debitModel);
                            logger.debug("撮合交易委托 借款钱包资产成功=" + debitModel.toString());
                        }
                        catch (BusinessException e)
                        {
                            logger.debug("撮合交易委托 借款钱包资产失败=" + debitModel.toString());
                            throw new BusinessException(e.getErrorCode());
                        }
                    }
                    // 杠杆现货交易 不可借的时候 余额不足
                    else
                    {
                        logger.debug("法定货币可用余额不足");
                        throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                    }
                }
            }
            //  ---撮合现货买入---杠杆现货交易---结束----

            //  ---撮合现货买入---纯现货交易---开始----
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                // 纯现货交易 是两个货币之间的交易 与交易对无关 取决于交易标的 和 计价标的
                // 买入的时候 查询计价标的
                accountWalletAssetService.selectForUpdate(entrustModel.getAccountId(),stockInfo.getCapitalStockinfoId());
                enableModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmtEx()) < 0)
                {
                    logger.debug("钱包资产买入交易：数字货币可用余额不足");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
            }
            //  ---撮合现货买入---纯现货交易---结束----
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }

        }
        else // 撮合现货卖出
        {
            //  ---撮合现货卖出---现货合约交易---开始----
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                logger.debug("交易标的证券ID："+stockInfo.getTradeStockinfoId());
                boolean isVCoin = stockInfo.getTradeStockinfoId().longValue()==entrustModel.getStockinfoId().longValue();
                logger.debug("是否数字货币标的："+isVCoin);
                // 可借 锁合约资产两笔:因为要借款
                accountContractAssetService.selectByPrimaryKeyOnRowLock(entrustModel.getAccountId(), entrustModel.getStockinfoId(), entrustModel.getStockinfoIdEx(),getStockInfo(entrustModel.getStockinfoIdEx()).getTableAsset());
                accountContractAssetService.selectByPrimaryKeyOnRowLock(entrustModel.getAccountId(), entrustModel.getStockinfoIdEx(), entrustModel.getStockinfoIdEx(),getStockInfo(entrustModel.getStockinfoIdEx()).getTableAsset());
                // 用户自动还款数字货币
                accountDebitAssetService.doAccountDebitRepaymentToPlat(
                        (isVCoin?FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH:FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY),
                        (isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx()),
                        entrustModel.getStockinfoIdEx(), entrustModel.getAccountId());
                // 判断数字货币可用
                enableModel.setStockinfoId((isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx()));
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());// 法定货币
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) < 0)
                {
                    logger.debug("数字货币可用余额小于0不能借");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmt()) < 0)
                {
                    // 可借 走合约资产可用判断(含借款)
                    if(StringUtils.equalsIgnoreCase(stockInfo.getCanBorrow(),FundConsts.PUBLIC_STATUS_YES)
                            && StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                    {
                        // 数字货币可用余额不足 向平台借数字货币
                        logger.debug("撮合交易委托 借款数字货币数量=" + (entrustModel.getEntrustAmt().subtract(enableModel.getEnableAmount())));
                        DebitAssetModel debitModel = new DebitAssetModel();
                        debitModel.setBorrowerAccountId(enableModel.getAccountId());
                        debitModel.setDebitAmt(entrustModel.getEntrustAmt().subtract(enableModel.getEnableAmount())); // 数字货币借款数量
                        debitModel.setStockinfoId(entrustModel.getStockinfoId());// 数字货币：与法定货币对应的数字货币
                        debitModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());// 法定货币
                        debitModel.setStockinfoType((isVCoin?FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH:FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY));
                        debitModel.setMaxShortLever(stockInfo.getMaxShortLever());
                        debitModel.setMaxLongLever(stockInfo.getMaxLongLever());
                        logger.debug("撮合交易委托 借款数字货币准备=" + debitModel.toString());
                        try
                        {
                            // 累计借款和本次借款是否已超过 数字货币折合法定货币 的数量 已在贷款业务中处理
                            debitModel = accountDebitAssetService.doDebitBorrowFromPlat(debitModel);
                            logger.debug("撮合交易委托 借款数字货币成功=" + debitModel.toString());
                        }
                        catch (BusinessException e)
                        {
                            logger.debug("撮合交易委托 借款数字货币失败=" + debitModel.toString());
                            throw new BusinessException(e.getErrorCode());
                        }
                    }
                    // 现货合约交易 不可借的时候 余额不足
                    else
                    {
                        logger.debug("法定货币可用余额不足");
                        throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                    }
                }
            }
            //  ---撮合现货卖出---现货合约交易---结束----

            //  ---撮合现货卖出---杠杆现货交易---开始----
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                accountSpotAssetService.selectForUpdate(entrustModel.getAccountId(), stockInfo.getTradeStockinfoId(), stockInfo.getCapitalStockinfoId());
                accountSpotAssetService.selectForUpdate(entrustModel.getAccountId(), stockInfo.getCapitalStockinfoId(), stockInfo.getCapitalStockinfoId());
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getTradeStockinfoId(), entrustModel.getStockinfoIdEx(), entrustModel.getAccountId());
                enableModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(BigDecimal.ZERO) < 0)
                {
                    logger.debug("钱包资产卖出杠杆现货交易：数字货币可用余额不足");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmt()) < 0)
                {
                    Account account = accountService.selectByPrimaryKey(enableModel.getAccountId());
                    // 可借 走合约资产可用判断(含借款)
                    if (StringUtils.equalsIgnoreCase(stockInfo.getCanBorrow(), FundConsts.PUBLIC_STATUS_YES)  && (account.getAutoDebit().intValue() == 1)
                            && StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                    {
                        // 数字货币可用余额不足 向平台借数字货币
                        logger.debug("撮合交易委托 借款数字货币数量=" + (entrustModel.getEntrustAmt().subtract(enableModel.getEnableAmount())));
                        DebitAssetModel debitModel = new DebitAssetModel();
                        debitModel.setBorrowerAccountId(enableModel.getAccountId());
                        debitModel.setDebitAmt(entrustModel.getEntrustAmt().subtract(enableModel.getEnableAmount()));
                        debitModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                        debitModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                        debitModel.setStockinfoType(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH);
                        debitModel.setMaxShortLever(stockInfo.getMaxShortLever());
                        debitModel.setMaxLongLever(stockInfo.getMaxLongLever());
                        logger.debug("撮合交易委托 借款数字货币准备=" + debitModel.toString());
                        try
                        {
                            // 累计借款和本次借款是否已超过 数字货币折合法定货币 的数量 已在贷款业务中处理
                            debitModel = accountDebitAssetService.doDebitBorrowFromPlat(debitModel);
                            logger.debug("撮合交易委托 钱包资产卖出杠杆现货交易 借款数字货币成功=" + debitModel.toString());
                        }
                        catch (BusinessException e)
                        {
                            logger.debug("撮合交易委托 钱包资产卖出杠杆现货交易 借款数字货币失败=" + debitModel.toString());
                            throw new BusinessException(e.getErrorCode());
                        }
                    }
                    // 现货合约交易 不可借的时候 余额不足
                    else
                    {
                        logger.debug("钱包资产卖出杠杆现货交易：数字货币可用余额不足");
                        throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                    }
                }
            }
            //  ---撮合现货卖出---杠杆现货交易---结束----

            //  ---撮合现货卖出---纯现货交易---开始----
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                // 纯现货交易 是两个货币之间的交易 与交易对无关 取决于交易标的 和 计价标的
                // 卖出的时候 查询交易标的
                accountWalletAssetService.selectForUpdate(entrustModel.getAccountId(),stockInfo.getTradeStockinfoId());
                enableModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmt()) < 0)
                {
                    logger.debug("钱包资产卖出交易：数字货币可用余额不足");
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
            }
            //  ---撮合现货卖出---纯现货交易---结束----
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }

        // step2 插入委托表等待成交
        EntrustVCoinMoney entrustx = new EntrustVCoinMoney();
        entrustx.setEntrustTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setAccountId(entrustModel.getAccountId());
        entrustx.setEntrustTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setBusinessFlag(fundModel.getBusinessFlag());
        entrustx.setTradeType(entrustModel.getTradeType());
        entrustx.setEntrustDirect(entrustModel.getEntrustDirect());
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
        {
            if (TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode().equals(entrustModel.getEntrustType()))
            {
                //  ---撮合现货买入---现货合约交易---开始----
                if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
                {
                    boolean isVCoin = stockInfo.getTradeStockinfoId().longValue()==entrustModel.getStockinfoId().longValue();
                    entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());// 数字货币
                    entrustx.setFeeStockinfoId(isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx());
                    entrustx.setEntrustAmt(entrustModel.getEntrustAmtEx()); // 市价买入是金额
                }
                //  ---撮合现货买入---现货合约交易---结束----

                //  ---撮合现货买入---杠杆现货交易---开始----
                else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                {

                }
                //  ---撮合现货买入---杠杆现货交易---结束----

                //  ---撮合现货买入---纯现货交易---开始----
                else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
                {

                }
                //  ---撮合现货买入---纯现货交易---结束----
                else
                {
                    logger.debug("证券信息类型错误");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }

            }
            else
            {
                //  ---撮合现货买入---现货合约交易---开始----
                if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
                {
                    boolean isVCoin = stockInfo.getTradeStockinfoId().longValue()==entrustModel.getStockinfoId().longValue();
                    entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());// 数字货币
                    entrustx.setFeeStockinfoId(isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx());
                    entrustx.setEntrustAmt(entrustModel.getEntrustAmt()); // 市价卖出是数量
                }
                //  ---撮合现货买入---现货合约交易---结束----

                //  ---撮合现货买入---杠杆现货交易---开始----
                else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                {
                    entrustx.setEntrustStockinfoId(stockInfo.getTradeStockinfoId());
                    entrustx.setFeeStockinfoId(stockInfo.getTradeStockinfoId());
                    entrustx.setEntrustAmt(entrustModel.getEntrustAmt());
                }
                //  ---撮合现货买入---杠杆现货交易---结束----

                //  ---撮合现货买入---纯现货交易---开始----
                else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
                {
                    entrustx.setEntrustStockinfoId(stockInfo.getTradeStockinfoId());
                    entrustx.setFeeStockinfoId(stockInfo.getTradeStockinfoId());
                    entrustx.setEntrustAmt(entrustModel.getEntrustAmt());
                }
                //  ---撮合现货买入---纯现货交易---结束----
                else
                {
                    logger.debug("证券信息类型错误");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }

            }
        }
        else
        {
            //  ---撮合现货卖出---现货合约交易---开始----
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                boolean isVCoin = stockInfo.getTradeStockinfoId().longValue()==entrustModel.getStockinfoId().longValue();
                entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());// 数字货币
                entrustx.setFeeStockinfoId(isVCoin?entrustModel.getStockinfoIdEx():entrustModel.getStockinfoId());
                entrustx.setEntrustAmt(entrustModel.getEntrustAmt());
            }
            //  ---撮合现货卖出---现货合约交易---结束----

            //  ---撮合现货卖出---杠杆现货交易---开始----
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                entrustx.setEntrustStockinfoId(stockInfo.getTradeStockinfoId());
                entrustx.setFeeStockinfoId(stockInfo.getCapitalStockinfoId());
                entrustx.setEntrustAmt(entrustModel.getEntrustAmt());
            }
            //  ---撮合现货卖出---杠杆现货交易---结束----

            //  ---撮合现货卖出---纯现货交易---开始----
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                entrustx.setEntrustStockinfoId(stockInfo.getTradeStockinfoId());
                entrustx.setFeeStockinfoId(stockInfo.getCapitalStockinfoId());
                entrustx.setEntrustAmt(entrustModel.getEntrustAmt());

            }
            //  ---撮合现货买入---纯现货交易---结束----
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        entrustx.setEntrustRelatedStockinfoId(entrustModel.getStockinfoIdEx());
        entrustx.setEntrustPrice(entrustModel.getEntrustPrice());
        entrustx.setEntrustRemark(entrustModel.getEntrustRemark());
        entrustx.setId(SerialnoUtils.buildPrimaryKey());
        entrustx.setDealAmt(BigDecimal.ZERO);
        entrustx.setDealFee(BigDecimal.ZERO);
        entrustx.setDealBalance(BigDecimal.ZERO);
        entrustx.setEntrustAccountType(entrustModel.getEntrustAccountType());// 默认用户下单0 如果是P用户委托下单 传入系统下单1
        entrustx.setEntrustSource(TradeEnums.ENTRUST_X_ENTRUST_SOURCE_WEB.getCode());
        entrustx.setEntrustType(entrustModel.getEntrustType());
        entrustx.setFeeRate(entrustModel.getFeeRate());
        entrustx.setStatus(TradeEnums.DEAL_STATUS_INIT.getCode());
        entrustx.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setTableName(entrustModel.getTableName());
        logger.debug("撮合交易委托 entrustVCoinMoney:" + entrustx.toString());
        entrustVCoinMoneyService.insert(entrustx);

        // setp3 增加证券冻结数量减少可用数量，并记录账户资金流水
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
        {
            if (entrustModel.getEntrustType().equals(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
            {
                fundModel.setAmount(entrustx.getEntrustAmt().multiply(entrustx.getEntrustPrice()));
            }
            else
            {
                fundModel.setAmount(entrustx.getEntrustAmt());
            }
            fundModel.setOriginalBusinessId(entrustx.getId());
            fundService.fundTransaction(fundModel);
        }
        else
        {
            fundModel.setAmount(entrustx.getEntrustAmt());
            fundModel.setOriginalBusinessId(entrustx.getId());
            fundService.fundTransaction(fundModel);
        }
        logger.debug("撮合交易委托 end--------------------------------------------------");
        return entrustx.getId();
    }

    /**
     * 交割撮合交易
     * @param entrustModel
     * @param enableModel
     * @param fundModel
     * @throws BusinessException
     * @author zhangcx  2017-10-28 08:39:51
     */
    private Long settlementTradeEntrust(EntrustModel entrustModel, EnableModel enableModel, FundModel fundModel) throws BusinessException
    {
        logger.debug("交割撮合交易委托 start--------------------------------------------------");
        logger.debug("交割撮合交易委托 entrustModel:" + entrustModel.toString());
        logger.debug("交割撮合交易委托 enableModel:" + enableModel.toString());
        logger.debug("交割撮合交易委托 fundModel:" + fundModel.toString());

        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(entrustModel.getStockinfoIdEx());
        if(stockInfo==null)
        {
            logger.debug("证券ID不存在，非法下单");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        logger.debug("交易标的证券ID："+stockInfo.getTradeStockinfoId());
        boolean isVCoin = stockInfo.getTradeStockinfoId().longValue()==entrustModel.getStockinfoId().longValue();
        logger.debug("是否数字货币标的："+isVCoin);

        // step1 判断可用足否
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect())) // 撮合现货买入数字货币
        {
            enableModel.setStockinfoId((isVCoin?entrustModel.getStockinfoIdEx():entrustModel.getStockinfoId()));
            enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());// 法定货币
            enableModel = enableService.entrustTerminalEnable(enableModel);
            if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmtEx()) < 0)
            {
                throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
            }
        }
        else // 撮合现货卖出数字货币
        {
            // 判断数字货币可用
            enableModel.setStockinfoId((isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx()));
            enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());// 法定货币
            enableModel = enableService.entrustTerminalEnable(enableModel);
            if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmt()) < 0)
            {
                throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
            }
        }

        // step2 插入委托表等待成交
        EntrustVCoinMoney entrustx = new EntrustVCoinMoney();
        entrustx.setEntrustTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setAccountId(entrustModel.getAccountId());
        entrustx.setEntrustTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setBusinessFlag(fundModel.getBusinessFlag());
        entrustx.setTradeType(entrustModel.getTradeType());
        entrustx.setEntrustDirect(entrustModel.getEntrustDirect());
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
        {
            if (TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode().equals(entrustModel.getEntrustType()))
            { // 如果是市价买入BTC委托
                entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());
                entrustx.setFeeStockinfoId(entrustModel.getStockinfoId());
                entrustx.setEntrustAmt(entrustModel.getEntrustAmtEx()); // 市价买入是金额
            }
            else
            {
                entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());
                entrustx.setFeeStockinfoId(entrustModel.getStockinfoId());
                entrustx.setEntrustAmt(entrustModel.getEntrustAmt()); // 市价卖出是数量
            }
        }
        else
        {
            entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());
            entrustx.setFeeStockinfoId(entrustModel.getStockinfoIdEx());
            entrustx.setEntrustAmt(entrustModel.getEntrustAmt());
        }
        entrustx.setEntrustPrice(entrustModel.getEntrustPrice());
        entrustx.setEntrustRemark(entrustModel.getEntrustRemark());
        entrustx.setId(SerialnoUtils.buildPrimaryKey());
        entrustx.setDealAmt(BigDecimal.ZERO);
        entrustx.setDealFee(BigDecimal.ZERO);
        entrustx.setDealBalance(BigDecimal.ZERO);
        entrustx.setEntrustAccountType(entrustModel.getEntrustAccountType());// 默认用户下单0 如果是P用户委托下单 传入系统下单1
        entrustx.setEntrustSource(TradeEnums.ENTRUST_X_ENTRUST_SOURCE_WEB.getCode());
        entrustx.setEntrustType(entrustModel.getEntrustType());
        entrustx.setFeeRate(entrustModel.getFeeRate());
        entrustx.setStatus(TradeEnums.DEAL_STATUS_INIT.getCode());
        entrustx.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setEntrustRelatedStockinfoId(entrustModel.getStockinfoIdEx());
        entrustx.setTableName(getStockInfo(entrustModel.getStockinfoIdEx()).getTableEntrust());
        logger.debug("交割撮合交易委托 entrustVCoinMoney:" + entrustx.toString());
        entrustVCoinMoneyService.insert(entrustx);
        // setp3 增加证券冻结数量减少可用数量，并记录账户资金流水
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
        {
            if (entrustModel.getEntrustType().equals(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
            {
                fundModel.setAmount(entrustx.getEntrustAmt().multiply(entrustx.getEntrustPrice()));
            }
            else
            {
                fundModel.setAmount(entrustx.getEntrustAmt());
            }
            fundModel.setOriginalBusinessId(entrustx.getId());
            fundService.fundTransaction(fundModel);
        }
        else
        {
            fundModel.setAmount(entrustx.getEntrustAmt());
            fundModel.setOriginalBusinessId(entrustx.getId());
            fundService.fundTransaction(fundModel);
        }
        logger.debug("交割撮合交易委托 end--------------------------------------------------");
        return entrustx.getId();
    }

    /**
     * 内部委托和成交
     * @param entrustModel    entrustModel
     * @return
     * @throws BusinessException
     * @author zcx  2017-10-11
     */
    @Override
    public void innerEntrustAndRealDeal(EntrustModel entrustModel, Long superAccountId) throws BusinessException
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(entrustModel.getStockinfoIdEx());
        EnableModel enableModel = new EnableModel();
        FundModel fundModel = new FundModel();
        // 组装
        this.constructEnableModelAndFundModelByEntrustModel(entrustModel, enableModel, fundModel);
        logger.debug("内部成交 委托开始");
        logger.debug("内部交易委托 start--------------------------------------------------");
        logger.debug("内部交易委托 entrustModel:" + entrustModel.toString());
        logger.debug("内部交易委托 enableModel:" + enableModel.toString());
        logger.debug("内部交易委托 fundModel:" + fundModel.toString());

        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue()!=stockInfo.getId());
        logger.debug("是否数字货币标的："+isVCoin);
        // step1 判断可用足否
        // 现货买入
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
        {
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                // 判断法定货币可用
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 内部交易时 不需要判断保证金
                enableModel.setStockinfoId((isVCoin?entrustModel.getStockinfoIdEx():entrustModel.getStockinfoId()));
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmtEx()) < 0)
                {
                    logger.debug("可用法定货币:" + enableModel.getEnableAmount() + "委托法定货币:" + entrustModel.getEntrustAmtEx());
                    // 委托可用不足异常
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                // 判断法定货币可用
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 内部交易时 不需要判断保证金
                enableModel.setStockinfoId(stockInfo.getCapitalStockinfoId());
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmtEx()) < 0)
                {
                    logger.debug("计价货币:" + enableModel.getEnableAmount() + "委托计价货币:" + entrustModel.getEntrustAmtEx());
                    // 委托可用不足异常
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        // 现货卖出
        else
        {
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                // 判断数字货币可用
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST); // 内部交易时 不需要判断保证金
                enableModel.setStockinfoId((isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx()));
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmt()) < 0)
                {
                    logger.debug("可用数字货币:" + enableModel.getEnableAmount() + "委托数字货币:" + entrustModel.getEntrustAmt());
                    // 委托可用不足异常
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                // 判断数字货币可用
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST); // 内部交易时 不需要判断保证金
                enableModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                enableModel.setRelatedStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel = enableService.entrustTerminalEnable(enableModel);
                if (enableModel.getEnableAmount().compareTo(entrustModel.getEntrustAmt()) < 0)
                {
                    logger.debug("可用交易货币:" + enableModel.getEnableAmount() + "委托交易货币:" + entrustModel.getEntrustAmt());
                    // 委托可用不足异常
                    throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
                }
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }

        // step2 插入委托表 状态已成交（当前用户）
        EntrustVCoinMoney entrustx = new EntrustVCoinMoney();
        entrustx.setAccountId(entrustModel.getAccountId());
        entrustx.setEntrustTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setBusinessFlag(fundModel.getBusinessFlag());
        entrustx.setTradeType(entrustModel.getTradeType());
        entrustx.setEntrustDirect(entrustModel.getEntrustDirect());
        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());
            if (entrustModel.getEntrustDirect().equalsIgnoreCase(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()))
            {
                entrustx.setFeeStockinfoId(isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx());
            }
            else
            {
                entrustx.setFeeStockinfoId(isVCoin?entrustModel.getStockinfoIdEx():entrustModel.getStockinfoId());
            }
        }
        else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());
            if (entrustModel.getEntrustDirect().equalsIgnoreCase(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()))
            {
                entrustx.setFeeStockinfoId(stockInfo.getTradeStockinfoId());
            }
            else
            {
                entrustx.setFeeStockinfoId(stockInfo.getCapitalStockinfoId());
            }
        }
        else
        {
            entrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());
            entrustx.setFeeStockinfoId(entrustModel.getStockinfoId());
        }
        entrustx.setEntrustAmt(entrustModel.getEntrustAmt());
        entrustx.setEntrustPrice(entrustModel.getEntrustPrice());
        entrustx.setEntrustRemark(entrustModel.getEntrustRemark());
        entrustx.setId(SerialnoUtils.buildPrimaryKey());
        entrustx.setDealAmt(entrustModel.getEntrustAmt());
        entrustx.setDealFee(BigDecimal.ZERO);
        entrustx.setDealBalance(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
        entrustx.setEntrustAccountType(entrustModel.getEntrustAccountType());// 默认用户下单0 如果是P用户委托下单 传入系统下单1
        entrustx.setEntrustSource(TradeEnums.ENTRUST_X_ENTRUST_SOURCE_WEB.getCode());
        entrustx.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustx.setFeeRate(BigDecimal.ZERO);
        entrustx.setStatus(TradeEnums.DEAL_STATUS_ALLDEAL.getCode());
        entrustx.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        entrustx.setRemark("内部交易 已成交 时间：" + CalendarUtils.dateFormat(new Date(), "yyyy-mm-dd hh:MM:SS"));
        entrustx.setTableName(entrustModel.getTableName());
        entrustx.setEntrustRelatedStockinfoId(entrustModel.getStockinfoIdEx());
        logger.debug("当前用户内部交易委托 entrustVCoinMoney:" + entrustx.toString());
        entrustVCoinMoneyService.insert(entrustx);

        // 插入委托表 状态已成交（超级用户）
        EntrustVCoinMoney SuperEntrustx = new EntrustVCoinMoney();
        SuperEntrustx.setAccountId(superAccountId);
        SuperEntrustx.setEntrustTime(new Timestamp(System.currentTimeMillis()));
        SuperEntrustx.setTradeType(entrustModel.getTradeType());
        SuperEntrustx.setEntrustStockinfoId(entrustModel.getStockinfoId());
        if(StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(),FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST))
        {
            SuperEntrustx.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
        }
        else
        {
            SuperEntrustx.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
        }
        if (entrustModel.getEntrustDirect().equalsIgnoreCase(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()))
        {
            SuperEntrustx.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode());// 用户买入数字货币 超级用户卖出数字货币
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                SuperEntrustx.setFeeStockinfoId(isVCoin?entrustModel.getStockinfoId():entrustModel.getStockinfoIdEx());
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                SuperEntrustx.setFeeStockinfoId(stockInfo.getCapitalStockinfoId());
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }

        }
        else
        {
            SuperEntrustx.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode());// 用户卖出数字货币 超级用户买入数字货币
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                SuperEntrustx.setFeeStockinfoId(isVCoin?entrustModel.getStockinfoIdEx():entrustModel.getStockinfoId());
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                SuperEntrustx.setFeeStockinfoId(stockInfo.getTradeStockinfoId());
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        SuperEntrustx.setEntrustAmt(entrustModel.getEntrustAmt());
        SuperEntrustx.setEntrustPrice(entrustModel.getEntrustPrice());
        SuperEntrustx.setEntrustRemark(entrustModel.getEntrustRemark());
        SuperEntrustx.setId(SerialnoUtils.buildPrimaryKey());
        SuperEntrustx.setDealAmt(entrustModel.getEntrustAmt());
        SuperEntrustx.setDealFee(BigDecimal.ZERO);
        SuperEntrustx.setDealBalance(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
        SuperEntrustx.setEntrustAccountType(entrustModel.getEntrustAccountType());// 默认用户下单0 如果是P用户委托下单 传入系统下单1
        SuperEntrustx.setEntrustSource(TradeEnums.ENTRUST_X_ENTRUST_SOURCE_WEB.getCode());
        SuperEntrustx.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        SuperEntrustx.setFeeRate(BigDecimal.ZERO);
        SuperEntrustx.setStatus(TradeEnums.DEAL_STATUS_ALLDEAL.getCode());
        SuperEntrustx.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        SuperEntrustx.setTableName(entrustModel.getTableName());
        SuperEntrustx.setRemark("内部交易 已成交 时间：" + CalendarUtils.dateFormat(new Date(), "yyyy-mm-dd hh:MM:SS"));
        SuperEntrustx.setEntrustRelatedStockinfoId(entrustModel.getStockinfoIdEx());
        logger.debug("超级用户内部交易委托 entrustVCoinMoney:" + SuperEntrustx.toString());
        entrustVCoinMoneyService.insert(SuperEntrustx);
        logger.debug("内部交易委托 end--------------------------------------------------");
        Long userEntrustxId = entrustx.getId();
        Long superEntrustxId = SuperEntrustx.getId();
        logger.debug("内部成交 用户委托ID=" + userEntrustxId);
        logger.debug("内部成交 委托结束!");
        // -----------------------------------------------------------
        logger.debug("内部成交 成交开始");
        logger.debug("内部成交根据对方委托进行无委托成交 start--------------------------------------------------");
        // 对委托记录状态进行拦截判断
        try
        {
            // step2 插入成交表
            // 成交者
            RealDealVCoinMoney realDealVCoinMoney = new RealDealVCoinMoney();
            realDealVCoinMoney.setBuyFee(BigDecimal.ZERO);
            if (entrustModel.getEntrustDirect().equalsIgnoreCase(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()))
            {
                realDealVCoinMoney.setBuyAccountId(entrustx.getAccountId()); // 普通用户买入
                realDealVCoinMoney.setBuyEntrustId(userEntrustxId);
                realDealVCoinMoney.setSellAccountId(superAccountId);// 超级用户卖出
                realDealVCoinMoney.setSellEntrustId(superEntrustxId);
                realDealVCoinMoney.setBuyFeeType(entrustModel.getStockinfoId());
                realDealVCoinMoney.setSellFeeType(entrustModel.getStockinfoIdEx());
                realDealVCoinMoney.setDealDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()); // 现货卖出
                realDealVCoinMoney.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平法定货币
            }
            else
            {
                realDealVCoinMoney.setSellAccountId(entrustx.getAccountId());// 普通用户卖出
                realDealVCoinMoney.setSellEntrustId(userEntrustxId);
                realDealVCoinMoney.setBuyAccountId(superAccountId); // 超级用户买入
                realDealVCoinMoney.setBuyEntrustId(superEntrustxId);
                realDealVCoinMoney.setBuyFeeType(entrustModel.getStockinfoIdEx());
                realDealVCoinMoney.setSellFeeType(entrustModel.getStockinfoId());
                realDealVCoinMoney.setDealDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()); // 现货买入
                realDealVCoinMoney.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平数字货币
            }
            realDealVCoinMoney.setDealAmt(entrustx.getDealAmt());
            realDealVCoinMoney.setDealPrice(entrustx.getEntrustPrice());
            realDealVCoinMoney.setDealBalance(realDealVCoinMoney.getDealAmt().multiply(realDealVCoinMoney.getDealPrice()));
            realDealVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_CLOSEPOSITIONTRADE.getCode()); // 爆仓强平成交
            realDealVCoinMoney.setDealStockinfoId(entrustx.getEntrustStockinfoId());
            realDealVCoinMoney.setDealTime(new Timestamp(System.currentTimeMillis()));
            realDealVCoinMoney.setRemark("内部成交 数字货币：" + entrustx.getDealAmt() + "个");
            realDealVCoinMoney.setSellFee(BigDecimal.ZERO);
            realDealVCoinMoney.setId(SerialnoUtils.buildPrimaryKey());
            realDealVCoinMoney.setTableName(getStockInfo(entrustModel.getStockinfoIdEx()).getTableRealDeal());
            logger.debug("内部成交 根据对方委托进行无委托成交 成交者realDeal:" + realDealVCoinMoney.toString());
            realDealVCoinMoneyService.insert(realDealVCoinMoney);
            // setp3 资产处理 两者一同处理
            // 成交者：减少本身证券当前数量，并记录账户资金流水 增加扩展证券当前数量，并记录账户资金流水
            // 委托者：减少本身证券冻结数量，减少本身证券当前数量，并记录账户资金流水 增加扩展证券当前数量，并记录账户资金流水
            FundModel fundModelExchange = new FundModel();
            fundModelExchange.setAccountId(entrustx.getAccountId());// 委托者ID
            if (entrustModel.getEntrustDirect().equalsIgnoreCase(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()))
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
                {
                    if(isVCoin)
                    {
                        // 有数字货币借款 卖出法定货币
                        fundModelExchange.setStockinfoId(entrustx.getEntrustRelatedStockinfoId());
                        fundModelExchange.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平
                        fundModelExchange.setAmount(entrustModel.getEntrustAmtEx());
                        fundModelExchange.setAmountEx(entrustModel.getEntrustAmtEx());
                    }
                    else
                    {
                        // 有法定货币借款 卖出数字货币
                        fundModelExchange.setStockinfoId(entrustx.getEntrustStockinfoId());
                        fundModelExchange.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平
                        fundModelExchange.setAmount(entrustModel.getEntrustAmtEx());
                        fundModelExchange.setAmountEx(entrustModel.getEntrustAmtEx());
                    }
                }
                else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                {
                    fundModelExchange.setStockinfoId(stockInfo.getCapitalStockinfoId());
                    fundModelExchange.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平
                    fundModelExchange.setAmount(entrustModel.getEntrustAmtEx());
                    fundModelExchange.setAmountEx(entrustModel.getEntrustAmtEx());
                }
                else
                {
                    logger.debug("证券信息类型错误");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }
            }
            else
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
                {
                    if(isVCoin)
                    {
                        // 有法币借款 卖出数字货币
                        fundModelExchange.setStockinfoId(entrustx.getEntrustStockinfoId());
                        fundModelExchange.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平
                        fundModelExchange.setAmount(entrustx.getEntrustAmt());
                        fundModelExchange.setAmountEx(fundModel.getAmountEx());
                    }
                    else
                    {
                        // 有数字货币借款 卖出法定货币
                        fundModelExchange.setStockinfoId(entrustx.getEntrustRelatedStockinfoId());
                        fundModelExchange.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平
                        fundModelExchange.setAmount(entrustx.getEntrustAmt());
                        fundModelExchange.setAmountEx(fundModel.getAmountEx());
                    }
                }
                else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
                {
                    fundModelExchange.setStockinfoId(stockInfo.getTradeStockinfoId());
                    fundModelExchange.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER); // 强平
                    fundModelExchange.setAmount(entrustx.getEntrustAmt());
                    fundModelExchange.setAmountEx(fundModel.getAmountEx());
                }
                else
                {
                    logger.debug("证券信息类型错误");
                    throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                }
            }
            logger.debug("内部成交资产处理之前："+fundModel.toString());
            logger.debug("内部成交资产处理之前："+entrustx.toString());
            fundModelExchange.setStockinfoIdEx(entrustModel.getStockinfoIdEx());
            fundModelExchange.setFee(BigDecimal.ZERO); // 费用0
            fundModelExchange.setOriginalBusinessId(realDealVCoinMoney.getId());
            fundModelExchange.setCreateBy(superAccountId);// 超级用户ID
            logger.debug("内部成交 根据对方委托进行无委托成交 成交者fundModel:" + fundModelExchange.toString());
            fundService.fundTransaction(fundModelExchange);
            logger.debug("内部成交 成交结束!");
        }
        catch (BusinessException e)
        {
            logger.error("根据对方委托进行无委托成交 错误:" + e.getMessage(), e);
            throw new BusinessException(e.getErrorCode());
        }
        logger.debug("根据对方委托进行无委托成交 end--------------------------------------------------");
    }

    /**
     * 构造组装enableModel与fundModel
     * @param entrustModel
     * @param enableModel
     * @param fundModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private void constructEnableModelAndFundModelByEntrustModel(EntrustModel entrustModel, EnableModel enableModel, FundModel fundModel)
    {
        logger.debug("构造组装enableModel与fundModel constructEnableModelAndFundModelByEntrustModel 开始------------------------------------");
        logger.debug("构造组装enableModel与fundModel constructEnableModelAndFundModelByEntrustModel entrustModel:" + entrustModel.toString());
        fundModel.setAccountId(entrustModel.getAccountId());
        fundModel.setStockinfoId(entrustModel.getStockinfoId());
        fundModel.setAmount(entrustModel.getEntrustAmt());
        fundModel.setStockinfoIdEx(entrustModel.getStockinfoIdEx());
        fundModel.setAmountEx(entrustModel.getEntrustAmtEx());
        fundModel.setFee(entrustModel.getFee()); // 费用
        enableModel.setAccountId(entrustModel.getAccountId());
        // Push交易
        if (TradeEnums.TRADE_TYPE_PUSHTRADE.getCode().equals(entrustModel.getTradeType()))
        {
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST);
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST);
            }
        } // 集市交易
        else if (TradeEnums.TRADE_TYPE_FAIRTRADE.getCode().equals(entrustModel.getTradeType()))
        {
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST);
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST);
            }
        } // 撮合交易
        else if (TradeEnums.TRADE_TYPE_MATCHTRADE.getCode().equals(entrustModel.getTradeType()))
        {
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoId());
                enableModel.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
            }
        } // 交割交易
        else if (TradeEnums.TRADE_TYPE_SETTLEMENTTRADE.getCode().equals(entrustModel.getTradeType()))
        {
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoId());
                enableModel.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
            }
        }// 爆仓强平交易
        else if (TradeEnums.TRADE_TYPE_CLOSEPOSITIONTRADE.getCode().equals(entrustModel.getTradeType()))
        {
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoId());
                enableModel.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustModel.getEntrustDirect()))
            {
                enableModel.setStockinfoId(entrustModel.getStockinfoIdEx());
                enableModel.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
            }
        }
        logger.debug("构造组装enableModel与fundModel constructEnableModelAndFundModelByEntrustModel enableModel:" + enableModel.toString());
        logger.debug("构造组装enableModel与fundModel constructEnableModelAndFundModelByEntrustModel fundModel:" + fundModel.toString());
        logger.debug("构造组装enableModel与fundModel constructEnableModelAndFundModelByEntrustModel 结束------------------------------------");
    }

    /**
     * 根据dealModel得到成交对应的业务类别
     * @param dealModel
     * @return String
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private String getEntrustDealBusinessFlagByDealModel(DealModel dealModel)
    {
        logger.debug("根据dealModel得到成交对应的业务类别 getEntrustDealBusinessFlagByDealModel 开始------------------------------------");
        logger.debug("根据dealModel得到成交对应的业务类别 getEntrustDealBusinessFlagByDealModel dealModel:" + dealModel.toString());
        String businessFlag = null;
        // 汇兑Push交易
        if (TradeEnums.TRADE_TYPE_PUSHTRADE.getCode().equals(dealModel.getTradeType()))
        {
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(dealModel.getDealDirect()))
            {
                // 汇兑Push交易现货买入委托成交
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_DEAL;
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(dealModel.getDealDirect()))
            {
                // 汇兑Push交易现货卖出委托成交
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_DEAL;
            }
        } // 集市交易
        else if (TradeEnums.TRADE_TYPE_FAIRTRADE.getCode().equals(dealModel.getTradeType()))
        {
            if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(dealModel.getDealDirect()))
            {
                // 集市交易现货买入委托成交
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_DEAL;
            }
            else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(dealModel.getDealDirect()))
            {
                // 集市交易现货卖出委托成交
                businessFlag = FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_DEAL;
            }
        }
        logger.debug("根据dealModel得到成交对应的业务类别 getEntrustDealBusinessFlagByDealModel businessFlag:" + businessFlag);
        logger.debug("根据dealModel得到成交对应的业务类别 getEntrustDealBusinessFlagByDealModel 结束------------------------------------");
        return businessFlag;
    }

    /**
     * 根据entrustDealBusinessFlag匹配得到对应的noEntrustDealBusinessFlag
     * @param businessFlag
     * @return String
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private String getNoEntrustDealBusinessFlagByMatchEntrustDealBusinessFlag(String businessFlag)
    {
        logger.debug(
                "根据entrustDealBusinessFlag匹配得到对应的noEntrustDealBusinessFlag getNoEntrustDealBusinessFlagByMatchEntrustDealBusinessFlag 开始------------------------------------");
        logger.debug("根据entrustDealBusinessFlag匹配得到对应的noEntrustDealBusinessFlag getNoEntrustDealBusinessFlagByMatchEntrustDealBusinessFlag businessFlag:" + businessFlag);
        String businessFlagMatch = null;
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_DEAL.equals(businessFlag))
        {
            // 汇兑Push交易现货买入无委托成交
            businessFlagMatch = FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_NOENTRUST_DEAL;
        }
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_DEAL.equals(businessFlag))
        {
            // 汇兑Push交易现货卖出无委托成交
            businessFlagMatch = FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_NOENTRUST_DEAL;
        }
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_DEAL.equals(businessFlag))
        {
            // 集市交易现货买入无委托成交
            businessFlagMatch = FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_NOENTRUST_DEAL;
        }
        else if (FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_DEAL.equals(businessFlag))
        {
            // 集市交易现货卖出无委托成交
            businessFlagMatch = FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_NOENTRUST_DEAL;
        }
        logger.debug("根据entrustDealBusinessFlag匹配得到对应的noEntrustDealBusinessFlag getNoEntrustDealBusinessFlagByMatchEntrustDealBusinessFlag businessFlagMatch:"
                + businessFlagMatch);
        logger.debug(
                "根据entrustDealBusinessFlag匹配得到对应的noEntrustDealBusinessFlag getNoEntrustDealBusinessFlagByMatchEntrustDealBusinessFlag 结束------------------------------------");
        return businessFlagMatch;
    }

    /**
     * 根据entrustDealDirect匹配得到对应的entrustDealDirect
     * @param entrustDealDirect
     * @return String
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private String getEntrustDealDirectByMatchEntrustDealDirect(String entrustDealDirect)
    {
        logger.debug("根据entrustDealDirect匹配得到对应的entrustDealDirect getEntrustDealDirectByMatchEntrustDealDirect 开始------------------------------------");
        logger.debug("根据entrustDealDirect匹配得到对应的entrustDealDirect getEntrustDealDirectByMatchEntrustDealDirect entrustDealDirect:" + entrustDealDirect);
        String entrustDealDirectMatch = null;
        if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode().equals(entrustDealDirect))
        {
            // 现货卖出
            entrustDealDirectMatch = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode();
        }
        else if (TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode().equals(entrustDealDirect))
        {
            // 现货买入
            entrustDealDirectMatch = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode();
        }
        logger.debug("根据entrustDealDirect匹配得到对应的entrustDealDirect getEntrustDealDirectByMatchEntrustDealDirect entrustDealDirectMatch:" + entrustDealDirectMatch);
        logger.debug("根据entrustDealDirect匹配得到对应的entrustDealDirect getEntrustDealDirectByMatchEntrustDealDirect 结束------------------------------------");
        return entrustDealDirectMatch;
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
        if (amount.compareTo(BigDecimal.ZERO) < 0) { throw new BusinessException(CommonEnums.RISK_FUND_AMOUNT_SMALL_ZERO); }
    }

    public  StockInfo  getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
