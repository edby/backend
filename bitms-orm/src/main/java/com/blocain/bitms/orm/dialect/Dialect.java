/*
 * @(#)Dialect.java 2015-4-17 下午3:29:26
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.orm.dialect;

/**
 * <p>File：Dialect.java</p>
 * <p>Title: </p>
 * <p>Description:类似hibernate的Dialect,但只精简出分页部分</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-17 下午3:29:26</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface Dialect
{
    /**
     * 数据库本身是否支持当前的分页查询方式
     * 如果数据库不支持的话，则不进行数据库分页
     *
     * @return true：支持当前的分页查询方式
     */
    boolean supportsLimit();
    
    /**
     * 将sql转换为分页SQL，分别调用分页sql
     *
     * @param sql    SQL语句
     * @param offset 开始条数
     * @param limit  每页显示多少纪录条数
     * @return 分页查询的sql
     */
    String getLimitString(String sql, int offset, int limit);
}
