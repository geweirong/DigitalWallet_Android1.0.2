package com.innext.szqb.ui.authentication.bean;

import java.util.List;

/**
 * 作者：${hengxinyongli} on 2017/2/17 0017 14:23
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class FaceResultBean {

    private String result;
    private int resultcode;
    private List<String> imgs;

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public int getResultcode()
    {
        return resultcode;
    }

    public void setResultcode(int resultcode)
    {
        this.resultcode = resultcode;
    }

    public List<String> getImgs()
    {
        return imgs;
    }

    public void setImgs(List<String> imgs)
    {
        this.imgs = imgs;
    }


}
