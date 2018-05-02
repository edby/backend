package com.blocain.bitms.apps.sdk.internal.util;

/**
 * 密钥类型 Introduce
 * <p>Title: KeyType</p>
 * <p>File：KeyType.java</p>
 * <p>Description: KeyType</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class KeyType
{
    public KeyType(String name, int length)
    {
        this.name = name;
        this.length = length;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getLength()
    {
        return this.length;
    }
    
    public void setLength(int length)
    {
        this.length = length;
    }
    
    public static final KeyType MD5     = new KeyType("MD5", 32);
    
    public static final KeyType DSA1024 = new KeyType("DSA", 1024);
    
    public static final KeyType DSA2048 = new KeyType("DSA", 2048);
    
    public static final KeyType RSA1024 = new KeyType("RSA", 1024);
    
    public static final KeyType RSA2048 = new KeyType("RSA", 2048);
    
    private String              name;
    
    private int                 length;
}
