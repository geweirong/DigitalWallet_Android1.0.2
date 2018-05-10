package com.innext.szqb.ui.login.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.events.LoginEvent;
import com.innext.szqb.ui.login.bean.CaptchaUrlBean;
import com.innext.szqb.ui.login.contract.GetRegisterCodeContract;
import com.innext.szqb.ui.login.contract.RegisterContract;
import com.innext.szqb.ui.login.presenter.GetRegisterCodePresenter;
import com.innext.szqb.ui.login.presenter.RegisterPresenter;
import com.innext.szqb.ui.main.WebViewActivity;
import com.innext.szqb.ui.my.bean.UserInfoBean;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.view.TextViewUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.ClearEditText;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.innext.szqb.R.id.ck_agreement;
import static com.innext.szqb.R.id.et_invite;
import static com.innext.szqb.R.id.tv_loan_agreement;
import static com.innext.szqb.R.id.tv_verification;


public class RegisterPasswordActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View,
        GetRegisterCodeContract.View {
    @BindView(R.id.et_verification)
    EditText mEtVerification;
    @BindView(R.id.et_password)
    ClearEditText mEtPassword;
    @BindView(et_invite)
    ClearEditText mEtInvite;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(ck_agreement)
    CheckBox mCkAgreement;
    @BindView(tv_loan_agreement)
    TextView mTvLoanAgreement;
    @BindView(tv_verification)
    TextView mTvVerification;
    @BindView(R.id.tv_credit_agreement)
    TextView mTvCreaditAgreement;
    @BindView(R.id.rl_graph_code)
    RelativeLayout mRlGraphCode;
    @BindView(R.id.et_graph_code)
    EditText mEtGraphCode;
    @BindView(R.id.iv_graph_code)
    ImageView mIvGraphCode;
    @BindView(R.id.rl_invitecode_show)
    RelativeLayout mRlInvitecodeShow;
    @BindView(R.id.rl_invitecode)
    RelativeLayout mRlInvitecode;

    private String phone;
    private String captchaUrl;
    private static final int INTERVAL = 1;
    private int curTime;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private SmsObserver smsObserver;
    protected AlertDialog dialog;
    private GetRegisterCodePresenter registerCodePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_pwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        registerCodePresenter = new GetRegisterCodePresenter();
        registerCodePresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("注册");
        mCkAgreement.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTvSubmit.setEnabled(isChecked);
            }
        });
        TextViewUtil.setPartialColor(mTvLoanAgreement, 5, mTvLoanAgreement.getText().length(), ContextCompat.getColor(mContext, R.color.theme_color));
        initContents();
        initSmsGet();
    }


    private void initContents() {
        phone = getIntent().getStringExtra("phone");
        captchaUrl = getIntent().getStringExtra("captchaUrl");
        if (!StringUtil.isBlank(captchaUrl)) {
            mRlGraphCode.setVisibility(View.VISIBLE);
            setSendCode(false);
            mTvVerification.setText("获取验证码");
            Glide.with(this).load(captchaUrl).placeholder(R.mipmap.icon_captcha_back).error(R.mipmap.icon_captcha_back)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(mIvGraphCode);
        } else {
            if (StringUtil.isBlank(phone)) {
                ToastUtil.showToast("手机号码获取失败，请重试");
                finish();
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
            mTvVerification.setTextColor(ContextCompat.getColor(this, R.color.global_label_color));
            mTvVerification.setEnabled(false);
        } else {
            mTvVerification.setText("重新发送");
            mTvVerification.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
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

        ;
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(smsObserver);
    }

    @OnClick({tv_verification, R.id.tv_submit, R.id.ll_agreement, R.id.tv_credit_agreement, R.id.tv_loan_agreement, R.id.rl_invitecode_show})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case tv_verification:
                registerCodePresenter.getRegisterCode(phone, "1", mEtGraphCode.getText().toString().trim());
                break;
            case R.id.tv_submit:
                String edit_verification = mEtVerification.getText().toString().trim();
                String edit_password = mEtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(edit_verification)) {
                    ToastUtil.showToast("请输入短信验证码");
                } else if (TextUtils.isEmpty(edit_password)) {
                    ToastUtil.showToast("请输入登录密码");
                } else if (edit_password.length() < 6 || edit_password.length() > 16) {
                    ToastUtil.showToast("登录密码需由6~16字符组成");
                } else if (!Tool.isDigitalAlphabet(edit_password)) {
                    ToastUtil.showToast(getResources().getString(R.string.login_password_alter));
                } else {
                    String inviteCode = mEtInvite.getText().toString();
                    String user_from = WalleChannelReader.get(mContext, "user_from");
                    mPresenter.toRegister(phone, edit_verification, edit_password, "37", inviteCode, user_from, "");
                }
                break;
            case R.id.tv_loan_agreement:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", App.getConfig().REGISTER_AGREEMENT);
                startActivity(intent);
                break;
            case R.id.tv_credit_agreement:
                Intent intentTwo = new Intent(mContext, WebViewActivity.class);
                intentTwo.putExtra("url", App.getConfig().CREDIT_AUTHORIZATION_AGREEMENT);
                startActivity(intentTwo);
                break;
            case R.id.rl_invitecode_show:
                mRlInvitecodeShow.setVisibility(View.GONE);
                mRlInvitecode.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void getCodeSuccess(CaptchaUrlBean captchaUrlBean) {
        ToastUtil.showToast("验证码已发送");
        setSendCode(true);
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void registerSuccess(UserInfoBean info) {
        EventBus.getDefault().post(new LoginEvent(getApplicationContext(), info, false));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
        finish();
    }

    /**
     * 请求失败清空验证码和密码
     */
    @Override
    public void registerError() {
        mEtVerification.getText().clear();
        mEtPassword.getText().clear();
    }

    @Override
    public void showLoading(String content) {
        if (content.equals("正在发送...")) {
            mTvVerification.setText("正在发送");
        }
        App.loadingContent(this, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (null != type && type.equals(registerCodePresenter.TYPE_REGISTER_CODE)) {
            mTvVerification.setText("重新发送");
        }
        ToastUtil.showToast(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, "register");
    }
}
