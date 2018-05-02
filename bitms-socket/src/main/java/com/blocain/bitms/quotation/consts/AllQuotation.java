package com.blocain.bitms.quotation.consts;

import com.blocain.bitms.tools.utils.PropertiesUtils;

/**
 * TopicConst Introduce
 * <p>File：TopicConst.java</p>
 * <p>Title: TopicConst</p>
 * <p>Description: TopicConst</p>
 * <p>Copyright: Copyright (c) 2018/3/6</p>
 * <p>Company: BloCain</p>
 *
 * @author fzk
 * @version 1.0
 */
public class AllQuotation
{
    private AllQuotation()
    {
    }
    
    // 全行情
    public String TOPIC_ALLRTQUOTATION;
    
    public AllQuotation(String props)
    {
        PropertiesUtils properties = new PropertiesUtils(props);
        // 全行情
        TOPIC_ALLRTQUOTATION = properties.getProperty("topic.allRtQuotation");
    }
}
