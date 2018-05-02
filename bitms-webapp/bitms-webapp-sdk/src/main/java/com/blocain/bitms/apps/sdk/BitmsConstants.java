/**
 * blocain.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.blocain.bitms.apps.sdk;

/**
 * BITMS常量定义
 * @author playguy
 */
public class BitmsConstants
{
    /**
     * sha128WithRsa 算法请求类型
     */
    public static final String SIGN_TYPE_RSA             = "RSA";
    
    /**
     * sha256WithRsa 算法请求类型
     */
    public static final String SIGN_TYPE_RSA2            = "RSA2";
    
    public static final String SIGN_TYPE                 = "sign_type";
    
    public static final String SIGN_ALGORITHMS           = "SHA1WithRSA";
    
    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
    
    public static final String ENCRYPT_TYPE_AES          = "AES";
    
    public static final String FORMAT                    = "format";
    
    public static final String TIMESTAMP                 = "timestamp";
    
    public static final String VERSION                   = "version";
    
    public static final String SIGN                      = "sign";

    public static final String AUTH_TOKEN                = "auth_token";
    
    public static final String TERMINAL_TYPE             = "terminal_type";
    
    public static final String TERMINAL_INFO             = "terminal_info";
    
    public static final String CHARSET                   = "charset";
    
    public static final String ENCRYPT_KEY               = "encrypt_key";
    
    public static final String CONTENT_KEY               = "content";
    
    /**  Date默认时区 **/
    public static final String DATE_TIMEZONE             = "GMT+8";
    
    /** UTF-8字符集 **/
    public static final String CHARSET_UTF8              = "UTF-8";
    
    /** JSON 应格式 */
    public static final String FORMAT_JSON               = "json";
    
    /** 新版本节点后缀 */
    public static final String RESPONSE_KEY              = "data";
}
