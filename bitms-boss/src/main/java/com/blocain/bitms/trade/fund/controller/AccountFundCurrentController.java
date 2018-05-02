/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

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
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
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
import com.blocain.bitms.trade.fund.enums.FundEnums;
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
public class AccountFundCurrentController extends GenericController
{
    @Autowired(required = false)
    private AccountFundCurrentService    accountCurrentService;
    
    @Autowired(required = false)
    private AccountWithdrawRecordService accountWithdrawRecordService;
    
    @Autowired(required = false)
    private AccountFundTransferService   accountTransferService;
    
    @Autowired(required = false)
    private FundCurrentService           fundCurrentService;
    
    @Autowired(required = false)
    private AcctAssetChkService          acctAssetChkService;
    
    @Autowired(required = false)
    private AccountService               accountService;
    
    @Autowired(required = false)
    private UserInfoService              userInfoService;
    
    @Autowired(required = false)
    private EnableService                enableService;
    
    @Autowired(required = false)
    private AccountLogNoSql              accountLogNoSql;
    
    @Autowired(required = false)
    private AccountCertificationService  accountCertificationService;
    
    @Autowired(required = false)
    private StockInfoService             stockInfoService;
    
    @Autowired(required = false)
    private BlockTransConfirmService     blockTransConfirmService;
    
    @Autowired(required = false)
    private AccountWalletAssetService    accountWalletAssetService;
    
