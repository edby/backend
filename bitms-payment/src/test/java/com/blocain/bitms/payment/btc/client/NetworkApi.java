package com.blocain.bitms.payment.btc.client;

import java.util.Properties;

import com.blocain.bitms.payment.btc.core.util.ResourceUtils;
import org.apache.http.impl.client.CloseableHttpClient;

import com.blocain.bitms.payment.btc.core.client.BtcdClient;

/**A list of examples demonstrating the use of <i>bitcoind</i>'s network RPCs (via the JSON-RPC 
 * API).*/
public class NetworkApi
{
    public static void main(String[] args) throws Exception
    {
        CloseableHttpClient httpProvider = ResourceUtils.getHttpProvider();
        Properties nodeConfig = ResourceUtils.getNodeConfig();
        BtcdClient client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);
        client.getPeerInfo();
        client.ping();
    }
}