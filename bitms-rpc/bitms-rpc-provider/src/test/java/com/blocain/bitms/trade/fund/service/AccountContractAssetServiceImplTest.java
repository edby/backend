package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/9/25.
 */
public class AccountContractAssetServiceImplTest extends AbstractBaseTest
{

    @Test
    //交割平仓-查询超级用户资产数据
    public void selectSuperAdminAsset() throws Exception
    {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setRows(20);
        AccountContractAsset entity = new AccountContractAsset();
        entity.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        accountContractAssetService.selectSuperAdminAsset(pagination, entity,
                FundConsts.SYSTEM_ACCOUNT_ID,
                FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID,
                FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID
        );
    }

    @Autowired
    AccountContractAssetService accountContractAssetService;
    
    @Test
    public void findAccountSunContractAsset() throws Exception
    {
        ContractAssetModel model = accountContractAssetService.findAccountSumContractAsset(FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE, null);
        System.out.println(model.toString());
        model = accountContractAssetService.findAccountSumContractAsset(FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE, 1L);
        System.out.println(model.toString());
    }

    @Test
    public void findAccountContractAssetGtZreo() throws Exception
    {
        accountContractAssetService.findAccountContractAssetGtZreo(FundConsts.WALLET_BTC_TYPE, FundConsts.WALLET_BTC2USDX_TYPE);
    }

}