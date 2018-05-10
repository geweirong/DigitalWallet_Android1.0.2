package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.contract.FeedBackContract;

/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

public class FeedBackPresenter extends BasePresenter<FeedBackContract.View> implements FeedBackContract.Presenter  {

    @Override
    public void feedBack(String content) {
        toSubscribe(HttpManager.getApi().feedBack(content), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }
            @Override
            protected void _onNext(Object o) {
                mView.feedBackSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
