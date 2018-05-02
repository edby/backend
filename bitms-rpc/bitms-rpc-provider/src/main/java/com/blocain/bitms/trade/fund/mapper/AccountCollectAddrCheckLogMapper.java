/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrCheckLog;

/**
 * 账户收款地址审核日志表 持久层接口
 * <p>File：AccountCollectAddrCheckLogMapper.java </p>
 * <p>Title: AccountCollectAddrCheckLogMapper </p>
 * <p>Description:AccountCollectAddrCheckLogMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountCollectAddrCheckLogMapper extends GenericMapper<AccountCollectAddrCheckLog>
{

}
