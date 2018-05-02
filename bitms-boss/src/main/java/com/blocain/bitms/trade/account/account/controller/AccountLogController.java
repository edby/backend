/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.account.controller;


import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;

/**
 * 用户操作记录
 * <p>File：AccountLogController.java</p>
 * <p>Title: AccountLogController</p>
 * <p>Description:AccountLogController</p>
 * <p>Copyright: Copyright (c) 2017年8月16日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
public class AccountLogController extends GenericController
{
    @Autowired(required = false)
    private AccountLogNoSql accountLogNoSql;
    
    /**
     * 页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/accountLog")
    @RequiresPermissions("trade:setting:accountlog:index")
    public String list() throws BusinessException
    {
        return "trade/account/log/list";
    }
    
    /**
     * 用户操作记录
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountLog/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountlog:data")
    public JsonMessage data(AccountLog entity, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            entity.setTimeStart(CalendarUtils.getLongFromTime(timeStart, DateConst.DATE_FORMAT_YMDHMS));
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entity.setTimeEnd(CalendarUtils.getLongFromTime(timeEnd, DateConst.DATE_FORMAT_YMDHMS));
        }
        PaginateResult<AccountLog> result = accountLogNoSql.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
