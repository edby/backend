/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.apps.fund.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.AccountDebitAssetModel;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.trade.fund.entity.AccountDebitAssetDetail;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetDetailService;
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
 * <p>Description:AccountDebitAssetController</p>
 * <p>Date: Create in 9:12 2018/3/26</p>
 * <p>Modify By: XiaoDing</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class AccountDebitAssetController extends AppsController
{
    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private AccountDebitAssetDetailService accountDebitAssetDetailService;

    /**
     * 查询借款记录
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/accountDebitAsset/getAccountDebitAssetData")
    public AppsMessage getAccountDebitAssetData(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        AccountDebitAssetModel AccountDebitAsset = checkSign(params, AccountDebitAssetModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, AccountDebitAsset)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(AccountDebitAsset.getAuthToken());
        PaginateResult<AccountDebitAssetDetail> result = new PaginateResult<>();
        if (beanValidator(message, AccountDebitAsset))
        {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) { throw new BusinessException(CommonEnums.USER_NOT_LOGIN); }
            // 限定自己账户的账户借贷资产情况
            AccountDebitAssetDetail entity = new AccountDebitAssetDetail();
            entity.setBorrowerAccountId(accountId);
            StockInfo stockInfo = stockInfoService.selectByPrimaryKey(AccountDebitAsset.getRelatedStockinfoId());
            if (null == stockInfo.getTableDebitAssetDetail()) { throw new BusinessException(CommonEnums.ERROR_DB_ACCESS_FAILED);}
            entity.setTableName(stockInfo.getTableDebitAssetDetail());
            //判断时间段
            if (!StringUtils.isBlank(AccountDebitAsset.getTimeStart()))
            {
                entity.setTimeStart(AccountDebitAsset.getTimeStart() + " 00:00:00");
            }
            if (!StringUtils.isBlank(AccountDebitAsset.getTimeEnd()))
            {
                entity.setTimeEnd(AccountDebitAsset.getTimeEnd() + " 23:59:59");
            }
            //分页对象
            Pagination pagin = new Pagination();
            if (AccountDebitAsset.getRows() != null)
            {
                pagin.setRows(AccountDebitAsset.getRows());
            }
            if (AccountDebitAsset.getPage() != null)
            {
                pagin.setPage(AccountDebitAsset.getPage());
            }
            result = accountDebitAssetDetailService.search(pagin, entity);
        }
        String resultJson = JSON.toJSONString(result);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }
}