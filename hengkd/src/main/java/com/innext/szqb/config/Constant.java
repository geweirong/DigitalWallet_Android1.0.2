package com.innext.szqb.config;

public class Constant {

    //提额H5
    public static String LIMIT_URL = "http://mobile.souyijie.com/recommend?channelId=bHV2/eJaRv0=";

    public static final String DEBUG_TAG = "logger";// LogCat的标记

    //sharedpren里面的常量
    public static final String CACHE_TAG_USERNAME = "username"; //用户名
    public static final String CACHE_TAG_SESSIONID = "sessionid";//sessionId
    public static final String CACHE_TAG_UID = "uid";//uid
    public static final String CACHE_MEMBER_STATE = "memberState";//会员状态
    public static final String CACHE_EXPIRE_TIME = "expireTime";//会员到期日期

    public static final String CACHE_USERLIST_KEY = "userList";

    public static final String CACHE_USER_INFO = "userinfo";//用户信息

    public static final String CACHE_TAG_COUNT_DOWN = "showCountDown";    //显示倒计时页面
    public static final String CACHE_TAG_REPAY_COUNT_DOWN = "showRepaymentCountDown";    //显示还款倒计时页面
    public static final String CACHE_TAG_NEXT_LOAN = "showNextLoanCountDown";    //显示下次申请借款倒计时页面
    public static final String CACHE_TAG_NEXT_LOAN_REPULSE = "showNextLoanCountDownRepulse";    //审核拒绝显示下次申请借款倒计时页面

    public static final String CACHE_CALENDAR_PERMISSIONS = "calendarPermissions";//是否允许往日历中插入数据

    public static final String CACHE_CALENDAR_LOAN_DATE = "loanStartTime";    //日历中插入的下次可借款时间
    public static final String CACHE_CALENDAR_REPAY_DATE = "loanEndTime";    //日历中插入的还款时间
    public static final String CACHE_CALENDAR_REPAY_MONEY = "loanEndMoney";    //日历中插入的还款金额

    public static final String CACHE_TAG_REAL_NAME = "realName"; //实名姓名
    public static final String CACHE_INDEX_ACTIVE_URL = "indexActiveUrl"; //首页活动

    public static final String URL_KEY = "baseUrlKey";
    //判断是不是第一次进app 是的话暂时引导页
    public static final String CACHE_IS_FIRST_LOGIN = "FirstLogin";//key
    public static final int HAS_ALREADY_LOGIN = 1;//首次
    public static final int NOT_FIRST_LOGIN = -1;

    //支付结果
    public static final String PAY_RESULT_LEND_FAILED = "PAY_RESULT_LEND_FAILED";


    //智齿机器人对应的key
    public static String SOBOT_KEY = "0588ea10f6d34312a468606ac430d12f";
    /************
     * 提升额度配置
     */
    public static final int TAG_QUOTA_PERSONAL = 1;//个人
    public static final int TAG_QUOTA_WORK = 2;//工作
    public static final int TAG_QUOTA_CONTACT = 3;//联系人
    public static final int TAG_QUOTA_BANK = 4;//银行卡信息
    public static final int TAG_QUOTA_PHONE = 5;//手机运营商
    public static final int TAG_QUOTA_MORE = 7;//更多
    public static final int TAG_QUOTA_ZMXY = 8;//芝麻信用
    public static final int TAG_QUOTA_ALIPAY = 9;//支付宝认证

    //位置信息上传间隔时间
    public static final int INTERVAL_TIME = 30 * 60 * 1000;
    public static final int RETRIVE_SERVICE_COUNT = 50;
    public static final int BROADCAST_ELAPSED_TIME_DELAY = 2 * 60 * 1000;

    public static final String POI_SERVICE = "com.coder80.timer.service.UploadPOIService";
    //Activity转场动画Key
    public static final String TRANSITION_ANIMATION_SHOW_PIC = "showView";

    public static final String CACHE_TAG_ACTIVITY_IMGURL = "activityImgUrl";
    public static final String CACHE_TAG_ACTIVITY_URL = "activityUrl";

    public static final String TAG_OPERATE_BEAN = "BEAN";
    //首页(loanDay)(loanMoney)
    public static final String LOAN_DAY = "loan_day";
    public static final String LOAN_MONEY = "loan_money";
    //风控审核是否通过 1显示
    public static final String RISK_STATUS = "1";
    public static final String USER_ID = "user_id";
    public static final String POOL_ID = "poolId";

    /** 客服qq */
    public static final String[] QQ_LIST = {"2682270488"};
    /** 客服热线 */
    public static final String SERVICE_TEL = "400-185-5918";

    /**
     * 完善资料列表
     */
    public static final String CODE_PERSON_INFO = "personalInfo";
    public static final String CODE_CONTACTS = "emergencyInfo";
    public static final String CODE_MOBILE_OPERATOR = "phoneInfo";
    public static final String CODE_ZM_CREDIT = "riskCarditInfo";
    public static final String CODE_CB = "cbInfo";
    public static final String CODE_ALI_PAY = "payTreasure";
    public static final String CODE_JD = "jdInfo";
    public static final String CODE_GJJ = "gjjInfo";
    public static final String CODE_XYK = "creditCardInfo";

    public static final String FILE_PROVIDER_AUTHORITY = "com.innext.szqb.fileProvider";
}
