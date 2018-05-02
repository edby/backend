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
public class RawOutput extends Entity
{
    @Setter(AccessLevel.NONE)
    private BigDecimal   value;
    
    private Integer      n;
    
    private PubKeyScript scriptPubKey;
    
    public void setValue(BigDecimal value)
    {
        this.value = value.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}