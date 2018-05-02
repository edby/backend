package com.blocain.bitms.quotation.model;

import java.io.Serializable;

/**
 * Coinbase行情指数对象
 * <p>File：Coinbase.java</p>
 * <p>Title: Coinbase</p>
 * <p>Description:
 *  https://api.coinbase.com/v2/exchange-rates?currency=BTC
 * </p>
 * <p>Copyright: Copyright (c) 2017/6/28</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class CoinbaseIndex implements Serializable
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9093804044957952626L;
    private DataBean data;
    
    public DataBean getData()
    {
        return data;
    }
    
    public void setData(DataBean data)
    {
        this.data = data;
    }
    
    public static class DataBean
    {
        private String    currency;
        
        private RatesBean rates;
        
        public String getCurrency()
        {
            return currency;
        }
        
        public void setCurrency(String currency)
        {
            this.currency = currency;
        }
        
        public RatesBean getRates()
        {
            return rates;
        }
        
        public void setRates(RatesBean rates)
        {
            this.rates = rates;
        }
        
        public static class RatesBean
        {
            private String CNY;
            
            private String USD;
            
            public String getCNY()
            {
                return CNY;
            }
            
            public void setCNY(String CNY)
            {
                this.CNY = CNY;
            }
            
            public String getUSD()
            {
                return USD;
            }
            
            public void setUSD(String USD)
            {
                this.USD = USD;
            }
        }
    }
}
