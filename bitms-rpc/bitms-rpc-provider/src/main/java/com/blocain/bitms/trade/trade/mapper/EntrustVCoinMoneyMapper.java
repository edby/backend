/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.trade.mapper;

import com.blocain.bitms.orm.annotation.MyBatisDao;
import com.blocain.bitms.orm.core.GenericMapper;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.trade.entity.EntrustVCoinMoney;
import com.blocain.bitms.trade.trade.model.FeeModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 委托表X 持久层接口
 * <p>File：EntrustVCoinMoneyMapper.java </p>
 * <p>Title: EntrustVCoinMoneyMapper </p>
 * <p>Description:EntrustVCoinMoneyMapper </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@MyBatisDao
public interface EntrustVCoinMoneyMapper extends GenericMapper<EntrustVCoinMoney>
{
    /**
     * 根据主键id获取唯一记录
     * @param id
     * @return
     * @throws BusinessException
     */
    EntrustVCoinMoney selectByPrimaryKey(@Param("tableName") String tableName, @Param("id") Long id) throws BusinessException;
    
    /**
     * 行级锁读取数据
     * @param id
     * @return
     * @throws BusinessException
     */
    EntrustVCoinMoney selectByPrimaryKeyOnRowLock(@Param("tableName") String tableName, @Param("id") Long id) throws BusinessException;
    
    BigDecimal findSumMatchEntrustVCoinMoneyAmtByAccount(@Param("accountId") Long accountId, @Param("entrustStockinfoId") Long entrustStockinfoId);
    
    List<EntrustVCoinMoney> findAccountInEntrust(@Param("tableName") String tableName, @Param("accountId") Long accountId);
    
    List<EntrustVCoinMoney> findAllInEntrust(EntrustVCoinMoney entrustVCoinMoney);
    
    List<EntrustVCoinMoney> findWithdrawBySysEntrust(EntrustVCoinMoney entrustVCoinMoney);

    /**
     * 获取从上一个交割时间开始的所有委托(条件查询)
     * @param entrustVCoinMoney
     */
    List<EntrustVCoinMoney> findListAfterPreSettlement(EntrustVCoinMoney entrustVCoinMoney);
    
    /**
     * 获取超级用户委托挂单(条件查询)
     * @param entrustVCoinMoney
     */
    List<EntrustVCoinMoney> findAdminEnturstList(EntrustVCoinMoney entrustVCoinMoney);
    
    /**
     * 查询所有的委托
     * @param entrustVCoinMoney
     */
    List<EntrustVCoinMoney> searchAll(EntrustVCoinMoney entrustVCoinMoney);
    
    /**
     * 获取用户历史委托
     * @param entrustVCoinMoney
     */
    List<EntrustVCoinMoney> getAccountHistoryEntrustVCoinMoneyList(EntrustVCoinMoney entrustVCoinMoney);

    /**
     * 获取用户当前查询
     * @param entrustVCoinMoney
     */
    List<EntrustVCoinMoney> getAccountDoingEntrustVCoinMoneyList(EntrustVCoinMoney entrustVCoinMoney);
    
    /**
     * 空头爆仓 超级用户挂单的盈利情况
     * @return
     */
    BigDecimal findSumShortReserveAllocation(@Param("tableName") String tableName);
    
    /**
     * 多头爆仓 超级用户挂单的盈利情况
     * @return
     */
    BigDecimal findSumLongReserveAllocation(@Param("tableName") String tableName);

    /**
     * 获取用户正在委托中的委托数量
     * @param accountId
     * @param tableName
     */
    Long getAccountDoingEntrustVCoinMoneyCnt(@Param("accountId")Long accountId,@Param("tableName") String tableName);

    /**
     * 获取单币种进入撮合的委托数量
     * @param tableName
     * @return
     */
    Long getMoneyDoingEntrustVCoinMoneyCnt(@Param("tableName") String tableName);

    /**
     * 统计多头超级用户挂单未成交数量金额
     * @return
     */
    BigDecimal clearCalcLongSuperAccountLossAmt(@Param("tableName") String tableName, @Param("clearPrice") BigDecimal clearPrice, @Param("relatedStockinfoId") Long relatedStockinfoId);

    /**
     * 统计空头超级用户挂单未成交数量金额
     * @return
     */
    BigDecimal clearCalcShortSuperAccountLossAmt(@Param("tableName") String tableName, @Param("clearPrice") BigDecimal clearPrice, @Param("relatedStockinfoId") Long relatedStockinfoId);

    List<FeeModel> selectSumFeeNeedAward(@Param("tableName") String tableName,@Param("yestoday") String yestoday,@Param("today") String today);

    List<EntrustVCoinMoney> getAccountDoingEntrustVCoinMoneyListByPrice(EntrustVCoinMoney entrustVCoinMoney);
}
