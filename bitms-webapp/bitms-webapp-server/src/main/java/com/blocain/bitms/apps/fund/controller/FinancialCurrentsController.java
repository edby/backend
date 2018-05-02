package com.blocain.bitms.apps.fund.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.CurrentsModel;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

/**
 * <p>Author：XiaoDing</p>
 * <p>Description:FinancialCurrentsController</p>
 * <p>Date: Create in 8:56 2018/3/27</p>
 * <p>Modify By: XiaoDing</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class FinancialCurrentsController extends AppsController {
    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private AccountFundCurrentService accountFundCurrentService;

    /**
     * 查询借款记录
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/currents/financialCurrentsList")
    public AppsMessage financialCurrentsList(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        CurrentsModel financialCurrents = checkSign(params, CurrentsModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, financialCurrents)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(financialCurrents.getAuthToken());
        PaginateResult<AccountFundCurrent> result = new PaginateResult<>();
        if (beanValidator(message, financialCurrents)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            // 限定自己账户的账户借贷资产情况
            AccountFundCurrent entity = new AccountFundCurrent();
            entity.setRemark(financialCurrents.getSymbol());
            entity.setAccountId(accountId);
            //判断当前查询还是历史查询
            boolean isHisValue = StringUtils.equalsIgnoreCase(financialCurrents.getIsHis(), "yes");
            entity.setTableName(isHisValue ? getStockInfo(financialCurrents.getSymbol()).getTableFundCurrentHis() : getStockInfo(financialCurrents.getSymbol()).getTableFundCurrent());
            if (null == entity.getTableName()) {
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            //判断时间段
            if (!StringUtils.isBlank(financialCurrents.getTimeStart())) {
                entity.setTimeStart(financialCurrents.getTimeStart() + " 00:00:00");
            }
            if (!StringUtils.isBlank(financialCurrents.getTimeEnd())) {
                entity.setTimeEnd(financialCurrents.getTimeEnd() + " 23:59:59");
            }
            //分页对象
            Pagination pagin = new Pagination();
            if (financialCurrents.getRows() != null) {
                pagin.setRows(financialCurrents.getRows());
            }
            if (financialCurrents.getPage() != null) {
                pagin.setPage(financialCurrents.getPage());
            }
            result = accountFundCurrentService.findListByAccount(pagin, entity,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE_SDF,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_SPOT,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WALLET,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_SPOT_2_WEALTH,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_WEALTH_2_SPOT,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB,
                    FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD
            );

        }
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    public StockInfo getStockInfo(String symbol)
    {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(symbol);
        List<StockInfo> list = stockInfoService.findList(stockInfo);
        if (list.size() != 0)
        {
            stockInfo = list.get(0);
        }
        return stockInfo;
    }
}



