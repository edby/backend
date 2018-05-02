package com.blocain.bitms.platscan.service;

import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 账户资产变动扫描服务接口
 * <p>File：FundScanService.java </p>
 * <p>Title: FundScanService </p>
 * <p>Description:FundScanService </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author jiangsc
 * @version 1.0
 */
public interface FundScanService
{
    /**
     * 账户资产变动扫描初始化
     * @return
     * @throws BusinessException
     */
    void initialize() throws BusinessException;
    
    /**
     * 定时账户资产变动执行扫描
     * @return
     * @throws BusinessException
     */
    void fundChangeScan() throws BusinessException;
}
