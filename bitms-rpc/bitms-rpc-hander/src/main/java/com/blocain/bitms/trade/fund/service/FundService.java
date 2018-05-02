/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.model.FundModel;

import java.math.BigDecimal;

/**
 * 资金类统一服务接口
 * <p>File：FundService.java </p>
 * <p>Title: FundService </p>
 * <p>Description:FundService </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public interface FundService
{
    /**
     * 资金类联机交易处理
     * @param fundModel    fundModel
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月10日 上午10:38:51
     */
    FundModel fundTransaction(FundModel fundModel) throws BusinessException;
    
    /**
     * 解冻BMS资产
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年8月7日 上午10:38:51
     */
    int fundUnfrozenBmsAsset() throws BusinessException;

    /**
     * 普通用户借款转移给超级用户
     * @param accountDebitAsset
     * @return
     * @throws BusinessException
     * @author zcx
     */
    int fundDebitMoveToPlatAsset(AccountDebitAsset accountDebitAsset, Long superAccountId, String businessFlag) throws BusinessException;

    /**
     * 交割时数字货币转移
     * @param fundModel
     * @return
     * @throws BusinessException
     * @author zcx
     */
    void  doSettlementVCoinMove(FundModel fundModel) throws BusinessException;


    /**
     * 交割时超级用户用数字货币兑换法定货币
     * @return
     * @throws BusinessException
     * @author zcx
     */
    void  doSettlementMoneyExchangeVCoin(Long exchangePairMoney,Long exchangePairVCoin) throws BusinessException;


    /**
     * 交割时资产转移
     * @param fundModel
     * @return
     * @throws BusinessException
     * @author zcx
     */
    void  doSettlementAssetMove(FundModel fundModel) throws BusinessException;

    /**
     * 交割时超级用户用数字货币兑换法定货币准备金
     * @return
     * @throws BusinessException
     * @author zcx
     */
    void superSettlementMoneyExchangeVCoin(AccountContractAsset accountContractAsset, BigDecimal settlementPrice) throws BusinessException;

    /**
     * 网络手续费处理
     * @param fundModel
     * @param accountWalletAsset
     * @param direct
     */
    void superAdminNetFee(FundModel fundModel, AccountWalletAsset accountWalletAsset, String direct) throws BusinessException;

    /**
     * 理财利息和借款利息处理
     * @param fundModel
     * @param direct
     */
    void doSuperAdminDebitInterest(FundModel fundModel,String direct) throws BusinessException;

}
