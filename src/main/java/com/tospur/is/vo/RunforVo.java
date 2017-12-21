package com.tospur.is.vo;

import java.io.Serializable;


import io.swagger.annotations.ApiModelProperty;

public class RunforVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	private String id;
	
	/**
	 * 工号
	 */
	private String workNo;
	
	/**
	 * 姓名
	 */
	private String name;
	
	
	
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
	 * 报名类型1.自荐，2.他荐
	 */
	@ApiModelProperty(notes = "报名类型1.自荐，2.他荐")
	private String enrollType;
	
	/**
	 * 竞选类型：1.同心，2.同德，3.同策，4.同力
	 */
	@ApiModelProperty(notes = "竞选类型：1.同心，2.同德，3.同策，4.同力")
	private String type;
	
	/**
	 * 竞选理由
	 */
	@ApiModelProperty(notes = "竞选理由")
	private String enrollInfo;
	
	/**
	 * 头像图片地址
	 */
	@ApiModelProperty(notes = "头像图片地址")
	private String imgIcon;
	
	@ApiModelProperty(notes = "活动类型1.TOP40,2.TOP4")
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getEnrollType() {
		return enrollType;
	}

	public void setEnrollType(String enrollType) {
		this.enrollType = enrollType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEnrollInfo() {
		return enrollInfo;
	}

	public void setEnrollInfo(String enrollInfo) {
		this.enrollInfo = enrollInfo;
	}

	public String getImgIcon() {
		return imgIcon;
	}

	public void setImgIcon(String imgIcon) {
		this.imgIcon = imgIcon;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    


}
