package com.blocain.bitms.trade.robot.service;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

import com.blocain.bitms.trade.fund.model.FundChangeModel;
import com.blocain.bitms.trade.robot.entity.Order;
import com.blocain.bitms.trade.robot.entity.RobotModel;

public interface GridRobotService
{
    void cancelAllOrders(RobotModel robot);
    
    void changeBorrowSwitch(Long accountId, Integer autoDebit);
    
    BigDecimal calBasePrice(RobotModel robot);
    
    RobotModel updateTradeSwitch(RobotModel robot);

    FundChangeModel getAccountFundAsset(Long accountId, Long exchangePairMoney);
    
    List<Order> findOrders(RobotModel robot, String spotbuy);
    
    void cancelOrder(RobotModel robot, Order order);
    
    BigDecimal calOrderPrice(RobotModel robot, BigDecimal basePrice, int i, String spotbuy);
    
    BigDecimal calOrderAmt(RobotModel robot);
    
    void doBuy(RobotModel robot, BigDecimal price, BigDecimal amt);
    
    void doSell(RobotModel robot, BigDecimal price, BigDecimal amt);
    
    BigDecimal replacePrice(RobotModel robot, BigDecimal oldPrice);
}
