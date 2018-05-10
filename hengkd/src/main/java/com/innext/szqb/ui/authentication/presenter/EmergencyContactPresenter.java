package com.innext.szqb.ui.authentication.presenter;
/**
 * Created by hengxinyongli on 2017/2/16 0016.
 */

import android.util.Log;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.authentication.bean.GetRelationBean;
import com.innext.szqb.ui.authentication.contract.EmergencyContactContract;

/**
 * Created by hengxinyongli at 2017/2/16 0016
 */
public class EmergencyContactPresenter extends BasePresenter<EmergencyContactContract.View> implements EmergencyContactContract.Presenter {
    public final String TYPE_GET_CONTACT = "getContact";
    public final String TYPE_SAVE_CONTACT = "saveContact";
    @Override
    public void getContacts() {
        toSubscribe(HttpManager.getApi().getContacts(), new HttpSubscriber<GetRelationBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(GetRelationBean getRelationBean) {
                if (null!=getRelationBean&&null!=getRelationBean.getItem()){
                    mView.getContactsSuccess(getRelationBean.getItem());
                }else{
                    mView.showErrorMsg("信息获取失败，请重试",TYPE_GET_CONTACT);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_GET_CONTACT);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void saveContacts(String type, String mobile, String name, String relation_spare, String mobile_spare, String name_spare) {
        Log.e("这里是日志","输出日志信息打印日志数据type===="+type+"---mobile="+mobile+"---name=="+name+"---relation_spare="+relation_spare+"---mobile_spare="+mobile_spare+"---name_spare=="+name_spare);
            toSubscribe(HttpManager.getApi().saveContacts(type, mobile, name, relation_spare, mobile_spare, name_spare), new HttpSubscriber() {
                @Override
                protected void _onStart() {
                    mView.showLoading("保存中...");
                }

                @Override
                protected void _onNext(Object o) {
                    mView.saveContactsSuccess();
                }

                @Override
                protected void _onError(String message) {
                    mView.showErrorMsg(message,TYPE_SAVE_CONTACT);
                }

                @Override
                protected void _onCompleted() {
                    mView.stopLoading();
                }
            });
    }
}
