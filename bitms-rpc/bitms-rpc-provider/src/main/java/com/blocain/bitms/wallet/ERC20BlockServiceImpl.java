package com.blocain.bitms.wallet;

import com.blocain.bitms.bitpay.entity.BitpayKeychainERC20;
import com.blocain.bitms.bitpay.service.BitpayKeychainERC20Service;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.payment.eth.EthLocalService;
import com.blocain.bitms.payment.eth.EthereumUtils;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ConversionUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;
import com.blocain.bitms.trade.block.service.BlockInfoERC20Service;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.BlockTransConfirmERC20;
import com.blocain.bitms.trade.fund.entity.EthAddrs;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import com.blocain.bitms.trade.fund.entity.SystemWalletAddrERC20;
import com.blocain.bitms.trade.fund.entity.SystemWalletERC20;
import com.blocain.bitms.trade.fund.model.FundModel;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.Erc20TokenService;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * ERC20区块服务
 */
@Service
public class ERC20BlockServiceImpl implements ERC20BlockService
{
    protected static final Logger         logger = LoggerFactory.getLogger(ERC20BlockServiceImpl.class);
    
    @Autowired
    private StockInfoService              stockInfoService;
    
    @Autowired
    private BlockInfoERC20Service         blockInfoERC20Service;
    
    @Autowired
    private BlockTransConfirmERC20Service blockTransConfirmERC20Service;
    
    @Autowired
    private SystemWalletERC20Service      systemWalletERC20Service;
    
    @Autowired
    private SystemWalletAddrERC20Service  systemWalletAddrERC20Service;

    @Autowired
    private FundCurrentService            fundCurrentService;

    @Autowired
    private BitpayKeychainERC20Service    bitpayKeychainERC20Service;

    @Autowired
    private EthLocalService               ethLocalService;

    @Autowired
    private Erc20TokenLocalService        erc20TokenLocalService;

    @Autowired
    private Erc20TokenService             erc20TokenService;

    @Autowired
    private EthAddrsService               ethAddrsService;

    private final  Double                 GAS_AMOUT = 60000d;

    private final  Double                 APPROVE_AMOUT = 10000000000d;
    
    @Override
    public void doSyncBlockNumber() throws BusinessException
    {
        // 取出ERC20区块节点最新的区块高度
        EthBlockNumber ethBlockNumber = EthereumUtils.getBlockNumber();
        if (null != ethBlockNumber)
        {
            logger.info("erc20区块最新块高度ethBlockNumber:" + ethBlockNumber.getBlockNumber());
            // 取出当前数据库中最大的区块高度
            BlockInfoERC20 blockInfoERC20 = blockInfoERC20Service.getMaxHeightBlockInfo();
            if (null != blockInfoERC20)
            {
                logger.info("blockInfoERC20:" + blockInfoERC20.toString());
                logger.info("数据库最大块高度:" + blockInfoERC20.getHeight().longValue());
                // 比较大小 当前数据库中的高度小于 区块节点最新的区块高度就插入数据库
                Long difference = ethBlockNumber.getBlockNumber().longValue() - blockInfoERC20.getHeight().longValue();
                // 如果外部区块高度与平台内部高度有差异
                if (difference > 0)
                {
                    // 根据区块高度差异循环
                    for (int i = 1; i <= difference; i++)
                    {
                        EthBlock.Block ethBlock = EthereumUtils.ethGetBlockByNumber(BigInteger.valueOf(blockInfoERC20.getHeight().longValue() + i), false);
                        if (null != ethBlock)
                        {
                            BlockInfoERC20 blockInfoERC20New = new BlockInfoERC20();
                            blockInfoERC20New.setHash(ethBlock.getHash());
                            blockInfoERC20New.setParentHash(ethBlock.getParentHash());
                            blockInfoERC20New.setHeight(ethBlock.getNumber().longValue());
                            blockInfoERC20New.setBlockTimeStamp(new Timestamp(ethBlock.getTimestamp().longValue() * 1000));// 区块产生的时间戳
                            blockInfoERC20New.setTransScanStatus("false"); // 区块是否已经扫描对应交易默认为false
                            blockInfoERC20New.setErc20TokenScanNumber(0L);
                            blockInfoERC20New.setRemark("ERC20区块节点信息同步,高度为" + ethBlock.getNumber().longValue());
                            logger.debug("save blockInfoERC20New:" + blockInfoERC20New.toString());
                            blockInfoERC20Service.save(blockInfoERC20New);
                        }
                        else
                        {
                            logger.debug("当前高度外部区块不存在！：" + blockInfoERC20.getHeight().longValue() + i);
                        }
                    }
                }
            }
            else // 首次启动ERC20区块节点信息同步
            {
                logger.info("首次启动ERC20区块节点信息同步");
                EthBlock.Block ethBlock = EthereumUtils.ethGetBlockByNumber(BigInteger.valueOf(ethBlockNumber.getBlockNumber().longValue()), false);
                BlockInfoERC20 blockInfoERC20New = new BlockInfoERC20();
                blockInfoERC20New.setHash(ethBlock.getHash());
                blockInfoERC20New.setParentHash(ethBlock.getParentHash());
                blockInfoERC20New.setHeight(ethBlock.getNumber().longValue());
                blockInfoERC20New.setBlockTimeStamp(new Timestamp(ethBlock.getTimestamp().longValue() * 1000));// 区块产生的时间戳
                blockInfoERC20New.setTransScanStatus("false"); // 区块是否已经扫描对应交易默认为false
                blockInfoERC20New.setErc20TokenScanNumber(0L);
                blockInfoERC20New.setRemark("首次启动ERC20区块节点信息同步,高度为" + ethBlock.getNumber().longValue());
                logger.info("save blockInfoERC20New:" + blockInfoERC20New.toString());
                blockInfoERC20Service.save(blockInfoERC20New);
            }
        }
        else
        {
            logger.error("未获取到外部区块信息！");
        }
    }
    
