package com.blocain.bitms.ignite.repository;

import java.io.Serializable;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * EntrustVcoinRepository Introduce
 * <p>Title: EntrustVcoinRepository</p>
 * <p>File：EntrustVcoinRepository.java</p>
 * <p>Description: EntrustVcoinRepository</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@NoRepositoryBean
public interface EntrustVCoinMoneyRepository<T, ID extends Serializable> extends IgniteRepository<T, ID>
{
}
