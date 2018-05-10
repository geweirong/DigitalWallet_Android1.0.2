package com.innext.szqb.events;

public class PayLeanResultEvent extends RefreshUIEvent {
	private int errCode;
	private String errMessage;

	public PayLeanResultEvent(int type, int errCode, String errMessage, String code, String message) {
		super(type, code, message);
		this.errCode = errCode;
		this.errMessage = errMessage;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	
}
