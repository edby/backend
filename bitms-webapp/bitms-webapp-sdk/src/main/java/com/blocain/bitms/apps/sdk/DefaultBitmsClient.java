/**
 * blocain.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.blocain.bitms.apps.sdk;

import java.io.IOException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import com.blocain.bitms.apps.sdk.internal.json.JSONWriter;
import com.blocain.bitms.apps.sdk.internal.parser.ObjectJsonParser;
import com.blocain.bitms.apps.sdk.internal.util.*;

/**
 * 
 * @author playguy
 * @version $Id: DefaultBitmsClient.java, v 0.1 2012-11-49:45:21 playguy Exp $
 */
public class DefaultBitmsClient implements BitmsClient
{
    private String serverUrl;
    
    private String privateKey;
    
    private String format         = BitmsConstants.FORMAT_JSON;
    
    private String sign_type      = BitmsConstants.SIGN_TYPE_RSA2;
    
    private String encryptType    = BitmsConstants.ENCRYPT_TYPE_AES;
    
    private String charset        = BitmsConstants.CHARSET_UTF8;
    
    private String encryptKey;
    
    private int    connectTimeout = 3000;
    
    private int    readTimeout    = 15000;
    static
    {
        // 清除安全设置
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }
    
    /**
     * 默认构造方法
     * @param serverUrl 服务器地址
     * @param privateKey 私钥
     */
    public DefaultBitmsClient(String serverUrl, String privateKey)
    {
        this.serverUrl = serverUrl;
        this.privateKey = privateKey;
    }
    
    /**
     * 构造方法
     * @param serverUrl 服务器地址
     * @param privateKey 私钥
     */
    public DefaultBitmsClient(String serverUrl, String privateKey, String encryptKey)
    {
        this(serverUrl, privateKey);
        this.encryptKey = encryptKey;
    }
    
    /**
     * 构造方法
     * @param serverUrl
     * @param privateKey
     * @param format
     * @param charset
     */
    public DefaultBitmsClient(String serverUrl, String privateKey, String format, String charset)
    {
        this(serverUrl, privateKey);
        this.format = format;
        this.charset = charset;
    }
    
    /**
     * 构造方法
     * @param serverUrl
     * @param privateKey
     * @param format
     * @param charset
     * @param signType
     */
    public DefaultBitmsClient(String serverUrl, String privateKey, String format, String charset, String signType)
    {
        this(serverUrl, privateKey, format, charset);
        this.sign_type = signType;
    }
    
    /**
     * 构造方法
     * @param serverUrl
     * @param privateKey
     * @param format
     * @param charset
     * @param signType
     * @param encryptKey
     */
    public DefaultBitmsClient(String serverUrl, String privateKey, String format, String charset, String signType, String encryptKey)
    {
        this(serverUrl, privateKey, format, charset, signType);
        this.encryptKey = encryptKey;
    }

    /**
     *  发起一个常规请求
     * @param request
     * @param <T>
     * @return
     * @throws ApiException
     */
    public <T extends BitmsResponse> T execute(BasicRequest<T> request) throws ApiException
    {
        return execute(request, null);
    }

    /**
     * 发起一个带授权码的请求
     * @param request
     * @param authToken
     * @param <T>
     * @return
     * @throws ApiException
     */
    public <T extends BitmsResponse> T execute(BasicRequest<T> request, String authToken) throws ApiException
    {
        BitmsParser<T> parser = new ObjectJsonParser<T>(request.getResponseClass());
        return _execute(request, parser, authToken);
    }
    
    /**
     * SDK调用生成FORM表单
     * @param request
     * @param <T>
     * @return
     * @throws ApiException
     */
    public <T extends BitmsResponse> T pageExecute(BasicRequest<T> request) throws ApiException
    {
        return pageExecute(request, "POST");
    }
    
    /**
     * SDK调用生成FORM表单
     * @param request
     * @param httpMethod
     * @param <T>
     * @return
     * @throws ApiException
     */
    public <T extends BitmsResponse> T pageExecute(BasicRequest<T> request, String httpMethod) throws ApiException
    {
        RequestHolder requestHolder = getRequestHolderWithSign(request, null);
        // 打印完整请求报文
        if (BitmsLogger.isBizDebugEnabled())
        {
            BitmsLogger.logBizDebug(getRedirectUrl(requestHolder));
        }
        T rsp = null;
        try
        {
            Class<T> clazz = request.getResponseClass();
            rsp = clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            BitmsLogger.logBizError(e);
        }
        catch (IllegalAccessException e)
        {
            BitmsLogger.logBizError(e);
        }
        if ("GET".equalsIgnoreCase(httpMethod))
        {
            rsp.setBody(getRedirectUrl(requestHolder));
        }
        else
        {
            String baseUrl = getRequestUrl(requestHolder);
            rsp.setBody(WebUtils.buildForm(baseUrl, requestHolder.getApplicationParams()));
        }
        return rsp;
    }
    
