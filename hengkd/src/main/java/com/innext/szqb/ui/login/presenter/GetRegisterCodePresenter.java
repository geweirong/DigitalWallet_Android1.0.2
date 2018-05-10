package com.innext.szqb.ui.login.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.login.bean.RegisterCodeBean;
import com.innext.szqb.ui.login.contract.GetRegisterCodeContract;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */

public class GetRegisterCodePresenter extends BasePresenter<GetRegisterCodeContract.View> implements GetRegisterCodeContract.Presenter {
    public final String TYPE_REGISTER_CODE = "registerCode";
    @Override
    public void getRegisterCode(String phone,String captcha,String type) {
        toSubscribe(HttpManager.getApi().getRegisterCode(phone,captcha,type), new HttpSubscriber<RegisterCodeBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("正在验证...");
            }

            @Override
            protected void _onNext(RegisterCodeBean register) {
                if (register!=null&&register.getItem()!=null){
                    mView.getCodeSuccess(register.getItem());
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_REGISTER_CODE);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,code);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
