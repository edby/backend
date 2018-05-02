package com.blocain.bitms.tools.utils;

/**
 * <p>File：ERC20TokenCoinUtils.java</p>
 * <p>Title: ERC20TokenCoinUtils </p>
 * <p>Description: ERC20钱包地址校验工具类 </p>
 * <p>Copyright: Copyright (c) 2017/8/16</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ERC20TokenCoinUtils
{

    /**
     * 验证地址有郊性
     * @param addr
     * @return
     */
    public static boolean ValidateERC20TokenCoinAddress(String addr)
    {
        boolean result = false;
        if(StringUtils.isNotBlank(addr))
        {
            if(addr.startsWith("0x") && addr.length()==42)
            {
                String subAddr = addr.substring(2);
                String regex="^[A-Fa-f0-9]+$";
                if(subAddr.matches(regex)){
                    result = true;
                }
            }
        }
        return result;
    }
    
    public static void main(String[] args)
    {
        System.out.println(ValidateERC20TokenCoinAddress("0x86169ee5a4ef045f611c891d6f4315e27ba2a8c4"));
        System.out.println(ValidateERC20TokenCoinAddress("0w86169ee5a4ef045f611c891d6f4315e27ba2a8c4"));
        System.out.println(ValidateERC20TokenCoinAddress("0x86169eg5a4ef045f611c891d6f4315e27ba2a8c4"));
    }
}
