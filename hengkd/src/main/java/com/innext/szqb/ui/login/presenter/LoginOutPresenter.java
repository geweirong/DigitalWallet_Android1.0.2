package com.innext.szqb.ui.login.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.contract.LoginOutContract;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */

public class LoginOutPresenter extends BasePresenter<LoginOutContract.View> implements LoginOutContract.Presenter {
    public final String TYPE_LOGIN_OUT = "loginOut";
    public final String TYPE_MEMBER = "member";
    @Override
    public void loginOut() {
        toSubscribe(HttpManager.getApi().loginOut(), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("退出中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.loginOutSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_LOGIN_OUT);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

}
