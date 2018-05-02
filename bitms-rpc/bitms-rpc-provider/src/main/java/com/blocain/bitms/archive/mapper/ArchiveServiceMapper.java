package com.blocain.bitms.archive.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;

import java.util.HashMap;

/**
 * 归档服务接口
 * <p>File：ArchiveServiceMapper.java </p>
 * <p>Title: ArchiveServiceMapper </p>
 * <p>Description:ArchiveServiceMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface ArchiveServiceMapper
{

    /**
     * K线数据归档
     */
    void dealQuotationKlineArchive(HashMap<String, Object> paramMap);

    /**
     * 外部指数行情归档
     */
    void dealQuotationArchive(HashMap<String, Object> paramMap);

    /**
     * 资金流水归档
     */
    void dealFundcurrentArchive(HashMap<String, Object> paramMap);

    /**
     * 交易相关流水归档
     */
    void dealTradeArchive(HashMap<String, Object> paramMap);
}
