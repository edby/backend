/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 查询系统参数值-交易系统
 * <p>File：ParameterController.java</p>
 * <p>Title: ParameterController</p>
 * <p>Description:ParameterController</p>
 * <p>Copyright: Copyright (c) 2017年7月24日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@Api(description = "交易系统参数")
@RequestMapping(BitmsConst.COMMON)
public class ParameterController extends GenericController
{
    @Autowired(required = false)
    private SysParameterService sysParameterService;
    
    /**
     * 交易系统：根据参数名查值
     * @param sysParameter
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/parameter/getValue", method = RequestMethod.POST)
    @ApiOperation(value = "交易系统：根据参数名查值", httpMethod = "POST")
    public SysParameter getValue(SysParameter sysParameter) throws BusinessException
    {
        sysParameter.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        return sysParameter;
    }
}
