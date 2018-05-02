/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.monitor.entity.AcctAssetChkResult;
import com.blocain.bitms.monitor.service.AcctAssetChkService;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.service.BankRechargeService;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 银行充值记录表 控制器
 * <p>File：BankRechargeController.java </p>
 * <p>Title: BankRechargeController </p>
 * <p>Description:BankRechargeController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping("/fund/bankRecharge")
@Api(description = "银行充值记录表")
public class BankRechargeController extends GenericController
{
    @Autowired(required = false)
    private BankRechargeService         bankRechargeService;

    @Autowired(required = false)
    private AccountService              accountService;

    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;

    @Autowired(required = false)
    private EnableService               enableService;

    @Autowired(required = false)
    private AccountWalletAssetService   accountWalletAssetService;

    @Autowired(required = false)
    private UserInfoService             userInfoService;

    @Autowired(required = false)
    private AcctAssetChkService         acctAssetChkService;

    /**
     * 列表页面导航-充值
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("trade:setting:moneychargeinput:index")
    public String indexList() throws BusinessException
    {
        return "trade/fund/bankRecharge/list";
    }

    /**
     * 列表页面导航-充值审核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/approval_list")
    @RequiresPermissions("trade:setting:moneychargeapprove:index")
    public String approveList() throws BusinessException
    {
        return "trade/fund/bankRecharge/approval_list";
    }

    /**
     * 操作银行充值记录表
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存银行充值记录表", httpMethod = "POST")
    @RequiresPermissions("trade:setting:moneychargeinput:operator")
    public JsonMessage save(@ModelAttribute BankRecharge info) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        Account account = accountService.getAccountByUnid(info.getUnid());
        if(account == null )
        {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);// 用户不存在
        }

        // 账身份认证信息
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        certification.setStatus((short) 1); // 审核通过
        List<AccountCertification> accountCertificationList = accountCertificationService.findList(certification);
        if (accountCertificationList.size() == 0)
        {
            throw new BusinessException("该充值账户未认证，请先认证！");// 该充值账户未认证，请先认证！
        } else {
            certification = accountCertificationList.get(0);
        }

         // 比较名字
        if(!certification.getSurname().equals(info.getSurname())){
            throw new BusinessException("该充值账户姓氏lastName与账户身份认证姓氏lastName不匹配，请先确认！");// 该充值账户姓氏lastName与账户身份认证姓氏lastName不匹配，请先认证！
        }
        if(!certification.getRealname().equals(info.getRealname())){
            throw new BusinessException("该充值账户名字firstName与账户身份认证名字firstName不匹配，请先确认！");// 该充值账户名字firstName与账户身份认证名字firstName不匹配，请先确认！
        }

        info.setAccountId(account.getId());
        info.setFee(BigDecimal.ZERO); // 充值费用固定为0
        info.setCreateBy(principal.getId());
        info.setCreateDate(new Date());
        info.setStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_AUDITPENDING);// 待审核
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        if (beanValidator(json, info))
        {
            bankRechargeService.save(info);
        }
        return json;
    }

    /**
     * 录入界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/edit")
    @RequiresPermissions("trade:setting:moneychargeinput:operator")
    public ModelAndView edit(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/bankRecharge/modify");
        BankRecharge bankRecharge = bankRechargeService.selectByPrimaryKey(id);
        mav.addObject("entity", bankRecharge);
        return mav;
    }

    /**
     * 审核界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/approval")
    @RequiresPermissions("trade:setting:moneychargeapprove:operator")
    public ModelAndView approvalIndex(Long id, Long exchangePairMoney) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/bankRecharge/approval");
       // 当前资金流水
        BankRecharge accountWithdrawRecord = new BankRecharge();
        if (id != null)
        {
            accountWithdrawRecord = bankRechargeService.selectByPrimaryKey( id);
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountWithdrawRecord.getAccountId());

        // 账户认证信息
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        certification.setStatus((short) 1); // 审核通过
        List<AccountCertification> accountCertificationList = accountCertificationService.findList(certification);
        if (accountCertificationList.size() == 0)
        {
            throw new BusinessException("该充值账户未认证，请先认证！");// 该充值账户未认证，请先认证！
        } else {
            certification = accountCertificationList.get(0);
        }

        // 当前账户可用BTC
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountWithdrawRecord.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 提币业务
        enableModel.setStockinfoId(accountWithdrawRecord.getStockinfoId());// BTC账户余额
        enableModel.setRelatedStockinfoId(accountWithdrawRecord.getStockinfoId());// BTC账户余额
        enableModel = enableService.entrustTerminalEnable(enableModel);
        // 当前账户已提现
        java.math.BigDecimal usedAmt = BigDecimal.ZERO;
        // 当前账户已充值
        java.math.BigDecimal chargeAmt = BigDecimal.ZERO;
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

        Pagination pagin = new Pagination();
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(11);
        BankRecharge blockTransConfirm = new BankRecharge();
        blockTransConfirm.setStockinfoId(accountWithdrawRecord.getStockinfoId());
        blockTransConfirm.setAccountId(accountWithdrawRecord.getAccountId());
        List<BankRecharge> chargeList = bankRechargeService.findList(blockTransConfirm);

        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountWithdrawRecord);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("chargeList", chargeList);
        mav.addObject("exchangePairMoney", exchangePairMoney);
        return mav;
    }

    /**
     * 打印界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/approvalPrint")
    public ModelAndView approvalPrint(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/bankRecharge/approvalPrint");
        // 当前资金流水
        BankRecharge accountWithdrawRecord = new BankRecharge();
        if (id != null)
        {
            accountWithdrawRecord = bankRechargeService.selectByPrimaryKey( id);
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountWithdrawRecord.getAccountId());
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        List<AccountCertification> certList = accountCertificationService.findList(certification);
        if (certList.size() > 0)
        {
            certification = certList.get(0);
        }
        // 当前账户可用BTC
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountWithdrawRecord.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);// 提币业务
        enableModel.setStockinfoId(accountWithdrawRecord.getStockinfoId());// BTC账户余额
        enableModel.setRelatedStockinfoId(accountWithdrawRecord.getStockinfoId());// BTC账户余额
        enableModel = enableService.entrustTerminalEnable(enableModel);
        // 当前账户已提现
        java.math.BigDecimal usedAmt = BigDecimal.ZERO;
        // 当前账户已充值
        java.math.BigDecimal chargeAmt = BigDecimal.ZERO;
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

        Pagination pagin = new Pagination();
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(6);
        BankRecharge blockTransConfirm = new BankRecharge();
        blockTransConfirm.setStockinfoId(accountWithdrawRecord.getStockinfoId());
        blockTransConfirm.setAccountId(accountWithdrawRecord.getAccountId());
        List<BankRecharge> chargeList = bankRechargeService.findList(blockTransConfirm);

        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountWithdrawRecord);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("chargeList", chargeList);
        return mav;
    }

    /**
     * 审批处理
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/approval/approval", method = RequestMethod.POST)
    @ApiOperation(value = "审批处理", httpMethod = "POST")
    @RequiresPermissions("trade:setting:moneychargeapprove:operator")
    public JsonMessage approve(@ModelAttribute BankRecharge info, String gaCode) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        logger.debug("入参：" + info.toString());
        UserInfo user = userInfoService.selectByPrimaryKey(principal.getId());
        if (StringUtils.equalsIgnoreCase(info.getStatus(), FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH))
        {
            if (org.apache.commons.lang3.StringUtils.isBlank(gaCode)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        }
        BankRecharge bankRecharge = bankRechargeService.selectByPrimaryKey(info.getId());
        Account accountCurr = accountService.selectByPrimaryKey(bankRecharge.getAccountId());
        checkAccountDataValidate(accountCurr);
        if (StringUtils.isBlank(gaCode)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
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
        bankRechargeService.doChargeApproval(info, principal.getId());
        return json;
    }

    /**
     * 查询银行充值记录表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询银行充值记录表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute BankRecharge entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<BankRecharge> result = bankRechargeService.search(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 根据指定ID删除
     * @param id
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @RequiresPermissions("trade:setting:moneychargeinput:operator")
    public JsonMessage del(Long id) throws BusinessException
    {
        bankRechargeService.doDeleteBankRecharge(id);
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
