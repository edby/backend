/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.service;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.DateUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.fund.service.FundService;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.stockinfo.mapper.Erc20TokenMapper;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * ERC20 TOKEN 服务实现类
 * <p>File：Erc20TokenServiceImpl.java </p>
 * <p>Title: Erc20TokenServiceImpl </p>
 * <p>Description:Erc20TokenServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Service
public class Erc20TokenServiceImpl extends GenericServiceImpl<Erc20Token> implements Erc20TokenService {

    protected Erc20TokenMapper erc20TokenMapper;

    @Autowired
    StockInfoService stockInfoService;

    @Autowired
    StockRateService stockRateService;

    @Autowired
    Erc20TokenService erc20TokenService;        // 事务

    @Autowired
    EnableService enableService;

    @Autowired
    SysParameterService sysParameterService;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountWalletAssetService accountWalletAssetService;

    @Autowired
    FundService fundService;

    @Autowired
    public Erc20TokenServiceImpl(Erc20TokenMapper erc20TokenMapper) {
        super(erc20TokenMapper);
        this.erc20TokenMapper = erc20TokenMapper;
    }

    @Override
    public Erc20Token getErc20Token(String contractAddr, String pair) {
        return erc20TokenMapper.getErc20Token(contractAddr, pair);
    }

