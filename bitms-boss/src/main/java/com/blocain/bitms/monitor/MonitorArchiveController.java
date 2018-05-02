package com.blocain.bitms.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.monitor.entity.MonitorArchiveResult;
import com.blocain.bitms.monitor.service.MonitorArchiveService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 归档服务 控制器
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
public class MonitorArchiveController extends GenericController
{
    @Autowired(required = false)
    private MonitorArchiveService monitorArchiveService;
    
    /**
     * 归档服务页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/archive")
    @RequiresPermissions("monitor:setting:archive:index")
    public String index() throws BusinessException
    {
        return "monitor/archive/archive";
    }
    
    /**
     * 执行归档
     */
    @ResponseBody
    @RequiresPermissions("monitor:setting:archive:operator")
    @RequestMapping(value = "/archive/execute", method = RequestMethod.POST)
    public JsonMessage archive() throws BusinessException
    {
        // 此处调用归档服务接口
        MonitorArchiveResult res = monitorArchiveService.executeArchive();
        return getJsonMessage(CommonEnums.SUCCESS, res);
    }
}
