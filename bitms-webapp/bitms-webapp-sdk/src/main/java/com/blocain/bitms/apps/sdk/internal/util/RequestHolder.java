package com.blocain.bitms.apps.sdk.internal.util;

/**
 * 请求参数头文件
 */
public class RequestHolder
{
    /**
     * 公共必填参数
     */
    private BitmsMap protocalMustParams;
    
    /**
     * 公共选填参数
     */
    private BitmsMap protocalOptParams;
    
    /**
     * 业务请求参数
     */
    private BitmsMap applicationParams;
    
    public BitmsMap getProtocalMustParams()
    {
        return protocalMustParams;
    }
    
    public void setProtocalMustParams(BitmsMap protocalMustParams)
    {
        this.protocalMustParams = protocalMustParams;
    }
    
    public BitmsMap getProtocalOptParams()
    {
        return protocalOptParams;
    }
    
    public void setProtocalOptParams(BitmsMap protocalOptParams)
    {
        this.protocalOptParams = protocalOptParams;
    }
    
    public BitmsMap getApplicationParams()
    {
        return applicationParams;
    }
    
    public void setApplicationParams(BitmsMap applicationParams)
    {
        this.applicationParams = applicationParams;
    }
}
