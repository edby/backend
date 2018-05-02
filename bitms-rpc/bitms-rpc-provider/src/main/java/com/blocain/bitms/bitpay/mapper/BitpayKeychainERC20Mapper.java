/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.mapper;

import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

/**
 * BitpayKeychainERC20 持久层接口
 * <p>File：BitpayKeychainERC20Mapper.java </p>
 * <p>Title: BitpayKeychainERC20Mapper </p>
 * <p>Description:BitpayKeychainERC20Mapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface BitpayKeychainERC20Mapper extends GenericMapper<BitpayKeychainERC20>
{

    List<BitpayKeychainERC20> findJoinList(BitpayKeychainERC20 entity);
}
