package com.blocain.bitms.apps.sdk.internal.json;

public interface JSONErrorListener
{
    void start(String text);
    
    void error(String message, int column);
    
    void end();
}
