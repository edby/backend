package com.blocain.bitms.ignite.repository;

import java.util.List;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;

import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;

/**
 * AccountWalletAssetRepository Introduce
 * <p>Title: AccountWalletAssetRepository</p>
 * <p>File：AccountWalletAssetRepository.java</p>
 * <p>Description: AccountWalletAssetRepository</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@RepositoryConfig(cacheName = "AccountWalletAssetCache")
public interface AccountWalletAssetRepository extends IgniteRepository<AccountWalletAsset, Long>
{
    /**
     * 根据帐户ID查询
     * @param accountId
     * @return
     */
    List<AccountWalletAsset> findAccountWalletAssetByAccountIdLike(String accountId);
}
