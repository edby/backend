/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;

/**
 * 账户收款地址表 服务接口
 * <p>File：AccountCollectAddrService.java </p>
 * <p>Title: AccountCollectAddrService </p>
 * <p>Description:AccountCollectAddrService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface AccountCollectAddrService extends GenericService<AccountCollectAddr>
{
	
    AccountCollectAddr findAccountCollectAddr(AccountCollectAddr accountCollectAddr);
    
    /**
     * 保存提币地址并返回 ID
     * @param accountCollectAddr
     * @return
     */
    Long insertAccountCollectAddr(AccountCollectAddr accountCollectAddr);
}
