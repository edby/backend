/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.mapper;

import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.orm.annotation.MyBatisDao ;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetPremium;
import com.blocain.bitms.trade.fund.model.AccountPremiumAssetModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账户持仓调节(溢价费)记录表 持久层接口
 * <p>File：AccountDebitAssetPremiumMapper.java </p>
 * <p>Title: AccountDebitAssetPremiumMapper </p>
 * <p>Description:AccountDebitAssetPremiumMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface AccountDebitAssetPremiumMapper extends GenericMapper<AccountDebitAssetPremium>
{

    List<AccountPremiumAssetModel> getPremiumLongAccountAsset(@Param("relatedStockinfoId") Long relatedStockinfoId,@Param("stockinfoId") Long stockinfoId);

    List<AccountPremiumAssetModel> getPremiumShortAccountAsset(@Param("relatedStockinfoId") Long relatedStockinfoId,@Param("stockinfoId") Long stockinfoId);
}
