package com.blocain.bitms.trade.robot.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable
{

    private static final long serialVersionUID = -7798251215981608866L;
    
    private Long              id;
    
    private String            orderType;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private BigDecimal price;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getOrderType()
    {
        return orderType;
    }
    
    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }
    


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderType='" + orderType + '\'' +
                ", price=" + price +
                '}';
    }
}
