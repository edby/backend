package com.blocain.bitms.trade.robot.entity;

import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.robot.config.ExchangePairConfig;

import java.io.Serializable;

/**
 * 机器人模型，可执行下单，撤单等操作
 */
public class RobotModel implements Serializable
{
    
    // 交易参数
    private GridRobotConfig     param;
    //资产参数
    private FundChangeModel curAssetInfo;
    private FundChangeModel beginAssetInfo;
    // 币对参数
    private ExchangePairConfig  pair;
    
    // 买单开关
    private Boolean             buySwitch;
    
    // 卖单开关
    private Boolean             sellSwitch;

    public FundChangeModel getCurAssetInfo() {
        return curAssetInfo;
    }

    public void setCurAssetInfo(FundChangeModel curAssetInfo) {
        this.curAssetInfo = curAssetInfo;
    }

    public FundChangeModel getBeginAssetInfo() {
        return beginAssetInfo;
    }

    public void setBeginAssetInfo(FundChangeModel beginAssetInfo) {
        this.beginAssetInfo = beginAssetInfo;
    }

    public ExchangePairConfig getPair()
    {
        return pair;
    }
    
    public void setPair(ExchangePairConfig pair)
    {
        this.pair = pair;
    }
    
    public GridRobotConfig getParam()
    {
        return param;
    }
    
    public void setParam(GridRobotConfig param)
    {
        this.param = param;
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
    
    // 模型初始化
    public void init(GridRobotConfig param)
    {
        this.param = param;
        pair = new ExchangePairConfig("robot/btc2usd");
        buySwitch = false;
        sellSwitch = false;
    }
    
    public static void main(String[] args)
    {
    }
}
