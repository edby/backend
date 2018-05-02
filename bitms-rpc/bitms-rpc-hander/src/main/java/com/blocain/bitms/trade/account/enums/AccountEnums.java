package com.blocain.bitms.trade.account.enums;

import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.enums.CommonEnums;

/**
 * AccountEnums 介绍
 * <p>File：AccountEnums.java </p>
 * <p>Title: AccountEnums </p>
 * <p>Description:AccountEnums </p>
 * <p>Copyright: Copyright (c) 2017/7/10 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public enum AccountEnums implements EnumDescribable
{
    ACCOUNT_PHONE_TIPS(30000, "Please enter your phone number"), // 请输入手机号
    /*ACCOUNT_NAME_NOTEXITS(30001, "Username cannot be blank"), // 帐户名称不能为空*/
    ACCOUNT_PASSWORD_ERROR(30002, "Incorrect password"), // 密碼錯誤
    ACCOUNT_SMSCODE_ERROR(30003, "SMS code error"), // 手机验证码错误
    ACCOUNT_GACODE_ERROR(30004, "Google auth code error"), // 谷歌验证码错误
    ACCOUNT_PHONE_NOTBIND(30005, "Please enable SMS Verification first."), // 手机号未绑定
    ACCOUNT_PHONE_HAS_BIND(30006, "The phone number has bound"), // 手机号已绑定
    ACCOUNT_WALLET_ASSET_NOTEXITS(30007, "The wallet account does not exist"), // 钱包账户不存在
    ACCOUNT_WALLET_ASSET_INSUFFICIENT_BAL(30008, "The balance of the wallet account is insufficient")// 钱包账户余额不足 insufficient Balance
    ;
    public Integer code;
    
    public String  message;
    
    private AccountEnums(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }
    
    /**
     * 根据状态码获取状态码描述
     * @param code 状态码
     * @return String 状态码描述
     */
    public static String getMessage(Integer code)
    {
        String result = null;
        for (CommonEnums c : CommonEnums.values())
        {
            if (c.code.equals(code))
            {
                result = c.message;
                break;
            }
        }
        return result;
    }
    
    @Override
    public Integer getCode()
    {
        return this.code;
    }
    
    public void setCode(Integer code)
    {
        this.code = code;
    }
    
    @Override
    public String getMessage()
    {
        return this.message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
}
