package com.blocain.bitms.quotation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blocain.bitms.quotation.entity.KLineEntity;

/**
 * K线映射类
 * <p>File：KLineRowMapper.java</p>
 * <p>Title: KLineRowMapper</p>
 * <p>Description:KLineRowMapper</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class KLineEntityRowMapper implements RowMapper<KLineEntity>
{
    @Override
    public KLineEntity mapRow(ResultSet rs, int i) throws SQLException
    {
        KLineEntity kline = new KLineEntity();
        kline.setId(rs.getLong("closeid"));
        kline.setQuotationTime(rs.getTimestamp("quotationTime"));
        kline.setDisplayTime(rs.getTimestamp("displayTime"));
        kline.setClosePrice(rs.getBigDecimal("closePrice"));
        kline.setOpenPrice(rs.getBigDecimal("openPrice"));
        kline.setHighestPrice(rs.getBigDecimal("highestPrice"));
        kline.setLowestPrice(rs.getBigDecimal("lowestPrice"));
        kline.setDealBal(rs.getBigDecimal("dealbal"));
        kline.setDealAmtSum(rs.getBigDecimal("dealamtsum"));
        return kline;
    }
}
