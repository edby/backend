/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.bitpay.service.BitpayKeychainERC20Service;
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;
import com.blocain.bitms.monitor.service.AcctAssetChkService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.payment.eth.EthLocalService;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

/**
 *  ERC20 ETH提现管理
 * <p>File：AccountWithdrawRecordERC20Controller.java </p>
 * <p>Title: AccountWithdrawRecordERC20Controller </p>
 * <p>Description:AccountWithdrawRecordERC20Controller </p>
 * <p>Copyright: Copyright (c) 2018-03-02 08:34:13 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountWithdrawRecordERC20Controller extends GenericController
{
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String      keyPrefix        = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();
    
    @Autowired(required = false)
    private AcctAssetChkService               acctAssetChkService;
    
    @Autowired(required = false)
    private AccountService                    accountService;
    
    @Autowired(required = false)
    private EnableService                     enableService;
    
    @Autowired(required = false)
    private AccountLogNoSql                   accountLogNoSql;
    
    @Autowired(required = false)
    private AccountCertificationService       accountCertificationService;
    
    @Autowired(required = false)
    private AccountWalletAssetService         accountWalletAssetService;
    
    @Autowired(required = false)
    private AccountWithdrawRecordERC20Service accountWithdrawRecordERC20Service;
    
    @Autowired(required = false)
    private BlockTransConfirmService   blockTransConfirmService;
    
    @Autowired(required = false)
    private FundCurrentService         fundCurrentService;
    
    @Autowired(required = false)
    private StockInfoService           stockInfoService;
    
    @Autowired(required = false)
    private Erc20TokenLocalService     erc20TokenLocalService;
    
    @Autowired(required = false)
    private EthLocalService            ethLocalService;
    
    @Autowired(required = false)
    private BitpayKeychainERC20Service bitpayKeychainERC20Service;
    
    /**
     * 列表页面导航-账户提现审核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/erctokenwithdraw/approval_list")
    @RequiresPermissions("trade:setting:erctokenwithdrawapprove:index")
    public String list() throws BusinessException
    {
        return "trade/fund/erctokenwithdraw/approval_list";
    }
    
    /**
     * 审核界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/erctokenwithdraw/approval")
    @RequiresPermissions("trade:setting:erctokenwithdrawapprove:operator")
    public ModelAndView approval(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/erctokenwithdraw/approval");
        // 获取ERC20 TOKEN的付款钱包
        BitpayKeychainERC20 bitpayKeychainERC20 = new BitpayKeychainERC20();
        bitpayKeychainERC20.setWalletType(Integer.valueOf(2));// 冷钱包生成二维码
        bitpayKeychainERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE.toString());
        List<BitpayKeychainERC20> listBitpay = bitpayKeychainERC20Service.findList(bitpayKeychainERC20);
        if (listBitpay.size() == 0) { throw new BusinessException("付款钱包不存在"); }
        bitpayKeychainERC20 = listBitpay.get(0);

        // 当前资金流水
        AccountWithdrawRecordERC20 accountWithdrawRecordERC20 = new AccountWithdrawRecordERC20();
        if (id != null)
        {
            accountWithdrawRecordERC20 = accountWithdrawRecordERC20Service.selectByPrimaryKey(id);
            if (accountWithdrawRecordERC20 != null)
            {
                String pwd = EncryptUtils.desDecrypt(accountWithdrawRecordERC20.getWithdrawAddr());
                accountWithdrawRecordERC20.setWithdrawAddr(pwd);
            }
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountWithdrawRecordERC20.getAccountId());
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        List<AccountCertification> certList = accountCertificationService.findList(certification);
        if (certList.size() > 0)
        {
            certification = certList.get(0);
        }
        // 当前账户可用BTC
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountWithdrawRecordERC20.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 提币业务
        enableModel.setStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        enableModel.setRelatedStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        // 当前账户已提现
        BigDecimal usedAmt = BigDecimal.ZERO;
        // 当前账户已充值
        BigDecimal chargeAmt = BigDecimal.ZERO;
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(enableModel.getAccountId());
        accountWalletAsset.setStockinfoId(enableModel.getStockinfoId());
        logger.debug("从钱包账户资产db中查找可用数量 accountWalletAsset:" + accountWalletAsset.toString());
        List<AccountWalletAsset> accountWalletAssetList = accountWalletAssetService.findList(accountWalletAsset);
        if (accountWalletAssetList.size() > 0)
        {
            accountWalletAsset = accountWalletAssetList.get(0);
            usedAmt = accountWalletAsset.getWithdrawedTotal();
            chargeAmt = accountWalletAsset.getChargedTotal();
        }
        // 最近10笔提现
        AccountWithdrawRecordERC20 accountWithdrawRecordSearch = new AccountWithdrawRecordERC20();
        accountWithdrawRecordSearch.setAccountId(accountWithdrawRecordERC20.getAccountId());
        accountWithdrawRecordSearch.setStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        accountWithdrawRecordSearch.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        accountWithdrawRecordSearch.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
        Pagination pagin = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        List<AccountWithdrawRecordERC20> currList = accountWithdrawRecordERC20Service.search(pagin, accountWithdrawRecordSearch).getList();
        for (AccountWithdrawRecordERC20 curr : currList)
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                String pwd = EncryptUtils.desDecrypt(curr.getWithdrawAddr());
                curr.setWithdrawAddr(pwd);
            }
        }
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
        blockTransConfirm.setStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        blockTransConfirm.setAccountId(accountWithdrawRecordERC20.getAccountId());
        blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        List<BlockTransConfirm> chargeList = blockTransConfirmService.findChargeList(pagin1, blockTransConfirm).getList();
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(accountWithdrawRecordERC20.getStockinfoId());
        // 最近10次登录
//        mav.addObject("logList", accountLogNoSql.findLastTenLoginLogs(accountWithdrawRecordERC20.getAccountId()));
        // 最近10次安全设置
//        mav.addObject("settingList", accountLogNoSql.findLastTenSettingLogs(accountWithdrawRecordERC20.getAccountId()));
        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountWithdrawRecordERC20);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("currList", currList);
        mav.addObject("chargeList", chargeList);
        mav.addObject("stockinfo", stockInfo);
        if ((accountWithdrawRecordERC20.getOccurAmt().subtract(accountWithdrawRecordERC20.getFee())).compareTo(stockInfo.getSmallWithdrawHotSignValue()) <= 0)
        {
            mav.addObject("needSign", false);
        }
        else
        {
            mav.addObject("needSign", true);
            if (stockInfo.getId().longValue() == FundConsts.WALLET_ETH_TYPE.longValue())
            {
                String signStr = ethLocalService.eth_getImTokenUnsignTransactionData(bitpayKeychainERC20.getWalletId(), accountWithdrawRecordERC20.getWithdrawAddr(),
                        accountWithdrawRecordERC20.getOccurAmt().subtract(accountWithdrawRecordERC20.getFee()));
                mav.addObject("signStr", signStr);
                String oldMd5 = signStr.substring(4+signStr.indexOf("md5="));
                mav.addObject("oldMd5", oldMd5);
            }
            else
            {
                String signStr = erc20TokenLocalService.erc20_getImTokenUnsignTransactionData(stockInfo.getTokenContactAddr(), stockInfo.getStockCode().toUpperCase(),
                        bitpayKeychainERC20.getWalletId(), accountWithdrawRecordERC20.getWithdrawAddr(),
                        accountWithdrawRecordERC20.getOccurAmt().subtract(accountWithdrawRecordERC20.getFee()));
                mav.addObject("signStr", signStr);
                String oldMd5 = signStr.substring(4+signStr.indexOf("md5="));
                mav.addObject("oldMd5", oldMd5);
            }
        }

        return mav;
    }
    
    /**
     * 打印界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/erctokenwithdraw/approvalPrint")
    public ModelAndView approvalPrint(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/erctokenwithdraw/approvalPrint");
        // 当前资金流水
        AccountWithdrawRecordERC20 accountWithdrawRecordERC20 = new AccountWithdrawRecordERC20();
        if (id != null)
        {
            accountWithdrawRecordERC20 = accountWithdrawRecordERC20Service.selectByPrimaryKey(id);
            if (accountWithdrawRecordERC20 != null)
            {
                String pwd = EncryptUtils.desDecrypt(accountWithdrawRecordERC20.getWithdrawAddr());
                accountWithdrawRecordERC20.setWithdrawAddr(pwd);
            }
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountWithdrawRecordERC20.getAccountId());
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        List<AccountCertification> certList = accountCertificationService.findList(certification);
        if (certList.size() > 0)
        {
            certification = certList.get(0);
        }
        // 当前账户可用BTC
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountWithdrawRecordERC20.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 提币业务
        enableModel.setStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        enableModel.setRelatedStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        // 当前账户已提现
        BigDecimal usedAmt = BigDecimal.ZERO;
        // 当前账户已充值
        BigDecimal chargeAmt = BigDecimal.ZERO;
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(enableModel.getAccountId());
        accountWalletAsset.setStockinfoId(enableModel.getStockinfoId());
        logger.debug("从钱包账户资产db中查找可用数量 accountWalletAsset:" + accountWalletAsset.toString());
        List<AccountWalletAsset> accountWalletAssetList = accountWalletAssetService.findList(accountWalletAsset);
        if (accountWalletAssetList.size() > 0)
        {
            accountWalletAsset = accountWalletAssetList.get(0);
            usedAmt = accountWalletAsset.getWithdrawedTotal();
            chargeAmt = accountWalletAsset.getChargedTotal();
        }
        // 最近10笔提现
        AccountWithdrawRecordERC20 accountWithdrawRecordSearch = new AccountWithdrawRecordERC20();
        accountWithdrawRecordSearch.setAccountId(accountWithdrawRecordERC20.getAccountId());
        accountWithdrawRecordSearch.setStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        accountWithdrawRecordSearch.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        accountWithdrawRecordSearch.setApproveStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
        Pagination pagin = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        List<AccountWithdrawRecordERC20> currList = accountWithdrawRecordERC20Service.search(pagin, accountWithdrawRecordSearch).getList();
        for (AccountWithdrawRecordERC20 curr : currList)
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                String pwd = EncryptUtils.desDecrypt(curr.getWithdrawAddr());
                curr.setWithdrawAddr(pwd);
            }
        }
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
        blockTransConfirm.setStockinfoId(accountWithdrawRecordERC20.getStockinfoId());
        blockTransConfirm.setAccountId(accountWithdrawRecordERC20.getAccountId());
        blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        List<BlockTransConfirm> chargeList = blockTransConfirmService.findChargeList(pagin1, blockTransConfirm).getList();
        // 最近10次登录
        mav.addObject("logList", accountLogNoSql.findLastTenLoginLogs(accountWithdrawRecordERC20.getAccountId()));
        // 最近10次安全设置
        mav.addObject("settingList", accountLogNoSql.findLastTenSettingLogs(accountWithdrawRecordERC20.getAccountId()));
        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountWithdrawRecordERC20);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("currList", currList);
        mav.addObject("chargeList", chargeList);
        mav.addObject("stockinfo", stockInfoService.selectByPrimaryKey(accountWithdrawRecordERC20.getStockinfoId()));
        return mav;
    }
    
    /**
     * 查询账户流水表-查询审批表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/erctokenwithdraw/approvalData", method = RequestMethod.POST)
    public JsonMessage approvalData(AccountWithdrawRecordERC20 entity, Pagination pagin) throws BusinessException
    {
        if (StringUtils.isNotBlank(entity.getTimeStart()))
        {
            entity.setTimeStart(entity.getTimeStart() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(entity.getTimeEnd()))
        {
            entity.setTimeEnd(entity.getTimeEnd() + " 23:59:59");
        }
        PaginateResult<AccountWithdrawRecordERC20> result = accountWithdrawRecordERC20Service.search(pagin, entity);
        for (AccountWithdrawRecordERC20 curr : result.getList())
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据id 修改钱包账户提现 审批状态
     * @param accountWithdrawRecordERC20
     * @param  signedTransactionData 大额签名
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/erctokenwithdraw/approval/approval", method = RequestMethod.POST)
    @RequiresPermissions(value = {"trade:setting:erctokenwithdrawapprove:operator"})
    public JsonMessage approval(AccountWithdrawRecordERC20 accountWithdrawRecordERC20, String signedTransactionData) throws BusinessException
    {
        logger.debug("入参：" + accountWithdrawRecordERC20.toString());
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(accountWithdrawRecordERC20.getStockinfoId());
        AccountWithdrawRecordERC20 curr = accountWithdrawRecordERC20Service.selectByPrimaryKey(accountWithdrawRecordERC20.getId());
        if (accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH)
                || accountWithdrawRecordERC20.getApproveStatus().equals(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKPENDING))
        {
            if ((curr.getOccurAmt().subtract(curr.getFee())).compareTo(stockInfo.getSmallWithdrawHotSignValue()) > 0)
            {
                if (StringUtils.isBlank(signedTransactionData)) { throw new BusinessException("大额需要加签"); }
            }
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == curr)
        {
            logger.info("资金流水 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        Account accountCurr = accountService.selectByPrimaryKey(curr.getAccountId());
        checkAccountDataValidate(accountCurr);
//        UserInfo user = userInfoService.selectByPrimaryKey(principal.getId());
//        if (StringUtils.isBlank(user.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
//        Authenticator authenticator = new Authenticator();
        // if (!authenticator.checkCode(EncryptUtils.desDecrypt(user.getAuthKey()), Long.valueOf(gaCode)))
        // {// 判断验证码
        // // 开始记录操作次数
        // int count = 1;
        // String opCountKey = new StringBuffer(CacheConst.OPERATION_COUNT_PREFIX)// 加入缓存前缀
        // .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RAISE_DO_RAISE)// 加入模块标识
        // .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
        // String opTimes = RedisUtils.get(opCountKey);
        // if (StringUtils.isNotBlank(opTimes))
        // {// 表示操作记数缓存中已经存在
        // count = Integer.valueOf(opTimes) + 1;
        // if (count >= BitmsConst.LOCK_INTERVAL_COUNT)
        // {// 操作频率达到30次时,锁定用户
        // accountService.modifyAccountStatusToFrozen(OnLineUserUtils.getId(), AccountConsts.FROZEN_REASON_CHANGE_WALLET);
        // }
        // }
        // RedisUtils.putObject(opCountKey, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
        // throw new BusinessException(AccountEnums.ACCOUNT_GACODE_ERROR);
        // }
        // accountWithdrawRecordERC20 中有审批状态
        fundCurrentService.doApprovalERC20(accountWithdrawRecordERC20, principal.getId(), signedTransactionData);
        // 刷新缓存
        stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        for (StockInfo stockInfo1 : list)
        {
            // 刷新缓存
            clearAccountAssetCache(curr.getAccountId(), stockInfo1.getId(), stockInfo1.getId());
        }
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    public void clearAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        RedisUtils.del(key);
    }
    private void checkAccountDataValidate(Account account)
    {
        if (null == account) { throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT); }
        if (account.getStatus() != AccountConsts.ACCOUNT_STATUS_NORMAL) { throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK); }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
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
}
