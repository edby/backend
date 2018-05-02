package com.blocain.bitms.monitor.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.basic.service.AbstractBaseTest;

/**
 * Created by admin on 2017/10/20.
 */
public class MonitorEngineServiceImplTest extends AbstractBaseTest
{
    @Autowired
    AcctAssetChkService acctAssetChkService;
    
    @Test
    public void dealMonitor() throws Exception
    {
        acctAssetChkService.doAcctAssetChk(14241935732510720L);
    }
}