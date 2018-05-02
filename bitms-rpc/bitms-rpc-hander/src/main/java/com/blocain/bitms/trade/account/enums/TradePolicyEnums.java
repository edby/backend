package com.blocain.bitms.trade.account.enums;

import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.enums.CommonEnums;

/**
 * 交易验证策略枚举类型
 * <p>File：PolicyEnums.java </p>
 * <p>Title: PolicyEnums </p>
 * <p>Description:PolicyEnums </p>
 * <p>Copyright: Copyright (c) 2017/7/10 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public enum TradePolicyEnums implements EnumDescribable
{
    TRADE_POLICY_DEFAULT(0, "Default trade policy "), // 默认交易验证策略
    TRADE_POLICY_TWOHOUR(1, "Trade policy [2h]"), // 交易验证策略两小时验证一次
    TRADE_POLICY_EVERYTIME(2, "Trade policy [Everytime]"), // 交易验证策略每次都验证
    ;
    public Integer code;
    
    public String  message;
    
    TradePolicyEnums(Integer code, String message)
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
