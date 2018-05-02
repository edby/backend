package com.blocain.bitms.payment.eth.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.eth.model.ErcTokenRequestModel;
import com.blocain.bitms.payment.eth.model.ErcTokenResponceModel;
import com.blocain.bitms.payment.eth.model.ErcTokenResultModel;
import com.blocain.bitms.payment.eth.EthLocalService;
import com.blocain.bitms.payment.basic.BasicServiceImpl;
import com.blocain.bitms.tools.utils.ConversionUtils;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.MD5Util;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * ETH接口Impl
 * <p>File：EthLocalServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-02-27 10:59:05</p>
 * <p>Company: BloCain</p>
 * @author 张春喜
 * @version 1.0
 */
@Component("ethLocalService")
public class EthLocalServiceImpl extends BasicServiceImpl implements EthLocalService
{
    public static final Logger logger          = LoggerFactory.getLogger(EthLocalServiceImpl.class);

    // private static final String erc20token_root             =  BitmsConst.ETH_CLIENT_URL;

    // http://192.168.31.122:8545
    public static final String erc20token_root = propertiesErc20.getProperty("erc20token.root.url");
    
    /**
     * 封装POST接口 返回result字符串
     * @param model 例如：{"jsonrpc":"2.0","method":"web3_clientVersion","params":[],"id":67}
     * @return
     */
    protected String doPost(ErcTokenRequestModel model)
    {
        HttpClient client = HttpUtils.getHttpClient();
        JSONObject jsonParam = null;
        jsonParam = JSONObject.parseObject(JSON.toJSONString(model));// JSONObject.toJSON(model);
        logger.info("请求报文：" + jsonParam.toJSONString());
        String content = super.httpPostWithJSON(client, erc20token_root, null, jsonParam, null);
        logger.info("应答报文：" + content);
        return content;
    }
    
    @Override
    public String personal_newAccount(String passWord)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("personal_newAccount");
        Object param[]={passWord};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
    @Override
    public Boolean personal_lockAccount(String accountEthAddress)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("personal_lockAccount");
        Object lockAccount_param[]={accountEthAddress};
        model.setParams(lockAccount_param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getBoolean("result");
    }
    
    @Override
    public Boolean personal_unlockAccount(String accountEthAddress,String passWord)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("personal_unlockAccount");
        Object lockAccount_param[]={accountEthAddress, passWord, 10}; // 10秒 超时时间
        model.setParams(lockAccount_param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getBoolean("result");
    }

    private Boolean personal_unlockAccount(ErcTokenRequestModel model)
    {
        model.setMethod("personal_unlockAccount");
        String content = doPost(model);
        return JSONObject.parseObject(content).getBoolean("result");
    }
    
