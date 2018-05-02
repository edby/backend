package com.blocain.bitms.tools.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomProbabilityUtils
{
    // String 可以为任意类型 也可以自定义类型
    static Map<String, Integer> keyChanceMap = new HashMap<String, Integer>();
    
    /**
     * 输入小数 如0.01 获取概率
     * @param percent
     * @return
     */
    public static int getProbability(String percent)
    {
        int fenzi = Decimal2fractionUtils.getFraction(percent)[0];
        int fenmu = Decimal2fractionUtils.getFraction(percent)[1];
        keyChanceMap.put("0", fenmu - fenzi);
        keyChanceMap.put("1", fenzi);
        Map<String, Integer> count = new HashMap<String, Integer>();
        List<String> list = new ArrayList<>();
        String item = null;
        for (int i = 0; i < fenmu; i++)
        {
            item = chanceSelect(keyChanceMap);
            list.add(item);
            if (count.containsKey(item))
            {
                count.put(item, count.get(item) + 1);
            }
            else
            {
                count.put(item, 1);
            }
        }
        for (String id : count.keySet())
        {
            System.out.println(id + "\t出现了 " + count.get(id) + " 次");
        }
        Random rand = new Random();
        int num = rand.nextInt(fenmu);
        System.out.print("最终选择的随机数为：" + list.get(num));
        if (list.get(num).equals("1")) { return 1; }
        return 0;
    }
    
    public static String chanceSelect(Map<String, Integer> keyChanceMap)
    {
        if (keyChanceMap == null || keyChanceMap.size() == 0) return null;
        Integer sum = 0;
        for (Integer value : keyChanceMap.values())
        {
            sum += value;
        }
        // 从1开始
        Integer rand = new Random().nextInt(sum) + 1;
        for (Map.Entry<String, Integer> entry : keyChanceMap.entrySet())
        {
            rand -= entry.getValue();
            // 选中
            if (rand <= 0)
            {
                String item = entry.getKey();
                return item;
            }
        }
        return null;
    }
}