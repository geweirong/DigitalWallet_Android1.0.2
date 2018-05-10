package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.bean.BorrowingRecordBean;
import com.innext.szqb.ui.my.contract.BorrowingRecordContract;


/**
 * Created by hengxinyongli on 2017/8/28.
 */

public class BorrowingRecordPresenter extends BasePresenter<BorrowingRecordContract.View> implements BorrowingRecordContract.presenter  {
    @Override
    public void getBorrowingDetail(String poolId) {
        toSubscribe(HttpManager.getApi().getLoanRecordDetail(poolId), new HttpSubscriber<BorrowingRecordBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(BorrowingRecordBean borrowingRecordBean) {
                if (borrowingRecordBean.getAssetRepaymentList() != null && borrowingRecordBean.getAssetRepaymentList().size() > 0) {
                    mView.getBorrowingDetail(borrowingRecordBean);
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


}
