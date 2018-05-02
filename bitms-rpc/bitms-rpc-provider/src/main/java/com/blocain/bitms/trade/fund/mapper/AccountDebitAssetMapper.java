/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户借贷资产表 持久层接口
 * <p>File：AccountDebitAssetMapper.java </p>
 * <p>Title: AccountDebitAssetMapper </p>
 * <p>Description:AccountDebitAssetMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountDebitAssetMapper extends GenericMapper<AccountDebitAsset>
{
    List<AccountDebitAsset> findListForDebit(AccountDebitAsset accountDebitAsset);

    List<AccountDebitAsset> findListByIds(@Param("ids") Long[] ids );

    AccountDebitAsset selectByPrimaryKeyForUpdate(@Param("tableName")String tableName ,@Param("id")Long id);

    /**
     * 查询保证金情况
     * @param accountDebitAsset
     * @return
     */
    List<AccountDebitAsset> findMarginList(AccountDebitAsset accountDebitAsset);


    /**
     * 负债统计
     * @param accountDebitAsset
     * @return
     */
    List<AccountDebitAsset> debitSumData(AccountDebitAsset accountDebitAsset);


    /**
     * 按条件查找总负债
     * @param accountDebitAsset
     * @return
     */
    BigDecimal findSumDebitAmt(AccountDebitAsset accountDebitAsset);
}
