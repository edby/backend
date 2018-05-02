package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.config.MonitorConfig;
import com.blocain.bitms.monitor.entity.MonitorMatchFund;
import com.blocain.bitms.monitor.mapper.MonitorEngineMapper;
import com.blocain.bitms.monitor.mapper.MonitorMatchFundMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 交易总账监控表 服务实现类
 * <p>File：MonitorMatchFundServiceImpl.java </p>
 * <p>Title: MonitorMatchFundServiceImpl </p>
 * <p>Description:MonitorMatchFundServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorMatchFundServiceImpl extends GenericServiceImpl<MonitorMatchFund> implements MonitorMatchFundService {
    protected MonitorMatchFundMapper monitorMatchFundMapper;

    @Autowired
    private   MonitorEngineMapper    monitorEngineMapper;

    @Autowired
    public MonitorMatchFundServiceImpl(MonitorMatchFundMapper monitorMatchFundMapper) {
        super(monitorMatchFundMapper);
        this.monitorMatchFundMapper = monitorMatchFundMapper;
    }

    @Override
    public PaginateResult<MonitorMatchFund> findMonitorMatchFundList(Pagination pagin, MonitorMatchFund monitorMatchFund) {
        monitorMatchFund.setPagin(pagin);
        List<MonitorMatchFund> list = monitorMatchFundMapper.findMonitorMatchFundList(monitorMatchFund);
        return new PaginateResult<>(pagin, list);
    }

    @Override
    public List<StockInfo> selectByCurType() {
        List<StockInfo> list = monitorMatchFundMapper.selectByCurType();
        return list;
    }

    @Override
    public MonitorMatchFund findRiskInfo() {
        return monitorMatchFundMapper.findRiskInfo();
    }

//    @Override public List<MonitorMatchFund> checkLeveragedSpotFundCur(String bizs) throws BusinessException
//    {
//        HashMap<String, Object> resultMap = new HashMap<String, Object>();
//        if (StringUtils.isNotEmpty(bizs))
//        {
//            resultMap.put("bizids", MonitorConfig.BIZ_IDS);
//            monitorEngineMapper.dealMatchFundCurMonitor(resultMap);
//        }
//    }

//    private List<MonitorMatchFund> dealMonitorResult(String bizs,HashMap<String, Object> resultMap) throws BusinessException
//    {
//
//    }
}