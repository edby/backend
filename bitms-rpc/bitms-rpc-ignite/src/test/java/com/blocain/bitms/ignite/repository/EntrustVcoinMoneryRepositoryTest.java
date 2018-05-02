package com.blocain.bitms.ignite.repository;

import java.util.List;

import javax.cache.Cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.SqlQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.ignite.basic.AbstractService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;

/**
 * 操作原生API示例 Introduce
 * <p>Title: EntrustVcoinMoneryRepositoryTest</p>
 * <p>File：EntrustVcoinMoneryRepositoryTest.java</p>
 * <p>Description: EntrustVcoinMoneryRepositoryTest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class EntrustVcoinMoneryRepositoryTest extends AbstractService
{
    @Autowired
    private Ignite ignite;

    @Test
    public void queryData()
    {
        String sql = "select * from EntrustVCoinMoney limit 50";
        SqlQuery<Long, EntrustVCoinMoney> query = new SqlQuery<>(EntrustVCoinMoney.class, sql);
        // 映射具体的cache 配置在DAO层与缓存XML文件中
        IgniteCache<Long, EntrustVCoinMoney> igniteCache = getEntrustVCoinMoneyCache();
        List<Cache.Entry<Long, EntrustVCoinMoney>> data = igniteCache.query(query).getAll();
        for (Cache.Entry<Long, EntrustVCoinMoney> datum : data)
        {
            System.out.println(datum.getValue().toString());
        }
    }

    @Test
    public void queryCount()
    {
        String sql = "select * from EntrustVCoinMoney ";
        SqlQuery<Long, EntrustVCoinMoney> query = new SqlQuery<>(EntrustVCoinMoney.class, sql);
        // 映射具体的cache 配置在DAO层与缓存XML文件中
        IgniteCache<Long, EntrustVCoinMoney> igniteCache = getEntrustVCoinMoneyCache();
        List<Cache.Entry<Long, EntrustVCoinMoney>> data = igniteCache.query(query).getAll();
        System.out.println("EntrustVCoinMoney302 size:" + data.size());

        sql = "select * from EntrustVCoinMoney ";
        query = new SqlQuery<>(EntrustVCoinMoney.class, sql);
        // 映射具体的cache 配置在DAO层与缓存XML文件中
        igniteCache = ignite.cache("EntrustVCoinMoney402Cache");;
        data = igniteCache.query(query).getAll();
        System.out.println("EntrustVCoinMoney402 size:" + data.size());
    }

    @Test
    public void removeById()
    {
        getEntrustVCoinMoneyCache().removeAsync(57329739894886400L);
    }

    @Test
    public void updateById()
    {
    }

    // get alerts cache store
    protected IgniteCache<Long, EntrustVCoinMoney> getEntrustVCoinMoneyCache()
    {
        return ignite.cache("EntrustVCoinMoney302Cache");
    }
}