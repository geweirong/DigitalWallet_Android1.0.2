package com.innext.szqb.ui.lend.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 2016/9/22.
 */
public class ConfirmLoanBean implements Serializable{
    private String bankName;
    private String firstUserBankUrl;//银行卡链接
    private String cardNoLastFour;//银行卡后4位
    private String protocolMsg;
    private Boolean realPayPwdStatus;
    private String protocolUrl;//借款协议
    private String cardNo;
    private String withholdingUrl;
    private String platformServiceUrl;//扣款协议
    private ArrayList<String> loanUsage;//借款用途
    private ArrayList<ProductList> productList;
    private int selectLoanUsePos;    //选择借款用途的position
    private int loadPeriod;
    public ArrayList<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductList> productList) {
        this.productList = productList;
    }
    public Boolean getRealPayPwdStatus() {
        return realPayPwdStatus;
    }

    public void setRealPayPwdStatus(Boolean realPayPwdStatus) {
        this.realPayPwdStatus = realPayPwdStatus;
    }
    public int getLoadPeriod() {
        return loadPeriod;
    }

    public void setLoadPeriod(int loadPeriod) {
        this.loadPeriod = loadPeriod;
    }

    @Override
    public String toString() {
        return "ConfirmLoanBean{" +
                ", bankName='" + bankName + '\'' +
                ", firstUserBankUrl='" + firstUserBankUrl + '\'' +
                ", cardNoLastFour='" + cardNoLastFour + '\'' +
                ", protocolMsg='" + protocolMsg + '\'' +
                ", realPayPwdStatus='" + realPayPwdStatus + '\'' +
                ", protocolUrl='" + protocolUrl + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", withholdingUrl='" + withholdingUrl + '\'' +
                ", platformServiceUrl='" + platformServiceUrl + '\'' +
                '}';
    }

    public String getbankName() {
        return bankName;
    }

    public void setbankName(String bankName) {
        this.bankName = bankName;
    }

    public String getfirstUserBankUrl() {
        return firstUserBankUrl;
    }

    public void setfirstUserBankUrl(String firstUserBankUrl) {
        this.firstUserBankUrl = firstUserBankUrl;
    }
    public int getSelectLoanUsePos() {
        return selectLoanUsePos;
    }

    public void setSelectLoanUsePos(int selectLoanUsePos) {
        this.selectLoanUsePos = selectLoanUsePos;
    }

    public String getcardNoLastFour() {
        return cardNoLastFour;
    }

    public void setcardNoLastFour(String cardNoLastFour) {
        this.cardNoLastFour = cardNoLastFour;
    }

    public String getprotocolMsg() {
        return protocolMsg;
    }

    public void setprotocolMsg(String protocolMsg) {
        this.protocolMsg = protocolMsg;
    }
    public String getprotocolUrl() {
        return protocolUrl;
    }

    public void setprotocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }

    public String getcardNo() {
        return cardNo;
    }

    public void setcardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getwithholdingUrl() {
        return withholdingUrl;
    }

    public void setwithholdingUrl(String withholdingUrl) {
        this.withholdingUrl = withholdingUrl;
    }

    public String getplatformServiceUrl() {
        return platformServiceUrl;
    }

    public void setplatformServiceUrl(String platformServiceUrl) {
        this.platformServiceUrl = platformServiceUrl;
    }

    public ArrayList<String> getLoanUse() {
        return loanUsage;
    }

    public void setLoanUse(ArrayList<String> loanUse) {
        this.loanUsage = loanUse;
    }
}
