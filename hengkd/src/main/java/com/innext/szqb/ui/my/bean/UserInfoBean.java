package com.innext.szqb.ui.my.bean;

//用户信息
public class UserInfoBean {
	/**
	 * uid : 422
	 * username : 13120678525
	 * token : 3d17a21c0811f5b47d2fc019c2205ddb29743944111c0fbeb5c10034e1ac0ba5
	 * realname : 谢井文
	 * sessionid : 89D76DB739E5400F640E164A6D2BAD20
	 */

	private int uid;
	private String username;
	private String token;
	private String realname;
	private String sessionid;
	private String member_state;	//会员标识
	private String expire_time;		//会员到期日期

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getMember_state() {
		return member_state;
	}

	public void setMember_state(String member_state) {
		this.member_state = member_state;
	}

	public String getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
}
