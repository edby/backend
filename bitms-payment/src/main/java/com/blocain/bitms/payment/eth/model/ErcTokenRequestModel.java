package com.blocain.bitms.payment.eth.model;

import java.util.Arrays;

/**
 * Created by admin on 2018/2/26.
 */
public class ErcTokenRequestModel
{
    private String   jsonrpc = "2.0";
    
    private String   method;
    
    private Object[] params;
    
    private Long     id;
    
    public String getJsonrpc()
    {
        return jsonrpc;
    }
    
    public void setJsonrpc(String jsonrpc)
    {
        this.jsonrpc = jsonrpc;
    }
    
    public String getMethod()
    {
        return method;
    }
    
    public void setMethod(String method)
    {
        this.method = method;
    }
    
    public Object[] getParams()
    {
        return params;
    }
    
    public void setParams(Object[] params)
    {
        this.params = params;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ErcTokenRequestModel{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", method='" + method + '\'' +
                ", params=" + Arrays.toString(params) +
                ", id=" + id +
                '}';
    }
}
