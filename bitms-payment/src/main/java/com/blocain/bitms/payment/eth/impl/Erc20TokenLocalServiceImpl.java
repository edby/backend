package com.blocain.bitms.payment.eth.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.eth.model.ErcTokenRequestModel;
import com.blocain.bitms.payment.eth.Erc20TokenLocalService;
import com.blocain.bitms.payment.basic.BasicServiceImpl;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.ConversionUtils;
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
 * ERC20 TOKE
 * <p>File：Erc20TokenLocalServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-02-27 10:58:55</p>
 * <p>Company: BloCain</p>
 * @author 张春喜
 * @version 1.0
 */
@Component("erc20TokenLocalService")
public class Erc20TokenLocalServiceImpl extends BasicServiceImpl implements Erc20TokenLocalService
{
    public static final Logger logger            = LoggerFactory.getLogger(Erc20TokenLocalServiceImpl.class);
    // private static final String erc20token_root = BitmsConst.ETH_CLIENT_URL;
    
    // http://192.168.31.122:8545
    public static final String erc20token_root   = propertiesErc20.getProperty("erc20token.root.url");

    public static String       SHA3_NAME         = "0x06fdde03";                                             // name()

    public static String       SHA3_SYMBOL       = "0x95d89b41";                                             // symbol()

    public static String       SHA3_DECIMALS     = "0x313ce567";                                             // decimals()
    
    public static String       SHA3_TOTALSUPPLY  = "0x18160ddd";                                             // totalSupply()
    
    public static String       SHA3_BALANCEOF    = "0x70a08231";                                             // balanceOf(uint256)
    
    public static String       SHA3_ALLOWWANCE   = "0xdd62ed3e";                                             // allowance(address,address)
    
    public static String       SHA3_APPROVE      = "0x095ea7b3";                                             // approve(address,uint256)
    
    public static String       SHA3_TRANSFERFROM = "0x23b872dd";                                             // transferFrom(address,address,uint256)
    
    public static String       SHA3_TRANSFER     = "0xa9059cbb";                                             // transfer(address,uint256)
    
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
        System.out.println("请求报文：" + jsonParam.toJSONString());
        logger.info("请求报文：" + jsonParam.toJSONString());
        String content = super.httpPostWithJSON(client, erc20token_root, null, jsonParam, null);
        // System.out.println("应答报文："+ content);
        logger.info("应答报文：" + content);
        return content;
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
    
    private Boolean personal_unlockAccount(ErcTokenRequestModel model)
    {
        model.setMethod("personal_unlockAccount");
        String content = doPost(model);
        return JSONObject.parseObject(content).getBoolean("result");
    }
    
    @Override
    public String erc20_web3_sha3(String str16)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("web3_sha3");
        Object param_sha[] = {str16};
        model.setParams(param_sha);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
    private String erc20_web3_sha3(ErcTokenRequestModel model)
    {
        model.setMethod("web3_sha3");
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }

    @Override
    public String erc20_name(String tokenContactAdddr)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        StringBuilder data = new StringBuilder(SHA3_NAME);
        map.put("data", data);
        Object param[] = {map, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_name：" + model.toString());
        String content = doPost(model);
        return ConversionUtils.hexStrToString(JSONObject.parseObject(content).getString("result").substring(2)).trim();
    }

    @Override
    public String erc20_symbol(String tokenContactAdddr)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        StringBuilder data = new StringBuilder(SHA3_SYMBOL);
        map.put("data", data);
        Object param[] = {map, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_symbol：" + model.toString());
        String content = doPost(model);
        return ConversionUtils.hexStrToString(JSONObject.parseObject(content).getString("result").substring(2)).trim();
    }

