/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisLock;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.BankRecharge;
import com.blocain.bitms.trade.fund.entity.WalletCashTransferCurrent;
import com.blocain.bitms.trade.fund.mapper.BankRechargeMapper;
import com.blocain.bitms.trade.fund.model.FundModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 银行充值记录表 服务实现类
 * <p>File：BankRechargeServiceImpl.java </p>
 * <p>Title: BankRechargeServiceImpl </p>
 * <p>Description:BankRechargeServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class BankRechargeServiceImpl extends GenericServiceImpl<BankRecharge> implements BankRechargeService
{
    protected BankRechargeMapper    bankRechargeMapper;
    
    @Autowired
    private FundService                     fundService;

    @Autowired
    private WalletCashTransferCurrentService walletCashTransferCurrentService;

    @Autowired
    private RedisTemplate                   redisTemplate;
    
    @Autowired
    public BankRechargeServiceImpl(BankRechargeMapper bankRechargeMapper)
    {
        super(bankRechargeMapper);
        this.bankRechargeMapper = bankRechargeMapper;
    }
    
    /**
     * 现金充值审核
     * @param bankRecharge
     * @param SuperAdminId
     */
    @Override
    public void doChargeApproval(BankRecharge bankRecharge, Long SuperAdminId)
    {
        // 现金充值审核通过，平台自动增加外部台帐
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.OP_FUND_ASSET).append("autoWalletCashTransferCurrentDeal").toString();
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                // 现金充值审核处理
                BankRecharge newbankRecharge = bankRechargeMapper.selectForUpdate(bankRecharge.getId());
                if (StringUtils.equalsIgnoreCase(newbankRecharge.getStatus(), FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING))
                {
                    newbankRecharge.setAuditBy(SuperAdminId);
                    newbankRecharge.setAuditDate(new Timestamp(System.currentTimeMillis()));
                    newbankRecharge.setStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
                    bankRechargeMapper.updateByPrimaryKey(newbankRecharge);

                    FundModel fundModel = new FundModel();
                    fundModel.setOriginalBusinessId(newbankRecharge.getId());
                    fundModel.setAccountId(newbankRecharge.getAccountId());
                    fundModel.setStockinfoId(newbankRecharge.getStockinfoId());
                    fundModel.setStockinfoIdEx(newbankRecharge.getStockinfoId());
                    fundModel.setAmount(newbankRecharge.getAmount());
                    fundModel.setAmountEx(newbankRecharge.getAmount());
                    fundModel.setFee(newbankRecharge.getFee());
                    fundModel.setAddress(newbankRecharge.getTransId());
                    fundModel.setTransId(newbankRecharge.getTransId());
                    fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE);
                    // 资产处理
                    fundService.fundTransaction(fundModel);
                }
                else
                {
                    logger.error("状态错误");
                    throw new BusinessException("状态错误！");
                }

                // 现金充值审核通过，平台自动增加外部台帐！
                BigDecimal orgAmt = BigDecimal.ZERO;
                WalletCashTransferCurrent walletCashTransferCurrentDB = walletCashTransferCurrentService.getLastEntity();
                if(null != walletCashTransferCurrentDB)
                {
                    orgAmt = walletCashTransferCurrentDB.getLastAmt();
                }
                WalletCashTransferCurrent walletCashTransferCurrent = new WalletCashTransferCurrent();
                walletCashTransferCurrent.setCurrentDate(new Timestamp(System.currentTimeMillis()));
                walletCashTransferCurrent.setStockinfoId(newbankRecharge.getStockinfoId());
                walletCashTransferCurrent.setOccurDirect(FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE); // 增加
                walletCashTransferCurrent.setOrgAmt(orgAmt);
                walletCashTransferCurrent.setOccurAmt(newbankRecharge.getAmount());
                walletCashTransferCurrent.setLastAmt(orgAmt.add(walletCashTransferCurrent.getOccurAmt())); // 增加
                walletCashTransferCurrent.setFee(BigDecimal.ZERO);
                walletCashTransferCurrent.setTransId(newbankRecharge.getTransId());
                walletCashTransferCurrent.setRemark("现金充值审核通过，平台自动增加外部台帐！");
                walletCashTransferCurrent.setCreateDate(new Timestamp(System.currentTimeMillis()));
                walletCashTransferCurrent.setCreateBy(SuperAdminId);
                walletCashTransferCurrentService.insert(walletCashTransferCurrent);
            }
            catch (BusinessException e)
            {
                logger.error("现金充值审核错误:" + e.getMessage(), e);
                throw new BusinessException(e.getErrorCode());
            }
            finally
            {
                redisLock.unlock();
            }

        }
        else
        {
            logger.error("现金充值审核错误");
            throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    @Override
    public void doDeleteBankRecharge(Long id)
    {
        BankRecharge bankRecharge = bankRechargeMapper.selectForUpdate(id);
        if (StringUtils.equalsIgnoreCase(bankRecharge.getStatus(), FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING))
        {
            bankRechargeMapper.delete(id);
        }
    }
}
