package com.innext.szqb.ui.my.contract

import com.innext.szqb.base.BaseView
import com.innext.szqb.ui.my.bean.MoxieApproveStateBean

/**
 * Created by HX0010204NET on 2018/1/3.
 */
interface ApproveContract {
    interface View:BaseView{
        fun getApproveStateSuccess(moxieApproveStateBean:MoxieApproveStateBean)
    }

    interface Presenter{
        fun getApproveState(orderNo:String)
    }
}