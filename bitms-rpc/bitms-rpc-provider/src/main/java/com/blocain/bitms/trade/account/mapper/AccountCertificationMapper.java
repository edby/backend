/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.account.entity.AccountCertification;

/**
 * 用户实名认证信息 持久层接口
 * <p>File：AccountCertificationMapper.java </p>
 * <p>Title: AccountCertificationMapper </p>
 * <p>Description:AccountCertificationMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountCertificationMapper extends GenericMapper<AccountCertification>
{
    /**
     * 根据用户编号取认证信息
     * @param accountId
     * @return {@link AccountCertification}
     */
    AccountCertification findByAccountId(Long accountId);
}
