/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;


import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.WalletTransferCurrent;
import com.blocain.bitms.trade.fund.mapper.WalletTransferCurrentMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包转账流水表 服务实现类
 * <p>File：WalletTransferCurrentServiceImpl.java </p>
 * <p>Title: WalletTransferCurrentServiceImpl </p>
 * <p>Description:WalletTransferCurrentServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class WalletTransferCurrentServiceImpl extends GenericServiceImpl<WalletTransferCurrent> implements WalletTransferCurrentService
{

    protected WalletTransferCurrentMapper walletTransferCurrentMapper;

    @Autowired
    public WalletTransferCurrentServiceImpl(WalletTransferCurrentMapper walletTransferCurrentMapper)
    {
        super(walletTransferCurrentMapper);
        this.walletTransferCurrentMapper = walletTransferCurrentMapper;
    }

    @Override
    public  WalletTransferCurrent getLastEntity(Long stockinfoId)
    {
        return walletTransferCurrentMapper.getLastEntity(stockinfoId);
    }

    @Override
    public  WalletTransferCurrent getLastEntityForUpdate(Long stockinfoId)
    {
        return walletTransferCurrentMapper.getLastEntityForUpdate(stockinfoId);
    }

    @Override
    public WalletTransferCurrent doSaveWalletTransferCurrent(WalletTransferCurrent walletTransferCurrent,String currentDateStr)
    {
        BigDecimal orgAmt = BigDecimal.ZERO;
        WalletTransferCurrent lastEntity = null;
        try
        {
            lastEntity = walletTransferCurrentMapper.getLastEntityForUpdate(walletTransferCurrent.getStockinfoId());
        }catch(Exception e)
        {
            throw new BusinessException("数据访问超时");
        }
        if(lastEntity!=null)
        {
            orgAmt = lastEntity.getLastAmt();
        }
        walletTransferCurrent.setId(null);
        walletTransferCurrent.setOrgAmt(orgAmt);
        if(walletTransferCurrent.getOccurAmt().compareTo(BigDecimal.ZERO)<0)
        {
            throw new BusinessException("发生额错误");
        }
        if(StringUtils.equalsIgnoreCase(walletTransferCurrent.getOccurDirect(), FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
        {
            walletTransferCurrent.setLastAmt(orgAmt.add(walletTransferCurrent.getOccurAmt()));
        }else if(StringUtils.equalsIgnoreCase(walletTransferCurrent.getOccurDirect(),FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
        {
            walletTransferCurrent.setLastAmt(orgAmt.subtract(walletTransferCurrent.getOccurAmt()));
        }else
        {
            throw new BusinessException("参数校验错误");
        }
        if(walletTransferCurrent.getLastAmt().compareTo(BigDecimal.ZERO)<0)
        {
            throw new BusinessException("最新余额错误");
        }
        walletTransferCurrent.setCreateDate(new Date());
        walletTransferCurrent.setCurrentDate(DateUtils.parseDate(currentDateStr));
        walletTransferCurrent.setId(SerialnoUtils.buildPrimaryKey());
        walletTransferCurrentMapper.insert(walletTransferCurrent);
        return walletTransferCurrent;
    }
}