    @Override
    public Long erc20_decimals(String tokenContactAdddr)
    {
        /*
         * ErcTokenRequestModel model_sha = new ErcTokenRequestModel();
         * String sign = "0x" + ConversionUtils.toHexString("decimals()");
         * Object param_sha[] = {sign};
         * model_sha.setParams(param_sha);
         * model_sha.setId(System.currentTimeMillis());
         * String result = web3_sha3(model_sha);
         * System.out.println(result);
         */
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        StringBuilder data = new StringBuilder(SHA3_DECIMALS);
        map.put("data", data);
        Object param[] = {map, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_decimals：" + model.toString());
        String content = doPost(model);
        BigDecimal ret = hexToBigDecimal(JSONObject.parseObject(content).getString("result").substring(2));
        return ret.longValue();
    }
    
    @Override
    public BigDecimal erc20_totalSupply(String tokenContactAdddr)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        StringBuilder data = new StringBuilder(SHA3_TOTALSUPPLY);
        map.put("data", data);
        Object param[] = {map, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_totalSupply：" + model.toString());
        String content = doPost(model);
        if(JSONObject.parseObject(content).getString("result").length() >= 3){
            BigDecimal ret = hexToBigDecimal(JSONObject.parseObject(content).getString("result").substring(2));
            Long decimals = erc20_decimals(tokenContactAdddr);
            return ret.divide(BigDecimal.valueOf(Math.pow(10,decimals)));
        }
        else {
            return null;
        }
    }
    
    @Override
    public BigDecimal erc20_balanceOf(String accountAddress, String tokenContactAdddr, String status)
    {
        /*
         * ErcTokenRequestModel model_sha = new ErcTokenRequestModel();
         * String sign = "0x" + ConversionUtils.toHexString("balanceOf(uint256)");
         * Object param_sha[] = {sign};
         * model_sha.setParams(param_sha);
         * model_sha.setId(System.currentTimeMillis());
         * String result = web3_sha3(model_sha);
         * System.out.println(result);
         */
        StringBuilder data = new StringBuilder(SHA3_BALANCEOF).append("000000000000000000000000").append(accountAddress.substring(2));
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        map.put("data", data);
        Object param[] = {map, status};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        System.out.println("erc20_balanceOf：" + model.toString());
        logger.info("erc20_balanceOf：" + model.toString());
        String content = doPost(model);
        Long decimals = erc20_decimals(tokenContactAdddr);
        BigDecimal ret = hexToBigDecimal(JSONObject.parseObject(content).getString("result").substring(2)).divide(BigDecimal.valueOf(Math.pow(10, decimals)));
        System.out.println(ret);
        return ret;
    }
    
    @Override
    public BigDecimal erc20_allowance(String ownerAddr, String tokenContactAdddr, String feeAddress, String status)
    {
        /*
         * ErcTokenRequestModel model_sha = new ErcTokenRequestModel();
         * String sign = "0x" + ConversionUtils.toHexString("allowance(address,address)");
         * Object param_sha[] = {sign};
         * model_sha.setParams(param_sha);
         * model_sha.setId(System.currentTimeMillis());
         * String result = web3_sha3(model_sha);
         * System.out.println(result);
         */
        StringBuilder data = new StringBuilder(SHA3_ALLOWWANCE).append("000000000000000000000000").append(ownerAddr.substring(2)).append("000000000000000000000000")
                .append(feeAddress.substring(2));
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        map.put("data", data);
        Object param[] = {map, status};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        System.out.println("erc20_allowance：" + model.toString());
        logger.info("erc20_allowance：" + model.toString());
        String content = doPost(model);
        // 根据具体token的小数精度计算
        Long decimals = erc20_decimals(tokenContactAdddr);
        BigDecimal ret = hexToBigDecimal(JSONObject.parseObject(content).getString("result").substring(2)).divide(BigDecimal.valueOf(Math.pow(10, decimals)));
        System.out.println(ret);
        return ret;
    }
    
    @Override
    public String erc20_approve(String tokenContactAdddr, String chargeAddress, String chargeAddressPwd, String feeAddress, BigDecimal amount)
    {
        // 先解锁
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param_unlock[] = {chargeAddress, chargeAddressPwd, 10}; // 10秒
        unlockModel.setId(System.currentTimeMillis());
        unlockModel.setParams(param_unlock);
        boolean result_unlock = personal_unlockAccount(unlockModel);
        logger.info("unlock:" + result_unlock);
        /*
         * ErcTokenRequestModel model_sha = new ErcTokenRequestModel();
         * String sign = "0x" + ConversionUtils.toHexString("approve(address,uint256)");
         * Object param_sha[] = {sign};
         * model_sha.setParams(param_sha);
         * model_sha.setId(System.currentTimeMillis());
         * String result = web3_sha3(model_sha);
         * System.out.println(result);
         */
        String amount0 = "0000000000000000000000000000000000000000000000000000000000000000";
        // 根据具体token的小数精度计算
        Long decimals = erc20_decimals(tokenContactAdddr);
        BigInteger number = (amount.multiply(BigDecimal.valueOf(Math.pow(10, decimals)))).toBigInteger();
        String amount1 = ConversionUtils.toHexString(number);
        StringBuilder data = new StringBuilder(SHA3_APPROVE).append("000000000000000000000000").append(feeAddress.substring(2))
                .append(amount0.substring(0, amount0.length() - amount1.length())).append(amount1);
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_sendTransaction");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        map.put("data", data);
        // 根据参数计算gas
        Object gas_param[] = {map};
        String gas = eth_estimateGas(gas_param);
        map.put("from", chargeAddress);
        map.put("value", "0x0"); // eth 0
        map.put("gas", gas);
        // 计算gasPrice
        map.put("gasPrice", eth_gasPrice());
        Object param[] = {map};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_approve：" + model.toString());
        String content = doPost(model);
        String ret = JSONObject.parseObject(content).getString("result");
        System.out.println(ret);
        return ret;
    }
    
    @Override
    public String erc20_transferFrom(String tokenContactAdddr, String chargeAddress, String feeAddress, String feeAddressPwd, String collectAddress, BigDecimal amount)
    {
        // 先解锁
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param_unlock[] = {feeAddress, feeAddressPwd, 10}; // 10秒
        unlockModel.setId(System.currentTimeMillis());
        unlockModel.setParams(param_unlock);
        boolean result_unlock = personal_unlockAccount(unlockModel);
        logger.info("unlock:" + result_unlock);
        /*
         * ErcTokenRequestModel model_sha = new ErcTokenRequestModel();
         * String sign = "0x" + ConversionUtils.toHexString("transferFrom(address,address,uint256)");
         * Object param_sha[] = {sign};
         * model_sha.setParams(param_sha);
         * model_sha.setId(System.currentTimeMillis());
         * String result = web3_sha3(model_sha);
         * System.out.println(result);
         */
        String amount0 = "0000000000000000000000000000000000000000000000000000000000000000";
        // 根据具体token的小数精度计算
        Long decimals = erc20_decimals(tokenContactAdddr);
        BigInteger number = (amount.multiply(BigDecimal.valueOf(Math.pow(10, decimals)))).toBigInteger();
        String amount1 = ConversionUtils.toHexString(number);
        StringBuilder data = new StringBuilder(SHA3_TRANSFERFROM).append("000000000000000000000000").append(chargeAddress.substring(2)).append("000000000000000000000000")
                .append(collectAddress.substring(2)).append(amount0.substring(0, amount0.length() - amount1.length())).append(amount1);
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_sendTransaction");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        map.put("data", data);
        // 根据参数计算gas
        Object gas_param[] = {map};
        String gas = eth_estimateGas(gas_param);
        map.put("from", feeAddress);
        map.put("value", "0x0"); // eth 0
        map.put("gas", gas);
        // 计算gasPrice
        map.put("gasPrice", eth_gasPrice());
        Object param[] = {map};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_transferFrom eth_call：" + model.toString());
        String content = doPost(model);
        String ret = JSONObject.parseObject(content).getString("result");
        System.out.println(ret);
        return ret;
    }
    
    @Override
    public String erc20_transfer(String tokenContactAddr, String formAddress, String formAddressPwd, String destAddress, BigDecimal amount)
    {
        // 先解锁
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param_unlock[] = {formAddress, formAddressPwd, 10}; // 10秒
        unlockModel.setId(System.currentTimeMillis());
        unlockModel.setParams(param_unlock);
        boolean result_unlock = personal_unlockAccount(unlockModel);
        logger.info("unlock:" + result_unlock);
        /*
         * ErcTokenRequestModel model_sha = new ErcTokenRequestModel();
         * String sign = "0x" + ConversionUtils.toHexString("transferFrom(address,address,uint256)");
         * Object param_sha[] = {sign};
         * model_sha.setParams(param_sha);
         * model_sha.setId(System.currentTimeMillis());
         * String result = web3_sha3(model_sha);
         * System.out.println(result);
         */
        String amount0 = "0000000000000000000000000000000000000000000000000000000000000000";
        // 根据具体token的小数精度计算
        Long decimals = erc20_decimals(tokenContactAddr);
        System.out.println("decimals=" + decimals);
        BigInteger number = (amount.multiply(BigDecimal.valueOf(Math.pow(10, decimals)))).toBigInteger();
        String amount1 = ConversionUtils.toHexString(number);
        System.out.println("amount1=" + amount);
        System.out.println("amount1=" + amount1);
        StringBuilder data = new StringBuilder(SHA3_TRANSFER).append("000000000000000000000000").append(destAddress.substring(2))
                .append(amount0.substring(0, amount0.length() - amount1.length())).append(amount1);
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_sendTransaction");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAddr);
        map.put("data", data);
        // 根据参数计算gas
        Object gas_param[] = {map};
        String gas = eth_estimateGas(gas_param);
        map.put("from", formAddress);
        map.put("value", "0x0"); // eth 0
        map.put("gas", gas);
        // 计算gasPrice
        map.put("gasPrice", eth_gasPrice());
        Object param[] = {map};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_transfer eth_call：" + model.toString());
        String content = doPost(model);
        String ret = JSONObject.parseObject(content).getString("result");
        System.out.println(ret);
        return ret;
    }
    
    /**
     * 根据ethAddress地址查询当前地址的交易总数（实际就是nonce数值）
     * @param ethAddress
     * @return
     */
    private Long eth_getTransactionCount(String ethAddress)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_getTransactionCount");
        Object[] param = {ethAddress, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        String content = doPost(model);
        return ConversionUtils.fromHexString(JSONObject.parseObject(content).getString("result").substring(2));
    }
    
    @Override
    public String erc20_getImTokenUnsignTransactionData(String tokenContractAddress, String tokenSymbol, String fromTokenAddress, String toTokenAddress,
            BigDecimal transactionAmount)
    {
        // ImToken Token发送待签名交易数据
        // ethereum:0x86169ee5A4Ef045f611c891D6F4315e27ba2A8C4?
        // contractAddress=0xd892a5122ce547d928ca20ce50e51a5152f00d3a
        // &data=0xa9059cbb00000000000000000000000086169ee5A4Ef045f611c891D6F4315e27ba2A8C400000000000000000000000000000000000000000000000000000000000186a0
        // &decimals=6&fee=0.00048000&from=0x1707bFf613009833F0cd18B03f94aC7392C6e387&gas=60000&gasPrice=8000000000&memo=&mode=offlineSign&nonce=7&token=BIEX&value=0&md5=02ae6ad65a53a9dce00c8035a4417017
        String amount0 = "0000000000000000000000000000000000000000000000000000000000000000";
        // 根据具体token的小数精度计算
        Long decimals = erc20_decimals(tokenContractAddress);
        BigInteger number = (transactionAmount.multiply(BigDecimal.valueOf(Math.pow(10, decimals)))).toBigInteger();
        String amount1 = ConversionUtils.toHexString(number);
        StringBuilder gas_data = new StringBuilder(SHA3_TRANSFER).append(amount0.substring(0, amount0.length() - amount1.length())).append(amount1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContractAddress);
        map.put("data", gas_data);
        // 根据参数计算gas
        Long estimateGasLong = 0L;
        try
        {
            Object gas_param[] = {map};
            String estimateGasStr = eth_estimateGas(gas_param);
            estimateGasLong = ConversionUtils.fromHexString(estimateGasStr.substring(2));
        }
        catch (Exception e)
        {
            System.out.println("预估gas出错");
            estimateGasLong = 60000L;
        }
        StringBuilder data = new StringBuilder(SHA3_TRANSFER).append("000000000000000000000000").append(toTokenAddress.substring(2))
                .append(amount0.substring(0, amount0.length() - amount1.length())).append(amount1);
        map.put("data", data);
        // 计算gasPrice
        String gasPriceStr = eth_gasPrice();
        Long gasPriceLong = ConversionUtils.fromHexString(gasPriceStr.substring(2));
        // 根据gasPrice与estimateGas计算实际的eth手续费
        BigDecimal ethFee = BigDecimal.valueOf(Double.valueOf(gasPriceLong)).multiply(BigDecimal.valueOf(Double.valueOf(estimateGasLong)))
                .divide(BigDecimal.valueOf(Math.pow(10, 18)));// 注意手续费是eth,所以小数精度固定
        // 获取fromTokenAddress的交易nonce
        Long fromEthAddressTxNonce = eth_getTransactionCount(fromTokenAddress);
        // 组装报文
        final StringBuilder sb = new StringBuilder("ethereum:").append(toTokenAddress).append("?");
        sb.append("contractAddress=").append(tokenContractAddress);
        sb.append("&data=").append(data);
        sb.append("&decimals=").append(decimals);
        sb.append("&fee=").append(ethFee.toPlainString());
        sb.append("&from=").append(fromTokenAddress);
        sb.append("&gas=").append(estimateGasLong);
        sb.append("&gasPrice=").append(gasPriceLong);
        sb.append("&memo=");
        sb.append("&mode=").append("offlineSign");
        sb.append("&nonce=").append(fromEthAddressTxNonce);
        sb.append("&token=").append(tokenSymbol);
        sb.append("&value=").append(0);
        String md5 = MD5Util.MD5(sb.toString());
        sb.append("&md5=").append(md5.toLowerCase());
        return sb.toString();
    }
    
    @Override
    public String erc20_sendRawTransaction(String signedTransactionData)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_sendRawTransaction");
        Object param[] = {signedTransactionData};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_sendRawTransaction：" + model.toString());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
    @Override
    public String eth_gasPrice()
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_gasPrice");
        model.setId(System.currentTimeMillis());
        logger.info("eth_gasPrice：" + model.toString());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
    @Override
    public String eth_estimateGas(Object[] params)
    {
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_estimateGas");
        model.setId(System.currentTimeMillis());
        model.setParams(params);
        logger.info("eth_estimateGas：" + model.toString());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
    public String erc20_burn(String tokenContactAdddr)
    {
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param_unlock[] = {"0x78fc26027b58edf783c5b3de0be2199bf528f559", "zhangchunxi", 10}; // 10秒
        unlockModel.setId(System.currentTimeMillis());
        unlockModel.setParams(param_unlock);
        personal_unlockAccount(unlockModel);
        ErcTokenRequestModel model_sha = new ErcTokenRequestModel();
        String sign = "0x" + ConversionUtils.toHexString("burn(uint256)");
        Object param_sha[] = {sign};
        model_sha.setParams(param_sha);
        model_sha.setId(System.currentTimeMillis());
        String result = erc20_web3_sha3(model_sha);
        System.out.println(result);
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        // map.put("from", "0x78fc26027b58edf783c5b3de0be2199bf528f559");
        map.put("to", tokenContactAdddr);
        BigDecimal amount = BigDecimal.valueOf(20000);
        String amount0 = "0000000000000000000000000000000000000000000000000000000000000000";
        Long decimals = erc20_decimals(tokenContactAdddr);
        System.out.println("decimals=" + decimals);
        BigInteger number = (amount.multiply(BigDecimal.valueOf(Math.pow(10, decimals)))).toBigInteger();
        String amount1 = ConversionUtils.toHexString(number);
        System.out.println("amount1=" + amount);
        System.out.println("amount1=" + amount1);
        // map.put("value", "0x"+amount1);
        StringBuilder data = new StringBuilder(result.substring(0, 10)).append(amount0.substring(0, amount0.length() - amount1.length())).append(amount1);
        map.put("data", data);
        Object param[] = {map, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_decimals eth_call：" + model.toString());
        String content = doPost(model);
        System.out.println(content);
        return JSONObject.parseObject(content).getString("result");
    }
    
    public String erc20_burnFrom(String tokenContactAdddr, String address, BigDecimal amount)
    {
        ErcTokenRequestModel unlockModel = new ErcTokenRequestModel();
        Object param_unlock[] = {address, "zhangchunxi", 10}; // 10秒
        unlockModel.setId(System.currentTimeMillis());
        unlockModel.setParams(param_unlock);
        personal_unlockAccount(unlockModel);
        String amount0 = "0000000000000000000000000000000000000000000000000000000000000000";
        Long decimals = erc20_decimals(tokenContactAdddr);
        System.out.println("decimals=" + decimals);
        BigInteger number = (amount.multiply(BigDecimal.valueOf(Math.pow(10, decimals)))).toBigInteger();
        String amount1 = ConversionUtils.toHexString(number);
        System.out.println("amount1=" + amount);
        System.out.println("amount1=" + amount1);
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setMethod("eth_call");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("to", tokenContactAdddr);
        StringBuilder data = new StringBuilder("0x79cc6790").append("000000000000000000000000").append(address.substring(2))
                .append(amount0.substring(0, amount0.length() - amount1.length())).append(amount1);
        map.put("data", data);
        Object param[] = {map, "latest"};
        model.setParams(param);
        model.setId(System.currentTimeMillis());
        logger.info("erc20_decimals eth_call：" + model.toString());
        String content = doPost(model);
        return JSONObject.parseObject(content).getString("result");
    }
    
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
    
    /**
     * 测试用例
     * @param args
     */
    public static void main(String args[])
    {
        /**
         0x06fdde03 -> [ function ] name
         0x095ea7b3 -> [ function ] approve
         0x18160ddd -> [ function ] totalSupply
         0x23b872dd -> [ function ] transferFrom
         0x313ce567 -> [ function ] decimals
         0x475a9fa9 -> [ function ] issueTokens
         0x70a08231 -> [ function ] balanceOf
         0x95d89b41 -> [ function ] symbol
         0xa9059cbb -> [ function ] transfer
         0xdd62ed3e -> [ function ] allowance
         0xddf252ad -> [ event ] Transfer
         0x8c5be1e5 -> [ event ] Approval
         */
        Erc20TokenLocalServiceImpl erc20TokenLocalService = new Erc20TokenLocalServiceImpl();
        ErcTokenRequestModel model = new ErcTokenRequestModel();
        model.setId(1L);
        model.setJsonrpc("2.0");
        BigDecimal total = erc20TokenLocalService.erc20_totalSupply("0x8406d5d0795417C42790991d03d638486bD10651");
        System.out.println(total.setScale(0));
        String name = erc20TokenLocalService.erc20_name("0x8406d5d0795417C42790991d03d638486bD10651");
        System.out.println(name.trim());
        String symbol = erc20TokenLocalService.erc20_symbol("0x8406d5d0795417C42790991d03d638486bD10651");
        System.out.println(symbol.trim());
        // erc20TokenLocalService.erc20_transfer("0x22f5396fca754eabde1ad2b5af0398b7b7f722b7"
        // ,"0x78fc26027b58edf783c5b3de0be2199bf528f559", "zhangchunxi",
        // "0x65d552a9d35d43c629742b94d71e251958275629", BigDecimal.valueOf(5555555L));
        // String sign = "0x" + ConversionUtils.toHexString("burn()");
        // String ret = erc20TokenLocalService.eth_web3_sha3(sign);
        // System.out.println(ret);
        // erc20TokenLocalService.erc20_burn("0x22f5396fca754eabde1ad2b5af0398b7b7f722b7");
        // BigDecimal x = BigDecimal.valueOf(2000);
        // erc20TokenLocalService.erc20_transferFrom("0x22f5396fca754eabde1ad2b5af0398b7b7f722b7","0x78fc26027b58edf783c5b3de0be2199bf528f559","0x78fc26027b58edf783c5b3de0be2199bf528f559","zhangchunxi","0x0000000000000000000000000000000000000000",
        // x);
        // String sign = "0x" + ConversionUtils.toHexString("BIEX");
        // BigDecimal amount = BigDecimal.valueOf(0.2);
        // BigInteger number = (amount.multiply(BigDecimal.valueOf(Math.pow(10, 18)))).toBigInteger();
        // String amount1 = ConversionUtils.toHexString(number);
        // System.out.print(amount1);
        // erc20TokenLocalService.erc20_burnFrom("0x22f5396fca754eabde1ad2b5af0398b7b7f722b7","0x78fc26027b58edf783c5b3de0be2199bf528f559",BigDecimal.valueOf(2000));
        /*
         * // web3_sha3
         * Object param[] = {sign};
         * model.setParams(param);
         * String result = erc20TokenLocalService.erc20_web3_sha3(model);
         * System.out.println(result);
         * // 获取精度
         * Long decimals = erc20TokenLocalService.erc20_decimals("0x8406d5d0795417C42790991d03d638486bD10651");
         * System.out.println(decimals);
         * // balanceOf 目标地址 合约地址
         * erc20TokenLocalService.erc20_balanceOf("0x65d552a9d35d43c629742b94d71e251958275629", "0x8406d5d0795417C42790991d03d638486bD10651");
         * // token拥有者 token合约地址 token被授权者
         * erc20TokenLocalService.erc20_allowance("0x65d552a9d35d43c629742b94d71e251958275629",
         * "0x8406d5d0795417C42790991d03d638486bD10651","0x1ab5490b8571cca9ecb37cb3d19129ca2c1d16e");
         * // 获取gasPrice
         * erc20TokenLocalService.eth_gasPrice();
         * // token授权approve操作 合约tokenaddresss chargeAddress chargeAddressPwd feeAddress amount 授权数量
         * BigDecimal t = BigDecimal.valueOf(1);
         * erc20TokenLocalService.erc20_approve("0x8406d5d0795417C42790991d03d638486bD10651",
         * "0x727d1f40143a56a987478d19aa79088e22e4355a","Bcbc963852","0x65d552a9D35d43c629742B94D71e251958275629", t);
         * // token授权approve操作 合约tokenaddresss chargeAddress 充值地址 feeAddressPwd 充值地址密码 feeAddress 手续费地址 collectAddress 归集地址 amount 数量
         * BigDecimal x = BigDecimal.valueOf(1);
         * erc20TokenLocalService.erc20_transferFrom("0x8406d5d0795417C42790991d03d638486bD10651","0x727d1f40143a56a987478d19aa79088e22e4355a",
         * "0x65d552a9D35d43c629742B94D71e251958275629","Bcbc963852","0x1ab5490b8571cca9ecb37cb3d19129ca2c1d16e", x);
         * // 普通转账transfer 不用授权
         * BigDecimal x = BigDecimal.valueOf(1);
         * erc20TokenLocalService.erc20_transfer("0x8406d5d0795417C42790991d03d638486bD10651","0x727d1f40143a56a987478d19aa79088e22e4355a","Bcbc963852",
         * "0x65d552a9D35d43c629742B94D71e251958275629", x);
         * // 发送签名交易
         * String sendTransactionResult = erc20TokenLocalService.erc20_sendRawTransaction(
         * "0xf8a9808501dcd6500082ea60948406d5d0795417c42790991d03d638486bd1065180b844a9059cbb00000000000000000000000065d552a9d35d43c629742b94d71e251958275629000000000000000000000000000000000000000000000000000000000000000b2aa09c4b4cfe38ef381dadfa82b2f227146a46eaa8f8b5e0b0cf03ea039b095ed841a011a3e04a3dc7ca77112ef3afde0087c1d2acc7c46fe402c50a3807a9c2decc60"
         * );
         * System.out.println(sendTransactionResult);
         * // 发送签名交易（离线签名交易OK）【返回txHash:0x79959437830085a711723021b032b726b43d3443082ca005fd0bda5fe0d78c7f】
         * sendTransactionResult = erc20TokenLocalService.erc20_sendRawTransaction(
         * "0xf8a92785098bca5a00830249f0948406d5d0795417c42790991d03d638486bd1065180b844a9059cbb00000000000000000000000065d552a9d35d43c629742b94d71e251958275629000000000000000000000000000000000000000000000000000000000000000229a00b8c9bef557c49a1a198785b86219d667b3d496f63b62e91f06fb0afb5ce1ff69f3262179286b479a238317a801088a237e7c2baf554676b7ce65eb6d657b2af"
         * );
         * System.out.println(sendTransactionResult);
         */
    }
}
