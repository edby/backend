/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import oracle.sql.TIMESTAMP;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ERC20 TOKEN 控制器
 * <p>File：Erc20TokenController.java </p>
 * <p>Title: Erc20TokenController </p>
 * <p>Description:Erc20TokenController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.STOCK)
public class Erc20TokenController extends GenericController
{
    @Autowired(required = false)
    private Erc20TokenService erc20TokenService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/erc20Token")
    @RequiresPermissions("trade:setting:erc20Token:index")
    public String list() throws BusinessException
    {
        return "trade/stock/erc20Token/list";
    }
    
    /**
     * ERC20 TOKEN
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/erc20Token/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:erc20Token:operator")
    public JsonMessage save(Erc20Token info) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        info.setCreateDate(new Timestamp(System.currentTimeMillis()));
        info.setActiveEndDate(new Date());
        info.setContractAddr(info.getContractAddr().toLowerCase());
        info.setIsActive("no");
        info.setNeedAward(0);
        info.setAwardStatus(0);
        info.setBlockHeight(1L);
        erc20TokenService.save(info);
        return json;
    }
    
    /**
     * 添加或修改TOKEN
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/erc20Token/modify")
    @RequiresPermissions("trade:setting:erc20Token:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        Erc20Token erc20Token = new Erc20Token();
        if (id != null)
        {
            erc20Token = erc20TokenService.selectByPrimaryKey(id);
        }
        ModelAndView mav = new ModelAndView("trade/stock/erc20Token/modify");
        mav.addObject("erc20Token", erc20Token);
        return mav;
    }
    
    /**
     * 查询证券信息表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/erc20Token/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:erc20Token:data")
    public JsonMessage data(Erc20Token entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<Erc20Token> result = erc20TokenService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    // /**
    // * 根据指定ID删除
    // * @param id
    // * @return {@link JsonMessage}
    // * @throws BusinessException
    // */
    // @CSRFToken
    // @ResponseBody
    // @RequestMapping(value = "/erc20Token/del", method = RequestMethod.POST)
    // public JsonMessage del(Long id) throws BusinessException
    // {
    // Erc20Token erc20Token = erc20TokenService.selectByPrimaryKey(id);
    // if(StringUtils.equalsIgnoreCase(erc20Token.getIsActive(),FundConsts.PUBLIC_STATUS_YES))
    // {
    // throw new BusinessException("该交易已经激活不可删除！");
    // }
    //// erc20TokenService.remove(id);
    // return getJsonMessage(CommonEnums.SUCCESS);
    // }
}
