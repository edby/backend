package com.blocain.bitms.apps.fund.controller;

import com.blocain.bitms.apps.fund.model.WithdrawAddrModel;
import com.blocain.bitms.apps.fund.model.WithdrawModel;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.fund.model.WithdrawRequest;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:ConversionTest </p>
 * <p>Date: Create in 19:52 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawAddrTest {

    public static String root = "http://localhost:8080/";
    @Test
    public void getWithdrawAddr() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/getWithdrawAddr", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setSymbol("eth"); // btc eth biex
        withdrawRequest.setAuthToken("G6/3qGFN9TX7JVerr/NCdPf6vMXfIMhjfyBngkyGMVIV1zbq+NRad/obdNAUeqOlpsJIecrvaqpdY4IACaqADQJVZ+OhquA/4ypVxk07I+bT/5njgJpPymCAgVtNTMJ+0tyyiKTkNGavGtHbQt+NkZzNhVr2GaKfq4ZxBZf4v0/A9cWw9O2mOA8penwNQAmOTybkSpTg49DrDi2gghgRZto0oJu7dF03");
        request.setBizModel(withdrawRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    @Test
    public void withdrawAddrAdd() throws Exception
    {
        BitmsClient client = new DefaultBitmsClient(root + "fund/addWithdrawAddr", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawModel withdrawModel = new WithdrawModel();
        withdrawModel.setSymbol("btc"); // btc eth biex
        withdrawModel.setFundPwd("123456");
        withdrawModel.setSms("056307");
        withdrawModel.setGa("092227");
        withdrawModel.setCollectAddr("36gLuiT9uvgYxXHqEy1rdNdxmBDL2ZqPxc");
        withdrawModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdMRU87ojm9PlKRbHDslW5kFn9160UG12mSrjtOqMNrGa3ia3besz+PxDCeoZy5twqBLewgfZhhe7oTTCIV/c5TBFNqQKR9GwrPTxO75oiAoYdXme16Pm/uxu+PuGrGgLNij3Uo1nxTb5mD0RQLNAKRxwZg5/qEIvpRubBgYqLF3XlGq3TeQNSpvr4A14kdOM59kH+yRGILk9");
        request.setBizModel(withdrawModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }
}
