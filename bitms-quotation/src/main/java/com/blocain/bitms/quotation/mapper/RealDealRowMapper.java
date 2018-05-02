package com.blocain.bitms.quotation.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.trade.trade.entity.RealDealVCoinMoney;

/**
 * 成交表映射类
 */
public class RealDealRowMapper implements RowMapper<RealDealVCoinMoney>
{
    @Override
    public RealDealVCoinMoney mapRow(ResultSet resultSet, int i) throws SQLException
    {
        RealDealVCoinMoney realDeal = new RealDealVCoinMoney();
        realDeal.setId(resultSet.getLong("id"));
        realDeal.setDealTime(resultSet.getTimestamp("dealTime"));
        realDeal.setDealPrice(resultSet.getBigDecimal("dealPrice").setScale(InQuotationConfig.QUOTATION_PRICE_DIGIT, BigDecimal.ROUND_HALF_UP));
        realDeal.setDealAmt(resultSet.getBigDecimal("dealAmt").setScale(InQuotationConfig.QUOTATION_AMT_DIGIT, BigDecimal.ROUND_HALF_UP));
        realDeal.setDealBalance(resultSet.getBigDecimal("dealBalance").setScale(InQuotationConfig.QUOTATION_ACCUMULATEBAL_DIGIT, BigDecimal.ROUND_HALF_UP));
        return realDeal;
    }
}
