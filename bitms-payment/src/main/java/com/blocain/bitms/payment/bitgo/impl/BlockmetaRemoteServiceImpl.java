/*
 * @(#)BlockmetaRemoteImpl.java 2017年7月7日 下午2:37:41
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.bitgo.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.blocain.bitms.payment.basic.BasicServiceImpl;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.OutputModel;
import com.blocain.bitms.payment.bitgo.BlockmetaRemoteService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <p>File：BlockmetaRemoteImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 下午2:37:41</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Component("blockmetaRemote")
public class BlockmetaRemoteServiceImpl extends BasicServiceImpl implements BlockmetaRemoteService
{
    public static final String blockmeta_root               = properties.getProperty("blockmeta.root");
    
    public static final String blockmeta_wallet_trans_query = properties.getProperty("blockmeta.wallet.trans.query");
    
    /*
     * (non-Javadoc)
     * @see com.blocain.bitms.payment.remote.BlockmetaRemote#transQuery(java.lang.String)
     */
    @Override
    public BitPayModel transQuery(String transId) throws BusinessException
    {
        if (StringUtils.isBlank(transId)) { throw new BusinessException("交易ID不能空"); }
        HttpClient client = HttpUtils.getHttpClient();
        String url = MessageFormat.format(blockmeta_wallet_trans_query, blockmeta_root, transId);
        Map<String, String> param = Maps.newHashMap();
        param.put("q", "info");
        String content = HttpUtils.get(client, url, param);
        JSONObject json = this.validate(content);
        json = json.getJSONObject("data");
        BitPayModel bitPayModel = new BitPayModel();
        bitPayModel.setConfirmations(json.getInteger("confirmation"));
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
            String message = json.getString("message");
            if (StringUtils.isNotBlank(message)) { throw new BusinessException(message); }
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
                String amount = tempJson.getString("amount");
                String address = tempJson.getString("addr");
                if (StringUtils.isNotBlank(address) && StringUtils.isNotBlank(amount))
                {
                    OutputModel outputModel = new OutputModel();
                    outputModel.setAccount(address);
                    outputModel.setValue(new BigDecimal(amount).multiply(new BigDecimal(100000000)).longValue());
                    outputList.add(outputModel);
                }
            }
        }
        return outputList;
    }
    
    public static void main(String[] args)
    {
        try
        {
            BlockmetaRemoteServiceImpl br = new BlockmetaRemoteServiceImpl();
            BitPayModel bp = br.transQuery("76a0a618bfca5a2dfb7540754df8afe7629cd37c25b76eef8386645097859835");
            System.out.println(bp.toString());
            System.out.println("已确认区块节点数量:" + bp.getConfirmations());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