    /**
     * SDK客户端调用生成sdk字符串
     * @param request
     * @param <T>
     * @return
     * @throws ApiException
     */
    public <T extends BitmsResponse> T sdkExecute(BasicRequest<T> request) throws ApiException
    {
        RequestHolder requestHolder = getRequestHolderWithSign(request , null);
        // 打印完整请求报文
        if (BitmsLogger.isBizDebugEnabled())
        {
            BitmsLogger.logBizDebug(getSdkParams(requestHolder));
        }
        T rsp = null;
        try
        {
            Class<T> clazz = request.getResponseClass();
            rsp = clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            BitmsLogger.logBizError(e);
        }
        catch (IllegalAccessException e)
        {
            BitmsLogger.logBizError(e);
        }
        rsp.setBody(getSdkParams(requestHolder));
        return rsp;
    }
    
    /**
     * 组装接口参数，处理加密、签名逻辑
     * 
     * @param request
     * @param authToken
     * @return
     * @throws ApiException
     */
    private <T extends BitmsResponse> RequestHolder getRequestHolderWithSign(BasicRequest<?> request, String authToken) throws ApiException
    {
        RequestHolder requestHolder = new RequestHolder();
        BitmsMap appParams = new BitmsMap(request.getTextParams());
        // 仅当API包含biz_content参数且值为空时，序列化bizModel填充bizContent
        try
        {
            if (request.getClass().getMethod("getContent") != null && StringUtils.isEmpty(appParams.get(BitmsConstants.CONTENT_KEY)) && request.getBizModel() != null)
            {
                appParams.put(BitmsConstants.CONTENT_KEY, new JSONWriter().write(request.getBizModel(), true));
            }
        }
        catch (NoSuchMethodException e)
        {
            // 找不到getContent则什么都不做
        }
        catch (SecurityException e)
        {
            BitmsLogger.logBizError(e);
        }
        if (request.isNeedEncrypt())
        {// 只有新接口和设置密钥才能支持加密
            if (StringUtils.isEmpty(appParams.get(BitmsConstants.CONTENT_KEY))) { throw new ApiException("当前API不支持加密请求"); }
            if (!StringUtils.areNotEmpty(this.encryptKey, this.encryptType))
            {// 需要加密必须设置密钥和加密算法
                throw new ApiException("API请求要求加密，则必须设置密钥和密钥类型：encryptKey=" + encryptKey + ",encryptType=" + encryptType);
            }
            String encryptContent = Encrypt.encryptContent(appParams.get(BitmsConstants.CONTENT_KEY), this.encryptType, this.encryptKey);
            appParams.put(BitmsConstants.CONTENT_KEY, encryptContent);
        }
        if (!StringUtils.isEmpty(authToken)) appParams.put(BitmsConstants.AUTH_TOKEN, authToken);
        requestHolder.setApplicationParams(appParams);// 业务请求参数
        if (StringUtils.isEmpty(charset)) charset = BitmsConstants.CHARSET_UTF8;
        BitmsMap protocalMustParams = new BitmsMap();// 公共必填参数
        protocalMustParams.put(BitmsConstants.VERSION, request.getApiVersion());
        protocalMustParams.put(BitmsConstants.SIGN_TYPE, this.sign_type);
        protocalMustParams.put(BitmsConstants.TERMINAL_TYPE, request.getTerminalType());
        protocalMustParams.put(BitmsConstants.TERMINAL_INFO, request.getTerminalInfo());
        protocalMustParams.put(BitmsConstants.CHARSET, charset);
        if (request.isNeedEncrypt())
        {// 加密时将加密类型加入到必填参数中
            protocalMustParams.put(BitmsConstants.ENCRYPT_KEY, this.encryptKey);
        }
        protocalMustParams.put(BitmsConstants.TIMESTAMP, System.currentTimeMillis());
        requestHolder.setProtocalMustParams(protocalMustParams);
        BitmsMap protocalOptParams = new BitmsMap();// 公共选填参数
        protocalOptParams.put(BitmsConstants.FORMAT, format);
        requestHolder.setProtocalOptParams(protocalOptParams);
        if (!StringUtils.isEmpty(this.sign_type))
        {// 将签名加入公共必填参数中
            String signContent = Signature.getSignatureContent(requestHolder);
            protocalMustParams.put(BitmsConstants.SIGN, Signature.rsaSign(signContent, privateKey, charset, this.sign_type));
        }
        else
        {
            protocalMustParams.put(BitmsConstants.SIGN, "");
        }
        return requestHolder;
    }
    
