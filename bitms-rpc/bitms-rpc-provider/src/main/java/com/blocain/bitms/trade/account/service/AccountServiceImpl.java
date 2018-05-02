/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.account.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.entity.AccountInvitation;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.mapper.AccountInvitationMapper;
import com.blocain.bitms.trade.account.mapper.AccountMapper;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountSpotAsset;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.AccountSpotAssetService;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.fund.service.AccountWealthAssetService;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

/**
 * 账户表 服务实现类
 * <p>File：Account.java </p>
 * <p>Title: Account </p>
 * <p>Description:Account </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class AccountServiceImpl extends GenericServiceImpl<Account> implements AccountService
{
    private AccountMapper               accountMapper;
    
    @Autowired
    private AccountInvitationMapper     accountInvitationMapper;
    
    @Autowired
    private MsgRecordNoSql              msgRecordService;
    
    @Autowired
    private AccountWalletAssetService   accountWalletAssetService;
    
    @Autowired
    private AccountSpotAssetService     accountSpotAssetService;
    
    @Autowired
    private AccountWealthAssetService   accountWealthAssetService;
    
    @Autowired
    private AccountContractAssetService accountContractAssetService;
    
    @Autowired
    private StockInfoService            stockInfoService;
    
    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper)
    {
        super(accountMapper);
        this.accountMapper = accountMapper;
    }
    
    @Override
    public Account findByName(String accountName) throws BusinessException
    {
        if (StringUtils.isBlank(accountName)) return null;
        return accountMapper.findByName(accountName);
    }
    
    @Override
    public Account findByNameAndNormal(String accountName) throws BusinessException
    {
        if (StringUtils.isBlank(accountName)) return null;
        return accountMapper.findByNameAndStatus(accountName, AccountConsts.ACCOUNT_STATUS_NORMAL);
    }
    
    @Override
    public Boolean valiEmail(String email) throws BusinessException
    {
        Account account = findByNameAndNormal(email);
        if (null != account && !account.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        return account != null ? true : false;
    }
    
    @Override
    public Account selectByPrimaryKey(Long id) throws BusinessException
    {
        Account account = super.selectByPrimaryKey(id);
        if (null != account && !account.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        return account;
    }
    
    @Override
    public Account findByEmailAndMob(String email, String phone) throws BusinessException
    {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(phone)) return null;
        Account account = accountMapper.findByEmailAndMob(email, phone);
        if (null != account && !account.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        return account;
    }
    
    @Override
    public Boolean checkBindPhone(String phone) throws BusinessException
    {
        if (StringUtils.isBlank(phone)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account entity = accountMapper.findByPhone(phone);
        return null != entity ? true : false;
    }
    
    @Override
    public Boolean resetPass(Account account) throws BusinessException
    {
        Account entity = accountMapper.findByUnid(account.getUnid());
        if (null == entity) throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        if (!entity.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        StringBuffer cacheKey = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(account.getUnid());
        RedisUtils.del(cacheKey.toString());
        entity.setLoginPwd(EncryptUtils.entryptPassword(account.getLoginPwd()));
        int flag = accountMapper.updateByPrimaryKey(entity);
        return flag > 0 ? true : false;
    }
    
    @Override
    public void register(Account account, String lang, String requestIP) throws BusinessException
    {
        if (!ValidateUtils.isMailFormat(account.getEmail(), true, 64))
        {// 验证邮件地址
            throw new BusinessException(CommonEnums.ERROR_EMAIL_FORMAT_FAILED);
        }
        if (valiEmail(account.getEmail())) { throw new BusinessException(CommonEnums.ERROR_EMAIL_EXIST); }
        // 每次重新发送邮件都清除之前的临时会话缓存
        StringBuffer cacheAddress = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(account.getEmail());
        String cacheKey = RedisUtils.get(cacheAddress.toString());
        if (StringUtils.isNotBlank(cacheKey)) RedisUtils.del(cacheKey);
        msgRecordService.sendRegisterEmail(account.getEmail(), String.valueOf(account.getInvitCode()), lang, requestIP);
    }
    
    @Override
    public String mobileRegister(Account account, String lang, String requestIP) throws BusinessException
    {
        if (valiEmail(account.getEmail()))
        {// 判断邮件是否存在
            throw new BusinessException(CommonEnums.ERROR_EMAIL_EXIST);
        }
        StringBuffer cacheAddress = new StringBuffer(BitmsConst.MESSAGE).append(BitmsConst.SEPARATOR).append(account.getEmail());
        String cacheKey = RedisUtils.get(cacheAddress.toString());
        if (StringUtils.isNotBlank(cacheKey)) RedisUtils.del(cacheKey);
        return msgRecordService.sendMobileRegisterEmail(account.getEmail(), String.valueOf(account.getInvitCode()), lang, requestIP);
    }
    
    @Override
    public void registerConfirm(Account account) throws BusinessException
    {
        account.setUnid(buildUNID());
        account.setAccountName(account.getEmail());
        account.setLoginPwd(EncryptUtils.entryptPassword(account.getLoginPwd()));
        Long currentLong = CalendarUtils.getCurrentLong();
        account.setCreateDate(currentLong);
        account.setUpdateDate(currentLong);
        account.setTradePolicy(AccountConsts.SECURITY_POLICY_DEFAULT);
        account.setSecurityPolicy(AccountConsts.TRADE_POLICY_DEFAULT);
        account.setStatus(AccountConsts.ACCOUNT_STATUS_NORMAL);
        account.setAutoDebit(0); // 自动借贷策略默认不开启0
        this.beanValidator(account);
        if (null != account.getInvitCode())
        {// 保存邀请记录
            if (null != accountMapper.findByUnid(account.getInvitCode()))
            {// 判断验证码是否有郊
                AccountInvitation invitation = new AccountInvitation();
                invitation.setId(SerialnoUtils.buildPrimaryKey());
                invitation.setAccountId(account.getId());
                invitation.setAccountName(account.getAccountName());
                invitation.setInvitCode(account.getInvitCode());
                invitation.setBmsNum(BigDecimal.ZERO);
                invitation.setCreateDate(currentLong);
                invitation.setIsGrant(AccountConsts.ACCOUNT_GRANT_NO);
                invitation.setGrantFlag(AccountConsts.ACCOUNT_GRANT_NO);
                accountInvitationMapper.insert(invitation);
            }
        }
        accountMapper.insert(account);
        // 注册初始化账户资产与系统钱包地址信息
        this.initAccountAssetAndSystemWalletAddress(account);
    }
    
    @Override
    public void registerConfirmTradex(Account account) throws BusinessException
    {
        Account entity = accountMapper.findByName(account.getEmail());
        if (null != entity) { throw new BusinessException(CommonEnums.ERROR_EMAIL_EXIST); }
        account.setId(SerialnoUtils.buildPrimaryKey());
        account.setUnid(buildUNID());
        account.setAccountName(account.getEmail());
        account.setLoginPwd(EncryptUtils.entryptPassword(account.getLoginPwd()));
        Long currentLong = CalendarUtils.getCurrentLong();
        account.setCreateDate(currentLong);
        account.setUpdateDate(currentLong);
        account.setTradePolicy(AccountConsts.SECURITY_POLICY_DEFAULT);
        account.setSecurityPolicy(AccountConsts.TRADE_POLICY_DEFAULT);
        account.setStatus(AccountConsts.ACCOUNT_STATUS_NORMAL);
        account.setAutoDebit(0); // 自动借贷策略默认不开启0
        account.setCreateDate(System.currentTimeMillis());
        account.setUpdateDate(System.currentTimeMillis());
        this.beanValidator(account);
        accountMapper.insert(account);
        // 注册初始化账户资产与系统钱包地址信息
        this.initAccountAssetAndSystemWalletAddressTradex(account);
    }
    
    @Override
    public void checkWalletassetTradex(Account account) throws BusinessException
    {
        List<StockInfo> list = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_ERC20_TOKEN);
        for (StockInfo stockInfo : list)
        {
            if(StringUtils.equalsIgnoreCase(stockInfo.getIsActive(),FundConsts.PUBLIC_STATUS_YES))
            {
                checkWalletAsset(account.getId(), stockInfo.getId());
            }
        }
    }
    
    /**
     * 默认给用户添加资产账户
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
            if(stockinfoId.longValue() == FundConsts.WALLET_ETH_TYPE.longValue() || stockinfoId.longValue() == FundConsts.WALLET_BIEX_TYPE.longValue())
            {
                accountWalletAsset.setIsActiveTrade(FundConsts.PUBLIC_STATUS_YES);
            }
            accountWalletAssetService.insert(accountWalletAsset);
        }
        else
        {
            if(stockinfoId.longValue() == FundConsts.WALLET_ETH_TYPE.longValue() || stockinfoId.longValue() == FundConsts.WALLET_BIEX_TYPE.longValue())
            {
                if(!StringUtils.equalsIgnoreCase(accountWalletAsset.getIsActiveTrade(),FundConsts.PUBLIC_STATUS_YES))
                {
                    accountWalletAsset.setIsActiveTrade(FundConsts.PUBLIC_STATUS_YES);
                    accountWalletAssetService.updateByPrimaryKey(accountWalletAsset);
                }
            }
        }
    }
    
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
     * TRADEX注册初始化钱包账户资产与系统钱包地址信息
     * @param account
     * @return
     */
    private int initAccountAssetAndSystemWalletAddressTradex(Account account) throws BusinessException
    {
        List<StockInfo> list = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_ERC20_TOKEN);
        for (StockInfo stockInfo : list)
        {
            AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
            accountWalletAsset.setAccountId(account.getId());
            accountWalletAsset.setStockinfoId(stockInfo.getId());
            accountWalletAsset.setRelatedStockinfoId(stockInfo.getId());
            accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
            accountWalletAsset.setPrice(BigDecimal.ONE);
            accountWalletAsset.setAmount(BigDecimal.ZERO);
            accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
            accountWalletAsset.setRemark("注册自动开通钱包账户");
            accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            accountWalletAssetService.insert(accountWalletAsset);
        }
        return 0;
    }
    
    @Override
    public void modifyAccountStatusToFrozen(Long accountId, String reason) throws BusinessException
    {
        if (null == accountId) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        Account account = accountMapper.selectByPrimaryKey(accountId);
        account.setStatus(AccountConsts.ACCOUNT_STATUS_FROZEN);
        account.setThawTime(CalendarUtils.getCurrentTime());
        account.setRemark(reason);
        accountMapper.updateByPrimaryKey(account);
    }
    
    @Override
    public void modifyAccountByTask() throws BusinessException
    {
        List<Account> data = accountMapper.findThawUserList();
        if (ListUtils.isNotNull(data))
        {
            Long currentTime = CalendarUtils.getCurrentLong();
            for (Account account : data)
            {
                if (account.getThawTime() >= currentTime)
                {
                    account.setStatus(AccountConsts.ACCOUNT_STATUS_NORMAL);
                    accountMapper.updateByPrimaryKey(account);
                }
            }
        }
    }
    
    @Override
    public int save(Account entity) throws BusinessException
    {
        this.beanValidator(entity);
        if (null == entity.getId())
        {
            entity.setId(SerialnoUtils.buildPrimaryKey());
            entity.setUnid(buildUNID());
            entity.setLoginPwd(EncryptUtils.entryptPassword(entity.getLoginPwd()));
            return accountMapper.insert(entity);
        }
        else
        {
            if (StringUtils.isBlank(entity.getLoginPwd()))
            {
                entity.setLoginPwd(EncryptUtils.entryptPassword(entity.getLoginPwd()));
            }
            return accountMapper.updateByPrimaryKey(entity);
        }
    }
    
    @Override
    public int changeLoginPwd(Long id, String origPass, String newPass) throws BusinessException
    {
        Account entity = accountMapper.selectByPrimaryKey(id);
        if (!EncryptUtils.validatePassword(origPass, entity.getLoginPwd()))
        {// 检验原始密码
            throw new BusinessException(AccountEnums.ACCOUNT_PASSWORD_ERROR);
        }
        if (null != entity && !entity.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        entity.setLoginPwd(EncryptUtils.entryptPassword(newPass));
        return accountMapper.updateByPrimaryKey(entity);
    }
    
    @Override
    public int changeFundPwd(Long id, String fundPwd) throws BusinessException
    {
        Account entity = accountMapper.selectByPrimaryKey(id);
        if (null != entity && !entity.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        entity.setTradePolicy(AccountConsts.TRADE_POLICY_TWOHOUR);
        entity.setWalletPwd(EncryptUtils.entryptPassword(fundPwd));
        return accountMapper.updateByPrimaryKey(entity);
    }
    
    @Override
    public Account getAccountByUnid(Long unid) throws BusinessException
    {
        Account account = accountMapper.findByUnid(unid);
        if (null != account && !account.verifySignature())
        {// 校验数据
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        return account;
    }
    
    /**
     * 生成UNID
     * @return
     */
    protected Long buildUNID()
    {
        Long unid = accountMapper.getMaxUNID();
        if (null != unid)
        {
            unid = unid + 1L;
        }
        else
        {
            unid = BitmsConst.DEFAULT_UNID;
        }
        return unid;
    }
    
    /**
     * 注册初始化钱包账户资产与系统钱包地址信息
     * @param account
     * @return
     */
    private int initAccountAssetAndSystemWalletAddress(Account account) throws BusinessException
    {
        // 注册自动开通钱包账户 BTC
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(account.getId());
        accountWalletAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE); // btc
        accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE); // btc
        accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        accountWalletAsset.setPrice(BigDecimal.ONE);
        accountWalletAsset.setAmount(BigDecimal.ZERO);
        accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
        accountWalletAsset.setRemark("注册自动开通钱包账户 BTC");
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWalletAssetService.insert(accountWalletAsset);
        // 注册自动开通钱包账户 EUR
        accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(account.getId());
        accountWalletAsset.setStockinfoId(FundConsts.WALLET_EUR_TYPE); // eur
        accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_EUR_TYPE); // eur
        accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        accountWalletAsset.setPrice(BigDecimal.ONE);
        accountWalletAsset.setAmount(BigDecimal.ZERO);
        accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
        accountWalletAsset.setRemark("注册自动开通钱包账户 EUR");
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWalletAssetService.insert(accountWalletAsset);
        // 注册自动开通钱包账户 BIEX
        accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(account.getId());
        accountWalletAsset.setStockinfoId(FundConsts.WALLET_BIEX_TYPE); // biex
        accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_BIEX_TYPE); // biex
        accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        accountWalletAsset.setPrice(BigDecimal.ONE);
        accountWalletAsset.setAmount(BigDecimal.ZERO);
        accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
        accountWalletAsset.setRemark("注册自动开通钱包账户 BIEX");
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWalletAssetService.insert(accountWalletAsset);
        // 注册自动开通钱包账户 ETH
        accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(account.getId());
        accountWalletAsset.setStockinfoId(FundConsts.WALLET_ETH_TYPE); // eth
        accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_ETH_TYPE); // eth
        accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        accountWalletAsset.setPrice(BigDecimal.ONE);
        accountWalletAsset.setAmount(BigDecimal.ZERO);
        accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
        accountWalletAsset.setRemark("注册自动开通钱包账户 ETH");
        accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountWalletAssetService.insert(accountWalletAsset);
        // 注册自动开通钱包账户 SUN
        // accountWalletAsset = new AccountWalletAsset();
        // accountWalletAsset.setAccountId(account.getId());
        // accountWalletAsset.setStockinfoId(FundConsts.WALLET_SUN_TYPE); // sun
        // accountWalletAsset.setRelatedStockinfoId(FundConsts.WALLET_SUN_TYPE); // sun
        // accountWalletAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        // accountWalletAsset.setPrice(BigDecimal.ONE);
        // accountWalletAsset.setAmount(BigDecimal.ZERO);
        // accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
        // accountWalletAsset.setRemark("注册自动开通钱包账户 SUN");
        // accountWalletAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        // accountWalletAssetService.insert(accountWalletAsset);
        // ---
        // 注册自动开通现货账户 BTC
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(account.getId());
        accountSpotAsset.setStockinfoId(FundConsts.WALLET_BTC_TYPE); // usd
        accountSpotAsset.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE); // usd
        accountSpotAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        accountSpotAsset.setPrice(BigDecimal.ONE);
        accountSpotAsset.setAmount(BigDecimal.ZERO);
        accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
        accountSpotAsset.setRemark("注册自动开通现货账户 BTC");
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountSpotAssetService.insert(accountSpotAsset);
        // 注册自动开通现货账户 USD
        accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(account.getId());
        accountSpotAsset.setStockinfoId(FundConsts.WALLET_USD_TYPE); // usd
        accountSpotAsset.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE); // usd
        accountSpotAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        accountSpotAsset.setPrice(BigDecimal.ONE);
        accountSpotAsset.setAmount(BigDecimal.ZERO);
        accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
        accountSpotAsset.setRemark("注册自动开通现货账户 USD");
        accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        accountSpotAssetService.insert(accountSpotAsset);
        // ---
        // 注册自动开通现货理财账户 USD
        // AccountWealthAsset accountWealthAsset = new AccountWealthAsset();
        // accountWealthAsset.setWealthAccountId(account.getId());
        // accountWealthAsset.setIssuerAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
        // accountWealthAsset.setStockinfoId(FundConsts.WALLET_USD_TYPE); // usd
        // accountWealthAsset.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE); // usd
        // accountWealthAsset.setWealthAmt(BigDecimal.ZERO);
        // accountWealthAsset.setAccumulateInterest(BigDecimal.ZERO);
        // accountWealthAsset.setLastInterestDay(0L);
        // accountWealthAsset.setRemark("注册自动开通现货理财账户 USD");
        // accountWealthAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        // accountWealthAssetService.insert(accountWealthAsset);
        // // 杠杆现货类品种
        // StockInfo stockInfoSelect = new StockInfo();
        // stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        // stockInfoSelect.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        // List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
        // if (stockInfoList.size() > 0) {
        // for (int i = 0; i < stockInfoList.size(); i++) {
        // // stockInfo
        // StockInfo stockInfo = stockInfoList.get(i);
        // // 现货标的
        // accountSpotAsset = new AccountSpotAsset();
        // accountSpotAsset.setAccountId(account.getId());
        // accountSpotAsset.setStockinfoId(stockInfo.getId());
        // accountSpotAsset.setRelatedStockinfoId(FundConsts.WALLET_BTC_TYPE);
        // accountSpotAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        // accountSpotAsset.setPrice(BigDecimal.ONE);
        // accountSpotAsset.setAmount(BigDecimal.ZERO);
        // accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
        // accountSpotAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        // accountSpotAssetService.insert(accountSpotAsset);
        // }
        // }
        // // 现货合约类品种
        // stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        // stockInfoSelect.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        // stockInfoList = stockInfoService.findList(stockInfoSelect);
        // // 注册自动开通交易账户 BTC/USDX USDZ/BTC
        // if (stockInfoList.size() > 0) {
        // for (int i = 0; i < stockInfoList.size(); i++) {
        // // stockInfo
        // StockInfo stockInfo = stockInfoList.get(i);
        // // 交易标的
        // AccountContractAsset accountContractAsset = new AccountContractAsset();
        // accountContractAsset.setAccountId(account.getId());
        // accountContractAsset.setStockinfoId(stockInfo.getTradeStockinfoId());
        // accountContractAsset.setRelatedStockinfoId(stockInfo.getId());
        // accountContractAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        // accountContractAsset.setPrice(BigDecimal.ONE);
        // accountContractAsset.setAmount(BigDecimal.ZERO);
        // accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
        // accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        // accountContractAsset.setTableName(stockInfo.getTableAsset());
        // accountContractAssetService.insert(accountContractAsset);
        // //资金计价标的
        // accountContractAsset = new AccountContractAsset();
        // accountContractAsset.setAccountId(account.getId());
        // accountContractAsset.setStockinfoId(stockInfo.getCapitalStockinfoId());
        // accountContractAsset.setRelatedStockinfoId(stockInfo.getId());
        // accountContractAsset.setDirection(FundConsts.ASSET_DIRECTION_LONG);
        // accountContractAsset.setPrice(BigDecimal.ONE);
        // accountContractAsset.setAmount(BigDecimal.ZERO);
        // accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
        // accountContractAsset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        // accountContractAsset.setTableName(stockInfo.getTableAsset());
        // accountContractAssetService.insert(accountContractAsset);
        // }
        // }
        /**
        // 注册自动开通钱包账户 LTC
        accountSpotAsset.setId(null);
        accountSpotAsset.setStockinfoId(FundConsts.WALLET_LTC_TYPE);
        accountSpotAssetService.insert(accountSpotAsset);
        // 注册自动开通钱包账户 ETH
        accountSpotAsset.setId(null);
        accountSpotAsset.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
        accountSpotAssetService.insert(accountSpotAsset);
        // 注册自动开通钱包账户 BMS
        accountSpotAsset.setId(null);
        accountSpotAsset.setStockinfoId(FundConsts.WALLET_BMS_TYPE);
        accountSpotAssetService.insert(accountSpotAsset);
        // 默认产生BTC钱包地址信息
        systemWalletAddrService.createBtWalletAddress(BitmsConst.BITGO_WALLET_NAME, account.getId(), BitmsConst.DEFAULT_UNID);
         */
        return 0;
    }
    
    /**
     * 异常账户批量冻结，冻结后的账户需要手工解冻
     * @param accountIdList
     * @return
     */
    @Override
    public int abNormalAcctFrozenBatch(List<Long> accountIdList) throws BusinessException
    {
        Long currentLong = CalendarUtils.getCurrentLong();
        List<Account> acctList = findListByAcctIds(accountIdList);
        // 根据异常账户ID列表获取相应账户列表
        if (CollectionUtils.isEmpty(acctList)) return 0;
        for (Account account : acctList)
        {
            account.setStatus(AccountConsts.ACCOUNT_STATUS_FROZEN);
            account.setUpdateDate(currentLong);
            account.setRemark("监控发现的异常账户,暂停账户交易，需手工解冻");
        }
        return accountMapper.updateBatch(acctList);
    }
    
    /**
     * 根据账户ID列表获取相关账户列表
     * @param accountIdList
     * @return
     * @throws BusinessException
     */
    @Override
    public List<Account> findListByAcctIds(List<Long> accountIdList) throws BusinessException
    {
        if (CollectionUtils.isEmpty(accountIdList)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        return accountMapper.findListByAcctIds(accountIdList);
    }
}