    /**
     * 列表页面导航-账户提现审核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/account/approval_list")
    @RequiresPermissions("trade:setting:accountfundcurrent:index")
    public String list() throws BusinessException
    {
        return "trade/fund/account/approval_list";
    }
    
    /**
     * 列表页面导航-账户提现复核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/account/check_list")
    @RequiresPermissions("trade:setting:accountfundcurrent:index2")
    public String checkList() throws BusinessException
    {
        return "trade/fund/account/check_list";
    }
    
    /**
     * 列表页面导航-用户充币归集
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/account/recharge_list")
    @RequiresPermissions("trade:setting:accountfundcurrent:rechargeindex")
    public String rechargeList() throws BusinessException
    {
        return "trade/fund/account/recharge_list";
    }
    
    /**
     * 列表页面导航-用户资金划拨
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/account/allot_list")
    @RequiresPermissions("trade:setting:accountfundcurrent:allotindex")
    public String allotList() throws BusinessException
    {
        return "trade/fund/account/allot_list";
    }
    
    /**
     * 列表页面导航-综合查询[资金流水]
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/account/curr_list")
    @RequiresPermissions("trade:setting:accountfundcurrent:currindex")
    public String currList() throws BusinessException
    {
        return "trade/fund/account/curr_list";
    }
    
    /**
     * 审核界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/account/approval")
    @RequiresPermissions("trade:setting:accountfundcurrent:operator")
    public ModelAndView approval(Long id, Long exchangePairMoney) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/account/approval");
        // 当前资金流水
        AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
        if (id != null)
        {
            accountWithdrawRecord = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(), id);
            if (accountWithdrawRecord != null)
            {
                accountWithdrawRecord.setWithdrawAddr(EncryptUtils.desDecrypt(accountWithdrawRecord.getWithdrawAddr()));
            }
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
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);// BTC账户余额
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
        // 最近10笔提现
        AccountWithdrawRecord accountWithdrawRecordSearch = new AccountWithdrawRecord();
        accountWithdrawRecordSearch.setAccountId(accountWithdrawRecord.getAccountId());
        accountWithdrawRecordSearch.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountWithdrawRecordSearch.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        accountWithdrawRecordSearch.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
        Pagination pagin = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        List<AccountWithdrawRecord> currList = accountWithdrawRecordService.search(pagin, accountWithdrawRecordSearch).getList();
        for (AccountWithdrawRecord curr : currList)
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
            }
        }
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
        blockTransConfirm.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        blockTransConfirm.setAccountId(accountWithdrawRecord.getAccountId());
        blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        List<BlockTransConfirm> chargeList = blockTransConfirmService.findChargeList(pagin1, blockTransConfirm).getList();
        // 最近10次登录
        mav.addObject("logList", accountLogNoSql.findLastTenLoginLogs(accountWithdrawRecord.getAccountId()));
        // 最近10次安全设置
        mav.addObject("settingList", accountLogNoSql.findLastTenSettingLogs(accountWithdrawRecord.getAccountId()));
        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountWithdrawRecord);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("currList", currList);
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
    @RequestMapping(value = "/account/approvalPrint")
    public ModelAndView approvalPrint(Long id, Long exchangePairMoney) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/account/approvalPrint");
        // 当前资金流水
        AccountWithdrawRecord accountWithdrawRecord = new AccountWithdrawRecord();
        if (id != null)
        {
            accountWithdrawRecord = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(), id);
            if (accountWithdrawRecord != null)
            {
                accountWithdrawRecord.setWithdrawAddr(EncryptUtils.desDecrypt(accountWithdrawRecord.getWithdrawAddr()));
            }
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
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);// BTC账户余额
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
        // 最近10笔提现
        AccountWithdrawRecord accountWithdrawRecordearch = new AccountWithdrawRecord();
        accountWithdrawRecordearch.setAccountId(accountWithdrawRecord.getAccountId());
        accountWithdrawRecordearch.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        accountWithdrawRecordearch.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        accountWithdrawRecordearch.setTableName(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent());
        Pagination pagin = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        List<AccountWithdrawRecord> currList = accountWithdrawRecordService.search(pagin, accountWithdrawRecordearch).getList();
        for (AccountWithdrawRecord curr : currList)
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
            }
        }
        // 最近10笔充值
        Pagination pagin1 = new Pagination();
        pagin.setPage(1);
        pagin.setRows(10);
        BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
        blockTransConfirm.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        blockTransConfirm.setAccountId(accountWithdrawRecord.getAccountId());
        blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        List<BlockTransConfirm> chargeList = blockTransConfirmService.findChargeList(pagin1, blockTransConfirm).getList();
        // 最近10次登录
        mav.addObject("logList", accountLogNoSql.findLastTenLoginLogs(accountWithdrawRecord.getAccountId()));
        // 最近10次安全设置
        mav.addObject("settingList", accountLogNoSql.findLastTenSettingLogs(accountWithdrawRecord.getAccountId()));
        mav.addObject("certification", certification);
        mav.addObject("accountFundCurrent", accountWithdrawRecord);
        mav.addObject("account", account);
        mav.addObject("enableModel", enableModel);
        AccountFundTransfer trans = accountTransferService.selectByOriginalCurrentId(accountWithdrawRecord.getId());
        mav.addObject("trans", trans);
        mav.addObject("coin", getStockInfo(accountWithdrawRecord.getStockinfoId()).getStockCode());
        mav.addObject("usedAmt", usedAmt);
        mav.addObject("chargeAmt", chargeAmt);
        mav.addObject("currList", currList);
        mav.addObject("chargeList", chargeList);
        mav.addObject("exchangePairMoney", exchangePairMoney);
        return mav;
    }

    /**
     * 查询账户流水表-查询审批表
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/account/approvalData", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountfundcurrent:data")
    public JsonMessage approvalData(AccountWithdrawRecord entity, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {

        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd);
        }
        StockInfo stockInfo = getStockInfo(entity.getRelatedStockinfoId());
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            entity.setTableName(stockInfo.getTableFundCurrent());
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            if (entity.getStockinfoId() != null && entity.getRelatedStockinfoId() != null)
            {
                StockInfo s = new StockInfo();
                s.setTradeStockinfoId(entity.getStockinfoId());
                s.setCapitalStockinfoId(entity.getRelatedStockinfoId());
                s.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
                List<StockInfo> list = stockInfoService.findList(s);
                if (list.size() > 0)
                {
                    stockInfo = list.get(0);
                    entity.setRelatedStockinfoId(stockInfo.getId());
                    entity.setTableName(stockInfo.getTableFundCurrent());
                }
                else
                {
                    entity.setTableName(stockInfo.getTableFundCurrent());
                }
            }
            else
            {
                entity.setTableName(stockInfo.getTableFundCurrent());
            }
        }
        else
        {
            entity.setTableName(stockInfo.getTableFundCurrent());
        }
        PaginateResult<AccountWithdrawRecord> result = accountWithdrawRecordService.search(pagin, entity);
        for (AccountWithdrawRecord curr : result.getList())
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 查询账户流水表-查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/account/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountfundcurrent:data")
    public JsonMessage data(AccountFundCurrent entity, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {

        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd);
        }
        StockInfo stockInfo = getStockInfo(entity.getRelatedStockinfoId());
        String table=stockInfo.getTableFundCurrent();
        if(StringUtils.contains(entity.getTableName(),"His"))
        {
            table=stockInfo.getTableFundCurrentHis();
        }
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            entity.setTableName(table);
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            if (entity.getStockinfoId() != null && entity.getRelatedStockinfoId() != null)
            {
                StockInfo s = new StockInfo();
                s.setTradeStockinfoId(entity.getStockinfoId());
                s.setCapitalStockinfoId(entity.getRelatedStockinfoId());
                s.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
                List<StockInfo> list = stockInfoService.findList(s);
                if (list.size() > 0)
                {
                    stockInfo = list.get(0);
                    entity.setRelatedStockinfoId(stockInfo.getId());
                    entity.setTableName(table);
                }
                else
                {
                    entity.setTableName(table);
                }
            }
            else
            {
                entity.setTableName(table);
            }
        }
        else
        {
            entity.setTableName(table);
        }
        PaginateResult<AccountFundCurrent> result = accountCurrentService.search(pagin, entity);
        for (AccountFundCurrent curr : result.getList())
        {
            if (curr.getWithdrawAddr() != null && !curr.getWithdrawAddr().equals(""))
            {
                curr.setWithdrawAddr(EncryptUtils.desDecrypt(curr.getWithdrawAddr()));// des解密
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 查询交易划拨信息
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/account/transferdata", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountfundcurrent:data")
    public JsonMessage data(AccountFundTransfer entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountFundTransfer> result = accountTransferService.search(pagin, entity);
        for (AccountFundTransfer trans : result.getList())
        {
            trans.setTargetWalletAddr(EncryptUtils.desDecrypt(trans.getTargetWalletAddr()));
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据id 修改钱包账户提现 审批状态
     * @param accountWithdrawRecord
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/account/approval/approval", method = RequestMethod.POST)
    @RequiresPermissions(value = {"trade:setting:accountfundcurrent:operator"})
    public JsonMessage approval(AccountWithdrawRecord accountWithdrawRecord, String gaCode) throws BusinessException
    {
        logger.debug("入参：" + accountWithdrawRecord.toString());
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        UserInfo user = userInfoService.selectByPrimaryKey(principal.getId());
        if (StringUtils.equalsIgnoreCase(accountWithdrawRecord.getApproveStatus(), FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH))
        {
            if (StringUtils.isBlank(gaCode)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        }
        AccountWithdrawRecord curr = accountWithdrawRecordService.selectByPrimaryKey(getStockInfo(accountWithdrawRecord.getStockinfoId()).getTableFundCurrent(),
                accountWithdrawRecord.getId());
        if (null == curr)
        {
            logger.info("资金流水 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        Account accountCurr = accountService.selectByPrimaryKey(curr.getAccountId());
        checkAccountDataValidate(accountCurr);
        fundCurrentService.doApproval(accountWithdrawRecord, principal.getId(), gaCode);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 充币归集
     * @param ids
     * @param status
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/account/recharge", method = RequestMethod.POST)
    @RequiresPermissions(value = {"trade:setting:accountfundcurrent:rachargeoperator"})
    public JsonMessage recharge(String ids, boolean status) throws BusinessException
    {
        // UserPrincipal principal = OnLineUserUtils.getPrincipal();
        String id[] = ids.split(",");
        for (int i = 0; i < id.length; i++)
        {
            AccountFundCurrent fund = accountCurrentService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(), Long.parseLong(id[i]));
            /**
             * 2017-07-12 由于业务变更 先注释掉相关归集的代码
             //查询系统帐号
             SystemWallet systemWallet = new SystemWallet();
             systemWallet.setWalletUsageType(WalletConsts.WALLET_USAGE_TYPE_COLLECT_ACCOUNT);
             systemWallet = systemWalletService.findWalletAndAddr(systemWallet);
             if( systemWallet == null )
             {
             //缺少系统钱包id
             return getJsonMessage(CommonConst.FAIL);
             }
             AccountFundCollect collect = new AccountFundCollect();
            
            
             collect.setAccountId(fund.getAccountId());
             collect.setUserinfoId(principal.getId());
             collect.setStockinfoId(fund.getStockinfoId());
             //用户 源地址
             SystemWallet accountallet = new SystemWallet();
             accountallet.setWalletUsageType(WalletConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
             accountallet.setAccountId(fund.getAccountId());
             accountallet = systemWalletService.findWalletAndAddr(accountallet);
             if(accountallet != null)
             {
             collect.setSrcWalletId(accountallet.getWalletId());
             collect.setSrcWalletAddr(fund.getChargeAddr());
            
             }else{
             //缺少用户钱包id
             return getJsonMessage(CommonConst.FAIL);
             }
            
             //目标地址 系统
             collect.setTargetWalletAddr(systemWallet.getWalletAddr());
             collect.setTargetWalletId(systemWallet.getWalletId());
             collect.setCollectTime(CalendarUtils.getCurrentLong());
             collect.setCollectAmt(fund.getOccurAmt());
             collect.setCollectFee(BigDecimal.ZERO);
             collect.setCollectStatus(WalletConsts.WALLET_COLLECT_STATUS_ED);//已归集
             collect.setTransId("default id");
             collect.setConfirmStatus(WalletConsts.WALLET_TRANS_STATUS_UNCONFIRM);//未确认
             collect.setOriginalCurrentId(fund.getId());
             accountCollectService.insert(collect);

            if (status)
            {
                fund.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_ED);
            }
            else
            {
                fund.setCollectStatus(FundConsts.WALLET_COLLECT_STATUS_UN);
            } */
            accountCurrentService.updateByPrimaryKey(fund);
        }
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 提币划拨
     * @param ids
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/account/allot", method = RequestMethod.POST)
    @RequiresPermissions(value = {"trade:setting:accountfundcurrent:allotoperator"})
    public JsonMessage allot(String ids) throws BusinessException
    {
        String[] split = ids.split(",");
        for (String id : split)
        {
            AccountFundCurrent fund = accountCurrentService.selectByPrimaryKey(getStockInfo(FundConsts.WALLET_BTC_TYPE).getTableFundCurrent(), Long.parseLong(id));
            checkFundCurrentDataValidate(fund);
            Account account = accountService.selectByPrimaryKey(fund.getAccountId());
            checkAccountDataValidate(account);
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        fundCurrentService.doAllot(ids, principal.getId());
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
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    private void checkFundCurrentDataValidate(AccountFundCurrent accountFundCurrent) throws BusinessException
    {
        if (null == accountFundCurrent) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
        if (null != accountFundCurrent)
        {// 校验数据
            logger.info("资金流水 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }
}
