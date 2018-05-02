package com.blocain.bitms.trade.trade.enums;

import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.enums.CommonEnums;

/**
 * 
 * <p>File：EntrustEnums.java</p>
 * <p>Title: EntrustEnums</p>
 * <p>Description:EntrustEnums</p>
 * <p>Copyright: Copyright (c) 2017年8月13日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public enum EntrustEnums implements EnumDescribable
{
    ENTRUST_LE_ENABLE_ENTRUSTAMT(2001, "Available trading volume is insufficient"), // 可成交數量不足
//    ERROR_ENTRUSTAMT_MUST_BE_INTEGER(2002, "spot.entrusamt.must.be.integer"), // 委托数量必须是整数
    ERROR_DEALAMT_MUST_BE_INTEGER(2003, "Deal amount must be integer"),// 成交数量必须是整数
    ;
    public Integer code;
    
    public String  message;
    
    private EntrustEnums(Integer code, String message)
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
