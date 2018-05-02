package com.blocain.bitms.ignite.repository;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;

import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;

/**
 * AccountFundCurrentRepository Introduce
 * <p>Title: AccountFundCurrentRepository</p>
 * <p>File：AccountFundCurrentRepository.java</p>
 * <p>Description: AccountFundCurrentRepository</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@RepositoryConfig(cacheName = "AccountFundCurrentCache")
public interface AccountFundCurrentRepository extends IgniteRepository<AccountFundCurrent, Long>
{
}
