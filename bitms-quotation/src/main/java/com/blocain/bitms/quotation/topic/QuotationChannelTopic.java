package com.blocain.bitms.quotation.topic;

import org.springframework.data.redis.listener.ChannelTopic;

import com.blocain.bitms.quotation.config.InQuotationConfig;

/**
 * QuotationChannelTopic Introduce
 * <p>Title: QuotationChannelTopic</p>
 * <p>File：QuotationChannelTopic.java</p>
 * <p>Description: QuotationChannelTopic</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class QuotationChannelTopic extends ChannelTopic
{
    public QuotationChannelTopic()
    {
        super(InQuotationConfig.PUSH_TOPIC);
    }
}
