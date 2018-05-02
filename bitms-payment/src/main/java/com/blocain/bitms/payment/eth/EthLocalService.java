package com.blocain.bitms.payment.eth;

import com.blocain.bitms.payment.eth.model.ErcTokenResultModel;

import java.math.BigDecimal;

/**
 *  ETH接口
 * <p>File：EthLocalService.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-02-26 13:07:06</p>
 * <p>Company: BloCain</p>
 * @author 张春喜
 * @version 1.0
 */
public interface EthLocalService
{
    /**
     * 创建账户
     * 传入参数示例：{"method": "personal_newAccount", "params": ["123456"],"id":1}
     * @return  账户地址
     */
    String personal_newAccount(String passWord);
    
    /**
     * 锁定用户
     * 传入参数示例：{"method": "personal_lockAccount", "params": ["0x419abcd98e8b9182d07ff270a1502587887bb650"],"id":1}
     * @param accountEthAddress
     * @return result [true|false]
     */
    Boolean personal_lockAccount(String accountEthAddress);
    
    /**
     * 解锁用户
     * 传入参数示例：{"method": "personal_unlockAccount", "params": ["0x419abcd98e8b9182d07ff270a1502587887bb650", "123456", 3000],"id":1}
     * @param accountEthAddress
     * @param  passWord
     * @return result [true|false]
     */
    Boolean personal_unlockAccount(String accountEthAddress,String passWord);
    
    /**
     * 获取账户列表
     * 传入参数示例：{"method": "personal_listAccounts", "params": [],"id":1}
     * @return
     */
    String[] personal_listAccounts();
    
    /**
     * 使用指定帐户签名要发送的数据，帐户需要处于unlocked状态
     * 传入参数示例：{"method": "personal_sign", "params": ["BIEX", "0x419abcd98e8b9182d07ff270a1502587887bb650", "123456"],"id":1}
     * @return
     */
    String personal_sign(String str16,String tokenAddress,String tokenPwd);
    
    /**
     * 获得在指定区块时给定地址的余额。
     * 参数：
     * String - 要查询余额的地址。
     * Number|String -（可选）如果不设置此值使用web3.eth.defaultBlock设定的块，否则使用指定的块。
     * Funciton - （可选）回调函数，用于支持异步的方式执行7。
     * 返回值：String - 一个包含给定地址的当前余额的BigNumber实例，单位为wei
     * @param status pending latest earliest
     * @return ETH单位
     */
    BigDecimal eth_getBalance(String accountEthAddress, String status);
    
    /**
     * 返回当前的gas价格。这个值由最近几个块的gas价格的中值6决定。
     返回值：
     BigNumber - 当前的gas价格的BigNumber实例，以wei为单位。
     * @return 16进制
     */
    String eth_gasPrice();
    
    /**
     * 在节点的VM节点中执行一个消息调用，或交易。但是不会合入区块链中。返回使用的gas量。
     参数：
     同web3.eth.sendTransaction，所有的属性都是可选的。
     返回值：
     Number - 模拟的call/transcation花费的gas。
     * @param params
     * @return
     */
    String eth_estimateGas(Object[] params);

    /**
     * 使用指定帐户签名要发送的数据，帐户需要处于unlocked状态。
     参数：
     String - 签名使用的地址
     String - 要签名的数据
     Function -（可选）回调函数，用于支持异步的方式执行7。
     返回值：
     String - 签名后的数据。
     *
     * @return
     */
    String eth_sign(String str16,String tokenAddress,String tokenPwd);

    /**
     * 发送一个交易到网络。 // 注意 ：传入单位为 ETH数量
     参数：
     Object - 要发送的交易对象。
     from: String - 指定的发送者的地址。如果不指定，使用web3.eth.defaultAccount。
     to: String - （可选）交易消息的目标地址，如果是合约创建，则不填.
     value: Number|String|BigNumber - （可选）交易携带的货币量，以wei为单位。如果合约创建交易，则为初始的基金。
     gas: Number|String|BigNumber - （可选）默认是自动，交易可使用的gas，未使用的gas会退回。
     gasPrice: Number|String|BigNumber - （可选）默认是自动确定，交易的gas价格，默认是网络gas价格的平均值 。
     data: String - （可选）或者包含相关数据的字节字符串，如果是合约创建，则是初始化要用到的代码。
     nonce: Number - （可选）整数，使用此值，可以允许你覆盖你自己的相同nonce的，正在pending中的交易11。
     Function - 回调函数，用于支持异步的方式执行7。
     返回值：
     String - 32字节的交易哈希串。用16进制表示。
     如果交易是一个合约创建，请使用web3.eth.getTransactionReceipt()在交易完成后获取合约的地址。
     * @return
     */
    String eth_sendTransaction(String fromEthAddress, String fromEthAddressPwd, String toEthAddress, BigDecimal transactionAmount);

    /**
     * 根据参数返回eth发送签名交易的待签名交易数据(ImToken钱包冷签)
     * @param
     * @return
     */
    String eth_getImTokenUnsignTransactionData(String fromEthAddress, String toEthAddress, BigDecimal transactionAmount);

    /**
     * 发送签名交易数据进行交易
     * @param signedTransactionData
     * @return
     */
    String eth_sendRawTransaction(String signedTransactionData);
    
    /**
     * 根据ethAddress地址查询当前地址的交易总数（实际就是nonce数值）
     * @param ethAddress
     * @return
     */
    Long eth_getTransactionCount(String ethAddress);
    
    /**
    * (根据block的hash值以及交易Index索引查询具体某一笔交易详情(注意 一次只能查询一笔交易))
    * @param blockHash 16进制字符串
     * @param  index 16进制字符串
    * @return
    */
    ErcTokenResultModel eth_getTransactionByBlockHashAndIndex(String blockHash, String index);
    
    /**
     * 根据交易hash获取交易详情
     * @param txHash 16进制字符串
     * @return
     */
    ErcTokenResultModel eth_getTransactionByHash(String txHash);

    /**
     * 参数：
     String - 传入的需要使用Keccak-256 SHA3算法进行哈希运算的字符串。
     Object - 可选项设置。如果要解析的是hex格式的十六进制字符串。需要设置encoding为hex。因为JS中会默认忽略0x。
     返回值：
     String - 使用Keccak-256 SHA3算法哈希过的结果。
     * @param str16 16进制
     * @return
     */
    String eth_web3_sha3(String str16);

}
