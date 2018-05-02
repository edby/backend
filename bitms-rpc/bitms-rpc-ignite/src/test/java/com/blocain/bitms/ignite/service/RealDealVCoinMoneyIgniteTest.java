package com.blocain.bitms.ignite.service;

import java.util.List;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.ignite.basic.AbstractService;
import com.blocain.bitms.ignite.config.IgniteConsts;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;

/**
 * RealDealVCoinMoneyIgniteTest
 * <p>File：RealDealVCoinMoneyIgniteTest.java </p>
 * <p>Title: RealDealVCoinMoneyIgniteTest </p>
 * <p>Description:RealDealVCoinMoneyIgniteTest </p>
 * <p>Copyright: Copyright (c) 2018/4/2</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RealDealVCoinMoneyIgniteTest extends AbstractService
{
    @Autowired
    private RealDealVCoinMoneyIgnite realDealVCoinMoneyIgnite;
    
    @Test
    public void save()
    {
        RealDealVCoinMoney dealVCoinMoney = realDealVCoinMoneyIgnite.findOne(IgniteConsts.cache_btc2eur_realdeal, 57824742719123457l);
        dealVCoinMoney.setRemark("回滚数据");
        realDealVCoinMoneyIgnite.save(IgniteConsts.cache_btc2eur_realdeal, dealVCoinMoney, true);
    }
    
    @Test
    public void findOne()
    {
        RealDealVCoinMoney dealVCoinMoney = realDealVCoinMoneyIgnite.findOne(IgniteConsts.cache_btc2eur_realdeal, 57824742719123457l);
        System.out.println(dealVCoinMoney);
    }
    
    @Test
    public void exists()
    {
        boolean flag = realDealVCoinMoneyIgnite.exists(IgniteConsts.cache_btc2eur_realdeal, 57824742719123457l);
        System.out.println(flag);
    }
    
    @Test
    public void findAll()
    {
        List<RealDealVCoinMoney> data = realDealVCoinMoneyIgnite.findAll(IgniteConsts.cache_btc2usd_realdeal);
        // List<Long> ids = Lists.newArrayList(57825356941389825l, 57824742719123462l, 57821634102325248l);
        // List<RealDealVCoinMoney> data = realDealVCoinMoneyIgnite.findAll(IgniteConsts.cache_btc2eur_realdeal, ids);
        for (RealDealVCoinMoney coinMoney : data)
        {
            System.out.println(coinMoney.toString());
        }
    }
    
    @Test
    public void count()
    {
        realDealVCoinMoneyIgnite.count(IgniteConsts.cache_btc2eur_realdeal);
    }
    
    @Test
    public void deleteAll()
    {
        realDealVCoinMoneyIgnite.deleteAll(IgniteConsts.cache_btc2eur_realdeal);
    }
}