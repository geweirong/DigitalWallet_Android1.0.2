package com.innext.szqb.events;

public class RefreshUIEvent extends UIBaseEvent {

	private int type =  EVENT_DEFAULT;
	public RefreshUIEvent(int type)
	{
		this.type = type;
	}
	
	public RefreshUIEvent(int type, String code, String message)
	{
		super(code,message);
		this.type = type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public int getType()
	{
		return type;
	}
}

