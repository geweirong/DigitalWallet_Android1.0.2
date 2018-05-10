package com.innext.szqb.ui.my.presenter

import com.innext.szqb.base.BasePresenter
import com.innext.szqb.http.HttpManager
import com.innext.szqb.http.HttpSubscriber
import com.innext.szqb.ui.my.bean.AllScoreBean
import com.innext.szqb.ui.my.contract.MyScoreContract

/**
 * @author      : yan
 * @date        : 2017/12/26 16:35
 * @description : 我的积分p层
 */
class MyScorePresenter : BasePresenter<MyScoreContract.View>(), MyScoreContract.Presenter {
    /**
     * 获取所有积分
     */
    override fun getAllScore(userId: Int) {
        toSubscribe(HttpManager.getApi().getAllScore(userId), object : HttpSubscriber<AllScoreBean>() {
            override fun _onStart() = mView.showLoading("加载中...")

            override fun _onNext(bean: AllScoreBean?) = mView.getAllScoreSuccess(bean)

            override fun _onError(message: String?) = mView.showErrorMsg(message, null)

            override fun _onCompleted() = mView.stopLoading()
        })
    }

}