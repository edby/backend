/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 交割核心业务处理
 * <p>File：SettlementService.java</p>
 * <p>Title: SettlementService</p>
 * <p>Description:SettlementService</p>
 * <p>Copyright: Copyright (c) 2017年8月17日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public interface SettlementService
{
    /**
     * 平台交割委托撤单
     */
    void withdrawByPlatSettlement(Long exchangePairMoney,Long exchangePairVCoin) throws BusinessException;

    /**
     * 交割分摊操作
     */
    void doFenTanByPlatSettlement(Long exchangePairMoney,Long exchangePairVCoin) throws BusinessException;

    /**
     * 多空超级用户数字货币转移操作
     */
    void doSuperAssetMoveSettlement(Long exchangePairMoney,Long exchangePairVCoin) throws BusinessException;

    /**
     * 准备金分摊提取
     */
    void doCalcReserveFund(Long exchangePairMoney,Long exchangePairVCoin) throws BusinessException;

    /**
     * 交割挂单
     * @param relatedStockinfoId 关联证券ID
     */
    void doSettlementEntrustVCoinMoney(Long relatedStockinfoId,Long stockinfoId) throws BusinessException;

    /**
     * 法定货币残渣清零
     *
     */
    void settlementAssetAndDebitToZero(Long relatedStockinfoId) throws BusinessException;

    /**
     * 交割步骤
     * @param step
     * @param exchangePairMoney
     * @param exchangePairVCoin
     * @return
     * @throws BusinessException
     */
    void stepOperator(Integer step, Long exchangePairMoney, Long exchangePairVCoin,Long accountId) throws BusinessException;

    /**
     * 公用-插入流程清算日志
     * @param step
     * @param status
     * @param processName
     */
    void insertSettlementProcessLog(Long step, int status, String processName, String remark,Long accountId);
}
