package com.blocain.bitms.tools.bean;

/**
 * <p>File：ClientParameter.java</p>
 * <p>Title: API开发平台接口参数对象</p>
 * <p>Description:提供内部支撑及相关系统对接参数的定义</p>
 * <p>Copyright: Copyright (c) 2017 </p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ClientParameter
{
    public static final String USER_DES = "userDes";
    
    public static final String USER_KEY = "userKey";
    
    public static final String DATA_LEN = "dataLen";
    
    public static final String DATA     = "data";
    
    public static final String PIC_SIZE = "picSize";
    
    // 构造器
    public ClientParameter()
    {
        super();
    }
    
    // MD5或者异或检验码
    private String  userDes;
    
    // 第三方key
    private String  userKey;
    
    // 参数长度
    private Integer dataLen;
    
    // 参数内容
    private String  data;
    
    private String  picSize;
    
    public String getUserDes()
    {
        return userDes;
    }
    
    public void setUserDes(String userDes)
    {
        this.userDes = userDes;
    }
    
    public String getUserKey()
    {
        return userKey;
    }
    
    public void setUserKey(String userKey)
    {
        this.userKey = userKey;
    }
    
    public Integer getDataLen()
    {
        return dataLen;
    }
    
    public void setDataLen(Integer dataLen)
    {
        this.dataLen = dataLen;
    }
    
    public String getData()
    {
        return data;
    }
    
    public void setData(String data)
    {
        this.data = data;
    }
    
    public String getPicSize()
    {
        return picSize;
    }
    
    public void setPicSize(String picSize)
    {
        this.picSize = picSize;
    }
}
