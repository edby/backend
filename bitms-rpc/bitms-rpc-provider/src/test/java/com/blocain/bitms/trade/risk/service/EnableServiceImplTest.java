package com.blocain.bitms.trade.risk.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountContractAsset;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountContractAssetService;
import com.blocain.bitms.trade.fund.service.FundService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by admin on 2017/9/19.
 */
public class EnableServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    EnableService               enableService;
    
    @Autowired(required = false)
    AccountContractAssetService accountContractAssetService;
    
    @Autowired(required = false)
    FundService                 fundService;
    
    // 初始化资产
    @Test
    public void initContractAsset() throws Exception
    {
        // 插入一笔 合约账户资产 BTC 关联USDX
        AccountContractAsset asset = new AccountContractAsset();
        asset.setAccountId(2174374631182336l);
        asset.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        asset.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        asset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        asset.setPrice(BigDecimal.ONE);
        asset.setDirection("Long");
        asset.setAmount(BigDecimal.valueOf(100));
        asset.setFrozenAmt(BigDecimal.ONE);
        accountContractAssetService.insert(asset);
        // 插入一笔 合约账户资产 USDX 关联USDX
        asset = new AccountContractAsset();
        asset.setId(null);
        asset.setAccountId(2174374631182336l);
        asset.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        asset.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        asset.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        asset.setPrice(BigDecimal.ONE);
        asset.setDirection("Long");
        asset.setAmount(BigDecimal.valueOf(100));
        asset.setFrozenAmt(BigDecimal.ONE);
        accountContractAssetService.insert(asset);
    }

    // 委托挂单可用判断逻
    @Test
    public void entrustTerminalEnable() throws Exception
    {
        // 委托挂单可用判断逻
        // 买入USDX，卖出BTC，可用BTC数量为 当前BTC可用数量=当前BTC数量-冻结BTC数量
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(2174374631182336l);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
        enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        System.out.print(enableModel.toString());
        // 委托挂单可用判断逻
        // 卖出USDX，买入BTC，可用USDX数量为 当前USDX可用数量=当前USDX数量-冻结USDX数量
        enableModel = new EnableModel();
        enableModel.setAccountId(2174374631182336l);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
        enableModel.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        System.out.print(enableModel.toString());
        // 撮合提现可用判断逻辑
        // 假如账户持仓有借USDX，该账户直接控制不能提现BTC，也就是提现可用数量为0
        // 可用BTC数量为 当前BTC可用数量=当前BTC数量-冻结BTC数量
        enableModel = new EnableModel();
        enableModel.setAccountId(2174374631182336l);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET);// 合约账户转钱包账户
        enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
        System.out.print(enableModel.toString());
    }

    // 钱包资产转合约资产
    @Test
    public void walletToContract() throws Exception
    {
        FundModel fundModel = new FundModel();
        fundModel.setAccountFundCurrentId(2174374631182336l);
        fundModel.setAmount(BigDecimal.valueOf(1));
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);

        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC2USDX_TYPE);
        fundModel.setAmountEx(fundModel.getAmount());
        // 如果是合约转钱包 互调stockinfoId
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET.equals(fundModel.getBusinessFlag()))
        {
            Long temp = fundModel.getStockinfoId();
            fundModel.setStockinfoId(fundModel.getStockinfoIdEx());
            fundModel.setStockinfoIdEx(temp);
        }
        System.out.println("/conversion/conversion page form = " + fundModel.toString());
        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) < 0
                || fundModel.getAmount().compareTo(BigDecimal.valueOf(999999)) > 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (fundModel.getFee().compareTo(BigDecimal.ZERO) < 0
                || fundModel.getFee().compareTo(BigDecimal.valueOf(999)) >= 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (!(fundModel.getStockinfoId().equals(FundConsts.WALLET_BTC_TYPE) && fundModel.getStockinfoIdEx().equals(FundConsts.WALLET_BTC2USDX_TYPE))
                && !(fundModel.getStockinfoId().equals(FundConsts.WALLET_BTC2USDX_TYPE) && fundModel.getStockinfoIdEx().equals(FundConsts.WALLET_BTC_TYPE)))
        {
            // 如果不是BTC和Btc合约互转 则参数错误
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        // 互转数量判断
        if (null == fundModel.getAmount()
                || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_CHARGE_AMOUNT_GREATER_ZEOR); }
        // 业务类别判断
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_NOT_EXIST); }
        if (!FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT.equals(fundModel.getBusinessFlag()) && !FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET
                .equals(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_ERROR); }
        fundModel.setAccountId(2174374631182336l);
        fundModel.setCreateBy(2174374631182336l);
        System.out.println("conversion fundModel:" + fundModel.toString());
        try
        {
            EnableModel enableModel = new EnableModel();
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("钱包转入合约之前 钱包账户 "+enableModel.toString());
            enableModel = new EnableModel();
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTSELL_ENTRUST);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("钱包转入合约之前 合约账户 "+enableModel.toString());
            fundService.fundTransaction(fundModel);
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("钱包转入合约后 钱包账户 "+enableModel.toString());
            enableModel = new EnableModel();
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("钱包转入合约之后 合约账户 "+enableModel.toString());
        }
        catch (BusinessException e)
        {
            System.out.println(e.getMessage());
        }
    }

    //合约资产转钱包资产
    @Test
    public void ContractToWallet() throws Exception
    {
        FundModel fundModel = new FundModel();
        fundModel.setAccountFundCurrentId(2174374631182336l);
        fundModel.setAmount(BigDecimal.valueOf(2));
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET);

        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BTC_TYPE);
        fundModel.setAmountEx(fundModel.getAmount());
        // 如果是合约转钱包 互调stockinfoId
        if (FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET.equals(fundModel.getBusinessFlag()))
        {
            Long temp = fundModel.getStockinfoId();
            fundModel.setStockinfoId(fundModel.getStockinfoIdEx());
            fundModel.setStockinfoIdEx(temp);
        }
        System.out.println("/conversion/conversion page form = " + fundModel.toString());
        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) < 0
                || fundModel.getAmount().compareTo(BigDecimal.valueOf(999999)) > 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (fundModel.getFee().compareTo(BigDecimal.ZERO) < 0
                || fundModel.getFee().compareTo(BigDecimal.valueOf(999)) >= 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        if (!(fundModel.getStockinfoId().equals(FundConsts.WALLET_BTC_TYPE) && fundModel.getStockinfoIdEx().equals(FundConsts.WALLET_BTC2USDX_TYPE))
                && !(fundModel.getStockinfoId().equals(FundConsts.WALLET_BTC2USDX_TYPE) && fundModel.getStockinfoIdEx().equals(FundConsts.WALLET_BTC_TYPE)))
        {
            // 如果不是BTC和Btc合约互转 则参数错误
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 证券信息ID判断
        if (null == fundModel.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
        // 互转数量判断
        if (null == fundModel.getAmount()
                || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_CHARGE_AMOUNT_GREATER_ZEOR); }
        // 业务类别判断
        if (StringUtils.isBlank(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_NOT_EXIST); }
        if (!FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT.equals(fundModel.getBusinessFlag()) && !FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET
                .equals(fundModel.getBusinessFlag())) { throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_ERROR); }
        fundModel.setAccountId(2174374631182336l);
        fundModel.setCreateBy(2174374631182336l);
        System.out.println("conversion fundModel:" + fundModel.toString());
        try
        {
            EnableModel enableModel = new EnableModel();
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("合约账户转钱包之前 钱包账户 "+enableModel.toString());
            enableModel = new EnableModel();
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("合约账户转钱包之前  合约账户 "+enableModel.toString());
            fundService.fundTransaction(fundModel);
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("合约账户转钱包后 钱包账户 "+enableModel.toString());
            enableModel = new EnableModel();
            enableModel.setAccountId(2174374631182336l);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOTBUY_ENTRUST);
            enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
            System.out.print("合约账户转钱包之后 合约账户 "+enableModel.toString());
        }
        catch (BusinessException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Test
    //从合约账户资产db中查找可用数量(减去已借款USDX折合已占用保证金BTC数量 减去已借款BTC数量)
    public void testEnable()
    {
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(12029184939397120L);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_MATCHTRADE_SPOT_AUTO_DEBIT);
        enableModel.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        enableModel.setRelatedStockinfoId(FundConsts.WALLET_BTC2USDX_TYPE);
        enableModel = enableService.entrustTerminalEnable(enableModel);
    }
    
    @Test
    public void getContractAssetListByAccountId() throws Exception
    {
    }
}