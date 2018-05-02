/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.block.controller;

import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;
import com.blocain.bitms.trade.block.service.BlockInfoERC20Service;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


/**
 * BlockInfoERC20 控制器
 * <p>File：BlockInfoERC20Controller.java </p>
 * <p>Title: BlockInfoERC20Controller </p>
 * <p>Description:BlockInfoERC20Controller </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.BLOCK)
@Api(description = "BlockInfoERC20")
public class BlockInfoERC20Controller extends GenericController
{
    @Autowired(required = false)
    private BlockInfoERC20Service blockInfoERC20Service;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/blockinfoerc20")
    @RequiresPermissions("trade:setting:blockinfoerc20:index")
    public String list() throws BusinessException
    {
        return "trade/block/blockInfo_erc20/list";
    }

    /**
     * 操作BlockInfoERC20
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
//    @ResponseBody
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ApiOperation(value = "保存BlockInfoERC20", httpMethod = "POST")
//    public JsonMessage save(@ModelAttribute BlockInfoERC20 info) throws BusinessException
//    {
//        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
//        if (beanValidator(json, info))
//        {
//            blockInfoERC20Service.save(info);
//        }
//        return json;
//    }

    /**
     * 查询BlockInfoERC20
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "blockinfoerc20/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:blockinfoerc20:data")
    @ApiOperation(value = "查询BlockInfoERC20", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute BlockInfoERC20 entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<BlockInfoERC20> result = blockInfoERC20Service.search(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组",paramType = "form")
    public JsonMessage del(String ids) throws BusinessException
    {
        blockInfoERC20Service.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
