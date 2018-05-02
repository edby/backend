/*
 * @(#)BitGoRemote.java 2017年7月7日 上午9:59:47
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo;

import java.util.List;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.RecipientModel;

/**
 * <p>File：BitGoRemote.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 上午9:59:47</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public interface BitGoRemoteService
{
    /**
     * 获取钱包地址
     * @param walletName    钱包名称
     * @return
     * @author 施建波  2017年7月7日 上午10:07:46
     */
     BitPayModel getWallet(String walletName) throws BusinessException;

    /**
     * 获取钱包地址
     * @param walletName    钱包名称
     * @param token         TOKEN
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月18日 下午3:42:36
     */
     BitPayModel getWallet(String walletName, String token) throws BusinessException;

    /**
     * 获取钱包信息
     * @param walletId  钱包ID
     * @param token     TOKEN
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月18日 下午4:14:07
     */
     BitPayModel getWalletInfo(String walletId, String token) throws BusinessException;

    /**
     * 创建钱包地址
     * @param walletId  钱包ID
     * @return
     * @author 施建波  2017年7月7日 上午10:08:55
     */
     BitPayModel createWalletAddress(String walletId) throws BusinessException;

    /**
     * 单笔交易
     * @param walletId  钱包ID
     * @param address   目标地址
     * @param amount    发送金额
     * @param fee       手续费
     * @param token     Token
     * @param passphrase 钱包密码
     * @return
     * @author 施建波  2017年7月7日 下午1:06:21
     */
     BitPayModel sendCoins(String walletId, String address, Long amount, Long fee, String token, String passphrase) throws BusinessException;

    /**
     * 多笔交易
     * @param walletId  钱包ID
     * @param recipientList 目标集合
     * @param feeTxConfirmTarget    手续费费率
     * @param token TOKEN
     * @param passphrase 钱包密码
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月19日 下午3:04:09
     */
     BitPayModel sendMultipleCoins(String walletId, List<RecipientModel> recipientList, Integer feeTxConfirmTarget, String token, String passphrase) throws BusinessException;

    /**
     * 单笔交易查询
     * @param transId   交易ID
     * @return
     * @author 施建波  2017年7月7日 下午1:55:49
     */
     BitPayModel transQuery(String transId) throws BusinessException;

    /**
     * 更新钱包密码
     * @param walletPass    旧密码
     * @param newWalletPass 新密码
     * @param encryptedXprv 加密后的私钥
     * @param xpub  公钥
     * @param token TOKEN
     * @return
     * @throws BusinessException
     * @author 施建波  2017年7月19日 下午4:53:39
     */
     String updateKeychain(String walletPass, String newWalletPass, String encryptedXprv, String xpub, String token) throws BusinessException;

    /**
     * 获取单个钱包地址 Get Single Wallet Address 获取钱包中一个地址的信息。亦可被用于检查钱包中是否存在某个地址。
     * @param walletId    钱包ID
     * @param address     地址
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年8月25日 下午4:53:39
     */
     String getSingleWalletAddressInfo(String walletId, String address) throws BusinessException;

}
