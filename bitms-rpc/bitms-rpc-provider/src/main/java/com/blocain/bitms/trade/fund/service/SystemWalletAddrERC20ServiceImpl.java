/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.payment.eth.EthereumUtils;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RandomUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddrERC20;
import com.blocain.bitms.trade.fund.entity.SystemWalletERC20;
import com.blocain.bitms.trade.fund.mapper.SystemWalletAddrERC20Mapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 系统ERC20钱包地址表 服务实现类
 * <p>File：SystemWalletAddrERC20ServiceImpl.java </p>
 * <p>Title: SystemWalletAddrERC20ServiceImpl </p>
 * <p>Description:SystemWalletAddrERC20ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class SystemWalletAddrERC20ServiceImpl extends GenericServiceImpl<SystemWalletAddrERC20> implements SystemWalletAddrERC20Service
{
    protected SystemWalletAddrERC20Mapper systemWalletAddrERC20Mapper;
    
    @Autowired
    private SystemWalletERC20Service      systemWalletERC20Service;
    
    @Autowired
    public SystemWalletAddrERC20ServiceImpl(SystemWalletAddrERC20Mapper systemWalletAddrERC20Mapper)
    {
        super(systemWalletAddrERC20Mapper);
        this.systemWalletAddrERC20Mapper = systemWalletAddrERC20Mapper;
    }
    
    @Override
    public SystemWalletAddrERC20 createERC20WalletAddress(Long accountId, Long createBy, Long stockinfoId) throws BusinessException
    {
        try
        {
            if (null == accountId) { throw new BusinessException("账户ID不能为空!"); }
            if (null == createBy) { throw new BusinessException("创建人不能为空!"); }
            if (null == stockinfoId) { throw new BusinessException("币种不能为空!"); }
            // 根据证券信息ID和钱包名称获取钱包实体
            SystemWalletERC20 systemWalletERC20 = new SystemWalletERC20();
            systemWalletERC20.setStockinfoId(stockinfoId);
            systemWalletERC20.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT); // 充值钱包
            systemWalletERC20 = systemWalletERC20Service.findWallet(systemWalletERC20);
            if (null == systemWalletERC20)
            {
                logger.error("createERC20WalletAddress充值钱包不存在!");
                throw new BusinessException("createERC20WalletAddress充值钱包不存在!");
            }
            if (!systemWalletERC20.verifySignature())
            {
                logger.error("createERC20WalletAddress充值钱包数据校验失败!");
                throw new BusinessException("createERC20WalletAddress充值钱包数据校验失败!");
            }
            String address = "";
            // 随机64位密码（包括大小写字母和数字）
            String pwd = RandomUtils.generateString(64);
            try
            {
                // 通过ERC20接口生成钱包地址
                address = EthereumUtils.newAccount(pwd);
            }
            catch (Exception e)
            {
                logger.debug("访问接口失败："+e.getLocalizedMessage());
                throw new BusinessException("ETH node link timeout："+e.getLocalizedMessage());
            }
            if (StringUtils.isNotBlank(address))
            {
                // 生成新的钱包地址
                SystemWalletAddrERC20 systemWalletAddr = new SystemWalletAddrERC20();
                systemWalletAddr.setId(SerialnoUtils.buildPrimaryKey());
                systemWalletAddr.setStockinfoId(stockinfoId);
                systemWalletAddr.setWalletId(systemWalletERC20.getWalletId());
                systemWalletAddr.setWalletAddr(address.toLowerCase());
                systemWalletAddr.setAccountId(accountId.toString());
                systemWalletAddr.setWalletPwd(EncryptUtils.desEncrypt(pwd)); // 保存密码(加密)
                systemWalletAddr.setRemark("ETH TOKEN 钱包地址生成");
                systemWalletAddr.setCreateBy(createBy);
                systemWalletAddr.setCreateDate(new Timestamp(System.currentTimeMillis()));
                systemWalletAddr.setReceived(BigDecimal.ZERO);
                systemWalletAddr.setUnconfirmedReceived(BigDecimal.ZERO);
                systemWalletAddrERC20Mapper.insert(systemWalletAddr);
                return systemWalletAddr;
            }
            else
            {
                logger.debug("ETH 地址创建失败");
                throw new BusinessException("ETH address creation failure");
            }
        }
        catch (BusinessException e)
        {
            logger.error("创建系统钱包地址失败:" + e.getMessage());
            throw e;
        }
    }

    @Override
    public Set<String> getAccountInterAddrsByParams(String[] extAddress) {
        return systemWalletAddrERC20Mapper.findAccountInterAddrsByParams(extAddress);
    }
}
