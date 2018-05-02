package com.blocain.bitms.apps.sdk.internal.util;

import java.security.*;

import com.blocain.bitms.apps.sdk.BitmsConstants;

/**
 * PKCS8KEY 密钥生成器
 * <p>Title: PKCS8KEY</p>
 * <p>File：PKCS8KEY.java</p>
 * <p>Description: PKCS8KEY</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class PKCS8KEY
{
    /**
     * 生成公私密钥
     * <p>
     *     String[0] 公钥
     *     String[1] 私钥
     * </p>
     * @param keytype
     * @return {@link String}
     * @throws Exception
     */
    public static String[] generateKey(KeyType keytype) throws Exception
    {
        KeyPairGenerator keyPairGenerator;
        try
        {
            keyPairGenerator = KeyPairGenerator.getInstance(keytype.getName());
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("不支持的算法名称" + keytype.getName());
        }
        keyPairGenerator.initialize(keytype.getLength(), new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String[] result = new String[2];
        result[0] = Base64.encodeStr(publicKey.getEncoded());
        result[1] = Base64.encodeStr(privateKey.getEncoded());
        return result;
    }
    
    /**
     * 校验密钥
     * @param keytype
     * @param publicKeyData
     * @param privateKeyData
     * @return
     * @throws Exception
     */
    public static boolean checkKey(String keytype, String publicKeyData, String privateKeyData) throws Exception
    {
        String text = "abcdefghijklmknopq";
        String sign;
        try
        {
            sign = Signature.rsaSign(text, privateKeyData, BitmsConstants.CHARSET_UTF8, keytype);
        }
        catch (Exception e)
        {
            throw new Exception("私钥内容不正确：" + e.getMessage());
        }
        try
        {
            return Signature.rsaCheck(text, sign, publicKeyData, BitmsConstants.CHARSET_UTF8, keytype);
        }
        catch (Exception e)
        {
            throw new Exception("公钥内容不正确：" + e.getMessage());
        }
    }
}
