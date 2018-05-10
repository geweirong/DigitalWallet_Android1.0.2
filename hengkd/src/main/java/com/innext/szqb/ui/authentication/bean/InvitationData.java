package com.innext.szqb.ui.authentication.bean;

/**
 * Created by HX0010637 on 2018/1/30.
 */

public class InvitationData {
private String gender;  //":2,
private String invate_img;  //":"http://f.hengkuaidai.com//invite/img/QR_files/48ba7929206775b7eac452b25ba8ad50.jpg",
private String invite_code;  //":"MTI5Mzkw"

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInvate_img() {
        return invate_img;
    }

    public void setInvate_img(String invate_img) {
        this.invate_img = invate_img;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }
}
