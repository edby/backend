package com.blocain.bitms.orm.annotation;

import java.lang.annotation.*;

/**
 * 数据源标识，用来动态切换主从数据库
 * 默认不配置此注解的方法走主库，反之则根据slave状态决定
 * 因AOP是通过此注解进入方法切入的，正常情况下走主库的方法不需要配置此注解.
 * <p>File: SlaveDataSource.java</p>
 * <p>Title: SlaveDataSource</p>
 * <p>Description: SlaveDataSource</p>
 * <p>Copyright: Copyright (c) 16/3/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SlaveDataSource
{
    boolean slave() default true;
}
