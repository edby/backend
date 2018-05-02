package com.blocain.bitms.boss.system.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.system.entity.RoleInfo;
import com.blocain.bitms.orm.db.MultipleDataSource;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.google.common.collect.Lists;

/**
 * Created by Playguy on 2017/6/23.
 */
public class RoleInfoServiceImplTest extends AbstractBaseTest
{
    @Autowired
    RoleInfoService roleInfoService;
//
//    @Test
//    public void insertBatch() throws Exception
//    {
//        RoleInfo role;
//        List<RoleInfo> datas = Lists.newArrayList();
//        for (int i = 0; i < 100; i++)
//        {
//            role = new RoleInfo();
//            role.setId(SerialnoUtils.buildPrimaryKey());
//            role.setRoleCode("roleCode" + i);
//            role.setRoleName("roleName" + i);
//            datas.add(role);
//        }
//        roleInfoService.insertBatch(datas);
//    }
//
//    @Test
//    public void multipleSourceTestBatch()
//    {
//        RoleInfo role;
//        List<RoleInfo> datas = Lists.newArrayList();
//        for (int i = 100; i < 200; i++)
//        {
//            role = new RoleInfo();
//            role.setId(SerialnoUtils.buildPrimaryKey());
//            role.setRoleCode("roleCode" + i);
//            role.setRoleName("roleName" + i);
//            datas.add(role);
//        }
//        MultipleDataSource.setDataSourceKey("testDataSource");
//        roleInfoService.insertBatch(datas, false);
//        datas = Lists.newArrayList();
//        for (int i = 200; i < 300; i++)
//        {
//            role = new RoleInfo();
//            role.setId(SerialnoUtils.buildPrimaryKey());
//            role.setRoleCode("roleCode" + i);
//            role.setRoleName("roleName" + i);
//            datas.add(role);
//        }
//        MultipleDataSource.setDataSourceKey("masterDataSource");
//        roleInfoService.insertBatch(datas, true);
//    }
}