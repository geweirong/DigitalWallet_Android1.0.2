package com.innext.szqb.ui.main;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.base.PermissionsListener;
import com.innext.szqb.config.Constant;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.events.LoginNoRefreshUIEvent;
import com.innext.szqb.ui.login.contract.LoginOutContract;
import com.innext.szqb.ui.login.presenter.LoginOutPresenter;
import com.innext.szqb.ui.my.contract.DeviceReportContract;
import com.innext.szqb.ui.my.presenter.DeviceReportPresenter;
import com.innext.szqb.util.ServiceUtil;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.view.ViewUtil;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 启动页
 * hengxinyongli
 */
public class SplashActivity extends BaseActivity implements LoginOutContract.View
,DeviceReportContract.View{
    private boolean isRequesting;//为了避免在onResume中多次请求权限
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_CONTACTS};
    private LoginOutPresenter mLoginOutPresenter;
    private DeviceReportPresenter mDeviceReportPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
        mDeviceReportPresenter =new DeviceReportPresenter();
        mDeviceReportPresenter.init(this);
        mLoginOutPresenter =new LoginOutPresenter();
        mLoginOutPresenter.init(this);
    }

    @Override
    public void loadData() {
        ServiceUtil.setTagAndAlias(mContext);
        //新的应用重复启动解决方法
        if (!isTaskRoot()) {
            //判断该Activity是不是任务空间的源Activity,"非"也就是说是被系统重新实例化出来的
            //如果你就放在Launcher Activity中的话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isRequesting) {
            requestPermissions(permissions, mListener);
            isRequesting = true;
        }
    }
    private PermissionsListener mListener = new PermissionsListener() {
        @Override
        public void onGranted() {
            isRequesting = false;
            if (SpUtil.getInt(Constant.CACHE_IS_FIRST_LOGIN,Constant.HAS_ALREADY_LOGIN) == Constant.HAS_ALREADY_LOGIN) {
                // TODO: 2017/5/11  疑问？首次登录，为什么要调loginOut()接口
                mLoginOutPresenter.loginOut();
                // TODO: 2017/5/11  疑问？首次登录，肯定没有用户信息，updateDeviceReport()这个时候调用没意义
                updateDeviceReport();
                startActivity(GuideActivity.class);
                finish();
            } else {
                if (App.getConfig().isDebug()){
                    startActivity(UrlSelectorActivity.class);
                }else{
                    startActivity(MainActivity.class);
                }
                finish();
            }
            //未看到其他地方的响应逻辑，无用代码
            EventBus.getDefault().post(new LoginNoRefreshUIEvent(getApplicationContext(), App.getConfig().getUserInfo()));
        }
        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            if (!isNeverAsk) {//请求权限没有全被勾选不再提示然后拒绝
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("退出").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        finish();
                    }
                }).setContent("为了能正常使用\"" + App.getAPPName() + "\"，请授予所需权限")
                        .setRightBtnText("授权").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        requestPermissions(permissions, mListener);
                    }
                }).build();
            } else {//全被勾选不再提示
                new AlertFragmentDialog.Builder(mActivity)
                        .setLeftBtnText("退出").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                    @Override
                    public void dialogLeftBtnClick() {
                        finish();
                    }
                }).setContent("\""+ App.getAPPName()+"\"缺少必要权限\n请手动授予\""+ App.getAPPName()+"\"访问您的权限")
                        .setRightBtnText("授权").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        isRequesting = false;
                    }
                }).build();
            }
        }
    };

    private void updateDeviceReport() {
        if (App.getConfig().getUserInfo() != null) {
            mDeviceReportPresenter.deviceReport(ViewUtil.getDeviceId(this),
                    ViewUtil.getInstalledTime(this),
                    App.getConfig().getUserInfo().getUid()+"",
                    App.getConfig().getUserInfo().getUsername(),
                    ViewUtil.getNetworkType(this),
                    ViewUtil.getDeviceName(),
                    App.getConfig().getChannelName());
        }
    }


    @Override
    public void loginOutSuccess() {

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
    public void deviceReportSuccess() {

    }
}
