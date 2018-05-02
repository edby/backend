package com.blocain.bitms.payment.eth.impl;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.eth.EtherscanRemoteService;
import com.blocain.bitms.payment.basic.BasicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * EtherscanRemoteService接口Impl
 * <p>File：EtherscanRemoteServiceImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-02-27 10:59:05</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 * @version 1.0
 */
@Component("etherscanRemoteService")
public class EtherscanRemoteServiceImpl extends BasicServiceImpl implements EtherscanRemoteService
{
    public static final Logger logger               = LoggerFactory.getLogger(EtherscanRemoteServiceImpl.class);
    // private static final String erc20token_root = BitmsConst.ETH_CLIENT_URL;
    
    // http://192.168.31.122:8545
    public static final String erc20token_etherscan = propertiesErc20.getProperty("erc20token.etherscan.url");
    
    public static final String key                  = propertiesErc20.getProperty("etherscan.io.apiKey");
    
    @Override
    public String eth_blockNumber()
    {
        // https://api-ropsten.etherscan.io/api?module=proxy&action=eth_blockNumber&apikey=YourApiKeyToken
        JSONObject jsonReturn = httpPost(erc20token_etherscan, "module=proxy&action=eth_blockNumber&apikey=" + key);
        return jsonReturn.getString("result");
    }

    @Override
    public int eth_txlist(String address, Long startblock, Long endIndex, int page, int offset)
    {
        StringBuilder reqStr = new StringBuilder("module=account&action=txlist&address=").append(address).append("&startblock=").append(startblock).append("&endblock=")
                .append(endIndex).append("&page=").append(page).append("&offset=").append(offset).append("&sort=asc&apikey=").append(key);
        JSONObject jsonReturn = httpPost(erc20token_etherscan, reqStr.toString());
        return jsonReturn.getJSONArray("result").size();
    }
    
    /**
     * 测试用例
     * @param args
     */
    public static void main(String args[])
    {
        EtherscanRemoteServiceImpl etherscanRemoteService = new EtherscanRemoteServiceImpl();
//        String eth_blockNumber = etherscanRemoteService.eth_blockNumber();
//        System.out.println("eth_blockNumber:" + eth_blockNumber);

        int cnt = etherscanRemoteService.eth_txlist("0xddbd2b932c763ba5b1b7ae3b362eac3e8d40121a",0L,550000L,1,10);
        logger.debug("存在最早交易笔数:"+cnt);

    }
}
