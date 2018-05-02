package com.blocain.bitms.payment.eth;

import java.math.BigDecimal;

/**
 * ERC20 TOKEN 接口
 * <p>File：Erc20TokenLocalService.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-02-27 10:58:00</p>
 * <p>Company: BloCain</p>
 * @author 张春喜
 * @version 1.0
 */
public interface Erc20TokenLocalService
{
    /**
     * 参数：
     String - 传入的需要使用Keccak-256 SHA3算法进行哈希运算的字符串。
     Object - 可选项设置。如果要解析的是hex格式的十六进制字符串。需要设置encoding为hex。因为JS中会默认忽略0x。
     返回值：
     String - 使用Keccak-256 SHA3算法哈希过的结果。
     * @param str16
     * @return
     */
    String erc20_web3_sha3(String str16);

    /**
     * 查询token的name
     * @return
     */
    String erc20_name(String tokenContactAdddr);

    /**
     * 查询token的symbol
     * @return
     */
    String erc20_symbol(String tokenContactAdddr);
    /**
     * 查询token小数精度decimals
     * @return
     */
    Long erc20_decimals(String tokenContactAdddr);
    
    /**
     * 查询token的发行量
     * @return
     */
    BigDecimal erc20_totalSupply(String tokenContactAdddr);
    
    /**
     * 查询账户token余额balanceOf
     * @param accountAddress 原始地址 0x000.....
     * @param tokenContactAdddr
     * @param status latest、pending
     * @return ETH数量
     */
    BigDecimal erc20_balanceOf(String accountAddress, String tokenContactAdddr, String status);
    
    /**
     * 查询token授权量allowance
     * @param ownerAddr  token拥有者
     * @param feeAddress token被授权者
     * @param tokenContactAdddr
     * @param status pending latest earliest
     * @return ETH数量
     */
    BigDecimal erc20_allowance(String ownerAddr, String tokenContactAdddr, String feeAddress, String status);
    
    /**
     * token授权approve操作
     * @param tokenContactAdddr 合约tokenaddresss
     * @param chargeAddress
     * @param chargeAddressPwd
     * @param feeAddress
     * @param amount 授权数量
     * @return
     */
    String erc20_approve(String tokenContactAdddr, String chargeAddress, String chargeAddressPwd, String feeAddress, BigDecimal amount);
    
    /**
     * token授权approve操作
     * @param tokenContactAdddr 合约tokenaddresss
     * @param chargeAddress 充值地址
     * @param feeAddressPwd 充值地址密码
     * @param feeAddress 手续费地址
     * @param collectAddress 归集地址
     * @param amount ETH数量
     * @return
     */
    String erc20_transferFrom(String tokenContactAdddr, String chargeAddress, String feeAddress, String feeAddressPwd, String collectAddress, BigDecimal amount);
    
    /**
     * token授权approve操作
     * @param tokenContactAddr 合约tokenaddresss
     * @param formAddress 源头地址
     ** @param formAddressPwd 源头地址密码
     * @param destAddress 目标地址
     * @param amount ETH数量
     * @return
     */
    String erc20_transfer(String tokenContactAddr, String formAddress, String formAddressPwd, String destAddress, BigDecimal amount);
    
    /**
     * 根据参数返回erc20发送签名交易的待签名交易数据(ImToken钱包冷签)
     * @param
     * @return
     */
    String erc20_getImTokenUnsignTransactionData(String tokenContractAddress, String tokenSymbol, String fromEthAddress, String toEthAddress, BigDecimal transactionAmount);
    
    /**
     * 发送签名交易数据进行交易
     * @param signedTransactionData
     * @return
     */
    String erc20_sendRawTransaction(String signedTransactionData);
    
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
}
