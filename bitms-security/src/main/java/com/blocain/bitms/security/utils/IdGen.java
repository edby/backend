package com.blocain.bitms.security.utils;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * <p>File：IdGen.java </p>
 * <p>Title: 封装各种生成唯一性ID算法的工具类. </p>
 * <p>Description: IdGen </p>
 * <p>Copyright: Copyright (c) 2014 08/08/2015 15:27</p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public class IdGen implements SessionIdGenerator
{
    private static SecureRandom random = new SecureRandom();
    
    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid()
    {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "").toLowerCase();
    }
    
    /**
     * 使用SecureRandom随机生成Long. 
     */
    public static long randomLong()
    {
        return Math.abs(random.nextLong());
    }
    
    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length)
    {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return EncryptUtils.desEncrypt(randomBytes.toString());
    }
    
    @Override
    public Serializable generateId(Session session)
    {
        return IdGen.uuid();
    }
}