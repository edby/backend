/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.NumericUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.fund.service.FundService;
import com.blocain.bitms.trade.quotation.QuotationController;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

/**
 *  Fund账户互转  控制器
 * <p>File：ConversionController.java</p>
 * <p>Title: ConversionController</p>
 * <p>Description:ConversionController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund账户互转")
public class ConversionController extends QuotationController
{
    public static final Logger        logger = LoggerFactory.getLogger(ConversionController.class);
    
    @Autowired(required = false)
    private AccountFundCurrentService accountFundCurrentService;
    
    @Autowired(required = false)
    private FundService               fundService;
    
    @Autowired(required = false)
    private AccountService            accountService;
    
    @Autowired(required = false)
    private SysParameterService       sysParameterService;
    
    @Autowired(required = false)
    private MsgRecordNoSql            msgRecordService;
    
    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    /**
     * Fund账户互转页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/conversion", method = RequestMethod.GET)
    @ApiOperation(value = "Fund账户互转页面导航", httpMethod = "GET")
    public ModelAndView index() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/conversion");
        return mav;
    }

    /**
     * Fund账户互转页面弹框导航
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/conversion/conversionDialog", method = RequestMethod.POST)
    @ApiOperation(value = "Fund账户互转页面弹框导航", httpMethod = "POST")
    public JsonMessage conversionDialog(Long currencyTypeId) throws BusinessException
    {
        // 杠杆现货品种
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
//        stockInfoSelect.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        stockInfoSelect.setCanConversion(FundConsts.PUBLIC_STATUS_YES);
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
        // btc等币种证券品种
        StockInfo currencyTypeStockInfo = stockInfoService.selectByPrimaryKey(currencyTypeId);
        // 数据合并
        stockInfoList.add(currencyTypeStockInfo);

        logger.debug("conversionDialog stockInfoList.size:" + stockInfoList.size());

        return getJsonMessage(CommonEnums.SUCCESS, stockInfoList);
    }
    
    /**
     * Fund账户互转合约协议页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/conversionProtocol", method = RequestMethod.GET)
    @ApiOperation(value = "Fund账户互转合约协议页面导航", httpMethod = "GET")
    public ModelAndView conversionProtocol() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/conversionProtocol");
        return mav;
    }
    
    /**
     * Fund账户互转历史列表
     * @param accountFundCurrent
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 下午2:55:45
     */
    @ResponseBody
    @RequestMapping(value = "/conversion/conversionList", method = RequestMethod.POST)
    @ApiOperation(value = "Fund账户互转历史列表", httpMethod = "POST")
    public JsonMessage conversionList(AccountFundCurrent accountFundCurrent, Pagination pagin) throws BusinessException
    {
        // 证券信息ID判断
        if (null == accountFundCurrent.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountFundCurrent.setAccountId(principal.getId());
        //accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getStockinfoId()).getTableFundCurrent());
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        String[] aArray = new String[stockInfoList.size()];
        for(int i = 0 ; i < stockInfoList.size(); i ++)
        {
            StockInfo stockInfotemp = stockInfoList.get(i);
            aArray[i] = stockInfotemp.getTableFundCurrent(); // 查询流水表 非 流水视图
        }
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.accountFundCurrentChargeSearch(pagin, accountFundCurrent, aArray);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * Fund账户互转:钱包帐户和理财帐户互转账
     * @param fundModel
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月13日 下午2:35:13
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/conversion/wealth", method = RequestMethod.POST)
    @ApiOperation(value = "Fund账户互转:钱包帐户和理财帐户互转账", httpMethod = "POST")
    public JsonMessage wealth(FundModel fundModel) throws BusinessException
    {
        checkSwitch();
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(fundModel.getAmount());
        logger.debug("/conversion/wealth page form = " + fundModel.toString());
        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) < 0 || fundModel.getAmount().compareTo(BigDecimal.valueOf(999999999)) > 0)
        {
            logger.debug("参数验证错误：转移数量范围错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 小数位数判断
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 4);
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_NOT_EXIST); }
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH.equals(fundModel.getBusinessFlag()))
        {
            Long temp = fundModel.getStockinfoId();
            fundModel.setStockinfoId(fundModel.getStockinfoIdEx());
            fundModel.setStockinfoIdEx(temp);
        }
        Long stockinfoIdEx=fundModel.getStockinfoIdEx();
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(BigDecimal.ZERO);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_USD_TYPE);//USD专区
        fundModel.setStockinfoId(FundConsts.WALLET_USD_TYPE);
        fundModel.setAccountId(principal.getId());
        fundModel.setCreateBy(principal.getId());
        logger.debug("conversion wealth fundModel:" + fundModel.toString());
        fundService.fundTransaction(fundModel);
        setAccountAssetCache(principal.getId(), fundModel.getStockinfoId(), stockinfoIdEx);
        return getJsonMessage(CommonEnums.SUCCESS);
    }

    /**
     * Fund账户互转:钱包帐户和合约帐户互转账
     * @param fundModel
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月13日 下午2:35:13
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/conversion/conversion", method = RequestMethod.POST)
    @ApiOperation(value = "Fund账户互转:钱包帐户和合约帐户互转账", httpMethod = "POST")
    public JsonMessage conversion(FundModel fundModel) throws BusinessException
    {
        checkSwitch();
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        Account account = accountService.selectByPrimaryKey(principal.getId());
        checkAccountDataValidate(account);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(fundModel.getAmount());
        logger.debug("/conversion/conversion page form = " + fundModel.toString());
        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) < 0 || fundModel.getAmount().compareTo(BigDecimal.valueOf(999999999)) > 0)
        {
            logger.debug("参数验证错误：转移数量范围错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 小数位数判断
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 4);
        SysParameter sysParameter = new SysParameter();
        sysParameter.setSystemName(TradeEnums.SYS_PARAMETER_SYSTEM_NAME_TRADE.getCode());
        sysParameter.setParameterName(TradeEnums.FUND_CONVERSION_MAX_CNT_BY_ONCE.getCode());
        sysParameter = sysParameterService.getSysParameterByEntity(sysParameter);
        if (sysParameter == null)
        {
            logger.debug("单笔最大参数有问题");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        BigDecimal maxAmount = BigDecimal.valueOf(Double.parseDouble(sysParameter.getValue()));
        if (fundModel.getAmount().compareTo(maxAmount) > 0)
        {
            logger.debug("已超过单笔最大");
            throw new BusinessException(CommonEnums.ERROR_GT_MAX_AMT);
        }
        if (fundModel.getFee().compareTo(BigDecimal.ZERO) < 0 || fundModel.getFee().compareTo(BigDecimal.valueOf(999)) >= 0)
        {
            logger.debug("参数验证错误：手续费范围错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        // 互转数量判断
        if (null == fundModel.getAmount()
                || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_CHARGE_AMOUNT_GREATER_ZEOR); }
        // 业务类别判断
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_NOT_EXIST); }
        // 如果是合约转钱包 互调stockinfoId
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET.equals(fundModel.getBusinessFlag()))
        {
            Long temp = fundModel.getStockinfoId();
            fundModel.setStockinfoId(fundModel.getStockinfoIdEx());
            fundModel.setStockinfoIdEx(temp);
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(fundModel.getStockinfoIdEx());
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.debug("交易对错误");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        stockInfo = stockInfoList.get(0);
        // 合约资产
        if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
            Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
            if (!(fundModel.getStockinfoId().equals(exchangePairVCoin)))
            {
                // 如果不是BTC和USDX合约互转 则参数错误
                logger.debug("参数验证错误：换转类型不匹配");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            if (!FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT.equals(fundModel.getBusinessFlag()) && !FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET
                    .equals(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_ERROR); }
            fundModel.setAccountId(principal.getId());
            fundModel.setCreateBy(principal.getId());
            logger.debug("conversion contract fundModel:" + fundModel.toString());
            fundService.fundTransaction(fundModel);

        }else if(StringUtils.equalsIgnoreCase(stockInfo.getStockType(),FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            if (!FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT.equals(fundModel.getBusinessFlag()) && !FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET
                    .equals(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_ERROR); }

            if(StringUtils.equalsIgnoreCase(fundModel.getBusinessFlag(),FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT))
            {
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT);
            }
            else
            {
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET);
            }
            fundModel.setAccountId(principal.getId());
            fundModel.setCreateBy(principal.getId());
            logger.debug("conversion spot fundModel:" + fundModel.toString());
            fundService.fundTransaction(fundModel);

        }else
        {
            logger.debug("不支持互转");
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }

        setAccountAssetCache(principal.getId(), fundModel.getStockinfoId(), fundModel.getStockinfoIdEx());

        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    private void checkAccountDataValidate(Account account) throws BusinessException
    {
        if (null == account) { throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT); }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) { throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK); }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }

    private void checkSwitch()
    {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null)
        {
            logger.debug("===========开关值空==========");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!StringUtils.isBlank(params.getValue()))
        {
            if (!params.getValue().equals("yes"))
            {
                logger.debug("===========开关已关闭==========");
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            else
            {
                logger.debug("===========开关已打开==========");
            }
        }
        else
        {
            logger.debug("===========开关值不存在==========");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
    }
    
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
