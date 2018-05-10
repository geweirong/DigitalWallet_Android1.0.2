package com.innext.szqb.ui.repayment.presenter

import com.innext.szqb.base.BasePresenter
import com.innext.szqb.http.HttpManager
import com.innext.szqb.http.HttpSubscriber
import com.innext.szqb.ui.repayment.bean.RepaymentPlanBean
import com.innext.szqb.ui.repayment.contract.RepaymentContract
import com.innext.szqb.ui.repayment.contract.RepaymentPlanContract

/**
 * Created by HX0010637 on 2018/4/28.
 */
class RepaymentPlayPresenter :BasePresenter<RepaymentPlanContract.View>(),RepaymentPlanContract.Presenter{
    override fun getRepaymentPlanList(borrowAmount: String, productNo: String) {
        toSubscribe(HttpManager.getApi().getPlanList(borrowAmount,productNo), object : HttpSubscriber<RepaymentPlanBean> (){
            override fun _onStart() = mView.showLoading("加载中...")

            override fun _onNext(t: RepaymentPlanBean?) {
                t?.let { mView.getRepaymentPlanListSuccess(it) }
            }

            override fun _onError(message: String?) = mView.showErrorMsg(message, null)

            override fun _onCompleted() = mView.stopLoading()

        })
    }

    override fun getRepaymentPlan(assetOrderId: String) {
       toSubscribe(HttpManager.getApi().getPlan(assetOrderId), object : HttpSubscriber<RepaymentPlanBean> (){
           override fun _onStart() = mView.showLoading("加载中...")

           override fun _onNext(t: RepaymentPlanBean?) {
               t?.let { mView.getRepaymentPlanSuccess(it) }
           }

           override fun _onError(message: String?) = mView.showErrorMsg(message, null)

           override fun _onCompleted() = mView.stopLoading()
       })
    }

}