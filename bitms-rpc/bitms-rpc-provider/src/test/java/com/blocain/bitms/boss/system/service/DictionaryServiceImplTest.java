package com.blocain.bitms.boss.system.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.common.service.DictionaryService;

/**
 * Created by Playguy on 2017/6/23.
 */
public class DictionaryServiceImplTest extends AbstractBaseTest
{
    @Autowired
     DictionaryService dictionaryService;
    
//    @Test
//    public void insertBatch() throws Exception
//    {
//        Dictionary dict;
//        Long current = CalendarUtils.getCurrentLong();
//        List<Dictionary> datas = Lists.newArrayList();
//        for (int i = 0; i < 10; i++)
//        {
//            dict = new Dictionary();
//            dict.setId(SerialnoUtils.buildPrimaryKey());
//            dict.setCode("code" + i);
//            dict.setName("name" + i);
//            dict.setActive(true);
//            dict.setCreateBy(OnLineUserUtils.getDevId());
//            dict.setCreateDate(current);
//            datas.add(dict);
//        }
//        dictionaryService.insertBatch(datas);
//    }
}