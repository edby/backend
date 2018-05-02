package com.blocain.bitms.orm.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源路由
 * <p>File: MultipleDataSource.java</p>
 * <p>Title: MultipleDataSource</p>
 * <p>Description: MultipleDataSource</p>
 * <p>Copyright: Copyright (c) 16/3/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class MultipleDataSource extends AbstractRoutingDataSource
{
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();
    
    @Override
    protected Object determineCurrentLookupKey()
    {
        return dataSourceKey.get();
    }
    
    /**
     * 设置数据源
     * @param dataSource
     */
    public static void setDataSourceKey(String dataSource)
    {
        dataSourceKey.set(dataSource);
    }
}
