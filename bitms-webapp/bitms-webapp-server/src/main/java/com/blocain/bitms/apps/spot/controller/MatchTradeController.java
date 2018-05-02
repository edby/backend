package com.blocain.bitms.apps.spot.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.quotation.controller.QuotationController;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.apps.spot.model.EntrustCancelModel;
import com.blocain.bitms.apps.spot.model.EntrustModel;
import com.blocain.bitms.apps.spot.model.MathTradeModel;
import com.blocain.bitms.apps.spot.model.PremiumModel;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.NumericUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetPremium;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetPremiumService;
import com.blocain.bitms.trade.risk.service.RiskService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.TradeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * MatchTradeController Introduce
 * <p>Title: MatchTradeController</p>
 * <p>File：MatchTradeController.java</p>
 * <p>Description: MatchTradeController</p>
 * <p>Copyright: Copyright (c) 2018-03-20</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.SOPT)
public class MatchTradeController extends QuotationController {
    public static final Logger logger = LoggerFactory.getLogger(MatchTradeController.class);

    @Autowired(required = false)
    private TradeService tradeService;

    @Autowired(required = false)
    private AccountService accountService;

    @Autowired(required = false)
    private SysParameterService sysParameterService;

    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;

    @Autowired(required = false)
    private RiskService riskService;

    @Autowired(required = false)
    private StockRateService stockRateService;

    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    AccountDebitAssetPremiumService accountDebitAssetPremiumService;

