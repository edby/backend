package com.blocain.bitms.trade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 合约交易接口控制器 Introduce
 * <p>File：FutureTradeController.java</p>
 * <p>Title: FutureTradeController</p>
 * <p>Description: FutureTradeController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping("/api/v1")
public class FutureTradeController extends GenericController
{
    /**
     * 获取合约账户信息
     * @param symbol
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/future_userinfo")
    public JsonMessage userinfo(String symbol) throws BusinessException
    {
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
    /**
    * 获取用户持仓获取合约账户信息
    * @param symbol
    * @return {@link JsonMessage}
    * @throws BusinessException
    */
    @ResponseBody
    @RequestMapping("/future_position")
    public JsonMessage position(String symbol) throws BusinessException
    {
        return this.getJsonMessage(CommonEnums.SUCCESS);
    }
}
