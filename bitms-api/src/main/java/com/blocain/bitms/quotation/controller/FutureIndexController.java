package com.blocain.bitms.quotation.controller;

import java.util.HashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.api.consts.TopicConst;
import com.blocain.bitms.api.consts.TopicQuotationConst;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api/v2")
public class FutureIndexController extends GenericController
{
    private static final Logger logger = LoggerFactory.getLogger(FutureIndexController.class);
    
    @Resource(name = "topicMap")
    private HashMap             topicMap;
    
    /**
     * 获取合约行情
     *
     * @param symbol
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/ticker")
    @ApiOperation(value = "合约行情API", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage ticker(String symbol) throws BusinessException
    {
        if (topicMap.containsKey(symbol))
        {
            // 获取业务品种对应的消息主题
            String topic = ((TopicConst) topicMap.get(symbol)).TOPIC_RTQUOTATION_PRICE;
            String content = TopicQuotationConst.rtQuotationMap.get(topic);
            JSONObject json = JSON.parseObject(content);
            return getJsonMessage(CommonEnums.SUCCESS, json);
        }
        else
        {
            return getJsonMessage(CommonEnums.SUCCESS, "symbol不合法！");
        }
    }
    
    /**
     * 获取合约深度信息
     *
     * @param symbol
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/deep")
    @ApiOperation(value = "合约深度", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage depth(String symbol) throws BusinessException
    {
        if (topicMap.containsKey(symbol))
        {
            // 获取业务品种对应的消息主题
            String topic = ((TopicConst) topicMap.get(symbol)).TOPIC_ENTRUST_DEEPPRICE;
            String content = TopicQuotationConst.deepPriceMap.get(topic);
            JSONObject json = JSON.parseObject(content);
            return getJsonMessage(CommonEnums.SUCCESS, json);
        }
        else
        {
            return getJsonMessage(CommonEnums.SUCCESS, "symbol不合法！");
        }
    }
    
    /**
     * 获取合约交易记录信息
     *
     * @param symbol
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/deal")
    @ApiOperation(value = "合约交易信息", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage trades(String symbol) throws BusinessException
    {
        if (topicMap.containsKey(symbol))
        {
            // 获取业务品种对应的消息主题
            String topic = ((TopicConst) topicMap.get(symbol)).TOPIC_REALDEAL_TRANSACTION;
            String content = TopicQuotationConst.realdealMap.get(topic);
            JSONObject json = JSON.parseObject(content);
            return getJsonMessage(CommonEnums.SUCCESS, json);
        }
        else
        {
            return getJsonMessage(CommonEnums.SUCCESS, "symbol不合法！");
        }
    }
    
    /**
     * 获取合约交易K线图
     *
     * @param symbol
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/{kLine}")
    @ApiOperation(value = "合约交易K线图信息", httpMethod = "GET", consumes = "application/x-www-form-urlencoded")
    public JsonMessage kline(String symbol, @PathVariable String kLine) throws BusinessException
    {
        if (topicMap.containsKey(symbol))
        {
            // 获取业务品种对应的消息主题
            String topic = "";
            if (TopicQuotationConst.KLINE_1M.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_1M;
            }
            else if (TopicQuotationConst.KLINE_5M.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_5M;
            }
            else if (TopicQuotationConst.KLINE_15M.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_15M;
            }
            else if (TopicQuotationConst.KLINE_30M.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_30M;
            }
            else if (TopicQuotationConst.KLINE_HOUR.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_HOUR;
            }
            else if (TopicQuotationConst.KLINE_DAY.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_DAY;
            }
            else if (TopicQuotationConst.KLINE_WEEK.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_WEEK;
            }
            else if (TopicQuotationConst.KLINE_MONTH.equals(kLine))
            {
                topic = ((TopicConst) topicMap.get(symbol)).TOPIC_KLINE_MONTH;
            }
            String content = TopicQuotationConst.kLineMap.get(topic);
            JSONObject json = JSON.parseObject(content);
            return getJsonMessage(CommonEnums.SUCCESS, json);
        }
        else
        {
            return getJsonMessage(CommonEnums.SUCCESS, "symbol不合法！");
        }
    }
}
