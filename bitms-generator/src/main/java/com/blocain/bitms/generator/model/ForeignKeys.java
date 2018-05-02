package com.blocain.bitms.generator.model;

import com.blocain.bitms.generator.utils.ListHashtable;

/**
 * 外键组对象
 * <p>File：ForeignKeys.java</p>
 * <p>Title: ForeignKeys</p>
 * <p>Description:ForeignKeys</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public class ForeignKeys
{
    protected Table         parentTable;
    
    protected ListHashtable associatedTables;
    
    public ForeignKeys(Table aTable)
    {
        super();
        parentTable = aTable;
        associatedTables = new ListHashtable();
    }
    
    /**
     * 添加外键
     * @author Playguy
     * @param tableName
     * @param columnName
     * @param parentColumn
     * @param seq
     */
    public void addForeignKey(String tableName, String columnName, String parentColumn, Integer seq)
    {
        ForeignKey tbl = null;
        if (associatedTables.containsKey(tableName))
        {
            tbl = (ForeignKey) associatedTables.get(tableName);
        }
        else
        {
            tbl = new ForeignKey(parentTable, tableName);
            associatedTables.put(tableName, tbl);
        }
        tbl.addColumn(columnName, parentColumn, seq);
    }
    
    /**
     * 获取关联表
     * @author Playguy
     * @return
     */
    public ListHashtable getAssociatedTables()
    {
        return associatedTables;
    }
    
    public int getSize()
    {
        return getAssociatedTables().size();
    }
    
    /**
     * 判断列名是否为外键
     * @author Playguy
     * @param aColumn
     * @return
     */
    public boolean getHasImportedKeyColumn(String aColumn)
    {
        boolean isFound = false;
        int numKeys = getAssociatedTables().size();
        for (int i = 0; i < numKeys; i++)
        {
            ForeignKey aKey = (ForeignKey) getAssociatedTables().getOrderedValue(i);
            if (aKey.getHasImportedKeyColumn(aColumn))
            {
                isFound = true;
                break;
            }
        }
        return isFound;
    }
    
    /**
     * 获取外键的表名
     * @author Playguy
     * @param name
     * @return
     */
    public ForeignKey getAssociatedTable(String name)
    {
        Object fkey = getAssociatedTables().get(name);
        if (fkey != null)
        {
            return (ForeignKey) fkey;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取上级表名
     * @author Playguy
     * @return
     */
    public Table getParentTable()
    {
        return parentTable;
    }
    
    /**
     * 判断列的上级列是否为外键列
     * @author Playguy
     * @param aColumn
     * @return
     */
    public boolean getHasImportedKeyParentColumn(String aColumn)
    {
        boolean isFound = false;
        int numKeys = getAssociatedTables().size();
        for (int i = 0; i < numKeys; i++)
        {
            ForeignKey aKey = (ForeignKey) getAssociatedTables().getOrderedValue(i);
            if (aKey.getHasImportedKeyParentColumn(aColumn))
            {
                isFound = true;
                break;
            }
        }
        return isFound;
    }
    
    /**
     * 获取外键对象
     * @author Playguy
     * @param aColumn
     * @return
     */
    public ForeignKey getImportedKeyParentColumn(String aColumn)
    {
        ForeignKey aKey = null;
        int numKeys = getAssociatedTables().size();
        for (int i = 0; i < numKeys; i++)
        {
            aKey = (ForeignKey) getAssociatedTables().getOrderedValue(i);
            if (aKey.getHasImportedKeyParentColumn(aColumn))
            {
                break;
            }
        }
        return aKey;
    }
}
