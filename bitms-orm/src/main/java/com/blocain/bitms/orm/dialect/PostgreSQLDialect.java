/*
 * @(#)PostgreSQLDialect.java 2015-4-17 下午3:35:32
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.orm.dialect;

/**
 * <p>File：PostgreSQLDialect.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-17 下午3:35:32</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class PostgreSQLDialect implements Dialect
{
    @Override
    public boolean supportsLimit()
    {
        return true;
    }
    
    @Override
    public String getLimitString(String sql, int offset, int limit)
    {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
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
    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder)
    {
        StringBuilder pageSql = new StringBuilder().append(sql);
        pageSql = offset <= 0 ? pageSql.append(" limit ").append(limitPlaceholder) : pageSql.append(" limit ").append(limitPlaceholder).append(" offset ")
                .append(offsetPlaceholder);
        return pageSql.toString();
    }
}
