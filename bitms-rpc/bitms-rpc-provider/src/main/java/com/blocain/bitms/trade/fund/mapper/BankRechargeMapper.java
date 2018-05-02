/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.BankRecharge;
import org.apache.ibatis.annotations.Param;

/**
 * 银行充值记录表 持久层接口
 * <p>File：BankRechargeMapper.java </p>
 * <p>Title: BankRechargeMapper </p>
 * <p>Description:BankRechargeMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface BankRechargeMapper extends GenericMapper<BankRecharge>
{
    BankRecharge selectForUpdate(@Param("id") Long id);
}
