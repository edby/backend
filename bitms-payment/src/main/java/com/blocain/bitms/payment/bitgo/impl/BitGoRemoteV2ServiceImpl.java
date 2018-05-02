/*
 * @(#)BitGoRemoteImpl.java 2017年7月7日 上午10:09:32
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.impl;

import java.text.MessageFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.payment.basic.BasicServiceImpl;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.RecipientModel;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Maps;

/**
 * bitGo 接口 v2 版本
 * 支持币种：BTC BCH ETH XRP LTC RMG ERC OMG KIN REP
 * <p>File：BitGoRemoteV2ServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-01-19 10:44:42</p>
 * <p>Company: BloCain</p>
 * @author 张春喜
 * @version 1.0
 */
@Component("bitGoRemoteV2")
public class BitGoRemoteV2ServiceImpl extends BasicServiceImpl implements BitGoRemoteV2Service
{
    public static final Logger logger                              = LoggerFactory.getLogger(BitGoRemoteV2ServiceImpl.class);
    
    // https://www.bitgo.com
    public static final String bitgo_root                          = propertiesV2.getProperty("bitgo.root");
    
    // ${bitgo.localhost.root} http://192.168.31.15:3080
    public static final String bitgo_local                         = propertiesV2.getProperty("bitgo.localhost.root.prop");
    
    // ${bitgo.access.token} Tokens 需要关联服务器的IP地址
    public static final String bitgo_access_token                  = propertiesV2.getProperty("bitgo.access.token.prop");
    
    // {0}/api/v2/{1}/decrypt
    public static final String bitgo_utils_decrypt                 = propertiesV2.getProperty("bitgo.utils.decrypt");
    
    // {0}/api/v2/{1}/encrypt
    public static final String bitgo_utils_encrypt                 = propertiesV2.getProperty("bitgo.utils.encrypt");
    
    // {0}/api/v2/{1}/wallet 获取钱包列表 https://www.bitgo.com/api/v2/btc/wallet
    public static final String bitgo_wallet_list                   = propertiesV2.getProperty("bitgo.wallet.list");
    
    // {0}/api/v2/{1}/wallet/{2} 获取钱包信息 https://www.bitgo.com/api/v2/btc/wallet/钱包ID
    public static final String bitgo_wallet_info                   = propertiesV2.getProperty("bitgo.wallet.info");
    
    // {0}/api/v2/{1}/wallet/{2}/address 创建钱包地址
    public static final String bitgo_wallet_address                = propertiesV2.getProperty("bitgo.wallet.address");
    
    // {0}/api/v2/{1}/wallet/{2}/sendmany 多笔交易
    public static final String bitgo_wallet_sendmany               = propertiesV2.getProperty("bitgo.wallet.sendmany");
    
    // {0}/api/v2/{1}/wallet/{2}/sendcoins 单笔交易
    public static final String bitgo_wallet_sendcoins              = propertiesV2.getProperty("bitgo.wallet.sendcoins");
    
    // {0}/api/v2/{1}/wallet/{2}/tx/{3} 查询单笔交易
    public static final String bitgo_wallet_trans_query            = propertiesV2.getProperty("bitgo.wallet.trans.query");
    
    // {0}/api/v2/{1}/wallet/address/{3} 获取单个钱包地址
    public static final String bitgo_wallet_single_info            = propertiesV2.getProperty("bitgo.wallet.getSingleWalletAddressInfo");
    
    // {0}/api/v2/{1}/pendingapprovals 获取待审核交易
    public static final String bitgo_wallet_pendingapproval_list   = propertiesV2.getProperty("bitgo.wallet.pendingapproval.list");
    
    // {0}/api/v2/{1}/pendingapprovals/{2} 获取单条待审核
    public static final String bitgo_wallet_pendingapproval_single = propertiesV2.getProperty("bitgo.wallet.pendingapproval.single");
    
    // {0}/api/v2/{1}/key 更新钱包密码
    public static final String bitgo_wallet_keychain_update        = propertiesV2.getProperty("bitgo.wallet.keychain.update");
    
    // {0}/api/v2/user/login 登录
    public static final String bitgo_wallet_login                  = propertiesV2.getProperty("bitgo.wallet.login");
    
