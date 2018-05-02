package com.blocain.bitms.quotation.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blocain.bitms.quotation.entity.DeepPrice;

/**
 * 委托流水映射类
 * <p>File：DeepPriceRowMapper.java</p>
 * <p>Title: DeepPriceRowMapper</p>
 * <p>Description:DeepPriceRowMapper</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class DeepPriceRowMapper implements RowMapper<DeepPrice>
{
    @Override
    public DeepPrice mapRow(ResultSet rs, int i) throws SQLException
    {
        DeepPrice deepPrice = new DeepPrice();
        double ratio = 0;
        BigDecimal entTotalBal = rs.getBigDecimal("enttotalbal");
        BigDecimal entBal = rs.getBigDecimal("entrustBal");
        if (entTotalBal.compareTo(BigDecimal.ZERO) == 1)
            ratio = entBal.divide(entTotalBal,4).doubleValue();
        StringBuilder desc = new StringBuilder();
        desc.append(rs.getInt("rowno"));
        deepPrice.setDesc(desc.toString());
        deepPrice.setEntrustAccountType(rs.getInt("entrustaccounttype"));
        deepPrice.setDeepLevel(rs.getInt("deepLevel"));
        deepPrice.setDirect(rs.getString("entrustdirect"));
        deepPrice.setEntrustAmt(rs.getBigDecimal("entrustAmt").toPlainString());
        deepPrice.setEntrustAmtSum(rs.getBigDecimal("entrustBal").toPlainString());
        deepPrice.setEntrustPrice(rs.getBigDecimal("entrustprice").toPlainString());
        deepPrice.setEntBalRatio(ratio*100);
        return deepPrice;
    }
}
