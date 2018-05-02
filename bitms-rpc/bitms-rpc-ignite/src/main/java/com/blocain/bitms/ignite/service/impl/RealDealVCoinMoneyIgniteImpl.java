package com.blocain.bitms.ignite.service.impl;

import com.blocain.bitms.ignite.service.RealDealVCoinMoneyIgnite;
import com.blocain.bitms.orm.core.AbstractIgniteImpl;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;
import org.springframework.transaction.annotation.Transactional;

/**
 * RealDealVCoinMoneyIgniteImpl
 * <p>File：RealDealVCoinMoneyIgniteImpl.java </p>
 * <p>Title: RealDealVCoinMoneyIgniteImpl </p>
 * <p>Description:RealDealVCoinMoneyIgniteImpl </p>
 * <p>Copyright: Copyright (c) 2018/4/2</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RealDealVCoinMoneyIgniteImpl extends AbstractIgniteImpl<RealDealVCoinMoney> implements RealDealVCoinMoneyIgnite
{
    @Override
    @Transactional
    public RealDealVCoinMoney save(String cacheName, RealDealVCoinMoney entity, boolean txFlag) throws BusinessException
    {
        getCache(cacheName).put(entity.getId(), entity);
        if (txFlag) throw new BusinessException("业务执行失败，回滚数据！");
        return entity;
    }
}
