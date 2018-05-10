package com.innext.szqb.ui.login.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.bean.LoginBean;
import com.innext.szqb.ui.login.contract.LoginContract;
import com.innext.szqb.util.crypt.AESCrypt;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.presenter{
    @Override
    public void login(String username, String password) {
        password = AESCrypt.INSTANCE.aesEncrypt(password);  //加密密码
        toSubscribe(HttpManager.getApi().login(username, password), new HttpSubscriber<LoginBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("登录中...");
            }

            @Override
            protected void _onNext(LoginBean login) {
                mView.loginSuccess(login.getItem());
            }
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }
            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
