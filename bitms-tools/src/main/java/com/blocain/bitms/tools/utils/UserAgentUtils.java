package com.blocain.bitms.tools.utils;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;

import java.io.IOException;

/**
 * 解析requst中用户代理设备信息等
 * Created by admin on 2018/1/16.
 */
public class UserAgentUtils
{
    static UASparser uasParser = null;
    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
            // java.lang.UnsupportedClassVersionError:
            // cz/mallat/uasparser/UASparser : Unsupported major.minor version 51.0
            // 用jdk1.6测试时会报以上错，需要jdk1.7以上版本支持
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *   根据user_agent 字符串解析对象
     System.out.println("操作系统家族：" + userAgentInfo.getOsFamily());
     System.out.println("操作系统详细名称：" + userAgentInfo.getOsName());
     System.out.println("浏览器名称和版本:" + userAgentInfo.getUaName());
     System.out.println("类型：" + userAgentInfo.getType());
     System.out.println("浏览器名称：" + userAgentInfo.getUaFamily());
     System.out.println("浏览器版本：" + userAgentInfo.getBrowserVersionInfo());
     System.out.println("设备类型：" + userAgentInfo.getDeviceType());
     * @param userAgentString
     * @return
     */
    public static String getUserAgentInfo(String userAgentString)
    {
        try {
            UserAgentInfo userAgentInfo = UserAgentUtils.uasParser.parse(userAgentString);
            return new StringBuilder(userAgentInfo.getOsName()).append("    ").append(userAgentInfo.getUaFamily()).toString();
        } catch (IOException e) {
            return userAgentString;
        }
    }

    // TEST
    public static void  main(String args[])
    {
        String str = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36";
        str = getUserAgentInfo(str);
        System.out.println(str);
    }
}
