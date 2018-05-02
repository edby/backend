package com.blocain.bitms.generator.provider;

import com.blocain.bitms.generator.IModelProvider;
import com.blocain.bitms.generator.model.IModel;

/**
 * 数据库表模型的实现类
 * <p>File：DbTableProvider.java</p>
 * <p>Title: DbTableProvider</p>
 * <p>Description:DbTableProvider</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class DbTableProvider implements IModelProvider
{
    /**
     * 表名
     */
    private String dbTableName;
    
    public DbTableProvider(String dbTableName)
    {
        super();
        this.dbTableName = dbTableName;
    }
    
    public IModel getModel() throws Exception
    {
        return DbModelHelper.getInstance().getTable(dbTableName);
    }
}
