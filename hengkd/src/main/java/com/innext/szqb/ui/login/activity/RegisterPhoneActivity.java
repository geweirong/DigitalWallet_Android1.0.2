package com.innext.szqb.ui.login.activity;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.comm.CommContract;
import com.innext.szqb.events.BaseEvent;
import com.innext.szqb.events.LoginEvent;
import com.innext.szqb.ui.login.bean.CaptchaUrlBean;
import com.innext.szqb.ui.login.contract.GetRegisterCodeContract;
import com.innext.szqb.ui.login.presenter.GetRegisterCodePresenter;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.widget.ClearEditText;
import com.innext.szqb.widget.keyboard.KeyboardNumberUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.innext.szqb.util.common.ToastUtil.showToast;

public class RegisterPhoneActivity extends BaseActivity<GetRegisterCodePresenter> implements GetRegisterCodeContract.View,CommContract.View {

    @BindView(R.id.et_phone_number)
    ClearEditText mEtPhoneNumber;
    @BindView(R.id.scrollview)
    ScrollView mScrollview;
    @BindView(R.id.llCustomerKb)
    LinearLayout mLlCustomerKb;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    private String userName;
//    private CommPresenter mCommPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_phone;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
//        mCommPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mTitle.setTitle(App.getAPPName());
        mEtPhoneNumber.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touchable = event.getX() > (mEtPhoneNumber.getWidth() - mEtPhoneNumber.getTotalPaddingRight())
                        && (event.getX() < ((mEtPhoneNumber.getWidth() - mEtPhoneNumber.getPaddingRight())));
                if (touchable) {
                    mEtPhoneNumber.setText("");
                } else if (!isKeyboardShow()) {
                    showKeyboard(mLlCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE.NUMBER, mEtPhoneNumber);
                }
                return true;
            }
        });
        mScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvNext.requestFocus();
        MobclickAgent.onEvent(this, "register_phone_activity");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LoginSuccess(BaseEvent event) {
        if (event instanceof LoginEvent) {
            finish();
        }
    }

    @OnClick(R.id.tv_next)
    public void onClick() {
        hideKeyboard();
        userName = mEtPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            showToast("手机号码不能为空");
        } else if (StringUtil.isMobileNO(userName)) {
            mPresenter.getRegisterCode(userName,"0","");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            得到InputMethodManager的实例
            imm.hideSoftInputFromWindow(mEtPhoneNumber.getWindowToken(), 0);


//            DialogUtils.showVerifyCodeDialog(this, false, "url",
//                    new VerifyCodeDialog.RightClickCallBack() {
//                        @Override
//                        public void dialogRightBtnClick(VerifyCodeDialog dialog, String code) {
//                            mPresenter.getRegisterCode(userName, "0", "");
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            //得到InputMethodManager的实例
//                            imm.hideSoftInputFromWindow(mEtPhoneNumber.getWindowToken(), 0);
//                        }
//                    },
//                    new VerifyCodeDialog.ChangeCodeClickCallBack() {
//                        @Override
//                        public void onChangeCodeClick(VerifyCodeDialog dialog) {
//                            // TODO 1.调用验证码接口,
//                            mCommPresenter.getVerifyCode("","","");
//                            // TODO 2.调用验证码接口
////                            dialog
//
//                        }
//                    }
//            );

        } else {
            showToast("手机号码输入不正确");
        }
    }


    @Override
    public void getCodeSuccess(CaptchaUrlBean captchaUrlBean) {
        String captcha = captchaUrlBean.getCaptchaUrl();
        Intent intent = new Intent(RegisterPhoneActivity.this, RegisterPasswordActivity.class);
        intent.putExtra("phone", userName);
        intent.putExtra("captchaUrl", captcha);
        startActivity(intent);
    }


    /**
     * 公共组件中的成功接口实现
     * @param
     */
    @Override
    public void getVerifyCodeSuccess(CaptchaUrlBean captchaUrl) {

    }

    /**
     * 公共组件中的错误接口实现
     * @param msg
     * @param code
     */
    @Override
    public void showCodeErrorMsg(String msg, int code) {

    }

    @Override
    public void showErrorMsg(String msg, int code) {
        if (code == 1000) {
            // 这个手机号已经注册，直接登录
            Intent intent = new Intent(RegisterPhoneActivity.this, LoginActivity.class);
            intent.putExtra("tag", StringUtil.changeMobile(userName));
            intent.putExtra("phone", userName);
            intent.putExtra("message", msg);
            startActivity(intent);
        } else {
            showToast(msg);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
