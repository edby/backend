package com.blocain.bitms.apps.sdk;

import java.util.Map;

import com.blocain.bitms.apps.sdk.internal.util.BitmsMap;

/**
 * 基础的请求对象
 * 
 * @author auto create
 * @since 1.0, 2017-07-20 10:41:44
 */
public class BitmsRequest implements BasicRequest<BitmsResponse>
{
    private String      apiVersion  = "1.0";
    
    /**
     * add user-defined text parameters
     */
    private BitmsMap    udfParams;

    /**
     * 终端类型
     */
    private String      terminalType;
    
    /**
     * 终端信息
     */
    private String      terminalInfo;

    /**
     * 是否加密
     */
    private boolean     needEncrypt = true;
    
    /**
     * 业务对象
     */
    private BitmsObject bizModel    = null;
    
    /**
     * 请求内容
     */
    private String      content;
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getApiVersion()
    {
        return this.apiVersion;
    }
    
    public void setApiVersion(String apiVersion)
    {
        this.apiVersion = apiVersion;
    }
    
    public void setTerminalType(String terminalType)
    {
        this.terminalType = terminalType;
    }
    
    public String getTerminalType()
    {
        return this.terminalType;
    }
    
    public void setTerminalInfo(String terminalInfo)
    {
        this.terminalInfo = terminalInfo;
    }
    
    public String getTerminalInfo()
    {
        return this.terminalInfo;
    }


    public Map<String, String> getTextParams()
    {
        BitmsMap txtParams = new BitmsMap();
        txtParams.put("content", this.content);
        if (udfParams != null)
        {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }
    
    public void putOtherTextParam(String key, String value)
    {
        if (this.udfParams == null)
        {
            this.udfParams = new BitmsMap();
        }
        this.udfParams.put(key, value);
    }
    
    public Class<BitmsResponse> getResponseClass()
    {
        return BitmsResponse.class;
    }
    
    public boolean isNeedEncrypt()
    {
        return this.needEncrypt;
    }
    
    public void setNeedEncrypt(boolean needEncrypt)
    {
        this.needEncrypt = needEncrypt;
    }
    
    public BitmsObject getBizModel()
    {
        return this.bizModel;
    }
    
    public void setBizModel(BitmsObject bizModel)
    {
        this.bizModel = bizModel;
    }
}
