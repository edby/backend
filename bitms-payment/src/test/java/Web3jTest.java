
import com.blocain.bitms.payment.eth.EthereumUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;

/**
 * Created by admin on 2018/2/27.
 */
public class Web3jTest
{
    public static void main(String args[])
    {
        Long aa = EthereumUtils.tokenDecimals("0x8406d5d0795417C42790991d03d638486bD10651");
        System.out.println("decimals="+aa);

//        BigDecimal a1 = EthereumUtils.tokenBalanceOf("0x8406d5d0795417C42790991d03d638486bD10651","0x8406d5d0795417C42790991d03d638486bD10651");
//        System.out.println(a1);

        HttpService httpService = new HttpService("http://192.168.31.122:8545");
        Web3j web3 = Web3j.build(httpService); // defaults to http://localhost:8545/

//        Web3ClientVersion web3ClientVersion = null;
//        try
//        {
//            web3ClientVersion = web3.web3ClientVersion().send();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
//        System.out.println(clientVersion);
        EthBlock a = null;
        EthGetBalance b = null;
        EthCall c = null;
        EthSendTransaction d = null;
        EthGetTransactionReceipt f = null;
        try
        {
            f = web3.ethGetTransactionReceipt("0x761e5f91b15851df339a43b512dcb17855a341c7a5725828aed8209b86dd16b1").send();
            System.out.println(f.getTransactionReceipt().get().getStatus());

            a = web3.ethGetBlockByHash("0x73a3c3e11aac935a944eeb1ace1e3de91dff3ba6b0fd03cde198fc242b11732e",true).send();
            System.out.println(a.getBlock().getHash());
//            b = web3.ethGetBalance("0x419abcd98e8b9182d07ff270a1502587887bb650",DefaultBlockParameter.valueOf("latest")).send();
//            System.out.println(b.getBalance());
// decimals
//            Transaction transaction = new Transaction(null, null, null, null,
//                    "0x8406d5d0795417C42790991d03d638486bD10651", null, "0x313ce567");
//            c = web3.ethCall(transaction,DefaultBlockParameter.valueOf("latest")).send();
//            System.out.println(c.getValue());
//            Long decimalsss = EthereumUtils.tokenDecimals("0x8406d5d0795417C42790991d03d638486bD10651");
//            System.out.println(decimalsss);
//查询账户token余额balanceOf
//            Transaction transaction2 = new Transaction(null, null, null, null,
//                    "0x8406d5d0795417C42790991d03d638486bD10651", null, "0x70a0823100000000000000000000000065d552a9d35d43c629742b94d71e251958275629");
//            c = web3.ethCall(transaction2,DefaultBlockParameter.valueOf("latest")).send();
//            System.out.println(c.getValue());
//            BigDecimal balance = EthereumUtils.tokenBalanceOf( "0x8406d5d0795417C42790991d03d638486bD10651", "0x65d552a9d35d43c629742b94d71e251958275629");
//            System.out.println(balance);
//查询token授权量allowance
//            Transaction transaction3 = new Transaction(null, null, null, null,
//                    "0x8406d5d0795417C42790991d03d638486bD10651", null, "0xdd62ed3e000000000000000000000000727d1f40143a56a987478d19aa79088e22e4355a00000000000000000000000065d552a9d35d43c629742b94d71e251958275629");
//            c = web3.ethCall(transaction3,DefaultBlockParameter.valueOf("latest")).send();
//            System.out.println(c.getValue());

//            BigDecimal allowwance = EthereumUtils.tokenAllowance("0x727d1f40143a56a987478d19aa79088e22e4355a", "0x8406d5d0795417C42790991d03d638486bD10651","0x5d552a9d35d43c629742b94d71e251958275629");
//            System.out.println(allowwance);
            // token授权操作
//
//            Transaction transaction41 = Transaction.createContractTransaction("0x727d1f40143a56a987478d19aa79088e22e4355a", null, BigInteger.valueOf(100000), (BigInteger)null, (BigInteger)null, "0xdd62ed3e000000000000000000000000727d1f40143a56a987478d19aa79088e22e4355a00000000000000000000000065d552a9d35d43c629742b94d71e251958275629");
//            d = web3.ethSendTransaction(transaction41).send();
//            System.out.println(d.getError().getMessage());

//            Admin admin = Admin.build(httpService);  // defaults to http://localhost:8545/
//            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount("0x727d1f40143a56a987478d19aa79088e22e4355a", "Bcbc963852").send();
//            System.out.println(personalUnlockAccount.accountUnlocked());
//            if (personalUnlockAccount.accountUnlocked()) {
//                Transaction transaction42 = Transaction.createContractTransaction("0x727d1f40143a56a987478d19aa79088e22e4355a",
//                        null, BigInteger.valueOf(100000), (BigInteger)null, (BigInteger)null,
//                        "0x095ea7b300000000000000000000000065d552a9D35d43c629742B94D71e251958275629000000000000000000000000000000000000000000000000000000000000000c");
//                d = web3.ethSendTransaction(transaction42).send();
//                System.out.println(d.getTransactionHash());
//           }

//            String approvemsg = EthereumUtils.tokenApprove("0x8406d5d0795417C42790991d03d638486bD10651","0x65d552a9d35d43c629742b94d71e251958275629", "Bcbc963852", "0x65d552a9D35d43c629742B94D71e251958275629", BigDecimal.valueOf(1));
//            System.out.println(approvemsg);
//测试授权后交易
            //Admin admin = Admin.build(httpService);  // defaults to http://localhost:8545/
//            PersonalUnlockAccount personalUnlockAccount2 = admin.personalUnlockAccount("0x65d552a9D35d43c629742B94D71e251958275629", "Bcbc963852").send();
//            System.out.println(personalUnlockAccount2.accountUnlocked());
//            if (personalUnlockAccount2.accountUnlocked()) {
//                Transaction transaction42 = Transaction.createContractTransaction("0x65d552a9D35d43c629742B94D71e251958275629", null, BigInteger.valueOf(100000000000L), (BigInteger)null, (BigInteger)null,
//                        "0x23b872dd000000000000000000000000727d1f40143a56a987478d19aa79088e22e4355a00000000000000000000000001ab5490b8571cca9ecb37cb3d19129ca2c1d16e00000000000000000000000000000000000000000000000000000000000003d3"
//                );
//                d = web3.ethSendTransaction(transaction42).send();
//                System.out.println(d.getTransactionHash());
//            }
//            String approvemsg = EthereumUtils.tokenTransferFrom("0x8406d5d0795417C42790991d03d638486bD10651",
//                    "0x65d552a9d35d43c629742b94d71e251958275629", "0x65d552a9D35d43c629742B94D71e251958275629",
//                    "Bcbc963852", "0x65d552a9d35d43c629742b94d71e251958275629",BigDecimal.valueOf(1));
//            System.out.println(approvemsg);
// ETH 发送
//            PersonalUnlockAccount personalUnlockAccount3 = admin.personalUnlockAccount("0x65d552a9D35d43c629742B94D71e251958275629", "Bcbc963852").send();
//            System.out.println(personalUnlockAccount3.accountUnlocked());
//            if (personalUnlockAccount3.accountUnlocked()) {
//                Transaction transaction43 = Transaction.createEtherTransaction("0x65d552a9D35d43c629742B94D71e251958275629",null,BigInteger.valueOf(900000),
//                        BigInteger.valueOf(900000),"0x65d552a9d35d43c629742b94d71e251958275629", BigDecimal.valueOf(Math.pow(10,18)).toBigInteger());
//                d = web3.ethSendTransaction(transaction43).send();
//                System.out.println(d.getTransactionHash());
//            }
            //(String fromAddress,String fromAddressPwd,String toAddress,BigDecimal amount)
//            EthereumUtils.ethSendTransaction("0x65d552a9D35d43c629742B94D71e251958275629", "Bcbc963852","0x65d552a9d35d43c629742b94d71e251958275629",BigDecimal.valueOf(0.001));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
