/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.monitor.service.MonitorConfigService;
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
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * MonitorConfig 控制器
 * <p>File：MonitorConfigController.java </p>
 * <p>Title: MonitorConfigController </p>
 * <p>Description:MonitorConfigController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "MonitorConfig")
public class MonitorConfigController extends GenericController
{
    @Autowired(required = false)
    private MonitorConfigService monitorConfigService;
    
    @Autowired(required = false)
    private StockInfoService     stockInfoService;
    // @Autowired(required = false)
    // private MonitorLimitParamService monitorLimitParamService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/config")
    @RequiresPermissions("monitor:setting:monitorconfig:index")
    public String list() throws BusinessException
    {
        return "monitor/config/list";
    }
    
    /**
     * 添加或修改监控配置
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/config/modify")
    @RequiresPermissions("monitor:setting:monitorconfig:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/config/modify");
        MonitorConfig monitorConfig = new MonitorConfig();
        if (id != null)
        {
            monitorConfig = monitorConfigService.selectByPrimaryKey(id);
        }
        mav.addObject("monitorConfig", monitorConfig);
        return mav;
    }
    
    /**
     * 查询MonitorConfig
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/config/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询MonitorConfig", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorConfig entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorConfig> result = monitorConfigService.findJoinList(pagin, entity);
        List<StockInfo> list = stockInfoService.findAll();
        Map<String, String> stockMap = new HashMap<>();
        for (StockInfo stock : list)
        {
            stockMap.put(String.valueOf(stock.getId()), stock.getStockName());
        }
        for (MonitorConfig config : result.getList())
        {
            String[] ids = config.getMonitorCategorys().split(",");
            StringBuilder idsSb = new StringBuilder("");
            for (int i = 0; i < ids.length; i++)
            {
                if (stockMap.containsKey(ids[i]))
                {
                    idsSb = idsSb.append(stockMap.get(ids[i])).append("、");
                }
                else
                {
                    idsSb = idsSb.append(ids[i]).append("、");
                }
            }
            String idsStr = "";
            if (idsSb.length() != 0)
            {
                idsStr = idsSb.substring(0, idsSb.lastIndexOf("、"));
            }
            config.setMonitorCategorys_str(idsStr);
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 添加或修改监控配置的保存
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/save", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorconfig:operator")
    public JsonMessage save(MonitorConfig monitorConfig) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        MonitorConfig mc = new MonitorConfig();
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if ((null == monitorConfig.getId()))
        {// 新增模式，需要判断参数代码是否已存在
            mc.setMonitorCode(monitorConfig.getMonitorCode());
            List<MonitorConfig> configList = monitorConfigService.findList(mc);
            // 保存前，检查是否存在同一参数代码
            if (!CollectionUtils.isEmpty(configList))
            {
                json.setCode(1000);
                json.setMessage("监控代码已存在!!!");
                return json;
            }
        }
        monitorConfig.setCreateBy(String.valueOf(principal.getId()));
        monitorConfig.setCreateDate(new Timestamp(System.currentTimeMillis()));
        if (beanValidator(json, monitorConfig))
        {
            monitorConfigService.save(monitorConfig);
        }
        return json;
    }
    
    /**
     * 根据指定ID启用
     * @param id
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/active", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorconfig:operator")
    public JsonMessage active(String id) throws BusinessException
    {
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setActive(true);
        monitorConfig.setId(Long.parseLong(id));
        monitorConfigService.updateByPrimaryKeySelective(monitorConfig);
        // 更新成功后缓存
        // monitorConfigService.cacheSingleConfigInfo(monitorConfig);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 根据指定ID停用
     * @param id
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/unActive", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorconfig:operator")
    public JsonMessage unActive(String id) throws BusinessException
    {
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setActive(false);
        monitorConfig.setId(Long.parseLong(id));
        monitorConfigService.updateByPrimaryKeySelective(monitorConfig);
        // 更新成功后缓存
        // monitorConfigService.cacheSingleConfigInfo(monitorConfig);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
