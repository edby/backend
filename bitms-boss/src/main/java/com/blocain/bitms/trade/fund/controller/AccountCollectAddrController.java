/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import java.util.Date;
import java.util.List;

import com.blocain.bitms.boss.system.service.UserInfoService;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrCheckLog;
import com.blocain.bitms.trade.fund.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.blocain.bitms.boss.system.entity.UserInfo;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.google.Authenticator;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

/**
 * 提币地址管理 控制器
 * <p>File：AccountCurrentController.java </p>
 * <p>Title: AccountCurrentController </p>
 * <p>Description:AccountCurrentController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountCollectAddrController extends GenericController
{
    @Autowired(required = false)
    private AccountCollectAddrService   accountCollectAddrService;

    @Autowired(required = false)
    private AccountCollectAddrCheckLogService   accountCollectAddrCheckLogService;

    @Autowired(required = false)
    private AccountService              accountService;

    @Autowired(required = false)
    private AccountCertificationService accountCertificationService;

    /**
     * 列表页面导航-提币地址审核
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/accountCollectAddr")
    @RequiresPermissions("trade:setting:accountCollectAddr:index")
    public String list() throws BusinessException
    {
        return "trade/fund/accountCollectAddr/list";
    }

    /**
     * 审核界面
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/accountCollectAddr/approval")
    @RequiresPermissions("trade:setting:accountCollectAddr:operator")
    public ModelAndView approvalIndex(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("trade/fund/accountCollectAddr/approval");
        // 当前资金流水
        AccountCollectAddr accountCollectAddr = new AccountCollectAddr();
        if (id != null)
        {
            accountCollectAddr = accountCollectAddrService.selectByPrimaryKey(id);
            if (accountCollectAddr.getCollectAddr() != null && !accountCollectAddr.getCollectAddr().equals(""))
            {
                accountCollectAddr.setCollectAddr(EncryptUtils.desDecrypt(accountCollectAddr.getCollectAddr()));// des解密
            }
        }
        // 当前账户
        Account account = accountService.selectByPrimaryKey(accountCollectAddr.getAccountId());
        AccountCertification certification = new AccountCertification();
        certification.setAccountId(account.getId());
        List<AccountCertification> certList = accountCertificationService.findList(certification);
        if (certList.size() > 0)
        {
            certification = certList.get(0);
        }
        mav.addObject("certification", certification);
        mav.addObject("accountCollectAddr", accountCollectAddr);
        mav.addObject("account", account);
        AccountCollectAddrCheckLog accountCollectAddrCheckLog = new AccountCollectAddrCheckLog();
        accountCollectAddrCheckLog.setCollectAddrId(id);
        mav.addObject("logList", accountCollectAddrCheckLogService.findList(accountCollectAddrCheckLog));
        return mav;
    }

    /**
     * 查询
     * @param entity
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/accountCollectAddr/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:accountCollectAddr:data")
    public JsonMessage data(AccountCollectAddr entity, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        PaginateResult<AccountCollectAddr> result = accountCollectAddrService.search(pagin, entity);
        for (AccountCollectAddr curr : result.getList())
        {
            if (curr.getCollectAddr() != null && !curr.getCollectAddr().equals(""))
            {
                curr.setCollectAddr(EncryptUtils.desDecrypt(curr.getCollectAddr()));// des解密
            }
        }
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    

    /**
     * 审核操作
     * @param accountCollectAddr
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/accountCollectAddr/approval/approval", method = RequestMethod.POST)
    @RequiresPermissions(value = {"trade:setting:accountCollectAddr:operator"})
    public JsonMessage approval(AccountCollectAddr accountCollectAddr) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        AccountCollectAddr addr = accountCollectAddrService.selectByPrimaryKey(accountCollectAddr.getId());
        if (null == addr)
        {
            logger.info("数据校验失败");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        if(FundConsts.PUBLIC_STATUS_YES.equals(addr.getStatus())){
            logger.info("已经审核通过，不能再审核！");
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        addr.setStatus(accountCollectAddr.getStatus());
        accountCollectAddrService.updateByPrimaryKey(addr);

        AccountCollectAddrCheckLog accountCollectAddrCheckLog = new AccountCollectAddrCheckLog();
        accountCollectAddrCheckLog.setCollectAddrId(addr.getId());
        accountCollectAddrCheckLog.setCreateBy(principal.getId());
        accountCollectAddrCheckLog.setCreateDate(new Date());
        if(accountCollectAddr.getRemark()==null)
        {
            accountCollectAddr.setRemark("");
        }
        accountCollectAddrCheckLog.setRemark(accountCollectAddr.getRemark()+" 审核状态变为："+accountCollectAddr.getStatus());
        accountCollectAddrCheckLogService.insert(accountCollectAddrCheckLog);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
}
