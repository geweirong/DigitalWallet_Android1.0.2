package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.bean.InvitationDataInfo;
import com.innext.szqb.ui.my.contract.MyInvitationContract;

/**
 * Created by HX0010637 on 2018/1/29.
 */

public class MyInvitationCodePresenter extends BasePresenter<MyInvitationContract.View> implements MyInvitationContract.presenter{
    @Override
    public void getCode() {
        toSubscribe(HttpManager.getApi().getCode(), new HttpSubscriber<InvitationDataInfo>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(InvitationDataInfo invitationBean) {
//                if (invitationBean != null && invitationBean.getData() != null){
                    mView.myCodeSuccess(invitationBean);
//                }else{
//                    mView.showErrorMsg("数据获取失败，请重新获取",null);
//                }
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
