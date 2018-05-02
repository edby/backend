/*
 * @(#)BitGoRemote.java 2017年7月7日 上午9:59:47
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.RecipientModel;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * bitGo 接口 v2 版本
 * 支持币种：BTC BCH ETH XRP LTC RMG ERC OMG KIN REP
 * <p>File：BitGoRemoteV2Service.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-01-19 10:38:09</p>
 * <p>Company: BloCain</p>
 * @author 张春喜
 * @version 1.0
 */
public interface BitGoRemoteV2Service
{
    /**
     * 获取钱包地址
     * @param walletName    钱包名称
     * @param coin  币种
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19
     */
    BitPayModel getWallet(String walletName, String coin) throws BusinessException;
    
    /**
     * 获取钱包信息
     * @param walletId  钱包ID
     * @param coin  币种
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19
     */
    BitPayModel getWalletInfo(String walletId, String coin) throws BusinessException;
    
    /**
     * 创建钱包地址
     * @param walletId  钱包ID
     * @param coin  币种
     * @return
     * @author 张春喜 2018-01-19
     */
    BitPayModel createWalletAddress(String walletId, String coin) throws BusinessException;
    
    /**
     * 单笔交易
     * 手续费由查询approvel接口返回
     * @param walletId  钱包ID
     * @param address   目标地址
     * @param amount    发送金额
     * @param coin       币种
     * @param passphrase 钱包密码
     * @return
     * @author 张春喜 2018-01-19
     */
    BitPayModel sendCoins(String walletId, String address, Long amount, String coin, String token, String passphrase, Integer minConfirms, String otp) throws BusinessException;
    
    /**
     * 多笔交易
     * 手续费由查询approvel接口返回
     * @param walletId  钱包ID
     * @param recipientList 目标集合
     * @param coin       币种
     * @param passphrase 钱包密码
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19
     */
    BitPayModel sendMultipleCoins(String walletId, List<RecipientModel> recipientList, String coin, String token, String passphrase, Integer minConfirms)
            throws BusinessException;
    
    /**
     * 单笔交易查询
     * @param coin   币种
     * @param walletId   钱包ID
     * @param transId   交易ID
     * @return
     * @author 张春喜 2018-01-19
     */
    BitPayModel transQuery(String coin,String walletId, String transId) throws BusinessException;
    
    /**
     * 更新钱包密码
     * @param oldWalletPass 旧密码
     * @param newWalletPass 新密码
     * @param encryptedXprv 加密后的私钥
     * @param xpub  公钥
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19
     */
    String updateKeychain(String coin, String oldWalletPass, String newWalletPass, String encryptedXprv, String xpub, String token) throws BusinessException;
    
    /**
     * 获取单个钱包地址 Get Single Wallet Address 获取钱包中一个地址的信息。亦可被用于检查钱包中是否存在某个地址。
     * @param coin    币种
     * @param address     地址
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19
     */
    String getSingleWalletAddressInfo(String coin, String address) throws BusinessException;

    /**
     * 获取待审核列表
     * @param coin
     * @return
     * @throws BusinessException
     */
    JSONObject getPendingApprovalsList(String coin) throws BusinessException;

    /**
     * 获取单条待审核列表
     * @param coin
     * @param pendingApprovalId
     * @return
     * @throws BusinessException
     */
    JSONObject getSinglePendingApprovals(String coin, String pendingApprovalId) throws BusinessException;

    /**
     * 登录
     * @param email 邮箱
     * @param loginPwd 登录密码
     * @param otp GA
     * @return
     */
    JSONObject login(String email,String loginPwd,String otp);

    /**
     * 解锁
     * @param otp
     * @return
     */
    JSONObject unlock(String otp);

    /**
     * 加锁
     * @return
     */
    JSONObject lock();

    /**
     * 会话
     * @return
     */
    JSONObject session();

    /**
     * 添加hook
     * @param coin
     * @param walletId
     * @param backUrl
     * @param type=transfer
     * @return
     */
    JSONObject addWebHook(String coin,String walletId,String backUrl,String type);

    /**
     * 移除hook
     * @param coin
     * @param walletId
     * @param backUrl
     * @param type=transfer
     * @return
     */
    JSONObject removeWebHook(String coin,String walletId,String backUrl,String type);

    /**
     * 获取webhook列表
     * @param coin
     * @param walletId
     * @return
     */
    JSONObject listWebHook(String coin, String walletId);

    /**
     * 创建钱包
     * @param coin
     * @param lebel 标签
     * @Param passphrase 密码
     * @return
     */
    JSONObject createWallet(String coin, String lebel,String passphrase,String enterprise);
}
