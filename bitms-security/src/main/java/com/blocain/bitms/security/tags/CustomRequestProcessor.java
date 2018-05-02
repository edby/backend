package com.blocain.bitms.security.tags;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * CustomRequestProcessor Introduce
 * <p>File：CustomRequestProcessor.java</p>
 * <p>Title: CustomRequestProcessor</p>
 * <p>Description: CustomRequestProcessor</p>
 * <p>Copyright: Copyright (c) 2017/8/7</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public interface CustomRequestProcessor extends RequestDataValueProcessor
{
    /**
     * Invoked after all form fields have been rendered.
     * @param request the current request
     * @return additional hidden form fields to be added, or {@code null}
     */
    Map<String, String> getExtraHiddenFields(HttpServletRequest request, String formId);
}
