package com.innext.szqb.comm;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.base.BaseView;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.bean.RegisterCodeBean;

/**
 * Created by hengxinyongli at 2017/11/17 0015
 */

public class CommPresenter extends BasePresenter<BaseView> implements CommContract.Presenter {
    public final String TYPE_REGISTER_CODE = "verifyCode";

    @Override
    public void getVerifyCode(String phone, String captcha, String type) {
        toSubscribe(HttpManager.getApi().getRegisterCode(phone, captcha, type), new HttpSubscriber<RegisterCodeBean>() {
            @Override
            protected void _onStart() {
//                mView.showLoading("正在验证...");
            }

            @Override
            protected void _onNext(RegisterCodeBean register) {
                if (register != null && register.getItem() != null) {
//                    mView.getCodeSuccess(register.getItem());
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, TYPE_REGISTER_CODE);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
//                mView.showErrorMsg(message, code);
            }

            @Override
            protected void _onCompleted() {
//                mView.stopLoading();
            }
        });
    }
}