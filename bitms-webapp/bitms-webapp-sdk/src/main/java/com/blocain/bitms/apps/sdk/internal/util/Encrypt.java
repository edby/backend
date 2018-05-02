/**
 * blocain.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.blocain.bitms.apps.sdk.internal.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.blocain.bitms.apps.sdk.ApiException;

/**
 *  加密工具
 * 
 * @author playguy
 * @version $Id: Encrypt.java, v 0.1 2016-3-28 下午5:14:12 playguy Exp $
 */
public class Encrypt
{
    private static final String   AES_ALG         = "AES";
    
    private static final String   HMAC_SHA1       = "HmacSHA1";
    
    private static final String   AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";
    
    private final static String[] HEX_DIGITS      = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    
    /**
     *   加密
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @return {@link String}
     * @throws ApiException
     */
    public static String encryptContent(String content, String encryptType, String encryptKey) throws ApiException
    {
        if (AES_ALG.equals(encryptType))
        {
            String encryptContent;
            try
            {
                encryptContent = aesEncrypt(content, encryptKey);
            }
            catch (Exception e)
            {
                throw new ApiException(e.getLocalizedMessage());
            }
            return encryptContent;
        }
        else
        {
            throw new ApiException("当前不支持该算法类型：encrypeType=" + encryptType);
        }
    }
    
    /**
     *  解密
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @return {@link String}
     * @throws ApiException
     */
    public static String decryptContent(String content, String encryptType, String encryptKey) throws ApiException
    {
        if (AES_ALG.equals(encryptType))
        {
            String decryptContent;
            try
            {
                decryptContent = aesDecrypt(content, encryptKey);
            }
            catch (Exception e)
            {
                throw new ApiException(e.getLocalizedMessage());
            }
            return decryptContent;
        }
        else
        {
            throw new ApiException("当前不支持该算法类型：encrypeType=" + encryptType);
        }
    }
    
    /**
     * 使用HmacSHA1进行加密
     *
     * @param data 需加密数据
     * @param key  加密Key
     * @return
     */
    public static String hmacSha1(final String data, final String key) throws Exception
    {
        try
        {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            return byteArrayToHexString(mac.doFinal(data.getBytes()));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e)
        {
            throw new Exception(e.getLocalizedMessage());
        }
    }
    
    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception
    {
        SecretKeySpec skeySpec = getKey(encryptKey);
        Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
        return Base64.encodeStr(encrypted);
    }
    
    /**
     * 将base 64 code AES解密
     * @param content 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String content, String decryptKey) throws Exception
    {
        SecretKeySpec skeySpec = getKey(decryptKey);
        Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.decode(content));
        String originalString = new String(original);
        return originalString;
    }
    
    private static SecretKeySpec getKey(String strKey)
    {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++)
        {
            arrB[i] = arrBTmp[i];
        }
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, AES_ALG);
        return skeySpec;
    }
    
    private static String byteArrayToHexString(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        for (byte aB : b)
        {
            sb.append(byteToHexString(aB));
        }
        return sb.toString();
    }
    
    private static String byteToHexString(byte b)
    {
        return HEX_DIGITS[(b & 0xf0) >> 4] + HEX_DIGITS[b & 0x0f];
    }
}
