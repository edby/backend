package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.RealDealVCoinMoneyService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class FundScanServiceImpTest extends AbstractBaseTest
{

    // 账户信息KEY: changescan_fundCurrent_[acctid]
    public static final  String    keyPrefix        = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_CURRENT)
            .append(BitmsConst.SEPARATOR).toString();
    private static final     Logger    logger           = LoggerFactory.getLogger(FundScanServiceImpl.class);


    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false) RtQuotationInfoService rtQuotationInfoService;

    @Autowired
    FundScanService                                     fundScanService;

    @Test
    public void fundChangeScan()
    {
        fundScanService.fundChangeScan();
    }

    @Test
    public void testChangescanFundCurrent()
    {
        String key = new StringBuilder(keyPrefix).append("17051655480545280|177777777702").toString();

        Object changescanFundCurrent =  RedisUtils.getObject(key);
        System.out.print(changescanFundCurrent.toString());
    }


    @Test
    public void testSetAccountContractFundAssetAttr()
    {
        // FundChangeModel setAccountAssetAttr(Long accountId, Long exchangePairVCoin, Long exchangePairMoney);
        fundScanService.setAccountAssetAttr(300000060002L,133333333302l, 133333333302l);
    }

    @Test
    public void RongDuan()
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_BTC2USD_TYPE);
        StockInfo stockInfo1 = new StockInfo();
        stockInfo1.setId(stockInfo.getId());
        logger.debug("进入：多空杠杆自动阈值开关，相对风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆，负溢价超出-5%关闭空头杠杆。");
        RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(stockInfo.getTradeStockinfoId(), stockInfo.getId());
        logger.debug(rtQuotationInfo.toString());
        BigDecimal idxAvgPrice = BigDecimal.ZERO;
        BigDecimal platPrice = BigDecimal.ZERO;
        if (rtQuotationInfo != null)
        {
            idxAvgPrice = rtQuotationInfo.getIdxAvgPrice();
            platPrice = rtQuotationInfo.getPlatPrice();
            if (idxAvgPrice == null || idxAvgPrice.compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(10000001, "行情异常！"); }
            if (platPrice == null || platPrice.compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(10000001, "行情异常！"); }
            logger.debug("多空杠杆自动阈值开关，相对风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆，负溢价超出-5%关闭空头杠杆。");
            logger.debug("指数均价："+idxAvgPrice);
            logger.debug("平台价格："+platPrice);
            logger.debug("高于 关闭多头杠杆："+stockInfo.getMaxLongFuse());
            logger.debug("负溢价超出 关闭空头杠杆："+stockInfo.getMaxShortFuse());

            boolean key = false;

            if(platPrice.compareTo(idxAvgPrice)>0
                    && (platPrice.divide(idxAvgPrice,8,BigDecimal.ROUND_HALF_UP)).compareTo((BigDecimal.ONE.add(stockInfo.getMaxLongFuse())))>0)
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                {
                    stockInfo1.setMaxLongLeverSwitch(FundConsts.PUBLIC_STATUS_NO);
                    logger.debug("风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆");
                    key = true;
                }
            }
            else
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(),FundConsts.PUBLIC_STATUS_NO))
                {
                    stockInfo1.setMaxLongLeverSwitch(FundConsts.PUBLIC_STATUS_YES);
                    logger.debug("多头杠杆解除关闭");
                    key = true;
                }
            }

            if(platPrice.compareTo(idxAvgPrice)<0
                    && (platPrice.divide(idxAvgPrice,8,BigDecimal.ROUND_HALF_UP)).compareTo((BigDecimal.ONE.subtract(stockInfo.getMaxShortFuse())))<0)
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(),FundConsts.PUBLIC_STATUS_YES))
                {
                    stockInfo1.setMaxShortLeverSwitch(FundConsts.PUBLIC_STATUS_NO);
                    logger.debug("风控基价的±溢价百分比关闭，负溢价超出-5%关闭空头杠杆");
                    key = true;
                }
            }
            else
            {
                if(StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(),FundConsts.PUBLIC_STATUS_NO))
                {
                    stockInfo1.setMaxShortLeverSwitch(FundConsts.PUBLIC_STATUS_YES);
                    logger.debug("空头杠杆解除关闭");
                    key = true;
                }
            }
            if(key)
            {
                stockInfoService.updateByPrimaryKeySelective(stockInfo1);
            }
            logger.debug("完成：多空杠杆自动阈值开关，相对风控基价的±溢价百分比关闭，溢价高于5%关闭多头杠杆，负溢价超出-5%关闭空头杠杆。");
        }
        else
        {
            throw new BusinessException("行情异常！");
        }
    }
}
