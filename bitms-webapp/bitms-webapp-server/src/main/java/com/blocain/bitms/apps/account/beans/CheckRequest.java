package com.blocain.bitms.apps.account.beans;

/**
 * 二次校验参数对象 Introduce
 * <p>Title: CheckRequest</p>
 * <p>File：CheckRequest.java</p>
 * <p>Description: CheckRequest</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CheckRequest extends AccessRequest
{
    private static final long serialVersionUID = -8940431397041474885L;
    
    // 随机码
    private String            code;
    
    // 密码
    private String            password;
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
}
