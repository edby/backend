package com.blocain.bitms.quotation.topic;

import org.springframework.data.redis.listener.ChannelTopic;

import com.blocain.bitms.quotation.consts.TopicConst;

/**
 * KLine15MinChannelTopic Introduce
 * <p>File：KLine15MinChannelTopic.java</p>
 * <p>Title: KLine15MinChannelTopic</p>
 * <p>Description: KLine15MinChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KLine15MinChannelTopic extends ChannelTopic
{
    public KLine15MinChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_KLINE_15M);
    }
}