    /**
     * 获取POST请求的base url
     * 
     * @param requestHolder
     * @return
     * @throws ApiException
     */
    private String getRequestUrl(RequestHolder requestHolder) throws ApiException
    {
        StringBuffer urlSb = new StringBuffer(serverUrl);
        try
        {
            String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(), charset);
            String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(), charset);
            urlSb.append("?");
            urlSb.append(sysMustQuery);
            if (sysOptQuery != null & sysOptQuery.length() > 0)
            {
                urlSb.append("&");
                urlSb.append(sysOptQuery);
            }
        }
        catch (IOException e)
        {
            throw new ApiException(e);
        }
        return urlSb.toString();
    }
    
    /**
     * GET模式下获取跳转链接
     * 
     * @param requestHolder
     * @return
     * @throws ApiException
     */
    private String getRedirectUrl(RequestHolder requestHolder) throws ApiException
    {
        StringBuffer urlSb = new StringBuffer(serverUrl);
        try
        {
            Map<String, String> sortedMap = Signature.getSortedMap(requestHolder);
            String sortedQuery = WebUtils.buildQuery(sortedMap, charset);
            String sign = requestHolder.getProtocalMustParams().get(BitmsConstants.SIGN);
            urlSb.append("?");
            urlSb.append(sortedQuery);
            if (sign != null & sign.length() > 0)
            {
                Map<String, String> signMap = new HashMap<String, String>();
                signMap.put(BitmsConstants.SIGN, sign);
                String signQuery = WebUtils.buildQuery(signMap, charset);
                urlSb.append("&");
                urlSb.append(signQuery);
            }
        }
        catch (IOException e)
        {
            throw new ApiException(e);
        }
        return urlSb.toString();
    }
    
    /**
     * 拼装sdk调用时所传参数
     * 
     * @param requestHolder
     * @return
     * @throws ApiException
     */
    private String getSdkParams(RequestHolder requestHolder) throws ApiException
    {
        StringBuffer urlSb = new StringBuffer();
        try
        {
            Map<String, String> sortedMap = Signature.getSortedMap(requestHolder);
            String sortedQuery = WebUtils.buildQuery(sortedMap, charset);
            String sign = requestHolder.getProtocalMustParams().get(BitmsConstants.SIGN);
            urlSb.append(sortedQuery);
            if (sign != null & sign.length() > 0)
            {
                Map<String, String> signMap = new HashMap<String, String>();
                signMap.put(BitmsConstants.SIGN, sign);
                String signQuery = WebUtils.buildQuery(signMap, charset);
                urlSb.append("&");
                urlSb.append(signQuery);
            }
        }
        catch (IOException e)
        {
            throw new ApiException(e);
        }
        return urlSb.toString();
    }
    
    private <T extends BitmsResponse> T _execute(BasicRequest<T> request, BitmsParser<T> parser,String authToken) throws ApiException
    {
        Map<String, Object> rt = doPost(request, authToken);
        if (null == rt) { return null; }
        T tRsp;
        try
        {
            // 若需要解密则先解密
            ResponseEncryptItem responseItem = encryptResponse(request, rt, parser);
            // 解析实际串
            tRsp = parser.parse(responseItem.getRealContent());
            tRsp.setBody(responseItem.getRealContent());
        }
        catch (RuntimeException e)
        {
            BitmsLogger.logBizError((String) rt.get("rsp"));
            throw e;
        }
        tRsp.setParams((BitmsMap) rt.get("textParams"));
        return tRsp;
    }
    
    /**
     * POST 方式提交
     * @param request
     * @param authToken
     * @return
     * @throws ApiException
     */
    private <T extends BitmsResponse> Map<String, Object> doPost(BasicRequest<T> request,  String authToken) throws ApiException
    {
        Map<String, Object> result = new HashMap<String, Object>();
        RequestHolder requestHolder = getRequestHolderWithSign(request, authToken);
        String url = getRequestUrl(requestHolder);
        BitmsLogger.logBizDebug(getRedirectUrl(requestHolder));
        String rsp;
        try
        {
            if (request instanceof UploadRequest)
            {
                UploadRequest<T> uRequest = (UploadRequest<T>) request;
                Map<String, FileItem> fileParams = BitmsUtils.cleanupMap(uRequest.getFileParams());
                rsp = WebUtils.doPost(url, requestHolder.getApplicationParams(), fileParams, charset, connectTimeout, readTimeout);
            }
            else
            {
                rsp = WebUtils.doPost(url, requestHolder.getApplicationParams(), charset, connectTimeout, readTimeout);
            }
        }
        catch (IOException e)
        {
            throw new ApiException(e);
        }
        result.put("rsp", rsp);
        result.put("textParams", requestHolder.getApplicationParams());
        result.put("protocalMustParams", requestHolder.getProtocalMustParams());
        result.put("protocalOptParams", requestHolder.getProtocalOptParams());
        result.put("url", url);
        return result;
    }
    
    /**
     *  解密响应
     * @param request
     * @param rt
     * @param parser
     * @return
     * @throws ApiException
     */
    private <T extends BitmsResponse> ResponseEncryptItem encryptResponse(BasicRequest<T> request, Map<String, Object> rt, BitmsParser<T> parser) throws ApiException
    {
        String responseBody = (String) rt.get("rsp");
        String realBody;
        // 解密
        if (request.isNeedEncrypt())
        {
            // 解密原始串
            realBody = parser.encryptSourceData(request, responseBody, this.format, this.encryptType, this.encryptKey, this.charset);
        }
        else
        {
            // 解析原内容串
            realBody = (String) rt.get("rsp");
        }
        return new ResponseEncryptItem(responseBody, realBody);
    }
}
