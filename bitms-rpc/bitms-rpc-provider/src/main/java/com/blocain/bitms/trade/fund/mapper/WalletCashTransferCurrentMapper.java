/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.WalletCashTransferCurrent;

/**
 * 外部钱包现金转账流水表 持久层接口
 * <p>File：WalletCashTransferCurrentMapper.java </p>
 * <p>Title: WalletCashTransferCurrentMapper </p>
 * <p>Description:WalletCashTransferCurrentMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface WalletCashTransferCurrentMapper extends GenericMapper<WalletCashTransferCurrent>
{
    WalletCashTransferCurrent getLastEntity();
}
