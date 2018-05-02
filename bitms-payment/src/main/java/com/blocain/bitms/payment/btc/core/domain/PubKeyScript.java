package com.blocain.bitms.payment.btc.core.domain;

import java.util.List;

import com.blocain.bitms.payment.btc.core.domain.enums.ScriptTypes;
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
public class PubKeyScript extends SignatureScript
{
    private Integer      reqSigs;
    
    private ScriptTypes  type;
    
    private List<String> addresses;
}