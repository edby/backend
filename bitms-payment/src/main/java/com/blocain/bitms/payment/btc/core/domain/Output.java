package com.blocain.bitms.payment.btc.core.domain;

import java.math.BigDecimal;

import com.blocain.bitms.payment.btc.core.common.Defaults;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Output extends OutputOverview
{
    private String     address;
    
    private String     account;
    
    private String     scriptPubKey;
    
    private String     redeemScript;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal amount;
    
    private Integer    confirmations;
    
    private Boolean    spendable;
    
    public Output(String txId, Integer vOut, String scriptPubKey, String redeemScript)
    {
        super(txId, vOut);
        this.scriptPubKey = scriptPubKey;
        this.redeemScript = redeemScript;
    }
    
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}