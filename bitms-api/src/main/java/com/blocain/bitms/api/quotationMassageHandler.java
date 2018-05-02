package com.blocain.bitms.api;

import com.blocain.bitms.api.consts.TopicConst;

/**
 * 主题处理器，可将不同业务币种的主题与对应的监听器关联起来
 */
public class quotationMassageHandler
{
    private TopicConst topicConst;
    
    public TopicConst getTopicConst()
    {
        return topicConst;
    }
    
    public void setTopicConst(TopicConst topicConst)
    {
        this.topicConst = topicConst;
    }
}
