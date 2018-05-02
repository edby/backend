/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
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
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundAdjust;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountFundAdjustService;
import com.blocain.bitms.trade.fund.service.FundCurrentService;
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
import java.sql.Timestamp;
import java.util.List;

/**
 * 资金账户 强增强减
 * <p>File：AccountFundAdjustController.java</p>
 * <p>Title: AccountFundAdjustController</p>
 * <p>Description:AccountFundAdjustController</p>
 * <p>Copyright: Copyright (c) 2017年8月8日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountFundAdjustController extends GenericController
{
    @Autowired(required = false)
    private AccountFundAdjustService accountFundAdjustService;
    
    @Autowired(required = false)
    private FundCurrentService       fundCurrentService;
    
    @Autowired(required = false)
    private AccountService           accountService;

    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String      keyPrefix        = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();

    public void setAccountAssetCache(Long accountId,  Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        RedisUtils.del(key);
    }
    
    /**
     * 列表页面导航-强增强减
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/adjust")
    @RequiresPermissions("trade:setting:accountfundadjust:index")
    public String list() throws BusinessException
    {
        return "trade/fund/adjust/list";
    }
    
    /**
     * 强增强减  新增
     * @param accountFundAdjust
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/adjust/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountfundadjust:operator")
    public JsonMessage save(AccountFundAdjust accountFundAdjust, String lockEndDay, String accountName) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        Account account = accountService.findByNameAndNormal(accountName);
        if (account == null)
        {
            JsonMessage json = getJsonMessage(CommonEnums.ERROR_LOGIN_ACCOUNT);
            return json;
        }
        accountFundAdjust.setAccountId(account.getId());
        if (StringUtils.isNotBlank(lockEndDay))
        {
            lockEndDay += "";
            Long pdate = CalendarUtils.getLongFromTime(lockEndDay, DateConst.DATE_FORMAT_YMDHMS);
            accountFundAdjust.setLockEndDay(new Timestamp(pdate));
        }
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        if (null == accountFundAdjust.getId())
        {
            accountFundAdjust.setCreateBy(principal.getId());
            accountFundAdjust.setCreateDate(new Timestamp(System.currentTimeMillis()));
        }
        accountFundAdjust.setUpdateBy(principal.getId());
        accountFundAdjust.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountFundAdjust.setLockStatus(FundConsts.ASSET_LOCK_STATUS_YES);
        if (beanValidator(json, accountFundAdjust))
        {
            accountFundAdjust.setAdjustAmt(accountFundAdjust.getAdjustAmt().setScale(6,BigDecimal.ROUND_DOWN));
            System.out.println(accountFundAdjust.getAdjustAmt()+"=======================");
            accountFundAdjust.setAdjustAmt(accountFundAdjust.getAdjustAmt().setScale(8,BigDecimal.ROUND_DOWN));
            System.out.println(accountFundAdjust.getAdjustAmt()+"=======================");
            // 调用fund
            FundModel fundModel = new FundModel();
            fundModel.setAccountId(accountFundAdjust.getAccountId());
            fundModel.setStockinfoId(accountFundAdjust.getStockinfoId());
            fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
            fundModel.setBusinessFlag(accountFundAdjust.getBusinessFlag());
            fundModel.setAmount(accountFundAdjust.getAdjustAmt());
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setCreateBy(principal.getId());
            fundModel.setAmountEx(BigDecimal.ZERO);
            if (accountFundAdjust.getLockEndDay() == null)
            {
                accountFundAdjust.setLockEndDay(new Timestamp(System.currentTimeMillis()));
            }
            fundCurrentService.doSaveAccountFundAdjust(accountFundAdjust, fundModel);
        }
        StockInfo info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_PURESPOT);
        List<StockInfo> list = stockInfoService.findList(info);
        for(StockInfo stockInfo:list)
        {
            setAccountAssetCache( accountFundAdjust.getAccountId(), stockInfo.getId());
        }
        return json;
    }
    
    /**
     * 添加界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/adjust/modify")
    @RequiresPermissions("trade:setting:accountfundadjust:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/adjust/modify");
        AccountFundAdjust accountFundAdjust = new AccountFundAdjust();
        if (id != null)
        {
            accountFundAdjust = accountFundAdjustService.selectByPrimaryKey(id);
        }
        mav.addObject("accountFundAdjust", accountFundAdjust);
        return mav;
    }
    
    /**
     * 查询  强增强碱
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/adjust/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountfundadjust:data")
    public JsonMessage data(AccountFundAdjust entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountFundAdjust> result = accountFundAdjustService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
