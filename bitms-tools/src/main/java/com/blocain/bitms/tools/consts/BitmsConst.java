/*
 * @(#)ZttxConst.java 2015-4-14 下午2:02:23
 * Copyright 2015 Playguy, Inc. All rights reserved. com.blocain
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.blocain.bitms.tools.consts;

import com.blocain.bitms.tools.utils.PropertiesUtils;

/**
 * <p>File：BitmsConst.java</p>
 * <p>Title: </p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2015 2015-4-14 下午2:02:23</p>
 * <p>Company: BloCain</p>
 *
 * @author Playguy
 * @version 1.0
 */
public class BitmsConst
{
    public static final PropertiesUtils properties = new PropertiesUtils("bitms.properties");
    
    private BitmsConst()
    {// 防止实例化
    }
    
    /**
     * 当前页面
     */
    public static final Integer  DEFAULT_CURRENT_PAGE          = 1;
    
    /**
     * 分页大小
     */
    public static final Integer  DEFAULT_PAGE_SIZE             = 10;
    
    /**
     * 分页起始大小
     */
    public static final Integer  DEFAULT_START_INDEX           = 0;
    
    /**
     * UUID length
     */
    public static final Integer  UUID_SIZE                     = 32;
    
    /**
     * 默认UNID
     */
    public static final Long     DEFAULT_UNID                  = 10000L;
    
    /**
     * 生产环境名称
     */
    public static final String   PROJECT_NAME                  = "BITMS";
    
    /**
     * 开发环境名称
     */
    public static final String   PROJECT_DEV_NAME              = "BITMS_DEV";
    
    /**
     * 测试环境名称
     */
    public static final String   PROJECT_TEST_NAME             = "BITMS_TEST";
    
    /**
     * TBIEX生产环境名称
     */
    public static final String   PROJECT_BIEX_NAME             = "BIEX";
    
    /**
     * BIEX开发环境名称
     */
    public static final String   PROJECT_BIEX_DEV_NAME         = "BIEX_DEV";
    
    /**
     * BIEX测试环境名称
     */
    public static final String   PROJECT_BIEX_TEST_NAME        = "BIEX_TEST";
    
    /**
     * 项目名称-boss
     */
    public static final String   PROJECT_BOSS_NAME             = "BITMS_BOSS";
    
    /**
     * 开发环境名称-boss
     */
    public static final String   PROJECT_BOSS_DEV_NAME         = "BITMS_BOSS_DEV";
    
    /**
     * 测试环境名称-boss
     */
    public static final String   PROJECT_BOSS_TEST_NAME        = "BITMS_BOSS_TEST";
    
    /**
     * 阿里云的访问·KEY
     */
    public static final String   ALIYUN_ACCESS_KEY             = "LTAI3htwbHQwp7g1";
    
    /**
     * 阿里云的访问·秘钥
     */
    public static final String   ALIYUN_ACCESS_SECRET          = "LmtN0ncqUOGf8F6aGjZ0JXL5ryl3jv";
    
    /**
     * OSS访问地址
     */
    public static final String   ALIYUN_OSS_ENDPOINT           = "https://oss-cn-hongkong.aliyuncs.com";
    
    /**
     * AWS访问密钥ID
     */
    public static final String   AWS_ACCESS_KEY_ID             = "AKIAJ7FJPZ6FEC5KGVZA";
    
    /**
     * AWS私有访问密钥
     */
    public static final String   AWS_SECRET_ACCESS_KEY         = "f5FS57F+0N1MlcK0ucfqN1bn+WQB9VopiXkAz5b2";
    
    /**
     * 邮件发送类型
     */
    public static final String   EMAIL_SENDER_PROVIDER         = properties.getProperty("bitms.email.provider");
    
    /**
     * 亚马逊邮件推送服务
     */
    public static final String   EMAIL_PROVIDER_AMAZON         = "amazon";
    
    /**
     * 正式空间
     */
    public static final String   BUCKET_BITMS_NAME             = properties.getProperty("bitms.bucket.release.name");
    
    public static final String   BUCKET_BITMS_URL              = properties.getProperty("bitms.bucket.release.url");
    
    /**
     * 临时空间
     */
    public static final String   BUCKET_TEMP_NAME              = properties.getProperty("bitms.bucket.temp.name");
    
    public static final String   BUCKET_TEMP_URL               = properties.getProperty("bitms.bucket.temp.url");
    
