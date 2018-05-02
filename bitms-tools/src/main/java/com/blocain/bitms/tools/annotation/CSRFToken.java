package com.blocain.bitms.tools.annotation;

import java.lang.annotation.*;

/**
 * 验证CSRF口令 Introduce
 * <p>File：CSRFToken.java</p>
 * <p>Title: CSRFToken</p>
 * <p>Description: CSRFToken</p>
 * <p>Copyright: Copyright (c) 2017/7/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CSRFToken
{
    /**
     * 需要验证防跨站请求
     * @author tangguilin
     * @return
     */
    boolean check() default true;
}
