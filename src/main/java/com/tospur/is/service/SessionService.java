package com.tospur.is.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tospur.utils.RedisService;

@Service
public class SessionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);
	
	@Resource
	private RedisService redisService;
	
	public String GenAndSaveToken(String workNo) {
		String s = UUID.randomUUID().toString();
		String token = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24)+"#"+workNo; 
		try {
			redisService.set(token, workNo, 3600*24*60);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
		return token;
	}
	
	public boolean isTokenValid(String token) {
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		return redisService.exists(token);
	}
	
	public void resetToken() {
		
	}
	
	public String getWorkNo(String token) {
		return token.substring(token.indexOf("#")+1);
//		return redisService.get(token);
	}
	/**
	 * 根据用户创建 TOKEN
	 * @param user 用户
	 * @return TOKEN
	 */
//	public synchronized String generateToken(SysUser user) {
//		Assert.notNull(user);
//		final String format = TIME_FORMAT.format(System.currentTimeMillis());
//		// 工号
//		final String account = user.getUserAccount();
//		final String string = account + "#" + format;
//		final String token = string; // DigestUtils.md5Hex(string);
//		tokenMap.put(token, StringUtils.EMPTY);
//		userId2TokenMap.put(account, token);
//		// 生成 token
//		return token;
//	}

	public synchronized void destroyToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			redisService.del(token);
		}
	}
	
	/**
	 * 从 TOKEN 获取用户工号
	 * 
	 * @param token
	 *            令牌
	 * @return 用户工号
	 */
//	public String getWorkNo(String token) {
//		final String workNo = token.split("#")[0];
//		return workNo;
//	}
	
	/**
	 * 根据用户创建 TOKEN
	 * @param user 用户
	 * @return TOKEN
	 */
//	public synchronized String generateTokenByUser(BaseUser user) {
//		Assert.notNull(user);
//		final String format = TIME_FORMAT.format(System.currentTimeMillis());
//		// 工号
//		final String account = user.getWorkNo();
//		final String string = account + "#" + format;
//		final String token = string; // DigestUtils.md5Hex(string);
//		tokenMap.put(token, StringUtils.EMPTY);
//		userId2TokenMap.put(account, token);
//		// 生成 token
//		return token;
//	}
}
