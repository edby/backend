package com.blocain.bitms.quotation.topic;

import org.springframework.data.redis.listener.ChannelTopic;

import com.blocain.bitms.quotation.consts.TopicConst;

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