package com.innext.szqb.ui.lend.bean

/**
 * Created by HX0010204NET on 2018/1/16.
 */
data class HomeInfoResponseBean(
        var amount_days_list: AmountDaysListBean,
        var statusMap: StatusMapBean?,
        var user_loan_log_list: List<String>,
        var today_last_amount: String?, //123400
        var index_images: List<IndexImagesBean>?,
        var maxAmount: Int,
        var repay_info: RepayInfo?,
        var recommends: Recommends?
)

data class AmountDaysListBean(
        var amountAvailable: Int, //5000000
        var amounts: List<String>?,
        var days: String, //30
        var interests: String //0.18
)

data class StatusMapBean(
        var service_phone: String?, //4001855918
        var risk_status: Int, //8
        val wechat_public: String?,
        var loan_infos: LoanInfosBean?,
        var agree_status: String?,
//        var urgentUrl: String, //加急费链接
        var companyName: String,  // : "恒信永利"
        var commitstatus: Int //0
)

data class LoanInfosBean(
        var lists: List<ListsBean>
)

data class ListsBean(
        var title: String?, //审核失败
        var body: String? //您的申请金额为0,不可大于您的可用额度50请重新申请借款。
)

data class IndexImagesBean(
        var reurl: String, //http://101.132.78.154/hkd_api/gotoAboutIndex
        var sort: String?, //1
        var title: String?, //首页活动三
        var url: String? //http://localhost:8080/hkd_api/common/web/images/index_banner3.png
)
data class RepayInfo(
        var period: Int,  //":1,
        var repayment_amount: Int,  //":159000,
        var is_overdue: Boolean?,  //":true,
        var repayment_time: String?, //":"2018-04-24"
        var asset_order_id : Long
)
data class Recommends(
        var more_link: String,
        var list: List<CompanyContent>
)
data class CompanyContent(
        var title: String?, //公司名称
        var pic_link: String?,//公司logo
        var content: String?, //描述
        var apply_persons: String?,  //已有xxx人申请
        var apply_link: String?,  //点击一键申请时跳转的链接
        var tagline: String?   //3分钟放款，通过率高
)