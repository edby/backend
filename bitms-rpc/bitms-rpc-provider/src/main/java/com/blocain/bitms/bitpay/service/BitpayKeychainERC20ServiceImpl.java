/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.bitpay.mapper.BitpayKeychainERC20Mapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;

import java.util.List;

/**
 * BitpayKeychainERC20 服务实现类
 * <p>File：BitpayKeychainERC20ServiceImpl.java </p>
 * <p>Title: BitpayKeychainERC20ServiceImpl </p>
 * <p>Description:BitpayKeychainERC20ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class BitpayKeychainERC20ServiceImpl extends GenericServiceImpl<BitpayKeychainERC20> implements BitpayKeychainERC20Service
{
    protected BitpayKeychainERC20Mapper bitpayKeychainERC20Mapper;
    
    @Autowired
    public BitpayKeychainERC20ServiceImpl(BitpayKeychainERC20Mapper bitpayKeychainERC20Mapper)
    {
        super(bitpayKeychainERC20Mapper);
        this.bitpayKeychainERC20Mapper = bitpayKeychainERC20Mapper;
    }
    
    @Override
    public PaginateResult<BitpayKeychainERC20> findJoinList(Pagination pagin, BitpayKeychainERC20 entity)
    {
        entity.setPagin(pagin);
        List<BitpayKeychainERC20> list = bitpayKeychainERC20Mapper.findJoinList(entity);
        return new PaginateResult<>(pagin, list);
    }
}
