package com.innext.szqb.ui.mall.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.mall.bean.MallCheackAuthorBean;
import com.innext.szqb.ui.mall.contract.MallContract;

/**
 * Created by HX0010637 on 2018/4/11.
 */

public class MallPresenter extends BasePresenter<MallContract.View> implements MallContract.Presenter{

    @Override
    public void getAuthorInfo() {
       toSubscribe(HttpManager.getApi().getAuthorInfo(), new HttpSubscriber() {
           @Override
           protected void _onStart() {
               mView.showLoading("正在授权...");
           }

           @Override
           protected void _onNext(Object o) {
             mView.getAuthorSuccess();
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
    /*
     *登录商城授权校验
     */
    @Override
    public void getCheackAuthor() {
       toSubscribe(HttpManager.getApi().getCheackAuthorization(), new HttpSubscriber<MallCheackAuthorBean>() {


           @Override
           protected void _onStart() {

           }

           @Override
           protected void _onNext(MallCheackAuthorBean mallAuthorBeanBaseResponse) {
               mView.getCheackAuthorSuccess(mallAuthorBeanBaseResponse);

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
