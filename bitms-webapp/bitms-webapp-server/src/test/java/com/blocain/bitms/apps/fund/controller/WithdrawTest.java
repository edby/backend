package com.blocain.bitms.apps.fund.controller;

import com.blocain.bitms.apps.fund.model.*;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.apps.demo.controller.DemoController;
import com.blocain.bitms.apps.sdk.BitmsClient;
import com.blocain.bitms.apps.sdk.BitmsRequest;
import com.blocain.bitms.apps.sdk.BitmsResponse;
import com.blocain.bitms.apps.sdk.DefaultBitmsClient;

import java.math.BigDecimal;

/**
 * <p>Author：ChenGang</p>
 * <p>Description:ConversionTest </p>
 * <p>Date: Create in 19:52 2018/3/22</p>
 * <p>Modify By: ChenGang</p>
 *
 * @version 1.0
 */
public class WithdrawTest {

    public static String root = "http://localhost:8001/";

    @Test
    public void withdrawFee() throws Exception {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawFee", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setSymbol("btc");
        withdrawRequest.setAuthToken("G6/3qGFN9TX7JVerr/NCdPf6vMXfIMhjfyBngkyGMVIV1zbq+NRad/obdNAUeqOlpsJIecrvaqpdY4IACaqADQJVZ+OhquA/4ypVxk07I+bT/5njgJpPymCAgVtNTMJ+0tyyiKTkNGavGtHbQt+NkZzNhVr2GaKfq4ZxBZf4v0/A9cWw9O2mOA8penwNQAmOTybkSpTg49DrDi2gghgRZto0oJu7dF03");
        request.setBizModel(withdrawRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    @Test
    public void withdrawList() throws Exception {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawList", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawPaginRequest withdrawPaginRequest = new WithdrawPaginRequest();
        withdrawPaginRequest.setSymbol("btc");
        withdrawPaginRequest.setAuthToken("G6/3qGFN9TX7JVerr/NCdPf6vMXfIMhjfyBngkyGMVIV1zbq+NRad/obdNAUeqOlpsJIecrvaqpdY4IACaqADQJVZ+OhquA/4ypVxk07I+bT/5njgJpPymCAgVtNTMJ+0tyyiKTkNGavGtHbQt+NkZzNhVr2GaKfq4ZxBZf4v0/A9cWw9O2mOA8penwNQAmOTybkSpTg49DrDi2gghgRZto0oJu7dF03");
        withdrawPaginRequest.setPage(1);
        withdrawPaginRequest.setRows(10);
        request.setBizModel(withdrawPaginRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    @Test
    public void withdrawLimit() throws Exception {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawLimit", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setSymbol("btc");
        withdrawRequest.setAuthToken("G6/3qGFN9TX7JVerr/NCdPf6vMXfIMhjfyBngkyGMVIV1zbq+NRad/obdNAUeqOlpsJIecrvaqpdY4IACaqADQJVZ+OhquA/4ypVxk07I+bT/5njgJpPymCAgVtNTMJ+0tyyiKTkNGavGtHbQt+NkZzNhVr2GaKfq4ZxBZf4v0/A9cWw9O2mOA8penwNQAmOTybkSpTg49DrDi2gghgRZto0oJu7dF03");
        request.setBizModel(withdrawRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    @Test
    public void withdrawCancel() throws Exception {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawCancel", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawCancelRequest withdrawCancelRequest = new WithdrawCancelRequest();
        withdrawCancelRequest.setSymbol("eth");
        withdrawCancelRequest.setAuthToken("G6/3qGFN9TX7JVerr/NCdPf6vMXfIMhjfyBngkyGMVIV1zbq+NRad/obdNAUeqOlpsJIecrvaqpdY4IACaqADQJVZ+OhquA/4ypVxk07I+bT/5njgJpPymCAgVtNTMJ+0tyyiKTkNGavGtHbQt+NkZzNhVr2GaKfq4ZxBZf4v0/A9cWw9O2mOA8penwNQAmOTybkSpTg49DrDi2gghgRZto0oJu7dF03");
        withdrawCancelRequest.setId(72566326857568256L);
        request.setBizModel(withdrawCancelRequest);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 提币提现申请
     * @throws Exception
     */
    @Test
    public void withdraw() throws Exception {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawApply", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawApplyModel withdrawApplyModel = new WithdrawApplyModel();
        withdrawApplyModel.setSymbol("btc");
        withdrawApplyModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdPf6vMXfIMhjfyBngkyGMVIV1zbq+NRad/obdNAUeqOlpsJIecrvaqpdY4IACaqADQJVZ+OhquA/4ypVxk07I+bT/5njgJpPymCAgVtNTMJ+0tyyiKTkNGavGtHbQt+NkZzNhVr2GaKfq4ZxBZf4v0/A9cWw9O2mOA8penwNQAmOTybkSpTg49DrDi2gghgRZto0oJu7dF03");
        withdrawApplyModel.setFundPwd("960610Chen");
        withdrawApplyModel.setAmount(new BigDecimal(4));
        withdrawApplyModel.setCollectAddr("36gLuiT9uvgYxXHqEy1rdNdxmBDL2ZqPxc");
        request.setBizModel(withdrawApplyModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    @Test
    public void withdrawConfirm() throws Exception {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawConfirm", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawConfirmRequest withdrawConfirmModel = new WithdrawConfirmRequest();
        withdrawConfirmModel.setSymbol("btc");
        withdrawConfirmModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdPf6vMXfIMhjfyBngkyGMVIV1zbq+NRad/obdNAUeqOlpsJIecrvaqpdY4IACaqADQJVZ+OhquA/4ypVxk07I+bT/5njgJpPymCAgVtNTMJ+0tyyiKTkNGavGtHbQt+NkZzNhVr2GaKfq4ZxBZf4v0/A9cWw9O2mOA8penwNQAmOTybkSpTg49DrDi2gghgRZto0oJu7dF03");
        withdrawConfirmModel.setId("72585175942303744");
        withdrawConfirmModel.setConfirmCode("414952");
        request.setBizModel(withdrawConfirmModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

    /**
     * 测试6.13/14/15公用接口
     * @author:yukai
     */
    @Test
    public void testWithdrawCommon() {
        BitmsClient client = new DefaultBitmsClient(root + "fund/withdrawCommonInfo", DemoController.privateKey, "0123456789");
        BitmsRequest request = new BitmsRequest();
        request.setNeedEncrypt(true);
        WithdrawConfirmRequest withdrawConfirmModel = new WithdrawConfirmRequest();
        withdrawConfirmModel.setSymbol("btc");
        withdrawConfirmModel.setAuthToken("G6/3qGFN9TX7JVerr/NCdMcbf9A0MLnc0Tluybe3sCX5NgsI0ophny3niJcFCE7N7Xs/xQgM8x/+GlVCim5+fmMrB4xXJ6J9G6HuR94BqlCsQ7zmJI1IcL7u9enc57k1rxrR20LfjZGczYVa9hmin6uGcQWX+L9PCrkgzbjuxPfFrIEZ5hLOqQ==");
        request.setBizModel(withdrawConfirmModel);
        BitmsResponse response = client.execute(request);
        JSONObject json = JSON.parseObject(response.getBody());
        String content = json.getString("data");
        System.out.println(json);
        System.out.println(content);
    }

}
