package com.tospur.is.service;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tospur.is.dao.OAUserDao;
import com.tospur.is.dao.SysUserMapper;

@Service
public class OAUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OAUserService.class);
	
	@Resource
	private OAUserDao oaUserDao;
	
	public Map<String, String> getUserInfoById(String workNo){
		return oaUserDao.getUserInfoById(workNo);
		
	}
}
