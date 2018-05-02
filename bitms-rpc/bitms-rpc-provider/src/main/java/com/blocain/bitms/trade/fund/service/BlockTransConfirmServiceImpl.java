/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.OutputModel;
import com.blocain.bitms.payment.bitgo.BlockmetaRemoteService;
import com.blocain.bitms.payment.bitgo.BtcRemoteService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirm;
import com.blocain.bitms.trade.fund.entity.SystemWallet;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddr;
import com.blocain.bitms.trade.fund.mapper.BlockTransConfirmMapper;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 区块交易确认表 服务实现类
 * <p>File：BlockTransConfirm.java </p>
 * <p>Title: BlockTransConfirm </p>
 * <p>Description:BlockTransConfirm </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class BlockTransConfirmServiceImpl extends GenericServiceImpl<BlockTransConfirm> implements BlockTransConfirmService
{
    public static final Logger         logger = LoggerFactory.getLogger(BlockTransConfirmServiceImpl.class);
    
    private BlockTransConfirmMapper    blockTransConfirmMapper;
    
    @Autowired
    private BitGoRemoteV2Service       bitGoRemoteService;
    
    @Autowired
    private BlockmetaRemoteService     blockmetaRemoteService;
    
    @Autowired
    private BtcRemoteService           btcRemoteService;
    
    @Autowired
    private SystemWalletService        systemWalletService;
    
    @Autowired
    private SystemWalletAddrService    systemWalletAddrService;
    
    @Autowired
    private AccountFundTransferService accountFundTransferService;
    
    @Autowired
    private FundService                fundService;

    @Autowired(required = false)
    private StockInfoService           stockInfoService;
    
    @Autowired
    public BlockTransConfirmServiceImpl(BlockTransConfirmMapper blockTransConfirmMapper)
    {
        super(blockTransConfirmMapper);
        this.blockTransConfirmMapper = blockTransConfirmMapper;
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.BlockTransConfirmService#createWalletTransRecord(java.lang.String, java.lang.String)
     */
    @Override
    public void createWalletTransRecord(String walletId, String transId) throws BusinessException
    {
        logger.debug("createWalletTransRecord 开始...");
        logger.debug("createWalletTransRecord walletId:" + walletId + "; transId:" + transId);
        try
        {
            if (StringUtils.isNotBlank(walletId) && StringUtils.isNotBlank(transId))
            {
                // 根据证券信息ID和钱包ID获取系统钱包实体
                SystemWallet systemWallet = new SystemWallet();
                systemWallet.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                systemWallet.setWalletId(walletId);
                logger.debug("createWalletTransRecord systemWallet:" + systemWallet.toString());
                systemWallet = systemWalletService.findWallet(systemWallet);
                if (null != systemWallet)
                {
                    // 通过BITGO接口获取交易信息
                    BitPayModel bitPayModel = bitGoRemoteService.transQuery("btc", walletId, transId);
                    logger.debug("createWalletTransRecord bitPayModel:" + bitPayModel.toString());
                    if (null != bitPayModel && CollectionUtils.isNotEmpty(bitPayModel.getOutputs()))
                    {
                        // 根据证券信息ID和交易ID获取区块交易确认信息列表
                        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
                        blockTransConfirm.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                        blockTransConfirm.setTransId(transId);
                        logger.debug("createWalletTransRecord blockTransConfirm:" + blockTransConfirm.toString());
                        // 根据证券信息ID和交易ID获取区块交易确认信息列表
                        List<BlockTransConfirm> transList = this.findWalletTransList(blockTransConfirm);
                        logger.debug("createWalletTransRecord transList size:" + transList.size());
                        if (CollectionUtils.isEmpty(transList))
                        {
                            // 组装从BITGO获取的地址列表集
                            List<String> addrList = Lists.newArrayList();
                            Map<String, OutputModel> outputMap = Maps.newHashMap();
                            for (OutputModel outputModel : bitPayModel.getOutputs())
                            {
                                if(null != outputModel.getAccount()){
                                    addrList.add(outputModel.getAccount());
                                    outputMap.put(outputModel.getAccount(), outputModel); // bitGoV1、btc接口返回的是account字段
                                } else {
                                    addrList.add(outputModel.getAddress());
                                    outputMap.put(outputModel.getAddress(), outputModel); // bitGoV2接口返回的是address字段
                                }
                            }
                            logger.debug("createWalletTransRecord addrList:" + addrList);
                            Map<String, Object> addrMap = Maps.newHashMap();
                            addrMap.put("stockinfoId", FundConsts.WALLET_BTC_TYPE);
                            addrMap.put("walletId", walletId);
                            addrMap.put("addrList", addrList);
                            logger.debug("createWalletTransRecord 充值addrMap:" + addrMap);
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表 充值部分
                            List<SystemWalletAddr> systemWalletAddrList = systemWalletAddrService.findWalletAddrList(addrMap);
                            logger.debug("createWalletTransRecord systemWalletAddrList 充值部分size:" + systemWalletAddrList.size());
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表
                            if (CollectionUtils.isNotEmpty(systemWalletAddrList))
                            {
                                List<BlockTransConfirm> transInsertList = Lists.newArrayList();
                                for (SystemWalletAddr walletAddr : systemWalletAddrList)
                                {
                                    logger.debug("createWalletTransRecord walletAddr:" + walletAddr.toString());
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(walletAddr.getWalletAddr()), 0,
                                            FundConsts.WALLET_TRANS_CONFIRMSIDE_BTC));
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(walletAddr.getWalletAddr()),
                                            bitPayModel.getConfirmations(), FundConsts.WALLET_TRANS_CONFIRMSIDE_BITGO));
                                    // transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(walletAddr.getWalletAddr()), 0,
                                    // FundConsts.WALLET_TRANS_CONFIRMSIDE_BLOCKMETA));
                                }
                                // 生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态
                                logger.debug("createWalletTransRecord insertBatch");
                                logger.debug("createWalletTransRecord 充值　生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态....");
                                blockTransConfirmMapper.insertBatch(transInsertList);
                            }
                            /*
                            addrMap = Maps.newHashMap();
                            addrMap.put("stockinfoId", FundConsts.WALLET_BTC_TYPE);
                            addrMap.put("transferStatus", FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                            addrMap.put("walletId", walletId);
                            addrMap.put("transId", transId);
                            addrMap.put("addrList", addrList);
                            logger.debug("createWalletTransRecord 提现addrMap:" + addrMap);
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表 提现部分
                            List<AccountFundTransfer> transferAddrList = accountFundTransferService.findTransferAddrList(addrMap);
                            logger.debug("createWalletTransRecord transferAddrList  提现部分size:" + transferAddrList.size());
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表
                            if (CollectionUtils.isNotEmpty(transferAddrList))
                            {
                                List<BlockTransConfirm> transInsertList = Lists.newArrayList();
                                for (AccountFundTransfer transferAddr : transferAddrList)
                                {
                                    logger.debug("createWalletTransRecord transferAddr:" + transferAddr.toString());
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(transferAddr.getTargetWalletAddr()), 0,
                                            FundConsts.WALLET_TRANS_CONFIRMSIDE_BTC));
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(transferAddr.getTargetWalletAddr()),
                                            bitPayModel.getConfirmations(), FundConsts.WALLET_TRANS_CONFIRMSIDE_BITGO));
                                    // transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(transferAddr.getTargetWalletAddr()), 0,
                                    // FundConsts.WALLET_TRANS_CONFIRMSIDE_BLOCKMETA));
                                }
                                // 生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态
                                logger.debug("createWalletTransRecord insertBatch");
                                logger.debug("createWalletTransRecord 提现　生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态....");
                                blockTransConfirmMapper.insertBatch(transInsertList);
                            }
                            */
                        }
                    }
                }
            }
        }
        catch (BusinessException e)
        {
            logger.error("createWalletTransRecord error:" + e.getMessage());
            throw new BusinessException(e.getErrorCode());
        }
        logger.debug("createWalletTransRecord 结束...");
    }

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.BlockTransConfirmService#createBtcWalletTransRecord(java.lang.String)
     */
    @Override
    public void createBtcWalletTransRecord(String transId) throws BusinessException
    {
        logger.info("createBtcWalletTransRecord 开始...");
        logger.info("createBtcWalletTransRecord transId:" + transId);
        StockInfo stockInfo = getStockInfo(FundConsts.WALLET_BTC_TYPE);
        if(!StringUtils.equalsIgnoreCase(stockInfo.getCanRecharge(),FundConsts.PUBLIC_STATUS_YES))
        {
            logger.info(stockInfo.getStockCode()+"["+stockInfo.getTokenContactAddr()+"]充值开关已关闭!");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        try
        {
            // 根据证券信息ID和钱包ID获取系统钱包实体
            SystemWallet systemWallet = new SystemWallet();
            systemWallet.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            systemWallet.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT); // 充值钱包
            logger.info("createBtcWalletTransRecord systemWallet:" + systemWallet.toString());
            systemWallet = systemWalletService.findWallet(systemWallet);
            if (null != systemWallet) {
                logger.info("createBtcWalletTransRecord systemWallet:" + systemWallet);
                if (StringUtils.isNotBlank(systemWallet.getWalletId()) && StringUtils.isNotBlank(transId))
                {
                    String walletId = systemWallet.getWalletId();
                    logger.info("createBtcWalletTransRecord walletId:" + walletId);

                    // 通过BITGO接口获取交易信息
                    BitPayModel bitPayModel = bitGoRemoteService.transQuery("btc", walletId, transId);
                    logger.info("createBtcWalletTransRecord bitPayModel:" + bitPayModel.toString());
                    if (null != bitPayModel && CollectionUtils.isNotEmpty(bitPayModel.getOutputs()))
                    {
                        // 根据证券信息ID和交易ID获取区块交易确认信息列表
                        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
                        blockTransConfirm.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                        blockTransConfirm.setTransId(transId);
                        logger.info("createBtcWalletTransRecord blockTransConfirm:" + blockTransConfirm.toString());
                        // 根据证券信息ID和交易ID获取区块交易确认信息列表
                        List<BlockTransConfirm> transList = this.findWalletTransList(blockTransConfirm);
                        logger.info("createBtcWalletTransRecord transList size:" + transList.size());
                        if (CollectionUtils.isEmpty(transList))
                        {
                            // 组装从BITGO获取的地址列表集
                            List<String> addrList = Lists.newArrayList();
                            Map<String, OutputModel> outputMap = Maps.newHashMap();
                            for (OutputModel outputModel : bitPayModel.getOutputs())
                            {
                                if(null != outputModel.getAccount()){
                                    addrList.add(outputModel.getAccount());
                                    outputMap.put(outputModel.getAccount(), outputModel); // bitGoV1、btc接口返回的是account字段
                                } else {
                                    addrList.add(outputModel.getAddress());
                                    outputMap.put(outputModel.getAddress(), outputModel); // bitGoV2接口返回的是address字段
                                }
                            }
                            logger.info("createBtcWalletTransRecord addrList:" + addrList);
                            Map<String, Object> addrMap = Maps.newHashMap();
                            addrMap.put("stockinfoId", FundConsts.WALLET_BTC_TYPE);
                            addrMap.put("walletId", walletId);
                            addrMap.put("addrList", addrList);
                            logger.info("createBtcWalletTransRecord 充值addrMap:" + addrMap);
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表 充值部分
                            List<SystemWalletAddr> systemWalletAddrList = systemWalletAddrService.findWalletAddrList(addrMap);
                            logger.info("createBtcWalletTransRecord systemWalletAddrList 充值部分size:" + systemWalletAddrList.size());
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表
                            if (CollectionUtils.isNotEmpty(systemWalletAddrList))
                            {
                                List<BlockTransConfirm> transInsertList = Lists.newArrayList();
                                for (SystemWalletAddr walletAddr : systemWalletAddrList)
                                {
                                    logger.info("createBtcWalletTransRecord walletAddr:" + walletAddr.toString());
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(walletAddr.getWalletAddr()), 0,
                                            FundConsts.WALLET_TRANS_CONFIRMSIDE_BTC));
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(walletAddr.getWalletAddr()),
                                            bitPayModel.getConfirmations(), FundConsts.WALLET_TRANS_CONFIRMSIDE_BITGO));
                                    // transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(walletAddr.getWalletAddr()), 0,
                                    // FundConsts.WALLET_TRANS_CONFIRMSIDE_BLOCKMETA));
                                }
                                // 生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态
                                logger.info("createBtcWalletTransRecord insertBatch");
                                logger.info("createBtcWalletTransRecord 充值　生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态....");
                                blockTransConfirmMapper.insertBatch(transInsertList);
                            }
                            /*
                            addrMap = Maps.newHashMap();
                            addrMap.put("stockinfoId", FundConsts.WALLET_BTC_TYPE);
                            addrMap.put("transferStatus", FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                            addrMap.put("walletId", walletId);
                            addrMap.put("transId", transId);
                            addrMap.put("addrList", addrList);
                            logger.info("createBtcWalletTransRecord 提现addrMap:" + addrMap);
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表 提现部分
                            List<AccountFundTransfer> transferAddrList = accountFundTransferService.findTransferAddrList(addrMap);
                            logger.info("createBtcWalletTransRecord transferAddrList  提现部分size:" + transferAddrList.size());
                            // 根据从BITGO获取的地址列表，查询实际属于平台地址的列表
                            if (CollectionUtils.isNotEmpty(transferAddrList))
                            {
                                List<BlockTransConfirm> transInsertList = Lists.newArrayList();
                                for (AccountFundTransfer transferAddr : transferAddrList)
                                {
                                    logger.info("createBtcWalletTransRecord transferAddr:" + transferAddr.toString());
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(transferAddr.getTargetWalletAddr()), 0,
                                            FundConsts.WALLET_TRANS_CONFIRMSIDE_BTC));
                                    transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(transferAddr.getTargetWalletAddr()),
                                            bitPayModel.getConfirmations(), FundConsts.WALLET_TRANS_CONFIRMSIDE_BITGO));
                                    // transInsertList.add(this.getTransList(systemWallet, transId, bitPayModel, outputMap.get(transferAddr.getTargetWalletAddr()), 0,
                                    // FundConsts.WALLET_TRANS_CONFIRMSIDE_BLOCKMETA));
                                }
                                // 生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态
                                logger.info("createBtcWalletTransRecord insertBatch");
                                logger.info("createBtcWalletTransRecord 提现　生成三条区块交易确认信息批量插入 待三方接口轮询确认回填状态....");
                                blockTransConfirmMapper.insertBatch(transInsertList);
                            }
                            */
                        }
                    }
                }
            }
        }
        catch (BusinessException e)
        {
            logger.error("createBtcWalletTransRecord error:" + e.getMessage());
            throw new BusinessException(e.getErrorCode());
        }
        logger.debug("createBtcWalletTransRecord 结束...");
    }
    
    /**
     * 组建区块交易确认实体
     * @param systemWallet  系统钱包实体
     * @param transId       交易ID
     * @param bitPayModel   bitPayModel
     * @param outputModel   钱包地址
     * @param confirmations 确认数量
     * @param confirmSide   区块确认方
     * @return
     * @author 施建波  2017年7月12日 下午2:27:20
     */
    private BlockTransConfirm getTransList(SystemWallet systemWallet, String transId, BitPayModel bitPayModel, OutputModel outputModel, Integer confirmations,
            String confirmSide)
    {
        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
        blockTransConfirm.setId(SerialnoUtils.buildPrimaryKey());
        blockTransConfirm.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        blockTransConfirm.setWalletId(systemWallet.getWalletId());
        if(null != outputModel.getAccount()){
            blockTransConfirm.setWalletAddr(outputModel.getAccount()); // bitGoV1、btc接口返回的是account字段
        } else {
            blockTransConfirm.setWalletAddr(outputModel.getAddress()); // bitGoV2接口返回的是address字段
        }
        blockTransConfirm.setTransId(transId);
        blockTransConfirm.setConfirmSide(confirmSide);
        blockTransConfirm.setAmount(this.changeAmount(outputModel.getValue()).abs());
        blockTransConfirm.setFee(this.changeAmount(bitPayModel.getFee()));
        // 方向
        if (FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT.equals(systemWallet.getWalletUsageType()))
        { // 充币转账
            blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        }
        else if (FundConsts.WALLET_USAGE_TYPE_TRANSFER_ACCOUNT.equals(systemWallet.getWalletUsageType()))
        { // 提币划拨
            blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_PAYMENT);
        }
        /*
         * if (outputModel.getValue() >= 0)
         * {
         * blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
         * }
         * else
         * {
         * blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_PAYMENT);
         * }
         */
        // 节点确认数
        if (confirmations >= 2)
        {
            blockTransConfirm.setStatus(FundConsts.WALLET_TRANS_STATUS_CONFIRM);
        }
        else
        {
            blockTransConfirm.setStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM);
        }
        blockTransConfirm.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return blockTransConfirm;
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.BlockTransConfirmService#findWalletTransList(com.blocain.bitms.trade.fund.entity.BlockTransConfirm)
     */
    @Override
    public List<BlockTransConfirm> findWalletTransList(BlockTransConfirm blockTransConfirm)
    {
        return blockTransConfirmMapper.findWalletTransList(blockTransConfirm);
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.BlockTransConfirmService#modifyWalletTransStatus()
     */
    @Override
    public void modifyWalletTransStatus(List<BlockTransConfirm> blockTransList) throws BusinessException
    {
        logger.debug("modifyWalletTransStatus 开始...");
        if (CollectionUtils.isNotEmpty(blockTransList))
        {
            FundModel fundModel = new FundModel();
            boolean isAllConfirm = Boolean.TRUE;
            for (BlockTransConfirm blockTrans : blockTransList)
            {
                try
                {
                    BitPayModel bitPayModel = null;
                    // 从各个区块确认方获取交易信息
                    if (FundConsts.WALLET_TRANS_CONFIRMSIDE_BTC.equals(blockTrans.getConfirmSide()))
                    {
                        bitPayModel = btcRemoteService.transQuery(blockTrans.getTransId());
                    }
                    else if (FundConsts.WALLET_TRANS_CONFIRMSIDE_BITGO.equals(blockTrans.getConfirmSide()))
                    {
                        bitPayModel = bitGoRemoteService.transQuery("btc", blockTrans.getWalletId(), blockTrans.getTransId());
                    }
                    else if (FundConsts.WALLET_TRANS_CONFIRMSIDE_BLOCKMETA.equals(blockTrans.getConfirmSide()))
                    {
                        bitPayModel = blockmetaRemoteService.transQuery(blockTrans.getTransId());
                    }
                    logger.debug("modifyWalletTransStatus bitPayModel值:" + bitPayModel.toString());
                    if (null != bitPayModel && CollectionUtils.isNotEmpty(bitPayModel.getOutputs()))
                    {
                        Map<String, OutputModel> outputMap = Maps.newHashMap();
                        for (OutputModel outputModel : bitPayModel.getOutputs())
                        {
                            if(null != outputModel.getAccount()){
                                outputMap.put(outputModel.getAccount(), outputModel); // bitGoV1、btc接口返回的是account字段
                            } else {
                                outputMap.put(outputModel.getAddress(), outputModel); // bitGoV2接口返回的是address字段
                            }
                        }
                        if (outputMap.containsKey(blockTrans.getWalletAddr()))
                        {
                            OutputModel outputModel = outputMap.get(blockTrans.getWalletAddr());
                            logger.debug("modifyWalletTransStatus outputModel值:" + outputModel.toString());
                            // 方向 收
                            String direct = FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT;
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE);// 钱包账户充值
                            // 方向 付
                            if (FundConsts.WALLET_TRANS_FUND_DIRECT_PAYMENT.equals(blockTrans.getDirect()))
                            {
                                direct = FundConsts.WALLET_TRANS_FUND_DIRECT_PAYMENT;
                                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 钱包账户提现
                            }
                            /*
                             * if (outputModel.getValue() < 0)
                             * {
                             * direct = FundConsts.WALLET_TRANS_FUND_DIRECT_PAYMENT;
                             * fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 钱包账户提现
                             * }
                             */
                            if (!direct.equals(blockTrans.getDirect())) { throw new BusinessException("不同平台的资产方向不一致"); }
                            if (bitPayModel.getConfirmations() >= 2)
                            {
                                // 如果交易节点确认数大于等于2，则区块确认状态为已确认
                                blockTrans.setStatus(FundConsts.WALLET_TRANS_STATUS_CONFIRM);
                                // 更新区块确认状态
                                blockTransConfirmMapper.updateByPrimaryKey(blockTrans);
                                // 内部钱包资产处理
                                fundModel.setStockinfoId(blockTrans.getStockinfoId());
                                fundModel.setTransId(blockTrans.getTransId());
                                fundModel.setAddress(blockTrans.getWalletAddr().toString());
                                fundModel.setAmount(this.changeAmount(outputModel.getValue()).abs());
                                fundModel.setOriginalBusinessId(blockTrans.getId());
                                if (null != bitPayModel.getFee())
                                {
                                    fundModel.setFee(this.changeAmount(bitPayModel.getFee()));
                                }
                                else
                                {
                                    fundModel.setFee(blockTrans.getFee()); // 提现 区块确认回来的费用可能为空，所以从系统内部取
                                }
                            }
                            else
                            {
                                logger.debug("modifyWalletTransStatus 继续轮询  目前未达到2个区块确认....");
                                isAllConfirm = Boolean.FALSE;
                            }
                        }
                        else
                        {
                            throw new BusinessException("交易:" + blockTrans.getTransId() + "，不存在地址：" + blockTrans.getWalletAddr());
                        }
                    }
                    else
                    {
                        throw new BusinessException("BitGo异步回调获取的区块交易数据错误，请检查");
                    }
                }
                catch (BusinessException e)
                {
                    isAllConfirm = Boolean.FALSE;
                    logger.error(e.getMessage(), e);
                    throw new BusinessException(e.getErrorCode());
                }
            }
            if (isAllConfirm)
            {
                // 修改区块交易确认状态和修改钱包账户资产当前金额
                if (FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW.equals(fundModel.getBusinessFlag()))
                { // 提现
                    AccountFundTransfer accountFundTransfer = new AccountFundTransfer();
                    accountFundTransfer.setStockinfoId(fundModel.getStockinfoId());
                    accountFundTransfer.setTargetWalletAddr(fundModel.getAddress());
                    accountFundTransfer.setTransId(fundModel.getTransId());
                    logger.debug("modifyWalletTransStatus 提现accountFundTransfer:" + accountFundTransfer.toString());
                    AccountFundTransfer transferAddr = accountFundTransferService.findTransferAddr(accountFundTransfer);
                    fundModel.setAccountId(transferAddr.getAccountId());
                    fundModel.setOriginalBusinessId(transferAddr.getId());// 原始业务ID
                }
                else
                { // 充值
                    SystemWalletAddr systemWalletAddr = new SystemWalletAddr();
                    systemWalletAddr.setStockinfoId(fundModel.getStockinfoId());
                    systemWalletAddr.setWalletAddr(fundModel.getAddress());
                    systemWalletAddr = systemWalletAddrService.findWalletAddr(systemWalletAddr);
                    logger.debug("modifyWalletTransStatus 充值systemWalletAddr:" + systemWalletAddr.toString());
                    fundModel.setAccountId(systemWalletAddr.getAccountId());
                }
                // accountWalletAssetService.modifyAccountWallet(accountWalletModel);
                // 钱包账户资产充值 进行相关资产处理 增加当前数量
                // 钱包账户资产提现 进行相关资产处理 减少当前数量 减少冻结数量
                logger.debug("modifyWalletTransStatus 达到2个区块确认条件   进行资产处理....");
                logger.debug("modifyWalletTransStatus fundModel:" + fundModel.toString());
                fundService.fundTransaction(fundModel);
            }
        }
        logger.debug("modifyWalletTransStatus 结束...");
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
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.BlockTransConfirmService#findWalletTransUnconfirmMap()
     */
    @Override
    public Map<String, List<BlockTransConfirm>> findWalletTransUnconfirmMap()
    {
        // 根据证券信息ID和未确认状态获取所有区块交易信息
        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
        blockTransConfirm.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        blockTransConfirm.setStatus(FundConsts.WALLET_TRANS_STATUS_UNCONFIRM);
        // 根据证券信息ID和未确认状态获取所有区块交易信息
        List<BlockTransConfirm> blockTransList = blockTransConfirmMapper.findWalletTransList(blockTransConfirm);
        Map<String, List<BlockTransConfirm>> blockTransMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(blockTransList))
        {
            StringBuilder sb = new StringBuilder();
            // 根据交易ID和钱包地址为key,组装成MAP集合
            for (BlockTransConfirm blockTrans : blockTransList)
            {
                sb.setLength(0);
                sb.append(blockTrans.getTransId()).append("_").append(blockTrans.getWalletAddr());
                String key = sb.toString();
                List<BlockTransConfirm> transList = Lists.newArrayList();
                // 如何理解？？？
                if (blockTransMap.containsKey(key))
                {
                    transList = blockTransMap.get(key);
                }
                transList.add(blockTrans);
                blockTransMap.put(key, transList);
            }
        }
        return blockTransMap;
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.trade.fund.service.BlockTransConfirmService#transExternalQuery()
     */
    @Override
    public void transExternalQuery()
    {
        logger.debug("区块交易确认轮询任务 transExternalQuery start...");
        // 根据证券信息ID和未确认状态获取所有区块交易信息
        Map<String, List<BlockTransConfirm>> blockTransMap = this.findWalletTransUnconfirmMap();
        if (MapUtils.isNotEmpty(blockTransMap))
        {
            try
            {
                for (List<BlockTransConfirm> transList : blockTransMap.values())
                {
                    logger.debug("区块交易确认轮询任务 transExternalQuery transList:" + transList.toString());
                    this.modifyWalletTransStatus(transList);
                    logger.debug("区块交易确认轮询任务 transExternalQuery end...");
                }
            }
            catch (Exception e)
            {
                logger.debug("区块交易确认轮询任务执行失败{}", e.getLocalizedMessage());
            }
        }
        logger.debug("区块交易确认轮询任务 transExternalQuery end...");
    }
    
    @Override
    public PaginateResult<BlockTransConfirm> findChargeList(Pagination pagin, BlockTransConfirm entity)
    {
        entity.setPagin(pagin);
        List<BlockTransConfirm> list = blockTransConfirmMapper.findChargeList(entity);
        return new PaginateResult<>(pagin, list);
    }
    
    @Override
    public PaginateResult<BlockTransConfirm> findConfirmChargeList(Pagination pagin, BlockTransConfirm entity)
    {
        entity.setPagin(pagin);
        List<BlockTransConfirm> list = blockTransConfirmMapper.findConfirmChargeList(entity);
        return new PaginateResult<>(pagin, list);
    }
    
    @Override
    public PaginateResult<BlockTransConfirm> findConfirmRaiseList(Pagination pagin, BlockTransConfirm entity)
    {
        entity.setPagin(pagin);
        List<BlockTransConfirm> list = blockTransConfirmMapper.findConfirmRaiseList(entity);
        return new PaginateResult<>(pagin, list);
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
