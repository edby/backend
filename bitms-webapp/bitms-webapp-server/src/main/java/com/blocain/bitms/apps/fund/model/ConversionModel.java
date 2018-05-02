package com.blocain.bitms.apps.fund.model;

import com.blocain.bitms.apps.account.beans.AuthTokenRequest;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 互转model
 * Created by admin on 2018/3/21.
 */
public class ConversionModel extends AuthTokenRequest
{
    /** 转出账户 */
    @NotNull(message = "转出账户不能为空")
    private String     fromStockinfo;
    
    /** 转入账户 */
    @NotNull(message = "转入账户不能为空")
    private String     toStockinfo;
    
    /** 转入金额 */
    @NotNull(message = "转入金额不能为空")
    private BigDecimal amount;
    
    public String getFromStockinfo()
    {
        return fromStockinfo;
    }
    
    public void setFromStockinfo(String fromStockinfo)
    {
        this.fromStockinfo = fromStockinfo;
    }
    
    public String getToStockinfo()
    {
        return toStockinfo;
    }
    
    public void setToStockinfo(String toStockinfo)
    {
        this.toStockinfo = toStockinfo;
    }
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
