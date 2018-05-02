/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.SheetBalance;

import java.util.List;

/**
 * 报表资产负债表 持久层接口
 * <p>File：SheetBalanceMapper.java </p>
 * <p>Title: SheetBalanceMapper </p>
 * <p>Description:SheetBalanceMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface SheetBalanceMapper extends GenericMapper<SheetBalance>
{

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
    List<SheetBalance>  selectAllAdmin(SheetBalance sheetBalance);

}
