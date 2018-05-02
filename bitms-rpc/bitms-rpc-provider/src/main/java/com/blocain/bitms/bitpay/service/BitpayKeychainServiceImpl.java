/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service;

import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.SystemWallet;
import com.blocain.bitms.trade.fund.service.SystemWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.mapper.BitpayKeychainMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;

import java.util.List;

/**
 * 钱包参数 服务实现类
 * <p>File：BitpayKeychainServiceImpl.java </p>
 * <p>Title: BitpayKeychainServiceImpl </p>
 * <p>Description:BitpayKeychainServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class BitpayKeychainServiceImpl extends GenericServiceImpl<BitpayKeychain> implements BitpayKeychainService
{
    public static final Logger      logger = LoggerFactory.getLogger(BitpayKeychainServiceImpl.class);

    private BitpayKeychainMapper    bitpayKeychainMapper;

    @Autowired
    private BitGoRemoteV2Service    bitGoRemoteService;

    @Autowired
    private SystemWalletService     systemWalletService;
    
    @Autowired
    public BitpayKeychainServiceImpl(BitpayKeychainMapper bitpayKeychainMapper)
    {
        super(bitpayKeychainMapper);
        this.bitpayKeychainMapper = bitpayKeychainMapper;
    }
    
    @Override
    public String builderEncryptPassword() throws BusinessException
    {
        return EncryptUtils.entryptPassword(SerialnoUtils.buildUUID());
    }

    /**
     * 获取比特币提现钱包总余额(包括已经确认与未确认的)
     * @return
     * @throws BusinessException
     */
    @Override
    public String getBtcTXWalletAllBalance() throws BusinessException
    {
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setType(2);
        List<BitpayKeychain> listBitpayKeychain = bitpayKeychainMapper.findList(bitpayKeychain);
        if(null != listBitpayKeychain){
            logger.debug("getBtcTXWalletAllBalance listBitpayKeychain:"+listBitpayKeychain.get(0));
            BitPayModel bp = bitGoRemoteService.getWalletInfo(listBitpayKeychain.get(0).getWalletId(), "btc");
            logger.debug("getBtcTXWalletAllBalance bp:"+bp);
            return  bp.getBalance();
        }

        return "0";
    }

    /**
     * 获取比特币充值钱包已确认余额
     * @return
     * @throws BusinessException
     */
    @Override
    public String getBtcCZWalletConfirmedBalance() throws BusinessException
    {
        SystemWallet systemWalletSelect = new SystemWallet();
        systemWalletSelect.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
        systemWalletSelect.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        SystemWallet systemWallet =  systemWalletService.findWallet(systemWalletSelect);
        if(null != systemWallet){
            logger.debug("getBtcCZWalletConfirmedBalance systemWallet:"+systemWallet);
            BitpayKeychain bitpayKeychain = new BitpayKeychain();
            bitpayKeychain.setType(2);
            List<BitpayKeychain> listBitpayKeychain = bitpayKeychainMapper.findList(bitpayKeychain);
            if(null != listBitpayKeychain){
                logger.debug("getBtcCZWalletConfirmedBalance listBitpayKeychain:"+listBitpayKeychain.get(0));
                BitPayModel bp = bitGoRemoteService.getWalletInfo(systemWallet.getWalletId(), "btc");
                logger.debug("getBtcCZWalletConfirmedBalance bp:"+bp);
                return  bp.getConfirmedBalance();
            }
        }

        return "0";
    }
}
