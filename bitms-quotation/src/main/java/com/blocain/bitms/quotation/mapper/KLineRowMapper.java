package com.blocain.bitms.quotation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blocain.bitms.quotation.entity.KLine;

/**
 * K线映射类
 * <p>File：KLineRowMapper.java</p>
 * <p>Title: KLineRowMapper</p>
 * <p>Description:KLineRowMapper</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class KLineRowMapper implements RowMapper<KLine>
{
    @Override
    public KLine mapRow(ResultSet rs, int i) throws SQLException
    {
        KLine kline = new KLine();
        // 每天零点时的displaytime只取日期。
        String a = rs.getString("displayTime");
        if (a.endsWith("00:00"))
        {
            a = a.replace(" 00:00", "");
        }
        else
        {
            a = a.substring(a.indexOf(" ") + 1);
        }
        kline.setA(a);
        kline.setC(rs.getString("closePrice"));
        kline.setB(rs.getString("openPrice"));
        kline.setE(rs.getString("highestPrice"));
        kline.setD(rs.getString("lowestPrice"));
        kline.setF(rs.getString("dealbal"));
        kline.setG(rs.getString("dealamtsum"));
        // kline.setClosePrice(rs.getBigDecimal("closePrice"));
        // kline.setOpenPrice(rs.getBigDecimal("openPrice"));
        // kline.setHighestPrice(rs.getBigDecimal("highestPrice"));
        // kline.setLowestPrice(rs.getBigDecimal("lowestPrice"));
        // kline.setDealBal(rs.getBigDecimal("dealbal"));
        // kline.setDealAmtSum(rs.getBigDecimal("dealamtsum"));
        return kline;
    }
    
    public static void main(String[] args)
    {
        String a = "1-22 09:15";
        System.out.println(a.substring(a.indexOf(" ") + 1));
    }
}
