package com.innext.szqb.ui.login.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.bean.RegisterCodeBean;
import com.innext.szqb.ui.login.contract.ForgetPwdContract;

/**
 * Created by hengxinyongli on 2017/2/9 0009.
 */

public class ForgetPwdPresenter extends BasePresenter<ForgetPwdContract.View> implements ForgetPwdContract.presenter {
    public final String TYPE_FORGET_PWD = "ForgetPwd";
    @Override
    public void forgetPwd(String phone,String type,String captcha,String type2) {
        toSubscribe(HttpManager.getApi().forgetPwd(phone,type,captcha,type2), new HttpSubscriber<RegisterCodeBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("正在发送...");
            }

            @Override
            protected void _onNext(RegisterCodeBean register) {
                if (register!=null&&register.getItem()!=null){
                    mView.forgetPwdSuccess(register.getItem());
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_FORGET_PWD);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
