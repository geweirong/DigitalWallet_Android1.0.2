package com.innext.szqb.ui.login.presenter;/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.contract.ResetPwdContract;
import com.innext.szqb.util.crypt.AESCrypt;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */
public class ResetPwdPresenter extends BasePresenter<ResetPwdContract.View> implements ResetPwdContract.Presenter {
    @Override
    public void resetPwd(String phone, String code, String password) {
        password = AESCrypt.INSTANCE.aesEncrypt(password);  //加密密码
        toSubscribe(HttpManager.getApi().resetPwd(phone, code, password), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.resetPwdSuccess();
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
