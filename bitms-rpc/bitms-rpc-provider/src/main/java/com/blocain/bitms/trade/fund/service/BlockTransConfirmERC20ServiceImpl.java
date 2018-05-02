/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirm;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirmERC20;
import com.blocain.bitms.trade.fund.mapper.BlockTransConfirmERC20Mapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * ERC20区块交易确认表 服务实现类
 * <p>File：BlockTransConfirmERC20ServiceImpl.java </p>
 * <p>Title: BlockTransConfirmERC20ServiceImpl </p>
 * <p>Description:BlockTransConfirmERC20ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class BlockTransConfirmERC20ServiceImpl extends GenericServiceImpl<BlockTransConfirmERC20> implements BlockTransConfirmERC20Service
{

    protected BlockTransConfirmERC20Mapper blockTransConfirmERC20Mapper;

    @Autowired
    public BlockTransConfirmERC20ServiceImpl(BlockTransConfirmERC20Mapper blockTransConfirmERC20Mapper)
    {
        super(blockTransConfirmERC20Mapper);
        this.blockTransConfirmERC20Mapper = blockTransConfirmERC20Mapper;
    }

    @Override
    public PaginateResult<BlockTransConfirmERC20> findConfirmERC20ChargeList(Pagination pagin, BlockTransConfirmERC20 entity) {
        entity.setPagin(pagin);
        List<BlockTransConfirmERC20> list = blockTransConfirmERC20Mapper.findConfirmERC20ChargeList(entity);
        return new PaginateResult<>(pagin, list);
    }

    @Override
    public BlockTransConfirmERC20 selectByIdForUpdate(Long id)
    {
        return blockTransConfirmERC20Mapper.selectByIdForUpdate(id);
    }

    @Override public List<BlockTransConfirmERC20> findUnsignList(Long accountId)
    {
        return blockTransConfirmERC20Mapper.findUnsignList(accountId);
    }

    @Override public List<BlockTransConfirmERC20> getByIdsForUpdate(Long ... ids)
    {
        List<Long> filter = Lists.newArrayList(ids);
        return blockTransConfirmERC20Mapper.getByIdsForUpdate(filter.toArray(new Long[]{}));
    }
}
