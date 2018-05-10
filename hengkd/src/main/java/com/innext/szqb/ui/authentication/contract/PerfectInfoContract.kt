package com.innext.szqb.ui.authentication.contract

import com.innext.szqb.base.BaseView
import com.innext.szqb.ui.authentication.bean.PerfectInfoBean
import com.innext.szqb.ui.my.bean.MoxieApproveBean

/**
 * @author      : yan
 * @date        : 2017/12/29 10:17
 * @description : 完善资料协议
 */
interface PerfectInfoContract {
    interface View : BaseView {
        fun getInfoSuccess(bean: PerfectInfoBean)
        fun applyInfoSuccess()
//        fun moXieApproveSuccess(bean:MoxieApproveBean, approveType: String)
    }

    interface Presenter {
        fun getInfo()
        fun applyInfo()
//        fun moXieApprove(orderType: String)
    }

}