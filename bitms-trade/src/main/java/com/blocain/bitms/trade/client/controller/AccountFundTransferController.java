package com.blocain.bitms.trade.client.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.ClientParameter;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ParameterUtils;
import com.blocain.bitms.tools.utils.ValidateUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundTransfer;
import com.blocain.bitms.trade.fund.service.AccountFundTransferService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * AccountFundTransfer 账户资金划拨接口
 * <p>File：AccountFundTransfer.java </p>
 * <p>Title: AccountFundTransfer </p>
 * <p>Description:AccountFundTransfer </p>
 * <p>Copyright: Copyright (c) 2017/7/27</p>
 * <p>Company: BloCain</p>
 *
 * @author sunbiao
 * @version 1.0
 */
//@Controller
//@RequestMapping(BitmsConst.CLIENT)
public class AccountFundTransferController extends GenericController
{
//    public static final Logger         logger = LoggerFactory.getLogger(AccountFundTransferController.class);
//
//    @Autowired(required = false)
//    private AccountFundTransferService accountFundTransferService;
//
//    /**
//     * 拉取账户资金待划拨申请数据，用于bitpay真正提币划拨
//     * @param request
//     * @param param
//     * @return
//     * @throws BusinessException
//     */
//    @ResponseBody
//    @RequestMapping(value = "/fund/pullAccountFundTransferData", method = RequestMethod.POST)
//    public JsonMessage pullAccountFundTransferData(HttpServletRequest request,
//            ClientParameter param) throws BusinessException
//    {
//        logger.debug("拉取账户资金待划拨申请数 start...");
//        AccountFundTransfer accountFundTransfer = new AccountFundTransfer();
//        accountFundTransfer.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
//        accountFundTransfer.setTransferStatus(
//                FundConsts.ACCOUNT_FUND_TRANSFER_STATUS_TRANSFERPENDING); // 待划拨
//        logger.debug("pullAccountFundTransferData accountFundTransfer:"
//                + accountFundTransfer.toString());
//        List<AccountFundTransfer> list = accountFundTransferService
//                .findList(accountFundTransfer);
//        logger.debug("拉取账户资金待划拨申请数 end...");
//        return this.getJsonMessage(CommonEnums.SUCCESS, list);
//    }
//
//    /**
//     * bitpay真正提币划拨处理成功后，回填更新账户资金待划拨申请数据以及对应账户资金流水数据的划拨状态
//     * @param request
//     * @param param
//     * @return
//     * @throws BusinessException
//     */
//    @ResponseBody
//    @RequestMapping(value = "/fund/pushAccountFundTransferStatus", method = RequestMethod.POST)
//    public JsonMessage pushAccountFundTransferStatus(HttpServletRequest request,
//            ClientParameter param) throws BusinessException
//    {
//        logger.debug("回填更新账户资金待划拨申请数据以及对应账户资金流水数据的划拨状态 start...");
//        accountFundTransferService.updatePushAccountFundTransferStatus(param);
//        logger.debug("回填更新账户资金待划拨申请数据以及对应账户资金流水数据的划拨状态 end...");
//        return this.getJsonMessage(CommonEnums.SUCCESS);
//    }
//
//    public static void main(String[] args)
//    {
//        // 回调交易平台，添加交易ID易
//        List<Map<String, String>> callbackList = Lists.newArrayList();
//        Map<String, String> withdrawMap = Maps.newHashMap();
//        withdrawMap.put("id", "1234567");
//        withdrawMap.put("transId", "234567");
//        callbackList.add(withdrawMap);
//        withdrawMap = Maps.newHashMap();
//        withdrawMap.put("id", "1234563");
//        withdrawMap.put("transId", "234567");
//        callbackList.add(withdrawMap);
//        Map<String, String> param = Maps.newHashMap();
//        String list = JSONObject.toJSONString(callbackList);
//        System.out.println("list=" + list);
//        param.put("list", list);
//        System.out.println("param=" + param.toString());
//        //
//        String data = ParameterUtils.getDataFromMap(param);
//        System.out.println("data=" + data);
//        int dataLen = ValidateUtils.length(data);
//        System.out.println("dataLen=" + dataLen);
//        String userKey = "ad6397b8482c44e794d8b4195d7c2f2d";
//        String userDes = ParameterUtils.getUserDes(userKey, dataLen);
//        System.out.println("userDes=" + userDes);
//        Map<String, String> httpMap = Maps.newHashMap();
//        httpMap.put("userKey", userKey);
//        httpMap.put("dataLen", String.valueOf(dataLen));
//        httpMap.put("userDes", userDes);
//        httpMap.put("data", data);
//        System.out.println("userKey=" + httpMap.get("userKey"));
//        System.out.println("userDes=" + httpMap.get("userDes"));
//        System.out.println("dataLen=" + httpMap.get("dataLen"));
//        System.out.println("data=" + httpMap.get("data"));
//        Map<String, String> httpMapNew = Maps.newHashMap();
//        httpMapNew = ParameterUtils.getMapFromData(data);
//        System.out.println("list=" + httpMapNew.get("list"));
//    }
}
