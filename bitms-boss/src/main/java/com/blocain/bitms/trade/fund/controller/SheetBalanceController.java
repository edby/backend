/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.tools.utils.ExportExcel;
import com.blocain.bitms.trade.fund.entity.SheetBalance;
import com.blocain.bitms.trade.fund.service.SheetBalanceService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 报表资产负债表
 * <p>File：SheetBalanceController.java</p>
 * <p>Title: SheetBalanceController</p>
 * <p>Description:SheetBalanceController</p>
 * <p>Copyright: Copyright (c) 2017年10月24日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class SheetBalanceController extends GenericController
{
    @Autowired(required = false)
    private SheetBalanceService sheetBalanceService;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/sheetBalance")
    @RequiresPermissions("trade:setting:sheetBalance:index")
    public String list() throws BusinessException
    {
        return "trade/fund/sheetBalance/list";
    }
    
    /**
     * 报表资产负债表-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/sheetBalance/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:sheetBalance:data")
    public JsonMessage data(SheetBalance entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<SheetBalance> result = sheetBalanceService.search(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 平台营收统计
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/sheetBalanceAdmin")
    @RequiresPermissions("trade:setting:sheetBalance:index")
    public String adminList() throws BusinessException
    {
        return "trade/fund/sheetBalance/adminList";
    }

    /**
     * 报表资产负债表-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/sheetBalanceAdmin/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:sheetBalanceAdmin:data")
    public JsonMessage adminData(SheetBalance entity, Pagination pagin) throws BusinessException
    {
        List<SheetBalance> list = sheetBalanceService.selectAllAdmin(entity);
        pagin.setTotalPage(1);
        pagin.setTotalRows(list.size()+0L);
        pagin.setRows(list.size());
        pagin.setPage(1);
        PaginateResult<SheetBalance> result = new PaginateResult<>();
        result.setPage(pagin);
        result.setList(list);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 导出
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/sheetBalanceAdmin/export", method = RequestMethod.GET)
    public void export(SheetBalance entity,HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException
    {
        ExportExcel excel = new ExportExcel("平台营收统计", SheetBalance.class);
        List<SheetBalance> list = sheetBalanceService.selectAllAdmin(entity);
        excel.setDataList(list);
        excel.write(response, "平台营收统计.xls");
    }
}
