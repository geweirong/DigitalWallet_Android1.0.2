package com.innext.szqb.events;


import com.innext.szqb.bean.UpdateBean;

public class FragmentRefreshEvent{
	private int type =  UIBaseEvent.EVENT_DEFAULT;
	private String code;
	private UpdateBean updateBean;
	
	public FragmentRefreshEvent() {
		super();
	}

	public FragmentRefreshEvent(UpdateBean updateBean) {
		super();
		this.updateBean = updateBean;
	}

	public FragmentRefreshEvent(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setType(int type) {
		this.type = type;
	}

	public UpdateBean getUpdateBean() {
		return updateBean;
	}

	public void setUpdateBean(UpdateBean updateBean) {
		this.updateBean = updateBean;
	}
	
	
}
