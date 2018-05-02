package com.blocain.bitms.quotation.topic;

import com.blocain.bitms.quotation.consts.AllQuotation;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * 全行情消息主题
 */
public class AllQuotationChannelTopic extends ChannelTopic
{
    public AllQuotationChannelTopic(AllQuotation topicConst)
    {
        super(topicConst.TOPIC_ALLRTQUOTATION);
    }
}
