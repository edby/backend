package com.blocain.bitms.bitpay.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.bitpay.common.ApplicationConst;
import com.blocain.bitms.bitpay.common.JsonMessage;
import com.blocain.bitms.bitpay.common.Pagination;
import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.entity.WithdrawRecord;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import com.blocain.bitms.bitpay.service.WithdrawRecordService;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.bitgo.model.RecipientModel;
import com.blocain.bitms.payment.remote.BitGoRemoteService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>File: WithdrawRecordController.java</p>
 * <p>Title: </p>
 * <p>Description:bitpay</p>
 * <p>Copyright: Copyright (c) 2017-07-18 11:05</p>
 * <p>Company: jmwenhua.cn</p>
 * @author 施建波
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/withdraw")
public class WithdrawController extends BaseController {
    
    public static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);

	@Resource
	private WithdrawRecordService withdrawRecordService;
	
	@Autowired
    private BitGoRemoteService bitGoRemoteService;
	
	@Autowired
	private BitpayKeychainService bitpayKeychainService;
	
	@Value("${bitms.client.transfer.data}")
    private String transferDataUrl;
	
	@Value("${bitms.client.transfer.status}")
	private String transferStatusUrl;

    /**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	初始化查询页面
	 * </pre>
	 * 
	 * @return
	 */
	@RequestMapping(value = "/search")
	public String search(){
		return "admin/withdraw/search";
	}
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	分页查询
	 * </pre>
	 * 
	 * @param withdrawRecord
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, WithdrawRecord withdrawRecord, Pagination pagination, Model model){
	    withdrawRecord.setStockinfoId(super.getStockinfoBtcId());
	    PageInfo<WithdrawRecord> pageInfo = withdrawRecordService.findWithdrawRecordPage(withdrawRecord, pagination);
		model.addAttribute("pageInfo", pageInfo);
		return "admin/withdraw/list";
	}
	
	/**
	 * 
	 * <pre>
	 * 	2017-07-18 11:05 施建波
	 * 	单笔提现
	 * </pre>
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "/confirm.ajax")
	@ResponseBody
	public JsonMessage confirm(Long id, String password) throws BusinessException{
	    if(id==null) {
	        throw new BusinessException("提现ID不能为空");
	    }
	    WithdrawRecord withdrawRecord = new WithdrawRecord();
	    withdrawRecord.setId(id);
	    withdrawRecord.setStockinfoId(super.getStockinfoBtcId());
	    withdrawRecord = withdrawRecordService.findWithdrawRecord(withdrawRecord);
	    if(null == withdrawRecord) {
	        throw new BusinessException("提现记录不存在");
	    }
	    if(0 != withdrawRecord.getState()) {
	        throw new BusinessException("此提现记录已提现");
	    }
	    //获取付款钱包
	    BitpayKeychain bitpayKeychain = new BitpayKeychain();
	    bitpayKeychain.setType(2);
	    bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
	    bitpayKeychain = bitpayKeychainService.findBitpayKeychain(bitpayKeychain);
	    if(null == bitpayKeychain) {
	        throw new BusinessException("付款钱包不存在");
	    }
	    try {
	        if(StringUtils.isNotBlank(bitpayKeychain.getSystemPass())) {
	            password = super.passDecryption(bitpayKeychain, password);
            }
	        //调用外部接口交易
	        BitPayModel bitPayModel = bitGoRemoteService.sendCoins(bitpayKeychain.getWalletId(), withdrawRecord.getRaiseAddr(), super.chargAmount(withdrawRecord.getOccurAmt()), this.chargAmount(withdrawRecord.getNetFee()), bitpayKeychain.getToken(), password);
	        
	        withdrawRecord.setState(1);
	        withdrawRecord.setTransId(bitPayModel.getHash());
	        withdrawRecordService.saveWithdrawRecord(withdrawRecord);
	        
	        try {
	            //回调交易平台，添加交易ID易
	            List<Map<String, String>> callbackList = Lists.newArrayList();
	            Map<String, String> withdrawMap = Maps.newHashMap();
	            withdrawMap.put("id", withdrawRecord.getId().toString());
	            withdrawMap.put("transId", withdrawRecord.getTransId());
	            callbackList.add(withdrawMap);
    	        Map<String, String> param = Maps.newHashMap();
    	        
    	        String list =JSONObject.toJSONString(callbackList);
                param.put("list", list);
                this.clientPost(param, this.transferStatusUrl);
	        }catch(Exception e) {
	            throw new BusinessException("交易成功，回调失败：" + e.getMessage());
	        }
	        
	    }catch(BusinessException e) {
            throw e;
        }catch(Exception e) {
	        logger.error(e.getMessage(), e);
	        throw new BusinessException("提现交易失败"+e.getMessage());
	    }
		return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
	}
	
	/**
	 * 批量提现
	 * @param idAry
	 * @return
	 * @author 施建波  2017年7月20日 上午11:06:07
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "/confirm/batch.ajax")
    @ResponseBody
    public JsonMessage confirmBatch(String[] idAry, String password) throws BusinessException{
	    if(ArrayUtils.isEmpty(idAry)) {
	        throw new BusinessException("至少选择一条交易记录");
	    }
	    List<RecipientModel> recipientList = Lists.newArrayList();
	    List<WithdrawRecord> withdrawList = Lists.newArrayList();
	    for(String id:idAry) {
	        WithdrawRecord withdrawRecord = new WithdrawRecord();
	        withdrawRecord.setId(Long.parseLong(id));
	        withdrawRecord.setStockinfoId(super.getStockinfoBtcId());
	        withdrawRecord.setState(0);
	        withdrawRecord = withdrawRecordService.findWithdrawRecord(withdrawRecord);
	        //组装付款地址集合
	        if(null != withdrawRecord) {
	            RecipientModel recipientModel = new RecipientModel();
	            recipientModel.setAddress(withdrawRecord.getRaiseAddr());
	            recipientModel.setAmount(super.chargAmount(withdrawRecord.getOccurAmt()));
	            recipientList.add(recipientModel);
	            withdrawList.add(withdrawRecord);
	        }
	    }
	    if(CollectionUtils.isNotEmpty(recipientList)) {
            //获取付款钱包
            BitpayKeychain bitpayKeychain = new BitpayKeychain();
            bitpayKeychain.setType(2);
            bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
            bitpayKeychain = bitpayKeychainService.findBitpayKeychain(bitpayKeychain);
            if(null == bitpayKeychain) {
                throw new BusinessException("付款钱包不存在");
            }
            try {
                if(StringUtils.isNotBlank(bitpayKeychain.getSystemPass())) {
                    password = super.passDecryption(bitpayKeychain, password);
                }
                //调用外部接口交易
                BitPayModel bitPayModel = bitGoRemoteService.sendMultipleCoins(bitpayKeychain.getWalletId(), recipientList, bitpayKeychain.getFeeTxConfirmTarget(), bitpayKeychain.getToken(), password);
                List<Map<String, String>> callbackList = Lists.newArrayList();
                for(WithdrawRecord item:withdrawList) {
                    item.setState(1);
                    item.setTransId(bitPayModel.getHash());
                    
                    Map<String, String> withdrawMap = Maps.newHashMap();
                    withdrawMap.put("id", item.getId().toString());
                    withdrawMap.put("transId", item.getTransId());
                    callbackList.add(withdrawMap);
                }
                withdrawRecordService.updateWithdrawRecordBatch(withdrawList);
                
                try {
                    if(CollectionUtils.isNotEmpty(callbackList)) {
                        //回调交易平台，添加交易ID
                        Map<String, String> param = Maps.newHashMap();
                        
                        String list =JSONObject.toJSONString(callbackList);
                        param.put("list", list);
                        this.clientPost(param, this.transferStatusUrl);
                    }
                }catch(Exception e) {
                    throw new BusinessException("交易成功，回调失败：" + e.getMessage());
                }
            }catch(BusinessException e) {
                throw e;
            }catch(Exception e) {
                logger.error(e.getMessage(), e);
                throw new BusinessException("批量提现交易失败"+e.getMessage());
            }
	    }
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
    }
	
	/**
	 * 文件导入EXCEL
	 * @param fileupload
	 * @return
	 * @author 施建波  2017年7月19日 上午10:46:27
	 * @throws BusinessException 
	 */
	@RequestMapping(value = "/import.ajax")
    @ResponseBody
    public JsonMessage excimport(MultipartFile fileupload) throws BusinessException{ 
	    String fileName = fileupload.getOriginalFilename();
	    try {
	        ImportExcel importExcel = new ImportExcel(fileName, fileupload.getInputStream(), 0, 0);
	        Map<String, WithdrawRecord> withdrawMap = Maps.newHashMap();
	        List<String> idList = Lists.newArrayList();
	        String[] titleArry = {"id","accountId","withdrawAddr","occurAmt","netFee","createDate"};
	        for(int i=importExcel.getDataRowNum();i<=importExcel.getLastDataRowNum();i++) {
	            WithdrawRecord withdrawRecord = new WithdrawRecord();
	            try {
    	            for(int j=0;j<importExcel.getLastCellNum();j++) { 
    	                this.setObjValue(withdrawRecord, titleArry[j], importExcel.getCellValue(importExcel.getRow(i), j));
    	            }
    	            withdrawRecord.setStockinfoId(super.getStockinfoBtcId());
    	            withdrawRecord.setState(0); 
	            }catch(Exception e) {
	                logger.error(e.getMessage(), e);
	                continue;
	            }
	            if(!idList.contains(withdrawRecord.getId())) {
	                withdrawMap.put(withdrawRecord.getId().toString(), withdrawRecord);
                    idList.add(withdrawRecord.getId().toString());
                }
	        }
	        //检查是否有重复的记录，有则去除
	        if(CollectionUtils.isNotEmpty(idList)) {
	            WithdrawRecord withdrawRecord = new WithdrawRecord();
	            List<WithdrawRecord> list = withdrawRecordService.findWithdrawRecordSearch(withdrawRecord);
	            if(CollectionUtils.isNotEmpty(list)) {
	                for(WithdrawRecord item:list) {
	                    withdrawMap.remove(item.getId());
	                }
	            }
	        }
	        List<WithdrawRecord> insertList = Lists.newArrayList(withdrawMap.values());
	        withdrawRecordService.insertWithdrawRecordBatch(insertList);
	    }catch(Exception e) {
	        logger.error(e.getMessage(), e);
	        throw new BusinessException("提现记录导入失败:"+e.getMessage()); 
	    }
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
    }
	
	private void setObjValue(Object obj, String titleName, Object value) throws Exception {
        try {
            if(null != value) {
                Field f = obj.getClass().getDeclaredField(titleName);
                f.setAccessible(true);
                Class<?> classType = f.getType();
                if(classType.equals(BigDecimal.class)) {
                    f.set(obj, new BigDecimal(value.toString()));
                }else if(classType.equals(Date.class)){
                    f.set(obj, CalendarUtils.getDate(Long.parseLong(value.toString())));
                }else {
                    f.set(obj, value); 
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }
	
	/**
	 * 导出EXCEL
	 * @return
	 * @author 施建波  2017年7月19日 下午3:35:23
	 * @throws IOException 
	 * @throws BusinessException 
	 */
    @RequestMapping(value = "/export")
    public void excexport(String[] idAry, HttpServletResponse response) throws IOException, BusinessException{ 
        if(ArrayUtils.isEmpty(idAry)) {
            throw new BusinessException("至少选择一条交易记录");
        }
        List<String> idList = Lists.newArrayList(idAry);
        List<WithdrawRecord> updateList = Lists.newArrayList();
        WithdrawRecord withdrawRecord = new WithdrawRecord();
        withdrawRecord.setStockinfoId(super.getStockinfoBtcId());
        withdrawRecord.setIdList(idList);
        List<WithdrawRecord> list = withdrawRecordService.findWithdrawRecordSearch(withdrawRecord);
        if(CollectionUtils.isNotEmpty(list)) {
            Iterator<WithdrawRecord> iterator = list.iterator();
            while(iterator.hasNext()) {
                WithdrawRecord item = iterator.next();
                if(0 >= item.getState()) {
                    iterator.remove();
                }else {
                    if(1 == item.getState()) {
                        item.setState(2);
                        updateList.add(item);
                    }
                }
            }
        }
        withdrawRecordService.updateWithdrawRecordBatch(updateList);
        ExportExcel excel = new ExportExcel(null,WithdrawRecord.class );
        excel.setDataList(list);
        excel.write(response, "提现记录.xls");
    }
    
    /**
     * 接口导入提现
     * @return
     * @author 施建波  2017年7月19日 上午10:46:27
     * @throws BusinessException 
     */
    @RequestMapping(value = "/apiimport.ajax")
    @ResponseBody
    public JsonMessage apiImport() throws BusinessException{ 
        try {
            Map<String, String> param = Maps.newHashMap();
            param.put("stockinfoId", super.getStockinfoBtcId());
            param.put("businessFlag", "walletWithdraw");
            JSONObject json = this.clientPost(param, this.transferDataUrl);
            
            //解析组装数据
            JSONArray jsonArry = json.getJSONArray("object");
            if(!jsonArry.isEmpty()) {
                Map<String, WithdrawRecord> withdrawMap = Maps.newHashMap();
                List<String> idList = Lists.newArrayList();
                for(int i=0;i<jsonArry.size();i++) {
                    JSONObject item = jsonArry.getJSONObject(i);
                    WithdrawRecord withdrawRecord = new WithdrawRecord();
                    withdrawRecord.setId(item.getLong("id"));
                    withdrawRecord.setAccountId(item.getString("accountId"));
                    withdrawRecord.setRaiseAddr(item.getString("targetWalletAddr"));
                    withdrawRecord.setOccurAmt(item.getBigDecimal("transferAmt"));
                    withdrawRecord.setNetFee(item.getBigDecimal("transferFee"));
                    withdrawRecord.setStockinfoId(item.getString("stockinfoId"));
                    withdrawRecord.setState(0);
                    withdrawRecord.setCreateDate(item.getDate("transferTime").getTime());
                    withdrawMap.put(withdrawRecord.getId().toString(), withdrawRecord);
                    idList.add(withdrawRecord.getId().toString());
                } 
                
                //检查是否有重复的记录，有则去除
                if(CollectionUtils.isNotEmpty(idList)) {
                    WithdrawRecord withdrawRecord = new WithdrawRecord();
                    withdrawRecord.setIdList(idList);
                    List<WithdrawRecord> list = withdrawRecordService.findWithdrawRecordSearch(withdrawRecord);
                    if(CollectionUtils.isNotEmpty(list)) {
                        for(WithdrawRecord item:list) {
                            withdrawMap.remove(item.getId());
                        }
                    }
                    List<WithdrawRecord> insertList = Lists.newArrayList(withdrawMap.values());
                    withdrawRecordService.insertWithdrawRecordBatch(insertList);
                }
            }
        }catch(BusinessException e) {
            throw e;
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException("提现记录导入失败"+e.getMessage()); 
        }
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
    }
    
    private JSONObject validate(String content) throws BusinessException
    {
        JSONObject json = JSONObject.parseObject(content);
        String code = json.getString("code");
        String message = json.getString("message");
        if(!"200".equals(code)) {
            throw new BusinessException(message);
        }
        return json;
    }
    
    private JSONObject clientPost(Map<String, String> param, String url) throws BusinessException {
        //从远程获取数据
        HttpClient client = HttpUtils.getHttpClient();
        String data = ParameterUtils.getDataFromMap(param);
        int dataLen = ValidateUtils.length(data);
        String userDes = ParameterUtils.getUserDes(super.getClientUserKey(), dataLen);
        Map<String, String> httpMap = Maps.newHashMap();
        httpMap.put("userKey", super.getClientUserKey());
        httpMap.put("dataLen", String.valueOf(dataLen));
        httpMap.put("userDes", userDes);
        httpMap.put("data", data);
        String content = HttpUtils.post(client, url, httpMap);
        return this.validate(content);
    }
}
