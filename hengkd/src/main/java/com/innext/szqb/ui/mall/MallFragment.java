package com.innext.szqb.ui.mall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseFragment;
import com.innext.szqb.config.Constant;
import com.innext.szqb.config.WebViewConstant;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.dialog.MaterialDialog;
import com.innext.szqb.events.LoginEvent;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.ui.authentication.presenter.SaveAlipayInfoPresenter;
import com.innext.szqb.ui.main.WebViewActivity;
import com.innext.szqb.ui.mall.bean.MallCheackAuthorBean;
import com.innext.szqb.ui.mall.contract.MallContract;
import com.innext.szqb.ui.mall.presenter.MallPresenter;
import com.innext.szqb.ui.my.bean.MoreContentBean;
import com.innext.szqb.util.Tool;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.LogUtils;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.loading.LoadingLayout;
import com.innext.szqb.widget.statusBar.ImmersionBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by HX0010637 on 2018/3/29.
 */

public class MallFragment extends BaseFragment<MallPresenter> implements MallContract.View{
    public static MallFragment sFragment;
    @BindView(R.id.wb_mall)
    WebView mWebView;
    @BindView(R.id.layout_no_authentication)
    LinearLayout mNoAuthentication;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
//    @BindView(R.id.toolbar1)
//    LinearLayout mToolbar1;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    private String mUrl;
    private boolean isZhbTitle;
    private String title;
    private int cacheType;  //是否使用缓存，默认0使用，1不使用
    private HashMap<String, String> mHashMap;
    /**
     * 是否隐藏左侧'X'按钮,默认false
     */
    private boolean isHideLeftCloseBtn;
    private String urlParameter = "";
    private boolean isNeedParamter = false;
    private String typeStr = "";
    public static MallFragment getInstance(){
        if (sFragment == null)
            sFragment = new MallFragment();
        return sFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mall;
    }

    @Override
    public void initPresenter() {
       mPresenter.init(this);
    }

    @Override
    public void loadData() {
        ImmersionBar.with(this)
                .titleBar(mTitle.getmToolbar())
                .init();
        mTitle.setTitle("商城");
        mPresenter.getCheackAuthor();
        initView();
    }
    /*
     * 判断是否授权
     */
    private void getAuthentication(boolean isAuthentication,String url) {
        if (isAuthentication){
            mWebView.setVisibility(View.VISIBLE);
            mNoAuthentication.setVisibility(View.GONE);
            mWebView.loadUrl(url);
        }else {
            showDialog();
            mWebView.setVisibility(View.GONE);
            mNoAuthentication.setVisibility(View.VISIBLE);
        }
    }
    /*
     * 授权弹框
     */
    private void showDialog(){
        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext);
        mMaterialDialog.setTitle("授权提示").setMessage("授权获取位置、昵称信息")
                .setPositiveButton("同意", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认授权
                mPresenter.getAuthorInfo();
                mMaterialDialog.dismiss();
            }
        }).setNegativeButton("拒绝", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        }).show();
    }
    public void initView() {
        mHashMap = new HashMap<>();
        if (mActivity.getIntent() != null) {
            if (!StringUtil.isBlank(mActivity.getIntent().getStringExtra("title"))) {
                title = mActivity.getIntent().getStringExtra("title");
                mTitle.setTitle(title);
            }
            if (!StringUtil.isBlank(mActivity.getIntent().getStringExtra("improveUrl"))) {//该链接是为了提额的改动
                mUrl = mActivity.getIntent().getStringExtra("improveUrl");
            } else {
                isNeedParamter = mActivity.getIntent().getBooleanExtra(Constant.CACHE_TAG_UID, false);
                typeStr = mActivity.getIntent().getStringExtra("type");
                if (isNeedParamter) {
                    urlParameter = "user_id=" + SpUtil.getString(Constant.CACHE_TAG_UID);
                }
                mUrl = mActivity.getIntent().getStringExtra("url");
            }
            cacheType = mActivity.getIntent().getIntExtra(WebViewConstant.CACHE_TYPE, WebViewConstant.USED_CACHE);
        }
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //WebView属性设置！！！
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        if (cacheType == WebViewConstant.UNUSED_CACHE) {
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else  {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);

        //webview在安卓5.0之前默认允许其加载混合网络协议内容
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.addJavascriptInterface(new JavaMethod(), "nativeMethod");
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(mActivity, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void getCheackAuthorSuccess(MallCheackAuthorBean mallAuthorBean) {
//        Log.e("效验是否授权","请求地址："+mallAuthorBean.getStatus());
        mUrl = mallAuthorBean.getUrl();
        if (mallAuthorBean.getStatus()) {
            getAuthentication(true, mallAuthorBean.getUrl());
        }else{
            getAuthentication(false, "");
        }
    }

    @Override
    public void getAuthorSuccess() {
        getAuthentication(true,mUrl);
    }
    class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //handler.cancel(); // Android默认的处理方式
            handler.proceed();  // 接受所有网站的证书  解决https拦截问题
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
            if (view.canGoBack() && !isHideLeftCloseBtn) { //如果当前不是初始页面则显示关闭按钮
                mTitle.showClose(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWebView.goBack();
                    }
                });
            } else {
                mTitle.hintClose();
            }
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

class MyWebChromeClient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mProgressBar.setProgress(newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        LogUtils.loge(mUrl);
        mProgressBar.setVisibility(View.GONE);
        if (view.canGoBack() && !isHideLeftCloseBtn) { //如果当前不是初始页面则显示关闭按钮
            mTitle.showClose(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        } else {
            mTitle.hintClose();
        }
    }
}
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent event) {
        //登录成功后刷新界面
        mWebView.reload();
    }
    public class JavaMethod {
        @JavascriptInterface
        public void hideBackCloseBtn() {
            mWebView.loadUrl(mUrl);
        }
    }
    @OnClick(R.id.btn_confirm_attach)
    public void onClick(View view){
        if (Tool.isFastDoubleClick())
            return;
        showDialog();
    }
}
