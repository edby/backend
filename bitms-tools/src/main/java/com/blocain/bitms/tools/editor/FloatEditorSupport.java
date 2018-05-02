/*
 * @(#)FloatEditorSupport.java 2014-6-14 下午4:24:22
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.editor;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.beans.PropertyEditorSupport;

/**
 * <p>File：FloatEditorSupport.java</p>
 * <p>Title: 单精度型参数处理</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-6-14 下午4:24:22</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class FloatEditorSupport extends PropertyEditorSupport
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
                setValue(Float.parseFloat(text));
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
        Integer value = (Integer) getValue();
        if (null == value) return null;
        else return value.toString();
    }
}
