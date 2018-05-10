package com.innext.szqb.ui.login.activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.ui.login.bean.CaptchaUrlBean;
import com.innext.szqb.ui.login.contract.ForgetPwdContract;
import com.innext.szqb.ui.login.contract.VerifyResetPwdContract;
import com.innext.szqb.ui.login.presenter.ForgetPwdPresenter;
import com.innext.szqb.ui.login.presenter.VerifyResetPwdPresenter;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.Tool;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;


public class ForgetPwdActivity extends BaseActivity<VerifyResetPwdPresenter> implements VerifyResetPwdContract.View, ForgetPwdContract.View {

    @BindView(R.id.et_verification)
    EditText mEtVerification;
    @BindView(R.id.tv_verification)
    TextView mTvVerification;
    @BindView(R.id.et_graph_code)
    EditText mEtGraphCode;
    @BindView(R.id.iv_graph_code)
    ImageView mIvGraphCode;
    @BindView(R.id.rl_graph_code)
    RelativeLayout mRlGraphCode;
    private ForgetPwdPresenter forgetPwdPresenter;
    private String phone;
    private static final int INTERVAL = 1;
    private int curTime;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private SmsObserver smsObserver;
    private String captchaUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        forgetPwdPresenter = new ForgetPwdPresenter();
        forgetPwdPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("找回登录密码");
        initSmsGet();
        initContents();
    }

    private void initContents() {
        phone = getIntent().getStringExtra(Constant.CACHE_TAG_USERNAME);
        captchaUrl = getIntent().getStringExtra("captchaUrl");
        if (!StringUtil.isBlank(captchaUrl)) {
            mRlGraphCode.setVisibility(View.VISIBLE);
            setSendCode(false);
            mTvVerification.setText("获取验证码");
            Glide.with(this).load(captchaUrl).placeholder(R.mipmap.icon_captcha_back).error(R.mipmap.icon_captcha_back)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(mIvGraphCode);
        } else {
            if (TextUtils.isEmpty(phone)) {
                ToastUtil.showToast("手机号码获取失败，请重试");
            } else {
                phone = phone.trim();
                setSendCode(true);
            }
        }
    }
    private void setSendCode(boolean send) {
        curTime = 60;
        if (send == true) {
            mHandler.sendEmptyMessage(INTERVAL);
            mTvVerification.setTextColor(ContextCompat.getColor(mContext, R.color.global_label_color));
            mTvVerification.setEnabled(false);
        } else {
            mTvVerification.setText("重新发送");
            mTvVerification.setTextColor(ContextCompat.getColor(mContext, R.color.theme_color));
            mTvVerification.setEnabled(true);
        }
    }

    private void initSmsGet() {
        smsObserver = new SmsObserver(mHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
    }

    private Handler mHandler = new Handler() {
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
        getContentResolver().unregisterContentObserver(smsObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this,"forget_pwd");
    }

    @OnClick({R.id.tv_verification, R.id.tv_submit})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.tv_verification:
                mTvVerification.setText("正在发送");
                forgetPwdPresenter.forgetPwd(phone, "find_pwd", mEtGraphCode.getText().toString().trim(), "0");
                break;
            case R.id.tv_submit:
                //下一步
                final String verifyCode = mEtVerification.getText().toString().trim();
                if (TextUtils.isEmpty(verifyCode)) {
                    ToastUtil.showToast("请输入短信验证码");
                } else if (mEtVerification.getText().length() < 6) {
                    ToastUtil.showToast("验证码输入不正确");
                } else {
                    mPresenter.verifyResetPwd(phone, verifyCode, "", "", "find_pwd", "");
                }
                break;
        }
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
        ToastUtil.showToast(msg);
        if (!TextUtils.isEmpty(type) && type.equals(forgetPwdPresenter.TYPE_FORGET_PWD)) {
            mTvVerification.setText("重新发送");
        }
    }

    @Override
    public void verifySuccess() {
        Intent intent = new Intent(ForgetPwdActivity.this, ResetPwdActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code", mEtVerification.getText().toString().trim());
        startActivity(intent);
        finish();
    }
}

