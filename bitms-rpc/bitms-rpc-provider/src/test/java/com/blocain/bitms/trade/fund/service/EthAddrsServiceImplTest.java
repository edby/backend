package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/4/25.
 */
public class EthAddrsServiceImplTest extends AbstractBaseTest
{

    @Autowired
    EthAddrsService ethAddrsService;

    @Test
    public void getChargeFromAddress() throws Exception
    {
        ethAddrsService.getChargeFromAddress(1L);
    }

    @Test
    public void getByIdsForUpdate() throws Exception
    {
        ethAddrsService.getByIdsForUpdate(1001L);
    }
}