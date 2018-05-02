package com.blocain.bitms.tools.google;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.blocain.bitms.tools.utils.RedisUtils;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * GoogleAuthenticator 介绍
 * <p>File：GoogleAuthenticator.java </p>
 * <p>Title: GoogleAuthenticator </p>
 * <p>Description:GoogleAuthenticator </p>
 * <p>Copyright: Copyright (c) 2017/7/14 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class Authenticator
{
    // 默认 3 - 最大值17 (from google docs)多可偏移的时间--3*30秒的验证时间（手机客户端验证为30秒变化次）
    public static int          window_size             = 1;
    
    // 来自谷歌文档，不用修该
    public static final int    SECRET_SIZE             = 10;
    
    // 安全哈希算法（Secure Hash Algorithm）
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    
    public static final String SEED                    = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";
    
    /**
     * 设置偏移量，最大值17
     * 默认 3 - 最大值17 (from google docs)多可偏移的时间--3*30秒的验证时间（手机客户端验证为30秒变化次）
     * @param s
     */
    public void setWindowSize(int s)
    {
        if (s >= 1 && s <= 17) window_size = s;
    }
    
    /**
     *  生成秘钥
     * 随机生成1个秘钥，这个秘钥必须在服务器上保存，用户在手机Google身份验证器上配置账号时也要这个秘钥
     * @return secret key
     */
    public static String generateSecretKey()
    {
        SecureRandom sr;
        try
        {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            byte[] seedBytes = SEED.getBytes();
            sr.setSeed(Base64.decodeBase64(seedBytes));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String encodedKey = new String(bEncodedKey);
            return encodedKey;
        }
        catch (NoSuchAlgorithmException e)
        {
            // should never occur... configuration error
        }
        return null;
    }
    
    /**
     * 查用户输入的6位码是否有效
     * @param secret 秘钥
     * @param code 6位码
     * @return
     */
    public boolean checkCode(String secret, Long code)
    {
        if (StringUtils.isBlank(secret) || code == null) return false;
        StringBuffer buffer = new StringBuffer(CacheConst.GOOGLE_CODE_PERFIX).append(BitmsConst.SEPARATOR).append(code);
        if(StringUtils.isNotBlank(RedisUtils.get(buffer.toString()))) return false;
        boolean flag = checkCode(secret, code, CalendarUtils.getCurrentLong());
        if (flag)
        {// GA验证码验证成功之后加入缓存
            RedisUtils.putObject(buffer.toString(),String.valueOf(CalendarUtils.getCurrentLong()), CacheConst.DEFAULT_CACHE_TIME);
        }
        return flag;
    }
    
    /**
     * 查用户输入的6位码是否有效
     * @param secret 秘钥
     * @param code 6位码
     * @param timeMsec 偏移时间
     * @return
     */
    public boolean checkCode(String secret, long code, long timeMsec)
    {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = (timeMsec / 1000L) / 30L;
        for (int i = -window_size; i <= window_size; ++i)
        {
            long hash;
            try
            {
                hash = generateCode(decodedKey, t + i);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e.getMessage());
            }
            if (hash == code) { return true; }
        }
        return false;
    }
    
    /**
     * 生成验证码
     * @param key
     * @param t
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private static int generateCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException
    {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8)
        {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i)
        {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }
}
