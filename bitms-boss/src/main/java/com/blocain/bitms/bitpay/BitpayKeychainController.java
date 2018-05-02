/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay;

import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.payment.bitgo.BitGoRemoteV2Service;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import com.blocain.bitms.orm.core.GenericController;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.annotation.CSRFToken;
import com.blocain.bitms.tools.bean.JsonMessage;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.AES256;
import com.blocain.bitms.tools.utils.StringMerge;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.tools.utils.ValidateUtils;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 钱包参数 控制器
 * <p>File：BitpayKeychainController.java </p>
 * <p>Title: BitpayKeychainController </p>
 * <p>Description:BitpayKeychainController </p>
 * <p>Copyright: Copyright (c) May 26, 2015 </p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.BITPAY)
@Api(description = "钱包参数")
public class BitpayKeychainController extends GenericController
{
    @Autowired(required = false)
    private BitpayKeychainService bitpayKeychainService;
    
    @Autowired(required = false)
    private BitGoRemoteV2Service  bitGoRemoteService;
    
    /**
     * 列表页面导航
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/bitpayKeychain")
    @RequiresPermissions("trade:setting:keychain:index")
    public String list() throws BusinessException
    {
        return "/bitpay/bitpayKeychain/list";
    }
    
    /**
     * 编辑页面导航
     * @param id
     * @return {@link String}
     * @throws BusinessException
     */
    @RequestMapping(value = "/bitpayKeychain/modify")
    @RequiresPermissions("trade:setting:keychain:operator")
    public ModelAndView modify(Long id, Long parentId) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("bitpay/bitpayKeychain/modify");
        BitpayKeychain keychain = new BitpayKeychain();
        if (null != id) keychain = bitpayKeychainService.selectByPrimaryKey(id);
        mav.addObject("keychain", keychain);
        return mav;
    }
    
    /**
     * 编辑页面导航-修改密码
     * @param id
     * @throws BusinessException
     */
    @RequestMapping(value = "/bitpayKeychain/chagePwd")
    @RequiresPermissions("trade:setting:keychain:operator")
    public ModelAndView chagePwd(Long id) throws BusinessException
    {
        ModelAndView mav = new ModelAndView("bitpay/bitpayKeychain/chgpwd");
        BitpayKeychain keychain = new BitpayKeychain();
        if (id != null) keychain = bitpayKeychainService.selectByPrimaryKey(id);
        mav.addObject("keychain", keychain);
        return mav;
    }
    
    /**
     * 操作钱包参数
     * <p>
     *     @RequestBody 此注解加入后接收的参数将是JSON字符串，若相应模块中需要以FORM表单方式提交，
     *     请将此注册取消掉;加入此注解是的目的是为了统一数据交互方式，实现真正的前后端分离。
     * </p>
     * @param bitpayKeychain
     * @return {@link com.blocain.bitms.tools.bean.JsonMessage}
     * @throws com.blocain.bitms.tools.exception.BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/bitpayKeychain/save")
    @RequiresPermissions("trade:setting:keychain:operator")
    @ApiOperation(value = "保存钱包参数", httpMethod = "POST")
    public JsonMessage save(BitpayKeychain bitpayKeychain) throws BusinessException
    {
        JsonMessage json = getJsonMessage(CommonEnums.SUCCESS);
        if (beanValidator(json, bitpayKeychain))
        {
            if (StringUtils.isBlank(bitpayKeychain.getWalletName())) { throw new BusinessException("钱包名称不能为空"); }
            if (StringUtils.isBlank(bitpayKeychain.getToken())) { throw new BusinessException("TOKEN不能为空"); }
            if (null == bitpayKeychain.getType() || !ValidateUtils.isInRange(bitpayKeychain.getType(), 1, 2)) { throw new BusinessException("钱包类型错误"); }
            if (null == bitpayKeychain.getFeeTxConfirmTarget()
                    || !ValidateUtils.isInRange(bitpayKeychain.getFeeTxConfirmTarget(), 2, 20)) { throw new BusinessException("手续费费率必须大于等2或小于等20"); }
            BitpayKeychain oldKeychain = new BitpayKeychain();
            if (bitpayKeychain.getId() != null)
            {
                if ("******".equals(bitpayKeychain.getToken()))
                {
                    bitpayKeychain.setToken(null);
                }
                if ("******".equals(bitpayKeychain.getXprv()))
                {
                    bitpayKeychain.setXprv(null);
                }
                // 设置修改钱包的属性
                BitpayKeychain whereKeychain = new BitpayKeychain();
                whereKeychain.setId(bitpayKeychain.getId());
                whereKeychain.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                if (bitpayKeychain.getId() != null)
                {
                    oldKeychain = bitpayKeychainService.selectByPrimaryKey(bitpayKeychain.getId());
                    if (null == oldKeychain) { throw new BusinessException("修改的钱包参数记录不存在"); }
                }
            }
            else
            {
                // 设置新增钱包的属性
                BitpayKeychain whereKeychain = new BitpayKeychain();
                whereKeychain.setType(bitpayKeychain.getType());
                whereKeychain.setStockinfoId(bitpayKeychain.getStockinfoId());
                Integer count = bitpayKeychainService.findList(whereKeychain).size();
                if (count > 0) { throw new BusinessException("相同的钱包类型不能创建多条"); }
                oldKeychain.setStockinfoId(bitpayKeychain.getStockinfoId());
            }
            String oldWalletName = oldKeychain.getWalletName();
            if (!bitpayKeychain.getWalletName().equals(oldWalletName) || StringUtils.isBlank(oldKeychain.getWalletId()) || StringUtils.isBlank(oldKeychain.getXpub()))
            {
                try
                {
                    // 从BITGO接口获取钱包公钥
                    BitPayModel bitPayModel = bitGoRemoteService.getWallet(bitpayKeychain.getWalletName(), "btc");
                    oldKeychain.setWalletId(bitPayModel.getId());
                    logger.debug("walletid = " + oldKeychain.getWalletId());
                    bitPayModel = bitGoRemoteService.getWalletInfo(oldKeychain.getWalletId(), "btc");
                    oldKeychain.setXpub(bitPayModel.getXpub());
                }
                catch (BusinessException e)
                {
                    throw e;
                }
                catch (Exception e)
                {
                    throw new BusinessException("bitgo远程接口调用失败");
                }
            }
            oldKeychain.setSystemPass(EncryptUtils.desEncrypt(bitpayKeychain.getSystemPass()));
            oldKeychain.setCoin(bitpayKeychain.getCoin());
            oldKeychain.setWalletName(bitpayKeychain.getWalletName());
            oldKeychain.setToken(bitpayKeychain.getToken());
            oldKeychain.setXprv(bitpayKeychain.getXprv());
            oldKeychain.setType(bitpayKeychain.getType());
            oldKeychain.setFeeTxConfirmTarget(bitpayKeychain.getFeeTxConfirmTarget());
            oldKeychain.setCreateDate(new Timestamp(System.currentTimeMillis()));
            bitpayKeychainService.save(oldKeychain);
        }
        return json;
    }
    
    /**
     * 锁定钱包
     * @param gaCode
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/bitpayKeychain/lock")
    @RequiresPermissions("trade:setting:keychain:operator")
    @ApiOperation(value = "锁定钱包", httpMethod = "POST")
    public JsonMessage lock() throws BusinessException
    {
        JSONObject obj = bitGoRemoteService.lock();
        logger.debug("lock response =:"+obj.toJSONString());
        if (obj.get("session") != null)
        {
            JsonMessage json = getJsonMessage(CommonEnums.SUCCESS, obj.getJSONObject("unlock"));
            return json;
        }
        else
        {
            JsonMessage json = getJsonMessage(CommonEnums.FAIL,obj.get("error"));
            return json;
        }
    }
    
    /**
     * 解锁钱包
     * @param gaCode
     * @return
     * @throws BusinessException
     */
    @CSRFToken
    @ResponseBody
    @RequestMapping(value = "/bitpayKeychain/unlock")
    @RequiresPermissions("trade:setting:keychain:operator")
    @ApiOperation(value = "解锁钱包", httpMethod = "POST")
    public JsonMessage unlock(String gaCode) throws BusinessException
    {
        JSONObject obj = bitGoRemoteService.unlock(gaCode);
        logger.debug("unlock response =:"+obj.toJSONString());
        if (obj.get("session") != null)
        {
            JsonMessage json = getJsonMessage(CommonEnums.SUCCESS, obj.getJSONObject("unlock"));
            return json;
        }
        else
        {
            JsonMessage json = getJsonMessage(CommonEnums.FAIL,obj.get("error"));
            return json;
        }
    }
    
    /**
     * 查询钱包参数
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
    @RequestMapping(value = "/bitpayKeychain/data", method = RequestMethod.POST)
    @ApiOperation(value = "查询钱包参数", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("trade:setting:keychain:data")
    public JsonMessage data(@ModelAttribute BitpayKeychain entity, @ModelAttribute Pagination pagin) throws BusinessException
    {
        List<BitPayModel> walletList = Lists.newArrayList();
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
        PaginateResult<BitpayKeychain> keychainList = bitpayKeychainService.search(pagin, entity);
        if (CollectionUtils.isNotEmpty(keychainList.getList()))
        {
            for (BitpayKeychain keychain : keychainList.getList())
            {
                String walletId = keychain.getWalletId();
                String token = keychain.getToken();
                if (StringUtils.isNotBlank(walletId) && StringUtils.isNotBlank(token))
                {
                    try
                    {
                        BitPayModel bitPayModel = bitGoRemoteService.getWalletInfo(walletId, keychain.getCoin());
                        bitPayModel.setCoin(keychain.getCoin());
                        bitPayModel.setId(walletId);
                        bitPayModel.setWalletType(keychain.getType());
                        bitPayModel.setKeychainId(keychain.getId().toString());
                        bitPayModel.setStockinfoId(keychain.getStockinfoId().toString());
                        bitPayModel.setFeeTxConfirmTarget(keychain.getFeeTxConfirmTarget());
                        walletList.add(bitPayModel);
                    }
                    catch (Exception e)
                    {
                        logger.error(e.getMessage(), e);
                    }
                }
                else
                {
                    BitPayModel bitPayModel = new BitPayModel();
                    bitPayModel.setWalletType(keychain.getType());
                    bitPayModel.setKeychainId(keychain.getId().toString());
                    walletList.add(bitPayModel);
                }
            }
        }
        PaginateResult<BitPayModel> result = new PaginateResult<BitPayModel>();
        result.setList(walletList);
        result.setPage(pagin);
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
    @RequestMapping(value = "/bitpayKeychain/del", method = RequestMethod.POST)
    @RequiresPermissions("trade:setting:keychain:operator")
    @ApiOperation(value = "根据指定ID删除", httpMethod = "POST")
    @ApiImplicitParam(name = "ids", value = "以','分割的编号组", paramType = "form")
    public JsonMessage del(String ids) throws BusinessException
    {
        bitpayKeychainService.removeBatch(ids.split(","));
        return getJsonMessage(CommonEnums.SUCCESS);
    }
    
    /**
     * 修改支付密码
     * @param oldPass   旧密码
     * @param newPass   新密码
     * @return
     * @author 施建波  2017年7月19日 下午4:49:19
     * @throws BusinessException 
     */
    @CSRFToken
    @RequestMapping(value = "/bitpayKeychain/chagePwd/update")
    @RequiresPermissions("trade:setting:keychain:operator")
    @ResponseBody
    public JsonMessage passUpdate(Long id, String oldPass, String newPass, String newPass2, Integer type) throws BusinessException
    {
        UserPrincipal principal = OnLineUserUtils.getPrincipal();
        if (!"admin".equals(principal.getUserName())) { throw new BusinessException("只有管理员才能修改支付密码"); }
        if (null == id) { throw new BusinessException("修改的钱包参数记录不存在"); }
        if (!newPass.equals(newPass2)) { throw new BusinessException("密码不一致"); }
        if (StringUtils.isBlank(newPass)) { throw new BusinessException("新密码不能为空"); }
        if (null == type || !ValidateUtils.isInRange(type, 1, 2)) { throw new BusinessException("钱包类型错误"); }
        BitpayKeychain bitpayKeychain = bitpayKeychainService.selectByPrimaryKey(id);
        if (null == bitpayKeychain) { throw new BusinessException("修改的钱包参数记录不存在"); }
        if (StringUtils.isBlank(bitpayKeychain.getXprv())) { throw new BusinessException("加密后的私钥不存在"); }
        if (StringUtils.isBlank(bitpayKeychain.getXpub())) { throw new BusinessException("公钥不存在"); }
        try
        {
            if (StringUtils.isNotBlank(bitpayKeychain.getSystemPass()))
            {
                if (StringUtils.isBlank(oldPass)) { throw new BusinessException("旧密码不能为空"); }
                oldPass = passDecryption(bitpayKeychain, oldPass);
            }
            // 创建新的钱包密码
            String newWalletPass = bitpayKeychainService.builderEncryptPassword();
            // 创建新的系统密码
            String newSystemPass = bitpayKeychainService.builderEncryptPassword();
            // 合并新密码和新系统密码
            newPass = StringMerge.processString(newPass, newSystemPass);
            // 加密新的钱包密码
            String ciphertext = AES256.encrypt(newWalletPass, newPass);
            if (null == ciphertext) { throw new BusinessException("新密码创建失败"); }
            // 通过BITGO外部接口修改钱包密码
            // String xprv = bitGoRemoteService.updateKeychain(oldPass, newWalletPass, bitpayKeychain.getXprv(), bitpayKeychain.getXpub(), bitpayKeychain.getToken());
            String xprv = bitGoRemoteService.updateKeychain("btc", oldPass, newWalletPass, bitpayKeychain.getXprv(), bitpayKeychain.getXpub(), bitpayKeychain.getToken());
            // 更新数据库
            bitpayKeychain.setXprv(xprv);
            bitpayKeychain.setSystemPass(newSystemPass);
            bitpayKeychain.setCiphertext(ciphertext);
            bitpayKeychainService.updateByPrimaryKey(bitpayKeychain);
        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new BusinessException("修改密码失败");
        }
        return super.getJsonMessage(CommonEnums.SUCCESS);
    }
    
    protected String passDecryption(BitpayKeychain bitpayKeychain, String payPass) throws BusinessException
    {
        // 根据旧密码解密钱包密码
        payPass = StringMerge.processString(payPass, bitpayKeychain.getSystemPass());
        payPass = AES256.decrypt(bitpayKeychain.getCiphertext(), payPass);
        if (null == payPass) { throw new BusinessException("支付密码错误"); }
        return payPass;
    }
}
