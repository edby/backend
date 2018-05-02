/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import java.sql.Timestamp;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.monitor.entity.MonitorIndex;
import com.blocain.bitms.monitor.service.MonitorIndexService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 监控指标表 控制器
 * <p>File：MonitorIndexController.java </p>
 * <p>Title: MonitorIndexController </p>
 * <p>Description:MonitorIndexController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "监控指标表")
public class MonitorIndexController extends GenericController
{
    @Autowired(required = false)
    private MonitorIndexService monitorIndexService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/monitorIndex")
    @RequiresPermissions("monitor:setting:monitorindex:index")
    public String list() throws BusinessException
    {
        return "monitor/config/index/indexList";
    }
    
    /**
     * 操作监控指标表
     * @param info
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitorIndex/save", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorindex:operator")
    @ApiOperation(value = "保存监控指标表", httpMethod = "POST")
    public JsonMessage save(@ModelAttribute MonitorIndex info) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        info.setCreateBy(String.valueOf(principal.getId()));
        info.setCreateDate(new Timestamp(System.currentTimeMillis()));
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        if (beanValidator(json, info))
        {
            // 如果处理方式为无操作或前端传来的参数值为null,则入库时给参数值赋值空字符串。
            if (info.getActionValue()== null || "doNothing".equals(info.getActionType()))
            {
                info.setActionValue("");
            }
            monitorIndexService.save(info);
        }
        return json;
    }
    
    /**
     * 查询监控指标表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitorIndex/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询监控指标表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorIndex entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorIndex> result = monitorIndexService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据id查询监控指标表
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitorIndex/detail")
    public ModelAndView detail(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/config/index/idxDetail");
        MonitorIndex mi = new MonitorIndex();
        if (id != null)
        {
            mi = monitorIndexService.selectByPrimaryKey(id);
        }
        mav.addObject("monitorIndex", mi);
        return mav;
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitorIndex/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @RequiresPermissions("monitor:setting:monitorindex:operator")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组", paramType = "form")
    public JsonMessage del(String ids) throws BusinessException
    {
        monitorIndexService.deleteBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 查询所有证券
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitorIndex/all", method = RequestMethod.GET)
    public List<MonitorIndex> all() throws BusinessException
    {
        List<MonitorIndex> list = monitorIndexService.selectAll();
        return list;
    }
    
    /**
     * 添加或修改监控配置
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/monitorIndex/modify")
    @RequiresPermissions("monitor:setting:monitorindex:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/config/index/modify_index");
        MonitorIndex mi = new MonitorIndex();
        if (id != null)
        {
            mi = monitorIndexService.selectByPrimaryKey(id);
        }
        mav.addObject("monitorIndex", mi);
        return mav;
    }
}
