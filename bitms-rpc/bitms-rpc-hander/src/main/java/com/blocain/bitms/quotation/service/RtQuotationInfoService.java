package com.blocain.bitms.quotation.service;

import com.blocain.bitms.quotation.entity.RtQuotationInfo;

import java.util.List;

/**
 * 最新实时行情服务接口
 * <p>File：RtQuotationInfoService.java </p>
 * <p>Title: MatchPriceService </p>
 * <p>Description:MatchPriceService </p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 * @author Jiangsc
 * @version 1.0
 */
public interface RtQuotationInfoService
{

    /**
     * 从缓存获取最新实时行情对象
     * @param baseCur                本币
     * @param bizCategoryId          业务品种ID
     * @return
     */
    RtQuotationInfo queryRtQuotationInfoFromCache(Long baseCur, Long bizCategoryId);

    /**
     * 获取最新实时行情对象
     * @param baseCur                本币
     * @param bizCategoryId          业务品种ID
     * @return
     */
    RtQuotationInfo queryRtQuotationInfo(Long baseCur,Long bizCategoryId);

}
