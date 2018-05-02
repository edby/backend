/**
 * blocain.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.blocain.bitms.apps.sdk.internal.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import com.blocain.bitms.apps.sdk.ApiException;
import com.blocain.bitms.apps.sdk.BitmsConstants;

/**
 * 加签工具
 * @author playguy
 */
public class Signature
{
    /**
     * 获取私钥
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, String ins) throws Exception
    {
        if (ins == null || StringUtils.isEmpty(algorithm)) { return null; }
        byte[] encodedKey = Base64.decode(ins);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }
    
    /**
     * 获取公钥
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromX509(String algorithm, String ins) throws Exception
    {
        if (ins == null || StringUtils.isEmpty(algorithm)) { return null; }
        byte[] encodedKey = Base64.decode(ins);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }
    
    /**
     * 集合去重和排序
     * @param requestHolder
     * @return
     */
    public static Map<String, String> getSortedMap(RequestHolder requestHolder)
    {
        Map<String, String> sortedParams = new TreeMap<String, String>();
        BitmsMap appParams = requestHolder.getApplicationParams();
        if (appParams != null && appParams.size() > 0)
        {
            sortedParams.putAll(appParams);
        }
        BitmsMap protocalMustParams = requestHolder.getProtocalMustParams();
        if (protocalMustParams != null && protocalMustParams.size() > 0)
        {
            sortedParams.putAll(protocalMustParams);
        }
        BitmsMap protocalOptParams = requestHolder.getProtocalOptParams();
        if (protocalOptParams != null && protocalOptParams.size() > 0)
        {
            sortedParams.putAll(protocalOptParams);
        }
        return sortedParams;
    }
    
    /**
     * 将请求对象转成文本
     * @param requestHolder
     * @return
     */
    public static String getSignatureContent(RequestHolder requestHolder)
    {
        return getSignContent(getSortedMap(requestHolder));
    }
    
    /**
     * 将Map参数对象转成文本
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams)
    {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.areNotEmpty(key, value))
            {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }
    
    /**
     *  参数签名
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws ApiException
     */
    public static String rsaSign(String content, String privateKey, String charset, String signType) throws ApiException
    {
        if (BitmsConstants.SIGN_TYPE_RSA.equals(signType))
        {
            return rsa128Sign(content, privateKey, charset);
        }
        else if (BitmsConstants.SIGN_TYPE_RSA2.equals(signType))
        {
            return rsa256Sign(content, privateKey, charset);
        }
        else
        {
            throw new ApiException("Sign Type is Not Support : signType=" + signType);
        }
    }
    
