/*
 * @(#)OracleDialect.java 2015-4-17 下午3:29:17
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.orm.dialect;

/**
 * <p>File：OracleDialect.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-17 下午3:29:17</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class OracleDialect implements Dialect
{
    @Override
    public boolean supportsLimit()
    {
        return true;
    }
    
    @Override
    public String getLimitString(String sql, int offset, int limit)
    {
        return getLimitString(sql, offset, offset, limit);
    }
    
    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql               实际SQL语句
     * @param offset            分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limitPlaceholder  分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sql, int offset, int offsetPlaceholder, int limitPlaceholder)
    {
        sql = sql.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update"))
        {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }
        StringBuilder pagingSelect = new StringBuilder(sql.length());
        if (offset > 0) pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        else pagingSelect.append("select * from ( ");
        pagingSelect.append(sql);
        if (offset > 0)
        {
            pagingSelect.append(" ) row_ where rownum <=");
            pagingSelect.append(offsetPlaceholder + limitPlaceholder).append(")");
            pagingSelect.append(" where rownum_ > ");
            pagingSelect.append(offsetPlaceholder);
        }
        else
        {
            pagingSelect.append(") where rownum <= ");
            pagingSelect.append(limitPlaceholder);
        }
        if (isForUpdate) pagingSelect.append(" for update");
        return pagingSelect.toString();
    }
}
