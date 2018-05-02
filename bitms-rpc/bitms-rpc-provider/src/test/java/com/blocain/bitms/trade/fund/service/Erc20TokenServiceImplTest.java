package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2018/3/30.
 */
public class Erc20TokenServiceImplTest extends AbstractBaseTest
{
    @Autowired
    Erc20TokenService erc20TokenService;
    
    @Test
    public void getList()
    {
        erc20TokenService.selectAll();
    }
    
    @Test
    public void getErc20Token() throws Exception
    {
        erc20TokenService.getErc20Token("0xf3e014fe81267870624132ef3a646b8e83853a96", "vin2eth");
    }
    
    @Test
    public void openToken()
    {
        erc20TokenService.doActiveToken("0x50e6e08ea13b0c11ccc5cfcc558b431665521b7a", 80107307110764544L,10002L);
    }
    
    @Test
    public void autoCloseToken()
    {
        erc20TokenService.autoCloseActiveToken();
    }

    @Test
    public void update()
    {
        Erc20Token e = erc20TokenService.selectByPrimaryKey(78665561411686400L);
        e.setAwardStatus(1);
        e.setNeedAward(1);
        e.setInviteAccountId(78677668656058368L);
        erc20TokenService.updateByPrimaryKey(e);
    }
}