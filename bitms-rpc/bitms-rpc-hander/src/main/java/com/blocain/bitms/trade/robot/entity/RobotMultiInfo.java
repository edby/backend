package com.blocain.bitms.trade.robot.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RobotMultiInfo implements Serializable
{
    // 币对名
    private String     pairName;
    
    // 配置名称
    private String     configName;
    
    // 机器人启用状态
    private Boolean    botStatus;
    
    // 买单开关
    private Boolean    buySwitch;
    
    // 卖单开关
    private Boolean    sellSwitch;
    
    // btc初始余额
    private BigDecimal btcOrgBal;
    
    // btc当前余额
    private BigDecimal btcCurBal;
    
    // usd初始余额
    private BigDecimal usdOrgBal;
    
    // usd当前余额
    private BigDecimal usdCurBal;
    
    // 机器人启动时间
    private Date       startTime;
    
    // 基准价
    private BigDecimal basePrice;
    
    // 平台所有运行机器人数量
    private Integer    robotAmt;
    
    // 备注消息
    private String     message;
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getConfigName()
    {
        return configName;
    }
    
    public void setConfigName(String configName)
    {
        this.configName = configName;
    }
    
    public String getPairName()
    {
        return pairName;
    }
    
    public void setPairName(String pairName)
    {
        this.pairName = pairName;
    }
    
    public Boolean getBotStatus()
    {
        return botStatus;
    }
    
    public void setBotStatus(Boolean botStatus)
    {
        this.botStatus = botStatus;
    }
    
    public Boolean getBuySwitch()
    {
        return buySwitch;
    }
    
    public void setBuySwitch(Boolean buySwitch)
    {
        this.buySwitch = buySwitch;
    }
    
    public Boolean getSellSwitch()
    {
        return sellSwitch;
    }
    
    public void setSellSwitch(Boolean sellSwitch)
    {
        this.sellSwitch = sellSwitch;
    }
    
    public BigDecimal getBtcOrgBal()
    {
        return btcOrgBal;
    }
    
    public void setBtcOrgBal(BigDecimal btcOrgBal)
    {
        this.btcOrgBal = btcOrgBal;
    }
    
    public BigDecimal getBtcCurBal()
    {
        return btcCurBal;
    }
    
    public void setBtcCurBal(BigDecimal btcCurBal)
    {
        this.btcCurBal = btcCurBal;
    }
    
    public BigDecimal getUsdOrgBal()
    {
        return usdOrgBal;
    }
    
    public void setUsdOrgBal(BigDecimal usdOrgBal)
    {
        this.usdOrgBal = usdOrgBal;
    }
    
    public BigDecimal getUsdCurBal()
    {
        return usdCurBal;
    }
    
    public void setUsdCurBal(BigDecimal usdCurBal)
    {
        this.usdCurBal = usdCurBal;
    }
    
    public Date getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
    
    public BigDecimal getBasePrice()
    {
        return basePrice;
    }
    
    public void setBasePrice(BigDecimal basePrice)
    {
        this.basePrice = basePrice;
    }
    
    public Integer getRobotAmt()
    {
        return robotAmt;
    }
    
    public void setRobotAmt(Integer robotAmt)
    {
        this.robotAmt = robotAmt;
    }
}
