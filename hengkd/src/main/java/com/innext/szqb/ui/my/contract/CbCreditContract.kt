package com.innext.szqb.ui.my.contract

import com.innext.szqb.base.BaseView
import com.innext.szqb.ui.authentication.bean.SelectPicBean
import com.innext.szqb.ui.my.bean.CBResponseItem

/**
 * @author      : yan
 * @date        : 2017/12/26 16:18
 * @description : 央行征信协议层
 */
interface CbCreditContract {
    interface View : BaseView {
        fun getImageListSuccess(bean: CBResponseItem)
        fun upLoadImgSuccess(bean: SelectPicBean)
    }

    interface Presenter {
        fun getImageList()
        fun uploadImage(info: SelectPicBean)
    }
}