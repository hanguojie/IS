package com.tospur.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GlobalParameter {

	/**
	 * 静态文件服务器IP地址
	 */
	public static String ip;
	
	/**
	 * ad域服务器IP地址
	 */
	public static String adIp;
	
	/**
	 * ad域服务器接口验证串
	 */
	public static String adSign;
 
	//配置文件中读取ip地址信息
	static{
		Properties props = new Properties();
	    InputStream in = GlobalParameter.class.getResourceAsStream("/environment/application.properties");
	    try {
			props.load(in);
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ip = props.getProperty("server.ip");
		adIp = props.getProperty("ad.server.ip");
		adSign = props.getProperty("ad.sign");
	}
	
	/**
	 * 自己跟进
	 */
	public static final int FOLLOW_STATUS_0 =0;
	
	/**
	 * 下属跟进
	 */
	public static final int FOLLOW_STATUS_1 =1;
	
	/**
	 * 他人跟进
	 */
	public static final int FOLLOW_STATUS_2 =2;
	
	/**
	 * 无人跟进
	 */
	public static final int FOLLOW_STATUS_3 =3;
	
	/**
	 * 未读
	 */
	public static final int NO_READER =0;
	
	/**
	 * 是否有权限
	 */
	public static final int PERMISSION_IS_FLAG =0;
	
	public static final int PERMISSION_NO_FLAG =1;
}
