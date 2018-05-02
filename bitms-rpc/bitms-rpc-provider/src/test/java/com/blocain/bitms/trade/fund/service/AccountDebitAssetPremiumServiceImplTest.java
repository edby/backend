package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountSpotAsset;
import com.blocain.bitms.trade.fund.entity.AccountSpotAssetSnap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/4/2.
 */
public class AccountDebitAssetPremiumServiceImplTest extends AbstractBaseTest
{
    @Autowired
    AccountDebitAssetPremiumService accountDebitAssetPremiumService;
    
    @Autowired
    AccountSpotAssetSnapService     accountSpotAssetSnapService;
    
    @Test
    public void doMarketSnap() throws Exception
    {
        accountDebitAssetPremiumService.doMarketSnap();
    }
    
    @Test
    public void doAssetAndDebitSnap() throws Exception
    {
        // List<AccountSpotAssetSnap> list = accountSpotAssetSnapService.selectAll();
        // for (AccountSpotAssetSnap spotAssetSnap : list)
        // {
        // accountSpotAssetSnapService.remove(spotAssetSnap.getId());
        // }
        // accountSpotAssetSnapService.insertSpotAsset();
        accountDebitAssetPremiumService.doMarketSnap();
        //accountDebitAssetPremiumService.doAssetAndDebitSnap();
    }
    
    @Test
    public void getPremiumLongAccountAsset() throws Exception
    {
        accountDebitAssetPremiumService.getPremiumLongAccountAsset(FundConsts.WALLET_USD_TYPE, FundConsts.WALLET_USD_TYPE);
    }
    
    @Test
    public void getPremiumShortAccountAsset() throws Exception
    {
        accountDebitAssetPremiumService.getPremiumShortAccountAsset(FundConsts.WALLET_USD_TYPE, FundConsts.WALLET_USD_TYPE);
    }
    
    @Test
    public void autoPremium() throws Exception
    {
        accountDebitAssetPremiumService.autoPremium();
    }
    @Test
    public void doPureSpotAssetSnap() throws Exception
    {
        accountDebitAssetPremiumService.doPureSpotAssetSnap();
    }
}