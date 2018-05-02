package com.blocain.bitms.quotation.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.quotation.entity.KLine;

import java.util.List;

/**
 * KlineMessage Introduce
 * <p>File：KlineMessage.java</p>
 * <p>Title: KlineMessage</p>
 * <p>Description: KlineMessage</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KlineMessage extends BaseMessage
{
    private  String msgContent = null;

    public KlineMessage()
    {
        super(InQuotationConsts.MESSAGE_TYPE_KLINE);
    }

    public String getMsgInfo(List<KLine> msgContent)
    {
        this.msgContent = JSON.toJSONString(msgContent,SerializerFeature.BeanToArray,SerializerFeature.WriteNonStringValueAsString);

        return JSON.toJSONString(this,SerializerFeature.WriteNonStringValueAsString,SerializerFeature.SortField);
    }

    public String getMsgContent()
    {
        return msgContent;
    }

    public void setMsgContent(String msgContent)
    {
        this.msgContent = msgContent;
    }
}
