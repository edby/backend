/*
 * @(#)ByteEditorSupport.java 2014-6-14 下午4:31:29
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.editor;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.beans.PropertyEditorSupport;

/**
 * <p>File：ByteEditorSupport.java</p>
 * <p>Title: Byte型参数处理</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-6-14 下午4:31:29</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ByteEditorSupport extends PropertyEditorSupport
{
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        if (StringUtils.isBlank(text)) setValue(null);
        else
        {
            text = Jsoup.clean(text, Whitelist.relaxed());
            try
            {
                setValue(Byte.parseByte(text));
            }
            catch (NumberFormatException e)
            {
                setValue(null);
            }
        }
    }
    
    @Override
    public String getAsText()
    {
        Byte value = (Byte) getValue();
        if (null == value) return null;
        else return value.toString();
    }
}
