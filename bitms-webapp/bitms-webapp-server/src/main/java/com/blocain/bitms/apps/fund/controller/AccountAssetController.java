package com.blocain.bitms.apps.fund.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.ConversionEnableModel;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.model.AccountAssetModel;
import com.blocain.bitms.trade.fund.service.AccountAssetService;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.risk.service.EnableService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Author：ChenGang</p>
 * <p>Description: FundController</p>
 * <p>Date: Create in 15:58 2018/3/20</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountAssetController extends AppsController {
    @Autowired(required = false)
    private AccountWalletAssetService accountWalletAssetService;

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private AccountAssetService accountAssetService;

    @Autowired(required = false)
    private EnableService enableService;

    /**
     * 现货账户
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/walletAssetData")
    @ApiOperation(value = "现货账户")
    public AppsMessage walletAssetData(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        AuthTokenRequest tokenRequest = checkSign(params, AuthTokenRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session,tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        AccountWalletAsset entity = new AccountWalletAsset();
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 限定自己账户的账户资产情况
        entity.setAccountId(accountId);
        List<AccountWalletAsset> walletAssetList = accountWalletAssetService.findList(entity);
        if (walletAssetList.size() == 0) {
            getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        String resultJson = JSON.toJSONString(walletAssetList);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 杠杆账户
     *
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/spotAssetData")
    @ApiOperation(value = "当前账户杠杆现货账户资产")
    public AppsMessage spotAssetdata(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        AuthTokenRequest tokenRequest = checkSign(params, AuthTokenRequest.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session,tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        AccountAssetModel entity = new AccountAssetModel();
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        // 限定自己账户的账户资产情况
        entity.setAccountId(accountId);
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setStockType(FundConsts.STOCKTYPE_LEVERAGEDSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < stockInfoList.size(); i++) {
            StockInfo stockInfotemp = stockInfoList.get(i);
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("tableAsset", stockInfotemp.getTableAsset());
            obj.put("tableDebitAsset", stockInfotemp.getTableDebitAsset());
            list.add(obj);
        }
        List<AccountAssetModel> assetList = accountAssetService.findAssetList(entity, list);
        transFormationList(assetList);//修改集合元素
        if (assetList.size() == 0) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        String resultJson = JSON.toJSONString(assetList);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 集合转换，对特殊元素操作
     */
    public void transFormationList(List<AccountAssetModel> assetList) {
        for (int i = 0; i < assetList.size(); i++)
            assetList.get(i).setDebitAmt(assetList.get(i).getDebitAmt() == null ? new BigDecimal(0) : assetList.get(i).getDebitAmt());
    }

    /**
     * 互转可用余额
     *
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/conversionEnableAmount")
    public AppsMessage conversionEnableAmount(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        ConversionEnableModel tokenRequest = checkSign(params, ConversionEnableModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session,tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        EnableModel enableModel = new EnableModel();
        if (beanValidator(message, tokenRequest)) {
            // 获取转成账户
            StockInfo stockInfo = new StockInfo();
            stockInfo.setRemark(tokenRequest.getFromStockinfo());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            String fromStockType = stockInfo.getStockType();
            Long fromStockinfoId = stockInfo.getId();
            // 获取转出账户
            stockInfo = new StockInfo();
            stockInfo.setRemark(tokenRequest.getToStockinfo());
            stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            StockInfo toStockInfo = stockInfoList.get(0);
            String toStockType = toStockInfo.getStockType();
            Long toStockinfoId = toStockInfo.getId();
            // 合约账户转钱包账户
            if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_CONTRACTSPOT)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_DIGITALCOIN)) {
                // enableModel.setAccountId(accountId);
                // enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET);
                // enableModel.setStockinfoId(toStockinfoId);
                // enableModel.setRelatedStockinfoId(fromStockinfoId);
                // enableModel = enableService.entrustTerminalEnable(enableModel);
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            // 钱包账户转合约账户
            else if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_DIGITALCOIN)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_CONTRACTSPOT)) {
                // enableModel.setAccountId(accountId);
                // enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT);
                // enableModel.setStockinfoId(fromStockinfoId);
                // enableModel.setRelatedStockinfoId(toStockinfoId);
                // enableModel = enableService.entrustTerminalEnable(enableModel);
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            // 钱包账户转杠杆现货
            else if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_DIGITALCOIN)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_LEVERAGEDSPOT)) {
                enableModel.setAccountId(accountId);
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT);
                enableModel.setStockinfoId(fromStockinfoId);
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
            }
            // 杠杆现货转钱包账户
            else if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_LEVERAGEDSPOT)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_DIGITALCOIN)) {
                enableModel.setAccountId(accountId);
                enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET);
                enableModel.setStockinfoId(toStockinfoId);
                enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
                enableModel = enableService.entrustTerminalEnable(enableModel);
            } else {
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
        }
        String resultJson = JSON.toJSONString(enableModel);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 提币可用余额
     *
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/withdrawEnableAmount")
    public AppsMessage withdrawEnableAmount(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        ConversionEnableModel tokenRequest = checkSign(params, ConversionEnableModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session,tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) {
            throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        }
        EnableModel enableModel = new EnableModel();
        // 获取账户
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(tokenRequest.getFromStockinfo());
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0) {
            logger.debug("币种错误");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        stockInfo = stockInfoList.get(0);
        if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)
                || StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
            enableModel.setAccountId(accountId);
            enableModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW);
            enableModel.setStockinfoId(stockInfo.getId());
            enableModel.setRelatedStockinfoId(FundConsts.WALLET_USD_TYPE);
            enableModel = enableService.entrustTerminalEnable(enableModel);
        } else {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        String resultJson = JSON.toJSONString(enableModel);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
}
