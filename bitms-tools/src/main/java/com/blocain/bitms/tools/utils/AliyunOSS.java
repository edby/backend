package com.blocain.bitms.tools.utils;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.model.UploadFileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.google.common.collect.Maps;

/**
 * 阿里云OSS存储服务 Introduce
 * <p>File：AliyunOSS.java</p>
 * <p>Title: AliyunOSS</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class AliyunOSS
{
    public static final Logger logger = LoggerFactory.getLogger(AliyunOSS.class);
    
    /**
     * 获取阿里云OSS上传策略
     * <p>
     *     临时空间地址：http://bitms-temp.oss-cn-hongkong.aliyuncs.com
     * </p>
     * @param dir 上传路径
     * @param host 服务器上传地址
     * @return {@link String}
     */
    public static Map getPostPolicy(String dir, String host)
    {
        Map policy = null;
        OSSClient ossClient = new OSSClient(BitmsConst.ALIYUN_OSS_ENDPOINT, BitmsConst.ALIYUN_ACCESS_KEY, BitmsConst.ALIYUN_ACCESS_SECRET);
        try
        {
            long expireEndTime = System.currentTimeMillis() + 60 * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            Map<String, String> respMap = Maps.newLinkedHashMap();
            respMap.put("dir", dir);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("accessid", BitmsConst.ALIYUN_ACCESS_KEY);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            policy = respMap;
        }
        catch (Exception e)
        {
            LoggerUtils.logError(logger, "获取策略失败：{} ", e.getLocalizedMessage());
        }
        finally
        {
            ossClient.shutdown();
        }
        return policy;
    }
    
    /**
     * 断点续传本地文件
     * <p>
     *     默认采用客户端直传的方式，断点续传只是做一个JAVA端的示例。
     *     不允许在WEB环境中直接使用
     * </p>
     * @param path 路径
     * @param fileName 文件名
     */
    public static void upload(String path, String fileName)
    {
        OSSClient ossClient = new OSSClient(BitmsConst.ALIYUN_OSS_ENDPOINT, BitmsConst.ALIYUN_ACCESS_KEY, BitmsConst.ALIYUN_ACCESS_SECRET);
        try
        {
            // 设置断点续传请求
            UploadFileRequest uploadFileRequest = new UploadFileRequest(BitmsConst.BUCKET_TEMP_NAME, fileName);
            // 指定上传的本地文件
            uploadFileRequest.setUploadFile(path);
            // 指定上传并发线程数
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小
            uploadFileRequest.setPartSize(1 * 1024 * 1024);
            // 开启断点续传
            uploadFileRequest.setEnableCheckpoint(true);
            // 断点续传上传
            ossClient.uploadFile(uploadFileRequest);
        }
        catch (Throwable e)
        {
            LoggerUtils.logError(logger, "文件上传失败：{} ", e.getLocalizedMessage());
        }
        finally
        {
            ossClient.shutdown();
        }
    }
    
    /**
     * 转移临时空间中的文件到正式空间
     * <p>
     *     将真实有性的文件转移到正式空间，转移完毕之后自动清理临时文件。
     *     另：临时空间可做定期清理以减少资料开销
     * </p>
     * @param fileName 文件名
     * @return {@link Boolean}
     */
    public static boolean transferObject(String fileName)
    {
        boolean flag = true;
        OSSClient ossClient = new OSSClient(BitmsConst.ALIYUN_OSS_ENDPOINT, BitmsConst.ALIYUN_ACCESS_KEY, BitmsConst.ALIYUN_ACCESS_SECRET);
        try
        {
            ossClient.copyObject(BitmsConst.BUCKET_TEMP_NAME, fileName, BitmsConst.BUCKET_BITMS_NAME, fileName);
            ossClient.deleteObject(BitmsConst.BUCKET_TEMP_NAME, fileName);
        }
        catch (RuntimeException e)
        {
            flag = false;
            LoggerUtils.logError(logger, "转移文件失败：{} ", e.getLocalizedMessage());
        }
        finally
        {
            ossClient.shutdown();
        }
        return flag;
    }
    
    /**
     * 删除临时空间中的文件
     * @param fileName 文件名
     * @return {@link Boolean}
     */
    public static boolean deleteObject(String fileName)
    {
        boolean flag = true;
        OSSClient ossClient = new OSSClient(BitmsConst.ALIYUN_OSS_ENDPOINT, BitmsConst.ALIYUN_ACCESS_KEY, BitmsConst.ALIYUN_ACCESS_SECRET);
        try
        {
            ossClient.deleteObject(BitmsConst.BUCKET_TEMP_NAME, fileName);
        }
        catch (RuntimeException e)
        {
            flag = false;
            LoggerUtils.logError(logger, "删除临时文件失败：{} ", e.getLocalizedMessage());
        }
        finally
        {
            ossClient.shutdown();
        }
        return flag;
    }
}
