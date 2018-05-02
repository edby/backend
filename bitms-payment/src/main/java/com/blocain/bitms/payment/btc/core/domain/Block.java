package com.blocain.bitms.payment.btc.core.domain;

import java.math.BigDecimal;
import java.util.List;

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
public class Block extends Entity
{
    private String       hash;
    
    private Integer      confirmations;
    
    private Integer      size;
    
    private Integer      height;
    
    private Integer      version;
    
    @JsonProperty("merkleroot")
    private String       merkleRoot;
    
    private List<String> tx;
    
    private Long         time;
    
    private Long         nonce;
    
    private String       bits;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal   difficulty;
    
    @JsonProperty("chainwork")
    private String       chainWork;
    
    @JsonProperty("previousblockhash")
    private String       previousBlockHash;
    
    @JsonProperty("nextblockhash")
    private String       nextBlockHash;
    
    public void setDifficulty(BigDecimal difficulty)
    {
        this.difficulty = difficulty.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}