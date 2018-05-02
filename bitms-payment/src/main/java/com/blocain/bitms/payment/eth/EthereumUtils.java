package com.blocain.bitms.payment.eth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;

import com.blocain.bitms.payment.basic.BasicServiceImpl;
import com.blocain.bitms.tools.utils.ConversionUtils;
import com.blocain.bitms.tools.utils.LoggerUtils;

/**
 * ETH公共服务类
 */
public class EthereumUtils extends BasicServiceImpl
{
    public static final Logger  logger            = LoggerFactory.getLogger(EthereumUtils.class);
    
    private static final String SPILT_ZERO_24     = "000000000000000000000000";
    
    private static String       SPPIT_ZREO_64     = "0000000000000000000000000000000000000000000000000000000000000000";
    
    private static Admin        web3              = Admin.build(new HttpService(propertiesErc20.getProperty("erc20token.root.url")));
    
    // decimals()
    private static String       SHA3_DECIMALS     = "0x313ce567";
    
    // balanceOf(uint256)
    private static String       SHA3_BALANCEOF    = "0x70a08231";
    
    // allowance(address,address)
    private static String       SHA3_ALLOWWANCE   = "0xdd62ed3e";
    
    // approve(address,uint256)
    private static String       SHA3_APPROVE      = "0x095ea7b3";
    
    // transferFrom(address,address,uint256)
    private static String       SHA3_TRANSFERFROM = "0x23b872dd";
    
    // transfer(address,uint256)
    private static String       SHA3_TRANSFER     = "0xa9059cbb";
    
