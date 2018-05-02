package com.blocain.bitms.boss.common.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * JdbcTemplate增加分页查询
 * 
 * @author chenh
 * 
 */
public class JdbcTemplateWithPaging
{
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateWithPaging.class);
    
    @Autowired
    private JdbcTemplate        jdbcTemplate;
    
    public JdbcTemplateWithPaging()
    {
    }
    
    /**
     * 分页查询
     * 
     * @param sql
     *            查询的sql语句
     * @param args
     *            参数
     * @param start
     *            起始行
     * @param limit
     *            获取的行数
     * @return
     * @throws DataAccessException
     */
    public List<Map<String, Object>> queryPage(String sql, Object[] args, int start, int limit)
    {
        if (start <= 0 && limit <= 0) { return (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, args); }
        if (start <= 1)
        {
            sql = getLimitString(sql, false);
            args = ArrayUtils.add(args, args.length, limit);
        }
        else
        {
            sql = getLimitString(sql, true);
            args = ArrayUtils.add(args, args.length, start);
            args = ArrayUtils.add(args, args.length, start + limit);
        }
        logger.info("paging sql : \n" + sql);
        return (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, args);
    }
    
    /**
     * 分页查询
     * 
     * @param sql
     *            查询的sql语句
     * @param start
     *            起始行
     * @param limit
     *            获取的行数
     * @return
     */
    public List<Map<String, Object>> queryPage(String sql, int start, int limit)
    {
        Object[] args = new Object[]{};
        return this.queryPage(sql, args, start, limit);
    }
    
    private String getLimitString(String sql, boolean hasOffset)
    {
        int startOfSelect = sql.toLowerCase().indexOf("select");
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100).append(sql.substring(0, startOfSelect)) // add the comment
                .append("select * from ( select ").append("rownum as rownumber_,");
        if (hasDistinct(sql))
        {
            pagingSelect.append(" row_.* from ( ").append(sql.substring(startOfSelect)) // add the main query
                    .append(" ) as row_"); // close off the inner nested select
        }
        else
        {
            pagingSelect.append(sql.substring(startOfSelect + 6));
        }
        pagingSelect.append(" ) temp_ where rownumber_ ");
        // add the restriction to the outer select
        if (hasOffset)
        {
            pagingSelect.append("between ?+1 and ?");
        }
        else
        {
            pagingSelect.append("<= ?");
        }
        return pagingSelect.toString();
    }
    
    private String getRowNumber(String sql)
    {
        StringBuffer rownumber = new StringBuffer(50).append("rownumber() over(");
        int orderByIndex = sql.toLowerCase().indexOf("order by");
        int lastFromIndex = sql.toLowerCase().lastIndexOf(" from ");
        int lastWhereIndex = sql.toLowerCase().lastIndexOf(" where ");
        if (orderByIndex > 0 && orderByIndex > lastFromIndex && orderByIndex > lastWhereIndex && !hasDistinct(sql))
        {
            rownumber.append(sql.substring(orderByIndex));
        }
        rownumber.append(") as rownumber_,");
        return rownumber.toString();
    }
    
    private static boolean hasDistinct(String sql)
    {
        return sql.toLowerCase().indexOf("select distinct") >= 0;
    }
}
