/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.entity.AccountSpotAsset;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.mapper.AccountAssetMapper;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import com.blocain.bitms.trade.fund.model.ContractAssetModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账户资产
 * <p>File：AccountAssetServiceImpl.java</p>
 * <p>Title: AccountAssetServiceImpl</p>
 * <p>Description:AccountAssetServiceImpl</p>
 * <p>Copyright: Copyright (c) 2017年7月19日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Service
public class AccountAssetServiceImpl extends GenericServiceImpl<AccountAssetModel> implements AccountAssetService
{
    protected static final Logger   logger = LoggerFactory.getLogger(AccountAssetServiceImpl.class);

    AccountAssetMapper accountAssetMapper;
    
    @Autowired
    public AccountAssetServiceImpl(AccountAssetMapper accountAssetMapper)
    {
        super(accountAssetMapper);
        this.accountAssetMapper = accountAssetMapper;
    }
    
    @Autowired
    private AccountDebitAssetService        accountDebitAssetService;

    @Autowired
    private AccountContractAssetService     accountContractAssetService;

    @Autowired
    private AccountWalletAssetService       accountWalletAssetService;

    @Autowired
    private AccountSpotAssetService         accountSpotAssetService;

    @Autowired
    private StockInfoService                stockInfoService;

    @Autowired
    private RtQuotationInfoService          rtQuotationInfoService;

