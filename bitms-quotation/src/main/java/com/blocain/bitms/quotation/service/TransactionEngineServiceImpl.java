package com.blocain.bitms.quotation.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.blocain.bitms.quotation.config.QuerySqlConfig;
import com.blocain.bitms.quotation.entity.Transaction;
import com.blocain.bitms.quotation.mapper.TransactionRowMapper;
import com.blocain.bitms.quotation.model.TransactionMessage;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * 成交流水接口服务
 * <p>File：TransactionServiceImpl.java</p>
 * <p>Title: TransactionServiceImpl</p>
 * <p>Description:TransactionServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class TransactionEngineServiceImpl implements TransactionEngineService
{
    public static final Logger logger = LoggerFactory.getLogger(TransactionEngineServiceImpl.class);
    
    @Autowired
    private JdbcTemplate       jdbcTemplate;
    
    @Override
    public void pushTransactionData(String topic, Integer pageSize)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        LoggerUtils.logDebug(logger, "开始推送成交流水数据");
        LoggerUtils.logDebug(logger, "成交流水开始时间：【" + formatter.format(new Date()) + "】");
        // 1. 从后台数据库获取成交流水数据
        LoggerUtils.logDebug(logger, "================= 成交流水取数SQL ===========================");
        LoggerUtils.logDebug(logger, "SQL:" + QuerySqlConfig.SQL_GET_REALDEAL);
        LoggerUtils.logDebug(logger, "pageSize:" + pageSize);
        LoggerUtils.logDebug(logger, "================= 成交流水取数SQL ===========================");
        LoggerUtils.logDebug(logger, "成交流水SQL查询开始时间：【" + formatter.format(new Date()) + "】");
        List<Transaction> realDealList = (List<Transaction>) jdbcTemplate.query(QuerySqlConfig.SQL_GET_REALDEAL, new TransactionRowMapper(), pageSize);
        LoggerUtils.logDebug(logger, "成交流水SQL查询完成时间：【" + formatter.format(new Date()) + "】");
        // 2. 推送数据到Redis
        TransactionMessage transactionMessage = new TransactionMessage();
        LoggerUtils.logDebug(logger, "成交流水数据转JSON开始时间：【" + formatter.format(new Date()) + "】");
        String content = transactionMessage.getMsgInfo(realDealList);
        LoggerUtils.logDebug(logger, "成交流水数据转JSON完成时间：【" + formatter.format(new Date()) + "】");
        LoggerUtils.logDebug(logger, "================= 推送成交流水数据 ===========================");
        LoggerUtils.logDebug(logger, "topic:" + topic);
        LoggerUtils.logDebug(logger, "content:" + content.toString());
        LoggerUtils.logDebug(logger, "================= 推送成交流水数据 ===========================");
        LoggerUtils.logDebug(logger, "成交流水数据推送开始时间：【" + formatter.format(new Date()) + "】");
        DataPushUtil.doDataPush(content, topic);
        LoggerUtils.logDebug(logger, "成交流水数据推送完成时间：【" + formatter.format(new Date()) + "】");
        LoggerUtils.logDebug(logger, "推送成交流水数据结束");
    }
}
