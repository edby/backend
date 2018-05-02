package com.blocain.bitms.api.topic;

import com.blocain.bitms.api.consts.TopicConst;
import org.springframework.data.redis.listener.ChannelTopic;


/**
 * KLineHourChannelTopic Introduce
 * <p>File：KLineHourChannelTopic.java</p>
 * <p>Title: KLineHourChannelTopic</p>
 * <p>Description: KLineHourChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KLineHourChannelTopic extends ChannelTopic
{
    public KLineHourChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_KLINE_HOUR);
    }
}