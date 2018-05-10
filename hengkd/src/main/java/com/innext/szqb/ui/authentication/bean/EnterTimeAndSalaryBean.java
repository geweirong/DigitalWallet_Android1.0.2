package com.innext.szqb.ui.authentication.bean;

/**
 * 作者：${hengxinyongli} on 2017/2/15 0015 18:07
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class EnterTimeAndSalaryBean {
    private String name;
    private String entry_time_type;//入司时长id
    private String salary_type;//月薪id
    private String degrees;//学历id
    private String live_time_type;//居住时长id
    private String marriage;//婚姻状态id

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEntry_time_type() {
        return entry_time_type;
    }
    public void setEntry_time_type(String entry_time_type) {
        this.entry_time_type = entry_time_type;
    }
    public String getSalary_type() {
        return salary_type;
    }
    public void setSalary_type(String salary_type) {
        this.salary_type = salary_type;
    }
    public String getDegrees() {
        return degrees;
    }
    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }
    public String getLive_time_type() {
        return live_time_type;
    }
    public void setLive_time_type(String live_time_type) {
        this.live_time_type = live_time_type;
    }
    public String getMarriage() {
        return marriage;
    }
    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

}
