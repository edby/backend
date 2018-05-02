/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;
import com.blocain.bitms.monitor.service.AcctAssetChkService;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.RecipientModel;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import com.blocain.bitms.tools.bean.ClientParameter;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.entity.AccountWithdrawRecord;
import com.blocain.bitms.trade.fund.mapper.AccountFundTransferMapper;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 账户资金划拨表 服务实现类
 * <p>File：AccountFundTransfer.java </p>
 * <p>Title: AccountFundTransfer </p>
 * <p>Description:AccountFundTransfer </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountFundTransferServiceImpl extends GenericServiceImpl<AccountFundTransfer> implements AccountFundTransferService
{
    private AccountFundTransferMapper    accountFundTransferMapper;
    
    @Autowired
    private AccountFundTransferService   accountTransferService;
    
    @Autowired
    private RedisTemplate                redisTemplate;
    
    @Autowired
    private AccountFundCurrentService    accountFundCurrentService;
    
    @Autowired
    private AccountWithdrawRecordService accountWithdrawRecordService;
    
    @Autowired
    private AccountService               accountService;
    
    @Autowired
    private AcctAssetChkService          acctAssetChkService;
    
    @Autowired
    private BitpayKeychainService        bitpayKeychainService;
    
    @Autowired
    private BitGoRemoteV2Service         bitGoRemoteService;
    
    @Autowired
    private StockInfoService             stockInfoService;
    
    @Autowired
    private FundService                  fundService;
    
    @Autowired
    private AccountFundTransferService   accountFundTransferService;
    
    @Autowired
    private AccountWalletAssetService    accountWalletAssetService;
    
    protected final PropertiesUtils      properties = new PropertiesUtils("wallet.properties");
    
    @Autowired
    public AccountFundTransferServiceImpl(AccountFundTransferMapper accountFundTransferMapper)
    {
        super(accountFundTransferMapper);
        this.accountFundTransferMapper = accountFundTransferMapper;
    }
    
    @Override
    public List<AccountFundTransfer> findTransferAddrList(Map<String, Object> addrMap)
    {
        return accountFundTransferMapper.findTransferAddrList(addrMap);
    }
    
    @Override
    public AccountFundTransfer findTransferAddr(AccountFundTransfer accountFundTransfer)
    {
        return accountFundTransferMapper.findTransferAddr(accountFundTransfer);
    }
    
    @Override
    public void updatePushAccountFundTransferStatus(ClientParameter param) throws BusinessException
    {
        String userKey = param.getUserKey();
        String userDes = param.getUserDes();
        Integer dataLen = param.getDataLen();
        String data = param.getData();
        logger.debug("pushAccountFundTransferStatus userKey:" + userKey);
        logger.debug("pushAccountFundTransferStatus userDes:" + userDes);
        logger.debug("pushAccountFundTransferStatus dataLen:" + dataLen);
        logger.debug("pushAccountFundTransferStatus data:" + data);
        if (StringUtils.isNotBlank(data))
        {
            Map<String, String> callbackMap = Maps.newHashMap();
            callbackMap = ParameterUtils.getMapFromData(data);
            logger.debug("pushAccountFundTransferStatus callbackMap list:" + callbackMap.get("list"));
            // 解析JSONArray
            for (AccountFundTransfer item : JSONArray.parseArray(callbackMap.get("list"), AccountFundTransfer.class))
            {
                logger.debug("pushAccountFundTransferStatus getTransId:" + item.getTransId());
                logger.debug("pushAccountFundTransferStatus getId:" + item.getId());
                AccountFundTransfer accountFundTransfer = accountTransferService.selectByPrimaryKey(item.getId());
                accountFundTransfer.setTransId(item.getTransId());
                accountFundTransfer.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                accountTransferService.updateByPrimaryKey(accountFundTransfer);
                logger.debug("pushAccountFundTransferStatus getOriginalCurrentId:" + accountFundTransfer.getOriginalCurrentId());
                AccountFundCurrent accountFundCurrent = accountFundCurrentService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(),
                        accountFundTransfer.getOriginalCurrentId());
                accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
                accountFundCurrent.setTransId(item.getTransId());
                accountFundCurrentService.updateByPrimaryKey(accountFundCurrent);
            }
        }
    }
    
    @Override
    public void doSingleCashWthdrawal(Long id, String password, String otp) throws BusinessException
    {
        if (null == id) { throw new BusinessException("提现ID不能为空"); }
        AccountFundTransfer transfer = accountTransferService.selectByPrimaryKey(id);
        checkAccountFundTransferDateValidate(transfer);// 校验提现和流水数据
        String lock = new StringBuffer(CacheConst.LOCK_PERFIX).append(BitmsConst.BITPAY_WITHDRAW)// 加入模块标识
                .append(id).toString();
        RedisLock redisLock = new RedisLock(redisTemplate, lock);
        if (redisLock.lock())
        {
            try
            {
                // 超级用户区块费用资产处理
                AccountWalletAsset accountWalletAsset95 = accountWalletAssetService.selectForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID,
                        FundConsts.WALLET_BTC_TYPE);
                if (null == accountWalletAsset95) { throw new BusinessException("super admin asset doesn't exist"); }
                if (!transfer.getTransferStatus().equals(
                        FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING)) { throw new BusinessException("此提现记录不是待划拨记录 status=" + transfer.getConfirmStatus()); }
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
                        password = EncryptUtils.desDecrypt(bitpayKeychain.getSystemPass());
                    }
                    FundModel fundModel = new FundModel();
                    fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    fundModel.setFee(transfer.getTransferFee());
                    fundModel.setBusinessFlag("doSingleCashWthdrawal");
                    fundModel.setAddress("");
                    AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
                    accountWalletAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    fundService.superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
                    // 调用外部接口交易
                    // BitPayModel bitPayModel = bitGoRemoteService.sendCoins(bitpayKeychain.getWalletId(),
                    // EncryptUtils.desDecrypt(transfer.getTargetWalletAddr().toString()),
                    // chargAmount(transfer.getTransferAmt()), chargAmount(transfer.getTransferFee()), bitpayKeychain.getToken(), password);
                    BitPayModel bitPayModel = bitGoRemoteService.sendCoins(bitpayKeychain.getWalletId(), EncryptUtils.desDecrypt(transfer.getTargetWalletAddr().toString()),
                            chargAmount(transfer.getTransferAmt()), "btc", bitpayKeychain.getToken(), password, bitpayKeychain.getFeeTxConfirmTarget(), otp);
                    // 回填划款状态
                    transfer.setRemark(bitPayModel.getPendingApproval());
                    transfer.setPendingApproval(bitPayModel.getPendingApproval());
                    transfer.setConfirmStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                    transfer.setTransId(bitPayModel.getHash());
                    transfer.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                    accountTransferService.updateByPrimaryKey(transfer);
                    // 回填划款资金流水状态以及交易ID
                    AccountFundCurrent accountFundCurrent = accountFundCurrentService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(),
                            transfer.getOriginalCurrentId());
                    accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                    accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
                    accountFundCurrent.setTransId(transfer.getTransId());
                    accountFundCurrentService.updateByPrimaryKey(accountFundCurrent);
                    /*
                     * try
                     * {
                     * // 回调交易平台，添加交易ID易
                     * List<Map<String, String>> callbackList = Lists.newArrayList();
                     * Map<String, String> withdrawMap = com.beust.jcommander.internal.Maps.newHashMap();
                     * withdrawMap.put("id", transfer.getId().toString());
                     * withdrawMap.put("transId", transfer.getTransId());
                     * callbackList.add(withdrawMap);
                     * Map<String, String> param = com.beust.jcommander.internal.Maps.newHashMap();
                     * String list = JSONObject.toJSONString(callbackList);
                     * param.put("list", list);
                     * this.clientPost(param, properties.getProperty("bitms.client.transfer.status"));
                     * }
                     * catch (Exception e)
                     * {
                     * throw new BusinessException("交易成功，回调失败：" + e.getMessage());
                     * }
                     */
                }
                catch (BusinessException e)
                {
                    logger.error("单笔提现交易失败:" + e.getMessage(), e);
                    throw e;
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                    throw new BusinessException("单笔提现交易失败" + e.getMessage());
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
    public void doMultipleCashWthdrawal(String ids, String password)
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
                // 超级用户区块费用资产处理
                AccountWalletAsset accountWalletAsset95 = accountWalletAssetService.selectForUpdate(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_NETFEE_ID,
                        FundConsts.WALLET_BTC_TYPE);
                if (null == accountWalletAsset95) { throw new BusinessException("super admin asset doesn't exist"); }
                List<RecipientModel> recipientList = Lists.newArrayList();
                List<AccountFundTransfer> withdrawList = Lists.newArrayList();
                for (String id : idAry)
                {
                    AccountFundTransfer transfer = accountTransferService.selectByPrimaryKey(Long.parseLong(id));
                    if (null == transfer) { throw new BusinessException("划拨记录不存在 id=" + id + " status=" + transfer.getConfirmStatus()); }
                    checkAccountFundTransferDateValidate(transfer);
                }
                // 获取付款钱包
                BitpayKeychain bitpayKeychain = new BitpayKeychain();
                bitpayKeychain.setType(2);
                bitpayKeychain.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                List<BitpayKeychain> listBitpay = bitpayKeychainService.findList(bitpayKeychain);
                if (listBitpay.size() == 0) { throw new BusinessException("付款钱包不存在"); }
                bitpayKeychain = listBitpay.get(0);
                for (String id : idAry)
                {
                    AccountFundTransfer transfer = accountTransferService.selectByPrimaryKey(Long.parseLong(id));
                    // 组装付款地址集合
                    if (null != transfer)
                    {
                        RecipientModel recipientModel = new RecipientModel();
                        recipientModel.setAddress(EncryptUtils.desDecrypt(transfer.getTargetWalletAddr().toString()));
                        recipientModel.setAmount(chargAmount(transfer.getTransferAmt()));
                        recipientList.add(recipientModel);
                        withdrawList.add(transfer);
                    }
                }
                if (CollectionUtils.isNotEmpty(recipientList))
                {
                    try
                    {
                        if (StringUtils.isNotBlank(bitpayKeychain.getSystemPass()))
                        {
                            password = passDecryption(bitpayKeychain, password);
                        }
                        // 调用外部接口交易
                        // BitPayModel bitPayModel = bitGoRemoteService.sendMultipleCoins(bitpayKeychain.getWalletId(), recipientList,
                        // bitpayKeychain.getFeeTxConfirmTarget(),
                        // bitpayKeychain.getToken(), password);
                        BitPayModel bitPayModel = bitGoRemoteService.sendMultipleCoins(bitpayKeychain.getWalletId(), recipientList, "btc", bitpayKeychain.getToken(),
                                password, bitpayKeychain.getFeeTxConfirmTarget());
                        // 内部更新
                        for (AccountFundTransfer item : withdrawList)
                        {
                            // 回填划款状态
                            item.setConfirmStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                            item.setTransId(bitPayModel.getHash());
                            item.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                            accountTransferService.updateByPrimaryKey(item);
                            // 回填划款资金流水状态以及交易ID
                            AccountFundCurrent accountFundCurrent = accountFundCurrentService
                                    .selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(), item.getOriginalCurrentId());
                            accountFundCurrent.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                            accountFundCurrent.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
                            accountFundCurrent.setTransId(item.getTransId());
                            accountFundCurrentService.updateByPrimaryKey(accountFundCurrent);
                        }
                        FundModel fundModel = new FundModel();
                        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                        fundModel.setFee(BigDecimal.valueOf(bitPayModel.getFee()).setScale(8, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100000000),
                                BigDecimal.ROUND_HALF_UP));
                        fundModel.setBusinessFlag("doMultipleCashWthdrawal");
                        fundModel.setAddress("");
                        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
                        accountWalletAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                        accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);
                        fundService.superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
                        /*
                         * List<Map<String, String>> callbackList = Lists.newArrayList();
                         * for (AccountFundTransfer item : withdrawList)
                         * {
                         * item.setConfirmStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                         * item.setTransId(bitPayModel.getHash());
                         * Map<String, String> withdrawMap = com.beust.jcommander.internal.Maps.newHashMap();
                         * withdrawMap.put("id", item.getId().toString());
                         * withdrawMap.put("transId", item.getTransId());
                         * callbackList.add(withdrawMap);
                         * }
                         * accountTransferService.updateBatch(withdrawList);
                         * try
                         * {
                         * if (CollectionUtils.isNotEmpty(callbackList))
                         * {
                         * // 回调交易平台，添加交易ID
                         * Map<String, String> param = com.beust.jcommander.internal.Maps.newHashMap();
                         * String list = JSONObject.toJSONString(callbackList);
                         * param.put("list", list);
                         * this.clientPost(param, this.properties.getProperty("bitms.client.transfer.status"));
                         * }
                         * }
                         * catch (Exception e)
                         * {
                         * throw new BusinessException("交易成功，回调失败：" + e.getMessage());
                         * }
                         */
                    }
                    catch (BusinessException e)
                    {
                        logger.error("批量提现交易失败:" + e.getMessage(), e);
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
    
    private JSONObject clientPost(Map<String, String> param, String url) throws BusinessException
    {
        // 从远程获取数据
        HttpClient client = HttpUtils.getHttpClient();
        String data = ParameterUtils.getDataFromMap(param);
        int dataLen = ValidateUtils.length(data);
        String userDes = ParameterUtils.getUserDes(properties.getProperty("bitms.client.userkey"), dataLen);
        Map<String, String> httpMap = com.google.common.collect.Maps.newHashMap();
        httpMap.put("userKey", properties.getProperty("bitms.client.userkey"));
        httpMap.put("dataLen", String.valueOf(dataLen));
        httpMap.put("userDes", userDes);
        httpMap.put("data", data);
        String content = HttpUtils.post(client, url, httpMap);
        return this.validate(content);
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
    
    private JSONObject validate(String content) throws BusinessException
    {
        JSONObject json = JSONObject.parseObject(content);
        String code = json.getString("code");
        String message = json.getString("message");
        if (!"200".equals(code)) { throw new BusinessException(message); }
        return json;
    }
    
    public void checkAccountFundTransferDateValidate(AccountFundTransfer transfer)
    {
        if (null == transfer) { throw new BusinessException("提现记录不存在id=" + transfer.getId()); }
        if (null != transfer && !transfer.verifySignature())
        {// 校验数据
            throw new BusinessException("提现数据校验失败id=" + transfer.getId());
        }
        if (!transfer.getTransferStatus()
                .equals(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING)) { throw new BusinessException("提现记录不是待划拨记录ID=" + transfer.getId()); }
        AccountFundCurrent curr = accountFundCurrentService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(),
                transfer.getOriginalCurrentId());
        if (null == curr) { throw new BusinessException("资金流水不存在id=" + transfer.getOriginalCurrentId() + " 提现ID=" + transfer.getId()); }
        Account account = accountService.selectByPrimaryKey(transfer.getAccountId());
        if (null == account) { throw new BusinessException("账户不存在id=" + transfer.getAccountId() + " 提现ID=" + transfer.getId()); }
        if (null != account && !account.verifySignature())
        {// 校验数据
            throw new BusinessException("账户数据校验失败id=" + transfer.getAccountId() + " 提现ID=" + transfer.getId());
        }
        // (存储过程执行 returnCode：0 执行成功，-1 执行失败; 检查结果 chekResult 1.正常 ，小于0 异常; 提示信息 chekMsg)
        AcctAssetChkResult result = acctAssetChkService.doAcctAssetChk(account.getId());
        if (result != null && result.getReturnCode() != null && result.getChekResult() != null)
        {
            if (result.getReturnCode().intValue() != 1 || result.getChekResult().intValue() != 1)
            {
                String msg = result.getChekMsg() + "  accountId=" + account.getId();
                throw new BusinessException(msg);
            }
        }
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    @Override
    public List<AccountFundTransfer> findByIds(String ids)
    {
        String[] idss = ids.split(",");
        Long[] longIds = new Long[idss.length];
        for (int i = 0; i < idss.length; i++)
        {
            longIds[i] = Long.parseLong(idss[i]);
        }
        return accountFundTransferMapper.findByIds(longIds);
    }
    
    /**
     * 定时轮询待审核提币申请
     */
    @Override
    public void autoGetSinglePendingApprovals()
    {
        List<AccountFundTransfer> list = accountFundTransferMapper.findNeedUpdatePendingApprovals();
        for (AccountFundTransfer accountFundTransfer : list)
        {
            JSONObject json = bitGoRemoteService.getSinglePendingApprovals("btc", accountFundTransfer.getPendingApproval().toString());
            logger.debug(json.toJSONString());
            String state = json.getString("state");
            logger.debug(state);
            if (StringUtils.equalsIgnoreCase(state, "approved"))
            {
                // 已审核
                String transId = json.getJSONObject("info").getJSONObject("transactionRequest").getString("validTransactionHash");
                Long netFee = json.getJSONObject("info").getJSONObject("transactionRequest").getLong("fee");
                accountFundTransferService.doSetSinglePendingApprovals(accountFundTransfer.getId(), true, netFee, transId);
                logger.debug("id:" + accountFundTransfer.getId() + " 已审核");
            }
            else if (StringUtils.equalsIgnoreCase(state, "rejected"))
            {
                // 已拒绝
                accountFundTransferService.doSetSinglePendingApprovals(accountFundTransfer.getId(), false, null, null);
                logger.debug("id:" + accountFundTransfer.getId() + " 已拒绝");
                break;
            }
            else
            {
                logger.debug("id:" + accountFundTransfer.getAccountId() + " " + state);
                // 未审核及其它
            }
        }
    }
    
    /**
     * 定时轮询待审核提币申请-事务处理
     */
    @Override
    public void doSetSinglePendingApprovals(Long id, boolean state, Long fee, String transId)
    {
        AccountFundTransfer accountFundTransfer = accountFundTransferMapper.selectByPrimaryKey(id);
        if (StringUtils.equalsIgnoreCase(accountFundTransfer.getTransferStatus(), FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING)
                && StringUtils.equalsIgnoreCase(accountFundTransfer.getConfirmStatus(), FundConsts.WALLET_TRANS_STATUS_UNCONFIRM))
        {
            if (state)
            {
                // 已审核
                // 更新划拨记录 已确认 已汇出 实际网络手续费 交易ID
                accountFundTransfer.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                accountFundTransfer.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_CONFIRM);
                accountFundTransfer.setRealTransferFee(BigDecimal.valueOf(fee).divide(BigDecimal.valueOf(100000000)));
                accountFundTransfer.setTransId(transId);
                accountFundTransferMapper.updateByPrimaryKey(accountFundTransfer);
                // 更新网络手续费超级用户 超级用户网络手续费处理
                FundModel fundModel = new FundModel();
                fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                fundModel.setFee(accountFundTransfer.getRealTransferFee());
                fundModel.setBusinessFlag("doSingleCashWthdrawal");
                fundModel.setAddress("");
                AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
                accountWalletAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);
                fundService.superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
                AccountWalletAsset userWalletAsset = accountWalletAssetService.selectForUpdate(accountFundTransfer.getAccountId(), accountFundTransfer.getStockinfoId());
                userWalletAsset.setWithdrawedTotal(userWalletAsset.getWithdrawedTotal().add(accountFundTransfer.getTransferAmt()));
                userWalletAsset.setWithdrawingTotal(userWalletAsset.getWithdrawingTotal().subtract(accountFundTransfer.getTransferAmt()));
                accountWalletAssetService.updateByPrimaryKey(userWalletAsset);
                // 回填划款资金流水状态以及交易ID
                AccountWithdrawRecord accountWithdrawRecord = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(),
                        accountFundTransfer.getOriginalCurrentId());
                accountWithdrawRecord.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
                accountWithdrawRecord.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
                accountWithdrawRecord.setTransId(accountFundTransfer.getTransId());
                accountWithdrawRecordService.updateByPrimaryKey(accountWithdrawRecord);
            }
            else
            {
                // 已拒绝
                // 更新划拨记录 已确认 拒绝汇出
                accountFundTransfer.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER_REJECTED);
                accountFundTransfer.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_CONFIRM);
                accountFundTransfer.setRealTransferFee(BigDecimal.ZERO);
                accountFundTransferMapper.updateByPrimaryKey(accountFundTransfer);
                // 审核退回
                AccountWithdrawRecord curr = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(),
                        accountFundTransfer.getOriginalCurrentId());
                curr.setTableName(getStockInfo(curr.getStockinfoId()).getTableFundCurrent());
                if (!curr.getTransferStatus().equals(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING) && !curr.getApproveStatus()
                        .equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)) { throw new BusinessException(CommonEnums.ERROR_LOGIN_TIMEOUT); }
                curr.setCheckBy(FundConsts.SYSTEM_ACCOUNT_ID);
                curr.setCheckDate(new Timestamp(System.currentTimeMillis()));
                // curr.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPREJECT);
                curr.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER_REJECTED);
                accountWithdrawRecordService.updateByPrimaryKey(curr);
                // 审批拒绝或审核拒绝，调用接口
                if (StringUtils.isBlank(curr.getWithdrawAddr()))
                {
                    // 目标地址空
                    throw new BusinessException(CommonEnums.FAIL);
                }
                FundModel fundModel = new FundModel();
                fundModel.setAccountId(curr.getAccountId());
                fundModel.setAddress(curr.getWithdrawAddr());
                fundModel.setAmount(curr.getOccurAmt().subtract(curr.getNetFee()));
                fundModel.setFee(curr.getNetFee());
                fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
                fundModel.setStockinfoId(curr.getStockinfoId());
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT);
                // 做业务取消
                fundService.fundTransaction(fundModel);
            }
        }
    }
    
    /**
     * 定时轮询已交易的提币申请
     */
    public void autoGetSingleTransaction()
    {
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setType(2);
        List<BitpayKeychain> chainList = bitpayKeychainService.findList(bitpayKeychain);
        if(chainList.size()>0)
        {
            bitpayKeychain = chainList.get(0);
            List<AccountFundTransfer> list = accountFundTransferMapper.findNeedUpdateTransaction();
            for (AccountFundTransfer accountFundTransfer : list)
            {
                BitPayModel bitPayModel = bitGoRemoteService.transQuery("btc", bitpayKeychain.getWalletId(), accountFundTransfer.getTransId());
                if (bitPayModel.getFee() != null)
                {
                    accountFundTransferService.doSetSingleTransaction(accountFundTransfer.getId(), bitPayModel.getFee());
                }
            }
        }
    }
    
    /**
     * 定时轮询已交易提币申请-事务处理
     */
    public void doSetSingleTransaction(Long id, Long fee)
    {
        AccountFundTransfer accountFundTransfer = accountFundTransferMapper.selectByPrimaryKey(id);
        if (StringUtils.equalsIgnoreCase(accountFundTransfer.getConfirmStatus(), FundConsts.WALLET_TRANS_STATUS_UNCONFIRM))
        {
            // 更新划拨记录 已确认 已汇出 实际网络手续费 交易ID
            accountFundTransfer.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
            accountFundTransfer.setConfirmStatus(FundConsts.WALLET_TRANS_STATUS_CONFIRM);
            accountFundTransfer.setRealTransferFee(BigDecimal.valueOf(fee).divide(BigDecimal.valueOf(100000000)));
            accountFundTransferMapper.updateByPrimaryKey(accountFundTransfer);
            // 更新网络手续费超级用户 超级用户网络手续费处理
            FundModel fundModel = new FundModel();
            fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            fundModel.setFee(accountFundTransfer.getRealTransferFee());
            fundModel.setBusinessFlag("doSingleCashWthdrawal");
            fundModel.setAddress("");
            AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
            accountWalletAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);
            fundService.superAdminNetFee(fundModel, accountWalletAsset, FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE);
            AccountWalletAsset userWalletAsset = accountWalletAssetService.selectForUpdate(accountFundTransfer.getAccountId(), accountFundTransfer.getStockinfoId());
            userWalletAsset.setWithdrawedTotal(userWalletAsset.getWithdrawedTotal().add(accountFundTransfer.getTransferAmt()));
            userWalletAsset.setWithdrawingTotal(userWalletAsset.getWithdrawingTotal().subtract(accountFundTransfer.getTransferAmt()));
            accountWalletAssetService.updateByPrimaryKey(userWalletAsset);
            // 回填划款资金流水状态以及交易ID
            AccountWithdrawRecord accountWithdrawRecord = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(),
                    accountFundTransfer.getOriginalCurrentId());
            accountWithdrawRecord.setTransferStatus(FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFER);
            accountWithdrawRecord.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
            accountWithdrawRecord.setTransId(accountFundTransfer.getTransId());
            accountWithdrawRecordService.updateByPrimaryKey(accountWithdrawRecord);
        }
    }
    
    @Override
    public AccountFundTransfer selectByOriginalCurrentId(Long id)
    {
        return accountFundTransferMapper.selectByOriginalCurrentId(id);
    }
}
