package com.blocain.bitms.platscan.thread.quotation;

import com.blocain.bitms.quotation.model.QuotationParam;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.PropertiesUtils;

import java.util.List;

/**
 * 基础汇率对象 Introduce
 * <p>File：AbstractRates.java</p>
 * <p>Title: AbstractRates</p>
 * <p>Description: AbstractRates</p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public abstract class AbstractQuotation
{
    public static final PropertiesUtils properties = new PropertiesUtils("quotation.properties");
    
    /**
    * 获取行情
    * @return {@link List<   QuotationParam   >}
    * @throws BusinessException
    */
    public abstract List<QuotationParam> getQuotation() throws BusinessException;
}
