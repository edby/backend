package com.blocain.bitms.ignite.repository;

import java.util.ArrayList;
import java.util.List;

import com.blocain.bitms.ignite.service.AccountWalletAssetIgnite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.ignite.basic.AbstractService;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;

/**
 * AccountWalletAssetIgnite Introduce
 * <p>Title: AccountWalletAssetIgnite</p>
 * <p>File：AccountWalletAssetIgnite.java</p>
 * <p>Description: AccountWalletAssetIgnite</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountWalletAssetIgniteTest extends AbstractService
{
    @Autowired
    private AccountWalletAssetIgnite accountWalletAssetIgnite;
    
    @Test
    public void queryWallet()
    {
        Iterable<AccountWalletAsset> data = accountWalletAssetIgnite.findAll();
        for (AccountWalletAsset asset : data)
        {
            System.out.println("queryWallet:" + asset.toString());
        }
    }
    
    @Test
    public void findById()
    {
        AccountWalletAsset asset = accountWalletAssetIgnite.findOne(26765L);
        if (null != asset)
        {
            System.out.println("findById:" + JSON.toJSONString(asset));
        }
    }

    //@Test
    public void save()
    {
        List<AccountWalletAsset> entities = new ArrayList<>();
        AccountWalletAsset asset;
        while (true)
        {
            asset = accountWalletAssetIgnite.findOne(199999999500L);
            asset.setId(System.currentTimeMillis());
            entities.add(asset);
            if (entities.size() == 500)
            {
                System.out.println("save start...");
                accountWalletAssetIgnite.save(entities);
                entities = new ArrayList<>();
                System.out.println("save end...");
            }
        }
    }
}