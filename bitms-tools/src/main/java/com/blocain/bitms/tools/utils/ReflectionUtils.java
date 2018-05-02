package com.blocain.bitms.tools.utils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

/**
 * <p>File：ReflectionUtils.java </p>
 * <p>Title: ReflectionUtils </p>
 * <p>Description: ReflectionUtils </p>
 * <p>Copyright: Copyright (c) 2014 08/09/2015 18:16</p>
 * <p>Company: BloCain</p>
 *
 * @author playguy
 * @version 1.0
 */
public class ReflectionUtils
{
    // set方法前缀
    private static final String SETTER_PREFIX = "set";
    
    // get方法前缀
    private static final String GETTER_PREFIX = "get";
    
    // log4j
    private static final Logger logger        = Logger.getLogger(ReflectionUtils.class);
    
    // 私有构造器，防止类的实例化
    private ReflectionUtils()
    {
        super();
    }
    
    /**
     * SpringDataJPA或Hibernate查询返回结果集为对象数组时，可以通过该方法转化为对应的Object
     * @param object 指定字段查询时返回的Object[]
     * @param entity 预期的Entity对象
     * @return Object Object
     */
    public static Object getEntityFromResult(Object object, Class<?> entity)
    {
        Object model = null;
        if (null != object)
        {
            List<Object[]> list = Lists.newArrayList();
            list.add((Object[]) object);
            List<?> lList = getListFromResult(list, entity);
            if (null != lList && !lList.isEmpty())
            {
                model = lList.get(0);
            }
        }
        return model;
    }
    
    /**
     * SpringDataJPA或Hibernate查询返回结果集为对象数组时，可以通过该方法转化为对应的List<?>
     * 如：List<Object[]>转为化List<Userm>，前提：查询指定的参数列表在entity中有对应的构造函数
     * List<?> lList = Lists.newArrayList();
     * @param list SpringDataJPA或Hibernate查询返回的对象数组结果集
     * @param entity list中预期的entity对象，实际转化时将按该对象进行转化
     * @return List<?> List<?>
     */
    public static List<?> getListFromResult(List<Object[]> list, Class<?> entity)
    {
        List<Object> lList = null;
        if (null != list && !list.isEmpty())
        {
            lList = new ArrayList<Object>();
            for (Object[] object : list)
            {
                if (null != object)
                {
                    int objectLength = object.length;
                    if (objectLength > 1)
                    {
                        // 获取参数类型
                        // 必须这样获取各个参数的类型，如果有参数为null获取不到参数类型会报错（ConstructorUtils获取参数类型时没做null处理）！
                        Class<?> parameterTypes[] = new Class[objectLength];
                        for (int i = 0; i < objectLength; i++)
                        {
                            if (null != object[i]) parameterTypes[i] = object[i].getClass();
                        }
                        // 根据构造函数进行对象实例化
                        Object obj = null;
                        try
                        {
                            obj = ConstructorUtils.invokeExactConstructor(entity, object, parameterTypes);
                        }
                        catch (NoSuchMethodException e)
                        {
                            logger.error(e.getMessage());
                        }
                        catch (IllegalAccessException e)
                        {
                            logger.error(e.getMessage());
                        }
                        catch (InvocationTargetException e)
                        {
                            logger.error(e.getMessage());
                        }
                        catch (InstantiationException e)
                        {
                            logger.error(e.getMessage());
                        }
                        lList.add(obj);
                    }
                }
            }
        }
        return lList;
    }
    
