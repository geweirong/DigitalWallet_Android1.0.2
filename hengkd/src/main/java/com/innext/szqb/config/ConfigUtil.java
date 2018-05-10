package com.innext.szqb.config;

import android.text.TextUtils;

import com.innext.szqb.BuildConfig;
import com.innext.szqb.ui.my.bean.UserInfoBean;
import com.innext.szqb.util.ConvertUtil;
import com.innext.szqb.util.common.SpUtil;

/**
 * 基础接口地址配置
 * ConfigUrlForNew.java为新接口地址配置
 */
public class ConfigUtil {
    private boolean isDebug = BuildConfig.LOG_DEBUG;//是否调试模式,上线必须改为false/
//    private boolean isDebug = false;//是否调试模式,上线必须改为false

    /*是否显示bugly测试环境日志*/
    public static final boolean BUGLY_TEST = BuildConfig.LOG_DEBUG;
//    public static final boolean BUGLY_TEST = true;

    public String baseUrl = BuildConfig.API_DOMAIN;//测试服务器
    public String baseUrl1 = BuildConfig.API_CONTRACTR;//合同地址
//    public String baseUrl = "http://172.30.2.170/hkd_api/";//本地服务器
//    public String baseUrl = "http://172.30.2.122:8080/hkd_api/";//本地服务器
//    public String baseUrl = "http://172.30.2.123:8080/hkd_api/";//yelin
//    public String baseUrl = "http://172.30.2.195:8080/hkd_api/";//本地服务器
//    public String baseUrl = "http://172.30.2.165:8080/hkd_api/";//yangni
    public String[] urls = {"http://10.32.1.3:28080/hkd_api/",//测试
            "http://a.hengkuaidai.com/hkd_api/"//线上域名
    };
    //爬取支付宝数据js
    public String GET_ALIPAY_JS = baseUrl + "resources/js/alipay.js";

    //我的邀请码H5
    public String INVITATION_CODE = baseUrl + "page/detail";

    //活动中心H5
    public String ACTIVITY_CENTER = baseUrl + "content/activity";

    //帮助中心H5
    public String HELP = baseUrl + "help";

    //注册协议
    public String REGISTER_AGREEMENT = baseUrl + "act/light-loan-xjx/agreement";

    //信用授权协议
    public String CREDIT_AUTHORIZATION_AGREEMENT = baseUrl + "agreement/creditExtension";

    //关于我们
    public String ABOUT_US = baseUrl + "page/detailAbout";

    //立即还款H5
    public String REPAYMENT = baseUrl + "repayment/repay-choose";
    private boolean isLogin = false;//用户的登陆状态

    private String channelName = "MySelf";//默认渠道号

    /*
    * 获取延期还款信息
    */
    public String REPAYMENT_GETINFO = baseUrl + "repayment/postpone/getInfo";

    public ConfigUtil() {
        setUserInfo(getUserInfo());
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }


    public String getChannelName() {
        return channelName;
    }


    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public UserInfoBean getUserInfo() {
        return ConvertUtil.toObject(SpUtil.getString(Constant.CACHE_USER_INFO), UserInfoBean.class);
    }


    public void setUserInfo(UserInfoBean userInfo) {
        isLogin = userInfo != null;
        SpUtil.putString(Constant.CACHE_USER_INFO, ConvertUtil.toJsonString(userInfo));
    }
    //央行认证
    public final String CBCREDIT = baseUrl + "yhzx/get-page";
    //获取用户当前登录状态
    public boolean getLoginStatus() {
        return isLogin;
    }

    public String getBaseUrl() {
        if (isDebug() && !TextUtils.isEmpty(SpUtil.getString(Constant.URL_KEY))) {
            baseUrl = SpUtil.getString(Constant.URL_KEY);
        }
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        GET_ALIPAY_JS = baseUrl + "resources/js/alipay.js";

        //我的邀请码H5
        INVITATION_CODE = baseUrl + "page/detail";

        //活动中心H5
        ACTIVITY_CENTER = baseUrl + "content/activity";

        //帮助中心H5
        HELP = baseUrl + "help";

        //注册协议
        REGISTER_AGREEMENT = baseUrl + "act/light-loan-xjx/agreement";

        //关于我们
        ABOUT_US = baseUrl + "page/detailAbout";

        CREDIT_AUTHORIZATION_AGREEMENT = baseUrl + "agreement/creditExtension";

    }
}
