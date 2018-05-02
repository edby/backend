package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/9/29.
 */
public class FundServiceImplTest extends AbstractBaseTest
{
    @Autowired
    FundService fundService;
    public static final Logger logger = LoggerFactory.getLogger(FundServiceImplTest.class);
    /**
     * 交割时超级用户用BTC兑换USDX
     * @throws Exception
     */
    @Test
    public void doSettlementUsdxExchangeBtc() throws Exception
    {
        fundService.doSettlementMoneyExchangeVCoin(FundConsts.WALLET_BTC2USDX_TYPE,FundConsts.WALLET_BTC_TYPE);
    }

    @Test
    public void testSuperAdminDebitInterest()
    {
        FundModel fundModel = new FundModel();
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT_INTEREST);
        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USD_TYPE);
        fundModel.setAmount(BigDecimal.valueOf(10));
        fundModel.setFee(BigDecimal.valueOf(10));
        fundModel.setAccountId(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_DEBIT_WEALTH_ID);
        fundModel.setCreateBy(FundConsts.SYSTEM_ACCOUNT_MATCHTRADE_VCOIN_MONEY_DEBIT_WEALTH_ID);
        fundModel.setOriginalBusinessId(0L);
        fundService.doSuperAdminDebitInterest(fundModel, FundConsts.ACCOUNT_ASSET_DIRECT_INCREASE);
    }

    /**
     * 账户互转
     * @throws Exception
     */
    @Test
    public void fundTransaction() throws Exception
    {
        FundModel fundModel = new FundModel();

        //来自界面-钱包转合约
//        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
//        fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
//        fundModel.setAmount(BigDecimal.TEN);
//        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);

        //来自界面-合约转钱包
        fundModel.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC_TYPE);
        fundModel.setAmount(BigDecimal.ONE);
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET);

        fundModel.setAccountId(2174374631182336L);
        fundModel.setCreateBy(2174374631182336L);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAmountEx(fundModel.getAmount());
        logger.debug("/conversion/conversion page form = " + fundModel.toString());

        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) < 0 || fundModel.getAmount().compareTo(BigDecimal.valueOf(999999)) > 0) { throw new BusinessException(
                CommonEnums.PARAMS_VALID_ERR); }
        if (fundModel.getFee().compareTo(BigDecimal.ZERO) < 0 || fundModel.getFee().compareTo(BigDecimal.valueOf(999)) >= 0) { throw new BusinessException(
                CommonEnums.PARAMS_VALID_ERR); }
        if (!(fundModel.getStockinfoId().equals(FundConsts.WALLET_BTC_TYPE) && fundModel.getStockinfoIdEx().equals(FundConsts.WALLET_BTC2USDX_TYPE))
                && !(fundModel.getStockinfoId().equals(FundConsts.WALLET_BTC2USDX_TYPE) && fundModel.getStockinfoIdEx().equals(FundConsts.WALLET_BTC_TYPE)))
        {
            // 如果不是BTC和Btc合约互转 则参数错误
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        // 互转数量判断
        if (null == fundModel.getAmount() || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(
                FundEnums.ERROR_CHARGE_AMOUNT_GREATER_ZEOR); }
        // 业务类别判断
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_NOT_EXIST); }
        // 如果是合约转钱包 互调stockinfoId
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET.equals(fundModel.getBusinessFlag()))
        {
            Long temp = fundModel.getStockinfoId();
            fundModel.setStockinfoId(fundModel.getStockinfoIdEx());
            fundModel.setStockinfoIdEx(temp);
        }
        if (!FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT.equals(fundModel.getBusinessFlag())
                && !FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET.equals(fundModel.getBusinessFlag())) { throw new BusinessException(
                FundEnums.ERROR_BUSINESSFLAG_ERROR); }
        logger.debug("conversion fundModel:" + fundModel.toString());
        try
        {
            fundService.fundTransaction(fundModel);
        }
        catch (BusinessException e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getErrorCode());
        }
        //return getJsonMessage(CommonEnums.SUCCESS);
    }
}