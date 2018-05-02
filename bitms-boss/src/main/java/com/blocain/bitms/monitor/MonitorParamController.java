/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor;

import com.blocain.bitms.monitor.entity.MonitorParam;
import com.blocain.bitms.monitor.service.MonitorParamService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.sql.Timestamp;
import java.util.List;


/**
 * 监控参数表 控制器
 * <p>File：MonitorParamController.java </p>
 * <p>Title: MonitorParamController </p>
 * <p>Description:MonitorParamController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "监控参数表")
public class MonitorParamController extends GenericController
{
    @Autowired(required = false)
    private MonitorParamService monitorParamService;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/param")
    @RequiresPermissions("monitor:setting:monitorparam:index")
    public String list() throws BusinessException
    {
        return "monitor/paramset/list";
    }

    /**
     * 操作证券信息表
     * @param monitorParam
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/param/save", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorparam:operator")
    public JsonMessage save(MonitorParam monitorParam) throws BusinessException
    {
        MonitorParam mp = new MonitorParam();
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == monitorParam.getId())
        {
            mp.setParamCode(monitorParam.getParamCode());
            mp.setParamValue(monitorParam.getParamValue());
            List<MonitorParam> paramList = monitorParamService.findList(mp);

            //保存前，检查是否存在同一参数代码和参数值
            if(!CollectionUtils.isEmpty(paramList))
            {
                json.setCode(1000);
                json.setMessage("参数代码已存在!!!");
                return json;
            }
        }
        monitorParam.setCreateBy(String.valueOf(principal.getId()));
        monitorParam.setCreateDate(new Timestamp(System.currentTimeMillis()));
        if (beanValidator(json, monitorParam)) {
            //如果是修改操作，需要同步缓存信息
//            if (null == monitorParam.getId()) {
//                monitorParamService.save(monitorParam);
//                List<MonitorConfig> configs = monitorConfigService.findRelatedList(String.valueOf(monitorParam.getId()));
//                for (MonitorConfig config : configs) {
//                    monitorConfigService.cacheSingleConfigInfo(config);
//                }
//            }else{
                monitorParamService.save(monitorParam);
//            }
        }
        return json;
    }
    /**
     * 查询监控参数表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/param/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:monitorparam:data")
    @ApiOperation(value = "查询监控参数表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute MonitorParam entity,@ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<MonitorParam> result = monitorParamService.search(pagin,entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
//    @ResponseBody
//    @RequestMapping(value = "/del", method = RequestMethod.POST)
//    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
//    @ApiImplicitParam(name = "ids", value = "以','分割的编号组",paramType = "form")
//    public JsonMessage del(String ids) throws BusinessException
//    {
//        monitorParamService.deleteBatch(ids.split(","));
//        return getJsonMessage(CommonEnums.SUCCESS);
//    }


    /**
     * 添加或修改参数
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/param/modify",method = RequestMethod.GET)
    @RequiresPermissions("monitor:setting:monitorparam:operator")
    public ModelAndView modify(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/paramset/modify");
        MonitorParam monitorParam = new MonitorParam();
        if (id != null)
        {
            monitorParam = monitorParamService.selectByPrimaryKey(id);
        }
        mav.addObject("monitorParam", monitorParam);
        return mav;
    }

    /**
     * 查询参数类型为消息提醒（包括短信和邮件）的所有记录
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/param/related", method = RequestMethod.GET)
    public List<MonitorParam> related() throws BusinessException
    {
        List<MonitorParam> list = monitorParamService.findRelatedList();
        return list;
    }
}
