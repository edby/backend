/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.BankRecharge;
/**
 * 银行充值记录表 服务接口
 * <p>File：BankRechargeService.java </p>
 * <p>Title: BankRechargeService </p>
 * <p>Description:BankRechargeService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface BankRechargeService extends GenericService<BankRecharge>{
    /**
     * 现金充值审核
     * @param bankRecharge
     * @param SuperAdminId
     */
    void doChargeApproval(BankRecharge bankRecharge,Long SuperAdminId);

    void doDeleteBankRecharge(Long id);

}
