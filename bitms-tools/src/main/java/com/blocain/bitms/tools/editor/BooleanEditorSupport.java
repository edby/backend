/*
 * @(#)BooleanEditSupport.java 2014-6-14 下午3:28:43
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.editor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.BooleanUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * <p>File：BooleanEditSupport.java</p>
 * <p>Title: 布尔型参数处理</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-6-14 下午3:28:43</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BooleanEditorSupport extends PropertyEditorSupport
{
    private static final String INT_FALSE = "0";
    
    private static final String INT_TRUE  = "1";
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        if (null == text) setValue(null);
        else if (text.equals(INT_FALSE)) setValue(Boolean.FALSE);
        else if (text.equals(INT_TRUE)) setValue(Boolean.TRUE);
        else
        {
            text = Jsoup.clean(text, Whitelist.relaxed());
            setValue(BooleanUtils.toBoolean(text));
        }
    }
    
    @Override
    public String getAsText()
    {
        Boolean value = (Boolean) getValue();
        if (null == value) return null;
        else return value.toString();
    }
}