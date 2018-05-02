package com.blocain.bitms.orm.db;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.blocain.bitms.orm.annotation.SlaveDataSource;

/**
 * 动态数据源切入
 * <p>File: DataSourceAspect.java</p>
 * <p>Title: DataSourceAspect</p>
 * <p>Description: DataSourceAspect</p>
 * <p>Copyright: Copyright (c) 16/3/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DataSourceAspect
{
    /**
     * 仅切入到带有@DataSource注解的方法体中
     * 默认不带此注解的方法走主库；带有@DataSource的方法根据slave状态区分！
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable
    {
        Object retVal = null;
        boolean selectedDataSource = false;
        Object target = pjp.getTarget();
        try
        {
            String methodName = pjp.getSignature().getName();
            MethodSignature signature = ((MethodSignature) pjp.getSignature());
            Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
            Class<?>[] classz = target.getClass().getInterfaces();
            Method m = classz[0].getMethod(methodName, parameterTypes);
            SlaveDataSource annotation = m.getAnnotation(SlaveDataSource.class);
            if (m != null && null != annotation)
            {
                if ((null != annotation))
                {
                    selectedDataSource = true;
                    if (annotation.slave())
                    {
                        DataSourceHolder.useSlave();
                    }
                }
            }
            retVal = pjp.proceed();
        }
        catch (Throwable e)
        {
            throw e;
        }
        finally
        {
            if (selectedDataSource)
            {
                DataSourceHolder.reset();
            }
        }
        return retVal;
    }
}
