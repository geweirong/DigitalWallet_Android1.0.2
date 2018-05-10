package com.innext.szqb.ui.lend.presenter

import com.innext.szqb.base.BasePresenter
import com.innext.szqb.http.HttpApi
import com.innext.szqb.http.HttpManager
import com.innext.szqb.http.HttpSubscriber
import com.innext.szqb.ui.lend.bean.AgreeStateBean
import com.innext.szqb.ui.lend.bean.ConfirmLoanResponseBean
import com.innext.szqb.ui.lend.bean.ExpenseDetailBean
import com.innext.szqb.ui.lend.bean.HomeInfoResponseBean
import com.innext.szqb.ui.lend.contract.LendContract
import com.innext.szqb.ui.my.bean.BorrowingRecordBean

/**
 * 首页presenter
 */
class LendPresenter : BasePresenter<LendContract.View>(), LendContract.Presenter {
    companion object {
        const val TYPE_INDEX = "index"
        const val TYPE_STATE = "agree_state"
    }

    override fun loadInfo() {
        toSubscribe(HttpManager.getApi().getinfo(), object :
                HttpSubscriber<HomeInfoResponseBean>() {
            public override fun _onStart() {
                mView.showLoading("加载中...")
            }

            override fun _onNext(homeInfoResponseBean: HomeInfoResponseBean?) {
                //信息回传
                if (homeInfoResponseBean == null) {
                    mView.showErrorMsg("获取信息失败,请稍后重新", TYPE_INDEX)
                } else {
                    mView.infoSuccess(homeInfoResponseBean)
                }
            }

            override fun _onError(message: String) {
                mView.showErrorMsg(message, TYPE_INDEX)
            }

            override fun _onCompleted() {
                mView.stopLoading()
            }
        })
    }

//    override fun updateAssetOrderPool(userid: Int) {
//        toSubscribe(HttpManager.getApi().updateAssetOrder(userid), object : HttpSubscriber<Any>() {
//            public override fun _onStart() {
//                mView.showLoading("加载中...")
//            }
//
//            override fun _onNext(`object`: Any?) {
//                //信息回传
//                mView.updateAssetOrderSuccess()
//            }
//
//            override fun _onError(message: String) {
//                mView.showErrorMsg(message, TYPE_INDEX)
//            }
//
//            override fun _onCompleted() {
//                mView.stopLoading()
//            }
//        })
//    }


    /**
     * 修改同意收取报告费状态接口
     */
    override fun loadStateInfo(){
        toSubscribe(HttpManager.getApi().getStateArgee(), object : HttpSubscriber<AgreeStateBean>(){
            override fun _onStart() {
                mView.showLoading("加载中")
            }

            override fun _onNext(t: AgreeStateBean?) {
              if (t != null){
                  mView.infoAgreeStateSuccess(t)
              }
            }

            override fun _onError(message: String?) {
                mView.showErrorMsg(message, TYPE_STATE)
            }

            override fun _onCompleted() {
                mView.stopLoading()
            }
        })
    }
}
