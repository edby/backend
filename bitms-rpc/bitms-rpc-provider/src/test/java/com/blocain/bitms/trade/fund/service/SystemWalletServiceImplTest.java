package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.entity.SystemWallet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/1/22.
 */
public class SystemWalletServiceImplTest extends AbstractBaseTest
{
    @Autowired
    SystemWalletService systemWalletService;

    @Test
    public void fixRecord()
    {
        SystemWallet entity = new SystemWallet();
        List<SystemWallet> list = systemWalletService.findList(entity);
        for(SystemWallet r:list)
        {
            systemWalletService.updateByPrimaryKey(r);
        }
    }
}