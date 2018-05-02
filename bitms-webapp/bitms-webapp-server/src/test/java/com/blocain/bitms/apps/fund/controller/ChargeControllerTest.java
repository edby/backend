package com.blocain.bitms.apps.fund.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.fund.model.ChargeModelRequest;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:ChargeControllerTest </p>
 * <p>Date: Create in 18:15 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class ChargeControllerTest {

    public static String root = "http://localhost:8001/";

    @Test
    public void getRecharegeAddr() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/recharge/getRecharegeAddr", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        ChargeModelRequest chargeModelRequest = new ChargeModelRequest();
        chargeModelRequest.setPair("eth"); // btc eth biex
        chargeModelRequest.setSymbol("eth"); // btc eth biex
        chargeModelRequest.setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+fh05/cMvp5oLEMQ2n7oCtn2sQ7zmJI1IcL7u9enc57k1rxrR20LfjZGczYVa9hmin6uGcQWX+L9PVB14Yeo1sOqDD4ziU6Mhqw==");
        request.setBizModel(chargeModelRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);


    }

}
