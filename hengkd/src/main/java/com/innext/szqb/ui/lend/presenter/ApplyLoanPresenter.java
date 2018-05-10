package com.innext.szqb.ui.lend.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.lend.bean.ConfirmLoanBean;
import com.innext.szqb.ui.lend.contract.ApplyLoanContract;
import com.innext.szqb.util.crypt.AESCrypt;


public class ApplyLoanPresenter extends BasePresenter<ApplyLoanContract.View> implements ApplyLoanContract.Presenter {
    @Override
    public void applyLoan(String money,String payPassword, String usageIndex,String productNo) {
        payPassword = AESCrypt.INSTANCE.aesEncrypt(payPassword);  //加密密码
        ///借款金额：单位为分 支付密码  借款用途索引  金融产品编号
        toSubscribe(HttpManager.getApi().applyLoan(money, payPassword, usageIndex, productNo), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("申请中...");
            }
            @Override
            protected void _onNext(Object o) {
                mView.applyLoanSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.applyLoanFaild(code,message);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
