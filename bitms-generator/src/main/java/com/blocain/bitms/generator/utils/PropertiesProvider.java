package com.blocain.bitms.generator.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 读取属性文件实现类
 * <p>File：PropertiesProvider.java</p>
 * <p>Title: PropertiesProvider</p>
 * <p>Description:PropertiesProvider</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * 
 * @author Playguy
 * @version 1.0
 */
public class PropertiesProvider
{
    static Properties props;
    
    private PropertiesProvider()
    {
    }
    
    /**
     * 初始化资源文件
     */
    private static void initProperties()
    {
        try
        {
            props = loadAllProperties("generator.properties");
            String basepackage = props.getProperty("basepackage");
            String basepackage_dir = basepackage.replace('.', '/');
            props.put("basepackage_dir", basepackage_dir);
        }
        catch (IOException e)
        {
            throw new RuntimeException("[资源文件] 读取错误!", e);
        }
    }
    
    public static Properties getProperties()
    {
        if (props == null)
        {
            initProperties();
        }
        return props;
    }
    
    public static String getProperty(String key, String defaultValue)
    {
        return getProperties().getProperty(key, defaultValue);
    }
    
    /**
     * 根据KEY取数据
     * 
     * @param key
     * @return {@link String}
     */
    public static String getProperty(String key)
    {
        return getProperties().getProperty(key);
    }
    
    /**
     * 读取资源文件里的所有信息
     * 
     * @param resourceName
     * @return {@link Properties}
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static Properties loadAllProperties(String resourceName) throws IOException
    {
        Properties properties = new Properties();
        Enumeration urls = PropertiesProvider.class.getClassLoader().getResources(resourceName);
        while (urls.hasMoreElements())
        {
            URL url = (URL) urls.nextElement();
            InputStream is = null;
            try
            {
                URLConnection con = url.openConnection();
                con.setUseCaches(false);
                is = con.getInputStream();
                properties.load(is);
            }
            finally
            {
                if (is != null)
                {
                    is.close();
                }
            }
        }
        return properties;
    }
}
