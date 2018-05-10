package com.innext.szqb.ui.repayment.contract

import com.innext.szqb.base.BaseView
import com.innext.szqb.ui.my.bean.BorrowingRecordBean
import com.innext.szqb.ui.repayment.bean.RepaymentDetailBean

/**
 * @author      : HX0010239ANDROID
 * @date        : 2017/12/17 16:08
 * @description : 还款明细
 */
interface RepaymentDetailContract {
    interface Presenter {
        fun getDetail(poolId :String,assetOrderId: String)
    }

    interface View : BaseView {
        fun showSuccess(bean: RepaymentDetailBean?)
    }
}