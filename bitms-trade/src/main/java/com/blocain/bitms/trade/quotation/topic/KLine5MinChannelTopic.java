package com.blocain.bitms.trade.quotation.topic;

import org.springframework.data.redis.listener.ChannelTopic;

import com.blocain.bitms.trade.quotation.consts.TopicConst;

/**
 * KLine5MinChannelTopic Introduce
 * <p>File：KLine5MinChannelTopic.java</p>
 * <p>Title: KLine5MinChannelTopic</p>
 * <p>Description: KLine5MinChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KLine5MinChannelTopic extends ChannelTopic
{
    public KLine5MinChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_KLINE_5M);
    }
}