package com.blocain.bitms.quotation.topic;

import org.springframework.data.redis.listener.ChannelTopic;

import com.blocain.bitms.quotation.consts.TopicConst;

/**
 * RealDealChannelTopic Introduce
 * <p>File：RealDealChannelTopic.java</p>
 * <p>Title: RealDealChannelTopic</p>
 * <p>Description: RealDealChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RealDealChannelTopic extends ChannelTopic
{
    public RealDealChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_REALDEAL_TRANSACTION);
    }
}
