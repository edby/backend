package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.account.entity.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/11/1.
 */
public class MessageServiceImplTest extends AbstractBaseTest
{
    @Autowired
    MessageService messageService;

    //账户消息查询
    @Test
    public void findHistoryList() throws Exception
    {
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        Message message = new Message();
        messageService.findHistoryList(pagination,message);
    }
}