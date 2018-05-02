package com.blocain.bitms.apps.spot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;
import com.blocain.bitms.apps.spot.model.EntrustRequest;
import com.blocain.bitms.apps.spot.model.PaginModel;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.trade.enums.TradeEnums;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by admin on 2018/3/22.
 */
public class MatchTradeControllerTest
{

    public static String root = "http://localhost:8001/";

    public static String pair = "biex2btc";

    public static String auth_token = "G6/3qGFN9TX7JVerr/NCdA1I0NclXliE0Tluybe3sCW+8554CRQ9MfGUqY0uITD1CTYQAWHoyEY3BLwCORf4wOmsLvD3naW+clicjoRWSVJg7zSfanZNanUCpgZvR7rt9Ic79dpCibfxlKmNLiEw9WLsPKBQH0dgm68ODbVrTUU=";
    /**
     * 买入 200
     * @throws Exception
     */
    @Test
    public void doMatchBuyRequest() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"spot/doMatchBuy",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        EntrustRequest paginModel = new EntrustRequest();
        paginModel.setEntrustAmt(BigDecimal.ONE);
        paginModel.setEntrustPrice(BigDecimal.ONE);
        paginModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        paginModel.setPair(pair);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 卖出 200
     * @throws Exception
     */
    @Test
    public void doMatchSellRequest() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"spot/doMatchSell",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        EntrustRequest paginModel = new EntrustRequest();
        paginModel.setEntrustAmt(BigDecimal.ONE);
        paginModel.setEntrustPrice(BigDecimal.valueOf(8200));
        paginModel.setEntrustType(TradeEnums.ENTRUST_X_ENTRUST_TYPE_LIMITPRICE.getCode());
        paginModel.setPair(pair);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 委托取消   200
     * @throws Exception
     */
    @Test
    public void doMatchCancelRequest() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"spot/doMatchCancel",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        EntrustRequest paginModel = new EntrustRequest();
        paginModel.setEntrustId(75502572638572544L);
        paginModel.setPair(pair);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 当前委托列表   200
     * @throws Exception
     */
    @Test
    public void entrustxOnDoingRequest() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"spot/entrustxOnDoing",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(20);
        paginModel.setPair(pair);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 历史委托列表
     * @throws Exception
     */
    @Test
    public void entrustxOnHistoryRequest() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"spot/entrustxOnHistory",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(20);
        paginModel.setPair(pair);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        System.out.println(paginModel.toString());
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        System.out.println(json);
    }

    /**
     * 当前用户交易对下指标查询 200
     * @throws Exception
     */
    @Test
    public void getAccountFundAssetRequest() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"spot/getAccountFundAsset",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(20);
        paginModel.setPair(pair);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        System.out.println(paginModel.toString());
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        System.out.println(json);
    }

    /**
     * 历史委托列表   200
     * @throws Exception
     */
    @Test
    public void historyEntrust() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root+"spot/historyEntrust",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(20);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        System.out.println(paginModel.toString());
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        System.out.println(json);
    }

    /**
     * 溢价记录
     * @throws BusinessException
     */
    @Test
    public void getPremiumData() throws BusinessException{
        BitmsClient client = new DefaultBitmsClient(root+"spot/premiumData",  DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        PaginModel paginModel = new PaginModel();
        paginModel.setPage(1);
        paginModel.setRows(20);
        paginModel.setAuthToken(auth_token);
        request.setBizModel(paginModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        System.out.println(json);
    }

}