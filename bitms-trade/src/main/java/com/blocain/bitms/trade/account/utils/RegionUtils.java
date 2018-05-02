package com.blocain.bitms.trade.account.utils;

import com.blocain.bitms.boss.common.entity.Region;
import com.blocain.bitms.boss.common.service.RegionService;
import com.blocain.bitms.orm.utils.SpringContext;
import com.blocain.bitms.tools.exception.BusinessException;

import java.util.List;

/**
 * 区域代码工具类 介绍
 * <p>File：RegionUtils.java </p>
 * <p>Title: RegionUtils </p>
 * <p>Description:RegionUtils </p>
 * <p>Copyright: Copyright (c) 2017/7/19 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RegionUtils
{
    static RegionService regionalService = SpringContext.getBean(RegionService.class);
    
    /**
     * 根据区域编码取地区名称
     * @return
     */
    public static List<Region> getRegions()
    {
        List<Region> data = null;
        try
        {
            data = regionalService.selectAll();
        }
        catch (BusinessException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}
