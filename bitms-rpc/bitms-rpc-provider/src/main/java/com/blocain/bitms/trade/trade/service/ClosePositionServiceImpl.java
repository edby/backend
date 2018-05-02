/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.monitor.entity.MonitorMargin;
import com.blocain.bitms.monitor.service.MonitorMarginService;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.fund.service.AccountSpotAssetService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.risk.service.RiskService;
import com.blocain.bitms.trade.settlement.entity.ClosePositionLog;
import com.blocain.bitms.trade.settlement.service.ClosePositionLogService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.EntrustModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 强制平仓业务处理
 * <p>File：ClosePositionServiceImpl.java</p>
 * <p>Title: ClosePositionServiceImpl</p>
 * <p>Description:ClosePositionServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class ClosePositionServiceImpl implements ClosePositionService
{
    public static final Logger  logger = LoggerFactory.getLogger(ClosePositionServiceImpl.class);
    
    @Autowired
    AccountDebitAssetService       accountDebitAssetService;
    
    @Autowired
    AccountContractAssetService    accountContractAssetService;

    @Autowired
    AccountSpotAssetService 	   accountSpotAssetService;
    
    @Autowired
    ClosePositionService           closePositionService;
    
    @Autowired
    EnableService                  enableService;
    
    @Autowired
    SysParameterService            sysParameterService;
    
    @Autowired(required = false)
    TradeService                   tradeService;
    
    @Autowired(required = false)
    EntrustVCoinMoneyService       entrustVCoinMoneyService;
    
    @Autowired
    ClosePositionLogService        closePositionLogService;
    
    @Autowired(required = false)
    MonitorMarginService           monitorMarginService;
    
    @Autowired(required = false)
    AccountService                 accountService;
    
    @Autowired(required = false)
    MsgRecordNoSql                 msgRecordService;
    
    @Autowired(required = false)
    StockInfoService               stockInfoService;
    
    @Autowired(required = false)
    RiskService                    riskService;
    
    /**
     * 强制平仓(自动定时)
     * @return
     */
    @Override
    public void autoClosePosition() throws BusinessException
    {
        // 撮合交易模块开关
        SysParameter paramsMatchTradeSwitch = new SysParameter();
        paramsMatchTradeSwitch.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        paramsMatchTradeSwitch.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        paramsMatchTradeSwitch = sysParameterService.getSysParameterByEntity(paramsMatchTradeSwitch);
        if (paramsMatchTradeSwitch == null)
        {
            logger.debug("===========paramsMatchTradeSwitch开关值空==========");
        }
        if (!StringUtils.isBlank(paramsMatchTradeSwitch.getValue()))
        {
            if (!paramsMatchTradeSwitch.getValue().equals("yes"))
            {
                logger.debug("===========paramsMatchTradeSwitch开关已关闭==========");
            }
            else
            {
                // 自动爆仓模块开关
                SysParameter paramsClosePositionSwitch = new SysParameter();
                paramsClosePositionSwitch.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
                paramsClosePositionSwitch.setParameterName(ParamConsts.CLOSEPOSITION_SWITCH);
                paramsClosePositionSwitch = sysParameterService.getSysParameterByEntity(paramsClosePositionSwitch);
                if (paramsClosePositionSwitch == null)
                {
                    logger.debug("===========paramsClosePositionSwitch开关值空==========");
                }
                if (!StringUtils.isBlank(paramsClosePositionSwitch.getValue()))
                {
                    if (!paramsClosePositionSwitch.getValue().equals("yes"))
                    {
                        logger.debug("===========paramsClosePositionSwitch开关已关闭==========");
                    }
                    else
                    {
                        try {
                            Thread.sleep(3000);

                            // 根据SQL查询得到临近爆仓的保证金监控记录
                            List<MonitorMargin> listMonitorMargin = monitorMarginService.findClosePositionDataList();
                            // 根据账户循环调用强制平仓
                            for (int i = 0; i < listMonitorMargin.size(); i++)
                            {
                                try
                                {
                                    MonitorMargin monitorMargin = listMonitorMargin.get(i);
                                    StockInfo stockInfoSelect = new StockInfo();
                                    stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
                                    stockInfoSelect.setId(monitorMargin.getStockinfoId());
                                    List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
                                    if (stockInfoList.size() > 0){
                                        StockInfo stockInfo = stockInfoList.get(0);
                                        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue()!=stockInfo.getId());
                                        Long exchangePairVCoin = (isVCoin?stockInfo.getTradeStockinfoId():stockInfo.getCapitalStockinfoId());
                                        // 爆仓调用处理
                                        closePositionService.doClosePositionSelect(String.valueOf(monitorMargin.getAccountId()), exchangePairVCoin, monitorMargin.getStockinfoId(), monitorMargin);
                                    }
                                }
                                catch (Exception exception)
                                {
                                    logger.error(exception.getMessage());
                                    continue; // 继续下一个爆仓用户爆仓
                                }
                            }
                        } catch (Exception exception) {
                            logger.error(exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                }
                else
                {
                    logger.debug("===========paramsClosePositionSwitch开关值不存在==========");
                }
            }
        }
        else
        {
            logger.debug("===========paramsMatchTradeSwitch开关值不存在==========");
        }
    }
    
    /**
     * 强制平仓
     * @param accountIds 账户ID 逗号分割
     * @param stockinfoId 数字货币证券ID
     * @param relatedStockinfoId 查询余额关联stockinfoId 法定货币证券ID
     * @return
     */
    @Override
    public void doClosePositionSelect(String accountIds, Long stockinfoId, Long relatedStockinfoId, MonitorMargin monitorMargin) throws Exception
    {
        int index = 0;
        logger.info("第" + (++index) + "条开始处理");
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        String accountIdArray[] = accountIds.split(",");
        for (String accountId : accountIdArray)
        {
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                try{
                    this.contractAssetClosePosition( accountId,  stockinfoId,  relatedStockinfoId,  monitorMargin);
                }
                catch (RuntimeException runtimeException)
                {
                    continue;
                }
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                try{
                    this.spotAssetClosePosition( accountId,  stockinfoId,  relatedStockinfoId,  monitorMargin);
                }
                catch (RuntimeException runtimeException)
                {
                    continue;
                }
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.info("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            else
            {
                logger.info("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 循环记录
            logger.info("第" + (index) + "条处理结束");
        }
    }

    public void spotAssetClosePosition(String accountId, Long stockinfoId, Long relatedStockinfoId, MonitorMargin monitorMargin) throws BusinessException
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        Account account = accountService.selectByPrimaryKey(Long.parseLong(accountId));
        // 锁住资产表
        accountSpotAssetService.selectForUpdate(Long.parseLong(accountId), stockInfo.getTradeStockinfoId(), stockInfo.getCapitalStockinfoId());// 交易标的货币
        accountSpotAssetService.selectForUpdate(Long.parseLong(accountId), stockInfo.getCapitalStockinfoId(), stockInfo.getCapitalStockinfoId());// 计价标的货币

        // 1.批量撤单 查询跟交易对相关的数据表
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyService.findAccountInEntrust(getStockInfo(relatedStockinfoId).getTableEntrust(),
                Long.parseLong(accountId));
        for (EntrustVCoinMoney entrustVCoinMoney : entrustVCoinMoneyList)
        {
            EntrustModel entrustModel = new EntrustModel();
            entrustModel.setEntrustId(entrustVCoinMoney.getId());
            entrustModel.setStockinfoId(stockInfo.getTradeStockinfoId());
            entrustModel.setStockinfoIdEx(relatedStockinfoId);
            entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
            tradeService.entrustWithdrawX(entrustModel);
        }
        logger.info("账户：" + accountId + "批量撤单完成...");

        // 2.普通用户自动还款
        accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getTradeStockinfoId(), relatedStockinfoId,
                Long.parseLong(accountId)); // 自动还交易标的借款
        accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getCapitalStockinfoId(), relatedStockinfoId,
                Long.parseLong(accountId)); // 自动还计价标的借款
        logger.info("账户：" + accountId + "自动还款完成...");

        // 查询计价标的借款情况
        AccountDebitAsset borrowLegalDebitRecord = new AccountDebitAsset();
        borrowLegalDebitRecord.setStockinfoId(stockInfo.getCapitalStockinfoId());// 计价标的
        borrowLegalDebitRecord.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
        borrowLegalDebitRecord.setBorrowerAccountId(Long.parseLong(accountId));
        borrowLegalDebitRecord.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        List<AccountDebitAsset> list = accountDebitAssetService.findList(borrowLegalDebitRecord);
        BigDecimal legalBorrow = BigDecimal.ZERO;
        if (list.size() > 0)
        {
            borrowLegalDebitRecord = list.get(0);
            legalBorrow = borrowLegalDebitRecord.getDebitAmt(); // 已借款金额
        }
        // 计价标的可用查询
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(Long.parseLong(accountId));
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);// 买数字货币算法定货币可用
        enableModel.setStockinfoId(stockInfo.getCapitalStockinfoId());// 计价标的
        enableModel.setRelatedStockinfoId(relatedStockinfoId);// 法定货币
        enableModel = enableService.entrustTerminalEnable(enableModel);
        BigDecimal legalEnable = enableModel.getEnableAmount();
        BigDecimal legalFrozen = enableModel.getFrozenAmt();
        // 账户法定货币余额
        BigDecimal legalAmtNet = legalEnable.add(legalFrozen).subtract(legalBorrow);
        logger.info("账户：" + accountId + " 计价标的净值：" + legalAmtNet);

        // 查询数字货币借款情况
        AccountDebitAsset borrowDigitalDebitRecord = new AccountDebitAsset();
        borrowDigitalDebitRecord.setStockinfoId(stockInfo.getTradeStockinfoId());// 交易标的
        borrowDigitalDebitRecord.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());// 法定货币
        borrowDigitalDebitRecord.setBorrowerAccountId(Long.parseLong(accountId));
        borrowDigitalDebitRecord.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        list = accountDebitAssetService.findList(borrowDigitalDebitRecord);
        BigDecimal digitalBorrow = BigDecimal.ZERO;
        if (list.size() > 0)
        {
            borrowDigitalDebitRecord = list.get(0);
            digitalBorrow = borrowDigitalDebitRecord.getDebitAmt(); // 已借款数字货币
        }
        // 数字货币可用查询
        enableModel = new EnableModel();
        enableModel.setAccountId(Long.parseLong(accountId));
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);// 卖数字货币算数字货币可用
        enableModel.setStockinfoId(stockInfo.getTradeStockinfoId());// 交易标的
        enableModel.setRelatedStockinfoId(relatedStockinfoId);// 法定货币
        enableModel = enableService.entrustTerminalEnable(enableModel);
        BigDecimal digitalEnable = enableModel.getEnableAmount();
        BigDecimal digitalFrozen = enableModel.getFrozenAmt();
        // 账户数字货币余额
        BigDecimal digitalAmtNet = digitalEnable.add(digitalFrozen).subtract(digitalBorrow);
        logger.info("账户：" + accountId + " 交易标的可用：" + digitalEnable);
        logger.info("账户：" + accountId + " 交易标的净值：" + digitalAmtNet);

        try
        {
            if (legalAmtNet.compareTo(BigDecimal.ZERO) < 0 && digitalAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                // 多头爆仓
                // 超级用户自动还款 数字货币资产加锁
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getTradeStockinfoId(), relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getCapitalStockinfoId(), relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                logger.info("账户：" + accountId + "多头爆仓.........进入..........");
                logger.info(new StringBuilder("账户：").append(borrowLegalDebitRecord.getBorrowerAccountId()).append("借款情况：")
                        .append(borrowLegalDebitRecord.getBorrowerAccountName()).append(borrowLegalDebitRecord.toString()).toString());
                // 爆仓价格=（每个账户法定货币净值/每个账户数字货币净值）取正
                if (digitalAmtNet.compareTo(BigDecimal.ZERO) == 0)
                {
                    logger.info("账户数字货币净值为0，自动平仓异常！");
                    String log = "用户：" + borrowLegalDebitRecord.getBorrowerAccountId() + "数字货币净值为0，自动平仓异常！";
                    insertClosePositionLog(borrowLegalDebitRecord, true, log, BigDecimal.ZERO);
                }
                else
                {
                    BigDecimal explosionPrice = legalAmtNet.divide(digitalAmtNet, 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(-1));
                    logger.info("账户：" + accountId + "爆仓价格" + explosionPrice);
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    if ((monitorMargin.getExplosionPrice().subtract(explosionPrice)).abs().compareTo(BigDecimal.ONE) > 0)
                    {
                        logger.info("多头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                        throw new BusinessException("多头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                    }
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    // 内部成交 当前账户 委托卖出 数字货币 超级账户全部成交
                    logger.info("内部成交 委托价格" + explosionPrice);
                    EntrustModel entrustModel = new EntrustModel();
                    entrustModel.setEntrustAmt(digitalEnable);
                    entrustModel.setEntrustPrice(explosionPrice);
                    entrustModel.setEntrustAmtEx(legalBorrow);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_CLOSEPOSITIONTRADE.getCode()); // 爆仓平仓交易
                    entrustModel.setAccountId(borrowLegalDebitRecord.getBorrowerAccountId());
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()); // 现货卖出
                    entrustModel.setStockinfoId(stockInfo.getTradeStockinfoId());// 交易标的
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);// 法定货币
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustAccountType(true); // 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.info("内部成交 委托数据准备：" + entrustModel.toString());
                    // 内部委托和成交
                    tradeService.innerEntrustAndRealDeal(entrustModel, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                    // ------------------------------------------------------------------------------------------
                    // 超级用户 增加向自己借的一笔借款 数量同当前用户借款数量
                    // 当前用户 账户借款数量清零
                    // -----------------------------------处理借款start------------------------------------------
                    if (borrowLegalDebitRecord.getDebitAmt() != null && borrowLegalDebitRecord.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                    {
                        borrowLegalDebitRecord.setRelatedStockinfoId(relatedStockinfoId);//这里要传入币对
                        accountDebitAssetService.doDebtMoveToPlat(borrowLegalDebitRecord, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID,
                                FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER);
                    }
                    else
                    {
                        logger.info("账户：" + accountId + "无债务...");
                    }
                    logger.info("账户：" + accountId + "债务转移完成...");
                    // -----------------------------------处理借款end-------------------------------------------
                    // 超级用户 委托挂单

                    BigDecimal entrustAmt = (digitalEnable);
                    double amount = entrustAmt.setScale(stockInfo.getSellAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                    entrustAmt = BigDecimal.valueOf(amount);

                    double price = explosionPrice.setScale(stockInfo.getSellPricePrecision(), BigDecimal.ROUND_HALF_UP).doubleValue();
                    explosionPrice = BigDecimal.valueOf(price);
                    logger.info("超级用户 委托价格" + explosionPrice);
                    logger.info("超级用户 委托开始");
                    entrustModel = new EntrustModel();
                    entrustModel.setEntrustAmt(entrustAmt);
                    entrustModel.setEntrustPrice(explosionPrice);
                    entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(explosionPrice));
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
                    entrustModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()); // 卖出数字货币
                    entrustModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                    entrustModel.setEntrustAccountType(true);// 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.info("超级用户 委托卖出数字货币：" + entrustModel.toString());
                    // 风控
                    riskService.entrustRisk(stockInfo, entrustModel.getAccountId(), entrustModel.getEntrustDirect(), entrustModel.getEntrustPrice(),
                            entrustModel.getEntrustType(), entrustModel.getStockinfoId(), entrustModel.getStockinfoIdEx());
                    logger.info("超级用户 委托开始");
                    tradeService.entrust(entrustModel);
                    logger.info("超级用户 委托结束");
                    // stockInfo.getStockName();
                    /*if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 短信提醒
                        if (StringUtils.isNotBlank(account.getMobNo()))
                        {// 确保手机已绑定过
                            String vagueMobile = StringUtils.vagueMobile(account.getMobNo());
                            String mobile = new StringBuffer(account.getCountry()).append(account.getMobNo()).toString();
                            msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CLOSE_POSITION_PHONE, account.getLang(), vagueMobile,
                                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                        }
                    }*/
                    if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 邮件提醒
                        msgRecordService.sendRemindEmail(account.getEmail(), MessageConst.REMIND_CLOSE_POSITION_EMAIL, "en_US",BitmsConst.HOST_EMAIL_LOGO_URL, account.getEmail(),
                                CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                    }
                }
                logger.info("账户：" + accountId + "多头爆仓.........结束..........");
            }
            else if (legalAmtNet.compareTo(BigDecimal.ZERO) > 0 && digitalAmtNet.compareTo(BigDecimal.ZERO) < 0)
            { // 空头爆仓
                // 超级用户自动还款 法定货币资产加锁
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getTradeStockinfoId(), relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockInfo.getCapitalStockinfoId(), relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                logger.info("账户：" + accountId + "空头爆仓.....开始......");
                logger.info(new StringBuilder("账户：").append(borrowDigitalDebitRecord.getBorrowerAccountId()).append(borrowDigitalDebitRecord.getBorrowerAccountName())
                        .append("借款情况：").append(borrowDigitalDebitRecord.toString()).toString());
                // 爆仓价格=（每个账户法定货币净值/每个账户数字货币净值）取正
                if (digitalAmtNet.compareTo(BigDecimal.ZERO) == 0)
                {
                    logger.info("账户数字货币净值为0，自动平仓异常！");
                    String log = "用户：" + borrowLegalDebitRecord.getBorrowerAccountId() + "数字货币净值为0，自动平仓异常！";
                    insertClosePositionLog(borrowLegalDebitRecord, true, log, BigDecimal.ZERO);
                }
                else
                {
                    BigDecimal explosionPrice = legalAmtNet.divide(digitalAmtNet, 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(-1));
                    logger.info("爆仓价格" + explosionPrice);
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    if ((monitorMargin.getExplosionPrice().subtract(explosionPrice)).abs().compareTo(BigDecimal.ONE) > 0)
                    {
                        logger.info("空头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                        throw new BusinessException("空头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                    }
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    // 内部成交 当前账户 委托买入 数字货币 超级账户全部成交
                    logger.info("内部成交 委托价格" + explosionPrice);
                    EntrustModel entrustModel = new EntrustModel();
                    BigDecimal entrustAmt = BigDecimal.ZERO;
                    entrustAmt = legalEnable.divide(explosionPrice, 4, BigDecimal.ROUND_HALF_UP);
                    entrustModel.setEntrustAmt(entrustAmt);
                    entrustModel.setEntrustPrice(explosionPrice);
                    entrustModel.setEntrustAmtEx(legalEnable);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_CLOSEPOSITIONTRADE.getCode()); // 爆仓强平交易
                    entrustModel.setAccountId(borrowDigitalDebitRecord.getBorrowerAccountId());
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()); // 现货买入数字货币
                    entrustModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustAccountType(true); // 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.info("内部成交 委托数据准备：" + entrustModel.toString());
                    // 内部委托和成交
                    tradeService.innerEntrustAndRealDeal(entrustModel, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                    // ------------------------------------------------------------------------------------------
                    // 超级用户 增加向自己借的一笔借款 数量同当前用户借款数量
                    // 当前用户 账户借款数量清零
                    // -----------------------------------处理借款start------------------------------------------
                    logger.info("账户处理借款start...");
                    if (borrowDigitalDebitRecord.getDebitAmt() != null && borrowDigitalDebitRecord.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                    {
                        borrowDigitalDebitRecord.setRelatedStockinfoId(relatedStockinfoId);//这里要传入币对
                        accountDebitAssetService.doDebtMoveToPlat(borrowDigitalDebitRecord, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID,
                                FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER);
                    }
                    else
                    {
                        logger.info("账户：" + accountId + "无债务...");
                    }
                    logger.info("账户：" + accountId + "债务转移完成...");
                    logger.info("账户处理借款end...");
                    // -----------------------------------处理借款end-------------------------------------------
                    // 超级用户 委托挂单
                    double price = explosionPrice.setScale(stockInfo.getBuyPricePrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                    explosionPrice = BigDecimal.valueOf(price);
                    logger.info("超级用户 委托价格" + explosionPrice);
                    logger.info("超级用户 委托开始");
                    entrustModel = new EntrustModel();
                    entrustAmt = BigDecimal.ZERO;
                    entrustAmt = legalEnable.divide(explosionPrice, 8, BigDecimal.ROUND_DOWN);
                    double amount = entrustAmt.setScale(stockInfo.getBuyAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                    entrustAmt = BigDecimal.valueOf(amount);
                    entrustModel.setEntrustAmt(entrustAmt);
                    entrustModel.setEntrustPrice(explosionPrice);
                    entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(explosionPrice));
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
                    entrustModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()); // 买入数字货币
                    entrustModel.setStockinfoId(stockInfo.getTradeStockinfoId());
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                    entrustModel.setEntrustAccountType(true);// 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.info("超级用户 委托买入数字货币：" + entrustModel.toString());
                    // 风控
                    riskService.entrustRisk(stockInfo, entrustModel.getAccountId(), entrustModel.getEntrustDirect(), entrustModel.getEntrustPrice(),
                            entrustModel.getEntrustType(), entrustModel.getStockinfoId(), entrustModel.getStockinfoIdEx());
                    // 撮合委托服务
                    logger.info("超级用户 委托开始");
                    tradeService.entrust(entrustModel);
                    logger.info("超级用户 委托结束");
                   /* if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 短信提醒
                        if (StringUtils.isNotBlank(account.getMobNo()))
                        {// 确保手机已绑定过
                            String vagueMobile = StringUtils.vagueMobile(account.getMobNo());
                            String mobile = new StringBuffer(account.getCountry()).append(account.getMobNo()).toString();
                            msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CLOSE_POSITION_PHONE, account.getLang(), vagueMobile,
                                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                        }
                    }*/
                    if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 邮件提醒
                        msgRecordService.sendRemindEmail(account.getEmail(), MessageConst.REMIND_CLOSE_POSITION_EMAIL, "en_US", BitmsConst.HOST_EMAIL_LOGO_URL,account.getEmail(),
                                CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                    }
                    logger.info("超级用户 委托结束");
                }
                logger.info("强制平仓操作结束................................................");
                logger.info("账户：" + accountId + "空头爆仓.....结束......");
            }
            else
            {
                logger.info("账户：" + accountId + "引爆爆仓,但是法定货币净资产大于零，数字货币净资产大于零!直接结束!");
                String log = "账户：" + accountId + "引爆爆仓,但是法定货币净资产大于零，数字货币净资产大于零!直接结束!";
                if(borrowDigitalDebitRecord == null)
                {
                    borrowDigitalDebitRecord = borrowLegalDebitRecord;
                    if(borrowDigitalDebitRecord == null)
                    {
                        borrowDigitalDebitRecord = new AccountDebitAsset();
                        borrowDigitalDebitRecord.setId(0L);
                        borrowDigitalDebitRecord.setDebitAmt(BigDecimal.ZERO);
                        borrowDigitalDebitRecord.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                        borrowDigitalDebitRecord.setLastPrice(BigDecimal.ZERO);
                        borrowDigitalDebitRecord.setStockinfoId(stockinfoId);
                        borrowDigitalDebitRecord.setBorrowerAccountId(Long.parseLong(accountId));
                    }
                }
                insertClosePositionLog(borrowDigitalDebitRecord, true, log, BigDecimal.ZERO);
            }
            // 更新保证监控表riskRate与chkResult
            monitorMargin.setRiskRate(BigDecimal.ZERO);
            monitorMargin.setChkResult(true);
            monitorMarginService.updateByPrimaryKeySelective(monitorMargin);
        }
        catch (RuntimeException runtimeException)
        {
            String log = "强制平仓过处处理 出错runtimeException:" + runtimeException.getMessage();
            logger.info(log);
            insertClosePositionLog(borrowDigitalDebitRecord, true, log, BigDecimal.ZERO);
        }
    }

    public void contractAssetClosePosition(String accountId, Long stockinfoId, Long relatedStockinfoId, MonitorMargin monitorMargin) throws BusinessException
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        logger.debug("是否为数字货币标的：" + isVCoin);
        Account account = accountService.selectByPrimaryKey(Long.parseLong(accountId));
        accountContractAssetService.selectByPrimaryKeyOnRowLock(Long.parseLong(accountId), stockinfoId, relatedStockinfoId,
                getStockInfo(relatedStockinfoId).getTableAsset());
        accountContractAssetService.selectByPrimaryKeyOnRowLock(Long.parseLong(accountId), relatedStockinfoId, relatedStockinfoId,
                getStockInfo(relatedStockinfoId).getTableAsset());
        // 1.批量撤单 查询跟交易对相关的数据表
        List<EntrustVCoinMoney> entrustVCoinMoneyList = entrustVCoinMoneyService.findAccountInEntrust(getStockInfo(relatedStockinfoId).getTableEntrust(),
                Long.parseLong(accountId));
        for (EntrustVCoinMoney entrustVCoinMoney : entrustVCoinMoneyList)
        {
            EntrustModel entrustModel = new EntrustModel();
            entrustModel.setEntrustId(entrustVCoinMoney.getId());
            entrustModel.setStockinfoId(stockinfoId);
            entrustModel.setStockinfoIdEx(relatedStockinfoId);
            entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
            tradeService.entrustWithdrawX(entrustModel);
        }
        logger.debug("账户：" + accountId + "批量撤单完成...");
        // 2.普通用户自动还款
        accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockinfoId, relatedStockinfoId,
                Long.parseLong(accountId));
        accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY, relatedStockinfoId, relatedStockinfoId,
                Long.parseLong(accountId));
        logger.debug("账户：" + accountId + "自动还款完成...");
        // 查询法定货币借款情况
        AccountDebitAsset borrowLegalDebitRecord = new AccountDebitAsset();
        borrowLegalDebitRecord.setStockinfoId(relatedStockinfoId);// 法定货币
        borrowLegalDebitRecord.setRelatedStockinfoId(relatedStockinfoId);// 法定货币
        borrowLegalDebitRecord.setBorrowerAccountId(Long.parseLong(accountId));
        borrowLegalDebitRecord.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        List<AccountDebitAsset> list = accountDebitAssetService.findList(borrowLegalDebitRecord);
        BigDecimal legalBorrow = BigDecimal.ZERO;
        if (list.size() > 0)
        {
            borrowLegalDebitRecord = list.get(0);
            legalBorrow = borrowLegalDebitRecord.getDebitAmt(); // 已借款金额
        }
        // 法定货币可用查询
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(Long.parseLong(accountId));
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);// 买数字货币算法定货币可用
        enableModel.setStockinfoId(relatedStockinfoId);// 法定货币
        enableModel.setRelatedStockinfoId(relatedStockinfoId);// 法定货币
        enableModel = enableService.entrustTerminalEnable(enableModel);
        BigDecimal legalEnable = enableModel.getEnableAmount();
        BigDecimal legalFrozen = enableModel.getFrozenAmt();
        // 账户法定货币余额
        BigDecimal legalAmtNet = legalEnable.add(legalFrozen).subtract(legalBorrow);
        logger.debug("账户：" + accountId + " 法定货币净值：" + legalAmtNet);
        // 查询数字货币借款情况
        AccountDebitAsset borrowDigitalDebitRecord = new AccountDebitAsset();
        borrowDigitalDebitRecord.setStockinfoId(stockinfoId);// 数字货币
        borrowDigitalDebitRecord.setRelatedStockinfoId(relatedStockinfoId);// 法定货币
        borrowDigitalDebitRecord.setBorrowerAccountId(Long.parseLong(accountId));
        borrowDigitalDebitRecord.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        list = accountDebitAssetService.findList(borrowDigitalDebitRecord);
        BigDecimal digitalBorrow = BigDecimal.ZERO;
        if (list.size() > 0)
        {
            borrowDigitalDebitRecord = list.get(0);
            digitalBorrow = borrowDigitalDebitRecord.getDebitAmt(); // 已借款数字货币
        }
        // 数字货币可用查询
        enableModel = new EnableModel();
        enableModel.setAccountId(Long.parseLong(accountId));
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);// 卖数字货币算数字货币可用
        enableModel.setStockinfoId(stockinfoId);// 数字货币
        enableModel.setRelatedStockinfoId(relatedStockinfoId);// 法定货币
        enableModel = enableService.entrustTerminalEnable(enableModel);
        BigDecimal digitalEnable = enableModel.getEnableAmount();
        BigDecimal digitalFrozen = enableModel.getFrozenAmt();
        // 账户数字货币余额
        BigDecimal digitalAmtNet = digitalEnable.add(digitalFrozen).subtract(digitalBorrow);
        logger.debug("账户：" + accountId + " 数字货币可用：" + digitalEnable);
        logger.debug("账户：" + accountId + " 数字货币净值：" + digitalAmtNet);
        BigDecimal temp = BigDecimal.ZERO;
        if (!isVCoin)
        { // 法币作为标的时
            temp = legalAmtNet;
            legalAmtNet = digitalAmtNet;
            digitalAmtNet = temp;
        }
        try
        {
            if (legalAmtNet.compareTo(BigDecimal.ZERO) < 0 && digitalAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                // 多头爆仓
                // 超级用户自动还款 数字货币资产加锁
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockinfoId, relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY, relatedStockinfoId, relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                logger.debug("账户：" + accountId + "多头爆仓.........进入..........");
                logger.debug(new StringBuilder("账户：").append(borrowLegalDebitRecord.getBorrowerAccountId()).append("借款情况：")
                        .append(borrowLegalDebitRecord.getBorrowerAccountName()).append(borrowLegalDebitRecord.toString()).toString());
                // 爆仓价格=（每个账户法定货币净值/每个账户数字货币净值）取正
                if (digitalAmtNet.compareTo(BigDecimal.ZERO) == 0)
                {
                    logger.debug("账户数字货币净值为0，自动平仓异常！");
                    String log = "用户：" + borrowLegalDebitRecord.getBorrowerAccountId() + "数字货币净值为0，自动平仓异常！";
                    insertClosePositionLog(borrowLegalDebitRecord, true, log, BigDecimal.ZERO);
                }
                else
                {
                    BigDecimal explosionPrice = legalAmtNet.divide(digitalAmtNet, 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(-1));
                    logger.debug("账户：" + accountId + "爆仓价格" + explosionPrice);
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    if ((monitorMargin.getExplosionPrice().subtract(explosionPrice)).abs().compareTo(BigDecimal.ONE) > 0)
                    {
                        logger.debug("多头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                        throw new BusinessException("多头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                    }
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    // 内部成交 当前账户 委托卖出 数字货币 超级账户全部成交
                    logger.debug("内部成交 委托价格" + explosionPrice);
                    EntrustModel entrustModel = new EntrustModel();
                    entrustModel.setEntrustAmt(isVCoin ? digitalEnable : legalEnable);
                    entrustModel.setEntrustPrice(explosionPrice);
                    entrustModel.setEntrustAmtEx(isVCoin ?legalBorrow:digitalBorrow);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_CLOSEPOSITIONTRADE.getCode()); // 爆仓平仓交易
                    entrustModel.setAccountId(borrowLegalDebitRecord.getBorrowerAccountId());
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()); // 现货卖出
                    entrustModel.setStockinfoId(stockinfoId);// 数字货币
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);// 法定货币
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustAccountType(true); // 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.debug("内部成交 委托数据准备：" + entrustModel.toString());
                    // 内部委托和成交
                    tradeService.innerEntrustAndRealDeal(entrustModel, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                    // ------------------------------------------------------------------------------------------
                    // 超级用户 增加向自己借的一笔借款 数量同当前用户借款数量
                    // 当前用户 账户借款数量清零
                    // -----------------------------------处理借款start------------------------------------------
                    if (isVCoin)
                    {
                        if (borrowLegalDebitRecord.getDebitAmt() != null && borrowLegalDebitRecord.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetService.doDebtMoveToPlat(borrowLegalDebitRecord, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID,
                                    FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER);
                        }
                        else
                        {
                            logger.debug("账户：" + accountId + "无债务...");
                        }
                    }
                    else
                    {
                        if (borrowDigitalDebitRecord.getDebitAmt() != null && borrowDigitalDebitRecord.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetService.doDebtMoveToPlat(borrowDigitalDebitRecord, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID,
                                    FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER);
                        }
                        else
                        {
                            logger.debug("账户：" + accountId + "无债务...");
                        }
                    }
                    logger.debug("账户：" + accountId + "债务转移完成...");
                    // -----------------------------------处理借款end-------------------------------------------
                    // 超级用户 委托挂单

                    BigDecimal entrustAmt = (isVCoin ? digitalEnable : legalEnable);
                    double amount = entrustAmt.setScale(stockInfo.getSellAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                    entrustAmt = BigDecimal.valueOf(amount);

                    double price = explosionPrice.setScale(stockInfo.getSellPricePrecision(), BigDecimal.ROUND_HALF_UP).doubleValue();
                    explosionPrice = BigDecimal.valueOf(price);
                    logger.debug("超级用户 委托价格" + explosionPrice);
                    logger.debug("超级用户 委托开始");
                    entrustModel = new EntrustModel();
                    entrustModel.setEntrustAmt(entrustAmt);
                    entrustModel.setEntrustPrice(explosionPrice);
                    entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(explosionPrice));
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
                    entrustModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID);
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode()); // 卖出数字货币
                    entrustModel.setStockinfoId(stockinfoId);
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                    entrustModel.setEntrustAccountType(true);// 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.debug("超级用户 委托卖出数字货币：" + entrustModel.toString());
                    // 风控
                    riskService.entrustRisk(stockInfo, entrustModel.getAccountId(), entrustModel.getEntrustDirect(), entrustModel.getEntrustPrice(),
                            entrustModel.getEntrustType(), entrustModel.getStockinfoId(), entrustModel.getStockinfoIdEx());
                    logger.debug("超级用户 委托开始");
                    tradeService.entrust(entrustModel);
                    logger.debug("超级用户 委托结束");
                    // stockInfo.getStockName();
                   /* if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 短信提醒
                        if (StringUtils.isNotBlank(account.getMobNo()))
                        {// 确保手机已绑定过
                            String vagueMobile = StringUtils.vagueMobile(account.getMobNo());
                            String mobile = new StringBuffer(account.getCountry()).append(account.getMobNo()).toString();
                            msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CLOSE_POSITION_PHONE, account.getLang(), vagueMobile,
                                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                        }
                    }*/
                    if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 邮件提醒
                        msgRecordService.sendRemindEmail(account.getEmail(), MessageConst.REMIND_CLOSE_POSITION_EMAIL, "en_US",BitmsConst.HOST_EMAIL_LOGO_URL, account.getEmail(),
                                CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                    }
                }
                logger.debug("账户：" + accountId + "多头爆仓.........结束..........");
            }
            else if (legalAmtNet.compareTo(BigDecimal.ZERO) > 0 && digitalAmtNet.compareTo(BigDecimal.ZERO) < 0)
            { // 空头爆仓
                // 超级用户自动还款 法定货币资产加锁
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_DIGITAL_CASH, stockinfoId, relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                accountDebitAssetService.doAccountDebitRepaymentToPlat(FundConsts.BORROW_STOCKINFO_TYPE_LEGAL_MONEY, relatedStockinfoId, relatedStockinfoId,
                        FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                logger.debug("账户：" + accountId + "空头爆仓.....开始......");
                logger.debug(new StringBuilder("账户：").append(borrowDigitalDebitRecord.getBorrowerAccountId()).append(borrowDigitalDebitRecord.getBorrowerAccountName())
                        .append("借款情况：").append(borrowDigitalDebitRecord.toString()).toString());
                // 爆仓价格=（每个账户法定货币净值/每个账户数字货币净值）取正
                if (digitalAmtNet.compareTo(BigDecimal.ZERO) == 0)
                {
                    logger.debug("账户数字货币净值为0，自动平仓异常！");
                    String log = "用户：" + borrowLegalDebitRecord.getBorrowerAccountId() + "数字货币净值为0，自动平仓异常！";
                    insertClosePositionLog(borrowLegalDebitRecord, true, log, BigDecimal.ZERO);
                }
                else
                {
                    BigDecimal explosionPrice = legalAmtNet.divide(digitalAmtNet, 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(-1));
                    logger.debug("爆仓价格" + explosionPrice);
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    if ((monitorMargin.getExplosionPrice().subtract(explosionPrice)).abs().compareTo(BigDecimal.ONE) > 0)
                    {
                        logger.debug("空头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                        throw new BusinessException("空头爆仓 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓!");
                    }
                    // 监控算出来的爆仓价 不等于 应爆实时算出来的爆仓价 就直接返回不爆仓
                    // 内部成交 当前账户 委托买入 数字货币 超级账户全部成交
                    logger.debug("内部成交 委托价格" + explosionPrice);
                    EntrustModel entrustModel = new EntrustModel();
                    BigDecimal entrustAmt = BigDecimal.ZERO;
                    if (isVCoin)
                    {
                        entrustAmt = legalEnable.divide(explosionPrice, 4, BigDecimal.ROUND_HALF_UP);
                        entrustModel.setEntrustAmt(entrustAmt);
                        entrustModel.setEntrustPrice(explosionPrice);
                        entrustModel.setEntrustAmtEx(legalEnable);
                    }
                    else
                    {
                        entrustAmt = digitalEnable.divide(explosionPrice, 4, BigDecimal.ROUND_HALF_UP);
                        entrustModel.setEntrustAmt(entrustAmt);
                        entrustModel.setEntrustPrice(explosionPrice);
                        entrustModel.setEntrustAmtEx(digitalEnable);
                    }
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_CLOSEPOSITIONTRADE.getCode()); // 爆仓强平交易
                    entrustModel.setAccountId(borrowDigitalDebitRecord.getBorrowerAccountId());
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()); // 现货买入数字货币
                    entrustModel.setStockinfoId(stockinfoId);
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustAccountType(true); // 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.debug("内部成交 委托数据准备：" + entrustModel.toString());
                    // 内部委托和成交
                    tradeService.innerEntrustAndRealDeal(entrustModel, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                    // ------------------------------------------------------------------------------------------
                    // 超级用户 增加向自己借的一笔借款 数量同当前用户借款数量
                    // 当前用户 账户借款数量清零
                    // -----------------------------------处理借款start------------------------------------------
                    logger.debug("账户处理借款start...");
                    if (isVCoin)
                    {
                        if (borrowDigitalDebitRecord.getDebitAmt() != null && borrowDigitalDebitRecord.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetService.doDebtMoveToPlat(borrowDigitalDebitRecord, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID,
                                    FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER);
                        }
                        else
                        {
                            logger.debug("账户：" + accountId + "无债务...");
                        }
                    }
                    else
                    {
                        if (borrowLegalDebitRecord.getDebitAmt() != null && borrowLegalDebitRecord.getDebitAmt().compareTo(BigDecimal.ZERO) > 0)
                        {
                            accountDebitAssetService.doDebtMoveToPlat(borrowLegalDebitRecord, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID,
                                    FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_DEBIT_TRANSFER);
                        }
                        else
                        {
                            logger.debug("账户：" + accountId + "无债务...");
                        }
                    }
                    logger.debug("账户：" + accountId + "债务转移完成...");
                    logger.debug("账户处理借款end...");
                    // -----------------------------------处理借款end-------------------------------------------
                    // 超级用户 委托挂单
                    double price = explosionPrice.setScale(stockInfo.getBuyPricePrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                    explosionPrice = BigDecimal.valueOf(price);
                    logger.debug("超级用户 委托价格" + explosionPrice);
                    logger.debug("超级用户 委托开始");
                    entrustModel = new EntrustModel();
                    entrustAmt = BigDecimal.ZERO;
                    if (isVCoin)
                    {
                        entrustAmt = legalEnable.divide(explosionPrice, 8, BigDecimal.ROUND_DOWN);
                        double amount = entrustAmt.setScale(stockInfo.getBuyAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                        entrustAmt = BigDecimal.valueOf(amount);
                        entrustModel.setEntrustAmt(entrustAmt);
                        entrustModel.setEntrustPrice(explosionPrice);
                        entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(explosionPrice));
                    }
                    else
                    {
                        entrustAmt = digitalEnable.divide(explosionPrice, 8, BigDecimal.ROUND_DOWN);
                        double amount = entrustAmt.setScale(stockInfo.getBuyAmountPrecision(), BigDecimal.ROUND_DOWN).doubleValue();
                        entrustAmt = BigDecimal.valueOf(amount);
                        entrustModel.setEntrustAmt(entrustAmt);
                        entrustModel.setEntrustPrice(explosionPrice);
                        entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(explosionPrice));
                    }
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
                    entrustModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
                    entrustModel.setEntrustDirect(TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode()); // 买入数字货币
                    entrustModel.setStockinfoId(stockinfoId);
                    entrustModel.setStockinfoIdEx(relatedStockinfoId);
                    entrustModel.setFee(BigDecimal.ZERO);
                    entrustModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
                    entrustModel.setEntrustAccountType(true);// 系统下单
                    entrustModel.setTableName(getStockInfo(relatedStockinfoId).getTableEntrust());
                    logger.debug("超级用户 委托买入数字货币：" + entrustModel.toString());
                    // 风控
                    riskService.entrustRisk(stockInfo, entrustModel.getAccountId(), entrustModel.getEntrustDirect(), entrustModel.getEntrustPrice(),
                            entrustModel.getEntrustType(), entrustModel.getStockinfoId(), entrustModel.getStockinfoIdEx());
                    // 撮合委托服务
                    logger.debug("超级用户 委托开始");
                    tradeService.entrust(entrustModel);
                    logger.debug("超级用户 委托结束");
                   /* if (BitmsConst.REMIND_PHONE_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 短信提醒
                        if (StringUtils.isNotBlank(account.getMobNo()))
                        {// 确保手机已绑定过
                            String vagueMobile = StringUtils.vagueMobile(account.getMobNo());
                            String mobile = new StringBuffer(account.getCountry()).append(account.getMobNo()).toString();
                            msgRecordService.sendRemindSMS(mobile, MessageConst.REMIND_CLOSE_POSITION_PHONE, account.getLang(), vagueMobile,
                                    CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                        }
                    }*/
                    if (BitmsConst.REMIND_EMAIL_SWITCH.equals(BitmsConst.SWITCH_ENABLE))
                    {// 邮件提醒
                        msgRecordService.sendRemindEmail(account.getEmail(), MessageConst.REMIND_CLOSE_POSITION_EMAIL, "en_US", BitmsConst.HOST_EMAIL_LOGO_URL,account.getEmail(),
                                CalendarUtils.getCurrentDate(DateConst.DATE_FORMAT_YMDHMS));
                    }
                    logger.debug("超级用户 委托结束");
                }
                logger.debug("强制平仓操作结束................................................");
                logger.debug("账户：" + accountId + "空头爆仓.....结束......");
            }
            else
            {
                logger.debug("账户：" + accountId + "引爆爆仓,但是法定货币净资产大于零，数字货币净资产大于零!直接结束!");
                String log = "账户：" + accountId + "引爆爆仓,但是法定货币净资产大于零，数字货币净资产大于零!直接结束!";
                if(borrowDigitalDebitRecord == null)
                {
                    borrowDigitalDebitRecord = borrowLegalDebitRecord;
                    if(borrowDigitalDebitRecord == null)
                    {
                        borrowDigitalDebitRecord = new AccountDebitAsset();
                        borrowDigitalDebitRecord.setId(0L);
                        borrowDigitalDebitRecord.setDebitAmt(BigDecimal.ZERO);
                        borrowDigitalDebitRecord.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
                        borrowDigitalDebitRecord.setLastPrice(BigDecimal.ZERO);
                        borrowDigitalDebitRecord.setStockinfoId(stockinfoId);
                        borrowDigitalDebitRecord.setBorrowerAccountId(Long.parseLong(accountId));
                    }
                }
                insertClosePositionLog(borrowDigitalDebitRecord, true, log, BigDecimal.ZERO);
            }
            // 更新保证监控表riskRate与chkResult
            monitorMargin.setRiskRate(BigDecimal.ZERO);
            monitorMargin.setChkResult(true);
            monitorMarginService.updateByPrimaryKeySelective(monitorMargin);
        }
        catch (RuntimeException runtimeException)
        {
            String log = "强制平仓过处处理 出错runtimeException:" + runtimeException.getMessage();
            logger.debug(log);
            insertClosePositionLog(borrowDigitalDebitRecord, true, log, BigDecimal.ZERO);
        }
    }
    
    /**
     * 新增平仓操纵日志
     * @param record
     * @param status
     * @param remark
     * @param btcUsdxInnerPrice
     */
    public void insertClosePositionLog(AccountDebitAsset record, boolean status, String remark, BigDecimal btcUsdxInnerPrice)
    {
        if(record == null)
        {
            record = new AccountDebitAsset();
            record.setId(0L);
            record.setDebitAmt(BigDecimal.ZERO);
            record.setLenderAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            record.setLastPrice(BigDecimal.ZERO);
            record.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            record.setBorrowerAccountId(0L);
        }
        if(null == record.getLastPrice())
        {
            record.setLastPrice(BigDecimal.ZERO);
        }
        ClosePositionLog log = new ClosePositionLog();
        log.setBorrowerAccountId(record.getBorrowerAccountId());
        log.setDebitId(record.getId());
        log.setDebitAmt(record.getDebitAmt());
        log.setLastPrice(btcUsdxInnerPrice);
        log.setLenderAccountId(record.getLenderAccountId());
        log.setMonitorLastPrice(record.getLastPrice());
        log.setRemark(remark);
        log.setStatus(status);
        log.setStockinfoId(record.getStockinfoId());
        log.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        log.setMonitorDate(new Timestamp(System.currentTimeMillis()));
        log.setMonitorMarginRatio(BigDecimal.ZERO);
        logger.debug("插入日志前："+log.toString());
        closePositionLogService.insert(log);
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
