/*
 * @(#)BitGoRemoteImpl.java 2017年7月7日 上午10:09:32
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.impl;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.blocain.bitms.payment.basic.BasicServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.RecipientModel;
import com.blocain.bitms.payment.bitgo.BitGoRemoteService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Maps;

/**
 * <p>File：BitGoRemoteImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 上午10:09:32</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Component("bitGoRemote")
public class BitGoRemoteServiceImpl extends BasicServiceImpl implements BitGoRemoteService
{
    public static final Logger logger                       = LoggerFactory.getLogger(BitGoRemoteServiceImpl.class);

    public static final String bitgo_root                   = properties.getProperty("bitgo.root");

    public static final String bitgo_local                  = properties.getProperty("bitgo.localhost.root.prop");

    public static final String bitgo_access_token           = properties.getProperty("bitgo.access.token.prop");

    public static final String bitgo_utils_decrypt          = properties.getProperty("bitgo.utils.decrypt");

    public static final String bitgo_utils_encrypt          = properties.getProperty("bitgo.utils.encrypt");

    public static final String bitgo_wallet_list            = properties.getProperty("bitgo.wallet.list");

    public static final String bitgo_wallet_info            = properties.getProperty("bitgo.wallet.info");

    public static final String bitgo_wallet_address         = properties.getProperty("bitgo.wallet.address");

    public static final String bitgo_wallet_sendmany        = properties.getProperty("bitgo.wallet.sendmany");

    public static final String bitgo_wallet_sendcoins       = properties.getProperty("bitgo.wallet.sendcoins");

    public static final String bitgo_wallet_trans_query     = properties.getProperty("bitgo.wallet.trans.query");

    public static final String bitgo_wallet_single_info     = properties.getProperty("bitgo.wallet.getSingleWalletAddressInfo");

    public static final String bitgo_wallet_keychain_update = properties.getProperty("bitgo.wallet.keychain.update");

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#getWallet(java.lang.String)
     */
    @Override
    public BitPayModel getWallet(String walletName) throws BusinessException
    {
        return this.getWallet(walletName, null);
    }

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#getWallet(java.lang.String, java.lang.String)
     */
    @Override
    public BitPayModel getWallet(String walletName, String token) throws BusinessException
    {
        if (StringUtils.isBlank(walletName)) { throw new BusinessException("钱包名称不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_list, bitgo_root);
        String content = HttpUtils.get(client, url, null, null, this.getHeader(token));
        JSONObject json = this.validate(content);
        JSONArray jsonArray = json.getJSONArray("wallets");
        if (jsonArray.isEmpty()) { throw new BusinessException("钱包不存在"); }
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
        throw new BusinessException("钱包不存在");
    }

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#getWalletInfo(java.lang.String, java.lang.String)
     */
    @Override
    public BitPayModel getWalletInfo(String walletId, String token) throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_info, bitgo_root, walletId);
        String content = HttpUtils.get(client, url, null, null, this.getHeader(token));
        JSONObject json = this.validate(content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        JSONObject tempJson = json.getJSONObject("private");
        JSONArray tempJsonArry = tempJson.getJSONArray("keychains");
        if (!tempJsonArry.isEmpty())
        {
            tempJson = tempJsonArry.getJSONObject(0);
            bitPayModel.setXpub(tempJson.getString("xpub"));
        }
        return bitPayModel;
    }

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

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#createWalletAddress(java.lang.String)
     */
    @Override
    public BitPayModel createWalletAddress(String walletId) throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_address, bitgo_root, walletId, 0);
        String content = HttpUtils.post(client, url, null, null, this.getHeader(null));
        JSONObject json = this.validate(content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        if (null == bitPayModel || StringUtils.isBlank(bitPayModel.getAddress())) { throw new BusinessException("钱包地址生成失败"); }
        return bitPayModel;
    }

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#sendCoins(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public BitPayModel sendCoins(String walletId, String address, Long amount, Long fee, String token, String passphrase) throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能为空"); }
        if (StringUtils.isBlank(address)) { throw new BusinessException("目标地址不能为空"); }
        if (null == amount || amount <= 0) { throw new BusinessException("发送金额必须大于0"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_sendcoins, bitgo_local, walletId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("address", address);
        jsonParam.put("amount", amount);
        if (null != fee)
        {
            jsonParam.put("fee", fee);
        }
        jsonParam.put("walletPassphrase", passphrase);
        String content = super.httpPostWithJSON(client, url, this.getHeader(token), jsonParam, null);
        logger.info("单笔划拨应答报文："+content);
        JSONObject json = this.validate(content);
        return json.toJavaObject(BitPayModel.class);
    }

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#sendMultipleCoins(java.lang.String, java.util.List, java.lang.Integer, java.lang.String)
     */
    @Override
    public BitPayModel sendMultipleCoins(String walletId, List<RecipientModel> recipientList, Integer feeTxConfirmTarget, String token, String passphrase)
            throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能为空"); }
        if (CollectionUtils.isEmpty(recipientList)) { throw new BusinessException("目标集合不存在"); }
        Iterator<RecipientModel> iterator = recipientList.iterator();
        while (iterator.hasNext())
        {
            RecipientModel recipient = iterator.next();
            if (StringUtils.isBlank(recipient.getAddress())) { throw new BusinessException("目标地址不能为空"); }
            if (null == recipient.getAmount() || recipient.getAmount() <= 0) { throw new BusinessException("发送金额必须大于0"); }
        }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_sendmany, bitgo_local, walletId);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("recipients", recipientList);
        jsonParam.put("feeTxConfirmTarget", feeTxConfirmTarget);
        jsonParam.put("walletPassphrase", passphrase);
        String content = super.httpPostWithJSON(client, url, this.getHeader(token), jsonParam, null);
        logger.info("多笔划拨应答报文："+content);
        JSONObject json = this.validate(content);
        return json.toJavaObject(BitPayModel.class);
    }

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#transQuery(java.lang.String)
     */
    @Override
    public BitPayModel transQuery(String transId) throws BusinessException
    {
        if (StringUtils.isBlank(transId)) { throw new BusinessException("交易ID不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_trans_query, bitgo_root, transId);
        String content = HttpUtils.get(client, url);
        JSONObject json = this.validate(content);
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        return bitPayModel;
    }

    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BitGoRemote#updateKeychain(java.lang.String)
     */
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
    @Override
    public String updateKeychain(String walletPass, String newWalletPass, String encryptedXprv, String xpub, String token) throws BusinessException
    {
        if (StringUtils.isBlank(walletPass)) { throw new BusinessException("钱包旧密码不能为空"); }
        HttpClient client = HttpUtils.getHttpClient();
        // 使用旧密码解密私钥
        String url = MessageFormat.format(bitgo_utils_decrypt, bitgo_local);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("password", walletPass);
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
        url = MessageFormat.format(bitgo_wallet_keychain_update, bitgo_local, xpub);
        jsonParam.clear();
        jsonParam.put("encryptedXprv", encryptedXprv);
        content = super.httpPutWithJSON(client, url, this.getHeader(token), jsonParam, null);
        this.validate(content);
        return encryptedXprv;
    }

    /**
     * 获取单个钱包地址 Get Single Wallet Address 获取钱包中一个地址的信息。亦可被用于检查钱包中是否存在某个地址。
     * @param walletId    钱包
     * @param address     地址
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年8月25日 下午4:53:39
     */
    @Override
    public String getSingleWalletAddressInfo(String walletId, String address) throws BusinessException
    {
        if (StringUtils.isBlank(walletId)) { throw new BusinessException("钱包ID不能空"); }
        if (StringUtils.isBlank(address)) { throw new BusinessException("地址不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(bitgo_wallet_single_info, bitgo_root, walletId, address);
        String content = HttpUtils.get(client, url, null, null, this.getHeader(null));
        System.out.println("content:" + content);
        // JSONObject json = this.validate(content);
        // String returnAddress = (String) json.get("address");
        // System.out.println("returnAddress:" + returnAddress);
        return content;
    }


    public static void main(String[] args)
    {
        try
        {
            // BTC Test
            BitGoRemoteServiceImpl bg = new BitGoRemoteServiceImpl();
//            bg.sendCoins("38zcCoYUyC5SA3qfTsKcuJni6L5Yb4s5t3","31mjPhwXxpXn95FvixZPuWDdRQvjDxuFKy",
//                    100000l, 10000l, "v2x73b68acb5ea1e1685d684fa20ba5e4d5916af374942309cd927d80bb264a1433","xdsdfffff");
            BitPayModel bp = bg.getWallet("bitmsCZ");
            System.out.println("getWallet BitPayModel:" + bp.toString());
            bp = bg.getWalletInfo("38zcCoYUyC5SA3qfTsKcuJni6L5Yb4s5t3", "v2x73b68acb5ea1e1685d684fa20ba5e4d5916af374942309cd927d80bb264a1433");
            System.out.println("getWalletInfo BitPayModel:" + bp.toString());
            // bp = bg.createWalletAddress(bp.getId());
            // System.out.println("createWalletAddress:" + bp.toString());
            bp = bg.transQuery("7e40666c6cd75aa7efe9a638fb498367703e0f3d0e35d9d23dd0ec2613ee7955");
            System.out.println("transQuery BitPayModel:" + bp.toString());
            System.out.println("已确认区块节点数量 BitPayModel :" + bp.getConfirmations());
            bg.getSingleWalletAddressInfo("38zcCoYUyC5SA3qfTsKcuJni6L5Yb4s5t3", "3LHcQ9FqG18gXYRdQHaUjy3CiVhgRnZGqM");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
