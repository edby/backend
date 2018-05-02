/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;

import java.util.List;

/**
 * 合约账户资产表 服务接口
 * <p>File：AccountContractAssetService.java </p>
 * <p>Title: AccountContractAssetService </p>
 * <p>Description:AccountContractAssetService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountContractAssetService extends GenericService<AccountContractAsset>
{
    /**
     * 行级锁读取数据
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     * @param tableName
     * @return
     * @throws BusinessException
     */
    AccountContractAsset selectByPrimaryKeyOnRowLock(Long accountId, Long stockinfoId, Long relatedStockinfoId,String tableName) throws BusinessException;

    /**
     * 获取合约账户资产实体
     * @param accountContractAsset  查询实体
     * @return
     * @author 施建波  2017年7月12日 上午9:32:27
     */
    AccountContractAsset findAccountContractAsset(AccountContractAsset accountContractAsset);

    /**
     * 统计全市场（除P用户之外）当前BTC持仓数量 tableName 已在服务层处理
     * @param stockinfoId
     * @param relatedStockinfoId
     * @param accountId
     * @return
     */
    ContractAssetModel findAccountSumContractAsset(Long stockinfoId,Long relatedStockinfoId,Long accountId);

    /**
     * 查询全市场盈利账户列表
     * @param stockinfoId
     * @param relatedStockinfoId
     * @return
     */
    List<ContractAssetModel> findAccountContractAssetGtZreo(Long stockinfoId, Long relatedStockinfoId);

    /**
     * 支撑系统综合查询-合约账户资产
     * @param entity
     * @return
     * @throws BusinessException
     */
    @SlaveDataSource()
    PaginateResult<AccountContractAsset> selectAll(Pagination pagination,AccountContractAsset entity) throws BusinessException;

    /**
     * 交割平仓-查询超级用户资产数据
     * @param entity
     * @return
     * @throws BusinessException
     */
    PaginateResult<AccountContractAsset> selectSuperAdminAsset(Pagination pagination,AccountContractAsset entity,Long ... ids) throws BusinessException;

    /**
     * 交割平仓-查询超级用户资产数据
     * @param entity
     * @return
     * @throws BusinessException
     */
    List<AccountContractAsset> selectSuperAdminAsset(AccountContractAsset entity,Long ... ids) throws BusinessException;

    /**
     * 根据关联证券ID 更新期初值
     * @param relatedStockinfoId
     */
    void updateContractAssetInitialAmt(Long relatedStockinfoId);

}
