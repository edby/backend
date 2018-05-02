package com.blocain.bitms.apps.sdk.internal.mapping;

import com.blocain.bitms.apps.sdk.ApiException;
import com.blocain.bitms.apps.sdk.BasicRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.internal.util.SignItem;

/**
 * 动态格式转换器。
 * 
 * @author playguy
 * @since 1.0, Apr 11, 2010
 */
public interface Converter
{
    /**
     * 把字符串转换为响应对象。
     * 
     * @param <T> 领域泛型
     * @param rsp 响应字符串
     * @param clazz 领域类型
     * @return 响应对象
     * @throws ApiException
     */
    <T extends BitmsResponse> T toResponse(String rsp, Class<T> clazz) throws ApiException;
    
    /**
     * 获取响应内的签名数据
     * 
     * @param request
     * @param responseBody
     * @return
     * @throws ApiException
     */
    SignItem getSignItem(BasicRequest<?> request, String responseBody) throws ApiException;
    
    /**
     *  获取解密后的响应内的真实内容
     * 
     * @param request
     * @param body
     * @param format
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws ApiException
     */
    String encryptSourceData(BasicRequest<?> request, String body, String format, String encryptType, String encryptKey, String charset) throws ApiException;
}
