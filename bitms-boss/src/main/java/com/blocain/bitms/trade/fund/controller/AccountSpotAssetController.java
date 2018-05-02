package com.blocain.bitms.trade.fund.controller;


import com.blocain.bitms.trade.fund.entity.AccountSpotAsset;
import com.blocain.bitms.trade.fund.service.AccountSpotAssetService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;


/**
 * 账户现货资产
 * <p>File：AccountSpotAssetController.java</p>
 * <p>Title: AccountSpotAssetController</p>
 * <p>Description:AccountSpotAssetController</p>
 * <p>Copyright: Copyright (c) 2017年10月24日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountSpotAssetController extends GenericController
{
    @Autowired(required = false)
    private AccountSpotAssetService accountSpotAssetService;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/spotAsset")
    @RequiresPermissions("trade:setting:spotAsset:index")
    public String list() throws BusinessException
    {
        return "trade/fund/spotAsset/list";
    }

    /**
     * 账户钱包资产-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/spotAsset/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:spotAsset:data")
    public JsonMessage data(AccountSpotAsset entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountSpotAsset> result = accountSpotAssetService.selectAll(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
