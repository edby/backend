/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.trade.stockinfo.service;

import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.PaginateResult;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.trade.stockinfo.entity.Erc20Token;

import java.util.List;

/**
 * ERC20 TOKEN 服务接口
 * <p>File：Erc20TokenService.java </p>
 * <p>Title: Erc20TokenService </p>
 * <p>Description:Erc20TokenService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface Erc20TokenService extends GenericService<Erc20Token>
{
    Erc20Token getErc20Token(String contractAddr, String pair);
    
    /**
     *  激活TOKEN
     * @param addr
     * @param accountId
     */
    void doActiveToken(String addr, Long accountId, Long unid) throws BusinessException;
    
    /**
     *  轮询关闭TOKEN
     */
    void autoCloseActiveToken();
    
    /**
     *  关闭TOKEN
     */
    void doCloseActiveToken(Erc20Token erc20Token);
    
    /**
     * 我的邀请
     * @param token
     * @return
     */
    PaginateResult<Erc20Token> findListForAward(Erc20Token token);
    
    /**
     * 按条件查询
     * @param symbol
     * @param contractAddr
     * @return
     */
    List<Erc20Token> searchByKey(String symbol, String contractAddr);

    /**
     * 查询高度字段最小值记录findMinHeight
     * @return
     */
    List<Erc20Token> findMinHeight();
}
