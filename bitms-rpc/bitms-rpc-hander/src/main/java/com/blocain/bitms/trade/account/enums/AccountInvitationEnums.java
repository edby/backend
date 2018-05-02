package com.blocain.bitms.trade.account.enums;

import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.enums.CommonEnums;

/**
 * 帐号邀请奖励发放 
 * <p>File：AccountInvitationEnums.java</p>
 * <p>Title: AccountInvitationEnums</p>
 * <p>Description:AccountInvitationEnums</p>
 * <p>Copyright: Copyright (c) 2017年7月20日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public enum AccountInvitationEnums implements EnumDescribable
{
//    ACCOUNT_WALLET_ASSET_NOTEXITS(30001, "account.wallet.asset.notexist"),// 钱包账户不存在
//    ACCOUNT_WALLET_INVITATION_NOTEXITS(30002, "account.wallet.invitation.notexist"), // 被邀请人钱包帐号不存在
//    ACCOUNT_WALLET_INVITATION_EQ0(30003, "account.wallet.invitation.eq0"),// 验证码错误
//    ACCOUNT_WALLET_UNID_NOTEXITS(30004, "account.wallet.unid.notexist")// 邀请人账户不存在
    ;
    public Integer code;
    
    public String  message;
    
    private AccountInvitationEnums(Integer code, String message)
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
