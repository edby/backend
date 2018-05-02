/*
 * @(#)AbstractRemoteImpl.java 2017年7月7日 上午10:32:35
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.payment.basic;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.PropertiesUtils;
import com.blocain.bitms.tools.utils.StringUtils;

/**
 * <p>File：AbstractRemoteImpl.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月7日 上午10:32:35</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public abstract class BasicServiceImpl
{
    public static final Logger             logger           = LoggerFactory.getLogger(BasicServiceImpl.class);

    protected static final PropertiesUtils properties       = new PropertiesUtils("bitpay.properties");

    protected static final PropertiesUtils propertiesV2     = new PropertiesUtils("bitpayV2.properties");

    protected static final PropertiesUtils propertiesErc20  = new PropertiesUtils("erc20Token.properties");

    private static RequestConfig requestConfig = null;

    static
    {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
    }

    protected String httpPostWithJSON(HttpClient client, String url, Map<String, String> headerMap, JSONObject jsonParam, String charsetName) throws BusinessException
    {
        try
        {
            HttpPost httpPost = new HttpPost(url);
            if (MapUtils.isNotEmpty(headerMap))
            {
                for (Map.Entry<String, String> entry : headerMap.entrySet())
                {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            String respContent = null;
            String params = jsonParam.toString();
            StringEntity entity = new StringEntity(params, "utf-8");
            if (StringUtils.isNotBlank(charsetName))
            {
                entity.setContentEncoding(charsetName);
            }
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = client.execute(httpPost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode == 200)
            {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
            else
            {
                HttpEntity he = resp.getEntity();
                String error = EntityUtils.toString(he, "UTF-8");
                logger.error("请求失败：" + error);
                throw new BusinessException("BITGO-POST远程请求失败：" + error);
            }
            return respContent;
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    protected String httpGetWithJSON(HttpClient client, String url, Map<String, String> headerMap, JSONObject jsonParam, String charsetName) throws BusinessException
    {
        try
        {
            HttpGet httpPost = new HttpGet(url);
            if (MapUtils.isNotEmpty(headerMap))
            {
                for (Map.Entry<String, String> entry : headerMap.entrySet())
                {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            String respContent = null;
            String params = jsonParam.toString();
            StringEntity entity = new StringEntity(params, "utf-8");
            if (StringUtils.isNotBlank(charsetName))
            {
                entity.setContentEncoding(charsetName);
            }
            entity.setContentType("application/json");
            HttpResponse resp = client.execute(httpPost);
            int statusCode = resp.getStatusLine().getStatusCode();
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
            return respContent;
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    protected String httpDeleteWithJSON(HttpClient client, String url, Map<String, String> headerMap, JSONObject jsonParam, String charsetName) throws BusinessException
    {

        /**
         * 没有现成的delete可以带json的，自己实现一个，参考HttpPost的实现
         */
        class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase
        {
            public static final String METHOD_NAME = "DELETE";

            @SuppressWarnings("unused")
            public HttpDeleteWithBody() {
            }

            @SuppressWarnings("unused")
            public HttpDeleteWithBody(URI uri) {
                setURI(uri);
            }

            public HttpDeleteWithBody(String uri) {
                setURI(URI.create(uri));
            }

            public String getMethod() {
                return METHOD_NAME;
            }
        }
        try
        {
            HttpDeleteWithBody  httpDelete = new HttpDeleteWithBody (url);
            if (MapUtils.isNotEmpty(headerMap))
            {
                for (Map.Entry<String, String> entry : headerMap.entrySet())
                {
                    httpDelete.setHeader(entry.getKey(), entry.getValue());
                }
            }
            String respContent = null;
            String params = jsonParam.toString();
            StringEntity entity = new StringEntity(params, "utf-8");
            if (StringUtils.isNotBlank(charsetName))
            {
                entity.setContentEncoding(charsetName);
            }
            entity.setContentType("application/json");
            httpDelete.setEntity(entity);
            HttpResponse resp = client.execute(httpDelete);
            int statusCode = resp.getStatusLine().getStatusCode();
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he, "UTF-8");
            return respContent;
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    protected String httpPutWithJSON(HttpClient client, String url, Map<String, String> headerMap, JSONObject jsonParam, String charsetName) throws BusinessException
    {
        try
        {
            HttpPut httpPut = new HttpPut(url);
            if (MapUtils.isNotEmpty(headerMap))
            {
                for (Map.Entry<String, String> entry : headerMap.entrySet())
                {
                    httpPut.setHeader(entry.getKey(), entry.getValue());
                }
            }
            String respContent = null;
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
            if (StringUtils.isNotBlank(charsetName))
            {
                entity.setContentEncoding(charsetName);
            }
            entity.setContentType("application/json");
            httpPut.setEntity(entity);
            HttpResponse resp = client.execute(httpPut);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode == 200)
            {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
            else
            {
                HttpEntity he = resp.getEntity();
                String error = EntityUtils.toString(he, "UTF-8");
                logger.error("请求失败：" + error);
                throw new BusinessException("BITGO-PUT远程请求失败：" + error);
            }
            return respContent;
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * post请求传输String参数 例如：name=Jack&sex=1&type=2
     * Content-type:application/x-www-form-urlencoded
     * @param url            url地址
     * @param strParam       参数
     * @return
     */
    public static JSONObject httpPost(String url, String strParam)
    {
        // post请求返回结果
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try
        {
            if (null != strParam)
            {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                String str = "";
                try
                {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    // 把json字符串转换成json对象
                    jsonResult = JSONObject.parseObject(str);
                }
                catch (Exception e)
                {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        }
        catch (IOException e)
        {
            logger.error("post请求提交失败:" + url, e);
        }
        finally
        {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }

    /**
     * 发送get请求
     * @param url 路径
     * @return
     */
    public static JSONObject httpGet(String url)
    {
        // get请求返回结果
        JSONObject jsonResult = null;
        CloseableHttpClient client = HttpClients.createDefault();
        // 发送get请求
        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        try
        {
            CloseableHttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                // 把json字符串转换成json对象
                jsonResult = JSONObject.parseObject(strResult);
            }
            else
            {
                logger.error("get请求提交失败:" + url);
            }
        }
        catch (IOException e)
        {
            logger.error("get请求提交失败:" + url, e);
        }
        finally
        {
            request.releaseConnection();
        }
        return jsonResult;
    }

}
