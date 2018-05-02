/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.mapper;

import com.blocain.bitms.boss.common.entity.MsgTemplate;
import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 消息模版 持久层接口
 * <p>File：MsgTemplateDao.java </p>
 * <p>Title: MsgTemplateDao </p>
 * <p>Description:MsgTemplateDao </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MsgTemplateMapper extends GenericMapper<MsgTemplate>
{
    /**
     * 根据模版KEY和语言类型取消息模版
     * @param key
     * @param type
     * @param lang
     * @return
     */
    MsgTemplate findByKeyAndLang(@Param("key") String key, @Param("type") String type, @Param("lang") String lang);
}
