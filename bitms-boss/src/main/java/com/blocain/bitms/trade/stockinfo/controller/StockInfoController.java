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
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.consts.StockConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 证券信息表 控制器
 * <p>File：StockInfoController.java </p>
 * <p>Title: StockInfoController </p>
 * <p>Description:StockInfoController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.STOCK)
public class StockInfoController extends GenericController
{
    @Autowired(required = false)
    private StockInfoService  stockInfoService;
    
    @Autowired(required = false)
    private DictionaryService dictionaryService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/info")
    @RequiresPermissions("trade:setting:stockinfo:index")
    public String list() throws BusinessException
    {
        return "trade/stock/info/list";
    }
    
    /**
     * 操作证券信息表
     * @param info
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/info/save", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:stockinfo:operator")
    public JsonMessage save(StockInfo info, BigDecimal buyAmountPrecision, BigDecimal buyPricePrecision, BigDecimal sellAmountPrecision, BigDecimal sellPricePrecision)
            throws BusinessException
    {
        if (buyPricePrecision != null)
        {
            info.setBuyPricePrecision(buyPricePrecision.intValue());
        }
        if (buyAmountPrecision != null)
        {
            info.setBuyAmountPrecision(buyAmountPrecision.intValue());
        }
        if (sellAmountPrecision != null)
        {
            info.setSellAmountPrecision(sellAmountPrecision.intValue());
        }
        if (sellPricePrecision != null)
        {
            info.setSellPricePrecision(sellPricePrecision.intValue());
        }
        logger.debug(info.toString());
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        info.setCreateBy(principal.getId());
        info.setCreateDate(new Timestamp(System.currentTimeMillis()));
        info.setUpdateBy(principal.getId());
        info.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        stockInfoService.updateByPrimaryKeySelective(info);
        return json;
    }
    
    /**
     * 添加或修改证券信息
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/info/modify")
    @RequiresPermissions("trade:setting:stockinfo:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        StockInfo stockInfo = new StockInfo();
        if (id != null)
        {
            stockInfo = stockInfoService.selectByPrimaryKey(id);
        }
        ModelAndView mav = new ModelAndView("trade/stock/info/modify");
        mav.addObject("stockInfo", stockInfo);
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
    @RequestMapping(value = "/info/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:stockinfo:data")
    public JsonMessage data(StockInfo entity, Pagination pagin) throws BusinessException
    {
        entity.setIsActive("no");// 查询所有
        PaginateResult<StockInfo> result = stockInfoService.search(pagin, entity);
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
    @RequestMapping(value = "/info/del", method = RequestMethod.POST)
    public JsonMessage del(String ids) throws BusinessException
    {
        stockInfoService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 查询所有证券
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/info/all", method = RequestMethod.GET)
    public List<StockInfo> all() throws BusinessException
    {
        List<StockInfo> list = stockInfoService.selectAll();
        return list;
    }

    /**
     * 查询所有币对
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/info/realAll", method = RequestMethod.GET)
    public List<StockInfo> realAll() throws BusinessException
    {
        List<StockInfo> list = stockInfoService.findAll();
        return list;
    }


    /**
     * 查询所有交易对
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/info/allInExchange", method = RequestMethod.GET)
    public List<StockInfo> allInExchange() throws BusinessException
    {
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> list = stockInfoService.findList(stockInfoSelect);
        return list;
    }
    
    /**
     * 查询所有数字货币
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/info/allCoin", method = RequestMethod.GET)
    public List<StockInfo> allCoin() throws BusinessException
    {
        List<StockInfo> stockInfoList = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_DIGITALCOIN, FundConsts.STOCKTYPE_CASHCOIN, FundConsts.STOCKTYPE_ERC20_TOKEN);
        return stockInfoList;
    }
    
    /**
     * 查询所有可调整数字货币
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/info/allCanAdjustCoin", method = RequestMethod.GET)
    public List<StockInfo> allCanAdjustCoin() throws BusinessException
    {
        List<StockInfo> stockInfoList = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_DIGITALCOIN, FundConsts.STOCKTYPE_ERC20_TOKEN);
        return stockInfoList;
    }
    
    /**
     * 查询所有合约交易对
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/info/allContractExchange", method = RequestMethod.GET)
    public List<StockInfo> allContractExchange() throws BusinessException
    {
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
        return stockInfoList;
    }
    
    /**
     * 按类别查询
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/info/findByStockType", method = RequestMethod.GET)
    public List<StockInfo> findByStockType(String stockType) throws BusinessException
    {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setStockType(stockType);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        return list;
    }
    
    /**
     * 导出所有证券
     * @return
     * @throws BusinessException
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping(value = "/info/export", method = RequestMethod.GET)
    public void export(HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException
    {
        String lang = "zh_CN";
        Map<String, Object> stockType = new HashMap<String, Object>();
        List<Dictionary> dictList = dictionaryService.findByCode(lang, StockConsts.STOCKINFO_STOCKTYPE);
        for (Dictionary dict : dictList)
        {
            stockType.put(dict.getCode(), dict.getName());
        }
        Map<String, Object> yesOrNo = new HashMap<String, Object>();
        dictList = dictionaryService.findByCode(lang, StockConsts.STOCKINFO_YESORNO);
        for (Dictionary dict : dictList)
        {
            yesOrNo.put(dict.getCode(), dict.getName());
        }
        ExportExcel excel = new ExportExcel("证券信息", StockInfo.class);
        List<StockInfo> list = stockInfoService.selectAll();
        for (StockInfo info : list)
        {
            info.setStockTypeName((stockType.get(info.getStockType()) == null ? "" : stockType.get(info.getStockType()).toString()));
            info.setCanRecharge((yesOrNo.get(info.getCanRecharge()) == null ? "" : yesOrNo.get(info.getCanRecharge()).toString()));
            info.setCanTrade((yesOrNo.get(info.getCanTrade()) == null ? "" : yesOrNo.get(info.getCanTrade()).toString()));
            info.setCanWithdraw((yesOrNo.get(info.getCanWithdraw()) == null ? "" : yesOrNo.get(info.getCanWithdraw()).toString()));
        }
        excel.setDataList(list);
        excel.write(response, "证券信息.xls");
    }
}
