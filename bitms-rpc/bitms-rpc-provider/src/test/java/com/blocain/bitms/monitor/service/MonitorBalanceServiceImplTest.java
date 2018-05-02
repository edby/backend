package com.blocain.bitms.monitor.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.MonitorBalance;
import com.blocain.bitms.tools.utils.CalendarUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/20.
 */
public class MonitorBalanceServiceImplTest extends AbstractBaseTest
{
    @Autowired
    MonitorBalanceService monitorBalanceService;
    
    @Test
    public void createInitialBalance() throws Exception
    {

        List<Long> acctList = new ArrayList<Long>();
        acctList.add(123L);
        acctList.add(456L);


        List<MonitorBalance> list = setAttr(acctList);

        int iResult = monitorBalanceService.createInitialBalance(list);
        System.out.println(iResult);
    }

    /**
     * 根据参数列表初始化实体列表
     * @param acctList
     * @return
     */
    public List<MonitorBalance> setAttr(List<Long> acctList)
    {
        List<MonitorBalance> list = new ArrayList<MonitorBalance>();

        Long currentLong = CalendarUtils.getCurrentLong(); //业务日期必须为统一的日期
        Timestamp time = new Timestamp(CalendarUtils.getCurrentLong());
        for (Long acctId : acctList)
        {
            MonitorBalance monitorBalance = initAttr(time);
            monitorBalance.setAccountId(acctId);

            list.add(monitorBalance);
        }

        return list;
    }

    /***
     * 初始化属性
     * @param time
     * @return
     */
    private MonitorBalance initAttr(Timestamp time)
    {
        MonitorBalance monitorBalance = new MonitorBalance();
        monitorBalance.setAccountId(0L);
        monitorBalance.setStockinfoId(1L);
        monitorBalance.setAcctAssetType(MonitorConst.MONITOR_ASSETTYPE_WALLET);
        //期初日期
        monitorBalance.setBusinessDate(time);
        monitorBalance.setBeginBal(BigDecimal.ZERO);
        monitorBalance.setBeginFrozenBal(BigDecimal.ZERO);
        monitorBalance.setBeginFeeBal(BigDecimal.ZERO);
        monitorBalance.setEndBal(BigDecimal.ZERO);
        monitorBalance.setEndFrozenBal(BigDecimal.ZERO);
        monitorBalance.setEndFeeBal(BigDecimal.ZERO);
        //生成日期
        monitorBalance.setCreateDate(time);

        return monitorBalance;
    }
}