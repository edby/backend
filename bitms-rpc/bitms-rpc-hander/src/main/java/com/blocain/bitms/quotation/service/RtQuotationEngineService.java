package com.blocain.bitms.quotation.service;

/**
 * 实时行情引擎服务接口
 * <p>File：RtQuotationEngineService.java </p>
 * <p>Title: RtQuotationEngineService </p>
 * <p>Description:RtQuotationEngineService </p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
public interface RtQuotationEngineService
{
    //实时行情数据推送
    void pushRtQuotationInfoData(String topic);

}
