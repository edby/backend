/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.bitpay.entity.WithdrawRecord;

/**
 * 账户提现记录 持久层接口
 * <p>File：WithdrawRecordMapper.java </p>
 * <p>Title: WithdrawRecordMapper </p>
 * <p>Description:WithdrawRecordMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface WithdrawRecordMapper extends GenericMapper<WithdrawRecord>
{

}
