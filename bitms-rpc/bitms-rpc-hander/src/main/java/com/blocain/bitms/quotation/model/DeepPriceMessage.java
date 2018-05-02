package com.blocain.bitms.quotation.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blocain.bitms.quotation.consts.InQuotationConsts;

import java.util.List;

public class DeepPriceMessage extends BaseMessage
{
    private List msgContent;

    public DeepPriceMessage()
    {
        super(InQuotationConsts.MESSAGE_TYPE_DEEPPRICE);
    }
    
    public String getMsgInfo(List msgContent)
    {
        this.msgContent = msgContent;
        return JSON.toJSONString(this, SerializerFeature.WriteNonStringValueAsString,SerializerFeature.WriteNullListAsEmpty);
    }
    
    public List getMsgContent()
    {
        return msgContent;
    }
    
    public void setMsgContent(List msgContent)
    {
        this.msgContent = msgContent;
    }
}
