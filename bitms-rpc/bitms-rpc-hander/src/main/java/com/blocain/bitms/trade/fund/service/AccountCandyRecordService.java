/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountCandyRecord;

import java.math.BigDecimal;

/**
 * 账户糖果流水表 服务接口
 * <p>File：AccountCandyRecordService.java </p>
 * <p>Title: AccountCandyRecordService </p>
 * <p>Description:AccountCandyRecordService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountCandyRecordService extends GenericService<AccountCandyRecord>{

    AccountCandyRecord findLastRecord(Long stockinfoId);

    /**
     * 轮询交易奖励
     */
    void autoTradeAward();

    /**
     * 执行单笔交易奖励
     */
    void doTradeAward(Long accountId, Long stockinfoId, BigDecimal awardAmt);

    /**
     * 按日期串获取 例如：2018-03-14
     * @param date
     * @return
     */
    AccountCandyRecord findRecordByDateStrng(Long accountId, Long stockinfoId, String date);
}
