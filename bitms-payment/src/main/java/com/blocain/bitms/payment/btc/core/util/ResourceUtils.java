package com.blocain.bitms.payment.btc.core.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.blocain.bitms.payment.btc.core.BitcoindException;
import com.blocain.bitms.payment.btc.core.CommunicationException;
import com.blocain.bitms.payment.btc.core.client.BtcdClient;
import com.blocain.bitms.payment.btc.core.client.BtcdClientImpl;
import com.blocain.bitms.tools.utils.PropertiesUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceUtils
{
    public static CloseableHttpClient getHttpProvider()
    {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(connManager).build();
        return httpProvider;
    }
    
    public static BtcdClient getBtcdProvider() throws BitcoindException, CommunicationException, IOException
    {
        return new BtcdClientImpl(getHttpProvider(), getNodeConfig());
    }
    
    public static Properties getNodeConfig() throws IOException
    {
        PropertiesUtils properties = new PropertiesUtils("bitcoind.properties");
        return properties.getProperties();
    }
}