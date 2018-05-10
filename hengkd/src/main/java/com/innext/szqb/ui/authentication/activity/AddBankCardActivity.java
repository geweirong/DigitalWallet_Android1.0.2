package com.innext.szqb.ui.authentication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.bean.BankItem;
import com.innext.szqb.config.Constant;
import com.innext.szqb.dialog.BankListFragmentDialog;
import com.innext.szqb.ui.authentication.contract.AddBankCardContract;
import com.innext.szqb.ui.authentication.presenter.AddBankCardPresenter;
import com.innext.szqb.ui.main.WebViewActivity;
import com.innext.szqb.util.check.FormatUtil;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.CardEditText;
import com.innext.szqb.widget.keyboard.KeyboardNumberUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.innext.szqb.R.id.llCustomerKb;


/**
 * 添加银行卡
 * hengxinyongli
 */

public class AddBankCardActivity extends BaseActivity<AddBankCardPresenter> implements AddBankCardContract.View {

    @BindView(R.id.tv_user_rname)
    TextView mTvUserRname;
    @BindView(R.id.tv_bank_name)
    TextView mTvBankName;
    @BindView(R.id.et_bankcard_num)
    CardEditText mEtBankcardNum;
    @BindView(R.id.et_phone_num)
    EditText mEtPhoneNum;
    @BindView(R.id.et_verifycode)
    EditText mEtVerifycode;
    @BindView(llCustomerKb)
    LinearLayout mLlCustomerKb;
    @BindView(R.id.tv_send_code)
    TextView mTvSendCode;

    private int bankID;
    private List<BankItem> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_bankcard;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mTitle.setTitle("添加银行卡");
        mTitle.setRightTitle("保存", new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (checkInput()) {
                    save();
                }
            }
        });
        String realname = SpUtil.getString(Constant.CACHE_TAG_REAL_NAME);
        mTvUserRname.setText(realname);
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    public void initListener() {
        mEtPhoneNum.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touchable = event.getX() > (mEtPhoneNum.getWidth() - mEtPhoneNum.getTotalPaddingRight())
                        && (event.getX() < ((mEtPhoneNum.getWidth() - mEtPhoneNum.getPaddingRight())));
                if (touchable) {
                    mEtPhoneNum.setText("");
                } else {
                    mEtPhoneNum.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtVerifycode.getWindowToken(), 0);
                    showKeyboard(mLlCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE.NUMBER, mEtPhoneNum);
                }
                return true;
            }
        });

        mEtBankcardNum.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touchable = event.getX() > (mEtBankcardNum.getWidth() - mEtBankcardNum.getTotalPaddingRight()) && (event.getX() < ((mEtBankcardNum.getWidth() - mEtBankcardNum.getPaddingRight())));
                if (touchable) {
                    mEtBankcardNum.setText("");
                } else {
                    mEtBankcardNum.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtVerifycode.getWindowToken(), 0);
                    showKeyboard(mLlCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE.NUMBER, mEtBankcardNum);
                }
                return true;
            }
        });
    }

    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mTvSendCode.setText(millisUntilFinished / 1000 + "秒 ");
        }

        @Override
        public void onFinish() {
            mTvSendCode.setTextColor(ContextCompat.getColor(mContext, R.color.theme_color));
            mTvSendCode.setEnabled(true);
            mTvSendCode.setText("重新发送");
        }
    };

    /*******
     * 发送验证码
     */
    private void sendVerifyCode() {
        String phoneNum = mEtPhoneNum.getText().toString();
        if (StringUtil.isBlankEdit(mTvBankName)) {
            ToastUtil.showToast("请选择银行");
            return;
        }
        if (StringUtil.isBlankEdit(mEtBankcardNum)) {
            ToastUtil.showToast("请输入银行卡号");
            return;
        }
        if (!StringUtil.isMobileNO(mEtPhoneNum.getText().toString())) {
            ToastUtil.showToast("请输入正确的手机号码");
            return;
        }
        mTvSendCode.setText("正在发送");
        mTvSendCode.setEnabled(false);
        mPresenter.getCardCode(phoneNum);
    }


//    /***********
//     * eventBus 监听
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(ChangePayPwdEvent event) {
//        finish();
//    }

    @Override
    public void getCardCodeSuccess() {
        mTvSendCode.setTextColor(ContextCompat.getColor(mContext, R.color.global_label_color));
        countDownTimer.start();
        mEtVerifycode.requestFocus();
    }

    @Override
    public void getBankCardListSuccess(List<BankItem> list) {
        this.list = list;
        chooseBank();
    }

    @Override
    public void addBankCardSuccess(String signpath) {
        Intent intent = new Intent(AddBankCardActivity.this, WebViewActivity.class);
        intent.putExtra("url", signpath);
        startActivity(intent);
        finish();
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
        if (type.equals(mPresenter.TYPE_GET_CODE)) {
            mTvSendCode.setText("重新发送");
            mTvSendCode.setTextColor(ContextCompat.getColor(mContext, R.color.theme_color));
            mTvSendCode.setEnabled(true);
        }
        ToastUtil.showToast(msg);
    }

    @OnClick({R.id.tv_send_code,R.id.ll_choose_bankcard})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()){
            case R.id.tv_send_code:
                hideKeyboard();
                sendVerifyCode();
                break;
            case R.id.ll_choose_bankcard:
                hideKeyboard();
                chooseBank();
                break;
        }

    }

    private void chooseBank() {
        if (list != null && list.size() > 0) {
            new BankListFragmentDialog.Builder(this)
                    .setList(list)
                    .setSelectBankCardListener(new BankListFragmentDialog.selectBankCardListener() {
                        @Override
                        public void selectBankCard(BankItem list) {
                            mTvBankName.setText(list.getBank_name());
                            bankID = list.getBank_id();
                        }
                    }).build();
        } else {
            mPresenter.getBankCardList();
        }
    }

    private void save() {
        String carNum = mEtBankcardNum.getText().toString().trim().replaceAll(" ", "");
        mPresenter.addBankCard(mEtPhoneNum.getText().toString()
                , mEtVerifycode.getText().toString()
                , carNum
                , String.valueOf(bankID));
    }


    private boolean checkInput() {
        if (StringUtil.isBlankEdit(mTvBankName)) {
            ToastUtil.showToast("请选择银行");
            return false;
        }
        if (StringUtil.isBlankEdit(mEtBankcardNum)) {
            ToastUtil.showToast("请输入银行卡号");
            return false;
        }
        if (!FormatUtil.checkBankCard(mEtBankcardNum.getText().toString().trim().replaceAll(" ", ""))){
            ToastUtil.showToast("请输入正确的银行卡号");
            return false;
        }
        if (!StringUtil.isMobileNO(mEtPhoneNum.getText().toString())) {
            ToastUtil.showToast("请输入正确的手机号码");
            return false;
        }
        if (StringUtil.isBlankEdit(mEtVerifycode)) {
            ToastUtil.showToast("请输入短信验证码");
            return false;
        }
        return true;
    }

}
