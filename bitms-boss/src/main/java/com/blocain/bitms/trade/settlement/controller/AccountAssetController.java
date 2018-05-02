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
import com.blocain.bitms.trade.settlement.entity.SettlementAccountAsset;
import com.blocain.bitms.trade.settlement.service.SettlementAccountAssetService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  交割结算账户资产表控制器
 * <p>File：AccountAssetController.java </p>
 * <p>Title:AccountAssetController </p>
 * <p>Description:AccountAssetController </p>
 * <p>Copyright: Copyright (c) 2017.11.02</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SETTLEMENT)
public class AccountAssetController extends GenericController
{
    @Autowired(required = false)
    private SettlementAccountAssetService   settlementAccountAssetService;
    
    /**
     * 列表页面导航-交割管理[交割账户资产]
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/settlementAccountAsset/list")
    @RequiresPermissions("trade:setting:settlementAccountAsset:index")
    public String accountAssetList() throws BusinessException
    {
        return "trade/settlement/accountAsset/list";
    }
    
    /**
     * 查询交割结算账户资产表-查询
     * @param settlementAccountAsset
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/settlementAccountAsset/list/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:settlementAccountAsset:data")
    public JsonMessage accountAssetListData(SettlementAccountAsset settlementAccountAsset, String timeStart, String timeEnd, Pagination pagin,String settlementAccountTypeString) throws BusinessException
    {
        if (StringUtils.isNotBlank(timeStart))
        {
            settlementAccountAsset.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            settlementAccountAsset.setTimeEnd(timeEnd);
        }
        //交割结算类型特殊处理
        if( StringUtils.isBlank(settlementAccountTypeString))
        {
            settlementAccountAsset.setSettlementType(null);
        }
        else
        {
            if(StringUtils.equalsIgnoreCase(settlementAccountTypeString,"1"))
            {
                settlementAccountAsset.setSettlementType(1);
            }
            else
            {
                settlementAccountAsset.setSettlementType(2);
            }
        }
        PaginateResult<SettlementAccountAsset> result = settlementAccountAssetService.search(pagin, settlementAccountAsset);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
