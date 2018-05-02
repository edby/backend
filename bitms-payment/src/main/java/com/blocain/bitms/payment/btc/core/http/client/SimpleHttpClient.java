package com.blocain.bitms.payment.btc.core.http.client;

import com.blocain.bitms.payment.btc.core.http.HttpLayerException;

public interface SimpleHttpClient
{
    String execute(String reqMethod, String reqPayload) throws HttpLayerException;
    
    void close();
}