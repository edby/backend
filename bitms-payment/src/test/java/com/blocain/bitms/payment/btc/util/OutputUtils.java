package com.blocain.bitms.payment.btc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import com.blocain.bitms.payment.btc.core.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OutputUtils
{
    private static final int LINE_SEPARATOR_LENGTH = 89;
    
    public static void printResult(String methodName, String[] paramNames, Object[] paramValues, Object result)
    {
        List<Object> printables = new ArrayList<Object>();
        printables.add(methodName);
        if (!(paramNames == null || paramValues == null))
        {
            printables.addAll(CollectionUtils.mergeInterlaced(Arrays.asList(paramNames), Arrays.asList(paramValues)));
        }
        printables.add(result);
        System.out.println(JSON.toJSONString(printables));
    }
    
    public static void printSeparator()
    {
        printSeparator(LINE_SEPARATOR_LENGTH);
    }
    
    public static void printSeparator(int length)
    {
        System.out.println(StringUtils.repeat('-', length));
    }
}