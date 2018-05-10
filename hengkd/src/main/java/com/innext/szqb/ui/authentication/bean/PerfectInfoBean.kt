package com.innext.szqb.ui.authentication.bean

/**
 * @author      : yan
 * @date        : 2017/12/29 10:19
 * @description : 完善资料data层数据
 */
data class PerfectInfoBean(
		var mustBeCount: Int, //35
		var commitStatus: Int,	//资料是否提交：0，未提交，1，已提交
		var VerificationList: List<Verification>
)

data class Verification(
		var logoImg: String?, //http://10.32.1.6:28080/hkd_api/common/web/images/certification/icon_personal_info.png
		var code: String?, //personalInfo
		var mustBe: Int, //是否必填项，0非，1是
		var title: String?, //个人信息
		var status: Int, //认证状态：芝麻信用、运营商：1，未认证，2，已认证；其他：0，未认证，1，已认证
        var url: String? //跳转路径
)