/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.monitor.entity.MonitorNegativeAsset;
import com.blocain.bitms.monitor.service.MonitorNegativeAssetService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * 账户负资产监控表 控制器
 * <p>File：MonitorNegativeAssetController.java </p>
 * <p>Title: MonitorNegativeAssetController </p>
 * <p>Description:MonitorNegativeAssetController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "账户负资产监控表")
public class MonitorNegativeAssetController extends GenericController
{
    @Autowired(required = false)
    private MonitorNegativeAssetService monitorNegativeAssetService;
    
    /**
     * 列表页面导航-BMS交易统计
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/monitornegativeasset")
    @RequiresPermissions("monitor:setting:monitornegativeasset:index")
    public ModelAndView list(String monitorDate,String monitorType,String monitorSubType,String monitorResult) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/monitor/monitornegativeasset_list");
        mav.addObject("chkDate",monitorDate);
        mav.addObject("monitorType",monitorType);
        mav.addObject("monitorSubType",monitorSubType);
        mav.addObject("chkResult",monitorResult);
        
        return mav;
    }
    
    /**
     * 查询 BMS交易统计
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitornegativeasset/data", method = RequestMethod.POST)
    public JsonMessage data(MonitorNegativeAsset entity, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd);
        }
        PaginateResult<MonitorNegativeAsset> result = monitorNegativeAssetService.findMonitorNegativeAssetList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
