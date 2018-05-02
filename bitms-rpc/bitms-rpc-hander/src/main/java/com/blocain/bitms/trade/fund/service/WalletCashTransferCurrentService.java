/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.WalletCashTransferCurrent;

/**
 * 外部钱包现金转账流水表 服务接口
 * <p>File：WalletCashTransferCurrentService.java </p>
 * <p>Title: WalletCashTransferCurrentService </p>
 * <p>Description:WalletCashTransferCurrentService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface WalletCashTransferCurrentService extends GenericService<WalletCashTransferCurrent>{

    WalletCashTransferCurrent getLastEntity();
}
