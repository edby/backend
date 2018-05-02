/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.exchange.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.fund.service.FundCurrentService;
import com.blocain.bitms.trade.quotation.QuotationController;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 *  Fund资金流水  控制器
 * <p>File：CurrentsController.java</p>
 * <p>Title: CurrentsController</p>
 * <p>Description:CurrentsController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping("")
@Api(description = "交易")
public class ExchangeController extends QuotationController
{
    public static final Logger           logger = LoggerFactory.getLogger(ExchangeController.class);
    
    @Autowired(required = false)
    private FundCurrentService        fundCurrentService;
    
    @Autowired(required = false)
    private AccountWalletAssetService accountWalletAssetService;
    
    @Autowired(required = false)
    private EnableService             enableService;
    
    @Autowired(required = false)
    private AccountService            accountService;
    
    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    @Autowired(required = false)
    private Erc20TokenService         erc20TokenService;
    
    @Autowired(required = false)
    private SysParameterService          sysParameterService;
    
    /**
     * market
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/market", method = RequestMethod.GET)
    @ApiOperation(value = "market", httpMethod = "GET")
    public ModelAndView market(HttpServletRequest request, HttpServletResponse response) throws BusinessException
    {
        if (localeResolver == null) { throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?"); }
        localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString("en_US"));
        ModelAndView mav = new ModelAndView("exchange/market");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        return mav;
    }

    /**
     * erc20Token
     * @param erc20Token
     * @param pagin
     * @return
     * @throws BusinessException
     * @author zhangchunxi 2018-04-25 19:01:14
     */
    @ResponseBody
    @RequestMapping(value = "/exchange/tokens")
    @ApiOperation(value = "ERC20 TOKEN")
    public JsonMessage withdrawEthList(@ModelAttribute Erc20Token erc20Token,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<Erc20Token> result = erc20TokenService.search(pagin, erc20Token);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * exchange
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/exchange", method = RequestMethod.GET)
    @ApiOperation(value = "exchange", httpMethod = "GET")
    public ModelAndView exchange(HttpServletRequest request, HttpServletResponse response, String contractAddr) throws BusinessException
    {
        if (localeResolver == null) { throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?"); }
        localeResolver.setLocale(request, response, org.springframework.util.StringUtils.parseLocaleString("en_US"));
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        ModelAndView mav = new ModelAndView("exchange/exchange");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        // ==============================检查交易对 start====================================
        if (StringUtils.isBlank(contractAddr) || contractAddr.equalsIgnoreCase("eth"))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
        }
        StockInfo entity = new StockInfo();
        entity.setStockType(FundConsts.STOCKTYPE_PURESPOT);
        entity.setTokenContactAddr(contractAddr);
        entity.setIsActive(FundConsts.ASSET_LOCK_STATUS_YES);
        List<StockInfo> stockInfoList = stockInfoService.findList(entity);
        if (stockInfoList.size() == 0)
        {
            mav.addObject("stockInfo", null);
            if (isLogin)
            {
                // vin 可用
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
                enableModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_BIEX_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
                mav.addObject("vinEnable", enableModel);
                enableModel.setAccountId(principal.getId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
                enableModel.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_ETH_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
                mav.addObject("ethEnable", enableModel);
            }
            else
            {
                EnableModel enableModel = new EnableModel();
                enableModel.setEnableAmount(BigDecimal.ZERO);
                mav.addObject("vinEnable", enableModel);
                mav.addObject("ethEnable", enableModel);
            }
            SysParameter params = new SysParameter();
            params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
            params.setParameterName(ParamConsts.ERC20TOKEN_PAIR_ACTIVE_FEE);
            params = sysParameterService.getSysParameterByEntity(params);
            if (params == null) { throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN); }
            if (StringUtils.isBlank(params.getValue())) { throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN); }
            mav.addObject("vinOpenFee", BigDecimal.valueOf(Double.parseDouble(params.getValue())));
        }
        else
        {
            StockInfo pairStock = stockInfoList.get(0);
            if (isLogin)
            {
                AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
                accountWalletAsset.setId(principal.getId());
                accountWalletAsset.setStockinfoId(pairStock.getTradeStockinfoId());
                List<AccountWalletAsset> assetlist = accountWalletAssetService.findList(accountWalletAsset);
                accountWalletAsset.setIsActiveTrade("no");
                if (assetlist.size() > 0)
                {
                    accountWalletAsset = assetlist.get(0);
                }
                if (StringUtils.isBlank(accountWalletAsset.getIsActiveTrade()))
                {
                    accountWalletAsset.setIsActiveTrade("no");
                }
                mav.addObject("isActiveTrade", accountWalletAsset.getIsActiveTrade());
                // vin 可用
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
                enableModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_BIEX_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
                mav.addObject("vinEnable", enableModel);
                // ==============================检查交易对 end====================================
            }
            mav.addObject("stockInfo", pairStock);
            if (pairStock != null && !StringUtils.equalsIgnoreCase(pairStock.getIsActive(), "yes"))
            {
                mav.addObject("stockInfo", null);
            }
            // ==============================充值地址 end====================================
        }
        // ==============================TOKEN start====================================
        Erc20Token erc20Token = erc20TokenService.getErc20Token(contractAddr, null);
        if (erc20Token == null)
        {
            mav.addObject("symbol", "");
            mav.addObject("symbolName", "");
            mav.addObject("totalSupply", "");
            mav.addObject("ERC20ContractEnd", "");
            mav.addObject("ERC20Contract", "");
            mav.addObject("activeDays", 0L);
            mav.addObject("isActive", "");
        }
        else
        {
            mav.addObject("symbol", erc20Token.getSymbol());
            mav.addObject("symbolName", erc20Token.getSymbolName());
            mav.addObject("totalSupply", erc20Token.getTotalSupply());
            mav.addObject("ERC20ContractEnd",
                    erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
            mav.addObject("ERC20Contract", erc20Token.getContractAddr());
            Double day = DateUtils.getDistanceOfTwoDate(new Date(), erc20Token.getActiveEndDate());
            if (day < 0) day = 0d;
            mav.addObject("activeDays", day);
            mav.addObject("isActive", erc20Token.getIsActive());
        }
        // ==============================TOKEN end====================================
        // 获取所有的TOKEN
        // mav.addObject("tokens", erc20TokenService.selectAll());
        mav.addObject("label", true);
        return mav;
    }
    
    /**
     * 获取tokens
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/exchange/getTokens")
    public JsonMessage getTokens(Erc20Token erc20Token) throws BusinessException
    {
        //List<Erc20Token> result = erc20TokenService.selectAll();
        List<Erc20Token> result = erc20TokenService.searchByKey(erc20Token.getSymbol(),erc20Token.getContractAddr());
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 交易流水
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/exchange/billHistory", method = RequestMethod.GET)
    @ApiOperation(value = "交易流水", httpMethod = "GET")
    public ModelAndView financialCurrents(String contractAddr) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/billHistory");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        Long id;
        if (StringUtils.isBlank(contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
        }
        if (StringUtils.equalsIgnoreCase("eth", contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
            id = getStockInfo(contractAddr).getCapitalStockinfoId();
        }
        else
        {
            id = getStockInfo(contractAddr).getTradeStockinfoId();
        }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(id);
        Erc20Token erc20Token = erc20TokenService.getErc20Token(contractAddr, null);
        if (erc20Token == null)
        {
            mav.addObject("symbol", "");
            mav.addObject("symbolName", "");
            mav.addObject("totalSupply", "");
            mav.addObject("ERC20ContractEnd", "");
            mav.addObject("ERC20Contract", "");
            mav.addObject("activeDays", "0");
        }
        else
        {
            mav.addObject("symbol", stockInfo.getStockCode());
            mav.addObject("symbolName", stockInfo.getStockName());
            mav.addObject("totalSupply", erc20Token.getTotalSupply());
            mav.addObject("ERC20ContractEnd",
                    erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
            mav.addObject("ERC20Contract", stockInfo.getTokenContactAddr());
            Double day = DateUtils.getDistanceOfTwoDate(new Date(), erc20Token.getActiveEndDate());
            if (day < 0) day = 0d;
            mav.addObject("activeDays", day.longValue());
        }
        mav.addObject("stockinfoId", id);
        mav.addObject("label", true);
        return mav;
    }
    
    public StockInfo getStockInfo(String addr)
    {
        StockInfo stockInfo = stockInfoService.findByContractAddr(addr);
        if (!StringUtils.equalsIgnoreCase(stockInfo.getIsActive(), FundConsts.PUBLIC_STATUS_YES)) { throw new BusinessException("pair not open"); }
        return stockInfo;
    }
    
    /**
     * 激活交易对导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/exchange/activeToken", method = RequestMethod.GET)
    @ApiOperation(value = "激活交易对导航界面", httpMethod = "GET")
    public ModelAndView activeToken(String contractAddr) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/activeToken");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        boolean isLogin = checkLogin();
        if (isLogin)
        {
            if (isLogin)
            {
                // vin 可用
                EnableModel enableModel = new EnableModel();
                enableModel.setAccountId(principal.getId());
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
                enableModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_BIEX_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
                mav.addObject("vinEnable", enableModel);
            }
            else
            {
                EnableModel enableModel = new EnableModel();
                enableModel.setEnableAmount(BigDecimal.ZERO);
                mav.addObject("vinEnable", enableModel);
            }
        }
        else
        {
            EnableModel enableModel = new EnableModel();
            enableModel.setEnableAmount(BigDecimal.ZERO);
            mav.addObject("vinEnable", enableModel);
            mav.addObject("vinOpenFee", BigDecimal.ZERO);
        }
        mav.addObject("longStatus", isLogin);
        if (StringUtils.isBlank(contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
        }
        SysParameter paramsFee = new SysParameter();
        paramsFee.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        paramsFee.setParameterName(ParamConsts.ERC20TOKEN_PAIR_ACTIVE_FEE);
        paramsFee = sysParameterService.getSysParameterByEntity(paramsFee);
        if (paramsFee == null) { throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN); }
        if (StringUtils.isBlank(paramsFee.getValue())) { throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN); }
        mav.addObject("vinOpenFee", BigDecimal.valueOf(Double.parseDouble(paramsFee.getValue())));
        Erc20Token erc20Token = erc20TokenService.getErc20Token(contractAddr, null);
        if (erc20Token == null)
        {
            mav.addObject("symbol", "");
            mav.addObject("symbolName", "");
            mav.addObject("totalSupply", "");
            mav.addObject("ERC20ContractEnd", "");
            mav.addObject("ERC20Contract", "");
            mav.addObject("activeDays", "0");
            mav.addObject("awardStatus", -1);
        }
        else
        {
            mav.addObject("symbol", erc20Token.getSymbol());
            mav.addObject("symbolName", erc20Token.getSymbolName());
            mav.addObject("totalSupply", erc20Token.getTotalSupply());
            mav.addObject("ERC20ContractEnd",
                    erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
            mav.addObject("ERC20Contract", erc20Token.getContractAddr());
            Double day = DateUtils.getDistanceOfTwoDate(new Date(), erc20Token.getActiveEndDate());
            if (day < 0) day = 0d;
            mav.addObject("activeDays", day.longValue());
            mav.addObject("awardStatus", erc20Token.getAwardStatus());
        }
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.ERC20TOKEN_PAIR_ACTIVE_AWARD);
        params = sysParameterService.getSysParameterByEntity(params);
        BigDecimal award = BigDecimal.valueOf(Double.parseDouble(params.getValue()));
        mav.addObject("award", award);
        mav.addObject("label", true);
        return mav;
    }
    
    /**
     * 充值记录
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/exchange/depositHistory", method = RequestMethod.GET)
    @ApiOperation(value = "充值记录", httpMethod = "GET")
    public ModelAndView depositHistory(String contractAddr) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/depositHistory");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        if (StringUtils.isBlank(contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
        }
        Long id;
        if (StringUtils.equalsIgnoreCase("eth", contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
            id = getStockInfo(contractAddr).getCapitalStockinfoId();
        }
        else
        {
            id = getStockInfo(contractAddr).getTradeStockinfoId();
        }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(id);
        Erc20Token erc20Token = erc20TokenService.getErc20Token(contractAddr, null);
        if (erc20Token == null)
        {
            mav.addObject("symbol", "");
            mav.addObject("symbolName", "");
            mav.addObject("totalSupply", "");
            mav.addObject("ERC20ContractEnd", "");
            mav.addObject("ERC20Contract", "");
            mav.addObject("activeDays", "0");
        }
        else
        {
            mav.addObject("symbol", stockInfo.getStockCode());
            mav.addObject("symbolName", stockInfo.getStockName());
            mav.addObject("totalSupply", erc20Token.getTotalSupply());
            mav.addObject("ERC20ContractEnd",
                    erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
            mav.addObject("ERC20Contract", stockInfo.getTokenContactAddr());
            Double day = DateUtils.getDistanceOfTwoDate(new Date(), erc20Token.getActiveEndDate());
            if (day < 0) day = 0d;
            mav.addObject("activeDays", day.longValue());
        }
        mav.addObject("stockinfoId", id);
        mav.addObject("label", true);
        return mav;
    }
    
    /**
     * 提现记录
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/exchange/withdrawHistory", method = RequestMethod.GET)
    @ApiOperation(value = "提现记录", httpMethod = "GET")
    public ModelAndView withdrawHistory(String contractAddr) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/withdrawHistory");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        if (StringUtils.isBlank(contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
        }
        Long id;
        if (StringUtils.equalsIgnoreCase("eth", contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
            id = getStockInfo(contractAddr).getCapitalStockinfoId();
        }
        else
        {
            id = getStockInfo(contractAddr).getTradeStockinfoId();
        }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(id);
        Erc20Token erc20Token = erc20TokenService.getErc20Token(contractAddr, null);
        if (erc20Token == null)
        {
            mav.addObject("symbol", "");
            mav.addObject("symbolName", "");
            mav.addObject("totalSupply", "");
            mav.addObject("ERC20ContractEnd", "");
            mav.addObject("ERC20Contract", "");
            mav.addObject("activeDays", "0");
        }
        else
        {
            mav.addObject("symbol", stockInfo.getStockCode());
            mav.addObject("symbolName", stockInfo.getStockName());
            mav.addObject("totalSupply", erc20Token.getTotalSupply());
            mav.addObject("ERC20ContractEnd",
                    erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
            mav.addObject("ERC20Contract", stockInfo.getTokenContactAddr());
            Double day = DateUtils.getDistanceOfTwoDate(new Date(), erc20Token.getActiveEndDate());
            if (day < 0) day = 0d;
            mav.addObject("activeDays", day.longValue());
        }
        mav.addObject("stockinfoId", id);
        mav.addObject("label", true);
        return mav;
    }
    
    /**
     * 历史委托
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/exchange/orderHistory", method = RequestMethod.GET)
    @ApiOperation(value = "历史委托", httpMethod = "GET")
    public ModelAndView orderHistory(String contractAddr) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/orderHistory");
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        if (StringUtils.isBlank(contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
        }
        Long id;
        if (StringUtils.equalsIgnoreCase("eth", contractAddr))
        {
            contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
            id = getStockInfo(contractAddr).getCapitalStockinfoId();
        }
        else
        {
            id = getStockInfo(contractAddr).getTradeStockinfoId();
        }
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(id);
        Erc20Token erc20Token = erc20TokenService.getErc20Token(contractAddr, null);
        if (erc20Token == null)
        {
            mav.addObject("symbol", "");
            mav.addObject("symbolName", "");
            mav.addObject("totalSupply", "");
            mav.addObject("ERC20ContractEnd", "");
            mav.addObject("ERC20Contract", "");
            mav.addObject("activeDays", "0");
        }
        else
        {
            mav.addObject("symbol", stockInfo.getStockCode());
            mav.addObject("symbolName", stockInfo.getStockName());
            mav.addObject("totalSupply", erc20Token.getTotalSupply());
            mav.addObject("ERC20ContractEnd",
                    erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
            mav.addObject("ERC20Contract", stockInfo.getTokenContactAddr());
            Double day = DateUtils.getDistanceOfTwoDate(new Date(), erc20Token.getActiveEndDate());
            if (day < 0) day = 0d;
            mav.addObject("activeDays", day.longValue());
        }
        mav.addObject("stockinfoId", id);
        mav.addObject("label", true);
        return mav;
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
    
    /**
     * 邀请界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/invitationCode")
    public ModelAndView invitationCode() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/invitationCode");
        boolean isLogin = checkLogin();
        String inviteCode = "";
        if (isLogin)
        {
            UserPrincipal principal = OnLineUserUtils.getPrincipal();
            inviteCode = Long.toHexString(principal.getUnid() * Long.parseLong(DateUtils.getYear()));
        }
        mav.addObject("longStatus", isLogin);
        mav.addObject("inviteCode", inviteCode.toUpperCase());
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.ERC20TOKEN_PAIR_ACTIVE_AWARD);
        params = sysParameterService.getSysParameterByEntity(params);
        BigDecimal award = BigDecimal.valueOf(Double.parseDouble(params.getValue()));
        mav.addObject("award", award);
        return mav;
    }

    /**
     * 领取糖果
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/getCandy")
    public ModelAndView getCandy() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("account/getCandy");
        boolean isLogin = checkLogin();
        if (isLogin)
        {
            UserPrincipal principal = OnLineUserUtils.getPrincipal();
            Account account = accountService.selectByPrimaryKey(principal.getId());
            if(account == null)
            {
                mav.addObject("candyStatus",false);
            }
            else
            {
                if(account.getChargeAward().intValue() != 1)
                {
                    mav.addObject("candyStatus",true);
                }
                else
                {
                    mav.addObject("candyStatus",false);
                }
            }
        }else
        {
            mav.addObject("candyStatus",false);
        }
        return mav;
    }

    /**
     * 获取糖果操作
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/colletCandy")
    public JsonMessage colletCandy() throws BusinessException
    {
        boolean isLogin = checkLogin();
        if (isLogin)
        {
            UserPrincipal principal = OnLineUserUtils.getPrincipal();
            fundCurrentService.doChargeAward(principal.getId());
            return getJsonMessage(CommonEnums.SUCCESS);
        }
        else
        {
            return getJsonMessage(CommonEnums.USER_NOT_LOGIN);
        }
    }
    /**
     * 激活TOKEN交易操作
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/exchange/openToken")
    @ApiOperation(value = "激活TOKEN交易操作", consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({@ApiImplicitParam(name = "addr", value = "合约地址", required = true, paramType = "form")})
    public JsonMessage openToken(String addr, String unid, String ga) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (principal == null) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
        // 解密
        Long uid = 0L;
        BigDecimal bguid = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(unid))
        {
            try
            {
                uid = Integer.parseInt(unid, 16) / Long.parseLong(DateUtils.getYear());
                bguid = BigDecimal.valueOf(Integer.parseInt(unid, 16))
                        .divide(BigDecimal.valueOf(Long.parseLong(DateUtils.getYear())),8,BigDecimal.ROUND_HALF_UP);
                if(bguid.compareTo(BigDecimal.valueOf(uid)) != 0 )
                {
                    throw new BusinessException("Inviting code error");
                }
            }
            catch (Exception e)
            {
                throw new BusinessException("Inviting code error");
            }
        }
        if (uid == 0l) uid = null;
        if (StringUtils.isBlank(addr)) { throw new BusinessException("ERC20 Contract is empty"); }
//        if (StringUtils.isBlank(ga)) { throw new BusinessException(CommonEnums.ERROR_GA_VALID_FAILED); }
       /* // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        Account account = accountService.selectByPrimaryKey(principal.getId());
        // ga没绑定
        if (com.blocain.bitms.tools.utils.StringUtils.isBlank(account.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
        String secretKey = EncryptUtils.desDecrypt(account.getAuthKey());
        Authenticator authenticator = new Authenticator();
        if (!authenticator.checkCode(secretKey, Long.valueOf(ga)))
        {// 判断验证码
            return getJsonMessage(AccountEnums.ACCOUNT_GACODE_ERROR);
        }*/
        erc20TokenService.doActiveToken(addr, principal.getId(), uid);
        // 刷新缓存.
        setAccountAssetCache(principal.getId(), FundConsts.WALLET_BIEX_TYPE, FundConsts.WALLET_VIN2ETH_TYPE);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
