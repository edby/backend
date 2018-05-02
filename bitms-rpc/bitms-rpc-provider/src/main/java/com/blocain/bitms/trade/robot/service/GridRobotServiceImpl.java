package com.blocain.bitms.trade.robot.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.quotation.entity.RtQuotationInfo;
import com.blocain.bitms.quotation.service.RtQuotationInfoService;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.consts.CacheConst;
import com.blocain.bitms.tools.utils.NumericUtils;
import com.blocain.bitms.tools.utils.RedisUtils;
import com.blocain.bitms.trade.account.consts.AccountConsts;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.AccountDebitAsset;
import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.fund.service.AccountDebitAssetService;
import com.blocain.bitms.trade.fund.service.FundScanService;
import com.blocain.bitms.trade.risk.service.RiskService;
import com.blocain.bitms.trade.robot.consts.TradeConst;
import com.blocain.bitms.trade.robot.entity.Order;
import com.blocain.bitms.trade.robot.entity.RobotModel;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.entity.StockRate;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.blocain.bitms.trade.stockinfo.service.StockRateService;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import com.blocain.bitms.trade.trade.model.EntrustModel;
import com.blocain.bitms.trade.trade.service.EntrustVCoinMoneyService;
import com.blocain.bitms.trade.trade.service.TradeService;

@Service
public class GridRobotServiceImpl implements GridRobotService
{
    private static final Logger      logger         = LoggerFactory.getLogger(GridRobotServiceImpl.class);
    
    // 机器人挂单
    private static final String      robotOrder     = "robot|entrustxOnDoing|";
    
    // 账户资产缓存
    private static final String      robotAsset     = "robot|asset|";
    
    // 账户信息KEY: platscan_fundCurrent_[acctid]
    private static final String      keyPrefix      = new StringBuffer(CacheConst.REDIS_PLATSCAN_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_FUND_ASSET)
            .append(BitmsConst.SEPARATOR).toString();
    
    private static final String      opQuotationKey = new StringBuffer(CacheConst.REDIS_QUOTATION_PREFIX).append(BitmsConst.SEPARATOR).append(BitmsConst.OP_RTQUOTATIONINFO)
            .append(BitmsConst.SEPARATOR).toString();
    
    @Autowired(required = false)
    private FundScanService          fundScanService;
    
    @Autowired(required = false)
    private TradeService             tradeService;
    
    @Autowired(required = false)
    private AccountService           accountService;
    
    @Autowired(required = false)
    private RtQuotationInfoService   rtQuotationInfoService;
    
    @Autowired(required = false)
    private SysParameterService      sysParameterService;
    
    @Autowired(required = false)
    private EntrustVCoinMoneyService entrustVCoinMoneyService;
    
    @Autowired(required = false)
    private RiskService              riskService;
    
    @Autowired(required = false)
    private StockRateService         stockRateService;
    
    @Autowired(required = false)
    private StockInfoService         stockInfoService;
    
    @Autowired(required = false)
    private AccountDebitAssetService accountDebitAssetService;
    
    /**
     * 单笔撤单
     */
    @Override
    public void cancelOrder(RobotModel robot, Order order)
    {
        Long accountId = robot.getParam().getAccountId();
        Long entrustId = order.getId();
        Long exchangePairMoney = robot.getPair().getCapital();
        doMatchCancel(accountId, entrustId, exchangePairMoney);
    }
    
    /**
     * 全量撤单
     * @param robot
     */
    @Override
    public void cancelAllOrders(RobotModel robot)
    {
        List<Order> orders = findOrders(robot, null);
        // 撤单时csrf不区分买卖单，这里默认获取了买单的csrf
        for (Order order : orders)
        {
            cancelOrder(robot, order);
        }
    }
    
