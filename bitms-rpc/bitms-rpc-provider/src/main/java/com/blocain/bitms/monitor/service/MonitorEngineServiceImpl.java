package com.blocain.bitms.monitor.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.bitpay.service.BitpayKeychainERC20Service;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import com.blocain.bitms.monitor.entity.*;
import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.payment.eth.EthLocalService;
import com.blocain.bitms.payment.eth.EtherscanRemoteService;
import com.blocain.bitms.payment.eth.EthereumUtils;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.blocain.bitms.boss.common.consts.MessageConst;
import com.blocain.bitms.boss.common.consts.ParamConsts;
import com.blocain.bitms.boss.common.entity.SysParameter;
import com.blocain.bitms.boss.common.service.MsgRecordNoSql;
import com.blocain.bitms.boss.common.service.SysParameterService;
import com.blocain.bitms.monitor.consts.MonitorConst;
import com.blocain.bitms.monitor.mapper.MonitorEngineMapper;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.trade.account.service.AccountService;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.utils.Numeric;

/**
 * 监控服务服务基类
 * <p>File：MonitorEngineServiceImpl.java</p>
 * <p>Title: BaseMonitorService</p>
 * <p>Description: BaseMonitorService</p>
 * <p>Copyright: Copyright (c) 2017/9/22</p>
 * <p>Company: BloCain</p>
 *
 * @author Jiangsc
 * @version 1.0
 */
@Service
public class MonitorEngineServiceImpl implements MonitorEngineService
{
    private static final Logger logger = LoggerFactory.getLogger(MonitorEngineServiceImpl.class);

    @Autowired
    private MonitorEngineMapper        monitorEngineMapper;
    @Autowired
    private MonitorLogsService          monitorLogsService;
    @Autowired
    private AccountService             accountService;
    
    @Autowired
    private MsgRecordNoSql             msgRecordNoSql;
    
    @Autowired
    private SysParameterService        sysParameterService;

    @Autowired
    private BitpayKeychainService      bitpayKeychainService;

    @Autowired
    private EtherscanRemoteService     etherscanRemoteService;

    @Autowired
    private BitpayKeychainERC20Service bitpayKeychainERC20Service;

    @Autowired
    private StockInfoService           stockInfoService;

    @Autowired
    private EthLocalService            ethLocalService;

