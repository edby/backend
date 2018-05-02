/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.account.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Message;
import com.blocain.bitms.trade.account.service.MessageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  交割结算记录表控制器
 * <p>File：AccountMsgController.java </p>
 * <p>Title:AccountMsgController </p>
 * <p>Description:AccountMsgController</p>
 * <p>Copyright: Copyright (c) 2017.11.02</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
public class AccountMsgController extends GenericController
{
    @Autowired(required = false)
    private  MessageService messageService;
    
    /**
     * 列表页面导航-综合查询[账户消息查询]
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/msgList")
    @RequiresPermissions("trade:setting:account:index")
    public String recordList() throws BusinessException
    {
        return "trade/account/account/msgList";
    }
    
    /**
     * 查询交割结算记录表-查询
     * @param message
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/msgList/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:account:data")
    public JsonMessage accountMsgListData(Message message, String timeStart, String timeEnd,Pagination pagin) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            message.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            message.setTimeEnd(timeEnd);
        }
        PaginateResult<Message> result = messageService.search(pagin, message);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
