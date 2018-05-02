package com.blocain.bitms.boss.system.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.basic.service.AbstractBaseTest;

/**
 * UserInfoServiceImplTest Introduce
 * <p>File：UserInfoServiceImplTest.java </p>
 * <p>Title: UserInfoServiceImplTest </p>
 * <p>Description:UserInfoServiceImplTest </p>
 * <p>Copyright: Copyright (c) 17/6/22</p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class UserInfoServiceImplTest extends AbstractBaseTest
{
    @Autowired
     UserInfoService userInfoService;
    
//    @Test
//    public void insertBatch() throws Exception
//    {
//        UserInfo info;
//        List<UserInfo> data = Lists.newArrayList();
//        for (int i = 0; i < 100; i++)
//        {
//            info = new UserInfo();
//            info.setId(SerialnoUtils.buildPrimaryKey());
//            info.setOrgId("00001L");
//            info.setUserName("userName" + i);
//            info.setTrueName("trueName" + i);
//            info.setGender(false);
//            info.setActive(true);
//            info.setPassWord(EncryptUtils.entryptPassword("123456"));
//            data.add(info);
//        }
//        userInfoService.insertBatch(data);
//    }
}