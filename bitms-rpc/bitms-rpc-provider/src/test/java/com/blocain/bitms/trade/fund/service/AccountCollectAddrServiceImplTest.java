package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/3/26.
 */
public class AccountCollectAddrServiceImplTest extends AbstractBaseTest
{
    @Autowired
    AccountCollectAddrService accountCollectAddrService;

    @Test
    public void fixedRecord()
    {
        AccountCollectAddr entity = new AccountCollectAddr();
        List<AccountCollectAddr> list = accountCollectAddrService.findList(entity);
        for(AccountCollectAddr addr:list)
        {
            addr.setCertStatus(FundConsts.WALLET_AUTH_STATUS_AUTH);
            accountCollectAddrService.updateByPrimaryKey(addr);
        }
    }
}