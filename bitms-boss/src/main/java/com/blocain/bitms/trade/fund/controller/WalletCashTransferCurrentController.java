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
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.WalletCashTransferCurrent;
import com.blocain.bitms.trade.fund.service.WalletCashTransferCurrentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包转账流水 控制器
 * <p>File：WalletCashTransferCurrentController.java </p>
 * <p>Title: WalletCashTransferCurrentController </p>
 * <p>Description:WalletCashTransferCurrentController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "钱包转账流水")
public class WalletCashTransferCurrentController extends GenericController
{
    @Autowired(required = false)
    private WalletCashTransferCurrentService walletCashTransferCurrentService;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/walletCashTransferCurrent")
    @RequiresPermissions("trade:setting:walletCashTransferCurrent:index")
    public String list() throws BusinessException
    {
        return "trade/fund/walletCashTransferCurrent/list";
    }

    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/walletCashTransferCurrent/modify")
    @RequiresPermissions("trade:setting:walletCashTransferCurrent:operator")
    public ModelAndView modify(Long id, Long parentId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/walletCashTransferCurrent/modify");
        WalletCashTransferCurrent entity = new WalletCashTransferCurrent();
        if (null != id) entity = walletCashTransferCurrentService.selectByPrimaryKey(id);
        mav.addObject("entity", entity);
        return mav;
    }

    /**
     * 保存
     * @param walletCashTransferCurrent
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/walletCashTransferCurrent/save")
    @RequiresPermissions("trade:setting:walletCashTransferCurrent:operator")
    @ApiOperation(value = "保存钱包参数", httpMethod = "POST")
    public JsonMessage save(WalletCashTransferCurrent walletCashTransferCurrent,String currentDateStr) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);

        BigDecimal orgAmt = BigDecimal.ZERO;
        WalletCashTransferCurrent lastEntity = walletCashTransferCurrentService.getLastEntity();
        if(lastEntity!=null)
        {
            orgAmt = lastEntity.getLastAmt();
        }
        walletCashTransferCurrent.setId(null);
        walletCashTransferCurrent.setOrgAmt(orgAmt);
        if(walletCashTransferCurrent.getOccurAmt().compareTo(BigDecimal.ZERO)<0)
        {
            throw new BusinessException("发生额错误");
        }
        if(StringUtils.equalsIgnoreCase(walletCashTransferCurrent.getOccurDirect(),FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE))
        {
            walletCashTransferCurrent.setLastAmt(orgAmt.add(walletCashTransferCurrent.getOccurAmt()));
        }else if(StringUtils.equalsIgnoreCase(walletCashTransferCurrent.getOccurDirect(),FundConsts.ACCOUNT_ASSET_DIRECT_DECREASE))
        {
            walletCashTransferCurrent.setLastAmt(orgAmt.subtract(walletCashTransferCurrent.getOccurAmt()));
        }else
        {
            throw new BusinessException("参数校验错误");
        }
        if(walletCashTransferCurrent.getLastAmt().compareTo(BigDecimal.ZERO)<0)
        {
            throw new BusinessException("最新错误");
        }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        walletCashTransferCurrent.setFee(BigDecimal.ZERO);
        walletCashTransferCurrent.setCreateBy(principal.getId());
        walletCashTransferCurrent.setCreateDate(new Date());
        walletCashTransferCurrent.setCurrentDate(DateUtils.parseDate(currentDateStr));
        if (beanValidator(json, walletCashTransferCurrent))
        {
            walletCashTransferCurrentService.save(walletCashTransferCurrent);
        }
        return json;
    }

    /**
     * 查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/walletCashTransferCurrent/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询钱包参数", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("trade:setting:walletCashTransferCurrent:data")
    public JsonMessage data(@ModelAttribute WalletCashTransferCurrent entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<WalletCashTransferCurrent> result = walletCashTransferCurrentService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

}
