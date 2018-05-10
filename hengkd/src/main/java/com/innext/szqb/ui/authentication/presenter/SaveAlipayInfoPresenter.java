package com.innext.szqb.ui.authentication.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.authentication.bean.SaveInfoBean;
import com.innext.szqb.ui.authentication.contract.SaveAlipayInfoContract;

/**
 * Created by hengxinyongli on 2017/2/20 0020.
 */

public class SaveAlipayInfoPresenter extends BasePresenter<SaveAlipayInfoContract.View> implements SaveAlipayInfoContract.Presenter {
    public final String TYPE_ALIPAY_INFO = "saveAlipayInfo";
    @Override
    public void toSaveInfo(String data) {
        toSubscribe(HttpManager.getApi().saveAlipayInfo(data), new HttpSubscriber<SaveInfoBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(SaveInfoBean saveInfoBean) {
                mView.saveInfoSuccess(saveInfoBean.getMessage());
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_ALIPAY_INFO);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
