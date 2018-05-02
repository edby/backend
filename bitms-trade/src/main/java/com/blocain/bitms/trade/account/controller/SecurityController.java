package com.blocain.bitms.trade.account.controller;

import java.util.List;
import java.util.Map;

import com.blocain.bitms.orm.utils.ServletsUtils;
import com.blocain.bitms.tools.ip.GeoIPUtils;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.IPUtil;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountLogConsts;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.maxmind.geoip.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.EnumUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.enums.SecurityPolicyEnums;
import com.blocain.bitms.trade.account.enums.TradePolicyEnums;
import com.blocain.bitms.trade.account.service.AccountService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证策略管理控制器
 * <p>File：SecurityController.java</p>
 * <p>Title: SecurityController</p>
 * <p>Description: SecurityController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
@Api(description = "验证策略管理")
public class SecurityController extends GenericController
{
    @Autowired(required = false)
    private AccountService       accountService;
    
    @Autowired(required = false)
    private AccountLogNoSql      accountLogNoSql;
    
    @Autowired(required = false)
    private AccountPolicyService accountPolicyService;
    
    /**
     * 获取交易验证策略
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/policy/trade", method = RequestMethod.GET)
    @ApiOperation(value = "获取交易验证策略", httpMethod = "GET")
    public JsonMessage getTradePolicys() throws BusinessException
    {
        List<TradePolicyEnums> EnumConstants = EnumUtils.toList(TradePolicyEnums.class);
        Map<String, Object> result = Maps.newHashMap();
        for (TradePolicyEnums policyEnums : EnumConstants)
        {
            result.put(String.valueOf(policyEnums.code), policyEnums.getMessage());
//            result.put(String.valueOf(policyEnums.code), getMessage(policyEnums.getMessage()));
        }
        return super.getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
    * 获取安全验证策略
    * @return {@link JsonMessage}
    * @throws BusinessException
    */
    @ResponseBody
    @RequestMapping(value = "/policy/security", method = RequestMethod.GET)
    @ApiOperation(value = "获取安全验证策略", httpMethod = "GET")
    public JsonMessage getSecurityPolicys() throws BusinessException
    {
        List<SecurityPolicyEnums> EnumConstants = EnumUtils.toList(SecurityPolicyEnums.class);
        Map<String, Object> result = Maps.newHashMap();
        for (SecurityPolicyEnums policyEnums : EnumConstants)
        {
            result.put(String.valueOf(policyEnums.code), policyEnums.getMessage());
//            result.put(String.valueOf(policyEnums.code), getMessage(policyEnums.getMessage()));
        }
        return super.getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 设置交易验证策略
     * @param policy
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/policy/trade/save", method = RequestMethod.POST)
    @ApiImplicitParam(name = "level", value = "验证策略", required = true, paramType = "form")
    @ApiOperation(value = "设置交易验证策略", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage saveTradePolicy(Integer level, @ModelAttribute PolicyModel policy) throws BusinessException
    {
        if (level == null) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if (AccountConsts.TRADE_POLICY_TWOHOUR == level || AccountConsts.TRADE_POLICY_EVERYTIME == level)
        {// 如果启动资金密码后需要先判断用户是否已设置资金密码
            if (StringUtils.isBlank(account.getWalletPwd())) throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST);
        }
        accountPolicyService.validSecurityPolicy(account, policy);
        account.setTradePolicy(level);
        accountService.updateByPrimaryKey(account);
        saveOperationLogs(principal, "设置交易验证策略");
        return super.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 设置安全验证验证策略
     * @param policy
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/policy/security/save", method = RequestMethod.POST)
    @ApiImplicitParam(name = "level", value = "验证策略", required = true, paramType = "form")
    @ApiOperation(value = "设置安全验证策略", httpMethod = "POST", consumes = "application/x-www-form-urlencoded")
    public JsonMessage saveSecurityPolicy(Integer level, @ModelAttribute PolicyModel policy) throws BusinessException
    {
        if (level == null) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        if (AccountConsts.SECURITY_POLICY_NEEDGA == level)
        {// 判断是否已绑定GA
            if (StringUtils.isBlank(account.getAuthKey())) throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND);
        }
        if (AccountConsts.SECURITY_POLICY_NEEDSMS == level)
        {// 判断是否已绑定SMS
            if (StringUtils.isBlank(account.getMobNo())) throw new BusinessException(AccountEnums.ACCOUNT_PHONE_NOTBIND);
        }
        if (AccountConsts.SECURITY_POLICY_NEEDGAANDSMS == level)
        {// 判断是否已绑定SMS和GA
            if (StringUtils.isBlank(account.getAuthKey())) throw new BusinessException(CommonEnums.ERROR_GA_NOT_BIND);
            if (StringUtils.isBlank(account.getMobNo())) throw new BusinessException(AccountEnums.ACCOUNT_PHONE_NOTBIND);
        }
        accountPolicyService.validSecurityPolicy(account, policy);
        account.setSecurityPolicy(level);
        accountService.updateByPrimaryKey(account);
        saveOperationLogs(principal, "设置安全验证策略");
        return super.getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
     * 保存操作日志
     * @param principal
     * @param content
     */
    void saveOperationLogs(UserPrincipal principal, String content)
    {
        try
        {
            if (null == principal) principal = OnLineUserUtils.getPrincipal();
            HttpServletRequest request = ServletsUtils.getRequest();
            AccountLog accountLog = new AccountLog();
            accountLog.setContent(content);
            accountLog.setSystemName(BitmsConst.BITMS_PROJECT_NAME);
            accountLog.setAccountId(principal.getId());
            accountLog.setUrl(request.getRequestURI());
            accountLog.setOpType(AccountLogConsts.LOG_TYPE_SETTING);
            accountLog.setAccountName(principal.getTrueName());
            accountLog.setIpAddr(IPUtil.getOriginalIpAddr(request));
            accountLog.setCreateDate(CalendarUtils.getCurrentLong());
            if (null != accountLog.getIpAddr())
            {
                String rigonName = "Unknown address";
                String[] ipArray = accountLog.getIpAddr().split(",");
                for (String ip : ipArray) {
                    Location location = GeoIPUtils.getInstance().getLocation(ip);
                    if (null != location)
                    {
                        rigonName = new StringBuilder(location.countryName).append("|").append(location.city).toString();
                    }
                    break;
                }
                accountLog.setRigonName(rigonName);
            }
            accountLogNoSql.insert(accountLog);
        }
        catch (RuntimeException e)
        {
            logger.error("操作日志记录失败：{}", e.getCause());
        }
    }
}
