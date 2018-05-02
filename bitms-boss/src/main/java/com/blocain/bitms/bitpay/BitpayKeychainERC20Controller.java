/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay;

import java.sql.Timestamp;

import com.blocain.bitms.tools.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.bitpay.service.BitpayKeychainERC20Service;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ValidateUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * BitpayKeychainERC20 控制器
 * <p>File：BitpayKeychainERC20Controller.java </p>
 * <p>Title: BitpayKeychainERC20Controller </p>
 * <p>Description:BitpayKeychainERC20Controller </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.BITPAY)
@Api(description = "BitpayKeychainERC20")
public class BitpayKeychainERC20Controller extends GenericController
{
    @Autowired(required = false)
    private BitpayKeychainERC20Service bitpayKeychainERC20Service;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/bitpayKeychainErc20")
    @RequiresPermissions("trade:setting:keychainerc20:index")
    public String list() throws BusinessException
    {
        return "/bitpay/bitpayKeychainERC20/list";
    }
    
    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/bitpayKeychainErc20/modify")
    @RequiresPermissions("trade:setting:keychainerc20:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("bitpay/bitpayKeychainERC20/modify");
        BitpayKeychainERC20 keychain = new BitpayKeychainERC20();
        if (null != id) keychain = bitpayKeychainERC20Service.selectByPrimaryKey(id);
        mav.addObject("keychainERC20", keychain);
        return mav;
    }
    
    /**
     * 操作BitpayKeychainERC20
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/bitpayKeychainErc20/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存BitpayKeychainERC20", httpMethod = "POST")
    public JsonMessage save(@ModelAttribute BitpayKeychainERC20 entity) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        entity.setCreateDate(new Timestamp(System.currentTimeMillis()));
        if (beanValidator(json, entity))
        {
            if ((entity.getWalletType() == 1 || entity.getWalletType() == 3) && StringUtils.isEmpty(entity.getWalletPwd()))
            {
                // 热钱包,eth归集费用钱包密码不可为空,冷钱包密码可为空
                throw new BusinessException("该类型钱包密码不可为空");
            }
            if (null == entity.getWalletType() || !ValidateUtils.isInRange(entity.getWalletType(), 1, 3)) { throw new BusinessException("钱包类型错误"); }
            BitpayKeychainERC20 oldKeychain = bitpayKeychainERC20Service.selectByPrimaryKey(entity.getId());
            // 判断是添加模式还是编辑模式
            if (entity.getId() != null)
            {
                // 编辑模式
                if (null == oldKeychain) { throw new BusinessException("修改的钱包参数记录不存在"); }
            }
            if (StringUtils.isNotEmpty(entity.getWalletPwd()))
            {
                entity.setWalletPwd(EncryptUtils.desEncrypt(entity.getWalletPwd()));
            }
            bitpayKeychainERC20Service.save(entity);
        }
        return json;
    }
    
    /**
     * 查询BitpayKeychainERC20
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/bitpayKeychainErc20/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:keychainerc20:data")
    @ApiOperation(value = "查询BitpayKeychainERC20", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute BitpayKeychainERC20 entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<BitpayKeychainERC20> result = bitpayKeychainERC20Service.findJoinList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/bitpayKeychainErc20/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组", paramType = "form")
    public JsonMessage del(String ids) throws BusinessException
    {
        bitpayKeychainERC20Service.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
