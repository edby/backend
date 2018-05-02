/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.monitor.entity.MonitorIndex;
import com.blocain.bitms.monitor.entity.MonitorLimitParam;
import com.blocain.bitms.monitor.entity.MonitorParam;
import com.blocain.bitms.monitor.mapper.MonitorConfigMapper;
import com.blocain.bitms.monitor.mapper.MonitorIndexMapper;
import com.blocain.bitms.monitor.mapper.MonitorLimitParamMapper;
import com.blocain.bitms.monitor.mapper.MonitorParamMapper;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MonitorConfig 服务实现类
 * <p>File：MonitorConfigServiceImpl.java </p>
 * <p>Title: MonitorConfigServiceImpl </p>
 * <p>Description:MonitorConfigServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Service
public class MonitorConfigServiceImpl extends GenericServiceImpl<MonitorConfig> implements MonitorConfigService
{
    protected MonitorConfigMapper     monitorConfigMapper;
    
    @Autowired
    protected MonitorParamMapper      monitorParamMapper;
    
    @Autowired
    protected MonitorIndexMapper      monitorIndexMapper;
    
    @Autowired
    protected MonitorLimitParamMapper monitorLimitParamMapper;
    
    @Autowired
    public MonitorConfigServiceImpl(MonitorConfigMapper monitorConfigMapper)
    {
        super(monitorConfigMapper);
        this.monitorConfigMapper = monitorConfigMapper;
    }
    
    /**
     * 为监控服务类设置指标列表属性,可通过指标列表属性的size是否为0判断是否
     * 配置了指标
     */
    @Override
    public List<MonitorConfig> buildConfigList()
    {
        List<MonitorConfig> configList = null;
        configList = monitorConfigMapper.selectAll();
        for (MonitorConfig config : configList)
        {
            // 为每个服务配置类初始化指标列表属性
            HashMap<Long, MonitorIndex> ids = new HashMap<Long, MonitorIndex>();
            if (config.getIdxid1() != null && config.getIdxid1() != 0)
            {
                // 查询指标信息
                MonitorIndex mi = monitorIndexMapper.selectByPrimaryKey(config.getIdxid1());
                if (mi != null)
                {
                    mi = addLimitMap(mi);
                    mi = addActionValueList(mi);
                    ids.put(config.getIdxid1(), mi);
                }
            }
            if (config.getIdxid2() != null && config.getIdxid2() != 0)
            {
                MonitorIndex mi = monitorIndexMapper.selectByPrimaryKey(config.getIdxid2());
                if (mi != null)
                {
                    mi = addLimitMap(mi);
                    mi = addActionValueList(mi);
                    ids.put(config.getIdxid2(), mi);
                }
            }
            if (config.getIdxid3() != null && config.getIdxid3() != 0)
            {
                MonitorIndex mi = monitorIndexMapper.selectByPrimaryKey(config.getIdxid3());
                if (mi != null)
                {
                    mi = addLimitMap(mi);
                    mi = addActionValueList(mi);
                    ids.put(config.getIdxid3(), mi);
                }
            }
            if (config.getIdxid4() != null && config.getIdxid4() != 0)
            {
                MonitorIndex mi = monitorIndexMapper.selectByPrimaryKey(config.getIdxid4());
                if (mi != null)
                {
                    mi = addLimitMap(mi);
                    mi = addActionValueList(mi);
                    ids.put(config.getIdxid4(), mi);
                }
            }
            config.setIdxids(ids);
        }
        return configList;
    }
    
    /**
     * 为每个指标类设置阈值集合
     */
    private MonitorIndex addLimitMap(MonitorIndex mi)
    {
        if (mi != null)
        {
            List<MonitorLimitParam> list = monitorLimitParamMapper.findByIdx(mi.getId());
            if (CollectionUtils.isNotEmpty(list))
            {
                HashMap<Long, MonitorLimitParam> map = new HashMap<>();
                for (MonitorLimitParam param : list)
                {
                    map.put(param.getStockinfoId(), param);
                }
                mi.setLimitParamMap(map);
            }
        }
        return mi;
    }
    
    /**
     * 如果指标内容包含消息提醒，则为指标对象设置相关属性
     */
    public MonitorIndex addActionValueList(MonitorIndex mi)
    {
        if (mi != null)
        {
            if (mi.getActionType() != null && mi.getActionType().contains(MonitorConst.MONITOR_ACTIONTYPE_MESSAGE))
            {
                List<MonitorParam> paramList = new ArrayList<MonitorParam>();
                String[] paramVals = mi.getActionValue().split(",");
                for (int i = 0; i < paramVals.length; i++)
                {
                    String paramVal = paramVals[i];
                    MonitorParam param = monitorParamMapper.selectByPrimaryKey(Long.parseLong(paramVal));
                    if (param != null)
                    {
                        paramList.add(param);
                    }
                }
                mi.setActionValueList(paramList);
            }
        }
        return mi;
    }
    
    @Override
    public List<MonitorConfig> findRelatedList(String id)
    {
        return monitorConfigMapper.findRelatedList(id);
    }

    @Override
    public PaginateResult<MonitorConfig> findJoinList(Pagination pagin, MonitorConfig entity) {
        entity.setPagin(pagin);
        List<MonitorConfig> list = monitorConfigMapper.findJoinList(entity);
        return new PaginateResult<>(pagin, list);
    }
}
