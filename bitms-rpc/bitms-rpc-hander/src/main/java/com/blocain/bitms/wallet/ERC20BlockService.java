package com.blocain.bitms.wallet;

import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.block.entity.BlockInfoERC20;
import com.blocain.bitms.trade.fund.entity.EthAddrs;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.List;
import java.util.Set;

/**
 * ERC20区块服务
 */
public interface ERC20BlockService
{
    /**
     * 同步区块最新编号
     * <p>
     *     将钱包节点中最新的块同步到区块信息表中去
     * </p>
     * @throws BusinessException
     */
    void doSyncBlockNumber() throws BusinessException;

    /**
     * ERC20区块交易扫描
     * @throws BusinessException
     */
    void doScanERC20Block(BlockInfoERC20 LastUnScanTransBlockInfoErc20) throws BusinessException;

    /**
     * ERC20区块Erc20Token信息扫描
     * @throws BusinessException
     */
    void doScanERC20BlockErc20Token(EthBlock.Block block) throws BusinessException;

    /**
     * EthAddrs地址库信息扫描
     * @throws BusinessException
     */
    void doScanEthAddrs(EthBlock.Block block) throws BusinessException;

    /**
     * ERC20交易扫描入账
     * @throws BusinessException
     */
    void scanERC20Trans() throws BusinessException;

    /**
     * ERC20余额归集
     * @throws BusinessException
     */
    void collectERC20Balance() throws BusinessException;
    
    /**
     * 取最后一次待交易扫描的区块
     * <p>
     *     从数据库中取出，每扫描完一个区块都将会做扫描过的标识
     * </p>
     * @return
     * @throws BusinessException
     */
    BlockInfoERC20 getLastUnScanTransBlockInfoErc20() throws BusinessException;
    
    /**
     * 根据参数地址取平台中存在的地址
     * @param extAddress
     * @return
     * @throws BusinessException
     */
    Set<String> getAccountInterAddrsByParams(String ... extAddress) throws BusinessException;
    
    /**
     * 根据区块编号取所有交易信息
     * @param blockNumber
     * @return {@link Transaction}
     * @throws BusinessException
     */
    List<Transaction> getTransactionsByBlockNumber(Long blockNumber) throws BusinessException;


    /**
     * 查询Erc20Token中最低块高度信息的列表
     * @return
     */
    List<Erc20Token> findListMinHeightErc20Token();

    /**
     * 查询EthAddrs中最低块高度信息的列表
     * @return
     */
    List<EthAddrs> findListMinHeightEthAddrs();

}