    @Autowired
    private Erc20TokenLocalService     erc20TokenLocalService;
    /**
     * 执行杠杆保证金监控
     */
    @Override
    public void dealMarginMonitor(MonitorConfig config)throws BusinessException
    {
        if (null == config || !config.getActive())
            throw new BusinessException("保证金监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("保证金监控配置未设置监控业务!");

        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        // 获取撮合交易开关
        params = sysParameterService.getSysParameterByEntity(params);
        // 结算交割时，交易关闭，则不再进行保证金监控
        if (null != params && params.getValue().equalsIgnoreCase("no")) return;


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 保证金监控开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("bizids", config.getMonitorCategorys());
        monitorEngineMapper.dealMarginMonitor(resultMap);
        LoggerUtils.logDebug(logger, "================= 保证金监控结束执行存储过程:【" + formatter.format(new Date()) + "】");
        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }

    /**
     * 执行总账资金监控
     */
    @Override
    public void dealInternalPlatFundCurMonitor(MonitorConfig config)throws BusinessException
    {
        
        if (null == config || !config.getActive())
            throw new BusinessException("总账资金监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("总账资金监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        // 获取撮合交易开关
        params = sysParameterService.getSysParameterByEntity(params);
        // 结算交割时，交易关闭，则不再进行监控
        if (null != params && params.getValue().equalsIgnoreCase("no")) return;

        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 撮合总账资金监控开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("bizids", config.getMonitorCategorys());
        monitorEngineMapper.dealInternalPlatFundCur(resultMap);
        LoggerUtils.logDebug(logger, "================= 撮合总账资金监控结束执行存储过程:【" + formatter.format(new Date()) + "】");
        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }


    /**
     * 账户级别资金流水监控
     */
    @Override
    public void dealAccountFundCur(MonitorConfig config)throws BusinessException
    {
        if (null == config || !config.getActive())
            throw new BusinessException("账户级别资金流水监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("账户级别资金流水监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        // 获取撮合交易开关
        params = sysParameterService.getSysParameterByEntity(params);
        // 结算交割时，交易关闭，则不再进行监控
        if (null != params && params.getValue().equalsIgnoreCase("no")) return;

        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 账户级别资金流水监控开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("bizids", config.getMonitorCategorys());
        monitorEngineMapper.dealAccountFundCur(resultMap);
        LoggerUtils.logDebug(logger, "================= 账户级别资金流水监控结束执行存储过程:【" + formatter.format(new Date()) + "】");
        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }

    /**
     * 平台BTC总额核对监控
     */
    @Override
    public void dealDigitalCoinMonitor(MonitorConfig config)throws BusinessException
    {
        if (null == config || !config.getActive())
            throw new BusinessException("数字资产内外总额监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("数字资产内外总额监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        // 获取撮合交易开关
        params = sysParameterService.getSysParameterByEntity(params);
        // 结算交割时，交易关闭，则不再进行监控
        if (null != params && params.getValue().equalsIgnoreCase("no")) return;

        String rechargeWalletBal = bitpayKeychainService.getBtcCZWalletConfirmedBalance();
        String withdrawWalletBal = bitpayKeychainService.getBtcTXWalletAllBalance();
        String digitalCoin = config.getMonitorCategorys();
        if (StringUtils.isEmpty(rechargeWalletBal) && StringUtils.isEmpty(withdrawWalletBal))
            return;
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 数字资产内外总额监控开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("digitalCoin", digitalCoin);
        resultMap.put("externalRBal", rechargeWalletBal);
        resultMap.put("externalWBal", withdrawWalletBal);
        monitorEngineMapper.dealDigitalCoin(resultMap);
        LoggerUtils.logDebug(logger, "================= 数字资产内外总额监控结束执行存储过程:【" + formatter.format(new Date()) + "】");
        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }

    @Override
    public void dealCachCoinMonitor(MonitorConfig config)throws BusinessException
    {
        if (null == config || !config.getActive())
            throw new BusinessException("现金资产内外总额监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("现金资产内外总额监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        // 获取撮合交易开关
        params = sysParameterService.getSysParameterByEntity(params);
        // 结算交割时，交易关闭，则不再进行监控
        if (null != params && params.getValue().equalsIgnoreCase("no")) return;

        String cashCoins = config.getMonitorCategorys();
        if (StringUtils.isEmpty(cashCoins))
            return;
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 现金资产内外总额监控开始执行存储过程:【" + formatter.format(new Date()) + "】");
        resultMap.put("cashCoins", cashCoins);
        monitorEngineMapper.dealCashCoin(resultMap);
        LoggerUtils.logDebug(logger, "================= 现金资产内外总额监控结束执行存储过程:【" + formatter.format(new Date()) + "】");
        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }

    @Override
    public void dealBlockNumberMonitor(MonitorConfig config)throws BusinessException
    {
        if (null == config || !config.getActive())
            throw new BusinessException("区块高度内外部的监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("区块高度内外部的监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= 区块高度内外部的监控开始:【" + formatter.format(new Date()) + "】");
        //获取内部区块高度
        EthBlockNumber innerBlockNum = EthereumUtils.getBlockNumber();

        //获取外部区块高度
        String strOutBlockNum = etherscanRemoteService.eth_blockNumber();
        resultMap.put("inBlockNum", innerBlockNum.getBlockNumber().toString());
        resultMap.put("outBlockNum", Numeric.decodeQuantity((String)strOutBlockNum).toString());
        resultMap.put("blockResource", "ETH");
        monitorEngineMapper.dealBlockNumberMonitor(resultMap);
        LoggerUtils.logDebug(logger, "================= 区块高度内外部的监控结束:【" + formatter.format(new Date()) + "】");
        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }

    @Override
    public void dealErc20BalMonitor(MonitorConfig config)throws BusinessException
    {
        if (null == config || !config.getActive())
            throw new BusinessException("ERC20 内外部总额监控服务配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("ERC20 内外部总额监控配置未设置监控业务!");

        SysParameter params = new SysParameter();
        params.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        params.setParameterName(ParamConsts.MATCHTRADE_SWITCH);
        // 获取撮合交易开关
        params = sysParameterService.getSysParameterByEntity(params);
        // 结算交割时，交易关闭，则不再进行监控
        if (null != params && params.getValue().equalsIgnoreCase("no")) return;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // 1. 调用存储过程生成对账结果
        LoggerUtils.logDebug(logger, "================= ERC20 内外部总额监控开始:【" + formatter.format(new Date()) + "】");
        String monitorCategorys = config.getMonitorCategorys();
        if(StringUtils.isNotEmpty(monitorCategorys))
        {
            //循环业务列表
            String[] categorys = monitorCategorys.split(",");
            for(int i = 0 ; i < categorys.length;i++)
            {
                Long stockId = Long.valueOf(categorys[i]);
                StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockId);
                if(null == stockInfo) { throw new BusinessException(stockId+" 证券信息不存在"); }
                HashMap<Integer,BigDecimal> map = getErc20Balance(stockInfo,0);//查找所有钱包余额
                if(!CollectionUtils.isEmpty(map))
                {
                    resultMap.put("digitalCoin", categorys[i]);
                    resultMap.put("externalHBal", map.get(FundConsts.WALLETTYPE_HWALLET).stripTrailingZeros().toPlainString());
                    resultMap.put("externalCBal", map.get(FundConsts.WALLETTYPE_CWALLET).stripTrailingZeros().toPlainString());
                    monitorEngineMapper.dealErc20BalMonitor(resultMap);
                    LoggerUtils.logDebug(logger, "================= ERC20 内外部总额监控结束:【" + formatter.format(new Date()) + "】");
                    // 2. 对账结果不平，推送消息给相关人员
                    parseMonitorResult(config,resultMap);
                }
            }

        }
    }

    @Override
    public void dealErc20CollectFeeMonitor(MonitorConfig config)
    {
        if (null == config || !config.getActive())
            throw new BusinessException("归集费用监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("归集费用监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        LoggerUtils.logDebug(logger, "================= 归集费用监控开始:【" + formatter.format(new Date()) + "】");
        String monitorCategorys = config.getMonitorCategorys();
        List<MonitorLogs> monitorLogs = new ArrayList<MonitorLogs>();
        if(StringUtils.isNotEmpty(monitorCategorys))
        {
            //循环业务列表
            String[] categorys = monitorCategorys.split(",");
            resultMap = initResultMap();//初始化监控结果
            for(int i = 0 ; i < categorys.length;i++)
            {
                Long stockId = Long.valueOf(categorys[i]);
                StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockId);
                if(null == stockInfo) { throw new BusinessException(stockId+" 证券信息不存在"); }

                HashMap<Integer,BigDecimal> map = getErc20Balance(stockInfo,FundConsts.WALLETTYPE_COLLECTFEE);//查找归集费用钱包
                if(!CollectionUtils.isEmpty(map))
                {
                    BigDecimal collectFee = map.get(FundConsts.WALLETTYPE_COLLECTFEE);
                    dealMonitor(monitorLogs,resultMap,config,collectFee,stockInfo);
                }
            }

        }

        //生成监控日志
        if (!CollectionUtils.isEmpty(monitorLogs))
            monitorLogsService.insertBatch(monitorLogs);
        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }

    @Override
    public void dealErc20HotWalletMonitor(MonitorConfig config)
    {
        if (null == config || !config.getActive())
            throw new BusinessException("ERC20 热钱包资产监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("ERC20 热钱包资产监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        List<MonitorLogs> monitorLogs = new ArrayList<MonitorLogs>();
        LoggerUtils.logDebug(logger, "================= ERC20 热钱包资产监控开始:【" + formatter.format(new Date()) + "】");
        String monitorCategorys = config.getMonitorCategorys();
        if(StringUtils.isNotEmpty(monitorCategorys))
        {
            //循环业务列表
            String[] categorys = monitorCategorys.split(",");
            resultMap = initResultMap();//初始化监控结果
            for(int i = 0 ; i < categorys.length;i++)
            {
                Long stockId = Long.valueOf(categorys[i]);
                StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockId);
                if(null == stockInfo) { throw new BusinessException(stockId+" 证券信息不存在"); }
                HashMap<Integer,BigDecimal> map = getErc20Balance(stockInfo,0);
                if(!CollectionUtils.isEmpty(map))
                {
                  BigDecimal hotWalletBal = map.get(FundConsts.WALLETTYPE_HWALLET);
                  dealMonitor(monitorLogs,resultMap,config,hotWalletBal,stockInfo);
                }
            }

        }
        //生成监控日志
        if (!CollectionUtils.isEmpty(monitorLogs))
            monitorLogsService.insertBatch(monitorLogs);

        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }

    @Override
    public void dealErc20ColdWalletMonitor(MonitorConfig config)
    {
        if (null == config || !config.getActive())
            throw new BusinessException("ERC20 冷钱包资产监控配置不存在或已停用!");

        //监控服务未设置监控业务
        if (StringUtils.isEmpty(config.getMonitorCategorys()))
            throw new BusinessException("ERC20 冷钱包资产监控配置未设置监控业务!");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        List<MonitorLogs> monitorLogs = new ArrayList<MonitorLogs>();
        LoggerUtils.logDebug(logger, "================= ERC20 冷钱包资产监控开始:【" + formatter.format(new Date()) + "】");
        String monitorCategorys = config.getMonitorCategorys();
        if(StringUtils.isNotEmpty(monitorCategorys))
        {
            //循环业务列表
            String[] categorys = monitorCategorys.split(",");
            resultMap = initResultMap();//初始化监控结果
            for(int i = 0 ; i < categorys.length;i++)
            {
                Long stockId = Long.valueOf(categorys[i]);
                StockInfo stockInfo = stockInfoService.selectByPrimaryKey(stockId);
                if(null == stockInfo) { throw new BusinessException(stockId+" 证券信息不存在"); }
                HashMap<Integer,BigDecimal> map = getErc20Balance(stockInfo,0);
                if(!CollectionUtils.isEmpty(map))
                {
                    BigDecimal codeWalletBal = map.get(FundConsts.WALLETTYPE_CWALLET);
                    dealMonitor(monitorLogs,resultMap,config,codeWalletBal,stockInfo);
                }
            }

        }
        //生成监控日志
        if (!CollectionUtils.isEmpty(monitorLogs))
            monitorLogsService.insertBatch(monitorLogs);

        // 2. 对账结果不平，推送消息给相关人员
        parseMonitorResult(config,resultMap);
    }
    /**
     * 手工发起账户资产校验
     *  根据业务品种核对账户资产
     * @param categorysId  品种代码
     * @param acctId        账户ID
     * @return
     */
    @Override
    public MonitorResult dealAccountFundCurChk(String categorysId, String acctId)
    {
        MonitorResult result = new MonitorResult();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(categorysId))
        {
            result.setChekResult(1);
            result.setReturnCode(-1);
            result.setMonitorDesc("品种代码或币对代码不能为空！");
            return result;
        }
        resultMap.put("bizids", categorysId);
        resultMap.put("acctId", acctId);
        resultMap.put("stockinfoid","0");
        return doAccountFundCur(resultMap);
    }

    /**
     * 手工发起账户资产校验
     *   对业务品种的单一币核对账户资产
     * @param categorysId  品种代码
     * @param acctId        账户ID
     * @param stockinfoid  币对代码
     * @return
     */
    @Override
    public MonitorResult dealAccountFundCurChk(String categorysId, String acctId,String stockinfoid)
    {
        MonitorResult result = new MonitorResult();
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(categorysId) && StringUtils.isEmpty(stockinfoid))
        {
            result.setChekResult(1);
            result.setReturnCode(-1);
            result.setMonitorDesc("品种代码或币对代码不能为空！");
            return result;
        }
        resultMap.put("acctId", acctId);
        resultMap.put("stockinfoid",stockinfoid);
        return doAccountFundCur(resultMap);
    }

    /**
     * 获取 Erc20 余额
     * @param stockInfo   证券信息
     * @param walletType  钱包类型
     * @return
     */
    private HashMap<Integer,BigDecimal> getErc20Balance(StockInfo stockInfo,Integer walletType)throws BusinessException
    {
        HashMap<Integer,BigDecimal> map = new HashMap<Integer,BigDecimal>();
        BigDecimal balance = BigDecimal.ZERO;
        BitpayKeychainERC20 bitpayKeychainERC20 = new BitpayKeychainERC20();
        if(FundConsts.WALLETTYPE_COLLECTFEE.intValue() == walletType.intValue())
            bitpayKeychainERC20.setWalletType(FundConsts.WALLETTYPE_COLLECTFEE);
        bitpayKeychainERC20.setStockinfoId(String.valueOf(FundConsts.WALLET_ETH_TYPE));
        List<BitpayKeychainERC20> listBitpay = bitpayKeychainERC20Service.findList(bitpayKeychainERC20);
        if (listBitpay.size() == 0) { throw new BusinessException(stockInfo.getStockName()+" 付款钱包不存在"); }

        try{
            for(int i = 0; i < listBitpay.size(); i++)
            {
                bitpayKeychainERC20 = listBitpay.get(i);

                if (stockInfo.getId().longValue() == FundConsts.WALLET_ETH_TYPE.longValue())
                    balance = ethLocalService.eth_getBalance(bitpayKeychainERC20.getWalletId(), "latest");
                else
                    balance = erc20TokenLocalService.erc20_balanceOf(bitpayKeychainERC20.getWalletId(), stockInfo.getTokenContactAddr(),"latest");

                map.put(bitpayKeychainERC20.getWalletType(),balance);

            }

        }catch(Exception e){
            LoggerUtils.logError(logger, "解析监控结果失败：{}", e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 初始化监控结果
     * @return
     */
    private HashMap<String, Object> initResultMap()
    {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        ArrayList<MonitorResult> results = new ArrayList<MonitorResult>();
        resultMap.put(MonitorConst.CHECK_MONITOR_IDX,0L);
        resultMap.put(MonitorConst.CHECK_RETURN_CODE,MonitorConst.CHECK_RETURNCODE_NORMAL);
        resultMap.put(MonitorConst.CHECK_RETURN_CHEKRESULT,MonitorConst.CHECK_CHEKRESULT_NORMAL);
        resultMap.put(MonitorConst.CHECK_RETURN_DATA,results);
        resultMap.put(MonitorConst.CHECK_RETURN_CHEKMSG,"");
        return resultMap;
    }


    private List<MonitorLogs> dealMonitor(List<MonitorLogs> monitorLogs,HashMap<String, Object> resultMap,MonitorConfig config,BigDecimal bal,StockInfo stockInfo)throws BusinessException
    {
        MonitorIndex monitorIndex = null;
         //1. 获取监控指标
        HashMap<Long,MonitorIndex> idxMap = config.getIdxids();
        MonitorLogs monitorLog = null;

        Long idxid1 = config.getIdxid1();//获取指标ID
        if(idxid1.longValue() > 0L)
        {
            if (!idxMap.containsKey(idxid1))
                throw new BusinessException("监控服务配置异常，获取不到【指标_"+idxid1+"】");
            monitorIndex = idxMap.get(idxid1);
            monitorLog =  dealMonitorIdx(config,monitorIndex,bal,stockInfo,resultMap);

            if (StringUtils.isNotEmpty(monitorLog.getMonitorLogDesc()))
                monitorLogs.add(monitorLog);
        }

        Long idxid2 = config.getIdxid2();//获取指标ID
        if(idxid2.longValue() > 0L)
        {
            if (!idxMap.containsKey(idxid2))
                throw new BusinessException("监控服务配置异常，获取不到【指标_"+idxid2+"】");
            monitorIndex = idxMap.get(idxid2);
            monitorLog =  dealMonitorIdx(config,monitorIndex,bal,stockInfo,resultMap);

            if (null != monitorLog)
                monitorLogs.add(monitorLog);

        }

        Long idxid3 = config.getIdxid3();//获取指标ID
        if(idxid3.longValue() > 0L)
        {
            if (!idxMap.containsKey(idxid3))
                throw new BusinessException("监控服务配置异常，获取不到【指标_"+idxid3+"】");
            monitorIndex = idxMap.get(idxid3);
            monitorLog =  dealMonitorIdx(config,monitorIndex,bal,stockInfo,resultMap);

            if (null != monitorLog)
                monitorLogs.add(monitorLog);
        }

        Long idxid4 = config.getIdxid4();//获取指标ID
        if(idxid4.longValue() > 0L)
        {
            if (!idxMap.containsKey(idxid4))
                throw new BusinessException("监控服务配置异常，获取不到【指标_"+idxid4+"】");
            monitorIndex = idxMap.get(idxid4);
            monitorLog =  dealMonitorIdx(config,monitorIndex,bal,stockInfo,resultMap);

            if (null != monitorLog)
                monitorLogs.add(monitorLog);
        }

        return monitorLogs;

    }

    private MonitorLogs dealMonitorIdx(MonitorConfig config,MonitorIndex monitorIndex,BigDecimal bal,StockInfo stockInfo,HashMap<String, Object> resultMap)
    {
        HashMap<Long, MonitorLimitParam> limitParamMap = monitorIndex.getLimitParamMap();
        BigDecimal minValue = BigDecimal.ZERO;
        BigDecimal maxValue = BigDecimal.ZERO;
        Integer monitorResult = 1;
        MonitorLogs monitorLogs = new MonitorLogs();
        StringBuffer errorMsg = new StringBuffer();
        if (limitParamMap.containsKey(stockInfo.getId()))
        {
            MonitorLimitParam param = limitParamMap.get(stockInfo.getId());
            minValue = param.getMinValue();
            maxValue = param.getMaxValue();
        }

        //阈值都设为0，则比较规则为 监控值 > 0
        if (minValue.compareTo(BigDecimal.ZERO) == 0 && maxValue.compareTo(BigDecimal.ZERO) == 0)
        {
            monitorResult = bal.compareTo(BigDecimal.ZERO) > 0 ? 1: -1;
        }
        else
        {
            //阈值设置为区间的，则不在区间内的监控值，则预警
            monitorResult = bal.compareTo(minValue)>=0 && bal.compareTo(maxValue) <= 0 ? 1: -1;
        }

        if (monitorResult < 0 )
        {
            // 同时触发多个指标优先级，预警处理方式以最高级指标为准。
            Long lastIdx = (Long) resultMap.get(MonitorConst.CHECK_MONITOR_IDX); //已触发指标
            Integer lastMonitorResult = (Integer) resultMap.get(MonitorConst.CHECK_RETURN_CHEKRESULT);
            ArrayList<MonitorResult> results = (ArrayList<MonitorResult>) resultMap.get(MonitorConst.CHECK_RETURN_DATA);
            MonitorIndex lastMonitorIdx = config.getIdxids().get(lastIdx);
            if (null == lastMonitorIdx)
                lastIdx = monitorIndex.getId();
            else
                lastIdx = lastMonitorIdx.getIdxLevel() > monitorIndex.getIdxLevel() ? lastMonitorIdx.getId() : monitorIndex.getId();

            //监控错误日志
            errorMsg.append("【监控服务：").append(config.getMonitorName()).append("，监控指标：").append(monitorIndex.getIdxName()).append("】触发预警。")
                    .append("当前").append(stockInfo.getStockName()).append("金额：").append(bal.toPlainString()).append(",阈值范围【").append(minValue).append("~").append(maxValue).append("】");
            monitorLogs.setAttr(stockInfo.getId(),config.getMonitorCode(),stockInfo.getStockName(),config.getMonitorName(),monitorResult,errorMsg.toString());

            //处理监控结果
            resultMap.put(MonitorConst.CHECK_MONITOR_IDX,lastIdx);
            resultMap.put(MonitorConst.CHECK_RETURN_CHEKRESULT,monitorResult);
            MonitorResult result = new MonitorResult();
            result.setAccountId(0L);
            result.setMonitorDesc(errorMsg.toString());
            results.add(result);
        }

        return monitorLogs;
     }


    private MonitorResult doAccountFundCur(HashMap<String, Object> resultMap)
    {
        monitorEngineMapper.doAcctFundCurChk(resultMap);
        return parseMonitorResult(null,resultMap);
    }

    
    /***
     * 解析监控结果
     * @param resultMap
     * @throws BusinessException
     */
    MonitorResult parseMonitorResult(MonitorConfig config, HashMap<String, Object> resultMap) throws BusinessException
    {
        MonitorResult monitorResult = new MonitorResult();
        ArrayList result = null;
        String errorInfo = null;

        if (null == config)
            errorInfo="资产核对";
        else
            errorInfo = config.getMonitorName();

        logger.debug(errorInfo+"解析监控结果");
        if (CollectionUtils.isEmpty(resultMap))
        {
            logger.info("监控异常：返回结果为空.");
            throw new BusinessException(errorInfo+"监控异常：返回结果为空.");
        }

        try
        {

            if(null == resultMap.get(MonitorConst.CHECK_MONITOR_IDX))
            {
                throw new BusinessException(errorInfo+" monitor_idx: 为空");
            }
            if(null == resultMap.get(MonitorConst.CHECK_RETURN_CODE))
            {
                throw new BusinessException(errorInfo+" return_code: 为空");
            }
            if(null == resultMap.get(MonitorConst.CHECK_RETURN_CHEKMSG))
            {
                throw new BusinessException(errorInfo+" return_str: 为空");
            }
            if(null == resultMap.get(MonitorConst.CHECK_RETURN_CHEKRESULT))
            {
                throw new BusinessException(errorInfo+" monitor_result: 为空");
            }

            // 获取监控执行结果代码 (1 执行成功，-1 执行失败)
            Integer returnCode = (Integer) resultMap.get(MonitorConst.CHECK_RETURN_CODE);
            // 获取监控执行消息提示 (执行结果异常，直接将错误提醒抛出)
            String returnMsg = (String) resultMap.get(MonitorConst.CHECK_RETURN_CHEKMSG);
            // 获取监控结果代码 (1,正常,-1 异常)
            Integer chekResult = (Integer) resultMap.get(MonitorConst.CHECK_RETURN_CHEKRESULT);


            if (null != config && chekResult.intValue() < 0)
            {
                // 监控服务触发预警或触发禁止
                result = (ArrayList<MonitorResult>) resultMap.get(MonitorConst.CHECK_RETURN_DATA);
                Long monitorIdxId = (Long) resultMap.get(MonitorConst.CHECK_MONITOR_IDX);
                MonitorIndex monitorIndex = config.getIdxids().get(monitorIdxId);
                dealMonitorAction(config,monitorIndex,chekResult,result);
                //dealMonitorAction(config,chekResult,result);

            }
            else if (null == config && chekResult.intValue() < 0)
            {
                //手工对账不做预警或禁止处理，只返回详细检查异常信息
                StringBuilder msgContent = new StringBuilder();
                StringBuilder emailContent = new StringBuilder();
                result = (ArrayList<MonitorResult>) resultMap.get(MonitorConst.CHECK_RETURN_DATA);
                getMsgContentFromMonitorResult(null,null, result,msgContent,emailContent);
                returnMsg = emailContent.toString();
            }

            monitorResult.setChekResult(chekResult);
            monitorResult.setReturnCode(returnCode);
            monitorResult.setMonitorDesc(returnMsg);

        }catch(Exception e)
        {
            LoggerUtils.logError(logger, "解析监控结果失败：{}", e.getLocalizedMessage());
        }

        return monitorResult;
    }

    /**
     * 根据监控配置对触发预警或禁止项进行处理
     * @param chekResult 1 正常  -1 触发预警   -2 触发禁止
     * @param results    监控结果集
     */
    void dealMonitorAction(MonitorConfig config,MonitorIndex monitorIndex,Integer chekResult,ArrayList<MonitorResult> results)throws BusinessException
    {
        //监控配置为空即，手工发起对账
        if(null == config)
            return;

        String[] actionTypes ;   //执行方式
        Integer  monitorLevel;
        try
        {
            if(chekResult == MonitorConst.CHECK_RETDATA_WARN)
            {
                actionTypes = monitorIndex.getActionType().split(",");      //触发预警
                monitorLevel = MonitorConst.MONITOR_LEVEL_WARN;
            }

            else
            {
                actionTypes = monitorIndex.getActionType().split(",");    //触发禁止
                monitorLevel = MonitorConst.MONITOR_LEVEL_FORBID;
            }


            for (int i = 0; i < actionTypes.length; i++) {
                //冻结账户
                if (!StringUtils.isEmpty(actionTypes[i]) && MonitorConst.MONITOR_ACTIONTYPE_FROZENACCT.equalsIgnoreCase(actionTypes[i]) )
                    frozenAbnormalAcct(results);

                // 推送消息
                if (!StringUtils.isEmpty(actionTypes[i]) && MonitorConst.MONITOR_ACTIONTYPE_MESSAGE.equalsIgnoreCase(actionTypes[i]))
                    sendMsg(config,monitorIndex,monitorLevel,results);

                //关停提现和交易
                if (!StringUtils.isEmpty(actionTypes[i]) && MonitorConst.MONITOR_ACTIONTYPE_CLOSETRADEANDWITHDRAW.equalsIgnoreCase(actionTypes[i]))
                    closeTradeSwitch();

            }
        }catch(Exception e)
        {
            LoggerUtils.logError(logger, "监控配置对触发预警或禁止项进行处理出错：{}", e.getLocalizedMessage());
        }


    }

    /**
     * 关闭交易和提现
     * @return
     */
    int closeTradeSwitch()
    {
        int iResult = 0;
        SysParameter tradeParams = new SysParameter();
        tradeParams.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        tradeParams.setParameterName(ParamConsts.MATCHTRADE_SWITCH);

        SysParameter withdrawParams = new SysParameter();
        withdrawParams.setSystemName(ParamConsts.SYS_PARAMETER_SYSTEM_NAME_TRADE);
        withdrawParams.setParameterName(ParamConsts.WITHDRAW_SWITCH);

        List<SysParameter> params = Lists.newArrayList();
        params.add(sysParameterService.getSysParameterByEntity(tradeParams));
        params.add(sysParameterService.getSysParameterByEntity(withdrawParams));
        if (!CollectionUtils.isEmpty(params))
        {
            for (int i = 0; i < params.size(); i++)
            {
                params.get(i).setValue("no");
            }
            iResult = sysParameterService.updateBatch(params);
        }
        return iResult;
    }

    /**
     * 冻结监控异常的账户列表
     * @param results
     * @return 冻结的账户个数
     */
    void frozenAbnormalAcct(ArrayList<MonitorResult> results)
    {
        List<Long> frozenAcctList = getFrozenAcctListFromMonitorResult(results);
        if (CollectionUtils.isEmpty(frozenAcctList)) return;

        List<Account> accounts= accountService.findListByAcctIds(frozenAcctList);
        if(!CollectionUtils.isEmpty(frozenAcctList))
        {
            Account acct ;
            String errorInfo = "检测到账户资金流水异常，平台暂时冻结账户。";
            for(int i =0 ; i < accounts.size(); i++)
            {
                acct = accounts.get(i);
                accountService.modifyAccountStatusToFrozen(acct.getId(),"监控资金流水异常，暂时冻结账户资产");
                if(!StringUtils.isEmpty(acct.getMobNo()))
                    msgRecordNoSql.sendAlarmSms(acct.getMobNo(), MessageConst.TEMPLATE_SEND_SMS_ALARM_GENR, errorInfo);

                if(!StringUtils.isEmpty(acct.getEmail()))
                    sendAlarmEmail(errorInfo, acct.getEmail());
            }

        }

    }

    /**
     * 消息推送条件
     *  1. 监控配置了手机短信或邮件的接收人列表
     *  2. 触发预警或禁止
     * @param config
     * @param result
     */
    void sendMsg(MonitorConfig config,MonitorIndex monitorIndex,Integer monitorLevel, ArrayList<MonitorResult> result)
    {
        StringBuilder msgContent = new StringBuilder();
        StringBuilder emailContent = new StringBuilder();

        //拼接处理要推送的消息
        getMsgContentFromMonitorResult(config,monitorIndex,result,msgContent,emailContent);
        //从监控配置获取接收手机短信的联系人列表
        String phoneList = parseMonitorParam(MonitorConst.MONITOR_MSGTYPE_SMS,monitorLevel,monitorIndex);
        //从监控配置获取接收邮件的联系人列表
        String emails = parseMonitorParam(MonitorConst.MONITOR_MSGTYPE_EMAIL,monitorLevel,monitorIndex);

        //监控配置了接收短信人列表，并且有手机消息
        if (StringUtils.isNotEmpty(phoneList) && StringUtils.isNotEmpty(msgContent.toString()))
            msgRecordNoSql.sendAlarmSms(phoneList, MessageConst.TEMPLATE_SEND_SMS_ALARM_GENR, msgContent.toString());

        //监控配置了接收邮件人列表，并且有邮件消息
        if (StringUtils.isNotEmpty(emails) && StringUtils.isNotEmpty(emailContent.toString()))
            sendAlarmEmail(emailContent.toString(), emails);
    }

    /**
     * 根据监控反馈结果，拼接返回的消息内容
     * @param result
     * @return
     */
    void getMsgContentFromMonitorResult(MonitorConfig config,MonitorIndex monitorIndex, ArrayList<MonitorResult> result,StringBuilder msgContent, StringBuilder emailContent)
    {
        MonitorResult monitorResult = new MonitorResult();
        if (!CollectionUtils.isEmpty(result))
        {
            if (null !=config && null != monitorIndex)
                msgContent.append("触发【监控：").append(config.getMonitorName()).append(",指标：").append(monitorIndex.getIdxName()).append("】预警");
//            if (null != config && MonitorConst.MONITOR_PARAMTYPE_INTERNALPLATFUNDCUR.equalsIgnoreCase(config.getMonitorCode()))
//                msgContent.append("杠杆现货总账异常，请及时关注。");
//            else if (null != config && MonitorConst.MONITOR_PARAMTYPE_ACCTFUNDCUR.equalsIgnoreCase(config.getMonitorCode()))
//                msgContent.append("账户资金流水异常，请及时关注。");
//            else if (null != config && MonitorConst.MONITOR_PARAMTYPE_DIGITALCOIN.equalsIgnoreCase(config.getMonitorCode()))
//                msgContent.append("数字资产内外部总额异常，请及时关注。");
//            else if (null != config && MonitorConst.MONITOR_PARAMTYPE_CASHCOIN.equalsIgnoreCase(config.getMonitorCode()))
//                msgContent.append("现金资产内外部总额异常，请及时关注。");

            for (int i = 0; i < result.size(); i++)
            {
                monitorResult = result.get(i);
                if (StringUtils.isNotBlank(monitorResult.getMonitorDesc()))
                    emailContent.append(monitorResult.getMonitorDesc()).append("<br />");
            }

            if(msgContent.length() > 0 )
                msgContent.insert(0,"[运行环境："+BitmsConst.RUNNING_ENVIRONMONT+"]");

            if(emailContent.length() > 0)
                emailContent.insert(0,"[运行环境："+BitmsConst.RUNNING_ENVIRONMONT+"]<br />");
        }
    }

    /**
     * 根据参数代码，解析监控配置里的监控参数值
     * @param paramCode
     * @param monitorIndex
     * @return
     */
    String parseMonitorParam(String paramCode,Integer monitorLevel,MonitorIndex monitorIndex)
    {
        StringBuffer sParam = new StringBuffer();
        List<MonitorParam> params= new ArrayList<MonitorParam>();
        //从监控配置获取监控参数
//        if( MonitorConst.MONITOR_LEVEL_WARN == monitorLevel)
//            params = config.getWarnParamList();
//        else if (MonitorConst.MONITOR_LEVEL_FORBID == monitorLevel)
//            params = config.getForbidParamList();
        params = monitorIndex.getActionValueList();
        //不存在配置直接返回
        if(CollectionUtils.isEmpty(params))
            return sParam.toString();

        MonitorParam param ;
        for (int i = 0; i < params.size(); i++)
        {
            param = params.get(i);
            if(paramCode.equalsIgnoreCase(param.getParamCode()) && !StringUtils.isEmpty(param.getParamValue()))
                sParam.append(param.getParamValue());
        }

        return sParam.toString();

    }

    /**
     * 发送预警邮件
     * @param
     * @throws BusinessException
     */
    void sendAlarmEmail(String content, String emailList) throws BusinessException
    {
        // String appendEmail = null;
        StringBuilder mainEmail = new StringBuilder();
        if (StringUtils.isBlank(emailList)) throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        String[] emails = emailList.toString().split(",");
        List<String> list = java.util.Arrays.asList(emails);
        // 通过数组转成得到的list没有重写remove()，add()等方法，这里进行转换成ArrayList
        List<String> arrList = new ArrayList(list);
        if (arrList.get(0).startsWith("[")) mainEmail.append(arrList.get(0).substring(1));
        else mainEmail.append(arrList.get(0));
        // 首个邮件地址作为主发人地址，后续作为抄送地址，从列表中剔除主发人地址
        arrList.remove(0);
        InternetAddress[] addrList = null;
        try
        {
            if (!CollectionUtils.isEmpty(arrList))
            {
                addrList = new InternetAddress[arrList.size()];
                for (int i = 0; i < arrList.size(); i++)
                {
                    if (arrList.get(i).endsWith("]")) addrList[i] = new InternetAddress(arrList.get(i).substring(0, arrList.get(i).length() - 1));
                    else addrList[i] = new InternetAddress(arrList.get(i));
                }
            }
        }
        catch (AddressException e)
        {
            logger.error(e.getLocalizedMessage());
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        msgRecordNoSql.sendAlarmEmail(mainEmail.toString(), addrList, content, MessageConst.TEMPLATE_SEND_EMAIL_ALARM_GENR);
    }

    /**
     * 从监控结果解析待冻结的账户ID
     * @param result
     * @return
     */
    List<Long> getFrozenAcctListFromMonitorResult(ArrayList<MonitorResult> result)
    {
        List<Long> frozenAcctList = new ArrayList<Long>();

        if(!CollectionUtils.isEmpty(result))
        {
            MonitorResult monitorResult = new MonitorResult();
            for (int i = 0; i < result.size(); i++)
            {
                monitorResult = result.get(i);
                //重复的账号ID就跳过
                if (null != monitorResult && monitorResult.getAccountId() > 0 && !frozenAcctList.contains(monitorResult.getAccountId()))
                    frozenAcctList.add(monitorResult.getAccountId());
            }
        }
        return frozenAcctList;
    }
}
