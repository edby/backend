/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.quotation.entity.Quotation;

import java.util.List;

/**
 * 外部行情信息 持久层接口
 * <p>File：QuotationMapper.java </p>
 * <p>Title: QuotationMapper </p>
 * <p>Description:QuotationMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface QuotationMapper extends GenericMapper<Quotation>
{

    /**
     * 取最新外部指数行情价
     * @param quotationParam
     * @return
     */
    List<Quotation> findQuotationByLastTime(Quotation quotationParam);
}
