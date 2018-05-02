package com.blocain.bitms.platscan.thread;

import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自动平台还款服务线程
 * DebitAssetThread Introduce
 * <p>File：DebitAssetThread.java</p>
 * <p>Title: DebitAssetThread</p>
 * <p>Description: DebitAssetThread</p>
 * <p>Copyright: Copyright (c) 2017/9/19</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
@Component
public class DebitAssetThread implements Runnable
{
    private static Logger   logger    = LoggerFactory.getLogger(DebitAssetThread.class);

    @Autowired
    private StockInfoService         stockInfoService;

    @Autowired
    private AccountDebitAssetService accountDebitAssetService;

    private boolean             isRunning = true;

    @Override
    public void run()
    {
        // 可以借款的品种
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES); // 是交易对
        stockInfoSelect.setCanBorrow(FundConsts.PUBLIC_STATUS_YES); // 可以借款
        List<StockInfo> stockInfoLists = stockInfoService.findList(stockInfoSelect);
        while (isRunning)
        {
            LoggerUtils.logInfo(logger, "开始定时向平台自动还款轮询任务");
            try
            {
                if(stockInfoLists.size() > 0){
                    for(int i=0; i<stockInfoLists.size(); i++ ){
                        StockInfo stockInfo = stockInfoLists.get(i);
                        accountDebitAssetService.autoDebitRepaymentToPlat(stockInfo);
                    }
                }
                Thread.sleep(3000);
            }
            catch (Exception e)
            {
                LoggerUtils.logError(logger, "定时向平台自动还款轮询任务失败：{}", e.getLocalizedMessage());
            }
            LoggerUtils.logInfo(logger, "结束定时向平台自动还款轮询任务");
        }
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
    
    public void setRunning(boolean running)
    {
        this.isRunning = running;
    }
}
