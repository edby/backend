package com.blocain.bitms.trade.account.utils;

import com.blocain.bitms.orm.utils.SpringContext;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.trade.account.entity.AccountCertification;
import com.blocain.bitms.trade.account.service.AccountCertificationService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.risk.service.EnableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 获取账户资产
 * <p>File：AccountAssetUtils.java</p>
 * <p>Title: AccountAssetUtils</p>
 * <p>Description:AccountAssetUtils</p>
 * <p>Copyright: Copyright (c) 2017年8月4日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
public class AssetUtils
{
    public static final Logger         logger                      = LoggerFactory.getLogger(AssetUtils.class);
    
    static EnableService               enableService               = SpringContext.getBean(EnableService.class);

    static AccountCertificationService accountCertificationService = SpringContext.getBean(AccountCertificationService.class);
    
    /**
     * 获取钱包账户
     * @return
     */
    public static List<AccountWalletAsset> getWalletAsset()
    {
        if (OnLineUserUtils.getPrincipal() != null)
        {
            return enableService.getWalletAssetListByAccountId(OnLineUserUtils.getPrincipal().getId());
        }
        return null;
    }
    
    /**
     * 获取合约账户
     * @return
     */
    public static List<AccountContractAsset> getContractAsset()
    {
        if (OnLineUserUtils.getPrincipal() != null)
        {
            return enableService.getContractAssetListByAccountId(OnLineUserUtils.getPrincipal().getId(), FundConsts.WALLET_BTC2USDX_TYPE);
        }
        return null;
    }

    /**
     * 获取当前用户认证信息
     * @return
     */
    public static  AccountCertification  getCertificationInfo()
    {
        if (OnLineUserUtils.getPrincipal() != null)
        {
            AccountCertification accountCertification = accountCertificationService.findByAccountId(OnLineUserUtils.getPrincipal().getId());
            return accountCertification;
        }
        return new AccountCertification();
    }
}