    @Override
    public BigDecimal getNetAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {

        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        if(stockInfo==null)
        {
            logger.debug("证券ID不存在，非法下单");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            // 查询借款
            AccountDebitAsset debitRecord = new AccountDebitAsset();
            debitRecord.setStockinfoId(stockinfoId);
            debitRecord.setRelatedStockinfoId(relatedStockinfoId);
            debitRecord.setBorrowerAccountId(accountId);
            debitRecord.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            List<AccountDebitAsset> debitList = accountDebitAssetService.findList(debitRecord);
            BigDecimal debitBorrow = BigDecimal.ZERO;
            if (debitList.size() > 0)
            {
                debitRecord = debitList.get(0);
                debitBorrow = debitRecord.getDebitAmt(); // 已借款金额
            }
            logger.debug("借款情况：" + debitRecord.toString());
            // 查询资产
            AccountContractAsset accountContractAsset = new AccountContractAsset();
            accountContractAsset.setAccountId(accountId);
            accountContractAsset.setStockinfoId(stockinfoId);
            accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
            accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
            List<AccountContractAsset> assetList = accountContractAssetService.findList(accountContractAsset);
            BigDecimal assetAmount = BigDecimal.ZERO;
            if (assetList.size() > 0)
            {
                accountContractAsset = assetList.get(0);
                assetAmount = accountContractAsset.getAmount();
            }
            logger.debug("资产情况：" + debitRecord.toString());
            BigDecimal netAsset = assetAmount.subtract(debitBorrow);
            logger.debug("账户：" + accountId + " " + stockinfoId + "净值：" + netAsset);
            return netAsset;
        }
        else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            // 查询借款
            AccountDebitAsset debitRecord = new AccountDebitAsset();
            debitRecord.setStockinfoId(stockinfoId);
            debitRecord.setRelatedStockinfoId(getStockInfo(relatedStockinfoId).getCapitalStockinfoId());
            debitRecord.setBorrowerAccountId(accountId);
            debitRecord.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            List<AccountDebitAsset> debitList = accountDebitAssetService.findList(debitRecord);
            BigDecimal debitBorrow = BigDecimal.ZERO;
            if (debitList.size() > 0)
            {
                debitRecord = debitList.get(0);
                debitBorrow = debitRecord.getDebitAmt(); // 已借款金额
            }
            logger.debug("借款情况：" + debitRecord.toString());
            // 查询资产
            AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
            accountSpotAsset.setAccountId(accountId);
            accountSpotAsset.setStockinfoId(stockinfoId);
            accountSpotAsset.setRelatedStockinfoId(getStockInfo(relatedStockinfoId).getCapitalStockinfoId());
            List<AccountSpotAsset> assetList = accountSpotAssetService.findList(accountSpotAsset);
            BigDecimal assetAmount = BigDecimal.ZERO;
            if (assetList.size() > 0)
            {
                accountSpotAsset = assetList.get(0);
                assetAmount = accountSpotAsset.getAmount();
            }
            logger.debug("资产情况：" + debitRecord.toString());
            BigDecimal netAsset = assetAmount.subtract(debitBorrow);
            logger.debug("账户：" + accountId + " " + stockinfoId + "净值：" + netAsset);
            return netAsset;
        }
        else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
        {
            BigDecimal debitBorrow = BigDecimal.ZERO;
            // 查询资产
            AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
            accountWalletAsset.setAccountId(accountId);
            accountWalletAsset.setStockinfoId(stockinfoId);
            accountWalletAsset.setRelatedStockinfoId(getStockInfo(relatedStockinfoId).getCapitalStockinfoId());
            List<AccountWalletAsset> assetList = accountWalletAssetService.findList(accountWalletAsset);
            BigDecimal assetAmount = BigDecimal.ZERO;
            if (assetList.size() > 0)
            {
                accountWalletAsset = assetList.get(0);
                assetAmount = accountWalletAsset.getAmount();
            }
            BigDecimal netAsset = assetAmount.subtract(debitBorrow);
            logger.debug("账户：" + accountId + " " + stockinfoId + "净值：" + netAsset);
            return netAsset;
        }
        else
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }

    @Override
    public BigDecimal getAccountNetAsset(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        return getAccountNetAssetFromDB(accountId, stockinfoId, relatedStockinfoId);
    }

    @Override
    public BigDecimal getProfitAndLoss(Long accountId, Long stockinfoId, Long relatedStockinfoId) throws BusinessException
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        if (StringUtils.equalsIgnoreCase(stockInfo.getCanBorrow(), FundConsts.PUBLIC_STATUS_YES))
        {
            // 账户BTC净值
            BigDecimal btcNetValue = getAccountNetAssetFromDB(accountId, stockinfoId, relatedStockinfoId);
            // 当前账户-当前BTC持仓数量 tableName已在服务层处理
            ContractAssetModel modelPerson = accountContractAssetService.findAccountSumContractAsset(stockinfoId, relatedStockinfoId, accountId);
            BigDecimal allBtcOfPersion = modelPerson.getSumAmount();
            logger.debug("当前账户" + accountId + ":" + allBtcOfPersion);
            BigDecimal allBtcSumInitialOfPersion = modelPerson.getSumInitialAmt();
            // 当前账户-总流入数字货币数量
            logger.debug("当前账户" + accountId + "--总流入数字货币数量:" + modelPerson.getSumFlowInAmt());
            // 当前账户-总流出数字货币数量
            logger.debug("当前用户" + accountId + "-总流出数字货币数量:" + modelPerson.getSumFlowOutAmt());
            BigDecimal profitAndLoss = btcNetValue.subtract(allBtcSumInitialOfPersion).subtract(modelPerson.getSumFlowInAmt()).add(modelPerson.getSumFlowOutAmt());
            return profitAndLoss;
        }
        else
        {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public PaginateResult<AccountAssetModel> findAssetList(Pagination pagination, AccountAssetModel accountAssetModel, List<Map<String,Object>> tableNames) throws BusinessException
    {
        accountAssetModel.setPagin(pagination);
        List<AccountAssetModel> fundCurrentList = accountAssetMapper.findAssetList(accountAssetModel,tableNames);
        return new PaginateResult<>(pagination, fundCurrentList);
    }

    @Override
    public List<AccountAssetModel> findAssetList(AccountAssetModel accountAssetModel, List<Map<String, Object>> tableNames) throws BusinessException {
        List<AccountAssetModel> fundCurrentList = accountAssetMapper.findAssetList(accountAssetModel,tableNames);
        return fundCurrentList;
    }

    @Override
    public AccountAssetModel findAssetAndDebitForAccount(AccountAssetModel accountAssetModel)
    {
        StockInfo stockInfo = getStockInfo(accountAssetModel.getRelatedStockinfoId());
        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            accountAssetModel.setTableAsset(stockInfo.getTableAsset());
            accountAssetModel.setTableDebitAsset(stockInfo.getTableDebitAsset());
        }else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            accountAssetModel.setTableAsset(stockInfo.getTableAsset());
            accountAssetModel.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            accountAssetModel.setTableDebitAsset(stockInfo.getTableDebitAsset());
        }
        return accountAssetMapper.findAssetAndDebitForAccount(accountAssetModel);

    }
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

    /**
     * 查询账户交易对净资产
     * @param accountId
     * @param stockinfoId
     * @param relatedStockinfoId
     * @return
     */
    private BigDecimal getAccountNetAssetFromDB(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(relatedStockinfoId);
        if (StringUtils.equalsIgnoreCase(stockInfo.getCanBorrow(), FundConsts.PUBLIC_STATUS_YES))
        {
            EnableModel enableModel = new EnableModel();
            enableModel.setAccountId(accountId);
            enableModel.setStockinfoId(stockinfoId);
            enableModel.setRelatedStockinfoId(relatedStockinfoId);
            enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(stockinfoId, relatedStockinfoId);
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("当前内部行情：" + platPrice);
            if (platPrice == null)
            {
                logger.debug("行情异常！查不到数据");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if (platPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                logger.debug("行情异常！行情为0");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            BigDecimal usdxLever = stockInfo.getMaxLongLever();
            logger.debug("USDX最大可贷款杠杆倍数:" + usdxLever);
            BigDecimal btcLever = stockInfo.getMaxShortLever();
            logger.debug("BTC最大可贷款杠杆倍数:" + btcLever);
            // 查询USDX借款情况
            AccountDebitAsset record = new AccountDebitAsset();
            record.setStockinfoId(relatedStockinfoId);
            record.setRelatedStockinfoId(relatedStockinfoId);
            record.setBorrowerAccountId(accountId);
            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            List<AccountDebitAsset> list = accountDebitAssetService.findList(record);
            BigDecimal usdxBorrow = BigDecimal.ZERO;
            if (list.size() > 0)
            {
                record = list.get(0);
                usdxBorrow = record.getDebitAmt(); // 已借款金额
            }
            logger.debug("usdxBorrow=" + usdxBorrow);
            // 查询BTC借款情况
            record = new AccountDebitAsset();
            record.setStockinfoId(stockinfoId);
            record.setRelatedStockinfoId(relatedStockinfoId);
            record.setBorrowerAccountId(accountId);
            record.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
            list = accountDebitAssetService.findList(record);
            BigDecimal btcBorrow = BigDecimal.ZERO;
            if (list.size() > 0)
            {
                record = list.get(0);
                btcBorrow = record.getDebitAmt(); // 已借款BTC
            }
            logger.debug("btcBorrow=" + btcBorrow);
            // USDX可用查询
            EnableModel enableModel2 = new EnableModel();
            enableModel2.setAccountId(accountId);
            enableModel2.setStockinfoId(relatedStockinfoId);
            enableModel2.setRelatedStockinfoId(relatedStockinfoId);
            enableModel2 = this.findEnableAmtFormAccountContractAssetDB(enableModel2);
            BigDecimal usdxEnable = enableModel2.getEnableAmount();
            BigDecimal usdxFrozen = enableModel2.getFrozenAmt();
            logger.debug("usdxEnable=" + usdxEnable);
            logger.debug("usdxFrozen=" + usdxFrozen);
            // BTC可用查询
            enableModel2 = enableModel;
            BigDecimal btcEnable = enableModel2.getEnableAmount();
            BigDecimal btcFrozen = enableModel2.getFrozenAmt();
            logger.debug("btcEnable=" + btcEnable);
            logger.debug("btcFrozen=" + btcFrozen);
            BigDecimal vcoinNet = btcEnable.add(btcFrozen);
            BigDecimal moneyNet = usdxEnable.add(usdxFrozen);
            BigDecimal vcoinDebit = btcBorrow;
            BigDecimal moneyDebit = usdxBorrow;
            // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
            BigDecimal accountVcoinNetAmt = BigDecimal.ZERO;
            accountVcoinNetAmt = vcoinNet.subtract(vcoinDebit).add((moneyNet.subtract(moneyDebit)).divide(platPrice, 12, BigDecimal.ROUND_HALF_UP));
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountVcoinNetAmt);

           // 账户USDX余额 其实就是账户USDX可用
            BigDecimal usdxAmtNet = usdxEnable.subtract(usdxBorrow); // usdxEnable.add(usdxFrozen).subtract(usdxBorrow);
            logger.debug("usdxAmtNet=" + usdxAmtNet);
            // 账户BTC余额 其实就是账户BTC可用
            BigDecimal btcAmtNet = btcEnable.add(btcFrozen).subtract(btcBorrow); // btcEnable.add(btcFrozen).subtract(btcBorrow);
            logger.debug("btcAmtNet=" + btcAmtNet);
            // 账户BTC净值
            BigDecimal btcNetValue = BigDecimal.ZERO;
            if (platPrice.compareTo(BigDecimal.ZERO) != 0)
            {
                btcNetValue = (usdxAmtNet).divide(platPrice, 12, BigDecimal.ROUND_HALF_UP).add(btcAmtNet);
            }
            return btcNetValue;
        }
        else
        {
            return BigDecimal.ZERO;
        }
    }

    private EnableModel findEnableAmtFormAccountContractAssetDB(EnableModel enableModel)
    {
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(enableModel.getAccountId());
        accountContractAsset.setStockinfoId(enableModel.getStockinfoId());
        accountContractAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        logger.debug("从合约账户资产db中查找可用数量 accountContractAsset:" + accountContractAsset.toString());
        List<AccountContractAsset> accountContractAssetList;
        try
        {
            accountContractAssetList = accountContractAssetService.findList(accountContractAsset);
            if (accountContractAssetList.size() > 0)
            {
                accountContractAsset = accountContractAssetList.get(0);
                // 如果冻结数量小于等于 直接取0 防止风险
                if (accountContractAsset.getFrozenAmt().compareTo(BigDecimal.ZERO) <= 0)
                {
                    accountContractAsset.setFrozenAmt(BigDecimal.ZERO);
                }
                enableModel.setEnableAmount(accountContractAsset.getAmount().subtract(accountContractAsset.getFrozenAmt()));
                enableModel.setFrozenAmt(accountContractAsset.getFrozenAmt());
            }
            else
            {
                enableModel.setEnableAmount(BigDecimal.ZERO);
                enableModel.setFrozenAmt(BigDecimal.ZERO);
            }
        }
        catch (Exception e)
        {
            enableModel.setEnableAmount(BigDecimal.ZERO);
            enableModel.setFrozenAmt(BigDecimal.ZERO);
            logger.error("从合约账户资产db中查找可用数量 error:" + e.getMessage());
        }
        logger.debug("从合约账户资产db中查找可用数量 enableModel:" + enableModel.toString());
        return enableModel;
    }

    @Override
    public void contract2WalletEnable(FundModel fundModel) throws BusinessException
    {
        // BTC可用=BTC余额-BTC冻结-BTC借款
        BigDecimal btcEnable = this.getEnableSubFrozenSubDebit(fundModel.getAccountId(),fundModel.getStockinfoId(),fundModel.getStockinfoIdEx());
        if (btcEnable.compareTo(BigDecimal.ZERO) <= 0)
        {
            // 委托可用不足异常
            logger.debug("BTC可用=BTC余额-BTC冻结-BTC借款 正常可用不足");
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
        // USDX可用=USDX余额-USDX冻结-USDX借款
        BigDecimal usdxEnable = this.getEnableSubFrozenSubDebit(fundModel.getAccountId(),fundModel.getStockinfoIdEx(),fundModel.getStockinfoIdEx());

        if (usdxEnable.compareTo(BigDecimal.ZERO) < 0)
        {
            // 委托可用不足异常  USDX存在负债，无法转出。
            logger.debug("USDX可用=USDX余额-USDX冻结-USDX借款 正常可用不足");
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(fundModel.getStockinfoIdEx());
            throw new BusinessException(CommonEnums.ERROR_DEBIT_GT_ZREO,stockInfo.getStockCode());
        }
        // 账户净值(账户权益)
        BigDecimal  accountNetAsset=  this.getAccountNetAsset(fundModel.getAccountId(),fundModel.getStockinfoId(),fundModel.getStockinfoIdEx());
        if(btcEnable.compareTo(accountNetAsset) > 0 )
        {
            btcEnable = accountNetAsset;
            logger.debug("BTC可用>账户权益  BTC可用=账户权益 ");
        }
        // 周盈亏
        BigDecimal  profitAndLoss=  this.getProfitAndLoss(fundModel.getAccountId(),fundModel.getStockinfoId(),fundModel.getStockinfoIdEx());
        logger.debug("用户的交易对资产净值："+accountNetAsset+" 周盈亏："+profitAndLoss);
        if(profitAndLoss.compareTo(BigDecimal.ZERO)<0){profitAndLoss=BigDecimal.ZERO;}
        if((btcEnable.subtract(profitAndLoss)).compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0)
        {
            logger.debug("BTC可用>0 USDX可用<0 BTC可用-当周盈利 小于 提币金额：提示：可用余额不足");
            throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE);
        }
    }

    @Override
    public BigDecimal contract2WalletEnableAmount(FundModel fundModel) throws BusinessException
    {
        // BTC可用=BTC余额-BTC冻结-BTC借款
        BigDecimal btcEnable = this.getEnableSubFrozenSubDebit(fundModel.getAccountId(),fundModel.getStockinfoId(),fundModel.getStockinfoIdEx());
        if (btcEnable.compareTo(BigDecimal.ZERO) <= 0)
        {
            logger.debug("BTC可用=BTC余额-BTC冻结-BTC借款 正常可用不足");
            return BigDecimal.ZERO;
        }
        logger.debug("BTC可用=BTC余额-BTC冻结-BTC借款="+btcEnable);
        // USDX可用=USDX余额-USDX冻结-USDX借款
        BigDecimal usdxEnable = this.getEnableSubFrozenSubDebit(fundModel.getAccountId(),fundModel.getStockinfoIdEx(),fundModel.getStockinfoIdEx());
        if (usdxEnable.compareTo(BigDecimal.ZERO) < 0)
        {
            logger.debug("USDX可用=USDX余额-USDX冻结-USDX借款 正常可用不足");
            return BigDecimal.ZERO;
        }
        logger.debug("USDX可用=USDX余额-USDX冻结-USDX借款="+usdxEnable);
        // 账户净值(账户权益)
        BigDecimal  accountNetAsset=  this.getAccountNetAsset(fundModel.getAccountId(),fundModel.getStockinfoId(),fundModel.getStockinfoIdEx());
        logger.debug("账户权益="+accountNetAsset);
        if(btcEnable.compareTo(accountNetAsset) > 0 )
        {
            btcEnable = accountNetAsset;
            logger.debug("BTC可用>账户权益  BTC可用=账户权益 ");
        }
        // 周盈亏
        BigDecimal  profitAndLoss=  this.getProfitAndLoss(fundModel.getAccountId(),fundModel.getStockinfoId(),fundModel.getStockinfoIdEx());
        logger.debug("用户的交易对资产净值："+accountNetAsset+" 周盈亏："+profitAndLoss);
        if((btcEnable.subtract(profitAndLoss)).compareTo(BigDecimal.ZERO) < 0)
        {
            logger.debug("BTC可用>0 USDX可用<0 BTC可用-当周盈利 小于 0 ：提示：可用余额不足");
            return BigDecimal.ZERO;
        }
        if(profitAndLoss.compareTo(BigDecimal.ZERO)<0){profitAndLoss=BigDecimal.ZERO;}
        return btcEnable.subtract(profitAndLoss);
    }

    public BigDecimal getEnableSubFrozenSubDebit(Long accountId, Long stockinfoId, Long relatedStockinfoId)
    {
        // 查询借款
        AccountDebitAsset debitRecord = new AccountDebitAsset();
        debitRecord.setStockinfoId(stockinfoId);
        debitRecord.setRelatedStockinfoId(relatedStockinfoId);
        debitRecord.setBorrowerAccountId(accountId);
        debitRecord.setTableName(getStockInfo(relatedStockinfoId).getTableDebitAsset());
        List<AccountDebitAsset> debitList = accountDebitAssetService.findList(debitRecord);
        BigDecimal debitBorrow = BigDecimal.ZERO;
        if (debitList.size() > 0)
        {
            debitRecord = debitList.get(0);
            debitBorrow = debitRecord.getDebitAmt(); // 已借款金额
        }
        logger.debug("借款情况：" + debitRecord.toString());
        // 查询资产
        AccountContractAsset accountContractAsset = new AccountContractAsset();
        accountContractAsset.setAccountId(accountId);
        accountContractAsset.setStockinfoId(stockinfoId);
        accountContractAsset.setRelatedStockinfoId(relatedStockinfoId);
        accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
        List<AccountContractAsset> assetList = accountContractAssetService.findList(accountContractAsset);
        BigDecimal assetAmount = BigDecimal.ZERO;
        BigDecimal assetFrozen = BigDecimal.ZERO;
        if (assetList.size() > 0)
        {
            accountContractAsset = assetList.get(0);
            assetAmount = accountContractAsset.getAmount();
            assetFrozen = accountContractAsset.getFrozenAmt();
        }
        logger.debug("资产情况：" + debitRecord.toString());
        BigDecimal netAsset = assetAmount.subtract(assetFrozen).subtract(debitBorrow);
        logger.debug("账户：" + accountId + " " + stockinfoId + "净值：" + netAsset);
        return netAsset;
    }
}
