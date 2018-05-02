package com.blocain.bitms.generator.provider;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.blocain.bitms.generator.model.Column;
import com.blocain.bitms.generator.model.Table;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blocain.bitms.generator.utils.PropertiesProvider;

/**
 * 数据库访问辅助类
 * <p>File：DbModelHelper.java</p>
 * <p>Title: DbModelHelper</p>
 * <p>Description:DbModelHelper</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * 
 * @author Playguy
 * @version 1.0
 */
public class DbModelHelper
{
    private static final Log     _log     = LogFactory.getLog(DbModelHelper.class);
    
    /**
     * 目录
     */
    public String                catalog;
    
    /**
     * 概要
     */
    public String                schema;
    
    /**
     * 连接
     */
    private Connection           connection;
    
    /**
     * 单例对象
     */
    private static DbModelHelper instance = new DbModelHelper();
    
    private DbModelHelper()
    {
        init();
    }
    
    public static DbModelHelper getInstance()
    {
        return instance;
    }
    
    /**
     * 初始化
     * @author Playguy
     */
    private void init()
    {
        try
        {
            this.schema = PropertiesProvider.getProperty("jdbc.schema");
            if ("".equals(schema.trim()))
            {
                this.schema = null;
            }
            this.catalog = PropertiesProvider.getProperty("jdbc.catalog");
            if ("".equals(catalog.trim()))
            {
                this.catalog = null;
            }
            Class.forName(PropertiesProvider.getProperty("jdbc.driver"));
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 取得一个连接
     * @author Playguy
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException
    {
        if (connection == null || connection.isClosed())
        {
            String url = PropertiesProvider.getProperty("jdbc.url");
            String username = PropertiesProvider.getProperty("jdbc.username");
            String password = PropertiesProvider.getProperty("jdbc.password");
            Properties props = new Properties();
            props.put("user", username);
            props.put("password", password);
            props.put("remarksReporting", "true");
            connection = DriverManager.getConnection(url, props);
        }
        return connection;
    }
    
    /**
     * 取所有表
     * @author Playguy
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List getAllTables() throws Exception
    {
        Connection conn = getConnection();
        return getAllTables(conn);
    }
    
    /**
     * 取单个
     * @author Playguy
     * @param sqlTableName
     * @return
     * @throws Exception
     */
    public Table getTable(String sqlTableName) throws Exception
    {
        Connection conn = getConnection();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(catalog, schema, sqlTableName, null);
        while (rs.next())
        {
            Table table = createTable(conn, rs);
            return table;
        }
        throw new RuntimeException("没有找到表名:" + sqlTableName);
    }
    
    /**
     * 创建表对象
     * @author Playguy
     * @param conn
     * @param rs
     * @return
     * @throws SQLException
     */
    private Table createTable(Connection conn, ResultSet rs) throws SQLException
    {
        String realTableName = rs.getString("TABLE_NAME");
        String tableType = rs.getString("TABLE_TYPE");
        Table table = new Table();
        table.setSqlName(realTableName);
        if (isMysqlDataBase())
        {
            table.setSqlRemark(getMysqlTableComment(realTableName, conn));
        }
        else
        {
            table.setSqlRemark(rs.getString("REMARKS"));
        }
        if ("SYNONYM".equals(tableType) && isOracleDataBase())
        {
            table.setOwnerName(getSynonymOwner(realTableName));
        }
        table.initExportedKeys(conn.getMetaData());
        table.initImportedKeys(conn.getMetaData());
        retriveTableColumns(table);
        return table;
    }
    
    /**
     * 取数据表的注释信息
     * @author Playguy
     * @param tableName
     * @param conn
     * @return
     */
    private String getMysqlTableComment(String tableName, Connection conn)
    {
        Statement st = null;
        ResultSet rs = null;
        String result = "";
        try
        {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + schema + "' AND TABLE_NAME='" + tableName + "'");
            while (rs.next())
            {
                result = rs.getString(1);
                break;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rs != null) rs.close();
                if (st != null) st.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 取所有表
     * @author Playguy
     * @param conn
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List getAllTables(Connection conn) throws SQLException
    {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(catalog, schema, null, null);
        List tables = new ArrayList();
        while (rs.next())
        {
            Table table = createTable(conn, rs);
            tables.add(table);
        }
        return tables;
    }
    
    /**
     * 判断是不是ORACLE数据库
     * @author Playguy
     * @return
     */
    private boolean isOracleDataBase()
    {
        boolean ret = false;
        try
        {
            ret = (getMetaData().getDatabaseProductName().toLowerCase().indexOf("oracle") != -1);
        }
        catch (Exception ignore)
        {
        }
        return ret;
    }
    
    /**
     * 判断是否为Mysql数据库
     * @author Playguy
     * @return
     */
    private boolean isMysqlDataBase()
    {
        boolean ret = false;
        try
        {
            ret = (getMetaData().getDatabaseProductName().toLowerCase().indexOf("mysql") != -1);
        }
        catch (Exception ignore)
        {
        }
        return ret;
    }
    
    /**
     * 取得表的所属帐户
     * @author Playguy
     * @param synonymName
     * @return
     */
    private String getSynonymOwner(String synonymName)
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String ret = null;
        try
        {
            ps = getConnection().prepareStatement("select table_owner from sys.all_synonyms where table_name=? and owner=?");
            ps.setString(1, synonymName);
            ps.setString(2, schema);
            rs = ps.executeQuery();
            if (rs.next())
            {
                ret = rs.getString(1);
            }
            else
            {
                String databaseStructure = getDatabaseStructureInfo();
                throw new RuntimeException(" 用户 " + synonymName + " 未找到. " + databaseStructure);
            }
        }
        catch (SQLException e)
        {
            String databaseStructure = getDatabaseStructureInfo();
            _log.error(e.getMessage(), e);
            throw new RuntimeException("取用户信息出错 " + databaseStructure);
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (Exception e)
                {
                }
            }
            if (ps != null)
            {
                try
                {
                    ps.close();
                }
                catch (Exception e)
                {
                }
            }
        }
        return ret;
    }
    
    /**
     * 取得数据库结构信息
     * 
     * @return {@link String}
     */
    @SuppressWarnings("resource")
    private String getDatabaseStructureInfo()
    {
        ResultSet schemaRs = null;
        ResultSet catalogRs = null;
        String nl = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer(nl);
        sb.append("Configured schema:").append(schema).append(nl);
        sb.append("Configured catalog:").append(catalog).append(nl);
        try
        {
            schemaRs = getMetaData().getSchemas();
            sb.append("Available schemas:").append(nl);
            while (schemaRs.next())
            {
                sb.append("  ").append(schemaRs.getString("TABLE_SCHEM")).append(nl);
            }
        }
        catch (SQLException e2)
        {
            _log.warn("未能取得 概要(schemas)", e2);
            sb.append("  ?? Couldn't get schemas ??").append(nl);
        }
        finally
        {
            try
            {
                schemaRs.close();
            }
            catch (Exception ignore)
            {
            }
        }
        try
        {
            catalogRs = getMetaData().getCatalogs();
            sb.append("Available catalogs:").append(nl);
            while (catalogRs.next())
            {
                sb.append("  ").append(catalogRs.getString("TABLE_CAT")).append(nl);
            }
        }
        catch (SQLException e2)
        {
            _log.warn("未能取得 目录(catalogs)", e2);
            sb.append("  ?? Couldn't get catalogs ??").append(nl);
        }
        finally
        {
            try
            {
                catalogRs.close();
            }
            catch (Exception ignore)
            {
            }
        }
        return sb.toString();
    }
    
    /**
     * 取数据库元数据
     * 
     * @return {@link DatabaseMetaData}
     * @throws SQLException
     */
    private DatabaseMetaData getMetaData() throws SQLException
    {
        return getConnection().getMetaData();
    }
    
    /**
     * 检索索引字段
     * 
     * @param table
     * @throws SQLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void retriveTableColumns(Table table) throws SQLException
    {
        _log.debug("-------设置列(" + table.getSqlName() + ")");
        List primaryKeys = getTablePrimaryKeys(table);
        table.setPrimaryKeyColumns(primaryKeys);
        List indices = new LinkedList();
        Map uniqueIndices = new HashMap();
        Map uniqueColumns = new HashMap();
        ResultSet indexRs = null;
        try
        {
            if (table.getOwnerName() != null)
            {
                indexRs = getMetaData().getIndexInfo(catalog, table.getOwnerName(), table.getSqlName(), false, true);
            }
            else
            {
                indexRs = getMetaData().getIndexInfo(catalog, schema, table.getSqlName(), false, true);
            }
            while (indexRs.next())
            {
                String columnName = indexRs.getString("COLUMN_NAME");
                if (columnName != null)
                {
                    _log.debug("索引:" + columnName);
                    indices.add(columnName);
                }
                String indexName = indexRs.getString("INDEX_NAME");
                boolean nonUnique = indexRs.getBoolean("NON_UNIQUE");
                if (!nonUnique && columnName != null && indexName != null)
                {
                    List l = (List) uniqueColumns.get(indexName);
                    if (l == null)
                    {
                        l = new ArrayList();
                        uniqueColumns.put(indexName, l);
                    }
                    l.add(columnName);
                    uniqueIndices.put(columnName, indexName);
                    _log.debug("约束:" + columnName + " (" + indexName + ")");
                }
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            if (indexRs != null)
            {
                indexRs.close();
            }
        }
        List columns = getTableColumns(table, primaryKeys, indices, uniqueIndices, uniqueColumns);
        for (Iterator i = columns.iterator(); i.hasNext();)
        {
            Column column = (Column) i.next();
            table.addColumn(column);
        }
        if (primaryKeys.size() == 0)
        {
            _log.warn("警告: 此表中未找到主键 " + table.getSqlName());
        }
    }
    
    /**
     * 取表的列
     * 
     * @param table
     * @param primaryKeys
     * @param indices
     * @param uniqueIndices
     * @param uniqueColumns
     * @return {@link List}
     * @throws SQLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List getTableColumns(Table table, List primaryKeys, List indices, Map uniqueIndices, Map uniqueColumns) throws SQLException
    {
        List columns = new LinkedList();
        ResultSet columnRs = getColumnsResultSet(table);
        while (columnRs.next())
        {
            int sqlType = columnRs.getInt("DATA_TYPE");
            String sqlTypeName = columnRs.getString("TYPE_NAME");
            String columnName = columnRs.getString("COLUMN_NAME");
            String columnRemark = columnRs.getString("REMARKS");
            String columnDefaultValue = columnRs.getString("COLUMN_DEF");
            boolean isNullable = (DatabaseMetaData.columnNullable == columnRs.getInt("NULLABLE"));
            int size = columnRs.getInt("COLUMN_SIZE");
            int decimalDigits = columnRs.getInt("DECIMAL_DIGITS");
            boolean isPk = primaryKeys.contains(columnName);
            boolean ifFk = table.getImportedKeys().getHasImportedKeyColumn(columnName);
            boolean isIndexed = indices.contains(columnName);
            String uniqueIndex = (String) uniqueIndices.get(columnName);
            List columnsInUniqueIndex = null;
            if (uniqueIndex != null)
            {
                columnsInUniqueIndex = (List) uniqueColumns.get(uniqueIndex);
            }
            boolean isUnique = columnsInUniqueIndex != null && columnsInUniqueIndex.size() == 1;
            if (isUnique)
            {
                _log.debug("唯一约束:" + columnName);
            }
            Column column = new Column(table, sqlType, sqlTypeName, columnName, size, decimalDigits, isPk, ifFk, isNullable, isIndexed, isUnique, columnDefaultValue,
                    columnRemark);
            columns.add(column);
        }
        columnRs.close();
        return columns;
    }
    
    /**
     * 取列的记录集
     * 
     * @param table
     * @return {@link ResultSet}
     * @throws SQLException
     */
    private ResultSet getColumnsResultSet(Table table) throws SQLException
    {
        ResultSet columnRs = null;
        if (table.getOwnerName() != null)
        {
            columnRs = getMetaData().getColumns(catalog, table.getOwnerName(), table.getSqlName(), null);
        }
        else
        {
            columnRs = getMetaData().getColumns(catalog, schema, table.getSqlName(), null);
        }
        return columnRs;
    }
    
    /**
     * 取表的主键
     * 
     * @param table
     * @return {@link List}
     * @throws SQLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List getTablePrimaryKeys(Table table) throws SQLException
    {
        List primaryKeys = new LinkedList();
        ResultSet primaryKeyRs = null;
        if (table.getOwnerName() != null)
        {
            primaryKeyRs = getMetaData().getPrimaryKeys(catalog, table.getOwnerName(), table.getSqlName());
        }
        else
        {
            primaryKeyRs = getMetaData().getPrimaryKeys(catalog, schema, table.getSqlName());
        }
        while (primaryKeyRs.next())
        {
            String columnName = primaryKeyRs.getString("COLUMN_NAME");
            _log.debug("主建:" + columnName);
            primaryKeys.add(columnName);
        }
        primaryKeyRs.close();
        return primaryKeys;
    }
}
