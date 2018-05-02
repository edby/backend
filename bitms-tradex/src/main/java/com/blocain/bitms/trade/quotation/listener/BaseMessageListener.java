package com.blocain.bitms.trade.quotation.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * 基础消息监听器
 * <p>File：BaseMessageListener.java</p>
 * <p>Title: BaseMessageListener</p>
 * <p>Description: BaseMessageListener</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public abstract class BaseMessageListener implements MessageListener
{
    public static final JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
    
    public static final Logger                          logger     = LoggerFactory.getLogger(BaseMessageListener.class);
}
