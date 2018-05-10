package com.innext.szqb.ui.my.contract

import com.innext.szqb.base.BaseView
import com.innext.szqb.ui.my.bean.AllScoreBean

/**
 * @author      : yan
 * @date        : 2017/12/26 16:18
 * @description : 我的积分协议层
 */
interface MyScoreContract {
    interface View : BaseView {
        fun getAllScoreSuccess(bean: AllScoreBean?)
    }

    interface Presenter {
        fun getAllScore(userId: Int)
    }
}