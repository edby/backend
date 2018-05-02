package com.blocain.bitms.trade.account.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountServiceImplTest 介绍
 * <p>File：AccountServiceImplTest.java </p>
 * <p>Title: AccountServiceImplTest </p>
 * <p>Description:AccountServiceImplTest </p>
 * <p>Copyright: Copyright (c) 2017/7/20 </p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountServiceImplTest extends AbstractBaseTest
{
    @Autowired
    AccountService              accountService;
    
    @Autowired
    AccountWalletAssetService   accountWalletAssetService;
    
    @Autowired
    AccountSpotAssetService     accountSpotAssetService;
    
    @Autowired
    AccountWealthAssetService   accountWealthAssetService;
    
    @Autowired
    AccountContractAssetService accountContractAssetService;
    
    @Autowired
    FundCurrentService          fundCurrentService;
    
    @Autowired
    FundService                 fundService;
    
    @Autowired
    StockInfoService            stockInfoService;
    
    @Autowired
    AccountFundAdjustService    accountFundAdjustService;
    
    public static final Logger  logger = LoggerFactory.getLogger(AccountServiceImplTest.class);

    @Test
    public void testEncrypt()
    {
       System.out.println(EncryptUtils.desEncrypt("testaddress"));
    }

    @Test
    public void testXGAccount()
    {
        Account account = accountService.findByName("60999@biex.com");
        System.out.println(account.toString());
        System.out.println(account.verifySignature());
        System.out.println(account.getAuthKey());
        account.setAccountName("60999@qq.com");
        account.setEmail("60999@qq.com");
        accountService.save(account);
        account = accountService.findByName("60999@qq.com");
        System.out.println(account.toString());
        System.out.println(account.verifySignature());
        System.out.println(account.getAuthKey());
    }

    @Test
    public void chekAccount()
    {
        Account account = accountService.findByName("2079953053@qq.com");
        System.out.println(account.toString());
        System.out.println(account.verifySignature());
        System.out.println(account.getAuthKey());
        account.setAuthKey(null);
        account.setSecurityPolicy(AccountConsts.SECURITY_POLICY_NEEDSMS);
        accountService.save(account);
        account = accountService.findByName("2079953053@qq.com");
        System.out.println(account.toString());
        System.out.println(account.verifySignature());
        System.out.println(account.getAuthKey());
    }
    
    /**
     * 数据加密
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException
    {
        // String entryptPassword = EncryptUtils.entryptPassword("Bcbc963852");
        // System.out.println("密码加密后为:" + entryptPassword);
        //
        // Account account = new Account();
        // account.setId(100000000000L); // ID
        // account.setUnid(1000l);// UNID
        // account.setAccountName("sunbiao@blocain.com");// accountName
        // account.setWalletPwd(entryptPassword);
        // account.setLoginPwd(entryptPassword);// password
        // account.setStatus(0);// status
        // account.doSign();
        // System.out.println("密钥串:" + account.getSign());
        // System.out.println("随机码:" + account.getRandomKey());
    }
    
    @Test
    public void abNormalAcctFrozenBatchTest()
    {
        try
        {
            ArrayList<Long> list = new ArrayList<Long>();
            list.add(1897364675629056L);
            list.add(7771489474449408L);
            accountService.abNormalAcctFrozenBatch(list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    /**
     * 修复账户
     */
    public void repairAccount()
    {
        List<Account> list = accountService.selectAll();
        for (Account account : list)
        {
            accountService.updateByPrimaryKey(account);
        }
    }
    
    @Test
    public void spot2Wealth()
    {
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(43638716878557184L);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(BigDecimal.ZERO);
        fundModel.setAmount(BigDecimal.ONE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_USD_TYPE);// USD专区
        fundModel.setStockinfoId(FundConsts.WALLET_USD_TYPE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH);
        fundModel.setCreateBy(43638716878557184L);
        fundService.fundTransaction(fundModel);
    }
    
    @Test
    public void wealth2Spot()
    {
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(43638716878557184L);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(BigDecimal.ZERO);
        fundModel.setAmount(BigDecimal.ONE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_USD_TYPE);// USD专区
        fundModel.setStockinfoId(FundConsts.WALLET_USD_TYPE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT);
        fundModel.setCreateBy(43638716878557184L);
        fundService.fundTransaction(fundModel);
    }
    
    @Test
    public void spot2Wallet()
    {
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(300000060003L);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(BigDecimal.ZERO);
        fundModel.setAmount(BigDecimal.ONE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET);
        fundModel.setCreateBy(300000060003L);
        fundService.fundTransaction(fundModel);
    }
    
    @Test
    public void wallet2Spot()
    {
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(300000060003L);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(BigDecimal.ZERO);
        fundModel.setAmount(BigDecimal.ONE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT);
        fundModel.setCreateBy(300000060003L);
        fundService.fundTransaction(fundModel);
    }
    
    @Test
    /**
     * 初始化一万个账户并强增1000BTC
     */
    public void AddTenThousandAccount()
    {
        // 第一步：用sql制造一万个用户
        /**
         DECLARE  x number; --声明变量
         BEGIN  x := 60001; --给初值
         FOR x IN REVERSE 60001 .. 70000 LOOP    --reverse由大到小
         insert into account(id , unid ,accountName ,loginPwd ,
         walletPwd ,email ,country ,mobNo ,authKey ,status ,thawTime ,
         remark ,sign ,randomKey ,createDate ,updateDate ,tradePolicy ,
         securityPolicy ,delFlag ,lang )
         select  x+300000000000 ,x ,x||'@biex.com' ,loginPwd ,
         walletPwd ,email ,country ,mobNo ,authKey ,status ,thawTime ,
         remark ,sign ,randomKey ,createDate ,updateDate ,tradePolicy ,
         securityPolicy ,delFlag ,'zh_CN'  from account where accountName = 'admin@bitms.com' ;
         commit;
         END LOOP;
         END;
         */
        // 强增金额 钱包转合约金额
        BigDecimal amount = BigDecimal.valueOf(100);
        Account entity = new Account();
        // 操作的用户对象
        entity.setAccountName("@biex");
        List<Account> list = accountService.findList(entity);
        System.out.println(list.size());
        for (Account account : list)
        {
            System.out.println(account.getAccountName());
            FundModel fundModel = new FundModel();
            // 第二步：每个用户修复加签 并初始化密码
            String entryptPassword = EncryptUtils.entryptPassword("123456");
            System.out.println("密码加密后为:" + entryptPassword);
            account.setLoginPwd(entryptPassword);// password
            account.setStatus(0);// status
            account.setRemark("");
            account.setMobNo(null);
            account.setSecurityPolicy(0);
            account.setEmail(account.getAccountName());
            accountService.updateByPrimaryKey(account);
            // 第三步：给每个用户强增1000BTC
            AccountFundAdjust accountFundAdjust = new AccountFundAdjust();
            accountFundAdjust.setAccountId(account.getId());
            accountFundAdjust.setLockEndDay(null);
            accountFundAdjust.setCreateDate(new Timestamp(System.currentTimeMillis()));
            accountFundAdjust.setUpdateBy(account.getId());
            accountFundAdjust.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountFundAdjust.setLockStatus(FundConsts.ASSET_LOCK_STATUS_NO);
            accountFundAdjust.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            accountFundAdjust.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD);
            accountFundAdjust.setAdjustType("adjustAdd");
            accountFundAdjust.setAdjustAmt(amount);
            accountFundAdjust.setNeedLock(FundConsts.ASSET_LOCK_STATUS_NO);
            // 调用强增fund
            fundModel.setAccountId(accountFundAdjust.getAccountId());
            fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            fundModel.setStockinfoIdEx(FundConsts.WALLET_USD_TYPE);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD);
            fundModel.setAmount(amount);
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
            fundModel.setAmountEx(BigDecimal.ZERO);
            // fundCurrentService.doSaveAccountFundAdjust(accountFundAdjust, fundModel);
            // // 第四步 钱包账户转交易账户
            fundModel = new FundModel();
            fundModel.setAccountId(account.getId());
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setAmountEx(amount);
            fundModel.setAmount(amount);
            fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
            fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);
            fundModel.setCreateBy(account.getId());
            // fundService.fundTransaction(fundModel);
        }
    }
    
    /**
     * 强增
     */
    @Test
    public void adjustAdd()
    {
        BigDecimal amount = BigDecimal.valueOf(50000000l);
        Long stockinfoId = FundConsts.WALLET_ETH_TYPE;
        Long accountId= 78391694369755136L;
        AccountFundAdjust accountFundAdjust = new AccountFundAdjust();
        accountFundAdjust.setAccountId(accountId);
        accountFundAdjust.setLockEndDay(null);
        accountFundAdjust.setCreateDate(new Timestamp(System.currentTimeMillis()));
        accountFundAdjust.setUpdateBy(FundConsts.SYSTEM_ACCOUNT_ID);
        accountFundAdjust.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountFundAdjust.setLockStatus(FundConsts.ASSET_LOCK_STATUS_NO);
        accountFundAdjust.setStockinfoId(stockinfoId);
        accountFundAdjust.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD);
        accountFundAdjust.setAdjustType("adjustAdd");
        accountFundAdjust.setAdjustAmt(amount);
        accountFundAdjust.setNeedLock(FundConsts.ASSET_LOCK_STATUS_NO);
        // 调用强增fund
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(accountFundAdjust.getAccountId());
        fundModel.setStockinfoId(accountFundAdjust.getStockinfoId());
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD);
        fundModel.setAmount(amount);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
        fundModel.setAmountEx(BigDecimal.ZERO);
        fundCurrentService.doSaveAccountFundAdjust(accountFundAdjust, fundModel);
    }
    
    /**
     * 初始化账户资产问题
     */
    @Test
    public void initAccountAsset()
    {
        // 钱包资产账户
        StockInfo info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_DIGITALCOIN);
        info.setId(FundConsts.WALLET_BTC_TYPE);// 如果是多个币 复制方法
        List<StockInfo> stockInfoList = stockInfoService.findList(info);
        for (StockInfo stockInfo : stockInfoList)
        {
            Account entity = new Account();
            List<Account> list = accountService.findList(entity);
            System.out.println(list.size());
            for (Account account : list)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkWalletAsset(account.getId(), stockInfo.getId());
                }
            }
        }
        // 纯现货
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_PURESPOT);
        stockInfoList = stockInfoService.findList(info);
        for (StockInfo stockInfo : stockInfoList)
        {
            Account entity = new Account();
            List<Account> list = accountService.findList(entity);
            System.out.println(list.size());
            for (Account account : list)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkWalletAsset(account.getId(), stockInfo.getTradeStockinfoId());
                    checkWalletAsset(account.getId(), stockInfo.getCapitalStockinfoId());
                }
            }
        }
        // 杠杆现货
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        stockInfoList = stockInfoService.findList(info);
        for (StockInfo stockInfo : stockInfoList)
        {
            Account entity = new Account();
            List<Account> list = accountService.findList(entity);
            System.out.println(list.size());
            for (Account account : list)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkSpotAsset(account.getId(), stockInfo.getTradeStockinfoId(), stockInfo.getCapitalStockinfoId());
                    checkSpotAsset(account.getId(), stockInfo.getCapitalStockinfoId(), stockInfo.getCapitalStockinfoId());
                }
            }
        }
        // 理财账户
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_DIGITALCOIN);
        info.setCanWealth(FundConsts.PUBLIC_STATUS_YES);
        stockInfoList = stockInfoService.findList(info);
        for (StockInfo stockInfo : stockInfoList)
        {
            Account entity = new Account();
            List<Account> list = accountService.findList(entity);
            System.out.println(list.size());
            for (Account account : list)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkWealthAsset(account.getId(), stockInfo.getId(), stockInfo.getId());
                }
            }
        }
        // 合约账户
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        stockInfoList = stockInfoService.findList(info);
        for (StockInfo stockInfo : stockInfoList)
        {
            Account entity = new Account();
            List<Account> list = accountService.findList(entity);
            System.out.println(list.size());
            for (Account account : list)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkContractAsset(account.getId(), stockInfo.getTradeStockinfoId(), stockInfo.getId());
                    checkContractAsset(account.getId(), stockInfo.getCapitalStockinfoId(), stockInfo.getId());
                }
            }
        }
    }
    
    /**
     * 初始化账户资产问题
     */
    @Test
    public void initAccountAsset2()
    {
        Account entity = new Account();
        List<Account> list = accountService.findList(entity);
        System.out.println(list.size());
        // 钱包资产账户
        StockInfo info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_DIGITALCOIN);
        info.setId(FundConsts.WALLET_BTC_TYPE);// 如果是多个币 复制方法
        List<StockInfo> stockInfoList1 = stockInfoService.findList(info);

        // 纯现货
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_PURESPOT);
        List<StockInfo> stockInfoList2 = stockInfoService.findList(info);

        // 杠杆现货
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        List<StockInfo> stockInfoList3 = stockInfoService.findList(info);

        // 理财账户
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_DIGITALCOIN);
        info.setCanWealth(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> stockInfoList4 = stockInfoService.findList(info);

        // 合约账户
        info = new StockInfo();
        info.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> stockInfoList5  = stockInfoService.findList(info);

        for (Account account : list)
        {

            for (StockInfo stockInfo : stockInfoList1)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkWalletAsset(account.getId(), stockInfo.getId());
                }
            }
            for (StockInfo stockInfo : stockInfoList2)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkWalletAsset(account.getId(), stockInfo.getTradeStockinfoId());
                    checkWalletAsset(account.getId(), stockInfo.getCapitalStockinfoId());
                }
            }
            for (StockInfo stockInfo : stockInfoList3)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkSpotAsset(account.getId(), stockInfo.getTradeStockinfoId(), stockInfo.getCapitalStockinfoId());
                    checkSpotAsset(account.getId(), stockInfo.getCapitalStockinfoId(), stockInfo.getCapitalStockinfoId());
                }
            }
            for (StockInfo stockInfo : stockInfoList4)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkWealthAsset(account.getId(), stockInfo.getId(), stockInfo.getId());
                }
            }
            for (StockInfo stockInfo : stockInfoList5)
            {
                if (account.getId().longValue() > FundConsts.SYSTEM_ACCOUNT_ID)
                {
                    checkContractAsset(account.getId(), stockInfo.getTradeStockinfoId(), stockInfo.getId());
                    checkContractAsset(account.getId(), stockInfo.getCapitalStockinfoId(), stockInfo.getId());
                }
            }
        }
    }
    
    /**
     * 从db中查找钱包账户资产记录
     * @param accountId
     * @param stockinfoId
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private AccountWalletAsset findAccountWalletAssetFormDB(Long accountId, Long stockinfoId)
    {
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(accountId);
        accountWalletAsset.setStockinfoId(stockinfoId);
        List<AccountWalletAsset> list;
        try
        {
            list = accountWalletAssetService.findList(accountWalletAsset);
            if (list.size() > 0)
            {
                accountWalletAsset = list.get(0);
            }
            else
            {
                accountWalletAsset = null;
            }
        }
        catch (Exception e)
        {
            accountWalletAsset = null;
            logger.debug("从db中查找钱包账户资产记录 error:" + e.getMessage());
        }
        if (null == accountWalletAsset)
        {
            logger.debug("从db中查找钱包账户资产记录 accountWalletAsset is null");
        }
        else
        {
            logger.debug("从db中查找钱包账户资产记录 accountWalletAsset:" + accountWalletAsset.toString());
        }
        return accountWalletAsset;
    }
    
    /**
     * 检查杠杆现货账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     */
    public void checkSpotAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(accountId);
        accountSpotAsset.setStockinfoId(stockinfoId);
        accountSpotAsset.setRelatedStockinfoId(relatedStockinfoId);
        List<AccountSpotAsset> list = accountSpotAssetService.findList(accountSpotAsset);
        if (list.size() > 0)
        {
            accountSpotAsset = accountSpotAssetService.selectByPrimaryKey(list.get(0).getId());
        }
        else
        {
            accountSpotAsset = new AccountSpotAsset();
            accountSpotAsset.setDirection("Long");
            accountSpotAsset.setAmount(BigDecimal.ZERO);
            accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
            accountSpotAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountSpotAsset.setStockinfoId(stockinfoId);
            accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountSpotAsset.setRemark("");
            accountSpotAsset.setAccountId(accountId);
            accountSpotAssetService.insert(accountSpotAsset);
        }
    }
    
    /**
     * 检查钱包账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     */
    public void checkWalletAsset(Long accountId, Long stockinfoId)
    {
        AccountWalletAsset accountWalletAsset = this.findAccountWalletAssetFormDB(accountId, stockinfoId);
        if (accountWalletAsset == null)
        {
            accountWalletAsset = new AccountWalletAsset();
            accountWalletAsset.setAmount(BigDecimal.ZERO);
            accountWalletAsset.setRelatedStockinfoId(stockinfoId);
            accountWalletAsset.setStockinfoId(stockinfoId);
            accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
            accountWalletAsset.setPrice(BigDecimal.ONE);
            accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
            accountWalletAsset.setRemark("");
            accountWalletAsset.setAccountId(accountId);
            accountWalletAsset.setRelatedStockinfoId(stockinfoId);
            accountWalletAssetService.insert(accountWalletAsset);
        }
    }
    
    /**
     * 检查理财账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     */
    public void checkWealthAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountWealthAsset accountWealthAsset = new AccountWealthAsset();
        accountWealthAsset.setWealthAccountId(accountId);
        accountWealthAsset.setStockinfoId(stockinfoId);
        accountWealthAsset.setRelatedStockinfoId(relatedStockinfoId);
        List<AccountWealthAsset> list = accountWealthAssetService.findList(accountWealthAsset);
        if (list.size() > 0)
        {
            accountWealthAsset = accountWealthAssetService.selectByPrimaryKey(list.get(0).getId());
        }
        else
        {
            accountWealthAsset = new AccountWealthAsset();
            accountWealthAsset.setWealthAmt(BigDecimal.ZERO);
            accountWealthAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountWealthAsset.setStockinfoId(stockinfoId);
            accountWealthAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountWealthAsset.setRemark("");
            accountWealthAsset.setIssuerAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_DEBIT_WEALTH_ID);
            accountWealthAsset.setAccumulateInterest(BigDecimal.ZERO);
            accountWealthAsset.setWealthAccountId(accountId);
            accountWealthAsset.setLastInterestDay(0L);
            accountWealthAssetService.insert(accountWealthAsset);
        }
    }
    
    /**
     * 检查合约账户是否存在 不存在则插入默认
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     */
    public void checkContractAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountContractAsset accountContractAsset = this.findAccountContractAssetFormDB(accountId, stockinfoId, relatedStockinfoId);
        if (accountContractAsset == null)
        {
            accountContractAsset = new AccountContractAsset();
            accountContractAsset.setAmount(BigDecimal.ZERO);
            accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountContractAsset.setStockinfoId(stockinfoId);
            accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
            accountContractAsset.setPrice(BigDecimal.ONE);
            accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountContractAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
            accountContractAsset.setRemark("");
            accountContractAsset.setAccountId(accountId);
            accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
            accountContractAssetService.insert(accountContractAsset);
        }
    }
    
    /**
     * 从db中查找合约账户资产记录
     * @param accountId
     * @param stockinfoId
     * @return
     * @author zcx 2017-09-19 15:52:34
     */
    private AccountContractAsset findAccountContractAssetFormDB(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(accountId);
        accountContractAsset.setStockinfoId(stockinfoId);
        accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
        accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
        List<AccountContractAsset> accountWalletAssetList;
        try
        {
            accountWalletAssetList = accountContractAssetService.findList(accountContractAsset);
            if (accountWalletAssetList.size() > 0)
            {
                accountContractAsset = accountWalletAssetList.get(0);
                logger.debug("从db中查找合约账户资产记录 accountContractAsset:" + accountContractAsset.toString());
            }
            else
            {
                accountContractAsset = new AccountContractAsset();
                accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
                accountContractAsset.setStockinfoId(stockinfoId);
                accountContractAsset.setAccountId(accountId);
                accountContractAsset.setAmount(BigDecimal.ZERO);
                accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
                accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                accountContractAsset.setId(SerialnoUtils.buildPrimaryKey());
                accountContractAsset.setTableName(getStockInfo(relatedStockinfoId).getTableAsset());
                accountContractAssetService.insert(accountContractAsset);
                logger.debug("从db中查找合约账户资产记录 accountContractAsset:" + accountContractAsset.toString());
            }
        }
        catch (Exception e)
        {
            accountContractAsset = null;
            logger.debug("从db中查找合约账户资产记录 error:" + e.getMessage());
        }
        return accountContractAsset;
    }

    @Test
    public void fiexAdmin()
    {
        String entryptPassword = EncryptUtils.entryptPassword("Bcbc963852");
        List<Account> list = accountService.selectAll();
        for(Account entity:list)
        {
            if(entity.getId().longValue()<=FundConsts.SYSTEM_ACCOUNT_ID.longValue())
            {
                entity.setLoginPwd(entryptPassword);
                entity.setStatus(AccountConsts.ACCOUNT_STATUS_NORMAL);
                entity.setSecurityPolicy(AccountConsts.SECURITY_POLICY_DEFAULT);
                entity.setAuthKey(null);
                entity.setMobNo(null);
                entity.setEmail(entity.getAccountName());
                entity.setTradePolicy(AccountConsts.TRADE_POLICY_DEFAULT);
                entity.setDelFlag(false);
                accountService.updateByPrimaryKey(entity);
            }

        }
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}