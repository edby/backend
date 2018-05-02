package com.blocain.bitms.trade.risk.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/11/24.
 */
public class RiskServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    SysParameterService      sysParameterService;
    
    @Autowired(required = false)
    EntrustVCoinMoneyService entrustVCoinMoneyService;

    public static final Logger logger = LoggerFactory.getLogger(RiskServiceImplTest.class);
    
    @Test
    public void entrustRisk() throws Exception
    {
    }

    /**
     * 判断正在委托中的数量
     * @throws Exception
     */
    @Test
    public void enturstDoingRisk()
    {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_TRADE_MONEY_MAX_ENTRUST_CNT);
        params = sysParameterService.getSysParameterByEntity(params);
        int i = 0;
        if (params != null)
        {
            while(true)
            {
                Long cnt = Long.parseLong(params.getValue().toString());
                Long doing = entrustVCoinMoneyService.getMoneyDoingEntrustVCoinMoneyCnt( FundConsts.WALLET_BTC2USDX_TYPE);
                logger.debug("max="+cnt+"  doing="+doing);
                if (doing.compareTo(cnt) >= 0 )
                {
                    i++;
                    logger.debug("第"+i+"次循环");
                    if(i == 11)
                    {
                        logger.debug("第"+i+"次循环 跳出 操作失败 抛出异常");
                        i=0;
                        break;
                    }
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e)
                    {
                    }
                }
                else
                {
                    logger.debug("允许下单");
                    i=0;
                    break;
                }
            }
        }
        logger.debug("结束");
    }
}