    @Override
    public void doActiveToken(String addr, Long accountId, Long unid) throws BusinessException {
        Erc20Token erc20Token = erc20TokenMapper.getErc20Token(addr, null);
        if (erc20Token == null) {
            throw new BusinessException("Token not exist");
        }
        try {
            erc20TokenMapper.selectByPrimaryKeyForUpdate(erc20Token.getId());
        } catch (Exception e) {
            throw new BusinessException("The system is so busy,please try again later.");
        }
        Account inviter = null;
        if (unid != null) {
            inviter = accountService.getAccountByUnid(unid);
            try {
                if (null == inviter) {
                    throw new BusinessException("The inviter does not exist");
                }
            } catch (Exception e) {
                throw new BusinessException("The inviter does not exist");

            }
            if (inviter.getId().longValue() == accountId.longValue()) {
                throw new BusinessException("Error:The inviter can't be yourself.");
            }
            if (erc20Token.getAwardStatus().intValue() != 0) {
                throw new BusinessException("Error:Has been rewarded or not required to be rewarded");
            } else {
                erc20Token.setInviteAccountId(inviter.getId());
                erc20Token.setNeedAward(1);
                erc20Token.setAwardStatus(1);
            }
        } else {
            erc20Token.setNeedAward(-1);
            erc20Token.setAwardStatus(-1);
        }
        if(erc20Token.getActiveAccountId() == null)
        {
            erc20Token.setActiveAccountId(accountId);
        }
        if (StringUtils.equalsIgnoreCase(erc20Token.getIsActive(), FundConsts.PUBLIC_STATUS_YES)) {
            throw new BusinessException("Already activated");
        }
        StockInfo stockInfoPair = stockInfoService.findByContractAddr(addr);
        if (stockInfoPair == null) {
            // 不存在合约地址 则从未激活中取得一条
            stockInfoPair = stockInfoService.findOneNotActive();
            if (stockInfoPair == null) {
                throw new BusinessException("The quota has been used up today,Please try again tomorrow.");
            }
        }
        if (StringUtils.equalsIgnoreCase(stockInfoPair.getIsActive(), FundConsts.PUBLIC_STATUS_YES)) {
            throw new BusinessException("Already activated");
        }
        StockInfo stockInfoCoin = stockInfoService.selectByPrimaryKey(stockInfoPair.getTradeStockinfoId());
        if (stockInfoCoin == null) {
            throw new BusinessException("Basic data is null(stockInfoCoin)");
        }
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountId);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BIEX_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.ERC20TOKEN_PAIR_ACTIVE_FEE);
        params = sysParameterService.getSysParameterByEntity(params);
        BigDecimal limit = BigDecimal.valueOf(Double.parseDouble(params.getValue()));
        if (limit.compareTo(enableModel.getEnableAmount()) > 0) {
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // 扣除手续费
        FundModel fundModel = new FundModel();
        fundModel.setAccountId(accountId);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BIEX_TYPE);
        fundModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_ACTIVE_TOKEN_TRADE_FEE);
        if (erc20Token.getAwardStatus().intValue() == 1 && erc20Token.getNeedAward().intValue() == 1) {
            limit = limit.multiply(BigDecimal.valueOf(0.95));
        }
        fundModel.setAmount(limit);
        fundService.fundTransaction(fundModel);

        // 进行奖励操作
        if (erc20Token.getAwardStatus().intValue() == 1 && erc20Token.getNeedAward().intValue() == 1) {
            params = new SysParameter();
            params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
            params.setParameterName(ParamConsts.ERC20TOKEN_PAIR_ACTIVE_AWARD);
            params = sysParameterService.getSysParameterByEntity(params);
            BigDecimal award = BigDecimal.valueOf(Double.parseDouble(params.getValue()));
            if (limit.compareTo(enableModel.getEnableAmount()) > 0) {
                throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
            }

            erc20Token.setAwardAmount(award);
            fundModel = new FundModel();
            fundModel.setAccountId(inviter.getId());
            fundModel.setStockinfoIdEx(FundConsts.WALLET_BIEX_TYPE);
            fundModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_ACTIVE_TOKEN_TRADE_AWARD);
            fundModel.setAmount(award);
            fundService.fundTransaction(fundModel);
        }
        // 证券信息处理-交易币种
        stockInfoCoin.setStockCode(erc20Token.getSymbol().toUpperCase());
        stockInfoCoin.setStockName(erc20Token.getSymbolName());
        stockInfoCoin.setRemark(erc20Token.getSymbol().toLowerCase());
        stockInfoCoin.setTokenContactAddr(addr);
        stockInfoCoin.setTokenDecimals(BigDecimal.valueOf(erc20Token.getTokenDecimals()));
        stockInfoCoin.setCanTrade(FundConsts.PUBLIC_STATUS_YES);
        stockInfoCoin.setCanRecharge(FundConsts.PUBLIC_STATUS_YES);
        stockInfoCoin.setCanWithdraw(FundConsts.PUBLIC_STATUS_YES);
        stockInfoCoin.setIsActive(FundConsts.PUBLIC_STATUS_YES);
        stockInfoService.updateByPrimaryKeySelective(stockInfoCoin);
        List<Account> list = accountService.selectAll();
        for (Account account : list) {
            checkWalletAsset(account.getId(), stockInfoCoin.getId());
        }
        // 证券信息处理-交易对
        stockInfoPair.setStockCode(erc20Token.getPair().toUpperCase());
        stockInfoPair.setStockName(erc20Token.getPair().toUpperCase());
        stockInfoPair.setTradeAmtUnit(erc20Token.getSymbol().toUpperCase());
        stockInfoPair.setTradeAmtSymbol(erc20Token.getSymbol().toUpperCase());
        stockInfoPair.setCapitalAmtUnit("ETH");
        stockInfoPair.setCapitalAmtSymbol("ETH");
        stockInfoPair.setTokenContactAddr(addr);
        stockInfoPair.setCanTrade(FundConsts.PUBLIC_STATUS_YES);
        stockInfoPair.setCanTrade(FundConsts.PUBLIC_STATUS_YES);
        stockInfoPair.setCanRecharge(FundConsts.PUBLIC_STATUS_YES);
        stockInfoPair.setCanWithdraw(FundConsts.PUBLIC_STATUS_YES);
        stockInfoPair.setTokenDecimals(BigDecimal.valueOf(erc20Token.getTokenDecimals()));
        stockInfoPair.setRemark(erc20Token.getPair().toLowerCase());
        stockInfoPair.setIsActive(FundConsts.ASSET_LOCK_STATUS_YES);
        stockInfoService.updateByPrimaryKeySelective(stockInfoPair);
        // erc20Token更新
        erc20Token.setIsActive(FundConsts.ASSET_LOCK_STATUS_YES);
        Date date = new Date();
        erc20Token.setActiveEndDate(DateUtils.addYears(date, 1));
        erc20TokenMapper.updateByPrimaryKey(erc20Token);
        // 提现手续费检查和设置
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(stockInfoCoin.getId());
        stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
        List<StockRate> feerateList = stockRateService.findList(stockRate);
        if (feerateList.size() == 0) {
            stockRate.setRateValueType(2);
            stockRate.setRate(BigDecimal.ZERO);
            stockRate.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
            stockRate.setCreateDate(new Timestamp(System.currentTimeMillis()));
            stockRate.setUpdateBy(FundConsts.SYSTEM_ACCOUNT_ID);
            stockRate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            stockRateService.save(stockRate);
        }
        // 买入交易手续费（得到TOKEN 收取TOKEN手续费）
        stockRate = new StockRate();
        stockRate.setStockinfoId(stockInfoPair.getId());
        stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);
        feerateList = stockRateService.findList(stockRate);
        if (feerateList.size() == 0) {
            stockRate.setRateValueType(1);
            stockRate.setRate(BigDecimal.valueOf(0.001));
            stockRate.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
            stockRate.setCreateDate(new Timestamp(System.currentTimeMillis()));
            stockRate.setUpdateBy(FundConsts.SYSTEM_ACCOUNT_ID);
            stockRate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            stockRateService.save(stockRate);
        }
        // 卖出交易手续费(得到ETH)
        stockRate = new StockRate();
        stockRate.setStockinfoId(stockInfoPair.getId());
        stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);
        feerateList = stockRateService.findList(stockRate);
        if (feerateList.size() == 0) {
            stockRate.setRateValueType(1);
            stockRate.setRate(BigDecimal.ZERO);
            stockRate.setCreateBy(FundConsts.SYSTEM_ACCOUNT_ID);
            stockRate.setCreateDate(new Timestamp(System.currentTimeMillis()));
            stockRate.setUpdateBy(FundConsts.SYSTEM_ACCOUNT_ID);
            stockRate.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            stockRateService.save(stockRate);
        }
    }

    /**
     * 默认给用户添加资产账户
     *
     * @param accountId
     * @param stockinfoId
     */
    public void checkWalletAsset(Long accountId, Long stockinfoId) {
        AccountWalletAsset accountWalletAsset = this.findAccountWalletAssetFormDB(accountId, stockinfoId);
        if (accountWalletAsset == null) {
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

    private AccountWalletAsset findAccountWalletAssetFormDB(Long accountId, Long stockinfoId) {
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(accountId);
        accountWalletAsset.setStockinfoId(stockinfoId);
        List<AccountWalletAsset> list;
        try {
            list = accountWalletAssetService.findList(accountWalletAsset);
            if (list.size() > 0) {
                accountWalletAsset = list.get(0);
            } else {
                accountWalletAsset = null;
            }
        } catch (Exception e) {
            accountWalletAsset = null;
            logger.debug("从db中查找钱包账户资产记录 error:" + e.getMessage());
        }
        if (null == accountWalletAsset) {
            logger.debug("从db中查找钱包账户资产记录 accountWalletAsset is null");
        } else {
            logger.debug("从db中查找钱包账户资产记录 accountWalletAsset:" + accountWalletAsset.toString());
        }
        return accountWalletAsset;
    }

    @Override
    public void autoCloseActiveToken() {
        Erc20Token entity = new Erc20Token();
        entity.setIsActive(FundConsts.ASSET_LOCK_STATUS_YES);
        List<Erc20Token> list = erc20TokenMapper.findList(entity);
        for (Erc20Token token : list) {
            if (DateUtils.getDistanceOfTwoDate(token.getActiveEndDate(), new Date()) >= 0) {
                erc20TokenService.doCloseActiveToken(token);
            }
        }
    }

    @Override
    public void doCloseActiveToken(Erc20Token erc20Token) {
        erc20Token.setIsActive(FundConsts.PUBLIC_STATUS_NO);
        erc20TokenMapper.updateByPrimaryKey(erc20Token);
        StockInfo stockInfo = stockInfoService.findByContractAddr(erc20Token.getContractAddr());
        stockInfo.setIsActive(FundConsts.PUBLIC_STATUS_NO);
        stockInfoService.updateByPrimaryKeySelective(stockInfo);
        stockInfo = stockInfoService.selectByPrimaryKey(stockInfo.getTradeStockinfoId());
        stockInfo.setIsActive(FundConsts.PUBLIC_STATUS_NO);
        stockInfoService.updateByPrimaryKeySelective(stockInfo);
    }

    @Override
    public PaginateResult<Erc20Token> findListForAward(Erc20Token entity)
    {
        Pagination pagin = entity.getPagin();
        if (null == pagin) pagin = new Pagination();
        entity.setPagin(pagin);
        List<Erc20Token> data = erc20TokenMapper.findListForAward(entity);
        for(Erc20Token erc20Token:data)
        {
            String email = erc20Token.getInviter();
            String[] key = email.split("@");
            email = key[0].substring(0,key[0].length()-4)+"****@****"+key[1].substring(4);
            erc20Token.setInviter(email);
        }
        return new PaginateResult<>(pagin, data);
    }

    /**
     * 按条件查询
     * @param symbol
     * @param contractAddr
     * @return
     */
    @Override
    public List<Erc20Token> searchByKey(String symbol, String contractAddr)
    {
        return erc20TokenMapper.searchByKey(symbol,contractAddr);
    }

    /**
     * 查询高度字段最小值记录findinHeight
     * @return
     */
    @Override
    public List<Erc20Token> findMinHeight()
    {
        return erc20TokenMapper.findMinHeight();
    }

}
