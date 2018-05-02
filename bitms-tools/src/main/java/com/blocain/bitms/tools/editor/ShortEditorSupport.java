/*
 * @(#)ShortEditorSupport.java 2014-6-14 下午6:37:21
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.editor;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.beans.PropertyEditorSupport;

/**
 * <p>File：ShortEditorSupport.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-6-14 下午6:37:21</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ShortEditorSupport extends PropertyEditorSupport
{
    @Override
    public String getAsText()
    {
        Short value = (Short) getValue();
        if (null == value) return null;
        else return value.toString();
    }
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        if (StringUtils.isBlank(text)) setValue(null);
        else
        {
            text = Jsoup.clean(text, Whitelist.relaxed());
            try
            {
                setValue(Short.parseShort(text));
            }
            catch (NumberFormatException e)
            {
                setValue(null);
            }
        }
    }
}
