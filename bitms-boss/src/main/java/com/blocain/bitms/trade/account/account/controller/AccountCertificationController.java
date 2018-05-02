/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.account.controller;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.Region;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.RegionService;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 账户实名查询和审核
 * <p>File：AccountCertificationController.java</p>
 * <p>Title: AccountCertificationController</p>
 * <p>Description:AccountCertificationController</p>
 * <p>Copyright: Copyright (c) 2017年9月26日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
public class AccountCertificationController extends GenericController
{
    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;
    
    @Autowired(required = false)
    private AccountService              accountService;

    @Autowired(required = false)
    private RegionService               regionService;

    @Autowired(required = false)
    private SysParameterService         sysParameterService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/cert")
    @RequiresPermissions("trade:setting:cert:index")
    public String list() throws BusinessException
    {
        return "trade/account/cert/list";
    }
    
    /**
     * 账户-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/cert/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:cert:data")
    public JsonMessage data(AccountCertification entity, String status, Pagination pagin) throws BusinessException
    {
        if (StringUtils.isNotBlank(status))
        {
            entity.setStatus(Short.valueOf(status));
        }
        PaginateResult<AccountCertification> result = accountCertificationService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 审核列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/cert/approval")
    @RequiresPermissions("trade:setting:cert:operator")
    public ModelAndView approval(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/account/cert/approval");
        AccountCertification certification = accountCertificationService.selectByPrimaryKey(id);
        Account account = accountService.selectByPrimaryKey(certification.getAccountId());
        Region region = new Region();
        region.setLCode(certification.getRegionId().toString());
        List<Region> list = regionService.findList(region);
        String baseUrl = "http://bitms-release.s3.amazonaws.com";
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.OSS_FILE_BASE_URL);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter != null) { baseUrl = sysParameter.getValue(); }
        if(list.size()>0)
        {
            mav.addObject("guojia", list.get(0).getCnName());
        }
        else{
            mav.addObject("guojia", "未知");
        }
        if(StringUtils.isNotBlank(certification.getAttachments()))
        {
            mav.addObject("files", JSONObject.parse(certification.getAttachments()));
        }
        else
        {
            mav.addObject("files",null);
        }
        mav.addObject("baseUrl", baseUrl);
        mav.addObject("certification", certification);
        mav.addObject("account", account);
        return mav;
    }

    /**
     * 审核详情页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/cert/details")
    @RequiresPermissions("trade:setting:cert:operator")
    public ModelAndView details(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/account/cert/details");
        AccountCertification certification = accountCertificationService.selectByPrimaryKey(id);
        Account account = accountService.selectByPrimaryKey(certification.getAccountId());
        Region region = new Region();
        region.setLCode(certification.getRegionId().toString());
        List<Region> list = regionService.findList(region);
        String baseUrl = "http://bitms-release.s3.amazonaws.com";
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.OSS_FILE_BASE_URL);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter != null) { baseUrl = sysParameter.getValue(); }
        if(list.size()>0)
        {
            mav.addObject("guojia", list.get(0).getCnName());
        }
        else{
            mav.addObject("guojia", "未知");
        }
        if(StringUtils.isNotBlank(certification.getAttachments()))
        {
            mav.addObject("files", JSONObject.parse(certification.getAttachments()));
        }
        else
        {
            mav.addObject("files",null);
        }
        mav.addObject("baseUrl", baseUrl);
        mav.addObject("certification", certification);
        mav.addObject("account", account);
        return mav;
    }

    /**
     * 账户-审核
     * @param id
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/cert/approval/pass", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:cert:operator")
    public JsonMessage pass(Long id, Short status) throws BusinessException
    {
        AccountCertification account = accountCertificationService.selectByPrimaryKey(id);
        if (null == account)
        {
            logger.info("账户信息 数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        account.setStatus(status);
        accountCertificationService.updateByPrimaryKey(account);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
