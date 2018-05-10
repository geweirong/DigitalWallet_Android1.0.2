package com.innext.szqb.ui.my.bean

import com.innext.szqb.ui.authentication.bean.SelectPicBean

/**
 * @author      : yan
 * @date        : 2017/12/28 14:04
 * @description : 央行征信返回数据
 */
data class CBResponse(var item: CBResponseItem)

data class CBResponseItem(
		var data: List<SelectPicBean>,
		var max_pictures: String?, //待定
		var title: String?, //央行个人征信
		var notice: String? //获取个人征信报告的方式：1.个人征信网:https://ipcrs.pbccrc.org.cn/;①去官网注册个账号;②登录查询自己的个人信用报告;③截图保存至手机相册后上传;2.线下银行打印; ①前往银行柜台咨询; ②打印一份个人信用报告; ③直接拍照上传或者保存至手机相册后上传;3.如遇问题可联系客服:
)