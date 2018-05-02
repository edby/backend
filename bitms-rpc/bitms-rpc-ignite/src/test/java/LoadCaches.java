import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import com.blocain.bitms.tools.utils.PropertiesUtils;

public class LoadCaches
{
    public static final PropertiesUtils properties = new PropertiesUtils("cache.properties");
    
    /**
     * <p>
     * Utility to load caches from database.
     * <p>
     * How to use:
     * <ul>
     *     <li>Start cluster.</li>
     *     <li>Start this utility and wait while load complete.</li>
     * </ul>
     * 
     * @param args Command line arguments, none required.
     * @throws Exception If failed.
     **/
    public static void main(String[] args) throws Exception
    {
        loadCache();
    }
    
    /**
     * 初始化数据
     * @throws Exception
     */
    public static void loadCache() throws Exception
    {
        try (Ignite ignite = Ignition.start("client.xml"))
        {
            System.out.println(">>> Loading caches...");
            // 加密帐户资产信息
            System.out.println(">>> Loading cache: AccountFundCurrentCache");
            ignite.cache("AccountFundCurrentCache").loadCache(null);
            System.out.println(">>> Loading cache: AccountWalletAssetCache");
            ignite.cache("AccountWalletAssetCache").loadCache(null);
            // 加载btc2eur数据
            String content = properties.getProperty("cache.btc2eur.entrust");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            content = properties.getProperty("cache.btc2eur.realdeal");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            content = properties.getProperty("cache.btc2eur.realdealhis");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            // 加载biex2btc数据
            content = properties.getProperty("cache.biex2btc.entrust");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            content = properties.getProperty("cache.biex2btc.realdeal");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            content = properties.getProperty("cache.biex2btc.realdealhis");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            // 加载btc2usd数据
            content = properties.getProperty("cache.btc2usd.entrust");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            content = properties.getProperty("cache.btc2usd.realdeal");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            content = properties.getProperty("cache.btc2usd.realdealhis");
            System.out.println(">>> Loading cache: " + content);
            ignite.cache(content).loadCache(null);
            System.out.println(">>> All caches loaded!");
        }
    }
}