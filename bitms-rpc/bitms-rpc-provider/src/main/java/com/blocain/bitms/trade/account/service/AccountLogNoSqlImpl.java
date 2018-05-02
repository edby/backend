/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import java.util.List;

import com.blocain.bitms.orm.core.GenericNoSqlImpl;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.AccountLog;

/**
 * 账户日志表 服务实现类
 * <p>File：AccountLog.java </p>
 * <p>Title: AccountLog </p>
 * <p>Description:AccountLog </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountLogNoSqlImpl extends GenericNoSqlImpl<AccountLog> implements AccountLogNoSql
{
    public static final Logger logger = LoggerFactory.getLogger(AccountLogNoSqlImpl.class);
    
    public AccountLogNoSqlImpl()
    {
        super(AccountLog.class);
    }
    
    @Override
    public void insert(AccountLog entity)
    {
        try
        {
            if (null == entity) return;
            if (null == entity.getId()) entity.setId(SerialnoUtils.buildPrimaryKey());
            mongoTemplate.insert(entity);
        }
        catch (RuntimeException e)
        {
            logger.error("日志插入失败：{}", e.getLocalizedMessage());
        }
    }
    
    @Override
    public List<AccountLog> findLastTenLoginLogs(Long accountId) throws BusinessException
    {
        if (null == accountId) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Query query = new Query();
        query.addCriteria(Criteria.where("accountId").is(accountId).and("opType").is("login"));
        query.with(new PageRequest(0, 10));
        query.with(new Sort(Sort.Direction.DESC, "id"));
        return mongoTemplate.find(query, AccountLog.class);
    }
    
    @Override
    public List<AccountLog> findLastTenSettingLogs(Long accountId) throws BusinessException
    {
        if (null == accountId) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Query query = new Query();
        query.addCriteria(Criteria.where("accountId").is(accountId).and("opType").is("setting"));
        query.with(new PageRequest(0, 10));
        query.with(new Sort(Sort.Direction.DESC, "id"));
        return mongoTemplate.find(query, AccountLog.class);
    }
    
    @Override
    public PaginateResult<AccountLog> search(Pagination pagin, AccountLog entity)
    {
        Query query = new Query();
        if (StringUtils.isNotBlank(entity.getAccountName()))
        {
            query.addCriteria(Criteria.where("accountName").regex(entity.getAccountName()));
        }
        if (StringUtils.isNotBlank(entity.getIpAddr()))
        {
            query.addCriteria(Criteria.where("ipAddr").regex(entity.getIpAddr()));
        }
        if (StringUtils.isNotBlank(entity.getSystemName()))
        {
            query.addCriteria(Criteria.where("systemName").regex(entity.getSystemName()));
        }
        Criteria criteria = Criteria.where("createDate");
        if (null != entity.getTimeStart())
        {
            criteria.gte(entity.getTimeStart());
        }
        if (null != entity.getTimeEnd())
        {
            criteria.lte(entity.getTimeEnd());
        }
        if (null != entity.getTimeStart() || null != entity.getTimeEnd())
        {
            query.addCriteria(criteria);
        }
        pagin.setTotalRows(mongoTemplate.count(query, AccountLog.class));
        query.with(new PageRequest(pagin.getPage() - 1, pagin.getRows()));// 分页
        Sort.Direction direction = Sort.Direction.DESC;
        if (pagin.getOrder().equalsIgnoreCase(BitmsConst.DEFAULT_SORT_ASC)) direction = Sort.Direction.ASC;
        query.with(new Sort(direction, pagin.getSort()));// 排序
        List<AccountLog> data = mongoTemplate.find(query, AccountLog.class);
        return new PaginateResult<>(pagin, data);
    }
}
