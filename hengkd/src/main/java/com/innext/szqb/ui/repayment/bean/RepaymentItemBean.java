package com.innext.szqb.ui.repayment.bean;

import java.util.List;

public class RepaymentItemBean {
    private String old_path;
    private List<RepaymentListBean> list;

    private String count;

    private String pay_title;
    /** 服务费 */
    private String loanInterests;

    private List<PayType> pay_type;

    /** 合同图片地址 */
    private String contractImg;

    public static class PayType{

        /**
         * type : 1
         * title : 主动还款(银行卡)
         * img_url : http://192.168.39.214/kdkj/credit/web/image/card/union_pay.png
         * link_url : http://api.koudailc.com/page/detail?id=629
         */

        private int type;
        private String title;
        private String img_url;
        private String link_url;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }
    }

    public String getPay_title() {
        return pay_title;
    }

    public void setPay_title(String pay_title) {
        this.pay_title = pay_title;
    }

    public List<PayType> getPay_type() {
        return pay_type;
    }

    public void setPay_type(List<PayType> pay_type) {
        this.pay_type = pay_type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public List<RepaymentListBean> getList() {
        return list;
    }

    public void setList(List<RepaymentListBean> list) {
        this.list = list;
    }

    public String getOld_path() {
        return old_path;
    }

    public void setOld_path(String old_path) {
        this.old_path = old_path;
    }

    public String getLoanInterests() {
        return loanInterests;
    }

    public void setLoanInterests(String loanInterests) {
        this.loanInterests = loanInterests;
    }

    public String getContractImg() {
        return contractImg;
    }

    public void setContractImg(String contractImg) {
        this.contractImg = contractImg;
    }
}