    /**
     * 取ETH节点信息
     *
     * @return
     */
    public static String getWeb3ClientVersion()
    {
        Web3ClientVersion web3ClientVersion = null;
        try
        {
            web3ClientVersion = web3.web3ClientVersion().send();
            return web3ClientVersion.getWeb3ClientVersion();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "取ETH节点信息失败：{}", web3ClientVersion.getError().getMessage());
        }
        return null;
    }
    
    /**
     * sha3
     *
     * @param str 字符串
     * @return
     */
    public static String web3sha3(String str)
    {
        Web3Sha3 web3Sha3 = null;
        try
        {
            web3Sha3 = web3.web3Sha3("0x" + ConversionUtils.toHexString(str)).send();
            return web3Sha3.getResult();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "查询合约SHA3失败：{}", web3Sha3.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 创建钱包地址
     *
     * @param password
     * @return
     */
    public static String newAccount(String password)
    {
        NewAccountIdentifier identifier = null;
        try
        {
            identifier = web3.personalNewAccount(password).send();
            return identifier.getAccountId();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "创建钱包地址失败：{}", identifier.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 解锁帐户
     *
     * @param account  帐户
     * @param password 密码
     * @param duration 解锁时间
     * @return {@link Boolean}
     */
    public static boolean unlockAccount(String account, String password, BigInteger duration)
    {
        PersonalUnlockAccount personalUnlockAccount = null;
        try
        {
            personalUnlockAccount = web3.personalUnlockAccount(account, password, duration).send();
            return personalUnlockAccount.accountUnlocked();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "解锁帐户{} 失败：{}", account, personalUnlockAccount.getError().getMessage());
        }
        return false;
    }
    
    /**
     * 获取账户列表
     *
     * @return
     */
    public static List<String> listAccount()
    {
        PersonalListAccounts personalListAccounts = null;
        try
        {
            personalListAccounts = web3.personalListAccounts().send();
            return personalListAccounts.getAccountIds();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "获取账户列表失败：{}", personalListAccounts.getError().getMessage());
            return null;
        }
    }
    
    /**
     * 获取燃料价格
     *
     * @return
     */
    public static BigInteger getEthGasPrice()
    {
        EthGasPrice ethGasPrice = null;
        try
        {
            ethGasPrice = web3.ethGasPrice().send();
            return ethGasPrice.getGasPrice();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "获取燃料价格失败：{}", ethGasPrice.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 获取预估费用
     *
     * @param transaction 交易对象（来自交易）
     * @return
     */
    public static BigInteger ethEstimateGas(Transaction transaction)
    {
        try
        {
            BigInteger amountUsed = web3.ethEstimateGas(transaction).send().getAmountUsed();
            return amountUsed;
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "查询EstimateGas失败：{}", e.getLocalizedMessage());
        }
        return null;
    }
    
    /**
     * 取ETH余额
     *
     * @param address  钱包地址
     * @return {@link BigInteger}
     */
    public static BigInteger getEthBalance(String address)
    {
        EthGetBalance ethGetBalance = null;
        try
        {
            ethGetBalance = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return ethGetBalance.getBalance();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "取ETH余额失败：{}", ethGetBalance.getError().getMessage());
            return null;
        }
    }
    
    /**
     * 发送ETH
     *
     * @param fromAddress    发送地址
     * @param fromAddressPwd 发送地址密码
     * @param toAddress      接收地址
     * @param amount         发送数量（单位：个ETH）
     * @return
     */
    public static String ethSendTransaction(String fromAddress, String fromAddressPwd, String toAddress, BigDecimal amount)
    {
        EthSendTransaction sendTransaction = null;
        try
        {
            BigInteger gasPrice = getEthGasPrice();
            if (unlockAccount(fromAddress, fromAddressPwd, BigInteger.valueOf(300000)))
            {
                Transaction transaction = Transaction.createEtherTransaction(fromAddress, null, gasPrice, BigInteger.valueOf(900000), toAddress,
                        (BigDecimal.valueOf(Math.pow(10, 18)).multiply(amount)).toBigInteger());
                BigInteger amountUsed = ethEstimateGas(transaction);
                Transaction transactionSend = Transaction.createEtherTransaction(fromAddress, null, gasPrice, amountUsed, toAddress,
                        (BigDecimal.valueOf(Math.pow(10, 18)).multiply(amount)).toBigInteger());
                sendTransaction = web3.ethSendTransaction(transactionSend).send();
                return sendTransaction.getTransactionHash();
            }
            return null;
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "发送ETH失败：{}", sendTransaction.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 获取区块最新高度
     *
     * @return
     */
    public static EthBlockNumber getBlockNumber()
    {
        EthBlockNumber ethBlockNumber = null;
        try
        {
            ethBlockNumber = web3.ethBlockNumber().send();
            return ethBlockNumber;
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "区块获取失败：{}", ethBlockNumber.getError().getMessage());
            return null;
        }
    }
    
    /**
     * 通过块hash值返回一个块信息
     *
     * @param hash  块HASH值
     * @param isAll 如果是true，则返回完整的交易对象，如果false仅仅是交易的HASH。
     * @return
     */
    public static EthBlock.Block ethGetBlockByHash(String hash, boolean isAll)
    {
        EthBlock block = null;
        try
        {
            block = web3.ethGetBlockByHash(hash, isAll).send();
            return block.getBlock();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "通过块hash值返回一个块信息失败：{}", block.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 用块数返回一个块的信息。
     *
     * @param isAll 如果是true，则返回完整的交易对象，如果false仅仅是交易的HASH。
     * @return
     */
    public static EthBlock.Block ethGetBlockByNumber(BigInteger blockNumber, boolean isAll)
    {
        EthBlock block = null;
        try
        {
            block = web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), isAll).send();
            return block.getBlock();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "用块数返回一个块的信息失败：{}", block.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 根据block的hash值查询交易总数
     *
     * @param blockHash 块的hash
     * @return
     */
    public static BigInteger ethGetTransactionCount(String blockHash)
    {
        EthGetTransactionCount ethGetTransactionCount = null;
        try
        {
            ethGetTransactionCount = web3.ethGetTransactionCount(blockHash, DefaultBlockParameterName.LATEST).send();
            return ethGetTransactionCount.getTransactionCount();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "根据block的hash值查询交易总数失败：{}", ethGetTransactionCount.getError().getMessage());
        }
        return null;
    }
    
    /**
     * (根据block的hash值以及交易Index索引查询具体某一笔交易详情(注意 一次只能查询一笔交易))
     *
     * @param blockHash bcock的hash
     * @param index     块索引
     * @return
     */
    public static org.web3j.protocol.core.methods.response.Transaction getTransactionByBlockHashAndIndex(String blockHash, BigInteger index)
    {
        EthTransaction transaction = null;
        try
        {
            transaction = web3.ethGetTransactionByBlockHashAndIndex(blockHash, index).send();
            return transaction.getTransaction().get();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "根据block的hash值以及交易Index索引查询具体某一笔交易详情失败：{}", transaction.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 根据交易hash获取交易详情
     *
     * @param hash 交易HASH
     * @return
     */
    public static org.web3j.protocol.core.methods.response.Transaction ethGetTransactionByHash(String hash)
    {
        EthTransaction transaction = null;
        try
        {
            transaction = web3.ethGetTransactionByHash(hash).send();
            return transaction.getResult();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "根据交易hash获取交易详情失败：{}", transaction.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 通过交易散列返回一个交易的接收
     *
     * @param hash 交易HASH值
     * @return
     */
    public static TransactionReceipt ethGetTransactionReceipt(String hash)
    {
        EthGetTransactionReceipt receipt = null;
        try
        {
            receipt = web3.ethGetTransactionReceipt(hash).send();
            return receipt.getTransactionReceipt().get();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "通过交易散列返回一个交易的接收失败：{}", receipt.getError().getMessage());
        }
        return null;
    }
    
    /**
     * sign
     *
     * @param address    地址
     * @param sha3String msg to sign 16进制
     * @return
     */
    public static String ethSign(String address, String sha3String)
    {
        EthSign ethSign = null;
        try
        {
            ethSign = web3.ethSign(address, sha3String).send();
            return ethSign.getSignature();
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "合约SIGN失败：{}", ethSign.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 获取token的精度
     *
     * @param tokenAddress token的合约地址
     * @return {@link Long}
     */
    public static Long tokenDecimals(String tokenAddress)
    {
        EthCall ethCall = null;
        try
        {
            Transaction transaction = Transaction.createEthCallTransaction(null, tokenAddress, SHA3_DECIMALS);
            ethCall = web3.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            return ConversionUtils.fromHexString(ethCall.getResult().substring(2));
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "查询合约精度失败：{}", ethCall.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 查询账户token余额balanceOf
     *
     * @param tokenAddress token的合约地址
     * @param fromAddress  查询地址
     * @return 币种的余额(单位 ： 个币)
     */
    public static BigDecimal tokenBalanceOf(String tokenAddress, String fromAddress)
    {
        EthCall ethCall = null;
        try
        {
            StringBuilder data = new StringBuilder(SHA3_BALANCEOF).append(SPILT_ZERO_24).append(fromAddress.substring(2));
            Transaction transaction = Transaction.createEthCallTransaction(null, tokenAddress, data.toString());
            ethCall = web3.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            BigDecimal ret = hexToBigDecimal(ethCall.getResult().substring(2));
            return ret;
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "查询合约余额失败：{}", ethCall.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 查询token授权量allowance
     *
     * @param tokenOwner        token 拥有者
     * @param toAddress         token被授权者
     * @param tokenContactAdddr token合约地址
     * @return
     */
    public static BigDecimal tokenAllowance(String tokenOwner, String tokenContactAdddr, String toAddress)
    {
        EthCall ethCall = null;
        try
        {
            StringBuilder data = new StringBuilder(SHA3_ALLOWWANCE).append(SPILT_ZERO_24).append(tokenOwner.substring(2)).append(SPILT_ZERO_24)
                    .append(toAddress.substring(2));
            Transaction transaction = Transaction.createEthCallTransaction(tokenOwner, tokenContactAdddr, data.toString());
            ethCall = web3.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            BigDecimal ret = hexToBigDecimal(ethCall.getResult().substring(2));
            return ret;
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "查询合约授权量失败：{}", ethCall.getError().getMessage());
        }
        return null;
    }
    
    /**
     * TOKEN授权APPROVE
     *
     * @param tokenAddress     token合约地址
     * @param chargeAddress    充值地址
     * @param chargeAddressPwd 充值地址密码
     * @param feeAddress       手续费地址
     * @param amount           授权数量(个BIEX)
     * @return
     */
    public static String tokenApprove(String tokenAddress, String chargeAddress, String chargeAddressPwd, String feeAddress, BigInteger amount)
    {
        EthSendTransaction sendTransaction = null;
        try
        {
            if (unlockAccount(chargeAddress, chargeAddressPwd, BigInteger.valueOf(3000)))
            {
                String hexAmount = ConversionUtils.toHexString(amount);
                BigInteger ethGasPrice = getEthGasPrice();
                StringBuilder data = new StringBuilder(SHA3_APPROVE).append(SPILT_ZERO_24).append(feeAddress.substring(2)).append(SPILT_ZERO_24)
                        .append(SPPIT_ZREO_64.substring(0, SPPIT_ZREO_64.length() - hexAmount.length())).append(hexAmount);
                Transaction transaction = Transaction.createFunctionCallTransaction(chargeAddress, null, ethGasPrice, BigInteger.valueOf(60000), tokenAddress,
                        BigInteger.ZERO, data.toString());
                sendTransaction = web3.ethSendTransaction(transaction).send();
                return sendTransaction.getTransactionHash();
            }
            else
            {
                LoggerUtils.logError(logger, "TOKEN授权失败：{}", "用户解锁失败");
                return null;
            }
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "TOKEN授权失败：{}", sendTransaction.getError().getMessage());
        }
        return null;
    }
    
    /**
     * token授权后交易发送
     * @param tokenAddress   token合约地址
     * @param chargeAddress  充值地址
     * @param feeAddress     费用地址
     * @param feeAddressPwd  费用地址密码
     * @param collectAddress 归集地址
     * @param amount         交易数量（个BIEX）
     * @return
     */
    public static String tokenTransferFrom(String tokenAddress, String chargeAddress, String feeAddress, String feeAddressPwd, String collectAddress, BigInteger amount)
    {
        EthSendTransaction sendTransaction = null;
        try
        {
            if (unlockAccount(feeAddress, feeAddressPwd, BigInteger.valueOf(3000)))
            {
                BigInteger ethGasPrice = getEthGasPrice();
                String hexAmount = ConversionUtils.toHexString(amount);
                StringBuilder data = new StringBuilder(SHA3_TRANSFERFROM).append(SPILT_ZERO_24).append(chargeAddress.substring(2)).append(SPILT_ZERO_24)
                        .append(collectAddress.substring(2)).append(SPPIT_ZREO_64.substring(0, SPPIT_ZREO_64.length() - hexAmount.length())).append(hexAmount);
                Transaction transaction = Transaction.createFunctionCallTransaction(feeAddress, null, ethGasPrice, BigInteger.valueOf(60000), tokenAddress, BigInteger.ZERO,
                        data.toString());
                sendTransaction = web3.ethSendTransaction(transaction).send();
                return sendTransaction.getTransactionHash();
            }
            else
            {
                LoggerUtils.logError(logger, "token授权后交易发送失败：{}", "用户解锁失败");
                return null;
            }
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "token授权后交易发送失败：{}", sendTransaction.getError().getMessage());
        }
        return null;
    }
    
    /**
     * 16进制字符串转高精度数
     *
     * @param hexStr
     * @return
     */
    public static BigDecimal hexToBigDecimal(String hexStr)
    {
        if (hexStr.length() > 10)
        {
            BigDecimal high = BigDecimal.valueOf(Math.pow(16, 10)).multiply(BigDecimal.valueOf(ConversionUtils.fromHexString(hexStr.substring(0, hexStr.length() - 10))));
            BigDecimal low = BigDecimal.valueOf(ConversionUtils.fromHexString(hexStr.substring(hexStr.length() - 10)));
            return high.add(low);
        }
        else
        {
            return BigDecimal.valueOf(ConversionUtils.fromHexString(hexStr));
        }
    }
    
    public static void main(String[] args)
    {
        EthBlockNumber blockNumber = EthereumUtils.getBlockNumber();
        System.out.println(blockNumber.getBlockNumber());
        EthBlock.Block block = EthereumUtils.ethGetBlockByNumber(BigInteger.valueOf(3104054l),true);
        System.out.println(block.getHash());
    }
}
