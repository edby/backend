package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.entity.SheetBalance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/1/9.
 */
public class SheetBalanceServiceImplTest extends AbstractBaseTest
{
    @Test
    public void selectAllAdmin() throws Exception
    {
        SheetBalance sheetBalance = new SheetBalance();
        sheetBalance.setStartDate(20180109L);
        sheetBalance.setEndDate(20180110L);
        sheetBalanceService.selectAllAdmin(sheetBalance);
    }

    @Autowired
    SheetBalanceService sheetBalanceService;

    @Test
    public void platCalUserAssetDebitForDay() throws Exception
    {
        sheetBalanceService.insertPlatCalUserAssetDebitForDay();
    }

    @Test
    public void list() throws Exception
    {
        SheetBalance entity = new SheetBalance();
        sheetBalanceService.findList(entity);
    }
}