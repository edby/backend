/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.risk.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.risk.model.EnableModel;

import java.util.List;

/**
 * 可用统一服务接口
 * <p>File：EnableService.java </p>
 * <p>Title: EnableService </p>
 * <p>Description:EnableService </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
public interface EnableService
{
    /**
     * 指令端可用统一服务
     * @param enableModel    enableModel
     * @return enableModel    enableModel
     * @throws BusinessException
     * @author sunbiao  2017年7月10日 上午10:38:51
     */
    EnableModel instructionTerminalEnable(EnableModel enableModel) throws BusinessException;
    
    /**
     * 委托端可用统一服务
     * @param enableModel    enableModel
     * @return enableModel    enableModel
     * @throws BusinessException
     * @author sunbiao  2017年7月10日 上午10:38:51
     */
    EnableModel entrustTerminalEnable(EnableModel enableModel) throws BusinessException;
    
    /**
     * 获取账户钱包资产列表
     * @param accountId
     * @return
     * @throws BusinessException
     */
    List<AccountWalletAsset> getWalletAssetListByAccountId(Long accountId) throws BusinessException;
    
    /**
     * 获取账户合约资产列表
     * @param accountId
     * @return
     * @throws BusinessException
     */
    List<AccountContractAsset> getContractAssetListByAccountId(Long accountId,Long exchangePairMoney) throws BusinessException;
}
