package com.blocain.bitms.apps.fund.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.PaginModel;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountCandyRecord;
import com.blocain.bitms.trade.fund.entity.AccountWalletAsset;
import com.blocain.bitms.trade.fund.service.AccountCandyRecordService;
import com.blocain.bitms.trade.fund.service.AccountWalletAssetService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Author：XiaoDing</p>
 * <p>Description:CandyRecordController</p>
 * <p>Date: Create in 8:56 2018/3/28</p>
 * <p>Modify By: XiaoDing</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class CandyRecordController extends AppsController
{
    @Autowired(required = false)
    AccountCandyRecordService accountCandyRecordService;
    
    @Autowired(required = false)
    Erc20TokenLocalService    erc20TokenLocalService;
    
    @Autowired(required = false)
    StockInfoService          stockInfoService;
    
    @Autowired(required = false)
    AccountWalletAssetService accountWalletAssetService;
    
    /**
     * 查询糖果记录
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/candyRecord")
    public AppsMessage candyRecord(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        PaginModel candyRecord = checkSign(params, PaginModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, candyRecord)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(candyRecord.getAuthToken());
        PaginateResult<AccountCandyRecord> result = new PaginateResult<>();
        if (beanValidator(message, candyRecord))
        {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
            // 限定自己账户的账户借贷资产情况
            AccountCandyRecord entity = new AccountCandyRecord();
            entity.setAccountId(accountId);
            if (!StringUtils.isBlank(candyRecord.getTimeStart()))
            {
                candyRecord.setTimeStart(candyRecord.getTimeStart() + " 00:00:00");
            }
            if (!StringUtils.isBlank(candyRecord.getTimeEnd()))
            {
                candyRecord.setTimeEnd(candyRecord.getTimeEnd() + " 23:59:59");
            }
            // 分页对象
            Pagination pagin = new Pagination();
            if (candyRecord.getRows() != null)
            {
                pagin.setRows(candyRecord.getRows());
            }
            if (candyRecord.getPage() != null)
            {
                pagin.setPage(candyRecord.getPage());
            }
            result = accountCandyRecordService.search(pagin, entity);
        }
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
    
    /**
     * 查询糖果记录
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/biexCandy")
    public AppsMessage biexCandy(HttpServletRequest request) throws BusinessException
    {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        PaginModel auth = checkSign(params, PaginModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, auth)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(auth.getAuthToken());
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        if (beanValidator(message, auth))
        {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
            AccountWalletAsset entity = new AccountWalletAsset();
            entity.setStockinfoId(FundConsts.WALLET_BIEX_TYPE);
            entity.setAccountId(FundConsts.SYSTEM_ACCOUNT_ID);
            BigDecimal hasAmt = BigDecimal.ZERO;
            List<AccountWalletAsset> list = accountWalletAssetService.findList(entity);
            if (list.size() > 0)
            {
                AccountWalletAsset accountWalletAsset = list.get(0);
                hasAmt = accountWalletAsset.getAmount();
            }
            BigDecimal orgAmt = BigDecimal.valueOf(5000000000L);// 原始创建数量
            BigDecimal lastAmt = hasAmt;// 剩余未奖励出去的数量
            BigDecimal burnAmt = BigDecimal.ZERO; // 燃烧掉的数量
            BigDecimal presentAmt = BigDecimal.ZERO; // 奖励出去的数量
            BigDecimal totalSupply = BigDecimal.ZERO; // 当前发行的数量
            AccountCandyRecord record = accountCandyRecordService.findLastRecord(FundConsts.WALLET_BIEX_TYPE);
            if (null != record)
            {
                lastAmt = record.getLastAmt();
            }
            else
            {
                lastAmt = hasAmt;
            }
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_BIEX_TYPE);
            try
            {
                totalSupply = erc20TokenLocalService.erc20_totalSupply(stockInfo.getTokenContactAddr());
                burnAmt = orgAmt.subtract(totalSupply);
            }
            catch (Exception e)
            {
                logger.debug(e.getLocalizedMessage());
            }
            presentAmt = orgAmt.subtract(lastAmt).subtract(burnAmt);
            map.put("lastAmt", lastAmt);// 剩余数量
            map.put("orgAmt", orgAmt);// 供应总量
            map.put("presentAmt", presentAmt);
            map.put("totalSupply", totalSupply);// 现有发行量
            map.put("burnAmt", burnAmt);// 已销毁
        }
        String resultJson = JSON.toJSONString(map);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
}
