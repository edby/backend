/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirmERC20;

/**
 * ERC20区块交易确认表 持久层接口
 * <p>File：BlockTransConfirmERC20Mapper.java </p>
 * <p>Title: BlockTransConfirmERC20Mapper </p>
 * <p>Description:BlockTransConfirmERC20Mapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface BlockTransConfirmERC20Mapper extends GenericMapper<BlockTransConfirmERC20>
{
    List<BlockTransConfirmERC20> findConfirmERC20ChargeList(BlockTransConfirmERC20 entity);
    
    BlockTransConfirmERC20 selectByIdForUpdate(Long id);
    
    /**
     *  查找没标记过的充值来源地址
     * @param accountId
     * @return
     */
    List<BlockTransConfirmERC20> findUnsignList(Long accountId);
    
    /**
     * 按ID获取 并加行锁
     * @param ids
     * @return
     */
    List<BlockTransConfirmERC20> getByIdsForUpdate(@Param("ids") Long[] ids);
}
