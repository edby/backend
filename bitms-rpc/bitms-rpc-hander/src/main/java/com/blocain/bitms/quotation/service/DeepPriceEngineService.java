package com.blocain.bitms.quotation.service;

import com.blocain.bitms.orm.annotation.SlaveDataSource;

/**
 * 委托盘口深度行情服务接口
 * <p>File：DeepPriceEngineService.java </p>
 * <p>Title: DeepPriceEngineService </p>
 * <p>Description:DeepPriceEngineService </p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
public interface DeepPriceEngineService
{
    /**
     * 推送委托盘口深度行情数据
     *
     * @param topic   主题
     */
    @SlaveDataSource()
    void pushDeepPriceData(String topic);
}
