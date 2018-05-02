/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.quotation.entity.Quotation;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 行情信息 服务接口
 * <p>File：QuotationService.java </p>
 * <p>Title: QuotationService </p>
 * <p>Description:QuotationService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface QuotationService extends GenericService<Quotation>
{

    /**
     * 取最新的行情
     * @param quotation
     * @return
     * @throws BusinessException
     */
    Quotation findQuotationByLastTime(Quotation quotation) throws BusinessException;

}
