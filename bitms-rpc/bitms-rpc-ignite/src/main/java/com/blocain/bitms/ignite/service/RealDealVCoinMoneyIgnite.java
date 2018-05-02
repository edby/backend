package com.blocain.bitms.ignite.service;

import com.blocain.bitms.orm.core.AbstractIgnite;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;

/**
 * RealDealVCoinMoneyIgnite
 * <p>File：RealDealVCoinMoneyIgnite.java </p>
 * <p>Title: RealDealVCoinMoneyIgnite </p>
 * <p>Description:RealDealVCoinMoneyIgnite </p>
 * <p>Copyright: Copyright (c) 2018/4/2</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public interface RealDealVCoinMoneyIgnite extends AbstractIgnite<RealDealVCoinMoney>
{
    /**
     * Saves a given entity using provided key.
     * @param entity
     * @param txFlag 是否回滚数据
     * @return T
     * @throws BusinessException
     */
    RealDealVCoinMoney save(String cacheName, RealDealVCoinMoney entity, boolean txFlag) throws BusinessException;
}
