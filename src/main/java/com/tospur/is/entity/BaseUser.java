package com.tospur.is.entity;


import io.swagger.annotations.ApiModelProperty;

public class BaseUser{
	
	

	/**用户ID**/
	@ApiModelProperty(notes = "工号，唯一标识")
    private String workNo;	

	@ApiModelProperty(notes = "用户名")
    private String userName;

	@ApiModelProperty(notes = "性别，0男1女")
    private String gender;
	@ApiModelProperty(notes = "手机")
    private String phone;
	@ApiModelProperty(notes = "电话")
    private String telphone;

	@ApiModelProperty(notes = "职位")
    private String positions;
	
	@ApiModelProperty(notes = "头像")
    private String headPortrait;
	
	@ApiModelProperty(notes = "区域ID")
    private String regionId;
	
	@ApiModelProperty(notes = "子公司ID")
    private String subcompanyId;
	
	@ApiModelProperty(notes = "处ID")
    private String branchId;
	
	@ApiModelProperty(notes = "邮箱")
    private String email;
	
	@ApiModelProperty(notes = "是否有效")
    private String validate;

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getSubcompanyId() {
		return subcompanyId;
	}

	public void setSubcompanyId(String subcompanyId) {
		this.subcompanyId = subcompanyId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}
	
}