package com.blocain.bitms.apps.fund.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.fund.model.ConversionEnableRequest;
import com.blocain.bitms.apps.fund.model.ConversionRequest;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;
import com.blocain.bitms.apps.spot.model.PaginModel;
import org.apache.commons.lang3.Conversion;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:ConversionTest </p>
 * <p>Date: Create in 19:52 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class ConversionTest {

    public static String root = "http://localhost:8080/";
    @Test
    public void spotAssetdata() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/conversionList", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(10);
        paginModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+flsl2tMYPGJkEMQ2n7oCtn2sQ7zmJI1IcL7u9enc57k1CZE8Eb2MOgmH/QF/ipRVCauGcQWX+L9PI4C4D6Ih1GkQEIzAQbROJA==");
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    @Test
    public void conversionEnableAmount() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/conversion", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setFromStockinfo("btc");
        conversionRequest.setToStockinfo("btc2usd");
        conversionRequest.setAmount(new BigDecimal(3));
        conversionRequest.setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+flsl2tMYPGJkEMQ2n7oCtn2sQ7zmJI1IcL7u9enc57k1CZE8Eb2MOgmH/QF/ipRVCauGcQWX+L9PI4C4D6Ih1GkQEIzAQbROJA==");
        request.setBizModel(conversionRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }
}