    @Override
    public void doScanERC20Block(BlockInfoERC20 LastUnScanTransBlockInfoErc20) throws BusinessException
    {
        logger.debug("LastUnScanTransBlockNumber:" + LastUnScanTransBlockInfoErc20.getHeight());
        List<Transaction> transactions = getTransactionsByBlockNumber(LastUnScanTransBlockInfoErc20.getHeight());
        LoggerUtils.logDebug(logger, "transactions.size():" + transactions.size());
        // -------------------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------
        // 第一步 eth本身处理
        LoggerUtils.logDebug(logger, "eth本身处理开始");
        Set<String> extToEthAddress = new TreeSet<>();// 取出所有外部接收地址
        for (Transaction transaction : transactions)
        {// 先将所有的接收地址收录起来
            if (null != transaction.getTo())
            {
                LoggerUtils.logDebug(logger, "transaction.getTo():" + transaction.getTo() + ";transaction.getValue():" + transaction.getValue());
                extToEthAddress.add(transaction.getTo());
            }
        }
        if (extToEthAddress.size() > 0)
        {
            // 根据接收地址匹配平台中已存在的地址
            Set<String> interEthAddress = getAccountInterAddrsByParams(extToEthAddress.toArray(new String[extToEthAddress.size()]));
            if (null != interEthAddress && interEthAddress.size() > 0)
            {
                for (Transaction transaction : transactions)
                {
                    if (interEthAddress.contains(transaction.getTo()))
                    {
                        TransactionReceipt transactionReceipt = EthereumUtils.ethGetTransactionReceipt(transaction.getHash());
                        LoggerUtils.logInfo(logger,
                                "transactionReceipt.getStatus():" + transactionReceipt.getStatus() + ";transaction.getValue():" + transaction.getValue());
                        // 必须交易是成功的 并且 ETH发生额必须大于0
                        if (transactionReceipt.getStatus().equals("0x1") && transaction.getValue().compareTo(BigInteger.ZERO) > 0)
                        {
                            LoggerUtils.logDebug(logger, "transactionReceipt.getStatus():" + transactionReceipt.getStatus());
                            LoggerUtils.logDebug(logger, "transaction.getValue():" + transaction.getValue());
                            // todo : 将交易信息同步到数据库BlockTransConfirmERC20表中,整张表 BlockTransConfirmERC20Service
                            BlockTransConfirmERC20 blockTransConfirmERC20New = new BlockTransConfirmERC20();
                            blockTransConfirmERC20New.setTransId(transaction.getHash());
                            List<BlockTransConfirmERC20> listBlockTransConfirmERC20 = blockTransConfirmERC20Service.findList(blockTransConfirmERC20New);
                            if (null != listBlockTransConfirmERC20 && listBlockTransConfirmERC20.size() > 0)
                            {
                                LoggerUtils.logDebug(logger, "eth扫块对应交易已经存在 无需入账 transaction.getHash() " + transaction.getHash());
                            }
                            else
                            {
                                StockInfo stockinfo = stockInfoService.selectByPrimaryKey(FundConsts.WALLET_ETH_TYPE);
                                // 如果充值数量小于等于限定数量，则不予入账
                                if (stockinfo != null && BigDecimal.valueOf(transaction.getValue().doubleValue()).divide(BigDecimal.valueOf(Math.pow(10, 18)))
                                        .compareTo(stockinfo.getSmallDepositFeeValue()) > 0 )
                                {
                                    if(!StringUtils.equalsIgnoreCase(stockinfo.getCanRecharge(),FundConsts.PUBLIC_STATUS_YES))
                                    {
                                        logger.info(stockinfo.getStockCode()+"["+stockinfo.getTokenContactAddr()+"]充值开关已关闭!");
                                        continue;
                                    }
                                    // 准备插入数据
                                    SystemWalletERC20 systemWalletERC20 = new SystemWalletERC20();
                                    systemWalletERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                                    blockTransConfirmERC20New.setWalletId(systemWalletERC20Service.findWallet(systemWalletERC20).getWalletId());
                                    blockTransConfirmERC20New.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                                    if(StringUtils.isNotBlank(transaction.getFrom()) ){
                                        blockTransConfirmERC20New.setFromAddress(transaction.getFrom());
                                    } else {
                                        blockTransConfirmERC20New.setFromAddress("eth充值");
                                    }
                                    blockTransConfirmERC20New.setWalletAddr(transaction.getTo());
                                    blockTransConfirmERC20New.setTransId(transaction.getHash());
                                    blockTransConfirmERC20New.setConfirmSide("localBlockNode");
                                    blockTransConfirmERC20New.setDirect("collect");
                                    blockTransConfirmERC20New.setStatus("unconfirm");
                                    blockTransConfirmERC20New.setCollectStatus("unCollect");
                                    blockTransConfirmERC20New
                                            .setAmount(BigDecimal.valueOf(transaction.getValue().doubleValue()).divide(BigDecimal.valueOf(Math.pow(10, 18))));// eth18位小数
                                    blockTransConfirmERC20New.setFee(BigDecimal.valueOf(0));
                                    blockTransConfirmERC20New.setBlockNumber(String.valueOf(transaction.getBlockNumber()));
                                    blockTransConfirmERC20New.setBlockHash(transaction.getBlockHash());
                                    blockTransConfirmERC20New.setRemark("eth扫块对应交易入账");
                                    blockTransConfirmERC20New.setCreateDate(new Date());
                                    LoggerUtils.logInfo(logger, "eth扫块对应交易入账blockTransConfirmERC20New:" + blockTransConfirmERC20New.toString());
                                    blockTransConfirmERC20Service.save(blockTransConfirmERC20New);
                                }
                            }
                        }
                    }
                }
            }
        }
        LoggerUtils.logDebug(logger, "eth本身处理结束");
        // -------------------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------
        // 第二步 token本身处理
        LoggerUtils.logDebug(logger, "token本身处理开始");
        Set<String> extToTokenAddress = new TreeSet<>();// 取出所有外部接收地址
        for (Transaction transaction : transactions)
        {// 先将所有的接收地址收录起来
            LoggerUtils.logDebug(logger, "transaction.getInput():" + transaction.getInput());
            if (null != transaction.getInput() && transaction.getInput().length() >= 137)
            {
                LoggerUtils.logDebug(logger, "transaction.getInput().substring(0,10):" + transaction.getInput().substring(0, 10));
                // SHA3_TRANSFER = "0xa9059cbb"; // transfer(address,uint256)
                if (transaction.getInput().substring(0, 10).equals("0xa9059cbb"))
                {
                    // 解析to的地址
                    LoggerUtils.logDebug(logger, "transaction.getInput().substring(34,74):" + transaction.getInput().substring(34, 74));
                    extToTokenAddress.add("0x" + transaction.getInput().substring(34, 74));
                }
                // SHA3_TRANSFERFROM = "0x23b872dd"; // transferFrom(address,address,uint256)
                else if (transaction.getInput().substring(0, 10).equals("0x23b872dd"))
                {
                    // 解析to的地址
                    LoggerUtils.logDebug(logger, "transaction.getInput().substring(98,138)" + transaction.getInput().substring(98, 138));
                    extToTokenAddress.add("0x" + transaction.getInput().substring(98, 138));
                }
            }
        }
        if (extToTokenAddress.size() > 0)
        {
            // 根据接收地址匹配平台中已存在的地址
            Set<String> interTokenAddress = getAccountInterAddrsByParams(extToTokenAddress.toArray(new String[extToTokenAddress.size()]));
            if (null != interTokenAddress && interTokenAddress.size() > 0)
            {
                for (Transaction transaction : transactions)
                {
                    String tokenToAddr = "";
                    BigDecimal tokenValue = BigDecimal.ZERO;
                    if (transaction.getInput().length() >= 137)
                    {
                        // SHA3_TRANSFER = "0xa9059cbb"; // transfer(address,uint256)
                        if (transaction.getInput().substring(0, 10).equals("0xa9059cbb"))
                        {
                            // 解析to的地址
                            tokenToAddr = "0x" + transaction.getInput().substring(34, 74);
                            tokenValue = EthereumUtils.hexToBigDecimal(transaction.getInput().substring(75, 138));
                        }
                        // SHA3_TRANSFERFROM = "0x23b872dd"; // transferFrom(address,address,uint256)
                        else if (transaction.getInput().substring(0, 10).equals("0x23b872dd"))
                        {
                            // 解析to的地址
                            tokenToAddr = "0x" + transaction.getInput().substring(98, 138);
                            tokenValue = EthereumUtils.hexToBigDecimal(transaction.getInput().substring(139, 202));
                        }
                    }
                    if (interTokenAddress.contains(tokenToAddr))
                    {
                        // 查询交易具体状态
                        TransactionReceipt transactionReceipt = EthereumUtils.ethGetTransactionReceipt(transaction.getHash());
                        LoggerUtils.logDebug(logger, "transactionReceipt.getStatus():" + transactionReceipt.getStatus() + ";tokenValue:" + tokenValue);
                        // 必须交易是成功的 并且 ETH发生额必须等于0 并且token发生额必须大于0
                        if (null != transactionReceipt && transactionReceipt.getStatus().equals("0x1") && transaction.getValue().intValue() == 0
                                && tokenValue.compareTo(BigDecimal.ZERO) > 0)
                        {
                            LoggerUtils.logDebug(logger, "transactionReceipt.getStatus():" + transactionReceipt.getStatus());
                            LoggerUtils.logDebug(logger, "tokenValue:" + tokenValue);
                            LoggerUtils.logDebug(logger, "tokenContactAddr:" + transaction.getTo());
                            StockInfo stockInfoSelect = new StockInfo();
                            stockInfoSelect.setTokenContactAddr(transaction.getTo());
                            List<StockInfo> listStockInfo = stockInfoService.findList(stockInfoSelect);
                            // 查询该token在平台内部存在,且充值数量大于限定数量，就插入，否则就不处理
                            if (null != listStockInfo && listStockInfo.size() > 0
                                    && tokenValue.divide(BigDecimal.valueOf(Math.pow(10, EthereumUtils.tokenDecimals(transaction.getTo())))).compareTo(listStockInfo
                                    .get(0).getSmallDepositFeeValue()) > 0 )
                            {
                                // todo : 将交易信息同步到数据库BlockTransConfirmERC20表中,整张表 BlockTransConfirmERC20Service
                                BlockTransConfirmERC20 blockTransConfirmERC20New = new BlockTransConfirmERC20();
                                blockTransConfirmERC20New.setTransId(transaction.getHash());
                                List<BlockTransConfirmERC20> listBlockTransConfirmERC20 = blockTransConfirmERC20Service.findList(blockTransConfirmERC20New);
                                if (null != listBlockTransConfirmERC20 && listBlockTransConfirmERC20.size() > 0)
                                {
                                    LoggerUtils.logDebug(logger, "token扫块对应交易已经存在 无需入账 transaction.getHash() " + transaction.getHash());
                                }
                                else
                                {
                                    if(!StringUtils.equalsIgnoreCase(listStockInfo.get(0).getCanRecharge(),FundConsts.PUBLIC_STATUS_YES))
                                    {
                                        logger.info(listStockInfo.get(0).getStockCode()+"["+listStockInfo.get(0).getTokenContactAddr()+"]充值开关已关闭!");
                                        continue;
                                    }
                                    SystemWalletERC20 systemWalletERC20 = new SystemWalletERC20();
                                    systemWalletERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                                    blockTransConfirmERC20New.setWalletId(systemWalletERC20Service.findWallet(systemWalletERC20).getWalletId());
                                    blockTransConfirmERC20New.setStockinfoId(listStockInfo.get(0).getId());
                                    if(StringUtils.isNotBlank(transaction.getFrom()) ){
                                        blockTransConfirmERC20New.setFromAddress(transaction.getFrom());
                                    } else {
                                        blockTransConfirmERC20New.setFromAddress("eerc20充值");
                                    }
                                    blockTransConfirmERC20New.setWalletAddr(tokenToAddr);
                                    blockTransConfirmERC20New.setTransId(transaction.getHash());
                                    blockTransConfirmERC20New.setConfirmSide("localBlockNode");
                                    blockTransConfirmERC20New.setDirect("collect");
                                    blockTransConfirmERC20New.setStatus("unconfirm");
                                    blockTransConfirmERC20New.setCollectStatus("unCollect");
                                    // 发生额
                                    blockTransConfirmERC20New
                                            .setAmount(tokenValue.divide(BigDecimal.valueOf(Math.pow(10, EthereumUtils.tokenDecimals(transaction.getTo())))));// eth18位小数
                                    // 如果有 Transfer事件 Approval事件 要处理 开始
                                    List<Log> listTransactionReceiptEventLogs = transactionReceipt.getLogs();
                                    if(null != listTransactionReceiptEventLogs && listTransactionReceiptEventLogs.size() > 0){
                                        for(Log listTransactionReceiptEventLog : listTransactionReceiptEventLogs)
                                        {
                                            // Transfer事件
                                            if(listTransactionReceiptEventLog.getTopics().size() > 0){
                                                if(listTransactionReceiptEventLog.getTopics().get(0).substring(0, 10).equals("0xddf252ad")){
                                                    logger.info("Transfer事件触发listTransactionReceiptEventLog:" + listTransactionReceiptEventLog.toString());
                                                    BigDecimal tokenValueEvent = EthereumUtils.hexToBigDecimal(listTransactionReceiptEventLog.getData().substring(2));
                                                    blockTransConfirmERC20New
                                                            .setAmount(tokenValueEvent.divide(BigDecimal.valueOf(Math.pow(10, EthereumUtils.tokenDecimals(transaction.getTo())))));// eth18位小数
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    // 如果有 Transfer事件 Approval事件 要处理  结束
                                    blockTransConfirmERC20New.setFee(BigDecimal.valueOf(0));
                                    blockTransConfirmERC20New.setBlockNumber(String.valueOf(transaction.getBlockNumber()));
                                    blockTransConfirmERC20New.setBlockHash(transaction.getBlockHash());
                                    blockTransConfirmERC20New.setRemark("token扫块对应交易入账");
                                    blockTransConfirmERC20New.setCreateDate(new Date());
                                    LoggerUtils.logDebug(logger, "token扫块对应交易入账blockTransConfirmERC20New:" + blockTransConfirmERC20New.toString());
                                    blockTransConfirmERC20Service.save(blockTransConfirmERC20New);
                                }
                            }
                        }
                    }
                }
            }
        }
        LoggerUtils.logDebug(logger, "token本身处理结束");
        // -------------------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------
        // todo : 将区块扫描信息回填到数据库BlockInfoERC20表中,transScanStatus字段
        LastUnScanTransBlockInfoErc20.setTransScanStatus("true"); // 扫完交易
        LoggerUtils.logDebug(logger, "将区块扫描信息回填到数据库BlockInfoERC20表中,transScanStatus字段LastUnScanTransBlockInfoErc20:" + LastUnScanTransBlockInfoErc20.toString());
        blockInfoERC20Service.updateByPrimaryKey(LastUnScanTransBlockInfoErc20);
    }

    /**
     * ERC20区块Erc20Token交易扫描
     * @throws BusinessException
     */
    @Override
    public void doScanERC20BlockErc20Token(EthBlock.Block block) throws BusinessException
    {
        LoggerUtils.logInfo(logger,"doScanERC20BlockErc20Token 当前块高度为:" + block.getNumber());
        // UnScanTransBlockInfoErc20.getErc20TokenScanNumber() 判断是否小于2 否则不扫描
        List<Transaction> transactions = getTransactionsByBlockNumber(block.getNumber().longValue());
        LoggerUtils.logDebug(logger, "transactions.size():" + transactions.size());
        // -------------------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------
        LoggerUtils.logDebug(logger, "doScanERC20BlockErc20Token开始");
        for (Transaction transaction : transactions)
        {
            TransactionReceipt transactionReceipt = EthereumUtils.ethGetTransactionReceipt(transaction.getHash());
            LoggerUtils.logDebug(logger,
                    "transactionReceipt.getStatus():" + transactionReceipt.getStatus() + ";transaction.getValue():" + transaction.getValue() + ";transactionReceipt.getContractAddress():" + transactionReceipt.getContractAddress());
            // 必须交易是成功的 并且 ETH发生额必须等于0 并且要有合约地址
            if (null != transactionReceipt.getStatus() && transactionReceipt.getStatus().equals("0x1") &&
                    transaction.getValue().compareTo(BigInteger.ZERO) == 0 && null != transactionReceipt.getContractAddress())
            {
                LoggerUtils.logDebug(logger, "transactionReceipt.getStatus():" + transactionReceipt.getStatus());
                LoggerUtils.logDebug(logger, "transaction.getValue():" + transaction.getValue());
                LoggerUtils.logDebug(logger, "transactionReceipt.getContractAddress():" + transactionReceipt.getContractAddress());
                // todo : 判断Erc20Token中是否已经存在Erc20oken
                Erc20Token erc20TokenNew = new Erc20Token();
                erc20TokenNew.setContractAddr(transactionReceipt.getContractAddress());
                List<Erc20Token> listErc20Token = erc20TokenService.findList(erc20TokenNew);
                if (null != listErc20Token && listErc20Token.size() > 0)
                {
                    LoggerUtils.logInfo(logger, "Erc20Token中已经存在Erc20oken 无需再处理 transactionReceipt.getContractAddress(): " + transactionReceipt.getContractAddress());
                }
                else
                {
                    BigDecimal totalSupply = erc20TokenLocalService.erc20_totalSupply(transactionReceipt.getContractAddress());
                    LoggerUtils.logInfo(logger, "totalSupply:" + totalSupply);
                    // 发行总量 长度大于等于100000  小于等于100000000000
                    if(null != totalSupply && totalSupply.compareTo(BigDecimal.valueOf(100000)) >= 0 && totalSupply.compareTo(BigDecimal.valueOf(100000000000l)) <= 0){
                        Long decimal =  erc20TokenLocalService.erc20_decimals(transactionReceipt.getContractAddress());
                        LoggerUtils.logInfo(logger, "decimal:" + decimal);
                        if(null != decimal ){
                            String symbol = erc20TokenLocalService.erc20_symbol(transactionReceipt.getContractAddress());
                            LoggerUtils.logInfo(logger, "symbol:" + symbol);
                            // 符号 长度大于等于2  小于等于6  必须是全英文字母
                            if(null != symbol && symbol.length() >= 2 && symbol.length() <= 6 && symbol.matches("[a-zA-Z]+")){
                                erc20TokenNew = new Erc20Token();
                                erc20TokenNew.setSymbol(symbol.toUpperCase());
                                erc20TokenNew.setSymbolName(erc20TokenLocalService.erc20_name(transactionReceipt.getContractAddress()));
                                erc20TokenNew.setPair(symbol.toLowerCase()+"2eth");
                                erc20TokenNew.setContractAddr(transactionReceipt.getContractAddress().toLowerCase());// 合约地址 小写
                                erc20TokenNew.setTotalSupply(totalSupply);
                                erc20TokenNew.setTokenDecimals(decimal);
                                erc20TokenNew.setCreateDate(new Date());
                                erc20TokenNew.setIsActive("no");
                                erc20TokenNew.setActiveEndDate(new Date());
                                erc20TokenNew.setNeedAward(0);
                                erc20TokenNew.setAwardStatus(0);
                                erc20TokenNew.setBlockHeight(block.getNumber().longValue()); // 对应块高度
                                LoggerUtils.logInfo(logger, "ERC20区块Erc20Token扫描入库:" + erc20TokenNew.toString());
                                erc20TokenService.save(erc20TokenNew);
                            }
                        }
                    }
                }
            }
        }
        LoggerUtils.logDebug(logger, "doScanERC20BlockErc20Token结束");
        // -------------------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------
    }

    /**
     * EthAddrs地址库信息扫描
     * @throws BusinessException
     */
    @Override
    public void doScanEthAddrs(EthBlock.Block block) throws BusinessException
    {
        LoggerUtils.logInfo(logger,"doScanEthAddrs 当前块高度为:" + block.getNumber());
        List<Transaction> transactions = getTransactionsByBlockNumber(block.getNumber().longValue());
        LoggerUtils.logDebug(logger, "transactions.size():" + transactions.size());
        // -------------------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------
        LoggerUtils.logDebug(logger, "doScanEthAddrs开始");
        for (Transaction transaction : transactions)
        {
            TransactionReceipt transactionReceipt = EthereumUtils.ethGetTransactionReceipt(transaction.getHash());
            LoggerUtils.logDebug(logger,
                    "transactionReceipt.getStatus():" + transactionReceipt.getStatus() + ";transaction.getValue():" + transaction.getValue() + ";transactionReceipt.getContractAddress():" + transactionReceipt.getContractAddress());
            // 必须交易是成功的 并且 ETH发生额必须大于0 并且要有合约地址
            if (null != transactionReceipt.getStatus() && transactionReceipt.getStatus().equals("0x1") &&
                    transaction.getValue().compareTo(BigInteger.ZERO) > 0 && null == transactionReceipt.getContractAddress())
            {
                LoggerUtils.logDebug(logger, "transactionReceipt.getStatus():" + transactionReceipt.getStatus());
                LoggerUtils.logDebug(logger, "transaction.getValue():" + transaction.getValue());
                LoggerUtils.logDebug(logger, "transactionReceipt.getContractAddress():" + transactionReceipt.getContractAddress());
                LoggerUtils.logDebug(logger, "transactionReceipt.getFrom():" + transactionReceipt.getFrom());
                LoggerUtils.logDebug(logger, "transactionReceipt.getTo():" + transactionReceipt.getTo());
                // todo : 判断EthAddrs中是否已经存在EEthAddrs  根据form地址
                EthAddrs ethAddrsNew = new EthAddrs();
                ethAddrsNew.setAddr(transactionReceipt.getFrom().toLowerCase());
                List<EthAddrs> listEthAddrs = ethAddrsService.findList(ethAddrsNew);
                if (null != listEthAddrs && listEthAddrs.size() > 0)
                {
                    LoggerUtils.logInfo(logger, "EthAddrs中已经存在EthAddrs 无需再处理 transactionReceipt.getFrom(): " + transactionReceipt.getFrom());
                }
                else {
                    BigDecimal fromEthBalance = ethLocalService.eth_getBalance(transactionReceipt.getFrom(), "latest");
                    if (null != fromEthBalance && fromEthBalance.compareTo(BigDecimal.ZERO) > 0) {
                        ethAddrsNew = new EthAddrs();
                        ethAddrsNew.setBlockHeight(transactionReceipt.getBlockNumber().longValue()); // 对应块高度
                        ethAddrsNew.setAddr(transactionReceipt.getFrom().toLowerCase());
                        ethAddrsNew.setEthBalance(fromEthBalance);
                        ethAddrsNew.setIsCollect("no"); // no
                        ethAddrsNew.setCreateDate(new Date());
                        LoggerUtils.logInfo(logger, "EthAddrs地址库扫描入库:" + ethAddrsNew.toString());
                        ethAddrsService.save(ethAddrsNew);
                    }
                }
                // todo : 判断EthAddrs中是否已经存在EEthAddrs  根据to地址
                ethAddrsNew = new EthAddrs();
                ethAddrsNew.setAddr(transactionReceipt.getTo().toLowerCase());
                listEthAddrs = ethAddrsService.findList(ethAddrsNew);
                if (null != listEthAddrs && listEthAddrs.size() > 0)
                {
                    LoggerUtils.logInfo(logger, "EthAddrs中已经存在EthAddrs 无需再处理 transactionReceipt.getTo(): " + transactionReceipt.getTo());
                }
                else {
                    BigDecimal toEthBalance = ethLocalService.eth_getBalance(transactionReceipt.getTo(), "latest");
                    if (null != toEthBalance && toEthBalance.compareTo(BigDecimal.ZERO) > 0) {
                        ethAddrsNew = new EthAddrs();
                        ethAddrsNew.setBlockHeight(transactionReceipt.getBlockNumber().longValue()); // 对应块高度
                        ethAddrsNew.setAddr(transactionReceipt.getTo().toLowerCase());
                        ethAddrsNew.setEthBalance(toEthBalance);
                        ethAddrsNew.setIsCollect("no"); // no
                        ethAddrsNew.setCreateDate(new Date());
                        LoggerUtils.logInfo(logger, "EthAddrs地址库扫描入库:" + ethAddrsNew.toString());
                        ethAddrsService.save(ethAddrsNew);
                    }
                }

            }
        }
        LoggerUtils.logDebug(logger, "doScanEthAddrs结束");
        // -------------------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------
    }

    @Override
    public void scanERC20Trans() throws BusinessException
    {
        BlockTransConfirmERC20 blockTransConfirmERC20 = new BlockTransConfirmERC20();
        blockTransConfirmERC20.setStatus("unconfirm"); // 未确认状态
        List<BlockTransConfirmERC20> listBlockTransConfirmERC20 = blockTransConfirmERC20Service.findList(blockTransConfirmERC20);
        if (CollectionUtils.isNotEmpty(listBlockTransConfirmERC20))
        {
            for (int i = 0; i < listBlockTransConfirmERC20.size(); i++ )
            {
                try
                {
                    // 取出ERC20区块节点最新的区块高度
                    EthBlockNumber ethBlockNumber = EthereumUtils.getBlockNumber();
                    // 内外部区块高度差
                    BigInteger blockNumberDiff = ethBlockNumber.getBlockNumber()
                            .subtract(BigInteger.valueOf(Long.valueOf(listBlockTransConfirmERC20.get(i).getBlockNumber())));
                    LoggerUtils.logDebug(logger, "blockNumberDiff:" + blockNumberDiff);
                    TransactionReceipt transactionReceipt = EthereumUtils.ethGetTransactionReceipt(listBlockTransConfirmERC20.get(i).getTransId());
                    LoggerUtils.logDebug(logger, "transactionReceipt.getStatus():" + transactionReceipt.getStatus());
                    // 必须交易是成功的 并且 区块报块至少12块以上
                    if (transactionReceipt.getStatus().equals("0x1") && blockNumberDiff.intValue() >= 12)
                    {
                        SystemWalletAddrERC20 systemWalletAddrERC20 = new SystemWalletAddrERC20();
                        systemWalletAddrERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                        systemWalletAddrERC20.setWalletId(listBlockTransConfirmERC20.get(i).getWalletId());
                        systemWalletAddrERC20.setWalletAddr(listBlockTransConfirmERC20.get(i).getWalletAddr());
                        List<SystemWalletAddrERC20> listSystemWalletAddrERC20 = systemWalletAddrERC20Service.findList(systemWalletAddrERC20);
                        // 该充值地址存在就处理资产入账处理
                        if (null != listSystemWalletAddrERC20 && listSystemWalletAddrERC20.size() > 0)
                        {
                            LoggerUtils.logDebug(logger, "listSystemWalletAddrERC20.get(0).getAccountId():" + listSystemWalletAddrERC20.get(0).getAccountId());
                            FundModel fundModel = new FundModel();
                            fundModel.setAccountId(Long.valueOf(listSystemWalletAddrERC20.get(0).getAccountId()));
                            fundModel.setStockinfoId(listBlockTransConfirmERC20.get(i).getStockinfoId());
                            fundModel.setStockinfoIdEx(listBlockTransConfirmERC20.get(i).getStockinfoId());
                            fundModel.setBusinessFlag(FundConsts.SYSTEM_BUSSINESS_FLAG_WALLETRECHARGE);
                            fundModel.setAmount(listBlockTransConfirmERC20.get(i).getAmount());
                            fundModel.setAmountEx(BigDecimal.ZERO);
                            fundModel.setFee(BigDecimal.ZERO);
                            fundModel.setAddress(listBlockTransConfirmERC20.get(i).getWalletAddr());
                            fundModel.setTransId(listBlockTransConfirmERC20.get(i).getTransId());
                            fundModel.setOriginalBusinessId(listBlockTransConfirmERC20.get(i).getId());
                            LoggerUtils.logDebug(logger, "fundModel:" + fundModel);

                            // 资产入账处理并且修改BlockTransConfirmERC20的状态为confirm
                            LoggerUtils.logDebug(logger, "资产入账处理并且修改BlockTransConfirmERC20的状态为confirm");
                            fundCurrentService.doRechargeERC20(fundModel, listBlockTransConfirmERC20.get(i).getId());
                        }
                    }
                }
                catch (Exception e)
                {
                    LoggerUtils.logError(logger, "ERC20交易扫描入账业务处理失败：{}", e.getLocalizedMessage());
                    continue;
                }
            }
        }

    }

    @Override
    public void collectERC20Balance() throws BusinessException
    {
        // 取出ERC20区块节点最新的区块高度
        EthBlockNumber ethBlockNumber = EthereumUtils.getBlockNumber();
        if (null != ethBlockNumber)
        {
            EthBlock.Block ethBlock = EthereumUtils.ethGetBlockByNumber(BigInteger.valueOf(ethBlockNumber.getBlockNumber().longValue()), false);
            // 区块产生的时间戳
            long blockTimestamp = ethBlock.getTimestamp().longValue() * 1000;
            long systemTimestamp = System.currentTimeMillis();
            if((systemTimestamp-blockTimestamp) < 120000){
                LoggerUtils.logDebug(logger, "collectERC20Balance 当前机器时间戳与eth区块最新块的时间戳差不超过120秒！");
                this.collectERC20ETHBalance();
                this.collectERC20TokenBalance();
            }
            else {
                LoggerUtils.logError(logger, "collectERC20Balance 当前机器时间戳与eth区块最新块的时间戳超过120秒！");
            }
        }
    }

    private void collectERC20ETHBalance() throws BusinessException
    {
        SystemWalletAddrERC20 systemWalletAddrERC20Select = new SystemWalletAddrERC20();
        systemWalletAddrERC20Select.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
        List<SystemWalletAddrERC20> listSystemWalletAddrERC20 = systemWalletAddrERC20Service.findList(systemWalletAddrERC20Select);
        if(null != listSystemWalletAddrERC20 && listSystemWalletAddrERC20.size() > 0)
        {
            StockInfo stockInfoSelect = new StockInfo();
            stockInfoSelect.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
            List<StockInfo> listStockInfo = stockInfoService.findList(stockInfoSelect);
            if(null != listStockInfo && listStockInfo.size() > 0)
            {
                LoggerUtils.logInfo(logger, "--------------------------------------------------ETH归集处理开始！");
                // 基于账户erc20充值地址 大循环
                for (int i = 0; i < listSystemWalletAddrERC20.size(); i++)
                {
                    LoggerUtils.logInfo(logger, "账户ID:" + listSystemWalletAddrERC20.get(i).getAccountId() + "归集处理开始！");
                    // 基于erc20Token列表 小循环
                    StockInfo stockInfo = null;
                    for (int j = 0; j < listStockInfo.size(); j++)
                    {
                        // 两个循环变量 start
                        stockInfo = listStockInfo.get(j);
                        SystemWalletAddrERC20 systemWalletAddrERC20 = listSystemWalletAddrERC20.get(i);
                        // 两个循环变量 end

                        // eth归集 start -----------------------------------------------------------------------------------------------------------------
                        if(stockInfo.getStockCode().equals("ETH")){
                            // eth归集   eth归集到提现冷钱包地址上
                            BitpayKeychainERC20 BitpayKeychainERC20Select = new BitpayKeychainERC20();
                            BitpayKeychainERC20Select.setStockinfoId(String.valueOf(FundConsts.WALLET_ETH_TYPE));
                            BitpayKeychainERC20Select.setWalletType(2);//付款冷钱包
                            List<BitpayKeychainERC20> listBitpayKeychainERC20Cold = bitpayKeychainERC20Service.findList(BitpayKeychainERC20Select);
                            if(null != listBitpayKeychainERC20Cold && listBitpayKeychainERC20Cold.size() > 0){
                                // 获取ETH余额
                                BigDecimal ethBalance = ethLocalService.eth_getBalance(systemWalletAddrERC20.getWalletAddr(),"latest");
                                BigDecimal ethPendingBalance = ethLocalService.eth_getBalance(systemWalletAddrERC20.getWalletAddr(),"pending");
                                LoggerUtils.logInfo(logger, "eth余额ethBalance为:" + ethBalance + "; ethPendingBalance:" + ethPendingBalance + "; eth SDF:" + stockInfo.getSmallDepositFeeValue());
                                // ethBalance >= sdf && ethPendingBalance >= sdf
                                if(null != ethBalance && ethBalance.compareTo(stockInfo.getSmallDepositFeeValue()) >= 0 && ethPendingBalance.compareTo(stockInfo.getSmallDepositFeeValue()) >= 0){
                                    Long gasPrice = ConversionUtils.fromHexString(ethLocalService.eth_gasPrice().substring(2)) ;
                                    BigDecimal gas = BigDecimal.valueOf(Double.valueOf(gasPrice) * GAS_AMOUT).divide(BigDecimal.valueOf(Math.pow(10,18)));
                                    LoggerUtils.logInfo(logger, "eth归集手续费:" + gas.doubleValue());
                                    LoggerUtils.logInfo(logger, "eth归集金额为:" + ethBalance.subtract(gas));
                                    // eth从账户充值地址中归集到eth冷钱包支付地址上去(手续费gas)
                                    // String eth_sendTransaction(String fromEthAddress, String fromEthAddressPwd, String toEthAddress, BigDecimal transactionAmount);
                                    ethLocalService.eth_sendTransaction(systemWalletAddrERC20.getWalletAddr(), EncryptUtils.desDecrypt(systemWalletAddrERC20.getWalletPwd()), listBitpayKeychainERC20Cold.get(0).getWalletId(), ethBalance.subtract(gas));
                                }
                            }
                        }
                        // eth归集 end -----------------------------------------------------------------------------------------------------------------
                    }
                    LoggerUtils.logInfo(logger, "账户ID:" + listSystemWalletAddrERC20.get(i).getAccountId() + "归集处理结束！");
                    LoggerUtils.logInfo(logger, "ETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETHETH！");
                }
                LoggerUtils.logInfo(logger, "--------------------------------------------------ETH归集处理结束！");
            }
        }
    }

    private void collectERC20TokenBalance() throws BusinessException
    {
        SystemWalletAddrERC20 systemWalletAddrERC20Select = new SystemWalletAddrERC20();
        systemWalletAddrERC20Select.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
        List<SystemWalletAddrERC20> listSystemWalletAddrERC20 = systemWalletAddrERC20Service.findList(systemWalletAddrERC20Select);
        if(null != listSystemWalletAddrERC20 && listSystemWalletAddrERC20.size() > 0) {
            StockInfo stockInfoSelect = new StockInfo();
            stockInfoSelect.setStockType(FundConsts.STOCKTYPE_ERC20_TOKEN);
            List<StockInfo> listStockInfo = stockInfoService.findList(stockInfoSelect);
            if (null != listStockInfo && listStockInfo.size() > 0) {
                LoggerUtils.logInfo(logger, "--------------------------------------------------TOKEN归集处理开始！");
                // 基于账户erc20充值地址 大循环
                for (int i = 0; i < listSystemWalletAddrERC20.size(); i++) {
                    LoggerUtils.logInfo(logger, "账户ID:" + listSystemWalletAddrERC20.get(i).getAccountId() + "归集处理开始！");
                    // 基于erc20Token列表 小循环
                    StockInfo stockInfo = null;
                    for (int j = 0; j < listStockInfo.size(); j++) {
                        // 两个循环变量 start
                        stockInfo = listStockInfo.get(j);
                        SystemWalletAddrERC20 systemWalletAddrERC20 = listSystemWalletAddrERC20.get(i);
                        // 两个循环变量 end

                        // token归集 start ---------------------------------------------------------------------------------------------------------------
                        if (!stockInfo.getStockCode().equals("ETH") && StringUtils.isNotBlank(stockInfo.getTokenContactAddr()))
                        {
                            LoggerUtils.logInfo(logger, "证券token:" + stockInfo.toString() + "归集处理开始！");
                            // token归集
                            BitpayKeychainERC20 bitpayKeychainERC20Select = new BitpayKeychainERC20();
                            bitpayKeychainERC20Select.setStockinfoId(String.valueOf(FundConsts.WALLET_ETH_TYPE));
                            bitpayKeychainERC20Select.setWalletType(3); // 归集eth费用钱包
                            List<BitpayKeychainERC20> listBitpayKeychainERC20Collect = bitpayKeychainERC20Service.findList(bitpayKeychainERC20Select);
                            if (null != listBitpayKeychainERC20Collect && listBitpayKeychainERC20Collect.size() > 0)
                            {
                                // 获取充值账户 token余额
                                BigDecimal tokenBalance = erc20TokenLocalService.erc20_balanceOf(systemWalletAddrERC20.getWalletAddr(), stockInfo.getTokenContactAddr(), "latest");
                                // 获取充值账户 pending token余额
                                BigDecimal tokenPendingBalance = erc20TokenLocalService.erc20_balanceOf(systemWalletAddrERC20.getWalletAddr(), stockInfo.getTokenContactAddr(), "pending");
                                // 非空并且含有一定的eth费用好授权或者归集
                                LoggerUtils.logInfo(logger, "token余额tokenBalance:" + tokenBalance + "; tokenPendingBalance:" + tokenPendingBalance + "; token SDF:" + stockInfo.getSmallDepositFeeValue());
                                // SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
                                if (null != tokenBalance && tokenBalance.compareTo(stockInfo.getSmallDepositFeeValue()) >= 0 &&
                                        null != tokenPendingBalance && tokenPendingBalance.compareTo(stockInfo.getSmallDepositFeeValue()) >= 0)
                                {
                                    LoggerUtils.logInfo(logger, "--------------------------------------------------Token 归集处理开始！");

                                    // 获取allowanceBalance
                                    // BigDecimal erc20_allowance(String ownerAddr, String tokenContactAdddr, String feeAddress);
                                    BigDecimal allowanceBalance = erc20TokenLocalService.erc20_allowance(systemWalletAddrERC20.getWalletAddr(), stockInfo.getTokenContactAddr(), listBitpayKeychainERC20Collect.get(0).getWalletId(), "latest");
                                    BigDecimal allowancePendingBalance = erc20TokenLocalService.erc20_allowance(systemWalletAddrERC20.getWalletAddr(), stockInfo.getTokenContactAddr(), listBitpayKeychainERC20Collect.get(0).getWalletId(), "pending");
                                    LoggerUtils.logInfo(logger, "tokenBalance: " + tokenBalance + "; allowanceBalance:" + allowanceBalance + "; allowancePendingBalance:" + allowancePendingBalance);
                                    // NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
                                    // 允许操作量 小于 token余数  并且 pengding状态允许操作量 也小于 token余数
                                    if (allowanceBalance.compareTo(tokenBalance) < 0 && allowancePendingBalance.compareTo(tokenBalance) < 0)
                                    {
                                        LoggerUtils.logInfo(logger, "允许操作量 小于 token余数  并且 pengding状态允许操作量 也小于 token余数！");
                                        LoggerUtils.logInfo(logger, "allowanceBalance不足重新授权,该地址为:" + systemWalletAddrERC20.getWalletAddr());
                                        LoggerUtils.logInfo(logger, "allowanceBalance不足重新授权,目前tokenBalance量:" + tokenBalance);
                                        LoggerUtils.logInfo(logger, "allowanceBalance不足重新授权,目前allowanceBalance剩余授权量:" + allowanceBalance);
                                        LoggerUtils.logInfo(logger, "allowanceBalance不足重新授权,目前allowancePendingBalance剩余授权量:" + allowancePendingBalance);
                                        Long gasPrice = ConversionUtils.fromHexString(ethLocalService.eth_gasPrice().substring(2));
                                        BigDecimal gas = BigDecimal.valueOf(Double.valueOf(gasPrice) * GAS_AMOUT).divide(BigDecimal.valueOf(Math.pow(10, 18)));
                                        LoggerUtils.logInfo(logger, "重新授权预估手续费:" + gas);
                                        //
                                        // 获取充值账户 ETH余额
                                        BigDecimal chargeAccountEthBalance = ethLocalService.eth_getBalance(systemWalletAddrERC20.getWalletAddr(), "latest");
                                        LoggerUtils.logInfo(logger, " 获取充值账户 ETH余额chargeAccountEthBalance:" + chargeAccountEthBalance);
                                        //
                                        // 充值账户里面的eth余额小于需要授权的手续费
                                        if (null != chargeAccountEthBalance && chargeAccountEthBalance.compareTo(gas) < 0)
                                        {
                                            LoggerUtils.logInfo(logger, "充值账户里面的eth余额小于需要授权的手续费,chargeAccountEthBalance:" + chargeAccountEthBalance);
                                            // 获取pending的ETH余额
                                            BigDecimal chargeAccountPendingEthBalance = ethLocalService.eth_getBalance(systemWalletAddrERC20.getWalletAddr(), "pending");
                                            LoggerUtils.logInfo(logger, "chargeAccountPendingEthBalance:" + chargeAccountPendingEthBalance);
                                            if (null != chargeAccountPendingEthBalance && chargeAccountPendingEthBalance.compareTo(gas) >= 0)
                                            {
                                                // pending中 直接过滤 继续循环
                                                continue;
                                            }
                                            else
                                            {
                                                // 获取归集eth费用钱包 ETH余额
                                                BigDecimal feeAccountEthBalance = ethLocalService.eth_getBalance(listBitpayKeychainERC20Collect.get(0).getWalletId(), "latest");
                                                if (null != feeAccountEthBalance && feeAccountEthBalance.compareTo(BigDecimal.valueOf(0.01d)) > 0)
                                                {
                                                    // 从费用账户从转少量eth手续费给充值账户
                                                    // String eth_sendTransaction(String fromEthAddress, String fromEthAddressPwd, String toEthAddress, BigDecimal transactionAmount);
                                                    if(gas.compareTo(BigDecimal.valueOf(0.005d)) < 0){
                                                        LoggerUtils.logDebug(logger, "从费用账户从转少量eth手续费给充值账户 gas小于eth对应的sdf(0.005)!");
                                                        ethLocalService.eth_sendTransaction(listBitpayKeychainERC20Collect.get(0).getWalletId(), EncryptUtils.desDecrypt(listBitpayKeychainERC20Collect.get(0).getWalletPwd()), systemWalletAddrERC20.getWalletAddr(), gas);
                                                        continue;
                                                    } else {
                                                        LoggerUtils.logDebug(logger, "从费用账户从转少量eth手续费给充值账户 gas大于eth对应的sdf(0.005)!");
                                                        // gas 默认值为0.005个eth
                                                        gas = BigDecimal.valueOf(0.005d);
                                                        ethLocalService.eth_sendTransaction(listBitpayKeychainERC20Collect.get(0).getWalletId(), EncryptUtils.desDecrypt(listBitpayKeychainERC20Collect.get(0).getWalletPwd()), systemWalletAddrERC20.getWalletAddr(), gas);
                                                        continue;
                                                    }
                                                }
                                            }
                                        }
                                        // 充值账户里面的eth余额大于等于需要授权的手续费
                                        else if (null != chargeAccountEthBalance && chargeAccountEthBalance.compareTo(gas) >= 0)
                                        {
                                            LoggerUtils.logInfo(logger, "充值账户里面的eth余额大于等于需要授权的手续费,chargeAccountEthBalance:" + chargeAccountEthBalance);
                                            // 再充值账户授权给费用账户(费用账户就是热钱包地址)
                                            LoggerUtils.logInfo(logger, "充值账户授权给费用账户:" + systemWalletAddrERC20.getWalletAddr());
                                            LoggerUtils.logInfo(logger, "充值账户授权给费用账户:" + listBitpayKeychainERC20Collect.get(0).getWalletId() + "授权量:" + BigDecimal.valueOf(APPROVE_AMOUT));
                                            // String erc20_approve(String tokenContactAdddr, String chargeAddress, String chargeAddressPwd, String feeAddress, BigDecimal amount);
                                            erc20TokenLocalService.erc20_approve(stockInfo.getTokenContactAddr(), systemWalletAddrERC20.getWalletAddr(), EncryptUtils.desDecrypt(systemWalletAddrERC20.getWalletPwd()),
                                                    listBitpayKeychainERC20Collect.get(0).getWalletId(), BigDecimal.valueOf(APPROVE_AMOUT));
                                            // 充值账户加锁
                                            ethLocalService.personal_lockAccount(systemWalletAddrERC20.getWalletAddr());
                                            continue;
                                        }
                                    }
                                    // NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
                                    // 允许操作量 小于 token余数  并且 pengding状态允许操作量 大于等于 token余数
                                    else if (allowanceBalance.compareTo(tokenBalance) < 0 && allowancePendingBalance.compareTo(tokenBalance) >= 0)
                                    {
                                        // pengding状态允许操作量 小于 token余数
                                        LoggerUtils.logInfo(logger, "允许操作量 小于 token余数  并且 pengding状态允许操作量 大于等于 token余数，直接跳过进行下个循环！");
                                        continue;
                                    }
                                    // NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
                                    // 允许操作量 大于等于 token余数
                                    else if (allowanceBalance.compareTo(tokenBalance) >= 0)
                                    {
                                        LoggerUtils.logInfo(logger, "允许操作量 大于等于 token余数！");
                                        bitpayKeychainERC20Select = new BitpayKeychainERC20();
                                        bitpayKeychainERC20Select.setStockinfoId(String.valueOf(FundConsts.WALLET_ETH_TYPE));
                                        bitpayKeychainERC20Select.setWalletType(2);//付款冷钱包
                                        List<BitpayKeychainERC20> listBitpayKeychainERC20Cold = bitpayKeychainERC20Service.findList(bitpayKeychainERC20Select);
                                        if (null != listBitpayKeychainERC20Cold && listBitpayKeychainERC20Cold.size() > 0)
                                        {
                                            // token从账户充值地址中归集到token冷钱包支付地址上去(手续费有)
                                            // 先解锁费用账户地址
                                            ethLocalService.personal_unlockAccount(listBitpayKeychainERC20Collect.get(0).getWalletId(), EncryptUtils.desDecrypt(listBitpayKeychainERC20Collect.get(0).getWalletPwd()));
                                            // token归集
                                            LoggerUtils.logInfo(logger, "token:" + stockInfo.getTokenContactAddr() + ";归集金额为:" + tokenBalance);
                                            // String erc20_transferFrom(String tokenContactAdddr, String chargeAddress, String feeAddress, String feeAddressPwd, String collectAddress, BigDecimal amount);
                                            erc20TokenLocalService.erc20_transferFrom(stockInfo.getTokenContactAddr(), systemWalletAddrERC20.getWalletAddr(), listBitpayKeychainERC20Collect.get(0).getWalletId(), EncryptUtils.desDecrypt(listBitpayKeychainERC20Collect.get(0).getWalletPwd()),
                                                    listBitpayKeychainERC20Cold.get(0).getWalletId(), tokenBalance);
                                            // 费用账户加锁
                                            ethLocalService.personal_lockAccount(listBitpayKeychainERC20Collect.get(0).getWalletId());
                                            continue;
                                        }
                                    }
                                    // NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
                                    LoggerUtils.logInfo(logger, "--------------------------------------------------Token 归集处理结束！");
                                }
                                // SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
                            }
                            // token归集 end ---------------------------------------------------------------------------------------------------------------
                        }
                    }
                    LoggerUtils.logInfo(logger, "账户ID:" + listSystemWalletAddrERC20.get(i).getAccountId() + "归集处理结束！");
                    LoggerUtils.logInfo(logger, "TOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKEN！");
                }
                LoggerUtils.logInfo(logger, "--------------------------------------------------TOKEN归集处理结束！");
            }
        }

    }

    @Override
    public BlockInfoERC20 getLastUnScanTransBlockInfoErc20() throws BusinessException
    {
        // 从数据库中获取最后一次待交易扫描的区块编号
        BlockInfoERC20 blockInfoERC20 = blockInfoERC20Service.getLastUnScanTransBlockNumber();
        if (null != blockInfoERC20)
        {
            return blockInfoERC20;
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public Set<String> getAccountInterAddrsByParams(String ... extAddress) throws BusinessException
    {
        // 从数据库中获取所有erc20充值地址
        return systemWalletAddrERC20Service.getAccountInterAddrsByParams(extAddress);
    }
    
    @Override
    public List<Transaction> getTransactionsByBlockNumber(Long blockNumber) throws BusinessException
    {
        List<Transaction> data = Lists.newArrayList();
        EthBlock.Block block = EthereumUtils.ethGetBlockByNumber(BigInteger.valueOf(blockNumber), true);
        List<EthBlock.TransactionResult> transactions = block.getTransactions();
        if (null != transactions && transactions.size() > 0)
        {
            for (EthBlock.TransactionResult transaction : transactions)
            {
                data.add((Transaction) transaction.get());
            }
        }
        return data;
    }

    /**
     * 查询Erc20Token中最低块高度信息的列表
     * @return
     */
    @Override
    public List<Erc20Token> findListMinHeightErc20Token()
    {
        return erc20TokenService.findMinHeight();
    }

    /**
     * 查询EthAddrs中最低块高度信息的列表
     * @return
     */
    @Override
    public List<EthAddrs> findListMinHeightEthAddrs()
    {
        return ethAddrsService.findMinHeight();
    }

    public static void main(String[] args) {
        System.out.println("1234567891010".substring(0,10));
    }
}
