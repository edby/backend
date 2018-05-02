package com.blocain.bitms.api.listener;

import com.blocain.bitms.api.quotationMassageHandler;
import com.blocain.bitms.api.consts.TopicQuotationConst;
import org.springframework.data.redis.connection.Message;

/**
 * RtQuotationMessageListener Introduce
 * <p>File：RtQuotationMessageListener.java</p>
 * <p>Title: RtQuotationMessageListener</p>
 * <p>Description: RtQuotationMessageListener</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RtQuotationMessageListener extends BaseMessageListener
{
    // 币种主题处理器，用于获取监听器对应的topic.
    private quotationMassageHandler bizCategoryHandler;
    
    @Override
    public void onMessage(Message message, byte[] pattern)
    {
        String content = (String) serializer.deserialize(message.getBody());
        String topic = bizCategoryHandler.getTopicConst().TOPIC_RTQUOTATION_PRICE;
        // 将最新的消息保存在静态map对应的主题中（会替换掉老的消息）
        TopicQuotationConst.rtQuotationMap.put(topic, content);
    }
    
    public void setBizCategoryHandler(quotationMassageHandler bizCategoryHandler)
    {
        this.bizCategoryHandler = bizCategoryHandler;
    }
}
