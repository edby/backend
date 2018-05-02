/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.monitor.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;

import java.util.HashMap;

/**
 * 监控引擎 持久层接口
 * <p>File：MonitorEngineMapper.java </p>
 * <p>Title: MonitorEngineMapper </p>
 * <p>Description:MonitorEngineMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface MonitorEngineMapper
{

    /**
     *  杠杆保证金监控
     * @param paramMap
     */
    void dealMarginMonitor(HashMap<String, Object> paramMap);


    /**
     * 杠杆现货总账资金监控
     */
    void dealInternalPlatFundCur(HashMap<String, Object> paramMap);

    /**
     *  账户级别监控
     */
    void dealAccountFundCur(HashMap<String, Object> paramMap);

    /**
     * 数字资产总账监控
     */
    void dealDigitalCoin(HashMap<String, Object> paramMap);

    /**
     * 现金资产总账监控
     */
    void dealCashCoin(HashMap<String, Object> paramMap);

    /**
     * 区块高度内外部监控
     */
    void dealBlockNumberMonitor(HashMap<String, Object> paramMap);

    /**
     * ERC20内外部总额监控
     */
    void dealErc20BalMonitor(HashMap<String, Object> paramMap);

    /**
     *  资产检查
     * @param paramMap
     */
    void doAcctAssetChk(HashMap<String, Object> paramMap);

    /**
     *  单账户资产检查
     * @param paramMap
     */
    void doAcctFundCurChk(HashMap<String, Object> paramMap);


}
