package com.blocain.bitms.trade.trade.controller;

import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;

import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Greated By blocain05 on 2017/11/1
 *
 * @version V1.0
 */
@Controller
@RequestMapping(BitmsConst.ENTRUST)
public class ClosePostionEntrustController extends GenericController
{
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;

    @Autowired(required = false)
    private StockInfoService         stockInfoService;

    /**
     * 列表页面导航-爆仓委托
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/closePostionEntrustList")
    @RequiresPermissions("trade:setting:closePostionEntrustList:index")
    public String list() throws BusinessException{
        return "/trade/trade/entrust/closePostionEntrustList";
    }

    /**
     * 查询 爆仓委托查询
     * @param entrustVCoinMoney
     * @param pagination
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/closePostionEntrustList/data",method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:closePostionEntrustList:data")
    public JsonMessage data(EntrustVCoinMoney entrustVCoinMoney, String tableName,Pagination pagination) throws BusinessException
    {
        String table=getStockInfo(entrustVCoinMoney.getEntrustRelatedStockinfoId()).getTableEntrust();
        if(StringUtils.contains(tableName,"His"))
        {
            table=getStockInfo(entrustVCoinMoney.getEntrustRelatedStockinfoId()).getTableEntrustHis();
        }
        entrustVCoinMoney.setTableName(table);
        entrustVCoinMoney.setEntrustAccountType(true); //系统下单  固定传入
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode()); //撮合交易 固定传入
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.search(pagination,entrustVCoinMoney);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }


    /**
     * 列表页面导航-爆仓委托
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/clsPosEntrustList")
    @RequiresPermissions("trade:setting:clsPosEntrustList:index")
    public String clsPosEntrustList() throws BusinessException{
        return "/trade/trade/entrust/clsPosEntrustList";
    }

    /**
     * 查询账户爆仓单委托表-查询
     * @param entrustVCoinMoney
     * @param pagin
     * @return {@link JsonMessage}
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/matchEntrustList/closeData", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:clsPosEntrustList:data")
    public JsonMessage matchEntrustClosePositionListData(EntrustVCoinMoney entrustVCoinMoney, String timeStart, String timeEnd, Pagination pagin) throws BusinessException
    {
        String table=getStockInfo(entrustVCoinMoney.getEntrustRelatedStockinfoId()).getTableEntrust();
        if(StringUtils.contains(entrustVCoinMoney.getTableName(),"His"))
        {
            table=getStockInfo(entrustVCoinMoney.getEntrustRelatedStockinfoId()).getTableEntrustHis();
        }
        entrustVCoinMoney.setTableName(table);
        if (StringUtils.isNotBlank(timeStart))
        {
            entrustVCoinMoney.setTimeStart(timeStart);
        }
        if (StringUtils.isNotBlank(timeEnd))
        {
            entrustVCoinMoney.setTimeEnd(timeEnd);
        }
        entrustVCoinMoney.setEntrustAccountType(true);
        PaginateResult<EntrustVCoinMoney> result = entrustVCoinMoneyService.search(pagin, entrustVCoinMoney);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }

    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }

}
