package com.blocain.bitms.monitor;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.monitor.service.MonitorMarginService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/9/20.
 */
public class MonitorMarginImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    MonitorMarginService monitorMarginService;
    
    @Test
    public void dealMarginMonitor() throws Exception
    {
        while(true)
        {
            //monitorMarginService.dealMarginMonitor();
        }
    }

    @Test
    public void findClosePositionDataList()
    {
        monitorMarginService.findClosePositionDataList();
    }
}