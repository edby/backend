package com.blocain.bitms.payment.btc.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**An enumeration specifying the <i>bitcoind</i> node properties currently recognized by 
 * btcd-cli4j.**/
@Getter
@ToString
@AllArgsConstructor
public enum NodeProperties
{
    RPC_PROTOCOL("bitcoind.rpc.protocol", "http"),
    /** **/
    RPC_HOST("bitcoind.rpc.host", "127.0.0.1"),
    /** **/
    RPC_PORT("bitcoind.rpc.port", "8332"),
    /** **/
    RPC_USER("bitcoind.rpc.user", "user"),
    /** **/
    RPC_PASSWORD("bitcoind.rpc.password", ""),
    /** **/
    HTTP_AUTH_SCHEME("bitcoind.http.auth_scheme", "Basic"),
    /** **/
    ALERT_PORT("bitcoind.notification.alert.port", "5158"),
    /** **/
    BLOCK_PORT("bitcoind.notification.block.port", "5159"),
    /** **/
    WALLET_PORT("bitcoind.notification.wallet.port", "5160");

    private final String key;
    
    private final String defaultValue;
}