package com.blocain.bitms.tools.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * outlook2 JSON MODEL
 * <p>File：TreeModel.java </p>
 * <p>Title: TreeModel </p>
 * <p>Description:TreeModel </p>
 * <p>Copyright: Copyright (c) 17/6/22</p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class TreeModel implements Serializable
{
    private static final long serialVersionUID = 4813767839529192339L;
    
    private Long              id;
    
    private Long              pid;
    
    private String            text;
    
    private String            state            = "open";              // open,closed
    
    private boolean           checked          = false;
    
    private Object            attributes;
    
    private String            iconCls;
    
    private String            openMode;
    
    private List<TreeModel>   children;
    
    @JSONField(serialize = false)
    private Integer           sort;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Long getPid()
    {
        return pid;
    }
    
    public void setPid(Long pid)
    {
        this.pid = pid;
    }
    
    public String getText()
    {
        return text;
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    public String getState()
    {
        return state;
    }
    
    public void setState(String state)
    {
        this.state = state;
    }
    
    public boolean isChecked()
    {
        return checked;
    }
    
    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }
    
    public Object getAttributes()
    {
        return attributes;
    }
    
    public void setAttributes(Object attributes)
    {
        this.attributes = attributes;
    }
    
    public String getIconCls()
    {
        return iconCls;
    }
    
    public void setIconCls(String iconCls)
    {
        this.iconCls = iconCls;
    }
    
    public String getOpenMode()
    {
        return openMode;
    }
    
    public void setOpenMode(String openMode)
    {
        this.openMode = openMode;
    }
    
    public List<TreeModel> getChildren()
    {
        return children;
    }
    
    public void setChildren(List<TreeModel> children)
    {
        this.children = children;
    }
    
    public Integer getSort()
    {
        return sort;
    }
    
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }
}
