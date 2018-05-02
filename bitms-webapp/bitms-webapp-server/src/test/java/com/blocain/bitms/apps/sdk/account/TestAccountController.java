package com.blocain.bitms.apps.sdk.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.account.beans.RegisterRequest;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.account.controller.AccountController;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.sdk.*;
import com.blocain.bitms.apps.sdk.consts.BitMsTestConst;
import com.blocain.bitms.apps.sdk.domain.Account;
import com.blocain.bitms.apps.sdk.domain.AccountReqModel;
import com.blocain.bitms.apps.sdk.domain.PaginModel;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.tools.utils.JsonUtils;
import net.sf.json.util.JSONUtils;
import org.junit.Test;

/**
 * <p>Author：yukai </p>
 * <p>Description: AccountController 中方法测试 </p>
 * <p>Date: Create in 9:15 2018/3/21</p>
 * <p>Modify By: yukai</p>
 *
 * @version 1.0
 */
public class TestAccountController {

    public String root = BitMsTestConst.ROOT;

    public String auth_token = BitMsTestConst.AUTH_TOKEN;

    public String username = "robot@biex.com";

    public String password = "123456Biex";

    /**
     * 4.1获取帐户信息 200
     */
    @Test
    public void testGetInfo() {
        BitmsClient client = new DefaultBitmsClient(root + "account/info", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setEmail(username);
        accountReqModel.setAuth_token(auth_token);
        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 3.4帐户注册 200
     */
    @Test
    public void testRegister() {
        BitmsClient client = new DefaultBitmsClient(root + "register", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setEmail("yukai0625@foxmail.com");//邮箱必须是未注册过的
//        accountReqModel.setAuth_token(auth_token);
        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 3.5注册校验 200
     */
    @Test
    public void testCheck() {
        BitmsClient client = new DefaultBitmsClient(root + "register/check", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setCode("241128");//邮箱验证码？
        accountReqModel.setAccess_token("i0M0P3fWKqJMRZz4n8mXueC3EbTl6ENB94M9QOctdlM=");//从上一个测试方法中获取

        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 3.6设置密码 200
     */
    @Test
    public void testSetPass() {
        BitmsClient client = new DefaultBitmsClient(root + "register/setPass", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);//需要加密
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setPassword("123456");
        accountReqModel.setAuth_token(auth_token);

        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 3.1帐户登陆 200
     */
    @Test
    public void testLogin() {
        BitmsClient client = new DefaultBitmsClient(root + "login", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);//需要加密
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setUsername(username);
        accountReqModel.setPassword(password);

        accountReqModel.setAuth_token(auth_token);
        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 3.2登陆二次验证 200
     */
    @Test
    public void testLoginCheck() {
        BitmsClient client = new DefaultBitmsClient(root + "login/check", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);//需要加密
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setCode("352552");//GA或者手机短信验证码
        accountReqModel.setAccess_token("pMGXqqNSWe+0oOXryG63q/YbwrPJNLxN");
        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 3.3发送登陆短信 200
     */
    @Test
    public void testSendSMS() {
        BitmsClient client = new DefaultBitmsClient(root + "login/sendSms", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);//需要加密
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setAccess_token("pMGXqqNSWe+iOMN7GiJ4rKrCL1AaROIq");
        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 测试登出
     */
    @Test
    public void testSignOut(){
        BitmsClient client = new DefaultBitmsClient(root + "account/signOut", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);//需要加密
        AccountReqModel accountReqModel = new AccountReqModel();
        accountReqModel.setAuth_token("G6/3qGFN9TX7JVerr/NCdA1I0NclXliE0Tluybe3sCW+8554CRQ9MfGUqY0uITD11vk5XBoAU7i6zRUbj/1IuOmsLvD3naW+clicjoRWSVJg7zSfanZNanUCpgZvR7rt9Ic79dpCibfxlKmNLiEw9QgHp3ttHfWHywxF7qoFnFE=");

        request.setBizModel(accountReqModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
//        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
//        System.out.println(content);
    }

    //解密
    @Test
    public void testDes(){
        String desStr = "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";
        String obj = Encrypt.decryptContent(desStr, BitmsConstants.ENCRYPT_TYPE_AES, "1522043507677");
        System.out.println(obj);
    }
    //加密
    @Test
    public void testEns(){
        String ensStr = "[{'accountId':70403583396286464,'accountName':'sheng.zhibo@163.com','amount':0,'canBorrow':'no','canConversion':'no','canRecharge':'yes','canTrade':'no','canWithdraw':'yes','chargedTotal':0,'direction':'Long','frozenAmt':0,'id':70403711758766080,'isExchange':'no','price':1,'relatedStockinfoId':100000000002,'remark':'注册自动开通钱包账户 EUR','stockCode':'EUR','stockName':'Euro','stockType':'cashCoin','stockinfoId':100000000002,'updateDate':1521530183920,'withdrawedTotal':0,'withdrawingTotal':0},{'accountId':70403583396286464,'accountName':'sheng.zhibo@163.com','amount':0,'canBorrow':'no','canConversion':'yes','canRecharge':'yes','canTrade':'no','canWithdraw':'yes','chargedTotal':0,'direction':'Long','frozenAmt':0,'id':70403711746183168,'isExchange':'no','price':1,'relatedStockinfoId':111111111101,'remark':'注册自动开通钱包账户 BTC','stockCode':'BTC','stockName':'Bitcoin','stockType':'digitalCoin','stockinfoId':111111111101,'updateDate':1521530183902,'withdrawedTotal':0,'withdrawingTotal':0},{'accountId':70403583396286464,'accountName':'sheng.zhibo@163.com','amount':0,'canBorrow':'no','canConversion':'no','canRecharge':'yes','canTrade':'no','canWithdraw':'yes','chargedTotal':0,'direction':'Long','frozenAmt':0,'id':70403711771348992,'isExchange':'no','price':1,'relatedStockinfoId':111111111102,'remark':'注册自动开通钱包账户 ETH','stockCode':'ETH','stockName':'ETH','stockType':'erc20Token','stockinfoId':111111111102,'updateDate':1521530183923,'withdrawedTotal':0,'withdrawingTotal':0},{'accountId':70403583396286464,'accountName':'sheng.zhibo@163.com','amount':0,'canBorrow':'no','canConversion':'no','canRecharge':'yes','canTrade':'no','canWithdraw':'yes','chargedTotal':0,'direction':'Long','frozenAmt':0,'id':70403711762960384,'isExchange':'no','price':1,'relatedStockinfoId':122222222201,'remark':'注册自动开通钱包账户 BIEX','stockCode':'BIEX','stockName':'BIEX','stockType':'erc20Token','stockinfoId':122222222201,'updateDate':1521530183921,'withdrawedTotal':0,'withdrawingTotal':0}]";
        String obj = Encrypt.encryptContent(ensStr, BitmsConstants.ENCRYPT_TYPE_AES, "1522040951251");
        System.out.println(obj);
    }

}
