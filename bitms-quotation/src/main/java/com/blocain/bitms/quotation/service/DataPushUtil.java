package com.blocain.bitms.quotation.service;

import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;

public class DataPushUtil
{
    /**
     * 行情推送逻辑：
     *如果行情内容不为空，且有更新或者接收到新的推送信号，则推送
     * @param content
     * @param topic
     */
    public static void doDataPush(String content, String topic)
    {
        if (StringUtils.isEmpty(content)) { return; }
        Long curPushTime = (Long) InQuotationConsts.PUSHTIME_MAP.get(topic);
        curPushTime = curPushTime == null ? 0l : curPushTime;
        String lastPushKey = new StringBuilder(InQuotationConsts.PUSH_TIME_LAST).append(topic).toString();
        Long lastPushTime = (Long) InQuotationConsts.PUSHTIME_MAP.get(lastPushKey);
        lastPushTime = lastPushTime == null ? 0l : lastPushTime;
        String lastContent = (String) InQuotationConsts.CONTENT_MAP.get(topic);
        if ((!content.equals(lastContent)) || curPushTime > lastPushTime)
        {
            // 满足推送条件，推送数据
            RedisUtils.send(topic, content);
            // System.out.println("当前推送时间：" + curPushTime);
            // System.out.println("上次推送时间：" + lastPushTime);
            // System.out.println("当前推送主题：" + topic);
            // 推送成功后更新静态缓存
            InQuotationConsts.CONTENT_MAP.put(topic, content);
            InQuotationConsts.PUSHTIME_MAP.put(lastPushKey, curPushTime);
        }
    }
}
