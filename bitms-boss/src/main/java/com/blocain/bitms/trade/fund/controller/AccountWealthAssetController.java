package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.trade.fund.entity.AccountWealthAsset;
import com.blocain.bitms.trade.fund.service.AccountWealthAssetService;
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
 * 账户理财资产
 * <p>File：AccountWealthAssetController.java</p>
 * <p>Title: AccountWealthAssetController</p>
 * <p>Description:AccountWealthAssetController</p>
 * <p>Copyright: Copyright (c) 2017年10月24日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountWealthAssetController extends GenericController
{
    @Autowired(required = false)
    private AccountWealthAssetService accountWealthAssetService;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/wealthAsset")
    @RequiresPermissions("trade:setting:wealthAsset:index")
    public String list() throws BusinessException
    {
        return "trade/fund/wealthAsset/list";
    }

    /**
     * 账户钱包资产-查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/wealthAsset/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:wealthAsset:data")
    public JsonMessage data(AccountWealthAsset entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountWealthAsset> result = accountWealthAssetService.selectAll(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
