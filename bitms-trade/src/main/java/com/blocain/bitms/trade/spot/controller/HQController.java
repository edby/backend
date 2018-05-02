/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.spot.controller;

import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *  行情中心  控制器
 * <p>File：HQController.java</p>
 * <p>Title: HQController</p>
 * <p>Description:HQController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SOPT)
@Api(description = "行情中心")
public class HQController extends com.blocain.bitms.trade.quotation.QuotationController
{

    /**
     * 行情中心页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/trade/hq", method = RequestMethod.GET)
    @ApiOperation(value = "行情中心页面导航", httpMethod = "GET")
    public ModelAndView quotation() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        ModelAndView mav = new ModelAndView("spot/quotation");
        return mav;
    }

}
