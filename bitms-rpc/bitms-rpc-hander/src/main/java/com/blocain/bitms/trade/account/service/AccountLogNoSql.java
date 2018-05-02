/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.orm.core.GenericNoSql;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.AccountLog;

import java.util.List;

/**
 * 账户日志表 服务接口
 * <p>File：AccountLogService.java </p>
 * <p>Title: AccountLogService </p>
 * <p>Description:AccountLogService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountLogNoSql extends GenericNoSql<AccountLog>
{
    /**
     * 插入
     * @param entity
     * @throws BusinessException
     */
    void insert(AccountLog entity);
    
    /**
     * 查询最近十条登陆日志
     * @param accountId
     * @return {@link List}
     * @throws BusinessException
     */
    List<AccountLog> findLastTenLoginLogs(Long accountId) throws BusinessException;
    
    /**
     * 查询最近十条安全设置日志
     * @param accountId
     * @return {@link List}
     * @throws BusinessException
     */
    List<AccountLog> findLastTenSettingLogs(Long accountId) throws BusinessException;
}
