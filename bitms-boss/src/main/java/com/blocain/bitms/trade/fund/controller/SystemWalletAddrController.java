/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddr;
import com.blocain.bitms.trade.fund.service.SystemWalletAddrService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 钱包地址表
 * <p>File：SystemWalletAddrController.java</p>
 * <p>Title: WalletAddrController</p>
 * <p>Description:WalletAddrController</p>
 * <p>Copyright: Copyright (c) 2017年7月5日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class SystemWalletAddrController extends GenericController
{
    @Autowired(required = false)
    private SystemWalletAddrService walletAddrService;
    
    /**
     * 钱包地址表-查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/systemwallet/addr/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletaddr:data")
    public JsonMessage walletData(SystemWalletAddr entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<SystemWalletAddr> result = walletAddrService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 保存 系统钱包地址
     * @param systemWalletAddr
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws BusinessException
     */
    /* @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/systemwallet/addr/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletaddr:operator")
    public JsonMessage save(SystemWalletAddr systemWalletAddr) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null== systemWalletAddr.getId())
        {
            systemWalletAddr.setCreateBy(principal.getId());
            systemWalletAddr.setCreateDate(new Timestamp(System.currentTimeMillis()));
        }
        if (beanValidator(json, systemWalletAddr))
        {
            if (systemWalletAddr.getId()!=null)
            {
                SystemWalletAddr systemWalletAddr2 = walletAddrService.selectByPrimaryKey(systemWalletAddr.getId());
                if (null == systemWalletAddr2) { throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR); }
                if (null != systemWalletAddr2 && !systemWalletAddr2.verifySignature())
                {// 校验数据
                    logger.info("钱包地址 数据校验失败");
                    throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
                }
            }
            walletAddrService.save(systemWalletAddr);
        }
        return json;
    }*/
    
    /**
     * 新增和编辑 系统钱包
     * @param id
     * @return {@link ModelAndView}
     * @throws BusinessException
     */
    /* @RequestMapping(value = "/systemwallet/addr/modify")
    @RequiresPermissions("trade:setting:walletaddr:operator")
    public ModelAndView modify(Long id, String walletId, Long stockinfoId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/systemwallet/addr/modify");
        SystemWalletAddr systemWallet = new SystemWalletAddr();
        systemWallet.setWalletId(walletId);
        systemWallet.setStockinfoId(stockinfoId);
        if (id!=null)
        {
            systemWallet = walletAddrService.selectByPrimaryKey(id);
        }
        mav.addObject("systemWalletAddr", systemWallet);
        return mav;
    }*/

    /**
     * 删除钱包
     * @param ids
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws BusinessException
     */
    /* @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/systemwallet/addr/del", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:walletaddr:operator")
    public JsonMessage del(String ids) throws BusinessException
    {
        walletAddrService.removeBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }*/
}
