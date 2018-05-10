package com.innext.szqb.ui.authentication.bean;

/**
 * 作者：${hengxinyongli} on 2017/2/17 0017 14:31
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class IdCardResultBean {
    private String result;
    private int side;
    private String idcardImg;
    private String portraitImg;

    public String getPortraitImg()
    {
        return portraitImg;
    }

    public void setPortraitImg(String portraitImg)
    {
        this.portraitImg = portraitImg;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public int getSide()
    {
        return side;
    }

    public void setSide(int side)
    {
        this.side = side;
    }

    public String getIdcardImg()
    {
        return idcardImg;
    }

    public void setIdcardImg(String idcardImg)
    {
        this.idcardImg = idcardImg;
    }

}
