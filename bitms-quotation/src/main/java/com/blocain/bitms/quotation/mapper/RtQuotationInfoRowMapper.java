package com.blocain.bitms.quotation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blocain.bitms.quotation.entity.RtQuotationInfo;

/**
 * 最新撮合行情映射类
 * <p>File：RtQuotationInfoRowMapper.java</p>
 * <p>Title: RtQuotationInfoRowMapper</p>
 * <p>Description:RtQuotationInfoRowMapper</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class RtQuotationInfoRowMapper implements RowMapper<RtQuotationInfo>
{
    @Override
    public RtQuotationInfo mapRow(ResultSet rs, int i) throws SQLException
    {
        RtQuotationInfo rtQuotationInfo = new RtQuotationInfo();
        rtQuotationInfo.setVcoinAmtSum24h(rs.getBigDecimal("dealamtsum"));
        rtQuotationInfo.setEntrustBuyOne(rs.getBigDecimal("buyone"));
        rtQuotationInfo.setEntrustSellOne(rs.getBigDecimal("sellone"));
        return rtQuotationInfo;
    }
}
