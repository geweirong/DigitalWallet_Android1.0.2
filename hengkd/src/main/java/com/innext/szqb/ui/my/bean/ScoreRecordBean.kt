package com.innext.szqb.ui.my.bean

/**
 * @author      : yan
 * @date        : 2017/12/26 19:17
 * @description : 积分记录bean
 */
data class ScoreRecordBean(
		var pageIndex: Int, //1
		var record: List<Record>,
		var pageSize: Int, //2
		var totalPageNum: Int, //107
		var recordData: List<RecordData>
)

data class Record(
//		var payIntegral: Int, //14140
//		var incomeIntegral: Int, //71300
		var mothStr: String? //2017-12
)

data class RecordData(
//        //下面是辅助新增字段
        var payIntegral: Int, //14140
        var incomeIntegral: Int, //71300
        var dateStr: String?, //2017-12

       var usage : String?, // "认证完成"
       var amount : Int, //168
       var finished_at : String? //"2018-04-23 10:18"

)