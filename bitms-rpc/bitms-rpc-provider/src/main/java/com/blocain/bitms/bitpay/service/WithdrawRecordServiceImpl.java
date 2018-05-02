/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.service;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.entity.WithdrawRecord;
import com.blocain.bitms.bitpay.mapper.WithdrawRecordMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.RecipientModel;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账户提现记录 服务实现类
 * <p>File：WithdrawRecordServiceImpl.java </p>
 * <p>Title: WithdrawRecordServiceImpl </p>
 * <p>Description:WithdrawRecordServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class WithdrawRecordServiceImpl extends GenericServiceImpl<WithdrawRecord> implements WithdrawRecordService
{
    private WithdrawRecordMapper withdrawRecordMapper;
    
    @Autowired
    public WithdrawRecordServiceImpl(WithdrawRecordMapper withdrawRecordMapper)
    {
        super(withdrawRecordMapper);
        this.withdrawRecordMapper = withdrawRecordMapper;
    }
    
    @Autowired(required = false)
    private BitpayKeychainService   bitpayKeychainService;
    
    @Autowired
    private BitGoRemoteV2Service    bitGoRemoteService;
    
    @Autowired(required = false)
    private WithdrawRecordService   withdrawRecordService;
    
    @Autowired(required = false)
    private RedisTemplate           redisTemplate;
    
    protected final PropertiesUtils properties = new PropertiesUtils("wallet.properties");
    
    @Override
    public void doSingleCashWthdrawal(Long id, String password,String otp) throws BusinessException
    {
        if (null == id) { throw new BusinessException("提现ID不能为空"); }
        WithdrawRecord withdrawRecord = withdrawRecordService.selectByPrimaryKey(id);
        if (null == withdrawRecord) { throw new BusinessException("提现记录不存在"); }
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.BITPAY_WITHDRAW)// 加入模块标识
                .append(id).toString();
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                if (0 != withdrawRecord.getState()) { throw new BusinessException("此提现记录已提现"); }
                // 获取付款钱包
                BitpayKeychain bitpayKeychain = new BitpayKeychain();
                bitpayKeychain.setType(2);
                bitpayKeychain.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                List<BitpayKeychain> listBitpay = bitpayKeychainService.findList(bitpayKeychain);
                if (listBitpay.size() == 0) { throw new BusinessException("付款钱包不存在"); }
                bitpayKeychain = listBitpay.get(0);
                try
                {
                    if (StringUtils.isNotBlank(bitpayKeychain.getSystemPass()))
                    {
                        password = passDecryption(bitpayKeychain, password);
                    }
                    // 调用外部接口交易
                    // BitPayModel bitPayModel = bitGoRemoteService.sendCoins(bitpayKeychain.getWalletId(), withdrawRecord.getRaiseAddr(),
                            //chargAmount(withdrawRecord.getOccurAmt()), chargAmount(withdrawRecord.getNetFee()), bitpayKeychain.getToken(), password);
                    BitPayModel bitPayModel = bitGoRemoteService.sendCoins(bitpayKeychain.getWalletId(), withdrawRecord.getRaiseAddr(),
                            chargAmount(withdrawRecord.getOccurAmt()), "btc", bitpayKeychain.getToken(), password, bitpayKeychain.getFeeTxConfirmTarget(),otp);
                    withdrawRecord.setState(1);
                    withdrawRecord.setTransId(bitPayModel.getHash());
                    withdrawRecordService.updateByPrimaryKey(withdrawRecord);
                    try
                    {
                        // 回调交易平台，添加交易ID易
                        List<Map<String, String>> callbackList = Lists.newArrayList();
                        Map<String, String> withdrawMap = Maps.newHashMap();
                        withdrawMap.put("id", withdrawRecord.getId().toString());
                        withdrawMap.put("transId", withdrawRecord.getTransId());
                        callbackList.add(withdrawMap);
                        Map<String, String> param = Maps.newHashMap();
                        String list = JSONObject.toJSONString(callbackList);
                        param.put("list", list);
                        this.clientPost(param, properties.getProperty("bitms.client.transfer.status"));
                    }
                    catch (Exception e)
                    {
                        throw new BusinessException("交易成功，回调失败：" + e.getMessage());
                    }
                }
                catch (BusinessException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                    throw new BusinessException("提现交易失败" + e.getMessage());
                }
            }
            catch (BusinessException e)
            {
                logger.error("comfirm 错误:" + e.getMessage(), e);
                throw new BusinessException(e.getErrorCode());
            }
            finally
            {
                redisLock.unlock();
            }
        }
        else
        {
            logger.error("confirm acquireLock failed");
            throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    @Override
    public void doMultipleCashWthdrawal(String ids, String password) throws BusinessException
    {
        String[] idAry = ids.split(",");
        if (ArrayUtils.isEmpty(idAry)) { throw new BusinessException("至少选择一条交易记录"); }
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.BITPAY_WITHDRAW)// 加入模块标识
                .append("confirmBatchOnlyOne").toString();// 只能一个人操作
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                List<RecipientModel> recipientList = Lists.newArrayList();
                List<WithdrawRecord> withdrawList = Lists.newArrayList();
                for (String id : idAry)
                {
                    WithdrawRecord withdrawRecord = withdrawRecordService.selectByPrimaryKey(Long.parseLong(id));
                    Integer state = 0;
                    if (!withdrawRecord.getState().equals(state)) { throw new BusinessException("存在重复提现 提现记录ID=" + id); }
                    // 组装付款地址集合
                    if (null != withdrawRecord)
                    {
                        RecipientModel recipientModel = new RecipientModel();
                        recipientModel.setAddress(withdrawRecord.getRaiseAddr());
                        recipientModel.setAmount(chargAmount(withdrawRecord.getOccurAmt()));
                        recipientList.add(recipientModel);
                        withdrawList.add(withdrawRecord);
                    }
                }
                if (CollectionUtils.isNotEmpty(recipientList))
                {
                    // 获取付款钱包
                    BitpayKeychain bitpayKeychain = new BitpayKeychain();
                    bitpayKeychain.setType(2);
                    bitpayKeychain.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    List<BitpayKeychain> listBitpay = bitpayKeychainService.findList(bitpayKeychain);
                    if (listBitpay.size() == 0) { throw new BusinessException("付款钱包不存在"); }
                    bitpayKeychain = listBitpay.get(0);
                    try
                    {
                        if (StringUtils.isNotBlank(bitpayKeychain.getSystemPass()))
                        {
                            password = passDecryption(bitpayKeychain, password);
                        }
                        // 调用外部接口交易
                        // BitPayModel bitPayModel = bitGoRemoteService.sendMultipleCoins(bitpayKeychain.getWalletId(), recipientList, bitpayKeychain.getFeeTxConfirmTarget(),
                                // bitpayKeychain.getToken(), password);
                        BitPayModel bitPayModel = bitGoRemoteService.sendMultipleCoins(bitpayKeychain.getWalletId(), recipientList,
                                "btc", bitpayKeychain.getToken(), password, bitpayKeychain.getFeeTxConfirmTarget());
                        List<Map<String, String>> callbackList = Lists.newArrayList();
                        for (WithdrawRecord item : withdrawList)
                        {
                            item.setState(1);
                            item.setTransId(bitPayModel.getHash());
                            Map<String, String> withdrawMap = Maps.newHashMap();
                            withdrawMap.put("id", item.getId().toString());
                            withdrawMap.put("transId", item.getTransId());
                            callbackList.add(withdrawMap);
                        }
                        withdrawRecordService.updateBatch(withdrawList);
                        try
                        {
                            if (CollectionUtils.isNotEmpty(callbackList))
                            {
                                // 回调交易平台，添加交易ID
                                Map<String, String> param = Maps.newHashMap();
                                String list = JSONObject.toJSONString(callbackList);
                                param.put("list", list);
                                this.clientPost(param, this.properties.getProperty("bitms.client.transfer.status"));
                            }
                        }
                        catch (Exception e)
                        {
                            throw new BusinessException("交易成功，回调失败：" + e.getMessage());
                        }
                    }
                    catch (BusinessException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        logger.error(e.getMessage(), e);
                        throw new BusinessException("批量提现交易失败" + e.getMessage());
                    }
                }
            }
            catch (BusinessException e)
            {
                logger.error("confirmBatch 错误:" + e.getMessage(), e);
                throw new BusinessException(e.getErrorCode());
            }
            finally
            {
                redisLock.unlock();
            }
        }
        else
        {
            logger.error("confirmBatch acquireLock failed");
            throw new BusinessException(CommonEnums.FAIL);
        }
    }
    
    protected String passDecryption(BitpayKeychain bitpayKeychain, String payPass) throws BusinessException
    {
        // 根据旧密码解密钱包密码
        payPass = StringMerge.processString(payPass, bitpayKeychain.getSystemPass());
        payPass = AES256.decrypt(bitpayKeychain.getCiphertext(), payPass);
        if (null == payPass) { throw new BusinessException("支付密码错误"); }
        return payPass;
    }
    
    protected Long chargAmount(BigDecimal amount)
    {
        Long btcAmount = 0L;
        if (null != amount)
        {
            btcAmount = amount.multiply(new BigDecimal(100000000)).longValue();
        }
        return btcAmount;
    }
    
    private JSONObject clientPost(Map<String, String> param, String url) throws BusinessException
    {
        // 从远程获取数据
        HttpClient client = HttpUtils.getHttpClient();
        String data = ParameterUtils.getDataFromMap(param);
        int dataLen = ValidateUtils.length(data);
        String userDes = ParameterUtils.getUserDes(properties.getProperty("bitms.client.userkey"), dataLen);
        Map<String, String> httpMap = Maps.newHashMap();
        httpMap.put("userKey", properties.getProperty("bitms.client.userkey"));
        httpMap.put("dataLen", String.valueOf(dataLen));
        httpMap.put("userDes", userDes);
        httpMap.put("data", data);
        String content = HttpUtils.post(client, url, httpMap);
        return this.validate(content);
    }
    
    private JSONObject validate(String content) throws BusinessException
    {
        JSONObject json = JSONObject.parseObject(content);
        String code = json.getString("code");
        String message = json.getString("message");
        if (!"200".equals(code)) { throw new BusinessException(message); }
        return json;
    }
}
