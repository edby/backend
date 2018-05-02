/*
 * @(#)StringEditorSupport.java 2017年7月19日 下午1:57:29
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.common;

import com.blocain.bitms.tools.utils.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.beans.PropertyEditorSupport;

/**
 * <p>File：StringEditorSupport.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月19日 下午1:57:29</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class StringEditorSupport extends PropertyEditorSupport
{

    
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        if (StringUtils.isBlank(text))
        {
            setValue(null);
        }
        else
        {
            if (null == text) setValue(null);
            else
            {
                text = StringEscapeUtils.escapeHtml4(text);
                setValue(text);
            }
            super.setAsText(text);
            setValue(text); 
        }
    }
    
    @Override
    public String getAsText()
    {
        String value = (String)getValue();
        if (null == value)
            return null;
        else
            return value.toString().trim();
    }
}