    /**
     * 日志记录开关
     */
    public static final String   ACCOUNT_LOGGER_STATUS         = properties.getProperty("account.logger.prop");
    
    /**
     * 默认语言
     */
    public static final String   DEFAULT_LANGUAGE              = "en_US";
    
    /**
     * 默认密码
     */
    public static final String   DEFAULT_USER_PASSWORD         = properties.getProperty("bitms.default.pwd.prop");
    
    /**
     * HOSTNAME
     */
    public static final String   HOST_URL                      = properties.getProperty("bitms.server.url.prop");
    
    /**
     * 邮件发送logo地址
     */
    public static final String   HOST_EMAIL_LOGO_URL           = new StringBuilder(properties.getProperty("bitms.server.url.prop")).append("/images/bitms/bitms-email.png")
            .toString();
    
    /**
     * 运行环境
     */
    public static final String   RUNNING_ENVIRONMONT           = properties.getProperty("bitms.running.env.prop");
    
    /**
     * 子项目名称
     */
    public static final String   BITMS_PROJECT_NAME            = properties.getProperty("bitms.project.name.prop");
    
    /**
     * 本地erc20RPC服务地址
     */
    // public static final String ETH_CLIENT_URL = properties.getProperty("erc20token.root.url");
    /**
     * XSS转义开关
     */
    public static final String   INTERCEPT_XSS_SWITCH          = properties.getProperty("bitms.xss.intercept.switch");
    
    /**
     * 短信提醒开关
     */
    public static final String   REMIND_PHONE_SWITCH           = properties.getProperty("bitms.remind.phone");
    
    /**
     * 邮件提醒开关
     */
    public static final String   REMIND_EMAIL_SWITCH           = properties.getProperty("bitms.remind.email");
    
    /**
     * 排除敏感的关键字
     */
    public static final String[] blackStrPathPattern           = new String[]{"*pass*", "*Pwd*", "*password*"};
    
    /**
     * 国际化语言
     */
    public static final String   COOKIE_LOCALE                 = "locale";
    
    /**
     * 启用
     */
    public static final String   SWITCH_ENABLE                 = "enable";
    
    /**
     * 停用
     */
    public static final String   SWITCH_DISABLE                = "disable";
    
    /**
     * 默认排序
     */
    public static final String   DEFAULT_SORT_ASC              = "asc";
    
    /**
     * 报价间隔时间
     */
    public static final Integer  QUOTATION_INTERVAL_TIME       = 30;
    
    /**
     * 操作频率限制
     * 默认30次
     */
    public static final Integer  LOCK_INTERVAL_COUNT           = 30;
    
    /**
     * COOKIE存放路径
     */
    public static final String   COOKIE_PATH                   = "/";
    
    /**
     * 请求方式
     */
    public static final String   POST                          = "post";
    
    /**
     * 操作标识
     */
    public static final String   OP                            = "op";
    
    /**
     * 分割符
     */
    public static final char     SEPARATOR                     = '|';
    
    /**
     * 登陆操作
     */
    public static final String   OP_LOGIN                      = "login";
    
    /**
     * 找回密码操作
     */
    public static final String   OP_FINDPWD                    = "findpwd";
    
    /**
     * 消息
     */
    public static final String   MESSAGE                       = "message";
    
    /**
     * 帐户模块
     */
    public static final String   OP_ACCOUNT_BIND_PHONE         = "account_bindPhone";
    
    /**
     * 邀请奖励模块
     */
    public static final String   OP_INVITATION                 = "invitation";
    
    /**
     * BMS兑换 预兑
     */
    public static final String   OP_ICO_SUBSCRIBE              = "icoSubscribe";
    
    /**
     * 强增强减模块
     */
    public static final String   OP_FUND_ADJUST                = "fundAdjust";
    
    /**
     * 撮合交易模块
     */
    public static final String   OP_ENTRUSTVCOOINMONEY         = "entrustVcoinMoney";
    
    /**
     * 钱包划拨
     */
    public static final String   BITPAY_WITHDRAW               = "bitpayWithdraw";
    
    /**
     * 资金流水模块
     */
    public static final String   OP_FUND_CURRENT               = "fundCurrent";
    
    /**
     * 帳戶資產
     */
    public static final String   OP_FUND_ASSET                 = "fundAsset";
    
    /**
     * PUSH交易
     */
    public static final String   OP_PUSH                       = "push";
    
