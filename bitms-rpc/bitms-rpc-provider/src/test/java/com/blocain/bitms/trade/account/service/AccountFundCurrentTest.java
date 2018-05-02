package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountServiceImplTest 介绍
 * <p>File：AccountServiceImplTest.java </p>
 * <p>Title: AccountServiceImplTest </p>
 * <p>Description:AccountServiceImplTest </p>
 * <p>Copyright: Copyright (c) 2017/7/20 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountFundCurrentTest extends AbstractBaseTest
{

    @Autowired AccountFundCurrentService accountFundCurrentService;

    @Test
    public void findTheLatestFundCurrent() {
            AccountFundCurrent accountFundCurrent = accountFundCurrentService.findTheLatestFundCurrent();
            System.out.println(accountFundCurrent.toString());
     }

    @Test
    public void  findTheChangeAcctListById()
    {
//        Long id = null;
//        List<Long> acctList = accountFundCurrentService.getChangeAcctListByTimestamp(id);
//        if(CollectionUtils.isNotEmpty(acctList))
//        {
//            for(Long acctId : acctList)
//                System.out.println("账户ID:"+acctId);
//        }
//        else
//         System.out.println("最近没有变动的账户啦");

    }
}