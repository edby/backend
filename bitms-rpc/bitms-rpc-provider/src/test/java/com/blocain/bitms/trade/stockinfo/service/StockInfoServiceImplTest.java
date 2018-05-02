package com.blocain.bitms.trade.stockinfo.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by admin on 2017/12/4.
 */
public class StockInfoServiceImplTest extends AbstractBaseTest
{
    @Autowired
    StockInfoService stockInfoService;
    
    @Test
    public void findList() throws Exception
    {
//        StockInfo entity = new StockInfo();
//        List<StockInfo> list = stockInfoService.findList(entity);
//        for(StockInfo stockInfo:list)
//        {
//            stockInfo.setTradeDebitTotal(BigDecimal.valueOf(10000000000L));
//            stockInfo.setCapitalDebitTotal(BigDecimal.valueOf(10000000000L));
//            stockInfoService.save(stockInfo);
//        }
//        JsonMessage msg = new JsonMessage(CommonEnums.SUCCESS);
//        msg.setRows(list);
//        System.out.println(msg);

//        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_ETH_TYPE);
//        System.out.println(stockInfo.toString());

        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark("eth");
        stockInfo.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        System.out.println(stockInfoList.get(0).toString());
    }
}