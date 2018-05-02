package com.blocain.bitms.quotation.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.entity.Transaction;

/**
 * 成交流水映射类
 * <p>File：TransactionRowMapper.java</p>
 * <p>Title: TransactionRowMapper</p>
 * <p>Description:TransactionRowMapper</p>
 * <p>Copyright: Copyright (c) 2017年9月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class TransactionRowMapper implements RowMapper<Transaction>
{
    @Override
    public Transaction mapRow(ResultSet rs, int i) throws SQLException
    {
        Transaction transaction = new Transaction();
        transaction.setDealTime(rs.getTimestamp("dealTime"));
        transaction.setDealAmt(rs.getBigDecimal("dealAmt").setScale(InQuotationConfig.QUOTATION_AMT_DIGIT, BigDecimal.ROUND_HALF_UP).toPlainString());
        transaction.setDealPrice(rs.getBigDecimal("dealPrice").setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP).toPlainString());
        transaction.setDirect(rs.getString("activeDealDirect"));
        return transaction;
    }
}
