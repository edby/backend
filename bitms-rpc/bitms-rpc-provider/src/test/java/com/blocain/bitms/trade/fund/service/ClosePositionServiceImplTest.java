package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.monitor.entity.MonitorMargin;
import com.blocain.bitms.monitor.service.MonitorMarginService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.trade.service.ClosePositionService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/9/19.
 */
public class ClosePositionServiceImplTest extends AbstractBaseTest
{

    @Autowired
    ClosePositionService            closePositionService;

    @Autowired MonitorMarginService monitorMarginService;

    /**
     * 强制平仓
     */
    @Test
    public void doClosePositionSelect() throws Exception
    {
        try{
        MonitorMargin monitorMargin = new MonitorMargin();
//      List<MonitorMargin> list = monitorMarginService.findList(monitorMargin);
//      monitorMargin = list.get(0);
        monitorMargin.setExplosionPrice(BigDecimal.valueOf(2000));
        //closePositionService.doClosePositionSelect("300000067890",FundConsts.WALLET_BTC_TYPE,FundConsts.WALLET_BTC2USD_TYPE,monitorMargin);
            closePositionService.doClosePositionSelect("300000067890",FundConsts.WALLET_BTC_TYPE,FundConsts.WALLET_BTC2USD_TYPE,monitorMargin);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 自动平仓
     * @throws Exception
     */
    @Test
    public void doClosePositionAuto() throws Exception
    {
        closePositionService.autoClosePosition();
    }

}