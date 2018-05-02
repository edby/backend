package com.blocain.bitms.trade.quotation.topic;

import com.blocain.bitms.trade.quotation.consts.TopicConst;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * 全行情消息主题
 */
public class AllRtQuotationChannelTopic extends ChannelTopic
{
    public AllRtQuotationChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_ALLRTQUOTATION);
    }
}
