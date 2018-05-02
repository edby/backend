/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirm;

import java.util.List;
import java.util.Map;

/**
 * 区块交易确认表 服务接口
 * <p>File：BlockTransConfirmService.java </p>
 * <p>Title: BlockTransConfirmService </p>
 * <p>Description:BlockTransConfirmService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface BlockTransConfirmService extends GenericService<BlockTransConfirm>
{
    /**
     * 创建钱包交易记录
     * @param walletId  钱包ID
     * @param transId   交易ID
     * @author 施建波  2017年7月10日 下午1:26:32
     */
    void createWalletTransRecord(String walletId, String transId) throws BusinessException;

    /**
     * 创建钱包交易记录
     * @param transId   交易ID
     * @author 施建波  2017年7月10日 下午1:26:32
     */
    void createBtcWalletTransRecord(String transId) throws BusinessException;
    
    /**
     * 获取交易列表
     * @param blockTransConfirm     交易确认实体
     * @return
     * @author 施建波  2017年7月10日 下午1:53:33
     */
    List<BlockTransConfirm> findWalletTransList(BlockTransConfirm blockTransConfirm);
    
    /**
     * 根据第三方平台查询，修改钱包交易状态
     * @param blockTransList    区块交易确认列表
     * @author 施建波  2017年7月11日 上午10:09:17
     */
    void modifyWalletTransStatus(List<BlockTransConfirm> blockTransList) throws BusinessException;
    
    /**
     * 获取未确认区块集合，按交易ID+钱包地址为KEY
     * @return
     * @author 施建波  2017年7月11日 下午3:19:57
     */
    Map<String, List<BlockTransConfirm>> findWalletTransUnconfirmMap();
    
    /**
     * 根据bitgo、btc、blockmeta三方接口查询区块交易确认结果
     * 
     * @author 施建波  2017年7月20日 上午8:55:31
     */
    void transExternalQuery();
    
    /**
     * 查询充值记录
     * @param pagin
     * @param entity
     * @return
     */
    PaginateResult<BlockTransConfirm> findChargeList(Pagination pagin, BlockTransConfirm entity);
    
    /**
     * 区块交易确认记录-充值
     * @param pagin
     * @param entity
     * @return
     */
    PaginateResult<BlockTransConfirm> findConfirmChargeList(Pagination pagin, BlockTransConfirm entity);
    
    /**
     * 区块交易确认记录-提现
     * @param pagin
     * @param entity
     * @return
     */
    PaginateResult<BlockTransConfirm> findConfirmRaiseList(Pagination pagin, BlockTransConfirm entity);
}
