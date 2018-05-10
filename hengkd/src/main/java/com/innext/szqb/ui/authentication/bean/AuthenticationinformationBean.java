package com.innext.szqb.ui.authentication.bean;

/**
 * 作者：${hengxinyongli} on 2017/2/14 0014 16:25
 * <p>
 * 邮箱：3244345578@qq.com
 *
 *  认证信息
 *
 */
public class AuthenticationinformationBean {
    private int type = 0;
    private String title;
    private String title_mark;
    private String subtitle;
    private int tag;
    private String operator;
    private String logo;
    private String url;
    private int status;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle_mark()
    {
        return title_mark;
    }

    public void setTitle_mark(String title_mark)
    {
        this.title_mark = title_mark;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public void setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
    }

    public int getTag()
    {
        return tag;
    }

    public void setTag(int tag)
    {
        this.tag = tag;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
