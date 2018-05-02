package com.blocain.bitms.orm.db;

import java.util.LinkedList;

/**
 * DataSourceHolder
 * <p>File: DataSourceHolder.java</p>
 * <p>Title: DataSourceHolder</p>
 * <p>Description: DataSourceHolder</p>
 * <p>Copyright: Copyright (c) 16/3/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DataSourceHolder
{
    public static final ThreadLocal<LinkedList<String>> threadLocal;

    static
    {
        threadLocal = new InheritableThreadLocal<LinkedList<String>>()
        {
            protected LinkedList<String> initialValue()
            {
                return new LinkedList<>();
            }
        };
    }
    
    /**
     * 选择从库
     */
    public static void useSlave()
    {
        LinkedList<String> m = threadLocal.get();
        m.offerFirst(DynamicDataSource.SLAVE);
    }
    
    /**
     * 重置数据源
     */
    public static void reset()
    {
        LinkedList<String> m = threadLocal.get();
        if (m.size() > 0) m.poll();
    }
}
