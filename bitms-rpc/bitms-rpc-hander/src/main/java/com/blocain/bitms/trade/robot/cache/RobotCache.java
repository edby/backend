package com.blocain.bitms.trade.robot.cache;

import com.blocain.bitms.trade.robot.thread.AutoTradeThread;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class RobotCache {
    //所有机器人线程缓存
    public static ConcurrentHashMap<Long, AutoTradeThread> THREAD_MAP = new ConcurrentHashMap();

}
