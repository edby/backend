package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorMatchFund;
import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;

import java.util.List;

public interface MonitorMatchFundService extends GenericService<MonitorMatchFund> {

    // 查询交易总账监控记录
    PaginateResult<MonitorMatchFund> findMonitorMatchFundList(Pagination pagin, MonitorMatchFund monitorMatchFund);

    List<StockInfo> selectByCurType();

    MonitorMatchFund findRiskInfo();

    /**
     * 检查杠杆现货总账是否对平
     * @param bizs  业务品种，支持多个业务品种检查
     * @return 业务品种检查结果列表，核对每个业务品种的chkResult，为-1 该业务总账不平
     */
    //List<MonitorMatchFund> checkLeveragedSpotFundCur(String bizs);
}
