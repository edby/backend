/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.service;

import java.util.List;

import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.boss.common.model.DictModel;
import com.blocain.bitms.orm.core.GenericService;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.exception.BusinessException;

/**
 * 数据典 服务接口
 * <p>File：DictionaryService.java </p>
 * <p>Title: DictionaryService </p>
 * <p>Description:DictionaryService </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
public interface DictionaryService extends GenericService<Dictionary>
{
    /**
     * 根据字典编码取字典数据
     * @param lang
     * @param code
     * @return {@link List}
     * @throws BusinessException
     */
    List<Dictionary> findByCode(String lang,String code) throws BusinessException;
    
    /**
     * 根据上线ID返回树形结构数据
     * @param id
     * @return {@link List}
     * @throws BusinessException
     */
    List<TreeModel> findByDict(String id) throws BusinessException;

    /**
     * 取所有的数据字典
     * @return
     * @throws BusinessException
     */
    List<DictModel> findAllDict() throws BusinessException;

}
