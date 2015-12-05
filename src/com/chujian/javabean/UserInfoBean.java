package com.chujian.javabean;

import java.io.Serializable;

public class UserInfoBean implements Serializable {

	private Integer id;
	private String userPhone;
	private String userPwd;
	private String userNickname;
	private String userSex;
	private String userBirthday;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserNickname() {

		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserSex() {

		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserBirthday() {

		return userBirthday;
	}

	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}

	public UserInfoBean(String userPhone, String userPwd, String userNickname,
			String userSex, String userBirthday) {
		this.userPhone = userPhone;
		this.userPwd = userPwd;
		this.userNickname = userNickname;
		this.userSex = userSex;
		this.userBirthday = userBirthday;
	}

	public UserInfoBean() {
	}

	@Override
	public String toString() {
		return "UserInfoBean [id=" + id + ", userPhone=" + userPhone
				+ ", userPwd=" + userPwd + ", userNickname=" + userNickname
				+ ", userSex=" + userSex + ", userBirthday=" + userBirthday
				+ ", userImage=" + "]";
	}

}
