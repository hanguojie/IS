package com.tospur.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA. User: tao Date: 14-4-25 Time: 下午2:36 To change
 * this template use File | Settings | File Templates.
 * <p>
 * 属性文件读取公共类
 */
public class PropertiesTools {

    /**
     * 读取属性文件值
     *
     * @param name    key
     * @param fileURL 文件url
     * @return
     */
    public static String getFileIO(String name, String fileURL) {
        Properties prop = new Properties();
        InputStream in = PropertiesTools.class.getResourceAsStream(fileURL);
        try {
            prop.load(in);
            return prop.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 写入属性文件值
     *
     * @param key     key值
     * @param value   value值
     * @param fileURL 文件路径
     */
    public static void writeData(String key, String value, String fileURL) {
        Properties prop = new Properties();
        InputStream fis = null;
        OutputStream fos = null;
        try {
            URL url = PropertiesTools.class.getResource(fileURL);
            File file = new File(url.toURI());
            if (!file.exists())
                file.createNewFile();
            fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();//一定要在修改值之前关闭fis
            fos = new FileOutputStream(file);
            prop.setProperty(key, value);
            prop.store(fos, "Update '" + key + "' value");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}