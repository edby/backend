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

import com.blocain.bitms.monitor.entity.MonitorAcctFundCurrent;
import com.blocain.bitms.monitor.service.MonitorAcctFundCurrentService;
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
 * 资金流水监控表 控制器
 * <p>File：MonitorAcctFundCurrentController.java </p>
 * <p>Title: MonitorAcctFundCurrentController </p>
 * <p>Description:MonitorAcctFundCurrentController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "资金流水监控表")
public class MonitorAcctFundCurrentController extends GenericController
{
    @Autowired(required = false)
    private MonitorAcctFundCurrentService monitorAcctFundCurrentService;
    
    /**
     * 列表页面导航-综合查询[资金流水]
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/monitorcur")
    @RequiresPermissions("monitor:setting:monitorcur:index")
    public ModelAndView currList(String monitorDate,String monitorType,String monitorSubType,String monitorResult) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/monitor/monitorcur_list");
        mav.addObject("chkDate",monitorDate);
        mav.addObject("monitorType",monitorType);
        mav.addObject("monitorSubType",monitorSubType);
        mav.addObject("chkResult",monitorResult);
        return mav;
    }
    
    /**
     * 查询账户流水表-查询
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitorcur/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountfundcurrent:data")
    public JsonMessage data(MonitorAcctFundCurrent entity, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(timeEnd);
        }
        PaginateResult<MonitorAcctFundCurrent> result = monitorAcctFundCurrentService.findMonitorAcctFundCurrentList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
