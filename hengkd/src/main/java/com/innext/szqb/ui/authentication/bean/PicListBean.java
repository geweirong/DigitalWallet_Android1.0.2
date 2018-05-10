package com.innext.szqb.ui.authentication.bean;

import java.util.List;

public class PicListBean {

	private String type;
	private String title;
	private String notice;
	private int max_pictures;
	private List<SelectPicBean> data;
	
	public int getMax_pictures() {
		return max_pictures;
	}
	public void setMax_pictures(int max_pictures) {
		this.max_pictures = max_pictures;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	public List<SelectPicBean> getData() {
		return data;
	}
	public void setData(List<SelectPicBean> data) {
		this.data = data;
	}
}
