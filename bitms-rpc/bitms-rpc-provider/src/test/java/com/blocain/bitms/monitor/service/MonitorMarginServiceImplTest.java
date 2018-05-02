package com.blocain.bitms.monitor.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/10/20.
 */
public class MonitorMarginServiceImplTest extends AbstractBaseTest
{
    @Autowired
    MonitorMarginService monitorMarginService;
    
    @Test
    public void findListByIds() throws Exception
    {
//      monitorMarginService.findListByIds("3", "");
    }
    
    @Test
    public void findClosePositionDataList() throws Exception
    {
        monitorMarginService.findClosePositionDataList();
    }
}