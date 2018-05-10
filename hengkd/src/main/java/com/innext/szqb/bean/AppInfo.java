package com.innext.szqb.bean;

/**
 * 作者：hengxinyongli on 2016/9/22 19:51
 */
public class AppInfo {
    private String appName;
    private String packageName;
    private String versionName;
    private int versionCode;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public AppInfo(){

    }
    public AppInfo(String packageName, String versionName, int versionCode, String appName, int userId) {
        this.appName = appName;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.userId=userId;
    }



    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
