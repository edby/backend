package com.blocain.bitms.orm.interceptor;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.blocain.bitms.orm.bean.SQLHelper;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * <p>File：PaginationInterceptor.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-17 下午4:02:10</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Intercepts({@Signature(args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}, method = "query", type = Executor.class)})
public class PaginationInterceptor extends BaseInterceptor
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Object parameterObject = boundSql.getParameterObject();
        if (parameterObject == null)
        {
            return invocation.proceed();
        }
        else
        {
            // 获取分页参数对象
            Pagination page = convertParameter(parameterObject);
            // 如果设置了分页对象，则进行分页
            if (page != null && page.getRows() != -1)
            {
                if (StringUtils.isBlank(boundSql.getSql())) return null;
                String originalSql = boundSql.getSql().trim();
                // 得到总记录数
                page.setTotalRows(SQLHelper.getCount(originalSql, null, mappedStatement, parameterObject, boundSql));
                // 分页查询 本地化对象 修改数据库注意修改实现
                String pageSql = SQLHelper.generatePageSql(originalSql, page, dialect);
                invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
                BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
                MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
                invocation.getArgs()[0] = newMs;
            }
        }
        return invocation.proceed();
    }
    
    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties)
    {
        super.initProperties(properties);
    }
    
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource)
    {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null)
        {
            for (String keyProperty : ms.getKeyProperties())
            {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.useCache(ms.isUseCache());
        builder.cache(ms.getCache());
        return builder.build();
    }
    
    public static class BoundSqlSqlSource implements SqlSource
    {
        BoundSql boundSql;
        
        public BoundSqlSqlSource(BoundSql boundSql)
        {
            this.boundSql = boundSql;
        }
        
        public BoundSql getBoundSql(Object parameterObject)
        {
            return boundSql;
        }
    }
}