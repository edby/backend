/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import com.blocain.bitms.payment.bitgo.BtcRemoteService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.SystemWallet;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddr;
import com.blocain.bitms.trade.fund.mapper.SystemWalletAddrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 *  系统钱包地址表 服务实现类
 * <p>File：SystemWalletAddr.java </p>
 * <p>Title: SystemWalletAddr </p>
 * <p>Description:SystemWalletAddr </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SystemWalletAddrServiceImpl extends GenericServiceImpl<SystemWalletAddr> implements SystemWalletAddrService
{
    private SystemWalletAddrMapper walletAddrMapper;
    
    @Autowired
    private BitGoRemoteV2Service   bitGoRemoteService;

    @Autowired
    private BtcRemoteService       btcRemoteService;
    
    @Autowired
    private SystemWalletService    systemWalletService;
    
    @Autowired
    public SystemWalletAddrServiceImpl(SystemWalletAddrMapper walletAddrMapper)
    {
        super(walletAddrMapper);
        this.walletAddrMapper = walletAddrMapper;
    }
    
    @Override
    public SystemWalletAddr createBtcWalletAddress(Long accountId, Long createBy) throws BusinessException
    {
        try
        {
            if (null == accountId) { throw new BusinessException("账户ID不能为空!"); }
            if (null == createBy) { throw new BusinessException("创建人不能为空!"); }
            // 根据证券信息ID和钱包名称获取钱包实体
            SystemWallet systemWallet = new SystemWallet();
            systemWallet.setStockinfoId(FundConsts.WALLET_BTC_TYPE); // btc
            systemWallet.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT); // 充值钱包
            systemWallet = systemWalletService.findWallet(systemWallet);
            if (null == systemWallet) {
                logger.error("createBtcWalletAddress充值钱包不存在!");
                throw new BusinessException("createBtcWalletAddress充值钱包不存在!");
            }
            if (!systemWallet.verifySignature()){
                logger.error("createBtcWalletAddress充值钱包数据校验失败!");
                throw new BusinessException("createBtcWalletAddress充值钱包数据校验失败!");
            }

            // 通过BITGO接口生成钱包地址
            BitPayModel bitPayModel = bitGoRemoteService.createWalletAddress(systemWallet.getWalletId(), "btc");
            // 生成新的钱包地址
            SystemWalletAddr systemWalletAddr = new SystemWalletAddr();
            systemWalletAddr.setId(SerialnoUtils.buildPrimaryKey());
            systemWalletAddr.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            systemWalletAddr.setWalletId(systemWallet.getWalletId());
            systemWalletAddr.setWalletAddr(bitPayModel.getAddress());
            systemWalletAddr.setAccountId(accountId);
            systemWalletAddr.setRemark("BitGo钱包地址生成");
            systemWalletAddr.setCreateBy(createBy);
            systemWalletAddr.setCreateDate(new Timestamp(System.currentTimeMillis()));
            systemWalletAddr.setReceived(BigDecimal.ZERO);
            systemWalletAddr.setUnconfirmedReceived(BigDecimal.ZERO);
            walletAddrMapper.insert(systemWalletAddr);
            return systemWalletAddr;
        }
        catch (BusinessException e)
        {
            logger.error("创建系统钱包地址失败:" + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<SystemWalletAddr> findWalletAddrList(Map<String, Object> addrMap)
    {
        return walletAddrMapper.findWalletAddrList(addrMap);
    }
    
    @Override
    public SystemWalletAddr findWalletAddr(SystemWalletAddr systemWalletAddr)
    {
        return walletAddrMapper.findWalletAddr(systemWalletAddr);
    }
    
    @Override
    public int deleteByWalletIdId(String walletIdId, Long stockinfoId)
    {
        return walletAddrMapper.deleteByWalletIdId(walletIdId, stockinfoId);
    }

    @Override
    public void addressExternalQuery( )
    {
        BitPayModel bp;
        SystemWalletAddr  systemWalletAddrSelect = new SystemWalletAddr();
        List<SystemWalletAddr> listSystemWalletAddr = walletAddrMapper.findList(systemWalletAddrSelect);
        for (int i = 0; i < listSystemWalletAddr.size(); i++){
            try
            {
                systemWalletAddrSelect = listSystemWalletAddr.get(i);
                logger.debug("systemWalletAddrSelect 获取:" + systemWalletAddrSelect);
                bp = btcRemoteService.addressQuery(systemWalletAddrSelect.getWalletAddr());
                logger.debug("bp:" + bp);
                if(null != bp){
                    systemWalletAddrSelect.setReceived(changeAmount(Long.valueOf(bp.getReceived())));
                    systemWalletAddrSelect.setUnconfirmedReceived(changeAmount(Long.valueOf(bp.getUnconfirmed_received())));
                    logger.debug("systemWalletAddrSelect 更新:" + systemWalletAddrSelect);
                    walletAddrMapper.updateByPrimaryKey(systemWalletAddrSelect);
                }

                // 休眠1秒钟  因为btc接口 1分钟最多调用120次
                Thread.sleep(2000);
            } catch (BusinessException e) {
                logger.error("addressExternalQuery失败:" + e.getMessage());
                continue; //继续下一条
            } catch (InterruptedException e) {
                logger.error("addressExternalQuery失败:" + e.getMessage());
                continue; //继续下一条
            }
        }
    }

    public BigDecimal changeAmount(Long amount)
    {
        BigDecimal changeAmount = BigDecimal.ZERO;
        if (null != amount)
        {
            changeAmount = new BigDecimal(amount).divide(new BigDecimal(100000000)).setScale(8, BigDecimal.ROUND_HALF_UP);
        }
        return changeAmount;
    }

    @Override
    public void judgeAddressIsFromBitMSCZWallet()
    {
        try
        {
            SystemWalletAddr systemWalletAddr = new SystemWalletAddr();
            systemWalletAddr.setWalletId("32usQ86cnpKZFZmoLxM2HbSy2veVkphs2M"); // BitMSCZ钱包ID
            List<SystemWalletAddr> listSystemWalletAddr = walletAddrMapper.findList(systemWalletAddr);
            for (SystemWalletAddr SystemWalletAddrItem : listSystemWalletAddr)
            {
                String walletAddrInfo = bitGoRemoteService.getSingleWalletAddressInfo(systemWalletAddr.getWalletId(), SystemWalletAddrItem.getWalletAddr());
                logger.debug("judgeAddressIsFromBitMSCZWallet walletAddrInfo:" + walletAddrInfo);
                // 判断是否存在该地址信息
                // 不存在就发送邮件、短信报警
            }
        }
        catch (BusinessException e)
        {
            logger.error("判断地址是否来自BitMSCZWallet钱包失败:" + e.getMessage());
            throw e;
        }
    }
}
