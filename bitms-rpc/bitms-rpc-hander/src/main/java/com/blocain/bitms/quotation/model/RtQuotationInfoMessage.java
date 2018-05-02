package com.blocain.bitms.quotation.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blocain.bitms.quotation.consts.InQuotationConsts;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.entity.RtQuotationInfoDto;
import com.google.common.collect.Lists;

/**
 * RtQuotationInfoMessage Introduce
 * <p>File：RtQuotationInfoMessage.java</p>
 * <p>Title: RtQuotationInfoMessage</p>
 * <p>Description: RtQuotationInfoMessage</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class RtQuotationInfoMessage extends BaseMessage
{
    private  List msgContent = Lists.newArrayList();

    public RtQuotationInfoMessage()
    {
        super(InQuotationConsts.MESSAGE_TYPE_RTQUOTATION);
    }

    public String getMsgInfo(RtQuotationInfo price)
    {
        RtQuotationInfoDto priceDto = convertObject(price);
        this.msgContent.add(priceDto);
        return JSON.toJSONString(this,SerializerFeature.WriteNonStringValueAsString);
    }

    public List getMsgContent()
    {
        return msgContent;
    }

    public void setMsgContent(List msgContent)
    {
        this.msgContent = msgContent;
    }

    private RtQuotationInfoDto convertObject(RtQuotationInfo price)
    {
        RtQuotationInfoDto priceDto = new RtQuotationInfoDto();
        BigDecimal platPrice = price.getPlatPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getPlatPrice();
        BigDecimal idxPrice = price.getIdxPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getIdxPrice();
        BigDecimal idxAvgPrice = price.getIdxAvgPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getIdxAvgPrice();
        BigDecimal premium = price.getPremium().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getPremium();
        BigDecimal premiumRate = price.getPremiumRate().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getPremiumRate();
        BigDecimal entrustSellOne = price.getEntrustSellOne().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getEntrustSellOne();
        BigDecimal entrustBuyOne = price.getEntrustBuyOne().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getEntrustBuyOne();
        BigDecimal buyHighestLimitPrice = price.getBuyHighestLimitPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getBuyHighestLimitPrice();
        BigDecimal buyLowestLimitPrice = price.getBuyLowestLimitPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getBuyLowestLimitPrice();
        BigDecimal sellHighestLimitPrice = price.getSellHighestLimitPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getSellHighestLimitPrice();
        BigDecimal sellLowestLimitPrice = price.getSellLowestLimitPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getSellLowestLimitPrice();
        BigDecimal vcoinAmtSum24h = price.getVcoinAmtSum24h().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getVcoinAmtSum24h();
        BigDecimal highestPrice = price.getHighestPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getHighestPrice();
        BigDecimal lowestPrice = price.getLowestPrice().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getLowestPrice();
        BigDecimal dealAmt = price.getDealAmt().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getDealAmt();
        BigDecimal dealBalance = price.getDealBalance().compareTo(BigDecimal.ZERO) == 0? BigDecimal.ZERO : price.getDealBalance();


        priceDto.setPlatPrice(platPrice.toPlainString());
        priceDto.setIdxPrice(idxPrice.toPlainString());
        priceDto.setIdxAvgPrice(idxAvgPrice.toPlainString());
        priceDto.setPremium(premium.toPlainString());
        priceDto.setPremiumRate(premiumRate.toPlainString());
        priceDto.setEntrustSellOne(entrustSellOne.toPlainString());
        priceDto.setEntrustBuyOne(entrustBuyOne.toPlainString());
        priceDto.setBuyHighestLimitPrice(buyHighestLimitPrice.toPlainString());
        priceDto.setBuyLowestLimitPrice(buyLowestLimitPrice.toPlainString());
        priceDto.setSellHighestLimitPrice(sellHighestLimitPrice.toPlainString());
        priceDto.setSellLowestLimitPrice(sellLowestLimitPrice.toPlainString());
        priceDto.setRange(price.getRange());
        priceDto.setDirect(price.getDirect());
        priceDto.setUpDown(price.getUpDown());
        priceDto.setUpDownIdx(price.getUpDownIdx());
        priceDto.setVcoinAmtSum24h(vcoinAmtSum24h.toPlainString());
        priceDto.setHighestPrice(highestPrice.toPlainString());
        priceDto.setLowestPrice(lowestPrice.toPlainString());
        priceDto.setQuotationTime(price.getQuotationTime());
        priceDto.setDealAmt(dealAmt.toPlainString());
        priceDto.setDealBalance(dealBalance.toPlainString());

        return priceDto;
    }
}
