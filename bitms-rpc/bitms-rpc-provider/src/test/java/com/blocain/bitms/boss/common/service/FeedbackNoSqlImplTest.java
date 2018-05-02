package com.blocain.bitms.boss.common.service;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.boss.common.entity.Feedback;

/**
 * Created by admin on 2017/11/1.
 */
public class FeedbackNoSqlImplTest extends AbstractBaseTest
{

    @Autowired
    FeedbackNoSql feedbackNoSql;

    @Test
    //用户意见反馈
    public void feedbackList()
    {
        // 来自界面 start
        Pagination pagination = new Pagination();
        pagination.setRows(10);
        pagination.setPage(1);
        Feedback feedback = new Feedback();
        // 来自界面 end
        PaginateResult<Feedback> result = feedbackNoSql.search(pagination,feedback);
        System.out.println(result.getList().size());
    }
}