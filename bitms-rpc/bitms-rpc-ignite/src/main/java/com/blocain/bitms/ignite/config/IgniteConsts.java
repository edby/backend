package com.blocain.bitms.ignite.config;

import com.blocain.bitms.tools.utils.PropertiesUtils;

/**
 * 缓存常量
 * <p>File：IgniteConsts.java </p>
 * <p>Title: IgniteConsts </p>
 * <p>Description:IgniteConsts </p>
 * <p>Copyright: Copyright (c) 2018/4/2</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class IgniteConsts
{
    public static final PropertiesUtils property                   = new PropertiesUtils("cache.properties");
    
    public static String                cache_btc2eur_entrust      = property.getProperty("cache.btc2eur.entrust");
    
    public static String                cache_btc2eur_realdeal     = property.getProperty("cache.btc2eur.realdeal");
    
    public static String                cache_btc2eur_realdealhis  = property.getProperty("cache.btc2eur.realdealhis");
    
    public static String                cache_biex2btc_entrust     = property.getProperty("cache.biex2btc.entrust");
    
    public static String                cache_biex2btc_realdeal    = property.getProperty("cache.biex2btc.realdeal");
    
    public static String                cache_biex2btc_realdealhis = property.getProperty("cache.biex2btc.realdealhis");
    
    public static String                cache_btc2usd_entrust      = property.getProperty("cache.btc2usd.entrust");
    
    public static String                cache_btc2usd_realdeal     = property.getProperty("cache.btc2usd.realdeal");
    
    public static String                cache_btc2usd_realdealhis  = property.getProperty("cache.btc2usd.realdealhis");
}
