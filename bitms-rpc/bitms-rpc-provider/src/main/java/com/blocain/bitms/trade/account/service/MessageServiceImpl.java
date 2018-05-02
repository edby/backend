/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.mapper.AccountMessageMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.trade.account.entity.Message;
import com.blocain.bitms.trade.account.mapper.MessageMapper;

import java.util.List;

/**
 * 消息表 服务实现类
 * <p>File：MessageServiceImpl.java </p>
 * <p>Title: MessageServiceImpl </p>
 * <p>Description:MessageServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class MessageServiceImpl extends GenericServiceImpl<Message> implements MessageService
{
    protected MessageMapper        messageMapper;
    
    @Autowired
    protected AccountMessageMapper accountMessageMapper;
    
    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper)
    {
        super(messageMapper);
        this.messageMapper = messageMapper;
    }
    
    @Override
    public void deleteAccountMessageById(Long accountId, String ... messageId) throws BusinessException
    {
        if (null == accountId || null == messageId) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        List<String> tempIds = Lists.newArrayList(messageId);
        List<Long> messageIds = Lists.newArrayList();
        for (String tmpId : tempIds)
        {
            messageIds.add(Long.valueOf(tmpId));
        }
        accountMessageMapper.deleteAccountMessageById(accountId, messageIds);
    }
    
    @Override
    public PaginateResult<Message> searchByAccount(Pagination pagin, Message entity) throws BusinessException
    {
        if (null == entity.getAccountId()) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        pagin.setTotalRows(messageMapper.countByAccount(entity));
        List<Message> data = messageMapper.searchByAccount(entity, pagin);
        return new PaginateResult<>(pagin, data);
    }
    
    @Override
    public PaginateResult<Message> findHistoryList(Pagination pagin, Message entity) throws BusinessException
    {
        List<Message> data = messageMapper.findHistoryList(entity);
        return new PaginateResult<>(pagin, data);
    }
}
