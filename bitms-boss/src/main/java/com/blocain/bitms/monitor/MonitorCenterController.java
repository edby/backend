package com.blocain.bitms.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.monitor.entity.*;
import com.blocain.bitms.monitor.service.*;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(BitmsConst.MONITOR)
public class MonitorCenterController extends GenericController
{
    @Autowired(required = false)
    private MonitorMarginService      monitorMarginService;
    
    @Autowired(required = false)
    private MonitorPlatBalService     monitorPlatBalService;
    
    @Autowired(required = false)
    private MonitorMatchFundService   monitorMatchFundService;
    
    @Autowired(required = false)
    private MonitorAcctFundCurService monitorAcctFundCurService;
    
    @Autowired(required = false)
    private MonitorERC20BalService    monitorERC20BalService;
    
    @Autowired(required = false)
    private MonitorBlockNumService    monitorBlockNumService;
    
    @RequestMapping(value = "/center")
    @RequiresPermissions("monitor:setting:center:index")
    public ModelAndView list() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("monitor/monitor/monitorcenter");
        return mav;
    }
    
    @ResponseBody
    @RequestMapping(value = "/monitorcenter/data", method = RequestMethod.POST)
    @RequiresPermissions("monitor:setting:center:data")
    public JsonMessage data() throws BusinessException
    {
        // 内部总账
        MonitorMatchFund matchFund = monitorMatchFundService.findRiskInfo();
        matchFund.setChkStatus(matchFund.getAbNormalCount() != 0 ? -1 : 1);
        // 内外部对账
        MonitorPlatBal platBal = monitorPlatBalService.findRiskInfo();
        platBal.setChkStatus(platBal.getAbNormalCount() != 0 ? -1 : 1);
        // 账户资产
        MonitorAcctFundCur acctFundCur = monitorAcctFundCurService.findRiskInfo();
        acctFundCur.setChkStatus(acctFundCur.getAbNormalCount() != 0 ? -1 : 1);
        // 保证金
        MonitorMargin margin = monitorMarginService.findRiskInfo();
        margin.setBullStatus(margin.getBullAbNormalCount() != 0 ? -1 : 1);
        margin.setBearStatus(margin.getBearAbNormalCount() != 0 ? -1 : 1);
        // erc20内外部总额
        MonitorERC20Bal ERC20Bal = monitorERC20BalService.findRiskInfo();
        ERC20Bal.setChkStatus(ERC20Bal.getAbNormalCount() != 0 ? -1 : 1);
        // 内外部区块高度
        MonitorBlockNum blockNum = monitorBlockNumService.findRiskInfo();
        blockNum.setChkStatus(blockNum.getAbNormalCount() != 0 ? -1 : 1);
        JSONObject json = new JSONObject();
        json.put("matchFund", matchFund);
        json.put("platBal", platBal);
        json.put("acctFundCur", acctFundCur);
        json.put("margin", margin);
        json.put("ERC20Bal", ERC20Bal);
        json.put("blockNum", blockNum);
        return getJsonMessage(CommonEnums.SUCCESS, json);
    }
}
