package com.blocain.bitms.trade.fund.service;

import com.blocain.bitms.basic.service.AbstractBaseTest;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CookieUtils;
import com.blocain.bitms.tools.utils.ImageUtils;
import com.blocain.bitms.tools.utils.LanguageUtils;
import com.blocain.bitms.tools.utils.NumericUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountCashWithdraw;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrERC20;
import com.blocain.bitms.trade.fund.entity.AccountCollectBank;
import com.blocain.bitms.trade.fund.entity.AccountFundWithdraw;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/2/8.
 */
public class FundCurrentServiceImplTest extends AbstractBaseTest
{
    @Autowired(required = false)
    private StockRateService            stockRateService;

    @Autowired(required = false)
    private EnableService               enableService;

    @Autowired(required = false)
    private FundCurrentService          fundCurrentService;

    @Autowired(required = false)
    private AccountCollectBankService   accountCollectBankService;

    @Autowired(required = false)
    private AccountCollectAddrERC20Service   accountCollectAddrERC20Service;
    @Autowired(required = false)
    private AccountFundWithdrawService   accountFundWithdrawService;

    @Test
    public void doTradexWithdrawERC20()
    {
        FundModel fundModel = new FundModel();
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setAccountId(75814244729229312L);
        fundModel.setAmount(BigDecimal.valueOf(0.1));
        fundModel.setFee(BigDecimal.valueOf(0.001));
        fundModel.setAddress("0x86169ee5A4Ef045f611c891D6F4315e27ba2A8C4");
        fundModel.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_BIEX_TYPE);
        fundCurrentService.doTradexWithdrawERC20(fundModel);
    }

    @Test
    public void doApplyWithdrawERC20() throws Exception
    {
        BigDecimal amount = BigDecimal.valueOf(0.1);

        FundModel fundModel = new FundModel();
        fundModel.setAmount(amount);
        fundModel.setAccountId(300000060678L);
        fundModel.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_ETH_TYPE);
        if (fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0 || fundModel.getAmount().compareTo(BigDecimal.valueOf(50)) > 0)
        {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 小数位数判断
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 4);
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(fundModel.getStockinfoId());
        stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
        List<StockRate> feeRateList = stockRateService.findList(stockRate);
        BigDecimal feeRate = BigDecimal.ZERO;
        if (feeRateList.size() > 0)
        {
            StockRate rateEntity = feeRateList.get(0);
            if (rateEntity.getRate() != null)
            {
                feeRate = rateEntity.getRate();
            }
            else
            {
                throw new BusinessException("feeRate error:record null");
            }
        }
        else
        {
            throw new BusinessException("feeRate error:no record");
        }
        fundModel.setFee(feeRate); //这里是手续费 不是费率
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 start----------
        // ---------用户数据验证 资金验证 安全策略验证 资金密码验证 end----------
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        String lang = "en_US";
        AccountCollectAddrERC20 accountCollectBank = new AccountCollectAddrERC20();
        accountCollectBank.setAccountId(fundModel.getAccountId());
        accountCollectBank.setStockinfoId(fundModel.getStockinfoId());
        List<AccountCollectAddrERC20> banklist = accountCollectAddrERC20Service.findList(accountCollectBank);
        String addressimg = "";
        if (banklist.size() > 0)
        {
            fundModel.setAddress(banklist.get(0).getCollectAddr());
            System.out.println("===========================================");
            System.out.println(fundModel.getAddress());
            System.out.println(EncryptUtils.desDecrypt(fundModel.getAddress()));
            addressimg =EncryptUtils.desDecrypt(fundModel.getAddress());
        }
        else
        {
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(fundModel.getAccountId());
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(fundModel.getStockinfoId());
        enableModel.setRelatedStockinfoId(fundModel.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount()
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        AccountFundWithdraw accountFundWithdraw = fundCurrentService.doApplyWithdrawERC20(lang, fundModel, FundConsts.PUBLIC_STATUS_YES, accountCollectBank.getCertStatus());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", accountFundWithdraw.getId());
        map.put("withdrawAmt", accountFundWithdraw.getWithdrawAmt());
        map.put("netFee", accountFundWithdraw.getNetFee());
        BufferedImage image =
                ImageUtils.GraphicsToConfirmWithdrawBufferedImage(fundModel.getAmount(), addressimg, accountFundWithdraw.getConfirmCode(),"ETH");
        map.put("image", "data:image/jpeg;base64," + ImageUtils.BufferedImageToBase64(
                ImageUtils.GraphicsToConfirmWithdrawBufferedImage(fundModel.getAmount(), addressimg, accountFundWithdraw.getConfirmCode(),"ETH")));
        try
        {
            ImageIO.write(image, "JPEG", new File("d:/"+accountFundWithdraw.getId()+".jpg"));
            System.out.println("=======2=======");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("======2========");
        }
    }


    @Test
    public void confirmERC20()
    {
        //63563720793526272  932668
        AccountFundWithdraw entity = accountFundWithdrawService.selectByPrimaryKey(63563720793526272l);
        fundCurrentService.doComfirmWithdrawERC20(entity, "932668", "en_US");
    }


    @Test
    public  void  drawImage()
    {
        int imageWidth = 500;// 图片的宽度
        int imageHeight = 270;// 图片的高度
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(104,137,28));
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setColor(Color.white);
        graphics.setFont(new Font("黑体", Font.BOLD, 20));
        graphics.drawString("Tamper-proof Confirmation", 107, 80);
        graphics.drawString("Withdraw 0.1 BTH to", 127, 120);
        graphics.drawString("0x99b19334c259034861c34c65fdade4f1202d581e", 20, 160);
        graphics.drawString("Security Code :", 97, 230);
        graphics.setColor(Color.red);
        graphics.setFont(new Font("黑体", Font.BOLD, 28));
        graphics.drawString("123456", 327, 228);
        try
        {
            ImageIO.write(image, "JPEG", new File("d:/a.jpg"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @Test
    public void doApplyCashWithdraw() throws Exception
    {
        BigDecimal amount = BigDecimal.valueOf(120);
        AccountCashWithdraw accountCashWithdraw = new AccountCashWithdraw();
        accountCashWithdraw.setStockinfoId(FundConsts.WALLET_EUR_TYPE);
        accountCashWithdraw.setOccurAmt(amount);

        Long accountId=300000060606L;

        FundModel fundModel = new FundModel();
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(FundConsts.WALLET_EUR_TYPE);
        stockRate.setRateType(FundConsts.RATE_TYPE_WITHDRAW_FEE);
        List<StockRate> feerateList = stockRateService.findList(stockRate);
        BigDecimal feeRate = BigDecimal.ZERO;
        if (feerateList.size() > 0)
        {
            StockRate rateEntity = feerateList.get(0);
            if (rateEntity.getRate() != null)
            {
                feeRate = rateEntity.getRate();
            }
            else
            {
                throw new BusinessException("feeRate error:record null");
            }
        }
        else
        {
            throw new BusinessException("feeRate error:no record");
        }
        fundModel.setFee(feeRate.multiply(amount));
        accountCashWithdraw.setFee(feeRate.multiply(accountCashWithdraw.getOccurAmt()));
        if (accountCashWithdraw.getOccurAmt().compareTo(BigDecimal.ZERO) <= 0
                || accountCashWithdraw.getOccurAmt().compareTo(BigDecimal.valueOf(999999999)) > 0) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        // 小数位数判断
        fundModel.setAmount(accountCashWithdraw.getOccurAmt());
        NumericUtils.checkDecimalDigits("amount", fundModel.getAmount(), 4);

        fundModel.setStockinfoId(accountCashWithdraw.getStockinfoId());
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        accountCashWithdraw.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        fundModel.setAccountId(accountId);
        accountCashWithdraw.setAccountId(accountId);
        AccountCollectBank accountCollectBank = new AccountCollectBank();
        accountCollectBank.setAccountId(accountId);
        accountCollectBank.setStockinfoId(accountCashWithdraw.getId());
        List<AccountCollectBank> banklist = accountCollectBankService.findList(accountCollectBank);
        if (banklist.size() > 0)
        {
            fundModel.setAddress(banklist.get(0).getCardNo());
            accountCashWithdraw.setWithdrawCardNo(banklist.get(0).getCardNo());
            accountCashWithdraw.setWithdrawBankName(banklist.get(0).getBankName());
        }
        else
        {
            throw new BusinessException(CommonEnums.ERROR_INVALID_ADDRESS);
        }
        EnableModel enableModel = new EnableModel();
        enableModel.setAccountId(accountId);
        enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
        enableModel.setStockinfoId(accountCashWithdraw.getStockinfoId());
        enableModel.setRelatedStockinfoId(accountCashWithdraw.getStockinfoId());
        enableModel = enableService.entrustTerminalEnable(enableModel);
        if (enableModel.getEnableAmount()
                .compareTo(fundModel.getAmount().add(fundModel.getFee())) < 0) { throw new BusinessException(CommonEnums.RISK_ENABLE_ENABLE_NOTAVAILABLE); }
        fundCurrentService.doApplyCashWithdraw(fundModel, accountCashWithdraw);
    }

    @Test
    public void doWithdrawCancel() throws Exception
    {
        Long id = 63569634799915009L;
        Long accountId=300000060678L;
        fundCurrentService.doWithdrawCancel(id,accountId,FundConsts.WALLET_ETH_TYPE);
    }

    @Test
    public void doRechargeERC20()
    {
        FundModel fundModel = new FundModel();
        fundModel.setOriginalBusinessId(1L);
        fundModel.setAccountId(64152575229825024L);
        fundModel.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
        fundModel.setStockinfoIdEx(FundConsts.WALLET_ETH_TYPE);
        fundModel.setAmount(BigDecimal.valueOf(100));
        fundModel.setAmountEx(BigDecimal.ZERO);
        fundModel.setFee(BigDecimal.ZERO);
        fundModel.setAddress("");
        fundModel.setTransId("");
        fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE);
        fundCurrentService.doRechargeERC20(fundModel,1L);
    }

    @Test
    public void doChargeAward() throws Exception
    {
        fundCurrentService.doChargeAward(84534411457990656L);
        //fundCurrentService.doChargeAward(81345419036069888L);
    }

}