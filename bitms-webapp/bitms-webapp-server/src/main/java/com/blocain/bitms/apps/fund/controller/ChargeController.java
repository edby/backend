package com.blocain.bitms.apps.fund.controller;

import com.alibaba.fastjson.JSON;
import com.blocain.bitms.apps.account.beans.AuthTokenRequest;
import com.blocain.bitms.apps.account.beans.SessionInfo;
import com.blocain.bitms.apps.basic.beans.AppsMessage;
import com.blocain.bitms.apps.basic.controller.AppsController;
import com.blocain.bitms.apps.basic.utils.AuthTokenUtils;
import com.blocain.bitms.apps.fund.model.ChargeListModel;
import com.blocain.bitms.apps.sdk.BitmsConstants;
import com.blocain.bitms.apps.sdk.internal.util.Encrypt;
import com.blocain.bitms.apps.spot.model.EntrustPageModel;
import com.blocain.bitms.orm.utils.EncryptUtils;
import com.blocain.bitms.security.OnLineUserUtils;
import com.blocain.bitms.security.shiro.model.UserPrincipal;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.consts.BitmsConst;
import com.blocain.bitms.tools.enums.CommonEnums;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.JsonUtils;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.blocain.bitms.trade.account.entity.Account;
import com.blocain.bitms.trade.account.enums.AccountEnums;
import com.blocain.bitms.trade.account.service.AccountService;
import com.blocain.bitms.trade.fund.consts.FundConsts;
import com.blocain.bitms.trade.fund.entity.*;
import com.blocain.bitms.trade.fund.enums.FundEnums;
import com.blocain.bitms.trade.fund.service.*;
import com.blocain.bitms.trade.stockinfo.entity.StockInfo;
import com.blocain.bitms.trade.stockinfo.service.StockInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChargeController Introduce
 * <p>Title: ChargeController</p>
 * <p>File：ChargeController.java</p>
 * <p>Description: ChargeController</p>
 * <p>Copyright: Copyright (c) 2018-03-21</p>
 * <p>Company: BloCain</p>
 *
 * @author zhangchunxi
 * @version 1.0
 */
@Controller
@RequestMapping(BitmsConst.FUND)
public class ChargeController extends AppsController {
    @Autowired(required = false)
    private StockInfoService stockInfoService;

    @Autowired(required = false)
    private SystemWalletAddrService systemWalletAddrService;

    @Autowired(required = false)
    private SystemWalletAddrERC20Service systemWalletAddrERC20Service;

    @Autowired(required = false)
    private AccountService accountService;

    @Autowired(required = false)
    private BankRechargeService bankRechargeService;

    @Autowired(required = false)
    private BlockTransConfirmService blockTransConfirmService;

    @Autowired(required = false)
    private BlockTransConfirmERC20Service blockTransConfirmERC20Service;

