/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.block.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;

/**
 * BlockInfoERC20 服务接口
 * <p>File：BlockInfoERC20Service.java </p>
 * <p>Title: BlockInfoERC20Service </p>
 * <p>Description:BlockInfoERC20Service </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface BlockInfoERC20Service extends GenericService<BlockInfoERC20>
{
    /**
     * 
     * @return
     */
    BlockInfoERC20 getLastUnScanTransBlockNumber();
    
    /**
     * 获取height字段最大值记录
     * @return
     */
    BlockInfoERC20 getMaxHeightBlockInfo();
}
