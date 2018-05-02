package com.blocain.bitms.quotation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blocain.bitms.quotation.entity.Quotation;

public class QuotationRowMapper implements RowMapper<Quotation>
{
    @Override
    public Quotation mapRow(ResultSet rs, int i) throws SQLException
    {
        Quotation quotation = new Quotation();
        quotation.setId(rs.getLong("id"));
        quotation.setChannel(rs.getString("channel"));
        quotation.setStockId(rs.getLong("stockid"));
        quotation.setIdxPrice(rs.getBigDecimal("idxPrice"));
        quotation.setIdxPriceAvg(rs.getBigDecimal("idxPriceAvg"));
        return quotation;
    }
}
