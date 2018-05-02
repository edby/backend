package com.blocain.bitms.tools.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ZTreeModel Introduce
 * <p>File：ZTreeModel.java </p>
 * <p>Title: ZTreeModel </p>
 * <p>Description:ZTreeModel </p>
 * <p>Copyright: Copyright (c) 17/6/22</p>
 * <p>Company: blocain.com</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class ZTreeModel implements Serializable
{
    private static final long serialVersionUID = 4813767839529192739L;
    
    private String            id;
    
    private String            parentId;
    
    private String            text;
    
    private String            url;
    
    private boolean           leaf;
    
    private boolean           open;
    
    private Integer           isOpen;
    
    private Integer           depath;
    
    private Integer           sort;
    
    private boolean           checked          = false;
    
    private boolean           show             = false;
    
    public List<ZTreeModel>   children;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getParentId()
    {
        return parentId;
    }
    
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }
    
    public String getText()
    {
        return text;
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public boolean isLeaf()
    {
        return leaf;
    }
    
    public void setLeaf(boolean leaf)
    {
        this.leaf = leaf;
    }
    
    public boolean isOpen()
    {
        return open;
    }
    
    public void setOpen(boolean open)
    {
        this.open = open;
    }
    
    public Integer getIsOpen()
    {
        return isOpen;
    }
    
    public void setIsOpen(Integer isOpen)
    {
        this.isOpen = isOpen;
    }
    
    public Integer getDepath()
    {
        return depath;
    }
    
    public void setDepath(Integer depath)
    {
        this.depath = depath;
    }
    
    public Integer getSort()
    {
        return sort;
    }
    
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }
    
    public boolean isChecked()
    {
        return checked;
    }
    
    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }
    
    public boolean isShow()
    {
        return show;
    }
    
    public void setShow(boolean show)
    {
        this.show = show;
    }
    
    public List<ZTreeModel> getChildren()
    {
        return children;
    }
    
    public void setChildren(List<ZTreeModel> children)
    {
        this.children = children;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZTreeModel that = (ZTreeModel) o;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return text != null ? text.equals(that.text) : that.text == null;
    }
    
    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
