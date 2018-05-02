/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.SheetBalance;

import java.util.List;

/**
 * 报表资产负债表 服务接口
 * <p>File：SheetBalanceService.java </p>
 * <p>Title: SheetBalanceService </p>
 * <p>Description:SheetBalanceService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface SheetBalanceService extends GenericService<SheetBalance>{

    /**
     * 统计用户每日营收
     * 注：每日执行一次
     */
    Long insertPlatCalUserAssetDebitForDay();

    /**
     * 查询所有超级用户统计
     * @param sheetBalance
     * @return
     */
    List<SheetBalance> selectAllAdmin(SheetBalance sheetBalance);
}
