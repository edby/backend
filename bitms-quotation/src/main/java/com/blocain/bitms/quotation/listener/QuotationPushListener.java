package com.blocain.bitms.quotation.listener;

import org.springframework.data.redis.connection.Message;

import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.tools.utils.StringUtils;

public class QuotationPushListener extends BaseMessageListener
{
    /**
     * 监听推送信号,将接收时间点放入缓存
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern)
    {
        String content = (String) serializer.deserialize(message.getBody());
        if (StringUtils.isNotBlank(content))
        {
            InQuotationConsts.PUSHTIME_MAP.put(content, System.currentTimeMillis());
        }
    }
}
