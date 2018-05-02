package com.blocain.bitms.quotation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.quotation.config.InQuotationConfig;
import com.blocain.bitms.quotation.entity.MainRtQuotationInfo;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.model.AllRtQuotationInfoMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

@Service
public class AllRtQuotationEngineServiceImpl implements AllRtQuotationEngineService
{
    @Autowired
    private StockInfoService stockInfoService;
    
    @Override
    public void pushAllRtQuotationData()
    {
        List<MainRtQuotationInfo> quotationList = getQuotationListFromRedis();
        // 未取到行情，直接返回
        if (CollectionUtils.isEmpty(quotationList)) return;
        Collections.sort(quotationList);
        AllRtQuotationInfoMessage transactionMessage = new AllRtQuotationInfoMessage();
        String content = transactionMessage.getMsgInfo(quotationList);
        String topic = InQuotationConfig.TOPIC_ALLQUOTATION;
        DataPushUtil.doDataPush(content, topic);
    }
    
    /**
     *  从redis获取行情（并转换格式）
     */
    private List<MainRtQuotationInfo> getQuotationListFromRedis()
    {
        List<MainRtQuotationInfo> quotationList = new ArrayList<MainRtQuotationInfo>();
        String[] stocks = InQuotationConfig.QUOTATION_STOCKS.split(",");
        for (int i = 0; i < stocks.length; i++)
        {
            // 1.首先从缓存中获取交易对对应行情
            Long id = Long.parseLong(stocks[i]);
            String quotationKey = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RTQUOTATIONINFO)
                    .append(BitmsConst.SEPARATOR).append(stocks[i]).toString();
            RtQuotationInfo rtQuotation = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
            // 2.将行情信息转化为指定格式的行情信息
            // 当不存在缓存行情时，则不会发送该币对消息，此时前端也将不会显示该币对信息
            // 如果要调整发送策略，则应该在此处调整，例如在rtQuotation==null时给该币对赋默认值
            if (rtQuotation == null)
            {
                continue;
            }
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(id);
            if (stockInfo == null || "no".equalsIgnoreCase(stockInfo.getCanTrade()) || "no".equalsIgnoreCase(stockInfo.getIsActive())) continue;
            MainRtQuotationInfo mainRtQuotation = new MainRtQuotationInfo();
            mainRtQuotation.setId(stocks[i]);
            mainRtQuotation.setStockType(stockInfo.getStockType());
            mainRtQuotation.setStockName(stockInfo.getStockName());
            mainRtQuotation.setExchangePair(stockInfo.getRemark());
            mainRtQuotation.setPlatPrice(rtQuotation.getPlatPrice().toPlainString());
            mainRtQuotation.setRange(String.valueOf(rtQuotation.getRange()));
            mainRtQuotation.setUpDown(rtQuotation.getUpDown());
            mainRtQuotation.setVolume(rtQuotation.getVcoinAmtSum24h().toPlainString());
            mainRtQuotation.setLowestPrice(rtQuotation.getLowestPrice().toPlainString());
            mainRtQuotation.setHighestPrice(rtQuotation.getHighestPrice().toPlainString());
            mainRtQuotation.setMarket(stockInfo.getCapitalStockinfoId().toString());
            mainRtQuotation.setTokencontactaddr(stockInfo.getTokenContactAddr());
            quotationList.add(mainRtQuotation);
        }
        return quotationList;
    }
}
