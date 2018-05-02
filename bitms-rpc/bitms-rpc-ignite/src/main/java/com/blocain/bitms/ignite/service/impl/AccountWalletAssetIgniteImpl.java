package com.blocain.bitms.ignite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.ignite.repository.AccountWalletAssetRepository;
import com.blocain.bitms.ignite.service.AccountWalletAssetIgnite;
import com.blocain.bitms.orm.core.GenericIgnigeImpl;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;

/**
 * AccountWalletAssetIgniteImpl Introduce
 * <p>Title: AccountWalletAssetIgniteImpl</p>
 * <p>File：AccountWalletAssetIgniteImpl.java</p>
 * <p>Description: AccountWalletAssetIgniteImpl</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
// @Service
public class AccountWalletAssetIgniteImpl extends GenericIgnigeImpl<AccountWalletAsset> implements AccountWalletAssetIgnite
{
    protected AccountWalletAssetRepository accountWalletAssetRepository;
    
    @Autowired
    public AccountWalletAssetIgniteImpl(AccountWalletAssetRepository cache)
    {
        super(cache);
        this.accountWalletAssetRepository = cache;
    }
}
