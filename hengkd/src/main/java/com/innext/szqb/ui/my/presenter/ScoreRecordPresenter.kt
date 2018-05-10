package com.innext.szqb.ui.my.presenter

import com.innext.szqb.base.BasePresenter
import com.innext.szqb.http.HttpManager
import com.innext.szqb.http.HttpSubscriber
import com.innext.szqb.ui.my.bean.ScoreRecordBean
import com.innext.szqb.ui.my.contract.ScoreRecordContract

/**
 * @author      : yan
 * @date        : 2017/12/26 19:20
 * @description : 积分记录p层
 */
class ScoreRecordPresenter : BasePresenter<ScoreRecordContract.View>(), ScoreRecordContract.Presenter {

    override fun getScoreRecord(userId: Int, pageIndex: Int, pageSize: Int, loadType: Int) {
        toSubscribe(HttpManager.getApi().getScoreRecord(userId, pageIndex, pageSize), object : HttpSubscriber<ScoreRecordBean>() {
            override fun _onStart() = mView.showLoading("加载中...")

            override fun _onNext(bean: ScoreRecordBean?) { bean?.let { mView.getScoreRecordSuccess(it, loadType) } }

            override fun _onError(message: String?) = mView.showErrorMsg(message, null)

            override fun _onCompleted() = mView.stopLoading()

        })
    }
}