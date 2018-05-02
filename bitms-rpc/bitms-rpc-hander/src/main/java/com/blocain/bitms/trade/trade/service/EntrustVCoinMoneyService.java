/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.service;

import com.blocain.bitms.orm.annotation.SlaveDataSource;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.bean.Pagination;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.model.FeeModel;
import javafx.util.converter.BigDecimalStringConverter;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.List;

/**
 * 委托表X 服务接口
 * <p>File：EntrustVCoinMoneyService.java </p>
 * <p>Title: EntrustVCoinMoneyService </p>
 * <p>Description:EntrustVCoinMoneyService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface EntrustVCoinMoneyService extends GenericService<EntrustVCoinMoney>{

    /**
     * 根据主键查询数据
     * @param id
     * @return {@link T}
     * @throws BusinessException
     */
    EntrustVCoinMoney  selectByPrimaryKey(String tableName,Long id) throws BusinessException;

    /**
     * 行级锁读取数据
     * @param tableName 交易对
     * @param id
     * @return
     * @throws BusinessException
     */
    EntrustVCoinMoney selectByPrimaryKeyOnRowLock(String tableName,Long id) throws BusinessException;

    /**
     * 查询单账户撮合交易还在委托的委托数量（包括委托买入和委托卖出）
     * @param accountId
     * @param entrustStockinfoId
     * @return
     */
    BigDecimal findSumMatchEntrustVCoinMoneyAmtByAccount(Long accountId,Long entrustStockinfoId);

    /**
     * 查询所有未完全成交的、未撤销的委托
     * @return
     */
    List<EntrustVCoinMoney> findAllInEntrust(String tableName) throws BusinessException;

    /**
     * 查询用户所有未完全成交的、未撤销的委托
     * @return
     */
    List<EntrustVCoinMoney> findAccountInEntrust(String tableName,Long AccountId) throws BusinessException;

    /**
     * 查询所有未完全成交的、未撤销的委托
     * @return
     */
    PaginateResult<EntrustVCoinMoney>  findAllInEntrust(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 查询所有交割中撤单的委托（不含超级用户）
     * @return
     */
    PaginateResult<EntrustVCoinMoney>  findWithdrawBySysEntrust(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 获取用户正在委托中的委托
     * @param entrustVCoinMoney
     */
    List<EntrustVCoinMoney>  getAccountDoingEntrustVCoinMoneyList(EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 获取用户正在委托中的委托，按委托价降序排列
     */
    List<EntrustVCoinMoney>  getAccountDoingEntrustVCoinMoneyListByPrice(EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;
    /**
     * 获取用户正在委托中的委托
     * @param entrustVCoinMoney
     */
    PaginateResult<EntrustVCoinMoney>  getAccountDoingEntrustVCoinMoneyPagin(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 获取用户历史委托
     * @param entrustVCoinMoney
     */
    @SlaveDataSource()
    PaginateResult<EntrustVCoinMoney>  getAccountHistoryEntrustVCoinMoneyList(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 空头爆仓 超级用户挂单的盈利情况
     * @return
     */
    BigDecimal findSumShortReserveAllocation(String tableName) throws BusinessException;

    /**
     * 多头爆仓 超级用户挂单的盈利情况
     * @return
     */
    BigDecimal findSumLongReserveAllocation(String tableName) throws BusinessException;

    /**
     * 获取从上一个交割时间开始的所有委托(条件查询)
     * @param entrustVCoinMoney
     */
    PaginateResult<EntrustVCoinMoney> findListAfterPreSettlement(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 获取超级用户委托挂单(条件查询)
     * @param entrustVCoinMoney
     */
    PaginateResult<EntrustVCoinMoney> findAdminEnturstList(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 查询所有委托表数据
     * @return
     */
    PaginateResult<EntrustVCoinMoney>  searchAll(Pagination pagin,EntrustVCoinMoney entrustVCoinMoney) throws BusinessException;

    /**
     * 获取用户正在委托中的委托
     * @param accountId
     * @param exchangePairMoney
     */
    Long getAccountDoingEntrustVCoinMoneyCnt(Long accountId,Long exchangePairMoney);

    /**
     * 获取单币种进入撮合的委托数量
     * @param exchangePairMoney
     * @return
     */
    Long getMoneyDoingEntrustVCoinMoneyCnt(Long exchangePairMoney);

    /**
     * 统计多头超级用户挂单未成交数量金额
     * @return
     */
    BigDecimal clearCalcLongSuperAccountLossAmt(@Param("tableName") String tableName, @Param("clearPrice") BigDecimal clearPrice, @Param("relatedStockinfoId") Long relatedStockinfoId) throws BusinessException;

    /**
     * 统计空头超级用户挂单未成交数量金额
     * @return
     */
    BigDecimal clearCalcShortSuperAccountLossAmt(@Param("tableName") String tableName, @Param("clearPrice") BigDecimal clearPrice, @Param("relatedStockinfoId") Long relatedStockinfoId) throws BusinessException;

    /**
     * 获取交易手续费
     * @param yestoday
     * @param today
     * @return
     * @throws BusinessException
     */
    List<FeeModel> selectSumFeeNeedAward(String tableName,String yestoday,String today) throws BusinessException;

}
