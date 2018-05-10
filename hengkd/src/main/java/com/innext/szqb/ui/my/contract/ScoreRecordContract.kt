package com.innext.szqb.ui.my.contract

import com.innext.szqb.base.BaseView
import com.innext.szqb.ui.my.bean.ScoreRecordBean

/**
 * @author      : yan
 * @date        : 2017/12/26 16:18
 * @description : 积分记录协议层
 */
interface ScoreRecordContract {
    interface View : BaseView {
        fun getScoreRecordSuccess(bean: ScoreRecordBean, loadType: Int)
    }

    interface Presenter {
        fun getScoreRecord(userId: Int, pageIndex: Int, pageSize: Int, loadType: Int)
    }
}