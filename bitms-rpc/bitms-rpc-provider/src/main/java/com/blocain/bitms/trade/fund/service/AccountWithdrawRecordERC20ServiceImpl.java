/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.payment.eth.EthereumUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecordERC20;
import com.blocain.bitms.trade.fund.mapper.AccountWithdrawRecordERC20Mapper;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * ERC20账户提现记录表 服务实现类
 * <p>File：AccountWithdrawRecordERC20ServiceImpl.java </p>
 * <p>Title: AccountWithdrawRecordERC20ServiceImpl </p>
 * <p>Description:AccountWithdrawRecordERC20ServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountWithdrawRecordERC20ServiceImpl extends GenericServiceImpl<AccountWithdrawRecordERC20> implements AccountWithdrawRecordERC20Service
{
    protected AccountWithdrawRecordERC20Mapper accountWithdrawRecordERC20Mapper;
    
    public static final Logger                 logger = LoggerFactory.getLogger(AccountWithdrawRecordERC20ServiceImpl.class);
    
    @Autowired
    FundService                                fundService;
    
    @Autowired
    AccountWithdrawRecordERC20Service          accountWithdrawRecordERC20Service;
    
    @Autowired
    AccountWalletAssetService                  accountWalletAssetService;
    
    @Autowired
    StockInfoService                           stockInfoService;
    
    @Autowired
    public AccountWithdrawRecordERC20ServiceImpl(AccountWithdrawRecordERC20Mapper accountWithdrawRecordERC20Mapper)
    {
        super(accountWithdrawRecordERC20Mapper);
        this.accountWithdrawRecordERC20Mapper = accountWithdrawRecordERC20Mapper;
    }
    
    @Override
    public AccountWithdrawRecordERC20 selectByIdForUpdate(Long id)
    {
        return accountWithdrawRecordERC20Mapper.selectByIdForUpdate(id);
    }
    
    @Override
    public void autoTransactionStatus()
    {
        List<AccountWithdrawRecordERC20> list = accountWithdrawRecordERC20Mapper.getListRecordUnCoinfirm();
        for (AccountWithdrawRecordERC20 everyEntity : list)
        {
            accountWithdrawRecordERC20Service.doTransactionStatus(everyEntity.getId());
        }
    }
    
    /**
     * 轮询ERC20 TOKEN 的交易状态 单个
     */
    @Override
    public void doTransactionStatus(Long id)
    {
        try
        {
            AccountWithdrawRecordERC20 entity = accountWithdrawRecordERC20Mapper.selectByIdForUpdate(id);
//            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(entity.getStockinfoId());
            if (StringUtils.equalsIgnoreCase(entity.getConfirmStatus(), FundConsts.WALLET_TRANS_STATUS_UNCONFIRM))
            {
                // 查询状态
                TransactionReceipt receipt = EthereumUtils.ethGetTransactionReceipt(entity.getTransId());
                String ret = receipt.getStatus();
                BigInteger useGas = receipt.getGasUsed();
//                if ((entity.getOccurAmt().subtract(entity.getFee())).compareTo(stockInfo.getSmallWithdrawHotSignValue()) > 0)
//                {
//                }
                // 未成功
                if (StringUtils.equalsIgnoreCase(ret, "0x0"))
                {
                    // 更新划拨记录 已确认 汇出失败
                    entity.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER_REJECTED);
                    entity.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_CONFIRM);
                    entity.setNetFee(BigDecimal.valueOf(EthereumUtils.getEthGasPrice().doubleValue() * useGas.doubleValue()).divide(BigDecimal.valueOf(Math.pow(10, 18))));
                    accountWithdrawRecordERC20Mapper.updateByPrimaryKey(entity);
                    // 更新网络手续费超级用户 超级用户网络手续费处理
                    // 2018-03-03 潘总认为有风险 手工线下处理
                    /**FundModel fundModel = new FundModel();
                    fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    fundModel.setFee(entity.getFee());
                    fundModel.setBusinessFlag("doGetTransactionStatu");
                    fundModel.setAddress("");
                    AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
                    accountWalletAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    fundService.superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
                    */
                    AccountWalletAsset userWalletAsset = accountWalletAssetService.selectForUpdate(entity.getAccountId(), entity.getStockinfoId());
                    userWalletAsset.setWithdrawedTotal(userWalletAsset.getWithdrawedTotal().add(entity.getOccurAmt().subtract(entity.getFee())));
                    userWalletAsset.setWithdrawingTotal(userWalletAsset.getWithdrawingTotal().subtract(entity.getOccurAmt().subtract(entity.getFee())));
                    accountWalletAssetService.updateByPrimaryKey(userWalletAsset);
                }
                else
                {
                    // 更新划拨记录 已确认 已汇出
                    entity.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                    entity.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_CONFIRM);
                    entity.setNetFee(BigDecimal.valueOf(EthereumUtils.getEthGasPrice().doubleValue() * useGas.doubleValue()).divide(BigDecimal.valueOf(Math.pow(10, 18))));
                    accountWithdrawRecordERC20Mapper.updateByPrimaryKey(entity);

                    AccountWalletAsset userWalletAsset = accountWalletAssetService.selectForUpdate(entity.getAccountId(), entity.getStockinfoId());
                    userWalletAsset.setWithdrawedTotal(userWalletAsset.getWithdrawedTotal().add(entity.getOccurAmt().subtract(entity.getFee())));
                    userWalletAsset.setWithdrawingTotal(userWalletAsset.getWithdrawingTotal().subtract(entity.getOccurAmt().subtract(entity.getFee())));
                    accountWalletAssetService.updateByPrimaryKey(userWalletAsset);
                }
                // 汇出失败会补汇 不涉及回退和资金处理  不需要清理缓存
            }
        }
        catch (Exception e)
        {
            logger.debug("查询ERC20提现记录处理状态失败：" + id + ":" + e.getMessage());
        }
    }
    
    @Override
    public BigDecimal findSumAmtByAccount(AccountWithdrawRecordERC20 accountWithdrawRecordERC20)
    {
        return accountWithdrawRecordERC20Mapper.findSumAmtByAccount(accountWithdrawRecordERC20);
    }

    @Override
    public Integer findCuntByAccount(AccountWithdrawRecordERC20 accountWithdrawRecordERC20)
    {
        return accountWithdrawRecordERC20Mapper.findCuntByAccount(accountWithdrawRecordERC20);
    }
}
