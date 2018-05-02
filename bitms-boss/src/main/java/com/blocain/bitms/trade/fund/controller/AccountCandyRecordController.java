/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.entity.AccountCandyRecord;
import com.blocain.bitms.trade.fund.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 糖果 控制器
 * <p>File：AccountCandyRecordController.java </p>
 * <p>Title: AccountCandyRecordController </p>
 * <p>Description:AccountCandyRecordController </p>
 * <p>Copyright: Copyright (c) 2018-03-14 19:02:02 </p>
 * <p>Company: BloCain</p>
 * @author zhangchunxi
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountCandyRecordController extends GenericController
{
    @Autowired(required = false)
    private AccountCandyRecordService          accountCandyRecordService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/accountCandyRecord")
    @RequiresPermissions("trade:setting:accountCandyRecord:index")
    public ModelAndView list() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/accountCandyRecord/list");
        return mav;
    }
    
    /**
     * 查询账户流水表-查询审批表
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequiresPermissions("trade:setting:accountCandyRecord:data")
    @RequestMapping(value = "/accountCandyRecord/data", method = RequestMethod.POST)
    public JsonMessage approvalData(AccountCandyRecord entity, Pagination pagin) throws BusinessException
    {
        if(StringUtils.isNotBlank(entity.getTimeStart()))
        {
            entity.setTimeStart(entity.getTimeStart()+" 00:00:00");
        }
        if(StringUtils.isNotBlank(entity.getTimeEnd()))
        {
            entity.setTimeEnd(entity.getTimeEnd()+" 23:59:59");
        }
        PaginateResult<AccountCandyRecord> result = accountCandyRecordService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
