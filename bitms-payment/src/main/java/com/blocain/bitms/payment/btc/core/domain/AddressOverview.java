package com.blocain.bitms.payment.btc.core.domain;

import java.math.BigDecimal;

import com.blocain.bitms.payment.btc.core.common.Defaults;
import com.blocain.bitms.payment.btc.core.jsonrpc.deserialization.AddressOverviewDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = AddressOverviewDeserializer.class)
public class AddressOverview extends Entity
{
    private String     address;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal balance;
    
    private String     account;
    
    public void setBalance(BigDecimal balance)
    {
        this.balance = balance.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}