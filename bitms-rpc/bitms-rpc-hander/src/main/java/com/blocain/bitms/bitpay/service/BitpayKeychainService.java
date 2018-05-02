/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service;

import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 钱包参数 服务接口
 * <p>File：BitpayKeychainService.java </p>
 * <p>Title: BitpayKeychainService </p>
 * <p>Description:BitpayKeychainService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface BitpayKeychainService extends GenericService<BitpayKeychain>
{
    /**
     * 生成随机密码
     * @return
     * @throws BusinessException
     */
    String builderEncryptPassword() throws BusinessException;

    /**
     * 获取比特币提现钱包总余额(包括已经确认与未确认的)
     * @return
     * @throws BusinessException
     */
    String getBtcTXWalletAllBalance() throws BusinessException;

    /**
     * 获取比特币充值钱包已确认余额
     * @return
     * @throws BusinessException
     */
    String getBtcCZWalletConfirmedBalance() throws BusinessException;
}
