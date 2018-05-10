package com.innext.szqb.ui.authentication.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.authentication.bean.GetWorkInfoBean;
import com.innext.szqb.ui.authentication.contract.WorkDetailContract;

import java.util.Map;

/**
 * Created by hengxinyongli on 2017/2/17 0017.
 */

public class WorkDetailPresenter extends BasePresenter<WorkDetailContract.View> implements WorkDetailContract.Presenter {
    public final String TYPE_GET = "getDetail";
    public final String TYPE_SAVE = "saveDetail";
    @Override
    public void getWorkInfo() {
        toSubscribe(HttpManager.getApi().getWorkInfo(),new HttpSubscriber<GetWorkInfoBean>(){

            @Override
            protected void _onStart() {
                mView.showLoading("正在加载...");
            }

            @Override
            protected void _onNext(GetWorkInfoBean getWorkInfoBean) {
                if (getWorkInfoBean!=null&&getWorkInfoBean.getItem()!=null){
                    mView.getWorkInfoSuccess(getWorkInfoBean.getItem());
                }else{
                    mView.showErrorMsg("信息获取失败,请重试",TYPE_GET);
                }

            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_GET);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void saveWorkInfo(Map<String, String> params) {
        toSubscribe(HttpManager.getApi().saveWorkInfo(params), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("保存中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.saveWorkInfoSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_SAVE);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
