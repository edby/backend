/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay;

import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;
import com.blocain.bitms.trade.fund.service.AccountFundTransferService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 划拨列表 控制器
 * <p>File：AccountFundTrunsferController.java </p>
 * <p>Title: AccountFundTrunsferController </p>
 * <p>Description:AccountFundTrunsferController </p>
 * <p>Company: BloCain</p>
 * @author zcx
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.BITPAY)
class AccountFundTransferController extends GenericController
{
    @Autowired(required = false)
    private AccountFundTransferService accountTransferService;

    @Autowired(required = false)
    private UserInfoService            userInfoService;

    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/transfer")
    @RequiresPermissions("trade:setting:accountfundtransfer:index")
    public String list() throws BusinessException
    {
        return "/bitpay/transfer/list";
    }

    /**
     * 提币确认界面页面导航
     * @param ids
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/transfer/password")
    @RequiresPermissions("trade:setting:accountfundtransfer:operator")
    public ModelAndView modify(String ids) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("bitpay/transfer/password");
        mav.addObject("ids", ids);
        List<AccountFundTransfer> list = accountTransferService.findByIds(ids);
        for(AccountFundTransfer transfer:list)
        {
            transfer.setTargetWalletAddr(EncryptUtils.desDecrypt(transfer.getTargetWalletAddr()));
        }
        mav.addObject("list", list);
        return mav;
    }

    /**
     * 查询交易划拨信息
     * @param entity
     * @param pagin
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/transfer/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountfundtransfer:data")
    public JsonMessage data(AccountFundTransfer entity, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountFundTransfer> result = accountTransferService.search(pagin, entity);
        for(AccountFundTransfer trans:result.getList()){
            if(trans.getTargetWalletAddr()!=null&&!trans.getTargetWalletAddr().equals(""))
            {
                trans.setTargetWalletAddr(EncryptUtils.desDecrypt(trans.getTargetWalletAddr()));
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     *  单笔提现
     * @param id
     * @param password
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @RequestMapping(value = "/transfer/confirm")
    @RequiresPermissions("trade:setting:accountfundtransfer:operator")
    @ResponseBody
    public JsonMessage confirm(Long id, String password,String gaCode) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        UserInfo account = userInfoService.selectByPrimaryKey(principal.getId());
        if (StringUtils.isBlank(account.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
        if (StringUtils.isBlank(gaCode)) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        accountTransferService.doSingleCashWthdrawal(id,password,gaCode);
        return getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
     * 批量提现
     * @param ids
     * @throws BusinessException
     */
    @CSRFToken
    @RequestMapping(value = "/transfer/confirm/batch")
    @RequiresPermissions("trade:setting:accountfundtransfer:operator")
    @ResponseBody
    public JsonMessage confirmBatch(String ids, String password,String gaCode) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        UserInfo account = userInfoService.selectByPrimaryKey(principal.getId());
        if (StringUtils.isBlank(account.getAuthKey())) { throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND); }
        Authenticator authenticator = new Authenticator();
        if (!authenticator.checkCode(EncryptUtils.desDecrypt(account.getAuthKey()), Long.valueOf(gaCode)))
        {// 判断验证码
            throw new BusinessException(AccountEnums.ACCOUNT_GACODE_ERROR);
        }
        accountTransferService.doMultipleCashWthdrawal(ids,password);
        return getJsonMessage(CommonEnums.SUCCESS);
    }

}
