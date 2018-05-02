package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddr;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddrERC20;
import com.blocain.bitms.trade.fund.entity.SystemWalletERC20;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2018/3/1.
 */
public class SystemWalletAddrERC20ServiceTest extends AbstractBaseTest
{
    @Autowired
    SystemWalletERC20Service     systemWalletERC20Service;
    
    @Autowired
    SystemWalletAddrERC20Service systemWalletAddrERC20Service;

    @Autowired
    SystemWalletAddrService systemWalletAddrService;
    
    @Test
    public void initData()
    {
        SystemWalletAddrERC20 a = new SystemWalletAddrERC20();
        List<SystemWalletAddrERC20> list = systemWalletAddrERC20Service.findList(a);
        for(SystemWalletAddrERC20 entity:list)
        {
            systemWalletAddrERC20Service.updateByPrimaryKeySelective(entity);
        }

        SystemWalletAddr b = new SystemWalletAddr();
        List<SystemWalletAddr> list2 = systemWalletAddrService.findList(b);
        for(SystemWalletAddr entity:list2)
        {
            systemWalletAddrService.updateByPrimaryKeySelective(entity);
        }
    }
}