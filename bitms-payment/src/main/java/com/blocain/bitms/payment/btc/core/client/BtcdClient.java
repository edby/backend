package com.blocain.bitms.payment.btc.core.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.blocain.bitms.payment.btc.core.BitcoindException;
import com.blocain.bitms.payment.btc.core.CommunicationException;
import com.blocain.bitms.payment.btc.core.domain.*;

public interface BtcdClient
{
    /**
     * 新增多重签名地址
     * <p>
     *     在钱包里添加一个多重签名地址。每个"KEY"参数都是一个地址或者是一个十六进制编码的公共密钥。
     * </p>
     * @param minSignatures
     * @param addresses
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String addMultiSigAddress(Integer minSignatures, List<String> addresses) throws BitcoindException, CommunicationException;
    
    /**
     * 新增多重签名地址
     * <p>
     *     在钱包里添加一个多重签名地址。每个"KEY"参数都是一个地址或者是一个十六进制编码的公共密钥。
     *     如果指定账户[account]，则将该地址分配给该帐户。
     * </p>
     * @param minSignatures
     * @param addresses
     * @param account
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String addMultiSigAddress(Integer minSignatures, List<String> addresses, String account) throws BitcoindException, CommunicationException;
    
    /**
     * 创建多重签名地址
     * <p>
     *     创建一个多重签名的地址，并返回一个JSON对象
     * </p>
     * @param minSignatures
     * @param addresses
     * @return {@link MultiSigAddress}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    MultiSigAddress createMultiSig(Integer minSignatures, List<String> addresses) throws BitcoindException, CommunicationException;
    
    /**
     * 创建原始交易
     * @param outputs
     * @param toAddresses
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String createRawTransaction(List<OutputOverview> outputs, Map<String, BigDecimal> toAddresses) throws BitcoindException, CommunicationException;
    
    /**
     * 解码原始交易
     * @param hexTransaction
     * @return {@link RawTransactionOverview}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    RawTransactionOverview decodeRawTransaction(String hexTransaction) throws BitcoindException, CommunicationException;
    
    /**
     * 解码一个hex编码的P2SH脚本。
     * @param hexRedeemScript
     * @return {@link RedeemScript}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    RedeemScript decodeScript(String hexRedeemScript) throws BitcoindException, CommunicationException;
    
    /**
     * 导出币地址<bitcoinaddress>对应的私钥。需要未锁定钱包。
     * @param address BTC地址
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String dumpPrivKey(String address) throws BitcoindException, CommunicationException;
    
    /**
     * 费率估算
     * @param maxBlocks
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal estimateFee(Integer maxBlocks) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定地址<bitcoinaddress>相关联的帐户
     * @param address BTC地址
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getAccount(String address) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定账户<account>当前的币收款地址，每次执行都会为指定账户创建一个的新的地址。
     * @param account 钱包帐户
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getAccountAddress(String account) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定账户<account>关联的所有地址列表。
     * @param account
     * @return {@link List<String>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<String> getAddressesByAccount(String account) throws BitcoindException, CommunicationException;
    
    /**
     * 获取帐户余额，帐户名默认为""
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getBalance() throws BitcoindException, CommunicationException;
    
    /**
     * 获取钱包余额
     * @param account default ""
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getBalance(String account) throws BitcoindException, CommunicationException;
    
    /**
     * 获取钱包余额
     * @param account default ""
     * @param confirmations 确认数量
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getBalance(String account, Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * 获取钱包余额
     * @param account default ""
     * @param confirmations 确认数量
     * @param withWatchOnly
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getBalance(String account, Integer confirmations, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
    
    /**
     * 取最新的区块HASHID
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getBestBlockHash() throws BitcoindException, CommunicationException;
    
    /**
     * 根据区块HASHID取区块明细
     * <p>
     *     此接口默认返回解密过的区块信息
     * </p>
     * @param headerHash txID
     * @return {@link Block}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Block getBlock(String headerHash) throws BitcoindException, CommunicationException;
    
    /**
     * 根据区块HASHID取区块明细,根据isDecoded参数决定是否解密报文
     * @param headerHash txID
     * @param isDecoded 是否密码
     * @return {@link Object}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Object getBlock(String headerHash, Boolean isDecoded) throws BitcoindException, CommunicationException;
    
    /**
     * 获取区块总量
     * @return {@link Integer}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Integer getBlockCount() throws BitcoindException, CommunicationException;
    
    /**
     * 根据区块高度取区块HashID
     * @param blockHeight 区域高度
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getBlockHash(Integer blockHeight) throws BitcoindException, CommunicationException;
    
    /**
     * 返回当前工作量证明难度（最低难度的倍数）
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getDifficulty() throws BitcoindException, CommunicationException;
    
    /**
     * 判断当前客户端是否正在采矿。
     * @return {@link Boolean}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Boolean getGenerate() throws BitcoindException, CommunicationException;
    
    /**
     * 返回当前客户端每秒计算哈希的性能。
     * <p>
     *     当客户端开启采矿模式时
     * </p>
     * @return {@link Long}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Long getHashesPerSec() throws BitcoindException, CommunicationException;
    
    /**
     * 返回一个包含当前客户端各种状态信息的对象。已废弃
     * @return {@link Info}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    @Deprecated
    Info getInfo() throws BitcoindException, CommunicationException;
    
    /**
     * 返回有关节点当前事务内存池的信息。
     * @return {@link MemPoolInfo}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    MemPoolInfo getMemPoolInfo() throws BitcoindException, CommunicationException;
    
    /**
     * 返回各种与采矿有关的信息。
     * @return {@link MiningInfo}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    MiningInfo getMiningInfo() throws BitcoindException, CommunicationException;
    
    /**
     * 返回一个新的比特币地址来接收支付。
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getNewAddress() throws BitcoindException, CommunicationException;
    
    /**
     * 返回一个新的比特币地址来接收支付。
     * <p>
     *     如果指定了一个帐户，收到的付款将记入该帐户。
     * </p>
     * @param account 钱包名称
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getNewAddress(String account) throws BitcoindException, CommunicationException;
    
    /**
     * 返回每个已连接节点的数据
     * @return {@link List}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<PeerNode> getPeerInfo() throws BitcoindException, CommunicationException;
    
    /**
     * 返回一个用于接收更改的新的比特币地址,这是用于原始事务，而不是正常使用。
     * @return
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getRawChangeAddress() throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定的交易ID<txid>的原始交易描述。
     * @param txId
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String getRawTransaction(String txId) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定的交易ID<txid>的原始交易描述。
     * @param txId
     * @param verbosity
     * @return {@link Object}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Object getRawTransaction(String txId, Integer verbosity) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定账户<account>上收到的收款交易总金额（不包括付款）
     * @param account
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getReceivedByAccount(String account) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定账户<account>上收到至少[confirmations]个确认的收款交易总金额（不包括付款）
     * @param account
     * @param confirmations
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getReceivedByAccount(String account, Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定地址<bitcoinaddress>上收到的收款交易总金额
     * @param address
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getReceivedByAddress(String address) throws BitcoindException, CommunicationException;
    
    /**
     * 返回指定地址<bitcoinaddress>上收到至少[confirmations]个确认的收款交易总金额
     * @param address
     * @param confirmations
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getReceivedByAddress(String address, Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * 获取有关钱包交易的详细信息
     * @param txId 交易HASHID
     * @return {@link Transaction}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Transaction getTransaction(String txId) throws BitcoindException, CommunicationException;
    
    /**
     * 获取有关钱包交易的详细信息
     * @param txId 交易HASHID
     * @param withWatchOnly
     * @return {@link Transaction}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Transaction getTransaction(String txId, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
    
    /**
     * 获取钱包未确认的总额
     * @return {@link BigDecimal}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    BigDecimal getUnconfirmedBalance() throws BitcoindException, CommunicationException;
    
    /**
     * 获取有关钱包的信息。
     * @return {@link WalletInfo}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    WalletInfo getWalletInfo() throws BitcoindException, CommunicationException;
    
    /**
     * 查询账户列表,返回一个Hash对象，帐户名作为键，帐户余额作为键的值。
     * @return {@link Map}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Map<String, BigDecimal> listAccounts() throws BitcoindException, CommunicationException;
    
    /**
     * 查询账户列表,返回一个Hash对象，帐户名作为键，帐户余额作为键的值。
     * @param confirmations 确认数量
     * @return {@link Map}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Map<String, BigDecimal> listAccounts(Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * 查询账户列表,返回一个Hash对象，帐户名作为键，帐户余额作为键的值。
     * @param confirmations 确认数量
     * @param withWatchOnly 如果设置为true，那么在细节和计算中只包含钱包的地址，就好像它们是属于钱包的固定地址一样。
     *                      如果设置为false(默认值)，就像不属于这个钱包一样，只处理监视的地址。
     * @return {@link Map}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Map<String, BigDecimal> listAccounts(Integer confirmations, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
    
    /**
     * 返回钱包上的所有地址信息（地址，余额，所属账户）
     * @return {@link List<List<AddressOverview>>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<List<AddressOverview>> listAddressGroupings() throws BitcoindException, CommunicationException;

    /**
     * 列出锁定的未动用输出
     * <p>
     *     返回暂时未动用的交易输出列表。
     * </p>
     * @return {@link List<OutputOverview>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<OutputOverview> listLockUnspent() throws BitcoindException, CommunicationException;
    
    /**
     * 列出账户的收款信息
     * <p>
     *     返回一个数组对象，包含：account：接收地址的帐户amount：该账户所有地址收到的总金额confirmations：包含的最近交易确认数量
     * </p>
     * @return {@link List<Account>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Account> listReceivedByAccount() throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listReceivedByAccount()}
     * @param confirmations
     * @return {@link List<Account>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Account> listReceivedByAccount(Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listReceivedByAccount()}
     * @param confirmations
     * @param withUnused
     * @return {@link List<Account>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listReceivedByAccount()}
     * @param confirmations
     * @param withUnused
     * @param withWatchOnly
     * @return {@link List<Account>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
    
    /**
     * 列出地址的收款信息
     * <p>
     *     返回一个数组对象，包含：address：接收地址account：接收地址的帐户amount：地址收到的总金额confirmations：包含的最近交易确认数量要得到系统上的所有帐户的列表
     * </p>
     * @return {@link List<Address>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Address> listReceivedByAddress() throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listReceivedByAddress()}
     * @param confirmations
     * @return {@link List<Address>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Address> listReceivedByAddress(Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listReceivedByAddress()}
     * @param confirmations
     * @param withUnused
     * @return {@link List<Address>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listReceivedByAddress()}
     * @param confirmations
     * @param withUnused
     * @param withWatchOnly
     * @return {@link List<Address>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
    
    /**
     * 获得了所有影响到钱包的事务，这是由于一个特定的块，加上一个特定深度的块的头哈希。
     * @return {@link SinceBlock}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SinceBlock listSinceBlock() throws BitcoindException, CommunicationException;
    
    /**
     * 列出指定块之后的交易
     * <p>
     *     获得自从指定哈希块[blockhash]产生之后的所有交易，哈希块参数省略则获得所有交易。
     * </p>
     * @return {@link SinceBlock}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SinceBlock listSinceBlock(String headerHash) throws BitcoindException, CommunicationException;

    /**
     * {@link BtcdClient#listSinceBlock(java.lang.String)}
     * @param headerHash
     * @param confirmations
     * @return {@link SinceBlock}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SinceBlock listSinceBlock(String headerHash, Integer confirmations) throws BitcoindException, CommunicationException;

    /**
     * {@link BtcdClient#listSinceBlock(java.lang.String)}
     * @param headerHash
     * @param confirmations
     * @param withWatchOnly
     * @return {@link SinceBlock}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SinceBlock listSinceBlock(String headerHash, Integer confirmations, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
    
    /**
     * 列出交易信息
     * <P>
     *     返回所有账户的最近交易。
     * </P>
     * @return {@link List<Payment>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Payment> listTransactions() throws BitcoindException, CommunicationException;
    
    /**
     * 列出交易信息
     * <P>
     *     返回指定账户[account]不包含前[from]次的最近[count]次的交易。如果未指定账户则返回所有账户的最近交易。
     * </P>
     * @param account
     * @return {@link List<Payment>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Payment> listTransactions(String account) throws BitcoindException, CommunicationException;
    
    /**
     * {@link com.blocain.bitms.payment.btc.core.client.BtcdClient#listTransactions(java.lang.String)}
     * @param account
     * @param count
     * @return {@link List<Payment>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Payment> listTransactions(String account, Integer count) throws BitcoindException, CommunicationException;
    
    /**
     * {@link com.blocain.bitms.payment.btc.core.client.BtcdClient#listTransactions(java.lang.String)}
     * @param account
     * @param count
     * @param offset
     * @return {@link List<Payment>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Payment> listTransactions(String account, Integer count, Integer offset) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listTransactions(java.lang.String)}
     * @param account
     * @param count
     * @param offset
     * @param withWatchOnly
     * @return {@link List<Payment>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Payment> listTransactions(String account, Integer count, Integer offset, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
    
    /**
     * 列出未动用输出
     * <p>
     *     返回钱包中未动用交易输入的数组。
     * </p>
     * @return {@link List<Output>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Output> listUnspent() throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listUnspent()}
     * @param minConfirmations
     * @return {@link List<Output>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Output> listUnspent(Integer minConfirmations) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listUnspent()}
     * @param minConfirmations
     * @param maxConfirmations
     * @return {@link List<Output>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#listUnspent()}
     * @param minConfirmations
     * @param maxConfirmations
     * @param addresses
     * @return {@link List<Output>}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations, List<String> addresses) throws BitcoindException, CommunicationException;
    
    /**
     * 锁定未动用输出
     * @param isUnlocked
     * @return {@link Boolean}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Boolean lockUnspent(Boolean isUnlocked) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#lockUnspent(java.lang.Boolean)}
     * @param isUnlocked
     * @param outputs
     * @return {@link Boolean}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Boolean lockUnspent(Boolean isUnlocked, List<OutputOverview> outputs) throws BitcoindException, CommunicationException;
    
    /**
     * 转账 把钱包中一个账户<fromaccount>上的指定金额<amount> 转移到另一个账户<toaccount>上
     * @param fromAccount
     * @param toAccount
     * @param amount
     * @return {@link Boolean}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    @Deprecated
    Boolean move(String fromAccount, String toAccount, BigDecimal amount) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#move(java.lang.String, java.lang.String, java.math.BigDecimal)}
     * @param fromAccount
     * @param toAccount
     * @param amount
     * @param dummy
     * @param comment
     * @return {@link Boolean}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    @Deprecated
    Boolean move(String fromAccount, String toAccount, BigDecimal amount, Integer dummy, String comment) throws BitcoindException, CommunicationException;
    
    /**
     * 设置交易费率
     * @param txFee 费率
     * @return {@link Boolean}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Boolean setTxFee(BigDecimal txFee) throws BitcoindException, CommunicationException;
    
    /**
     * 从账户付款
     * <p>
     *     从指定账户<fromaccount>向指定地址<tobitcoinaddress> 发送指定金额<amount>的BTC，确保帐户拥有得到[minconf]个确认的有效余额。
     *     支付金额是一个四舍五入至小数点后8位的实数。如果支付成功，将返回交易ID<txid>（而不是一个JSON对象）。需要未锁定钱包
     * </p>
     * @param fromAccount
     * @param toAddress
     * @param amount
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendFrom(String fromAccount, String toAddress, BigDecimal amount) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#sendFrom(java.lang.String, java.lang.String, java.math.BigDecimal)}
     * @param fromAccount
     * @param toAddress
     * @param amount
     * @param confirmations
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendFrom(String fromAccount, String toAddress, BigDecimal amount, Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#sendFrom(java.lang.String, java.lang.String, java.math.BigDecimal)}
     * @param fromAccount
     * @param toAddress
     * @param amount
     * @param confirmations
     * @param comment
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendFrom(String fromAccount, String toAddress, BigDecimal amount, Integer confirmations, String comment) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#sendFrom(java.lang.String, java.lang.String, java.math.BigDecimal)}
     * @param fromAccount
     * @param toAddress
     * @param amount
     * @param confirmations
     * @param comment
     * @param commentTo
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendFrom(String fromAccount, String toAddress, BigDecimal amount, Integer confirmations, String comment, String commentTo)
            throws BitcoindException, CommunicationException;
    
    /**
     * 向多个地址付款
     * <p>
     *     从指定账户<fromaccount>向多个地址发送指定金额{address:amount,...}。金额是双精度浮点数。需要未锁定钱包
     * </p>
     * @param fromAccount
     * @param toAddresses
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#sendMany(java.lang.String, java.util.Map)}
     * @param fromAccount
     * @param toAddresses
     * @param confirmations
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses, Integer confirmations) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#sendMany(java.lang.String, java.util.Map)}
     * @param fromAccount
     * @param toAddresses
     * @param confirmations
     * @param comment
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses, Integer confirmations, String comment) throws BitcoindException, CommunicationException;
    
    /**
     * 发布原始交易
     * <p>
     *     提交原始交易（序列化的十六进制编码）到本地节点和网络。
     * </p>
     * @param hexTransaction
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendRawTransaction(String hexTransaction) throws BitcoindException, CommunicationException;
    
    /**
     * {@link BtcdClient#sendRawTransaction(java.lang.String)}
     * @param hexTransaction
     * @param withHighFees 是否允许高费率
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendRawTransaction(String hexTransaction, Boolean withHighFees) throws BitcoindException, CommunicationException;
    
    /**
     * 发送指定的金额到具体的地址
     * @param toAddress 接收地址
     * @param amount 发送金额
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendToAddress(String toAddress, BigDecimal amount) throws BitcoindException, CommunicationException;
    
    /**
     * 发送指定的金额到具体的地址
     * @param toAddress 接收地址
     * @param amount 发送金额
     * @param comment 备注
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendToAddress(String toAddress, BigDecimal amount, String comment) throws BitcoindException, CommunicationException;
    
    /**
     * 发送指定的金额到具体的地址
     * @param toAddress 接收地址
     * @param amount 发送金额
     * @param comment 备注
     * @param commentTo 备注2
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String sendToAddress(String toAddress, BigDecimal amount, String comment, String commentTo) throws BitcoindException, CommunicationException;
    
    /**
     * 信息签名 需要未锁定钱包。
     * <p>
     *     用地址<bitcoinaddress>的私钥对信息<message>进行数字签名。
     * </p>
     * @param address BTC地址
     * @param message 消息
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String signMessage(String address, String message) throws BitcoindException, CommunicationException;
    
    /**
     * 对原始交易签名
     * <p>
     *     添加原始交易的签名，并返回原始交易的结果。
     * </p>
     * @param hexTransaction
     * @return {@link SignatureResult}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SignatureResult signRawTransaction(String hexTransaction) throws BitcoindException, CommunicationException;
    
    /**
     * 对原始交易签名
     * <p>
     *     添加原始交易的签名，并返回原始交易的结果。
     * </p>
     * @param hexTransaction
     * @param outputs 没有用完的交易输出细节
     * @return {@link SignatureResult}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs) throws BitcoindException, CommunicationException;
    
    /**
     * 对原始交易签名
     * <p>
     *     添加原始交易的签名，并返回原始交易的结果。
     * </p>
     * @param hexTransaction
     * @param outputs 没有用完的交易输出细节
     * @param privateKeys 私钥
     * @return {@link SignatureResult}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs, List<String> privateKeys) throws BitcoindException, CommunicationException;
    
    /**
     * 对原始交易签名
     * <p>
     *     添加原始交易的签名，并返回原始交易的结果。
     * </p>
     * @param hexTransaction
     * @param outputs 没有用完的交易输出细节
     * @param privateKeys 私钥
     * @param sigHashType 签名类型
     * @return {@link SignatureResult}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs, List<String> privateKeys, String sigHashType)
            throws BitcoindException, CommunicationException;
    
    /**
     * 验证BTC地址，验证通过并返回地址详情
     * @param address 地址
     * @return {@link Address}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    AddressInfo validateAddress(String address) throws BitcoindException, CommunicationException;
    
    /**
     * 验证签名的消息
     * @param address 地址
     * @param signature 签名信息
     * @param message 签名过的消息
     * @return {@link Boolean}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    Boolean verifyMessage(String address, String signature, String message) throws BitcoindException, CommunicationException;
    
    /**
     * 设定账户
     * <p>
     *     将地址<bitcoinaddress>关联到指定帐户<account>
     * </p>
     * @param address
     * @param account
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void setAccount(String address, String account) throws BitcoindException, CommunicationException;
    
    /**
     * 设定是否采矿
     * @param isGenerate
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void setGenerate(Boolean isGenerate) throws BitcoindException, CommunicationException;
    
    /**
     * 设定是否采矿
     * @param isGenerate
     * @param processors 纯程数
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void setGenerate(Boolean isGenerate, Integer processors) throws BitcoindException, CommunicationException;
    
    /**
     * 加密钱包
     * @param passphrase 密码
     * @return {@link String}
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String encryptWallet(String passphrase) throws BitcoindException, CommunicationException;
    
    /**
     * 备份钱包
     * @param filePath 文件地址
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void backupWallet(String filePath) throws BitcoindException, CommunicationException;
    
    /**
     * 创建或覆盖具有可读格式的所有钱包密钥的文件。
     * @param filePath 文件地址
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void dumpWallet(String filePath) throws BitcoindException, CommunicationException;
    
    /**
     * 从内存中移除钱包加密密钥，锁定钱包。
     * <p>
     *     调用此方法后，您需要再次调用walletPassphrase才能调用任何需要解锁钱包的方法。
     * </p>
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void walletLock() throws BitcoindException, CommunicationException;
    
    /**
     * 将钱包解密密钥存储在内存中指定的秒数。
     * <p>
     *     在钱包已解锁的情况下发出命令将设置新的解锁时间，以覆盖旧的解锁时间。
     * </p>
     * @param passphrase 钱包密码
     * @param authTimeout 解锁时间(秒)
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void walletPassphrase(String passphrase, Integer authTimeout) throws BitcoindException, CommunicationException;
    
    /**
     * 将钱包密码从“旧密码”更改为“新密码”
     * @param curPassphrase 当前密码
     * @param newPassphrase 新密码
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void walletPassphraseChange(String curPassphrase, String newPassphrase) throws BitcoindException, CommunicationException;
    
    /**
     * 在没有相关私钥的情况下向钱包添加一个地址或pubkey脚本
     * <p>
     *     允许您监视影响该地址或pubkey脚本的事务，而不能够花费其任何输出。
     * </p>
     * @param address 地址
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void importAddress(String address) throws BitcoindException, CommunicationException;
    
    /**
     * 在没有相关私钥的情况下向钱包添加一个地址或pubkey脚本
     * <p>
     *     允许您监视影响该地址或pubkey脚本的事务，而不能够花费其任何输出。
     * </p>
     * @param address 地址
     * @param account 钱包ID
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void importAddress(String address, String account) throws BitcoindException, CommunicationException;
    
    /**
     * 在没有相关私钥的情况下向钱包添加一个地址或pubkey脚本
     * <p>
     *     允许您监视影响该地址或pubkey脚本的事务，而不能够花费其任何输出。
     * </p>
     * @param address 地址
     * @param account 钱包ID
     * @param withRescan 是否重新扫描整个本地块数据库
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void importAddress(String address, String account, Boolean withRescan) throws BitcoindException, CommunicationException;
    
    /**
     * 导入私钥，密钥应该以dumpprivkey RPC创建的钱包导入格式进行格式化。
     * @param privateKey 私钥
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void importPrivKey(String privateKey) throws BitcoindException, CommunicationException;
    
    /**
     * 导入私钥，密钥应该以dumpprivkey RPC创建的钱包导入格式进行格式化。
     * @param privateKey 私钥
     * @param account 钱包
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void importPrivKey(String privateKey, String account) throws BitcoindException, CommunicationException;
    
    /**
     * 导入私钥，密钥应该以dumpprivkey RPC创建的钱包导入格式进行格式化。
     * @param privateKey 私钥
     * @param account 钱包
     * @param withRescan 是否重新扫描整个本地块数据库
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void importPrivKey(String privateKey, String account, Boolean withRescan) throws BitcoindException, CommunicationException;
    
    /**
     * 从文件夹转储文件格式(见dumpwallet RPC)中导入私有密钥。这些密钥将被添加到当前钱包的密钥中。
     * <p>
     *     这个调用可能需要重新扫描区块链的全部或部分，以处理影响新添加的键的事务，这可能需要几分钟。
     * </p>
     * @param filePath
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void importWallet(String filePath) throws BitcoindException, CommunicationException;
    
    /**
     * 重新填满密钥池。需要未锁定钱包
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void keyPoolRefill() throws BitcoindException, CommunicationException;
    
    /**
     * 重新填满密钥池。需要未锁定钱包
     * @param keypoolSize 密钥池大小
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void keyPoolRefill(Integer keypoolSize) throws BitcoindException, CommunicationException;
    
    /**
     * 向所有连接的节点发送P2P ping消息以测量ping时间。
     * @throws BitcoindException
     * @throws CommunicationException
     */
    void ping() throws BitcoindException, CommunicationException;
    
    /**
     * 停止钱包服务
     * @return
     * @throws BitcoindException
     * @throws CommunicationException
     */
    String stop() throws BitcoindException, CommunicationException;
    
    /**
     * 关闭rpc客户端连接
     */
    void close();
    
    /**
     * 加载钱包的配置文件
     * @return
     */
    Properties getNodeConfig();
    
    /**
     * 取节点版本
     * @return
     */
    String getNodeVersion();
}