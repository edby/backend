package com.blocain.bitms.orm.config;

import com.blocain.bitms.orm.utils.EncryptUtils;
import redis.clients.jedis.JedisShardInfo;

import java.net.URI;

/**
 * 自定义Redis连接
 * <p>File： CustomJedisShardInfo.java </p>
 * <p>Title:  CustomJedisShardInfo </p>
 * <p>Description: CustomJedisShardInfo </p>
 * <p>Copyright: Copyright (c) 2017/8/2 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CustomJedisShardInfo extends JedisShardInfo
{
    public CustomJedisShardInfo(String host)
    {
        super(host);
    }
    
    public CustomJedisShardInfo(String host, String name)
    {
        super(host, name);
    }
    
    public CustomJedisShardInfo(String host, int port)
    {
        super(host, port);
    }
    
    public CustomJedisShardInfo(String host, int port, String name)
    {
        super(host, port, name);
    }
    
    public CustomJedisShardInfo(String host, int port, int timeout)
    {
        super(host, port, timeout);
    }
    
    public CustomJedisShardInfo(String host, int port, int timeout, String name)
    {
        super(host, port, timeout, name);
    }
    
    public CustomJedisShardInfo(String host, int port, int connectionTimeout, int soTimeout, int weight)
    {
        super(host, port, connectionTimeout, soTimeout, weight);
    }
    
    public CustomJedisShardInfo(String host, String name, int port, int timeout, int weight)
    {
        super(host, name, port, timeout, weight);
    }
    
    public CustomJedisShardInfo(URI uri)
    {
        super(uri);
    }
    
    @Override
    public void setPassword(String auth)
    {
        super.setPassword(EncryptUtils.desDecrypt(auth));
    }
}
