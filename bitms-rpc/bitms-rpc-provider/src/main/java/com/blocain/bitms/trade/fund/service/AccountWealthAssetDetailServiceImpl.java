/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.fund.entity.AccountWealthAssetDetail;
import com.blocain.bitms.trade.fund.mapper.AccountWealthAssetDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 账户理财资产明细表 服务实现类
 * <p>File：AccountWealthAssetDetailServiceImpl.java </p>
 * <p>Title: AccountWealthAssetDetailServiceImpl </p>
 * <p>Description:AccountWealthAssetDetailServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountWealthAssetDetailServiceImpl extends GenericServiceImpl<AccountWealthAssetDetail> implements AccountWealthAssetDetailService
{

    protected AccountWealthAssetDetailMapper accountWealthAssetDetailMapper;

    @Autowired
    public AccountWealthAssetDetailServiceImpl(AccountWealthAssetDetailMapper accountWealthAssetDetailMapper)
    {
        super(accountWealthAssetDetailMapper);
        this.accountWealthAssetDetailMapper = accountWealthAssetDetailMapper;
    }
}
