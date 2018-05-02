/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountCandyRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 账户糖果流水表 持久层接口
 * <p>File：AccountCandyRecordMapper.java </p>
 * <p>Title: AccountCandyRecordMapper </p>
 * <p>Description:AccountCandyRecordMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountCandyRecordMapper extends GenericMapper<AccountCandyRecord>
{

    AccountCandyRecord findLastRecord(@Param("stockinfoId") Long stockinfoId);

    AccountCandyRecord findRecordByDateStrng(@Param("accountId")Long accountId,@Param("stockinfoId") Long stockinfoId,@Param("dateStr") String date);
}
