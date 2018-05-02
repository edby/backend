package com.blocain.bitms.api.topic;

import com.blocain.bitms.api.consts.TopicConst;
import org.springframework.data.redis.listener.ChannelTopic;


/**
 * KLine1MinChannelTopic Introduce
 * <p>File：KLine1MinChannelTopic.java</p>
 * <p>Title: KLine1MinChannelTopic</p>
 * <p>Description: KLine1MinChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KLine1MinChannelTopic extends ChannelTopic
{
    public KLine1MinChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_KLINE_1M);
    }
}
