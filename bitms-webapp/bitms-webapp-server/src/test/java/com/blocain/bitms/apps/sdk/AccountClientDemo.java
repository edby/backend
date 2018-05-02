package com.blocain.bitms.apps.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.sdk.domain.Account;

/**
 * AccountClientDemo Introduce
 * <p>Title: AccountClientDemo</p>
 * <p>File：AccountClientDemo.java</p>
 * <p>Description: AccountClientDemo</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AccountClientDemo
{
    public static void main(String[] args) throws Exception
    {
        BitmsClient client = new DefaultBitmsClient("http://localhost:8001/demo",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        Account account = new Account();
        account.setAccountName("wsgajl");
        account.setAccountNo("10001");
        account.setAccountType("VIP");
        account.setAccountExt("ExtInfo");
        request.setBizModel(account);
        System.out.println(JSON.toJSONString(account));
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("response");
        System.out.println(content.toJSONString());
    }
}
