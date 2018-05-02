package com.blocain.bitms.payment.btc.client;

import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;

import com.blocain.bitms.payment.btc.core.client.BtcdClient;
import com.blocain.bitms.payment.btc.core.util.ResourceUtils;

/**A list of examples demonstrating the use of <i>bitcoind</i>'s mining RPCs (via the JSON-RPC 
 * API).*/
public class MiningApi
{
    public static void main(String[] args) throws Exception
    {
        CloseableHttpClient httpProvider = ResourceUtils.getHttpProvider();
        Properties nodeConfig = ResourceUtils.getNodeConfig();
        BtcdClient client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);
        // client.walletPassphrase("Bcbc963852", 30);
        // client.dumpWallet("/root/wallet.txt");
        client.getTransaction("");
        client.getTransaction("",false);
        client.validateAddress("");
        // client.listAccounts();
        // client.listReceivedByAccount();
        // client.listReceivedByAddress();
        // client.listTransactions();
        // client.encryptWallet("Bcbc963852");
        // client.getNewAddress();
        // client.listAddressGroupings();
        // client.getBlock("00000000211c64e61540c4b1a18128b8df9a4c99de3bbb2e5523138f24bb24b2",true);
    }
}