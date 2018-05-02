/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.exchange.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *  Fund账户资产  控制器
 * <p>File：AccountAssetController.java</p>
 * <p>Title: AccountAssetController</p>
 * <p>Description:AccountAssetController</p>
 * <p>Copyright: Copyright (c) 2018-03-28</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping()
@Api(description = "Fund账户资产")
public class AccountAssetController extends GenericController
{
    public static final Logger        logger = LoggerFactory.getLogger(AccountAssetController.class);
    
    @Autowired(required = false)
    private AccountWalletAssetService accountWalletAssetService;
    
    @Autowired(required = false)
    private AccountPolicyService      accountPolicyService;
    
    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    @Autowired(required = false)
    private AccountService            accountService;
    
    /**
     * 账户资产
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    @ApiOperation(value = "账户资产", httpMethod = "GET")
    public ModelAndView AccountAsset(String contractAddr) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/accountAsset");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        mav.addObject("label", false);
        return mav;
    }
    
    /**
     * 账户资产激活界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/wallet/activeTrade", method = RequestMethod.GET)
    @ApiOperation(value = "账户资产", httpMethod = "GET")
    public ModelAndView activeTrade(Long stockinfoId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/accountAssetActive");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        mav.addObject("label", false);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(principal.getId());
        accountWalletAsset.setStockinfoId(stockinfoId);
        List<AccountWalletAsset> assetlist = accountWalletAssetService.findList(accountWalletAsset);
        if (assetlist.size() > 0)
        {
            accountWalletAsset = assetlist.get(0);
        }
        mav.addObject("accountWalletAsset", accountWalletAsset);
        mav.addObject("stockinfo", stockInfoService.selectByPrimaryKey(stockinfoId));
        return mav;
    }
    
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/wallet/doActiveTrade", method = RequestMethod.POST)
    public JsonMessage doActiveTrade(Long stockinfoId, String status, String ga) throws BusinessException
    {
        if (stockinfoId == null || StringUtils.isBlank(status)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if(principal == null ) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(principal.getId());
        accountWalletAsset.setStockinfoId(stockinfoId);
        List<AccountWalletAsset> assetlist = accountWalletAssetService.findList(accountWalletAsset);
        if (assetlist.size() > 0)
        {
            accountWalletAsset = assetlist.get(0);
            if (StringUtils.equalsIgnoreCase(status, "yes"))
            {
                if (StringUtils.isBlank(ga)) { throw new BusinessException(CommonEnums.ERROR_GA_VALID_FAILED); }
                Account account = accountService.selectByPrimaryKey(principal.getId());
                // ga没绑定
                if (com.blocain.bitms.tools.utils.StringUtils.isBlank(account.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
                String secretKey = EncryptUtils.desDecrypt(account.getAuthKey());
                checkGaErrorCnt(account.getUnid(),secretKey,ga);
                accountWalletAsset.setIsActiveTrade(FundConsts.PUBLIC_STATUS_YES);
                accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
            }
            else
            {
                accountWalletAsset.setIsActiveTrade(FundConsts.PUBLIC_STATUS_NO);
                accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
            }
        }
        else
        {
            throw new BusinessException("hasn't this asset");
        }
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 当前账户钱包账户资产
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountAsset/walletAssetData", method = RequestMethod.POST)
    @ApiOperation(value = "当前账户钱包账户资产", httpMethod = "POST")
    public JsonMessage walletAssetData(@ModelAttribute AccountWalletAsset entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 限定自己账户的账户资产情况
        entity.setAccountId(OnLineUserUtils.getId());
        PaginateResult<AccountWalletAsset> data = accountWalletAssetService.tradeXFindList(pagin, entity);
        return this.getJsonMessage(CommonEnums.SUCCESS, data);
    }
    
    public StockInfo getStockInfo(String addr)
    {
        StockInfo stockInfo = stockInfoService.findByContractAddr(addr);
        if (!org.apache.commons.lang3.StringUtils.equalsIgnoreCase(stockInfo.getIsActive(), FundConsts.PUBLIC_STATUS_YES)) { throw new BusinessException("pair not open"); }
        return stockInfo;
    }
    
    /**
     * 检查登录
     */
    public boolean checkLogin()
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (principal == null)
        {
            return false;
        }
        else
        {
            if (principal.getId() == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
