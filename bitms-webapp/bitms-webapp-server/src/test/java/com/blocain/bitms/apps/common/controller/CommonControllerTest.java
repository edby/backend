package com.blocain.bitms.apps.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;
import com.blocain.bitms.apps.sdk.domain.PaginModel;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by admin on 2018/3/22.
 */
public class CommonControllerTest
{
    public static  String root = "http://localhost:8001/";

    @Test
    public void allCoin() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"common/stockinfo/allCoin",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(false);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONArray content = json.getJSONArray("data");
        System.out.println(content.toJSONString());
    }
    
    @Test
    public void allPair() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"common/stockinfo/allPair",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(false);
        PaginModel paginModel = new PaginModel();
        paginModel.setStockType(FundConsts.STOCKTYPE_PURESPOT);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONArray content = json.getJSONArray("data");
        System.out.println(content.toJSONString());
    }
    
    @Test
    public void dictionary() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"common/dictionary",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(false);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(content);
    }
    
    @Test
    public void notice() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"common/notice",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(false);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(20);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }
    
    @Test
    public void noticeDetail() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"common/noticeDetail",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(false);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(10);
        paginModel.setId(67134880722456576L);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 测试发送短信验证码
     */
    @Test
    public void testSendSMS(){
        BitmsClient client = new DefaultBitmsClient(root+"common/sendSMS",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(false);
        PaginModel paginModel = new PaginModel();
        paginModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdExFnPifyZe54LcRtOXoQ0GHYLbZBXN2juxfnOjsmvryGVpagbaFgzl2Tlr8EQyuGen4FTxAm3aqHPAWS3c1AiDAqkr3fH8R03h8Ks2sDvsIvj9fEfbnMW1c1/BjpQvW/zFdpDnTrS/aPPglXo07hmFZwI+tDi02rFdj67VgUcXskBvH/EZoX40TJMAyspfs9SCYGgWr4+lvseRK0Cev9HI=");
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        JSONObject content = json.getJSONObject("data");
        System.out.println(json);
        System.out.println(content);
    }

}