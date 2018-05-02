package com.blocain.bitms.payment.btc.core.jsonrpc.client;

import java.util.List;

import com.blocain.bitms.payment.btc.core.BitcoindException;
import com.blocain.bitms.payment.btc.core.CommunicationException;
import com.blocain.bitms.payment.btc.core.jsonrpc.JsonMapper;
import com.blocain.bitms.payment.btc.core.jsonrpc.JsonPrimitiveParser;

public interface JsonRpcClient
{
    String execute(String method) throws BitcoindException, CommunicationException;
    
    <T> String execute(String method, T param) throws BitcoindException, CommunicationException;
    
    <T> String execute(String method, List<T> params) throws BitcoindException, CommunicationException;
    
    JsonPrimitiveParser getParser();
    
    JsonMapper getMapper();
    
    void close();
}