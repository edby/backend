package com.blocain.bitms.monitor;

import com.blocain.bitms.monitor.entity.MonitorMatchFund;
import com.blocain.bitms.monitor.service.MonitorMatchFundService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 撮合交易总账表 控制器
 */
@Controller
@RequestMapping(BitmsConst.MONITOR)
@Api(description = "交易总账监控表")
public class MonitorMatchFundController extends GenericController {
    @Autowired(required = false)
    private MonitorMatchFundService monitorMatchFundService;

    /**
     * 列表页面导航-交易总账监控
     *
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/matchfund")
    @RequiresPermissions("monitor:setting:matchfund:index")
    public ModelAndView list(String monitorDate, String monitorType, String monitorSubType, String monitorResult) throws BusinessException {

        ModelAndView mav = new ModelAndView("monitor/monitor/monitormatchfund_list");
        mav.addObject("chkDate", monitorDate);
        mav.addObject("monitorType", monitorType);
        mav.addObject("monitorSubType", monitorSubType);
        mav.addObject("chkResult", monitorResult);
        return mav;
    }

    /**
     * 查询 交易总账
     *
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/monitormatchfund/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:matchfund:data")
    public JsonMessage data(MonitorMatchFund entity, Pagination pagin) throws BusinessException {
        PaginateResult<MonitorMatchFund> result = monitorMatchFundService.findMonitorMatchFundList(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * 按现有类别查询所有证券
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/findByCurType", method = RequestMethod.GET)
    public List<StockInfo> findByCur() throws BusinessException
    {
        List<StockInfo> list = monitorMatchFundService.selectByCurType();
        return list;
    }
}
