/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.settlement.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.settlement.entity.SettlementRecord;
import com.blocain.bitms.trade.settlement.service.SettlementRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  交割结算记录表控制器
 * <p>File：RecordController.java </p>
 * <p>Title:RecordController </p>
 * <p>Description:RecordController</p>
 * <p>Copyright: Copyright (c) 2017.11.02</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SETTLEMENT)
public class RecordController extends GenericController
{
    @Autowired(required = false)
    private SettlementRecordService     settlementRecordService;
    
    /**
     * 列表页面导航-交割管理[交割结算记录]
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlementRecord/list")
    @RequiresPermissions("trade:setting:settlementRecord:index")
    public String recordList() throws BusinessException
    {
        return "trade/settlement/record/list";
    }
    
    /**
     * 查询交割结算记录表-查询
     * @param settlementRecord
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlementRecord/list/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlementRecord:data")
    public JsonMessage accountAssetListData(SettlementRecord settlementRecord,Pagination pagin,String settlementTypeString) throws BusinessException
    {
        //交割结算类型特殊处理
        if( StringUtils.isBlank(settlementTypeString))
        {
            settlementRecord.setSettlementType(null);
        }
        else
        {
            if(StringUtils.equalsIgnoreCase(settlementTypeString,"1"))
            {
                settlementRecord.setSettlementType(1);
            }
            else
            {
                settlementRecord.setSettlementType(2);
            }
        }
        PaginateResult<SettlementRecord> result = settlementRecordService.search(pagin, settlementRecord);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
