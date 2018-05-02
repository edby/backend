package com.blocain.bitms.monitor;

import com.blocain.bitms.monitor.entity.MonitorDetail;
import com.blocain.bitms.monitor.service.MonitorDetailService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;

/**
 * 明细账监控表 控制器
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "明细账监控表")
public class MonitorDetailController extends GenericController {
    @Autowired(required = false)
    private MonitorDetailService monitorDetailService;



    /**
     * 明细账查询
     *
     * @param entity
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitordetail/data", method = RequestMethod.POST)
    public JsonMessage data(MonitorDetail entity, Pagination pagin)
            throws BusinessException {
        Long time = Long.parseLong(entity.getMonitorDateStr());
        entity.setMonitorDate(new Timestamp(time));
        PaginateResult<MonitorDetail> result = monitorDetailService.findMonitorDetailList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }



}
