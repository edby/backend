package com.blocain.bitms.quotation.listener;

import org.springframework.data.redis.connection.Message;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * 全行情推送消息监听器
 */
public class AllRtQuotationMessageListener extends BaseMessageListener
{
    public static final String WATCH_FLAG = "open";
    
    /**
     * 监听推送信号消息，将接收的数据放在静态缓存中
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern)
    {
        if (WATCH_FLAG.equals(InQuotationConfig.QUOTATION_SWITCH))
        {
            String content = (String) serializer.deserialize(message.getBody());
            if (StringUtils.isNotBlank(content))
            {
                InQuotationConsts.PUSHTIME_MAP.put(content, System.currentTimeMillis());
            }
        }
    }
}
