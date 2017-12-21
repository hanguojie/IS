package com.tospur.is.vo;

import java.io.Serializable;


import io.swagger.annotations.ApiModelProperty;

public class LoginResultUserVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	private String userId;
	
	/**
	 * 姓名
	 */
	private String userName;
	
	
	/**
	 * 手机号
	 */
	private String phone;
	
	/**
	 * token安全令牌
	 */
	@ApiModelProperty(notes = "token安全令牌")
	private String token;
	
	/**
	 * 部门
	 */
	@ApiModelProperty(notes = "部门")
	private String department;
	
	/**
	 * 邮箱
	 */
	@ApiModelProperty(notes = "邮箱")
	private String email;
    
	/**
	 * 用户类型
	 */
	@ApiModelProperty(notes = "用户类型：user.客户，admin.处理人")
	private String userType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}


}
