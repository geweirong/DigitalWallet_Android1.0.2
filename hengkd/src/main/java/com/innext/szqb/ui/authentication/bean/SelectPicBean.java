package com.innext.szqb.ui.authentication.bean;


public class SelectPicBean {

	public final static int Type_None = 0;// 显示图片
	public final static int Type_Add = 1;// 显示添加
	public final static int Type_TakePhoto = 2;// 显示拍照
	public final static int Type_Uploaded = 3;//上传完成
	public final static int Type_Uploading = 4;//上传中
	public final static int Type_UploadFailed = 5;//上传失败

	private String url;
	private int type;
	private String pic_name;
	private String id;


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public String getPic_name() {
		return pic_name;
	}

	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
