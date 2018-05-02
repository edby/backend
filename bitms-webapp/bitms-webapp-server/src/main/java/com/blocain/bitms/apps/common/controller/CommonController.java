package com.blocain.bitms.apps.common.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.common.model.CommonModel;
import com.blocain.bitms.apps.common.model.SMSModel;
import com.blocain.bitms.apps.common.model.StockType;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.boss.common.model.DictModel;
import com.blocain.bitms.boss.common.service.DictionaryService;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.LanguageUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Notice;
import com.blocain.bitms.trade.account.service.NoticeService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;

import io.swagger.annotations.ApiOperation;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:CommonController</p>
 * <p>Date: Create in 10:12 2018/3/20</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.COMMON)
public class CommonController extends AppsController {
    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private DictionaryService dictionaryService;

    @Autowired(required = false)
    private NoticeService noticeService;

    @Autowired(required = false)
    private MsgRecordNoSql msgRecordService;

    /**
     * 查询所有数字货币
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/stockinfo/allCoin")
    public AppsMessage allCoin(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        StockType stockType = checkSign(params, StockType.class); // 校验签名并返回请求参数
        if (stockType != null && StringUtils.isNotBlank(stockType.getStockType())) {
            List<StockInfo> stockInfoList = stockInfoService.findListByTypes(stockType.getStockType());
            if (stockInfoList.size() == 0) {
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            message.setData(stockInfoList);
            return message;
        } else {
            List<StockInfo> stockInfoList = stockInfoService.findListByTypes(FundConsts.STOCKTYPE_DIGITALCOIN, FundConsts.STOCKTYPE_CASHCOIN,
                    FundConsts.STOCKTYPE_ERC20_TOKEN);
            if (stockInfoList.size() == 0) {
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            message.setData(stockInfoList);
            return message;
        }
    }

    /**
     * 查询所有交易对
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/stockinfo/allPair")
    public AppsMessage allPair(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        StockType stockType = checkSign(params, StockType.class); // 校验签名并返回请求参数
        StockInfo stockInfoSelect = new StockInfo();
        stockInfoSelect.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        if (stockType != null && StringUtils.isNotBlank(stockType.getStockType())) {
            stockInfoSelect.setStockType(stockType.getStockType());
        }
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfoSelect);
        if (stockInfoList.size() == 0) {
            getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        message.setData(stockInfoList);
        return message;
    }

    /**
     * 取所有字典
     *
     * @return {@link List}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/dictionary")
    @ApiOperation(value = "取所有字典")
    public AppsMessage dictionary(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        checkSign(params, Object.class); // 校验签名并返回请求参数
        List<DictModel> dictModels = dictionaryService.findAllDict();
        if (dictModels.size() == 0) {
            getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        String resultJson = JSON.toJSONString(dictModels);
        message.setData(resultJson);
        return message;
    }

    /**
     * 查询消息公告
     *
     * @return {@link AppsMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/notice")
    public AppsMessage notice(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        Pagination pagin = checkSign(params, Pagination.class); // 校验签名并返回请求参数
        if (StringUtils.isBlank(String.valueOf(pagin.getPage()))
                || StringUtils.isBlank(String.valueOf(pagin.getRows()))) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        Notice notice = new Notice();
        notice.setStatus(true);// 只取已发布的公告
        if (StringUtils.isBlank(notice.getLangType())) {// 设置语言类型
            notice.setLangType(LanguageUtils.getLang(request));
        }
        PaginateResult<Notice> result = noticeService.search(pagin, notice);
        message.setData(result);
        return message;
    }

    /**
     * 根据指定ID获取一条公告
     *
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/noticeDetail")
    @ApiOperation(value = "根据指定ID获取公告")
    public AppsMessage noticeDetail(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        CommonModel entity = checkSign(params, CommonModel.class); // 校验签名并返回请求参数
        long id;
        if (entity.getId() != null) {
            id = entity.getId();
        } else {
            return getJsonMessage(CommonEnums.ERROR_DATA_VALID_ERR);
        }
        Notice noticeDetail = noticeService.selectByPrimaryKey(id);
        if (noticeDetail == null) {
            return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
        }
        message.setData(noticeDetail);
        return message;
    }

    @ResponseBody
    @RequestMapping("/sendSMS")
    public AppsMessage sendMsg(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        SMSModel smsModel = checkSign(params, SMSModel.class); // 校验签名并返回请求参数
        SessionInfo session = AuthTokenUtils.getSession(smsModel.getAuthToken());
        checkSesion(session,smsModel.getAuthToken());
        if (null == session) throw new BusinessException(CommonEnums.ERROR_ILLEGAL_REQUEST);
        if (null == session.getMobile()) throw new BusinessException(AccountEnums.ACCOUNT_PHONE_NOTBIND);
        String phoneKey = new StringBuffer(session.getCountry()).append(session.getMobile()).toString();
        msgRecordService.sendSms(phoneKey,"en_US" );
        return message;
    }

}
