package com.innext.szqb.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.innext.szqb.app.App;
import com.innext.szqb.util.TUtil;
import com.innext.szqb.util.view.TitleUtil;
import com.innext.szqb.widget.keyboard.KeyboardNumberUtil;
import com.innext.szqb.widget.refresh.base.SwipeToLoadLayout;

import butterknife.ButterKnife;

/**
 * fragment基类
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    protected View mView;
    public T mPresenter;
    public Context mContext;
    public BaseActivity mActivity;
    protected TitleUtil mTitle;
    protected KeyboardNumberUtil input_controller;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mView = inflater.inflate(getLayoutId(),null);
        ButterKnife.bind(this, mView);
        mContext = getContext();
        mActivity= (BaseActivity) getActivity();
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            //mPresenter.mContext = mContext;
        }
        mTitle = new TitleUtil(mActivity,mView);
        initPresenter();
        loadData();
        return mView;
    }


    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //加载View、设置数据
    public abstract void loadData();


    protected boolean isKeyboardShow() {
        if (input_controller != null) {
            return input_controller.isKeyboardShow();
        } else {
            return false;
        }
    }


    protected void showKeyboard(View wholeView, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE type, EditText view) {
        ((InputMethodManager) getActivity().getSystemService(getActivity
                ().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        view.requestFocus();
        input_controller = new KeyboardNumberUtil(getActivity(), wholeView, type, view);
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
     * 请求权限封装
     *
     * @param permissions
     * @param listener
     */
    public void requestPermissions(String[] permissions, PermissionsListener listener) {
       mActivity.requestPermissions(permissions,listener);
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
        intent.setClass(mContext, cls);
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
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    protected void startActivityToLogin(Intent intent) {
        if (!App.getConfig().getLoginStatus()) {
            App.toLogin(getActivity());
        } else {
            startActivity(intent);
        }
    }
    /**
     * 还原刷新
     *
     * @param refresh
     */
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
