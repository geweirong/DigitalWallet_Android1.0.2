package com.innext.szqb.ui.repayment.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.ui.my.bean.BorrowingRecordBean;
import com.innext.szqb.ui.repayment.bean.RepayMentInfoBean;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.repayment.contract.RepaymentContract;

/**
 * 还款
 */

public class RePaymentPresenter extends BasePresenter<RepaymentContract.View> implements RepaymentContract.Presenter {
    /*
      * 获取延期还款信息
      */
    @Override
    public void gteRepaymentInfo(String assetId) {
        toSubscribe(HttpManager.getApi().gteRepaymentInfo(assetId), new HttpSubscriber<RepayMentInfoBean>() {
            @Override
            protected void _onStart() {

            }

            @Override
            protected void _onNext(RepayMentInfoBean repayMentInfoBean) {
                if (repayMentInfoBean != null) {
                    mView.repaymentInfoSuccess(repayMentInfoBean);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,"2");
            }

            @Override
            protected void _onCompleted() {

            }
        });
    }

    @Override
    public void getPaymentStatus(String assetOrderId) {
        toSubscribe(HttpManager.getApi().getPaymentStatus(assetOrderId), new HttpSubscriber<RepayMentInfoBean>() {
            @Override
            protected void _onStart() {

            }

            @Override
            protected void _onNext(RepayMentInfoBean repayMentInfoBean) {
              mView.repaymentInfoStatusSuccess(repayMentInfoBean);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,"3");
            }

            @Override
            protected void _onCompleted() {

            }
        });
    }

    @Override
    public void getListAssetRepayment(String assetOrderId) {
        toSubscribe(HttpManager.getApi().getListAssetRepayment(assetOrderId), new HttpSubscriber<BorrowingRecordBean>() {
            @Override
            protected void _onStart() {

            }

            @Override
            protected void _onNext(BorrowingRecordBean bean) {
              mView.showListAssetRepaymentSuccess(bean);
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
