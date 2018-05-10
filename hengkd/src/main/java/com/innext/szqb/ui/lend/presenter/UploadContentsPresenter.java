package com.innext.szqb.ui.lend.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.lend.contract.UploadContentsContract;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */
public class UploadContentsPresenter extends BasePresenter<UploadContentsContract.View> implements UploadContentsContract.Presenter {
    //1短信，2app，3通讯录
    public String TYPE_SMS = "1";
    public String TYPE_APP = "2";
    public String TYPE_CONTACT = "3";
    @Override
    public void toUploadContents(final String type, String data) {
        toSubscribe(HttpManager.getApi().upLoadContents(type, data), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.uploadSuccess(type);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,type);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
