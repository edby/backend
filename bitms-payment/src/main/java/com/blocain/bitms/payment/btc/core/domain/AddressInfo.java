package com.blocain.bitms.payment.btc.core.domain;

import java.util.List;

import com.blocain.bitms.payment.btc.core.domain.enums.ScriptTypes;
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
public class AddressInfo extends Entity
{
    @JsonProperty("isvalid")
    private Boolean      isValid;
    
    private String       address;
    
    @JsonProperty("ismine")
    private Boolean      isMine;
    
    @JsonProperty("iswatchonly")
    private Boolean      isWatchOnly;
    
    @JsonProperty("isscript")
    private Boolean      isScript;
    
    private ScriptTypes  script;
    
    private String       hex;
    
    private List<String> addresses;
    
    @JsonProperty("sigrequired")
    private Integer      sigRequired;
    
    @JsonProperty("pubkey")
    private String       pubKey;
    
    @JsonProperty("iscompressed")
    private Boolean      isCompressed;
    
    private String       account;
}