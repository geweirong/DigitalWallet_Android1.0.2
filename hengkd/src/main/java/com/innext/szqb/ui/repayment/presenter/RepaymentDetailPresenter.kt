package com.innext.szqb.ui.repayment.presenter

import com.innext.szqb.base.BasePresenter
import com.innext.szqb.bean.BaseResponse
import com.innext.szqb.http.HttpManager
import com.innext.szqb.http.HttpSubscriber
import com.innext.szqb.ui.my.bean.BorrowingRecordBean
import com.innext.szqb.ui.repayment.bean.RepaymentDetailBean
import com.innext.szqb.ui.repayment.contract.RepaymentDetailContract

/**
 * @author      : HX0010239ANDROID
 * @date        : 2017/12/17 16:10
 * @description : 还款页面明细 P 层
 */
class RepaymentDetailPresenter : BasePresenter<RepaymentDetailContract.View>(), RepaymentDetailContract.Presenter {
    override fun getDetail(poolId:String,assetOrderId: String) {
        toSubscribe(HttpManager.getApi().getLoanDetail(poolId,assetOrderId), object : HttpSubscriber<RepaymentDetailBean>() {
            override fun _onStart() = mView.showLoading("正在加载...")

            override fun _onNext(bean: RepaymentDetailBean?) = mView.showSuccess(bean)

            override fun _onError(message: String?) = mView.showErrorMsg(message, null)

            override fun _onCompleted() = mView.stopLoading()
        })
    }
}