package com.blocain.bitms.apps.fund.controller;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.fund.model.ConversionEnableRequest;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;
import com.blocain.bitms.apps.sdk.domain.PaginModel;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:AccountAssetControllerTest </p>
 * <p>Date: Create in 16:45 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class AccountAssetControllerTest
{
    public static String root = "http://localhost:8001/";

    /**
     * 200
     * @throws Exception
     */
    @Test
    public void walletAssetData() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/walletAssetData", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel .setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+fjC0lmdCKHFXa86ejw93dkusQ7zmJI1IcL7u9enc57k1CZE8Eb2MOgmH/QF/ipRVCauGcQWX+L9PlF5BG3+/RO2EI6rNQMj/CA==");
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 200
     * @throws Exception
     */
    @Test
    public void spotAssetdata() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/spotAssetData", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdMRU87ojm9PlKRbHDslW5kFn9160UG12mSrjtOqMNrGa3ia3besz+PxDCeoZy5twqGuF0lE6i0Y//6C1PGPXJLVFNqQKR9GwrPTxO75oiAoYdXme16Pm/uxu+PuGrGgLNij3Uo1nxTb5mD0RQLNAKRyCh+ZgthI0HkQZzvkb6VMNlGq3TeQNSpvr4A14kdOM59kH+yRGILk9");
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
        BitmsClient client = new DefaultBitmsClient(root + "fund/conversionEnableAmount", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        ConversionEnableRequest paginModel = new ConversionEnableRequest();
        paginModel.setFromStockinfo("btc");
        paginModel.setToStockinfo("btc2usd");
        paginModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+fkAT4pbAHheXYHDAQQOX+P6sQ7zmJI1IcL7u9enc57k1CZE8Eb2MOgmH/QF/ipRVCauGcQWX+L9PQsq1dR4n9hZLVjH7mEwKsw==");
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }
    
    @Test
    public void withdrawEnableAmount() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawEnableAmount", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        ConversionEnableRequest paginModel = new ConversionEnableRequest();
        paginModel.setFromStockinfo("btc");
        paginModel.setToStockinfo("btc2usd");
        paginModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+fkAT4pbAHheXYHDAQQOX+P6sQ7zmJI1IcL7u9enc57k1CZE8Eb2MOgmH/QF/ipRVCauGcQWX+L9PQsq1dR4n9hZLVjH7mEwKsw==");
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);


    }
}