    /**
     * 集市交易
     */
    public static final String   OP_FAIR                       = "fair";
    
    /**
     * 撮合交易
     */
    public static final String   OP_MATCH                      = "match";
    
    /**
     * 区块确认
     */
    public static final String   OP_MODIFY_WALLET_TRANS_STATUS = "modifyWalletTransStatus";
    
    /**
     * 提币申请模块-用于短信或GA次数判定
     */
    public static final String   OP_RAISE_DO_RAISE             = "raise_doRaise";
    
    /**
     * 提币地址添加模块-用于短信或GA次数判定
     */
    public static final String   OP_RAISE_ADD_ADDR             = "raise_addAddr";
    
    /**
     * 集市委托买入模块--用于短信或GA次数判定
     */
    public static final String   OP_FAIR_ENTRUST_BUY           = "fair_entrustBuy";
    
    /**
     * 集市委托卖出模块--用于短信或GA次数判定
     */
    public static final String   OP_FAIR_ENTRUST_SELL          = "fair_entrustSell";
    
    /**
     * 集市无委托买入模块--用于短信或GA次数判定
     */
    public static final String   OP_FAIR_NOENTRUST_BUY         = "fair_noEntrustBuy";
    
    /**
     * 集市无委托卖出模块--用于短信或GA次数判定
     */
    public static final String   OP_FAIR_NOENTRUST_SELL        = "fair_noEntrustSell";
    
    /**
     * PUSH委托卖出模块--用于短信或GA次数判定
     */
    public static final String   OP_PUSH_ENTRUST_SELL          = "push_entrustSell";
    
    /**
     * PUSH无委托买入模块--用于短信或GA次数判定
     */
    public static final String   OP_PUSH_NOENTRUST_BUY         = "push_noEntrustBuy";
    
    /**
     * 内部行情撮合成交价--用于计算行情涨跌幅
     */
    public static final String   OP_RTQUOTATIONINFO            = "RtQuotationInfo";
    
    /**
     * K线
     */
    public static final String   OP_KLINE                      = "KLINE";
    
    /**
     * K线表数据
     */
    public static final String   KLINE_DATA_TIME               = "KlineDataTime";
    
    /**
     * K线起始位置
     */
    public static final String   OP_KLINE_STARTLOCATION        = "StartLocation";
    
    /**
     * 当天第一笔数据，用于最低价，最高价，涨跌幅的计算
     */
    public static final String   OP_QUOTATION_CURRENTDAY_FIRST = "CurrentDay_FirstQuotation";
    
    /**
     * 24 小时成交量
     */
    public static final String   OP_QUOTATION_AMTSUM24H        = "amtsum24h";
    
    // 行情服务配置
    public static final String   OP_QUOTATION_CONFIG           = "config";
    
    // 已启动的行情服务器
    public static final String   OP_QUOTATION_SERVERS          = "servers";
    
    public static final String   STARTLOCATION                 = "start_location";
    
    // 公用模块(如：注册、取回密码)
    public static final String   COMMON                        = "/common";
    
    // 远程调用
    public static final String   CLIENT                        = "/client";
    
    // 回调地址
    public static final String   CALLBACK                      = "/callback";
    
    // 帐户
    public static final String   ACCOUNT                       = "/account";
    
    // 资金
    public static final String   FUND                          = "/fund";
    
    // 钱包
    public static final String   WALLET                        = "/wallet";
    
    // 指数交易
    public static final String   FUTURES                       = "/futures";
    
    // 发行代币trade
    public static final String   EXCHANGE                      = "/exchange";
    
    // 发行代币boss
    public static final String   ICO                           = "/ico";
    
    // 系统功能
    public static final String   SYSTEM                        = "/system";
    
    // 证券信息
    public static final String   STOCK                         = "/stock";
    
    // 现货交易
    public static final String   SOPT                          = "/spot";
    
    // 交割结算
    public static final String   SETTLEMENT                    = "/settlement";
    
    // 钱包管理
    public static final String   BITPAY                        = "/bitpay";
    
    // 监控管理
    public static final String   MONITOR                       = "/monitor";
    
    // 区块管理
    public static final String   BLOCK                         = "/block";
    
    // 委托
    public static final String   ENTRUST                       = "/entrust";
    
    // 成交
    public static final String   REALDEAL                      = "/realdeal";
    
    // API
    public static final String   API                           = "/api";
    
    // 行情推送信号
    public static final String   DOPUSH                        = ".doPush";
}
