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
import com.blocain.bitms.trade.fund.entity.SystemWallet;
import com.blocain.bitms.trade.fund.service.SystemWalletService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;

/**
 * 系统钱包表
 * <p>File：SystemWalletAddrController.java</p>
 * <p>Title: WalletAddrController</p>
 * <p>Description:WalletAddrController</p>
 * <p>Copyright: Copyright (c) 2017年7月12日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class SystemWalletController extends GenericController
{
    @Autowired(required = false)
    private SystemWalletService systemWalletService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/systemwallet")
    @RequiresPermissions("trade:setting:walletaddr:index")
    public String list() throws BusinessException
    {
        return "trade/fund/systemwallet/list";
    }
    
    /**
     * 系统钱包表-查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/systemwallet/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletaddr:data")
    public JsonMessage walletData(SystemWallet entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<SystemWallet> result = systemWalletService.search(pagin, entity);
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
    @RequestMapping(value = "/systemwallet/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletaddr:operator")
    public JsonMessage save(SystemWallet systemWallet) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == systemWallet.getId())
        {
            systemWallet.setCreateBy(principal.getId());
            systemWallet.setCreateDate(new Timestamp(System.currentTimeMillis()));
        }
        if (beanValidator(json, systemWallet))
        {
            if (systemWallet.getId()!=null)
            {
                SystemWallet systemWallet2 = systemWalletService.selectByPrimaryKey(systemWallet.getId());
                if (null == systemWallet2) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
                if (null != systemWallet2 && !systemWallet2.verifySignature())
                {// 校验数据
                    logger.info("钱包信息 数据校验失败");
                    throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
                }
            }
            systemWalletService.save(systemWallet);
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
    @RequestMapping(value = "/systemwallet/modify")
    @RequiresPermissions("trade:setting:walletaddr:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/systemwallet/modify");
        SystemWallet systemWallet = new SystemWallet();
        if (id!=null)
        {
            systemWallet = systemWalletService.selectByPrimaryKey(id);
        }
        mav.addObject("systemWallet", systemWallet);
        return mav;
    }
    
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/systemwallet/del", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletaddr:operator")
    public JsonMessage del(String ids) throws BusinessException
    {
        systemWalletService.doRemoveSysWalletAndAddr(ids);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
