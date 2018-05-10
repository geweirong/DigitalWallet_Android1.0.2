package com.innext.szqb.ui.my.presenter

import com.innext.szqb.base.BasePresenter
import com.innext.szqb.http.HttpManager
import com.innext.szqb.http.HttpSubscriber
import com.innext.szqb.ui.my.bean.MoxieApproveStateBean
import com.innext.szqb.ui.my.contract.ApproveContract
import com.innext.szqb.util.common.ToastUtil

/**
 * Created by HX0010204NET on 2018/1/3.
 */
class ApprovePresenter: BasePresenter<ApproveContract.View>(),ApproveContract.Presenter{

    override fun getApproveState(orderNo:String) {
        toSubscribe(HttpManager.getApi().getMoXieApproveState(orderNo),object :HttpSubscriber<MoxieApproveStateBean>(){
            override fun _onStart() {

            }

            override fun _onNext(moxieApproveStateBean: MoxieApproveStateBean) {
                mView.getApproveStateSuccess(moxieApproveStateBean)
            }

            override fun _onError(message: String?) {
                ToastUtil.showToast(message)
            }

            override fun _onCompleted() {

            }

        })
    }

}