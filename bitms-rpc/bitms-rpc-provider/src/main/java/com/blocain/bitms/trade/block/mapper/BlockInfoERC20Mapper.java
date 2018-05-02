/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.block.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;

/**
 * BlockInfoERC20 持久层接口
 * <p>File：BlockInfoERC20Mapper.java </p>
 * <p>Title: BlockInfoERC20Mapper </p>
 * <p>Description:BlockInfoERC20Mapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface BlockInfoERC20Mapper extends GenericMapper<BlockInfoERC20>
{
    BlockInfoERC20 findLastUnScanTransBlockNumber();

    BlockInfoERC20 findMaxHeight();
}
