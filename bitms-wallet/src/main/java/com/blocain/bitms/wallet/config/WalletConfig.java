package com.blocain.bitms.wallet.config;

import com.blocain.bitms.tools.utils.PropertiesUtils;

/**
 * WalletConfig Introduce
 * <p>Title: WalletConfig</p>
 * <p>File：WalletConfig.java</p>
 * <p>Description: WalletConfig</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class WalletConfig
{
    protected static final PropertiesUtils properties                  = new PropertiesUtils("wallet.properties");
    
    public static final long               THREAD_SYNC_ERC20_BLOCK_SLEEP        = properties.getLong("thread.sync.erc20.block.sleep");
    
    public static final long               THREAD_SACN_ERC20_BLOCK_SLEEP        = properties.getLong("thread.sacn.erc20.block.sleep");
    
    public static final long               THREAD_SCAN_ERC20_TRANS_SLEEP        = properties.getLong("thread.scan.erc20.trans.sleep");

    public static final long               THREAD_COLLECT_ERC20_BALANCE_SLEEP   = properties.getLong("thread.collect.erc20.balance.sleep");
}
