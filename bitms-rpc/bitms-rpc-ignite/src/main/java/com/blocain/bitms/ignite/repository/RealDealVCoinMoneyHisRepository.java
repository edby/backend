package com.blocain.bitms.ignite.repository;

import java.io.Serializable;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * RealDealVCoinMoneyRepository Introduce
 * <p>Title: RealDealVCoinMoneyRepository</p>
 * <p>File：RealDealVCoinMoneyRepository.java</p>
 * <p>Description: RealDealVCoinMoneyRepository</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@NoRepositoryBean
public interface RealDealVCoinMoneyHisRepository<T, ID extends Serializable> extends IgniteRepository<T, ID>
{
}
