package com.blocain.bitms.apps.fund.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.fund.model.CurrentsRequest;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;
import org.junit.Test;

/**
 * <p>Author：XiaoDing</p>
 * <p>Description:财务流水</p>
 * <p>Date: Create in 9:45 2018/3/26</p>
 * <p>Modify By: XiaoDing</p>
 *
 * @version 1.0
 */
public class CurrentsControllerTest
{
    public static String root = "http://localhost:8080/";
    
    @Test
    public void currentsList() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/currents/currentsList", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        CurrentsRequest currents = new CurrentsRequest();
        currents.setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+fjC0lmdCKHFXa86ejw93dkusQ7zmJI1IcL7u9enc57k1CZE8Eb2MOgmH/QF/ipRVCauGcQWX+L9PlF5BG3+/RO2EI6rNQMj/CA==");
        currents.setSymbol("biex2btc");
        currents.setIsHis("no");
        request.setBizModel(currents);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }
}