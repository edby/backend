import com.blocain.bitms.payment.eth.core.Transaction;
import org.bouncycastle.util.encoders.Hex;


/**
 * EthTransaction Introduce
 * <p>Title: EthTransaction</p>
 * <p>File：EthTransaction.java</p>
 * <p>Description: EthTransaction</p>
 * <p>Copyright: Copyright (c) 2018/4/18</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class EthTransaction
{
    public static byte[] senderPrivKey = Hex.decode("92987f40b5ae7626702d29b4168d953e56c8ea912ccbbc43bf9dedbc2eaec162");
    
    public static void main(String[] args) throws Exception
    {
        test1();
        test3();
    }
    
    public static void test1()
    {
        byte[] nonce = Hex.decode("00");
        byte[] gasPrice = Hex.decode("05");
        byte[] gasLimit = Hex.decode("5208");
        byte[] recieveAddress = Hex.decode("97507875cfbc24bf7f1a31dac4b65bf0805fa6c1");
        byte[] value = Hex.decode("038d7ea4c68000"); // 1000"
        byte[] data = Hex.decode("");
        Transaction tx1 = new Transaction(nonce, gasPrice, gasLimit, recieveAddress, value, data, 3);
        // System.out.println("Tx unsigned : " + Hex.toHexString(tx1.getEncoded()));
        tx1.sign(senderPrivKey);
        System.out.println("value    : " + Hex.toHexString(tx1.getValue()));
        System.out.println("nonce    : " + Hex.toHexString(tx1.getNonce()));
        System.out.println("receive  : " + Hex.toHexString(tx1.getReceiveAddress()));
        System.out.println("gasLimit : " + Hex.toHexString(tx1.getGasLimit()));
        System.out.println("gasPrice : " + Hex.toHexString(tx1.getGasPrice()));
        System.out.println("Tx signed: " + Hex.toHexString(tx1.getEncoded()));
    }
    
    // 签名过的原始交易
    public static void test3()
    {
        Transaction tx1 = new Transaction(Hex.decode(
                "f86680058252089497507875cfbc24bf7f1a31dac4b65bf0805fa6c187038d7ea4c680008029a0af0410720f88e10556ae4a603fc560cbcc3542049ca13792d57a3e87eb155b83a047c0e00abf317e3471626aab3d6175a00c8c3c2022abd217ee63e8b354ae7f34"));
        System.out.println("value    : " + Hex.toHexString(tx1.getValue()));
        System.out.println("nonce    : " + Hex.toHexString(tx1.getNonce()));
        System.out.println("receive  : " + Hex.toHexString(tx1.getReceiveAddress()));
        System.out.println("gasLimit : " + Hex.toHexString(tx1.getGasLimit()));
        // System.out.println("gasPrice : " + Hex.toHexString(tx1.getGasPrice()));
        System.out.println("Tx signed: " + Hex.toHexString(tx1.getEncoded()));
    }
}
