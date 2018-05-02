package com.blocain.bitms.quotation.consts;

/**
 * QuotationConsts Introduce
 * <p>File：QuotationConsts.java</p>
 * <p>Title: QuotationConsts</p>
 * <p>Description: QuotationConsts</p>
 * <p>Copyright: Copyright (c) 2017/7/5</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class QuotationConsts
{
    private QuotationConsts()
    {// 防止实例化
    }
    
    public static final String CURRECNCY_USD2BTC                 = "USD/BTC";
    
    public static final String CURRECNCY_LTCX2BTC                = "LTCX/BTC";
    
    public static final String CURRECNCY_ETHX2BTC                = "ETHX/BTC";
    
    public static final String CURRECNCY_USD2LTC                 = "USD/LTC";
    
    public static final String CURRECNCY_USD2ETH                 = "USD/ETH";
    
    public static final String CURRECNCY_XXBTZUSD                = "XXBTZUSD";
    
    public static final String CURRECNCY_XETHZUSD                = "XETHZUSD";
    
    public static final String CURRECNCY_XLTCZUSD                = "XLTCZUSD";
    
    // bitfienex channel
    public static final String RATES_CHANNEL_BITFINEX            = "bitfienex";
    
    // bitstamp channel
    public static final String RATES_CHANNEL_BITSTAMP            = "bitstamp";
    
    // coinbase channel
    public static final String RATES_CHANNEL_COINBASE            = "coinbase";
    
    // kraken channel
    public static final String RATES_CHANNEL_KRAKEN              = "kraken";
    
    // internalConversion channel
    public static final String RATES_CHANNEL_INTERNAL_CONVERSION = "internalConversion";
}
