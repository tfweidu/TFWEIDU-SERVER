package com.chujian.javabean;

public class UserFriendOutlineYesNoBean {

	private String userPhone;
	private String friendPhone;
	private String flag;// 0标示同意添加好友1标示不同意添加好友

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getFriendPhone() {
		return friendPhone;
	}

	public void setFriendPhone(String friendPhone) {
		this.friendPhone = friendPhone;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public UserFriendOutlineYesNoBean() {
	}

}
