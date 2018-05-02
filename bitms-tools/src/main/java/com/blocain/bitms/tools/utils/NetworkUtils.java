/*
 * @(#)NetworkUtils.java 2014-2-25 上午11:38:01
 * Copyright 2014 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <p>File：NetworkUtils.java</p>
 * <p>Title: 网络处理工具类</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2014 2014-2-25 上午11:38:01</p>
 * <p>Company: BloCain</p>
 * @author Playguy
 * @version 1.0
 */
@SuppressWarnings("restriction")
public class NetworkUtils
{
    private static final Logger logger = Logger.getLogger(NetworkUtils.class);
    
    // 私有构造器，防止类的实例化
    private NetworkUtils()
    {
        super();
    }
    
    /**
     * 取得当前host地址<解决font face跨域问题，nginx下尝试失败>
     * @param request HttpServletRequest
     * @return String 当前host地址
     */
    public static String getHost(HttpServletRequest request)
    {
        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return path;
    }
    
    /**
     * 将IP地址转换为整数类型
     * @param addr 字符串类型的IP地址
     * @return 整数
     */
    public static int ipToInt(final String addr)
    {
        int ip = 0;
        if (StringUtils.isNotBlank(addr))
        {
            final String[] addressBytes = addr.split("\\.");
            ip = 0;
            for (int i = 0; i < 4; i++)
            {
                ip <<= 8;
                ip |= Integer.parseInt(addressBytes[i]);
            }
        }
        return ip;
    }
    
    /**
     * 将整数类型的IP地址转换为字符串类型的IP地址
     * @param i 整数
     * @return IP地址
     */
    public static String intToIp(int i)
    {
        if (i == 0) return "";
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }
    
    /**
     * 根据网卡取本机配置的IP
     * 如果是双网卡的，则取出外网IP
     * @return
     */
    public static String getNetIp()
    {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        try
        {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded)
            {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements())
                {
                    ip = address.nextElement();
                    /*
                     * System.out.println(ni.getName() + ";" + ip.getHostAddress() + ";ip.isSiteLocalAddress()=" + ip.isSiteLocalAddress() + ";ip.isLoopbackAddress()=" +
                     * ip.isLoopbackAddress());
                     */
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                    {// 外网IP
                        netip = ip.getHostAddress();
                        finded = true;
                        break;
                    }
                    else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                    {// 内网IP
                        localip = ip.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException e)
        {
            logger.error(e);
        }
        if (netip != null && !"".equals(netip))
        {
            return netip;
        }
        else
        {
            return localip;
        }
    }
    
    /**
    * 根据网卡取本机配置的内网IP
    * 如果是双网卡的，则取出内网IP
    * @return String 内网IP地址
    */
    public static String getLocalIp()
    {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        try
        {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded)
            {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements())
                {
                    ip = address.nextElement();
                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                    {
                        localip = ip.getHostAddress();
                        finded = true;
                        break;
                    }
                }
            }
        }
        catch (SocketException e)
        {
            logger.error(e);
        }
        return localip;
    }
    
    /**
     * 获取客户端（访问者）IP地址
     * @param request
     * @return
     * @author 冯仁
     */
    public static String getRemortIp(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown"))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown"))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || ip.equalsIgnoreCase("unknown"))
        {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 获取客户端（访问者）IP地址
     * @param request
     * @return
     * @author 冯仁
     */
    public static int getRemortIpToInt(HttpServletRequest request)
    {
        String ip = getRemortIp(request);
        return ipToInt(ip);
    }
    
    /**
     * 获取服务器域名
     * @return
     * @author 鲍建明
     */
    public static String getDoMainName()
    {
        return "";
    }
    
    /**
     * 取得客户端IP地址并转化为整数
     * @param request HttpServletRequest
     * @return int 整数格式的客户端IP地址
     */
    public static int getIpAddr(HttpServletRequest request)
    {
        int ipInt = 0;
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(ip))
        {
            if (IPAddressUtil.isIPv4LiteralAddress(ip))
            {
                ipInt = NetworkUtils.ipToInt(ip);
            }
            else
            {
                String[] string = ip.split(",");
                int iLen = string.length;
                if (iLen > 0) ip = StringUtils.trimToEmpty(string[iLen - 1]);
                if (IPAddressUtil.isIPv4LiteralAddress(ip))
                {
                    ipInt = NetworkUtils.ipToInt(ip);
                }
            }
        }
        return ipInt;
    }
}
