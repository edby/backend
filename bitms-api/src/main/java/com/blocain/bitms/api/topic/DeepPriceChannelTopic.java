package com.blocain.bitms.api.topic;

import com.blocain.bitms.api.consts.TopicConst;
import org.springframework.data.redis.listener.ChannelTopic;


/**
 * DeepPriceChannelTopic Introduce
 * <p>File：DeepPriceChannelTopic.java</p>
 * <p>Title: DeepPriceChannelTopic</p>
 * <p>Description: DeepPriceChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DeepPriceChannelTopic extends ChannelTopic
{
    public DeepPriceChannelTopic(TopicConst topicConst)
    {
        super(topicConst.TOPIC_ENTRUST_DEEPPRICE);
    }
}