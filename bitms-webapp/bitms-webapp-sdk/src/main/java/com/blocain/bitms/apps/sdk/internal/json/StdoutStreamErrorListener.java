package com.blocain.bitms.apps.sdk.internal.json;

public class StdoutStreamErrorListener extends BufferErrorListener
{
    public void end()
    {
        System.out.print(buffer.toString());
    }
}
