/*
 * Copyright 2017 BLOCAIN, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.boss.common.service;

import java.util.List;

import com.blocain.bitms.boss.common.model.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blocain.bitms.boss.common.mapper.DictionaryMapper;
import com.blocain.bitms.boss.common.entity.Dictionary;
import com.blocain.bitms.orm.core.GenericServiceImpl;
import com.blocain.bitms.tools.bean.TreeModel;
import com.blocain.bitms.tools.exception.BusinessException;
import com.blocain.bitms.tools.utils.ListUtils;
import com.blocain.bitms.tools.utils.StringUtils;
import com.google.common.collect.Lists;

/**
 * 数据典 服务实现类
 * <p>File：Dictionary.java </p>
 * <p>Title: Dictionary </p>
 * <p>Description:Dictionary </p>
 * <p>Copyright: Copyright (c) May 26, 2015</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@Service
public class DictionaryServiceImpl extends GenericServiceImpl<Dictionary> implements DictionaryService
{
    private DictionaryMapper dictionaryMapper;
    
    @Autowired
    public DictionaryServiceImpl(DictionaryMapper dictionaryMapper)
    {
        super(dictionaryMapper);
        this.dictionaryMapper = dictionaryMapper;
    }
    
    @Override
    public List<Dictionary> findByCode(String lang, String code) throws BusinessException
    {
        return dictionaryMapper.findByCode(lang, code);
    }
    
    @Override
    public List<DictModel> findAllDict() throws BusinessException
    {
        List<DictModel> result = Lists.newArrayList();
        List<Dictionary> data = dictionaryMapper.selectAll();
        for (Dictionary parent : data)
        {
            if (null == parent.getParentId())
            {
                DictModel model = conventModel(parent);
                for (Dictionary child : data)
                {
                    if (null == child.getParentId()) continue;
                    if (model.getId().equals(child.getParentId()))
                    {
                        if (model.getChildren() == null)
                        {
                            model.setChildren(Lists.newArrayList(conventModel(child)));
                        }
                        else
                        {
                            model.getChildren().add(conventModel(child));
                        }
                    }
                }
                result.add(model);
            }
        }
        return result;
    }
    
    @Override
    public List<TreeModel> findByDict(String id) throws BusinessException
    {
        List<Dictionary> data = dictionaryMapper.findByParentId(id);
        if (ListUtils.isNull(data)) return null;
        List<TreeModel> treeModels = Lists.newArrayList();
        for (Dictionary dict : data)
        {
            treeModels.add(conventObject(dict));
        }
        return treeModels;
    }
    
    DictModel conventModel(Dictionary dict)
    {
        DictModel model = new DictModel();
        model.setId(dict.getId());
        model.setCode(dict.getCode());
        model.setLang(dict.getLang());
        model.setName(dict.getName());
        return model;
    }
    
    /**
     * 转换对象类型
     * @param dict
     * @return
     */
    TreeModel conventObject(Dictionary dict)
    {
        TreeModel model = new TreeModel();
        model.setId(dict.getId());
        if (null != dict.getParentId())
        {
            model.setState("closed");
        }
        model.setPid(dict.getParentId());
        model.setText(dict.getName());
        return model;
    }
}
