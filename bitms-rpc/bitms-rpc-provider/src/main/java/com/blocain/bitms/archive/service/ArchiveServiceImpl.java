package com.blocain.bitms.archive.service;

import com.blocain.bitms.archive.config.ArchiveConfig;
import com.blocain.bitms.archive.mapper.ArchiveServiceMapper;
import com.blocain.bitms.tools.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.blocain.bitms.trade.risk.service.EnableServiceImpl.logger;
@Service
public class ArchiveServiceImpl implements ArchiveService
{

    @Autowired
    private ArchiveServiceMapper archiveServiceMapper;

    @Override
    public void dealQuotationKlineArchive()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= K线数据归档，开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("preactTime", ArchiveConfig.KLINE_ARCHIVE_PREACTTINE);
        resultMap.put("unit", ArchiveConfig.ARCHIVE_PREACTUNIT_MONTH);
        archiveServiceMapper.dealQuotationKlineArchive(resultMap);
        LoggerUtils.logDebug(logger, "================= K线数据归档，结束执行存储过程:【" + formatter.format(new Date()) + "】");
    }

    @Override
    public void dealQuotationArchive()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 外部指数行情归档，开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("preactTime", ArchiveConfig.QUOTATION_ARCHIVE_PREACTTINE);
        resultMap.put("unit", ArchiveConfig.ARCHIVE_PREACTUNIT_HOUR);
        archiveServiceMapper.dealQuotationArchive(resultMap);
        LoggerUtils.logDebug(logger, "================= 外部指数行情归档，结束执行存储过程:【" + formatter.format(new Date()) + "】");
    }

    @Override
    public void dealFundcurrentArchive()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 资金流水归档，开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("preactTime", ArchiveConfig.FUNDCURRENT_ARCHIVE_PREACTTINE);
        resultMap.put("unit", ArchiveConfig.ARCHIVE_PREACTUNIT_MIN);
        archiveServiceMapper.dealFundcurrentArchive(resultMap);
        LoggerUtils.logDebug(logger, "================= 资金流水归档，结束执行存储过程:【" + formatter.format(new Date()) + "】");
    }

    @Override
    public void dealTradeArchive()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 交易相关流水归档，开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("preactTime", ArchiveConfig.TRADE_ARCHIVE_PREACTTINE);
        resultMap.put("unit", ArchiveConfig.ARCHIVE_PREACTUNIT_MIN);
        resultMap.put("rows", ArchiveConfig.TRADE_ARCHIVE_ROWS);
        archiveServiceMapper.dealTradeArchive(resultMap);
        LoggerUtils.logDebug(logger, "================= 交易相关流水归档，结束执行存储过程:【" + formatter.format(new Date()) + "】");
    }
}
