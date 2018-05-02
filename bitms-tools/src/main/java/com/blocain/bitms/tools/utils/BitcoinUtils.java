package com.blocain.bitms.tools.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * <p>File：BitcoinUtils.java</p>
 * <p>Title: BitcoinUtils </p>
 * <p>Description:BTC钱包地址校验工具类 </p>
 * <p>Copyright: Copyright (c) 2017/8/16</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BitcoinUtils
{
    private final static String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    
    /**
     * BASE58 解码
     * @param input
     * @param base
     * @param len
     * @return
     */
    private static byte[] DecodeBase58(String input, int base, int len)
    {
        byte[] output = new byte[len];
        for (int i = 0; i < input.length(); i++)
        {
            char t = input.charAt(i);
            int p = ALPHABET.indexOf(t);
            if (p == -1) return null;
            for (int j = len - 1; j >= 0; j--, p /= 256)
            {
                p += base * (output[j] & 0xFF);
                output[j] = (byte) (p % 256);
            }
            if (p != 0) return null;
        }
        return output;
    }
    
    /**
     * Sha256 编码
     * @param data
     * @param start
     * @param len
     * @param recursion
     * @return
     */
    static byte[] Sha256(byte[] data, int start, int len, int recursion)
    {
        if (recursion == 0) return data;
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Arrays.copyOfRange(data, start, start + len));
            return Sha256(md.digest(), 0, 32, recursion - 1);
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }
    }
    
    /**
     * 验证地址有郊性
     * @param addr
     * @return
     */
    public static boolean ValidateBitcoinAddress(String addr)
    {
        if (addr.length() < 26 || addr.length() > 35) return false;
        byte[] decoded = DecodeBase58(addr, 58, 25);
        if (decoded == null) return false;
        byte[] hash = Sha256(decoded, 0, 21, 2);
        String firstStr = addr.substring(0, 1);
        boolean flag = Arrays.equals(Arrays.copyOfRange(hash, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
        boolean bitFlag = firstStr.equalsIgnoreCase("1") || firstStr.equalsIgnoreCase("3");
        return flag && bitFlag;
    }
    
    public static void main(String[] args)
    {
        System.out.println(ValidateBitcoinAddress("LRmshFgpLBoKwVsw5WNorzyLmB6MkL6caW"));
    }
}
