package com.blocain.bitms.trade.fund.enums;

import com.blocain.bitms.tools.bean.EnumDescribable;
import com.blocain.bitms.tools.enums.CommonEnums;

/**
 * 证券信息枚举 介绍
 * <p>File：StockInfoEnums.java </p>
 * <p>Title: StockInfoEnums </p>
 * <p>Description:StockInfoEnums </p>
 * <p>Copyright: Copyright (c) 2017/7/10 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public enum FundEnums implements EnumDescribable
{
    ERROR_STOCKINFOID_NOT_EXIST(2010, "Security information ID cannot be empty"), // 证券信息ID不能为空
    ERROR_ASSETTYPE_NOT_EXIST(2011, "The account type cannot be empty"), // 账户类型不能为空
    ERROR_BUSINESSFLAG_NOT_EXIST(2012, "The business type cannot be empty"), // 业务类别不能为空
    ERROR_WITHDRAW_ADDRESS_NOT_EXIST(2013, "Withdrawal address cannot be empty"), // 提现地址不能为空
    ERROR_WITHDRAW_ADDRESS_DOESNOTTAKE(2020, "Withdrawal address is invalid"), // 提现地址未生效
    ERROR_WITHDRAW_AMOUNT_GREATER_ZEOR(2014, "Withdrawal amount must more than zero"), // 提现金额必须大于0
    ERROR_WITHDRAW_FEE_NOT_EXIST(2015, "Fee cannot be empty"), // 提现手续费不能为空
    ERROR_WITHDRAW_FAIL(2016, "Withdraw failed"), // 提现失败
    ERROR_CHARGE_AMOUNT_GREATER_ZEOR(2017, "Transfer amount must more than zero"), // 转帐金额必须大于0
    ERROR_BUSINESSFLAG_ERROR(2018, "Type error"), // 业务类别错误
    ERROR_CHARGE_FAIL(2019, "Transfer failed") // 转帐失败
    ;
    public Integer code;
    
    public String  message;
    
    private FundEnums(Integer code, String message)
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
