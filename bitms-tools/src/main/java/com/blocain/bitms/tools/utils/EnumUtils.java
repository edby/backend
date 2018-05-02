package com.blocain.bitms.tools.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举类辅助工具
 * <p>File：EnumUtils.java</p>
 * <p>Title: EnumUtils</p>
 * <p>Description: EnumUtils</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public abstract class EnumUtils
{
    /**
     * 把enum封成list，便于前端调用
     *
     * @param clazz
     * @param <T>
     * @return 例如：
     * model.addAttribute("cateList", Enums.toList(Enums.BrandCate.class));
     * <select cname="type" >
     * <c:forEach var="type" items="${cateList}">
     * <option value="${type.key}">${type.cname}</option>
     * </c:forEach>
     * </select>
     */
    public static <T extends Enum> List<T> toList(Class<T> clazz)
    {
        return Arrays.asList(clazz.getEnumConstants());
    }
    
    /**
     * 转为MAP
     * @param clazz enum类
     * @param <T>
     * @param  methodName enum类的指定的方法(getKey),当methodName=null时,默认为Enum.name方法
     * model.addAttribute("tradeStatusMap", Enums.toMap(Enums.TradeStatus.class,"getValue"));
     * 例如: ${tradeStatusMap[key].desc}  ,key为入参methodName的值
     * @return Map<Object, T>
     */
    public static <T extends Enum> Map<Object, T> toMap(Class<T> clazz, String methodName)
    {
        return enumConstantDirectory(clazz, methodName);
    }
    
    private static <T extends Enum> Map<Object, T> enumConstantDirectory(Class<T> clazz, String methodName)
    {
        Map<Object, T> directory = null;
        if (directory == null)
        {
            T[] universe = getEnumConstantsShared(clazz);
            if (universe == null) throw new IllegalArgumentException(clazz.getName() + " is not an enum type");
            Map<Object, T> m = new HashMap<>(2 * universe.length);
            for (T constant : universe)
                try
                {
                    Object key = null;
                    if (methodName != null && methodName.trim().length() > 0)
                    {
                        Method method = constant.getClass().getMethod(methodName);
                        key = method.invoke(constant);
                    }
                    else
                    {
                        key = (constant).name(); // 默认KEY为ENUM的NAME
                    }
                    m.put(key, constant);
                }
                catch (NoSuchMethodException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            directory = m;
        }
        return directory;
    }
    
    @SuppressWarnings("unchecked")
    private static <T extends Enum> T[] getEnumConstantsShared(Class<T> clazz)
    {
        T[] enumConstants = null;
        if (enumConstants == null)
        {
            if (!clazz.isEnum()) return null;
            try
            {
                final Method values = clazz.getMethod("values");
                java.security.AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                    values.setAccessible(true);
                    return null;
                });
                T[] temporaryConstants = (T[]) values.invoke(null);
                enumConstants = temporaryConstants;
            }
            catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex)
            {
                return null;
            }
        }
        return enumConstants;
    }
}
