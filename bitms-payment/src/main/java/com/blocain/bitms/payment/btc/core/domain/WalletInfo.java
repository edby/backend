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
public class WalletInfo extends Entity
{
    @JsonProperty("walletversion")
    private Integer    walletVersion;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal balance;
    
    @JsonProperty("txcount")
    private Integer    txCount;
    
    @JsonProperty("keypoololdest")
    private Long       keypoolOldest;
    
    @JsonProperty("keypoolsize")
    private Integer    keypoolSize;
    
    @JsonProperty("unlocked_until")
    private Long       unlockedUntil;
    
    public void setBalance(BigDecimal balance)
    {
        this.balance = balance.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}