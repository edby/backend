/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.settlement.entity.SettlementAccountAsset;

/**
 * 交割结算账户资产表 服务接口
 * <p>File：SettlementAccountAssetService.java </p>
 * <p>Title: SettlementAccountAssetService </p>
 * <p>Description:SettlementAccountAssetService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface SettlementAccountAssetService extends GenericService<SettlementAccountAsset>{

    /**
     * 查找合约账户批量插入账户交割记录
     * @param accountAsset
     * @return
     */
    Long insertFromAsset(SettlementAccountAsset accountAsset);
}
