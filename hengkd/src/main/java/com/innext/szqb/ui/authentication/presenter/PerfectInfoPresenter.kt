package com.innext.szqb.ui.authentication.presenter

import com.innext.szqb.base.BasePresenter
import com.innext.szqb.http.HttpManager
import com.innext.szqb.http.HttpSubscriber
import com.innext.szqb.ui.authentication.bean.PerfectInfoBean
import com.innext.szqb.ui.authentication.contract.PerfectInfoContract
import com.innext.szqb.ui.my.bean.MoxieApproveBean

/**
 * @author      : yan
 * @date        : 2017/12/29 10:29
 * @description : 完善资料p层
 */
class PerfectInfoPresenter : BasePresenter<PerfectInfoContract.View>(), PerfectInfoContract.Presenter {

    var approvetype = "0"
    //获取信息
    override fun getInfo() {
        toSubscribe(HttpManager.getApi().perfectInfo, object : HttpSubscriber<PerfectInfoBean>() {

            override fun _onStart() = mView.showLoading("正在加载...")

            override fun _onNext(bean: PerfectInfoBean?) { bean?.let(mView::getInfoSuccess) }

            override fun _onError(message: String) = mView.showErrorMsg(message, null)

            override fun _onCompleted() = mView.stopLoading()
        })
    }

    //申请提交
    override fun applyInfo() {
        toSubscribe(HttpManager.getApi().applyInfo(), object : HttpSubscriber<Any>() {

            override fun _onStart() = mView.showLoading("正在提交...")

            override fun _onNext(any: Any?) = mView.applyInfoSuccess()

            override fun _onError(message: String) = mView.showErrorMsg(message, null)

            override fun _onCompleted() = mView.stopLoading()
        })
    }

    //申请魔蝎下单
//    override fun moXieApprove( orderType:String) {
//        approvetype = orderType
//        toSubscribe(HttpManager.getApi().getMoXieApprove(orderType),object :HttpSubscriber<MoxieApproveBean>(){
//            override fun _onStart() = mView.showLoading("")
//
//            override fun _onNext(bean: MoxieApproveBean?) {
//                bean?.let{
//                    mView.moXieApproveSuccess(bean,approvetype )
//                }
//            }
//
//            override fun _onError(message: String?) = mView.showErrorMsg(message,null)
//
//            override fun _onCompleted() = mView.stopLoading()
//        })
//    }
}