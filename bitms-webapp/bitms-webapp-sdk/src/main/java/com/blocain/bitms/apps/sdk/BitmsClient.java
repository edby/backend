/**
 * blocain.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.blocain.bitms.apps.sdk;

/**
 * 标准客户端接口
 * @author playguy
 */
public interface BitmsClient
{
    /**
     * 发起一个常规请求
     * @param <T>
     * @param request
     * @return
     * @throws ApiException
     */
    <T extends BitmsResponse> T execute(BasicRequest<T> request) throws ApiException;

    /**
     * 发起一个带授权码的请求
     * @param request
     * @param authToken
     * @return
     * @throws ApiException
     */
    <T extends BitmsResponse> T execute(BasicRequest<T> request,  String authToken) throws ApiException;
    
    /**
     * SDK调用返回FORM表单元素
     * @param <T>
     * @param request
     * @return
     * @throws ApiException
     */
    <T extends BitmsResponse> T pageExecute(BasicRequest<T> request) throws ApiException;
    
    /**
     * SDK客户端调用生成sdk字符串
     * @param <T>
     * @param request
     * @return
     * @throws ApiException
     */
    <T extends BitmsResponse> T sdkExecute(BasicRequest<T> request) throws ApiException;
    
    /**
    * SDK调用返回FORM表单元素
    * @param request
    * @return
    * @throws ApiException
    */
    <T extends BitmsResponse> T pageExecute(BasicRequest<T> request, String method) throws ApiException;
}
