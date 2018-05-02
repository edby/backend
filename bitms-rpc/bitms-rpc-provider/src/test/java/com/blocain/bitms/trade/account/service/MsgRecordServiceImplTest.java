package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.tools.bean.PaginateResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.common.entity.MsgRecord;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.tools.bean.Pagination;

/**
 * Created by Playguy on 2017/7/10.
 */
public class MsgRecordServiceImplTest extends AbstractBaseTest
{
    @Autowired
    MsgRecordNoSql msgRecordService;

    @Test
    public void getMsgRecordList()
    {
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        MsgRecord msgRecord = new MsgRecord();
       PaginateResult<MsgRecord> list =  msgRecordService.search(pagination,msgRecord);
    }

}