/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;
import com.blocain.bitms.monitor.service.AcctAssetChkService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.ExportExcel;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.consts.StockConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户流水表 控制器
 * <p>File：AccountCurrentController.java </p>
 * <p>Title: AccountCurrentController </p>
 * <p>Description:AccountCurrentController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountCashWithdrawController extends GenericController
{
    @Autowired(required = false)
    private UserInfoService             userInfoService;
    
    @Autowired(required = false)
    private AcctAssetChkService         acctAssetChkService;
    
    @Autowired(required = false)
    private AccountService              accountService;
    
    @Autowired(required = false)
    private EnableService               enableService;
    
    @Autowired(required = false)
    private AccountLogNoSql             accountLogNoSql;
    
    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;
    
    @Autowired(required = false)
    private AccountWalletAssetService   accountWalletAssetService;
    
    @Autowired(required = false)
    private AccountCashWithdrawService  accountCashWithdrawService;
    
    @Autowired(required = false)
    private BankRechargeService         bankRechargeService;

    @Autowired(required = false)
    private FundCurrentService          fundCurrentService;
    
    /**
     * 列表页面导航-账户提现审核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/cashwithdraw/approval_list")
    @RequiresPermissions("trade:setting:moneywithdrawapprove:index")
    public String list() throws BusinessException
    {
        return "trade/fund/cashwithdraw/approval_list";
    }
    
    /**
     * 列表页面导航-账户提现复核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/cashwithdraw/confirm_list")
    @RequiresPermissions("trade:setting:moneywithdrawconfirm:index")
    public String checkList() throws BusinessException
    {
        return "trade/fund/cashwithdraw/check_list";
    }
    
    /**
     * 审核界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/cashwithdraw/approval")
    @RequiresPermissions("trade:setting:moneywithdrawapprove:operator")
    public ModelAndView approval(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/cashwithdraw/approval");
        // 当前资金流水
        AccountCashWithdraw accountCashWithdraw = new AccountCashWithdraw();
        if (id != null)
        {
            accountCashWithdraw = accountCashWithdrawService.selectByPrimaryKey(id);
            if (accountCashWithdraw != null)
            {
                accountCashWithdraw.setWithdrawCardNo(EncryptUtils.desDecrypt(accountCashWithdraw.getWithdrawCardNo()));
            }
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountCashWithdraw.getAccountId());
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        List<AccountCertification> certList = accountCertificationService.findList(certification);
        if (certList.size() > 0)
        {
            certification = certList.get(0);
        }
        // 当前账户可用BTC
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountCashWithdraw.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 提币业务
        enableModel.setStockinfoId(accountCashWithdraw.getStockinfoId());// BTC账户余额
        enableModel.setRelatedStockinfoId(accountCashWithdraw.getStockinfoId());// BTC账户余额
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
        AccountCashWithdraw accountWithdrawRecordSearch = new AccountCashWithdraw();
        accountWithdrawRecordSearch.setAccountId(accountCashWithdraw.getAccountId());
        accountWithdrawRecordSearch.setStockinfoId(accountCashWithdraw.getStockinfoId());
        accountWithdrawRecordSearch.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        Pagination pagin = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        List<AccountCashWithdraw> currList = accountCashWithdrawService.search(pagin, accountWithdrawRecordSearch).getList();
        for (AccountCashWithdraw curr : currList)
        {
            if (curr.getWithdrawCardNo() != null && !curr.getWithdrawCardNo().equals(""))
            {
                curr.setWithdrawCardNo(EncryptUtils.desDecrypt(curr.getWithdrawCardNo()));// des解密
            }
        }
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(11);
        BankRecharge blockTransConfirm = new BankRecharge();
        blockTransConfirm.setStockinfoId(accountCashWithdraw.getStockinfoId());
        blockTransConfirm.setAccountId(accountCashWithdraw.getAccountId());
        List<BankRecharge> chargeList = bankRechargeService.findList(blockTransConfirm);
        // 最近10次登录
        mav.addObject("logList", accountLogNoSql.findLastTenLoginLogs(accountCashWithdraw.getAccountId()));
        // 最近10次安全设置
        mav.addObject("settingList", accountLogNoSql.findLastTenSettingLogs(accountCashWithdraw.getAccountId()));
        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountCashWithdraw);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("currList", currList);
        mav.addObject("chargeList", chargeList);
        return mav;
    }
    
    /**
     * 打印界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/cashwithdraw/approvalPrint")
    public ModelAndView approvalPrint(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/cashwithdraw/approvalPrint");
        // 当前资金流水
        AccountCashWithdraw accountCashWithdraw = new AccountCashWithdraw();
        if (id != null)
        {
            accountCashWithdraw = accountCashWithdrawService.selectByPrimaryKey(id);
            if (accountCashWithdraw != null)
            {
                accountCashWithdraw.setWithdrawCardNo(EncryptUtils.desDecrypt(accountCashWithdraw.getWithdrawCardNo()));
            }
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountCashWithdraw.getAccountId());
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        List<AccountCertification> certList = accountCertificationService.findList(certification);
        if (certList.size() > 0)
        {
            certification = certList.get(0);
        }
        // 当前账户可用BTC
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountCashWithdraw.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 提币业务
        enableModel.setStockinfoId(accountCashWithdraw.getStockinfoId());// BTC账户余额
        enableModel.setRelatedStockinfoId(accountCashWithdraw.getStockinfoId());// BTC账户余额
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
        AccountCashWithdraw accountWithdrawRecordSearch = new AccountCashWithdraw();
        accountWithdrawRecordSearch.setAccountId(accountCashWithdraw.getAccountId());
        accountWithdrawRecordSearch.setStockinfoId(accountCashWithdraw.getStockinfoId());
        accountWithdrawRecordSearch.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        Pagination pagin = new Pagination();
        pagin.setPage(1);
        pagin.setRows(6);
        List<AccountCashWithdraw> currList = accountCashWithdrawService.search(pagin, accountWithdrawRecordSearch).getList();
        for (AccountCashWithdraw curr : currList)
        {
            if (curr.getWithdrawCardNo() != null && !curr.getWithdrawCardNo().equals(""))
            {
                curr.setWithdrawCardNo(EncryptUtils.desDecrypt(curr.getWithdrawCardNo()));// des解密
            }
        }
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(5);
        BankRecharge blockTransConfirm = new BankRecharge();
        blockTransConfirm.setStockinfoId(accountCashWithdraw.getStockinfoId());
        blockTransConfirm.setAccountId(accountCashWithdraw.getAccountId());
        List<BankRecharge> chargeList = bankRechargeService.findList(blockTransConfirm);
        // 最近10次登录
        mav.addObject("logList", accountLogNoSql.findLastTenLoginLogs(accountCashWithdraw.getAccountId()));
        // 最近10次安全设置
        mav.addObject("settingList", accountLogNoSql.findLastTenSettingLogs(accountCashWithdraw.getAccountId()));
        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountCashWithdraw);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("currList", currList);
        mav.addObject("chargeList", chargeList);
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
    @RequestMapping(value = "/cashwithdraw/approvalData", method = RequestMethod.POST)
    public JsonMessage approvalData(AccountCashWithdraw entity, Pagination pagin) throws BusinessException
    {
        if(StringUtils.isNotBlank(entity.getTimeStart()))
        {
            entity.setTimeStart(entity.getTimeStart()+" 00:00:00");
        }
        if(StringUtils.isNotBlank(entity.getTimeEnd()))
        {
            entity.setTimeEnd(entity.getTimeEnd()+" 23:59:59");
        }
        PaginateResult<AccountCashWithdraw> result = accountCashWithdrawService.search(pagin, entity);
        for (AccountCashWithdraw curr : result.getList())
        {
            if (curr.getWithdrawCardNo() != null && !curr.getWithdrawCardNo().equals(""))
            {
                curr.setWithdrawCardNo(EncryptUtils.desDecrypt(curr.getWithdrawCardNo()));// des解密
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据id 修改钱包账户提现 审批状态
     * @param accountCashWithdraw
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/cashwithdraw/approval/approval", method = RequestMethod.POST)
    @RequiresPermissions(value = {"trade:setting:moneywithdrawapprove:operator"})
    public JsonMessage approval(AccountCashWithdraw accountCashWithdraw, String gaCode) throws BusinessException
    {
        logger.debug("入参：" + accountCashWithdraw.toString());
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountCashWithdraw curr = accountCashWithdrawService.selectByPrimaryKey(accountCashWithdraw.getId());
        if (null == curr)
        {
            logger.info("资金流水 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        Account accountCurr = accountService.selectByPrimaryKey(curr.getAccountId());
        checkAccountDataValidate(accountCurr);
        if (StringUtils.equalsIgnoreCase(accountCashWithdraw.getApproveStatus(), FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH))
        {
            if (org.apache.commons.lang3.StringUtils.isBlank(gaCode)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
            UserInfo user = userInfoService.selectByPrimaryKey(principal.getId());
            if (StringUtils.isBlank(user.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
            Authenticator authenticator = new Authenticator();
            if (!authenticator.checkCode(EncryptUtils.desDecrypt(user.getAuthKey()), Long.valueOf(gaCode)))
            {// 判断验证码
                // 开始记录操作次数
                int count = 1;
                String opCountKey = new StringBuffer(CacheConst.OPERATION_COUNT_PREFIX)// 加入缓存前缀
                        .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RAISE_DO_RAISE)// 加入模块标识
                        .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
                String opTimes = RedisUtils.get(opCountKey);
                if (StringUtils.isNotBlank(opTimes))
                {// 表示操作记数缓存中已经存在
                    count = Integer.valueOf(opTimes) + 1;
                    if (count >= BitmsConst.LOCK_INTERVAL_COUNT)
                    {// 操作频率达到30次时,锁定用户
                        accountService.modifyAccountStatusToFrozen(OnLineUserUtils.getId(), AccountConsts.FROZEN_REASON_CHANGE_WALLET);
                    }
                }
                RedisUtils.putObject(opCountKey, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
                throw new BusinessException(AccountEnums.ACCOUNT_GACODE_ERROR);
            }
        }
        fundCurrentService.doCashApproval(accountCashWithdraw, principal.getId());
        return getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
     * 导出
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/cashwithdraw/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response,AccountCashWithdraw entity) throws BusinessException, IOException
    {
        if(StringUtils.isNotBlank(entity.getTimeStart()))
        {
            entity.setTimeStart(entity.getTimeStart()+" 00:00:00");
        }
        if(StringUtils.isNotBlank(entity.getTimeEnd()))
        {
            entity.setTimeEnd(entity.getTimeEnd()+" 23:59:59");
        }
        ExportExcel excel = new ExportExcel("Cash Withdrawal Information 【"+entity.getTimeStart()+"~"+entity.getTimeEnd()+"】", AccountCashWithdraw.class);
        List<AccountCashWithdraw> list = accountCashWithdrawService.listForExcel();
        for(AccountCashWithdraw accountCashWithdraw:list)
        {
            accountCashWithdraw.setDateStr(DateUtils.formatDate(new Date(), DateConst.DATE_FORMAT_YMDHMS));
            accountCashWithdraw.setIdStr(accountCashWithdraw.getId().toString());
            accountCashWithdraw.setOccurAmt(accountCashWithdraw.getOccurAmt().subtract(accountCashWithdraw.getFee()));
            accountCashWithdraw.setTransId("");
        }
        excel.setDataList(list);
        excel.write(response, "CashWithdrawalInformation"+ DateUtils.formatDate(new Date(), DateConst.DATE_FORMAT_YYMMDDHHMMSS)+".xls");
    }

    /**
     * 复核界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/cashwithdraw/confirm")
    @RequiresPermissions("trade:setting:moneywithdrawconfirm:operator")
    public ModelAndView confirmIndex(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/cashwithdraw/confirm");
        // 当前资金流水
        AccountCashWithdraw accountCashWithdraw = new AccountCashWithdraw();
        if (id != null)
        {
            accountCashWithdraw = accountCashWithdrawService.selectByPrimaryKey(id);
            if (accountCashWithdraw != null)
            {
                accountCashWithdraw.setWithdrawCardNo(EncryptUtils.desDecrypt(accountCashWithdraw.getWithdrawCardNo()));
            }
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountCashWithdraw.getAccountId());
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        List<AccountCertification> certList = accountCertificationService.findList(certification);
        if (certList.size() > 0)
        {
            certification = certList.get(0);
        }
        // 当前账户可用BTC
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountCashWithdraw.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 提币业务
        enableModel.setStockinfoId(accountCashWithdraw.getStockinfoId());// BTC账户余额
        enableModel.setRelatedStockinfoId(accountCashWithdraw.getStockinfoId());// BTC账户余额
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
        AccountCashWithdraw accountWithdrawRecordSearch = new AccountCashWithdraw();
        accountWithdrawRecordSearch.setAccountId(accountCashWithdraw.getAccountId());
        accountWithdrawRecordSearch.setStockinfoId(accountCashWithdraw.getStockinfoId());
        accountWithdrawRecordSearch.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        Pagination pagin = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        List<AccountCashWithdraw> currList = accountCashWithdrawService.search(pagin, accountWithdrawRecordSearch).getList();
        for (AccountCashWithdraw curr : currList)
        {
            if (curr.getWithdrawCardNo() != null && !curr.getWithdrawCardNo().equals(""))
            {
                curr.setWithdrawCardNo(EncryptUtils.desDecrypt(curr.getWithdrawCardNo()));// des解密
            }
        }
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(11);
        BankRecharge blockTransConfirm = new BankRecharge();
        blockTransConfirm.setStockinfoId(accountCashWithdraw.getStockinfoId());
        blockTransConfirm.setAccountId(accountCashWithdraw.getAccountId());
        List<BankRecharge> chargeList = bankRechargeService.findList(blockTransConfirm);
        // 最近10次登录
        mav.addObject("logList", accountLogNoSql.findLastTenLoginLogs(accountCashWithdraw.getAccountId()));
        // 最近10次安全设置
        mav.addObject("settingList", accountLogNoSql.findLastTenSettingLogs(accountCashWithdraw.getAccountId()));
        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountCashWithdraw);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("currList", currList);
        mav.addObject("chargeList", chargeList);
        return mav;
    }
    
    /**
     * 根据id 修改钱包账户提现 复核审批
     * @param accountCashWithdraw
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/cashwithdraw/confirm/confirm", method = RequestMethod.POST)
    @RequiresPermissions(value = {"trade:setting:moneywithdrawconfirm:operator"})
    public JsonMessage confirm(AccountCashWithdraw accountCashWithdraw, String gaCode) throws BusinessException
    {
        logger.debug("入参：" + accountCashWithdraw.toString());
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (StringUtils.equalsIgnoreCase(accountCashWithdraw.getApproveStatus(), FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH))
        {
            if (org.apache.commons.lang3.StringUtils.isBlank(gaCode)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        }
        AccountCashWithdraw curr = accountCashWithdrawService.selectByPrimaryKey(accountCashWithdraw.getId());
        if (null == curr)
        {
            logger.info("资金流水 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        Account accountCurr = accountService.selectByPrimaryKey(curr.getAccountId());
        checkAccountDataValidate(accountCurr);
        UserInfo user = userInfoService.selectByPrimaryKey(principal.getId());
        if (StringUtils.isBlank(user.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
        Authenticator authenticator = new Authenticator();
        if (!authenticator.checkCode(EncryptUtils.desDecrypt(user.getAuthKey()), Long.valueOf(gaCode)))
        {// 判断验证码
         // 开始记录操作次数
            int count = 1;
            String opCountKey = new StringBuffer(CacheConst.OPERATION_COUNT_PREFIX)// 加入缓存前缀
                    .append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RAISE_DO_RAISE)// 加入模块标识
                    .append(BitmsConst.SEPARATOR).append(OnLineUserUtils.getId()).toString();
            String opTimes = RedisUtils.get(opCountKey);
            if (StringUtils.isNotBlank(opTimes))
            {// 表示操作记数缓存中已经存在
                count = Integer.valueOf(opTimes) + 1;
                if (count >= BitmsConst.LOCK_INTERVAL_COUNT)
                {// 操作频率达到30次时,锁定用户
                    accountService.modifyAccountStatusToFrozen(OnLineUserUtils.getId(), AccountConsts.FROZEN_REASON_CHANGE_WALLET);
                }
            }
            RedisUtils.putObject(opCountKey, String.valueOf(count), CacheConst.ONE_HOUR_CACHE_TIME);
            throw new BusinessException(AccountEnums.ACCOUNT_GACODE_ERROR);
        }
        fundCurrentService.doCashConfirm(accountCashWithdraw, principal.getId());
        return getJsonMessage(CommonEnums.SUCCESS);
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
