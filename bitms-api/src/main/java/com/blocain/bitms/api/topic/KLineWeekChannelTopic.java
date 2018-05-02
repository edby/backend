package com.blocain.bitms.api.topic;

import com.blocain.bitms.api.consts.TopicConst;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * KLineWeekChannelTopic Introduce
 * <p>File：KLineWeekChannelTopic.java</p>
 * <p>Title: KLineWeekChannelTopic</p>
 * <p>Description: KLineWeekChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KLineWeekChannelTopic extends ChannelTopic
{
    public KLineWeekChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_KLINE_WEEK);
    }
}