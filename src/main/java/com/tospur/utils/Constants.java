package com.tospur.utils;


/**
 * Created by 32070 on 2015/10/20.
 */
public class Constants {


    public static final String USER_SESSION_NAME = "user";
    /**
     * 系统属性文件
     */
    public static final String TOSPUR_PRO = "/environment/application.properties";
    /**
     * 接口定义字符串
     */
    public static String SIGN = null;
    /**
     * 附件服务器附件虚拟目录
     */
    public static String FILEPATH = null;
    /**
     * ad域ip
     */
    public static String ADIP = null;
    /**
     * 服务器IP
     */
    public static String SERVER_IP = null;


    static {
        SERVER_IP = PropertiesTools.getFileIO("SERVER_IP", TOSPUR_PRO);
    }
}
