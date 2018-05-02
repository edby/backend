package com.blocain.bitms.trade.robot.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil
{
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    
    /**
     * 获取配置文件.
     */
    public static Properties getProperties(String path)
    {
        Properties prop = new Properties();
        try
        {
            InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(path);
            prop.load(in);
            in.close();
        }
        catch (Exception e)
        {
            logger.info("该配置文件读取异常：" + path);
        }
        return prop;
    }
}
