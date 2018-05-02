/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.mapper;

import com.blocain.bitms.tools.exception.BusinessException;
import org.apache.ibatis.annotations.Param;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.account.entity.Account;

import java.util.List;

/**
 * 账户表 持久层接口
 * <p>File：AccountDao.java </p>
 * <p>Title: AccountDao </p>
 * <p>Description:AccountDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountMapper extends GenericMapper<Account>
{
    /**
     * 根据帐户名取数据
     * @param accountName
     * @return
     */
    Account findByName(String accountName);
    
    /**
     * 根据用户UNID查
     * @param unid
     * @return
     */
    Account findByUnid(Long unid);
    
    /**
     * 根据邮件地址和手机短信取用户信息
     * @param email
     * @param mobNo
     * @return
     */
    Account findByEmailAndMob(@Param("email") String email, @Param("mobNo") String mobNo);
    
    /**
     * 根据帐户名取数据
     * @param accountName
     * @param status
     * @return
     */
    Account findByNameAndStatus(@Param("accountName") String accountName, @Param("status") Integer status);
    
    /**
     * 取有解冻时间的冻结用户
     * @return
     */
    List<Account> findThawUserList();
    
    /**
     * 取最大的UNID
     * @return
     */
    Long getMaxUNID();
    
    /**
     * 根据手机号查询
     * @param phone
     * @return
     */
    Account findByPhone(String phone);

    /**
     * 根据账户ID列表获取相关账户列表
     * @param accountIdList
     * @return
     * @throws BusinessException
     */
    List<Account> findListByAcctIds(List<Long> accountIdList) throws BusinessException;
}
