package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.contract.SetPayPwdContract;
import com.innext.szqb.util.crypt.AESCrypt;

/**
 * Created by hengxinyongli on 2017/2/18 0018.
 */

public class SetPayPwdPresenter extends BasePresenter<SetPayPwdContract.View> implements SetPayPwdContract.Presenter {
    @Override
    public void setPayPwd(String password) {
        password = AESCrypt.INSTANCE.aesEncrypt(password);  //加密密码
        toSubscribe(HttpManager.getApi().setPayPwd(password), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.setPayPwdSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
