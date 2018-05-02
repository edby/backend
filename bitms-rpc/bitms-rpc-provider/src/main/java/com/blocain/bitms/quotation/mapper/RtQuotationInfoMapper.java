/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.quotation.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 最新实时行情信息 持久层接口
 * <p>File：RtQuotationInfoMapper.java </p>
 * <p>Title: RtQuotationInfoMapper </p>
 * <p>Description:RtQuotationInfoMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface RtQuotationInfoMapper
{
    //获取行情额外信息
    RtQuotationInfo getQuotationExtraInfo(@Param("tblEntrustName") String tblEntrustName,@Param("tblKlineName") String tblKlineName);
}
