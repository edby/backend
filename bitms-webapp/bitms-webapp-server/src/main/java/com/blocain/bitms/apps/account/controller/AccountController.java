package com.blocain.bitms.apps.account.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.ChangeAccountBorrow;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.apps.account.beans.AccountResponse;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;

/**
 * AccountController Introduce
 * <p>Title: AccountController</p>
 * <p>File：AccountController.java</p>
 * <p>Description: AccountController</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.ACCOUNT)
public class AccountController extends AppsController {
    @Autowired(required = false)
    private AccountService accountService;

    @Autowired(required = false)
    private AccountDebitAssetService accountDebitAssetService;

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @ResponseBody
    @RequestMapping("/info")
    public AppsMessage getInfo(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        AuthTokenRequest tokenRequest = checkSign(params, AuthTokenRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        AccountResponse response = new AccountResponse();
        Account account = checkAccount(session.getId());
        BeanUtils.copyProperties(account, response);
        response.setGaState(null != account.getAuthKey());
        response.setPhState(null != account.getMobNo());
        String resultJson = JSON.toJSONString(response);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    @ResponseBody
    @RequestMapping("/setting/changeBorrowSwitch")
    public AppsMessage changeBorrowSwitch(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        ChangeAccountBorrow tokenRequest = checkSign(params, ChangeAccountBorrow.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_USD_TYPE);
        account.setAutoDebit(tokenRequest.getAutoDebit());
        AccountDebitAsset entity = new AccountDebitAsset();
        entity.setTableName(stockInfo.getTableDebitAsset());
        entity.setBorrowerAccountId(session.getId());
        entity.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
        List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
        if (list.size() > 0) {
            entity = list.get(0);
            logger.debug("存在借款 不能关闭");
            if (tokenRequest.getAutoDebit() == 0) {
                throw new BusinessException(1000, "Operation failed bcause of existing debts!", entity.getStockCode());
            }
        }
        accountService.updateByPrimaryKeySelective(account);
        return message;
    }


}
