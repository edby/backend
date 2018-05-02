/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.WalletTransferCurrent;
import org.apache.ibatis.annotations.Param;

/**
 * 钱包转账流水表 持久层接口
 * <p>File：WalletTransferCurrentMapper.java </p>
 * <p>Title: WalletTransferCurrentMapper </p>
 * <p>Description:WalletTransferCurrentMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface WalletTransferCurrentMapper extends GenericMapper<WalletTransferCurrent>
{
    WalletTransferCurrent getLastEntity(@Param("stockinfoId") Long stockinfoId);

    /**
     * 获取最后一条 并加行锁
     * @param stockinfoId
     * @return
     */
    WalletTransferCurrent getLastEntityForUpdate(@Param("stockinfoId") Long stockinfoId);

}
