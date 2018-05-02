/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.block.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;
import com.blocain.bitms.trade.block.mapper.BlockInfoERC20Mapper;

/**
 * BlockInfoERC20 服务实现类
 * <p>File：BlockInfoERC20ServiceImpl.java </p>
 * <p>Title: BlockInfoERC20ServiceImpl </p>
 * <p>Description:BlockInfoERC20ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class BlockInfoERC20ServiceImpl extends GenericServiceImpl<BlockInfoERC20> implements BlockInfoERC20Service {

    protected BlockInfoERC20Mapper blockInfoERC20Mapper;

    @Autowired
    public BlockInfoERC20ServiceImpl(BlockInfoERC20Mapper blockInfoERC20Mapper)
    {
        super(blockInfoERC20Mapper);
        this.blockInfoERC20Mapper = blockInfoERC20Mapper;
    }

    @Override
    public BlockInfoERC20 getLastUnScanTransBlockNumber() {
        return blockInfoERC20Mapper.findLastUnScanTransBlockNumber();
    }

    @Override
    public BlockInfoERC20 getMaxHeightBlockInfo() {
        return blockInfoERC20Mapper.findMaxHeight();
    }
}
