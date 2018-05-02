/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.controller;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import com.blocain.bitms.trade.fund.model.ShareLossModel;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.settlement.entity.SettlementProcessLog;
import com.blocain.bitms.trade.settlement.service.SettlementProcessLogService;
import com.blocain.bitms.trade.settlement.service.SettlementService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 交割控制器
 * <p>File：AccountSettlementController.java</p>
 * <p>Title: AccountSettlementController</p>
 * <p>Description:AccountSettlementController</p>
 * <p>Copyright: Copyright (c) 2017年9月18日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SETTLEMENT)
public class SettlementController extends GenericController
{
    @Autowired(required = false)
    private SysParameterService         sysParameterService;
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService    entrustVCoinMoneyService;
    
    @Autowired(required = false)
    private AccountContractAssetService accountContractAssetService;
    
    @Autowired(required = false)
    private AccountDebitAssetService    accountDebitAssetService;
    
    @Autowired(required = false)
    private AccountService              accountService;
    
    @Autowired(required = false)
    private SettlementProcessLogService settlementProcessLogService;
    
    @Autowired(required = false)
    private SettlementService           settlementService;
    
    @Autowired(required = false)
    private AccountFundCurrentService   accountFundCurrentService;
    
    @Autowired(required = false)
    private StockInfoService            stockInfoService;
    
