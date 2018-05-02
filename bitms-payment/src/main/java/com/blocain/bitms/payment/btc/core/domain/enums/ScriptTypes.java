package com.blocain.bitms.payment.btc.core.domain.enums;

import com.blocain.bitms.payment.btc.core.common.Errors;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ScriptTypes
{
    PUB_KEY("pubkey"),
    /** **/
    PUB_KEY_HASH("pubkeyhash"),
    /** **/
    SCRIPT_HASH("scripthash"),
    /** **/
    MULTISIG("multisig"),
    /** **/
    NULL_DATA("nulldata"),
    /** **/
    NONSTANDARD("nonstandard");
    private final String name;
    
    @JsonValue
    public String getName()
    {
        return name;
    }
    
    @JsonCreator
    public static ScriptTypes forName(String name)
    {
        if (name != null)
        {
            for (ScriptTypes scriptType : ScriptTypes.values())
            {
                if (name.equals(scriptType.getName())) { return scriptType; }
            }
        }
        throw new IllegalArgumentException(Errors.ARGS_BTCD_SCRIPTTYPE_UNSUPPORTED.getDescription());
    }
}