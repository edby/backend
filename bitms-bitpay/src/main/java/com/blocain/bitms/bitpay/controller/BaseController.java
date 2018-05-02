package com.blocain.bitms.bitpay.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.blocain.bitms.bitpay.common.AES256;
import com.blocain.bitms.bitpay.common.ErrorCodeDescribable;
import com.blocain.bitms.bitpay.common.JsonMessage;
import com.blocain.bitms.bitpay.common.StringEditorSupport;
import com.blocain.bitms.bitpay.common.StringMerge;
import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.tools.editor.BooleanEditorSupport;
import com.blocain.bitms.tools.editor.ByteEditorSupport;
import com.blocain.bitms.tools.editor.DoubleEditorSupport;
import com.blocain.bitms.tools.editor.FloatEditorSupport;
import com.blocain.bitms.tools.editor.IntegerEditorSupport;
import com.blocain.bitms.tools.editor.LongEditorSupport;
import com.blocain.bitms.tools.editor.ShortEditorSupport;
import com.blocain.bitms.tools.exception.BusinessException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

/**
 * <p>File：BaseController.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 下午12:56:29</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
public class BaseController{
    
    @Value("${wallet.btc.stockinfoId}")  
    private String stockinfoBtcId;  
    
    @Value("${bitms.client.userkey}")
    private String clientUserKey;
    
    @Value("${bitms.client.root}")
    private String clientRoot;
    
    public String getClientRoot()
    {
        return clientRoot;
    }

    public void setClientRoot(String clientRoot)
    {
        this.clientRoot = clientRoot;
    }

    public String getStockinfoBtcId()
    {
        return stockinfoBtcId;
    }

    public void setStockinfoBtcId(String stockinfoBtcId)
    {
        this.stockinfoBtcId = stockinfoBtcId;
    }

    public String getClientUserKey()
    {
        return clientUserKey;
    }

    public void setClientUserKey(String clientUserKey)
    {
        this.clientUserKey = clientUserKey;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) 
    {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
        Field field = null;
        if (null == field)
        {
            // Byte
            binder.registerCustomEditor(Byte.class, new ByteEditorSupport());
            // Float
            binder.registerCustomEditor(Float.class, new FloatEditorSupport());
            // Double
            binder.registerCustomEditor(Double.class, new DoubleEditorSupport());
            // Long
            binder.registerCustomEditor(Long.class, new LongEditorSupport());
            // Integer
            binder.registerCustomEditor(Integer.class, new IntegerEditorSupport());
            // Boolean
            binder.registerCustomEditor(Boolean.class, new BooleanEditorSupport());
            // String
            binder.registerCustomEditor(String.class, new StringEditorSupport());
            // Short
            binder.registerCustomEditor(Short.class, new ShortEditorSupport());  
        }
    }  
    
    protected JsonMessage getJsonMessage(Integer code) 
    {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(code);
        return jsonMessage;
    }
    
    public JsonMessage getJsonMessage(ErrorCodeDescribable describable)
    {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(describable.getCode());
        jsonMessage.setMessage(describable.getMessage());
        return jsonMessage;
    }
    
    protected <T extends Object> JsonMessage getJsonMessage(Integer code, PageInfo<T> result)
    {
        JsonMessage jsonMessage = new JsonMessage(); 
        jsonMessage.setCode(code);
        jsonMessage.setRows(result.getList());
        jsonMessage.setTotal(result.getTotal());
        jsonMessage.setHasNextPage(result.isHasNextPage());
        jsonMessage.setHasPreviousPage(result.isHasPreviousPage());
        jsonMessage.setPageNum(result.getPageNum());
        jsonMessage.setPages(result.getPages());
        return jsonMessage;
    }
    
    
    protected <T extends Object> JsonMessage getJsonMessageData(Integer code, PageInfo<T> result) {
        JsonMessage jsonMessage = new JsonMessage(); 
        jsonMessage.setCode(code);
        jsonMessage.setList(result.getList());
        jsonMessage.setTotal(result.getTotal());
        jsonMessage.setHasNextPage(result.isHasNextPage());
        jsonMessage.setHasPreviousPage(result.isHasPreviousPage());
        jsonMessage.setPageNum(result.getPageNum());
        jsonMessage.setFirstPage(1);
        jsonMessage.setLastPage(result.getPages());
        jsonMessage.setPages(result.getPages());
        jsonMessage.setNavigatepageNums(result.getNavigatepageNums());
        return jsonMessage;
    }
    
    protected JsonMessage getJsonMessage(Integer code, Object object)
    {
        return this.getJsonMessageObject(code, object);
    }
    
    protected JsonMessage getJsonMessageObject(Integer code, Object object)
    {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(code);
        jsonMessage.setObject(object);
        return jsonMessage;
    }
    
    protected JsonMessage getJsonMessageResult(Integer code, Object result)
    {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setCode(code);
        if(null == result){
            result = Maps.newHashMap();
        }
        jsonMessage.setResult(result);
        return jsonMessage;
    }
    
    protected JsonMessage getJsonMessageResult(Integer code)
    {
        return this.getJsonMessageResult(code, null);
    }
    
    /**
     * 设置SESSION
     * @param request
     * @param sessionKey
     * @param obj
     * @author 施建波  2017年7月18日 下午1:07:29
     */
    protected void setSession(HttpServletRequest request, String sessionKey, Object obj){
        if(null != obj){
            request.getSession().setAttribute(sessionKey, obj);
        }
    }
    
    /**
     * 获取SESSION对象
     * @param request
     * @param sessionKey
     * @return
     * @author 施建波  2017年7月18日 下午1:07:21
     */
    @SuppressWarnings("unchecked")
    protected <T> T getSession(HttpServletRequest request, String sessionKey){
        return (T) request.getSession().getAttribute(sessionKey);
    }
    
    /**
     * 清除SESSION对象
     * @param request
     * @param sessionKey
     * @author 施建波  2017年7月18日 下午1:07:12
     */
    protected void removeSession(HttpServletRequest request, String sessionKey){
        request.getSession().removeAttribute(sessionKey);
    }
    
    /**
     * 获取缓存中的对象
     * @param request
     * @param obj
     * @param sessionKey
     * @return
     * @author 施建波  2017年7月18日 下午1:07:02
     */
    @SuppressWarnings("unchecked")
    protected <T> T getCacheObj(HttpServletRequest request, Class<T> obj , String sessionKey){
        return  (T) this.getSession(request, sessionKey);
    }

    /**
     * 清除Session
     * @param request
     * @author 施建波  2017年7月18日 下午1:06:45
     */
    protected void clearSession(HttpServletRequest request){
        request.getSession().invalidate();
    }
    
    protected Long chargAmount(BigDecimal amount) {
        Long btcAmount = 0L;
        if(null != amount) {
            btcAmount = amount.multiply(new BigDecimal(100000000)).longValue();
        }
        return btcAmount;
    }
    
    protected String passDecryption(BitpayKeychain bitpayKeychain, String payPass) throws BusinessException {
        //根据旧密码解密钱包密码
        payPass = StringMerge.processString(payPass, bitpayKeychain.getSystemPass());
        payPass = AES256.decrypt(bitpayKeychain.getCiphertext(), payPass);
        if(null == payPass) {
            throw new BusinessException("支付密码错误"); 
        }
        return payPass;
    }
    
    public static void main(String[] args) {
        /*String payPass = "123456";
         *                   e06fcf6155adf0a0b4a466b9a2d14a86d9a8db6d1b238b8addce5dfe
        String systemPass = "1993e54b269d39581709f01e65262bd36e2faa09effc368628ec14b5";
        String ciphertext = "79597A5A32627A656C6B46707268424C45647A46684D655A52314B504E43484F6C446A52514A7436326A476137467A4D49395154517A6D7277497751647062664A4536504B6E4E45354E4E76374667714B6878574D513D3D";
        payPass = StringMerge.processString(payPass, systemPass);
        payPass = AES256.decrypt(ciphertext, payPass);
        System.out.println(payPass);*/
        /*String newPass = "123456";
        String newWalletPass = EncryptUtils.entryptPassword(SerialnoUtils.buildUUID());
        //创建新的系统密码
        String newSystemPass = EncryptUtils.entryptPassword(SerialnoUtils.buildUUID());
        //合并新密码和新系统密码
        String payPass = StringMerge.processString(newPass, newSystemPass);
        //加密新的钱包密码
        String ciphertext = AES256.encrypt(newWalletPass, payPass);
        System.out.println(newSystemPass);
        System.out.println(ciphertext);*/
        
        /*String newPass = "123456";
        String newSystemPass="1993e54b269d39581709f01e65262bd36e2faa09effc368628ec14b5";
        String ciphertext = "6A67396C6E2F4A4F51614A4A66334D4552786F5639704A47393542727731505A2F49494F64464D62684D6E642F7A3050714A74467A48734B4562764E364E4C4B7549323561496B447A7075475A68454C4165367632413D3D";
                             
        String payPass = StringMerge.processString(newPass, newSystemPass);
        payPass = AES256.decrypt(ciphertext, payPass);
        System.out.println(payPass);*/
        //6A67396C6E2F4A4F51614A4A66334D4552786F5639704A47393542727731505A2F49494F64464D62684D6E642F7A3050714A74467A48734B4562764E364E4C4B7549323561496B447A7075475A68454C4165367632413D3D
        String newPass = "123456";
        String newWalletPass="5c3b857181df85bcfdfbedcbe780e67834ae967faaba9e3496ef94e5";
        String newSystemPass="8f2495832d32eaa36e454bd194ddd0adb309275a7b49c5de404bfff7";
        String payPass = StringMerge.processString(newPass, newSystemPass);
        System.out.println(payPass);
        payPass = "00008888884844C424ACACA2A2626A66E616119191593935353535B3B373FB";
        String ciphertext = AES256.encrypt(newWalletPass, payPass);
        System.out.println(ciphertext);
        
        
        payPass = AES256.decrypt(ciphertext, payPass);
        System.out.println(payPass);
    }
}
