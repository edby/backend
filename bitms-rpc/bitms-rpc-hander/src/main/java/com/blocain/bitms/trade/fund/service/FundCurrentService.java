/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.model.FundModel;

import java.math.BigDecimal;

/**
 * 提币 事务处理
 * <p>File：FundCurrentService.java</p>
 * <p>Title: FundCurrentService</p>
 * <p>Description:FundCurrentService</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public interface FundCurrentService
{
    /**
     * ERC20 充值
     * @param fundModel 充值MODEL
     * @Param blockTransConfirmERC20Id 区块信息表ID
     */
    void doRechargeERC20(FundModel fundModel,Long blockTransConfirmERC20Id) throws BusinessException;

    /**
     * 提币申请-过期
     * @param fundModel
     */
    @Deprecated
    void doWithdraw(String lang, FundModel fundModel, String fundPwd, String activeStatus, String type, String checkCode) throws BusinessException;

    /**
     * 提币申请
     * @param fundModel
     */
    AccountFundWithdraw doApplyWithdraw(String lang, FundModel fundModel, String activeStatus, String certStatus) throws BusinessException;

    /**
     * 提币确认
     * @param accountFundWithdraw
     * @param confirmCode
     * @param lang
     */
    void doComfirmWithdraw(AccountFundWithdraw accountFundWithdraw ,String confirmCode,String lang) throws BusinessException;

    /**
     * 提币申请--ERC20 TOKE
     * @param fundModel
     */
    AccountFundWithdraw doApplyWithdrawERC20(String lang, FundModel fundModel, String activeStatus, String certStatus) throws BusinessException;

    /**
     * 提币确认--ERC20 TOKE
     * @param accountFundWithdraw
     * @param confirmCode
     * @param lang
     */
    void doComfirmWithdrawERC20(AccountFundWithdraw accountFundWithdraw ,String confirmCode,String lang) throws BusinessException;

    /**
     * 提币激活
     * @param accountFundWithdraw
     * @throws BusinessException
     */
    void doActiveWithdraw(AccountFundWithdraw accountFundWithdraw) throws BusinessException;

    /**
    * 取消提币申请
    * @param id
    * @param accountId
    * @throws BusinessException
    */
    void doWithdrawCancel(Long id, Long accountId,Long exchangePairMoney) throws BusinessException;
    
    /**
     * 添加提币地址
     * @param lang
     * @param accountCollectAddre
     * @throws BusinessException
     */
    void doWithdrawAddrAdd(String lang, AccountCollectAddr accountCollectAddre) throws BusinessException;
    
    /**
     * 强增强减 事务
     * @param accountFundAdjust
     * @param fundModel
     */
    void doSaveAccountFundAdjust(AccountFundAdjust accountFundAdjust, FundModel fundModel) throws BusinessException;
    
    /**
     * 提币划拨 事务
     * @param ids
     * @param accountId
     */
    void doAllot(String ids, Long accountId);

    /**
     * 提币审核
     * @param accountWithdrawRecord
     * @param bossUserId
     */
    void doApproval(AccountWithdrawRecord accountWithdrawRecord, Long bossUserId, String otp) throws BusinessException;

    /**
     * 现金提现审核
     * @param accountCashWithdraw
     * @param bossUserId
     */
    void doCashApproval(AccountCashWithdraw accountCashWithdraw, Long bossUserId) throws BusinessException;

    /**
     * ERC20审核
     * @param accountWithdrawRecordERC20
     * @param bossUserId
     * @param signedTransactionData 大额时签名数据
     */
    void doApprovalERC20(AccountWithdrawRecordERC20 accountWithdrawRecordERC20, Long bossUserId,String signedTransactionData) throws BusinessException;

    /**
     * 提币确认
     * @param accountCashWithdraw
     * @param bossUserId
     */
    void doCashConfirm(AccountCashWithdraw accountCashWithdraw, Long bossUserId) throws BusinessException;

    /**
     * 现金提现申请
     * @param fundModel
     */
    void doApplyCashWithdraw(FundModel fundModel, AccountCashWithdraw accountCashWithdraw) throws BusinessException;


    /**
     * TRADEX交易 提币申请
     */
    void doTradexWithdrawERC20(FundModel fundModel) throws BusinessException;

    /**
     *  充值奖励（TRADEX 糖果奖励）
     * @param accountId
     */
    void doChargeAward(Long accountId) throws BusinessException;
}
