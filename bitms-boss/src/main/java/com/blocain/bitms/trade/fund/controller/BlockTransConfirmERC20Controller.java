/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.trade.fund.entity.BlockTransConfirmERC20;
import com.blocain.bitms.trade.fund.service.BlockTransConfirmERC20Service;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirm;
import com.blocain.bitms.trade.fund.service.BlockTransConfirmService;

/**
 * ERC20区块交易确认表
 * <p>File：BlockTransConfirmERC20Controller.java</p>
 * <p>Title: BlockTransConfirmERC20Controller</p>
 * <p>Description:BlockTransConfirmERC20Controller</p>
 * <p>Copyright: Copyright (c) 2018年3月3日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class BlockTransConfirmERC20Controller extends GenericController
{
    @Autowired(required = false)
    private BlockTransConfirmERC20Service blockTransConfirmERC20Service;
    
    /**
     * 列表页面导航-充值
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/blocktranserc20/chargeList")
    @RequiresPermissions("trade:setting:blocktransconfirmerc20:index")
    public String chargeList() throws BusinessException
    {
        return "trade/fund/blocktransERC20/chargeList";
    }
    
    /**
     * 列表页面导航-提现
     * @return
     * @throws BusinessException
     */
    // @RequestMapping(value = "/blocktrans/withdrawList")
    // @RequiresPermissions("trade:setting:blocktransconfirm:index")
    // public String raiseList() throws BusinessException
    // {
    // return "trade/fund/blocktrans/withdrawList";
    // }
    /**
     * 区块交易确认表-充值查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/blocktranserc20/chargeData", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:blocktransconfirmerc20:data")
    public JsonMessage chargeData(BlockTransConfirmERC20 entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<BlockTransConfirmERC20> result = blockTransConfirmERC20Service.findConfirmERC20ChargeList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    /**
     * 区块交易确认表-提现查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    // @ResponseBody
    // @RequestMapping(value = "/blocktrans/withdrawData", method = RequestMethod.POST)
    // @RequiresPermissions("trade:setting:blocktransconfirm:data")
    // public JsonMessage raiseData(BlockTransConfirm entity, Pagination pagin) throws BusinessException
    // {
    // PaginateResult<BlockTransConfirm> result = blockTransConfirmService.findConfirmRaiseList(pagin, entity);
    // return getJsonMessage(CommonEnums.SUCCESS, result);
    // }
}
