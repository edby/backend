/*
 * @(#)WalletController.java 2017年7月18日 下午2:34:59
 * Copyright 2017 施建波, Inc. All rights reserved. BloCain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.bitpay.controller;

import com.blocain.bitms.bitpay.common.AES256;
import com.blocain.bitms.bitpay.common.ApplicationConst;
import com.blocain.bitms.bitpay.common.JsonMessage;
import com.blocain.bitms.bitpay.common.StringMerge;
import com.blocain.bitms.bitpay.entity.BitpayKeychain;
import com.blocain.bitms.bitpay.entity.Member;
import com.blocain.bitms.bitpay.entity.TransferRecord;
import com.blocain.bitms.bitpay.service.BitpayKeychainService;
import com.blocain.bitms.bitpay.service.TransferRecordService;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.payment.bitgo.model.BitPayModel;
import com.blocain.bitms.payment.remote.BitGoRemoteService;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.SerialnoUtils;
import com.blocain.bitms.tools.utils.ValidateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>File：WalletController.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2017年7月18日 下午2:34:59</p>
 * <p>Company: BloCain</p>
 * @author 施建波
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/wallet")
public class WalletController extends BaseController
{
    public static final Logger logger = LoggerFactory.getLogger(WalletController.class);
    
    @Autowired
    private BitGoRemoteService bitGoRemoteService;
    
    @Autowired
    private BitpayKeychainService bitpayKeychainService;
    
    @Autowired
    private TransferRecordService transferRecordService;
    
    @RequestMapping(value = "/search")
    public String search(){
        return "admin/wallet/search";
    }
    
    /**
     * 钱包列表页面
     * @param request
     * @param model
     * @return
     * @author 施建波  2017年7月19日 下午4:37:59
     */
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model){
        List<BitPayModel> walletList = Lists.newArrayList();
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
        List<BitpayKeychain> keychainList = bitpayKeychainService.findBitpayKeychainSearch(bitpayKeychain);
        if(CollectionUtils.isNotEmpty(keychainList)) {
            for(BitpayKeychain keychain:keychainList) {
                String walletId = keychain.getWalletId();
                String token = keychain.getToken();
                if(StringUtils.isNotBlank(walletId) && StringUtils.isNotBlank(token)) {
                    try {
                        BitPayModel bitPayModel = bitGoRemoteService.getWalletInfo(walletId, token);
                        bitPayModel.setId(walletId);
                        bitPayModel.setWalletType(keychain.getType());
                        bitPayModel.setKeychainId(keychain.getId().toString());
                        walletList.add(bitPayModel);
                    }catch(Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }else {
                    BitPayModel bitPayModel = new BitPayModel();
                    bitPayModel.setWalletType(keychain.getType());
                    bitPayModel.setKeychainId(keychain.getId().toString());
                    walletList.add(bitPayModel);
                }
            }
        }
        model.addAttribute("walletList", walletList);
        return "admin/wallet/list";
    }
    
    /**
     * 钱包参数页
     * @param id
     * @param request
     * @param model
     * @return
     * @author 施建波  2017年7月19日 下午4:38:24
     */
    @RequestMapping(value = "/info")
    public String info(Long id,HttpServletRequest request, Model model){
        if(null != id) {
            BitpayKeychain bitpayKeychain = new BitpayKeychain();
            bitpayKeychain.setId(id);
            bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
            bitpayKeychain = bitpayKeychainService.findBitpayKeychain(id);
            model.addAttribute("keychain", bitpayKeychain);
        }
        return "admin/wallet/info";
    }
    
    /**
     * 保存修改钱包参数
     * @param bitpayKeychain
     * @return
     * @author 施建波  2017年7月19日 下午4:38:38
     * @throws BusinessException 
     */
    @RequestMapping(value = "/save.ajax")
    @ResponseBody
    public JsonMessage save(BitpayKeychain bitpayKeychain) throws BusinessException {
        if(StringUtils.isBlank(bitpayKeychain.getWalletName())) {
            throw new BusinessException("钱包名称不能为空");
        }
        if(StringUtils.isBlank(bitpayKeychain.getToken())) {
            throw new BusinessException("TOKEN不能为空");
        }
        if(null == bitpayKeychain.getType() || !ValidateUtils.isInRange(bitpayKeychain.getType(), 1, 2)) {
            throw new BusinessException("钱包类型错误");
        }
        if(null == bitpayKeychain.getFeeTxConfirmTarget() || !ValidateUtils.isInRange(bitpayKeychain.getFeeTxConfirmTarget(), 2, 20)) {
            throw new BusinessException("手续费费率必须大于等2或小于等20");
        }
        
        BitpayKeychain oldKeychain = new BitpayKeychain();
        if(null != bitpayKeychain.getId()) {
            if("******".equals(bitpayKeychain.getToken())) {
                bitpayKeychain.setToken(null);
            }
            if("******".equals(bitpayKeychain.getXprv())) {
                bitpayKeychain.setXprv(null);
            }
            //设置修改钱包的属性
            BitpayKeychain whereKeychain = new BitpayKeychain();
            whereKeychain.setId(bitpayKeychain.getId());
            whereKeychain.setStockinfoId(super.getStockinfoBtcId());
            oldKeychain = bitpayKeychainService.findBitpayKeychain(bitpayKeychain.getId());
            if(null == oldKeychain) {
                throw new BusinessException("修改的钱包参数记录不存在");
            }
        }else {
            //设置新增钱包的属性
            BitpayKeychain whereKeychain = new BitpayKeychain();
            whereKeychain.setType(bitpayKeychain.getType());
            whereKeychain.setStockinfoId(super.getStockinfoBtcId());
            Integer count = bitpayKeychainService.countBitpayKeychain(whereKeychain);
            if(count > 0) {
                throw new BusinessException("相同的钱包类型不能创建多条"); 
            }
            oldKeychain.setStockinfoId(super.getStockinfoBtcId());
        }
        String oldWalletName = oldKeychain.getWalletName();
        if(!bitpayKeychain.getWalletName().equals(oldWalletName) || 
                StringUtils.isBlank(oldKeychain.getWalletId()) || 
                StringUtils.isBlank(oldKeychain.getXpub())) {
            try {
                //从BITGO接口获取钱包公钥
                BitPayModel bitPayModel = bitGoRemoteService.getWallet(bitpayKeychain.getWalletName(), bitpayKeychain.getToken());
                oldKeychain.setWalletId(bitPayModel.getId());
                bitPayModel = bitGoRemoteService.getWalletInfo(oldKeychain.getWalletId(), bitpayKeychain.getToken());
                oldKeychain.setXpub(bitPayModel.getXpub());
            }catch(BusinessException e) {
                throw e;
            }catch(Exception e) {
                logger.error(e.getMessage(), e);
                throw new BusinessException("bitgo远程接口调用失败"); 
            }
        }
        oldKeychain.setWalletName(bitpayKeychain.getWalletName());
        oldKeychain.setToken(bitpayKeychain.getToken());
        oldKeychain.setXprv(bitpayKeychain.getXprv());
        oldKeychain.setType(bitpayKeychain.getType());
        oldKeychain.setFeeTxConfirmTarget(bitpayKeychain.getFeeTxConfirmTarget());
        bitpayKeychainService.saveBitpayKeychain(oldKeychain);
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS); 
    }
    
    /**
     * 删除钱包
     * @param id
     * @return
     * @author 施建波  2017年7月19日 下午4:38:58
     * @throws BusinessException 
     */
    @RequestMapping(value = "/delete.ajax")
    @ResponseBody
    public JsonMessage delete(Long id) throws BusinessException{
        if(null == id) {
            throw new BusinessException("钱包参数ID不能为空");
        }
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setId(id);
        bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
        bitpayKeychainService.deleteBitpayKeychain(bitpayKeychain); 
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
    }
    
    /**
     * 修改密码页
     * @return
     * @author 施建波  2017年7月19日 下午4:40:30
     */
    @RequestMapping(value = "/pass")
    public String pass(){
        return "admin/wallet/pass";
    }
    
    /**
     * 修改支付密码
     * @param oldPass   旧密码
     * @param newPass   新密码
     * @return
     * @author 施建波  2017年7月19日 下午4:49:19
     * @throws BusinessException 
     */
    @RequestMapping(value = "/pass/update.ajax")
    @ResponseBody
    public JsonMessage passUpdate(String oldPass, String newPass, Integer type, HttpServletRequest request) throws BusinessException{
        Member member = super.getSession(request, ApplicationConst.SESSION_MEMBER_KEY);
        if(!"admin".equals(member.getRole())) {
            throw new BusinessException("只有管理员才能修改支付密码");
        }
        if(StringUtils.isBlank(oldPass)) {
            throw new BusinessException("旧密码不能为空");
        }
        if(StringUtils.isBlank(newPass)) {
            throw new BusinessException("新密码不能为空");
        }
        if(null == type || !ValidateUtils.isInRange(type, 1, 2)) {
            throw new BusinessException("钱包类型错误");
        }
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setType(type);
        bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
        bitpayKeychain = bitpayKeychainService.findBitpayKeychain(bitpayKeychain);
        if(null == bitpayKeychain) {
            throw new BusinessException("钱包不存在");
        }
        if(StringUtils.isBlank(bitpayKeychain.getXprv())) { 
            throw new BusinessException("加密后的私钥不存在");
        }
        if(StringUtils.isBlank(bitpayKeychain.getXpub())) {
            throw new BusinessException("公钥不存在");
        }
        try {
            if(StringUtils.isNotBlank(bitpayKeychain.getSystemPass())) {
                oldPass = super.passDecryption(bitpayKeychain, oldPass);
            }
            //创建新的钱包密码
            String newWalletPass = EncryptUtils.entryptPassword(SerialnoUtils.buildUUID());
            //创建新的系统密码
            String newSystemPass = EncryptUtils.entryptPassword(SerialnoUtils.buildUUID());
            //合并新密码和新系统密码
            newPass = StringMerge.processString(newPass, newSystemPass);
            //加密新的钱包密码
            String ciphertext = AES256.encrypt(newWalletPass, newPass);
            if(null == ciphertext) {
                throw new BusinessException("新密码创建失败");
            }
            
            //通过BITGO外部接口修改钱包密码
            String xprv = bitGoRemoteService.updateKeychain(oldPass, newWalletPass, bitpayKeychain.getXprv(), bitpayKeychain.getXpub(), bitpayKeychain.getToken());
            
            //更新数据库
            bitpayKeychain.setXprv(xprv);
            bitpayKeychain.setSystemPass(newSystemPass);
            bitpayKeychain.setCiphertext(ciphertext);
            bitpayKeychainService.updateBitpayKeychain(bitpayKeychain);
        }catch(BusinessException e){
            throw e;
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException("修改密码失败");
        }
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
    }
    
    /**
     * 转帐页
     * @return
     * @author 施建波  2017年7月19日 下午4:40:30
     */
    @RequestMapping(value = "/transfer")
    public String transfer(Model model){
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
        bitpayKeychain.setType(1);
        bitpayKeychain = bitpayKeychainService.findBitpayKeychain(bitpayKeychain);
        model.addAttribute("wallet", bitpayKeychain);
        return "admin/wallet/transfer";
    }
    
    /**
     * 转帐确认
     * @param transferRecord
     * @return
     * @author 施建波  2017年7月20日 下午1:38:41
     * @throws BusinessException 
     */
    @RequestMapping(value = "/transfer/confirm.ajax")
    @ResponseBody
    public JsonMessage transferConfirm(TransferRecord transferRecord) throws BusinessException{
        if(null == transferRecord.getAmount() || transferRecord.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("转帐金额必须大于0");
        }
        if(null != transferRecord.getFee() && transferRecord.getFee().compareTo(new BigDecimal(0.1)) > 0) {
            throw new BusinessException("手续费不能大于0.1");
        }
        if(StringUtils.isBlank(transferRecord.getAddress())) {
            throw new BusinessException("转帐地址不能为空");
        }
        if(StringUtils.isBlank(transferRecord.getPayPass())) {
            throw new BusinessException("支付密码不能为空");
        }
        String payPass = transferRecord.getPayPass();
        BitpayKeychain bitpayKeychain = new BitpayKeychain();
        bitpayKeychain.setType(1);
        bitpayKeychain.setStockinfoId(super.getStockinfoBtcId());
        bitpayKeychain = bitpayKeychainService.findBitpayKeychain(bitpayKeychain);
        if(null == bitpayKeychain) {
            throw new BusinessException("钱包不存在");
        }
        if(StringUtils.isNotBlank(bitpayKeychain.getSystemPass())) {
            payPass = super.passDecryption(bitpayKeychain, payPass);
        }
        Long fee = null;
        if(null != transferRecord.getFee()) {
            fee = super.chargAmount(transferRecord.getFee());
        }
        try {
            //调用外部接口交易
            BitPayModel bitPayModel = bitGoRemoteService.sendCoins(bitpayKeychain.getWalletId(), transferRecord.getAddress(), super.chargAmount(transferRecord.getAmount()), fee, bitpayKeychain.getToken(), payPass);
            //创建交易记录
            transferRecord.setId(null);
            transferRecord.setWalletId(bitpayKeychain.getWalletId());
            transferRecord.setWalletName(bitpayKeychain.getWalletName());
            transferRecord.setTransId(bitPayModel.getHash());
            transferRecordService.saveTransferRecord(transferRecord);
        }catch(BusinessException e) {
            throw e;
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException("转帐失败");
        }
        return super.getJsonMessage(ApplicationConst.ERROR_CODE_SUCCESS);
    }
//    
//    public static void main(String[] args) {
//        System.out.println(1);
//        JSONObject json = new JSONObject();
//        json.put("refrenceId", "0AC15143E6F548038292686BDA878B49");
//        Map map = Maps.newHashMap();
//        map.put("refrenceId", "0AC15143E6F548038292686BDA878B49");
//        map.put("refrenceId2", "0AC15143E6F548038292686BDA878B49");
//        String str = ParameterUtils.getDataFromMap(map);
//        System.out.println(str);
//        System.out.println(json);
//    }
}
