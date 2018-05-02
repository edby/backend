package com.blocain.bitms.payment.eth;

/**
 *  EtherscanRemoteService接口
 * <p>File：EtherscanRemoteService.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2018-02-26 13:07:06</p>
 * <p>Company: BloCain</p>
 * @author sunbiao
 *
 * API地址:https://ropsten.etherscan.io/apis#proxy
 *
 * @version 1.0
 */
public interface EtherscanRemoteService
{

    /**
     * Returns the number of most recent block
     *
     * https://api-ropsten.etherscan.io/api?module=proxy&action=eth_blockNumber&apikey=YourApiKeyToken
     *
     * @return
     */
    String eth_blockNumber();

    /**
     *  分页获取交易
     * @param address
     * @param startblock
     * @param endIndex
     * @param page
     * @param offset
     * @return
     */
    int eth_txlist(String address, Long startblock, Long endIndex, int page, int offset);

}
