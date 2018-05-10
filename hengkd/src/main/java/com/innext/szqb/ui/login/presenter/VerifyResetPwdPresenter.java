package com.innext.szqb.ui.login.presenter;/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.contract.VerifyResetPwdContract;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */
public class VerifyResetPwdPresenter extends BasePresenter<VerifyResetPwdContract.View> implements VerifyResetPwdContract.presenter {
    @Override
    public void verifyResetPwd(String phone, String code, String realname, String id_card, String type,String captcha) {
        toSubscribe(HttpManager.getApi().verifyResetPwd(phone, code, realname, id_card, type,captcha), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("验证中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.verifySuccess();
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
