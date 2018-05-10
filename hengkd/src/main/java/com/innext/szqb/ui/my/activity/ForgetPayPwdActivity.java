package com.innext.szqb.ui.my.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.events.RefreshUIEvent;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.login.activity.SmsObserver;
import com.innext.szqb.ui.login.bean.CaptchaUrlBean;
import com.innext.szqb.ui.login.contract.ForgetPwdContract;
import com.innext.szqb.ui.login.contract.VerifyResetPwdContract;
import com.innext.szqb.ui.login.presenter.ForgetPwdPresenter;
import com.innext.szqb.ui.login.presenter.VerifyResetPwdPresenter;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.ClearEditText;
import com.innext.szqb.widget.keyboard.KeyboardNumberUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by hengxinyongli on 2017/2/17 0017.
 */

public class ForgetPayPwdActivity extends BaseActivity<ForgetPwdPresenter> implements ForgetPwdContract.View, VerifyResetPwdContract.View {
    @BindView(R.id.et_phone_number)
    ClearEditText mEtPhoneNumber;
    @BindView(R.id.et_real_name)
    ClearEditText mEtRealName;
    @BindView(R.id.et_idcard_num)
    ClearEditText mEtIdcardNum;
    @BindView(R.id.et_verification)
    EditText mEtVerification;
    @BindView(R.id.tv_verification)
    TextView mTvVerification;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.llCustomerKb)
    View llCustomerKb;

    private int curTime;
    private static final int INTERVAL = 1;
    private String phone;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private SmsObserver smsObserver;
    protected AlertDialog dialog;
    private VerifyResetPwdPresenter mVerifyResetPwdPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ucenter_forgetpaypwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        mVerifyResetPwdPresenter = new VerifyResetPwdPresenter();
        mVerifyResetPwdPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("找回交易密码");
        EventBus.getDefault().register(this);
        initEtText();
        initSmsGet();
        mEtIdcardNum.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                boolean touchable = event.getX() > (mEtIdcardNum.getWidth() - mEtIdcardNum.getTotalPaddingRight())
                        && (event.getX() < ((mEtIdcardNum.getWidth() - mEtIdcardNum.getPaddingRight())));
                if (touchable) {
                    mEtIdcardNum.setText("");
                } else if (!isKeyboardShow()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    showKeyboard(llCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE.ID_CARD, mEtIdcardNum);
                }
                return true;
            }
        });
    }

    private void initEtText() {
        String phone = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        if (!TextUtils.isEmpty(phone)) {
            mEtPhoneNumber.setText(phone);
            mEtPhoneNumber.setEnabled(false);
            mEtPhoneNumber.setFocusable(false);
        } else {
            mEtPhoneNumber.addTextChangedListener(textWatcher);
        }
        mEtRealName.addTextChangedListener(textWatcher);
        mEtIdcardNum.addTextChangedListener(textWatcher);
        mEtVerification.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (check())
                mTvSubmit.setEnabled(true);
            else
                mTvSubmit.setEnabled(false);
        }
    };

    @OnClick({R.id.tv_verification, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verification:
                // 获取验证码的点击事件
                if (StringUtil.isBlankEdit(mEtPhoneNumber)) {
                    ToastUtil.showToast("请输入手机号");
                    return;
                }
                if (!StringUtil.isMobileNO(mEtPhoneNumber.getText().toString())) {
                    ToastUtil.showToast("请输入正确的手机号");
                    return;
                }
                phone = mEtPhoneNumber.getText().toString();
                mTvVerification.setText("正在发送");
                mPresenter.forgetPwd(phone, "find_pay_pwd","","0");
                break;
            case R.id.tv_submit:
                hideKeyboard();
                final String verifyCode = mEtVerification.getText().toString().trim();
                if (!StringUtil.isMobileNO(mEtPhoneNumber.getText().toString())) {
                    ToastUtil.showToast("请输入正确的手机号");
                } else if (mEtIdcardNum.getText().length() != 15 && mEtIdcardNum.getText().length() != 18) {
                    ToastUtil.showToast("请输入正确的身份证号");
                } else if (mEtVerification.getText().length() < 6) {
                    ToastUtil.showToast("验证码输入不正确");
                } else {
                    phone = mEtPhoneNumber.getText().toString();
                    mVerifyResetPwdPresenter.verifyResetPwd(phone,
                            verifyCode, mEtRealName.getText().toString(),
                            mEtIdcardNum.getText().toString(), "find_pay_pwd","");
                }
                break;
        }
    }

    private void setSendCode(boolean send) {
        curTime = 60;
        if (send) {
            mHandler.sendEmptyMessage(INTERVAL);
            mTvVerification.setTextColor(ContextCompat.getColor(mContext,R.color.global_label_color));
            mTvVerification.setEnabled(false);
        } else {
            mTvVerification.setText("重新发送");
            mTvVerification.setTextColor(ContextCompat.getColor(mContext,R.color.theme_color));
            mTvVerification.setEnabled(true);
        }
    }

    private void initSmsGet() {
        smsObserver = new SmsObserver(mHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshUIEvent event) {
        if (event.getType() == UIBaseEvent.EVENT_SET_PAYPWD) {
            ToastUtil.showToast("交易密码设置成功");
            finish();
        }
    }

    private  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INTERVAL:
                    if (curTime > 0) {
                        mTvVerification.setText("" + curTime + "秒");
                        mHandler.sendEmptyMessageDelayed(INTERVAL, 1000);
                        curTime--;
                    } else {
                        setSendCode(false);
                    }
                    break;
                case SmsObserver.SEND_VERIFY_NUM:
                    mEtVerification.setText(smsObserver.verifyNum);
                    mEtVerification.setSelection(smsObserver.verifyNum.length());
                    break;
                default:
                    setSendCode(false);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        getContentResolver().unregisterContentObserver(smsObserver);
    }

    private boolean check() {
        if (StringUtil.isBlankEdit(mEtPhoneNumber)) {
            return false;
        }
        if (StringUtil.isBlankEdit(mEtRealName)) {
            return false;
        }
        if (StringUtil.isBlankEdit(mEtIdcardNum)) {
            return false;
        }
        if (StringUtil.isBlankEdit(mEtVerification)) {
            return false;
        }
        return true;
    }

    @Override
    public void forgetPwdSuccess(CaptchaUrlBean captchaUrlBean) {
        ToastUtil.showToast("验证码已发送");
        setSendCode(true);
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (null != type && type.equals(mPresenter.TYPE_FORGET_PWD)) {
            mTvVerification.setText("重新发送");
        }
        ToastUtil.showToast(msg);
    }

    @Override
    public void verifySuccess() {
        startActivity(new Intent(ForgetPayPwdActivity.this, SetTradePwdActivity.class));
        finish();
    }
}
