/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.fund.controller;

import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.common.service.DictionaryService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.DateConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.CalendarUtils;
import com.blocain.bitms.tools.utils.CookieUtils;
import com.blocain.bitms.tools.utils.ExportExcel;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountFundCurrent;
import com.blocain.bitms.trade.fund.service.AccountFundCurrentService;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Fund资金流水  控制器
 * <p>File：CurrentsController.java</p>
 * <p>Title: CurrentsController</p>
 * <p>Description:CurrentsController</p>
 * <p>Copyright: Copyright (c) 2017年7月14日</p>
 * <p>Company: BloCain</p>
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
@Api(description = "Fund资金流水")
public class CurrentsController extends GenericController
{
    public static final Logger        logger = LoggerFactory.getLogger(CurrentsController.class);
    
    @Autowired(required = false)
    private AccountFundCurrentService accountFundCurrentService;
    
    @Autowired(required = false)
    private DictionaryService         dictionaryService;

    @Autowired(required = false)
    private StockInfoService          stockInfoService;

    /**
     * Fund财务流水页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/financialCurrents", method = RequestMethod.GET)
    @ApiOperation(value = "Fund财务流水页面导航", httpMethod = "GET")
    public ModelAndView financialCurrents() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/financialCurrents");
        return mav;
    }

    /**
     * Fund资金流水页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/currents", method = RequestMethod.GET)
    @ApiOperation(value = "Fund资金流水页面导航", httpMethod = "GET")
    public ModelAndView currents() throws BusinessException
    {
        ModelAndView mav = new ModelAndView("fund/currents");
        return mav;
    }

    /**
     * Fund财务流水列表
     * @param accountFundCurrent
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/currents/financialCurrentsList", method = RequestMethod.GET)
    @ApiOperation(value = "Fund资金流水列表", httpMethod = "GET")
    public JsonMessage financialCurrentsList(String isHis,String type,String timeStart, String timeEnd, @ModelAttribute AccountFundCurrent accountFundCurrent, @ModelAttribute Pagination pagin) throws BusinessException
    {
        boolean isHisValue= StringUtils.equalsIgnoreCase(isHis,"yes");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountFundCurrent.setAccountId(principal.getId());
        if (!StringUtils.isBlank(timeStart))
        {
            accountFundCurrent.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            accountFundCurrent.setTimeEnd(timeEnd + " 23:59:59");
        }
        // 正常前端要传入参数为： 账户类型；证券信息ID；业务类别；流水时间戳
        logger.debug("financialCurrentsList accountFundCurrent:" + accountFundCurrent.toString());
        if(StringUtils.equalsIgnoreCase(type,"wealth"))
        {
            accountFundCurrent.setTableName(isHisValue?getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableWealthCurrent()+"His"
            :getStockInfo(FundConsts.WALLET_BTC2USD_TYPE).getTableWealthCurrent());
        }else
        {
            //accountFundCurrent.setTableName(getStockInfo(accountFundCurrent.getStockinfoId()).getTableFundCurrent());
            accountFundCurrent.setTableName(
                    isHisValue?getStockInfo(accountFundCurrent.getStockinfoId()).getTableFundCurrentHis()
                            :getStockInfo(accountFundCurrent.getStockinfoId()).getTableFundCurrent());
        }
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.findListByAccount(pagin, accountFundCurrent,
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

        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    /**
     * Fund资金流水列表
     * @param accountFundCurrent
     * @param pagin
     * @return
     * @throws BusinessException
     * @author sunbiao  2017年7月19日 上午11:08:10
     */
    @ResponseBody
    @RequestMapping(value = "/currents/currentsList", method = RequestMethod.GET)
    @ApiOperation(value = "Fund资金流水列表", httpMethod = "GET")
    public JsonMessage currentsList(String isHis,String timeStart, String timeEnd, @ModelAttribute AccountFundCurrent accountFundCurrent, @ModelAttribute Pagination pagin) throws BusinessException
    {
        boolean isHisValue= StringUtils.equalsIgnoreCase(isHis,"yes");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        accountFundCurrent.setAccountId(principal.getId());
        if (!StringUtils.isBlank(timeStart))
        {
            accountFundCurrent.setTimeStart(timeStart + " 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            accountFundCurrent.setTimeEnd(timeEnd + " 23:59:59");
        }
        // 正常前端要传入参数为： 账户类型；证券信息ID；业务类别；流水时间戳
        logger.debug("currentsList accountFundCurrent:" + accountFundCurrent.toString());
        accountFundCurrent.setTableName(
                isHisValue?getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrentHis()
                :getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.findListByAccount(pagin, accountFundCurrent
//                ,FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_PRE_REQ,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_REQ,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_REGIST_AWARD,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_NOENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_NOENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_NOENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_NOENTRUST_DEAL,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_WITHDRAW,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_WITHDRAW,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_REJECT,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_REJECT,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_WITHDRAW,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_WITHDRAW,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET,
//                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT
        );
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * Fund资金流水列表导出
     * @param accountFundCurrent
     * @param pagin
     * @return
     * @throws BusinessException
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping(value = "/currents/currentsExport")
    @ApiOperation(value = "Fund资金流水列表导出")
    public void currentsExport(String isHis,String timeStart, String timeEnd, @ModelAttribute AccountFundCurrent accountFundCurrent, @ModelAttribute Pagination pagin,
            HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException
    {
        boolean isHisValue= StringUtils.equalsIgnoreCase(isHis,"yes");
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (null == principal) throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
        accountFundCurrent.setAccountId(principal.getId());
        String lang = CookieUtils.get(request, BitmsConst.COOKIE_LOCALE);
        if (StringUtils.isBlank(lang))
        {
            lang = "en_US";
        }
        if (!StringUtils.isBlank(timeStart))
        {
            accountFundCurrent.setTimeStart(timeStart+" 00:00:00");
        }
        if (!StringUtils.isBlank(timeEnd))
        {
            accountFundCurrent.setTimeEnd(timeEnd+" 23:59:59");
        }
        // 正常前端要传入参数为： 账户类型；证券信息ID；业务类别；流水时间戳
        logger.debug("currentsList accountFundCurrent:" + accountFundCurrent.toString());
        accountFundCurrent.setTableName(
                isHisValue?getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrentHis()
                        :getStockInfo(accountFundCurrent.getRelatedStockinfoId()).getTableFundCurrent());
        PaginateResult<AccountFundCurrent> result = accountFundCurrentService.findListByAccount(pagin, accountFundCurrent
                //                ,FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_CANCEL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETWITHDRAW_REJECT,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_PRE_REQ,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_ICOSUBSCRIBE_REQ,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_REGIST_AWARD,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_TRADE_AWARD,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_ADD,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_ASSET_ADJUST_SUB,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_ADD,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PLATFORM_FORZENASSET_ADJUST_SUB,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_NOENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_NOENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_NOENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_NOENTRUST_DEAL,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_WITHDRAW,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_WITHDRAW,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTBUY_ENTRUST_REJECT,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_PUSHTRADE_SPOTSELL_ENTRUST_REJECT,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTBUY_ENTRUST_WITHDRAW,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_FAIRTRADE_SPOTSELL_ENTRUST_WITHDRAW,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_ASSET_UNFROZEN,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_CONTRACT_2_WALLET,
                //                FundConsts.SYSTEM_BUSSINESS_FLAG_WALLET_2_CONTRACT
        );
        List<AccountFundCurrent> list = result.getList();
        Map<String, Object> asset = new HashMap<String, Object>();
        List<Dictionary> dictList = dictionaryService.findByCode(lang, FundConsts.ACCOUNT_ASSET_TYPE);
        for (Dictionary dict : dictList)
        {
            asset.put(dict.getCode(), dict.getName());
        }
        Map<String, Object> flag = new HashMap<String, Object>();
        dictList = dictionaryService.findByCode(lang, FundConsts.SYSTEM_BUSSINESS_FLAG);
        for (Dictionary dict : dictList)
        {
            flag.put(dict.getCode(), dict.getName());
        }
        dictList = dictionaryService.findByCode(lang, FundConsts.ACCOUNT_ASSET_DIRECT);
        for (Dictionary dict : dictList)
        {
            flag.put(dict.getCode(), dict.getName());
        }
        ExportExcel excel = new ExportExcel("账户资金流水", AccountFundCurrent.class);
        for (AccountFundCurrent info : list)
        {
            info.setCurrentDateStr(CalendarUtils.getStringTime(info.getCurrentDate().getTime(), DateConst.DATE_FORMAT_YMDHMS));
            info.setAccountAssetType((asset.get(info.getAccountAssetType()) == null ? "" : asset.get(info.getAccountAssetType()).toString()));
            info.setBusinessFlag((flag.get(info.getBusinessFlag()) == null ? "" : flag.get(info.getBusinessFlag()).toString()));
            info.setOccurDirect((flag.get(info.getOccurDirect()) == null ? "" : flag.get(info.getOccurDirect()).toString()));
        }
        excel.setDataList(list);
        excel.write(response, "账户资金流水.xls");
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
}
