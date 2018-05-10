package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.bean.TransactionBean;
import com.innext.szqb.ui.my.contract.TransactionRecordContract;

/**
 * Created by hengxinyongli on 2017/2/16 0016.
 * 描述：
 */

public class TransactionRecordPresenter extends BasePresenter<TransactionRecordContract.View> implements TransactionRecordContract.presenter {
    @Override
    public void recordRequest(String page, String pageSize) {
        toSubscribe(HttpManager.getApi().recordRequest(page, pageSize), new HttpSubscriber<TransactionBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(TransactionBean transactionBean) {
                if (transactionBean.getItem() != null) {
                    mView.recordSuccess(transactionBean.getItem(),transactionBean.getLink_url());
                } else {
                    mView.showErrorMsg("数据获取失败，请重新获取", null);
                }
            }


            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,"");

            }

            @Override
            protected void _onCompleted() {

            }
        });
    }

    @Override
    public void getLoanRecordRequest(String userId,String pageNum,String pageSize) {
        toSubscribe(HttpManager.getApi().getLoanRecord(userId,pageNum,pageSize), new HttpSubscriber<TransactionBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(TransactionBean transactionBean) {
                if (transactionBean.getItem() != null) {
                    mView.recordSuccess(transactionBean.getItem(),transactionBean.getLink_url());
                } else {
                    mView.showErrorMsg("数据获取失败，请重新获取", null);
                }
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
