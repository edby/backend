/*
 * @(#)AES256.java 2017年7月20日 上午10:29:14
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import net.iharder.Base64;

/**
 * <p>File：AES256.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月20日 上午10:29:14</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class AES256
{
    /**
     * 加密
     * @param content   内容
     * @param password  密钥
     * @return
     * @author 施建波  2017年7月20日 上午10:36:44
     */
    public static String encrypt(String content, String password) {  
        try {  
            //"AES"：请求的密钥算法的标准名称  
            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
            
            SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG","SUN");;
            
            //256：密钥生成参数；securerandom：密钥生成器的随机源  
            //SecureRandom securerandom = new SecureRandom(tohash256Deal(password));  
            securerandom.setSeed(tohash256Deal(password));
            kgen.init(256, securerandom);  
            //生成秘密（对称）密钥  
            SecretKey secretKey = kgen.generateKey();  
            //返回基本编码格式的密钥  
            byte[] enCodeFormat = secretKey.getEncoded();  
            //根据给定的字节数组构造一个密钥。enCodeFormat：密钥内容；"AES"：与给定的密钥内容相关联的密钥算法的名称  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
            //将提供程序添加到下一个可用位置  
            Security.addProvider(new BouncyCastleProvider());
            //创建一个实现指定转换的 Cipher对象，该转换由指定的提供程序提供。  
            //"AES/ECB/PKCS7Padding"：转换的名称；"BC"：提供程序的名称  
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");  
  
            cipher.init(Cipher.ENCRYPT_MODE, key);  
            byte[] byteContent = content.getBytes("utf-8");  
            byte[] cryptograph = cipher.doFinal(byteContent);  
            return bytesToHex(Base64.encodeBytesToBytes(cryptograph));
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /**
     * 解密
     * @param encryptResult   密文
     * @param password        密钥
     * @return
     * @author 施建波  2017年7月20日 上午10:37:07
     */
    public static String decrypt(String encryptResult, String password) {  
        try {  
            byte[] cryptograph = hexToBytes(encryptResult);
            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
            SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG","SUN");;
            securerandom.setSeed(tohash256Deal(password));
            //SecureRandom securerandom = new SecureRandom(tohash256Deal(password));  
            kgen.init(256, securerandom);  
            SecretKey secretKey = kgen.generateKey();  
            byte[] enCodeFormat = secretKey.getEncoded();  
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
            Security.addProvider(new BouncyCastleProvider());  
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");  
  
            cipher.init(Cipher.DECRYPT_MODE, key);  
            byte[] content = cipher.doFinal(Base64.decode(cryptograph));  
            return new String(content);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    public static String bytesToHex(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
            String hex = Integer.toHexString(buf[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            sb.append(hex.toUpperCase());  
        }  
        return sb.toString();  
    }  
    
    public static byte[] hexToBytes(String hex){
        int length = hex.length() / 2;
        byte[] bytes=new byte[length];
        for(int i=0;i<length;i++){
            String tempStr=hex.substring(2*i, 2*i+2);
            bytes[i]=(byte) Integer.parseInt(tempStr, 16);
        }
        return bytes;
    }
      
    private static byte[] tohash256Deal(String datastr) {  
        try {  
            MessageDigest digester=MessageDigest.getInstance("SHA-256");  
            digester.update(datastr.getBytes());  
            byte[] hex=digester.digest();  
            return hex;   
        } catch (NoSuchAlgorithmException e) {  
            throw new RuntimeException(e.getMessage());    
        }  
    }  
      
    public static void main(String[] args) {  
  
        String content = "0f607264fc6318a92b9e13c65db7cd3c";  
        String password = "8048482424A4AC6C6A6A6AE6EEEEE111999959555DDDD333BBBBB7777FFss222";  
        System.out.println("明文：" + content);  
        System.out.println("key：" + password);  
          
        String encryptResult = AES256.encrypt(content, password);  
        System.out.println("密文：" + encryptResult);  
        password = "8048482424A4AC6C6A6A6AE6EEEEE111999959555DDDD333BBBBB7777FFss223";  
        String decryptResult = AES256.decrypt(encryptResult, password);  
        System.out.println("解密：" + decryptResult);  
    }  
}
