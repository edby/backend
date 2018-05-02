/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.risk.service;

import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 可用统一服务接口实现类
 * <p>File：EnableServiceImpl.java </p>
 * <p>Title: EnableServiceImpl </p>
 * <p>Description:EnableServiceImpl </p>
 * <p>Copyright: Copyright (c) May 26, 2017</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
@Service
public class EnableServiceImpl implements EnableService
{
    public static final Logger          logger = LoggerFactory.getLogger(EnableServiceImpl.class);
    
    @Autowired
    private AccountWalletAssetService   accountWalletAssetService;

    @Autowired
    private AccountSpotAssetService     accountSpotAssetService;
    
    @Autowired
    private AccountContractAssetService accountContractAssetService;

    @Autowired
    private AccountWealthAssetService   accountWealthAssetService;
    
    @Autowired
    private AccountDebitAssetService    accountDebitAssetService;
    
    @Autowired
    private StockInfoService            stockInfoService;
    
    @Autowired
    private RtQuotationInfoService      rtQuotationInfoService;
    
    @Override
    public EnableModel instructionTerminalEnable(EnableModel enableModel) throws BusinessException
    {
        return null;
    }
    
    @Override
    public EnableModel entrustTerminalEnable(EnableModel enableModel) throws BusinessException
    {
        logger.debug("交易端可用服务 start--------------------------------------------------");
        logger.debug("交易端可用服务 enableModel:" + enableModel.toString());
        // 入参统一校验判断
        if (null == enableModel.getAccountId() || null == enableModel.getStockinfoId() || null == enableModel.getBusinessFlag())
        {//
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 根据业务类别进行不同的委托端可用判断
        // ICO认购申请 ||ICO预购申请 || ICO认购确认 || ICO锻造铸币 || 钱包账户充值 || 钱包账户提现 || 钱包账户转合约账户
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_REQ.equals(enableModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_PRE_REQ.equals(enableModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_SUCCESS.equals(enableModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_FAIL.equals(enableModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_ICOMINT.equals(enableModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE.equals(enableModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW.equals(enableModel.getBusinessFlag())
                || FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT.equals(enableModel.getBusinessFlag()))
        {
            enableModel = this.findEnableAmtFormAccountWalletAssetDB(enableModel);
        } // 合约账户转钱包账户
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET))
        {
            if (null == enableModel.getRelatedStockinfoId()) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            enableModel = this.findEnableAmtForContract2Wallet(enableModel);
        } // Push交易现货卖出委托 || Push交易现货买入委托 || Push交易现货买入委托成交 || Push交易现货卖出委托成交
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST)
                || enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST)
                || enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_DEAL)
                || enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_DEAL))
        {
            enableModel = this.findEnableAmtFormAccountWalletAssetDB(enableModel);
        } // 集市交易现货卖出委托 || 集市交易现货买入委托 || 集市交易现货买入委托成交 || 集市交易现货卖出委托成交
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST)
                || enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST)
                || enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_DEAL)
                || enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_DEAL))
        {
            enableModel = this.findEnableAmtFormAccountWalletAssetDB(enableModel);
        } // 自动借款
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT))
        {

            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(enableModel.getRelatedStockinfoId());
            if (stockInfo == null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
                enableModel = this.findEnableAmtFormAccountContractAssetDBSubAccountDebit(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
                enableModel = this.findEnableAmtFormAccountContractAssetDBSubAccountDebit(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 自动还款
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_REPAYMENT))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(enableModel.getRelatedStockinfoId());
            if(stockInfo==null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                enableModel = this.findEnableAmtFormAccountSpotAssetDB(enableModel);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }

        } // 撮合交易委托卖出数字货币 买入法定货币
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(enableModel.getRelatedStockinfoId());
            if(stockInfo==null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                logger.debug("进入撮合交易委托卖出合约资产可用查询");
                enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                enableModel = this.findEnableAmtFormAccountSpotAssetDB(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("进入撮合交易委托卖出钱包资产可用查询");
                enableModel = this.findEnableAmtFormAccountWalletAssetDB(enableModel);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }

        } // 撮合交易委托买入数字货币 卖出法定货币
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(enableModel.getRelatedStockinfoId());
            if(stockInfo==null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                logger.debug("进入撮合交易委托买入合约资产可用查询");
                enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                enableModel = this.findEnableAmtFormAccountSpotAssetDB(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("进入撮合交易委托买入钱包资产可用查询");
                enableModel = this.findEnableAmtFormAccountWalletAssetDB(enableModel);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } // 内部交易 强制平仓 强制平仓BTC
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_CLOSE_POSITION_ASSET_TRANSFER))
        {
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(enableModel.getRelatedStockinfoId());
            if(stockInfo==null)
            {
                logger.debug("证券ID不存在，非法下单");
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                enableModel = this.findEnableAmtFormAccountSpotAssetDB(enableModel);
            }
            else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
            {
                logger.debug("进入撮合交易委托卖出钱包资产可用查询");
                enableModel = this.findEnableAmtFormAccountWalletAssetDB(enableModel);
            }
            else
            {
                logger.debug("证券信息类型错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }

        } // 平台亏损分摊
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_PLAT_SHARE_OF_LOSSES_VCOIN_MONEY))
        {
            // 从合约账户资产db中查找可用数量
            enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
        }
        // 杠杆现货转理财
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH))
        {
            enableModel = this.findEnableAmtFormAccountSpotAssetDBSpot2Wealth(enableModel);
        }
        // 理财转杠杆现货
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT))
        {
            enableModel = this.findEnableAmtFormAccountWealthAssetDB(enableModel);
        }
        // 钱包转杠杆现货
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT))
        {
            enableModel = this.findEnableAmtFormAccountWalletAssetDB(enableModel);
        }
        // 杠杆现货转钱包
        else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET))
        {
            enableModel = this.findEnableAmtFormAccountSpotAssetDBSpot2Wealth(enableModel);
        }
        // 其他直接抛出
        else
        {
            logger.error("交易端可用服务 businessFlag " + enableModel.getBusinessFlag() + " is error Please check it!");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        logger.debug("交易端可用服务 end--------------------------------------------------");
        logger.debug("交易端可用服务 enableModel:" + enableModel.toString());
        return enableModel;
    }
    
    /**
     * 合约资产转钱包资产可用余额
     * 如果支持借款，借款金额>0 则可用余额为0
     * @param enableModel
     * @return
     * @author 2017-09-19 09:53:02
     */
    public EnableModel findEnableAmtForContract2Wallet(EnableModel enableModel)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(enableModel.getRelatedStockinfoId());
        if (StringUtils.equalsIgnoreCase(stockInfo.getCanBorrow(), FundConsts.PUBLIC_STATUS_YES))
        {
            enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
            return enableModel;
        }
        else
        {
            enableModel = this.findEnableAmtFormAccountContractAssetDB(enableModel);
        }
        return enableModel;
    }
    
    /**
     * 从现货账户资产db中查找可用数量
     * @param enableModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private EnableModel findEnableAmtFormAccountSpotAssetDB(EnableModel enableModel)
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(enableModel.getAccountId());
        accountSpotAsset.setStockinfoId(enableModel.getStockinfoId());
        accountSpotAsset.setRelatedStockinfoId(getStockInfo(enableModel.getRelatedStockinfoId()).getCapitalStockinfoId());
        logger.debug("从现货账户资产db中查找可用数量 accountSpotAsset:" + accountSpotAsset.toString());
        List<AccountSpotAsset> accountSpotAssetList;
        try
        {
            accountSpotAssetList = accountSpotAssetService.findList(accountSpotAsset);
            if (accountSpotAssetList.size() > 0)
            {
                accountSpotAsset = accountSpotAssetList.get(0);
                // 如果冻结数量小于等于 直接取0 防止风险
                if (accountSpotAsset.getFrozenAmt().compareTo(BigDecimal.ZERO) <= 0)
                {
                    accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
                }
                enableModel.setEnableAmount(accountSpotAsset.getAmount().subtract(accountSpotAsset.getFrozenAmt()));
                enableModel.setFrozenAmt(accountSpotAsset.getFrozenAmt());
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
            logger.error("从现货账户资产db中查找可用数量 error:" + e.getMessage());
        }
        logger.debug("从现货账户资产db中查找可用数量 enableModel:" + enableModel.toString());
        return enableModel;
    }


    /**
     * 从钱包账户资产db中查找可用数量
     * @param enableModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private EnableModel findEnableAmtFormAccountSpotAssetDBSpot2Wealth(EnableModel enableModel)
    {
        return findSpotAndDebit(enableModel);
    }

    /**
     * 共用 如果存在美元专区借款 则钱包账户可用余额为0
     * @param enableModel
     * @return
     */
    public EnableModel findSpotAndDebit(EnableModel enableModel)
    {
        AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
        accountSpotAsset.setAccountId(enableModel.getAccountId());
        accountSpotAsset.setStockinfoId(enableModel.getStockinfoId());
        accountSpotAsset.setRelatedStockinfoId(getStockInfo(enableModel.getRelatedStockinfoId()).getCapitalStockinfoId());
        logger.debug("从现货账户资产db中查找可用数量 accountSpotAsset:" + accountSpotAsset.toString());
        List<AccountSpotAsset> accountSpotAssetList;
        try
        {
            accountSpotAssetList = accountSpotAssetService.findList(accountSpotAsset);
            if (accountSpotAssetList.size() > 0)
            {
                accountSpotAsset = accountSpotAssetList.get(0);
                // 如果冻结数量小于等于 直接取0 防止风险
                if (accountSpotAsset.getFrozenAmt().compareTo(BigDecimal.ZERO) <= 0)
                {
                    accountSpotAsset.setFrozenAmt(BigDecimal.ZERO);
                }
                enableModel.setEnableAmount(accountSpotAsset.getAmount().subtract(accountSpotAsset.getFrozenAmt()));
                enableModel.setFrozenAmt(accountSpotAsset.getFrozenAmt());
                enableModel.setEnableAmountEx(accountSpotAsset.getAmount().subtract(accountSpotAsset.getFrozenAmt()));

                AccountDebitAsset entity = new AccountDebitAsset();
                entity.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableDebitAsset());
                entity.setBorrowerAccountId(enableModel.getAccountId());
                entity.setRelatedStockinfoId(getStockInfo(enableModel.getRelatedStockinfoId()).getCapitalStockinfoId());
                List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
                if (list.size() > 0)
                {
                    logger.debug("存在借款 可用为0");
                    enableModel.setEnableAmount(BigDecimal.ZERO);
                    enableModel.setFrozenAmt(BigDecimal.ZERO);
                }
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
            enableModel.setEnableAmountEx(BigDecimal.ZERO);
            logger.error("从现货账户资产db中查找可用数量 error:" + e.getMessage());
        }
        logger.debug("从现货账户资产db中查找可用数量 enableModel:" + enableModel.toString());
        return enableModel;
    }

    /**
     * 从钱包账户资产db中查找可用数量
     * @param enableModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private EnableModel findEnableAmtFormAccountWalletAssetDB(EnableModel enableModel)
    {
        AccountWalletAsset accountWalletAsset = new AccountWalletAsset();
        accountWalletAsset.setAccountId(enableModel.getAccountId());
        accountWalletAsset.setStockinfoId(enableModel.getStockinfoId());
        logger.debug("从钱包账户资产db中查找可用数量 accountWalletAsset:" + accountWalletAsset.toString());
        List<AccountWalletAsset> accountWalletAssetList;
        try
        {
            accountWalletAssetList = accountWalletAssetService.findList(accountWalletAsset);
            if (accountWalletAssetList.size() > 0)
            {
                accountWalletAsset = accountWalletAssetList.get(0);
                // 如果冻结数量小于等于 直接取0 防止风险
                if (accountWalletAsset.getFrozenAmt().compareTo(BigDecimal.ZERO) <= 0)
                {
                    accountWalletAsset.setFrozenAmt(BigDecimal.ZERO);
                }
                enableModel.setEnableAmount(accountWalletAsset.getAmount().subtract(accountWalletAsset.getFrozenAmt()));
                enableModel.setFrozenAmt(accountWalletAsset.getFrozenAmt());
                enableModel.setOriginalBusinessId(accountWalletAsset.getId());
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
            logger.error("从钱包账户资产db中查找可用数量 error:" + e.getMessage());
        }
        logger.debug("从钱包账户资产db中查找可用数量 enableModel:" + enableModel.toString());
        return enableModel;
    }

    /**
     * 从理财账户资产db中查找可用数量
     * @param enableModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private EnableModel findEnableAmtFormAccountWealthAssetDB(EnableModel enableModel)
    {
        AccountWealthAsset accountWealthAsset = new AccountWealthAsset();
        accountWealthAsset.setWealthAccountId(enableModel.getAccountId());
        accountWealthAsset.setStockinfoId(enableModel.getStockinfoId());
        accountWealthAsset.setRelatedStockinfoId(getStockInfo(enableModel.getRelatedStockinfoId()).getCapitalStockinfoId());
        logger.debug("从合约账户资产db中查找可用数量 accountWealthAsset:" + accountWealthAsset.toString());
        List<AccountWealthAsset> accountWealthAssetList;
        try
        {
            accountWealthAssetList = accountWealthAssetService.findList(accountWealthAsset);
            if (accountWealthAssetList.size() > 0)
            {
                accountWealthAsset = accountWealthAssetList.get(0);
                enableModel.setEnableAmount(accountWealthAsset.getWealthAmt());
                enableModel.setFrozenAmt(BigDecimal.ZERO);
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


    /**
     * 从合约账户资产db中查找可用数量
     * @param enableModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
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
    
    /**
     * 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
     * @param enableModel
     * @return
     * @author sunbiao  2017年7月21日 下午2:36:53
     */
    private EnableModel findEnableAmtFormAccountContractAssetDBSubAccountDebit(EnableModel enableModel)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(enableModel.getRelatedStockinfoId());
        logger.debug("交易标的证券ID："+stockInfo.getTradeStockinfoId());
        boolean isVCoin = stockInfo.getTradeStockinfoId().longValue()==enableModel.getStockinfoId().longValue();
        logger.debug("是否数字货币标的："+isVCoin);

        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            if(isVCoin) //数字货币标的的时候
            {
                BigDecimal vcoinNet = BigDecimal.ZERO;
                BigDecimal moneyNet = BigDecimal.ZERO;
                BigDecimal vcoinDebit = BigDecimal.ZERO;
                BigDecimal moneyDebit = BigDecimal.ZERO;
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit enableModel:" + enableModel.toString());

                // 1.从合约账户资产db中查找当前数量
                AccountContractAsset accountContractAsset = new AccountContractAsset();
                accountContractAsset.setAccountId(enableModel.getAccountId());
                accountContractAsset.setStockinfoId(enableModel.getStockinfoId());
                accountContractAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
                List<AccountContractAsset> listAccountContractAsset = accountContractAssetService.findList(accountContractAsset);
                if (listAccountContractAsset.size() > 0){
                    vcoinNet = listAccountContractAsset.get(0).getAmount();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit vcoinNet当前数量:" + vcoinNet);

                accountContractAsset = new AccountContractAsset();
                accountContractAsset.setAccountId(enableModel.getAccountId());
                accountContractAsset.setStockinfoId(enableModel.getRelatedStockinfoId());
                accountContractAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
                listAccountContractAsset = accountContractAssetService.findList(accountContractAsset);
                if (listAccountContractAsset.size() > 0){
                    moneyNet = listAccountContractAsset.get(0).getAmount();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit moneyNet当前数量:" + moneyNet);

                // 2.减去借款金额与借款数量
                AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
                accountDebitAsset.setBorrowerAccountId(enableModel.getAccountId());
                accountDebitAsset.setStockinfoId(enableModel.getStockinfoId());
                accountDebitAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountDebitAsset.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableDebitAsset());
                List<AccountDebitAsset> listAccountDebitAsset = accountDebitAssetService.findList(accountDebitAsset);
                if (listAccountDebitAsset.size() > 0){
                    vcoinDebit = listAccountDebitAsset.get(0).getDebitAmt();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit vcoinDebit借款数量:" + vcoinDebit);

                accountDebitAsset = new AccountDebitAsset();
                accountDebitAsset.setBorrowerAccountId(enableModel.getAccountId());
                accountDebitAsset.setStockinfoId(enableModel.getRelatedStockinfoId());
                accountDebitAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountDebitAsset.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableDebitAsset());
                listAccountDebitAsset = accountDebitAssetService.findList(accountDebitAsset);
                if (listAccountDebitAsset.size() > 0){
                    moneyDebit = listAccountDebitAsset.get(0).getDebitAmt();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit moneyDebit借款数量:" + moneyDebit);

                // 取USDX/BTC最新平台行情价格
                BigDecimal platPrice = BigDecimal.ZERO;
                RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(enableModel.getStockinfoId(), enableModel.getRelatedStockinfoId());
                if (null == rtQuotationInfo)
                {
                    enableModel.setEnableAmount(BigDecimal.ZERO);
                    enableModel.setFrozenAmt(BigDecimal.ZERO);
                    logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit 数字货币/法定货币平台行情价格异常!");
                    return enableModel;
                }
                else {
                    platPrice = rtQuotationInfo.getPlatPrice();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit 数字货币/法定货币平台行情价格:" + platPrice);

                // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
                BigDecimal accountVcoinNetAmt = BigDecimal.ZERO;
                accountVcoinNetAmt = vcoinNet.subtract(vcoinDebit).add((moneyNet.subtract(moneyDebit)).divide(platPrice, 12, BigDecimal.ROUND_HALF_UP));
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountVcoinNetAmt);

                //获取系统参数
                BigDecimal borrowLever = BigDecimal.ZERO;
                if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT))
                {
                    borrowLever = stockInfo.getMaxLongLever();
                    logger.debug("MONEY最大可贷款杠杆倍数:" + borrowLever);
                } else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT))
                {

                    borrowLever = stockInfo.getMaxShortLever();
                    logger.debug("VCOIN最大可贷款杠杆倍数:" + borrowLever);
                }

                // 最终实时可借等值的VCOIN数量
                BigDecimal enableBorrowVcoinAmt = accountVcoinNetAmt.multiply(borrowLever).subtract(moneyDebit.divide(platPrice, 12, BigDecimal.ROUND_HALF_UP)).subtract(vcoinDebit);
                logger.debug("enableBorrowVcoinAmt:" + enableBorrowVcoinAmt);

                enableModel.setEnableAmount(enableBorrowVcoinAmt);
                enableModel.setFrozenAmt(BigDecimal.ZERO);
                logger.debug("enableModel:" + enableModel.toString());

                return enableModel;
            }
            else //法定货币标的的时候 返回法定货币
            {
                BigDecimal vcoinNet = BigDecimal.ZERO;
                BigDecimal moneyNet = BigDecimal.ZERO;
                BigDecimal vcoinDebit = BigDecimal.ZERO;
                BigDecimal moneyDebit = BigDecimal.ZERO;
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit enableModel:" + enableModel.toString());

                // 1.从合约账户资产db中查找当前数量
                AccountContractAsset accountContractAsset = new AccountContractAsset();
                accountContractAsset.setAccountId(enableModel.getAccountId());
                accountContractAsset.setStockinfoId(enableModel.getStockinfoId());
                accountContractAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
                List<AccountContractAsset> listAccountContractAsset = accountContractAssetService.findList(accountContractAsset);
                if (listAccountContractAsset.size() > 0){
                    vcoinNet = listAccountContractAsset.get(0).getAmount();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit vcoinNet当前数量:" + vcoinNet);

                accountContractAsset = new AccountContractAsset();
                accountContractAsset.setAccountId(enableModel.getAccountId());
                accountContractAsset.setStockinfoId(enableModel.getRelatedStockinfoId());
                accountContractAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountContractAsset.setTableName(getStockInfo(accountContractAsset.getRelatedStockinfoId()).getTableAsset());
                listAccountContractAsset = accountContractAssetService.findList(accountContractAsset);
                if (listAccountContractAsset.size() > 0){
                    moneyNet = listAccountContractAsset.get(0).getAmount();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit moneyNet当前数量:" + moneyNet);

                // 2.减去借款金额与借款数量
                AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
                accountDebitAsset.setBorrowerAccountId(enableModel.getAccountId());
                accountDebitAsset.setStockinfoId(enableModel.getStockinfoId());
                accountDebitAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountDebitAsset.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableDebitAsset());
                List<AccountDebitAsset> listAccountDebitAsset = accountDebitAssetService.findList(accountDebitAsset);
                if (listAccountDebitAsset.size() > 0){
                    vcoinDebit = listAccountDebitAsset.get(0).getDebitAmt();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit vcoinDebit借款数量:" + vcoinDebit);

                accountDebitAsset = new AccountDebitAsset();
                accountDebitAsset.setBorrowerAccountId(enableModel.getAccountId());
                accountDebitAsset.setStockinfoId(enableModel.getRelatedStockinfoId());
                accountDebitAsset.setRelatedStockinfoId(enableModel.getRelatedStockinfoId());
                accountDebitAsset.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableDebitAsset());
                listAccountDebitAsset = accountDebitAssetService.findList(accountDebitAsset);
                if (listAccountDebitAsset.size() > 0){
                    moneyDebit = listAccountDebitAsset.get(0).getDebitAmt();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit moneyDebit借款数量:" + moneyDebit);

                // 取USDX/BTC最新平台行情价格
                BigDecimal platPrice = BigDecimal.ZERO;
                RtQuotationInfo rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(enableModel.getStockinfoId(), enableModel.getRelatedStockinfoId());
                if (null == rtQuotationInfo)
                {
                    enableModel.setEnableAmount(BigDecimal.ZERO);
                    enableModel.setFrozenAmt(BigDecimal.ZERO);
                    logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit 数字货币/法定货币平台行情价格异常!");
                    return enableModel;
                }
                else {
                    platPrice = rtQuotationInfo.getPlatPrice();
                }
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit 数字货币/法定货币平台行情价格:" + platPrice);

                BigDecimal accountMoneyNetAmt = BigDecimal.ZERO;
                accountMoneyNetAmt = moneyNet.subtract(moneyDebit).add((vcoinNet.subtract(vcoinDebit)).divide(platPrice, 12, BigDecimal.ROUND_HALF_UP));
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountMoneyNetAmt);

                //获取系统参数
                BigDecimal borrowLever = BigDecimal.ZERO;
                if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT))
                {
                    borrowLever = stockInfo.getMaxShortLever();
                    logger.debug("VCOIN最大可贷款杠杆倍数:" + borrowLever);
                } else if (enableModel.getBusinessFlag().equals(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT))
                {
                    borrowLever = stockInfo.getMaxLongLever();
                    logger.debug("MONEY最大可贷款杠杆倍数:" + borrowLever);
                }

                // 最终实时可借等值的VCOIN数量
                BigDecimal enableBorrowVcoinAmt = accountMoneyNetAmt.multiply(borrowLever).subtract(vcoinDebit.divide(platPrice, 12, BigDecimal.ROUND_HALF_UP)).subtract(moneyDebit);
                logger.debug("enableBorrowVcoinAmt:" + enableBorrowVcoinAmt);

                enableModel.setEnableAmount(enableBorrowVcoinAmt);
                enableModel.setFrozenAmt(BigDecimal.ZERO);
                logger.debug("enableModel:" + enableModel.toString());

                return enableModel;
            }
        }
        else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            enableModel.setMaxLongLever(stockInfo.getMaxLongLever());
            enableModel.setMaxShortLever(stockInfo.getMaxShortLever());
            BigDecimal vcoinNet = BigDecimal.ZERO;
            BigDecimal moneyNet = BigDecimal.ZERO;
            BigDecimal vcoinDebit = BigDecimal.ZERO;
            BigDecimal moneyDebit = BigDecimal.ZERO;
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit enableModel:" + enableModel.toString());

            // 1.从现货账户资产db中查找当前数量
            AccountSpotAsset accountSpotAsset = new AccountSpotAsset();
            accountSpotAsset.setAccountId(enableModel.getAccountId());
            accountSpotAsset.setStockinfoId(stockInfo.getTradeStockinfoId());
            accountSpotAsset.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            List<AccountSpotAsset> listAccountSpotAsset = accountSpotAssetService.findList(accountSpotAsset);
            if (listAccountSpotAsset.size() > 0){
                vcoinNet = listAccountSpotAsset.get(0).getAmount();
            }
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit vcoinNet当前数量:" + vcoinNet);

            accountSpotAsset = new AccountSpotAsset();
            accountSpotAsset.setAccountId(enableModel.getAccountId());
            accountSpotAsset.setStockinfoId(stockInfo.getCapitalStockinfoId());
            accountSpotAsset.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
            listAccountSpotAsset = accountSpotAssetService.findList(accountSpotAsset);
            if (listAccountSpotAsset.size() > 0){
                moneyNet = listAccountSpotAsset.get(0).getAmount();
            }
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit moneyNet当前数量:" + moneyNet);

            // 2.减去借款金额与借款数量
            AccountDebitAsset accountDebitAsset = new AccountDebitAsset();
            accountDebitAsset.setBorrowerAccountId(enableModel.getAccountId());
            accountDebitAsset.setStockinfoId(stockInfo.getTradeStockinfoId());
            accountDebitAsset.setRelatedStockinfoId(getStockInfo(enableModel.getRelatedStockinfoId()).getCapitalStockinfoId());
            accountDebitAsset.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableDebitAsset());
            List<AccountDebitAsset> listAccountDebitAsset = accountDebitAssetService.findList(accountDebitAsset);
            if (listAccountDebitAsset.size() > 0){
                vcoinDebit = listAccountDebitAsset.get(0).getDebitAmt();
            }
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit vcoinDebit借款数量:" + vcoinDebit);

            accountDebitAsset = new AccountDebitAsset();
            accountDebitAsset.setBorrowerAccountId(enableModel.getAccountId());
            accountDebitAsset.setStockinfoId(stockInfo.getCapitalStockinfoId());
            accountDebitAsset.setRelatedStockinfoId(getStockInfo(enableModel.getRelatedStockinfoId()).getCapitalStockinfoId());
            accountDebitAsset.setTableName(getStockInfo(enableModel.getRelatedStockinfoId()).getTableDebitAsset());
            listAccountDebitAsset = accountDebitAssetService.findList(accountDebitAsset);
            if (listAccountDebitAsset.size() > 0){
                moneyDebit = listAccountDebitAsset.get(0).getDebitAmt();
            }
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit moneyDebit借款数量:" + moneyDebit);

            // 取USDX/BTC最新平台行情价格
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = null;
            rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfo(stockInfo.getTradeStockinfoId(), enableModel.getRelatedStockinfoId());
            if (null == rtQuotationInfo)
            {
                enableModel.setEnableAmount(BigDecimal.ZERO);
                enableModel.setFrozenAmt(BigDecimal.ZERO);
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit 数字货币/法定货币平台行情价格异常!");
                return enableModel;
            }
            else {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit 数字货币/法定货币平台行情价格:" + platPrice);

            BigDecimal accountVcoinNetAmt = BigDecimal.ZERO;
            accountVcoinNetAmt = vcoinNet.subtract(vcoinDebit).add((moneyNet.subtract(moneyDebit)).divide(platPrice, 13, BigDecimal.ROUND_HALF_UP));
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountVcoinNetAmt:" + accountVcoinNetAmt);

            //获取系统参数
            BigDecimal borrowLever = BigDecimal.ZERO;
            if (stockInfo.getTradeStockinfoId().longValue() == enableModel.getStockinfoId().longValue()) // 空头 借标的
            {
                borrowLever = enableModel.getMaxShortLever();
                logger.debug("VCOIN最大可贷款杠杆倍数:" + borrowLever);
            }
            else  // 多头 借钱
            {
                borrowLever = enableModel.getMaxLongLever();
                logger.debug("MONEY最大可贷款杠杆倍数:" + borrowLever);
            }
            // 最终实时可借等值的VCOIN数量
            BigDecimal enableBorrowVcoinAmt = accountVcoinNetAmt.multiply(borrowLever).subtract(moneyDebit.divide(platPrice, 12, BigDecimal.ROUND_HALF_UP)).subtract(vcoinDebit);
            logger.debug("enableBorrowVcoinAmt:" + enableBorrowVcoinAmt);

            enableModel.setEnableAmount(enableBorrowVcoinAmt);
            enableModel.setFrozenAmt(BigDecimal.ZERO);
            logger.debug("enableModel:" + enableModel.toString());

            return enableModel;
        }
        else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_PURESPOT))
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        else
        {
            logger.debug("证券信息类型错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }
    
    @Override
    public List<AccountWalletAsset> getWalletAssetListByAccountId(Long accountId) throws BusinessException
    {
        AccountWalletAsset entity = new AccountWalletAsset();
        entity.setAccountId(accountId);
        return accountWalletAssetService.findList(entity);
    }
    
    @Override
    public List<AccountContractAsset> getContractAssetListByAccountId(Long accountId,Long exchangePairMoney) throws BusinessException
    {
        AccountContractAsset entity = new AccountContractAsset();
        entity.setAccountId(accountId);
        entity.setTableName(getStockInfo(exchangePairMoney).getTableAsset());
        return accountContractAssetService.findList(entity);
    }

    public  StockInfo  getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
