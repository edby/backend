package com.blocain.bitms.trade.account.enums;

import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.enums.CommonEnums;

/**
 * 安全验证策略枚举类型
 * <p>File：PolicyEnums.java </p>
 * <p>Title: PolicyEnums </p>
 * <p>Description:PolicyEnums </p>
 * <p>Copyright: Copyright (c) 2017/7/10 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public enum SecurityPolicyEnums implements EnumDescribable
{
    // 验证安全策略
    SECURITY_POLICY_DEFAULT(0, "Default security policy"), // 默认安全验证策略
    SECURITY_POLICY_NEEDGA(1, "Security policy [GA]"), // 安全验证策略启用GA
    SECURITY_POLICY_NEEDSMS(2, "Security policy [SMS]"), // 安全验证策略启用SMS
    SECURITY_POLICY_NEEDGAANDSMS(3, "Security.policy [GA+SMS]"), // 安全验证策略启用SMS和GA
    SECURITY_POLICY_NEEDGAORSMS(4, "Security.policy [GA/SMS]"), // 安全验证策略启用SMS或GA
    ;
    public Integer code;
    
    public String  message;
    
    SecurityPolicyEnums(Integer code, String message)
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