    // {0}/api/v2/user/session 会话
    public static final String bitgo_wallet_session                = propertiesV2.getProperty("bitgo.wallet.session");
    
    // {0}/api/v2/user/lock 锁定
    public static final String bitgo_wallet_lock                   = propertiesV2.getProperty("bitgo.wallet.lock");
    
    // {0}/api/v2/user/unlock 解锁
    public static final String bitgo_wallet_unlock                 = propertiesV2.getProperty("bitgo.wallet.unlock");
    
    // 解锁有效时间(秒)
    public static final Long   bitgo_wallet_unlock_duration        = propertiesV2.getLong("bitgo.wallet.unlock.duration");
    
    // 添加webhook /api/v2/:coin/wallet/:walletId/webhooks
    public static final String bitgo_wallet_add_webhook            = propertiesV2.getProperty("bitgo.wallet.add.webhook");
    
    // 移除webhook /api/v2/:coin/wallet/:walletId/webhooks
    public static final String bitgo_wallet_remove_webhook         = propertiesV2.getProperty("bitgo.wallet.remove.webhook");

    // 列表 /api/v2/:coin/wallet/:walletId/webhooks
    public static final String bitgo_wallet_list_webhook           = propertiesV2.getProperty("bitgo.wallet.list.webhook");

    // /api/v2/:coin/wallet/generate 创建钱包
    public static final String bitgo_wallet_generate               = propertiesV2.getProperty("bitgo.wallet.generate");

    private JSONObject validate(String content) throws BusinessException
    {
        JSONObject json = null;
        try
        {
            logger.debug("validate content:" + content);
            json = JSONObject.parseObject(content);
            String error = json.getString("error");
            if (StringUtils.isNotBlank(error)) { throw new BusinessException(error); }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException("validate error:" + e.getMessage());
        }
        return json;
    }
    
    private Map<String, String> getHeader(String token)
    {
        if (StringUtils.isBlank(token))
        {
            token = bitgo_access_token;
        }
        Map<String, String> header = Maps.newHashMap();
        header.put("Authorization", "Bearer " + token);
        return header;
    }
    
