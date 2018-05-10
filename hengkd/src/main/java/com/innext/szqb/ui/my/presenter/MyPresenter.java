package com.innext.szqb.ui.my.presenter;

import com.innext.szqb.base.BasePresenter;
import com.innext.szqb.http.HttpManager;
import com.innext.szqb.http.HttpSubscriber;
import com.innext.szqb.ui.my.bean.MoreBean;
import com.innext.szqb.ui.my.bean.MyOrderBean;
import com.innext.szqb.ui.my.bean.QueryVipStateBean;
import com.innext.szqb.ui.my.contract.MyContract;
import com.innext.szqb.util.common.ToastUtil;

/**
 * 我的
 */

public class MyPresenter extends BasePresenter<MyContract.View> implements MyContract.Presenter{

    private static final String TYPE_MEMBER = "vip_state";

    @Override
    public void getInfo() {
        toSubscribe(HttpManager.getApi().getInfo(),new HttpSubscriber<MoreBean>(){

            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(MoreBean moreBean) {
                if (moreBean!=null&&moreBean.getItem()!=null){
                    mView.userInfoSuccess(moreBean.getItem());
                }else{
                    mView.showErrorMsg("数据获取失败，请重新获取",null);
                }
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

    @Override
    public void getShareSuccess(String deviceId,String mobilePhone) {
        toSubscribe(HttpManager.getApi().getShareSuccess(deviceId,mobilePhone), new HttpSubscriber() {
            @Override
            protected void _onStart() {

            }

            @Override
            protected void _onNext(Object o) {

            }

            @Override
            protected void _onError(String message) {

            }

            @Override
            protected void _onCompleted() {

            }
        });
    }

    @Override
    public void getOrderInfo() {
        toSubscribe(HttpManager.getApi().getOrderInfo(), new HttpSubscriber<MyOrderBean>() {
            @Override
            protected void _onStart() {

            }

            @Override
            protected void _onNext(MyOrderBean bean) {
                if (bean.getLink() != null && !bean.getLink().isEmpty()) {
                    mView.myOrderSuccess(bean);
                }else {
                    ToastUtil.showToast("暂无订单");
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

    /**
     * 判断是否是会员
     */
//    @Override
//    public void judgeMember() {
//        toSubscribe(HttpManager.getApi().getVipState(), new HttpSubscriber<QueryVipStateBean>() {
//            @Override
//            protected void _onStart() {
//                mView.showLoading("加载中...");
//            }
//
//            @Override
//            protected void _onNext(QueryVipStateBean queryVipStateBean) {
//                mView.judgeMemberSuccess(queryVipStateBean);
//            }
//
//            @Override
//            protected void _onError(String message) {
//                mView.showErrorMsg("读取会员信息失败",TYPE_MEMBER);
//            }
//
//            @Override
//            protected void _onCompleted() {
//                mView.stopLoading();
//            }
//        });
//    }
}