    /**
     * 调用Getter方法，支持多级，如：对象名.对象名.方法
     * @param obj Object
     * @param propertyName 类成员属性名称
     * @return Object 调用get方法后返回的Object
     */
    public static Object invokeGetter(Object obj, String propertyName)
    {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, "."))
        {
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
        }
        return object;
    }
    
    /**
     * 调用Setter方法, 仅匹配方法名，支持多级，如：对象名.对象名.方法
     * @param obj 将要执行set方法的对象
     * @param propertyName 类成员属性名称
     * @param value set方法参数值
     */
    public static void invokeSetter(Object obj, String propertyName, Object value)
    {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++)
        {
            if (i < names.length - 1)
            {
                String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
            }
            else
            {
                String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[]{value});
            }
        }
    }
    
    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数
     * @param obj 将要被读取属性的对象
     * @param fieldName 将要被读取的对象属性名称
     * @return Object 对象属性值
     */
    public static Object getFieldValue(final Object obj, final String fieldName)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) { throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]"); }
        Object result = null;
        try
        {
            result = field.get(obj);
        }
        catch (IllegalAccessException e)
        {
            logger.error(e.getMessage());
        }
        return result;
    }
    
    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数
     * @param obj 将要被设置属性值的对象
     * @param fieldName 将要被设置值的属性名称
     * @param value 将要被设置的属性值
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) { throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]"); }
        try
        {
            field.set(obj, value);
        }
        catch (IllegalAccessException e)
        {
            logger.error(e.getMessage());
        }
    }
    
    /**
     * 直接调用对象方法, 无视private/protected修饰符
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用
     * 同时匹配方法名+参数类型
     * @param obj 将要被执的对象
     * @param methodName 将要被执行的方法
     * @param parameterTypes 参数类型
     * @param args 参数
     * @return Object 方法返回值
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args)
    {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) { throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]"); }
        try
        {
            return method.invoke(obj, args);
        }
        catch (Exception e)
        {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }
    
    /**
     * 根据方法名称及方法参数调用对象方法, 无视private/protected修饰符
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名称，如果有多个同名函数调用第一个。
     * @param obj 将要调用的对象
     * @param methodName 将要调用的对象方法
     * @param args 方法参数
     * @return Object 方法执行后返回的对象
     */
    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args)
    {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) { throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]"); }
        try
        {
            return method.invoke(obj, args);
        }
        catch (Exception e)
        {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }
    
    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * @param obj 将要被访问的对象
     * @param fieldName 对象的成员属性名称
     * @return Field Field
     */
    public static Field getAccessibleField(final Object obj, final String fieldName)
    {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            }
            catch (NoSuchFieldException e)
            {// NOSONAR
             // Field不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }
    
    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问
     * 如向上转型到Object仍无法找到, 返回null.匹配函数名+参数类型
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     * @param obj Object
     * @param methodName 方法名称
     * @param parameterTypes Class<?>参数类型
     * @return Method Method
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?> ... parameterTypes)
    {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            try
            {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (NoSuchMethodException e)
            {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }
    
    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.只匹配函数名。
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     * @param obj 将要被访问的对象
     * @param methodName 将要被访问的方法名称
     * @return Method Method
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName)
    {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals(methodName))
                {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }
    
    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager的抗议
     * @param field Field
     */
    public static void makeAccessible(Field field)
    {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers()))
                && !field.isAccessible())
        {
            field.setAccessible(true);
        }
    }
    
    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    public static <T> Class<?> getClassGenricType(final Class<?> clazz)
    {
        return getClassGenricType(clazz, 0);
    }
    
    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     *
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class<?> getClassGenricType(final Class<?> clazz, final int index)
    {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType))
        {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0)
        {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class))
        {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class<?>) params[index];
    }
    
    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抗议
     * @param method Method
     */
    public static void makeAccessible(Method method)
    {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible())
        {
            method.setAccessible(true);
        }
    }
    
    /**
     * 将反射时的checked exception转换为unchecked exception.
     * @param e Exception
     * @return RuntimeException RuntimeException
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e)
    {
        RuntimeException re = null;
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException)
        {
            re = new IllegalArgumentException(e);
        }
        else if (e instanceof InvocationTargetException)
        {
            re = new RuntimeException(((InvocationTargetException) e).getTargetException());
        }
        else if (e instanceof RuntimeException)
        {
            re = (RuntimeException) e;
        }
        else
        {
            re = new RuntimeException("Unexpected Checked Exception.", e);
        }
        return re;
    }
}
