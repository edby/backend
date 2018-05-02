/*
 * @(#)Dialect.java 2015-4-17 下午3:29:26
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.orm.interceptor;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import com.blocain.bitms.tools.utils.PropertiesUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.plugin.Interceptor;

import com.blocain.bitms.orm.dialect.*;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.utils.Reflections;

/**
 * <p>File：BaseInterceptor.java</p>
 * <p>Title: BaseInterceptor</p>
 * <p>Description:BaseInterceptor</p>
 * <p>Copyright: Copyright (c) 2015/04/21 11:17</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public abstract class BaseInterceptor implements Interceptor, Serializable
{
    private static final long      serialVersionUID = 1L;
    
    protected static final String  PAGINATION       = "pagin";
    
    private static PropertiesUtils properties       = new PropertiesUtils("jdbc.properties");
    
    protected Dialect              dialect;
    
    /**
     * 对参数进行转换和检查
     *
     * @param parameterObject 参数对象
     * @return 分页对象
     * @throws NoSuchFieldException 无法找到参数
     */
    @SuppressWarnings("unchecked")
    protected static Pagination convertParameter(Object parameterObject)
    {
        Pagination pagination = null;
        try
        {
            if (parameterObject instanceof Pagination)
            {// 判断当前参数是否就是分页对象
                pagination = (Pagination) parameterObject;
            }
//            else if (parameterObject instanceof Map)
//            {//取消Map参数分页转换
//                Map<String, Object> maps =  (Map<String, Object>) parameterObject;
//                if (maps.containsKey(PAGINATION))
//                {// 判断MAP中是否存在分页对象
//                    Object object = MapUtils.getObject(maps, PAGINATION, null);
//                    if (null != object && object instanceof Pagination) pagination = (Pagination) object;
//                }
//            }
            else
            {// 通过反射获取分页对象
                pagination = (Pagination) Reflections.getFieldValue(parameterObject, PAGINATION);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return pagination;
    }
    
    /**
     * 设置属性，支持自定义方言类和制定数据库的方式
     * <code>dialectClass</code>,自定义方言类。可以不配置这项
     * <ode>dbms</ode> 数据库类型，插件支持的数据库
     * <code>sqlPattern</code> 需要拦截的SQL ID
     *
     * @param p 属性
     */
    protected void initProperties(Properties p)
    {
        String dbType = properties.getProperty("jdbc.type", "");
        if ("mysql".equals(dbType))
        {
            dialect = new MysqlDialect();
        }
        else if ("oracle".equals(dbType))
        {
            dialect = new OracleDialect();
        }
        else if ("postgre".equals(dbType))
        {
            dialect = new PostgreSQLDialect();
        }
        else if ("mssql".equals(dbType) || "sqlserver".equals(dbType))
        {
            dialect = new SQLServer2005Dialect();
        }
        if (dialect == null) { throw new RuntimeException("mybatis dialect error."); }
    }
}