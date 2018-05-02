/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.SystemWallet;
import com.blocain.bitms.trade.fund.mapper.SystemWalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 系统系统钱包表 服务实现类
 * <p>File：SystemWallet.java </p>
 * <p>Title: SystemWallet </p>
 * <p>Description:SystemWallet </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SystemWalletServiceImpl extends GenericServiceImpl<SystemWallet> implements SystemWalletService
{
    private SystemWalletMapper      systemWalletMapper;
    
    @Autowired
    private BitGoRemoteV2Service    bitGoRemoteService;
    
    @Autowired
    private SystemWalletAddrService walletAddrService;
    
    @Autowired
    private SystemWalletService     systemWalletService;
    
    @Autowired
    public SystemWalletServiceImpl(SystemWalletMapper systemWalletMapper)
    {
        super(systemWalletMapper);
        this.systemWalletMapper = systemWalletMapper;
    }
    
    @Override
    public SystemWallet createBtcWallet(String walletName, Long createBy) throws BusinessException
    {
        try
        {
            // 通过BITGO接口创建钱包地址
            BitPayModel bitPayModel = bitGoRemoteService.getWallet(walletName,"btc");
            // 根据证券信息ID和钱包ID获取钱包实体
            SystemWallet systemWallet = new SystemWallet();
            systemWallet.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            systemWallet.setWalletId(bitPayModel.getId());
            systemWallet = this.findWallet(systemWallet);
            if (null == systemWallet)
            {
                // 创建新的钱包
                systemWallet = new SystemWallet();
                systemWallet.setId(SerialnoUtils.buildPrimaryKey());
                systemWallet.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                systemWallet.setWalletId(bitPayModel.getId());
                systemWallet.setWalletName(walletName);
                systemWallet.setRemark("BitGo平台比特币钱包");
                systemWallet.setCreateBy(createBy);
                systemWallet.setCreateDate(new Timestamp(System.currentTimeMillis()));
                systemWalletMapper.insert(systemWallet);
                return systemWallet;
            }
            else
            {
                throw new BusinessException("相同名称钱包已存在");
            }
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BusinessException("");
        }
    }
    
    @Override
    public SystemWallet findWallet(SystemWallet systemWallet)
    {
        return systemWalletMapper.findWallet(systemWallet);
    }
    
    @Override
    public SystemWallet findWalletAndAddr(SystemWallet wallet)
    {
        return systemWalletMapper.findWalletAndAddr(wallet);
    }
    
    @Override
    public int doRemoveSysWalletAndAddr(String ids) throws BusinessException
    {
        String id[] = ids.split(",");
        for (int i = 0; i < id.length; i++)
        {
            SystemWallet systemWallet = systemWalletService.selectByPrimaryKey(Long.parseLong(id[i]));
            // walletAddrService.deleteByWalletIdId(systemWallet.getWalletId(), systemWallet.getStockinfoId());
            systemWalletService.remove(Long.parseLong(id[i]));
        }
        return 0;
    }
}
