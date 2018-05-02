/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service;

import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * BitpayKeychainERC20 服务接口
 * <p>File：BitpayKeychainERC20Service.java </p>
 * <p>Title: BitpayKeychainERC20Service </p>
 * <p>Description:BitpayKeychainERC20Service </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface BitpayKeychainERC20Service extends GenericService<BitpayKeychainERC20>{

    PaginateResult<BitpayKeychainERC20> findJoinList(Pagination pagin, BitpayKeychainERC20 entity);
}
