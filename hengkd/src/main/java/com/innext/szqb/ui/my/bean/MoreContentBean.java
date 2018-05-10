package com.innext.szqb.ui.my.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：hengxinyongli on 2016/9/21 16:51
 */
public class MoreContentBean implements Parcelable {

    private CardInfos card_info;

    private String card_url;

    private CreditInfoBean credit_info;

    private String invite_code;

    private String phone;

    private String share_body;

    private String share_logo;

    private String share_title;

    private String share_url;

    private String userId;

    private VerifyInfoBean verify_info;
    /**
     * service : {"services_qq":["3356190848","3539967368","3524605396"],"qq_group":"414383376","service_phone":"021-56350010"}
     */

    private ServiceBean service;

    private StatusMap statusMap;
    /** 剩余可借款时间倒计时 */
    private String next_loan_day;
    /** 是否是会员 0：否，1：是 */
    private String member_state;
    /** 会员到期时间 */
    private String expire_time;

    public String getNext_loan_day() {
        return next_loan_day;
    }

    public void setNext_loan_day(String next_loan_day) {
        this.next_loan_day = next_loan_day;
    }

    public CardInfos getCard_info() {
        return card_info;
    }

    public void setCard_info(CardInfos card_info) {
        this.card_info = card_info;
    }

    public String getCard_url() {
        return card_url;
    }

    public void setCard_url(String card_url) {
        this.card_url = card_url;
    }

    public CreditInfoBean getCredit_info() {
        return credit_info;
    }

