package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/4/26.
 */
public class AccountWalletAssetServiceImplTest extends AbstractBaseTest
{

    @Autowired
    AccountWalletAssetService accountWalletAssetService;
    
    @Test
    public void getPlatSumCoinByStockInfoId() throws Exception
    {
        BigDecimal amt = accountWalletAssetService.getPlatSumCoinByStockInfoId(122222222201L);
        System.out.println(amt);
    }

    @Test
    public void checkPlatSumCoinByStockInfoId() throws Exception
    {
        boolean amt = accountWalletAssetService.doCheckPlatSumCoinByStockInfoId(122222222202L);
        System.out.println(amt);
    }

    @Test
    public void autoCheckPlatSumCoin() throws Exception
    {
        accountWalletAssetService.autoCheckPlatSumCoin();
    }

}