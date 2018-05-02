/*
 * @(#)BtcRemoteImpl.java 2017年7月7日 下午2:05:48
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.impl;

import java.text.MessageFormat;
import java.util.List;

import com.blocain.bitms.payment.basic.BasicServiceImpl;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.OutputModel;
import com.blocain.bitms.payment.bitgo.BtcRemoteService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;

/**
 * <p>File：BtcRemoteImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 下午2:05:48</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Component("btcRemote")
public class BtcRemoteServiceImpl extends BasicServiceImpl implements BtcRemoteService
{
    public static final String btc_root                       = properties.getProperty("btc.root");
    
    public static final String btc_wallet_trans_query         = properties.getProperty("btc.wallet.trans.query");
    
    public static final String btc_wallet_address_query       = properties.getProperty("btc.wallet.address.query");
    
    public static final String btc_wallet_address_trans_query = properties.getProperty("btc.wallet.address.trans.query");
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BtcRemote#transQuery(java.lang.String)
     */
    @Override
    public BitPayModel transQuery(String transId) throws BusinessException
    {
        if (StringUtils.isBlank(transId)) { throw new BusinessException("交易ID不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(btc_wallet_trans_query, btc_root, transId);
        String content = HttpUtils.get(client, url);
        JSONObject json = this.validate(content);
        json = json.getJSONObject("data");
        BitPayModel bitPayModel = json.toJavaObject(BitPayModel.class);
        bitPayModel.setOutputs(this.getOutputList(json));
        return bitPayModel;
    }
    
    private JSONObject validate(String content) throws BusinessException
    {
        JSONObject json = null;
        try
        {
            logger.debug("validate content:" + content);
            json = JSONObject.parseObject(content);
            int errNo = json.getInteger("err_no");
            if (errNo > 0) { throw new BusinessException(json.getString("err_msg")); }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException("validate error:" + e.getMessage());
        }
        return json;
    }
    
    private List<OutputModel> getOutputList(JSONObject json)
    {
        List<OutputModel> outputList = Lists.newArrayList();
        JSONArray jsonArray = json.getJSONArray("outputs");
        if (!jsonArray.isEmpty())
        {
            for (int i = 0; i < jsonArray.size(); i++)
            {
                JSONObject tempJson = jsonArray.getJSONObject(i);
                Long value = tempJson.getLong("value");
                JSONArray tempArray = tempJson.getJSONArray("addresses");
                if (null != value && !tempArray.isEmpty())
                {
                    OutputModel outputModel = new OutputModel();
                    outputModel.setAccount(tempArray.getString(0));
                    outputModel.setValue(value);
                    outputList.add(outputModel);
                }
            }
        }
        return outputList;
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BtcRemote#addressQuery(java.lang.String)
     */
    @Override
    public BitPayModel addressQuery(String address) throws BusinessException
    {
        if (StringUtils.isBlank(address)) { throw new BusinessException("钱包地址不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(btc_wallet_address_query, btc_root, address);
        String content = HttpUtils.get(client, url);
        logger.debug("addressQuery content:" + content);
        JSONObject json = this.validate(content);
        json = json.getJSONObject("data");
        if(null != json){
            return json.toJavaObject(BitPayModel.class);
        }
        else {
            return null;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BtcRemote#addressTransQuery(java.lang.String)
     */
    @Override
    public List<BitPayModel> addressTransQuery(String address, Integer page, Integer pagesize) throws BusinessException
    {
        if (StringUtils.isBlank(address)) { throw new BusinessException("钱包地址不能空"); }
        // if (null != pagesize && pagesize > 50)
        // {
        // pagesize = 50;
        // }
        List<BitPayModel> bitPayList = Lists.newArrayList();
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(btc_wallet_address_trans_query, btc_root, address);
        String content = HttpUtils.get(client, url);
        JSONObject json = this.validate(content);
        json = json.getJSONObject("data");
        JSONArray jsonArray = json.getJSONArray("list");
        if (!jsonArray.isEmpty())
        {
            for (int i = 0; i < jsonArray.size(); i++)
            {
                bitPayList.add(json.toJavaObject(BitPayModel.class));
            }
        }
        return bitPayList;
    }

    public static void main(String[] args)
    {
        try
        {
            BtcRemoteServiceImpl br = new BtcRemoteServiceImpl();
            BitPayModel bp = br.transQuery("76a0a618bfca5a2dfb7540754df8afe7629cd37c25b76eef8386645097859835");
            System.out.println("transQuery:" + bp.toString());
            Thread.sleep(1000);

            bp = br.addressQuery("15urYnyeJe3gwbGJ74wcX89Tz7ZtsFDVew");
            System.out.println("addressQuery:" + bp.toString());
            Thread.sleep(1000);

            List<BitPayModel> bps = br.addressTransQuery("15urYnyeJe3gwbGJ74wcX89Tz7ZtsFDVew", null, null);
            for (int index = 0; index < bps.size(); index++)
            {
                System.out.println("addressTransQuery:" + bps.get(index).toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
