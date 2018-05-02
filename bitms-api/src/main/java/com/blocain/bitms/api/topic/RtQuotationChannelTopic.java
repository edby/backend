package com.blocain.bitms.api.topic;

import com.blocain.bitms.api.consts.TopicConst;
import org.springframework.data.redis.listener.ChannelTopic;


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
