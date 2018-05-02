package com.blocain.bitms.trade.robot.config;

import com.blocain.bitms.trade.robot.util.PropertyUtil;

import java.io.Serializable;
import java.util.Properties;

public class ExchangePairConfig implements Serializable
{
    public ExchangePairConfig()
    {
    }
    
    // 计价证券
    private Long    capital;
    
    // 标的证券
    private Long    target;
    
    // 交易对名称
    private String  name;
    
    // 价格精度
    private Integer priceScale;
    
    // 下单量精度
    private Integer amtScale;
    
    // 动态盘口数量
    private Integer dynCount;
    
    // 动态盘口发生概率
    private Integer dynRandom;
    
    // 平台限制挂单最大数量
    private Integer maxOrderSize_plat;
    
    // 循环频率
    private Integer dt;
    
    public ExchangePairConfig(String pairName)
    {
        Properties prop = PropertyUtil.getProperties(pairName + ".properties");
        capital = Long.parseLong(prop.getProperty("exchangePair.capital"));
        target = Long.parseLong(prop.getProperty("exchangePair.target"));
        name = prop.getProperty("exchangePair.name");
        priceScale = Integer.parseInt(prop.getProperty("price.scale"));
        amtScale = Integer.parseInt(prop.getProperty("amt.scale"));
        dynCount = Integer.parseInt(prop.getProperty("dyn.count"));
        dynRandom = Integer.parseInt(prop.getProperty("dyn.random"));
        maxOrderSize_plat = Integer.parseInt(prop.getProperty("maxOrderSize.plat"));
        dt = Integer.parseInt(prop.getProperty("dt"));
    }
    
    public Long getCapital()
    {
        return capital;
    }
    
    public void setCapital(Long capital)
    {
        this.capital = capital;
    }
    
    public Long getTarget()
    {
        return target;
    }
    
    public void setTarget(Long target)
    {
        this.target = target;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Integer getPriceScale()
    {
        return priceScale;
    }
    
    public void setPriceScale(Integer priceScale)
    {
        this.priceScale = priceScale;
    }
    
    public Integer getAmtScale()
    {
        return amtScale;
    }
    
    public void setAmtScale(Integer amtScale)
    {
        this.amtScale = amtScale;
    }
    
    public Integer getDynCount()
    {
        return dynCount;
    }
    
    public void setDynCount(Integer dynCount)
    {
        this.dynCount = dynCount;
    }
    
    public Integer getDynRandom()
    {
        return dynRandom;
    }
    
    public void setDynRandom(Integer dynRandom)
    {
        this.dynRandom = dynRandom;
    }
    
    public Integer getMaxOrderSize_plat()
    {
        return maxOrderSize_plat;
    }
    
    public void setMaxOrderSize_plat(Integer maxOrderSize_plat)
    {
        this.maxOrderSize_plat = maxOrderSize_plat;
    }
    
    public Integer getDt()
    {
        return dt;
    }
    
    public void setDt(Integer dt)
    {
        this.dt = dt;
    }
    
    @Override
    public String toString()
    {
        return "ExchangePairConfig{" + "capital=" + capital + ", target=" + target + ", name='" + name + '\'' + ", priceScale=" + priceScale + ", amtScale=" + amtScale
                + ", dynCount=" + dynCount + ", dynRandom=" + dynRandom + ", maxOrderSize_plat=" + maxOrderSize_plat + ", dt=" + dt + '}';
    }
}
