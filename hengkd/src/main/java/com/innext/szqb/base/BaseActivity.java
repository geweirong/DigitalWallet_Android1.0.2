package com.innext.szqb.base;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.app.AppManager;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.util.rx.RxManager;
import com.innext.szqb.util.StatusBarUtil;
import com.innext.szqb.util.TUtil;
import com.innext.szqb.util.view.TitleUtil;
import com.innext.szqb.widget.keyboard.KeyboardNumberUtil;
import com.innext.szqb.widget.refresh.base.SwipeToLoadLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    public T mPresenter;
    public Context mContext;
    public BaseActivity mActivity;
    private PermissionsListener mListener;
    protected TitleUtil mTitle;
    protected KeyboardNumberUtil input_controller;
    protected int type;
    public RxManager mRxManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        AppManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        mRxManager = new RxManager();
        mContext = this;
        mActivity = this;
        mPresenter = TUtil.getT(this, 0);
        mTitle = new TitleUtil(this, getWindow().getDecorView());
        initStatusBar();
        initPresenter();
        loadData();
    }

    private void initStatusBar() {
        //层垫式状态栏
        StatusBarUtil.setStatusBarColor(this, R.color.colorPrimaryDark);
//        ImmersionBar.with(this)
//                .titleBar(mTitle.getmToolbar())
//                .init();
    }


    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //加载、设置数据
    public abstract void loadData();


    protected boolean isKeyboardShow() {
        if (input_controller != null) {
            return input_controller.isKeyboardShow();
        } else {
            return false;
        }
    }

    protected void showKeyboard(View wholeView, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE type, EditText view) {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        view.requestFocus();
        input_controller = new KeyboardNumberUtil(this, wholeView, type, view);
        input_controller.showKeyboard();
        addInputListener(view);
    }

    protected void hideKeyboard() {
        if (input_controller != null) {
            input_controller.hideKeyboard();
        }
    }

    private void addInputListener(final EditText view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard();
            }
        });
        input_controller.setmKeyboardClickListenr(new KeyboardNumberUtil.KeyboardNumberClickListener() {

            @Override
            public void postHideEt() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void handleHideEt(View relatedEt, int hideDistance) {
            }

            @Override
            public void clickDelete() {
                if (view.length() > 0) {
                    view.setText(view.getText().subSequence(0, view.length() - 1));
                    view.setSelection(view.getText().length());
                }
            }

            @Override
            public void click(String clickStr) {
                view.append(clickStr);
                view.setSelection(view.getText().length());
            }

            @Override
            public void clear() {
                view.setText("");
                view.setSelection(view.getText().length());
            }
        });
    }

    /**
     * 滑到底部自动加载
     *
     * @param refresh
     * @param swipeTarget
     */
    protected void autoLoading(final SwipeToLoadLayout refresh, RecyclerView swipeTarget) {
        //自动上拉
        swipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        refresh.setLoadingMore(true);
                    }
                }
            }
        });
    }

    protected void onComplete(SwipeToLoadLayout refresh) {
        if (refresh != null) {
            if (refresh.isRefreshing()) {
                refresh.setRefreshing(false);
            } else if (refresh.isLoadingMore()) {
                refresh.setLoadingMore(false);
            }
        }

    }

    /**
     * 请求权限封装
     *
     * @param permissions
     * @param listener
     */
    public void requestPermissions(String[] permissions, PermissionsListener listener) {
        mListener = listener;
        List<String> requestPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions.add(permission);
            }
        }
        if (!requestPermissions.isEmpty() && Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), 1);
        } else {
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                List<String> deniedPermissions = new ArrayList<>();
                //当所有拒绝的权限都勾选不再询问，这个值为true,这个时候可以引导用户手动去授权。
                boolean isNeverAsk = true;
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    String permission = permissions[i];
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        deniedPermissions.add(permissions[i]);
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) { // 点击拒绝但没有勾选不再询问
                            isNeverAsk = false;
                        }
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    try {
                        mListener.onGranted();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        mListener.onDenied(Arrays.asList(permissions), true);
                    }
                } else {
                    mListener.onDenied(deniedPermissions, isNeverAsk);
                }
                break;
            default:
                break;
        }
    }

    // 启动应用的设置弹窗
    public void toAppSettings(String message, final boolean isFinish) {
        if (TextUtils.isEmpty(message)) {
            message = "\"" + App.getAPPName() + "\"缺少必要权限";
        }
        AlertFragmentDialog.Builder builder = new AlertFragmentDialog.Builder(this);
        if (isFinish) {
            builder.setLeftBtnText("退出")
                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                        @Override
                        public void dialogLeftBtnClick() {
                            finish();
                        }
                    });
        } else {
            builder.setLeftBtnText("取消");
        }
        builder.setContent(message + "\n请手动授予\"" + App.getAPPName() + "\"访问您的权限")
                .setRightBtnText("去设置")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }).build();
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    IWindowFocus iFocus;

    public void setOnIWindowFocus(IWindowFocus windowFocus) {
        iFocus = windowFocus;
    }

    public interface IWindowFocus {
        void focused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (mRxManager != null) mRxManager.clear();
        AppManager.getInstance().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (isKeyboardShow()) {
            hideKeyboard();
        } else {
            super.onBackPressed();
        }
    }
}
