/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.settlement.entity.SettlementAccountAsset;

/**
 * 交割结算账户资产表 持久层接口
 * <p>File：SettlementAccountAssetMapper.java </p>
 * <p>Title: SettlementAccountAssetMapper </p>
 * <p>Description:SettlementAccountAssetMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SettlementAccountAssetMapper extends GenericMapper<SettlementAccountAsset>
{
    /**
     * 查找合约账户批量插入账户交割记录
     * @param accountAsset
     * @return
     */
    Long insertFromAsset(SettlementAccountAsset accountAsset);
}
