package com.blocain.bitms.apps.fund.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.ConversionModel;
import com.blocain.bitms.apps.fund.model.PaginModel;
import com.blocain.bitms.apps.quotation.controller.QuotationController;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.NumericUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.fund.service.FundService;
import com.blocain.bitms.trade.risk.model.EnableModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
@RequestMapping(BitmsConst.FUND)
public class ConversionController extends QuotationController
{
    @Autowired(required = false)
    private AccountFundCurrentService accountFundCurrentService;
    
    @Autowired(required = false)
    private StockInfoService          stockInfoService;
    
    @Autowired(required = false)
    private FundService               fundService;
    
    @Autowired(required = false)
    private SysParameterService       sysParameterService;
    
    @Autowired(required = false)
    private AccountService            accountService;
    
    /**
     * 互转记录
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/conversionList")
    public AppsMessage spotAssetdata(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        PaginModel paginModel = checkSign(params, PaginModel.class); // 校验签名并返回请求参数

        if (!beanValidator(message, paginModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(paginModel.getAuthToken());
        checkSesion(session,paginModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
        Pagination pagin = new Pagination();
        if (paginModel.getRows() != null)
        {
            pagin.setRows(Integer.valueOf(paginModel.getRows()));
        }
        else
        {
            pagin.setRows(BitmsConst.DEFAULT_PAGE_SIZE);
        }
        if (paginModel.getPage() != null)
        {
            pagin.setPage(Integer.valueOf(paginModel.getPage()));
        }
        else
        {
            pagin.setPage(BitmsConst.DEFAULT_CURRENT_PAGE);
        }
        AccountFundCurrent accountFundCurrent = new AccountFundCurrent();
        accountFundCurrent.setAccountId(accountId);
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setStockType(FundConsts.STOCKTYPE_CONTRACTSPOT);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        String[] aArray = new String[stockInfoList.size()];
        for (int i = 0; i < stockInfoList.size(); i++)
        {
            StockInfo stockInfotemp = stockInfoList.get(i);
            aArray[i] = stockInfotemp.getTableFundCurrent(); // 查询流水表 非 流水视图
        }
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.accountFundCurrentChargeSearch(pagin, accountFundCurrent, aArray);
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
    
    /**
     * 账户互转
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/conversion")
    public AppsMessage conversionEnableAmount(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        ConversionModel conversionModel = checkSign(params, ConversionModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, conversionModel)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(conversionModel.getAuthToken());
        checkSesion(session,conversionModel.getAuthToken());
        Account account = checkAccount(session.getId());
        // 登录判断
        Long accountId = session.getId();
        if (null == accountId) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }

            // 获取转成账户
            StockInfo stockInfo = new StockInfo();
            stockInfo.setRemark(conversionModel.getFromStockinfo());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0)
            {
                logger.debug("交易对错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            String fromStockType = stockInfo.getStockType();
            Long fromStockinfoId = stockInfo.getId();
            // 获取转出账户
            stockInfo = new StockInfo();
            stockInfo.setRemark(conversionModel.getToStockinfo());
            stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0)
            {
                logger.debug("币种错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            StockInfo toStockInfo = stockInfoList.get(0);
            String toStockType = toStockInfo.getStockType();
            Long toStockinfoId = toStockInfo.getId();
            FundModel fundModel = new FundModel();
            fundModel.setAmount(conversionModel.getAmount());
            checkSwitch();
            checkAccountDataValidate(account);
            fundModel.setFee(BigDecimal.ZERO);
            fundModel.setAmountEx(fundModel.getAmount());
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
            // 互转数量判断
            if (null == fundModel.getAmount()
                    || fundModel.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new BusinessException(FundEnums.ERROR_CHARGE_AMOUNT_GREATER_ZEOR); }
            // 合约账户转钱包账户
            if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_CONTRACTSPOT)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_DIGITALCOIN))
            {
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            // 钱包账户转合约账户
            else if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_DIGITALCOIN)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_CONTRACTSPOT))
            {
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            // 钱包账户转杠杆现货
            else if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_DIGITALCOIN)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_LEVERAGEDSPOT))
            {
                checkTradeSwitch(toStockinfoId);
                fundModel.setAccountId(accountId);
                fundModel.setCreateBy(accountId);
                fundModel.setStockinfoId(fromStockinfoId);
                fundModel.setStockinfoIdEx(FundConsts.WALLET_USD_TYPE);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT);
                fundService.fundTransaction(fundModel);
            }
            // 杠杆现货转钱包账户
            else if (StringUtils.equalsIgnoreCase(fromStockType, FundConsts.STOCKTYPE_LEVERAGEDSPOT)
                    && StringUtils.equalsIgnoreCase(toStockType, FundConsts.STOCKTYPE_DIGITALCOIN))
            {
                checkTradeSwitch(fromStockinfoId);
                fundModel.setAccountId(accountId);
                fundModel.setCreateBy(accountId);
                fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET);
                fundModel.setStockinfoId(toStockinfoId);
                fundModel.setStockinfoIdEx(FundConsts.WALLET_USD_TYPE);
                fundService.fundTransaction(fundModel);
            }
            else
            {
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            // 刷新缓存
            StockInfo entity = new StockInfo();
            entity.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            List<StockInfo> list = stockInfoService.findList(entity);
            for (StockInfo stockInfo1 : list)
            {
                // 刷新缓存
                clearAccountAssetCache(accountId, stockInfo1.getTradeStockinfoId(), stockInfo1.getId());
                clearAccountAssetCache(accountId, stockInfo1.getCapitalStockinfoId(), stockInfo1.getId());
            }

        String resultJson = JSON.toJSONString(conversionModel);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
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
    
    /**
     * 检查交易对小开关
     * @param stockinfoId
     */
    private void checkTradeSwitch(Long stockinfoId)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockinfoId);
        if (null == stockInfo) { throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN); }
        if (!StringUtils.isBlank(stockInfo.getIsExchange()))
        {
            if (!stockInfo.getCanTrade().equals("yes"))
            {
                logger.debug("===========币种交易开关已关闭==========");
                throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
            }
            else
            {
                logger.debug("===========币种交易开关已打开==========");
            }
        }
        else
        {
            logger.debug("===========币种交易开关值不存在==========");
            throw new BusinessException(CommonEnums.ERROR_FUNCTION_OPEN);
        }
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

}
