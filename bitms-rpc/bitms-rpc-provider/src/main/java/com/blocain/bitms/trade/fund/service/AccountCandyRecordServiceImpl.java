/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountCandyRecord;
import com.blocain.bitms.trade.fund.mapper.AccountCandyRecordMapper;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.model.FeeModel;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 账户糖果流水表 服务实现类
 * <p>File：AccountCandyRecordServiceImpl.java </p>
 * <p>Title: AccountCandyRecordServiceImpl </p>
 * <p>Description:AccountCandyRecordServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCandyRecordServiceImpl extends GenericServiceImpl<AccountCandyRecord> implements AccountCandyRecordService
{
    protected AccountCandyRecordMapper accountCandyRecordMapper;
    
    @Autowired
    FundService                        fundService;
    
    @Autowired
    AccountCandyRecordService          accountCandyRecordService;
    
    @Autowired
    StockInfoService                   stockInfoService;
    
    @Autowired
    EntrustVCoinMoneyService           entrustVCoinMoneyService;
    
    @Autowired
    public AccountCandyRecordServiceImpl(AccountCandyRecordMapper accountCandyRecordMapper)
    {
        super(accountCandyRecordMapper);
        this.accountCandyRecordMapper = accountCandyRecordMapper;
    }
    
    @Override
    public AccountCandyRecord findLastRecord(Long stockinfoId)
    {
        return accountCandyRecordMapper.findLastRecord(stockinfoId);
    }
    
    @Override
    public void autoTradeAward()
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_BTC2USD_TYPE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d = cal.getTime();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sp.format(d);
        String today = sp.format(new Date());
        List<FeeModel> listFeeModel = entrustVCoinMoneyService.selectSumFeeNeedAward(stockInfo.getTableFundCurrent(), yestoday, today);
        for(FeeModel feeModel:listFeeModel)
        {
           try
           {
               // 目前这个方法只支持结算昨天的交易奖励
               accountCandyRecordService.doTradeAward(feeModel.getAccountId(), FundConsts.WALLET_BITMS_TYPE, feeModel.getCaptalFee().multiply(BigDecimal.valueOf(100)));
           }catch(Exception e)
           {
               logger.error("定时轮询糖果奖励的交易状态失败" + e.getLocalizedMessage());
           }
        }
    }
    
    @Override
    public void doTradeAward(Long accountId, Long stockinfoId, BigDecimal awardAmt)
    {
        if (awardAmt.compareTo(BigDecimal.ZERO) <= 0 || stockinfoId == null || accountId == null)
        {
            logger.debug("accountId=" + accountId + ",stockinfoId=" + stockinfoId + ",awardAmt=" + awardAmt + "奖励失败");
        }
        else
        {
            logger.debug("accountId=" + accountId + ",stockinfoId=" + stockinfoId + ",awardAmt=" + awardAmt);
            // 4.奖励资金和流水处理
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(accountId);
            fundModel.setStockinfoId(stockinfoId);
            fundModel.setAmount(awardAmt);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD);
            fundService.fundTransaction(fundModel);
        }
    }
    
    @Override
    public AccountCandyRecord findRecordByDateStrng(Long accountId,Long stockinfoId, String date)
    {
        return accountCandyRecordMapper.findRecordByDateStrng(accountId,stockinfoId, date);
    }
}
