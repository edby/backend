package com.blocain.bitms.boss.common.service;

import java.util.List;

import com.blocain.bitms.orm.core.GenericNoSqlImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.boss.common.entity.Feedback;

/**
 * 问题反馈服务
 * <p>File：FeedbackServiceNoSqlImpl.java</p>
 * <p>Title: FeedbackServiceNoSqlImpl</p>
 * <p>Description: FeedbackServiceNoSqlImpl</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Service
public class FeedbackNoSqlImpl extends GenericNoSqlImpl<Feedback> implements FeedbackNoSql
{
    public FeedbackNoSqlImpl()
    {
        super(Feedback.class);
    }
    
    @Override
    public PaginateResult<Feedback> search(Pagination pagin, Feedback entity)
    {
        Query query = new Query();
        if (StringUtils.isNotBlank(entity.getTrueName()))
        {
            query.addCriteria(Criteria.where("trueName").regex(entity.getTrueName()));
        }
        if (StringUtils.isNotBlank(entity.getContactNumber()))
        {
            query.addCriteria(Criteria.where("contactNumber").regex(entity.getContactNumber()));
        }
        pagin.setTotalRows(mongoTemplate.count(query, Feedback.class));
        query.with(new PageRequest(pagin.getPage() - 1, pagin.getRows()));// 分页
        Sort.Direction direction = Sort.Direction.DESC;
        if (pagin.getOrder().equalsIgnoreCase(BitmsConst.DEFAULT_SORT_ASC)) direction = Sort.Direction.ASC;
        query.with(new Sort(direction, pagin.getSort()));// 排序
        List<Feedback> data = mongoTemplate.find(query, Feedback.class);
        return new PaginateResult<>(pagin, data);
    }
}
