/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.fund.entity.WalletTransferCurrent;
import com.blocain.bitms.trade.fund.service.WalletTransferCurrentService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
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
import com.blocain.bitms.trade.fund.consts.FundConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 钱包转账流水 控制器
 * <p>File：BitpayKeychainController.java </p>
 * <p>Title: BitpayKeychainController </p>
 * <p>Description:BitpayKeychainController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.BITPAY)
@Api(description = "钱包转账流水")
public class WalletTransferCurrentController extends GenericController
{
    @Autowired(required = false)
    private WalletTransferCurrentService walletTransferCurrentService;


    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/walletTransferCurrent")
    @RequiresPermissions("trade:setting:walletTransferCurrent:index")
    public String list() throws BusinessException
    {
        return "/bitpay/walletTransferCurrent/list";
    }

    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/walletTransferCurrent/modify")
    @RequiresPermissions("trade:setting:walletTransferCurrent:operator")
    public ModelAndView modify(Long id, Long parentId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("bitpay/walletTransferCurrent/modify");
        WalletTransferCurrent entity = new WalletTransferCurrent();
        if (null != id) entity = walletTransferCurrentService.selectByPrimaryKey(id);
        mav.addObject("entity", entity);
        return mav;
    }



    /**
     * 保存
     * @param walletTransferCurrent
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/walletTransferCurrent/save")
    @RequiresPermissions("trade:setting:walletTransferCurrent:operator")
    @ApiOperation(value = "保存钱包参数", httpMethod = "POST")
    public JsonMessage save(WalletTransferCurrent walletTransferCurrent,String currentDateStr) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        walletTransferCurrent.setCreateBy(principal.getId());
        walletTransferCurrentService.doSaveWalletTransferCurrent(walletTransferCurrent,currentDateStr);
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
    @RequestMapping(value = "/walletTransferCurrent/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询钱包参数", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("trade:setting:walletTransferCurrent:data")
    public JsonMessage data(@ModelAttribute WalletTransferCurrent entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<WalletTransferCurrent> result = walletTransferCurrentService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

}
