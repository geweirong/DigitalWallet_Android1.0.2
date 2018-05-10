package com.innext.szqb.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.app.AppManager;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.dialog.ActiveDialog;
import com.innext.szqb.dialog.ActivityFragmentDialog;
import com.innext.szqb.dialog.AlertDownloadDialog;
import com.innext.szqb.events.ChangeTabMainEvent;
import com.innext.szqb.events.FragmentRefreshEvent;
import com.innext.szqb.events.RefreshUIEvent;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.lend.bean.ActivityBean;
import com.innext.szqb.ui.lend.bean.IndexActivityBean;
import com.innext.szqb.ui.lend.contract.ActivityContract;
import com.innext.szqb.ui.lend.presenter.ActivityPresenter;
import com.innext.szqb.ui.login.activity.LoginActivity;
import com.innext.szqb.ui.login.activity.RegisterPhoneActivity;
import com.innext.szqb.ui.my.bean.Item;
import com.innext.szqb.util.ServiceUtil;
import com.innext.szqb.util.check.NetUtil;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.CommonUtil;
import com.innext.szqb.util.common.LogUtils;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.view.ViewUtil;
import com.innext.szqb.widget.statusBar.ImmersionBar;
import com.meituan.android.walle.WalleChannelReader;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;


/**
 * 首页
 * hengxinyongli
 */
public class MainActivity extends BaseActivity<ActivityPresenter> implements ActivityContract.View{
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.group)
    RadioGroup mGroup;
    private FragmentFactory.FragmentStatus toTabIndex = FragmentFactory.FragmentStatus.None;
    private int oldCheckId = R.id.rb_lend;

    private ProgressInfo mLastDownloadingInfo;
    private Handler mHandler;
    private AlertDownloadDialog mDownloadDialog;
    private String mSavedPath;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
//        String mClientId = JPushInterface.getRegistrationID(mContext);
//        JPushInterface.getAlias(mContext,Integer.parseInt(Constant.USER_ID));
        ServiceUtil.setTagAndAlias(mContext);
        ImmersionBar.with(this).init();
        EventBus.getDefault().register(this);
        mGroup.setOnCheckedChangeListener(changeListener);
        check(FragmentFactory.FragmentStatus.Lend);
        mPresenter.loadActivity();

        String channel = WalleChannelReader.getChannel(this, App.getConfig().getChannelName());
        mPresenter.checkUpdate(channel);

        mPresenter.getIndexActivity();

        mHandler = new Handler();
    }

    public void check(FragmentFactory.FragmentStatus status) {
        mGroup.check(getCheckIdByStatus(status));
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        mGroup.setOnCheckedChangeListener(changeListener);
    }
    @Override
    protected void onPause(){
        super.onPause();
        JPushInterface.onPause(this);
    }
    OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_lend:
                    toTabIndex = FragmentFactory.FragmentStatus.Lend;
                    changeTab(FragmentFactory.FragmentStatus.Lend);
                    oldCheckId = R.id.rb_lend;
                    break;
                case R.id.rb_account:
                    toTabIndex = FragmentFactory.FragmentStatus.Account;
                    if (App.getConfig().getLoginStatus()) {
                        changeTab(FragmentFactory.FragmentStatus.Account);
                    } else {
                        toLogin();
                        return;
                    }
                    break;
                case R.id.rb_mall:
                    toTabIndex = FragmentFactory.FragmentStatus.MALL;
                    if (App.getConfig().getLoginStatus()) {
                        changeTab(FragmentFactory.FragmentStatus.MALL);
                    }else {
                        toLogin();
                        return;
                    }
                    break;
                default:
                    break;

            }
        }
    };

    private void toLogin() {
        mGroup.setOnCheckedChangeListener(null);
        ((RadioButton) findViewById(oldCheckId)).setChecked(true);
        String uName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        LogUtils.loge(uName);
        if (!StringUtil.isBlank(uName) && StringUtil.isMobileNO(uName)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("tag", StringUtil.changeMobile(uName));
            intent.putExtra("phone", uName);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, RegisterPhoneActivity.class);
            startActivity(intent);
        }
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_LOGIN)//登录
            {
                if (toTabIndex != odlState)//切换
                {
                    changeTab(toTabIndex);
                    ((RadioButton) findViewById(getCheckIdByStatus(toTabIndex))).setChecked(true);
                }
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOGOUT) {
                //默认到首页
                changeTab(FragmentFactory.FragmentStatus.Lend);
                ((RadioButton) findViewById(getCheckIdByStatus(FragmentFactory.FragmentStatus.Lend))).setChecked(true);
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            } else if (code == UIBaseEvent.EVENT_LOAN_SUCCESS) {
                EventBus.getDefault().post(new FragmentRefreshEvent(code));
            }
        } else if (event instanceof ChangeTabMainEvent) {
            changeTab(((ChangeTabMainEvent) event).getTab());
        }
    }


    /***********
     * 获取所选状态的checkId
     *
     * @return
     */
    public int getCheckIdByStatus(FragmentFactory.FragmentStatus status) {
        int id = R.id.rb_lend;
        switch (status) {
            case Account:
                id = R.id.rb_account;
                break;
            case Lend:
                id = R.id.rb_lend;
                break;
            default:
                break;
        }

        return id;
    }

    /***********
     * 切换导航栏
     */
    private FragmentFactory.FragmentStatus odlState = FragmentFactory.FragmentStatus.None;

    public void changeTab(FragmentFactory.FragmentStatus status) {
        if (status == odlState) return;
        FragmentFactory.changeFragment(getSupportFragmentManager(), status, R.id.container);
        odlState = status;
        if (status == FragmentFactory.FragmentStatus.Lend)
            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_TAB_LEND));
