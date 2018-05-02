/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.util.List;
import java.util.Map;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddr;

/**
 * 系统钱包地址表 服务接口
 * <p>File：SystemWalletAddrService.java </p>
 * <p>Title: SystemWalletAddrService </p>
 * <p>Description:SystemWalletAddrService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface SystemWalletAddrService extends GenericService<SystemWalletAddr>
{
    /**
     * 创建BTC系统钱包地址
     * @param accountId     用户ID
     * @param createBy      创建者
     * @return
     * @author 施建波  2017年7月10日 上午11:29:35
     */
    SystemWalletAddr createBtcWalletAddress(Long accountId, Long createBy) throws BusinessException;
    
    /**
     * 获取系统钱包地址列表
     * @param addrMap   查询条件
     * @return
     * @author 施建波  2017年7月10日 下午3:12:48
     */
    List<SystemWalletAddr> findWalletAddrList(Map<String, Object> addrMap);
    
    /**
     * 获取系统钱包地址实体
     * @param systemWalletAddr  钱包实体
     * @return
     * @author 施建波  2017年7月11日 下午2:07:20
     */
    SystemWalletAddr findWalletAddr(SystemWalletAddr systemWalletAddr);
    
    /**
     * 通过钱包id和证券信息id 删除系统钱包地址信息
     * @param walletIdId
     * @param stockinfoId
     * @return
     */
    int deleteByWalletIdId(String walletIdId, Long stockinfoId);

    /**
     * 根据btc三方接口查询地址上总接收数量
     * https://chain.api.btc.com/v3/address/15urYnyeJe3gwbGJ74wcX89Tz7ZtsFDVew
     */
    void addressExternalQuery();

    /**
     * 定时任务监控 判断地址是否来自BitMSCZWallet钱包
     * @return
     */
    void judgeAddressIsFromBitMSCZWallet();
}
