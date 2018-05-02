/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import java.sql.Timestamp;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.blocain.bitms.trade.fund.entity.SystemWalletERC20;
import com.blocain.bitms.trade.fund.service.SystemWalletERC20Service;

/**
 * 系统ERC20钱包表
 * <p>File：SystemWalletErc20Controller.java</p>
 * <p>Title: WalletErc20Controller</p>
 * <p>Description:WalletErc20Controller</p>
 * <p>Copyright: Copyright (c) 2018年3月1日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class SystemWalletERC20Controller extends GenericController
{
    @Autowired(required = false)
    private SystemWalletERC20Service systemWalletERC20Service;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/systemwalleterc20")
    @RequiresPermissions("trade:setting:walleterc20addr:index")
    public String list() throws BusinessException
    {
        return "trade/fund/systemwallet_erc20/list";
    }
    
    /**
     * 系统ERC20钱包表-查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/systemwalleterc20/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walleterc20addr:data")
    public JsonMessage walletERC20Data(SystemWalletERC20 entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<SystemWalletERC20> result = systemWalletERC20Service.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 保存 系统钱包
     * @param systemWallet
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/systemwalleterc20/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walleterc20addr:operator")
    public JsonMessage save(SystemWalletERC20 systemWallet) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        systemWallet.setCreateBy(principal.getId());
        systemWallet.setCreateDate(new Timestamp(System.currentTimeMillis()));
        if (beanValidator(json, systemWallet))
        {
            if (systemWallet.getId() != null)
            {
                SystemWalletERC20 systemWallet2 = systemWalletERC20Service.selectByPrimaryKey(systemWallet.getId());
                if (null == systemWallet2) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
                if (null != systemWallet2 && !systemWallet2.verifySignature())
                {// 校验数据
                    logger.info("ERC20钱包信息 数据校验失败");
                    throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
                }
            }
            systemWalletERC20Service.save(systemWallet);
            return json;
        }
        else
        {
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }
    
    /**
     * 新增和编辑 系统钱包
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/systemwalleterc20/modify")
    @RequiresPermissions("trade:setting:walleterc20addr:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/systemwallet_erc20/modify");
        SystemWalletERC20 systemWallet = new SystemWalletERC20();
        if (id != null)
        {
            systemWallet = systemWalletERC20Service.selectByPrimaryKey(id);
        }
        mav.addObject("systemWalletERC20", systemWallet);
        return mav;
    }
    
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/systemwalleterc20/del", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walleterc20addr:operator")
    public JsonMessage del(String ids) throws BusinessException
    {
        systemWalletERC20Service.doRemoveSysWalletERC20AndAddr(ids);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
