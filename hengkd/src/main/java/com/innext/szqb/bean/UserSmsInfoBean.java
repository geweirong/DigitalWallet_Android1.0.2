package com.innext.szqb.bean;

/**
 * 作者：hengxinyongli on 2016/9/23 15:46
 *
 * 短信内容
 */
public class UserSmsInfoBean {

    private String messageContent;

    private String messageDate;

    private String phone;

    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
