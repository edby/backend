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
import com.blocain.bitms.quotation.entity.DeepPrice;
import com.blocain.bitms.quotation.mapper.DeepPriceRowMapper;
import com.blocain.bitms.quotation.model.DeepPriceMessage;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * 委托盘口深度行情服务接口
 * <p>File：DeepPriceServiceImpl.java </p>
 * <p>Title: DeepPriceServiceImpl </p>
 * <p>Description:DeepPriceServiceImpl </p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
@Service
public class DeepPriceEngineServiceImpl implements DeepPriceEngineService
{
    public static final Logger logger = LoggerFactory.getLogger(DeepPriceEngineServiceImpl.class);
    
    @Autowired
    private JdbcTemplate       jdbcTemplate;
    
    @Override
    public void pushDeepPriceData(String topic)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        if (StringUtils.isNotEmpty(topic))
        {
            // 1. 从后台数据库获取买入委托数据
            LoggerUtils.logDebug(logger, "============= 委托深度行情数据 start =========================");
            LoggerUtils.logDebug(logger, "SQL:" + QuerySqlConfig.SQL_GET_DEEPPRICE);
            LoggerUtils.logDebug(logger, "topic：" + topic);
            LoggerUtils.logDebug(logger, "============= 委托深度行情数据 end =========================");
            LoggerUtils.logDebug(logger, "委托深度行情价格开始时间：【" + formatter.format(new Date()) + "】");
            List<DeepPrice> entrList = (List<DeepPrice>) jdbcTemplate.query(QuerySqlConfig.SQL_GET_DEEPPRICE, new DeepPriceRowMapper());
            LoggerUtils.logDebug(logger, "委托深度行情价格SQL查询完成：【" + formatter.format(new Date()) + "】");
            LoggerUtils.logDebug(logger, "开始推送买入委托盘口数据");
            // 2. 推送数据到Redis
            DeepPriceMessage deepPriceMessage = new DeepPriceMessage();
            LoggerUtils.logDebug(logger, "委托深度行情价格数据转JSON开始：【" + formatter.format(new Date()) + "】");
            String content = deepPriceMessage.getMsgInfo(entrList);
            LoggerUtils.logDebug(logger, "委托深度行情价格数据转JSON完成：【" + formatter.format(new Date()) + "】");
            LoggerUtils.logDebug(logger, "================= 推送委托深度行情数据 start===========================");
            LoggerUtils.logDebug(logger, "topic:" + topic);
            LoggerUtils.logDebug(logger, "content:" + content.toString());
            LoggerUtils.logDebug(logger, "================= 推送委托深度行情数据 end ===========================");
            LoggerUtils.logDebug(logger, "委托深度行情价格推送开始：【" + formatter.format(new Date()) + "】");
            DataPushUtil.doDataPush(content, topic);
            LoggerUtils.logDebug(logger, "委托深度行情价格推送完成：【" + formatter.format(new Date()) + "】");
            LoggerUtils.logDebug(logger, "推送委托深度行情数据结束");
        }
    }
}
