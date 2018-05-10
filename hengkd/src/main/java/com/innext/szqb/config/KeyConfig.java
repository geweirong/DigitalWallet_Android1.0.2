package com.innext.szqb.config;

/**
 * 第三方Key存放
 */

public class KeyConfig {
    /**
     * 友盟key
     */
    public static String UM_KEY = "5aa788abb27b0a385b000193";
    /**
     * bugly App Id(线上)
     */
    public static String BUGLY_APP_ID = "e675326bc8";
    /**
     * bugly App Id(测试环境账号)
     */
    public static String BUGLY_APP_ID_DEBUG = "d34d99dea7";
    /**
     * bugly App Id
     * (测试环境账号)
     * (线上)已更换 3.14
     */
    public static final String BUGLY_APPID = ConfigUtil.BUGLY_TEST ? "02807b9252" : "02807b9252";
    //信鸽推送 accessId与accessKey
    public static long XG_ACCESS_ID = 2100282329;
    public static String XG_ACCESS_KEY = "A2Z636JZJ6NE";
//    public static long XG_ACCESS_ID = 2100266345;
//    public static String XG_ACCESS_KEY = "AP44CK3F41RL";
    //分享相关的key
    public static String WX_APP_KEY = "wx166bcdd4a00e63d9";//微信KEY
    public static String WX_APP_SECRET = "bd8bddf77b5ee959cfa39e857c3622da";//微信SECRET
    public static String QQ_APP_ID = "1106721105";//QQ appid  1106381086
    public static String QQ_APP_KEY = "kgBAmzzDxutQCXX1";//QQ appkey  ET6xPe94Z6xF4lc5
}
