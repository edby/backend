package com.blocain.bitms.generator.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blocain.bitms.generator.utils.DatabaseDataTypesUtils;
import com.blocain.bitms.generator.utils.StringHelper;

/**
 * 映射数据库表列名
 * <p>File：Column.java</p>
 * <p>Title: Column</p>
 * <p>Description:Column</p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * 
 * @author Playguy
 * @version 1.0
 */
public class Column
{
    private Table         _table;
    
    private int           _sqlType;
    
    private String        _sqlTypeName;
    
    private String        _sqlName;
    
    private String        _sqlRemark;
    
    private boolean       _isPk;
    
    private boolean       _isFk;
    
    private final int     _size;
    
    private final int     _decimalDigits;
    
    private final boolean _isNullable;
    
    private final boolean _isIndexed;
    
    private final boolean _isUnique;
    
    private final String  _defaultValue;
    
    private static Log    _log = LogFactory.getLog(Column.class);
    
    /**
     * 默认构造方法
     * @param table
     * @param sqlType
     * @param sqlTypeName
     * @param sqlName
     * @param size
     * @param decimalDigits
     * @param isPk
     * @param isFk
     * @param isNullable
     * @param isIndexed
     * @param isUnique
     * @param defaultValue
     * @param sqlRemark
     */
    public Column(Table table, int sqlType, String sqlTypeName, String sqlName, int size, int decimalDigits, boolean isPk, boolean isFk, boolean isNullable,
            boolean isIndexed, boolean isUnique, String defaultValue, String sqlRemark)
    {
        _table = table;
        _sqlType = sqlType;
        _sqlName = sqlName;
        _sqlTypeName = sqlTypeName;
        _sqlRemark = sqlRemark;
        _size = size;
        _decimalDigits = decimalDigits;
        _isPk = isPk;
        _isFk = isFk;
        _isNullable = isNullable;
        _isIndexed = isIndexed;
        _isUnique = isUnique;
        _defaultValue = defaultValue;
        _log.debug(sqlName + " isPk -> " + _isPk);
        _log.debug(sqlName + " isFk -> " + _isFk);
    }
    
    public int getSqlType()
    {
        return _sqlType;
    }
    
    public Table getTable()
    {
        return _table;
    }
    
    public int getSize()
    {
        return _size;
    }
    
    public int getDecimalDigits()
    {
        return _decimalDigits;
    }
    
    public String getSqlTypeName()
    {
        if ("NUMBER".equals(_sqlTypeName)) _sqlTypeName = "NUMERIC";
        if ("VARCHAR2".equals(_sqlTypeName)) _sqlTypeName = "VARCHAR";
        return _sqlTypeName;
    }
    
    public String getSqlName()
    {
        return _sqlName;
    }
    
    public String getRemark()
    {
        if (_sqlRemark != null && _sqlRemark.length() > 0) { return _sqlRemark; }
        return getColumnNameLower();
    }
    
    public boolean isPk()
    {
        return _isPk;
    }
    
    public boolean isFk()
    {
        return _isFk;
    }
    
    public boolean isNullable()
    {
        return _isNullable;
    }
    
    public final boolean isIndexed()
    {
        return _isIndexed;
    }
    
    public boolean isUnique()
    {
        return _isUnique;
    }
    
    public final String getDefaultValue()
    {
        return _defaultValue;
    }
    
    @Override
    public int hashCode()
    {
        return (getTable().getSqlName() + "#" + getSqlName()).hashCode();
    }
    
    @Override
    public boolean equals(Object o)
    {
        return this == o;
    }
    
    @Override
    public String toString()
    {
        return getSqlName();
    }
    
    protected final String prefsPrefix()
    {
        return "tables/" + getTable().getSqlName() + "/columns/" + getSqlName();
    }
    
    void setFk(boolean flag)
    {
        _isFk = flag;
    }
    
    public String getColumnName()
    {
        return StringHelper.makeAllWordFirstLetterUpperCase(getSqlName());
    }
    
    public String getColumnNameLower()
    {
        return StringHelper.uncapitalize(getColumnName());
    }
    
    public boolean getIsNotIdOrVersionField()
    {
        return !isPk();
    }
    
    public String getValidateString()
    {
        String result = getNoRequiredValidateString();
        if (!isNullable())
        {
            result = "required " + result;
        }
        return result;
    }
    
    public String getNoRequiredValidateString()
    {
        String result = "";
        if (getSqlName().indexOf("mail") >= 0)
        {
            result += "validate-email ";
        }
        if (DatabaseDataTypesUtils.isFloatNumber(getSqlType(), getSize(), getDecimalDigits()))
        {
            result += "validate-number ";
        }
        if (DatabaseDataTypesUtils.isIntegerNumber(getSqlType(), getSize(), getDecimalDigits()))
        {
            result += "validate-integer ";
        }
        if (DatabaseDataTypesUtils.isDate(getSqlType(), getSize(), getDecimalDigits()))
        {
            result += "validate-date ";
        }
        return result;
    }
    
    public boolean getIsDateTimeColumn()
    {
        return DatabaseDataTypesUtils.isDate(getSqlType(), getSize(), getDecimalDigits());
    }
    
    public boolean getIsBooleanColumn()
    {
        return DatabaseDataTypesUtils.isBoolean(getSqlType(), getSize(), getDecimalDigits());
    }
    
    public boolean getIsNumberColumn()
    {
        return DatabaseDataTypesUtils.isIntegerNumber(getSqlType(), getSize(), getDecimalDigits());
    }
    
    public boolean isHtmlHidden()
    {
        return isPk() && _table.isSingleId();
    }
    
    public String getJavaType()
    {
        return DatabaseDataTypesUtils.getPreferredJavaType(getSqlType(), getSize(), getDecimalDigits());
    }
}