    @Override
    public String[] personal_listAccounts()
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        Object listAccountsParam[] = {};
        model.setMethod("personal_listAccounts");
        model.setParams(listAccountsParam);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        ErcTokenResponceModel responceModel = JSONObject.parseObject(content).toJavaObject(ErcTokenResponceModel.class);
        return responceModel.getResult();
    }
    
    @Override
    public String personal_sign(String str16,String tokenAddress,String tokenPwd)
    {
        // 先解锁
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param[] = {tokenAddress, tokenPwd, 10}; // 10秒
        unlockModel.setParams(param);
        unlockModel.setId(System.currentTimeMillis());
        boolean result = personal_unlockAccount(unlockModel);
        logger.info("unlock:" + result);
        // 再签名
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("personal_sign");
        Object param_sign[] = {str16,tokenAddress, tokenPwd};
        model.setParams(param_sign);
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }

    private String personal_sign(ErcTokenRequestModel model)
    {
        // 先解锁
        ErcTokenRequestModel unlockModel = model;
        Object oldparams[] = model.getParams();
        Object param[] = {model.getParams()[1], model.getParams()[2], 10}; // 10秒
        unlockModel.setParams(param);
        boolean result = personal_unlockAccount(unlockModel);
        logger.info("unlock:" + result);
        // 再签名
        model.setMethod("personal_sign");
        model.setParams(oldparams);
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }

    /**
     * 获得在指定区块时给定地址的余额。
     * 参数：
     * String - 要查询余额的地址。
     * Number|String -（可选）如果不设置此值使用web3.eth.defaultBlock设定的块，否则使用指定的块。
     * Funciton - （可选）回调函数，用于支持异步的方式执行7。
     * 返回值：String - 一个包含给定地址的当前余额的BigNumber实例，单位为wei。
     * @return
     */
    @Override
    public BigDecimal eth_getBalance(String accountEthAddress, String status)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_getBalance");
        Object getBalanceparam[] = {accountEthAddress, status};
        model.setParams(getBalanceparam);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        // 已经转换为ETH单位
        return hexToBigDecimal(JSONObject.parseObject(content).getString("result").substring(2)).divide(BigDecimal.valueOf(Math.pow(10,18)));
    }

    private BigDecimal hexToBigDecimal(String hexStr)
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

    @Override
    public String eth_gasPrice()
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_gasPrice");
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
        // ConversionUtils.fromHexString(JSONObject.parseObject(content).getString("result").substring(2));
    }

    @Override
    public String eth_estimateGas(Object[] params)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_estimateGas");
        model.setParams(params);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
        // return ConversionUtils.fromHexString(JSONObject.parseObject(content).getString("result").substring(2));
    }

    @Override
    public String eth_sign(String str16,String tokenAddress,String tokenPwd)
    {
        // 先解锁
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param[] = {tokenAddress, tokenPwd, 10}; //10秒
        unlockModel.setParams(param);
        unlockModel.setId(System.currentTimeMillis());
        boolean result = personal_unlockAccount(unlockModel);
        logger.info("unlock:" + result);
        // 再签名
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_sign");
        Object paramSign[] = {tokenAddress, str16};
        model.setParams(paramSign);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }

    @Override
    public String eth_sendTransaction(String fromEthAddress, String fromEthAddressPwd, String toEthAddress, BigDecimal transactionAmount)
    {
        // 先解锁
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param_unlock[] = {fromEthAddress, fromEthAddressPwd, 10}; //10秒
        unlockModel.setId(System.currentTimeMillis());
        unlockModel.setParams(param_unlock);
        boolean result_unlock = personal_unlockAccount(unlockModel);
        logger.info("unlock:" + result_unlock);
        // 组装发送交易报文
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_sendTransaction");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("from", fromEthAddress);
        map.put("to", toEthAddress);
        transactionAmount = transactionAmount.multiply(BigDecimal.valueOf(Math.pow(10,18)));
        BigInteger intAmount = transactionAmount.toBigInteger();
        map.put("value", "0x" + intAmount.toString(16));
        // 计算estimateGas
        Object estimateGas_param[] = {map};
        String estimateGas = eth_estimateGas(estimateGas_param);
        map.put("gas", estimateGas);
        // 计算gasPrice
        map.put("gasPrice", eth_gasPrice());
        Object param[] = {map};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }

    @Override
    public String eth_getImTokenUnsignTransactionData(String fromEthAddress, String toEthAddress, BigDecimal transactionAmount){
        // ImToken ETH发送待签名交易数据
        // ethereum:0xc44e2681520D0514Eb8FCDF6F9d1408975E31c00?
        // contractAddress=&data=&decimals=18&fee=0.00020160&from=0x1707bFf613009833F0cd18B03f94aC7392C6e387&gas=25200&gasPrice=8000000000&memo=
        // &mode=offlineSign&nonce=7&token=ETH&value=0.012&md5=63f7db8c0815a213c33304f07d647628
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("from", fromEthAddress);
        map.put("to", toEthAddress);
        BigDecimal transactionAmountVar = transactionAmount.multiply(BigDecimal.valueOf(Math.pow(10,18)));
        BigInteger intAmount = transactionAmountVar.toBigInteger();
        map.put("value", "0x" + intAmount.toString(16));
        // 计算estimateGas
        Object estimateGas_param[] = {map};
        String estimateGasStr = eth_estimateGas(estimateGas_param);
        Long estimateGasLong = ConversionUtils.fromHexString(estimateGasStr.substring(2));
        // 计算gasPrice
        String gasPriceStr = eth_gasPrice();
        Long gasPriceLong = ConversionUtils.fromHexString(gasPriceStr.substring(2));
        // 根据gasPrice与estimateGas计算实际的eth手续费
        BigDecimal ethFee =  BigDecimal.valueOf(Double.valueOf(gasPriceLong)).multiply(BigDecimal.valueOf(Double.valueOf(estimateGasLong))).divide(BigDecimal.valueOf(Math.pow(10,18)));

        //获取fromEthAddress的交易nonce
        Long fromEthAddressTxNonce = eth_getTransactionCount(fromEthAddress);
        // 组装报文
        final StringBuilder sb = new StringBuilder("ethereum:").append(toEthAddress).append("?");
        sb.append("contractAddress=");
        sb.append("&data=");
        sb.append("&decimals=").append(18);
        sb.append("&fee=").append(ethFee.toPlainString());
        sb.append("&from=").append(fromEthAddress);
        sb.append("&gas=").append(estimateGasLong);
        sb.append("&gasPrice=").append(gasPriceLong);
        sb.append("&memo=");
        sb.append("&mode=").append("offlineSign");
        sb.append("&nonce=").append(fromEthAddressTxNonce);
        sb.append("&token=ETH");
        sb.append("&value=").append(transactionAmount.toPlainString());
        String md5 = MD5Util.MD5(sb.toString());
        sb.append("&md5=").append(md5.toLowerCase());
        return sb.toString();
    }

    @Override
    public String eth_sendRawTransaction(String signedTransactionData){
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_sendRawTransaction");
        Object param[] = {signedTransactionData};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
    /**
     * 根据ethAddress地址查询当前地址的交易总数（实际就是nonce数值）
     * @param ethAddress
     * @return
     */
    @Override
    public Long eth_getTransactionCount(String ethAddress)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_getTransactionCount");
        Object[] param = {ethAddress, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return ConversionUtils.fromHexString(JSONObject.parseObject(content).getString("result").substring(2));
    }
    
    /**
     * (根据block的hash值以及交易Index索引查询具体某一笔交易详情(注意 一次只能查询一笔交易))
     * @param blockHash 16进制字符串 index 16进制字符串
     * @return
     */
    @Override
    public ErcTokenResultModel eth_getTransactionByBlockHashAndIndex(String blockHash, String index)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_getTransactionByBlockHashAndIndex");
        Object[] param = {blockHash, index};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        ErcTokenResultModel responceModel = JSONObject.parseObject(content).getJSONObject("result").toJavaObject(ErcTokenResultModel.class);
        return responceModel;
    }
    
    /**
     * 根据交易hash获取交易详情
     * @param txHash 16进制字符串
     * @return
     */
    @Override
    public ErcTokenResultModel eth_getTransactionByHash(String txHash)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_getTransactionByHash");
        Object[] param = {txHash};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        ErcTokenResultModel responceModel = JSONObject.parseObject(content).getJSONObject("result").toJavaObject(ErcTokenResultModel.class);
        return responceModel;
    }

    @Override
    public String eth_web3_sha3(String str16)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("web3_sha3");
        Object param_sha[] = {str16};
        model.setParams(param_sha);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }

    private String eth_web3_sha3(ErcTokenRequestModel model)
    {
        model.setMethod("web3_sha3");
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
    /**
     * 测试用例
     * @param args
     */
    public static void main(String args[])
    {
        EthLocalServiceImpl ethLocalService = new EthLocalServiceImpl();
        ErcTokenRequestModel model = new ErcTokenRequestModel();

        //转16进制
        BigInteger number = (BigDecimal.valueOf(1000).multiply(BigDecimal.valueOf(Math.pow(10,18)))).toBigInteger();
        String oxstr = "0x"+ConversionUtils.toHexString(number);
        System.out.println(number+ ":10进制转16进制:"+ oxstr);

        model.setId(1L);
        model.setJsonrpc("2.0");
        String sign = "0x" + ConversionUtils.toHexString("BIEX");

        /*
        // web3_sha3
        Object sha3param[] = {sign};
        model.setParams(sha3param);
        String sha3result = ethLocalService.eth_web3_sha3(model);
        System.out.println(sha3result);

        // eth_sign签名
        String eth_signResult = ethLocalService.eth_sign(sign, "0x727d1f40143a56a987478d19aa79088e22e4355a", "Bcbc963852");
        System.out.println(eth_signResult);

        // 按hash查交易数量
        Long num = ethLocalService.eth_getTransactionCount("0x419abcd98e8b9182d07ff270a1502587887bb650");
        System.out.println(num);

        // 根据block的hash值以及交易Index索引查询具体某一笔交易详情(注意 一次只能查询一笔交易
        ErcTokenResultModel responceModel = ethLocalService.eth_getTransactionByBlockHashAndIndex("0x483392343457c06e3d27353928b65ae07cc61cee09528e62743378df05028dc1", "0x0");
        System.out.println(responceModel.toString());

        // 根据交易hash获取交易详情
        responceModel = ethLocalService.eth_getTransactionByHash("0x761e5f91b15851df339a43b512dcb17855a341c7a5725828aed8209b86dd16b1");
        System.out.println(responceModel.toString());

        // 获取ETH余额
        BigDecimal getBalanceresult = ethLocalService.eth_getBalance("0x727d1f40143a56a987478d19aa79088e22e4355a");
        System.out.println(getBalanceresult);

        // 获取estimateGas
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("from", "0x65d552a9d35d43c629742b94d71e251958275629");
        map.put("to", "0x727d1f40143a56a987478d19aa79088e22e4355a");
        BigInteger intAmount = BigDecimal.valueOf(Math.pow(10,18)).toBigInteger();
        map.put("value", "0x" + intAmount.toString(16));
        Object gas_param[] = {map};
        String gas = ethLocalService.eth_estimateGas(gas_param);
        System.out.println(gas);

        // 获取gasPrice
        String  eth_gasPrice = ethLocalService.eth_gasPrice();
        System.out.println(eth_gasPrice);

        // 账户列表
        String[] listAccountsresult = ethLocalService.personal_listAccounts();
        System.out.println(listAccountsresult.length);
        for(Object string:listAccountsresult)
        {
            System.out.println(string);
        }

        // 账户签名
        Object signparam[]={sign, "0x727d1f40143a56a987478d19aa79088e22e4355a", "Bcbc963852"};
        model.setParams(signparam);
        String personal_signResult = ethLocalService.personal_sign(model);
        System.out.println(personal_signResult);

        // 解锁账户
        Object unlockparam[] = {"0x65d552a9d35d43c629742b94d71e251958275629", "Bcbc963852", 10}; // 10秒
        model.setParams(unlockparam);
        Boolean unlockresult = ethLocalService.personal_unlockAccount(model);
        System.out.println(unlockresult);

        // 发送交易
        BigDecimal transactionAmount = BigDecimal.valueOf(0.01);
        String sendTransactionResult = ethLocalService.eth_sendTransaction("0x65d552a9d35d43c629742b94d71e251958275629","0x727d1f40143a56a987478d19aa79088e22e4355a",transactionAmount);
        System.out.println(sendTransactionResult);

        // 发送签名交易（离线签名交易OK）
        sendTransactionResult = ethLocalService.eth_sendRawTransaction("0xf8a9808501dcd6500082ea60948406d5d0795417c42790991d03d638486bd1065180b844a9059cbb00000000000000000000000065d552a9d35d43c629742b94d71e251958275629000000000000000000000000000000000000000000000000000000000000000b2aa09c4b4cfe38ef381dadfa82b2f227146a46eaa8f8b5e0b0cf03ea039b095ed841a011a3e04a3dc7ca77112ef3afde0087c1d2acc7c46fe402c50a3807a9c2decc60");
        System.out.println(sendTransactionResult);

        // 锁定账户
        Boolean lockAccount_result = ethLocalService.personal_lockAccount("0x65d552a9d35d43c629742b94d71e251958275629");
        System.out.println(lockAccount_result);


        // 创建账户
        String newaccount_result = ethLocalService.personal_newAccount("123456");
        System.out.println(newaccount_result);

        // 获取版本
        String version_result = ethLocalService.doPost(model);
        System.out.println(version_result);
        */
    }
}
