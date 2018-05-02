/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.util.List;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirmERC20;

/**
 * ERC20区块交易确认表 服务接口
 * <p>File：BlockTransConfirmERC20Service.java </p>
 * <p>Title: BlockTransConfirmERC20Service </p>
 * <p>Description:BlockTransConfirmERC20Service </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface BlockTransConfirmERC20Service extends GenericService<BlockTransConfirmERC20>{

    PaginateResult<BlockTransConfirmERC20> findConfirmERC20ChargeList(Pagination pagin, BlockTransConfirmERC20 entity);

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
    List<BlockTransConfirmERC20> getByIdsForUpdate(Long ... ids);

}
