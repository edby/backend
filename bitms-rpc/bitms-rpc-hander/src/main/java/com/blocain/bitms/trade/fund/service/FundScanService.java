package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.model.FundChangeModel;

/**
 * 资产变动扫描服务接口
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
     * 资产变动执行扫描
     * @return
     * @throws BusinessException
     */
    void fundChangeScan() throws BusinessException;

    /**
     * 准备账户指标基本数据
     * @param accountId
     * @param exchangePairVCoin
     * @param exchangePairMoney
     * @return
     */
    FundChangeModel setAccountAssetAttr(Long accountId, Long exchangePairVCoin, Long exchangePairMoney) throws BusinessException;
}
