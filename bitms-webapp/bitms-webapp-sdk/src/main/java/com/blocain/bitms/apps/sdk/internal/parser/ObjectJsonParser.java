package com.blocain.bitms.apps.sdk.internal.parser;

import com.blocain.bitms.apps.sdk.*;
import com.blocain.bitms.apps.sdk.internal.util.SignItem;
import com.blocain.bitms.apps.sdk.internal.mapping.Converter;

/**
 * 单个JSON对象解释器。
 * 
 * @author playguy
 * @since 1.0, Apr 11, 2010
 */
public class ObjectJsonParser<T extends BitmsResponse> implements BitmsParser<T>
{
    private Class<T> clazz;
    
    public ObjectJsonParser(Class<T> clazz)
    {
        this.clazz = clazz;
    }
    
    public T parse(String rsp) throws ApiException
    {
        Converter converter = new JsonConverter();
        return converter.toResponse(rsp, clazz);
    }
    
    public Class<T> getResponseClass()
    {
        return clazz;
    }
    
    /** 
     * @see BitmsParser#getSignItem(BasicRequest, String)
     */
    public SignItem getSignItem(BasicRequest<?> request, String responseBody) throws ApiException
    {
        Converter converter = new JsonConverter();
        return converter.getSignItem(request, responseBody);
    }
    
    /** 
     * @see BitmsParser#encryptSourceData(BasicRequest, String, String, String, String, String)
     */
    public String encryptSourceData(BasicRequest<?> request, String body, String format, String encryptType, String encryptKey, String charset) throws ApiException
    {
        Converter converter = new JsonConverter();
        return converter.encryptSourceData(request, body, format, encryptType, encryptKey, charset);
    }
}
