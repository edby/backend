package com.blocain.bitms.orm.interceptor;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.blocain.bitms.orm.core.SignableEntity;

/**
 * <p>File：UpdateInterceptor.java</p>
 * <p>Title: UpdateInterceptor </p>
 * <p>Description: 拦截需要签名的对象，实现自动签名</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-17 下午4:02:10</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class UpdateInterceptor extends BaseInterceptor
{
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        Object parameter = invocation.getArgs()[1];
        if (parameter instanceof SignableEntity)
        {
            ((SignableEntity) parameter).doSign();
        }
        if (parameter instanceof Map)
        {// 检查集合参数
            Map map = (Map) parameter;
            if (map.containsKey("list"))
            {
                List<Object> data = (List<Object>) map.get("list");
                for (Object o : data)
                {
                    if (o instanceof SignableEntity)
                    {// 如果对象是需要签名的
                        ((SignableEntity) o).doSign();
                    }
                    else
                    {// 如果对象不是签名对象直接终止
                        break;
                    }
                }
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
}