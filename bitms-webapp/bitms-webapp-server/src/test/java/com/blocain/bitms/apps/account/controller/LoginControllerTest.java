package com.blocain.bitms.apps.account.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.account.beans.LoginRequest;
import com.blocain.bitms.apps.account.model.CheckModel;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.sdk.*;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.orm.utils.EncryptUtils;
import org.junit.Test;

/**
 * Created by admin on 2018/3/22.
 */
public class LoginControllerTest
{
    public static String root = "http://localhost:8001/";

    @Test
    public void testAes()
    {
        String ret = Encrypt.encryptContent("test", BitmsConstants.ENCRYPT_TYPE_AES, "0123456789");
        System.out.println(ret);
        String ret2 = Encrypt.decryptContent(ret, BitmsConstants.ENCRYPT_TYPE_AES, "0123456789");
        System.out.println(ret2);
    }

    @Test
    public void testDes()
    {
        String ret = EncryptUtils.desEncrypt("test");
        System.out.println(ret);
        String ret2 = EncryptUtils.desDecrypt(ret);
        System.out.println(ret2);
    }
    
    @Test
    public void login() throws Exception
    {
        // 不需要二次验证
        BitmsClient client = new DefaultBitmsClient(root + "login", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("robot@biex.com");
        loginRequest.setPassword("123456Biex");
        request.setBizModel(loginRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        System.out.println(json);
        System.out.println(json.getJSONObject("data").getString("auth_token"));
//        System.out.println(json.getJSONObject("data").getString("auth_token").length());
//        String ret = EncryptUtils.desDecrypt(json.getJSONObject("data").getString("auth_token"));
//        System.out.println(ret);
        String ret2 = Encrypt.decryptContent(json.getJSONObject("data").getString("auth_token"), BitmsConstants.ENCRYPT_TYPE_AES, "0123456789");
        System.out.println(ret2);
        // 需要二次认证
        // BitmsClient client = new DefaultBitmsClient(root+"login", DemoController.privateKey, "0123456789");
        // BitmsRequest request = new BitmsRequest();
        // request.setNeedEncrypt(true);
        // LoginRequest loginRequest = new LoginRequest();
        // loginRequest.setUsername("zhangchunxi@qudao.so");
        // loginRequest.setPassword("Duanyi01");
        // request.setBizModel(loginRequest);
        // BitmsResponse response = client.execute(request);
        // JSONObject json = JSON.parseObject(response.getBody());
        // System.out.println(json);
        // {"msg":"Success","code":2001,"data":{"access_token":"pMGXqqNSWe+iOMN7GiJ4rGK6P7oEu2KQ","check_type":"GA"}}
    }
    
    @Test
    public void check() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "login/check", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(false);
        CheckModel checkModel = new CheckModel();
        checkModel.setCode("123456");
        checkModel.setPassword("Duanyi01");
        checkModel.setAccessToken("pMGXqqNSWe+iOMN7GiJ4rGK6P7oEu2KQ");
        request.setBizModel(checkModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        System.out.println(json);
    }
    
    @Test
    public void sendSMS() throws Exception
    {
    }
}