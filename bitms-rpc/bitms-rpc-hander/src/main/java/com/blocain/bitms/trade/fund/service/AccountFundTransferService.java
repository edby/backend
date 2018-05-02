/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.ClientParameter;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账户资金划拨表 服务接口
 * <p>File：AccountFundTransferService.java </p>
 * <p>Title: AccountFundTransferService </p>
 * <p>Description:AccountFundTransferService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountFundTransferService extends GenericService<AccountFundTransfer>
{
    List<AccountFundTransfer> findTransferAddrList(Map<String, Object> addrMap);
    
    AccountFundTransfer findTransferAddr(AccountFundTransfer accountFundTransfer);
    
    /**
     * bitpay真正提币划拨处理成功后，回填更新账户资金待划拨申请数据以及对应账户资金流水数据的划拨状态
     * 
     * @param param
     * @throws BusinessException
     * @author sunbiao  2017年8月17日 下午1:52:48
     */
    void updatePushAccountFundTransferStatus(ClientParameter param) throws BusinessException;

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

    List<AccountFundTransfer> findByIds(String ids);

    /**
     * 定时轮询待审核提币申请
     */
    void autoGetSinglePendingApprovals();

    /**
     * 定时轮询待审核提币申请-事务处理
     */
    void doSetSinglePendingApprovals(Long id,boolean state,Long fee,String transId);

    AccountFundTransfer selectByOriginalCurrentId(Long id);

    /**
     * 定时轮询已交易的提币申请
     */
    void autoGetSingleTransaction();

    /**
     * 定时轮询已交易提币申请-事务处理
     */
    void doSetSingleTransaction(Long id,Long fee);
}
