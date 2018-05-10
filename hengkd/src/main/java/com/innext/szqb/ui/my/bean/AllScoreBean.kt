package com.innext.szqb.ui.my.bean

/**
 * @author      : yan
 * @date        : 2017/12/26 16:28
 * @description : 所有积分bean
 */
data class AllScoreBean(
		var id: Int, //1
		var userId: Int, //196  用户id
		var avaiableIntegral: Int, //5000   可用积分
		var freezingIntegral: Int, //0  冻结积分
		var totalIntegral: Int, //5000   总积分
        var agreeStatus: Int,  //0：不存在 1：存在
        var reportUrl: String //当agreeStatus=1时该字段会有值
)