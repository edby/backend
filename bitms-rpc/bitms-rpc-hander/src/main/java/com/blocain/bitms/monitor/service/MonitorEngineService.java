package com.blocain.bitms.monitor.service;

import com.blocain.bitms.monitor.entity.MonitorConfig;
import com.blocain.bitms.monitor.entity.MonitorResult;

/**
 * 监控引擎服务
 * MonitorEngineService Introduce
 * <p>File：MonitorEngineService.java</p>
 * <p>Title: MonitorEngineService</p>
 * <p>Description: MonitorEngineService</p>
 * <p>Copyright: Copyright (c) 2017/9/26</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
public interface MonitorEngineService
{
    /**
     * 核对Btc总账
     */
    void dealDigitalCoinMonitor(MonitorConfig config);

    void dealCachCoinMonitor(MonitorConfig config);

    /**
     * 杠杆保证金监控
     */
    void dealMarginMonitor(MonitorConfig config);

    /**
     * 撮合总账资金监控
     */
    void dealInternalPlatFundCurMonitor(MonitorConfig config);

    /**
     * 账户级别资金流水监控
     * @return
     */
    void dealAccountFundCur(MonitorConfig config);

    /**
     * 区块高度内外部的监控
     * @param config
     */
    void dealBlockNumberMonitor(MonitorConfig config);

    /**
     * ERC20 内外部监控
     * @param config
     */
    void dealErc20BalMonitor(MonitorConfig config);

    /**
     * ERC20 归集费用监控
     * @param config
     */
    void dealErc20CollectFeeMonitor(MonitorConfig config);

    /**
     * ERC20 外部热钱包资产监控
     * @param config
     */
    void dealErc20HotWalletMonitor(MonitorConfig config);

    /**
     * ERC20 外部冷钱包资产监控
     * @param config
     */
    void dealErc20ColdWalletMonitor(MonitorConfig config);

    /**
     * 手动发起账户级币对品种资金流水检查
     * @param categorysId  品种代码
     * @param acctId        账户ID
     * @return
     */
    MonitorResult dealAccountFundCurChk(String categorysId, String acctId);

    /**
     * 手动发起账户级币对资金流水检查
     * @param categorysId  品种代码
     * @param acctId        账户ID
     * @param stockinfoid  币对代码
     * @return
     */
    MonitorResult dealAccountFundCurChk(String categorysId, String acctId,String stockinfoid);

}
