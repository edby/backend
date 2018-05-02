package com.blocain.bitms.boss.common.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 字典模型 Introduce
 * <p>File：DictModel.java</p>
 * <p>Title: DictModel</p>
 * <p>Description: DictModel</p>
 * <p>Copyright: Copyright (c) 2017/8/3</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class DictModel implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2148179382879547992L;
    
    @JSONField(serialize = false)
    private Long          id;
    
    /**编码*/
    private String          code;
    
    /**名称*/
    private String          name;
    
    /**语言*/
    private String          lang;
    
    /** 子项*/
    private List<DictModel> children;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getLang()
    {
        return lang;
    }
    
    public void setLang(String lang)
    {
        this.lang = lang;
    }
    
    public List<DictModel> getChildren()
    {
        return children;
    }
    
    public void setChildren(List<DictModel> children)
    {
        this.children = children;
    }
}
