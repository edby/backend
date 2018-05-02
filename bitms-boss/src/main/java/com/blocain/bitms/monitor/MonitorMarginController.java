/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import com.blocain.bitms.monitor.entity.MonitorMargin;
import com.blocain.bitms.monitor.service.MonitorMarginService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.service.ClosePositionService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 保证金监控表 控制器
 * <p>File：MonitorSetController.java </p>
 * <p>Title: MonitorSetController </p>
 * <p>Description:MonitorSetController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "保证金监控表")
public class MonitorMarginController extends GenericController
{
    @Autowired(required = false)
    private MonitorMarginService monitorMarginService;
    
    @Autowired(required = false)
    private ClosePositionService closePositionService;
    
    @Autowired(required = false)
    private StockInfoService     stockInfoService;
    
    /**
     * 列表页面导航-保证金监控
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/margin")
    @RequiresPermissions("monitor:setting:debitMargin:index")
    public String margin() throws BusinessException
    {
        return "monitor/margin/list";
    }
    
    /**
     * 列表页面导航-保证金监控审核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/margin/approval")
    @RequiresPermissions("monitor:setting:debitMargin:operator")
    public ModelAndView marginApproval(String ids, String targetStockinfoIds,String capitalStockinfoIds) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/margin/approval");
        mav.addObject("ids", ids);
        mav.addObject("targetStockinfoIds", targetStockinfoIds);
        mav.addObject("capitalStockinfoIds", capitalStockinfoIds);
        mav.addObject("list", monitorMarginService.findListByIds(ids, targetStockinfoIds,capitalStockinfoIds));
        return mav;
    }
    
    /**
     * 账户借贷记录表-保证金监控查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/margin/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:debitMargin:data")
    public JsonMessage marginData(MonitorMargin entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorMargin> result = monitorMarginService.findMarginList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 强制平仓
     * @param ids
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/margin/approval/confirm", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:debitMargin:operator")
    public JsonMessage doClosePositionSelect(String ids, String targetStockinfoIds,String capitalStockinfoIds) throws BusinessException
    {
        List<MonitorMargin> list = monitorMarginService.findListByIds(ids, targetStockinfoIds,capitalStockinfoIds);
        for (MonitorMargin monitorMargin : list)
        {
            if (monitorMargin.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_ID
                    && monitorMargin.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_FEE_ID
                    && monitorMargin.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_RESERVE_ALLOCATION_ID
                    && monitorMargin.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_LONG_ID
                    && monitorMargin.getAccountId().longValue() != FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_SHORT_ID)
            {
                // ids 可用关联STOCKINFOID
                try
                {
                    StockInfo stockInfo = new StockInfo();
                    stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
                    stockInfo.setId(monitorMargin.getStockinfoId());
                    List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
                    stockInfo = stockInfoList.get(0);
                    boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
                    Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
                    closePositionService.doClosePositionSelect(ids, exchangePairVCoin, monitorMargin.getStockinfoId(), monitorMargin);
                }
                catch (Exception e)
                {
                    logger.debug("处理异常：" + monitorMargin.toString());
                }
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