    /**
     * 委托买入操作
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/doMatchBuy")
    public AppsMessage doMatchBuyRequest(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        EntrustModel tokenRequest = checkSign(params, EntrustModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        if (beanValidator(message, tokenRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            BigDecimal entrustAmt = tokenRequest.getEntrustAmt();
            BigDecimal entrustPrice = tokenRequest.getEntrustPrice();
            String entrustType = tokenRequest.getEntrustType();
            Long exchangePairMoney = null;
            // 验证和获取交易对ID
            StockInfo stockInfo = new StockInfo();
            stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(tokenRequest.getPair());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("交易对错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 验证交易开关
            checkSwitch();// 检查开关
            checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
            // 调用方法
            doMatchBuy(entrustAmt, entrustPrice, entrustType, stockInfo, accountId);
        }
//        String resultJson = JSON.toJSONString(params);
//        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
//        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 委托卖出操作
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/doMatchSell")
    public AppsMessage doMatchSellRequest(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        EntrustModel tokenRequest = checkSign(params, EntrustModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        if (beanValidator(message, tokenRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            BigDecimal entrustAmt = tokenRequest.getEntrustAmt();
            BigDecimal entrustPrice = tokenRequest.getEntrustPrice();
            String entrustType = tokenRequest.getEntrustType();
            Long exchangePairMoney = null;
            // 验证和获取交易对ID
            StockInfo stockInfo = new StockInfo();
            stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(tokenRequest.getPair());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("交易对错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 验证交易开关
            checkSwitch();// 检查开关
            checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
            // 调用方法
            doMatchSell(entrustAmt, entrustPrice, entrustType, stockInfo, accountId);
        }
//        String resultJson = JSON.toJSONString(params);
//        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
//        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 委托撤单操作
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/doMatchCancel")
    public AppsMessage doMatchCancelRequest(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        EntrustCancelModel tokenRequest = checkSign(params, EntrustCancelModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        if (beanValidator(message, tokenRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            Long id = tokenRequest.getEntrustId();
            Long exchangePairMoney = null;
            // 验证和获取交易对ID
            StockInfo stockInfo = new StockInfo();
            stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(tokenRequest.getPair());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("交易对错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 验证交易开关
            checkSwitch();// 检查开关
            checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
            // 调用方法
            doMatchCancel(id, stockInfo, accountId);
        }
//        String resultJson = JSON.toJSONString(params);
//        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
//        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 当前委托列表
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/entrustxOnDoing")
    public AppsMessage entrustxOnDoingRequest(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        MathTradeModel tokenRequest = checkSign(params, MathTradeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        PaginateResult<EntrustVCoinMoney> result = new PaginateResult<>();
        if (beanValidator(message, tokenRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            Long exchangePairMoney = null;
            // 验证和获取交易对ID
            StockInfo stockInfo = new StockInfo();
            stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(tokenRequest.getPair());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("交易对错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 验证交易开关
            checkSwitch();// 检查开关
            checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
            // 调用方法
            EntrustVCoinMoney entity = new EntrustVCoinMoney();
            entity.setAccountId(accountId);// 个人数据
            entity.setTableName(stockInfo.getTableEntrust());
            Pagination pagin = new Pagination();
            copyPageProperties(tokenRequest, pagin);
            result = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyPagin(pagin, entity);
        }
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 历史委托列表
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/entrustxOnHistory")
    public AppsMessage entrustxOnHistoryRequest(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        MathTradeModel tokenRequest = checkSign(params, MathTradeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        PaginateResult<EntrustVCoinMoney> result = new PaginateResult<>();
        if (beanValidator(message, tokenRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            Long exchangePairMoney = null;
            // 验证和获取交易对ID
            StockInfo stockInfo = new StockInfo();
            stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(tokenRequest.getPair());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("交易对错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 验证交易开关
            checkSwitch();// 检查开关
            checkTradeSwitch(exchangePairMoney);// 检查币种交易开关
            // 调用方法
            EntrustVCoinMoney entity = new EntrustVCoinMoney();
            entity.setAccountId(accountId);// 个人数据
            entity.setTableName(stockInfo.getTableEntrust());
            Pagination pagin = new Pagination();
            copyPageProperties(tokenRequest, pagin);
            result = entrustVCoinMoneyService.getAccountHistoryEntrustVCoinMoneyList(pagin, entity);
        }
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 当前用户交易对下指标查询
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/getAccountFundAsset")
    public AppsMessage getAccountFundAssetRequest(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        MathTradeModel tokenRequest = checkSign(params, MathTradeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        FundChangeModel accountFundAsset = new FundChangeModel();
        if (beanValidator(message, tokenRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            Long exchangePairMoney = null;
            // 验证和获取交易对ID
            StockInfo stockInfo = new StockInfo();
            stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(tokenRequest.getPair());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("交易对错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 纯现货交易
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT)) {
                boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
                Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
                accountFundAsset = getAccountAsset(accountId, exchangePairVCoin, exchangePairMoney);
            }
            // 杠杆现货交易
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT)) {
                boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
                Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
                accountFundAsset = getAccountAsset(accountId, exchangePairVCoin, exchangePairMoney);
            }
            // 合约交易
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT)) {
                boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
                Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
                accountFundAsset = getAccountAsset(accountId, exchangePairVCoin, exchangePairMoney);
            }
            accountFundAsset.setTradeAmtUnit(stockInfo.getTradeAmtUnit());
            accountFundAsset.setCapitalAmtUnit(stockInfo.getCapitalAmtUnit());
        }
        String resultJson = JSON.toJSONString(accountFundAsset);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 溢价费记录
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/premiumData")
    public AppsMessage getData(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        PremiumModel tokenRequest = checkSign(params, PremiumModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());

        String timeStart = tokenRequest.getTimeStart();
        String timeEnd = tokenRequest.getTimeEnd();
        Pagination pagin = new Pagination(tokenRequest.getPage(), tokenRequest.getRows());
        AccountDebitAssetPremium accountCandyRecord = new AccountDebitAssetPremium();
        accountCandyRecord.setAccountId(session.getId());
        if (!StringUtils.isBlank(timeStart)) {
            accountCandyRecord.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd)) {
            accountCandyRecord.setTimeEnd(timeEnd + " 23:59:59");
        }
        PaginateResult<AccountDebitAssetPremium> data = accountDebitAssetPremiumService.search(pagin, accountCandyRecord);
        String resultJson = JSON.toJSONString(data);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 历史委托列表
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/historyEntrust")
    public AppsMessage historyEntrust(HttpServletRequest request, String isHis, String timeStart, String timeEnd) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        MathTradeModel tokenRequest = checkSign(params, MathTradeModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session, tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        PaginateResult<EntrustVCoinMoney> result = new PaginateResult<>();
        if (beanValidator(message, tokenRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            Long exchangePairMoney = null;
            // 验证和获取交易对ID
            StockInfo stockInfo = new StockInfo();
            stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(tokenRequest.getPair());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("交易对错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 调用方法
            EntrustVCoinMoney entity = new EntrustVCoinMoney();
            entity.setAccountId(accountId);// 个人数据
            boolean isHisValue = org.apache.commons.lang3.StringUtils.equalsIgnoreCase(isHis, "yes");
            if (!org.apache.commons.lang3.StringUtils.isBlank(timeStart)) {
                entity.setTimeStart(timeStart + " 00:00:00");
            }
            if (!org.apache.commons.lang3.StringUtils.isBlank(timeEnd)) {
                entity.setTimeEnd(timeEnd + " 23:59:59");
            }
            entity.setTableName(isHisValue ? getStockInfo(exchangePairMoney).getTableEntrustHis() : getStockInfo(exchangePairMoney).getTableEntrust());
            Pagination pagin = new Pagination();
            copyPageProperties(tokenRequest, pagin);
            result = entrustVCoinMoneyService.getAccountHistoryEntrustVCoinMoneyList(pagin, entity);
        }
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 委托卖出操作
     *
     * @param entrustAmt
     * @param entrustPrice
     * @param entrustType
     * @param stockInfo
     * @param accountId
     * @throws BusinessException
     */
    public void doMatchSell(BigDecimal entrustAmt, BigDecimal entrustPrice, String entrustType, StockInfo stockInfo, Long accountId) throws BusinessException {
        com.blocain.bitms.trade.trade.model.EntrustModel entrustModel = new com.blocain.bitms.trade.trade.model.EntrustModel();
        String entrustDirect = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode();// 现货卖出
        logger.debug("入参：entrustAmt=" + entrustAmt + " entrustPrice=" + entrustPrice + " entrustType=" + entrustType);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        checkEntrustMaxCnt(accountId, stockInfo.getId());
        // 委托类型判断
        if (StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())
                || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode())) {
            if (StringUtils.equalsIgnoreCase(entrustType, stockInfo.getOpenEntrustType())
                    || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITANDMARKETPRICE.getCode())) {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围正确");
                entrustModel.setEntrustType(entrustType);
            } else {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } else {
            logger.debug("委托类型(限价limitPrice、市价marketPrice)范围错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode())) {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(BigDecimal.ZERO);
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            if (entrustModel.getEntrustAmt().compareTo(stockInfo.getSellMinAmount()) < 0 || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0) {
                logger.debug("委托范围错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getSellAmountPrecision());
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())) {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(entrustPrice);
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            BigDecimal sellMaxCnt = stockInfo.getMaxSingleSellEntrustAmt();
            logger.debug("单笔卖出上限：" + sellMaxCnt);
            if (entrustModel.getEntrustAmt().compareTo(sellMaxCnt) > 0) {
                logger.debug("委托范围错误 已经超过系统单笔卖出上限");
                throw new BusinessException(CommonEnums.ERROR_GT_MAX_AMT);
            }
            BigDecimal sellMinAmt = stockInfo.getSellMinAmount();
            logger.debug("单笔卖出下限：" + sellMinAmt);
            if (entrustModel.getEntrustAmt().compareTo(sellMinAmt) < 0) {
                logger.debug("委托范围错误 已经超过系统单笔卖出下限");
                throw new BusinessException(CommonEnums.ERROR_GT_MIN_AMT);
            }
            if (entrustModel.getEntrustAmt().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellAmountPrecision())))) < 0
                    || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0) {
                logger.debug("委托数量范围不对");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 委托价格0~999999个
            if (entrustModel.getEntrustPrice().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellPricePrecision())))) < 0
                    || entrustModel.getEntrustPrice().compareTo(BigDecimal.valueOf(999999)) >= 0) {
                logger.debug("委托价格不在范围内");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getSellAmountPrecision());
            NumericUtils.checkDecimalDigits("entrustPrice", entrustModel.getEntrustPrice(), stockInfo.getSellPricePrecision());
            Account account = accountService.selectByPrimaryKey(accountId);
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) {
                logger.debug("用户异常");
                throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK);
            }
            // ---------------------------入参判断 end ------------------------------------------
            // 风控
            riskService.entrustRisk(stockInfo, accountId, entrustDirect, entrustPrice, entrustType, exchangePairVCoin, stockInfo.getId());
        }
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(stockInfo.getId());
        stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);// 卖出费率
        List<StockRate> list = stockRateService.findList(stockRate);
        if (list.size() > 0) {
            stockRate = list.get(0);
            entrustModel.setFeeRate(stockRate.getRate());
        } else {
            logger.debug("费率有问题");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        logger.debug("/matchTrade/doMatchSell page form = " + entrustModel.toString());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(accountId);
        entrustModel.setEntrustDirect(entrustDirect);// 委托卖出BTC
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setStockinfoIdEx(stockInfo.getId());
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(stockInfo.getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(accountId, exchangePairVCoin, stockInfo.getId());
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(stockInfo.getTableEntrust());
        clearEntrustOnDoingCache(entrustVCoinMoney, stockInfo.getId());
    }

    /**
     * 委托买入操作
     *
     * @param entrustAmt
     * @param entrustPrice
     * @param entrustType
     * @param stockInfo
     * @param accountId
     * @throws BusinessException
     */
    public void doMatchBuy(BigDecimal entrustAmt, BigDecimal entrustPrice, String entrustType, StockInfo stockInfo, Long accountId) throws BusinessException {
        com.blocain.bitms.trade.trade.model.EntrustModel entrustModel = new com.blocain.bitms.trade.trade.model.EntrustModel();
        String entrustDirect = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode();// 现货买入
        logger.debug("入参：entrustAmt=" + entrustAmt + " entrustPrice=" + entrustPrice + " entrustType=" + entrustType);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        checkEntrustMaxCnt(accountId, stockInfo.getId());
        // 委托类型判断
        if (StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())
                || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode())) {
            if (StringUtils.equalsIgnoreCase(entrustType, stockInfo.getOpenEntrustType())
                    || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITANDMARKETPRICE.getCode())) {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围正确");
                entrustModel.setEntrustType(entrustType);
            } else {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围错误");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        } else {
            logger.debug("委托类型(限价limitPrice、市价marketPrice)范围错误");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode())) {
            entrustModel.setEntrustAmt(BigDecimal.ZERO);
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(entrustAmt);
            // ---------------------------入参判断 start ------------------------------------------
            // 委托价格0~999999个
            if (entrustModel.getEntrustAmtEx().compareTo(stockInfo.getBuyMinAmount()) < 0 || entrustModel.getEntrustAmtEx().compareTo(BigDecimal.valueOf(999999)) > 0) {
                logger.debug("数量范围不对");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getBuyAmountPrecision());
            Account account = accountService.selectByPrimaryKey(accountId);
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) {
                logger.debug("账户状态异常");
                throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK);
            }
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())) {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(entrustPrice);
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            // ---------------------------入参判断 start ------------------------------------------
            BigDecimal buyMaxCnt = stockInfo.getMaxSingleBuyEntrustAmt();
            logger.debug("单笔买入上限：" + buyMaxCnt);
            if (entrustModel.getEntrustAmt().compareTo(buyMaxCnt) > 0) {
                logger.debug("委托范围错误 已经超过系统参数单笔买入上限");
                throw new BusinessException(CommonEnums.ERROR_GT_MAX_AMT);
            }
            BigDecimal buyMinAmt = stockInfo.getBuyMinAmount();
            logger.debug("单笔买入下限：" + buyMinAmt);
            if (entrustModel.getEntrustAmt().compareTo(buyMinAmt) < 0) {
                logger.debug("委托范围错误 已经超过系统单笔买入下限");
                throw new BusinessException(CommonEnums.ERROR_GT_MIN_AMT);
            }
            // BMS委托数量999~999999个
            if (entrustModel.getEntrustAmt().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyAmountPrecision())))) < 0
                    || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0) {
                logger.debug("委托数量范围不对");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 委托价格0~999999个
            if (entrustModel.getEntrustPrice().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyPricePrecision())))) < 0
                    || entrustModel.getEntrustPrice().compareTo(BigDecimal.valueOf(999999)) >= 0) {
                logger.debug("委托价格不在范围内");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getBuyAmountPrecision());
            NumericUtils.checkDecimalDigits("entrustPrice", entrustModel.getEntrustPrice(), stockInfo.getBuyPricePrecision());
            Account account = accountService.selectByPrimaryKey(accountId);
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) {
                logger.debug("账户状态异常");
                throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK);
            }
            // ---------------------------入参判断 end ------------------------------------------
            // 风控
            riskService.entrustRisk(stockInfo, accountId, entrustDirect, entrustPrice, entrustType, exchangePairVCoin, stockInfo.getId());
        }
        entrustModel.setFee(BigDecimal.ZERO);
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(stockInfo.getId());
        stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);// 买入费率
        List<StockRate> list = stockRateService.findList(stockRate);
        if (list.size() > 0) {
            stockRate = list.get(0);
            entrustModel.setFeeRate(stockRate.getRate());
            logger.debug("费率" + stockRate.getRate());
        } else {
            logger.debug("费率有问题");
            throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
        }
        logger.debug("检查 完毕");
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(accountId);
        entrustModel.setEntrustDirect(entrustDirect);// 现货买入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setStockinfoIdEx(stockInfo.getId());
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(stockInfo.getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(accountId, exchangePairVCoin, stockInfo.getId());
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(stockInfo.getTableEntrust());
        clearEntrustOnDoingCache(entrustVCoinMoney, stockInfo.getId());
    }

    /**
     * 委托撤单操作
     *
     * @param entrustId
     * @param stockInfo
     * @throws BusinessException
     */
    public void doMatchCancel(Long entrustId, StockInfo stockInfo, Long accountId) throws BusinessException {
        com.blocain.bitms.trade.trade.model.EntrustModel entrustModel = new com.blocain.bitms.trade.trade.model.EntrustModel();
        entrustModel.setEntrustId(entrustId);
        logger.debug("exchangePairMoney=" + stockInfo.getId());
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        checkSwitch();// 检查开关
        checkTradeSwitch(stockInfo.getId());// 检查币种交易开关
        logger.debug("检查开关完毕");
        EntrustVCoinMoney entrustDB = new EntrustVCoinMoney();
        entrustDB = entrustVCoinMoneyService.selectByPrimaryKey(stockInfo.getTableEntrust(), entrustModel.getEntrustId());
        logger.debug("操作者=" + accountId + " 拥有者=" + entrustDB.getAccountId());
        if (accountId.longValue() != FundConsts.SYSTEM_ACCOUNT_ID.longValue()) // 超级用户可以为用户撤单
        {
            if (accountId.longValue() != entrustDB.getAccountId().longValue()) {
                logger.debug("委托交易 越权");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
        }
        Account account = accountService.selectByPrimaryKey(accountId);
        checkAccountDataValidate(account);
        logger.debug("校验数据完毕");
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setTableName(stockInfo.getTableEntrust());
        entrustModel.setStockinfoIdEx(stockInfo.getId());
        entrustModel.setAccountId(account.getId());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        tradeService.entrustWithdrawX(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(accountId, exchangePairVCoin, stockInfo.getId());
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(stockInfo.getTableEntrust());
        clearEntrustOnDoingCache(entrustVCoinMoney, stockInfo.getId());
    }

    /**
     * 验证用户有效性
     *
     * @param account
     */
    private void checkAccountDataValidate(Account account) {
        if (null == account) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue()) {
            throw new BusinessException(CommonEnums.ERROR_LOGIN_ACCOUNT);
        }
        if (null != account && !account.verifySignature()) {// 校验数据
            logger.info("账户信息 数据校验失败");
            throw new BusinessException(CommonEnums.ERROR_LOGIN_LOCK);
        }
    }

    /**
     * 检查交易总开关
     */
    private void checkSwitch() {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null) {
            logger.debug("===========开关值空==========");
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        if (!StringUtils.isBlank(params.getValue())) {
            if (!params.getValue().equals("yes")) {
                logger.debug("===========开关已关闭==========");
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            } else {
                logger.debug("===========开关已打开==========");
            }
        } else {
            logger.debug("===========开关值不存在==========");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
    }

    /**
     * 判断最大委托量
     *
     * @param accountId
     * @param exchangePairMoney
     */
    private void checkEntrustMaxCnt(Long accountId, Long exchangePairMoney) {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_TRADE_ACCOUNT_MAX_ENTRUST_CNT);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params != null) {
            Long cnt = Long.parseLong(params.getValue().toString());
            Long done = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyCnt(accountId, exchangePairMoney);
            if (done.compareTo(cnt) >= 0) {
                throw new BusinessException(CommonEnums.ERROR_GT_MAX_ORDER_CNT);
            }
        }
        params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_TRADE_MONEY_MAX_ENTRUST_CNT);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params != null) {
            Long cnt = Long.parseLong(params.getValue().toString());
            Long doing = entrustVCoinMoneyService.getMoneyDoingEntrustVCoinMoneyCnt(exchangePairMoney);
            if (doing.compareTo(cnt) >= 0) {
                throw new BusinessException(CommonEnums.ERROR_GT_MAX_ORDER_CNT);
            }
        }
    }

    /**
     * 检查交易对小开关
     *
     * @param stockinfoId
     */
    private void checkTradeSwitch(Long stockinfoId) {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockinfoId);
        if (null == stockInfo) {
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
        if (!StringUtils.isBlank(stockInfo.getIsExchange())) {
            if (!stockInfo.getCanTrade().equals("yes")) {
                logger.debug("===========币种交易开关已关闭==========");
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            } else {
                logger.debug("===========币种交易开关已打开==========");
            }
        } else {
            logger.debug("===========币种交易开关值不存在==========");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
    }

    /**
     * 拷贝分页属性
     *
     * @param entityRequest
     * @param pagin
     */
    public void copyPageProperties(MathTradeModel entityRequest, Pagination pagin) {
        if (entityRequest.getRows() != null) {
            pagin.setRows(entityRequest.getRows());
        } else {
            pagin.setRows(BitmsConst.DEFAULT_PAGE_SIZE);
        }
        if (entityRequest.getPage() != null) {
            pagin.setPage(entityRequest.getPage());
        } else {
            pagin.setPage(BitmsConst.DEFAULT_CURRENT_PAGE);
        }
    }

    public StockInfo getStockInfo(Long id) {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
