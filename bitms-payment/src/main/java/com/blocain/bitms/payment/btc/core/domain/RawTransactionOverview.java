package com.blocain.bitms.payment.btc.core.domain;

import java.util.List;

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
public class RawTransactionOverview extends Entity
{
    @JsonProperty("txid")
    private String          txId;
    
    private Integer         version;
    
    @JsonProperty("locktime")
    private Long            lockTime;
    
    @JsonProperty("vin")
    private List<RawInput>  vIn;
    
    @JsonProperty("vout")
    private List<RawOutput> vOut;
}