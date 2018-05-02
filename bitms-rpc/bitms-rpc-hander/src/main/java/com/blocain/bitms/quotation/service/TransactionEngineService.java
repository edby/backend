package com.blocain.bitms.quotation.service;

import com.blocain.bitms.orm.annotation.SlaveDataSource;

/**
 * 成交流水服务接口
 * <p>File：TransactionEngineService.java </p>
 * <p>Title: TransactionEngineService </p>
 * <p>Description:TransactionEngineService </p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
public interface TransactionEngineService
{
    /**
     * 推送交易数据
     * @param pageSize  每次推送交易数据记录数(最少6条，最多50，默认为6)
     */
    @SlaveDataSource()
    void pushTransactionData(String topic, Integer pageSize);
}