    /**
     * 获取钱包地址
     * @param walletName    钱包名称
     * @param coin  币种
     * @return
     * @throws BusinessException
     */
    @Override
    public BitPayModel getWallet(String walletName, String coin) throws BusinessException
    {
        if (StringUtils.isBlank(walletName)) { throw new BusinessException("钱包名称不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        if (StringUtils.isBlank(bitgo_access_token)) { throw new BusinessException("token不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_list, bitgo_root, "btc");
        String content = HttpUtils.get(client, url, null, null, this.getHeader(bitgo_access_token));
        logger.info("获取钱包地址:接收到的报文：" + content);
        JSONObject json = this.validate(content);
        JSONArray jsonArray = json.getJSONArray("wallets");
        if (jsonArray.isEmpty()) { throw new BusinessException("钱包不存在"); }
        if (jsonArray.size() > 0)
        {
            for (int i = 0; i < jsonArray.size(); i++)
            {
                JSONObject tempJson = jsonArray.getJSONObject(i);
                if (walletName.equals(tempJson.getString("label")))
                {
                    BitPayModel bitPayModel = JSONObject.parseObject(tempJson.toString(), BitPayModel.class);
                    if (null == bitPayModel || StringUtils.isBlank(bitPayModel.getId())) { throw new BusinessException("钱包不存在"); }
                    return bitPayModel;
                }
            }
            return null;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取钱包信息
     * @param walletId  钱包ID
     * @param coin  币种
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19
     */
    @Override
    public BitPayModel getWalletInfo(String walletId, String coin) throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        if (StringUtils.isBlank(bitgo_access_token)) { throw new BusinessException("token不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_info, bitgo_root, coin, walletId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("allTokens", true);
        String content = HttpUtils.get(client, url, null, null, this.getHeader(bitgo_access_token));
        logger.info("获取钱包信息:接收到的报文：" + content);
        JSONObject json = this.validate(content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        // TODO;接口是通的 返回的报文不是期望的V1版本的报文
        /*
         * JSONObject tempJson = json.getJSONObject("private");
         * JSONArray tempJsonArry = tempJson.getJSONArray("keychains");
         * if (!tempJsonArry.isEmpty())
         * {
         * tempJson = tempJsonArry.getJSONObject(0);
         * bitPayModel.setXpub(tempJson.getString("xpub"));
         * }
         *//*
            * JSONObject tempJson = json.getJSONObject("private");
            * JSONArray tempJsonArry = tempJson.getJSONArray("keychains");
            * if (!tempJsonArry.isEmpty())
            * {
            * tempJson = tempJsonArry.getJSONObject(0);
            * bitPayModel.setXpub(tempJson.getString("xpub"));
            * }
            */
        return bitPayModel;
    }
    
    /**
     * 创建钱包地址
     * @param walletId  钱包ID
     * @param coin  币种
     * @return
     * @author 张春喜 2018-01-19
     */
    @Override
    public BitPayModel createWalletAddress(String walletId, String coin) throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        if (StringUtils.isBlank(bitgo_access_token)) { throw new BusinessException("token不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_address, bitgo_root, coin, walletId);
        String content = HttpUtils.post(client, url, null, null, this.getHeader(bitgo_access_token));
        JSONObject json = this.validate(content);
        logger.info("创建钱包地址:接收到的报文：" + content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        if (null == bitPayModel || StringUtils.isBlank(bitPayModel.getAddress())) { throw new BusinessException("钱包地址生成失败"); }
        return bitPayModel;
    }
    
    /**
     * 单笔交易
     * @param walletId  钱包ID
     * @param address   目标地址
     * @param amount    发送金额
     * @param coin       币种
     * @param passphrase 钱包密码
     * @return
     * @throws BusinessException
     */
    @Override
    public BitPayModel sendCoins(String walletId, String address, Long amount, String coin, String token, String passphrase, Integer minConfirms, String otp)
            throws BusinessException
    {
        try
        {
            // 试图锁定
            this.lock();
        }
        catch (Exception e)
        {
            logger.debug("锁定失败");
        }
        try
        {
            // 输入GA解锁
            JSONObject obj = this.unlock(otp);
            logger.debug("unlock:" + obj.toJSONString());
            if (obj.get("error") != null) { throw new BusinessException(obj.toJSONString()); }
        }
        catch (Exception e)
        {
            logger.debug("unlock error");
            throw new BusinessException("GA ERROR");
        }
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能为空"); }
        if (StringUtils.isBlank(address)) { throw new BusinessException("目标地址不能为空"); }
        if (null == amount || amount <= 0) { throw new BusinessException("发送金额必须大于0"); }
        if (StringUtils.isBlank(token)) { throw new BusinessException("token不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        if (StringUtils.isBlank(passphrase)) { throw new BusinessException("密码不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_sendcoins, bitgo_local, coin, walletId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("address", address);
        jsonParam.put("amount", amount);
        // jsonParam.put("minConfirms", minConfirms);
        // jsonParam.put("freeRate", minConfirms);
        jsonParam.put("walletPassphrase", passphrase);
        String content = "";
        try
        {
            content = super.httpPostWithJSON(client, url, this.getHeader(token), jsonParam, null);
            JSONObject obj = JSONObject.parseObject(content);
            if (obj.get("error") != null)
            {
                if (obj.get("pendingApproval") == null) { throw new BusinessException(obj.toJSONString()); }
            }
        }
        catch (Exception e)
        {
            logger.debug("Send Transaction error");
            throw new BusinessException("Send Transaction err");
        }
        logger.info("单笔划拨应答报文：" + content);
        JSONObject json = JSONObject.parseObject(content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        if (json.get("txid") != null)
        {
            bitPayModel.setHash(json.getString("txid"));
        }
        return bitPayModel;
    }
    
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
    @Override
    public BitPayModel sendMultipleCoins(String walletId, List<RecipientModel> recipientList, String coin, String token, String passphrase, Integer minConfirms)
            throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能为空"); }
        if (StringUtils.isBlank(token)) { throw new BusinessException("token不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        if (StringUtils.isBlank(passphrase)) { throw new BusinessException("密码不能空"); }
        if (CollectionUtils.isEmpty(recipientList)) { throw new BusinessException("目标集合不存在"); }
        Iterator<RecipientModel> iterator = recipientList.iterator();
        while (iterator.hasNext())
        {
            RecipientModel recipient = iterator.next();
            if (StringUtils.isBlank(recipient.getAddress())) { throw new BusinessException("目标地址不能为空"); }
            if (null == recipient.getAmount() || recipient.getAmount() <= 0) { throw new BusinessException("发送金额必须大于0"); }
        }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_sendmany, bitgo_local, coin, walletId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("recipients", recipientList);
        jsonParam.put("walletPassphrase", passphrase);
        jsonParam.put("minConfirms", minConfirms);
        String content = super.httpPostWithJSON(client, url, this.getHeader(token), jsonParam, null);
        logger.info("多笔划拨应答报文：" + content);
        JSONObject json = JSONObject.parseObject(content);
        return json.toJavaObject(BitPayModel.class);
    }
    
    /**
     * 单笔交易查询
     * @param coin   币种
     * @param walletId   钱包ID
     * @param transId   交易ID
     * @return
     * @author 张春喜 2018-01-19
     */
    @Override
    public BitPayModel transQuery(String coin, String walletId, String transId) throws BusinessException
    {
        if (StringUtils.isBlank(transId)) { throw new BusinessException("交易ID不能空"); }
        if (StringUtils.isBlank(bitgo_access_token)) { throw new BusinessException("token不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_trans_query, bitgo_root, coin, walletId, transId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("token", bitgo_access_token);
        String content = super.httpGetWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        JSONObject json = this.validate(content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        return bitPayModel;
    }
    
    /**
     * 获取单个钱包地址 Get Single Wallet Address 获取钱包中一个地址的信息。亦可被用于检查钱包中是否存在某个地址。
     * @param coin    币种
     * @param address     地址
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19
     */
    @Override
    public String getSingleWalletAddressInfo(String coin, String address) throws BusinessException
    {
        if (StringUtils.isBlank(bitgo_access_token)) { throw new BusinessException("token不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        if (StringUtils.isBlank(address)) { throw new BusinessException("地址不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_single_info, bitgo_root, coin, address);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("token", bitgo_access_token);
        String content = super.httpGetWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return content;
    }
    
    /**
     * 获取待审核列表
     * @param coin
     * @return
     * @throws BusinessException
     */
    @Override
    public JSONObject getPendingApprovalsList(String coin) throws BusinessException
    {
        if (StringUtils.isBlank(bitgo_access_token)) { throw new BusinessException("token不能空"); }
        if (StringUtils.isBlank(coin)) { throw new BusinessException("币种不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_pendingapproval_list, bitgo_root, coin);
        JSONObject jsonParam = new JSONObject();
        String content = super.httpGetWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        logger.info("获取待审核列表:应答报文：" + content);
        JSONObject json = JSONObject.parseObject(content);
        return json;
    }
    
    /**
     * 获取单条待审核
     * @param coin
     * @param pendingapprovalid
     * @return
     * @throws BusinessException
     */
    @Override
    public JSONObject getSinglePendingApprovals(String coin, String pendingapprovalid) throws BusinessException
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_pendingapproval_single, bitgo_root, coin, pendingapprovalid);
        JSONObject jsonParam = new JSONObject();
        String content = super.httpGetWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        logger.info("获取单条待审核 应答报文：" + content);
        JSONObject json = JSONObject.parseObject(content);
        return json;
    }
    
    /**
     * 更新钱包密码
     * @param oldWalletPass 旧密码
     * @param newWalletPass 新密码
     * @param encryptedXprv 加密后的私钥
     * @param xpub  公钥
     * @return
     * @throws BusinessException
     * @author 张春喜 2018-01-19 15:47:04
     */
    @Override
    public String updateKeychain(String coin, String oldWalletPass, String newWalletPass, String encryptedXprv, String xpub, String token) throws BusinessException
    {
        if (StringUtils.isBlank(oldWalletPass)) { throw new BusinessException("钱包旧密码不能为空"); }
        HttpClient client = HttpUtils.getHttpClient();
        // 使用旧密码解密私钥
        String url = MessageFormat.format(bitgo_utils_decrypt, bitgo_local);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("password", oldWalletPass);
        jsonParam.put("input", encryptedXprv);
        String content = super.httpPostWithJSON(client, url, null, jsonParam, null);
        JSONObject json = this.validate(content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        // 私钥
        String xprv = bitPayModel.getDecrypted();
        // 使用新密码加密私钥
        url = MessageFormat.format(bitgo_utils_encrypt, bitgo_local);
        jsonParam.clear();
        jsonParam.put("password", newWalletPass);
        jsonParam.put("input", xprv);
        content = super.httpPostWithJSON(client, url, null, jsonParam, null);
        json = this.validate(content);
        bitPayModel = json.toJavaObject(BitPayModel.class);
        encryptedXprv = bitPayModel.getEncrypted();
        // 更新钱包密码
        url = MessageFormat.format(bitgo_wallet_keychain_update, bitgo_local);
        jsonParam.clear();
        jsonParam.put("pub", xpub);
        jsonParam.put("encryptedXprv", encryptedXprv);
        jsonParam.put("type", coin);
        content = super.httpPutWithJSON(client, url, this.getHeader(token), jsonParam, null);
        this.validate(content);
        return encryptedXprv;
    }
    
    @Override
    public JSONObject login(String email, String loginPwd, String otp)
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_login, bitgo_root);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("email", email);
        jsonParam.put("password", loginPwd);
        jsonParam.put("otp", otp);
        String content = super.httpPostWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }
    
    @Override
    public JSONObject unlock(String otp)
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_unlock, bitgo_root);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("otp", otp);
        jsonParam.put("duration", bitgo_wallet_unlock_duration);
        String content = super.httpPostWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }
    
    @Override
    public JSONObject lock()
    {
        HttpClient client = HttpUtils.getHttpClient();
        // 使用旧密码解密私钥
        String url = MessageFormat.format(bitgo_wallet_lock, bitgo_root);
        JSONObject jsonParam = new JSONObject();
        String content = super.httpPostWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }
    
    @Override
    public JSONObject session()
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_session, bitgo_root);
        JSONObject jsonParam = new JSONObject();
        String content = super.httpGetWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }
    
    /**
     * 添加hook
     * @param coin
     * @param walletId
     * @param backUrl
     * @param type=transfer
     * @return
     */
    @Override
    public JSONObject addWebHook(String coin, String walletId, String backUrl, String type)
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_add_webhook, bitgo_root, coin, walletId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("url", backUrl);
        jsonParam.put("type", type);
        String content = super.httpPostWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }

    /**
     * 移除webhook
     * @param coin
     * @param walletId
     * @param backUrl
     * @param type=transfer
     * @return
     */
    @Override
    public JSONObject removeWebHook(String coin, String walletId, String backUrl, String type)
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_remove_webhook, bitgo_root, coin, walletId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("url", backUrl);
        jsonParam.put("type", type);
        String content = super.httpDeleteWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }

    /**
     * 获取webhook列表
     * @param coin
     * @param walletId
     * @return
     */
    @Override
    public JSONObject listWebHook(String coin, String walletId)
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_list_webhook, bitgo_root, coin, walletId);
        JSONObject jsonParam = new JSONObject();
        String content = super.httpGetWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }

    /**
     * 创建钱包
     * @param coin
     * @param label 标签
     * @Param passphrase 密码
     * @return
     */
    public JSONObject createWallet(String coin, String label,String passphrase,String enterprise)
    {
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_generate, bitgo_local, coin);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("passphrase",passphrase);
        jsonParam.put("label",label);
        jsonParam.put("enterprise",enterprise);
        String content = super.httpPostWithJSON(client, url, this.getHeader(bitgo_access_token), jsonParam, null);
        return JSON.parseObject(content);
    }
    
    public static void main(String[] args)
    {

//        New token with the label 'BTCTX' created. Please save it securely now:
//    v2xe0a4167979078cb2866f072e4cac2ac2ad2e2dd5e42ea06f33d0a59aac09c569
        BitGoRemoteV2ServiceImpl bg = new BitGoRemoteV2ServiceImpl();
        // 测试用的基本参数
        String address = "3ACDXjfXaNJdNKz4mowJ3gYZtqSvTjVsis";
        // 5a7157115a6d65690740976130f99838 5a6099577beedd2b07f6d82a078a1b3f
        String walletId = "5a608d7e1b1c79370783b520b81c32e4";
        long amount = 100000L;
        long fee = 100000L;
        String token = "v2xe0a4167979078cb2866f072e4cac2ac2ad2e2dd5e42ea06f33d0a59aac09c569";
        String passwd = "Duanyi@99";
        String passphrase = "Duanyi@99";
        String coin = "btc";

        String backUrl = "http://www.biex.com/callback/bitgoCallback/webhook/v2/btc";
//        backUrl = "http://www.biex.com/callback/bitgoCallback/webhook";

        // 创建钱包
//        JSONObject a = bg.createWallet("eth","groupeth001","Duanyi@99","5a607a8f90475d310775c40815a804d6");
//        System.out.println(a.toJSONString());

        // 获取webhook列表
//        JSONObject a = bg.listWebHook(coin,walletId);
//        System.out.println(a.toJSONString());

       // 添加webhook
//        a = bg.addWebHook(coin,walletId,backUrl,"transfer");
//        System.out.println(a.toJSONString());

        // 移除
//        JSONObject a = bg.removeWebHook(coin,walletId,backUrl,"transfer");
//        System.out.println(a.toJSONString());


        // bg.lock();
        // JSONObject j = bg.unlock("322164");
        // System.out.println(j.toJSONString());
        // bg.session();
        // bg.lock();
        // bg.login();
        // bg.session();
        // bg.logout();
        // bg.login();
        // bg.lock();
        // bg.logout();
        // 获取单条待审核
        // JSONObject a = bg.getSinglePendingApprovals(coin,"5a671afb8e0b933e07540b16c2f4afe0");
        // System.out.println(a.toJSONString()); /** //5a6097055eba9f3e0777e095cd08d90d这条已通过 5a619d8436bfc44007122cc468cdd5e8拒绝*/
        // 获取待审核列表
        /**JSONObject a = bg.getPendingApprovalsList( token,  coin);
         System.out.println(a.toJSONString());*/
        // 获取单个钱包地址
        /**String b = bg.getSingleWalletAddressInfo( token, coin,  "3AsPwpoy156WsowB98GU93sn2kZVqhwzaK");
         System.out.println("content:" + b);*/
        // 单笔交易查询
        String eosWalletId= "5a66052cb5803f540758e6859b9b2f54";
        BitPayModel b = bg.transQuery("eos", eosWalletId, "0x4722e0f35330a4da23e1a63501105b8de14bbab0f228b9b737b833af3cd49308");
        System.out.println(b.toString());

//        BitPayModel b = bg.transQuery(coin, walletId, "76a0a618bfca5a2dfb7540754df8afe7629cd37c25b76eef8386645097859835");
//        System.out.println(b.toString());/***/
        // 多笔交易
        /** List<RecipientModel> recipientList = new ArrayList<RecipientModel>();
         RecipientModel r = null;
         for(int i =0;i <2;i ++)
         {
         r = new RecipientModel();
         r.setAddress(address);
         r.setAmount(amount);
         recipientList.add(r);
         }
         BitPayModel b = bg.sendMultipleCoins( walletId,  recipientList, coin,  token,  passphrase);
         System.out.println(b.toString());
         //5a618b49b6860f3607a117706c1da541*/
        // // 单笔交易
        /**address="17gszQnj3mq3a2wHiMoZupFqpJUZkw22QG";
         BitPayModel b = bg.sendCoins( walletId, address, amount, coin, token, passphrase,2,"827265");
         System.out.println(b.toString());*/
        // 创建钱包地址
        /**BitPayModel b = bg.createWalletAddress(walletId,token,"btc");
         System.out.println(b.toString());*/
        // 获取钱包信息
//        String ethWalletId= "5a66052cb5803f540758e6859b9b2f54";
//        BitPayModel b = bg.getWalletInfo(ethWalletId,"eth");
//         System.out.println(b.toString());
//        {"id":"5a66052cb5803f540758e6859b9b2f54","users":[{"user":"59797a05dd9b328c0751fb9703dd14c1","permissions":["admin","view","spend"]},{"user":"5a60760ff6c17f3207b5f5825cd8a70f","permissions":["admin","spend","view"]}],"coin":"eth","label":"BIEXZF","m":2,"n":3,"keys":["5a6601ac736ade3407adba883b6d3ece","5a6601ad3843fc4107d222e71b1b2f44","59f28113cb548c8d1313df6a00efe498"],"enterprise":"597b2b0664755aa5075c39c276184d98","tags":["5a66052cb5803f540758e6859b9b2f54","597b2b0664755aa5075c39c276184d98"],"disableTransactionNotifications":false,"freeze":{},"deleted":false,"approvalsRequired":1,"isCold":false,"coinSpecific":{"deployedInBlock":false,"deployTxHash":"0x87c46081a44033660dc15ceab041421c1f6f0ee405a7a77bbdcad6cbc6bbe7af","lastChainIndex":{"0":7,"1":-1},"baseAddress":"0xff3951053bfdbbe7d1ac78bef527776fd4dc32de","feeAddress":"0xe00eddb908fb185dc170fa9b1fa74d531296c97d","pendingChainInitialization":false,"creationFailure":[]},"admin":{"policy":{"id":"5a66052cb5803f540758e688a6c43037","version":0,"date":"2018-01-22T15:37:16.394Z","mutableUpToDate":"2018-01-24T15:37:16.394Z","rules":[]}},"clientFlags":[],"allowBackupKeySigning":false,"balanceString":"0","confirmedBalanceString":"0","spendableBalanceString":"0","receiveAddress":{"id":"5a8e62ff4d80ef597f34f30e6ef5d11e","address":"0xe2c910cde030f7880ae40c9a24cf9e3ee3296223","chain":0,"index":7,"coin":"eth","lastNonce":0,"wallet":"5a66052cb5803f540758e6859b9b2f54","coinSpecific":{"nonce":-1,"updateTime":"2018-02-22T06:28:15.331Z","txCount":0,"pendingChainInitialization":false,"creationFailure":[]}},"pendingApprovals":[]}

//        String eosWalletId= "5a66052cb5803f540758e6859b9b2f54";
//        BitPayModel b = bg.getWalletInfo(eosWalletId,"eos");
//        System.out.println(b.toString());
//{"id":"5a66052cb5803f540758e6859b9b2f54","users":[{"user":"59797a05dd9b328c0751fb9703dd14c1","permissions":["admin","view","spend"]},{"user":"5a60760ff6c17f3207b5f5825cd8a70f","permissions":["admin","spend","view"]}],"coin":"eth","label":"BIEXZF","m":2,"n":3,"keys":["5a6601ac736ade3407adba883b6d3ece","5a6601ad3843fc4107d222e71b1b2f44","59f28113cb548c8d1313df6a00efe498"],"enterprise":"597b2b0664755aa5075c39c276184d98","tags":["5a66052cb5803f540758e6859b9b2f54","597b2b0664755aa5075c39c276184d98"],"disableTransactionNotifications":false,"freeze":{},"deleted":false,"approvalsRequired":1,"isCold":false,"coinSpecific":{"deployedInBlock":false,"deployTxHash":"0x87c46081a44033660dc15ceab041421c1f6f0ee405a7a77bbdcad6cbc6bbe7af","lastChainIndex":{"0":7,"1":-1},"baseAddress":"0xff3951053bfdbbe7d1ac78bef527776fd4dc32de","feeAddress":"0xe00eddb908fb185dc170fa9b1fa74d531296c97d","pendingChainInitialization":false,"creationFailure":[]},"admin":{"policy":{"id":"5a66052cb5803f540758e688a6c43037","version":0,"date":"2018-01-22T15:37:16.394Z","mutableUpToDate":"2018-01-24T15:37:16.394Z","rules":[]}},"clientFlags":[],"allowBackupKeySigning":false,"balanceString":"3190000000000000000","confirmedBalanceString":"3190000000000000000","spendableBalanceString":"3190000000000000000","receiveAddress":{"id":"5a8e62ff4d80ef597f34f30e6ef5d11e","address":"0xe2c910cde030f7880ae40c9a24cf9e3ee3296223","chain":0,"index":7,"coin":"eth","lastNonce":0,"wallet":"5a66052cb5803f540758e6859b9b2f54","coinSpecific":{"nonce":-1,"updateTime":"2018-02-22T06:28:15.331Z","txCount":0,"pendingChainInitialization":false,"creationFailure":[]}},"pendingApprovals":[]}

        // 测试读取钱包
        /**BitPayModel b = bg.getWallet("BIEXCZ", "btc");
         System.out.println(b.toString());
        */
    }
}
