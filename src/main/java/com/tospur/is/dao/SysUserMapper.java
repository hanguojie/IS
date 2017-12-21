package com.tospur.is.dao;


import com.tospur.is.entity.BaseUser;

public interface SysUserMapper {

	BaseUser getUserByWorkNo(String account);

}
