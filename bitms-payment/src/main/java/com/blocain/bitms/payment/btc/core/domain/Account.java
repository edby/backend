package com.blocain.bitms.payment.btc.core.domain;

import java.math.BigDecimal;

import com.blocain.bitms.payment.btc.core.common.Defaults;
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
public class Account extends Entity
{
    @JsonProperty("involvesWatchonly")
    private Boolean    involvesWatchOnly;
    
    private String     account;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal amount;
    
    private Integer    confirmations;
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}