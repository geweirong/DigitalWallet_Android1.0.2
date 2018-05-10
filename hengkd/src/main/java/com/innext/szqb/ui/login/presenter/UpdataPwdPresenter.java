package com.innext.szqb.ui.login.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.contract.UpdataPwdContract;
import com.innext.szqb.util.crypt.AESCrypt;

/**
 * Created by hengxinyongli on 2017/2/16 0016.
 */

public class UpdataPwdPresenter extends BasePresenter<UpdataPwdContract.View> implements UpdataPwdContract.Presenter {
    public final String UPDATA_PWD = "UpdataPwd";

    @Override
    public void UpdataPwd(String oldPwd, String newPwd) {
        oldPwd = AESCrypt.INSTANCE.aesEncrypt(oldPwd);  //加密密码
        newPwd = AESCrypt.INSTANCE.aesEncrypt(newPwd);  //加密密码
        toSubscribe(HttpManager.getApi().changePwd(oldPwd, newPwd), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.UpdataPwdSuccess();
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
