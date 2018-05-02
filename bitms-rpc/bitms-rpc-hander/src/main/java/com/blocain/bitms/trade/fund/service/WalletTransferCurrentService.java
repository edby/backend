/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.WalletTransferCurrent;
/**
 * 钱包转账流水表 服务接口
 * <p>File：WalletTransferCurrentService.java </p>
 * <p>Title: WalletTransferCurrentService </p>
 * <p>Description:WalletTransferCurrentService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface WalletTransferCurrentService extends GenericService<WalletTransferCurrent>{

    WalletTransferCurrent getLastEntity(Long stockinfoId);

    WalletTransferCurrent getLastEntityForUpdate(Long stockinfoId);

    WalletTransferCurrent doSaveWalletTransferCurrent(WalletTransferCurrent walletTransferCurrent,String currentDateStr);

}
