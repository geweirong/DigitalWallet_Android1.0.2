package com.innext.szqb.ui.lend.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.lend.bean.ConfirmLoanBean;
import com.innext.szqb.ui.lend.bean.ConfirmLoanResponseBean;
import com.innext.szqb.ui.lend.contract.LendConfirmLoanContract;

public class LendConfirmLoanPresenter extends BasePresenter<LendConfirmLoanContract.View> implements LendConfirmLoanContract.Presenter{
    public final String  TYPE_LOAN = "toLoan";
    @Override
    public void toLoan(String money, String period, String loan_periods) {
        toSubscribe(HttpManager.getApi().toLoan(money, period,loan_periods), new HttpSubscriber<ConfirmLoanResponseBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("验证中...");
            }

            @Override
            protected void _onNext(ConfirmLoanResponseBean confirmLoanResponseBean) {
                if (confirmLoanResponseBean!=null&&confirmLoanResponseBean.getItem()!=null){
                    mView.toLoanSuccess(confirmLoanResponseBean.getItem());
                }else{
                    mView.showErrorMsg("获取验证信息失败,请稍后重新",TYPE_LOAN);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_LOAN);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
    @Override
    public void borrowData() {
        toSubscribe(HttpManager.getApi().borrowData(), new HttpSubscriber<ConfirmLoanBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(ConfirmLoanBean confirmLoanBean) {
                if (confirmLoanBean != null)
                    mView.borrowDataSuccess(confirmLoanBean);
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
}