//        else if (status == FragmentFactory.FragmentStatus.RentLend)
//            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_TAB_REPAYMENT));
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    public RadioGroup getGroup() {
        return mGroup;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MobclickAgent.onKillProcess(this);
        EventBus.getDefault().unregister(this);
        AppManager.getInstance().appExit(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void loadActivitySuccess(ActivityBean result) {
        if ("0".equals(result.getType())){//0为显示，其他为不显示
            //缓存图片url与点击url。每个活动只弹出一次
            String imgUrl = SpUtil.getString(Constant.CACHE_TAG_ACTIVITY_IMGURL);
            String url = SpUtil.getString(Constant.CACHE_TAG_ACTIVITY_URL);
            if (!result.getTcImage().equals(imgUrl)||!result.getTcUrl().contains(url)){
                SpUtil.putString(Constant.CACHE_TAG_ACTIVITY_IMGURL,result.getTcImage());
                SpUtil.putString(Constant.CACHE_TAG_ACTIVITY_URL,result.getTcUrl());
                ActivityFragmentDialog.newInstance(result.getTcImage(),result.getTcUrl())
                        .show(getSupportFragmentManager(),ActivityFragmentDialog.TAG);
            }

        }
    }

    private int mType = DEFAULT_DOWNLOAD;
    private static final int DEFAULT_DOWNLOAD = 10;
    private static final int INSTALL_APK = 11;
    @Override
    public void loadCheckUpdateSuccess(final Item result) {
        if (mDownloadDialog == null) {
            mDownloadDialog = new AlertDownloadDialog.Builder(this)
                    .setTitle("发现新版本")
                    .setContent(result.getDescription())
                    .setRightBtnText("立即更新")
                    .setRightCallBack(new AlertDownloadDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick(AlertDownloadDialog dialog) {
                            if (mType == INSTALL_APK) installApk();
                            else if (NetUtil.isConnected(MainActivity.this)) {
                                downloadApk(result);
                            } else ToastUtil.showToast("请检查网络");
                        }
                    }).build();
        }

        // Okhttp/Retofit download
        ProgressManager.getInstance().addResponseListener(result.getPackageUrl(), getDownloadListener());
    }

    @Override
    public void onGetIndexActivitySuccess(final IndexActivityBean result) {
        if (result.getStatus() == 0) return;
        String activeUrl = SpUtil.getString(Constant.CACHE_INDEX_ACTIVE_URL);
        if (activeUrl.equals(result.getUrl())) return;

        SpUtil.putString(Constant.CACHE_INDEX_ACTIVE_URL, result.getUrl());
        new ActiveDialog(this, result.getUrl(), result.getReurl());
    }

    private void downloadApk(Item result) {
        String filePath = getExternalCacheDir() + "/download/";
        File file1 = new File(filePath);
        if (!file1.exists()) file1.mkdirs();
        mSavedPath = new File(filePath, "hengkd.apk").getAbsolutePath();
        mPresenter.downloadApk(result.getPackageUrl(), mSavedPath);

        mDownloadDialog.showOrHideButton(false);
        mDownloadDialog.showOrHideContentText(false);
        mDownloadDialog.showOrHideTvProgress(true);
        mDownloadDialog.showOrHideProgressbar(true);
    }

    @NonNull
    private ProgressListener getDownloadListener() {
        return new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                // 如果你不屏蔽用户重复点击上传或下载按钮,就可能存在同一个 Url 地址,上一次的上传或下载操作都还没结束,
                // 又开始了新的上传或下载操作,那现在就需要用到 id(请求开始时的时间) 来区分正在执行的进度信息
                // 这里我就取最新的下载进度用来展示,顺便展示下 id 的用法

                if (mLastDownloadingInfo == null) {
                    mLastDownloadingInfo = progressInfo;
                }

                //因为是以请求开始时的时间作为 Id ,所以值越大,说明该请求越新
                if (progressInfo.getId() < mLastDownloadingInfo.getId()) {
                    return;
                } else if (progressInfo.getId() > mLastDownloadingInfo.getId()) {
                    mLastDownloadingInfo = progressInfo;
                }

                int progress = mLastDownloadingInfo.getPercent();
                mDownloadDialog.setProgress(progress);
                Logger.d("--Download-- " + progress + " %  " + mLastDownloadingInfo.getSpeed() + " byte/s  " + mLastDownloadingInfo.toString());
                if (mLastDownloadingInfo.isFinish()) {
                    //说明已经下载完成
                    mType = INSTALL_APK;
                    mDownloadDialog.setRightText("立即安装");
                    installApk();
                }
            }

            @Override
            public void onError(long id, final Exception e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mType = DEFAULT_DOWNLOAD;
                        mDownloadDialog.setErrorProgress();
                    }
                });
            }
        };
    }

    private void installApk() {
        CommonUtil.INSTANCE.installApk(MainActivity.this, mSavedPath);
    }

}
