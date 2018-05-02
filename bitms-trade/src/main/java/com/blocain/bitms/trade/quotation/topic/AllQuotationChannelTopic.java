package com.blocain.bitms.trade.quotation.topic;

import com.blocain.bitms.trade.quotation.consts.AllQuotationTopicConst;
import org.springframework.data.redis.listener.ChannelTopic;

import com.blocain.bitms.trade.quotation.consts.TopicConst;

/**
 * 全行情消息主题
 */
public class AllQuotationChannelTopic extends ChannelTopic
{
    public AllQuotationChannelTopic(AllQuotationTopicConst topicConst)
    {
        super(topicConst.TOPIC_ALLRTQUOTATION);
    }
}
