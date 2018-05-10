package com.innext.szqb.ui.my.bean;

import java.io.Serializable;

/**
 * Created by HX0010637 on 2018/5/7.
 * 全部账单响应参数
 */

public class AssetRepaymentList implements Serializable{
private int   id; // ,//2,
private int   userId; // , //138375,
private int   assetOrderId; // ,//234034,
private int   repaymentAmount; // ,// 153000,
private int   repaymentedAmount; // ,//0,
private int   repaymentPrincipal; // ,//112500,
private int   repaymentInterest; // ,//37500,
private int   planLateFee; // ,//0,
private int   trueLateFee; // ,//0,
private int   lateFeeApr; // ,//200,
private String   creditRepaymentTime;//1524734930000,
private int   period; // ,//1,
private String   repaymentTime;//: String?,//1524821330000,
private String   repaymentRealTime;//: String?,//null,
private String   lateFeeStartTime;//null,
private String   interestUpdateTime;//: String?,//null,
private int   lateDay; // ,//0,
private String   createdAt;//1524734688000,
private String   updatedAt; //1524734688000,
private int   autoDebitFailTimes; // ,//0,
private int   renewalCount; // ,//0,
private int   status; // ,//21,
private String   remark;//: String?, //null,
private int   collection; // ,//0,
private String   repaymentNo;//: String?,//null,
private String   grantTime;//: String?,//null,
private String   firstRepaymentTime;//: String?, //1524821330000,
private int   fenqiNo;; // ,  //1
    /**
     * 当前订单是否被选中
     */
public Boolean  isSelected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAssetOrderId() {
        return assetOrderId;
    }

    public void setAssetOrderId(int assetOrderId) {
        this.assetOrderId = assetOrderId;
    }

    public int getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(int repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public int getRepaymentedAmount() {
        return repaymentedAmount;
    }

    public void setRepaymentedAmount(int repaymentedAmount) {
        this.repaymentedAmount = repaymentedAmount;
    }

    public int getRepaymentPrincipal() {
        return repaymentPrincipal;
    }

    public void setRepaymentPrincipal(int repaymentPrincipal) {
        this.repaymentPrincipal = repaymentPrincipal;
    }

    public int getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(int repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    public int getPlanLateFee() {
        return planLateFee;
    }

    public void setPlanLateFee(int planLateFee) {
        this.planLateFee = planLateFee;
    }

    public int getTrueLateFee() {
        return trueLateFee;
    }

    public void setTrueLateFee(int trueLateFee) {
        this.trueLateFee = trueLateFee;
    }

    public int getLateFeeApr() {
        return lateFeeApr;
    }

    public void setLateFeeApr(int lateFeeApr) {
        this.lateFeeApr = lateFeeApr;
    }

    public String getCreditRepaymentTime() {
        return creditRepaymentTime;
    }

    public void setCreditRepaymentTime(String creditRepaymentTime) {
        this.creditRepaymentTime = creditRepaymentTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(String repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public String getRepaymentRealTime() {
        return repaymentRealTime;
    }

    public void setRepaymentRealTime(String repaymentRealTime) {
        this.repaymentRealTime = repaymentRealTime;
    }

    public String getLateFeeStartTime() {
        return lateFeeStartTime;
    }

    public void setLateFeeStartTime(String lateFeeStartTime) {
        this.lateFeeStartTime = lateFeeStartTime;
    }

    public String getInterestUpdateTime() {
        return interestUpdateTime;
    }

    public void setInterestUpdateTime(String interestUpdateTime) {
        this.interestUpdateTime = interestUpdateTime;
    }

    public int getLateDay() {
        return lateDay;
    }

    public void setLateDay(int lateDay) {
        this.lateDay = lateDay;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAutoDebitFailTimes() {
        return autoDebitFailTimes;
    }

    public void setAutoDebitFailTimes(int autoDebitFailTimes) {
        this.autoDebitFailTimes = autoDebitFailTimes;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(int renewalCount) {
        this.renewalCount = renewalCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getRepaymentNo() {
        return repaymentNo;
    }

    public void setRepaymentNo(String repaymentNo) {
        this.repaymentNo = repaymentNo;
    }

    public String getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(String grantTime) {
        this.grantTime = grantTime;
    }

    public String getFirstRepaymentTime() {
        return firstRepaymentTime;
    }

    public void setFirstRepaymentTime(String firstRepaymentTime) {
        this.firstRepaymentTime = firstRepaymentTime;
    }

    public int getFenqiNo() {
        return fenqiNo;
    }

    public void setFenqiNo(int fenqiNo) {
        this.fenqiNo = fenqiNo;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
