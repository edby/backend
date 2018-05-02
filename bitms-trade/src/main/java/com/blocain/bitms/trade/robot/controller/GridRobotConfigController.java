/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.robot.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.robot.cache.RobotCache;
import com.blocain.bitms.trade.robot.thread.AutoTradeThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.robot.entity.GridRobotConfig;
import com.blocain.bitms.trade.robot.entity.RobotModel;
import com.blocain.bitms.trade.robot.entity.RobotMultiInfo;
import com.blocain.bitms.trade.robot.service.GridRobotConfigService;
import com.blocain.bitms.trade.robot.service.GridRobotService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

/**
 * GridRobotConfig 控制器
 * <p>File：GridRobotConfigController.java </p>
 * <p>Title: GridRobotConfigController </p>
 * <p>Description:GridRobotConfigController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping("/robot")
@Api(description = "GridRobotConfig")
public class GridRobotConfigController extends GenericController
{
    @Autowired(required = false)
    private GridRobotConfigService gridRobotConfigService;
    
    @Autowired(required = false)
    private GridRobotService       gridRobotService;
    
    /**
     * 普通用户页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/config")
    public ModelAndView list() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("robot/config");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) {
            mav = new ModelAndView("account/login");
        }
        return mav;
    }
    
    /**
     * 内部页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/config/in")
    public ModelAndView list_in() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("robot/inConfig");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) {
            mav = new ModelAndView("account/login");
        }
        return mav;
    }

    /**
     * 操作GridRobotConfig
     * @param entity
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存GridRobotConfig", httpMethod = "POST")
    public JsonMessage save(@ModelAttribute GridRobotConfig entity) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        GridRobotConfig config = new GridRobotConfig();
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        if (null == entity.getId())
        {
            // 增加模式
            config.setConfigName(entity.getConfigName());
            config.setAccountId(principal.getId());
            List<GridRobotConfig> paramList = gridRobotConfigService.findList(config);
            // 保存前，检查是否存在同一参数名称
            if (!CollectionUtils.isEmpty(paramList))
            {
                json.setCode(1000);
                json.setMessage("提交失败，配置名称已存在或为空");
                return json;
            }
        }
        else
        {
            // 编辑模式
            GridRobotConfig conf = gridRobotConfigService.selectByPrimaryKey(entity.getId());
            if (conf == null) { throw new BusinessException("原配置不存在！"); }
            if (conf.getAccountId().longValue() != principal.getId().longValue()) { throw new BusinessException("用户账号与配置id不匹配！"); }
        }
        if (entity.getActive() != null && entity.getActive() == 1)
        {
            json.setCode(1000);
            json.setMessage("提交失败，请先禁用配置");
            return json;
        }
        entity.setAccountId(principal.getId());
        // 每次更新配置都会设置为禁用状态
        entity.setActive(0);
        entity.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        // 暂时默认下单量类型恒为范围值。
        entity.setAmtType(1);
        if (beanValidator(json, entity))
        {
            // 参数验证成功，入库
            gridRobotConfigService.save(entity);
        }
        return json;
    }
    
    /**
     * 查询GridRobotConfig
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/config/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询GridRobotConfig", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute GridRobotConfig entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        entity.setAccountId(principal.getId());
        PaginateResult<GridRobotConfig> result = gridRobotConfigService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param id
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/del", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    public JsonMessage del(Long id) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        GridRobotConfig entity = gridRobotConfigService.selectByPrimaryKey(id);
        if (entity == null) { throw new BusinessException("配置不存在！"); }
        if (entity.getAccountId().longValue() != principal.getId().longValue()) { throw new BusinessException("用户账号与配置id不匹配！"); }
        if (entity.getActive() == 1)
        {
            json.setCode(1000);
            json.setMessage("删除失败，请先禁用配置");
            return json;
        }
        gridRobotConfigService.delete(id);
        return json;
    }
    
    /**
     * 根据指定ID启用
     * @param id
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/active", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID启用", httpMethod = "POST")
    public JsonMessage active(Long id) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        GridRobotConfig config = gridRobotConfigService.selectByPrimaryKey(id);
        if (config == null) { throw new BusinessException("配置不存在！"); }
        if (config.getAccountId().longValue() != principal.getId().longValue()) { throw new BusinessException("用户账号与配置id不匹配！"); }
        // 判断是否存在已经处于启用状态的配置
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        List<GridRobotConfig> list = gridRobotConfigService.selectByAccountId(principal.getId());
        if (!CollectionUtils.isEmpty(list))
        {
            for (GridRobotConfig conf : list)
            {
                if (conf.getActive() == 1)
                {
                    json.setCode(1002);
                    json.setMessage("启用失败，已存在启用状态的配置");
                    return json;
                }
            }
        }
        RobotModel robot = new RobotModel();
        config.setRobotType(0);
        robot.init(config);
        // 获取初始账户资产信息
        robot.setBeginAssetInfo(gridRobotService.getAccountFundAsset(config.getAccountId(), robot.getPair().getCapital()));
//        gridRobotConfigService.active(robot);
        AutoTradeThread tradeThread = new AutoTradeThread();
        tradeThread.setRobot(robot);
        tradeThread.setGridRobotService(gridRobotService);
        // 将线程加入缓存map,便于后面停止该线程操作
        RobotCache.THREAD_MAP.put(robot.getParam().getId(), tradeThread);
        Thread thread = new Thread(tradeThread);
        tradeThread.setStartTime(new Date(System.currentTimeMillis()));
        thread.start();

        // 更新机器人状态
        config.setActive(1);
        config.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        gridRobotConfigService.save(config);
        return json;
    }
    
    /**
     * 根据指定ID启用
     * @param id
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/active/in", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID启用", httpMethod = "POST")
    public JsonMessage inActive(Long id) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        GridRobotConfig config = gridRobotConfigService.selectByPrimaryKey(id);
        if (config == null) { throw new BusinessException("配置不存在！"); }
        if (config.getAccountId().longValue() != principal.getId().longValue()) { throw new BusinessException("用户账号与配置id不匹配！"); }
        // 判断是否存在已经处于启用状态的配置
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        List<GridRobotConfig> list = gridRobotConfigService.selectByAccountId(principal.getId());
        if (!CollectionUtils.isEmpty(list))
        {
            for (GridRobotConfig conf : list)
            {
                if (conf.getActive() == 1)
                {
                    json.setCode(1002);
                    json.setMessage("启用失败，已存在启用状态的配置");
                    return json;
                }
            }
        }
        RobotModel robot = new RobotModel();
        config.setRobotType(1);
        robot.init(config);
        // 获取初始账户资产信息
        robot.setBeginAssetInfo(gridRobotService.getAccountFundAsset(config.getAccountId(), robot.getPair().getCapital()));
        robot.setCurAssetInfo(gridRobotService.getAccountFundAsset(config.getAccountId(), robot.getPair().getCapital()));
//        gridRobotConfigService.active(robot);
        AutoTradeThread tradeThread = new AutoTradeThread();
        tradeThread.setRobot(robot);
        tradeThread.setGridRobotService(gridRobotService);
        // 将线程加入缓存map,便于后面停止该线程操作
        RobotCache.THREAD_MAP.put(robot.getParam().getId(), tradeThread);
        Thread thread = new Thread(tradeThread);
        tradeThread.setStartTime(new Date(System.currentTimeMillis()));
        thread.start();
        // 更新机器人状态
        config.setActive(1);
        config.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        gridRobotConfigService.save(config);
        return json;
    }


    /**
     * 根据指定ID禁用
     * @param id
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/config/unActive", method = RequestMethod.POST)
    @ApiOperation(value = "根据指定ID禁用", httpMethod = "POST")
    public JsonMessage unActive(Long id) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        GridRobotConfig config = gridRobotConfigService.selectByPrimaryKey(id);
        if (config == null) { throw new BusinessException("配置不存在！"); }
        if (config.getAccountId().longValue() != principal.getId().longValue()) { throw new BusinessException("用户账号与配置id不匹配！"); }
        if (config.getActive() == 0) { throw new BusinessException("禁用失败，已处于禁用状态"); }
//        gridRobotConfigService.unActive(config);
        // 获取机器人线程
        AutoTradeThread thread = RobotCache.THREAD_MAP.get(config.getId());
        if (null == thread)
        {
            // throw new BusinessException("机器人不存在,已恢复禁用状态！");
            logger.error("当前用户机器人线程不在队列中：" + config.getAccountId());
        }
        else
        {
            thread.setRunning(false);
            RobotCache.THREAD_MAP.remove(config.getId());
        }
        config.setActive(0);
        config.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        gridRobotConfigService.save(config);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 停止所有机器人线程，在服务器出现特定异常时，通过调用该接口来终止所有机器人运行(应急接口)
     */
    // @ResponseBody
    // @RequestMapping(value = "/stopAll", method = RequestMethod.GET)
    // public JsonMessage stopAll() throws BusinessException
    // {
    // UserPrincipal principal = OnLineUserUtils.getPrincipal();
    // if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
    // // 移除机器人线程
    // Iterator<Map.Entry<Long, AutoTradeThread>> it = RobotCache.THREAD_MAP.entrySet().iterator();
    // while (it.hasNext())
    // {
    // Map.Entry<Long, AutoTradeThread> entry = it.next();
    // AutoTradeThread thread = entry.getValue();
    // thread.setRunning(false);
    // it.remove();
    // }
    // return getJsonMessage(CommonEnums.SUCCESS);
    // }
    /**
     * 查询账户综合信息
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/config/multiData", method = RequestMethod.POST)
    public JsonMessage multiData() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
//        RobotMultiInfo info = gridRobotConfigService.getMultiData(principal.getId());
        GridRobotConfig config = new GridRobotConfig();
        // 查询是否有启用状态的配置
        config.setAccountId(principal.getId());
        config.setActive(1);
        List<GridRobotConfig> list = gridRobotConfigService.findList(config);
        RobotMultiInfo info = new RobotMultiInfo();
        info.setRobotAmt(RobotCache.THREAD_MAP.size());
        info.setPairName("btc2usd");
        info.setMessage("success");
        info.setBotStatus(false);
        if (!CollectionUtils.isEmpty(list))
        {
            config = list.get(0);
            AutoTradeThread thread = RobotCache.THREAD_MAP.get(config.getId());
            if (thread == null)
            {
                // 存在启用状态的配置但是实际上不存在运行的机器人，则需要将启用状态更新为禁用
                config.setActive(0);
                config.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                gridRobotConfigService.save(config);
                info.setMessage("机器人线程不存在，已自动恢复为禁用状态");
                return getJsonMessage(CommonEnums.SUCCESS, info);
            }
            // 存在活跃机器人时，需设置以下属性
            info.setBotStatus(true);
            info.setConfigName(config.getConfigName());
            info.setStartTime(thread.getStartTime());
            info.setBuySwitch(thread.getRobot().getBuySwitch());
            info.setSellSwitch(thread.getRobot().getSellSwitch());
            FundChangeModel beginAsset = thread.getRobot().getBeginAssetInfo();
            FundChangeModel curAsset = thread.getRobot().getCurAssetInfo();
            info.setUsdOrgBal(beginAsset.getUsdxAmount().subtract(beginAsset.getUsdxBorrow()));
            info.setUsdCurBal(curAsset.getUsdxAmount().subtract(curAsset.getUsdxBorrow()));
            info.setBtcOrgBal(beginAsset.getBtcAmount().subtract(beginAsset.getBtcBorrow()));
            info.setBtcCurBal(curAsset.getBtcAmount().subtract(curAsset.getBtcBorrow()));
        }
        return getJsonMessage(CommonEnums.SUCCESS, info);
    }
}
