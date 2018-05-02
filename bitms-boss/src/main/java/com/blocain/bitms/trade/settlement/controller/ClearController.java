/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.controller;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
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
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.settlement.entity.SettlementProcessLog;
import com.blocain.bitms.trade.settlement.service.SettlementProcessLogService;
import com.blocain.bitms.trade.settlement.service.SettlementService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 交割控制器
 * <p>File：ClearController.java</p>
 * <p>Title: ClearController</p>
 * <p>Description:ClearController</p>
 * <p>Copyright: Copyright (c) 2017年9月18日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SETTLEMENT)
public class ClearController extends GenericController
{
    @Autowired(required = false)
    private SysParameterService         sysParameterService;

    @Autowired(required = false)
    private SettlementProcessLogService settlementProcessLogService;

    @Autowired(required = false)
    private SettlementService           settlementService;

    @Autowired(required = false)
    private StockInfoService            stockInfoService;

    /**
     * 导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/clear")
    public ModelAndView index() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/clear/list");
        mav.addObject("step", 0);
        mav.addObject("jiesuanjia", 0);
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        mav.addObject("switchStatus", sysParameter.getValue());
        return mav;
    }

    /**
     * 开关状态-导航界面
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/clear/switch")
    public ModelAndView switchStatus(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/settlement/clear/switch");
        mav.addObject("exchangePairMoney", exchangePairMoney);
        mav.addObject("exchangePairVCoin", exchangePairVCoin);
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        mav.addObject("switchStatus", sysParameter.getValue());
        mav.addObject("stockInfo", stockInfo);
        return mav;
    }

    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/clear/updateTradeMasterSwitch", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:clear:operator")
    public JsonMessage updateTradeMasterSwitch(String status) throws BusinessException
    {
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter == null) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (StringUtils.equalsIgnoreCase(status, "yes"))
        {
            if (StringUtils.equalsIgnoreCase(status, sysParameter.getValue()))
            {
                insertSettlementProcessLog(0L, -1, "打开交易总控开关", "重复打开交易总控开关");
                throw new BusinessException("交易总控开关状态错误");
            }
            sysParameter.setValue("yes");
            sysParameterService.updateByPrimaryKey(sysParameter);
        }
        else if (StringUtils.equalsIgnoreCase(status, "no"))
        {
            if (StringUtils.equalsIgnoreCase(status, sysParameter.getValue()))
            {
                insertSettlementProcessLog(0L, -1, "打开交易总控开关", "重复关闭交易总控开关");
                throw new BusinessException("交易总控开关状态错误");
            }
            sysParameter.setValue("no");
            sysParameterService.updateByPrimaryKey(sysParameter);
        }
        else
        {
            insertSettlementProcessLog(0L, -1, "打开交易总控开关", "参数非法");
            throw new BusinessException("交易总控开关状态错误");
        }
        sysParameter.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        JsonMessage msg = getJsonMessage(CommonEnums.SUCCESS);
        return msg;
    }

    /**
     * 公用-插入流程清算日志
     * @param step
     * @param status
     * @param processName
     */
    public void insertSettlementProcessLog(Long step, int status, String processName, String remark)
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        SettlementProcessLog log = new SettlementProcessLog();
        log.setCreateBy(principal.getId());
        log.setCreateDate(new Timestamp(System.currentTimeMillis()));
        log.setProcessId(1002L);
        log.setStatus(BigDecimal.valueOf(status));
        log.setProcessName(step + "." + processName);
        log.setRemark(remark);
        settlementProcessLogService.insert(log);
    }

    /**
     * 公用-步骤处理
     * @param
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/clear/step/operator", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlement:operator")
    public JsonMessage stepOperator(Integer step, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        JsonMessage msg = getJsonMessage(CommonEnums.SUCCESS);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        try
        {
            // TODO:这里调用清算
            insertSettlementProcessLog(1L, 1, "清算操作", "清算完成");
        }catch(Exception e)
        {
            msg = getJsonMessage(CommonEnums.FAIL);
            insertSettlementProcessLog(1L, -1, "清算操作", "清算异常");
        }
        return msg;
    }

    /**
     * 结算价维护
     * @param
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/clear/updateSettlementPrice", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:clear:operator")
    public JsonMessage updateSettlementPrice(BigDecimal jiesuanjia, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        JsonMessage msg = getJsonMessage(CommonEnums.SUCCESS);
        List<StockInfo> list = stockInfoService.findListByIds(exchangePairMoney.toString());
        if (list.size() > 0)
        {
            StockInfo info = list.get(0);
            info.setSettlementPrice(jiesuanjia);
            stockInfoService.updateByPrimaryKey(info);
        }
        else
        {
            logger.debug("结算价不存在");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return msg;
    }

    /**
     * 操作日志-数据
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/clear/steplog/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:clear:data")
    public JsonMessage steplogData(Pagination pagin, Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        SettlementProcessLog settlementProcessLog = new SettlementProcessLog();
        settlementProcessLog.setProcessId(1002L);
        pagin.setOrder(" a.id desc ");
        PaginateResult<SettlementProcessLog> result = settlementProcessLogService.findDoingLogList(pagin, settlementProcessLog);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 公共方法 获取结算价
     * @param stockInfoId
     * @return
     * @throws BusinessException
     */
    public BigDecimal getSettlementPrice(Long stockInfoId) throws BusinessException
    {
        BigDecimal ret = BigDecimal.ZERO;
        List<StockInfo> list = stockInfoService.findListByIds(stockInfoId.toString());
        if (list.size() > 0)
        {
            StockInfo info = list.get(0);
            if (info.getSettlementPrice() == null)
            {
                logger.debug("结算价不存在");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            ret = info.getSettlementPrice();
        }
        else
        {
            ret = BigDecimal.ZERO;
            logger.debug("结算价不存在");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        return ret;
    }

    /**
     * 获取证券信息
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/clear/stockinfo/data", method = RequestMethod.POST)
    public JsonMessage stockinfoData(Long exchangePairMoney, Long exchangePairVCoin) throws BusinessException
    {
        StockInfo stockInfo = new StockInfo();
        List<StockInfo> list = stockInfoService.findListByIds(exchangePairMoney.toString());
        if (list.size() > 0)
        {
            stockInfo = list.get(0);
        }
        return getJsonMessage(CommonEnums.SUCCESS, stockInfo);
    }

    public  StockInfo  getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
