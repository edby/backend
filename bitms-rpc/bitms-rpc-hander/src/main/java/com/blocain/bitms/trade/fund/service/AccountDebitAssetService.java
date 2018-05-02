/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import com.blocain.bitms.trade.fund.model.DebitAssetModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户借贷记录表 服务接口
 * <p>File：AccountDebitAssetService.java </p>
 * <p>Title: AccountDebitAssetService </p>
 * <p>Description:AccountDebitAssetService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountDebitAssetService extends GenericService<AccountDebitAsset>{


    AccountDebitAsset selectByPrimaryKeyForUpdate(String tableName,Long id) throws BusinessException;

    /**
     * 自动计息
     * @return
     */
    void autoAccountDebitAssetInterest() throws BusinessException;

    /**
     * 单用户计息
     * @return
     */
    void doInterestRate(AccountDebitAsset record, AccountAssetModel model,Long exchangePairMoney) throws BusinessException;

    /**
     * 向平台自动借款
     * @param debitAssetModel
     * @return
     */
    DebitAssetModel doDebitBorrowFromPlat(DebitAssetModel debitAssetModel) throws BusinessException;

    /**
     * 超级用户向自己借款
     * @param debitAssetModel
     * @return
     */
    DebitAssetModel doDebitSuperAdminBorrowFromPlat(DebitAssetModel debitAssetModel) throws BusinessException;

    /**
     * 向平台自动还款
     * @return
     */
    void autoDebitRepaymentToPlat(StockInfo stockInfo);

    /**
     * 借款转移给超级用户
     * @param record
     * @param superAccountId
     * @throws BusinessException
     */
    void doDebtMoveToPlat(AccountDebitAsset record, Long superAccountId, String businessFlag) throws BusinessException;

    /**
     * 用户向平台还款
     * @param stockinfoType 货币类型：数字货币还是法定货币
     * @param stockinfoId 借款stockinfoId
     * @param relatedStockinfoId 查询余额关联stockinfoId
     * @param accountId 用户id
     * @return
     */
    void doAccountDebitRepaymentToPlat(String stockinfoType,Long stockinfoId,Long relatedStockinfoId,Long accountId) throws BusinessException;

    /**
     * 强制还款给平台(不够用BTC折算成USDX还款给平台)
     * @param stockinfoType 货币类型：数字货币 法定货币
     * @param stockinfoId 借款stockinfoId
     * @param relatedStockinfoId 查询余额关联stockinfoId
     * @param deductionStockinfoId 抵扣关联stockinfoId
     * @return
     */
    void doDebitRepaymentPowerByPlat(String stockinfoType,Long stockinfoId,Long relatedStockinfoId,Long deductionStockinfoId) throws BusinessException;

    /**
     * 查询未还借款信息
     * @return
     */
    List<AccountDebitAsset> findListForDebit(AccountDebitAsset accountDebitAsset);

    /**
     * 查询
     * @param  ids 逗号分割
     * @return
     */
    List<AccountDebitAsset> findListByIds(String ids);

    /**
     * 查询保证金情况
     * @param pagination
     * @param accountDebitAsset
     * @return
     */
    PaginateResult<AccountDebitAsset> findMarginList(Pagination pagination, AccountDebitAsset accountDebitAsset);

    /**
     * 交割过程中强制还款后的债务转移
     * @return
     */
     void settlementDebitMoveToPlatAfterPowerRepay(Long relatedStockinfoId,Long stockinfoId,Long superAccountId);

    /**
     * 按条件查找总负债
     * @param accountDebitAsset
     * @return
     */
    BigDecimal findSumDebitAmt(AccountDebitAsset accountDebitAsset);

    /**
     * 债务统计
     * @param accountDebitAsset
     * @return
     */
    PaginateResult<AccountDebitAsset> debitSumData(Pagination pagination, AccountDebitAsset accountDebitAsset);

}
