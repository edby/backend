package com.blocain.bitms.quotation.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blocain.bitms.quotation.consts.InQuotationConsts;

import java.util.List;

/**
 * 内部行情_成交交易流水消息
 * RealDealMessage Introduce
 * <p>File：RealDealMessage.java</p>
 * <p>Title: RealDealMessage</p>
 * <p>Description: RealDealMessage</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class TransactionMessage extends BaseMessage
{
    private List msgContent;

    public TransactionMessage()
    {
        super(InQuotationConsts.MESSAGE_TYPE_REALDEAL);
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
