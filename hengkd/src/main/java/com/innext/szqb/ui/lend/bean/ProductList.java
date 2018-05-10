package com.innext.szqb.ui.lend.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HX0010637 on 2018/4/27.
 */

public class ProductList {
    private String feeRate;  // 0,
    private String interestYearRate;  // 0,
    private int maxApplyAmount;  // 0,
    private int minApplyAmount;  // 0,
    private String period;  // 0,
    private String periodType;  // "string",
    private String productNo;  // "string",
    private String productType;  // "string",
    private String status;  // 0,
    private String yearDays;  // 0
    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getInterestYearRate() {
        return interestYearRate;
    }

    public void setInterestYearRate(String interestYearRate) {
        this.interestYearRate = interestYearRate;
    }

    public int getMaxApplyAmount() {
        return maxApplyAmount;
    }

    public void setMaxApplyAmount(int maxApplyAmount) {
        this.maxApplyAmount = maxApplyAmount;
    }

    public int getMinApplyAmount() {
        return minApplyAmount;
    }

    public void setMinApplyAmount(int minApplyAmount) {
        this.minApplyAmount = minApplyAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYearDays() {
        return yearDays;
    }

    public void setYearDays(String yearDays) {
        this.yearDays = yearDays;
    }
}