    /**
     * 获取用户充值地址
     *
     * @param request
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/recharge/getRecharegeAddr")
    public AppsMessage entrustxOnDoingRequest(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        EntrustPageModel tokenRequest = checkSign(params, EntrustPageModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session,tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        EntrustPageModel entityRequest = new EntrustPageModel();
        BeanUtils.copyProperties(tokenRequest, entityRequest);
        Map<String, Object> map = new HashMap<>();
        if (beanValidator(message, entityRequest)) {
            // 登录判断
            Long accountId = session.getId();
            if (null == accountId) {
                throw new BusinessException(CommonEnums.USER_NOT_LOGIN);
            }
            Long exchangePairMoney = null;
            // 验证和获取币种ID
            StockInfo stockInfo = new StockInfo();
            // stockInfo.setIsExchange(FundConsts.PUBLIC_STATUS_YES);
            stockInfo.setRemark(entityRequest.getSymbol());
            List<StockInfo> stockInfoList = stockInfoService.findList(stockInfo);
            if (stockInfoList.size() == 0) {
                logger.debug("币种错误");
                return getJsonMessage(CommonEnums.PARAMS_VALID_ERR);
            }
            stockInfo = stockInfoList.get(0);
            exchangePairMoney = stockInfo.getId();
            if (exchangePairMoney == null) {
                logger.debug("币种错误 入参为空");
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
            }
            if (!StringUtils.equalsIgnoreCase(stockInfo.getCanRecharge(), FundConsts.PUBLIC_STATUS_YES)) {
                logger.debug("该币种暂时不支持充值！");
                throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_RECHARGE);
            }
            // 数字货币
            if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_DIGITALCOIN)) {
                if (stockInfo.getId().longValue() == FundConsts.WALLET_BTC_TYPE.longValue()) {
                    if (account != null) {
                        if (org.apache.commons.lang3.StringUtils.isBlank(account.getMobNo())) {
                            throw new BusinessException(AccountEnums.ACCOUNT_PHONE_NOTBIND);
                        }
                    }
                    // 查询目前最新充值钱包对应的最新地址是否存在
                    SystemWalletAddr systemWalletAddr = new SystemWalletAddr();
                    systemWalletAddr.setAccountId(accountId);
                    systemWalletAddr.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
                    systemWalletAddr.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                    List<SystemWalletAddr> list = systemWalletAddrService.findList(systemWalletAddr);
                    if (ListUtils.isNotNull(list)) {
                        systemWalletAddr = list.get(0);
                        if (null != systemWalletAddr && !systemWalletAddr.verifySignature()) {// 校验数据
                            logger.info("充值地址 数据校验失败");
                            throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                        } else {
                            logger.info("充值地址 数据校验成功");
                            map.put("walletAddr", systemWalletAddr.getWalletAddr());
                            map.put("qrcodeStr", "bitcoin:" + systemWalletAddr.getWalletAddr());
                        }
                    } else {
                        try {
                            // add by sunbiao start
                            // 默认产生BTC钱包地址信息 注册的时候自动开通可能会影响注册 所以放到这里进行开通
                            systemWalletAddrService.createBtcWalletAddress(account.getId(), BitmsConst.DEFAULT_UNID);
                            systemWalletAddr = new SystemWalletAddr();
                            systemWalletAddr.setAccountId(accountId);
                            systemWalletAddr.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
                            systemWalletAddr.setStockinfoId(FundConsts.WALLET_BTC_TYPE);
                            list = systemWalletAddrService.findList(systemWalletAddr);
                            // add by sunbiao end
                            if (list.size() > 0) {
                                map.put("walletAddr", list.get(0).getWalletAddr());
                                map.put("qrcodeStr", "bitcoin:" + list.get(0).getWalletAddr());
                            } else {
                                throw new BusinessException("Create failure");
                            }
                        } catch (BusinessException e) {
                            logger.error("创建钱包地址失败:{}", e.getLocalizedMessage());
                            throw new BusinessException("Create failure");
                        }
                    }
                } else {
                    throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_RECHARGE);
                }
            }
            // 现金充值
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_CASHCOIN)) {
                throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_RECHARGE);
            }
            // ERC20充值
            else if (StringUtils.equalsIgnoreCase(stockInfo.getStockType(), FundConsts.STOCKTYPE_ERC20_TOKEN)) {
                // 查询币种
                // 查询目前最新充值钱包对应的最新地址是否存在
                SystemWalletAddrERC20 systemWalletAddrERC20 = new SystemWalletAddrERC20();
                systemWalletAddrERC20.setAccountId(accountId.toString());
                systemWalletAddrERC20.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
                systemWalletAddrERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                List<SystemWalletAddrERC20> list = systemWalletAddrERC20Service.findList(systemWalletAddrERC20);
                if (ListUtils.isNotNull(list)) {
                    systemWalletAddrERC20 = list.get(0);
                    if (null != systemWalletAddrERC20 && !systemWalletAddrERC20.verifySignature()) {// 校验数据
                        logger.info("充值地址 数据校验失败");
                        throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
                    } else {
                        logger.info("充值地址 数据校验成功");
                        map.put("walletAddr", systemWalletAddrERC20.getWalletAddr());
                        map.put("qrcodeStr", systemWalletAddrERC20.getWalletAddr());
                    }
                } else {
                    try {
                        // 默认产生ETH钱包地址信息
                        systemWalletAddrERC20Service.createERC20WalletAddress(accountId, BitmsConst.DEFAULT_UNID, FundConsts.WALLET_ETH_TYPE);
                        systemWalletAddrERC20 = new SystemWalletAddrERC20();
                        systemWalletAddrERC20.setAccountId(accountId.toString());
                        systemWalletAddrERC20.setWalletUsageType(FundConsts.WALLET_USAGE_TYPE_CHARGE_ACCOUNT);
                        systemWalletAddrERC20.setStockinfoId(FundConsts.WALLET_ETH_TYPE);
                        list = systemWalletAddrERC20Service.findList(systemWalletAddrERC20);
                        // add by sunbiao end
                        if (list.size() > 0) {
                            map.put("walletAddr", list.get(0).getWalletAddr());
                            map.put("qrcodeStr", list.get(0).getWalletAddr());
                        } else {
                            throw new BusinessException("Create failure");
                        }
                    } catch (BusinessException e) {
                        logger.error("创建钱包地址失败:{}", e.getLocalizedMessage());
                        throw new BusinessException("Create failure");
                    }
                }
            }
        } else {
            throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_RECHARGE);
        }
        String resultJson = JSON.toJSONString(map);
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        message.setData(Encrypt.encryptContent(resultJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

    /**
     * 获取用户充值记录
     *
     * @return
     * @throws BusinessException
     */
    @ResponseBody
    @RequestMapping("/recharge/cashChargeList")
    public AppsMessage getChargeRecord(HttpServletRequest request) throws BusinessException {
        AppsMessage message = getJsonMessage(CommonEnums.SUCCESS);
        Map<String, String> params = getParameters(request);
        if (!checkParams(params, message)) return message;// 验证公共参数
        ChargeListModel tokenRequest = checkSign(params, ChargeListModel.class); // 校验签名并返回请求参数
        if (!beanValidator(message, tokenRequest)) return message;// 验证业务参数
        String encryptKey = params.get(BitmsConstants.ENCRYPT_KEY);
        SessionInfo session = AuthTokenUtils.getSession(tokenRequest.getAuthToken());
        checkSesion(session,tokenRequest.getAuthToken());
        Account account = checkAccount(session.getId());
        //根据币种和用户id查询stockinfo中的id
        StockInfo stockInfo = new StockInfo();
        stockInfo.setRemark(tokenRequest.getSymbol());
        List<StockInfo> lists = stockInfoService.findList(stockInfo);
        if (lists.size() == 0) throw new BusinessException(CommonEnums.ERROR_NOT_SUPPORT_RECHARGE);
        stockInfo = lists.get(0);
        if (null == lists.get(0).getId()) throw new BusinessException(FundEnums.ERROR_STOCKINFOID_NOT_EXIST);
        String stockType = stockInfo.getStockType();
        if (null == stockType) throw new BusinessException(FundEnums.ERROR_BUSINESSFLAG_ERROR);
        Pagination pagin = new Pagination(tokenRequest.getPage(), tokenRequest.getRows());//分页对象
        String rJson = null;
        switch (stockType) {
            case FundConsts.STOCKTYPE_DIGITALCOIN://digitalCoin
            {
                BlockTransConfirm blockTransConfirm = new BlockTransConfirm();
                blockTransConfirm.setStockinfoId(lists.get(0).getId());
                blockTransConfirm.setAccountId(session.getId());
                blockTransConfirm.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
                PaginateResult<BlockTransConfirm> blockResults = blockTransConfirmService.findChargeList(pagin, blockTransConfirm);
                rJson = JsonUtils.beanToJson(blockResults);
                break;
            }
            case FundConsts.STOCKTYPE_CASHCOIN://cashCoin
            {
                BankRecharge bankRecharge = new BankRecharge();
                bankRecharge.setStockinfoId(lists.get(0).getId());
                bankRecharge.setAccountId(session.getId());
                bankRecharge.setStatus(FundConsts.ACCOUNT_FUND_APPROVE_STATUS_CHECKTHROUGH);
                PaginateResult<BankRecharge> bankResults = bankRechargeService.search(pagin, bankRecharge);
                rJson = JsonUtils.beanToJson(bankResults);
                break;
            }
            case FundConsts.STOCKTYPE_ERC20_TOKEN://erc20Token
            {
                BlockTransConfirmERC20 blockTransConfirmERC20 = new BlockTransConfirmERC20();
                blockTransConfirmERC20.setStockinfoId(lists.get(0).getId());
                blockTransConfirmERC20.setAccountId(session.getId());
                blockTransConfirmERC20.setDirect(FundConsts.WALLET_TRANS_FUND_DIRECT_COLLECT);
                PaginateResult<BlockTransConfirmERC20> blockTransResults = blockTransConfirmERC20Service.findConfirmERC20ChargeList(pagin, blockTransConfirmERC20);
                rJson = JsonUtils.beanToJson(blockTransResults);
                break;
            }
            default:
                throw new BusinessException(CommonEnums.PARAMS_VALID_ERR);
        }
        message.setData(Encrypt.encryptContent(rJson, BitmsConstants.ENCRYPT_TYPE_AES, encryptKey));
        return message;
    }

}
