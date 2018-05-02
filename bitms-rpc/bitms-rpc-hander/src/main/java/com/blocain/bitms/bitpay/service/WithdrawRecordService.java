/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service;

import com.blocain.bitms.bitpay.entity.WithdrawRecord;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 账户提现记录 服务接口
 * <p>File：WithdrawRecordService.java </p>
 * <p>Title: WithdrawRecordService </p>
 * <p>Description:WithdrawRecordService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface WithdrawRecordService extends GenericService<WithdrawRecord>{
    /**
     *  单笔提现
     * @param id
     * @param password
     * @return
     * @throws BusinessException
     */
    void doSingleCashWthdrawal(Long id, String password,String otp) throws BusinessException;

    /**
     *  多笔提现
     * @param ids
     * @param password
     * @return
     * @throws BusinessException
     */
    void doMultipleCashWthdrawal(String ids, String password) throws BusinessException;

}
