package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.contract.UploadPOIContract;

/**
 * Created by hengxinyongli on 2017/2/13 0013.
 */

public class UploadPOIPresenter extends BasePresenter<UploadPOIContract.View> implements UploadPOIContract.Presenter {
    public final String TYPE_UPLOAD_POI = "uploadPOI";
    @Override
    public void uploadPOI(String longitude, String latitude, String address, String time) {
        toSubscribe(HttpManager.getApi().uploadLocation(longitude, latitude, address, time)
                , new HttpSubscriber() {
                    @Override
                    protected void _onStart() {
                        mView.showLoading("");
                    }

                    @Override
                    protected void _onNext(Object o) {
                        mView.uploadPOISuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showErrorMsg(message,TYPE_UPLOAD_POI);
                    }

                    @Override
                    protected void _onCompleted() {
                        mView.stopLoading();
                    }
                });
    }
}
