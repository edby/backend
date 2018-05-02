package com.blocain.bitms.boss.common.utils;

import com.blocain.bitms.boss.common.model.SMSModel;
import com.blocain.bitms.boss.common.model.SMSResult;
import com.blocain.bitms.orm.utils.EncryptUtils;
import org.apache.http.impl.client.CloseableHttpClient;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.HttpUtils;
import com.blocain.bitms.tools.utils.PropertiesUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * 短信发送客户端 Introduce
 * <p>File：SMSClient.java</p>
 * <p>Title: SMSClient</p>
 * <p>Description: SMSClient</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class SMSClient
{
    public static final PropertiesUtils properties        = new PropertiesUtils("sms.properties");
    
    public static final String          SEND_SIGNATURE_EN = properties.getProperty("sms.signature.en");
    
    public static final String          SEND_SIGNATURE_CN = properties.getProperty("sms.signature.cn");
    
    private SMSClient()
    {// 防止实例化
    }
    
    /**
     * 发送国内短信服务
     * @param mobile
     * @param content
     * @return {@link SMSResult}
     * @throws BusinessException
     */
    public static SMSResult sendSMS(String mobile, String content) throws BusinessException
    {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content))
        {// 防止空指针
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        SMSModel model = new SMSModel();
        model.setAccount(properties.getProperty("sms.send.user.prop"));
        model.setPassword(EncryptUtils.desDecrypt(properties.getProperty("sms.send.pass.prop")));
        model.setPhone(mobile);
        model.setMsg(content);
        model.setReport(true);
        if (StringUtils.isNotBlank(SEND_SIGNATURE_CN))
        {
            StringBuffer sms = new StringBuffer(content).append("【").append(SEND_SIGNATURE_CN).append("】");
            model.setMsg(sms.toString());
        }
        String params = JSON.toJSONString(model);
        CloseableHttpClient client = HttpUtils.getHttpClient2();
        String response = HttpUtils.postWithJSON(client, properties.getProperty("sms.send.host.prop"), params);
        HttpUtils.releaseHttpClient(client);
        if (StringUtils.isBlank(response)) return null;
        return JSON.parseObject(response, SMSResult.class);
    }
    
    /**
     * 发送国际短信服务
     * @param mobile
     * @param content
     * @return {@link SMSResult}
     * @throws BusinessException
     */
    public static SMSResult sendIntSMS(String mobile, String content) throws BusinessException
    {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content))
        {// 防止空指针
            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        SMSModel model = new SMSModel();
        model.setAccount(properties.getProperty("sms.send.intuser.prop"));
        model.setPassword(EncryptUtils.desDecrypt(properties.getProperty("sms.send.intpass.prop")));
        model.setMobile(mobile);
        if (StringUtils.isNotBlank(SEND_SIGNATURE_EN))
        {
            StringBuffer sms = new StringBuffer(content).append("【").append(SEND_SIGNATURE_EN).append("】");
            model.setMsg(sms.toString());
        }
        String params = JSON.toJSONString(model);
        CloseableHttpClient client = HttpUtils.getHttpClient2();
        String response = HttpUtils.postWithJSON(client, properties.getProperty("sms.send.inthost.prop"), params);
        HttpUtils.releaseHttpClient(client);
        if (StringUtils.isBlank(response)) return null;
        return JSON.parseObject(response, SMSResult.class);
    }
    
    public static void main(String[] args) throws BusinessException
    {
        SMSResult result = sendIntSMS("8615306620878", "BITMS SMS TEST");
        System.out.println(JSON.toJSONString(result));
    }
}