    /**
     * 导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView index() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/list");
        mav.addObject("step", 0);
        mav.addObject("jiesuanjia", 0);
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        mav.addObject("switchStatus", sysParameter.getValue());
        return mav;
    }
    
    /**
     * 开关状态-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/switch")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView switchStatus(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/switch");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        mav.addObject("switchStatus", sysParameter.getValue());
        mav.addObject("stockInfo", stockInfo);
        return mav;
    }
    
    /**
     * 委托列表-未撤销的委托导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/entrustXDoing")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView entrusXDoing(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/entrustXDoing");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 委托列表-未撤销的委托数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/entrustXDoing/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage data(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.findAllInEntrust(pagin, entrustVCoinMoney);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 委托列表-已被交割自动撤销的委托导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/entrustXDone")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView entrusXDone(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/entrustXDone");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 委托列表-已被交割自动撤销的委托数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/entrustXDone/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage dataDone(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        // 更新时间 >= 开关按钮更新时间 已在sql语句中处理
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.findWithdrawBySysEntrust(pagin, entrustVCoinMoney);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 剩余借款（数据来源于保证金监控）
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/debitDoing")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView debitXDing(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/debitDoing");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 剩余借款（数据来源于保证金监控）-数据
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/debitDoing/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage marginData(AccountDebitAsset entity, Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        entity.setRelatedStockinfoId(exchangePairMoney);
        PaginateResult<AccountDebitAsset> result = accountDebitAssetService.findMarginList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 剩余借款-已还(数据来源于资金流水)
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/debitDone")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView debitXDone(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/debitDone");
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        mav.addObject("timeStart", sysParameter.getUpdateDate());
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 剩余借款-已还(数据来源于资金流水)数据
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/debitDone/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage debitDoneData(AccountFundCurrent entity, String timeStart, Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            // 交易时间 >= 开关按钮更新时间 已从界面传入
            entity.setTimeStart(timeStart);
        }
        entity.setRelatedStockinfoId(exchangePairMoney);
        entity.setTableName(getStockInfo(exchangePairMoney).getTableFundCurrent());
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.findDebitRepaySettlemenet(pagin, entity,
                FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT, FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT);
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
     * USDX持仓-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/usdxHave")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView usdxHave(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/usdxHave");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * USDX持仓减少-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/usdxHaveMove")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView usdxHaveMove(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/usdxHaveMove");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        mav.addObject("timeStart", sysParameter.getUpdateDate());
        return mav;
    }
    
    /**
     * USDX持仓-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/usdxHave/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage usdxHaveData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        pagin.setOrder(" a.amount desc ");
        accountContractAsset.setStockinfoId(exchangePairMoney);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        PaginateResult<AccountContractAsset> result = accountContractAssetService.search(pagin, accountContractAsset);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 超级用户资产情况-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/superasset")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView superAdmin(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/superasset");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 超级用户资产情况-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/superasset/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage superassetData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        PaginateResult<AccountContractAsset> result = accountContractAssetService.selectSuperAdminAsset(pagin, accountContractAsset, FundConsts.SYSTEM_ACCOUNT_ID,
                FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID, FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID,
                FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 平台亏损-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/platLoss")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView platLoss(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/platLoss");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        BigDecimal superUsdxDebit = BigDecimal.ZERO;
        AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairMoney);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        List<AccountDebitAsset> debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            accountDebitAsset = debitList.get(0);
            superUsdxDebit = accountDebitAsset.getDebitAmt();
        }
        logger.debug("超级用户法定货币借款数量：" + superUsdxDebit);
        mav.addObject("superUsdxDebit", superUsdxDebit);
        BigDecimal superBtcDebit = BigDecimal.ZERO;
        accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairVCoin);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            accountDebitAsset = debitList.get(0);
            superBtcDebit = accountDebitAsset.getDebitAmt();
        }
        logger.debug("超级用户数字货币借款数量：" + superBtcDebit);
        mav.addObject("superBtcDebit", superBtcDebit);
        BigDecimal settlementPrice = getSettlementPrice(exchangePairMoney);
        mav.addObject("settlementPrice", settlementPrice);
        logger.debug("结算价：" + settlementPrice);
        BigDecimal superBtcAsset = BigDecimal.ZERO;
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            superBtcAsset = list.get(0).getAmount();
        }
        mav.addObject("superBtcAsset", superBtcAsset);
        logger.debug("超级用户数字货币资产：" + superBtcAsset);
        BigDecimal superUsdxAsset = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairMoney);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            superUsdxAsset = list.get(0).getAmount();
        }
        mav.addObject("superUsdxAsset", superUsdxAsset);
        logger.debug("超级用户法定货币资产：" + superUsdxAsset);
        BigDecimal needBtc = superBtcAsset.subtract(superBtcDebit).add((superUsdxAsset.subtract(superUsdxDebit)).divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP));
        logger.debug("盈亏情况：" + needBtc);
        boolean result = needBtc.compareTo(BigDecimal.ZERO) >= 0;
        logger.debug("比较的结果：" + result);
        mav.addObject("result", result);// 比较结果 如果是false 则亏损
        if (result)
        {
            needBtc = BigDecimal.ZERO;
            logger.debug("需分摊数字货币:" + needBtc);
            mav.addObject("needBtc", needBtc);
        }
        else
        {
            // 比较结果 如果是false 则亏损
            needBtc = needBtc.abs();
            logger.debug("需分摊数字货币:" + needBtc);
            mav.addObject("needBtc", needBtc);
        }
        BigDecimal reserveFund = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);// 法定货币
        accountContractAsset.setStockinfoId(exchangePairVCoin);// 数字货币
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> asstList = accountContractAssetService.findList(accountContractAsset);
        if (asstList.size() > 0)
        {
            AccountContractAsset asset = asstList.get(0);
            reserveFund = asset.getAmount();
            logger.debug("存在准备金：" + reserveFund);
        }
        else
        {
            logger.debug("不存在准备金：" + reserveFund);
        }
        mav.addObject("reserveFund", reserveFund);
        BigDecimal superYingLi = BigDecimal.ZERO;
        ContractAssetModel modelPerson = accountContractAssetService.findAccountSumContractAsset(exchangePairVCoin, exchangePairMoney, null);
        superYingLi = modelPerson.getSumProfit();
        logger.debug("查询全市场- 数字货币盈利:" + superYingLi);
        mav.addObject("superYingLi", superYingLi);
        return mav;
    }
    
    /**
     * 亏损试分摊-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/platLossShare")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView platLossShare(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/platLossShare");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 亏损试分摊-数据（过期）
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/platLossShare/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage platLossShareData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        pagin.setRows(Integer.MAX_VALUE);
        // 基本分摊model
        ShareLossModel basicShareLossModel = new ShareLossModel();
        List<ShareLossModel> shareList = new ArrayList<ShareLossModel>();
        BigDecimal superUsdxDebit = BigDecimal.ZERO;
        AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairMoney);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        List<AccountDebitAsset> debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            accountDebitAsset = debitList.get(0);
            superUsdxDebit = accountDebitAsset.getDebitAmt();
        }
        BigDecimal superBtcDebit = BigDecimal.ZERO;
        accountDebitAsset = new AccountDebitAsset();
        accountDebitAsset.setBorrowerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountDebitAsset.setStockinfoId(exchangePairVCoin);
        accountDebitAsset.setRelatedStockinfoId(exchangePairMoney);
        debitList = accountDebitAssetService.findList(accountDebitAsset);
        if (debitList.size() > 0)
        {
            accountDebitAsset = debitList.get(0);
            superBtcDebit = accountDebitAsset.getDebitAmt();
        }
        BigDecimal settlementPrice = getSettlementPrice(exchangePairMoney);
        BigDecimal superBtcAsset = BigDecimal.ZERO;
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            superBtcAsset = list.get(0).getAmount();
        }
        BigDecimal superUsdxAsset = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        accountContractAsset.setStockinfoId(exchangePairMoney);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        list = accountContractAssetService.findList(accountContractAsset);
        if (list.size() > 0)
        {
            superUsdxAsset = list.get(0).getAmount();
        }
        BigDecimal reserveFund = BigDecimal.ZERO;
        accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setStockinfoId(exchangePairVCoin);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> asstList = accountContractAssetService.findList(accountContractAsset);
        if (asstList.size() > 0)
        {
            AccountContractAsset asset = asstList.get(0);
            reserveFund = asset.getAmount();
            logger.debug("存在准备金：" + reserveFund);
        }
        else
        {
            logger.debug("不存在准备金：" + reserveFund);
        }
        basicShareLossModel.setReserveFund(reserveFund);
        BigDecimal needBtc = superBtcAsset.subtract(superBtcDebit).add((superUsdxAsset.subtract(superUsdxDebit)).divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP));
        logger.debug("盈亏情况：" + needBtc);
        boolean result = needBtc.compareTo(BigDecimal.ZERO) >= 0;
        logger.debug("比较的结果：" + result);
        basicShareLossModel.setResult(result);
        if (result)
        {
            needBtc = BigDecimal.ZERO;
            logger.debug("需分摊数字货币:" + needBtc);
        }
        else
        {
            // 比较结果 如果是false 则亏损
            needBtc = needBtc.abs();
            if (reserveFund.compareTo(BigDecimal.ZERO) > 0)
            {
                logger.debug("准备金有余额：" + reserveFund);
                if (reserveFund.compareTo(needBtc) >= 0)
                {// 准备金足够分摊
                    logger.debug("准备金足够分摊");
                    needBtc = BigDecimal.ZERO;
                }
                else
                {
                    logger.debug("准备金部分分摊");
                    needBtc = needBtc.subtract(reserveFund);
                }
            }
            else
            {
                logger.debug("准备金无余额：" + reserveFund);
            }
        }
        basicShareLossModel.setSuperDebit(superUsdxDebit.divide(settlementPrice, 12, BigDecimal.ROUND_HALF_UP).add(superBtcDebit));
        basicShareLossModel.setHangqing(settlementPrice);
        basicShareLossModel.setNeedBtc(needBtc);
        BigDecimal superYingLi = BigDecimal.ZERO;
        List<Account> accounts = accountService.selectAll();
        for (Account account : accounts)
        {
            if (account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID)
            {
                // 当前账户-当前数字货币持仓数量 tableName 已在服务层处理
                // 交割过程到这一步 法定货币借款等于0 数字货币借款等于0 法定货币资产和冻结等于0 数字货币冻结等于0 数字货币净值=数字货币账户资产
                ContractAssetModel modelPerson = accountContractAssetService.findAccountSumContractAsset(exchangePairVCoin, exchangePairMoney, account.getId());
                BigDecimal allBtcOfPersion = modelPerson.getSumAmount();
                BigDecimal allBtcSumInitialOfPersion = modelPerson.getSumInitialAmt();
                BigDecimal allInOfPersion = modelPerson.getSumFlowInAmt();
                logger.debug("当前账户" + account.getId() + "--总流入数字货币数量:" + allInOfPersion);
                BigDecimal allOutOfPersion = modelPerson.getSumFlowOutAmt();
                logger.debug("当前用户" + account.getId() + "-总流出数字货币数量:" + allOutOfPersion);
                BigDecimal persionYingLi = allBtcOfPersion.subtract(allBtcSumInitialOfPersion).subtract(allInOfPersion).add(allOutOfPersion);
                if (persionYingLi.compareTo(BigDecimal.ZERO) > 0)
                {
                    superYingLi = superYingLi.add(persionYingLi);
                }
            }
        }
        logger.debug("查询全市场- 数字货币盈利:" + superYingLi);
        basicShareLossModel.setSuperYingLi(superYingLi);
        for (Account account : accounts)
        {
            if (account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID
                    && account.getId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID)
            {
                // 当前账户-当前数字货币持仓数量 tableName已在服务层处理
                // 交割过程到这一步 法定货币借款等于0 数字货币借款等于0 法定货币资产和冻结等于0 数字货币冻结等于0 数字货币净值=数字货币账户资产
                ContractAssetModel modelPerson = accountContractAssetService.findAccountSumContractAsset(exchangePairVCoin, exchangePairMoney, account.getId());
                BigDecimal allBtcOfPersion = modelPerson.getSumAmount();
                logger.debug("当前账户" + account.getId() + ":" + allBtcOfPersion);
                BigDecimal allBtcSumInitialOfPersion = modelPerson.getSumInitialAmt();
                BigDecimal allInOfPersion = modelPerson.getSumFlowInAmt();
                logger.debug("当前账户" + account.getId() + "--总流入数字货币数量:" + allInOfPersion);
                BigDecimal allOutOfPersion = modelPerson.getSumFlowOutAmt();
                logger.debug("当前用户" + account.getId() + "-总流出数字货币数量:" + allOutOfPersion);
                BigDecimal persionYingLi = allBtcOfPersion.subtract(allBtcSumInitialOfPersion).subtract(allInOfPersion).add(allOutOfPersion);
                logger.debug("当前用户" + account.getId() + "- 数字货币盈利:" + persionYingLi);
                if (persionYingLi.compareTo(BigDecimal.ZERO) > 0)
                {
                    ShareLossModel shareLossModel = new ShareLossModel();
                    exchange(basicShareLossModel, shareLossModel);
                    shareLossModel.setAccountId(account.getId());
                    shareLossModel.setAccountName(account.getAccountName());
                    shareLossModel.setPersonBtc(allBtcOfPersion);
                    shareLossModel.setPersonIn(allInOfPersion);
                    shareLossModel.setPersonOut(allOutOfPersion);
                    shareLossModel.setPersonYingLi(persionYingLi);
                    logger.debug("当前用户" + account.getId() + "- 数字货币盈利比例:" + persionYingLi.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP));
                    BigDecimal fenTanBtc = persionYingLi.divide(superYingLi, 12, BigDecimal.ROUND_HALF_UP).multiply(needBtc);
                    logger.debug("当前用户" + account.getId() + "- 需分摊数字货币:" + fenTanBtc);
                    shareLossModel.setShareLossAmt(fenTanBtc);
                    logger.debug("当前用户" + account.getId() + "组装数据：" + shareLossModel.toString());
                    shareList.add(shareLossModel);
                }
            }
        }
        PaginateResult<ShareLossModel> retList = new PaginateResult<>();
        retList.setPage(pagin);
        retList.setList(shareList);
        return getJsonMessage(CommonEnums.SUCCESS, retList);
    }
    
    /**
     * 亏损实际分摊-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/platLossShareConfirm")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView platLossShareConfirm(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/platLossShareConfirm");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        mav.addObject("timeStart", sysParameter.getUpdateDate());
        return mav;
    }
    
    /**
     * 超级用户全部成交的委托挂单(从上一个交割时间)-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/superentrust")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView superEntrustVCoinMoneyAllDeal(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/superentrust");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 超级用户全部成交的委托挂单(从上一个交割时间)-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/superentrust/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage superEntrustVCoinMoneyAllDealData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setStatus(TradeEnums.DEAL_STATUS_ALLDEAL.getCode());
        entrustVCoinMoney.setEntrustAccountType(true);// 系统下单
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.findListAfterPreSettlement(pagin, entrustVCoinMoney);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     *  准备金分摊提取-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/contributionQuota")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView doContributionQuota(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/contributionQuota");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 准备准备金提取-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/contributionQuota/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage contributionQuotaData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setRelatedStockinfoId(exchangePairMoney);
        accountFundCurrent.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SETTLEMENT_VCOIN_CONTRIBUTION_QUOTA_EXCHANGE_INCREASE);
        accountFundCurrent.setTableName(getStockInfo(exchangePairMoney).getTableFundCurrent());
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.search(pagin, accountFundCurrent);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 超级用户准备金资金查询-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/contributionQuota/admin", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage contributionQuotaAdminData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID);
        accountContractAsset.setRelatedStockinfoId(exchangePairMoney);
        accountContractAsset.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        List<AccountContractAsset> list = accountContractAssetService.findList(accountContractAsset);
        return getJsonMessage(CommonEnums.SUCCESS, list);
    }
    
    /**
     * 交换属性 把model1里面的部分属性给model2
     */
    protected void exchange(ShareLossModel model1, ShareLossModel model2)
    {
        model2.setSuperYingLi(model1.getSuperYingLi());
        model2.setAllOut(model1.getAllOut());
        model2.setAllIn(model1.getAllIn());
        model2.setAllBtc(model1.getAllBtc());
        model2.setResult(model1.getResult());
        model2.setNeedBtc(model1.getNeedBtc());
        model2.setSuperDebit(model1.getSuperDebit());
        model2.setHangqing(model1.getHangqing());
        model2.setInEntrustBtc(model1.getInEntrustBtc());
    }

    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/settlement/updateTradeMasterSwitch", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:operator")
    public JsonMessage updateTradeMasterSwitch(String status) throws BusinessException
    {
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (StringUtils.equalsIgnoreCase(status, "yes"))
        {
            if (StringUtils.equalsIgnoreCase(status, sysParameter.getValue()))
            {
                insertSettlementProcessLog(0L, -1, "打开交易总控开关", "重复打开交易总控开关");
                throw new BusinessException("交易总控开关状态错误");
            }
            sysParameter.setValue("yes");
            sysParameterService.updateByPrimaryKey(sysParameter);
        }
        else if (StringUtils.equalsIgnoreCase(status, "no"))
        {
            if (StringUtils.equalsIgnoreCase(status, sysParameter.getValue()))
            {
                insertSettlementProcessLog(0L, -1, "打开交易总控开关", "重复关闭交易总控开关");
                throw new BusinessException("交易总控开关状态错误");
            }
            sysParameter.setValue("no");
            sysParameterService.updateByPrimaryKey(sysParameter);
        }
        else
        {
            insertSettlementProcessLog(0L, -1, "打开交易总控开关", "参数非法");
            throw new BusinessException("交易总控开关状态错误");
        }
        sysParameter.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        JsonMessage msg = getJsonMessage(CommonEnums.SUCCESS);
        return msg;
    }

    /**
     * 公用-插入流程清算日志
     * @param step
     * @param status
     * @param processName
     */
    public void insertSettlementProcessLog(Long step, int status, String processName, String remark)
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        SettlementProcessLog log = new SettlementProcessLog();
        log.setCreateBy(principal.getId());
        log.setCreateDate(new Timestamp(System.currentTimeMillis()));
        log.setProcessId(1001L);
        log.setStatus(BigDecimal.valueOf(status));
        log.setProcessName(step + "." + processName);
        log.setRemark(remark);
        settlementProcessLogService.insert(log);
    }

    /**
     * 公用-步骤处理
     * @param
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/settlement/step/operator", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:operator")
    public JsonMessage stepOperator(Integer step, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        JsonMessage msg = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        settlementService.stepOperator(step,exchangePairMoney,exchangePairVCoin,principal.getId());
        return msg;
    }
    
    /**
     * 结算价维护
     * @param
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/settlement/updateSettlementPrice", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:operator")
    public JsonMessage updateSettlementPrice(BigDecimal jiesuanjia, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        JsonMessage msg = getJsonMessage(CommonEnums.SUCCESS);
        List<StockInfo> list = stockInfoService.findListByIds(exchangePairMoney.toString());
        if (list.size() > 0)
        {
            StockInfo info = list.get(0);
            info.setSettlementPrice(jiesuanjia);
            stockInfoService.updateByPrimaryKey(info);
        }
        else
        {
            logger.debug("结算价不存在");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return msg;
    }
    
    /**
     * 交割委托列表-未撤销的委托导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlement/settleEntrust")
    @RequiresPermissions("trade:setting:settlement:index")
    public ModelAndView settleEntrust(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/settlement/settleEntrust");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        return mav;
    }
    
    /**
     * 交割挂单-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/settleEntrust/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage settleEntrustdata(Pagination pagin, EntrustVCoinMoney entrustVCoinMoney, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_SETTLEMENTTRADE.getCode());// 交割交易
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.findListAfterPreSettlement(pagin, entrustVCoinMoney);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 操作日志-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/steplog/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage steplogData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        SettlementProcessLog settlementProcessLog = new SettlementProcessLog();
        settlementProcessLog.setProcessId(1001L);
        pagin.setOrder(" a.id desc ");
        PaginateResult<SettlementProcessLog> result = settlementProcessLogService.findDoingLogList(pagin, settlementProcessLog);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 公用-查询账户流水表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/current/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage currentData(AccountFundCurrent entity, String timeStart, String timeEnd, Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin)
            throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd);
        }
        entity.setRelatedStockinfoId(exchangePairMoney);
        entity.setTableName(getStockInfo(exchangePairMoney).getTableFundCurrent());
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.search(pagin, entity);
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
     * 公共方法 获取结算价
     * @param stockInfoId
     * @return
     * @throws BusinessException
     */
    public BigDecimal getSettlementPrice(Long stockInfoId) throws BusinessException
    {
        BigDecimal ret = BigDecimal.ZERO;
        List<StockInfo> list = stockInfoService.findListByIds(stockInfoId.toString());
        if (list.size() > 0)
        {
            StockInfo info = list.get(0);
            if (info.getSettlementPrice() == null)
            {
                logger.debug("结算价不存在");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            ret = info.getSettlementPrice();
        }
        else
        {
            ret = BigDecimal.ZERO;
            logger.debug("结算价不存在");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return ret;
    }
    
    /**
     * 获取证券信息
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlement/stockinfo/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:data")
    public JsonMessage stockinfoData(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        StockInfo stockInfo = new StockInfo();
        List<StockInfo> list = stockInfoService.findListByIds(exchangePairMoney.toString());
        if (list.size() > 0)
        {
            stockInfo = list.get(0);
        }
        return getJsonMessage(CommonEnums.SUCCESS, stockInfo);
    }

    public  StockInfo  getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
