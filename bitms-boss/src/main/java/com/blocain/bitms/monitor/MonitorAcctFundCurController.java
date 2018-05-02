/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import com.blocain.bitms.monitor.entity.MonitorAcctFundCur;
import com.blocain.bitms.monitor.entity.MonitorDetail;
import com.blocain.bitms.monitor.entity.MonitorResult;
import com.blocain.bitms.monitor.service.MonitorAcctFundCurService;
import com.blocain.bitms.monitor.service.MonitorEngineService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.LoggerUtils;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 账户资金流水监控表 控制器
 * <p>File：MonitorAcctFundCurController.java </p>
 * <p>Title: MonitorAcctFundCurController </p>
 * <p>Description:MonitorAcctFundCurController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "账户资金流水监控表")
public class MonitorAcctFundCurController extends GenericController
{
    @Autowired(required = false)
    private MonitorAcctFundCurService monitorAcctFundCurService;
    @Autowired(required = false)
    MonitorEngineService monitorEngineService;
    /**
     * 列表页面导航-账户资金流水监控
     *
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/acctfundcur")
    @RequiresPermissions("monitor:setting:acctfundcur:index")
    public ModelAndView list(String monitorDate, String monitorType, String monitorSubType, String monitorResult) throws BusinessException {

        ModelAndView mav = new ModelAndView("monitor/monitor/monitoracctfundcur_list");
        mav.addObject("chkDate", monitorDate);
        mav.addObject("monitorType", monitorType);
        mav.addObject("monitorSubType", monitorSubType);
        mav.addObject("chkResult", monitorResult);
        return mav;
    }

    /**
     * 对账结果框
     */
    @RequestMapping(value = "/monitoracctfundcur/assetchk")
    public ModelAndView assetchk() throws BusinessException {
        ModelAndView mav = new ModelAndView("monitor/monitor/assetchk");
        return mav;
    }

    /**
     * 查询账户资金流水监控表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitoracctfundcur/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:acctfundcur:data")
    public JsonMessage data(@ModelAttribute MonitorAcctFundCur entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorAcctFundCur> result = monitorAcctFundCurService.findAcctFundCurList(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 查询总账关联资金流水监控表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitoracctfundcur/relatedData", method = RequestMethod.POST)
    public JsonMessage relatedData(@ModelAttribute MonitorAcctFundCur entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorAcctFundCur> result = monitorAcctFundCurService.findRelatedList(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 对账
     */
    @ResponseBody
    @RequestMapping(value = "/monitoracctfundcur/checkData", method = RequestMethod.POST)
    public JsonMessage checkData(@RequestBody MonitorDetail entity)
            throws BusinessException {
//        LoggerUtils.logInfo(logger,entity.toString());
        //此处调用对账接口
        MonitorResult res = monitorEngineService.dealAccountFundCurChk(
                String.valueOf(entity.getRelatedStockinfoId()),
                String.valueOf(entity.getAccountId()),
                String.valueOf(entity.getStockinfoId()));
        return getJsonMessage(CommonEnums.SUCCESS,res);
    }


}
