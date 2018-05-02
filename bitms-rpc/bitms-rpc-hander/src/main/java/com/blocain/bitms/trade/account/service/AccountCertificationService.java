/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.AccountCertification;

/**
 * 用户实名认证信息 服务接口
 * <p>File：AccountCertificationService.java </p>
 * <p>Title: AccountCertificationService </p>
 * <p>Description:AccountCertificationService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountCertificationService extends GenericService<AccountCertification>
{
    /**
     * 根据用户编号取认证信息
     * @param accountId
     * @return {@link AccountCertification}
     * @throws BusinessException
     */
    AccountCertification findByAccountId(Long accountId) throws BusinessException;
}