    /**
     * 参数校验
     * @param content 内容
     * @param sign 签名
     * @param publicKey 公钥
     * @param charset 编码
     * @param signType 签名类型
     * @return
     * @throws ApiException
     */
    public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType) throws ApiException
    {
        if (BitmsConstants.SIGN_TYPE_RSA.equals(signType))
        {
            return rsaCheckContent(content, sign, publicKey, charset);
        }
        else if (BitmsConstants.SIGN_TYPE_RSA2.equals(signType))
        {
            return rsa256CheckContent(content, sign, publicKey, charset);
        }
        else
        {
            throw new ApiException("Sign Type is Not Support : signType=" + signType);
        }
    }
    
    /**
     * sha1WithRsa 加签
     * @param params
     * @param privateKey
     * @param charset
     * @return
     * @throws ApiException
     */
    public static String rsa128Sign(Map<String, String> params, String privateKey, String charset) throws ApiException
    {
        String signContent = getSignContent(params);
        return rsa128Sign(signContent, privateKey, charset);
    }
    
    /**
     * sha1WithRsa 加签
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws ApiException
     */
    public static String rsa128Sign(String content, String privateKey, String charset) throws ApiException
    {
        try
        {
            PrivateKey priKey = getPrivateKeyFromPKCS8(BitmsConstants.SIGN_TYPE_RSA, privateKey);
            java.security.Signature signature = java.security.Signature.getInstance(BitmsConstants.SIGN_ALGORITHMS);
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset))
            {
                signature.update(content.getBytes());
            }
            else
            {
                signature.update(content.getBytes(charset));
            }
            return Base64.encodeStr(signature.sign());
        }
        catch (InvalidKeySpecException ie)
        {
            throw new ApiException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
        }
        catch (Exception e)
        {
            throw new ApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }
    
    /**
     * sha256WithRsa 加签
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws ApiException
     */
    public static String rsa256Sign(String content, String privateKey, String charset) throws ApiException
    {
        try
        {
            PrivateKey priKey = getPrivateKeyFromPKCS8(BitmsConstants.SIGN_TYPE_RSA, privateKey);
            java.security.Signature signature = java.security.Signature.getInstance(BitmsConstants.SIGN_SHA256RSA_ALGORITHMS);
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset))
            {
                signature.update(content.getBytes());
            }
            else
            {
                signature.update(content.getBytes(charset));
            }
            return Base64.encodeStr(signature.sign());
        }
        catch (Exception e)
        {
            throw new ApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }
    
    public static String getSignCheckContentV1(Map<String, String> params)
    {
        if (params == null) { return null; }
        params.remove("sign");
        params.remove("sign_type");
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }
    
    public static String getSignCheckContentV2(Map<String, String> params)
    {
        if (params == null) { return null; }
        params.remove("sign");
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }
    
    public static boolean rsaCheckV1(Map<String, String> params, String publicKey, String charset) throws ApiException
    {
        String sign = params.get("sign");
        String content = getSignCheckContentV1(params);
        return rsaCheckContent(content, sign, publicKey, charset);
    }
    
    public static boolean rsaCheckV1(Map<String, String> params, String publicKey, String charset, String signType) throws ApiException
    {
        String sign = params.get("sign");
        String content = getSignCheckContentV1(params);
        return rsaCheck(content, sign, publicKey, charset, signType);
    }
    
    public static boolean rsaCheckV2(Map<String, String> params, String publicKey, String charset) throws ApiException
    {
        String sign = params.get("sign");
        String content = getSignCheckContentV2(params);
        return rsaCheckContent(content, sign, publicKey, charset);
    }
    
    public static boolean rsaCheckV2(Map<String, String> params, String publicKey, String charset, String signType) throws ApiException
    {
        String sign = params.get("sign");
        String content = getSignCheckContentV2(params);
        return rsaCheck(content, sign, publicKey, charset, signType);
    }
    
    /**
     * RSA校验
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws ApiException
     */
    static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) throws ApiException
    {
        try
        {
            PublicKey pubKey = getPublicKeyFromX509(BitmsConstants.SIGN_TYPE_RSA, publicKey);
            java.security.Signature signature = java.security.Signature.getInstance(BitmsConstants.SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            if (StringUtils.isEmpty(charset))
            {
                signature.update(content.getBytes());
            }
            else
            {
                signature.update(content.getBytes(charset));
            }
            return signature.verify(Base64.decode(sign));
        }
        catch (Exception e)
        {
            throw new ApiException(e.getLocalizedMessage());
        }
    }
    
    /**
     * RSA256校验
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws ApiException
     */
    static boolean rsa256CheckContent(String content, String sign, String publicKey, String charset) throws ApiException
    {
        try
        {
            PublicKey pubKey = getPublicKeyFromX509(BitmsConstants.SIGN_TYPE_RSA, publicKey);
            java.security.Signature signature = java.security.Signature.getInstance(BitmsConstants.SIGN_SHA256RSA_ALGORITHMS);
            signature.initVerify(pubKey);
            if (StringUtils.isEmpty(charset))
            {
                signature.update(content.getBytes());
            }
            else
            {
                signature.update(content.getBytes(charset));
            }
            return signature.verify(Base64.decode(sign));
        }
        catch (Exception e)
        {
            throw new ApiException(e.getLocalizedMessage());
        }
    }
    
    /**
     * 验证签名
     * @param params
     * @param publicKey
     * @return
     */
    public static boolean checkSign(Map<String, String> params, String publicKey)
    {
        String charset = params.get("charset");
        String signType = params.get("sign_type");
        return rsaCheckV2(params, publicKey, charset, signType);
    }
    
    /**
     * 验证签名
     * @param params
     * @param publicKey     公钥
     * @param isDecrypt     是否解密
     * @return 解密后明文，验签失败则异常抛出
     * @throws ApiException
     */
    public static String checkSignAndDecrypt(Map<String, String> params, String publicKey, boolean isDecrypt) throws ApiException
    {
        String content = params.get("content");
        // 开始签名
        if (!rsaCheckV2(params, publicKey, params.get("charset"), BitmsConstants.SIGN_TYPE_RSA2))
        {// 签名失败之后直接抛出异常
            throw new ApiException("签名验证失败");
        }
        if (isDecrypt)
        {// 解密
            String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
            if (StringUtils.isEmpty(encryptKey)) return content;
            return Encrypt.decryptContent(content, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey);
        }
        return content;
    }
    
    /**
     * 验证签名
     * @param params
     * @param publicKey     公钥
     * @param isCheckSign   是否验签
     * @param isDecrypt     是否解密
     * @return 解密后明文，验签失败则异常抛出
     * @throws ApiException
     */
    public static String checkSignAndDecrypt(Map<String, String> params, String publicKey, boolean isCheckSign, boolean isDecrypt) throws ApiException
    {
        String content = params.get("content");
        if (isCheckSign)
        {// 开始签名
            if (!rsaCheckV2(params, publicKey, params.get("charset"), params.get("sign_type")))
            {// 签名失败之后直接抛出异常
                throw new ApiException("rsaCheck failure:rsaParams=" + params);
            }
        }
        if (isDecrypt)
        {// 解密
            String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
            if (StringUtils.isEmpty(encryptKey)) return content;
            return Encrypt.decryptContent(content, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey);
        }
        return content;
    }
}
