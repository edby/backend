package com.blocain.bitms.payment.eth.model;

import java.util.Arrays;

/**
 * Created by admin on 2018/2/26.
 */
public class ErcTokenResponceModel
{
    private String jsonrpc;
    
    private String result[];
    
    private Long   id;
    
    public String getJsonrpc()
    {
        return jsonrpc;
    }
    
    public void setJsonrpc(String jsonrpc)
    {
        this.jsonrpc = jsonrpc;
    }
    
    public String[] getResult()
    {
        return result;
    }
    
    public void setResult(String result[])
    {
        this.result = result;
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
        return "ErcTokenResponceModel{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", result=" + Arrays.toString(result) +
                ", id=" + id +
                '}';
    }
}
