package com.blocain.bitms.api.topic;

import com.blocain.bitms.api.consts.TopicConst;
import org.springframework.data.redis.listener.ChannelTopic;


/**
 * KLine30MinChannelTopic Introduce
 * <p>File：KLine30MinChannelTopic.java</p>
 * <p>Title: KLine30MinChannelTopic</p>
 * <p>Description: KLine30MinChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KLine30MinChannelTopic extends ChannelTopic
{
    public KLine30MinChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_KLINE_30M);
    }
}