/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.entity.UserRole;
import com.blocain.bitms.boss.system.mapper.UserInfoMapper;
import com.blocain.bitms.boss.system.mapper.UserRoleMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;

/**
 * 用户基础信息表 服务实现类
 * <p>File：UserInfo.java </p>
 * <p>Title: UserInfo </p>
 * <p>Description:UserInfo </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class UserInfoServiceImpl extends GenericServiceImpl<UserInfo> implements UserInfoService
{
    private UserInfoMapper userInfoMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    public UserInfoServiceImpl(UserInfoMapper userInfoMapper)
    {
        super(userInfoMapper);
        this.userInfoMapper = userInfoMapper;
    }
    
    @Override
    public int save(UserInfo entity) throws BusinessException
    {
        int flag;
        this.beanValidator(entity);
        if (null == entity.getId())
        {
            if (StringUtils.isBlank(entity.getPassWord()))
            {// 如果用户没有设置密码，初始化一个
                entity.setPassWord(BitmsConst.DEFAULT_USER_PASSWORD);
            }
            entity.setId(SerialnoUtils.buildPrimaryKey());
            entity.setPassWord(EncryptUtils.entryptPassword(entity.getPassWord()));
            flag = userInfoMapper.insert(entity);
            saveUserRole(entity);
        }
        else
        {
            UserInfo dbInfo = userInfoMapper.selectByPrimaryKey(entity.getId());
            if (StringUtils.isNotBlank(dbInfo.getAuthKey()))
            {//防止空指针
                entity.setAuthKey(dbInfo.getAuthKey());
            }
            if (StringUtils.isNotBlank(entity.getPassWord()))
            {// 修改密码
                entity.setPassWord(EncryptUtils.entryptPassword(entity.getPassWord()));
            }
            else
            {// 保留原始密码
                entity.setPassWord(dbInfo.getPassWord());
            }
            flag = userInfoMapper.updateByPrimaryKey(entity);
            saveUserRole(entity);
        }
        return flag;
    }
    
    /**
     * 保存用户角色
     * @param info
     */
    private void saveUserRole(UserInfo info)
    {
        userRoleMapper.removeByUser(info.getId());
        if (StringUtils.isNotBlank(info.getRoleIds()))
        {
            String[] roleIds = info.getRoleIds().split(",");
            List<UserRole> userRoleList = Lists.newArrayList();
            UserRole userRole;
            for (String roleId : roleIds)
            {
                userRole = new UserRole(Long.parseLong(roleId), info.getId());
                userRole.setId(SerialnoUtils.buildPrimaryKey());
                userRoleList.add(userRole);
            }
            userRoleMapper.insertBatch(userRoleList);
        }
    }
    
    @Override
    public UserInfo findByUserName(String userName)
    {
        return userInfoMapper.findByUserName(userName);
    }
}
