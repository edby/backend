package com.blocain.bitms.quotation.service;

/**
 * K线图服务接口
 * <p>File：KLineEngineService.java </p>
 * <p>Title: KLineEngineService </p>
 * <p>Description:KLineEngineService </p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
public interface KLineEngineService
{
    /*******************
     * 根据K线主题推送数据
     * @param topic  主题
     */
    void pushKLineData(String topic);

    void buildKLineDate();
}
