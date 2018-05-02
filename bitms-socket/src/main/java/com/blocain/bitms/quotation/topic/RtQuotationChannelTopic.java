package com.blocain.bitms.quotation.topic;

import org.springframework.data.redis.listener.ChannelTopic;

import com.blocain.bitms.quotation.consts.TopicConst;

/**
 * RtQuotationChannelTopic Introduce
 * <p>File：RtQuotationChannelTopic.java</p>
 * <p>Title: RtQuotationChannelTopic</p>
 * <p>Description: RtQuotationChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RtQuotationChannelTopic extends ChannelTopic
{
    public RtQuotationChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_RTQUOTATION_PRICE);
    }
}
