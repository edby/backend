package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.monitor.entity.MonitorMargin;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 杠杆保证金监控 持久层接口
 * <p>File：MonitorMarginMapper.java </p>
 * <p>Title: MonitorMarginMapper </p>
 * <p>Description:MonitorMarginMapper </p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
@MyBatisDao
public interface MonitorMarginMapper extends GenericMapper<MonitorMargin>
{
    List<MonitorMargin> findListByIds(@Param("mapList") List<Map<String, Object>> mapList);
    
    /**
     * 查询保证金情况
     * @param monitorMargin
     * @return
     */
    List<MonitorMargin> findMarginList(MonitorMargin monitorMargin);
    
    /**
     * 查询已经爆仓的保证金记录
     * @return
     */
    List<MonitorMargin> findClosePositionDataList();

    MonitorMargin findRiskInfo();
}
