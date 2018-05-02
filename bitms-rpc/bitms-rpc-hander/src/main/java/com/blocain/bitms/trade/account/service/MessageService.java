/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Message;

/**
 * 消息表 服务接口
 * <p>File：MessageService.java </p>
 * <p>Title: MessageService </p>
 * <p>Description:MessageService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface MessageService extends GenericService<Message>
{
    /**
     * 根据消息ID和帐户ID删除
     * @param accountId
     * @param messageId
     * @throws BusinessException
     */
    void deleteAccountMessageById(Long accountId, String ... messageId) throws BusinessException;
    
    /**
     * 根据指定帐户的消息列表
     * <p>
     *     entity中的accountId 不可为空
     * </p>
     * @param pagin
     * @param entity
     * @return {@link PaginateResult}
     * @throws BusinessException
     */
    PaginateResult<Message> searchByAccount(Pagination pagin, Message entity) throws BusinessException;

    /**
     * 查询消息发送历史
     * @param pagin
     * @param entity
     * @return
     * @throws BusinessException
     */
    PaginateResult<Message> findHistoryList(Pagination pagin, Message entity) throws BusinessException;

}
