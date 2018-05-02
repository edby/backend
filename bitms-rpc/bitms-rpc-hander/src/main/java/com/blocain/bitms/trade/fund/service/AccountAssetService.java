/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import com.blocain.bitms.trade.fund.model.FundModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账户资产
 * <p>File：AccountAssetService.java</p>
 * <p>Title: AccountAssetService</p>
 * <p>Description:AccountAssetService</p>
 * <p>Copyright: Copyright (c) 2017年7月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public interface AccountAssetService extends GenericService<AccountAssetModel>
{
    /**
     * 净资产查询
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     * @return
     */
	BigDecimal getNetAsset(Long accountId ,Long stockinfoId,Long relatedStockinfoId);

    /**
     * 账户交易对净资产查询
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     * @return
     */
    BigDecimal getAccountNetAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId);
    /**
     * 查询周盈亏
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     * @return
     * @throws BusinessException
     */
    BigDecimal getProfitAndLoss(Long accountId, Long stockinfoId, Long relatedStockinfoId) throws BusinessException;

    /**
     * 查询账户合约资产情况
     * @param pagination
     * @param accountAssetModel
     * @param tableNames
     * @return
     * @throws BusinessException
     */
    PaginateResult<AccountAssetModel> findAssetList(Pagination pagination,AccountAssetModel accountAssetModel,List<Map<String,Object>> tableNames) throws BusinessException;

    /**
     * 查询账户合约资产情况
     * @param accountAssetModel
     * @param tableNames
     * @return
     * @throws BusinessException
     */
    List<AccountAssetModel> findAssetList(AccountAssetModel accountAssetModel,List<Map<String,Object>> tableNames) throws BusinessException;


    /**
     * 交易资产转钱包资产可用判断
     * @param fundModel
     * @throws BusinessException
     */
    void contract2WalletEnable(FundModel fundModel) throws BusinessException;

    /**
     * 交易资产转钱包资产可用判断
     * @param fundModel
     * @throws BusinessException
     */
    BigDecimal contract2WalletEnableAmount(FundModel fundModel) throws BusinessException;

    /**
     * 获取用户单个借款和对应的资产
     * @param accountAssetModel
     * @return
     */
    AccountAssetModel findAssetAndDebitForAccount(AccountAssetModel accountAssetModel);
}
