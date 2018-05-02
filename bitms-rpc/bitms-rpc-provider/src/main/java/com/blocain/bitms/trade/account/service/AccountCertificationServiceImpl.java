/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.tools.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.mapper.AccountCertificationMapper;

/**
 * 用户实名认证信息 服务实现类
 * <p>File：AccountCertificationServiceImpl.java </p>
 * <p>Title: AccountCertificationServiceImpl </p>
 * <p>Description:AccountCertificationServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountCertificationServiceImpl extends GenericServiceImpl<AccountCertification> implements AccountCertificationService
{
    protected AccountCertificationMapper accountCertificationMapper;
    
    @Autowired
    public AccountCertificationServiceImpl(AccountCertificationMapper accountCertificationMapper)
    {
        super(accountCertificationMapper);
        this.accountCertificationMapper = accountCertificationMapper;
    }
    
    @Override
    public AccountCertification findByAccountId(Long accountId) throws BusinessException
    {
        return accountCertificationMapper.findByAccountId(accountId);
    }
    
    @Override
    public int save(AccountCertification entity) throws BusinessException
    {
        if (null != entity.getAttachment())
        {// 将附件信息转成JSON写入到数据库
            entity.setAttachments(JSON.toJSONString(entity.getAttachment()));
        }
        return super.save(entity);
    }
}
