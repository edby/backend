package com.blocain.bitms.archive.service;

/**
 * 归档服务
 * ArchiveService Introduce
 * <p>File：ArchiveService.java</p>
 * <p>Title: ArchiveService</p>
 * <p>Description: ArchiveService</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public interface ArchiveService
{

    /**
     * K线数据归档
     */
    void dealQuotationKlineArchive();
    /**
     * 外部指数行情归档
     */
    void dealQuotationArchive();

    /**
     * 资金流水归档
     */
    void dealFundcurrentArchive();

    /**
     * 交易相关流水归档
     */
    void dealTradeArchive();
}
