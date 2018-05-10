package com.innext.szqb.ui.authentication.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.bean.BaseResponse;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.authentication.bean.CreditCardBean;
import com.innext.szqb.ui.authentication.contract.CreditCardContract;

/**
 * Created by HX0010637 on 2018/5/2.
 */

public class CreditCardPresenter extends BasePresenter<CreditCardContract.View> implements CreditCardContract.Presenter{
    @Override
    public void getEmailInfo() {
        toSubscribe(HttpManager.getApi().getEmailInfo(), new HttpSubscriber<CreditCardBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(CreditCardBean bean) {
              mView.getEmailSuccess(bean.getEmailList());
            }

            @Override
            protected void _onError(String message) {
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void getSaveCredit(String taskid) {
        toSubscribe(HttpManager.getApi().saveCreditCard(taskid), new HttpSubscriber<BaseResponse>() {
            @Override
            protected void _onStart() {

            }

            @Override
            protected void _onNext(BaseResponse o) {
               mView.getSaveCreditCardSuccess();
            }

            @Override
            protected void _onError(String message) {

            }

            @Override
            protected void _onCompleted() {

            }
        });
    }
}
