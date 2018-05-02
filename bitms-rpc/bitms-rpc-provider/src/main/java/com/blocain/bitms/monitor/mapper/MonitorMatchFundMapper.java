package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorMatchFund;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;

import java.util.List;

/**
 * 交易总账监控表 持久层接口
 * <p>File：MonitorMatchFundMapper.java </p>
 * <p>Title: MonitorMatchFundMapper </p>
 * <p>Description:MonitorMatchFundMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorMatchFundMapper extends GenericMapper<MonitorMatchFund> {

    /**
     * 交易总账监控列表
     *
     * @param monitorMatchFund
     * @return
     */
    List<MonitorMatchFund> findMonitorMatchFundList(MonitorMatchFund monitorMatchFund);

    List<StockInfo> selectByCurType();

    MonitorMatchFund findRiskInfo();
}
