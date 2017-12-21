package com.tospur.is.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 登录信息<p/>
 * 登录信息
 * @author XXZ
  */
@ApiModel(value="LoginInfo", description="登录信息")
public class LoginInfo {
	
	// 用户名，目前是工号
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "用户名称(登录名称)", required = true)	 	
	private String userName;
	
    @JsonProperty(required = true)
    @ApiModelProperty(notes = "密码", required = true)	     
	private String password;
	
    @JsonProperty(required = false)
    @ApiModelProperty(notes = "验证码", required = false)    
	private String captcha;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}		
	
}
