package com.blocain.bitms.payment.btc.core.domain;

import java.math.BigDecimal;

import com.blocain.bitms.payment.btc.core.common.Defaults;
import com.blocain.bitms.payment.btc.core.domain.enums.PaymentCategories;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOverview extends Entity
{
    @JsonProperty("involvesWatchonly")
    public Boolean            involvesWatchOnly;
    
    private String            account;
    
    private String            address;
    
    private PaymentCategories category;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal        amount;
    
    @JsonProperty("vout")
    private Integer           vOut;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal        fee;
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
    
    public void setFee(BigDecimal fee)
    {
        this.fee = fee.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}