    /**
     * 修改自动借贷默认策略
     * @return {@link JsonMessage}
    
     */
    @Override
    public void changeBorrowSwitch(Long accountId, Integer autoDebit)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_USD_TYPE);
        Account account = accountService.selectByPrimaryKey(accountId);
        account.setAutoDebit(autoDebit);
        AccountDebitAsset entity = new AccountDebitAsset();
        entity.setTableName(stockInfo.getTableDebitAsset());
        entity.setBorrowerAccountId(accountId);
        entity.setRelatedStockinfoId(stockInfo.getCapitalStockinfoId());
        List<AccountDebitAsset> list = accountDebitAssetService.findListForDebit(entity);
        if (list.size() > 0)
        {
            if (autoDebit.intValue() == 0)
            {
                logger.error("存在借款 不能关闭借贷开关");
                return;
            }
        }
        accountService.updateByPrimaryKeySelective(account);
    }
    
    /**
     * 计算下单价
     * @param basePrice
     * @param i
     * @param direct
     * @return
     */
    @Override
    public BigDecimal calOrderPrice(RobotModel robot, BigDecimal basePrice, int i, String direct)
    {
        BigDecimal bdPrice = null;
        if (TradeConst.SPOTBUY.equals(direct))
        {
            if (i == 0)
            {
                bdPrice = basePrice.subtract(robot.getParam().getBeginPriceGrade());
            }
            else
            {
                bdPrice = basePrice.subtract(robot.getParam().getBeginPriceGrade()).subtract(new BigDecimal(String.valueOf(i)).multiply(robot.getParam().getPriceGrade()));
            }
        }
        else if (TradeConst.SPOTSELL.equals(direct))
        {
            if (i == 0)
            {
                bdPrice = basePrice.add(robot.getParam().getBeginPriceGrade());
            }
            else
            {
                bdPrice = basePrice.add(robot.getParam().getBeginPriceGrade()).add(new BigDecimal(String.valueOf(i)).multiply(robot.getParam().getPriceGrade()));
            }
        }
        // 价格后两位随机
        double plus = Math.random();
        BigDecimal res = bdPrice.setScale(0, BigDecimal.ROUND_FLOOR).add(new BigDecimal(String.valueOf(plus))).setScale(2, BigDecimal.ROUND_FLOOR);
        return res;
    }
    
    /**
     * 撮合交易发起委托卖出操作
     * @param entrustAmt
     * @param entrustPrice
     */
    @Override
    public void doSell(RobotModel robot, BigDecimal entrustPrice, BigDecimal entrustAmt)
    {
        Long accountId = robot.getParam().getAccountId();
        String entrustType = TradeConst.LIMIT_PRICE;
        Long exchangePairMoney = robot.getPair().getCapital();
        EntrustModel entrustModel = new EntrustModel();
        String entrustDirect = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_SELL.getCode();// 现货卖出
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.error("交易对错误,卖出失败");
            return;
        }
        if (!checkSwitch())
        {
            logger.info("开关检查失败，卖出失败");
            return;
        }
        if (!checkTradeSwitch(exchangePairMoney))
        {
            logger.info("交易开关检查失败，卖出失败");
            return;
        }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        checkEntrustMaxCnt(accountId, exchangePairMoney);
        // 委托类型判断
        if (StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())
                || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            if (StringUtils.equalsIgnoreCase(entrustType, stockInfo.getOpenEntrustType())
                    || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITANDMARKETPRICE.getCode()))
            {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围正确");
                entrustModel.setEntrustType(entrustType);
            }
            else
            {
                logger.error("委托类型(限价limitPrice、市价marketPrice)开放范围错误，卖出失败");
                return;
            }
        }
        else
        {
            logger.error("委托类型(限价limitPrice、市价marketPrice)范围错误，卖出失败");
            return;
        }
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(BigDecimal.ZERO);
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            if (entrustModel.getEntrustAmt().compareTo(stockInfo.getSellMinAmount()) < 0 || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.error("委托范围错误，卖出失败");
                return;
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getSellAmountPrecision());
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(entrustPrice);
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            entrustModel.setFee(BigDecimal.ZERO);
            // ---------------------------入参判断 start ------------------------------------------
            BigDecimal sellMaxCnt = stockInfo.getMaxSingleSellEntrustAmt();
            logger.debug("单笔卖出上限：" + sellMaxCnt);
            if (entrustModel.getEntrustAmt().compareTo(sellMaxCnt) > 0)
            {
                logger.error("委托范围错误 已经超过系统单笔卖出上限，卖出失败");
                return;
            }
            BigDecimal sellMinAmt = stockInfo.getSellMinAmount();
            logger.debug("单笔卖出下限：" + sellMinAmt);
            if (entrustModel.getEntrustAmt().compareTo(sellMinAmt) < 0)
            {
                logger.error("委托范围错误 已经超过系统单笔卖出下限，卖出失败");
                return;
            }
            if (entrustModel.getEntrustAmt().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellAmountPrecision())))) < 0
                    || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.error("委托数量范围不对,卖出失败");
                return;
            }
            // 委托价格0~999999个
            if (entrustModel.getEntrustPrice().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getSellPricePrecision())))) < 0
                    || entrustModel.getEntrustPrice().compareTo(BigDecimal.valueOf(999999)) >= 0)
            {
                logger.error("委托价格不在范围内，卖出失败");
                return;
            }
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getSellAmountPrecision());
            NumericUtils.checkDecimalDigits("entrustPrice", entrustModel.getEntrustPrice(), stockInfo.getSellPricePrecision());
            Account account = accountService.selectByPrimaryKey(accountId);
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue())
            {
                logger.error("用户异常,卖出失败");
                return;
            }
            // ---------------------------入参判断 end ------------------------------------------
            // 风控
            riskService.entrustRisk(stockInfo, accountId, entrustDirect, entrustPrice, entrustType, exchangePairVCoin, exchangePairMoney);
        }
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(exchangePairMoney);
        stockRate.setRateType(FundConsts.MATCHTRADE_SELL_FEE_RATE);// 卖出费率
        List<StockRate> list = stockRateService.findList(stockRate);
        if (list.size() > 0)
        {
            stockRate = list.get(0);
            entrustModel.setFeeRate(stockRate.getRate());
        }
        else
        {
            logger.error("费率有问题,卖出失败");
            return;
        }
        logger.debug("/matchTrade/doMatchSell page form = " + entrustModel.toString());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(accountId);
        entrustModel.setEntrustDirect(entrustDirect);// 委托卖出BTC
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setStockinfoIdEx(exchangePairMoney);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(accountId, exchangePairVCoin, exchangePairMoney);
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        clearEntrustOnDoingByPriceCache(entrustVCoinMoney, exchangePairMoney);
    }
    
    /**
         * 撮合交易发起委托买入操作
         * @param entrustAmt
         * @param entrustPrice
         */
    @Override
    public void doBuy(RobotModel robot, BigDecimal entrustPrice, BigDecimal entrustAmt)
    {
        Long accountId = robot.getParam().getAccountId();
        String entrustType = TradeConst.LIMIT_PRICE;
        Long exchangePairMoney = robot.getPair().getCapital();
        EntrustModel entrustModel = new EntrustModel();
        String entrustDirect = TradeEnums.ENTRUST_DEAL_DIRECT_SPOT_BUY.getCode();// 现货买入
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.error("交易对错误,买入失败");
            return;
        }
        if (!checkSwitch())
        {
            logger.info("开关检查失败，买入失败");
            return;
        }
        if (!checkTradeSwitch(exchangePairMoney))
        {
            logger.info("交易开关检查失败，买入失败");
            return;
        }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        if (!checkEntrustMaxCnt(accountId, exchangePairMoney))
        {
            logger.error("已超出最大下单量，买入失败");
            return;
        }
        // 委托类型判断
        if (StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode())
                || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            if (StringUtils.equalsIgnoreCase(entrustType, stockInfo.getOpenEntrustType())
                    || StringUtils.equalsIgnoreCase(entrustType, TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITANDMARKETPRICE.getCode()))
            {
                logger.debug("委托类型(限价limitPrice、市价marketPrice)开放范围正确");
                entrustModel.setEntrustType(entrustType);
            }
            else
            {
                logger.error("委托类型(限价limitPrice、市价marketPrice)开放范围错误，买入失败");
                return;
            }
        }
        else
        {
            logger.error("委托类型(限价limitPrice、市价marketPrice)范围错误，买入失败");
            return;
        }
        // 市价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_MARKETPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(BigDecimal.ZERO);
            entrustModel.setEntrustPrice(BigDecimal.ZERO);
            entrustModel.setEntrustAmtEx(entrustAmt);
            // ---------------------------入参判断 start ------------------------------------------
            // 委托价格0~999999个
            if (entrustModel.getEntrustAmtEx().compareTo(stockInfo.getBuyMinAmount()) < 0 || entrustModel.getEntrustAmtEx().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.error("USDX数量范围不对，买入失败");
                return;
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getBuyAmountPrecision());
            Account account = accountService.selectByPrimaryKey(accountId);
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue())
            {
                logger.error("账户状态异常，买入失败");
                return;
            }
            // ---------------------------入参判断 end ------------------------------------------
        }
        // 限价
        if (StringUtils.equalsIgnoreCase(entrustModel.getEntrustType(), TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode()))
        {
            entrustModel.setEntrustAmt(entrustAmt);
            entrustModel.setEntrustPrice(entrustPrice);
            entrustModel.setEntrustAmtEx(entrustModel.getEntrustAmt().multiply(entrustModel.getEntrustPrice()));
            // ---------------------------入参判断 start ------------------------------------------
            BigDecimal buyMaxCnt = stockInfo.getMaxSingleBuyEntrustAmt();
            logger.debug("单笔买入上限：" + buyMaxCnt);
            if (entrustModel.getEntrustAmt().compareTo(buyMaxCnt) > 0)
            {
                logger.error("委托范围错误 已经超过系统参数单笔买入上限，买入失败");
                return;
            }
            BigDecimal buyMinAmt = stockInfo.getBuyMinAmount();
            logger.debug("单笔买入下限：" + buyMinAmt);
            if (entrustModel.getEntrustAmt().compareTo(buyMinAmt) < 0)
            {
                logger.error("委托范围错误 已经超过系统单笔买入下限，买入失败");
                return;
            }
            // BMS委托数量999~999999个
            if (entrustModel.getEntrustAmt().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyAmountPrecision())))) < 0
                    || entrustModel.getEntrustAmt().compareTo(BigDecimal.valueOf(999999)) > 0)
            {
                logger.error("委托数量范围不对，买入失败");
                return;
            }
            // 委托价格0~999999个
            if (entrustModel.getEntrustPrice().compareTo(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, stockInfo.getBuyPricePrecision())))) < 0
                    || entrustModel.getEntrustPrice().compareTo(BigDecimal.valueOf(999999)) >= 0)
            {
                logger.error("委托价格不在范围内，买入失败");
                return;
            }
            // 小数位数判断
            NumericUtils.checkDecimalDigits("entrustAmt", entrustModel.getEntrustAmt(), stockInfo.getBuyAmountPrecision());
            NumericUtils.checkDecimalDigits("entrustPrice", entrustModel.getEntrustPrice(), stockInfo.getBuyPricePrecision());
            Account account = accountService.selectByPrimaryKey(accountId);
            checkAccountDataValidate(account);
            if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue())
            {
                logger.error("账户状态异常，买入失败");
                return;
            }
            // ---------------------------入参判断 end ------------------------------------------
            // 风控
            riskService.entrustRisk(stockInfo, accountId, entrustDirect, entrustPrice, entrustType, exchangePairVCoin, exchangePairMoney);
        }
        entrustModel.setFee(BigDecimal.ZERO);
        StockRate stockRate = new StockRate();
        stockRate.setStockinfoId(exchangePairMoney);
        stockRate.setRateType(FundConsts.MATCHTRADE_BUY_FEE_RATE);// 买入费率
        List<StockRate> list = stockRateService.findList(stockRate);
        if (list.size() > 0)
        {
            stockRate = list.get(0);
            entrustModel.setFeeRate(stockRate.getRate());
            logger.debug("费率" + stockRate.getRate());
        }
        else
        {
            logger.error("费率有问题,买入失败");
            return;
        }
        logger.debug("检查 完毕");
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        entrustModel.setAccountId(accountId);
        entrustModel.setEntrustDirect(entrustDirect);// 现货买入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        entrustModel.setEntrustEndDate(calendar.getTimeInMillis());
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setStockinfoIdEx(exchangePairMoney);
        entrustModel.setFee(BigDecimal.ZERO);
        entrustModel.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        // 委托服务
        tradeService.entrust(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(accountId, exchangePairVCoin, exchangePairMoney);
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        clearEntrustOnDoingByPriceCache(entrustVCoinMoney, exchangePairMoney);
    }
    
    /**
     * 检查最大下单量
     * @param accountId
     * @param exchangePairMoney
     */
    private boolean checkEntrustMaxCnt(Long accountId, Long exchangePairMoney)
    {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_TRADE_ACCOUNT_MAX_ENTRUST_CNT);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params != null)
        {
            Long cnt = Long.parseLong(params.getValue().toString());
            Long done = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyCnt(accountId, exchangePairMoney);
            if (done.compareTo(cnt) >= 0) { return false; }
        }
        params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_TRADE_MONEY_MAX_ENTRUST_CNT);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params != null)
        {
            Long cnt = Long.parseLong(params.getValue().toString());
            Long doing = entrustVCoinMoneyService.getMoneyDoingEntrustVCoinMoneyCnt(exchangePairMoney);
            if (doing.compareTo(cnt) >= 0) { return false; }
        }
        return true;
    }
    
    /**
     * 下单价替换，只替换小数位
     * @param oldPrice
     * @return
     */
    @Override
    public BigDecimal replacePrice(RobotModel robot, BigDecimal oldPrice)
    {
        double plus = Math.random();
        BigDecimal res = oldPrice.setScale(0, BigDecimal.ROUND_FLOOR).add(new BigDecimal(String.valueOf(plus))).setScale(robot.getPair().getPriceScale(),
                BigDecimal.ROUND_FLOOR);
        return res;
    }
    
    /**
     * 计算下单量
     * @param robot
     * @return
     */
    @Override
    public BigDecimal calOrderAmt(RobotModel robot)
    {
        double max = robot.getParam().getMaxAmt().doubleValue();
        double min = robot.getParam().getMinAmt().doubleValue();
        double dAmt = Math.random() * (max - min) + min;
        BigDecimal bdAmt = new BigDecimal(String.valueOf(dAmt)).setScale(robot.getPair().getAmtScale(), BigDecimal.ROUND_HALF_UP);
        return bdAmt;
    }
    
    /**
     * 计算基准价
     * @return
     */
    @Override
    public BigDecimal calBasePrice(RobotModel robot)
    {
        RtQuotationInfo rt = ticker(robot.getPair().getName());
        // 首先获取买一价，卖一价，风控基价
        BigDecimal basePrice = BigDecimal.ZERO;
        BigDecimal buyOne = rt.getEntrustBuyOne();
        BigDecimal sellOne = rt.getEntrustSellOne();
        // 盘口均价
        basePrice = buyOne.add(sellOne).divide(new BigDecimal("2"), 2, BigDecimal.ROUND_HALF_UP);
        return basePrice;
    }
    
    /**
     * 获取最新行情
     */
    public RtQuotationInfo ticker(String symbol)
    {
        String quotationKey = new StringBuffer(opQuotationKey).append(TradeEnums.SYMBOL_BTC2USD.getMessage()).toString();
        RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
        if (null == rtQuotationInfo || rtQuotationInfo.getPlatPrice().compareTo(BigDecimal.ZERO) == 0)
        {
            logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: 缓存中不存在行情，尝试从数据库获取行情");
            rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(Long.valueOf(TradeEnums.SYMBOL_BTC2USD.getCode()),
                    Long.valueOf(TradeEnums.SYMBOL_BTC2USD.getMessage()));
        }
        return rtQuotationInfo;
    }
    
    /**
     * 更新下单开关
     */
    @Override
    public RobotModel updateTradeSwitch(RobotModel robot)
    {
        Long accountId = robot.getParam().getAccountId();
        Long capital = robot.getPair().getCapital();
        FundChangeModel model = getAccountFundAsset(accountId, capital);
        robot.setCurAssetInfo(model);
        BigDecimal openInterest = model.getUsdxAmount().subtract(model.getUsdxBorrow());
        // 买单开关的更新
        if (robot.getBuySwitch())
        {
            robot.setBuySwitch(openInterest.compareTo(robot.getParam().getMinOpenInterest()) == -1 ? false : true);
        }
        else
        {
            robot.setBuySwitch(openInterest.compareTo(robot.getParam().getReBuyOpenInterest()) == 1 ? true : false);
        }
        // 卖单开关的更新
        if (robot.getSellSwitch())
        {
            robot.setSellSwitch(openInterest.compareTo(robot.getParam().getMaxOpenInterest()) == 1 ? false : true);
        }
        else
        {
            robot.setSellSwitch(openInterest.compareTo(robot.getParam().getReSellOpenInterest()) == -1 ? true : false);
        }
        return robot;
    }
    
    /**
     * 获取账户资金资产等信息
     */
    @Override
    public FundChangeModel getAccountFundAsset(Long accountId, Long exchangePairMoney)
    {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        if (stockInfoList.size() == 0)
        {
            logger.error("证券信息有误，获取账户资产失败");
            return null;
        }
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        FundChangeModel accountFundAsset = getAccountAsset(accountId, exchangePairVCoin, exchangePairMoney);
        return accountFundAsset;
    }
    
    /**
     * 获取账户资产信息
     * @param accountId
     * @param exchangePairVCoin
     * @param exchangePairMoney
     */

    public FundChangeModel getAccountAsset(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        // 1.获取缓存基本数据
        FundChangeModel fundChangeModel = getAccountAssetCache(accountId, exchangePairVCoin, exchangePairMoney);
        // 2.参与账户资产指标计算
        fundChangeModel = calculateFundChangeModel(fundChangeModel, exchangePairVCoin, exchangePairMoney);
        return fundChangeModel;
    }
    
    /**
     * 获取缓存实时行情
     * @param exchangePairVCoin
     * @param exchangePairMoney
     * @return
     */
    public RtQuotationInfo getRtQuotationInfoCache(Long exchangePairVCoin, Long exchangePairMoney)
    {
        String quotationKey = new StringBuffer(opQuotationKey).append(exchangePairMoney).toString();
        RtQuotationInfo rtQuotationInfo = (RtQuotationInfo) RedisUtils.getObject(quotationKey);
        if (null == rtQuotationInfo || rtQuotationInfo.getPlatPrice().compareTo(BigDecimal.ZERO) == 0)
        {
            logger.debug("★☆★☆★☆★☆★☆ [queryRtQuotationInfoFromCache]: 缓存中不存在行情，尝试从数据库获取行情");
            rtQuotationInfo = rtQuotationInfoService.queryRtQuotationInfoFromCache(exchangePairVCoin, exchangePairMoney);
        }
        return rtQuotationInfo;
    }
    
    /**
     * 计算资产信息
     * @param fundChangeModel
     * @param exchangePairVCoin
     * @param exchangePairMoney
     * @return
     */
    private FundChangeModel calculateFundChangeModel(FundChangeModel fundChangeModel, Long exchangePairVCoin, Long exchangePairMoney)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(exchangePairMoney);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        if (com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CONTRACTSPOT))
        {
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = getRtQuotationInfoCache(exchangePairVCoin, exchangePairMoney);
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("当前内部行情：" + platPrice);
            if (platPrice == null)
            {
                platPrice = BigDecimal.ONE;
            }
            if (platPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                platPrice = BigDecimal.ONE;
            }
            if (isVCoin)
            {
                BigDecimal vcoinNet = fundChangeModel.getBtcAmount();
                BigDecimal moneyNet = fundChangeModel.getUsdxAmount();
                BigDecimal vcoinDebit = fundChangeModel.getBtcBorrow();
                BigDecimal moneyDebit = fundChangeModel.getUsdxBorrow();
                // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
                BigDecimal accountVcoinNetAmt = BigDecimal.ZERO;
                accountVcoinNetAmt = vcoinNet.subtract(vcoinDebit).add((moneyNet.subtract(moneyDebit)).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP));
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountVcoinNetAmt);
                // 最终实时可借等值的VCOIN数量
                BigDecimal btcCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getBtcLever()).subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP))
                        .subtract(vcoinDebit);
                BigDecimal usdxCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getUsdxLever())
                        .subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)).subtract(vcoinDebit);
                // 账户USDX余额 其实就是账户USDX可用
                BigDecimal usdxAmtNet = fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxBorrow()); // usdxEnable.add(usdxFrozen).subtract(usdxBorrow);
                logger.debug("usdxAmtNet=" + usdxAmtNet);
                fundChangeModel.setUsdxAmtBalance((usdxAmtNet.subtract(fundChangeModel.getUsdxFrozen())));
                // 账户BTC余额 其实就是账户BTC可用
                BigDecimal btcAmtNet = fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcBorrow()); // btcEnable.add(btcFrozen).subtract(btcBorrow);
                logger.debug("btcAmtNet=" + btcAmtNet);
                fundChangeModel.setBtcAmtBalance(btcAmtNet.subtract(fundChangeModel.getBtcFrozen()));
                // 账户BTC净值
                BigDecimal btcNetValue = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcNetValue = (usdxAmtNet).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).add(btcAmtNet);
                }
                logger.debug("btcNetValue=" + btcNetValue);
                fundChangeModel.setBtcNetValue(btcNetValue);
                // 强平价格 = 账户USDX余额 ÷ 账户BTC余额
                BigDecimal explosionPrice = BigDecimal.ZERO;
                if (btcAmtNet.compareTo(BigDecimal.ZERO) != 0)
                {
                    explosionPrice = usdxAmtNet.divide(btcAmtNet, 8, BigDecimal.ROUND_HALF_UP);
                    explosionPrice = explosionPrice.multiply(new BigDecimal(-1));
                    if (explosionPrice.compareTo(BigDecimal.ZERO) < 0)
                    {
                        explosionPrice = BigDecimal.ZERO;
                    }
                }
                logger.debug("explosionPrice=" + explosionPrice);
                fundChangeModel.setExplosionPrice(explosionPrice);
                // 方向 多 空 无
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    riskRate = calculateRiskRate("Long", btcAmtNet, btcNetValue, stockInfo.getClosePositionLongPrePercent());
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    if (explosionPrice.compareTo(BigDecimal.ZERO) != 0)
                    {
                        riskRate = calculateRiskRate("Short", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                    }
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                // 界面显示多空方向
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    fundChangeModel.setDirection("Long");
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    fundChangeModel.setDirection("Short");
                }
                else
                {
                    fundChangeModel.setDirection("None");
                }
                // usdx仓位数量
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    fundChangeModel.setUsdxPosition(usdxAmtNet.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).abs());
                }
                else
                {
                    fundChangeModel.setUsdxPosition(BigDecimal.ZERO);
                }
                // usdx仓位价值 其实就是 usdx净值的绝对值
                fundChangeModel.setUsdxPositionValue(usdxAmtNet.abs());
                // 最大可买 = USDX可用/平台行情 + 可借款额度
                if (usdxCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    usdxCanBorrowAmt = BigDecimal.ZERO;
                }
                BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)
                            .add(usdxCanBorrowAmt);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
                // 最大可卖 = BTC可用+BTC可借额度
                BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
                if (btcCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    btcCanBorrowAmt = BigDecimal.ZERO;
                }
                btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen())).add(btcCanBorrowAmt);
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
                fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
                // 账户盈亏=当前账户净值BTC数量-期初总流入BTC数量+期间总流出-期初初始数量
                BigDecimal profitAndLoss = fundChangeModel.getBtcNetValue().subtract(fundChangeModel.getBtcBeginning()).subtract(fundChangeModel.getBtcSumIn())
                        .add(fundChangeModel.getBtcSumOut());
                fundChangeModel.setProfitAndLoss(profitAndLoss);
                logger.debug("cal fundChangeModel=" + fundChangeModel.toString());
            }
            else
            { // 法定货币标的
                BigDecimal vcoinNet = fundChangeModel.getBtcAmount();
                BigDecimal moneyNet = fundChangeModel.getUsdxAmount();
                BigDecimal vcoinDebit = fundChangeModel.getBtcBorrow();
                BigDecimal moneyDebit = fundChangeModel.getUsdxBorrow();
                // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
                BigDecimal accountMoneyNetAmt = BigDecimal.ZERO;
                accountMoneyNetAmt = moneyNet.subtract(moneyDebit).add((vcoinNet.subtract(vcoinDebit)).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP));
                logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountMoneyNetAmt);
                // 最终实时可借等值的VCOIN数量
                BigDecimal btcCanBorrowAmt = accountMoneyNetAmt.multiply(fundChangeModel.getBtcLever()).subtract(vcoinDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP))
                        .subtract(moneyDebit);
                BigDecimal usdxCanBorrowAmt = accountMoneyNetAmt.multiply(fundChangeModel.getUsdxLever())
                        .subtract(vcoinDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)).subtract(moneyDebit);
                // 账户USDX余额 其实就是账户USDX可用
                BigDecimal usdxAmtNet = fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxBorrow()); // usdxEnable.add(usdxFrozen).subtract(usdxBorrow);
                logger.debug("usdxAmtNet=" + usdxAmtNet);
                fundChangeModel.setUsdxAmtBalance((usdxAmtNet.subtract(fundChangeModel.getUsdxFrozen())));
                // 账户BTC余额 其实就是账户BTC可用
                BigDecimal btcAmtNet = fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcBorrow()); // btcEnable.add(btcFrozen).subtract(btcBorrow);
                logger.debug("btcAmtNet=" + btcAmtNet);
                fundChangeModel.setBtcAmtBalance(btcAmtNet.subtract(fundChangeModel.getBtcFrozen()));
                // 账户BTC净值
                BigDecimal btcNetValue = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcNetValue = (usdxAmtNet).multiply(platPrice).add(btcAmtNet);
                }
                logger.debug("btcNetValue=" + btcNetValue);
                fundChangeModel.setBtcNetValue(btcNetValue);
                // 强平价格 = 账户USDX余额 ÷ 账户BTC余额
                BigDecimal explosionPrice = BigDecimal.ZERO;
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) != 0)
                {
                    explosionPrice = btcAmtNet.divide(usdxAmtNet, 8, BigDecimal.ROUND_HALF_UP);
                    explosionPrice = explosionPrice.multiply(new BigDecimal(-1));
                    if (explosionPrice.compareTo(BigDecimal.ZERO) < 0)
                    {
                        explosionPrice = BigDecimal.ZERO;
                    }
                }
                logger.debug("explosionPrice=" + explosionPrice);
                fundChangeModel.setExplosionPrice(explosionPrice);
                // 方向 多 空 无
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    riskRate = calculateRiskRate("Long", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    if (explosionPrice.compareTo(BigDecimal.ZERO) != 0)
                    {
                        riskRate = calculateRiskRate("Short", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                    }
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                else
                {
                    BigDecimal riskRate = BigDecimal.ZERO;
                    logger.debug("riskRate=" + riskRate);
                    fundChangeModel.setRiskRate(riskRate);
                }
                // 界面显示多空方向
                if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
                {
                    fundChangeModel.setDirection("Long");
                }
                else if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
                {
                    fundChangeModel.setDirection("Short");
                }
                else
                {
                    fundChangeModel.setDirection("None");
                }
                // usdx仓位数量
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    fundChangeModel.setUsdxPosition(usdxAmtNet.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).abs());
                }
                else
                {
                    fundChangeModel.setUsdxPosition(BigDecimal.ZERO);
                }
                // usdx仓位价值 其实就是 usdx净值的绝对值
                fundChangeModel.setUsdxPositionValue(usdxAmtNet.abs());
                // 最大可买 = USDX可用/平台行情 + 可借款额度
                if (usdxCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    usdxCanBorrowAmt = BigDecimal.ZERO;
                }
                BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)
                            .add(usdxCanBorrowAmt);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
                // 最大可卖 = BTC可用+BTC可借额度
                BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
                if (btcCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
                {
                    btcCanBorrowAmt = BigDecimal.ZERO;
                }
                btcSellMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).add(btcCanBorrowAmt);
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
                fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
                // 账户盈亏=当前账户净值BTC数量-期初总流入BTC数量+期间总流出-期初初始数量
                BigDecimal profitAndLoss = fundChangeModel.getBtcNetValue().subtract(fundChangeModel.getBtcBeginning()).subtract(fundChangeModel.getBtcSumIn())
                        .add(fundChangeModel.getBtcSumOut());
                fundChangeModel.setProfitAndLoss(profitAndLoss);
                logger.debug("cal fundChangeModel=" + fundChangeModel.toString());
            }
        }
        else if (com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_LEVERAGEDSPOT))
        {
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = getRtQuotationInfoCache(stockInfo.getTradeStockinfoId(), exchangePairMoney);
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("当前内部行情：" + platPrice);
            if (platPrice == null)
            {
                platPrice = BigDecimal.ONE;
            }
            if (platPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                platPrice = BigDecimal.ONE;
            }
            BigDecimal vcoinNet = fundChangeModel.getBtcAmount();
            BigDecimal moneyNet = fundChangeModel.getUsdxAmount();
            BigDecimal vcoinDebit = fundChangeModel.getBtcBorrow();
            BigDecimal moneyDebit = fundChangeModel.getUsdxBorrow();
            // 对应账户净资产换算成对应数字货币数量(账户净资产=账户法定货币净资产+ 账户数字货币净资产) - 对应账户法定货币借款金额折算对应数字货币数量 - 对应账户数字货币借款数量
            BigDecimal accountVcoinNetAmt = BigDecimal.ZERO;
            accountVcoinNetAmt = vcoinNet.subtract(vcoinDebit).add((moneyNet.subtract(moneyDebit)).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP));
            logger.debug("findEnableAmtFormAccountContractAssetDBSubAccountDebit accountNetAmt账户净资产:" + accountVcoinNetAmt);
            // 最终实时可借等值的VCOIN数量
            BigDecimal btcCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getBtcLever()).subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP))
                    .subtract(vcoinDebit);
            BigDecimal usdxCanBorrowAmt = accountVcoinNetAmt.multiply(fundChangeModel.getUsdxLever()).subtract(moneyDebit.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP))
                    .subtract(vcoinDebit);
            // 账户USDX余额 其实就是账户USDX可用
            BigDecimal usdxAmtNet = fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxBorrow()); // usdxEnable.add(usdxFrozen).subtract(usdxBorrow);
            logger.debug("usdxAmtNet=" + usdxAmtNet);
            fundChangeModel.setUsdxAmtBalance((usdxAmtNet.subtract(fundChangeModel.getUsdxFrozen())));
            // 账户BTC余额 其实就是账户BTC可用
            BigDecimal btcAmtNet = fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcBorrow()); // btcEnable.add(btcFrozen).subtract(btcBorrow);
            logger.debug("btcAmtNet=" + btcAmtNet);
            fundChangeModel.setBtcAmtBalance(btcAmtNet.subtract(fundChangeModel.getBtcFrozen()));
            // 账户BTC净值
            BigDecimal btcNetValue = BigDecimal.ZERO;
            if (platPrice.compareTo(BigDecimal.ZERO) != 0)
            {
                btcNetValue = (usdxAmtNet).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).add(btcAmtNet);
            }
            logger.debug("btcNetValue=" + btcNetValue);
            fundChangeModel.setBtcNetValue(btcNetValue);
            // 强平价格 = 账户USDX余额 ÷ 账户BTC余额
            BigDecimal explosionPrice = BigDecimal.ZERO;
            if (btcAmtNet.compareTo(BigDecimal.ZERO) != 0)
            {
                explosionPrice = usdxAmtNet.divide(btcAmtNet, 8, BigDecimal.ROUND_HALF_UP);
                explosionPrice = explosionPrice.multiply(new BigDecimal(-1));
                if (explosionPrice.compareTo(BigDecimal.ZERO) < 0)
                {
                    explosionPrice = BigDecimal.ZERO;
                }
            }
            logger.debug("explosionPrice=" + explosionPrice);
            // 方向 多 空
            if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
            {
                fundChangeModel.setExplosionPrice(explosionPrice.multiply(BigDecimal.ONE.add(stockInfo.getClosePositionLongPrePercent())));
                logger.debug("explosionPrice多头=" + explosionPrice);
            }
            else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                fundChangeModel.setExplosionPrice(explosionPrice.multiply(BigDecimal.ONE.subtract(stockInfo.getClosePositionShortPrePercent())));
                logger.debug("explosionPrice空头=" + explosionPrice);
            }
            else
            {
                fundChangeModel.setExplosionPrice(explosionPrice);
                logger.debug("explosionPrice3=" + explosionPrice);
            }
            logger.debug("explosionPrice=" + explosionPrice);
            // 方向 多 空 无
            if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
            {
                BigDecimal riskRate = BigDecimal.ZERO;
                riskRate = calculateRiskRate("Long", btcAmtNet, btcNetValue, stockInfo.getClosePositionLongPrePercent());
                logger.debug("riskRate=" + riskRate);
                fundChangeModel.setRiskRate(riskRate);
            }
            else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                BigDecimal riskRate = BigDecimal.ZERO;
                if (explosionPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    riskRate = calculateRiskRate("Short", btcAmtNet, btcNetValue, stockInfo.getClosePositionShortPrePercent());
                }
                logger.debug("riskRate=" + riskRate);
                fundChangeModel.setRiskRate(riskRate);
            }
            else
            {
                BigDecimal riskRate = BigDecimal.ZERO;
                logger.debug("riskRate=" + riskRate);
                fundChangeModel.setRiskRate(riskRate);
            }
            // 界面显示多空方向
            if (usdxAmtNet.compareTo(BigDecimal.ZERO) < 0)
            {
                fundChangeModel.setDirection("Long");
            }
            else if (usdxAmtNet.compareTo(BigDecimal.ZERO) > 0)
            {
                fundChangeModel.setDirection("Short");
            }
            else
            {
                fundChangeModel.setDirection("None");
            }
            // usdx仓位数量
            if (platPrice.compareTo(BigDecimal.ZERO) != 0)
            {
                fundChangeModel.setUsdxPosition(usdxAmtNet.divide(platPrice, 8, BigDecimal.ROUND_HALF_UP).abs());
            }
            else
            {
                fundChangeModel.setUsdxPosition(BigDecimal.ZERO);
            }
            // usdx仓位价值 其实就是 usdx净值的绝对值
            fundChangeModel.setUsdxPositionValue(usdxAmtNet.abs());
            // 最大可买 = USDX可用/平台行情 + 可借款额度
            if (usdxCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
            {
                usdxCanBorrowAmt = BigDecimal.ZERO;
            }
            BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
            if (platPrice.compareTo(BigDecimal.ZERO) != 0)
            {
                btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP)
                        .add(usdxCanBorrowAmt);
            }
            logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
            // 最大可卖 = BTC可用+BTC可借额度
            BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
            if (btcCanBorrowAmt.compareTo(BigDecimal.ZERO) < 0)
            {
                btcCanBorrowAmt = BigDecimal.ZERO;
            }
            btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen())).add(btcCanBorrowAmt);
            logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
            fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
            fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
            // 账户盈亏=当前账户净值BTC数量-期初总流入BTC数量+期间总流出-期初初始数量
            BigDecimal profitAndLoss = fundChangeModel.getBtcNetValue().subtract(fundChangeModel.getBtcBeginning()).subtract(fundChangeModel.getBtcSumIn())
                    .add(fundChangeModel.getBtcSumOut());
            fundChangeModel.setProfitAndLoss(profitAndLoss);
            // 账户不存在 或 设置不借时候的基本参数
            Account account = accountService.selectByPrimaryKey(fundChangeModel.getAccountId());
            if (account == null || account.getAutoDebit() == null || account.getAutoDebit().intValue() == 0)
            {
                btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen()));
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
                // fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
            }
            logger.debug("stcokinfo === " + stockInfo.toString());
            // 多头杠杆关闭
            if (!com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(stockInfo.getMaxLongLeverSwitch(), FundConsts.PUBLIC_STATUS_YES))
            {
                logger.debug("多头杠杆关闭");
                if (platPrice.compareTo(BigDecimal.ZERO) != 0)
                {
                    btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_HALF_UP);
                }
                logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
                fundChangeModel.setBtcBuyMaxCntBalance(fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen()));
            }
            // 空头杠杆关闭
            if (!com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(stockInfo.getMaxShortLeverSwitch(), FundConsts.PUBLIC_STATUS_YES))
            {
                logger.debug("空头杠杆关闭");
                btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen()));
                logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
                fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
                fundChangeModel.setBtcMaxBorrow(btcSellMaxCnt);
            }
            logger.debug("cal fundChangeModel=" + fundChangeModel.toString());
        }
        else if (com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_PURESPOT))
        {
            BigDecimal platPrice = BigDecimal.ZERO;
            RtQuotationInfo rtQuotationInfo = getRtQuotationInfoCache(stockInfo.getTradeStockinfoId(), exchangePairMoney);
            if (null != rtQuotationInfo)
            {
                platPrice = rtQuotationInfo.getPlatPrice();
            }
            logger.debug("当前内部行情：" + platPrice);
            if (platPrice == null)
            {
                platPrice = BigDecimal.ONE;
            }
            if (platPrice.compareTo(BigDecimal.ZERO) == 0)
            {
                platPrice = BigDecimal.ONE;
            }
            BigDecimal btcBuyMaxCnt = BigDecimal.ZERO;
            btcBuyMaxCnt = (fundChangeModel.getUsdxAmount().subtract(fundChangeModel.getUsdxFrozen())).divide(platPrice, 8, BigDecimal.ROUND_DOWN);
            logger.debug("btcBuyMaxCnt=" + btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCnt(btcBuyMaxCnt);
            fundChangeModel.setBtcMaxBorrow(btcBuyMaxCnt);
            fundChangeModel.setBtcBuyMaxCntBalance(btcBuyMaxCnt.multiply(platPrice));
            // 最大可卖 = BTC可用
            BigDecimal btcSellMaxCnt = BigDecimal.ZERO;
            btcSellMaxCnt = (fundChangeModel.getBtcAmount().subtract(fundChangeModel.getBtcFrozen()));
            logger.debug("btcSellMaxCnt=" + btcSellMaxCnt);
            fundChangeModel.setBtcSellMaxCnt(btcSellMaxCnt);
        }
        else
        {
            logger.error("证券信息类型错误，计算资产信息失败");
            return null;
        }
        fundChangeModel.setAccountId(null);
        return fundChangeModel;
    }
    
    /**
     * 获取缓存中挂单列表，按委托价降序排列
     * @param entity
     * @param exchangePairMoney
     * @return
     */
    public List<EntrustVCoinMoney> entrustxOnDoingCacheByPrice(EntrustVCoinMoney entity, Long exchangePairMoney)
    {
        String key = new StringBuilder(robotOrder).append(entity.getAccountId()).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_entrust_[acctid]
        logger.debug("entrustxOnDoingCache key=" + key);
        // 缓存读取
        logger.debug("开始缓存中获取");
        Object entrustList = null;
        try
        {
            entrustList = RedisUtils.getObject(key);
        }
        catch (Exception e)
        {
            logger.debug("获取缓存异常！" + e.getStackTrace());
        }
        logger.debug("结束缓存中获取");
        if (null != entrustList)
        {
            logger.debug("缓存中读取entrustxOnDoingCache");
            return (List<EntrustVCoinMoney>) entrustList;
        }
        else
        {
            return setEntrustOnDoingCacheByPrice(entity, exchangePairMoney);
        }
    }
    
    /**
     * 获取数据库中委托列表，按委托价降序排列
     * @param entity
     * @param exchangePairMoney
     * @return
     */
    public List<EntrustVCoinMoney> setEntrustOnDoingCacheByPrice(EntrustVCoinMoney entity, Long exchangePairMoney)
    {
        String key = new StringBuilder(robotOrder).append(entity.getAccountId()).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_entrust_[acctid]
        Object entrustList = null;
        logger.debug("操作完毕 刷新未成交缓存 数据库中读取entrustxOnDoingCache");
        logger.debug("操作完毕 刷新未成交缓存 entrustxOnDoingCache key=" + key);
        entrustList = entrustVCoinMoneyService.getAccountDoingEntrustVCoinMoneyListByPrice(entity);
        RedisUtils.putObject(key, entrustList, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
        return (List<EntrustVCoinMoney>) entrustList; // 设置完缓存 返回
    }
    
    /**
     * 撮合交易C2CTrade-进行中的委托列表
    
     */
    public List<EntrustVCoinMoney> entrustxOnDoing(Long exchangePairVCoin, Long exchangePairMoney, Long accountId)
    {
        EntrustVCoinMoney entity = new EntrustVCoinMoney();
        entity.setAccountId(accountId);// 个人数据
        // entity.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        // entity.setEntrustStockinfoId(exchangePairVCoin);
        entity.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entity.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        List<EntrustVCoinMoney> list = entrustxOnDoingCacheByPrice(entity, exchangePairMoney);
        return list;
    }
    
    /**
     * 获取挂单列表
     */
    @Override
    public List<Order> findOrders(RobotModel robot, String direct)
    {
        Long accountId = robot.getParam().getAccountId();
        Long target = robot.getPair().getTarget();
        Long capital = robot.getPair().getCapital();
        List<EntrustVCoinMoney> vcoinList = entrustxOnDoing(target, capital, accountId);
        List<Order> list = new ArrayList<>();
        for (EntrustVCoinMoney vcoin : vcoinList)
        {
            Order o = new Order();
            if (null == direct)
            {
                o.setId(vcoin.getId());
                o.setOrderType(vcoin.getEntrustDirect());
                o.setPrice(vcoin.getEntrustPrice());
                list.add(o);
            }
            else if (direct.equals(vcoin.getEntrustDirect()))
            {
                o.setId(vcoin.getId());
                o.setOrderType(direct);
                o.setPrice(vcoin.getEntrustPrice());
                list.add(o);
            }
        }
        return list;
    }
    
    /**
     * 内部撤单接口
     * @param accountId
     * @param entrustId
     * @param exchangePairMoney
     */
    public void doMatchCancel(Long accountId, Long entrustId, Long exchangePairMoney)
    {
        EntrustModel entrustModel = new EntrustModel();
        entrustModel.setEntrustId(entrustId);
        StockInfo stockInfo = new StockInfo();
        stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
        stockInfo.setId(exchangePairMoney);
        List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
        stockInfo = stockInfoList.get(0);
        boolean isVCoin = (stockInfo.getTradeStockinfoId().longValue() != stockInfo.getId());
        Long exchangePairVCoin = (isVCoin ? stockInfo.getTradeStockinfoId() : stockInfo.getCapitalStockinfoId());
        if (!checkSwitch())
        {
            logger.info("开关检查失败，撤单失败");
            return;
        }
        ;// 检查开关
        if (!checkTradeSwitch(exchangePairMoney))
        {
            logger.info("交易开关检查失败，撤单失败");
            return;
        }// 检查币种交易开关
        EntrustVCoinMoney entrustDB = new EntrustVCoinMoney();
        entrustDB = entrustVCoinMoneyService.selectByPrimaryKey(getStockInfo(exchangePairMoney).getTableEntrust(), entrustModel.getEntrustId());
        logger.debug("操作者=" + accountId + " 拥有者=" + entrustDB.getAccountId());
        if (accountId.longValue() != FundConsts.SYSTEM_ACCOUNT_ID.longValue()) // 超级用户可以为用户撤单
        {
            if (accountId.longValue() != entrustDB.getAccountId().longValue())
            {
                logger.error("委托交易 越权,撤单失败");
                return;
            }
        }
        Account account = accountService.selectByPrimaryKey(accountId);
        if (!checkAccountDataValidate(account))
        {
            logger.error("账号校验失败，撤单失败");
            return;
        }
        entrustModel.setStockinfoId(exchangePairVCoin);
        entrustModel.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        entrustModel.setStockinfoIdEx(exchangePairMoney);
        entrustModel.setAccountId(account.getId());
        entrustModel.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());// 撮合交易
        tradeService.entrustWithdrawX(entrustModel);
        // 委托成功 刷新未成交列表缓存
        setAccountAssetCache(accountId, exchangePairVCoin, exchangePairMoney);
        // clearAccountAssetCache( principal.getId(), exchangePairVCoin, exchangePairMoney);
        EntrustVCoinMoney entrustVCoinMoney = new EntrustVCoinMoney();
        entrustVCoinMoney.setAccountId(accountId);// 个人数据
        entrustVCoinMoney.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        entrustVCoinMoney.setEntrustStockinfoId(exchangePairVCoin);
        entrustVCoinMoney.setTradeType(TradeEnums.TRADE_TYPE_MATCHTRADE.getCode());
        entrustVCoinMoney.setTableName(getStockInfo(exchangePairMoney).getTableEntrust());
        clearEntrustOnDoingByPriceCache(entrustVCoinMoney, exchangePairMoney);
    }
    
    /**
     * 设置挂单缓存
     * @param accountId
     * @param exchangePairVCoin
     * @param exchangePairMoney
     */
    public void setAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        logger.debug("数据库中读取accountFundAsset");
        logger.debug("★★fundChangeScan key=" + key);
        FundChangeModel fundChangeModel = fundScanService.setAccountAssetAttr(accountId, exchangePairVCoin, exchangePairMoney);
        logger.debug("★★fundChangeScan fundChangeModel=" + fundChangeModel.toString());
        RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
    }
    
    /**
     * 检查开关
     */
    private boolean checkSwitch()
    {
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        params = sysParameterService.getSysParameterByEntity(params);
        if (params == null)
        {
            logger.error("==开关值空==");
            return false;
        }
        if (!StringUtils.isBlank(params.getValue()))
        {
            if (!params.getValue().equals("yes"))
            {
                logger.debug("===========开关已关闭==========");
                return false;
            }
            else
            {
                logger.debug("===========开关已打开==========");
            }
        }
        else
        {
            logger.debug("===========开关值不存在==========");
            return false;
        }
        return true;
    }
    
    /**
     * 检查交易开关
     * @param stockinfoId
     */
    private boolean checkTradeSwitch(Long stockinfoId)
    {
        StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockinfoId);
        if (null == stockInfo) { return false; }
        if (!StringUtils.isBlank(stockInfo.getIsExchange()))
        {
            if (!stockInfo.getCanTrade().equals("yes"))
            {
                logger.debug("===========币种交易开关已关闭==========");
                return false;
            }
            else
            {
                logger.debug("===========币种交易开关已打开==========");
            }
        }
        else
        {
            logger.debug("===========币种交易开关值不存在==========");
            return false;
        }
        return true;
    }
    
    /**
     * 根据主键获取stockinfo
     * @param id
     * @return
     */
    public StockInfo getStockInfo(Long id)
    {
        return stockInfoService.selectByPrimaryKey(id);
    }
    
    /**
     * 校验账户信息
     * @param account
     */
    private boolean checkAccountDataValidate(Account account)
    {
        if (null == account)
        {
            logger.error("账户不存在");
            return false;
        }
        if (account.getStatus().intValue() != AccountConsts.ACCOUNT_STATUS_NORMAL.intValue())
        {
            logger.error("账户状态异常");
            return false;
        }
        if (null != account && !account.verifySignature())
        {// 校验数据
            logger.error("账户信息 数据校验失败");
            return false;
        }
        return true;
    }
    
    /**
     * 清除按委托价降序排列的委托单缓存
     * @param entity
     * @param exchangePairMoney
     */
    public void clearEntrustOnDoingByPriceCache(EntrustVCoinMoney entity, Long exchangePairMoney)
    {
        String key = new StringBuilder(robotOrder).append(entity.getAccountId()).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_entrust_[acctid]
        RedisUtils.del(key);
    }
    
    /**
     * 获取账户资产缓存
     * @param accountId
     * @param exchangePairVCoin
     * @param exchangePairMoney
     */
    public FundChangeModel getAccountAssetCache(Long accountId, Long exchangePairVCoin, Long exchangePairMoney)
    {
        String key = new StringBuilder(keyPrefix).append(accountId).append(BitmsConst.SEPARATOR).append(exchangePairMoney).toString(); // changescan_fundCurrent_[acctid]
        logger.debug("getAccountFundAsset key=" + key);
        // 缓存读取
        logger.debug("开始缓存中获取");
        Object accountFundAsset = null;
        try
        {
            accountFundAsset = RedisUtils.getObject(key);
        }
        catch (Exception e)
        {
            logger.debug("获取缓存异常！" + e.getStackTrace());
        }
        logger.debug("结束缓存中获取");
        if (null != accountFundAsset)
        {
            logger.debug("缓存中读取accountFundAsset");
            logger.debug("accountFundAsset Cache:" + accountFundAsset);
            return (FundChangeModel) accountFundAsset;
        }
        else
        {
            logger.debug("数据库中读取accountFundAsset");
            logger.debug("★★fundChangeScan key=" + key);
            FundChangeModel fundChangeModel = fundScanService.setAccountAssetAttr(accountId, exchangePairVCoin, exchangePairMoney);
            logger.debug("★★fundChangeScan fundChangeModel=" + fundChangeModel.toString());
            RedisUtils.putObject(key, fundChangeModel, CacheConst.DEFAULT_CACHE_TIME); // 缓存有效期限5分钟
            accountFundAsset = RedisUtils.getObject(key);
            return (FundChangeModel) accountFundAsset; // 设置完缓存 返回
        }
    }
    
    /**
     * 计算风险率
     * @param direct
     * @param btcNetValue
     * @param acountBtcNetValue
     * @param proPercent
     */
    private BigDecimal calculateRiskRate(String direct, BigDecimal btcNetValue, BigDecimal acountBtcNetValue, BigDecimal proPercent)
    {
        if (com.blocain.bitms.tools.utils.StringUtils.equalsIgnoreCase(direct, "Long"))
        {
            if (acountBtcNetValue.compareTo(BigDecimal.ZERO) <= 0)
            {
                return BigDecimal.ONE;
            }
            else
            {
                BigDecimal ret = proPercent.multiply(BigDecimal.valueOf(100)).multiply(btcNetValue)
                        .divide((proPercent.multiply(BigDecimal.valueOf(100)).add(BigDecimal.valueOf(100))), 8, BigDecimal.ROUND_HALF_UP)
                        .divide(acountBtcNetValue, 8, BigDecimal.ROUND_HALF_UP).abs();
                return ret;
            }
        }
        else
        {
            if (acountBtcNetValue.compareTo(BigDecimal.ZERO) == 0)
            {
                return BigDecimal.ZERO;
            }
            else
            {
                BigDecimal ret = proPercent.multiply(BigDecimal.valueOf(100)).multiply(btcNetValue)
                        .divide((BigDecimal.valueOf(100).subtract(proPercent.multiply(BigDecimal.valueOf(100)))), 8, BigDecimal.ROUND_HALF_UP)
                        .divide(acountBtcNetValue, 8, BigDecimal.ROUND_HALF_UP).abs();
                return ret;
            }
        }
    }
}
