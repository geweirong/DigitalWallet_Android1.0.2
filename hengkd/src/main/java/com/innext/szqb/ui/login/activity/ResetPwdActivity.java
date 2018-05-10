package com.innext.szqb.ui.login.activity;

import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.ui.login.contract.ResetPwdContract;
import com.innext.szqb.ui.login.presenter.ResetPwdPresenter;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.Tool;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPwdActivity extends BaseActivity<ResetPwdPresenter> implements ResetPwdContract.View {
    private String phone;
    private String verifyCode;
    @BindView(R.id.et_new_pwd)
    EditText mEtNewPwd;
    @BindView(R.id.et_new_pwd_again)
    EditText mEtNewPwdAgain;
    public final static String ALTER_PASSWORD_FORGET="alterPassword";
    public final static String ALTER_PASSWORD_FORGET_TYPE="passwordAlterType";

    @Override
    public int getLayoutId() {
        return R.layout.activity_resetpwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("输入新密码");
        mEtNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEtNewPwdAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEtNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        mEtNewPwdAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        phone = getIntent().getStringExtra("phone");
        verifyCode = getIntent().getStringExtra("code");
    }

    /******
     * 修改密码
     */
    private void resetPwdRequest() {

        String pwd = mEtNewPwd.getText().toString().trim();
        String confirmPwd = mEtNewPwdAgain.getText().toString().trim();
        if (Tool.isBlank(pwd)) {
            ToastUtil.showToast("请输入新密码");
            return;
        }
        if (Tool.isBlank(confirmPwd)) {
            ToastUtil.showToast("请输入确认密码");
            return;
        }
        if (pwd.length() < 6 && pwd.length() > 16) {
            ToastUtil.showToast("密码长度错误");
            return;
        }
        if (confirmPwd.length() < 6 || confirmPwd.length() > 16) {
            ToastUtil.showToast("确认密码长度错误");
            return;
        }
        if(!Tool.isDigitalAlphabet(pwd)){ToastUtil.showToast(getResources().getString(R.string.login_password_alter));return;}
        if (!pwd.equals(confirmPwd)) {
            ToastUtil.showToast("两次密码不一致");
            return;
        }
        mPresenter.resetPwd(phone,verifyCode,confirmPwd);
    }
    @OnClick(R.id.tv_complete)
    public void onClick() {
        if (Tool.isFastDoubleClick()) return;
        resetPwdRequest();
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this,content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void resetPwdSuccess() {
        String alterMessage="登录密码设置成功";
        if(ALTER_PASSWORD_FORGET_TYPE.equals(getIntent().getStringExtra(ALTER_PASSWORD_FORGET_TYPE))){
            alterMessage="登录密码修改成功";
        }
        new AlertFragmentDialog.Builder(this)
                .setContent(alterMessage)
                .setRightBtnText("确定")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
//                        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_SET_PWD));
                        finish();
                    }
                }).setCancel(false)
                .build();
    }
}
