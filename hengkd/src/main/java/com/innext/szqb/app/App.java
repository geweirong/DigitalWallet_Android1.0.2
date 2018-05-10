package com.innext.szqb.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.anupcowkur.reservoir.Reservoir;
import com.facebook.stetho.Stetho;
import com.innext.szqb.R;
import com.innext.szqb.config.ConfigUtil;
import com.innext.szqb.config.Constant;
import com.innext.szqb.config.KeyConfig;
import com.innext.szqb.events.BaseEvent;
import com.innext.szqb.events.EventController;
import com.innext.szqb.ui.login.activity.LoginActivity;
import com.innext.szqb.ui.login.activity.RegisterPhoneActivity;
import com.innext.szqb.ui.main.MainActivity;
import com.innext.szqb.util.common.LogUtils;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.view.ViewUtil;
import com.meituan.android.walle.WalleChannelReader;
import com.moxie.client.manager.MoxieSDK;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;
import cn.jpush.android.api.JPushInterface;
import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

public class App extends Application {

    public static App mApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //突破Dex文件方法数不能超过最大值65536的上限
        MultiDex.install(getApplication());
        //热修复初始化
        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //stetho
        Stetho.initializeWithDefaults(this);
        //极光初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //添加魔蝎初始化
        MoxieSDK.init(this);
        MultiDex.install(this);//解决友盟分析问题（使用kotlin）
        //初始化同盾信用卡
        OctopusManager.getInstance().init(this,"szqb_mohe","6621f3eecce94e0c967ff1afa9adad8f");
        mApp = this;
        //log初始化,根据configUtil的isDebug参数控制是否显示log
        LogUtils.logInit(getConfig().isDebug());
        //toast初始化
        ToastUtil.register(getContext());
        //注册eventbus
        EventBus.getDefault().register(this);
        //缓存数据2.28
        try {
            Reservoir.init(this, 5120);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //友盟相关功能初始化(统计与分享)
        initUM();
        //bugly相关功能初始化(版本更新、bug检测)
        initBugly();
        initLoadingView(getContext());
        /**打印日志*/
        Logger.init("wzz");
    }

    public void initBugly() {
        // 获取当前包名
        String packageName = getContext().getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getContext());
        //解决多进程多次上报问题
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //设置渠道信息
        strategy.setAppChannel(getConfig().getChannelName());

        //设置版本更新相关
        //设置更新dialog只在MainActivity.class自动弹出
        Beta.canShowUpgradeActs.add(MainActivity.class);
        //设置弹出延时默认3000毫秒
        Beta.initDelay = 0;
        //设置更新dialog样式
        Beta.upgradeDialogLayoutId = R.layout.dialog_update;
        // 初始化Bugly
        Bugly.init(getContext(), KeyConfig.BUGLY_APPID, ConfigUtil.BUGLY_TEST, strategy);

    }

    public void initUM() {
        /*获取market*/
        String channel = WalleChannelReader.getChannel(getContext(), getConfig().getChannelName());
        LogUtils.loge("当前渠道:" + channel);
        /*注册UMENG*/
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getContext(), KeyConfig.UM_KEY, channel));
        getConfig().setChannelName(channel);

        //关闭默认统计
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(getConfig().isDebug());
        //微信
        PlatformConfig.setWeixin(KeyConfig.WX_APP_KEY, KeyConfig.WX_APP_SECRET);
        //QQ
        PlatformConfig.setQQZone(KeyConfig.QQ_APP_ID, KeyConfig.QQ_APP_KEY);

    }

    public static void initLoadingView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loan_loading, null);
        ViewUtil.setLoadingView(context, view);
    }

    public static void loadingDefault(Activity activity) {
        ViewUtil.setLoadingView(activity, null);
        ViewUtil.showLoading(activity, "");
    }

    public static void loadingContent(Activity activity, String content) {
        ViewUtil.setLoadingView(activity, null);
        ViewUtil.showLoading(activity, content);
    }

    public static void hideLoading() {
        ViewUtil.hideLoading();
    }


    public static String getAPPName() {
        return getContext().getResources().getString(R.string.app_name);
    }

    /*******
     * 将事件交给事件派发controller处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseEvent event) {
        event.setApplicationContext(getContext());
        EventController.getInstance().handleMessage(event);
    }


    //保存一些常用的配置
    private static ConfigUtil configUtil = null;

    public static ConfigUtil getConfig() {
        if (configUtil == null) {
            configUtil = new ConfigUtil();
        }
        return configUtil;
    }

    public static void toLogin(Context context) {
        String uName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        if (!TextUtils.isEmpty(uName) && StringUtil.isMobileNO(uName)) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("tag", StringUtil.changeMobile(uName));
            intent.putExtra("phone", uName);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, RegisterPhoneActivity.class);
            context.startActivity(intent);
        }
        return;
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
