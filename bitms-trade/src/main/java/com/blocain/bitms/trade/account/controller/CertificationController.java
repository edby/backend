/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.controller;

import com.blocain.bitms.boss.common.entity.Region;
import com.blocain.bitms.boss.common.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.AmazonS3Utils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.service.AccountCertificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * 用户实名认证信息 控制器
 * <p>File：CertificationController.java </p>
 * <p>Title: CertificationController </p>
 * <p>Description:CertificationController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
@Api(description = "用户实名认证")
public class CertificationController extends GenericController
{
    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;

    @Autowired(required = false)
    private RegionService               regionService;
    /**
     * 实名认证页面导航
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/certification")
    @ApiOperation(value = "实名认证页面导航", httpMethod = "GET")
    public ModelAndView index() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        ModelAndView mav = new ModelAndView();
        AccountCertification accountCertification = accountCertificationService.findByAccountId(principal.getId());
        if (null != accountCertification && accountCertification.getStatus() == Short.valueOf("1"))
        {
            Region region = new Region();
            region.setLCode(accountCertification.getRegionId().toString());
            List<Region> list = regionService.findList(region);
            mav.addObject("accountCertification", accountCertification);
            mav.addObject("country", list.get(0).getEnName());
            mav.setViewName("account/certificationDone");
        }
        else
        {
            mav.setViewName("account/certificationUndone");
            mav.addObject("accountCertification", accountCertification);
        }
        return mav;
    }
    
    /**
     * 实名认证上传资料页面导航
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping("/certificationDoing")
    @ApiOperation(value = "实名认证上传资料页面导航", httpMethod = "GET")
    public ModelAndView certificationDoing() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        AccountCertification accountCertification = accountCertificationService.findByAccountId(principal.getId());
        ModelAndView mav = new ModelAndView();
        if(null != accountCertification){
            if (null != accountCertification && accountCertification.getStatus() == Short.valueOf("1"))
            {
                Region region = new Region();
                region.setLCode(accountCertification.getRegionId().toString());
                List<Region> list = regionService.findList(region);
                mav.addObject("accountCertification", accountCertification);
                mav.addObject("country", list.get(0).getEnName());
                mav.setViewName("account/certificationDone");
            } else  if (null != accountCertification && accountCertification.getStatus() == Short.valueOf("0")){
                mav.addObject("accountCertification", accountCertification);
                mav.setViewName("account/certificationUndone");
            } else {
                // 实名验证被拒绝可以再跳转到认证页面
                mav.setViewName("account/certificationDoing");
            }
        }
        else
        {
            mav.setViewName("account/certificationDoing");
        }

        return mav;
    }
    
    /**
     * 操作用户实名认证信息
     * @param accountCertification
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/certification/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存用户实名认证信息", httpMethod = "POST")
    public JsonMessage save(@ModelAttribute AccountCertification accountCertification) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        AccountCertification tempCertification = accountCertificationService.findByAccountId(principal.getId());
        if (null != tempCertification && (tempCertification.getStatus() == Short.valueOf("0") || tempCertification.getStatus() == Short.valueOf("1")))
        {// 当实名认证已提交但未拒绝时不可再次提交认证信息
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        accountCertification.setAccountId(principal.getId());
        accountCertification.setStatus(Short.valueOf("0"));
        if (null != accountCertification.getAttachment())
        {// 转换文件存储空间
            AccountCertification.Attachment attachment = accountCertification.getAttachment();
            if (StringUtils.isNotBlank(attachment.getCover()))
            {
                AmazonS3Utils.moveObject(attachment.getCover());
            }
            if (StringUtils.isNotBlank(attachment.getFrontage()))
            {
                AmazonS3Utils.moveObject(attachment.getFrontage());
            }
            if (StringUtils.isNotBlank(attachment.getOpposite()))
            {
                AmazonS3Utils.moveObject(attachment.getOpposite());
            }
        }
        accountCertificationService.save(accountCertification);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
