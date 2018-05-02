package com.blocain.bitms.platscan.config;

import com.blocain.bitms.tools.utils.PropertiesUtils;
import java.io.IOException;

public class PlatScanConfigLoader
{
    public static final String CONFIGPATH = "platscan.properties";
    /**
     * 装载指定目录的配置文件，支持两种形式
     * "/dir/config.properties"  ,"config.properties "
     * 当使用绝对路径时，会自动读取绝读路径的地址
     *
     * @param
     * @return
     */
    public static PlatScanConfig loadProp() throws IOException
    {
        PropertiesUtils properties = new PropertiesUtils(CONFIGPATH);
        PlatScanConfig config = new PlatScanConfig();
        // 扫描休眠默认时间
        PlatScanConfig.SLEEP_DEFAULT_TINE = Long.valueOf(properties.getProperty("sleep.default.time"));

        return config;
    }
}
