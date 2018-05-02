package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorMargin;
import com.blocain.bitms.monitor.mapper.MonitorMarginMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 杠杆保证金监控服务
 * MonitorMarginServiceImpl Introduce
 * <p>File：MonitorMarginServiceImpl.java</p>
 * <p>Title: MonitorMarginServiceImpl</p>
 * <p>Description: MonitorMarginServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Service
public class MonitorMarginServiceImpl extends GenericServiceImpl<MonitorMargin> implements MonitorMarginService
{
    private MonitorMarginMapper monitorMarginMapper;
    
    @Autowired
    public MonitorMarginServiceImpl(MonitorMarginMapper monitorMarginMapper)
    {
        super(monitorMarginMapper);
        this.monitorMarginMapper = monitorMarginMapper;
    }
    
    /**
     * 执行杠杆保证金监控
    
    @Override
    public void dealMarginMonitor()
    {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(FundConsts.WALLET_BTC2USDX_TYPE,FundConsts.WALLET_BTC2USDX_TYPE);
        if (rtQuotationInfo != null)
        {
            logger.info("平台行情:" + rtQuotationInfo.getPlatPrice());
            // 1. 调用存储过程生成对账结果
            resultMap.put("quotation", rtQuotationInfo.getPlatPrice());
            monitorMarginMapper.dealMarginMonitor(resultMap);
        }
    }*/
    @Override
    public List<MonitorMargin> findListByIds(String ids, String targetStockinfoIds,String capitalStockinfoIds)
    {
        String idss[] = ids.split(",");
        String targetStockinfoId[] = targetStockinfoIds.split(",");
        String capitalStockinfoId[] = capitalStockinfoIds.split(",");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < idss.length; i++)
        {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("accountId", Long.parseLong(idss[i]));
            obj.put("targetStockinfoId", Long.parseLong(targetStockinfoId[i]));
            obj.put("capitalStockinfoId", Long.parseLong(capitalStockinfoId[i]));
            list.add(obj);
        }
        return monitorMarginMapper.findListByIds(list);
    }
    
    @Override
    public PaginateResult<MonitorMargin> findMarginList(Pagination pagination, MonitorMargin monitorMargin)
    {
        monitorMargin.setPagin(pagination);
        List<MonitorMargin> fundCurrentList = monitorMarginMapper.findMarginList(monitorMargin);
        return new PaginateResult<>(pagination, fundCurrentList);
    }

    @Override
    public List<MonitorMargin> findClosePositionDataList()
    {
        return monitorMarginMapper.findClosePositionDataList();
    }

    @Override
    public MonitorMargin findRiskInfo() {
        return monitorMarginMapper.findRiskInfo();
    }
}
