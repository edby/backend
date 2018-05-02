/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.bitpay.entity.WithdrawRecord;
import com.blocain.bitms.bitpay.service.WithdrawRecordService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.*;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.HttpClient;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 账户提现记录 控制器
 * <p>File：WithdrawRecordController.java </p>
 * <p>Title: WithdrawRecordController </p>
 * <p>Description:WithdrawRecordController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.BITPAY)
@Api(description = "账户提现记录")
public class WithdrawRecordController extends GenericController
{
    @Autowired(required = false)
    private WithdrawRecordService   withdrawRecordService;
    
    protected final PropertiesUtils properties = new PropertiesUtils("wallet.properties");
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdrawRecord")
    @RequiresPermissions("trade:setting:withdrawRecord:index")
    public String list() throws BusinessException
    {
        return "/bitpay/withdrawRecord/list";
    }
    
    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdrawRecord/modify")
    @RequiresPermissions("trade:setting:withdrawRecord:operator")
    public ModelAndView modify(Long id, Long parentId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("bitpay/withdrawRecord/modify");
        WithdrawRecord keychain = new WithdrawRecord();
        if (null == id) keychain = withdrawRecordService.selectByPrimaryKey(id);
        mav.addObject("keychain", keychain);
        return mav;
    }
    
    /**
     * 提币确认界面页面导航
     * @param ids
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdrawRecord/password")
    @RequiresPermissions("trade:setting:withdrawRecord:operator")
    public ModelAndView modify(String ids) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("bitpay/withdrawRecord/password");
        mav.addObject("ids", ids);
        return mav;
    }
    
    /**
     * 操作账户提现记录
     * <p>
     *     @RequestBody 此注解加入后接收的参数将是JSON字符串，若相应模块中需要以FORM表单方式提交，
     *     请将此注册取消掉;加入此注解是的目的是为了统一数据交互方式，实现真正的前后端分离。
     * </p>
     * @param info
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawRecord/save")
    @ApiOperation(value = "保存账户提现记录", httpMethod = "POST")
    @RequiresPermissions("trade:setting:withdrawRecord:operator")
    public JsonMessage save(WithdrawRecord info) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        if (beanValidator(json, info))
        {
            withdrawRecordService.save(info);
        }
        return json;
    }
    
    /**
     * 查询账户提现记录
     * <p>
     *     @RequestBody 此注解加入后接收的参数将是JSON字符串，加入此注解是的目的是为了统一数据交互方式，实现真正的前后端分离。
     *     若相应模块中需要以FORM表单方式提交，请将此注册取消掉;
     * </p>
     * @param entity
     * @param pagin
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawRecord/data", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:withdrawRecord:data")
    @ApiOperation(value = "查询账户提现记录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage data(@ModelAttribute WithdrawRecord entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        PaginateResult<WithdrawRecord> result = withdrawRecordService.search(pagin, entity);
        return getJsonMessage(CommonEnums.SUCCESS, result);
    }
    
    /**
     * 根据指定ID删除
     * @param ids
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/withdrawRecord/del", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:withdrawRecord:operator")
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组", paramType = "form")
    public JsonMessage del(String ids) throws BusinessException
    {
        withdrawRecordService.removeBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     *
     * <pre>
     *  2017-07-18 11:05 施建波
     *  单笔提现
     * </pre>
     *
     * @param id
     * @param password
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @RequestMapping(value = "/withdrawRecord/confirm")
    @ResponseBody
    public JsonMessage confirm(Long id, String password,String otp) throws BusinessException
    {
        withdrawRecordService.doSingleCashWthdrawal(id, password,otp);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 批量提现
     * @param ids
     * @return
     * @author 施建波  2017年7月20日 上午11:06:07
     * @throws BusinessException
     */
    @CSRFToken
    @RequestMapping(value = "/withdrawRecord/confirm/batch")
    @ResponseBody
    public JsonMessage confirmBatch(String ids, String password) throws BusinessException
    {
        withdrawRecordService.doMultipleCashWthdrawal(ids, password);
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 文件导入EXCEL
     * @param fileupload
     * @return
     * @author 施建波  2017年7月19日 上午10:46:27
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdrawRecord/import")
    @ResponseBody
    public JsonMessage excimport(MultipartFile fileupload) throws BusinessException
    {
        String fileName = fileupload.getOriginalFilename();
        try
        {
            ImportExcel importExcel = new ImportExcel(fileName, fileupload.getInputStream(), 0, 0);
            Map<String, WithdrawRecord> withdrawMap = Maps.newHashMap();
            List<String> idList = Lists.newArrayList();
            String[] titleArry = {"id", "accountId", "withdrawAddr", "occurAmt", "netFee", "createDate"};
            for (int i = importExcel.getDataRowNum(); i <= importExcel.getLastDataRowNum(); i++)
            {
                WithdrawRecord withdrawRecord = new WithdrawRecord();
                try
                {
                    for (int j = 0; j < importExcel.getLastCellNum(); j++)
                    {
                        this.setObjValue(withdrawRecord, titleArry[j], importExcel.getCellValue(importExcel.getRow(i), j));
                    }
                    withdrawRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    withdrawRecord.setState(0);
                }
                catch (Exception e)
                {
                    continue;
                }
                if (!idList.contains(withdrawRecord.getId()))
                {
                    withdrawMap.put(withdrawRecord.getId().toString(), withdrawRecord);
                    idList.add(withdrawRecord.getId().toString());
                }
            }
            // 检查是否有重复的记录，有则去除
            if (CollectionUtils.isNotEmpty(idList))
            {
                WithdrawRecord withdrawRecord = new WithdrawRecord();
                List<WithdrawRecord> list = withdrawRecordService.findList(withdrawRecord);
                if (CollectionUtils.isNotEmpty(list))
                {
                    for (WithdrawRecord item : list)
                    {
                        withdrawMap.remove(item.getId());
                    }
                }
            }
            List<WithdrawRecord> insertList = Lists.newArrayList(withdrawMap.values());
            withdrawRecordService.insertBatch(insertList);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BusinessException("提现记录导入失败:" + e.getMessage());
        }
        return super.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    private void setObjValue(Object obj, String titleName, Object value) throws Exception
    {
        try
        {
            if (null != value)
            {
                Field f = obj.getClass().getDeclaredField(titleName);
                f.setAccessible(true);
                Class<?> classType = f.getType();
                if (classType.equals(BigDecimal.class))
                {
                    f.set(obj, new BigDecimal(value.toString()));
                }
                else if (classType.equals(Date.class))
                {
                    f.set(obj, CalendarUtils.getDate(Long.parseLong(value.toString())));
                }
                else
                {
                    f.set(obj, value);
                }
            }
        }
        catch (Exception e)
        {
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
    @RequestMapping(value = "/withdrawRecord/export")
    public void excexport(Long[] idAry, HttpServletResponse response) throws IOException, BusinessException
    {
        if (ArrayUtils.isEmpty(idAry)) { throw new BusinessException("至少选择一条交易记录"); }
        List<Long> idList = Lists.newArrayList(idAry);
        List<WithdrawRecord> updateList = Lists.newArrayList();
        WithdrawRecord withdrawRecord = new WithdrawRecord();
        withdrawRecord.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        withdrawRecord.setIdList(idList);
        List<WithdrawRecord> list = withdrawRecordService.findList(withdrawRecord);
        if (CollectionUtils.isNotEmpty(list))
        {
            Iterator<WithdrawRecord> iterator = list.iterator();
            while (iterator.hasNext())
            {
                WithdrawRecord item = iterator.next();
                if (0 >= item.getState())
                {
                    iterator.remove();
                }
                else
                {
                    if (1 == item.getState())
                    {
                        item.setState(2);
                        updateList.add(item);
                    }
                }
            }
        }
        withdrawRecordService.updateBatch(updateList);
        ExportExcel excel = new ExportExcel(null, WithdrawRecord.class);
        excel.setDataList(list);
        excel.write(response, "提现记录.xls");
    }
    
    /**
     * 接口导入提现
     * @return
     * @author 施建波  2017年7月19日 上午10:46:27
     * @throws BusinessException
     */
    @RequestMapping(value = "/withdrawRecord/apiimport")
    @ResponseBody
    public JsonMessage apiImport() throws BusinessException
    {
        try
        {
            Map<String, String> param = Maps.newHashMap();
            param.put("stockinfoId", FundConsts.WALLET_BTC_TYPE.toString());
            param.put("businessFlag", "walletWithdraw");
            JSONObject json = this.clientPost(param, properties.getProperty("bitms.client.transfer.data"));
            // 解析组装数据
            JSONArray jsonArry = json.getJSONArray("object");
            if (!jsonArry.isEmpty())
            {
                Map<String, WithdrawRecord> withdrawMap = Maps.newHashMap();
                List<Long> idList = Lists.newArrayList();
                for (int i = 0; i < jsonArry.size(); i++)
                {
                    JSONObject item = jsonArry.getJSONObject(i);
                    WithdrawRecord withdrawRecord = new WithdrawRecord();
                    withdrawRecord.setId(item.getLong("id"));
                    withdrawRecord.setAccountId(item.getLong("accountId"));
                    withdrawRecord.setRaiseAddr(item.getString("targetWalletAddr"));
                    withdrawRecord.setOccurAmt(item.getBigDecimal("transferAmt"));
                    withdrawRecord.setNetFee(item.getBigDecimal("transferFee"));
                    withdrawRecord.setStockinfoId(item.getLong("stockinfoId"));
                    withdrawRecord.setState(0);
                    withdrawRecord.setCreateDate(item.getDate("transferTime").getTime());
                    withdrawMap.put(withdrawRecord.getId().toString(), withdrawRecord);
                    idList.add(withdrawRecord.getId());
                }
                // 检查是否有重复的记录，有则去除
                if (CollectionUtils.isNotEmpty(idList))
                {
                    WithdrawRecord withdrawRecord = new WithdrawRecord();
                    withdrawRecord.setIdList(idList);
                    List<WithdrawRecord> list = withdrawRecordService.findList(withdrawRecord);
                    if (CollectionUtils.isNotEmpty(list))
                    {
                        for (WithdrawRecord item : list)
                        {
                            withdrawMap.remove(item.getId());
                        }
                    }
                    List<WithdrawRecord> insertList = Lists.newArrayList(withdrawMap.values());
                    if (insertList.size() > 0)
                    {
                        withdrawRecordService.insertBatch(insertList);
                    }
                    else
                    {
                        throw new BusinessException("没有可导入的数据");
                    }
                }
            }
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("提现记录导入失败" + e.getMessage());
        }
        return super.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    private JSONObject validate(String content) throws BusinessException
    {
        JSONObject json = JSONObject.parseObject(content);
        String code = json.getString("code");
        String message = json.getString("message");
        if (!"200".equals(code)) { throw new BusinessException(message); }
        return json;
    }
    
    private JSONObject clientPost(Map<String, String> param, String url) throws BusinessException
    {
        // 从远程获取数据
        HttpClient client = HttpUtils.getHttpClient();
        String data = ParameterUtils.getDataFromMap(param);
        int dataLen = ValidateUtils.length(data);
        String userDes = ParameterUtils.getUserDes(properties.getProperty("bitms.client.userkey"), dataLen);
        Map<String, String> httpMap = Maps.newHashMap();
        httpMap.put("userKey", properties.getProperty("bitms.client.userkey"));
        httpMap.put("dataLen", String.valueOf(dataLen));
        httpMap.put("userDes", userDes);
        httpMap.put("data", data);
        String content = HttpUtils.post(client, url, httpMap);
        return this.validate(content);
    }
}
