/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Fund充币入金  控制器
 * <p>File：ChargeController.java</p>
 * <p>Title: ChargeController</p>
 * <p>Description:ChargeController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund充币入金")
public class ChargeController extends GenericController
{
    public static final Logger           logger = LoggerFactory.getLogger(ChargeController.class);
    
    @Autowired(required = false)
    private SystemWalletAddrService         systemWalletAddrService;
    
    @Autowired(required = false)
    private SystemWalletAddrERC20Service    systemWalletAddrERC20Service;
    
    @Autowired(required = false)
    private BlockTransConfirmService        blockTransConfirmService;

    @Autowired(required = false)
    private BlockTransConfirmERC20Service   blockTransConfirmERC20Service;
    
    @Autowired(required = false)
    private AccountCertificationService     accountCertificationService;
    
    @Autowired(required = false)
    private AccountService                  accountService;
    
    @Autowired(required = false)
    private BankRechargeService             bankRechargeService;
    
    @Autowired(required = false)
    private StockInfoService                stockInfoService;

    @Autowired(required = false)
    private AccountFundCurrentService       accountFundCurrentService;
    
    /**
     * Fund充币入金页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/charge", method = RequestMethod.GET)
    @ApiOperation(value = "Fund充币入金页面导航", httpMethod = "GET")
    public ModelAndView charge() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/charge");
        Map<String, Object> map = new HashMap<String, Object>();
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        Account account = accountService.selectByPrimaryKey(principal.getId());
        boolean bindMoblie = false;
        if (account != null)
        {
            if (!StringUtils.isBlank(account.getMobNo()))
            {
                bindMoblie = true;
            }
        }
        // 查询目前最新充值钱包对应的最新地址是否存在
        SystemWalletAddr systemWalletAddr = new SystemWalletAddr();
        systemWalletAddr.setAccountId(principal.getId());
        systemWalletAddr.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
        systemWalletAddr.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        List<SystemWalletAddr> list = systemWalletAddrService.findList(systemWalletAddr);
        if (ListUtils.isNotNull(list))
        {
            systemWalletAddr = list.get(0);
            map.put("btcWalletAddr", systemWalletAddr.getWalletAddr());
            if (null != systemWalletAddr && !systemWalletAddr.verifySignature())
            {// 校验数据
                logger.info("充值地址 数据校验失败");
                map.put("btcWalletAddr", "");
            }
            else
            {
                logger.info("充值地址 数据校验成功");
                map.put("btcWalletAddr", systemWalletAddr.getWalletAddr());
            }
        }
        else
        {
            try
            {
                // add by sunbiao start
                // 默认产生BTC钱包地址信息 注册的时候自动开通可能会影响注册 所以放到这里进行开通
                systemWalletAddrService.createBtcWalletAddress(account.getId(), BitmsConst.DEFAULT_UNID);
                systemWalletAddr = new SystemWalletAddr();
                systemWalletAddr.setAccountId(principal.getId());
                systemWalletAddr.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
                systemWalletAddr.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                list = systemWalletAddrService.findList(systemWalletAddr);
                // add by sunbiao end
                if (list.size() > 0)
                {
                    map.put("btcWalletAddr", list.get(0).getWalletAddr());
                }
                else
                {
                    map.put("btcWalletAddr", "");
                }
            }
            catch (BusinessException e)
            {
                logger.error("创建钱包地址失败:{}", e.getLocalizedMessage());
                map.put("btcWalletAddr", "");
            }
        }
        StockInfo stockInfoSearch = new StockInfo();
        stockInfoSearch.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        List<StockInfo> listCoin = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_ERC20_TOKEN,FundConsts.STOCKTYPE_DIGITALCOIN);
        mav.addObject("listCoin", listCoin);
        mav.addObject("addrs", JSONObject.toJSON(map));
        mav.addObject("bindMoblie", bindMoblie);
        return mav;
    }
    
    /**
     * Fund充币入金页面导航Eth
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/chargeEth", method = RequestMethod.GET)
    @ApiOperation(value = "Fund充币入金页面导航", httpMethod = "GET")
    public ModelAndView chargeEth(String symbol) throws BusinessException
    {
        if(StringUtils.isBlank(symbol))
        {
            symbol = "eth";
        }
        ModelAndView mav = new ModelAndView("fund/chargeEth");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        String address = "";
        // 查询币种
        StockInfo stockInfo = new StockInfo();
        stockInfo.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        stockInfo.setRemark(symbol);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0) { throw new BusinessException("not has this symbol!"); }
        stockInfo = stockInfoList.get(0);
        // 查询目前最新充值钱包对应的最新地址是否存在
        SystemWalletAddrERC20 systemWalletAddrERC20 = new SystemWalletAddrERC20();
        systemWalletAddrERC20.setAccountId(principal.getId().toString());
        systemWalletAddrERC20.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
        systemWalletAddrERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
        List<SystemWalletAddrERC20> list = systemWalletAddrERC20Service.findList(systemWalletAddrERC20);
        if (ListUtils.isNotNull(list))
        {
            systemWalletAddrERC20 = list.get(0);
            if (null != systemWalletAddrERC20 && !systemWalletAddrERC20.verifySignature())
            {// 校验数据
                logger.info("充值地址 数据校验失败");
                address = "";
            }
            else
            {
                logger.info("充值地址 数据校验成功");
                address = systemWalletAddrERC20.getWalletAddr();
            }
        }
        else
        {
            try
            {
                // 默认产生ETH钱包地址信息
                systemWalletAddrERC20Service.createERC20WalletAddress(principal.getId(), BitmsConst.DEFAULT_UNID, FundConsts.WALLET_ETH_TYPE);
                systemWalletAddrERC20 = new SystemWalletAddrERC20();
                systemWalletAddrERC20.setAccountId(principal.getId().toString());
                systemWalletAddrERC20.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
                systemWalletAddrERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                list = systemWalletAddrERC20Service.findList(systemWalletAddrERC20);
                // add by sunbiao end
                if (list.size() > 0)
                {
                    address = list.get(0).getWalletAddr();
                }
                else
                {
                    address = "";
                }
            }
            catch (BusinessException e)
            {
                logger.error("创建钱包地址失败:{}", e.getLocalizedMessage());
                address = "";
            }
        }
        StockInfo stockInfoSearch = new StockInfo();
        stockInfoSearch.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        List<StockInfo> listCoin = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_ERC20_TOKEN,FundConsts.STOCKTYPE_DIGITALCOIN);
        mav.addObject("listCoin", listCoin);
        mav.addObject("address", address);
        mav.addObject("symbol", stockInfo.getStockCode());
        mav.addObject("stockInfo", stockInfo);
        return mav;
    }
    
    /**
     * Fund充币入金页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/chargeCash", method = RequestMethod.GET)
    @ApiOperation(value = "Fund充币入金页面导航", httpMethod = "GET")
    public ModelAndView chargeEUR() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/chargeCash");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountCertification tempCertification = accountCertificationService.findByAccountId(principal.getId());
        boolean certStatus = false;
        if (null != tempCertification)
        {
            if (tempCertification.getStatus().intValue() == 1)
            {
                certStatus = true;
            }
            mav.addObject("realName", tempCertification.getRealname() + " " + tempCertification.getSurname());
        }
        else
        {
            mav.addObject("realName", "");
        }
        mav.addObject("certStatus", certStatus);
        return mav;
    }
    
    /**
     * Fund现金充值历史列表
     * @param bankRecharge
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/charge/cashChargeList", method = RequestMethod.POST)
    @ApiOperation(value = "Fund现金充值历史列表", httpMethod = "POST")
    public JsonMessage chargeCashList(@ModelAttribute BankRecharge bankRecharge, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 证券信息ID判断
        if (null == bankRecharge.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        bankRecharge.setAccountId(principal.getId());
        bankRecharge.setStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
        PaginateResult<BankRecharge> result = bankRechargeService.search(pagin, bankRecharge);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * Fund充币入金历史列表
     * @param blockTransConfirm
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/charge/chargeList", method = RequestMethod.POST)
    @ApiOperation(value = "Fund充币入金历史列表", httpMethod = "POST")
    public JsonMessage chargeList(@ModelAttribute BlockTransConfirm blockTransConfirm, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 证券信息ID判断
        if (null == blockTransConfirm.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        blockTransConfirm.setAccountId(principal.getId());
        blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        PaginateResult<BlockTransConfirm> result = blockTransConfirmService.findChargeList(pagin, blockTransConfirm);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * erc20 充值查询
     * @param blockTransConfirmERC20
     * @param pagin
     * @return
     * @throws BusinessException
     * @author ZCX 2018.03.01
     */
    @ResponseBody
    @RequestMapping(value = "/charge/chargeERC20List", method = RequestMethod.POST)
    @ApiOperation(value = "Fund ERC20 TOKEN 充值查询", httpMethod = "POST")
    public JsonMessage chargeERC20List(@ModelAttribute BlockTransConfirmERC20 blockTransConfirmERC20, @ModelAttribute Pagination pagin) throws BusinessException
    {
        // 证券信息ID判断
        if (null == blockTransConfirmERC20.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        blockTransConfirmERC20.setAccountId(principal.getId());
        blockTransConfirmERC20.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        PaginateResult<BlockTransConfirmERC20> result = blockTransConfirmERC20Service.findConfirmERC20ChargeList(pagin, blockTransConfirmERC20);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * Fund充币入金根据证券id获取充值钱包地址
     * @param stockinfoId
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/charge/getWalletAddr", method = RequestMethod.POST)
    @ApiOperation(value = "Fund充币入金根据证券id获取充值钱包地址", httpMethod = "POST")
    public JsonMessage getWalletAddrByStockinfoId(Long stockinfoId) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        // 证券信息ID判断
        if (null == stockinfoId) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        SystemWalletAddr systemWalletAddr = new SystemWalletAddr();
        systemWalletAddr.setAccountId(principal.getId());
        systemWalletAddr.setStockinfoId(stockinfoId);
        systemWalletAddr.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
        List<SystemWalletAddr> list = systemWalletAddrService.findList(systemWalletAddr);
        if (list.size() > 0)
        {
            systemWalletAddr = list.get(0);
        }
        return getJsonMessage(CommonEnums.SUCCESS, systemWalletAddr);
    }
}
