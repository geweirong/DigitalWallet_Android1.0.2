package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.contract.ChangePayPwdContract;
import com.innext.szqb.util.crypt.AESCrypt;

/**
 * Created by hengxinyongli on 2017/2/18 0018.
 */

public class ChangPayPwdPresenter extends BasePresenter<ChangePayPwdContract.View> implements ChangePayPwdContract.Presenter{
    @Override
    public void changePayPwd(String oldPwd, String newPwd) {
        oldPwd = AESCrypt.INSTANCE.aesEncrypt(oldPwd);  //加密密码
        newPwd = AESCrypt.INSTANCE.aesEncrypt(newPwd);  //加密密码
        toSubscribe(HttpManager.getApi().changePayPassWord(oldPwd, newPwd), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.changePayPwdSuccess();
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
