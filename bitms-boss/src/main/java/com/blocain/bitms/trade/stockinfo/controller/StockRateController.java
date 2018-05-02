/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.controller;

import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.common.service.DictionaryService;
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
import com.blocain.bitms.tools.utils.ExportExcel;
import com.blocain.bitms.trade.stockinfo.consts.StockConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证券费率表 控制器
 * <p>File：StockRateController.java </p>
 * <p>Title: StockRateController </p>
 * <p>Description:StockRateController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.STOCK)
public class StockRateController extends GenericController
{
    @Autowired(required = false)
    private StockRateService  stockRateService;
    
    @Autowired(required = false)
    private DictionaryService dictionaryService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/rate")
    @RequiresPermissions("trade:setting:stockrate:index")
    public String list() throws BusinessException
    {
        return "trade/stock/rate/list";
    }
    
    /**
     * 操作证券费率表
     * @param stockRate
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/rate/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:stockrate:operator")
    public JsonMessage save(StockRate stockRate) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == stockRate.getId())
        {
            stockRate.setCreateBy(principal.getId());
            stockRate.setCreateDate(new Timestamp(System.currentTimeMillis()));
        }
        stockRate.setUpdateBy(principal.getId());
        stockRate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        if (beanValidator(json, stockRate))
        {
            stockRateService.save(stockRate);
        }
        return json;
    }
    
    /**
     * 添加或修改证券费率
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/rate/modify")
    @RequiresPermissions("trade:setting:stockrate:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/stock/rate/modify");
        StockRate stockRate = new StockRate();
        if (id!=null)
        {
            stockRate = stockRateService.selectByPrimaryKey(id);
        }
        mav.addObject("stockRate", stockRate);
        return mav;
    }
    
    /**
     * 查询证券信息表
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/rate/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:stockrate:data")
    public JsonMessage data(StockRate entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<StockRate> result = stockRateService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/rate/del", method = RequestMethod.POST)
    public JsonMessage del(String ids) throws BusinessException
    {
        stockRateService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 导出所有证券
     * @return
     * @throws BusinessException
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping(value = "/rate/export", method = RequestMethod.GET)
    public void export(HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException
    {
        String lang = "zh_CN";
        Map<String, Object> map = new HashMap<String, Object>();
        List<Dictionary> dictList = dictionaryService.findByCode(lang, StockConsts.STOCKRATE_RATETYPE);
        for (Dictionary dict : dictList)
        {
            map.put(dict.getCode(), dict.getName());
        }
        ExportExcel excel = new ExportExcel("证券费率信息", StockRate.class);
        List<StockRate> list = stockRateService.selectAll();
        for (StockRate rate : list)
        {
            rate.setRateTypeName((map.get(rate.getRateType()) == null ? "" : map.get(rate.getRateType()).toString()));
        }
        excel.setDataList(list);
        excel.write(response, "证券费率信息.xls");
    }
}