    public void setCredit_info(CreditInfoBean credit_info) {
        this.credit_info = credit_info;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShare_body() {
        return share_body;
    }

    public void setShare_body(String share_body) {
        this.share_body = share_body;
    }

    public String getShare_logo() {
        return share_logo;
    }

    public void setShare_logo(String share_logo) {
        this.share_logo = share_logo;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public VerifyInfoBean getVerify_info() {
        return verify_info;
    }

    public void setVerify_info(VerifyInfoBean verify_info) {
        this.verify_info = verify_info;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

//    public String getMember_state() {
//        return member_state;
//    }

//    public void setMember_state(String member_state) {
//        this.member_state = member_state;
//    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public static class CardInfos implements Serializable {
        private String bank_name;
        private String card_no_end;

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getCard_no_end() {
            return card_no_end;
        }

        public void setCard_no_end(String card_no_end) {
            this.card_no_end = card_no_end;
        }
    }

    public static class CreditInfoBean implements Serializable {
        private String card_amount;

        private String card_unused_amount;
        /**
         * 风控审核是否通过
         */
        private String risk_status;
        private String amountStatus;
        private String card_used_amount;
        public String getCard_amount() {
            return card_amount;
        }

        public void setCard_amount(String card_amount) {
            this.card_amount = card_amount;
        }

        public String getCard_unused_amount() {
            return card_unused_amount;
        }

        public void setCard_unused_amount(String card_unused_amount) {
            this.card_unused_amount = card_unused_amount;
        }

        public String getRisk_status() {
            return risk_status;
        }

        public void setRisk_status(String risk_status) {
            this.risk_status = risk_status;
        }

        public String getAmountStatus() {
            return amountStatus;
        }

        public void setAmountStatus(String amountStatus) {
            this.amountStatus = amountStatus;
        }

        public String getCard_used_amount() {
            return card_used_amount;
        }

        public void setCard_used_amount(String card_used_amount) {
            this.card_used_amount = card_used_amount;
        }
    }

    public static class VerifyInfoBean implements Serializable {

        private String real_bind_bank_card_status;
        private String real_pay_pwd_status;
        private String real_verify_status;

        public String getReal_bind_bank_card_status() {
            return real_bind_bank_card_status;
        }

        public void setReal_bind_bank_card_status(String real_bind_bank_card_status) {
            this.real_bind_bank_card_status = real_bind_bank_card_status;
        }

        public String getReal_pay_pwd_status() {
            return real_pay_pwd_status;
        }

        public void setReal_pay_pwd_status(String real_pay_pwd_status) {
            this.real_pay_pwd_status = real_pay_pwd_status;
        }

        public String getReal_verify_status() {
            return real_verify_status;
        }

        public void setReal_verify_status(String real_verify_status) {
            this.real_verify_status = real_verify_status;
        }
    }

    public MoreContentBean() {
    }

    public static class ServiceBean implements Serializable{
        /**
         * holiday : 9:30~18:30
         * peacetime : 9:30~18:30
         * services_qq : ["3356190848","3539967368","3524605396"]
         * qq_group : 414383376
         * service_phone : 021-56350010
         */

        private String holiday;
        private String peacetime;
        private String qq_group;
        private String service_phone;
        private List<String> services_qq;

        public String getHoliday() {
            return holiday;
        }

        public void setHoliday(String holiday) {
            this.holiday = holiday;
        }

        public String getPeacetime() {
            return peacetime;
        }

        public void setPeacetime(String peacetime) {
            this.peacetime = peacetime;
        }

        public String getQq_group() {
            return qq_group;
        }

        public void setQq_group(String qq_group) {
            this.qq_group = qq_group;
        }

        public String getService_phone() {
            return service_phone;
        }

        public void setService_phone(String service_phone) {
            this.service_phone = service_phone;
        }

        public List<String> getServices_qq() {
            return services_qq;
        }

        public void setServices_qq(List<String> services_qq) {
            this.services_qq = services_qq;
        }
    }

    public static class StatusMap implements Serializable{
        /** 芝麻信用认证状态  1:未认证；2:已认证 */
        private String zmstatus;
        /** 是否计算额度  1:未计算；2:已计算*/
        private String newflag;
        /** 额度是否为0  0:额度为零；1:额度不为零 */
        private String limit;
        /** 是否存在借款  0:没有借款；1:存在借款 */
        private String hasOrder;
        /**
         * 订单审核状态：0:待初审(待机审);-3:初审驳回;1:初审通过;-4:复审驳回;20:复审通过,待放款;-5:放款驳回;
         * 22:放款中;-10:放款失败;21已放款，还款中;23:部分还款;30:已还款;-11:已逾期;-20:已坏账，34逾期已还款；
         */
        private String orderStatus;
        /** 认证提交状态,0:未提交；1:已提交 */
        private int commitstatus;

        public String getZmstatus() {
            return zmstatus;
        }

        public void setZmstatus(String zmstatus) {
            this.zmstatus = zmstatus;
        }

        public String getNewflag() {
            return newflag;
        }

        public void setNewflag(String newflag) {
            this.newflag = newflag;
        }

        public String getLimit() {
            return limit;
        }

        public void setLimit(String limit) {
            this.limit = limit;
        }

        public String getHasOrder() {
            return hasOrder;
        }

        public void setHasOrder(String hasOrder) {
            this.hasOrder = hasOrder;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getCommitstatus() {
            return commitstatus;
        }

        public void setCommitstatus(int commitstatus) {
            this.commitstatus = commitstatus;
        }
    }

    public StatusMap getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(StatusMap statusMap) {
        this.statusMap = statusMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.card_info);
        dest.writeString(this.card_url);
        dest.writeSerializable(this.credit_info);
        dest.writeString(this.invite_code);
        dest.writeString(this.phone);
        dest.writeString(this.share_body);
        dest.writeString(this.share_logo);
        dest.writeString(this.share_title);
        dest.writeString(this.share_url);
        dest.writeString(this.userId);
        dest.writeSerializable(this.verify_info);
        dest.writeSerializable(this.service);
        dest.writeSerializable(this.statusMap);
        dest.writeString(this.next_loan_day);
        dest.writeString(this.member_state);
        dest.writeString(this.expire_time);
    }

    protected MoreContentBean(Parcel in) {
        this.card_info = (CardInfos) in.readSerializable();
        this.card_url = in.readString();
        this.credit_info = (CreditInfoBean) in.readSerializable();
        this.invite_code = in.readString();
        this.phone = in.readString();
        this.share_body = in.readString();
        this.share_logo = in.readString();
        this.share_title = in.readString();
        this.share_url = in.readString();
        this.userId = in.readString();
        this.verify_info = (VerifyInfoBean) in.readSerializable();
        this.service = (ServiceBean) in.readSerializable();
        this.statusMap = (StatusMap) in.readSerializable();
        this.next_loan_day = in.readString();
        this.member_state = in.readString();
        this.expire_time = in.readString();
    }

    public static final Creator<MoreContentBean> CREATOR = new Creator<MoreContentBean>() {
        @Override
        public MoreContentBean createFromParcel(Parcel source) {
            return new MoreContentBean(source);
        }

        @Override
        public MoreContentBean[] newArray(int size) {
            return new MoreContentBean[size];
        }
    };
}
