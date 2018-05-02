/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.SheetBalance;
import com.blocain.bitms.trade.fund.mapper.SheetBalanceMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 报表资产负债表 服务实现类
 * <p>File：SheetBalanceServiceImpl.java </p>
 * <p>Title: SheetBalanceServiceImpl </p>
 * <p>Description:SheetBalanceServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SheetBalanceServiceImpl extends GenericServiceImpl<SheetBalance> implements SheetBalanceService
{

    protected SheetBalanceMapper sheetBalanceMapper;

    @Autowired
    public SheetBalanceServiceImpl(SheetBalanceMapper sheetBalanceMapper)
    {
        super(sheetBalanceMapper);
        this.sheetBalanceMapper = sheetBalanceMapper;
    }

    @Override
    public Long insertPlatCalUserAssetDebitForDay()
    {
        sheetBalanceMapper.insertPlatCalUserAssetDebitForDay();
        return 1L;
    }

    @Override public List<SheetBalance> selectAllAdmin(SheetBalance sheetBalance)
    {
        return sheetBalanceMapper.selectAllAdmin(sheetBalance);
    }
}
