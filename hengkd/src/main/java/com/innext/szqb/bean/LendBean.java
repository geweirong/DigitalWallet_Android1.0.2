package com.innext.szqb.bean;

import java.io.Serializable;

public class LendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -341990396214204213L;

	
	private String money;
	private String period;
	private String card_id;
	private int type;
	private String pay_password;
	
	public String getPay_password() {
		return pay_password;
	}
	public void setPay_password(String pay_password) {
		this.pay_password = pay_password;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
