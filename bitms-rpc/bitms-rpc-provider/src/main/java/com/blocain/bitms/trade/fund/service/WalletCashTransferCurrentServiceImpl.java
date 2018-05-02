/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.WalletCashTransferCurrent;
import com.blocain.bitms.trade.fund.mapper.WalletCashTransferCurrentMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 外部钱包现金转账流水表 服务实现类
 * <p>File：WalletCashTransferCurrentServiceImpl.java </p>
 * <p>Title: WalletCashTransferCurrentServiceImpl </p>
 * <p>Description:WalletCashTransferCurrentServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class WalletCashTransferCurrentServiceImpl extends GenericServiceImpl<WalletCashTransferCurrent> implements WalletCashTransferCurrentService
{

    protected WalletCashTransferCurrentMapper walletCashTransferCurrentMapper;

    @Autowired
    public WalletCashTransferCurrentServiceImpl(WalletCashTransferCurrentMapper walletCashTransferCurrentMapper)
    {
        super(walletCashTransferCurrentMapper);
        this.walletCashTransferCurrentMapper = walletCashTransferCurrentMapper;
    }

    @Override
    public WalletCashTransferCurrent getLastEntity()
    {
        return walletCashTransferCurrentMapper.getLastEntity();
    }
}
