/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;

/**
 * 合约账户资产表 持久层接口
 * <p>File：AccountContractAssetDao.java </p>
 * <p>Title: AccountContractAssetDao </p>
 * <p>Description:AccountContractAssetDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountContractAssetMapper extends GenericMapper<AccountContractAsset>
{
    /**
     * 行级锁读取数据
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     * @return
     * @throws BusinessException
     */
    AccountContractAsset selectByPrimaryKeyOnRowLock(@Param("accountId") Long accountId, @Param("stockinfoId") Long stockinfoId,
            @Param("relatedStockinfoId") Long relatedStockinfoId, @Param("tableName") String tableName) throws BusinessException;
    
    /**
     * 根据AccountContractAsset对象查询
     * @param accountContractAsset
     * @return
     */
    AccountContractAsset findContractAsset(AccountContractAsset accountContractAsset);
    
    /**
     * 统计全市场（除P用户之外）当前BTC持仓数量
     * @param stockinfoId
     * @param relatedStockinfoId
     * @param accountId (不传 查询所有)
     * @return
     */
    ContractAssetModel findAccountSumContractAsset(@Param("stockinfoId") Long stockinfoId, @Param("relatedStockinfoId") Long relatedStockinfoId,
            @Param("accountId") Long accountId, @Param("tableName") String tableName);

    /**
     * 查询全市场盈利账户列表
     * @param stockinfoId
     * @param relatedStockinfoId
     * @param tableName
     * @return
     */
    List<ContractAssetModel> findAccountContractAssetGtZreo(@Param("stockinfoId") Long stockinfoId, @Param("relatedStockinfoId") Long relatedStockinfoId, @Param("tableName") String tableName);
    
    /**
     * 查询所有数据列表
     * @return
     */
    List<AccountContractAsset> selectAll(AccountContractAsset accountContractAsset);
    
    /**
     * 交割平仓-查询超级用户资产数据
     * @return
     */
    List<AccountContractAsset> selectSuperAdminAsset(@Param("accountContractAsset") AccountContractAsset accountContractAsset, @Param("ids") Long[] ids);
    
    /**
     * 根据关联证券ID 更新期初值
     * @param relatedStockinfoId
     */
    void updateContractAssetInitialAmt(@Param("relatedStockinfoId") Long relatedStockinfoId, @Param("tableName") String tableName);
}
