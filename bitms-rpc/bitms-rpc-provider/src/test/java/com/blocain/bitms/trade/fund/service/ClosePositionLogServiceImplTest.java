package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.settlement.entity.ClosePositionLog;
import com.blocain.bitms.trade.settlement.service.ClosePositionLogService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by admin on 2017/10/11.
 */
public class ClosePositionLogServiceImplTest extends AbstractBaseTest
{

    @Autowired ClosePositionLogService closePositionLogService;

    @Autowired
    AccountDebitAssetService accountDebitAssetService;


    @Test
    public void testInserLog()
    {
        AccountDebitAsset r = new AccountDebitAsset();
        AccountDebitAsset record = accountDebitAssetService.findList(r).get(0);
        insertClosePositionLog(record,false,"测试",BigDecimal.ONE);
    }

    /**
     * 新增平仓操作日志
     * @param record
     * @param status
     * @param remark
     * @param btcUsdxInnerPrice
     */
    public void insertClosePositionLog(AccountDebitAsset record, boolean status, String remark, BigDecimal btcUsdxInnerPrice)
    {
        ClosePositionLog log = new ClosePositionLog();
        log.setBorrowerAccountId(record.getBorrowerAccountId());
        log.setDebitId(record.getId());
        log.setDebitAmt(record.getDebitAmt());
        log.setLastPrice(btcUsdxInnerPrice);
        log.setLenderAccountId(record.getLenderAccountId());
        log.setMonitorDate(new Date());
        log.setMonitorLastPrice(record.getLastPrice());
        log.setMonitorMarginRatio(BigDecimal.ZERO);
        log.setRemark(remark);
        log.setStatus(status);
        log.setStockinfoId(record.getStockinfoId());
        log.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        closePositionLogService.insert(log);
    }
}