package com.blocain.bitms.orm.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * DynamicDataSource
 * <p>File: DynamicDataSource.java</p>
 * <p>Title: DynamicDataSource</p>
 * <p>Description: DynamicDataSource</p>
 * <p>Copyright: Copyright (c) 16/3/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{
    public static final String  MASTER      = "master";           // 主库标识
    
    public static final String  SLAVE       = "slave";            // 从库标识
    
    private AtomicInteger       slaveCount  = new AtomicInteger();
    
    private Map<Object, Object> dataSources = new HashMap<>();
    
    private DataSource          master;
    
    private List<DataSource>    slaves;
    
    @Override
    public void afterPropertiesSet()
    {
        if (null == master) throw new IllegalArgumentException("Property 'master' is required");
        if ((null != slaves) && (slaves.size() > 0))
        {
            for (int i = 0; i < slaves.size(); i++)
            {
                StringBuffer key = new StringBuffer(SLAVE).append(i);
                dataSources.put(key.toString(), slaves.get(i));
            }
        }
        dataSources.put(MASTER, master);
        setDefaultTargetDataSource(master);
        setTargetDataSources(dataSources);
        super.afterPropertiesSet();
    }
    
    @Override
    protected Object determineCurrentLookupKey()
    {
        LinkedList<String> m = DataSourceHolder.threadLocal.get();
        String key = m.peekFirst() == null ? null : m.peekFirst();
        if (null != key)
        {
            if (SLAVE.equals(key))
            {
                logger.debug("从库");
                int index = this.slaveCount.incrementAndGet() % this.slaves.size();
                if (index < 0) index = -index;
                return new StringBuffer(SLAVE).append(index).toString();
            }
        }
        logger.debug("主库");
        return MASTER;
    }
    
    public void setMaster(DataSource master)
    {
        this.master = master;
    }
    
    public void setSlaves(List<DataSource> slaves)
    {
        this.slaves = slaves;
    }
}
