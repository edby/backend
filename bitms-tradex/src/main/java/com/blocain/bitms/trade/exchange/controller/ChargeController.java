/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.exchange.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirmERC20;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddrERC20;
import com.blocain.bitms.trade.fund.service.BlockTransConfirmERC20Service;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.fund.service.SystemWalletAddrERC20Service;
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

import java.util.Date;
import java.util.List;

/**
 *  Fund充币入金  控制器
 * <p>File：ChargeController.java</p>
 * <p>Title: ChargeController</p>
 * <p>Description:ChargeController</p>
 * <p>Copyright: Copyright (c) 2018-03-28</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.EXCHANGE)
@Api(description = "Fund充币入金")
public class ChargeController extends GenericController
{
    public static final Logger            logger = LoggerFactory.getLogger(ChargeController.class);
    
    @Autowired(required = false)
    private BlockTransConfirmERC20Service blockTransConfirmERC20Service;

    @Autowired(required = false)
    private SystemWalletAddrERC20Service systemWalletAddrERC20Service;

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private Erc20TokenService erc20TokenService;

    /**
     * Deposit
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/deposit", method = RequestMethod.GET)
    @ApiOperation(value = "deposit", httpMethod = "GET")
    public ModelAndView deposit(String contractAddr) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("exchange/deposit");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        boolean isLogin = checkLogin();
        mav.addObject("longStatus", isLogin);
        if (isLogin)
        {
            // ==============================充值地址 start====================================
            String address = "";
            // 查询币种
            StockInfo stockInfo = new StockInfo();
            stockInfo.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
            stockInfo.setRemark("eth");
            List<StockInfo> symbolList = stockInfoService.findList(stockInfo);
            if (symbolList.size() == 0) { throw new BusinessException("not has this symbol!"); }
            stockInfo = symbolList.get(0);
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
                    try
                    {
                        systemWalletAddrERC20Service.createERC20WalletAddress(principal.getId(), BitmsConst.DEFAULT_UNID, FundConsts.WALLET_ETH_TYPE);
                    }
                    catch (Exception e)
                    {
                        logger.debug(e.getLocalizedMessage());
                    }
                    systemWalletAddrERC20 = new SystemWalletAddrERC20();
                    systemWalletAddrERC20.setAccountId(principal.getId().toString());
                    systemWalletAddrERC20.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
                    systemWalletAddrERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                    list = systemWalletAddrERC20Service.findList(systemWalletAddrERC20);
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
            mav.addObject("address", address);
            // ==============================充值地址 end====================================
            // ==============================TOKEN start====================================
            if (StringUtils.isBlank(contractAddr))
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
            }
            Long id;
            if(StringUtils.equalsIgnoreCase("eth",contractAddr))
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
                id = getStockInfo(contractAddr).getCapitalStockinfoId();
            }
            else
            {
                if(stockInfoService.findByContractAddr(contractAddr) == null)
                {
                    contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
                }
                id = getStockInfo(contractAddr).getTradeStockinfoId();
            }
            StockInfo stockInfo2 = stockInfoService.selectByPrimaryKey(id);
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
                mav.addObject("symbol", stockInfo2.getStockCode());
                mav.addObject("symbolName", stockInfo2.getStockName());
                mav.addObject("totalSupply", erc20Token.getTotalSupply());
                mav.addObject("ERC20ContractEnd",
                        erc20Token.getContractAddr().length() > 10 ? erc20Token.getContractAddr().substring(erc20Token.getContractAddr().length() - 10) : "");
                mav.addObject("ERC20Contract", stockInfo2.getTokenContactAddr());
                Double day = DateUtils.getDistanceOfTwoDate(new Date(),erc20Token.getActiveEndDate());
                if (day < 0) day = 0d;
                mav.addObject("activeDays", day.longValue());
                mav.addObject("minValue", stockInfo2.getSmallDepositFeeValue());
            }
            mav.addObject("stockinfoId", id);
            // ==============================TOKEN end====================================
            mav.addObject("label", true);
        }
        return mav;
    }

    /**
     * erc20 充值查询
     * @param blockTransConfirmERC20
     * @param pagin
     * @return
     * @throws BusinessException
     * @author zhangchunxi 2018-03-28
     */
    @ResponseBody
    @RequestMapping(value = "/charge/chargeERC20List", method = RequestMethod.GET)
    @ApiOperation(value = "Fund ERC20 TOKEN 充值查询", httpMethod = "GET")
    public JsonMessage chargeERC20List(@ModelAttribute BlockTransConfirmERC20 blockTransConfirmERC20,String isCapital, @ModelAttribute Pagination pagin, String contractAddr) throws BusinessException
    {
        System.out.println(isCapital);
        if(isCapital == null)isCapital = "no";
        // 证券信息ID判断
        if(StringUtils.isNotBlank(contractAddr))
        {
            if(StringUtils.equalsIgnoreCase("eth",contractAddr))
            {
                contractAddr = FundConsts.WALLET_VINPAIR_CONTRACTADDR;
                isCapital = "yes";
            }
            StockInfo stockInfo = getStockInfo(contractAddr);
            blockTransConfirmERC20.setStockinfoId(StringUtils.equalsIgnoreCase(isCapital,"yes")?stockInfo.getCapitalStockinfoId():stockInfo.getTradeStockinfoId());
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        blockTransConfirmERC20.setAccountId(principal.getId());
        blockTransConfirmERC20.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
        PaginateResult<BlockTransConfirmERC20> result = blockTransConfirmERC20Service.findConfirmERC20ChargeList(pagin, blockTransConfirmERC20);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    public StockInfo getStockInfo(String addr)
    {
        StockInfo stockInfo = stockInfoService.findByContractAddr(addr);
        if(!StringUtils.equalsIgnoreCase(stockInfo.getIsActive(),FundConsts.PUBLIC_STATUS_YES))
        {
            throw new BusinessException("pair not open");
        }
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
