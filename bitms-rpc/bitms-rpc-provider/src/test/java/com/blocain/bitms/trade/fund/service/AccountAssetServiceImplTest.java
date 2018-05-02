package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;

/**
 * Created by admin on 2017/10/31.
 */
public class AccountAssetServiceImplTest extends AbstractBaseTest
{
    @Autowired
    AccountAssetService           accountAssetService;

    @Autowired
    AccountWalletAssetService 	  accountWalletAssetService;

    @Test
    public void getNetAsset() throws Exception
    {
        accountAssetService.getNetAsset(FundConsts.SYSTEM_ACCOUNT_ID,FundConsts.WALLET_BTC_TYPE,FundConsts.WALLET_BTC2USDX_TYPE);
    }
    @Test
    public void getConstractAsset()
    {
        AccountAssetModel entity = new AccountAssetModel();
        entity.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountAssetService.findList(entity);
    }

    @Test
    public void getWalletAsset()
    {
        AccountWalletAsset entity = new AccountWalletAsset();
        entity.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountWalletAssetService.findList(entity);
    }
    @Test
    public void findAssetAndDebitForAccount() throws Exception
    {
        AccountAssetModel model = new AccountAssetModel();
        model.setAccountId(300000067890L);
        model.setStockInfoId(FundConsts.WALLET_BTC_TYPE);
        model.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        model = accountAssetService.findAssetAndDebitForAccount(model);
        System.out.println(model.toString());
    }
}