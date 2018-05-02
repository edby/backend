package com.blocain.bitms.apps.fund.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.apps.fund.model.RecharegeModel;
import com.blocain.bitms.apps.fund.model.WithdrawAddrModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CookieUtils;
import com.blocain.bitms.tools.utils.LanguageUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.model.PolicyModel;
import com.blocain.bitms.trade.account.service.AccountPolicyService;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddr;
import com.blocain.bitms.trade.fund.entity.AccountCollectAddrERC20;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.service.AccountCollectAddrERC20Service;
import com.blocain.bitms.trade.fund.service.AccountCollectAddrService;
import com.blocain.bitms.trade.fund.service.FundCurrentService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

import io.swagger.annotations.ApiOperation;

/**
 * <p>Author：ChenGang</p>
 * <p>Description: WithdrawAddrController</p>
 * <p>Date: Create in 18:31 2018/3/20</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class WithdrawAddrController extends AppsController
{
    @Autowired(required = false)
    private AccountCollectAddrService      accountCollectAddrService;

    @Autowired(required = false)
    private AccountService                 accountService;

    @Autowired(required = false)
    private AccountPolicyService           accountPolicyService;

    @Autowired(required = false)
    private StockInfoService               stockInfoService;

    @Autowired(required = false)
    private FundCurrentService             fundCurrentService;

    @Autowired(required = false)
    private AccountCollectAddrERC20Service accountCollectAddrERC20Service;

    /**
     * 查询提币提现地址
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/getWithdrawAddr")
    @ApiOperation(value = "提币出金收款地址列表")
    public AppsMessage withdrawAddr(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        RecharegeModel recharegeModel = checkSign(params, RecharegeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, recharegeModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(recharegeModel.getAuthToken());
        checkSesion(session,recharegeModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
        // 查询证券信息表
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(recharegeModel.getSymbol());
        List<StockInfo> stockInfos = stockInfoService.findList(stockInfo);
        if (stockInfos.size() == 0) { return getJsonMessage(CommonEnums.PARAMS_VALID_ERR); }
        stockInfo = stockInfos.get(0);
        List<AccountCollectAddrERC20> banklist = null;
        List<AccountCollectAddr> list = null;
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        // 数字货币
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN))
        {
            if (stockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue())
            {
                AccountCollectAddr accountCollectAddr = new AccountCollectAddr();
                accountCollectAddr.setAccountId(accountId);
                accountCollectAddr.setStockinfoId(stockInfo.getId());
                if (null == accountCollectAddr.getStockinfoId()) { throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST); }
                logger.debug("withdrawAddr accountCollectAddr:" + accountCollectAddr.toString());
                list = accountCollectAddrService.findList(accountCollectAddr);
                for (AccountCollectAddr addr : list)
                {
                    if (addr.getCollectAddr() != null && !addr.getCollectAddr().equals(""))
                    {
                        addr.setCollectAddr(EncryptUtils.desDecrypt(addr.getCollectAddr()));
                    }
                }
                AccountCollectAddr ret = new AccountCollectAddr();
                if(list.size()>0)
                {
                    ret = list.get(0);
                }
                String resultJson = JSON.toJSONString(ret);
                message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
                return message;
            }
            else
            {
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // ETH
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN))
        {
            AccountCollectAddrERC20 ret = new AccountCollectAddrERC20();
            AccountCollectAddrERC20 accountCollectBank = new AccountCollectAddrERC20();
            accountCollectBank.setAccountId(accountId);
            accountCollectBank.setStockinfoId(FundConsts.WALLET_ETH_TYPE);// TOKEN 共用同一个ETH地址
            banklist = accountCollectAddrERC20Service.findList(accountCollectBank);
            if (banklist.size() > 0)
            {
                AccountCollectAddrERC20 bank = banklist.get(0);
                bank.setCollectAddr(EncryptUtils.desDecrypt(bank.getCollectAddr()));
                ret = bank;
            }

            String resultJson =  JSON.toJSONString(ret);
            message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
            return message;
        }
        else
        {
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
    }
    
    /**
     * 新增提币收款地址
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/addWithdrawAddr")
    @ApiOperation(value = "新增提币收款地址")
    public AppsMessage withdrawAddrAdd(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        WithdrawAddrModel withdrawAddrModel = checkSign(params, WithdrawAddrModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, withdrawAddrModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(withdrawAddrModel.getAuthToken());
        checkSesion(session,withdrawAddrModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
        // 判断当前币种是否存在
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(withdrawAddrModel.getSymbol());
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        if (list.size() == 0)
        {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        else
        {
            stockInfo = list.get(0);
        }
        if (StringUtils.isEmpty(withdrawAddrModel.getCollectAddr()) || StringUtils.isEmpty(withdrawAddrModel.getFundPwd()) || StringUtils.isEmpty(withdrawAddrModel.getGa())
                || StringUtils.isEmpty(withdrawAddrModel.getSms())) { throw new BusinessException(CommonEnums.PARAMS_VALID_ERR); }
        // 资金密码
        if (com.blocain.bitms.tools.utils.StringUtils.isBlank(account.getWalletPwd())) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_NOEXIST, null); }
        boolean wallertpwdvalidate = EncryptUtils.validatePassword(String.valueOf(withdrawAddrModel.getFundPwd()), account.getWalletPwd());
        if (!wallertpwdvalidate) { throw new BusinessException(CommonEnums.ERROR_WALLET_VALID_FAILED, null); }
        PolicyModel policy = new PolicyModel();
        policy.setGa(withdrawAddrModel.getGa());
        policy.setSms(withdrawAddrModel.getSms());
        account.setSecurityPolicy(4);// GA+短信
        accountPolicyService.validSecurityPolicy(account, policy);
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN))
        {
            if (stockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue())
            {
                AccountCollectAddr accountCollectAddr = new AccountCollectAddr();
                accountCollectAddr.setStockinfoId(stockInfo.getId());
                accountCollectAddr.setCollectAddr(withdrawAddrModel.getCollectAddr());
                accountCollectAddr.setAccountId(accountId);
                accountCollectAddr.setCreateBy(accountId);
                accountCollectAddr.setCertStatus(FundConsts.WALLET_AUTH_STATUS_AUTH);
                String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
                if (StringUtils.isBlank(lang)) lang = LanguageUtils.getLang(request);
                lang = "en_US";
                fundCurrentService.doWithdrawAddrAdd(lang, accountCollectAddr);
            }
            else
            {
                throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
            }
        }
        else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN))
        {
            AccountCollectAddrERC20 accountCollectAddrERC20 = new AccountCollectAddrERC20();
            accountCollectAddrERC20.setAccountId(accountId);
            accountCollectAddrERC20.setCreateBy(accountId);
            accountCollectAddrERC20.setCollectAddr(withdrawAddrModel.getCollectAddr());
            accountCollectAddrERC20.setStockinfoId(stockInfo.getId());
            AccountCollectAddrERC20 accountCollectBankSearch = new AccountCollectAddrERC20();
            accountCollectBankSearch.setAccountId(accountId);
            accountCollectBankSearch.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
            List<AccountCollectAddrERC20> banklist = accountCollectAddrERC20Service.findList(accountCollectBankSearch);
            if (banklist.size() > 0)
            {
                AccountCollectAddrERC20 bank = banklist.get(0);
                bank.setCollectAddr(EncryptUtils.desEncrypt(accountCollectAddrERC20.getCollectAddr()));
                bank.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
                bank.setStatus(FundConsts.PUBLIC_STATUS_YES);
                bank.setCertStatus(FundConsts.WALLET_AUTH_STATUS_AUTH);
                accountCollectAddrERC20Service.updateByPrimaryKey(bank);
            }
            else
            {
                accountCollectAddrERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                accountCollectAddrERC20.setCreateBy(accountId);
                accountCollectAddrERC20.setCreateDate(new Date());
                accountCollectAddrERC20.setIsActivate(FundConsts.PUBLIC_STATUS_YES);
                accountCollectAddrERC20.setStatus(FundConsts.PUBLIC_STATUS_YES);
                accountCollectAddrERC20.setCertStatus(FundConsts.WALLET_AUTH_STATUS_AUTH);
                accountCollectAddrERC20.setCollectAddr(EncryptUtils.desEncrypt(accountCollectAddrERC20.getCollectAddr()));//
                accountCollectAddrERC20Service.insert(accountCollectAddrERC20);
            }
        }
        else
        {
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_WITHDRAW);
        }
        Map<String, String> map = new HashMap<>();
        if (stockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue())
        {
            map.put("stockCode", "BTC");
        }
        else
        {
            // 具体的stockCode
            map.put("stockCode", "ETH");
        }
        map.put("collectAddr", withdrawAddrModel.getCollectAddr());
        map.put("certStatus", FundConsts.PUBLIC_STATUS_YES);
        map.put("isActivate", FundConsts.PUBLIC_STATUS_YES);
        map.put("status", FundConsts.WALLET_AUTH_STATUS_AUTH);
        String resultJson = JSON.toJSONString(map);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
    
    private void checkAccountDataValidate(Account account)
    {
        if (null == account) { throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT); }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) { throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK); }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_DATA_VALID_ERR);
        }
    }
}
