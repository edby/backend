/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;

import java.util.List;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirm;

/**
 * 区块交易确认表 持久层接口
 * <p>File：BlockTransConfirmDao.java </p>
 * <p>Title: BlockTransConfirmDao </p>
 * <p>Description:BlockTransConfirmDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface BlockTransConfirmMapper extends GenericMapper<BlockTransConfirm>
{
    /**
     * 获取交易列表
     * @param blockTransConfirm
     * @return
     */
    List<BlockTransConfirm> findWalletTransList(BlockTransConfirm blockTransConfirm);

    /**
     * 查询充值记录
     * @param blockTransConfirm
     * @return
     */
    List<BlockTransConfirm> findChargeList(BlockTransConfirm blockTransConfirm);
    /**
     * 区块交易确认记录-充值
     * @param entity
     * @return
     */
    List<BlockTransConfirm> findConfirmChargeList(BlockTransConfirm entity);
    
    /**
     * 区块交易确认记录-提现
     * @param entity
     * @return
     */
    List<BlockTransConfirm> findConfirmRaiseList(BlockTransConfirm entity);

}
