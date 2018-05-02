/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetDetail;
import com.blocain.bitms.trade.fund.mapper.AccountDebitAssetDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户借贷资产明细表 服务实现类
 * <p>File：AccountDebitAssetDetailServiceImpl.java </p>
 * <p>Title: AccountDebitAssetDetailServiceImpl </p>
 * <p>Description:AccountDebitAssetDetailServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountDebitAssetDetailServiceImpl extends GenericServiceImpl<AccountDebitAssetDetail> implements AccountDebitAssetDetailService
{

    protected AccountDebitAssetDetailMapper accountDebitAssetDetailMapper;

    @Autowired
    public AccountDebitAssetDetailServiceImpl(AccountDebitAssetDetailMapper accountDebitAssetDetailMapper)
    {
        super(accountDebitAssetDetailMapper);
        this.accountDebitAssetDetailMapper = accountDebitAssetDetailMapper;
    }
}
