/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.account.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息表 持久层接口
 * <p>File：MessageMapper.java </p>
 * <p>Title: MessageMapper </p>
 * <p>Description:MessageMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MessageMapper extends GenericMapper<Message>
{
    /**
     * 统计记录数
     * @param message
     * @return
     */
    Long countByAccount(Message message);
    
    /**
     * 分页查询
     * @param message
     * @return
     */
    List<Message> searchByAccount(@Param("message") Message message, @Param("page") Pagination page);

    /**
     * 分页查询历史数据
     * @param message
     * @return
     */
    List<Message> findHistoryList(Message message);

}
