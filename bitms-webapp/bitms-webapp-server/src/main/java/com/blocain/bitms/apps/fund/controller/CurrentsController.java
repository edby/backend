package com.blocain.bitms.apps.fund.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.CurrentsModel;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>Author：XiaoDing</p>
 * <p>Description:CurrentsController</p>
 * <p>Date: Create in 10:56 2018/3/27</p>
 * <p>Modify By: XiaoDing</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class CurrentsController extends AppsController {
    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private AccountFundCurrentService accountFundCurrentService;

    /**
     * 查询交易流水
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/currents/currentsList")
    public AppsMessage currentsList(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        CurrentsModel currents = checkSign(params, CurrentsModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, currents)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(currents.getAuthToken());
        PaginateResult<AccountFundCurrent> result = new PaginateResult<>();
        if (beanValidator(message, currents)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            // 限定自己账户的账户借贷资产情况
            AccountFundCurrent entity = new AccountFundCurrent();
            entity.setRemark(currents.getSymbol());
            entity.setAccountId(accountId);
            //判断当前查询还是历史查询
            boolean isHisValue = StringUtils.equalsIgnoreCase(currents.getIsHis(), "yes");
            entity.setTableName(isHisValue ? getStockInfo(currents.getSymbol()).getTableFundCurrentHis() : getStockInfo(currents.getSymbol()).getTableFundCurrent());
            if (null == entity.getTableName()) {
                throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);
            }
            //判断时间段
            if (!StringUtils.isBlank(currents.getTimeStart())) {
                entity.setTimeStart(currents.getTimeStart() + " 00:00:00");
            }
            if (!StringUtils.isBlank(currents.getTimeEnd())) {
                entity.setTimeEnd(currents.getTimeEnd() + " 23:59:59");
            }
            //分页对象
            Pagination pagin = new Pagination();
            if (currents.getRows() != null) {
                pagin.setRows(currents.getRows());
            }
            if (currents.getPage() != null) {
                pagin.setPage(currents.getPage());
            }
            result = accountFundCurrentService.findListByAccount(pagin, entity);
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



