package com.blocain.bitms.payment.btc.core.domain;

import java.math.BigDecimal;

import com.blocain.bitms.payment.btc.core.common.Defaults;
import com.blocain.bitms.payment.btc.core.domain.enums.ChainTypes;
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
public class MiningInfo extends Entity
{
    private Integer    blocks;
    
    @JsonProperty("currentblocksize")
    private Integer    currentBlockSize;
    
    @JsonProperty("currentblocktx")
    private Integer    currentBlockTx;
    
    @Setter(AccessLevel.NONE)
    private BigDecimal difficulty;
    
    private String     errors;
    
    @JsonProperty("genproclimit")
    private Integer    genProcLimit;
    
    @JsonProperty("networkhashps")
    private Long       networkHashPS;
    
    @JsonProperty("pooledtx")
    private Integer    pooledTx;
    
    private Boolean    testnet;
    
    private ChainTypes chain;
    
    private Boolean    generate;
    
    @JsonProperty("hashespersec")
    private Long       hashesPerSec;
    
    public void setDifficulty(BigDecimal difficulty)
    {
        this.difficulty = difficulty.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}