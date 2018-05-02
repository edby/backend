package com.blocain.bitms.orm.core;

import java.io.UnsupportedEncodingException;

/**
 * 签名与检验
 * @author Playguy
 * @version 1.0
 */
public interface SignableService
{
    /**
     * 进行签名并返回签名后的值
     * @return
     */
    byte[] doSign() throws UnsupportedEncodingException;
    
    /**
     * 验证签名, 正确返回true, 错误返回false
     * @return
     */
    boolean verifySignature();
}
