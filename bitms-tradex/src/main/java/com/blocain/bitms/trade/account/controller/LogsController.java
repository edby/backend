package com.blocain.bitms.trade.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.AccountLog;
import com.blocain.bitms.trade.account.service.AccountLogNoSql;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 日志控制器 Introduce
 * <p>File：LogsController.java</p>
 * <p>Title: LogsController</p>
 * <p>Description: LogsController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
@Api(description = "日志信息")
public class LogsController extends GenericController
{
    @Autowired(required = false)
    private AccountLogNoSql accountLogNoSql;
    
    /**
     * 登陆日志
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/logs/login", method = RequestMethod.GET)
    @ApiOperation(value = "登陆日志", httpMethod = "GET")
    public JsonMessage loginLog() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        List<AccountLog> result = accountLogNoSql.findLastTenLoginLogs(principal.getId());
        return this.getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 操作日志
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/logs/setting", method = RequestMethod.GET)
    @ApiOperation(value = "操作日志", httpMethod = "GET")
    public JsonMessage settingLog() throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        List<AccountLog> result = accountLogNoSql.findLastTenSettingLogs(principal.getId());
        return this.getJsonMessage(CommonEnums.SUCCESS, result);
    }
}
