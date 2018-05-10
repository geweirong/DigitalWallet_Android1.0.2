package com.innext.szqb.ui.repayment.bean;

/**
 * author：wangzuzhen on 2016/9/21 0021 13:18
 */
public class RepaymentListBean {
    /*
    "period": 1,
    "late_fee": "240.00",
    "plan_fee_time": "2017-12-02",
    "lateDay": 12,
    "url": "http://172.30.2.123:8080/hkd_api/repayment/repay-choose.do?id=2227",
    "asset_order_id": 2227,
    "principal": "1000.00",
    "receipts": "875.00",
    "counter_fee": "180.00",
    "id": 363,
    "text_tip": "<font color='#999999' size='3'>已还款</font>",
    "interests": 0,
    "debt": "1420.00",
    "status": 30
     */

    private Long asset_order_id;
    private Long id;
    private String debt;//实际欠款金额
    private String principal;//借款本金
    private String counter_fee;//服务费
    private String receipts;//实际到账金额
    private String interests;//利息
    private String late_fee;//逾期还款滞纳金
    private String plan_fee_time;//应还日期
    private String text_tip;//文本提示
    private String url;//跳转h5页面url地址
    private String period;//每条还款计划对应的期数
    private String lateDay;//逾期天数,默认值0
    private String count;//总期数
    private int status;//还款状态
    private String loanInterests;   //服务费

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCounter_fee() {
        return counter_fee;
    }

    public void setCounter_fee(String counter_fee) {
        this.counter_fee = counter_fee;
    }

    public String getReceipts() {
        return receipts;
    }

    public void setReceipts(String receipts) {
        this.receipts = receipts;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(String late_fee) {
        this.late_fee = late_fee;
    }

    public String getPlan_fee_time() {
        return plan_fee_time;
    }

    public void setPlan_fee_time(String plan_fee_time) {
        this.plan_fee_time = plan_fee_time;
    }

    public String getText_tip() {
        return text_tip;
    }

    public void setText_tip(String text_tip) {
        this.text_tip = text_tip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLateDay() {
        return lateDay;
    }

    public void setLateDay(String lateDay) {
        this.lateDay = lateDay;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Long getAsset_order_id() {
        return asset_order_id;
    }

    public void setAsset_order_id(Long asset_order_id) {
        this.asset_order_id = asset_order_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLoanInterests() {
        return loanInterests;
    }

    public void setLoanInterests(String loanInterests) {
        this.loanInterests = loanInterests;
    }
}
